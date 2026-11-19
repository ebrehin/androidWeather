package com.example.androidweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_city ORDER BY name")
    fun getFavorites(): Flow<List<FavoriteCityEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_city WHERE name = :name)")
    suspend fun isFavorite(name: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteCityEntity)

    @Query("DELETE FROM favorite_city WHERE name = :name")
    suspend fun deleteByName(name: String)
}