package com.example.cheatsignal.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.cheatsignal.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings",
    produceMigrations = { context ->
        listOf(SettingsMigration(context))
    }
)

private object PreferencesKeys {
    val THEME = stringPreferencesKey("theme_preference")
    val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
    val VERSION = intPreferencesKey("settings_version")
}

class SettingsRepositoryImpl(
    private val context: Context
) : SettingsRepository {

    override suspend fun getThemePreference(): Flow<Theme> =
        context.dataStore.data
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

    override suspend fun setThemePreference(theme: Theme) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }

    override suspend fun getNotificationsEnabled(): Flow<Boolean> =
        context.dataStore.data
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

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }
}

class SettingsMigration(private val context: Context) : DataMigration<Preferences> {
    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        val currentVersion = currentData[PreferencesKeys.VERSION] ?: 0
        return currentVersion < 1
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val currentVersion = currentData[PreferencesKeys.VERSION] ?: 0
        val mutablePreferences = currentData.toMutablePreferences()

        if (currentVersion < 1) {
            migrateV0ToV1(mutablePreferences)
        }

        return mutablePreferences.toPreferences()
    }

    override suspend fun cleanUp() {}

    private fun migrateV0ToV1(preferences: MutablePreferences) {
        // Set default values if not present
        if (!preferences.contains(PreferencesKeys.THEME)) {
            preferences[PreferencesKeys.THEME] = Theme.SYSTEM.name
        }
        if (!preferences.contains(PreferencesKeys.NOTIFICATIONS)) {
            preferences[PreferencesKeys.NOTIFICATIONS] = true
        }
        preferences[PreferencesKeys.VERSION] = 1
    }
}
