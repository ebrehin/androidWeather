package com.example.androidweather.presentation.screen.settings

import com.example.androidweather.domain.model.ThemeMode

data class SettingsUiState(
    val isLoading: Boolean = true,
    val themeMode: ThemeMode? = null
)