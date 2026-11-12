package com.example.androidweather.data.mapper

import com.example.androidweather.data.local.WeatherEntity
import com.example.androidweather.data.remote.WeatherResultVO
import com.example.androidweather.domain.model.CoordinateModel
import com.example.androidweather.domain.model.WeatherCityModel
import com.example.androidweather.domain.model.WeatherInfoModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.model.WeatherResultModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val apiDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

/** API (VO) -> base de données (Entity) : une entité par créneau de prévision. */
fun WeatherResultVO.toEntities(searchKey: String): List<WeatherEntity> {
    val city = this.city ?: return emptyList()
    return this.list.orEmpty().map { item ->
        WeatherEntity(
            searchKey = searchKey,
            cityName = city.name,
            lat = city.coord.lat,
            lon = city.coord.lon,
            population = city.population,
            sunrise = city.sunrise,
            sunset = city.sunset,
            date = LocalDateTime.parse(item.dt_txt, apiDateFormat).toString(),
            weatherType = mapCodeToWeatherType(item.weather?.firstOrNull()?.icon),
            temp = item.main.temp,
            humidity = item.main.humidity,
            pressure = item.main.pressure
        )
    }
}

/** Base de données (Entity) -> domaine (Model). */
fun List<WeatherEntity>.toModel(): WeatherResultModel {
    val first = this.firstOrNull() ?: return WeatherResultModel()
    return WeatherResultModel(
        city = WeatherCityModel(
            name = first.cityName,
            coord = CoordinateModel(lat = first.lat, lon = first.lon),
            population = first.population,
            sunrise = first.sunrise,
            sunset = first.sunset
        ),
        items = this.map { it.toItemModel() }
    )
}

private fun WeatherEntity.toItemModel(): WeatherItemModel {
    val dateTime = LocalDateTime.parse(this.date)
    return WeatherItemModel(
        date = dateTime,
        weatherType = this.weatherType,
        infos = WeatherInfoModel(temp = temp, humidity = humidity, pressure = pressure)
    ).apply {
        this.day = dateTime.dayOfMonth.toString()
        this.hour = dateTime.hour.toString()
    }
}