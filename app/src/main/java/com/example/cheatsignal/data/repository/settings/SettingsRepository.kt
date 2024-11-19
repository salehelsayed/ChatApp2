package com.example.cheatsignal.data.repository.settings

import com.example.cheatsignal.data.repository.base.BaseRepository
import com.example.cheatsignal.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository : BaseRepository {
    suspend fun getThemePreference(): Flow<Theme>
    suspend fun setThemePreference(theme: Theme)
    suspend fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
