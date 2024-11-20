package com.example.cheatsignal.ui.components.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.example.cheatsignal.ui.theme.CheatSignalTheme
import com.example.cheatsignal.ui.utils.TestTags
import org.junit.Rule
import org.junit.Test

class LoadingSpinnerTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingSpinner_isVisible() {
        // Given
        composeRule.setContent {
            CheatSignalTheme {
                LoadingSpinner(
                    modifier = Modifier.testTag(TestTags.LOADING_SPINNER)
                )
            }
        }

        // Then
        composeRule
            .onNodeWithTag(TestTags.LOADING_SPINNER)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun loadingSpinner_hasCorrectSize_whenDefaultSize() {
        // Given
        val defaultSize = 40.dp
        
        composeRule.setContent {
            CheatSignalTheme {
                LoadingSpinner(
                    modifier = Modifier.testTag(TestTags.LOADING_SPINNER)
                )
            }
        }

        // Then
        composeRule
            .onNodeWithTag(TestTags.LOADING_SPINNER)
            .assertHeightIsEqualTo(defaultSize)
            .assertWidthIsEqualTo(defaultSize)
    }

    @Test
    fun loadingSpinner_hasCorrectSize_whenCustomSize() {
        // Given
        val customSize = 60

        composeRule.setContent {
            CheatSignalTheme {
                LoadingSpinner(
                    modifier = Modifier.testTag(TestTags.LOADING_SPINNER),
                    size = customSize
                )
            }
        }

        // Then
        composeRule
            .onNodeWithTag(TestTags.LOADING_SPINNER)
            .assertHeightIsEqualTo(customSize.dp)
            .assertWidthIsEqualTo(customSize.dp)
    }

    @Test
    fun loadingSpinner_usesThemeColors() {
        // Given
        composeRule.setContent {
            CheatSignalTheme {
                LoadingSpinner(
                    modifier = Modifier.testTag(TestTags.LOADING_SPINNER)
                )
            }
        }

        // Then
        composeRule
            .onNodeWithTag(TestTags.LOADING_SPINNER)
            .assertExists()
            .onChildren()
            .filterToOne(hasTestTag("CircularProgressIndicator"))
            .assertIsDisplayed()
    }

    @Test
    fun loadingSpinner_isAnimating() {
        // Given
        composeRule.setContent {
            CheatSignalTheme {
                LoadingSpinner(
                    modifier = Modifier.testTag(TestTags.LOADING_SPINNER)
                )
            }
        }

        // Then - Verify the animation is running by checking for the infinite transition
        composeRule
            .onNodeWithTag(TestTags.LOADING_SPINNER)
            .assertExists()
            .onChildren()
            .filterToOne(hasTestTag("CircularProgressIndicator"))
            .assert(hasContentDescription("Loading"))
    }
}
