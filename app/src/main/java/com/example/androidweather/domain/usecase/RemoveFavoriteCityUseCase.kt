package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.repository.FavoriteRepositoryInterface

class RemoveFavoriteCityUseCase(private val repository: FavoriteRepositoryInterface) {
    suspend operator fun invoke(cityName: String) = repository.removeFavorite(cityName)
}