package com.example.roboapp.data.model

data class RoboUser(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val userType: String = "CHILD", // "CHILD" o "THERAPIST"
    val createdAt: Long = System.currentTimeMillis()
)