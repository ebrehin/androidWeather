package com.example.androidweather.presentation

import com.example.androidweather.domain.model.*
import java.time.format.DateTimeFormatter
import java.util.Locale

fun mapToWeatherUiState(
    weatherResult: WeatherResultModel?,
    isLoading: Boolean,
    errorMessage: String?,
    dailyItems: List<DailyItemUiState> = emptyList(),
    dailyLoading: Boolean = false,
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
        hourlyForecast = HourlyForecastUiState(items = hourlyItems),
        dailyForecast = DailyForecastUiState(items = dailyItems, isLoading = dailyLoading)
    )
}

fun mapToDailyItems(items: List<com.example.androidweather.domain.model.DailyWeatherModel>): List<DailyItemUiState> {
    val formatter = DateTimeFormatter.ofPattern("EEEE d MMM", Locale.FRENCH)
    return items.map { item ->
        DailyItemUiState(
            dayText = item.date.format(formatter),
            weatherType = item.weatherType,
            minTemperatureText = "Min ${item.minTemperature.toInt()}°C",
            maxTemperatureText = "Max ${item.maxTemperature.toInt()}°C"
        )
    }
}