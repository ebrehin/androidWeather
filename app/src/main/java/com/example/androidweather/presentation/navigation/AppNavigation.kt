package com.example.androidweather.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidweather.presentation.screen.detail.DetailScreen
import com.example.androidweather.presentation.screen.favorites.FavoritesScreen
import com.example.androidweather.presentation.screen.home.HomeScreen
import kotlinx.serialization.Serializable
import com.example.androidweather.presentation.screen.settings.SettingsScreen

sealed interface Screen {
    @Serializable
    data class Home(val cityName: String? = null) : Screen

    @Serializable
    data object Favorites : Screen

    @Serializable
    data object Settings : Screen

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
        startDestination = Screen.Home()
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToFavorites = { navController.navigate(Screen.Favorites) },
                onNavigateToSettings = { navController.navigate(Screen.Settings) },   // nouveau
                onNavigateToDetail = { cityName, dateString, lat, lon ->
                    navController.navigate(Screen.Detail(cityName, dateString, lat, lon))
                }
            )
        }

        composable<Screen.Favorites> {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onCityClick = { cityName ->
                    navController.navigate(Screen.Home(cityName))
                }
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
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