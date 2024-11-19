package com.example.cheatsignal.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SettingsDtoTest {
    
    @Test
    fun test_defaultConstructor_setsCorrectDefaults() {
        val settings = SettingsDto()
        
        assertThat(settings.theme).isEqualTo("SYSTEM")
        assertThat(settings.notificationsEnabled).isTrue()
        assertThat(settings.messagePreviewEnabled).isTrue()
        assertThat(settings.soundEnabled).isTrue()
        assertThat(settings.vibrationEnabled).isTrue()
        assertThat(settings.lastSyncTimestamp).isEqualTo(0L)
        assertThat(settings.version).isEqualTo(1)
    }
    
    @Test
    fun test_customValues_preservedCorrectly() {
        val settings = SettingsDto(
            theme = "DARK",
            notificationsEnabled = false,
            messagePreviewEnabled = false,
            soundEnabled = false,
            vibrationEnabled = false,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        
        assertThat(settings.theme).isEqualTo("DARK")
        assertThat(settings.notificationsEnabled).isFalse()
        assertThat(settings.messagePreviewEnabled).isFalse()
        assertThat(settings.soundEnabled).isFalse()
        assertThat(settings.vibrationEnabled).isFalse()
        assertThat(settings.lastSyncTimestamp).isEqualTo(123456789L)
        assertThat(settings.version).isEqualTo(2)
    }
    
    @Test
    fun test_copyWithModifications_preservesOtherValues() {
        val original = SettingsDto(
            theme = "DARK",
            notificationsEnabled = false,
            messagePreviewEnabled = false,
            soundEnabled = false,
            vibrationEnabled = false,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        
        val modified = original.copy(theme = "LIGHT", notificationsEnabled = true)
        
        // Changed values
        assertThat(modified.theme).isEqualTo("LIGHT")
        assertThat(modified.notificationsEnabled).isTrue()
        
        // Preserved values
        assertThat(modified.messagePreviewEnabled).isEqualTo(original.messagePreviewEnabled)
        assertThat(modified.soundEnabled).isEqualTo(original.soundEnabled)
        assertThat(modified.vibrationEnabled).isEqualTo(original.vibrationEnabled)
        assertThat(modified.lastSyncTimestamp).isEqualTo(original.lastSyncTimestamp)
        assertThat(modified.version).isEqualTo(original.version)
    }
    
    @Test
    fun test_toString_containsAllFields() {
        val settings = SettingsDto(
            theme = "DARK",
            notificationsEnabled = false,
            messagePreviewEnabled = true,
            soundEnabled = false,
            vibrationEnabled = true,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        
        val string = settings.toString()
        
        assertThat(string).contains("theme=DARK")
        assertThat(string).contains("notificationsEnabled=false")
        assertThat(string).contains("messagePreviewEnabled=true")
        assertThat(string).contains("soundEnabled=false")
        assertThat(string).contains("vibrationEnabled=true")
        assertThat(string).contains("lastSyncTimestamp=123456789")
        assertThat(string).contains("version=2")
    }
    
    @Test
    fun test_equals_comparesAllFields() {
        val settings1 = SettingsDto(
            theme = "DARK",
            notificationsEnabled = false,
            messagePreviewEnabled = true,
            soundEnabled = false,
            vibrationEnabled = true,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        
        val settings2 = SettingsDto(
            theme = "DARK",
            notificationsEnabled = false,
            messagePreviewEnabled = true,
            soundEnabled = false,
            vibrationEnabled = true,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        
        val settings3 = settings1.copy(theme = "LIGHT")
        
        assertThat(settings1).isEqualTo(settings2)
        assertThat(settings1).isNotEqualTo(settings3)
        assertThat(settings1.hashCode()).isEqualTo(settings2.hashCode())
        assertThat(settings1.hashCode()).isNotEqualTo(settings3.hashCode())
    }
}
