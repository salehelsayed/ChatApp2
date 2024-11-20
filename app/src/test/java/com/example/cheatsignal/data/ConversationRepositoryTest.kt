package com.example.cheatsignal.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ConversationRepositoryTest {
    private lateinit var repository: ConversationRepository
    private lateinit var openAIService: OpenAIService
    
    @Before
    fun setup() {
        openAIService = mock()
        repository = ConversationRepository(openAIService)
    }
    
    @Test
    fun `markConversationAsRead should reset unread count`() = runTest {
        // Given
        val conversationId = "1" // Using the predefined conversation ID
        
        // When
        repository.markConversationAsRead(conversationId)
        
        // Then
        val updatedConversation = repository.getConversations().first()
            .find { it.id == conversationId }
        assertEquals(0, updatedConversation?.unreadCount)
        assertEquals(true, updatedConversation?.isCurrentlyViewed)
    }
    
    @Test
    fun `sending message should update conversation correctly`() = runTest {
        // Given
        val conversationId = "1"
        val messageContent = "Test message"
        whenever(openAIService.sendMessage(messageContent)).thenReturn(flowOf("AI response"))
        
        // When
        repository.sendMessage(conversationId, messageContent)
        
        // Then
        val updatedConversation = repository.getConversations().first()
            .find { it.id == conversationId }
        
        // Verify the user message was added
        val lastUserMessage = updatedConversation?.messages?.findLast { it.isFromMe }
        assertEquals(messageContent, lastUserMessage?.content)
        
        // Verify the AI response was added (since conversation 1 is an AI conversation)
        val lastAIMessage = updatedConversation?.messages?.findLast { !it.isFromMe }
        assertEquals("AI response", lastAIMessage?.content)
    }
    
    @Test
    fun `sending message to non-AI conversation should only add user message`() = runTest {
        // Given
        val conversationId = "2" // Using Bob's conversation (non-AI)
        val messageContent = "Test message"
        
        // When
        repository.sendMessage(conversationId, messageContent)
        
        // Then
        val updatedConversation = repository.getConversations().first()
            .find { it.id == conversationId }
        
        // Verify only the user message was added
        val lastMessage = updatedConversation?.messages?.last()
        assertEquals(messageContent, lastMessage?.content)
        assertEquals(true, lastMessage?.isFromMe)
    }
}
