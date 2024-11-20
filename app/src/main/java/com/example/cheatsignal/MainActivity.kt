package com.example.cheatsignal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.cheatsignal.model.Conversation
import com.example.cheatsignal.navigation.NavGraph
import com.example.cheatsignal.ui.theme.CheatSignalTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModel
import com.example.cheatsignal.ui.viewmodels.SettingsViewModelFactory
import com.example.cheatsignal.di.SettingsModule

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
    val navController = rememberNavController()
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )
    
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

    NavGraph(
        navController = navController,
        conversations = sampleConversations,
        settingsViewModel = settingsViewModel,
        modifier = modifier
    )
}