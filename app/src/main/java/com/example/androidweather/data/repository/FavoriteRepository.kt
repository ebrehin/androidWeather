package com.example.androidweather.data.repository

import com.example.androidweather.data.local.FavoriteCityEntity
import com.example.androidweather.data.local.FavoriteDao
import com.example.androidweather.data.mapper.toModel
import com.example.androidweather.domain.model.FavoriteCityModel
import com.example.androidweather.domain.repository.FavoriteRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepository(private val favoriteDao: FavoriteDao) : FavoriteRepositoryInterface {

    override fun getFavorites(): Flow<Resource<List<FavoriteCityModel>>> =
        flow {
            emit(Resource.Loading())
            try {
                favoriteDao.getFavorites().collect { entities ->
                    emit(Resource.Success(entities.map { it.toModel() }))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }

    override suspend fun isFavorite(cityName: String): Boolean = favoriteDao.isFavorite(cityName)

    override suspend fun addFavorite(cityName: String) = favoriteDao.insert(FavoriteCityEntity(name = cityName))

    override suspend fun removeFavorite(cityName: String) = favoriteDao.deleteByName(cityName)
}