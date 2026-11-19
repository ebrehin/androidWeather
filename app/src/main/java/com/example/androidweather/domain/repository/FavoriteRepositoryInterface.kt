package com.example.androidweather.domain.repository

import com.example.androidweather.domain.model.FavoriteCityModel
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepositoryInterface {
    fun getFavorites(): Flow<Resource<List<FavoriteCityModel>>>
    suspend fun isFavorite(cityName: String): Boolean
    suspend fun addFavorite(cityName: String)
    suspend fun removeFavorite(cityName: String)
}