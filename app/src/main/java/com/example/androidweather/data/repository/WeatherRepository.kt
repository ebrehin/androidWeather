package com.example.androidweather.data.repository

import com.example.androidweather.data.local.WeatherDao
import com.example.androidweather.data.mapper.toEntities
import com.example.androidweather.data.mapper.toModel
import com.example.androidweather.data.remote.OpenWeatherApi
import com.example.androidweather.data.remote.WeatherResultVO
import com.example.androidweather.domain.model.WeatherResultModel
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Locale

class WeatherRepository(
    private val openWeatherApi: OpenWeatherApi,
    private val weatherDao: WeatherDao,
) : WeatherRepositoryInterface {

    private val mutex = Mutex()

    override fun fetchWeather(city: String): Flow<Resource<WeatherResultModel?>> =
        cachedApiCall(searchKey = "city:${city.trim().lowercase()}") { openWeatherApi.fetchWeather(city) }

    override fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResultModel?>> =
        // On arrondit pour que deux positions très proches partagent le même cache
        cachedApiCall(searchKey = "loc:${"%.2f".format(Locale.ROOT, lat)},${"%.2f".format(Locale.ROOT, lon)}") {
            openWeatherApi.fetchWeather(lat, lon)
        }

    private fun cachedApiCall(
        searchKey: String,
        apiCall: suspend () -> WeatherResultVO
    ): Flow<Resource<WeatherResultModel?>> = flow {
        emit(Resource.Loading())
        try {
            // Le Mutex fait attendre les appels simultanés (ex: écran Home qui charge
            // les 24h ET le résumé en même temps) : le 2e trouvera le cache rempli par le 1er.
            val weather = mutex.withLock {
                val now = System.currentTimeMillis()
                val cacheInfo = weatherDao.getCacheInfo(searchKey)
                val isCacheValid = cacheInfo != null && now - cacheInfo.cachedAt < CACHE_DURATION_MS

                if (!isCacheValid) {
                    val result = apiCall()
                    weatherDao.replaceWeather(searchKey, result.toEntities(searchKey), now)
                }
                weatherDao.getWeather(searchKey)
            }
            emit(Resource.Success(weather.toModel()))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    private companion object {
        const val CACHE_DURATION_MS = 10 * 60 * 1000L // 10 minutes
    }
}