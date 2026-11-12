package com.example.androidweather.presentation.screen.home

import com.example.androidweather.domain.model.WeatherTypeEnum

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentWeather: CurrentWeather? = null,
    val hourlyForecast: HourlyForecast = HourlyForecast(),
    val dailyForecast: DailyForecastUiState = DailyForecastUiState(),
    val search: SearchCriteria = SearchCriteria()
) {
    data class SearchCriteria(
        val city: String? = null,
        val lat: Double? = null,
        val lon: Double? = null
    )

    data class CurrentWeather(
        val cityName: String,
        val temperatureText: String,
        val weatherType: WeatherTypeEnum,
        val humidityText: String?
    )

    data class HourlyForecast(
        val items: List<HourlyItem> = emptyList()
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

data class DailyForecastUiState(
    val items: List<DailyItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class DailyItemUiState(
    val dateString: String,
    val dayText: String,
    val weatherType: WeatherTypeEnum,
    val minTemperatureText: String,
    val maxTemperatureText: String
)