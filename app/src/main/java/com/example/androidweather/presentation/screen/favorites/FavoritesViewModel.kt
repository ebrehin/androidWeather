package com.example.androidweather.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidweather.domain.usecase.GetFavoriteCitiesUseCase
import com.example.androidweather.domain.usecase.RemoveFavoriteCityUseCase
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val removeFavoriteCityUseCase: RemoveFavoriteCityUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(FavoritesUiState())
    val viewState: StateFlow<FavoritesUiState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCitiesUseCase()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _viewState.update {
                            it.copy(isLoading = true, errorMessage = null)
                        }

                        is Resource.Success -> _viewState.value = FavoritesUiState(
                            isLoading = false,
                            favoriteCities = resource.data.orEmpty().map { it.name }
                        )

                        is Resource.Error -> _viewState.update {
                            it.copy(isLoading = false, errorMessage = resource.error?.message ?: "Erreur inconnue")
                        }
                    }
                }
        }
    }

    fun removeFavorite(cityName: String) {
        viewModelScope.launch {
            removeFavoriteCityUseCase(cityName)
        }
    }
}