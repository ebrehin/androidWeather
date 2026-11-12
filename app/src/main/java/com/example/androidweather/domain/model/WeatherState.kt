package com.example.androidweather.domain.model

data class WeatherUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentWeather: CurrentWeatherUiState? = null,
    val hourlyForecast: HourlyForecastUiState = HourlyForecastUiState(),
    val dailyForecast: DailyForecastUiState = DailyForecastUiState()
)

data class CurrentWeatherUiState(
    val cityName: String,
    val temperatureText: String,
    val weatherType: WeatherTypeEnum,
    val humidityText: String?
)

data class HourlyForecastUiState(
    val items: List<HourlyItemUiState> = emptyList() //TODO ??
)

data class HourlyItemUiState(
    val index: Int,
    val hourText: String,
    val weatherType: WeatherTypeEnum,
    val humidityText: String,
    val tempText: String,
    val isSelected: Boolean
)

data class DailyForecastUiState(
    val items: List<DailyItemUiState> = emptyList(),
    val isLoading: Boolean = false
)

data class DailyItemUiState(
    val dayText: String,
    val weatherType: WeatherTypeEnum,
    val minTemperatureText: String,
    val maxTemperatureText: String
)