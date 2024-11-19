package com.example.cheatsignal.data.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.cheatsignal.domain.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "settings"
        )

        private object PreferencesKeys {
            val THEME = stringPreferencesKey("theme_preference")
            val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
            val VERSION = intPreferencesKey("settings_version")
        }
    }

    val themePreference: Flow<Theme> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            try {
                Theme.fromString(
                    preferences[PreferencesKeys.THEME] ?: Theme.SYSTEM.name
                )
            } catch (e: IllegalArgumentException) {
                Theme.SYSTEM
            }
        }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] ?: true
        }

    suspend fun setThemePreference(theme: Theme) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }
}
