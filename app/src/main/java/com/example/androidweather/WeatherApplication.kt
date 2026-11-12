package com.example.androidweather

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication : Application() {
    companion object {
        var instance: WeatherApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@WeatherApplication)
            modules(appModule)
            modules(commonModule)
        }
    }
}