package com.example.androidweather.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.androidweather.domain.usecase.GetWeatherByDateUseCase
import com.example.androidweather.presentation.navigation.Screen
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWeatherByDateUseCase: GetWeatherByDateUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Screen.Detail>()
    val cityName = route.cityName
    private val lat = route.lat
    private val lon = route.lon
    val dateString = route.dateString

    private val _viewState = MutableStateFlow(DetailUiState(dateName = dateString))
    val viewState: StateFlow<DetailUiState> = _viewState.asStateFlow()

    init {
        loadDetailData(cityName)
    }

    fun loadDetailData(cityName: String) {
        viewModelScope.launch {
            // Même critère de recherche que Home : GPS si on a des coordonnées, sinon la ville
            val weatherFlow = if (lat != null && lon != null) {
                getWeatherByDateUseCase(lat, lon, dateString)
            } else {
                getWeatherByDateUseCase(cityName, dateString)
            }
            weatherFlow.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _viewState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }
                    }
                    is Resource.Success -> {
                        resource.data?.let { detailModel ->
                            _viewState.value = mapToDetailUiState(
                                dateString = dateString,
                                detailModel = detailModel
                            )
                        }
                    }
                    is Resource.Error -> {
                        _viewState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.error?.message ?: "Erreur inconnue"
                            )
                        }
                    }
                }
            }
        }
    }
}