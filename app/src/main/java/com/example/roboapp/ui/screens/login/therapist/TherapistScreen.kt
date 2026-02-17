package com.example.roboapp.ui.screens.login.therapist

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

// -------------------------------------------------
// ENTRY POINT - AHORA RECIBE userId
// -------------------------------------------------
@Composable
fun TherapistScreen(userId: String) {
    TherapistRootScreen(userId = userId)
}

// -------------------------------------------------
// ROOT SCREEN (MODAL NAVIGATION DRAWER)
// -------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapistRootScreen(userId: String) {   // <-- userId añadido
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf(0) }

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
            userId = userId   // <-- pasamos userId hacia abajo
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
// MAIN SCAFFOLD (TOPBAR + CONTENT)
// -------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TherapistMainScaffold(
    drawerState: DrawerState,
    selectedScreen: Int,
    userId: String   // <-- nuevo parámetro
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
                0 -> TherapistHomeScreen(userId = userId)               // <-- pasamos userId
                1 -> TherapistPatientsScreen(userId = userId)           // <-- (futuro)
                2 -> TherapistExercisesScreen(userId = userId)          // <-- (futuro)
                3 -> TherapistControlRobotScreen(userId = userId)       // <-- (futuro)
                4 -> TherapistReportsScreen(userId = userId)            // <-- (futuro)
                5 -> TherapistAboutProjectScreen(userId = userId)       // <-- (futuro)
            }
        }
    }
}

// -------------------------------------------------
// HOME SCREEN (DASHBOARD) - AHORA RECIBE userId
// -------------------------------------------------
@Composable
fun TherapistHomeScreen(userId: String) {   // <-- userId añadido
    // Por ahora no se usa, pero en el futuro se podría cargar información específica del terapeuta
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera con avatar y mensaje (podríamos mostrar el nombre real del terapeuta)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE1F5FE))
                    .border(2.dp, Color(0xFF0066A1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Terapeuta",
                    tint = Color(0xFF0066A1),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "Bienvenido, Dr. Martínez",   // En el futuro, esto podría ser el nombre real
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
                Text(
                    "Clínica de Rehabilitación Infantil",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjetas de resumen (simuladas, podrían ser reales más adelante)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryCard(
                title = "Pacientes",
                value = "12",
                icon = Icons.Default.People,
                color = Color(0xFF4CAF50)
            )
            SummaryCard(
                title = "Sesiones hoy",
                value = "8",
                icon = Icons.Default.Today,
                color = Color(0xFF2196F3)
            )
            SummaryCard(
                title = "Progreso ⌀",
                value = "74%",
                icon = Icons.Default.TrendingUp,
                color = Color(0xFFFF9800)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Próximas sesiones
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Próximas sesiones",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
                Spacer(modifier = Modifier.height(12.dp))
                SessionItem("Ana López", "09:30 AM", "Ejercicios de marcha")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                SessionItem("Carlos Ruiz", "11:00 AM", "Alcance de objetos")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                SessionItem("Sofía Méndez", "02:15 PM", "Coordinación motora")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alertas / tareas pendientes
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
                    Icons.Default.Warning,
                    contentDescription = "Alerta",
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "3 pacientes requieren reevaluación esta semana",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D4037)
                )
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
fun SessionItem(name: String, time: String, exercise: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(exercise, fontSize = 14.sp, color = Color.Gray)
        }
        Text(time, fontSize = 14.sp, color = Color(0xFF0066A1), fontWeight = FontWeight.Medium)
    }
}

// -------------------------------------------------
// PATIENTS SCREEN (futuro)
// -------------------------------------------------
@Composable
fun TherapistPatientsScreen(userId: String) {   // <-- añadido userId
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "Listado de Pacientes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066A1)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de pacientes (simulada)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(patientList) { patient ->
                PatientCard(patient)
            }
        }
    }
}

data class Patient(
    val id: Int,
    val name: String,
    val age: Int,
    val lastSession: String,
    val progress: Int,
    val avatarRes: Int? = null
)

val patientList = listOf(
    Patient(1, "Ana López", 7, "Ayer", 75),
    Patient(2, "Carlos Ruiz", 5, "Hace 2 días", 60),
    Patient(3, "Sofía Méndez", 8, "Hoy", 82),
    Patient(4, "Mateo Gómez", 4, "Lunes", 45),
    Patient(5, "Valentina Torres", 6, "Viernes", 90)
)

@Composable
fun PatientCard(patient: Patient) {
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE1F5FE))
                    .border(1.dp, Color(0xFF0066A1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    patient.name.take(1),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066A1)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(patient.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "${patient.age} años · Última sesión: ${patient.lastSession}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Barra de progreso
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Progreso:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(patient.progress / 100f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF4CAF50))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${patient.progress}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------
// EXERCISES SCREEN (futuro)
// -------------------------------------------------
@Composable
fun TherapistExercisesScreen(userId: String) {   // <-- añadido userId
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
                        onClick = { /* Acción opcional: quizás filtrar? */ },
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
// ROBOT CONTROL SCREEN (futuro)
// -------------------------------------------------
@Composable
fun TherapistControlRobotScreen(userId: String) {   // <-- añadido userId
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Control Avanzado del Robot",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066A1)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Calibración y pruebas",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Estado del robot
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = "Estado", tint = Color(0xFF0066A1))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Estado del robot", fontWeight = FontWeight.Bold)
                    Text("Conectado · Batería 85%", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Controles direccionales
        Text("Movimiento manual", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlButtonAdv(icon = Icons.Default.ArrowUpward, text = "Avanzar", color = Color(0xFF2196F3))
            ControlButtonAdv(icon = Icons.Default.ArrowBack, text = "Izquierda", color = Color(0xFF2196F3))
            ControlButtonAdv(icon = Icons.Default.ArrowForward, text = "Derecha", color = Color(0xFF2196F3))
            ControlButtonAdv(icon = Icons.Default.ArrowDownward, text = "Retroceder", color = Color(0xFF2196F3))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Velocidad
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Velocidad", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = 0.5f,
                    onValueChange = {},
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF0066A1),
                        activeTrackColor = Color(0xFF0066A1)
                    )
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Lenta", fontSize = 12.sp)
                    Text("Rápida", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Acciones especiales
        Text("Acciones", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0066A1))
            ) {
                Icon(Icons.Default.Bluetooth, null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Calibrar")
            }
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.PlayArrow, null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Iniciar terapia")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Stop, null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Parada de emergencia")
        }
    }
}

@Composable
fun ControlButtonAdv(icon: ImageVector, text: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f))
        ) {
            Icon(icon, contentDescription = text, tint = color)
        }
        Text(text, fontSize = 12.sp, color = color, modifier = Modifier.padding(top = 4.dp))
    }
}

// -------------------------------------------------
// REPORTS SCREEN (futuro)
// -------------------------------------------------
@Composable
fun TherapistReportsScreen(userId: String) {   // <-- añadido userId
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

        // Selector de paciente simulado
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

        // Gráfico de barras simulado
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Frecuencia de ejercicios", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                // Barras horizontales (simulación)
                BarChartItem("Marcha", 8, 12)
                BarChartItem("Alcance", 5, 12)
                BarChartItem("Coordinación", 3, 12)
                BarChartItem("Equilibrio", 6, 12)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjetas de resumen
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
// ABOUT PROJECT SCREEN (futuro)
// -------------------------------------------------
@Composable
fun TherapistAboutProjectScreen(userId: String) {   // <-- añadido userId
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