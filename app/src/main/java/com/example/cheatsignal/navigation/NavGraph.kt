package com.example.cheatsignal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.cheatsignal.model.Conversation
import com.example.cheatsignal.ui.screens.ChatDetailScreen
import com.example.cheatsignal.ui.screens.ConversationListScreen
import com.example.cheatsignal.ui.screens.SettingsScreen
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    conversations: List<Conversation>,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ConversationList.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.ConversationList.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = Screen.ConversationList.deepLink!! },
                // Fallback for invalid deep links
                navDeepLink { uriPattern = "cheatsignal://invalid" },
                navDeepLink { uriPattern = "cheatsignal://*" }
            )
        ) {
            ConversationListScreen(
                conversations = conversations,
                onConversationClick = { conversation ->
                    navController.navigate(Screen.ChatDetail.createRoute(conversation.id))
                },
                onNewChatClick = { /* TODO: Implement new chat */ },
                onMenuClick = { /* TODO: Implement menu */ },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = Screen.ChatDetail.deepLink!! }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            val conversation = conversations.find { it.id == chatId }
            if (conversation != null) {
                ChatDetailScreen(
                    conversation = conversation,
                    onBackPressed = { navController.navigateUp() }
                )
            } else {
                // If chat not found, navigate back to conversation list
                navController.navigate(Screen.ConversationList.route) {
                    popUpTo(Screen.ConversationList.route) { inclusive = true }
                }
            }
        }

        composable(
            route = Screen.Settings.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = Screen.Settings.deepLink!! }
            )
        ) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
