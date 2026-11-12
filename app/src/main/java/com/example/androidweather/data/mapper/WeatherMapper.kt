package com.example.androidweather.data.mapper

import com.example.androidweather.data.remote.CoordinateVO
import com.example.androidweather.data.remote.WeatherCityVO
import com.example.androidweather.data.remote.WeatherInfoVo
import com.example.androidweather.data.remote.WeatherItemVO
import com.example.androidweather.data.remote.WeatherResultVO
import com.example.androidweather.domain.model.CoordinateModel
import com.example.androidweather.domain.model.WeatherCityModel
import com.example.androidweather.domain.model.WeatherInfoModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.model.WeatherTypeEnum
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
    weatherType = mapCodeToWeatherType(this.weather?.firstOrNull()?.icon),
    infos = this.main.toModel()
)

internal fun mapCodeToWeatherType(code: String?): WeatherTypeEnum {
    if (code == null) return WeatherTypeEnum.UNKNOWN
    return when {
        code.startsWith("01") -> WeatherTypeEnum.SUN
        code.startsWith("02") || code.startsWith("03") || code.startsWith("04") || code.startsWith("50") -> WeatherTypeEnum.CLOUD
        code.startsWith("09") || code.startsWith("10") -> WeatherTypeEnum.RAIN
        code.startsWith("11") -> WeatherTypeEnum.THUNDERSTORM
        code.startsWith("13") -> WeatherTypeEnum.SNOW
        else -> WeatherTypeEnum.SUN
    }
}

fun WeatherInfoVo.toModel() = WeatherInfoModel(
    temp = this.temp,
    humidity = this.humidity,
    pressure = this.pressure
)