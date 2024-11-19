package com.example.cheatsignal.data.mapper

import com.example.cheatsignal.data.model.SettingsDto
import com.example.cheatsignal.domain.model.Settings
import com.example.cheatsignal.domain.model.Theme
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SettingsMapperTest {
    private lateinit var settingsMapper: SettingsMapper

    @Before
    fun setup() {
        settingsMapper = SettingsMapper()
    }

    // Domain to DTO Tests
    @Test
    fun test_toDto_withDefaultSettings() {
        val settings = Settings()
        val dto = settingsMapper.toDto(settings)

        assertThat(dto.theme).isEqualTo("SYSTEM")
        assertThat(dto.notificationsEnabled).isTrue()
        assertThat(dto.messagePreviewEnabled).isTrue()
        assertThat(dto.soundEnabled).isTrue()
        assertThat(dto.vibrationEnabled).isTrue()
        assertThat(dto.lastSyncTimestamp).isEqualTo(0L)
        assertThat(dto.version).isEqualTo(1)
    }

    @Test
    fun test_toDto_withCustomSettings() {
        val settings = Settings(
            theme = Theme.DARK,
            notificationsEnabled = false,
            messagePreviewEnabled = false,
            soundEnabled = false,
            vibrationEnabled = false,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        val dto = settingsMapper.toDto(settings)

        assertThat(dto.theme).isEqualTo("DARK")
        assertThat(dto.notificationsEnabled).isFalse()
        assertThat(dto.messagePreviewEnabled).isFalse()
        assertThat(dto.soundEnabled).isFalse()
        assertThat(dto.vibrationEnabled).isFalse()
        assertThat(dto.lastSyncTimestamp).isEqualTo(123456789L)
        assertThat(dto.version).isEqualTo(2)
    }

    // DTO to Domain Tests
    @Test
    fun test_toDomain_withValidTheme() {
        val dto = SettingsDto(theme = "DARK")
        val settings = settingsMapper.toDomain(dto)

        assertThat(settings.theme).isEqualTo(Theme.DARK)
    }

    @Test
    fun test_toDomain_withInvalidTheme() {
        val dto = SettingsDto(theme = "INVALID_THEME")
        val settings = settingsMapper.toDomain(dto)

        assertThat(settings.theme).isEqualTo(Theme.SYSTEM)
    }

    @Test
    fun test_toDomain_withEmptyTheme() {
        val dto = SettingsDto(theme = "")
        val settings = settingsMapper.toDomain(dto)

        assertThat(settings.theme).isEqualTo(Theme.SYSTEM)
    }

    @Test
    fun test_toDomain_preservesAllSettings() {
        val dto = SettingsDto(
            theme = "LIGHT",
            notificationsEnabled = false,
            messagePreviewEnabled = false,
            soundEnabled = false,
            vibrationEnabled = false,
            lastSyncTimestamp = 123456789L,
            version = 2
        )
        val settings = settingsMapper.toDomain(dto)

        assertThat(settings.theme).isEqualTo(Theme.LIGHT)
        assertThat(settings.notificationsEnabled).isFalse()
        assertThat(settings.messagePreviewEnabled).isFalse()
        assertThat(settings.soundEnabled).isFalse()
        assertThat(settings.vibrationEnabled).isFalse()
        assertThat(settings.lastSyncTimestamp).isEqualTo(123456789L)
        assertThat(settings.version).isEqualTo(2)
    }

    // Round Trip Tests
    @Test
    fun test_roundTripConversion_preservesValues() {
        val original = Settings(
            theme = Theme.DARK,
            notificationsEnabled = false,
            messagePreviewEnabled = false,
            soundEnabled = false,
            vibrationEnabled = false,
            lastSyncTimestamp = 123456789L,
            version = 2
        )

        val dto = settingsMapper.toDto(original)
        val roundTripped = settingsMapper.toDomain(dto)

        assertThat(roundTripped).isEqualTo(original)
    }

    @Test
    fun test_roundTripConversion_withInvalidTheme() {
        val dto = SettingsDto(theme = "INVALID")
        val settings = settingsMapper.toDomain(dto)
        val roundTrippedDto = settingsMapper.toDto(settings)

        assertThat(roundTrippedDto.theme).isEqualTo("SYSTEM")
    }

    // Edge Cases
    @Test
    fun test_conversion_handlesAllThemeValues() {
        Theme.values().forEach { theme ->
            val settings = Settings(theme = theme)
            val dto = settingsMapper.toDto(settings)
            val roundTripped = settingsMapper.toDomain(dto)

            assertThat(roundTripped.theme).isEqualTo(theme)
        }
    }

    @Test
    fun test_conversion_handlesExtremeValues() {
        val settings = Settings(
            theme = Theme.SYSTEM,
            notificationsEnabled = true,
            messagePreviewEnabled = true,
            soundEnabled = true,
            vibrationEnabled = true,
            lastSyncTimestamp = Long.MAX_VALUE,
            version = Int.MAX_VALUE
        )

        val dto = settingsMapper.toDto(settings)
        val roundTripped = settingsMapper.toDomain(dto)

        assertThat(roundTripped).isEqualTo(settings)
    }
}
