package com.example.fisiobotkids.data.repository

import com.example.fisiobotkids.data.model.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class MockFisioBotRepository : FisioBotRepository {

    // Simulamos varios niños
    private val childrenList = MutableStateFlow(
        listOf(
            Child(id = "child1", nombre = "María", edad = 7),
            Child(id = "child2", nombre = "Carlos", edad = 9)
        )
    )

    // Datos por niño
    private val sensorFlows = mutableMapOf<String, MutableStateFlow<SensorData>>()
    private val stateFlows = mutableMapOf<String, MutableStateFlow<RobotState>>()

    private fun getOrCreateSensorFlow(childId: String) =
        sensorFlows.getOrPut(childId) {
            MutableStateFlow(SensorData(distancia_cm = 100f, velocidad = 0.5f))
        }

    private fun getOrCreateStateFlow(childId: String) =
        stateFlows.getOrPut(childId) {
            MutableStateFlow(RobotState())
        }

    override fun getSensorData(childId: String): Flow<SensorData> =
        getOrCreateSensorFlow(childId)

    override fun getRobotState(childId: String): Flow<RobotState> =
        getOrCreateStateFlow(childId)

    override suspend fun enviarComandos(childId: String, comandos: Comandos) {
        val state = getOrCreateStateFlow(childId)
        val sensor = getOrCreateSensorFlow(childId)
        state.value = state.value.copy(modo_actual = comandos.modo)
        when (comandos.modo) {
            "sigueme" -> sensor.value = sensor.value.copy(
                distancia_cm = Random.nextFloat() * 150f,
                velocidad = comandos.velocidad_max
            )
            "stop" -> sensor.value = sensor.value.copy(velocidad = 0f)
        }
    }

    override fun getChildrenList(): Flow<List<Child>> = childrenList

    override suspend fun addChild(nombre: String, edad: Int) {
        val newId = "child_${System.currentTimeMillis()}"
        val updated = childrenList.value + Child(newId, nombre, edad)
        childrenList.value = updated
    }
}