package com.example.roboapp.ui.screens.login.child

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun ChildModeScreen() {
    ChildModeRootScreen()
}

/* ------------------------------------------------ */
/* ------------------- ROOT SCREEN ---------------- */
/* ------------------------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildModeRootScreen() {

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
                    "Menú",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = selectedScreen == 0,
                    onClick = {
                        selectedScreen = 0
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Ejercicios") },
                    selected = selectedScreen == 1,
                    onClick = {
                        selectedScreen = 1
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.FitnessCenter, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Controlar Robot") },
                    selected = selectedScreen == 2,
                    onClick = {
                        selectedScreen = 2
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Gamepad, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Sobre el Proyecto") },
                    selected = selectedScreen == 3,
                    onClick = {
                        selectedScreen = 3
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Info, null) }
                )
            }
        }
    ) {
        ChildMainScaffold(
            drawerState = drawerState,
            selectedScreen = selectedScreen
        )
    }
}

/* ------------------------------------------------ */
/* ------------------- MAIN SCAFFOLD -------------- */
/* ------------------------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildMainScaffold(drawerState: DrawerState, selectedScreen: Int) {

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Modo Niño", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFFB6B9),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (selectedScreen) {
                0 -> ChildHomeScreen()
                1 -> ChildExercisesScreen()
                2 -> ControlRobotScreen()
                3 -> AboutProjectScreen()
            }
        }
    }
}

/* ------------------------------------------------ */
/* ------------------- HOME SCREEN ---------------- */
/* ------------------------------------------------ */

@Composable
fun ChildHomeScreen() {

    val infiniteTransition = rememberInfiniteTransition()

    val floatAnim by infiniteTransition.animateFloat(
        0f, 12f,
        infiniteRepeatable(
            tween(1600, easing = EaseInOut),
            RepeatMode.Reverse
        )
    )

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(y = floatAnim.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = R.drawable.robot_avatar,
                contentDescription = "Robot",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .shadow(16.dp, CircleShape)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "¡Hola! Soy tu robot amigo, ¿listo para explorar?",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF4A4A4A)
        )

        Spacer(Modifier.height(30.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            DashboardCard(
                title = "Progreso General",
                value = "72%",
                color = Color(0xFF4F90FF),
                icon = Icons.Default.BarChart
            )

            DashboardCard(
                title = "Puntaje Total",
                value = "350 ⭐",
                color = Color(0xFFFFC400),
                icon = Icons.Default.Star
            )

            DashboardCard(
                title = "Ejercicios Completados",
                value = "15",
                color = Color(0xFF73C98D),
                icon = Icons.Default.FitnessCenter
            )
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, color: Color, icon: ImageVector) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(icon, null, tint = color, modifier = Modifier.size(40.dp))

            Spacer(Modifier.width(16.dp))

            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            }
        }
    }
}

/* ------------------------------------------------ */
/* ------------------- EXERCISES SCREEN ----------- */
/* ------------------------------------------------ */

@Composable
fun ChildExercisesScreen() {

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Ejercicios Disponibles",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4A4A)
        )

        Spacer(Modifier.height(20.dp))

        ExerciseCard("Caminar", "🚶", Color(0xFF73C98D))
        Spacer(Modifier.height(14.dp))

        ExerciseCard("Bailar", "💃", Color(0xFFFF9EC4))
        Spacer(Modifier.height(14.dp))

        ExerciseCard("Alcanzar Objetos", "🎯", Color(0xFF4F90FF))
    }
}

@Composable
fun ExerciseCard(title: String, emoji: String, color: Color) {

    Card(
        Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(emoji, fontSize = 46.sp)

            Spacer(Modifier.width(20.dp))

            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
                Text("Toque para comenzar", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

/* ------------------------------------------------ */
/* ---------------- ABOUT PROJECT SCREEN ---------- */
/* ------------------------------------------------ */

@Composable
fun AboutProjectScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Sobre el Proyecto",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2D4059)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F7F3)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = """
                        Este proyecto fue creado como una herramienta accesible para ayudar a niños y jóvenes a mejorar sus habilidades motoras, cognitivas y sociales mediante la interacción con un robot amigable.
                        
                        El sistema incluye actividades interactivas, control remoto educativo, estímulos visuales y un diseño accesible para todo público. Además, busca convertirse en una plataforma escalable, segura y adaptable a escuelas, clínicas y hogares.
                        
                        Desarrollado con mucho esfuerzo y dedicación para ofrecer una experiencia moderna, intuitiva y motivadora.
                    """.trimIndent(),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Justify,
                    color = Color(0xFF4A4A4A)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFE5E5),
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = "Versión 1.0.0",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFF6B6B)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* ------------------------------------------------ */
/* ---------------- ROBOT CONTROL SCREEN ---------- */
/* ------------- CON IP Y COMANDOS LED ------------ */
/* ------------- CORREGIDO: Dispatchers.IO -------- */
/* ------------------------------------------------ */

@Composable
fun ControlRobotScreen() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var ipAddress by rememberSaveable { mutableStateOf("") }
    var connectionStatus by remember { mutableStateOf("⚪ No conectado") }
    var statusColor by remember { mutableStateOf(Color.Gray) }
    var isConnecting by remember { mutableStateOf(false) }

    // --- FUNCIONES DE RED CORREGIDAS: USAN withContext(Dispatchers.IO) ---
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

    // --- UI ---
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
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Controla tu Robot",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2D4059)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ----- CONFIGURACIÓN DE IP -----
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
                    color = Color(0xFF2D4059)
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
                        focusedBorderColor = Color(0xFFFF6B6B),
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = Color(0xFFFF6B6B)
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
                            containerColor = if (ipAddress.isNotBlank()) Color(0xFFFF6B6B) else Color.LightGray,
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
                        Text("Robot listo", fontWeight = FontWeight.Bold, color = Color(0xFF2D4059))
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
            color = Color(0xFF2D4059),
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
                color = Color(0xFFFF8B94),
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
                color = Color(0xFFA8E6CF),
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
                color = Color(0xFFA8E6CF),
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
                color = Color(0xFFFFD3B5),
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
                        color = Color(0xFF2D4059)
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
                        thumbColor = Color(0xFFFF6B6B),
                        activeTrackColor = Color(0xFFFF6B6B),
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
            "¡Acciones divertidas!",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D4059),
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
                    iconColor = Color(0xFF4A7A6B),
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
                    iconColor = Color(0xFFD4A200),
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
                    iconColor = Color(0xFFB45F2E),
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
                    iconColor = Color(0xFF1E7C8C),
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
                containerColor = Color(0xFFFF6B6B),
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

// ----- BOTÓN DIRECCIONAL -----
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

// ----- BOTÓN DE ACCIÓN -----
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