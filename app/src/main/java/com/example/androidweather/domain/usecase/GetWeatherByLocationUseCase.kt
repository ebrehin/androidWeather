package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetWeatherByLocationUseCase(
    private val repository: WeatherRepositoryInterface
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherResultModel?>> {
        return repository.fetchWeather(lat, lon)
    }
}
