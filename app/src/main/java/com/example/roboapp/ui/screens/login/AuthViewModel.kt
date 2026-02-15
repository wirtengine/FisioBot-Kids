package com.example.roboapp.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboapp.data.model.RoboUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Registro con email/contraseña
    fun registerWithEmail(
        email: String,
        password: String,
        username: String,
        role: RegisterUserType,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    saveUserToFirestore(
                        uid = user.uid,
                        email = email,
                        username = username,
                        role = role,
                        onSuccess = onSuccess,
                        onError = { error ->
                            _errorMessage.value = error
                            user.delete()
                        }
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Inicio de sesión con email/contraseña
    fun loginWithEmail(email: String, password: String, onSuccess: (role: String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    val document = db.collection("users").document(user.uid).get().await()
                    val role = document.getString("role") ?: "child"
                    onSuccess(role)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Autenticación con Google
    fun firebaseAuthWithGoogle(
        idToken: String,
        onFirstTime: () -> Unit,
        onSuccess: (role: String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("GoogleFlow", "Iniciando firebaseAuthWithGoogle")
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val user = result.user
                Log.d("GoogleFlow", "Usuario autenticado: ${user?.email}")
                if (user != null) {
                    val document = db.collection("users").document(user.uid).get().await()
                    Log.d("GoogleFlow", "Documento existe? ${document.exists()}")
                    if (document.exists()) {
                        val role = document.getString("role") ?: "child"
                        Log.d("GoogleFlow", "Rol existente: $role, llamando onSuccess")
                        onSuccess(role)
                    } else {
                        Log.d("GoogleFlow", "Primera vez, llamando onFirstTime")
                        onFirstTime()
                    }
                }
            } catch (e: Exception) {
                Log.e("GoogleFlow", "Error: ${e.message}")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Guardar datos del usuario después de elegir rol (Google first time) con campos adicionales
    fun saveGoogleUserWithPassword(
        role: RegisterUserType,
        username: String,
        age: Int?,
        password: String?,
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
                    code = code
                )
                db.collection("users").document(user.uid).set(roboUser).await()

                // Si se proporcionó contraseña, actualizarla en Firebase Auth
                if (!password.isNullOrBlank()) {
                    user.updatePassword(password).await()
                }
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Guardar usuario en Firestore (método auxiliar)
    private suspend fun saveUserToFirestore(
        uid: String,
        email: String,
        username: String,
        role: RegisterUserType,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val code = generateUniqueCode()
            val roboUser = RoboUser(
                uid = uid,
                email = email,
                username = username,
                role = role.name.lowercase(),
                code = code
            )
            db.collection("users").document(uid).set(roboUser).await()
            onSuccess()
        } catch (e: Exception) {
            onError(e.message ?: "Error al guardar usuario")
        }
    }

    // Generar código único de 6 caracteres
    private suspend fun generateUniqueCode(): String {
        val charset = ('A'..'Z') + ('0'..'9')
        var code: String
        var exists: Boolean
        do {
            code = (1..6)
                .map { charset.random() }
                .joinToString("")
            val query = db.collection("users").whereEqualTo("code", code).get().await()
            exists = !query.isEmpty
        } while (exists)
        return code
    }

    fun clearError() {
        _errorMessage.value = null
    }
}