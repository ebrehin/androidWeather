package com.example.androidweather.presentation.screen.detail

import com.example.androidweather.domain.model.WeatherTypeEnum

data class DetailUiState(
    val dateName: String,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentWeather: CurrentWeather? = null,
    val hourlyItems: List<HourlyItem> = emptyList()
) {
    data class CurrentWeather(
        val cityName: String,
        val temperatureText: String,
        val weatherType: WeatherTypeEnum,
        val humidityText: String?
    )

    data class HourlyItem(
        val index: Int,
        val hourText: String,
        val weatherType: WeatherTypeEnum,
        val humidityText: String,
        val tempText: String,
        val isSelected: Boolean
    )
}