package com.example.cheatsignal.ui.utils

/**
 * Test tags for UI components to make them easily identifiable in tests
 */
object TestTags {
    // Common Components
    const val LOADING_SPINNER = "loading_spinner"
    const val ERROR_DIALOG = "error_dialog"
    const val ERROR_DIALOG_RETRY_BUTTON = "error_dialog_retry_button"
    const val ERROR_DIALOG_DISMISS_BUTTON = "error_dialog_dismiss_button"
    const val APP_BAR = "app_bar"
    const val APP_BAR_BACK_BUTTON = "app_bar_back_button"
    const val APP_BAR_TITLE = "app_bar_title"
    
    // Settings Components
    const val THEME_SELECTOR = "theme_selector"
    const val THEME_OPTION = "theme_option_"  // Append theme name (e.g., theme_option_light)
    const val NOTIFICATION_TOGGLE = "notification_toggle"
    const val NOTIFICATION_TOGGLE_SWITCH = "notification_toggle_switch"
}
