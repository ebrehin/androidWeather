package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.DailyWeatherSummaryModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import com.example.androidweather.utils.calculateDominantWeatherType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDailyWeatherSummariesUseCase(
    private val repository: WeatherRepositoryInterface
) {
    operator fun invoke(city: String): Flow<Resource<List<DailyWeatherSummaryModel>>> =
        fetchAndAggregate { repository.fetchWeather(city) }

    operator fun invoke(lat: Double, lon: Double): Flow<Resource<List<DailyWeatherSummaryModel>>> =
        fetchAndAggregate { repository.fetchWeather(lat, lon) }

    private fun fetchAndAggregate(
        apiCall: () -> Flow<Resource<WeatherResultModel?>>
    ): Flow<Resource<List<DailyWeatherSummaryModel>>> = flow {
        apiCall().collect { resource ->
            when (resource) {
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> {
                    emit(Resource.Success(aggregate(resource.data?.items.orEmpty())))
                }
                is Resource.Error -> {
                    emit(Resource.Error(resource.error ?: Exception("Erreur inconnue.")))
                }
            }
        }
    }

    private fun aggregate(items: List<WeatherItemModel>): List<DailyWeatherSummaryModel> =
        items
            .groupBy { it.date.toLocalDate() }
            .toSortedMap()
            .entries
            .take(5)
            .map { (date, dayItems) ->
                DailyWeatherSummaryModel(
                    date = date,
                    weatherType = dayItems.calculateDominantWeatherType(),
                    minTemp = dayItems.minOf { it.infos.temp },
                    maxTemp = dayItems.maxOf { it.infos.temp }
                )
            }
}