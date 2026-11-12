package com.example.androidweather.presentation.screen.detail

import com.example.androidweather.domain.model.DayWeatherDetailModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun mapToDetailUiState(
    dateString: String,
    detailModel: DayWeatherDetailModel
): DetailUiState {
    val currentWeather = DetailUiState.CurrentWeather(
        cityName = detailModel.cityName,
        temperatureText = "Min: ${detailModel.minTemp.toInt()}°C  Max: ${detailModel.maxTemp.toInt()}°C",
        weatherType = detailModel.weatherType,
        humidityText = null
    )

    val hourlyItems = detailModel.hourlyItems.mapIndexed { index, item ->
        val hourStr = item.hour ?: "${item.date.hour}"
        DetailUiState.HourlyItem(
            index = index,
            hourText = "${hourStr}h",
            weatherType = item.weatherType,
            humidityText = "${item.infos.humidity.toInt()}%",
            tempText = "${item.infos.temp.toInt()}°C",
            isSelected = false
        )
    }

    val formattedDateName = try {
        LocalDate.parse(dateString).format(DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRENCH))
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.FRENCH) else it.toString() }
    } catch (_: Exception) {
        dateString
    }

    return DetailUiState(
        dateName = formattedDateName,
        isLoading = false,
        errorMessage = null,
        currentWeather = currentWeather,
        hourlyItems = hourlyItems
    )
}