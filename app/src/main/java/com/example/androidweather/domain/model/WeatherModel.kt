package com.example.androidweather.domain.model

import java.time.LocalDateTime

data class WeatherResultModel(
    val city: WeatherCityModel? = null,
    val items: List<WeatherItemModel>? = listOf()
)

data class WeatherCityModel(
    val name: String,
    val coord: CoordinateModel,
    var population: Int,
    val sunrise: Double? = null,
    val sunset: Double? = null
)

data class CoordinateModel(
    val lat: Double? = null,
    val lon: Double? = null
)

data class WeatherItemModel(
    val date: LocalDateTime,
    val weatherType: WeatherTypeEnum,
    val infos: WeatherInfoModel,
    var day: String? = null,
    var hour: String? = null
)

data class WeatherInfoModel(
    val temp: Double,
    val humidity: Double,
    val pressure: Double
)