package com.example.cheatsignal.navigation

sealed class Screen(val route: String, val deepLink: String? = null) {
    object ConversationList : Screen("conversationList", "cheatsignal://conversations")
    object ChatDetail : Screen("chat/{chatId}", "cheatsignal://chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
        fun createDeepLink(chatId: String) = "cheatsignal://chat/$chatId"
    }
    object Settings : Screen("settings", "cheatsignal://settings")
}
