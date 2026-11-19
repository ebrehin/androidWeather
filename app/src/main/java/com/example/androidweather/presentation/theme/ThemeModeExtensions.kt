package com.example.androidweather.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.example.androidweather.domain.model.ThemeMode

/**
 * Indique si le thème sombre doit être appliqué.
 * Tant que la préférence n'est pas chargée (null), on suit le système.
 */
@Composable
fun ThemeMode?.isDark(): Boolean = when (this) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    ThemeMode.SYSTEM, null -> isSystemInDarkTheme()
}