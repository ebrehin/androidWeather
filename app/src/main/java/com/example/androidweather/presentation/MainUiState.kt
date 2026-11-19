package com.example.androidweather.presentation

import com.example.androidweather.domain.model.ThemeMode

data class MainUiState(
    val isLoading: Boolean = true,
    val themeMode: ThemeMode? = null
)