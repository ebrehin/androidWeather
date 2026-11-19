package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.ThemeMode
import com.example.androidweather.domain.repository.SettingsRepositoryInterface
import kotlinx.coroutines.flow.Flow

class ObserveThemeModeUseCase(private val repository: SettingsRepositoryInterface) {
    operator fun invoke(): Flow<ThemeMode> = repository.observeThemeMode()
}