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
import com.example.androidweather.data.repository.FavoriteRepository
import com.example.androidweather.domain.repository.FavoriteRepositoryInterface
import com.example.androidweather.domain.usecase.AddFavoriteCityUseCase
import com.example.androidweather.domain.usecase.GetFavoriteCitiesUseCase
import com.example.androidweather.domain.usecase.IsCityFavoriteUseCase
import com.example.androidweather.domain.usecase.RemoveFavoriteCityUseCase
import com.example.androidweather.presentation.screen.favorites.FavoritesViewModel
import com.example.androidweather.data.local.settingsDataStore
import com.example.androidweather.data.repository.SettingsRepository
import com.example.androidweather.domain.repository.SettingsRepositoryInterface
import com.example.androidweather.domain.usecase.ObserveThemeModeUseCase
import com.example.androidweather.domain.usecase.SetThemeModeUseCase
import com.example.androidweather.presentation.MainViewModel
import com.example.androidweather.presentation.screen.settings.SettingsViewModel

val appModule = module {
    // Configuration API et client HTTP
    single<String>(named("weather_api_key")) { "15be47e3a59a4dbab25c31361996ac83" }
    single<HttpClientEngine> { OkHttp.create() }
    single { createJson() }
    single { createHttpClient(get(), get(), enableNetworkLogs = true) }
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    single { OpenWeatherApi(get(), get(named("weather_api_key"))) }

    // Couche Data : base locale Room
    single { get<WeatherDatabase>().weatherDao() }
    single { get<WeatherDatabase>().favoriteDao() }

    // Couche Data : base locale Room (cache)
    single {
        Room.databaseBuilder(androidContext(), WeatherDatabase::class.java, "weather.db")
            .fallbackToDestructiveMigration(true) // le cache est jetable : en cas de changement de schéma, on le recrée
            .build()
    }
    single { get<WeatherDatabase>().weatherDao() }

    // Couche Data : préférences utilisateur (DataStore)
    single { androidContext().settingsDataStore }

    // Couche Data : repository
    single<WeatherRepositoryInterface> { WeatherRepository(get(), get()) }
    single<FavoriteRepositoryInterface> { FavoriteRepository(get()) }
    single<SettingsRepositoryInterface> { SettingsRepository(get()) }

    // Couche Utils
    single { LocationTracker(get()) }

    // Couche Domain (Use Cases)
    single { GetWeatherForNext24HoursUseCase(get()) }
    single { GetDailyWeatherSummariesUseCase(get()) }
    single { GetWeatherByDateUseCase(get()) }
    single { GetFavoriteCitiesUseCase(get()) }
    single { AddFavoriteCityUseCase(get()) }
    single { RemoveFavoriteCityUseCase(get()) }
    single { IsCityFavoriteUseCase(get()) }
    single { ObserveThemeModeUseCase(get()) }
    single { SetThemeModeUseCase(get()) }

    // Couche Presentation (ViewModel)
    viewModel { (handle: SavedStateHandle) ->
        HomeViewModel(
            savedStateHandle = handle,
            getWeatherForNext24HoursUseCase = get(),
            getDailyWeatherSummariesUseCase = get(),
            locationTracker = get(),
            addFavoriteCityUseCase = get(),
            isCityFavoriteUseCase = get(),
            removeFavoriteCityUseCase = get()
        )
    }

    viewModel { (handle: SavedStateHandle) ->
        DetailViewModel(
            savedStateHandle = handle,
            getWeatherByDateUseCase = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            getFavoriteCitiesUseCase = get(),
            removeFavoriteCityUseCase = get()
        )
    }

    viewModel {
        MainViewModel(
            observeThemeModeUseCase = get()
        )
    }

    viewModel {
        SettingsViewModel(
            observeThemeModeUseCase = get(),
            setThemeModeUseCase = get()
        )
    }
}