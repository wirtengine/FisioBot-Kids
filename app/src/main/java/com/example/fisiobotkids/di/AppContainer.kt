package com.example.fisiobotkids.di

import com.example.fisiobotkids.data.repository.FirebaseFisioBotRepository
import com.example.fisiobotkids.data.repository.FisioBotRepository
import com.example.fisiobotkids.data.repository.MockFisioBotRepository

object AppContainer {
    // Cambiar a false para usar Firebase real
    private const val USE_MOCK = true

    val repository: FisioBotRepository by lazy {
        if (USE_MOCK) MockFisioBotRepository() else FirebaseFisioBotRepository()
    }
}