package com.example.androidweather.presentation.screen.home

import com.example.androidweather.domain.model.WeatherTypeEnum

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentWeather: CurrentWeather? = null,
    val hourlyForecast: HourlyForecast = HourlyForecast(),
    val dailyForecast: DailyForecastUiState = DailyForecastUiState()
) {
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

data class DailyUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val summaries: List<Summary> = emptyList()
) {
    data class Summary(
        val dateString: String,
        val dayName: String,
        val weatherType: WeatherTypeEnum,
        val minTempText: String,
        val maxTempText: String,
    )
}

data class DailyForecastUiState(
    val items: List<DailyItemUiState> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class DailyItemUiState(
    val dayText: String,
    val weatherType: WeatherTypeEnum,
    val minTemperatureText: String,
    val maxTemperatureText: String
)