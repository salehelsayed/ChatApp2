package com.example.cheatsignal.data.mapper

import com.example.cheatsignal.data.model.SettingsDto
import com.example.cheatsignal.domain.model.Settings
import com.example.cheatsignal.domain.model.Theme
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SettingsMapper @Inject constructor() {
    fun toDto(settings: Settings): SettingsDto {
        return SettingsDto(
            theme = settings.theme.name,
            notificationsEnabled = settings.notificationsEnabled,
            messagePreviewEnabled = settings.messagePreviewEnabled,
            soundEnabled = settings.soundEnabled,
            vibrationEnabled = settings.vibrationEnabled,
            lastSyncTimestamp = settings.lastSyncTimestamp,
            version = settings.version
        )
    }

    fun toDomain(dto: SettingsDto): Settings {
        return Settings(
            theme = try {
                Theme.fromString(dto.theme)
            } catch (e: IllegalArgumentException) {
                Theme.SYSTEM
            },
            notificationsEnabled = dto.notificationsEnabled,
            messagePreviewEnabled = dto.messagePreviewEnabled,
            soundEnabled = dto.soundEnabled,
            vibrationEnabled = dto.vibrationEnabled,
            lastSyncTimestamp = dto.lastSyncTimestamp,
            version = dto.version
        )
    }
}
