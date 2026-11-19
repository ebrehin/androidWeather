package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.repository.FavoriteRepositoryInterface

class IsCityFavoriteUseCase(private val repository: FavoriteRepositoryInterface) {
    suspend operator fun invoke(cityName: String): Boolean = repository.isFavorite(cityName)
}