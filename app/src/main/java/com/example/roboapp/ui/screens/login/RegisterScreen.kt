package com.example.roboapp.ui.screens.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roboapp.R
import com.example.roboapp.data.model.RegisterUserType   // ✅ Importamos el enum compartido
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

// ❌ Eliminamos la definición local del enum

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (RegisterUserType) -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var userType by remember { mutableStateOf(RegisterUserType.THERAPIST) }
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    val webClientId = stringResource(id = R.string.default_web_client_id)

    val googleSignInClient = remember(webClientId) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { idToken ->
                    viewModel.firebaseAuthWithGoogle(
                        idToken = idToken,
                        onExistingUser = { existingRole ->
                            viewModel._errorMessage.value = "Esta cuenta ya está registrada. Inicia sesión."
                        },
                        onNewUser = {
                            onRegisterSuccess(userType)
                        }
                    )
                }
            } catch (e: ApiException) {
                Log.e("Register", "Google sign in failed", e)
                viewModel._errorMessage.value = "Error al iniciar sesión con Google"
            }
        }
    }

    val fadeIn by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(800),
        label = ""
    )

    val cardElevation by animateDpAsState(
        targetValue = 12.dp,
        animationSpec = tween(600),
        label = ""
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFF8FBFF)
                        )
                    )
                )
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(fadeIn),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(cardElevation),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.register),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Registro Profesional",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF1565C0)
                    )

                    Text(
                        text = "Plataforma clínica segura",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Selecciona el tipo de perfil",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1565C0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(
                                color = Color(0xFFE3F2FD),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        RegisterUserType.values().forEach { type ->
                            val isSelected = userType == type
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) Color.White else Color.Transparent,
                                onClick = { userType = type }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = type.displayName,
                                        color = if (isSelected) Color(0xFF1565C0) else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            googleLauncher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Registrarse con Google",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = when (userType) {
                            RegisterUserType.CHILD ->
                                "El registro debe ser realizado por el tutor legal."
                            RegisterUserType.THERAPIST ->
                                "Se realizará verificación profesional antes de activar el perfil."
                        },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextButton(onClick = onBack) {
                        Text("¿Ya tienes cuenta? Iniciar sesión")
                    }
                }
            }
        }
    }
}