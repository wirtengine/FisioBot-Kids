package com.example.roboapp.ui.screens.login.therapist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboapp.data.model.RoboUser
import com.example.roboapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TherapistViewModel(
    private val userRepository: UserRepository,
    private val therapistId: String
) : ViewModel() {

    private val _therapist = MutableStateFlow<RoboUser?>(null)
    val therapist: StateFlow<RoboUser?> = _therapist

    private val _patients = MutableStateFlow<List<RoboUser>>(emptyList())
    val patients: StateFlow<List<RoboUser>> = _patients

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadTherapistData()
        loadPatients()
    }

    private fun loadTherapistData() {
        viewModelScope.launch {
            _isLoading.value = true
            _therapist.value = userRepository.getUser(therapistId)
            _isLoading.value = false
        }
    }

    private fun loadPatients() {
        viewModelScope.launch {
            _patients.value = userRepository.getPatientsByTherapist(therapistId)
        }
    }

    // Función para recargar pacientes después de asignar uno nuevo
    fun refreshPatients() {
        viewModelScope.launch {
            _patients.value = userRepository.getPatientsByTherapist(therapistId)
        }
    }
}