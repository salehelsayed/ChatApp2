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

    fun getConversation(id: String): Flow<Conversation?> {
        return conversations.map { convos ->
            convos.find { it.id == id }
        }
    }

    fun updateConversation(conversation: Conversation) {
        conversations.update { currentConvos ->
            currentConvos.map { if (it.id == conversation.id) conversation else it }
        }
    }

    fun addMessage(conversationId: String, message: Message) {
        conversations.update { currentConvos ->
            currentConvos.map { conversation ->
                if (conversation.id == conversationId) {
                    conversation.copy(
                        lastMessage = message.content,
                        timestamp = message.timestamp,
                        messages = conversation.messages + message,
                        unreadCount = if (!conversation.isCurrentlyViewed) conversation.unreadCount + 1 else 0
                    )
                } else conversation
            }
        }
    }

    fun setConversationViewed(conversationId: String, isViewed: Boolean) {
        conversations.update { currentConvos ->
            currentConvos.map { conversation ->
                if (conversation.id == conversationId) {
                    conversation.copy(
                        isCurrentlyViewed = isViewed,
                        unreadCount = if (isViewed) 0 else conversation.unreadCount
                    )
                } else conversation
            }
        }
    }

    suspend fun handleAIResponse(conversationId: String, userMessage: String) {
        val conversation = conversations.value.find { it.id == conversationId }
        if (conversation?.isAI == true) {
            openAIService.sendMessage(userMessage).collect { aiResponse ->
                addMessage(conversationId, Message(
                    content = aiResponse,
                    isFromMe = false
                ))
            }
        }
    }

    suspend fun sendMessage(conversationId: String, content: String) {
        // Add user message
        addMessage(conversationId, Message(
            content = content,
            isFromMe = true,
            status = MessageStatus.SENT
        ))

        // If it's an AI conversation, get AI response
        handleAIResponse(conversationId, content)
    }

    fun markConversationAsRead(conversationId: String) {
        conversations.update { currentConversations ->
            currentConversations.map { conversation ->
                if (conversation.id == conversationId) {
                    conversation.copy(
                        unreadCount = 0,
                        isCurrentlyViewed = true
                    )
                } else conversation
            }
        }
    }

    fun markConversationAsNotViewed(conversationId: String) {
        conversations.update { currentConversations ->
            currentConversations.map { conversation ->
                if (conversation.id == conversationId) {
                    conversation.copy(isCurrentlyViewed = false)
                } else conversation
            }
        }
    }
}
