package com.example.androidweather.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidweather.presentation.screen.detail.DetailScreen
import com.example.androidweather.presentation.screen.home.HomeScreen
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Detail(
        val cityName: String,
        val dateString: String,
        val lat: Double? = null,
        val lon: Double? = null
    ) : Screen
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToDetail = { cityName, dateString, lat, lon ->
                    navController.navigate(Screen.Detail(cityName, dateString, lat, lon))
                }
            )
        }

        composable<Screen.Detail> {
            DetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}