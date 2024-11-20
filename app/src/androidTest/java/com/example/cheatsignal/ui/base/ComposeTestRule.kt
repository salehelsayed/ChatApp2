package com.example.cheatsignal.ui.base

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

/**
 * Base class for Compose UI tests providing common test rule and utilities
 */
open class ComposeTestRule {
    @get:Rule
    val composeTestRule = createComposeRule()
}
