package com.example.androidweather.utils

import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.model.WeatherTypeEnum

fun List<WeatherItemModel>.calculateDominantWeatherType(): WeatherTypeEnum {
    if (this.isEmpty()) return WeatherTypeEnum.UNKNOWN
    return this.groupingBy { it.weatherType }
        .eachCount()
        .maxByOrNull { (_, count) -> count }
        ?.key ?: WeatherTypeEnum.UNKNOWN
}