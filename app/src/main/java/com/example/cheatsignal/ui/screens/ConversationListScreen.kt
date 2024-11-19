package com.example.cheatsignal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cheatsignal.model.Conversation
import com.example.cheatsignal.ui.components.ConversationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    conversations: List<Conversation>,
    onConversationClick: (Conversation) -> Unit,
    onNewChatClick: () -> Unit,
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Maktoub") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewChatClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Chat")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(conversations) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onConversationClick = onConversationClick
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConversationListScreenPreview() {
    MaterialTheme {
        val sampleConversations = listOf(
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
        ConversationListScreen(
            conversations = sampleConversations,
            onConversationClick = {},
            onNewChatClick = {},
            onMenuClick = {},
            onSettingsClick = {}
        )
    }
}
