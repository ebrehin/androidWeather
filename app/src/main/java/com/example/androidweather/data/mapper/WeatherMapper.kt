package com.example.androidweather.data.mapper

import com.example.androidweather.data.api.CoordinateVO
import com.example.androidweather.data.api.WeatherCityVO
import com.example.androidweather.data.api.WeatherInfoVo
import com.example.androidweather.data.api.WeatherItemVO
import com.example.androidweather.data.api.WeatherResultVO
import com.example.androidweather.domain.model.CoordinateModel
import com.example.androidweather.domain.model.WeatherCityModel
import com.example.androidweather.domain.model.WeatherInfoModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.model.WeatherResultModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun WeatherResultVO.toModel() = WeatherResultModel(
    city = this.city?.toModel(),
    items = this.list?.map { it.toModel() },
)

fun WeatherCityVO.toModel() = WeatherCityModel(
    name = this.name,
    coord = this.coord.toModel(),
    population = this.population,
    sunrise = this.sunrise,
    sunset = this.sunset
)

fun CoordinateVO.toModel() = CoordinateModel(
    lat = this.lat,
    lon = this.lon
)

fun WeatherItemVO.toModel() = WeatherItemModel(
    date = LocalDateTime.parse(this.dt_txt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    image = when {
        main.humidity < 60 -> "1"
        main.humidity < 80 -> "2"
        else -> "3"
    },
    infos = this.main.toModel()
).apply {
    this.day = date.dayOfMonth.toString()
    this.hour = date.hour.toString()
}

fun WeatherInfoVo.toModel() = WeatherInfoModel(
    temp = this.temp,
    humidity = this.humidity,
    pressure = this.pressure
)