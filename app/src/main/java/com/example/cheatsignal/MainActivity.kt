package com.example.cheatsignal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.cheatsignal.model.Conversation
import com.example.cheatsignal.ui.screens.ChatDetailScreen
import com.example.cheatsignal.ui.screens.ConversationListScreen
import com.example.cheatsignal.ui.theme.CheatSignalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheatSignalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentConversation by remember { mutableStateOf<Conversation?>(null) }

                    if (currentConversation == null) {
                        ConversationListScreen(
                            onConversationClick = { conversation ->
                                currentConversation = conversation
                            },
                            onNewChatClick = {
                                Toast.makeText(
                                    this,
                                    "New chat clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onMenuClick = {
                                Toast.makeText(
                                    this,
                                    "Menu clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } else {
                        ChatDetailScreen(
                            conversation = currentConversation!!,
                            onBackPressed = {
                                currentConversation = null
                            }
                        )
                    }
                }
            }
        }
    }
}