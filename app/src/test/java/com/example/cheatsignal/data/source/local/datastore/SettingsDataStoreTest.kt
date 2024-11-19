package com.example.cheatsignal.data.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.test.core.app.ApplicationProvider
import com.example.cheatsignal.domain.model.Theme
import com.example.cheatsignal.util.TestDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SettingsDataStoreTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var testDataStore: TestDataStore
    private lateinit var settingsDataStore: SettingsDataStore

    @Before
    fun setup() {
        testDataStore = TestDataStore()
        settingsDataStore = object : SettingsDataStore(testContext) {
            override val Context.dataStore: DataStore<Preferences>
                get() = testDataStore
        }
    }

    // Save Operations Tests
    @Test
    fun test_saveTheme_persistsData() = runTest {
        // Given
        val theme = Theme.DARK

        // When
        settingsDataStore.setThemePreference(theme)
        val savedTheme = settingsDataStore.themePreference.first()

        // Then
        assertThat(savedTheme).isEqualTo(theme)
    }

    @Test
    fun test_saveNotifications_persistsData() = runTest {
        // Given
        val enabled = false

        // When
        settingsDataStore.setNotificationsEnabled(enabled)
        val savedEnabled = settingsDataStore.notificationsEnabled.first()

        // Then
        assertThat(savedEnabled).isEqualTo(enabled)
    }

    @Test
    fun test_saveSettings_overwritesExistingData() = runTest {
        // Given - Save initial values
        settingsDataStore.setThemePreference(Theme.DARK)
        settingsDataStore.setNotificationsEnabled(false)

        // When - Overwrite with new values
        settingsDataStore.setThemePreference(Theme.LIGHT)
        settingsDataStore.setNotificationsEnabled(true)

        // Then - New values should be persisted
        val savedTheme = settingsDataStore.themePreference.first()
        val savedEnabled = settingsDataStore.notificationsEnabled.first()

        assertThat(savedTheme).isEqualTo(Theme.LIGHT)
        assertThat(savedEnabled).isTrue()
    }

    // Load Operations Tests
    @Test
    fun test_loadSettings_returnsDefaultsWhenEmpty() = runTest {
        // Given - Fresh DataStore
        val theme = settingsDataStore.themePreference.first()
        val notifications = settingsDataStore.notificationsEnabled.first()

        // Then - Should return defaults
        assertThat(theme).isEqualTo(Theme.SYSTEM)
        assertThat(notifications).isTrue()
    }

    @Test
    fun test_loadSettings_returnsStoredData() = runTest {
        // Given - Save some settings
        settingsDataStore.setThemePreference(Theme.DARK)
        settingsDataStore.setNotificationsEnabled(false)

        // When - Create new instance with same DataStore
        val newDataStore = object : SettingsDataStore(testContext) {
            override val Context.dataStore: DataStore<Preferences>
                get() = testDataStore
        }
        val theme = newDataStore.themePreference.first()
        val notifications = newDataStore.notificationsEnabled.first()

        // Then - Should return stored values
        assertThat(theme).isEqualTo(Theme.DARK)
        assertThat(notifications).isFalse()
    }

    @Test
    fun test_themePreference_handlesInvalidTheme() = runTest {
        // Given - Save an invalid theme string directly to preferences
        testDataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[SettingsDataStore.THEME] = "INVALID_THEME"
            }
        }

        // When - Read theme
        val theme = settingsDataStore.themePreference.first()

        // Then - Should fallback to SYSTEM
        assertThat(theme).isEqualTo(Theme.SYSTEM)
    }

    @Test
    fun test_themePreference_handlesEmptyTheme() = runTest {
        // Given - Save an empty theme string directly to preferences
        testDataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[SettingsDataStore.THEME] = ""
            }
        }

        // When - Read theme
        val theme = settingsDataStore.themePreference.first()

        // Then - Should fallback to SYSTEM
        assertThat(theme).isEqualTo(Theme.SYSTEM)
    }
}

class TestDataStore : DataStore<Preferences> {
    private val preferencesState = MutableStateFlow(emptyPreferences())

    override val data: Flow<Preferences> = preferencesState

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val newValue = transform(preferencesState.value)
        preferencesState.value = newValue
        return newValue
    }
}
