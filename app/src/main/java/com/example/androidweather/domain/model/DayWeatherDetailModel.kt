package com.example.androidweather.domain.model

data class DayWeatherDetailModel(
    val cityName: String,
    val dateString: String,
    val minTemp: Double,
    val maxTemp: Double,
    val weatherType: WeatherTypeEnum,
    val hourlyItems: List<WeatherItemModel>
)