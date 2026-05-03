package com.example.fisiobotkids.data.repository

import com.example.fisiobotkids.data.model.*
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class FirebaseFisioBotRepository : FisioBotRepository {

    private val db = FirebaseDatabase.getInstance()

    override fun getSensorData(childId: String): Flow<SensorData> = callbackFlow {
        val ref = db.getReference("robot/$childId/sensores")
        val listener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(SensorData::class.java)
                if (data != null) trySend(data)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun getRobotState(childId: String): Flow<RobotState> = callbackFlow {
        val ref = db.getReference("robot/$childId/estado")
        val listener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val state = snapshot.getValue(RobotState::class.java)
                if (state != null) trySend(state)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun enviarComandos(childId: String, comandos: Comandos) {
        db.getReference("robot/$childId/comandos").setValue(comandos)
    }

    override fun getChildrenList(): Flow<List<Child>> = callbackFlow {
        val ref = db.getReference("niños")
        val listener = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children.map { snap ->
                    Child(
                        id = snap.key ?: "",
                        nombre = snap.child("nombre").getValue(String::class.java) ?: "",
                        edad = snap.child("edad").getValue(Int::class.java) ?: 0
                    )
                }
                trySend(children)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun addChild(nombre: String, edad: Int) {
        val newRef = db.getReference("niños").push()
        newRef.setValue(mapOf("nombre" to nombre, "edad" to edad))
    }
}