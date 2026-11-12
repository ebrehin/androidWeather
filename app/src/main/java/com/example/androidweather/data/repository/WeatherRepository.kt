package com.example.androidweather.data.repository

import com.example.androidweather.data.remote.OpenWeatherApi
import com.example.androidweather.data.remote.WeatherResultVO
import com.example.androidweather.data.mapper.toModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val openWeatherApi: OpenWeatherApi,
) : WeatherRepositoryInterface {

    override fun fetchWeather(city: String): Flow<Resource<WeatherResultModel?>> =
        safeApiCall { openWeatherApi.fetchWeather(city) }

    override fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResultModel?>> =
        safeApiCall { openWeatherApi.fetchWeather(lat, lon) }

    private fun safeApiCall(apiCall: suspend () -> WeatherResultVO): Flow<Resource<WeatherResultModel?>> = flow {
        emit(Resource.Loading())
        try {
            val result = apiCall()
            val weatherResultModel = result.toModel()
            emit(Resource.Success(weatherResultModel))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}
