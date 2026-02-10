package com.example.roboapp.ui.screens.login.child

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChildModeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB)
                    )
                )
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "👋 ¡Hola!",
            style = MaterialTheme.typography.headlineLarge
        )

        Button(
            onClick = { /* luego aquí irá el ejercicio */ },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text("🚶 Caminar")
        }

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800)
            )
        ) {
            Text("🎵 Bailar")
        }

        Text(
            text = "⭐ Muy bien ⭐",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
