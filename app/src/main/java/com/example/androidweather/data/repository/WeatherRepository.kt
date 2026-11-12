package com.example.androidweather.data.repository

import com.example.androidweather.data.api.OpenWeatherApi
import com.example.androidweather.data.mapper.toModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class WeatherRepository(
    private val openWeatherApi: OpenWeatherApi,
) : KoinComponent, WeatherRepositoryInterface {
    override suspend fun fetchWeather(city: String): Flow<Resource<WeatherResultModel?>> = flow {
        emit(Resource.Loading())
        try {
            val latestNews = openWeatherApi.fetchWeather(city).toModel()
            emit(Resource.Success(latestNews))
        } catch (e : Exception) {
            emit(Resource.Error(e))
        }
    }

    override suspend fun fetchWeather(
        lat: Double,
        lon: Double
    ): Flow<Resource<WeatherResultModel?>> = flow {
        emit(Resource.Loading())
        try {
            val latestNews = openWeatherApi.fetchWeather(lat, lon).toModel()
            emit(Resource.Success(latestNews))
        } catch (e : Exception) {
            emit(Resource.Error(e))
        }
    }
}
