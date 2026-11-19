package com.example.androidweather.presentation.screen.favorites

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val favoriteCities: List<String> = emptyList()
)