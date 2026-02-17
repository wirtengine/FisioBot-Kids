package com.example.roboapp.data.model

import com.google.firebase.Timestamp

data class UserStats(
    val overallProgress: Int = 0,      // porcentaje
    val totalScore: Int = 0,            // puntaje acumulado
    val exercisesCompleted: Int = 0,    // número de ejercicios completados
    val lastUpdated: Timestamp = Timestamp.now()
)