package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.ThemeMode
import com.example.androidweather.domain.repository.SettingsRepositoryInterface

class SetThemeModeUseCase(private val repository: SettingsRepositoryInterface) {
    suspend operator fun invoke(themeMode: ThemeMode) = repository.setThemeMode(themeMode)
}