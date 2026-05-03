package com.example.roboapp.data.repository

import com.example.roboapp.data.model.RoboUser
import com.example.roboapp.data.model.UserStats
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    // Obtener usuario por UID
    suspend fun getUser(uid: String): RoboUser? {
        return try {
            val document = db.collection("users").document(uid).get().await()
            document.toObject(RoboUser::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Obtener estadísticas del usuario
    suspend fun getUserStats(uid: String): UserStats? {
        return try {
            val document = db.collection("users").document(uid)
                .collection("stats").document("current").get().await()
            document.toObject(UserStats::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Actualizar estadísticas (se llamará cuando complete un ejercicio)
    suspend fun updateUserStats(uid: String, newStats: UserStats) {
        try {
            db.collection("users").document(uid)
                .collection("stats").document("current")
                .set(newStats)
                .await()
        } catch (e: Exception) {
            // Manejar error
        }
    }
    suspend fun getPatientsByTherapist(therapistId: String): List<RoboUser> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("role", "child")
                .whereEqualTo("therapistId", therapistId)
                .get()
                .await()
            snapshot.toObjects(RoboUser::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}