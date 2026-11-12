package com.example.androidweather.presentation

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.model.WeatherCityModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.model.WeatherUiState
import com.example.androidweather.domain.usecase.GetWeatherByCityUseCase
import com.example.androidweather.domain.usecase.GetWeatherByLocationUseCase
import com.example.androidweather.domain.usecase.GetWeatherForNext24HoursUseCase
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherViewModelState(
    val city: WeatherCityModel? = null,
    val first: WeatherItemModel? = null,
    val items: List<WeatherItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class WeatherViewModel(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByLocationUseCase: GetWeatherByLocationUseCase,
    private val getWeatherForNext24HoursUseCase: GetWeatherForNext24HoursUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(WeatherViewModelState())
    val viewState: StateFlow<WeatherViewModelState> = _viewState.asStateFlow()

    private val _weatherState = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    fun cityChanged(cityName: String) {
        fetchWeather(cityName)
    }

    fun locationChanged(location: Location) {
        observeWeatherFlow(getWeatherByLocationUseCase(location.latitude, location.longitude))
    }

    private fun observeWeatherFlow(flow: Flow<Resource<WeatherResultModel?>>) {
        viewModelScope.launch {
            flow.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _viewState.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Success -> {
                        val data = resource.data
                        _viewState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                city = data?.city,
                                items = data?.items ?: emptyList(),
                                first = data?.items?.firstOrNull(),
                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _viewState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = resource.error?.message ?: "Une erreur inconnue est survenue."
                            )
                        }
                    }
                }
            }
        }
    }

    private fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            getWeatherForNext24HoursUseCase(cityName).collect { resource ->
                val isLoading = resource is Resource.Loading && resource.data == null
                val errorMessage = when (resource) {
                    is Resource.Error -> resource.error?.message ?: "Erreur de chargement de la météo."
                    else -> null
                }
                _weatherState.value = mapToWeatherUiState(
                    weatherResult = resource.data,
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }
}
