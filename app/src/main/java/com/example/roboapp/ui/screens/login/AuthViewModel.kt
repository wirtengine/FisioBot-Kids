package com.example.roboapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboapp.data.model.RoboUser
import com.example.roboapp.data.model.RegisterUserType   // ✅ Importación del enum compartido
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
    // LOGIN EMAIL/PASSWORD
    // ------------------------------------------------
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------------------------------------------
    // REGISTER EMAIL/PASSWORD
    // ------------------------------------------------
    fun registerWithEmail(
        email: String,
        password: String,
        username: String,
        age: Int,
        role: RegisterUserType,
        onSuccess: () -> Unit
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

                db.collection("users")
                    .document(uid)
                    .set(roboUser)
                    .await()

                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------------------------------------------
    // LOGIN CON GOOGLE
    // ------------------------------------------------
    fun firebaseAuthWithGoogle(
        idToken: String,
        onExistingUser: (RegisterUserType) -> Unit,
        onNewUser: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val user = result.user ?: return@launch

                val doc = db.collection("users")
                    .document(user.uid)
                    .get()
                    .await()

                if (doc.exists()) {
                    val roleString = doc.getString("role") ?: "child"
                    val role = RegisterUserType.valueOf(roleString.uppercase())
                    onExistingUser(role)
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
    // GUARDAR USUARIO GOOGLE (PRIMERA VEZ)
    // ------------------------------------------------
    fun saveGoogleUser(
        role: RegisterUserType,
        username: String,
        age: Int,
        onSuccess: () -> Unit
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

                db.collection("users")
                    .document(user.uid)
                    .set(roboUser)
                    .await()

                onSuccess()
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