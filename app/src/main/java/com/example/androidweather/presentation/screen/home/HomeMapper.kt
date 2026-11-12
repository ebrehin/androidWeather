package com.example.androidweather.presentation.screen.home

import com.example.androidweather.domain.model.DailyWeatherSummaryModel
import com.example.androidweather.domain.model.WeatherResultModel
import java.time.format.TextStyle
import java.util.Locale

fun mapToHomeUiState(
    weatherResult: WeatherResultModel?
): HomeUiState {
    val city = weatherResult?.city
    val items = weatherResult?.items ?: emptyList()
    val currentWeatherItem = items.firstOrNull()

    val currentWeather = if (city != null && currentWeatherItem != null) {
        HomeUiState.CurrentWeather(
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
        HomeUiState.HourlyItem(
            index = index,
            hourText = "${hourStr}h",
            weatherType = item.weatherType,
            tempText = "${item.infos.temp.toInt()}°C",
            isSelected = item == currentWeatherItem,
            humidityText = "${item.infos.humidity.toInt()}%",
        )
    }

    return HomeUiState(
        currentWeather = currentWeather,
        hourlyForecast = HomeUiState.HourlyForecast(items = hourlyItems),
        isLoading = false,
        errorMessage = null
    )
}

fun mapToDailyForecastUiState(
    dailySummaries: List<DailyWeatherSummaryModel>
): DailyForecastUiState {
    val dailyItems = dailySummaries.map { summary ->
        val dayName = summary.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRENCH)
        DailyItemUiState(
            dayText = "$dayName ${summary.date.dayOfMonth}",
            weatherType = summary.weatherType,
            minTemperatureText = "Min:${summary.minTemp.toInt()}°",
            maxTemperatureText = "Max:${summary.maxTemp.toInt()}°",
        )
    }

    return DailyForecastUiState(
        items = dailyItems,
        isLoading = false,
        errorMessage = null
    )
}
