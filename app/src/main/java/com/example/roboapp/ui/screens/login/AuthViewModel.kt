package com.example.roboapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboapp.data.model.RoboUser
import com.example.roboapp.data.model.RegisterUserType
import com.example.roboapp.data.model.UserStats  // Asegúrate de importar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // ------------------------------------------------
    // LOGIN EMAIL/PASSWORD (ahora devuelve rol y uid)
    // ------------------------------------------------
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (RegisterUserType, String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("Usuario sin UID")
                // Obtener el rol del usuario desde Firestore
                val doc = db.collection("users").document(uid).get().await()
                val roleString = doc.getString("role") ?: "child"
                val role = RegisterUserType.valueOf(roleString.uppercase())
                onSuccess(role, uid)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------------------------------------------
    // REGISTER EMAIL/PASSWORD (incluye uid y crea estadísticas)
    // ------------------------------------------------
    fun registerWithEmail(
        email: String,
        password: String,
        username: String,
        age: Int,
        role: RegisterUserType,
        onSuccess: (String) -> Unit  // Solo uid, el rol ya se conoce
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: return@launch
                val code = generateUniqueCode()

                val roboUser = RoboUser(
                    uid = uid,
                    email = email,
                    username = username,
                    age = age,
                    role = role.name.lowercase(),
                    code = code,
                    createdAt = Timestamp.now()
                )

                db.collection("users").document(uid).set(roboUser).await()

                // Crear estadísticas iniciales si es niño
                if (role == RegisterUserType.CHILD) {
                    val initialStats = UserStats()
                    db.collection("users").document(uid)
                        .collection("stats").document("current")
                        .set(initialStats)
                        .await()
                }

                onSuccess(uid)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------------------------------------------
    // LOGIN CON GOOGLE (ahora pasa rol y uid en onExistingUser)
    // ------------------------------------------------
    fun firebaseAuthWithGoogle(
        idToken: String,
        onExistingUser: (RegisterUserType, String) -> Unit,
        onNewUser: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val user = result.user ?: return@launch

                val doc = db.collection("users").document(user.uid).get().await()

                if (doc.exists()) {
                    val roleString = doc.getString("role") ?: "child"
                    val role = RegisterUserType.valueOf(roleString.uppercase())
                    onExistingUser(role, user.uid)
                } else {
                    onNewUser()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------------------------------------------
    // GUARDAR USUARIO GOOGLE (PRIMERA VEZ) - incluye uid en callback
    // ------------------------------------------------
    fun saveGoogleUser(
        role: RegisterUserType,
        username: String,
        age: Int,
        onSuccess: (String) -> Unit
    ) {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val code = generateUniqueCode()
                val roboUser = RoboUser(
                    uid = user.uid,
                    email = user.email ?: "",
                    username = username,
                    age = age,
                    role = role.name.lowercase(),
                    code = code,
                    createdAt = Timestamp.now()
                )

                db.collection("users").document(user.uid).set(roboUser).await()

                // Crear estadísticas para niño
                if (role == RegisterUserType.CHILD) {
                    val initialStats = UserStats()
                    db.collection("users").document(user.uid)
                        .collection("stats").document("current")
                        .set(initialStats)
                        .await()
                }

                onSuccess(user.uid)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------------------------------------------
    // LOGOUT
    // ------------------------------------------------
    fun logout() {
        auth.signOut()
    }

    // ------------------------------------------------
    // GENERAR CÓDIGO ÚNICO
    // ------------------------------------------------
    private fun generateUniqueCode(): String {
        return UUID.randomUUID().toString().take(6).uppercase()
    }

    // ------------------------------------------------
    // LIMPIAR ERROR
    // ------------------------------------------------
    fun clearError() {
        _errorMessage.value = null
    }
}