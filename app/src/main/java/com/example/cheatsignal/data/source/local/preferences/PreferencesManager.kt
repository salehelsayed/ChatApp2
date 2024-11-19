package com.example.cheatsignal.data.source.local.preferences

import com.example.cheatsignal.data.source.local.datastore.SettingsDataStore
import com.example.cheatsignal.domain.model.Theme
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class PreferencesManager @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {
    // Theme preferences
    val themePreference: Flow<Theme>
        get() = settingsDataStore.themePreference

    suspend fun setThemePreference(theme: Theme) {
        settingsDataStore.setThemePreference(theme)
    }

    // Notification preferences
    val notificationsEnabled: Flow<Boolean>
        get() = settingsDataStore.notificationsEnabled

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        settingsDataStore.setNotificationsEnabled(enabled)
    }

    companion object {
        // Version tracking for future migrations
        const val CURRENT_PREFERENCES_VERSION = 1
    }
}
