package com.example.androidweather.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "WeatherResult")
@Serializable
data class WeatherResultVO(
    @PrimaryKey(autoGenerate = true)
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
    val main: WeatherInfoVo
)

@Serializable
data class WeatherInfoVo(
    val temp: Double,
    val humidity: Double,
    val pressure: Double
)