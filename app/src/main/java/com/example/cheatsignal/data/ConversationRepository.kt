package com.example.cheatsignal.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository @Inject constructor(
    private val openAIService: OpenAIService
) {
    private val conversations = MutableStateFlow(
        listOf(
            Conversation(
                id = "1",
                name = "Alice AI",
                isAI = true,
                lastMessage = "Hi! I'm Alice AI, your friendly AI assistant. How can I help you today? 😊",
                timestamp = System.currentTimeMillis(),
                unreadCount = 0,
                isCurrentlyViewed = false,
                messages = listOf(
                    Message(
                        content = "Hi! I'm Alice AI, your friendly AI assistant. How can I help you today? 😊",
                        isFromMe = false
                    )
                )
            ),
            Conversation(
                id = "2",
                name = "Bob Johnson",
                lastMessage = "Hey, did you see the new movie?",
                timestamp = System.currentTimeMillis(),
                unreadCount = 0,
                isCurrentlyViewed = false,
                messages = listOf(
                    Message(
                        content = "Hey, did you see the new movie?",
                        isFromMe = false
                    )
                )
            ),
            Conversation(
                id = "3",
                name = "Carol Williams",
                lastMessage = "Are we still on for lunch tomorrow?",
                timestamp = System.currentTimeMillis(),
                unreadCount = 0,
                isCurrentlyViewed = false,
                messages = listOf(
                    Message(
                        content = "Are we still on for lunch tomorrow?",
                        isFromMe = false
                    )
                )
            )
        )
    )

    fun getConversations(): Flow<List<Conversation>> = conversations

    fun getConversation(id: String): Flow<Conversation?> = conversations.map { convs ->
        convs.find { it.id == id }
    }

    suspend fun sendMessage(conversationId: String, content: String) {
        val conversation = conversations.value.find { it.id == conversationId } ?: return
        val userMessage = Message(content = content, isFromMe = true)
        
        // First, add the user's message
        conversations.update { currentConversations ->
            currentConversations.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        messages = conv.messages + userMessage,
                        lastMessage = content,
                        timestamp = System.currentTimeMillis(),
                        unreadCount = 0 // Reset unread count when user sends a message
                    )
                } else {
                    conv
                }
            }
        }

        // Then, if it's an AI conversation, get and add the AI response
        if (conversation.isAI) {
            openAIService.sendMessage(content).collect { aiResponse ->
                val aiMessage = Message(content = aiResponse, isFromMe = false)
                conversations.update { currentConversations ->
                    currentConversations.map { conv ->
                        if (conv.id == conversationId) {
                            val currentConv = conv.copy(
                                messages = conv.messages + aiMessage,
                                lastMessage = aiResponse,
                                timestamp = System.currentTimeMillis()
                            )
                            // Only increment unread count if we're not currently viewing the conversation
                            if (!currentConv.isCurrentlyViewed) {
                                currentConv.copy(unreadCount = currentConv.unreadCount + 1)
                            } else {
                                currentConv
                            }
                        } else {
                            conv
                        }
                    }
                }
            }
        }
    }

    fun markConversationAsRead(conversationId: String) {
        conversations.update { currentConversations ->
            currentConversations.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        unreadCount = 0,
                        isCurrentlyViewed = true
                    )
                } else {
                    // Don't change other conversations' isCurrentlyViewed state
                    conv
                }
            }
        }
    }

    fun markConversationAsNotViewed(conversationId: String) {
        conversations.update { currentConversations ->
            currentConversations.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(isCurrentlyViewed = false)
                } else {
                    conv
                }
            }
        }
    }
}
