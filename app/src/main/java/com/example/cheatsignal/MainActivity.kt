package com.example.cheatsignal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cheatsignal.data.Conversation
import com.example.cheatsignal.ui.screens.*
import com.example.cheatsignal.ui.theme.CheatSignalTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModelFactory
import com.example.cheatsignal.di.SettingsModule
import com.example.cheatsignal.ui.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cheatsignal.ui.viewmodels.ConversationListViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the SettingsModule with application context
        SettingsModule.initialize(applicationContext)
        
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
    
    val conversationListViewModel: ConversationListViewModel = hiltViewModel()
    val uiState by conversationListViewModel.uiState.collectAsState()

    when (currentScreen) {
        Screen.ConversationList -> {
            ConversationListScreen(
                conversations = uiState.conversations,
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
                val chatViewModel: ChatViewModel = hiltViewModel()
                ChatDetailScreen(
                    conversation = conversation,
                    viewModel = chatViewModel,
                    onBackPressed = {
                        // Update selected conversation with latest state
                        selectedConversation = uiState.conversations.find { it.id == conversation.id }
                        currentScreen = Screen.ConversationList
                    }
                )
            }
        }
        Screen.Settings -> {
            val context = LocalContext.current
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(context)
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = {
                    currentScreen = Screen.ConversationList
                },
                modifier = Modifier
            )
        }
    }
}

sealed class Screen {
    object ConversationList : Screen()
    object ChatDetail : Screen()
    object Settings : Screen()
}