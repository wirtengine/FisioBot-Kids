package com.example.fisiobotkids.data.mapper

import com.example.fisiobotkids.data.model.RobotState
import com.example.fisiobotkids.data.model.SensorData
import com.google.firebase.database.DataSnapshot


object DataMapper {

    fun snapshotToSensorData(snapshot: DataSnapshot): SensorData {
        return SensorData(
            distancia_cm = snapshot.child("distancia_cm").getValue(Float::class.java) ?: 0f,
            velocidad    = snapshot.child("velocidad").getValue(Float::class.java) ?: 0f,
            encoder_izq  = snapshot.child("encoder_izq").getValue(Int::class.java) ?: 0,
            encoder_der  = snapshot.child("encoder_der").getValue(Int::class.java) ?: 0,
            timestamp    = snapshot.child("timestamp").getValue(Long::class.java) ?: System.currentTimeMillis()
        )
    }

    fun snapshotToRobotState(snapshot: DataSnapshot): RobotState {
        return RobotState(
            modo_actual = snapshot.child("modo_actual").getValue(String::class.java) ?: "stop",
            bateria     = snapshot.child("bateria").getValue(Int::class.java) ?: 100,
            conectado   = snapshot.child("conectado").getValue(Boolean::class.java) ?: false
        )
    }
}