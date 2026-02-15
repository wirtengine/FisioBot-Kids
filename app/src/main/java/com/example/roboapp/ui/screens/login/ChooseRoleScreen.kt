package com.example.roboapp.ui.screens.login

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roboapp.data.model.RegisterUserType   // ✅ Importamos el enum compartido
import com.google.firebase.auth.FirebaseAuth

// ❌ Eliminamos la definición local del enum

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseRoleScreen(
    onRoleSelected: (RegisterUserType, String, Int) -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var selectedRole by remember { mutableStateOf(RegisterUserType.CHILD) }
    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val email = currentUser?.email ?: "correo@no.disponible"

    // Mostrar errores del ViewModel
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // Validación del formulario
    val ageInt = age.toIntOrNull()
    val isFormValid = username.isNotBlank() && ageInt != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("👤 Completa tu perfil") },
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
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(28.dp)) {
                    Text(
                        text = "Configura tu cuenta en RoboApp 🤖",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Correo electrónico (solo lectura)
                    OutlinedTextField(
                        value = email,
                        onValueChange = {},
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Selecciona tu rol",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector de rol con animación
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RegisterUserType.values().forEach { type ->
                            val isSelected = selectedRole == type
                            val animatedElevation by animateDpAsState(
                                targetValue = if (isSelected) 12.dp else 4.dp,
                                label = "elevation_${type.name}"
                            )
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1f,
                                label = "scale_${type.name}"
                            )

                            Card(
                                onClick = { selectedRole = type },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .scale(scale),
                                elevation = CardDefaults.cardElevation(animatedElevation),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = type.emoji,
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = type.displayName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nombre completo
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Edad (solo números)
                    OutlinedTextField(
                        value = age,
                        onValueChange = { newValue ->
                            age = newValue.filter { it.isDigit() }
                        },
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Botón finalizar
                    Button(
                        onClick = {
                            ageInt?.let { validAge ->
                                onRoleSelected(selectedRole, username, validAge)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid && !isLoading,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("🚀 Finalizar configuración")
                        }
                    }
                }
            }
        }
    }
}