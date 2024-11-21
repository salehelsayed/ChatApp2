package com.example.cheatsignal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.cheatsignal.data.Conversation
import com.example.cheatsignal.ui.screens.*
import com.example.cheatsignal.ui.screens.menu.MenuScreen
import com.example.cheatsignal.ui.screens.menu.communal.CommunalFormScreen
import com.example.cheatsignal.ui.theme.CheatSignalTheme
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModelFactory
import com.example.cheatsignal.di.SettingsModule
import com.example.cheatsignal.ui.viewmodels.ChatViewModel
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
    val uiState by conversationListViewModel.uiState.collectAsStateWithLifecycle()

    when (currentScreen) {
        Screen.ConversationList -> {
            ConversationListScreen(
                conversations = uiState.conversations,
                groupChats = uiState.groupChats,
                onConversationClick = { conversation ->
                    selectedConversation = conversation
                    currentScreen = Screen.ChatDetail
                },
                onGroupChatClick = { /* TODO: Implement group chat navigation */ },
                onNewChatClick = { /* TODO: Implement new chat */ },
                onMenuClick = { currentScreen = Screen.Menu },
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
        Screen.Menu -> {
            MenuScreen(
                onNavigateBack = {
                    currentScreen = Screen.ConversationList
                },
                onNavigateToCommunalForm = {
                    currentScreen = Screen.CommunalForm
                }
            )
        }
        Screen.CommunalForm -> {
            CommunalFormScreen(
                onNavigateBack = {
                    currentScreen = Screen.Menu
                }
            )
        }
    }
}

sealed class Screen {
    object ConversationList : Screen()
    object ChatDetail : Screen()
    object Settings : Screen()
    object Menu : Screen()
    object CommunalForm : Screen()
}