package com.example.androidweather.presentation.screen.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.model.DailyWeatherSummaryModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.usecase.GetDailyWeatherSummariesUseCase
import com.example.androidweather.domain.usecase.GetWeatherForNext24HoursUseCase
import com.example.androidweather.utils.Resource
import com.example.androidweather.utils.LocationTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getWeatherForNext24HoursUseCase: GetWeatherForNext24HoursUseCase,
    private val getDailyWeatherSummariesUseCase: GetDailyWeatherSummariesUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _weatherState = MutableStateFlow(HomeUiState())
    val weatherState: StateFlow<HomeUiState> = _weatherState.asStateFlow()

    fun onPermissionGranted() {
        viewModelScope.launch {
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                locationChanged(location)
            } else {
                _weatherState.update { state ->
                    state.copy(
                        errorMessage = "Erreur lors de la récupération de la localisation du téléphone, veuillez réessayer plus tard."
                    )
                }
            }
        }
    }

    fun onPermissionDenied() {
        cityChanged("Brest")
    }

    fun cityChanged(cityName: String) {
        _weatherState.update { it.copy(search = HomeUiState.SearchCriteria(city = cityName)) }
        viewModelScope.launch {
            getWeatherForNext24HoursUseCase(cityName).collect { resource ->
                emitWeatherState(resource)
            }
        }
        viewModelScope.launch {
            getDailyWeatherSummariesUseCase(cityName).collect { resource ->
                emitDailySummariesState(resource)
            }
        }
    }

    fun locationChanged(location: Location) {
        _weatherState.update {
            it.copy(search = HomeUiState.SearchCriteria(lat = location.latitude, lon = location.longitude))
        }
        viewModelScope.launch {
            getWeatherForNext24HoursUseCase(
                location.latitude,
                location.longitude
            ).collect { resource ->
                emitWeatherState(resource)
            }
        }
        viewModelScope.launch {
            getDailyWeatherSummariesUseCase(
                location.latitude,
                location.longitude
            ).collect { resource ->
                emitDailySummariesState(resource)
            }
        }
    }

    private fun emitWeatherState(resource: Resource<WeatherResultModel?>) {
        when (resource) {
            is Resource.Loading -> {
                _weatherState.update { state ->
                    state.copy(isLoading = true, errorMessage = null)
                }
            }

            is Resource.Success -> {
                _weatherState.update { state ->
                    mapToHomeUiState(resource.data).copy(
                        dailyForecast = state.dailyForecast,
                        search = state.search
                    )
                }
            }

            is Resource.Error -> {
                _weatherState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = resource.error?.message
                            ?: "Erreur de chargement de la météo."
                    )
                }
            }
        }
    }

    private fun emitDailySummariesState(resource: Resource<List<DailyWeatherSummaryModel>>) {
        when (resource) {
            is Resource.Loading -> {
                _weatherState.update { state ->
                    state.copy(
                        dailyForecast = state.dailyForecast.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    )
                }
            }

            is Resource.Success -> {
                _weatherState.update { state ->
                    state.copy(
                        dailyForecast = mapToDailyForecastUiState(resource.data ?: emptyList())
                    )
                }
            }

            is Resource.Error -> {
                _weatherState.update { state ->
                    state.copy(
                        dailyForecast = state.dailyForecast.copy(
                            isLoading = false,
                            errorMessage = resource.error?.message
                                ?: "Erreur de chargement des moyennes journalières."
                        )
                    )
                }
            }
        }
    }
}
