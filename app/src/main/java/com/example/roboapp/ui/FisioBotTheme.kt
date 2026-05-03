package com.example.roboapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

@Composable
fun FisioBotTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4CAF50),
            secondary = Color(0xFF2196F3),
            background = Color(0xFFF5F5F5),
            surface = Color.White
        ),
        typography = MaterialTheme.typography.copy(
            headlineLarge = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            bodyLarge = TextStyle(
                fontSize = 18.sp
            )
        ),
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}
