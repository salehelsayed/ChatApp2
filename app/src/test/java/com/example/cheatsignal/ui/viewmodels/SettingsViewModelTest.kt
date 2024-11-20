package com.example.cheatsignal.ui.viewmodels

import com.example.cheatsignal.data.repository.SettingsRepository
import com.example.cheatsignal.model.Theme
import com.example.cheatsignal.util.TestDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var viewModel: SettingsViewModel
    private lateinit var repository: SettingsRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        // Setup default repository behavior
        coEvery { repository.getThemePreference() } returns flowOf(Theme.SYSTEM)
        coEvery { repository.getNotificationsEnabled() } returns flowOf(true)
        
        // Create ViewModel after setting up repository
        viewModel = SettingsViewModel(repository)
    }

    @Test
    fun `initial state loads from repository`() = runTest {
        // Assert initial state matches repository values
        with(viewModel.uiState.value) {
            assertEquals(Theme.SYSTEM, currentTheme)
            assertTrue(notificationsEnabled)
            assertFalse(isLoading)
            assertNull(error)
            assertFalse(isThemeMenuExpanded)
        }

        // Verify repository was called
        coVerify { 
            repository.getThemePreference()
            repository.getNotificationsEnabled()
        }
    }

    @Test
    fun `theme and notifications are updated when repository values change`() = runTest {
        // Arrange - Setup new repository values
        coEvery { repository.getThemePreference() } returns flowOf(Theme.DARK)
        coEvery { repository.getNotificationsEnabled() } returns flowOf(false)

        // Act - Create new ViewModel to trigger loading with new values
        viewModel = SettingsViewModel(repository)

        // Assert
        with(viewModel.uiState.value) {
            assertEquals(Theme.DARK, currentTheme)
            assertFalse(notificationsEnabled)
            assertFalse(isLoading)
            assertNull(error)
        }
    }

    @Test
    fun `updateTheme updates theme in repository and state`() = runTest {
        // Arrange
        coEvery { repository.setThemePreference(any()) } just runs

        // Act
        viewModel.updateTheme(Theme.DARK)

        // Assert
        coVerify { repository.setThemePreference(Theme.DARK) }
        assertEquals(Theme.DARK, viewModel.uiState.value.currentTheme)
    }

    @Test
    fun `updateTheme handles error`() = runTest {
        // Arrange
        coEvery { repository.setThemePreference(any()) } throws IOException("Network error")

        // Act
        viewModel.updateTheme(Theme.DARK)

        // Assert
        assertTrue(viewModel.uiState.value.error is SettingsError.StorageError)
    }

    @Test
    fun `updateNotifications updates notifications in repository and state`() = runTest {
        // Arrange
        coEvery { repository.setNotificationsEnabled(any()) } just runs

        // Act
        viewModel.updateNotifications(false)

        // Assert
        coVerify { repository.setNotificationsEnabled(false) }
        assertFalse(viewModel.uiState.value.notificationsEnabled)
    }

    @Test
    fun `updateNotifications handles error`() = runTest {
        // Arrange
        coEvery { repository.setNotificationsEnabled(any()) } throws IOException("Storage error")

        // Act
        viewModel.updateNotifications(false)

        // Assert
        assertTrue(viewModel.uiState.value.error is SettingsError.StorageError)
    }

    @Test
    fun `clearError removes error from state`() = runTest {
        // Arrange - Set an error first
        coEvery { repository.setNotificationsEnabled(any()) } throws IOException("Storage error")
        viewModel.updateNotifications(false)

        // Act
        viewModel.clearError()

        // Assert
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `onThemeMenuExpandedChange updates menu state`() = runTest {
        // Act & Assert - Expand menu
        viewModel.onThemeMenuExpandedChange(true)
        assertTrue(viewModel.uiState.value.isThemeMenuExpanded)

        // Act & Assert - Collapse menu
        viewModel.onThemeMenuExpandedChange(false)
        assertFalse(viewModel.uiState.value.isThemeMenuExpanded)
    }
}
