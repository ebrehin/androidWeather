package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.DailyWeatherModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherForNextFiveDaysUseCase(
    private val repository: WeatherRepositoryInterface
) {
    operator fun invoke(city: String): Flow<Resource<List<DailyWeatherModel>>> =
        fetchAndAggregate { repository.fetchWeather(city) }

    operator fun invoke(lat: Double, lon: Double): Flow<Resource<List<DailyWeatherModel>>> =
        fetchAndAggregate { repository.fetchWeather(lat, lon) }

    private fun fetchAndAggregate(
        apiCall: () -> Flow<Resource<com.example.androidweather.domain.model.WeatherResultModel?>>
    ): Flow<Resource<List<DailyWeatherModel>>> = flow {
        apiCall().collect { resource ->
            when (resource) {
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> emit(Resource.Success(aggregate(resource.data?.items.orEmpty())))
                is Resource.Error -> emit(
                    Resource.Error(resource.error ?: Exception("Erreur inconnue."))
                )
            }
        }
    }

    private fun aggregate(items: List<WeatherItemModel>): List<DailyWeatherModel> =
        items
            .groupBy { it.date.toLocalDate() }
            .toSortedMap()
            .entries
            .take(5)
            .map { (date, dayItems) ->
                DailyWeatherModel(
                    date = date,
                    weatherType = dayItems.groupingBy { it.weatherType }
                        .eachCount()
                        .maxBy { it.value }
                        .key,
                    minTemperature = dayItems.minOf { it.infos.temp },
                    maxTemperature = dayItems.maxOf { it.infos.temp }
                )
            }
}