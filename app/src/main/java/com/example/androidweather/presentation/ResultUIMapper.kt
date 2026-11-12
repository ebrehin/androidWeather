package com.example.androidweather.presentation

import com.example.androidweather.domain.model.*

fun mapToWeatherUiState(
    weatherResult: WeatherResultModel?,
    isLoading: Boolean,
    errorMessage: String?,
): WeatherUiState {
    val city = weatherResult?.city
    val items = weatherResult?.items ?: emptyList()
    val currentWeatherItem = items.firstOrNull()


    val currentWeather = if (city != null && currentWeatherItem != null) {
        CurrentWeatherUiState(
            cityName = city.name,
            temperatureText = "${currentWeatherItem.infos.temp.toInt()}°C",
            weatherType = currentWeatherItem.weatherType,
            humidityText = "${currentWeatherItem.infos.humidity.toInt()}%"
        )
    } else {
        null
    }

    val hourlyItems = items.mapIndexed { index, item ->
        val hourStr = item.hour ?: "${item.date.hour}"
        HourlyItemUiState(
            index = index,
            hourText = "${hourStr}h",
            weatherType = item.weatherType,
            tempText = "${item.infos.temp.toInt()}°C",
            isSelected = item == currentWeatherItem,
            humidityText = "${item.infos.humidity.toInt()}%",
        )
    }

    return WeatherUiState(
        isLoading = isLoading,
        errorMessage = errorMessage,
        currentWeather = currentWeather,
        hourlyForecast = HourlyForecastUiState(items = hourlyItems)
    )
}