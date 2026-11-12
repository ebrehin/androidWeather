package com.example.androidweather.di

import com.example.androidweather.data.remote.OpenWeatherApi
import com.example.androidweather.data.remote.createHttpClient
import com.example.androidweather.data.remote.createJson
import com.example.androidweather.data.repository.WeatherRepository
import com.example.androidweather.domain.repository.WeatherRepositoryInterface
import com.example.androidweather.domain.usecase.GetDailyWeatherSummariesUseCase
import com.example.androidweather.domain.usecase.GetWeatherByDateUseCase
import com.example.androidweather.domain.usecase.GetWeatherForNext24HoursUseCase
import com.example.androidweather.presentation.screen.detail.DetailViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.androidweather.presentation.screen.home.HomeViewModel
import com.example.androidweather.utils.LocationTracker
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import androidx.room.Room
import com.example.androidweather.data.local.WeatherDatabase
import org.koin.android.ext.koin.androidContext

val appModule = module {
    // Configuration API et client HTTP
    single<String>(named("weather_api_key")) { "15be47e3a59a4dbab25c31361996ac83" }
    single<HttpClientEngine> { OkHttp.create() }
    single { createJson() }
    single { createHttpClient(get(), get(), enableNetworkLogs = true) }
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    single { OpenWeatherApi(get(), get(named("weather_api_key"))) }

    // Couche Data : base locale Room (cache)
    single {
        Room.databaseBuilder(androidContext(), WeatherDatabase::class.java, "weather.db")
            .fallbackToDestructiveMigration(true) // le cache est jetable : en cas de changement de schéma, on le recrée
            .build()
    }
    single { get<WeatherDatabase>().weatherDao() }

    // Couche Data : repository
    single<WeatherRepositoryInterface> { WeatherRepository(get(), get()) }

    // Couche Utils
    single { LocationTracker(get()) }

    // Couche Domain (Use Cases)
    single { GetWeatherForNext24HoursUseCase(get()) }
    single { GetDailyWeatherSummariesUseCase(get()) }
    single { GetWeatherByDateUseCase(get()) }

    // Couche Presentation (ViewModel)
    viewModel { 
        HomeViewModel(
            getWeatherForNext24HoursUseCase = get(),
            getDailyWeatherSummariesUseCase = get(),
            locationTracker = get()
        ) 
    }

    viewModel { (handle: SavedStateHandle) ->
        DetailViewModel(
            savedStateHandle = handle,
            getWeatherByDateUseCase = get()
        )
    }
}
