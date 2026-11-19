package com.example.androidweather.domain.repository

import com.example.androidweather.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepositoryInterface {
    fun observeThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)
}