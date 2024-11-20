package com.example.cheatsignal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.cheatsignal.model.Conversation
import com.example.cheatsignal.model.Message
import com.example.cheatsignal.model.MessageStatus
import com.example.cheatsignal.ui.components.MessageBubble
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.imePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    conversation: Conversation,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()

    // Sample messages for preview
    val messages = remember {
        listOf(
            Message(
                id = "1",
                content = "Hey there!",
                timestamp = System.currentTimeMillis() - 3600000,
                isOutgoing = false
            ),
            Message(
                id = "2",
                content = "Hi! How are you?",
                timestamp = System.currentTimeMillis() - 3500000,
                isOutgoing = true,
                status = MessageStatus.READ
            ),
            Message(
                id = "3",
                content = "I'm good, thanks! Did you see the new updates?",
                timestamp = System.currentTimeMillis() - 3400000,
                isOutgoing = false
            ),
            Message(
                id = "4",
                content = "Yes, they look amazing! 😊",
                timestamp = System.currentTimeMillis() - 3300000,
                isOutgoing = true,
                status = MessageStatus.DELIVERED
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("chatDetailScreen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Fixed TopAppBar
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 12.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {}
                        Text(conversation.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement voice call */ }) {
                        Icon(Icons.Filled.Call, contentDescription = "Voice Call")
                    }
                    IconButton(onClick = { /* TODO: Implement menu */ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                }
            )

            // Messages list with padding for input field and keyboard
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .imePadding() // This makes the Box resize with keyboard
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp), // Only for input field height
                    state = listState,
                    reverseLayout = true
                ) {
                    items(messages.reversed()) { message ->
                        MessageBubble(
                            message = message,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Input field
                Surface(
                    tonalElevation = 3.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* TODO: Implement attachments */ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Attach")
                        }

                        TextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Message") },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            trailingIcon = {
                                IconButton(onClick = { /* TODO: Implement emoji picker */ }) {
                                    Icon(Icons.Filled.Face, contentDescription = "Emoji")
                                }
                            }
                        )

                        IconButton(
                            onClick = { /* TODO: Implement send */ },
                            enabled = messageText.isNotBlank()
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "Send")
                        }
                    }
                }
            }
        }
    }
}
