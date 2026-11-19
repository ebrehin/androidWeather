package com.example.androidweather.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue80,
    secondary = CloudGrey80,
    tertiary = SunOrange80,
    background = DarkBackground,
    surface = DarkBackground,              // TopAppBar
    surfaceContainer = DarkBackground,     // TopAppBar quand on scrolle
    primaryContainer = DarkCard,           // Cards
    onPrimaryContainer = OnDarkCard        // Texte dans les cards
)

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue40,
    secondary = CloudGrey40,
    tertiary = SunOrange40,
    background = LightBackground,
    surface = LightBackground,
    surfaceContainer = LightBackground,
    primaryContainer = LightCard,
    onPrimaryContainer = OnLightCard
)

@Composable
fun AndroidWeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}