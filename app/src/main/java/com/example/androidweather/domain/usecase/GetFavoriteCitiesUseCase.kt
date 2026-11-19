package com.example.androidweather.domain.usecase

import com.example.androidweather.domain.model.FavoriteCityModel
import com.example.androidweather.domain.repository.FavoriteRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetFavoriteCitiesUseCase(private val repository: FavoriteRepositoryInterface) {
    operator fun invoke(): Flow<Resource<List<FavoriteCityModel>>> = repository.getFavorites()
}