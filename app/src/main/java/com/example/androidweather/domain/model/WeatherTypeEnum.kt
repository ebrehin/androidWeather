package com.example.androidweather.domain.model

enum class WeatherTypeEnum(val description: String) {
    SUN("Ensoleillé"),
    CLOUD("Nuageux"),
    RAIN("Pluvieux"),
    SNOW("Neigeux"),
    THUNDERSTORM("Orageux"),
    UNKNOWN("Inconnu")
}