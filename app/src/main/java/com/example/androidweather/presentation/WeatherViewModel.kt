package com.example.androidweather.presentation

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.model.WeatherCityModel
import com.example.androidweather.domain.model.WeatherItemModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherViewModelState(
    var city: WeatherCityModel? = null,
    var first: WeatherItemModel? = null,
    var items: List<WeatherItemModel> = emptyList(),
    var isLoading: Boolean = false,
)

class WeatherViewModel(private val repository: WeatherRepositoryInterface) :
    ViewModel() {

    private val _viewState = MutableStateFlow<WeatherViewModelState>(WeatherViewModelState())
    var viewState = _viewState.asStateFlow()

    fun cityChanged(value: String) = fetchWeather(value)

    fun locationChanged(value: Location) = fetchWeather(value)

    private fun fetchWeather(location: Location) = fetchWithFlow(null, location)

    private fun fetchWeather(city: String) = fetchWithFlow(city)

    private fun fetchWithFlow(city: String? = null, location: Location? = null) {
        viewModelScope.launch {
            if (city != null) {
                repository.fetchWeather(city).collect { resource ->
                    _viewState.update{
                        it.copy(city = resource.data?.city, items = resource.data?.items ?: emptyList())
                    }
                }
            }
            else {
                val flow = repository.fetchWeather(location!!.latitude, location.longitude).collect { resource ->
                    _viewState.update{
                        it.copy(city = resource.data?.city, items = resource.data?.items ?: emptyList())
                    }
                }
            }
        }
    }
}