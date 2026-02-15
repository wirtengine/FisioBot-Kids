package com.example.roboapp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roboapp.R
import kotlinx.coroutines.launch

// --------------------------------------------------------
// ENUM COMPLETO
// --------------------------------------------------------
enum class RegisterUserType(val emoji: String, val displayName: String) {
    CHILD("🧒", "Niño"),
    THERAPIST("👨‍⚕️", "Fisioterapeuta")
}

// --------------------------------------------------------
// COMPOSABLE PRINCIPAL
// --------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegister: (RegisterUserType) -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf(RegisterUserType.CHILD) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Observar estados del ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Snackbar host para mostrar errores
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar error si existe
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // Validaciones
    val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 8
    val isConfirmPasswordValid = password == confirmPassword
    val isFormValid = username.isNotBlank() && isEmailValid && isPasswordValid &&
            isConfirmPasswordValid && termsAccepted

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crear cuenta", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.register),
                contentDescription = "Registro RoboApp",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
                contentScale = ContentScale.Crop
            )

            Text("Únete a RoboApp", style = MaterialTheme.typography.headlineSmall)
            Text("Comienza tu viaje en terapia asistida")

            Spacer(modifier = Modifier.height(16.dp))

            // CARD PRINCIPAL
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // -------------------------
                    // Selector Niño / Terapista
                    // -------------------------
                    Text(
                        "¿Quién se registra?",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RegisterUserType.values().forEach { type ->
                            val isSelected = userType == type

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                onClick = { userType = type }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(type.emoji, style = MaterialTheme.typography.headlineMedium)
                                    Text(type.displayName)
                                }
                            }
                        }
                    }

                    // -------------------------
                    // CAMPOS DEL FORMULARIO
                    // -------------------------
                    FormField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Nombre de usuario",
                        icon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    FormField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Correo electrónico",
                        icon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    PasswordField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        isVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                    )

                    PasswordField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirmar contraseña",
                        isVisible = confirmPasswordVisible,
                        onVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // -------------------------
                    // TÉRMINOS Y CONDICIONES
                    // -------------------------
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = { termsAccepted = it }
                        )
                        Text("Acepto los términos y condiciones")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // -------------------------
                    // BOTÓN REGISTRAR
                    // -------------------------
                    Button(
                        onClick = {
                            viewModel.registerWithEmail(
                                email = email,
                                password = password,
                                username = username,
                                role = userType,
                                onSuccess = {
                                    onRegister(userType)
                                }
                            )
                        },
                        enabled = isFormValid && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Crear cuenta")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // -------------------------
                    // INFO SEGÚN USUARIO
                    // -------------------------
                    Text(
                        text = when (userType) {
                            RegisterUserType.CHILD -> "Registro para niños"
                            RegisterUserType.THERAPIST -> "Registro para terapeutas"
                        },
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = when (userType) {
                            RegisterUserType.CHILD -> "Los padres deben completar este formulario"
                            RegisterUserType.THERAPIST -> "Se requiere verificación profesional"
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("¿Ya tienes cuenta?")
                TextButton(onClick = onBack) { Text("Iniciar sesión") }
            }
        }
    }
}

// --------------------------------------------------------
// FORM FIELD
// --------------------------------------------------------
@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

// --------------------------------------------------------
// PASSWORD FIELD
// --------------------------------------------------------
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}