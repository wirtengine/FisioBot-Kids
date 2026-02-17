package com.example.roboapp.data.model

import com.google.firebase.Timestamp

data class RoboUser(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val age: Int = 0,
    val role: String = "child",
    val code: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
