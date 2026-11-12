package com.example.androidweather.domain.repository

import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepositoryInterface {
    fun fetchWeather(city: String): Flow<Resource<WeatherResultModel?>>
    fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResultModel?>>
}
