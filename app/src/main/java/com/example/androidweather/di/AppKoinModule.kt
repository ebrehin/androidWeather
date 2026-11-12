package com.example.androidweather.di

import com.example.androidweather.data.remote.OpenWeatherApi
import com.example.androidweather.data.remote.createHttpClient
import com.example.androidweather.data.remote.createJson
import com.example.androidweather.data.repository.WeatherRepository
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.domain.usecase.GetWeatherByCityUseCase
import com.example.androidweather.domain.usecase.GetWeatherByLocationUseCase
import com.example.androidweather.domain.usecase.GetWeatherForNext24HoursUseCase
import com.example.androidweather.presentation.WeatherViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // Configuration API et client HTTP
    single<String>(named("weather_api_key")) { "15be47e3a59a4dbab25c31361996ac83" }
    single<HttpClientEngine> { OkHttp.create() }
    single { createJson() }
    single { createHttpClient(get(), get(), enableNetworkLogs = true) }
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    single { OpenWeatherApi(get(), get(named("weather_api_key"))) }

    // Couche Data (Repository)
    single<WeatherRepositoryInterface> { WeatherRepository(get()) }

    // Couche Domain (Use Cases)
    single { GetWeatherByCityUseCase(get()) }
    single { GetWeatherByLocationUseCase(get()) }
    single { GetWeatherForNext24HoursUseCase(get()) }

    // Couche Presentation (ViewModel)
    viewModel { 
        WeatherViewModel(
            getWeatherByCityUseCase = get(),
            getWeatherByLocationUseCase = get(),
            getWeatherForNext24HoursUseCase = get()
        ) 
    }
}
