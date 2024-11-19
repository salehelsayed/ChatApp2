package com.example.cheatsignal.data.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.cheatsignal.domain.model.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
open class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val THEME_KEY = "theme_preference"
        private const val NOTIFICATIONS_KEY = "notifications_enabled"
        private const val VERSION_KEY = "settings_version"

        val THEME = stringPreferencesKey(THEME_KEY)
        val NOTIFICATIONS = booleanPreferencesKey(NOTIFICATIONS_KEY)
        val VERSION = intPreferencesKey(VERSION_KEY)
    }

    protected open val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "settings"
    )

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
                    preferences[THEME] ?: Theme.SYSTEM.name
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
            preferences[NOTIFICATIONS] ?: true
        }

    suspend fun setThemePreference(theme: Theme) {
        context.dataStore.edit { preferences ->
            preferences[THEME] = theme.name
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS] = enabled
        }
    }
}
