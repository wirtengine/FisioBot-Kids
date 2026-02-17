package com.example.roboapp.ui.screens.login.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboapp.data.model.RoboUser
import com.example.roboapp.data.model.UserStats
import com.example.roboapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChildViewModel(
    private val userRepository: UserRepository,
    private val userId: String
) : ViewModel() {

    private val _user = MutableStateFlow<RoboUser?>(null)
    val user: StateFlow<RoboUser?> = _user

    private val _stats = MutableStateFlow<UserStats?>(null)
    val stats: StateFlow<UserStats?> = _stats

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUserData()
        loadStats()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            _user.value = userRepository.getUser(userId)
            _isLoading.value = false
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            _stats.value = userRepository.getUserStats(userId)
        }
    }

    fun updateStatsAfterExercise(score: Int) {
        viewModelScope.launch {
            val currentStats = _stats.value ?: UserStats()
            val newStats = currentStats.copy(
                totalScore = currentStats.totalScore + score,
                exercisesCompleted = currentStats.exercisesCompleted + 1,
                overallProgress = calculateProgress(currentStats.exercisesCompleted + 1)
            )
            userRepository.updateUserStats(userId, newStats)
            _stats.value = newStats
        }
    }

    private fun calculateProgress(completed: Int): Int {
        // Ejemplo: 10 ejercicios = 100% de progreso
        return (completed * 10).coerceAtMost(100)
    }
}