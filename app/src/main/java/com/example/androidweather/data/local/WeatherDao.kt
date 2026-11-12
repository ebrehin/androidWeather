package com.example.androidweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WeatherDao {

    // --- Données météo ---

    @Query("SELECT * FROM weather WHERE searchKey = :searchKey ORDER BY date")
    suspend fun getWeather(searchKey: String): List<WeatherEntity>

    @Query("DELETE FROM weather WHERE searchKey = :searchKey")
    suspend fun deleteWeather(searchKey: String)

    @Insert
    suspend fun insertWeather(entities: List<WeatherEntity>)

    // --- Gestion du cache ---

    @Query("SELECT * FROM cache_info WHERE searchKey = :searchKey")
    suspend fun getCacheInfo(searchKey: String): CacheInfoEntity?

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertCacheInfo(info: CacheInfoEntity)

    @Transaction
    suspend fun replaceWeather(searchKey: String, entities: List<WeatherEntity>, cachedAt: Long) {
        deleteWeather(searchKey)
        insertWeather(entities)
        insertCacheInfo(CacheInfoEntity(searchKey, cachedAt))
    }
}