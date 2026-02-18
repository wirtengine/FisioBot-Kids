package com.example.roboapp.ui.screens.login.therapist

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import com.example.roboapp.ui.screens.login.therapist.TherapistViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.roboapp.R
import com.example.roboapp.data.model.RoboUser
import com.example.roboapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


// -------------------------------------------------
// ENTRY POINT
// -------------------------------------------------
@Composable
fun TherapistScreen(userId: String) {
    TherapistRootScreen(userId = userId)
}

// -------------------------------------------------
// ROOT SCREEN
// -------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapistRootScreen(userId: String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf(0) }

    // Crear ViewModel
    val repository = UserRepository()
    val viewModel = remember { TherapistViewModel(repository, userId) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                Text(
                    "Panel Terapeuta",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
                Divider()
                DrawerItem(
                    selected = selectedScreen == 0,
                    onClick = {
                        selectedScreen = 0
                        scope.launch { drawerState.close() }
                    },
                    icon = Icons.Default.Dashboard,
                    label = "Inicio"
                )
                DrawerItem(
                    selected = selectedScreen == 1,
                    onClick = {
                        selectedScreen = 1
                        scope.launch { drawerState.close() }
                    },
                    icon = Icons.Default.People,
                    label = "Pacientes"
                )
                DrawerItem(
                    selected = selectedScreen == 2,
                    onClick = {
                        selectedScreen = 2
                        scope.launch { drawerState.close() }
                    },
                    icon = Icons.Default.FitnessCenter,
                    label = "Ejercicios"
                )
                DrawerItem(
                    selected = selectedScreen == 3,
                    onClick = {
                        selectedScreen = 3
                        scope.launch { drawerState.close() }
                    },
                    icon = Icons.Default.Computer,
                    label = "Control Robot"
                )
                DrawerItem(
                    selected = selectedScreen == 4,
                    onClick = {
                        selectedScreen = 4
                        scope.launch { drawerState.close() }
                    },
                    icon = Icons.Default.BarChart,
                    label = "Informes"
                )
                DrawerItem(
                    selected = selectedScreen == 5,
                    onClick = {
                        selectedScreen = 5
                        scope.launch { drawerState.close() }
                    },
                    icon = Icons.Default.Info,
                    label = "Sobre el Proyecto"
                )
            }
        }
    ) {
        TherapistMainScaffold(
            drawerState = drawerState,
            selectedScreen = selectedScreen,
            viewModel = viewModel
        )
    }
}

// Helper composable para drawer items
@Composable
fun DrawerItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String
) {
    NavigationDrawerItem(
        label = { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFFE1F5FE),
            selectedIconColor = Color(0xFF0066A1),
            selectedTextColor = Color(0xFF0066A1)
        )
    )
}

// -------------------------------------------------
// MAIN SCAFFOLD
// -------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapistMainScaffold(
    drawerState: DrawerState,
    selectedScreen: Int,
    viewModel: TherapistViewModel
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "FisioBot - Terapeuta",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0066A1)
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                0 -> TherapistHomeScreen(viewModel = viewModel)
                1 -> TherapistPatientsScreen(viewModel = viewModel)
                2 -> TherapistExercisesScreen() // Mantenemos ejercicios simulados por ahora
                3 -> TherapistControlRobotScreen() // Control funcional
                4 -> TherapistReportsScreen() // Informes simulados
                5 -> TherapistAboutProjectScreen() // About simulado
            }
        }
    }
}

// -------------------------------------------------
// HOME SCREEN (DATOS REALES)
// -------------------------------------------------
@Composable
fun TherapistHomeScreen(viewModel: TherapistViewModel) {
    val therapist by viewModel.therapist.collectAsState()
    val patients by viewModel.patients.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            therapist?.let {
                TherapistInfoHeader(name = it.username, code = it.code)
            } ?: Text("Error al cargar datos del terapeuta")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjetas de resumen con datos reales (pacientes)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryCard(
                title = "Pacientes",
                value = patients.size.toString(),
                icon = Icons.Default.People,
                color = Color(0xFF4CAF50)
            )
            SummaryCard(
                title = "Sesiones hoy",
                value = "0", // Placeholder, puedes implementar después
                icon = Icons.Default.Today,
                color = Color(0xFF2196F3)
            )
            SummaryCard(
                title = "Progreso ⌀",
                value = "0%", // Placeholder
                icon = Icons.Default.TrendingUp,
                color = Color(0xFFFF9800)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de pacientes recientes (primeros 3)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Pacientes recientes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (patients.isEmpty()) {
                    Text("No hay pacientes asignados", color = Color.Gray)
                } else {
                    patients.take(3).forEach { patient ->
                        PatientSimpleItem(patient)
                        if (patient != patients.lastOrNull()) Divider()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alerta (puede ser real después)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Recuerda actualizar los perfiles de tus pacientes regularmente.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D4037)
                )
            }
        }
    }
}

@Composable
fun TherapistInfoHeader(name: String, code: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF0066A1)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Dr. $name", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Código único: $code", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                title,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PatientSimpleItem(patient: RoboUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE1F5FE)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                patient.username.take(1),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066A1)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(patient.username, fontWeight = FontWeight.Medium)
            Text("Edad: ${patient.age}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

// -------------------------------------------------
// PATIENTS SCREEN (LISTA REAL DE PACIENTES)
// -------------------------------------------------
@Composable
fun TherapistPatientsScreen(viewModel: TherapistViewModel) {
    val patients by viewModel.patients.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "Mis Pacientes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066A1)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (patients.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes pacientes asignados")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(patients) { patient ->
                    PatientDetailCard(patient)
                }
            }
        }
    }
}

@Composable
fun PatientDetailCard(patient: RoboUser) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con inicial
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE1F5FE))
                    .border(1.dp, Color(0xFF0066A1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    patient.username.take(1),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(patient.username, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "Edad: ${patient.age} · Código: ${patient.code}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                // Puedes agregar más info como último progreso si lo tienes
            }

            IconButton(onClick = { /* Ver detalle */ }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Ver", tint = Color(0xFF0066A1))
            }
        }
    }
}

// -------------------------------------------------
// EXERCISES SCREEN (SIMULADA, IGUAL QUE ANTES)
// -------------------------------------------------
@Composable
fun TherapistExercisesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "Biblioteca de Ejercicios",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066A1)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Arrastra o asigna a pacientes",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(exerciseList) { exercise ->
                ExerciseLibraryCard(exercise)
            }
        }
    }
}

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val duration: String,
    val difficulty: String,
    val icon: ImageVector,
    val color: Color
)

val exerciseList = listOf(
    Exercise(1, "Marcha asistida", "Seguir al robot mientras camina", "10 min", "Básico", Icons.Default.DirectionsWalk, Color(0xFF4CAF50)),
    Exercise(2, "Alcance de objetos", "Tomar pelota del robot", "8 min", "Intermedio", Icons.Default.SportsHandball, Color(0xFF2196F3)),
    Exercise(3, "Coordinación", "Esquivar obstáculos", "12 min", "Avanzado", Icons.Default.Games, Color(0xFFFF9800)),
    Exercise(4, "Equilibrio", "Mantenerse sobre una línea", "6 min", "Básico", Icons.Default.AccountBalance, Color(0xFF9C27B0)),
    Exercise(5, "Estiramientos", "Imitar movimientos del robot", "8 min", "Básico", Icons.Default.SelfImprovement, Color(0xFF00BCD4))
)

@Composable
fun ExerciseLibraryCard(exercise: Exercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                exercise.icon,
                contentDescription = null,
                tint = exercise.color,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(exercise.description, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    AssistChip(
                        onClick = { /* Acción opcional */ },
                        label = { Text(exercise.duration) },
                        modifier = Modifier.height(24.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFE0E0E0)
                        ),
                        border = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AssistChip(
                        onClick = { /* Acción opcional */ },
                        label = { Text(exercise.difficulty) },
                        modifier = Modifier.height(24.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (exercise.difficulty) {
                                "Básico" -> Color(0xFFC8E6C9)
                                "Intermedio" -> Color(0xFFFFF9C4)
                                else -> Color(0xFFFFCDD2)
                            }
                        ),
                        border = null
                    )
                }
            }
            IconButton(onClick = { /* Acción asignar */ }) {
                Icon(Icons.Default.AssignmentInd, contentDescription = "Asignar", tint = Color(0xFF0066A1))
            }
        }
    }
}

// -------------------------------------------------
// ROBOT CONTROL SCREEN (FUNCIONAL, COPIADA DEL MODO NIÑO)
// -------------------------------------------------
@Composable
fun TherapistControlRobotScreen() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var ipAddress by rememberSaveable { mutableStateOf("") }
    var connectionStatus by remember { mutableStateOf("⚪ No conectado") }
    var statusColor by remember { mutableStateOf(Color.Gray) }
    var isConnecting by remember { mutableStateOf(false) }

    suspend fun testConnection(ip: String): Boolean = withContext(Dispatchers.IO) {
        if (ip.isBlank()) return@withContext false
        try {
            val url = URL("http://$ip/status")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            val responseCode = connection.responseCode
            connection.disconnect()
            responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    suspend fun sendCommand(ip: String, endpoint: String, method: String = "GET", body: String? = null): String? =
        withContext(Dispatchers.IO) {
            if (ip.isBlank()) return@withContext null
            try {
                val url = URL("http://$ip$endpoint")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = method
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                if (method == "POST" && body != null) {
                    connection.doOutput = true
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.outputStream.use { os ->
                        os.write(body.toByteArray())
                    }
                }
                val responseCode = connection.responseCode
                val response = if (responseCode == 200) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    null
                }
                connection.disconnect()
                response
            } catch (e: Exception) {
                null
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Gamepad,
                contentDescription = null,
                tint = Color(0xFF0066A1),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Control del Robot",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0066A1)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Configuración de IP
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7F3)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Dirección del Robot",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    placeholder = { Text("Ej: 192.168.1.100") },
                    leadingIcon = { Icon(Icons.Default.Computer, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0066A1),
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = Color(0xFF0066A1)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(connectionStatus, fontSize = 14.sp, color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            if (ipAddress.isNotBlank()) {
                                scope.launch {
                                    isConnecting = true
                                    connectionStatus = "🔄 Conectando..."
                                    statusColor = Color(0xFFFFA500)
                                    val success = testConnection(ipAddress)
                                    if (success) {
                                        connectionStatus = "✅ Conectado"
                                        statusColor = Color(0xFF4CAF50)
                                    } else {
                                        connectionStatus = "❌ Error de conexión"
                                        statusColor = Color.Red
                                    }
                                    isConnecting = false
                                }
                            }
                        },
                        enabled = ipAddress.isNotBlank() && !isConnecting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (ipAddress.isNotBlank()) Color(0xFF0066A1) else Color.LightGray,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isConnecting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (isConnecting) "Conectando..." else "Conectar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (statusColor == Color(0xFF4CAF50)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Android,
                        contentDescription = "Robot",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Robot listo", fontWeight = FontWeight.Bold, color = Color(0xFF0066A1))
                        Text("IP: $ipAddress", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            "Movimiento del LED",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0066A1),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DirectionalButton(
                icon = Icons.Default.ArrowUpward,
                text = "Avanzar",
                color = Color(0xFF0066A1),
                onClick = {
                    if (ipAddress.isNotBlank()) {
                        scope.launch {
                            sendCommand(ipAddress, "/led/on")
                            connectionStatus = "💡 LED encendido"
                        }
                    }
                }
            )
            DirectionalButton(
                icon = Icons.Default.ArrowBack,
                text = "Izquierda",
                color = Color(0xFF0066A1),
                onClick = {
                    if (ipAddress.isNotBlank()) {
                        scope.launch {
                            sendCommand(ipAddress, "/command", "POST", """{"command":"blink","times":1}""")
                            connectionStatus = "✨ Parpadeo 1 vez"
                        }
                    }
                }
            )
            DirectionalButton(
                icon = Icons.Default.ArrowForward,
                text = "Derecha",
                color = Color(0xFF0066A1),
                onClick = {
                    if (ipAddress.isNotBlank()) {
                        scope.launch {
                            sendCommand(ipAddress, "/command", "POST", """{"command":"blink","times":2}""")
                            connectionStatus = "✨✨ Parpadeo 2 veces"
                        }
                    }
                }
            )
            DirectionalButton(
                icon = Icons.Default.ArrowDownward,
                text = "Retroceder",
                color = Color(0xFF0066A1),
                onClick = {
                    if (ipAddress.isNotBlank()) {
                        scope.launch {
                            sendCommand(ipAddress, "/led/off")
                            connectionStatus = "💤 LED apagado"
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7F3)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Brillo del LED",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0066A1)
                    )
                    Text("🚀", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                var brightness by remember { mutableStateOf(100) }
                Slider(
                    value = brightness.toFloat(),
                    onValueChange = {
                        brightness = it.toInt()
                        if (ipAddress.isNotBlank() && statusColor == Color(0xFF4CAF50)) {
                            scope.launch {
                                sendCommand(ipAddress, "/pwm/$brightness")
                                connectionStatus = "💡 Brillo $brightness%"
                            }
                        }
                    },
                    valueRange = 0f..100f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF0066A1),
                        activeTrackColor = Color(0xFF0066A1),
                        inactiveTrackColor = Color(0xFFE0E0E0)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🐢 $brightness%", fontSize = 14.sp, color = Color.Gray)
                    Text("🐰 100%", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Acciones adicionales",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0066A1),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.MusicNote,
                    text = "Sonido",
                    color = Color(0xFFB5EAD7),
                    iconColor = Color(0xFF0066A1),
                    onClick = {
                        if (ipAddress.isNotBlank()) {
                            scope.launch {
                                sendCommand(ipAddress, "/command", "POST", """{"command":"pulse","duration":2}""")
                                connectionStatus = "🎵 Efecto pulso"
                            }
                        }
                    }
                )
                ActionButton(
                    icon = Icons.Default.Lightbulb,
                    text = "Luz",
                    color = Color(0xFFFFF3B0),
                    iconColor = Color(0xFF0066A1),
                    onClick = {
                        if (ipAddress.isNotBlank()) {
                            scope.launch {
                                sendCommand(ipAddress, "/command", "POST", """{"command":"set","state":true}""")
                                connectionStatus = "💡 Luz encendida"
                            }
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.EmojiEmotions,
                    text = "Bailar",
                    color = Color(0xFFFFC8A2),
                    iconColor = Color(0xFF0066A1),
                    onClick = {
                        if (ipAddress.isNotBlank()) {
                            scope.launch {
                                sendCommand(ipAddress, "/command", "POST", """{"command":"blink","times":5}""")
                                connectionStatus = "🕺 Baile de luces"
                            }
                        }
                    }
                )
                ActionButton(
                    icon = Icons.Default.Tune,
                    text = "Calibrar",
                    color = Color(0xFFD4F1F9),
                    iconColor = Color(0xFF0066A1),
                    onClick = {
                        connectionStatus = "⚙️ Calibrando..."
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (ipAddress.isNotBlank()) {
                    scope.launch {
                        sendCommand(ipAddress, "/led/off")
                        connectionStatus = "🛑 EMERGENCIA - LED apagado"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0066A1),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(6.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = "Parar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("¡DETENER EMERGENCIA!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// -------------------------------------------------
// REPORTS SCREEN (SIMULADA, IGUAL QUE ANTES)
// -------------------------------------------------
@Composable
fun TherapistReportsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            "Informes de Progreso",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066A1)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Evolución de pacientes",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF0066A1))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Ana López", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("Últimos 7 días", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Frecuencia de ejercicios", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                BarChartItem("Marcha", 8, 12)
                BarChartItem("Alcance", 5, 12)
                BarChartItem("Coordinación", 3, 12)
                BarChartItem("Equilibrio", 6, 12)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF4CAF50))
                    Text("+15%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    Text("mejora", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFFFF9800))
                    Text("87%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
                    Text("adherencia", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun BarChartItem(label: String, value: Int, max: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.width(90.dp), fontSize = 14.sp)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(value.toFloat() / max)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0066A1))
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("$value", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0066A1))
    }
}

// -------------------------------------------------
// ABOUT PROJECT SCREEN (SIMULADA)
// -------------------------------------------------
@Composable
fun TherapistAboutProjectScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Sobre FisioBot Kids",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066A1)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    """
                    Proyecto Integrador: Robot Móvil de Bajo Costo para Asistencia en Terapia Física Infantil.
                    
                    FisioBot Kids es una solución innovadora diseñada para niños con dificultades motoras, parálisis cerebral o discapacidades físicas. A través de un robot móvil interactivo con apariencia de animalito amigable, se busca:
                    
                    • Guiar ejercicios terapéuticos de forma lúdica.
                    • Motivar la repetición mediante juegos y recompensas.
                    • Permitir seguimiento remoto por parte del terapeuta.
                    • Recopilar datos de progreso para ajustar tratamientos.
                    
                    El robot reacciona emocionalmente con luces y sonidos al logro del niño, creando un vínculo afectivo que incrementa la adherencia a la terapia.
                    
                    Desarrollado con mucho esfuerzo para contribuir a la rehabilitación infantil en Nicaragua y el mundo.
                    """.trimIndent(),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Justify
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AssistChip(
                onClick = { /* Acción informativa */ },
                label = { Text("Versión 1.0.0") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFE1F5FE)
                ),
                border = null
            )
        }
    }
}

// -------------------------------------------------
// BOTONES REUTILIZABLES
// -------------------------------------------------
@Composable
fun DirectionalButton(
    icon: ImageVector,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(2.dp, color, CircleShape)
                .shadow(elevation = 4.dp, shape = CircleShape)
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = color.copy(alpha = 0.9f)
        )
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    text: String,
    color: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = iconColor
            )
        }
    }
}