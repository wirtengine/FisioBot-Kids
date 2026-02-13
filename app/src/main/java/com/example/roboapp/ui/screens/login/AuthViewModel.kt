package com.example.roboapp.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboapp.data.model.RoboUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.roboapp.R

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: RoboUser? = null
)

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    private val _registerState = MutableStateFlow(AuthState())
    val registerState: StateFlow<AuthState> = _registerState

    private val _loginState = MutableStateFlow(AuthState())
    val loginState: StateFlow<AuthState> = _loginState

    private lateinit var googleSignInClient: GoogleSignInClient

    fun initGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

    // ========== REGISTRO CON EMAIL Y CONTRASEÑA ==========
    fun registerUser(email: String, password: String, username: String, userType: String) {
        viewModelScope.launch {
            _registerState.value = AuthState(isLoading = true)
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid ?: throw Exception("Error al crear usuario")

                val newUser = RoboUser(
                    uid = uid,
                    email = email,
                    username = username,
                    userType = userType
                )
                firestore.collection("users").document(uid).set(newUser).await()

                _registerState.value = AuthState(isSuccess = true, user = newUser)
            } catch (e: Exception) {
                _registerState.value = AuthState(errorMessage = e.message ?: "Error desconocido")
            }
        }
    }

    // ========== LOGIN CON EMAIL Y CONTRASEÑA (CON VALIDACIÓN DE TIPO) ==========
    fun loginWithEmail(email: String, password: String, expectedType: String) {
        viewModelScope.launch {
            _loginState.value = AuthState(isLoading = true)
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user ?: throw Exception("Usuario no encontrado")

                val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
                val user = userDoc.toObject(RoboUser::class.java)

                if (user == null) {
                    throw Exception("Usuario no registrado correctamente")
                }

                if (user.userType != expectedType) {
                    auth.signOut()
                    throw Exception("Esta cuenta no es de tipo ${expectedType.lowercase()}")
                }

                _loginState.value = AuthState(isSuccess = true, user = user)
            } catch (e: Exception) {
                _loginState.value = AuthState(errorMessage = e.message ?: "Error al iniciar sesión")
            }
        }
    }

    // ========== LOGIN CON GOOGLE (CON VALIDACIÓN DE TIPO) ==========
    fun signInWithGoogle(idToken: String, expectedType: String) {
        viewModelScope.launch {
            _loginState.value = AuthState(isLoading = true)
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user ?: throw Exception("Error al autenticar con Google")

                val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()

                if (userDoc.exists()) {
                    val user = userDoc.toObject(RoboUser::class.java)!!
                    if (user.userType != expectedType) {
                        auth.signOut()
                        googleSignInClient.signOut()
                        throw Exception("Esta cuenta de Google ya está registrada como ${user.userType.lowercase()} y no puede acceder como ${expectedType.lowercase()}")
                    }
                    _loginState.value = AuthState(isSuccess = true, user = user)
                } else {
                    auth.signOut()
                    googleSignInClient.signOut()
                    throw Exception("Usuario no registrado. Por favor, crea una cuenta primero.")
                }
            } catch (e: Exception) {
                _loginState.value = AuthState(errorMessage = e.message ?: "Error con Google Sign-In")
            }
        }
    }

    // ========== CERRAR SESIÓN ==========
    fun signOut() {
        auth.signOut()
        if (::googleSignInClient.isInitialized) {
            googleSignInClient.signOut()
        }
    }

    fun resetRegisterState() {
        _registerState.value = AuthState()
    }

    fun resetLoginState() {
        _loginState.value = AuthState()
    }
}