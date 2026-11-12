package com.example.androidweather.presentation

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.model.WeatherUiState
import com.example.androidweather.domain.usecase.GetWeatherForNext24HoursUseCase
import com.example.androidweather.domain.usecase.GetWeatherForNextFiveDaysUseCase
import com.example.androidweather.utils.LocationTracker
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherForNext24HoursUseCase: GetWeatherForNext24HoursUseCase,
    private val getWeatherForNextFiveDaysUseCase: GetWeatherForNextFiveDaysUseCase,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _weatherState = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

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
        viewModelScope.launch {
            launch {
                getWeatherForNextFiveDaysUseCase(cityName).collect { resource ->
                    emitDailyState(resource)
                }
            }
            getWeatherForNext24HoursUseCase(cityName).collect { resource ->
                emitState(resource)
            }
        }
    }

    fun locationChanged(location: Location) {
        viewModelScope.launch {
            launch {
                getWeatherForNextFiveDaysUseCase(location.latitude, location.longitude).collect { resource ->
                    emitDailyState(resource)
                }
            }
            getWeatherForNext24HoursUseCase(location.latitude, location.longitude).collect { resource ->
                emitState(resource)
            }
        }
    }

    private fun emitState(resource: Resource<WeatherResultModel?>) {
        val isLoading = resource is Resource.Loading && resource.data == null
        val errorMessage = when (resource) {
            is Resource.Error -> resource.error?.message ?: "Erreur de chargement de la météo."
            else -> null
        }
        val mappedState = mapToWeatherUiState(
            weatherResult = resource.data,
            isLoading = isLoading,
            errorMessage = errorMessage
        )
        _weatherState.update { state ->
            mappedState.copy(dailyForecast = state.dailyForecast)
        }
    }

    private fun emitDailyState(resource: Resource<List<com.example.androidweather.domain.model.DailyWeatherModel>>) {
        _weatherState.update { state ->
            state.copy(
                dailyForecast = state.dailyForecast.copy(
                    items = when (resource) {
                        is Resource.Success -> mapToDailyItems(resource.data ?: emptyList())
                        else -> state.dailyForecast.items
                    },
                    isLoading = resource is Resource.Loading
                )
            )
        }
    }
}