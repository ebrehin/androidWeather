package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.DayWeatherDetailModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import com.example.androidweather.utils.calculateDominantWeatherType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherByDateUseCase(
    private val repository: WeatherRepositoryInterface
) {
    operator fun invoke(city: String, dateString: String): Flow<Resource<DayWeatherDetailModel?>> =
        fetchAndFilterByDate(defaultCityName = city, dateString = dateString) {
            repository.fetchWeather(city)
        }

    operator fun invoke(lat: Double, lon: Double, dateString: String): Flow<Resource<DayWeatherDetailModel?>> =
        fetchAndFilterByDate(defaultCityName = "", dateString = dateString) {
            repository.fetchWeather(lat, lon)
        }

    private fun fetchAndFilterByDate(
        defaultCityName: String,
        dateString: String,
        apiCall: () -> Flow<Resource<WeatherResultModel?>>
    ): Flow<Resource<DayWeatherDetailModel?>> = flow {
        apiCall().collect { resource ->
            when (resource) {
                is Resource.Loading -> emit(Resource.Loading())
                is Resource.Success -> {
                    val result = resource.data
                    val cityName = result?.city?.name ?: defaultCityName
                    val allItems = result?.items ?: emptyList()
                    val filteredItems = allItems.filter { it.date.toLocalDate().toString() == dateString }

                    if (filteredItems.isEmpty()) {
                        emit(Resource.Error(Exception("Aucune données disponibles.")))
                    } else {
                        val minTemp = filteredItems.minOf { it.infos.temp }
                        val maxTemp = filteredItems.maxOf { it.infos.temp }
                        val weatherType = filteredItems.calculateDominantWeatherType()

                        val detailModel = DayWeatherDetailModel(
                            cityName = cityName,
                            dateString = dateString,
                            minTemp = minTemp,
                            maxTemp = maxTemp,
                            weatherType = weatherType,
                            hourlyItems = filteredItems
                        )
                        emit(Resource.Success(detailModel))
                    }
                }
                is Resource.Error -> {
                    emit(Resource.Error(resource.error ?: Exception("Erreur inconnue")))
                }
            }
        }
    }
}