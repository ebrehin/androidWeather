package com.example.androidweather.presentation.theme

import androidx.compose.ui.graphics.Color

// Palette "météo" : bleu ciel, orange soleil, gris nuage.
// Format : Color(0xAARRGGBB) -> AA = opacité (FF = opaque), puis rouge, vert, bleu.

// Thème clair
val SkyBlue40 = Color(0xFF1E6FD9)        // primary : bleu ciel
val CloudGrey40 = Color(0xFF546E7A)      // secondary : gris nuage
val SunOrange40 = Color(0xFFE67E22)      // tertiary : orange soleil
val LightBackground = Color(0xFFFFFFFF)  // fonds blancs (écran + TopAppBar)
val LightCard = Color(0xFFBBD9FF)        // cards : bleu clair
val OnLightCard = Color(0xFF0B2545)      // texte sur les cards : bleu très foncé

// Thème sombre
val SkyBlue80 = Color(0xFFA8C7FF)
val CloudGrey80 = Color(0xFFB0BEC5)
val SunOrange80 = Color(0xFFFFB877)
val DarkBackground = Color(0xFF181818)   // fonds : gris très foncé (écran + TopAppBar)
val DarkCard = Color(0xFF00153A)         // cards : bleu nuit
val OnDarkCard = Color(0xFF73A7F3)       // texte sur les cards : bleu clair