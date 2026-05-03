package com.example.fisiobotkids.data.repository

import com.example.fisiobotkids.data.model.*
import kotlinx.coroutines.flow.Flow

interface FisioBotRepository {
    fun getSensorData(childId: String): Flow<SensorData>
    fun getRobotState(childId: String): Flow<RobotState>
    suspend fun enviarComandos(childId: String, comandos: Comandos)
    fun getChildrenList(): Flow<List<Child>>
    suspend fun addChild(nombre: String, edad: Int)
}