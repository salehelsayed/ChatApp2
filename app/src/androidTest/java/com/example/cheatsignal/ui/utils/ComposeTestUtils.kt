package com.example.cheatsignal.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.example.cheatsignal.ui.theme.CheatSignalTheme

/**
 * Test utilities for Compose UI tests
 */
object ComposeTestUtils {
    /**
     * Wraps the content in the app theme for testing
     */
    fun ComposeContentTestRule.setContent(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit
    ) {
        setContent {
            CheatSignalTheme(darkTheme = darkTheme) {
                content()
            }
        }
    }

    /**
     * Waits for animations and other async operations to complete
     */
    fun ComposeContentTestRule.waitForAnimations() {
        this.waitForIdle()
    }
}
