package com.example.roboapp.ui.screens.login.child
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

import com.example.roboapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildModeScreen() {

    var selectedExercise by remember { mutableStateOf<ExerciseType?>(null) }
    var robotMessage by remember { mutableStateOf("¡Hola! ¿Listo para jugar conmigo? 🤖") }
    var progress by remember { mutableStateOf(0.4f) }
    var score by remember { mutableStateOf(120) }
    var selectedTab by remember { mutableStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = EaseInOut),
            RepeatMode.Reverse
        ),
        label = ""
    )

    val rotateAnim by infiniteTransition.animateFloat(
        -6f, 6f,
        infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = ""
    )

    val pulseAnim by infiniteTransition.animateFloat(
        0.9f, 1.1f,
        infiniteRepeatable(tween(1400), RepeatMode.Reverse),
        label = ""
    )

    Scaffold(
        topBar = {
            ChildTopBar(score = score)
        },
        bottomBar = {
            ChildBottomBar(selectedTab) { selectedTab = it }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0A1F3A), Color(0xFF1E3C72))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(16.dp))

            // 🤖 ROBOT GIF
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .offset(y = floatAnim.dp)
                    .rotate(rotateAnim),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.robot_avatar,
                    contentDescription = "Robot",
                    modifier = Modifier
                        .size(180.dp)
                        .scale(pulseAnim)
                        .clip(CircleShape)
                        .border(3.dp, Color.Cyan, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = robotMessage,
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color.Cyan
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("🎮 EJERCICIOS") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("📊 ESTADÍSTICAS") }
                )
            }

            Spacer(Modifier.height(16.dp))

            if (selectedTab == 0) {
                ExercisesSection(
                    selectedExercise = selectedExercise,
                    onExerciseSelected = {
                        selectedExercise = it
                        robotMessage = it.robotMessage
                        score += 10
                        progress += 0.1f
                    }
                )
            } else {
                StatisticsSection(progress, score)
            }
        }
    }
}

/* ------------------------------------------------ */
/* ------------------ COMPONENTES ------------------ */
/* ------------------------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildTopBar(score: Int) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "🤖 MODO NIÑO",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Text("$score ⭐", fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.Emergency, contentDescription = null, tint = Color.Red)
            Spacer(Modifier.width(8.dp))
        }
    )
}

@Composable
fun ChildBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF081A2F))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomBarItem(Icons.Filled.Home, "Inicio", selectedTab == 0) {
            onTabSelected(0)
        }
        BottomBarItem(Icons.Filled.BarChart, "Stats", selectedTab == 1) {
            onTabSelected(1)
        }
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (selected) Color.Cyan else Color.White
        )
        Text(label, fontSize = 11.sp, color = Color.White)
    }
}

@Composable
fun ExercisesSection(
    selectedExercise: ExerciseType?,
    onExerciseSelected: (ExerciseType) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ExerciseType.values()) { exercise ->
            ExerciseCard(
                exercise = exercise,
                selected = exercise == selectedExercise,
                onClick = { onExerciseSelected(exercise) }
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseType,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(160.dp),
        shape = RoundedCornerShape(20.dp),
        border = if (selected) BorderStroke(3.dp, exercise.color) else null,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(exercise.emoji, fontSize = 34.sp)
            Spacer(Modifier.height(8.dp))
            Text(exercise.title, fontWeight = FontWeight.Bold)
            Text(
                exercise.description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun StatisticsSection(progress: Float, score: Int) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Progreso", color = Color.White)
        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = Color.Cyan
        )

        Spacer(Modifier.height(16.dp))

        Text("Puntaje total: $score ⭐", color = Color.White)
    }
}

/* ------------------------------------------------ */
/* ------------------ MODELO ---------------------- */
/* ------------------------------------------------ */

enum class ExerciseType(
    val emoji: String,
    val title: String,
    val description: String,
    val color: Color,
    val robotMessage: String
) {
    WALK(
        "🚶",
        "Caminar",
        "Sigue al robot caminando",
        Color.Green,
        "¡Vamos a caminar juntos!"
    ),
    DANCE(
        "💃",
        "Bailar",
        "Imita los movimientos",
        Color.Magenta,
        "¡Muévete conmigo!"
    ),
    REACH(
        "🎯",
        "Alcanzar",
        "Toca los objetivos",
        Color.Cyan,
        "¡Intenta alcanzarme!"
    )
}
