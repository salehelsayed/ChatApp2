package com.example.cheatsignal.data.repository.settings

import android.content.Context
import com.example.cheatsignal.data.source.local.datastore.SettingsDataStore
import com.example.cheatsignal.domain.model.Theme
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    context: Context
) : SettingsRepository {
    private val dataStore = SettingsDataStore(context)

    override suspend fun getThemePreference(): Flow<Theme> = dataStore.themePreference

    override suspend fun setThemePreference(theme: Theme) {
        dataStore.setThemePreference(theme)
    }

    override suspend fun getNotificationsEnabled(): Flow<Boolean> = dataStore.notificationsEnabled

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.setNotificationsEnabled(enabled)
    }
}
