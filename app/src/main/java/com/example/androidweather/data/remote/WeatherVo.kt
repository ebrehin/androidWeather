package com.example.androidweather.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResultVO(
    val id: Int? = null,
    val city: WeatherCityVO? = null,
    val list: List<WeatherItemVO>? = listOf()
)

@Serializable
data class WeatherCityVO(
    val name: String,
    val coord: CoordinateVO,
    var population: Int,
    val sunrise: Double? = null,
    val sunset: Double? = null
)

@Serializable
data class CoordinateVO(
    val lat: Double? = null,
    val lon: Double? = null
)

@Serializable
data class WeatherItemVO(
    val dt_txt: String,
    val dt: Float,
    val main: WeatherInfoVo,
    val weather: List<WeatherConditionVO>? = listOf()
)

@Serializable
data class WeatherConditionVO(
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)

@Serializable
data class WeatherInfoVo(
    val temp: Double,
    val humidity: Double,
    val pressure: Double
)