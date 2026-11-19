package com.example.androidweather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidweather.presentation.navigation.AppNavigation
import com.example.androidweather.presentation.theme.AndroidWeatherTheme
import com.example.androidweather.presentation.theme.isDark
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Le splash screen reste affiché tant que la préférence de thème n'est pas chargée
        installSplashScreen().setKeepOnScreenCondition { viewModel.viewState.value.isLoading }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.viewState.collectAsStateWithLifecycle()

            AndroidWeatherTheme(darkTheme = state.themeMode.isDark()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!state.isLoading) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}