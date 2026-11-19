package com.example.androidweather.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.androidweather.domain.model.ThemeMode
import com.example.androidweather.domain.repository.SettingsRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepositoryInterface {

    private companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }

    override fun observeThemeMode(): Flow<ThemeMode> =
        dataStore.data.map { preferences ->
            preferences[THEME_MODE_KEY]
                ?.let { name -> ThemeMode.entries.find { it.name == name } }
                ?: ThemeMode.SYSTEM
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }
}