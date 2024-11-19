package com.example.cheatsignal.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SettingsTest {
    
    @Test
    fun `default constructor creates Settings with expected default values`() {
        // When
        val settings = Settings()
        
        // Then
        with(settings) {
            assertThat(theme).isEqualTo(Theme.SYSTEM)
            assertThat(notificationsEnabled).isTrue()
            assertThat(messagePreviewEnabled).isTrue()
            assertThat(soundEnabled).isTrue()
            assertThat(vibrationEnabled).isTrue()
            assertThat(lastSyncTimestamp).isEqualTo(0L)
        }
    }
    
    @Test
    fun `custom constructor creates Settings with provided values`() {
        // Given
        val customTheme = Theme.DARK
        val customNotifications = false
        val customPreview = false
        val customSound = false
        val customVibration = false
        val customTimestamp = 123456789L
        
        // When
        val settings = Settings(
            theme = customTheme,
            notificationsEnabled = customNotifications,
            messagePreviewEnabled = customPreview,
            soundEnabled = customSound,
            vibrationEnabled = customVibration,
            lastSyncTimestamp = customTimestamp
        )
        
        // Then
        with(settings) {
            assertThat(theme).isEqualTo(customTheme)
            assertThat(notificationsEnabled).isEqualTo(customNotifications)
            assertThat(messagePreviewEnabled).isEqualTo(customPreview)
            assertThat(soundEnabled).isEqualTo(customSound)
            assertThat(vibrationEnabled).isEqualTo(customVibration)
            assertThat(lastSyncTimestamp).isEqualTo(customTimestamp)
        }
    }
    
    @Test
    fun `copy creates new Settings with updated values`() {
        // Given
        val original = Settings()
        
        // When
        val updated = original.copy(
            theme = Theme.LIGHT,
            notificationsEnabled = false
        )
        
        // Then
        assertThat(updated.theme).isEqualTo(Theme.LIGHT)
        assertThat(updated.notificationsEnabled).isFalse()
        // Verify unchanged values remain the same
        assertThat(updated.messagePreviewEnabled).isEqualTo(original.messagePreviewEnabled)
        assertThat(updated.soundEnabled).isEqualTo(original.soundEnabled)
        assertThat(updated.vibrationEnabled).isEqualTo(original.vibrationEnabled)
        assertThat(updated.lastSyncTimestamp).isEqualTo(original.lastSyncTimestamp)
    }
    
    @Test
    fun `data class equals works correctly`() {
        // Given
        val settings1 = Settings(
            theme = Theme.DARK,
            notificationsEnabled = false,
            messagePreviewEnabled = true,
            soundEnabled = false,
            vibrationEnabled = true,
            lastSyncTimestamp = 123L
        )
        
        val settings2 = Settings(
            theme = Theme.DARK,
            notificationsEnabled = false,
            messagePreviewEnabled = true,
            soundEnabled = false,
            vibrationEnabled = true,
            lastSyncTimestamp = 123L
        )
        
        val different = Settings(
            theme = Theme.LIGHT,
            notificationsEnabled = true,
            messagePreviewEnabled = false,
            soundEnabled = true,
            vibrationEnabled = false,
            lastSyncTimestamp = 456L
        )
        
        // Then
        assertThat(settings1).isEqualTo(settings2)
        assertThat(settings1).isNotEqualTo(different)
    }
}
