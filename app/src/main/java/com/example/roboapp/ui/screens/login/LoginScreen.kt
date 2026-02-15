package com.example.roboapp.ui.screens.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import com.example.roboapp.data.model.RegisterUserType   // ✅ Único enum
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

// Mantenemos UserType para la UI (selector de perfil)
enum class UserType(val displayName: String) {
    CHILD("👶 Niño"),
    THERAPIST("👩‍⚕️ Terapeuta")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginChild: () -> Unit,
    onLoginTherapist: () -> Unit,
    onRegister: () -> Unit,
    onFirstTimeGoogle: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var userType by rememberSaveable { mutableStateOf(UserType.CHILD) }

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

    /* ---------------- Google Config ---------------- */

    val webClientId = stringResource(R.string.default_web_client_id)

    val googleSignInClient = remember {
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
                        onExistingUser = { role ->
                            // role es de tipo RegisterUserType (del paquete data.model)
                            when (role) {
                                RegisterUserType.CHILD -> onLoginChild()
                                RegisterUserType.THERAPIST -> onLoginTherapist()
                            }
                        },
                        onNewUser = {
                            onFirstTimeGoogle()
                        }
                    )
                }
            } catch (e: ApiException) {
                Log.e("Login", "Google Sign In Error", e)
            }
        }
    }

    /* ---------------- Animations ---------------- */

    val fadeIn by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(800)
    )

    val pulse by rememberInfiniteTransition().animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    /* ---------------- UI ---------------- */

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFF8FBFF)
                        )
                    )
                )
                .padding(padding)
                .padding(24.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(fadeIn)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_robo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .alpha(pulse),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "🤖 RoboApp",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF1565C0)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Terapia asistida por robots para potenciar el desarrollo infantil y facilitar el trabajo clínico.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Selecciona tu perfil",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1565C0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE3F2FD)),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        UserType.values().forEach { type ->
                            val isSelected = userType == type
                            val animatedColor by animateColorAsState(
                                targetValue = if (isSelected) Color.White else Color.Transparent,
                                label = ""
                            )

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                shape = RoundedCornerShape(14.dp),
                                color = animatedColor,
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
                                text = "Continuar con Google",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Acceso seguro y cifrado. Cumplimos estándares de confidencialidad clínica.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}