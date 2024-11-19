package com.example.cheatsignal.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cheatsignal.ui.screens.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(
    private val context: Context
) : SettingsRepository {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_preference")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
    }

    override suspend fun getThemePreference(): Flow<ThemePreference> =
        context.dataStore.data.map { preferences ->
            try {
                ThemePreference.valueOf(
                    preferences[THEME_KEY] ?: ThemePreference.SYSTEM.name
                )
            } catch (e: IllegalArgumentException) {
                ThemePreference.SYSTEM
            }
        }

    override suspend fun setThemePreference(theme: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    override suspend fun getNotificationsEnabled(): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[NOTIFICATIONS_KEY] ?: true
        }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = enabled
        }
    }
}
