package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class GetWeatherForNext24HoursUseCase(
    private val repository: WeatherRepositoryInterface
) {
    operator fun invoke(city: String): Flow<Resource<WeatherResultModel?>> {
        return flow {
            repository.fetchWeather(city).collect { resource ->
                when (resource) {
                    is Resource.Loading -> emit(Resource.Loading())
                    is Resource.Success -> {
                        val result = resource.data
                        val filteredItems = result?.items?.let { items ->
                            val startTime = items.firstOrNull()?.date ?: LocalDateTime.now()
                            val endTime = startTime.plusHours(24)
                            items.filter { it.date in startTime..endTime }
                        }
                        emit(Resource.Success(result?.copy(items = filteredItems)))
                    }
                    is Resource.Error -> emit(Resource.Error(resource.error ?: Exception("Erreur Inconnue.")))
                }
            }
        }
    }
}