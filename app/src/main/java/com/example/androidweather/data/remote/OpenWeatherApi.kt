package com.example.androidweather.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.component.KoinComponent

class OpenWeatherApi(
    private val client: HttpClient,
    private val apiKey: String,
    private var baseUrl: String = "https://api.openweathermap.org/data/2.5/forecast",
) : KoinComponent {

    suspend fun fetchWeather(city: String) = client.get(baseUrl) {
        url {
            parameters.append("q", city)
            parameters.append("APPID", apiKey)
            parameters.append("units", "metric")
        }
    }.body<WeatherResultVO>()

    suspend fun fetchWeather(lat: Double, lon: Double) = client.get(baseUrl) {
        url {
            parameters.append("lat", "$lat")
            parameters.append("lon", "$lon")
            parameters.append("APPID", apiKey)
            parameters.append("units", "metric")
        }
    }.body<WeatherResultVO>()
}
