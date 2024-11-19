package com.example.cheatsignal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cheatsignal.model.Conversation
import com.example.cheatsignal.ui.screens.*
import com.example.cheatsignal.ui.theme.CheatSignalTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheatSignalTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.ConversationList) }
    var selectedConversation by remember { mutableStateOf<Conversation?>(null) }
    
    val sampleConversations = remember {
        listOf(
            Conversation(
                id = "1",
                name = "Alice Smith",
                lastMessage = "Hey, how are you?",
                timestamp = System.currentTimeMillis() - 3600000,
                unreadCount = 2
            ),
            Conversation(
                id = "2",
                name = "Bob Johnson",
                lastMessage = "Did you see the news?",
                timestamp = System.currentTimeMillis() - 7200000
            ),
            Conversation(
                id = "3",
                name = "Carol Williams",
                lastMessage = "Meeting at 3 PM",
                timestamp = System.currentTimeMillis() - 86400000,
                unreadCount = 1
            )
        )
    }

    when (currentScreen) {
        Screen.ConversationList -> {
            ConversationListScreen(
                conversations = sampleConversations,
                onConversationClick = { conversation ->
                    selectedConversation = conversation
                    currentScreen = Screen.ChatDetail
                },
                onNewChatClick = { /* TODO: Implement new chat */ },
                onMenuClick = { /* TODO: Implement menu */ },
                onSettingsClick = {
                    currentScreen = Screen.Settings
                }
            )
        }
        Screen.ChatDetail -> {
            selectedConversation?.let { conversation ->
                ChatDetailScreen(
                    conversation = conversation,
                    onBackPressed = {
                        currentScreen = Screen.ConversationList
                    }
                )
            }
        }
        Screen.Settings -> {
            SettingsScreen(
                onNavigateBack = {
                    currentScreen = Screen.ConversationList
                }
            )
        }
    }
}

sealed class Screen {
    object ConversationList : Screen()
    object ChatDetail : Screen()
    object Settings : Screen()
}