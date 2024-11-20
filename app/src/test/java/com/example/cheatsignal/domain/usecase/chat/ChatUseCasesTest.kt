package com.example.cheatsignal.domain.usecase.chat

import com.example.cheatsignal.data.Conversation
import com.example.cheatsignal.data.ConversationRepository
import com.example.cheatsignal.data.Message
import com.example.cheatsignal.data.OpenAIService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.util.*

@ExperimentalCoroutinesApi
class ChatUseCasesTest {
    private lateinit var getConversationsUseCase: GetConversationsUseCase
    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var markAsReadUseCase: MarkAsReadUseCase
    private lateinit var getAIResponseUseCase: GetAIResponseUseCase
    
    private val mockRepository: ConversationRepository = mock()
    private val mockOpenAIService: OpenAIService = mock()
    
    @Before
    fun setup() {
        getConversationsUseCase = GetConversationsUseCase(mockRepository)
        sendMessageUseCase = SendMessageUseCase(mockRepository)
        markAsReadUseCase = MarkAsReadUseCase(mockRepository)
        getAIResponseUseCase = GetAIResponseUseCase(mockOpenAIService, mockRepository)
    }
    
    @Test
    fun `GetConversationsUseCase should emit updates from repository`() = runTest {
        // Given
        val conversations = listOf(
            Conversation(
                id = "1",
                name = "Test Chat",
                lastMessage = "Hello",
                timestamp = System.currentTimeMillis(),
                unreadCount = 0
            )
        )
        whenever(mockRepository.getConversations()).thenReturn(flowOf(conversations))
        
        // When
        val result = getConversationsUseCase().first()
        
        // Then
        assertEquals(conversations, result)
    }
    
    @Test
    fun `SendMessageUseCase should add message to repository`() = runTest {
        // Given
        val conversationId = "1"
        val message = "Hello"
        
        // When
        sendMessageUseCase(SendMessageUseCase.Params(conversationId, message))
        
        // Then
        verify(mockRepository).addMessage(eq(conversationId), any())
    }
    
    @Test
    fun `MarkAsReadUseCase should mark conversation as read`() = runTest {
        // Given
        val conversationId = "1"
        
        // When
        markAsReadUseCase(MarkAsReadUseCase.Params(conversationId))
        
        // Then
        verify(mockRepository).markConversationAsRead(conversationId)
    }
    
    @Test
    fun `GetAIResponseUseCase should get response and add to conversation`() = runTest {
        // Given
        val conversationId = "1"
        val userMessage = "Hello"
        val aiResponse = "Hi there!"
        
        whenever(mockOpenAIService.getAIResponse(any())).thenReturn(aiResponse)
        
        // When
        getAIResponseUseCase(GetAIResponseUseCase.Params(conversationId, userMessage))
        
        // Then
        verify(mockOpenAIService).getAIResponse(userMessage)
        verify(mockRepository).addMessage(eq(conversationId), any())
    }
    
    @Test
    fun `GetAIResponseUseCase should handle error from OpenAI service`() = runTest {
        // Given
        val conversationId = "1"
        val userMessage = "Hello"
        
        whenever(mockOpenAIService.getAIResponse(any())).thenThrow(RuntimeException("API Error"))
        
        var exceptionThrown = false
        
        try {
            // When
            getAIResponseUseCase(GetAIResponseUseCase.Params(conversationId, userMessage))
        } catch (e: Exception) {
            // Then
            exceptionThrown = true
        }
        
        assertEquals(true, exceptionThrown)
    }
    
    @Test
    fun `SendMessageUseCase should update conversation timestamp`() = runTest {
        // Given
        val conversationId = "1"
        val message = "Hello"
        val beforeTime = System.currentTimeMillis()
        
        // When
        sendMessageUseCase(SendMessageUseCase.Params(conversationId, message))
        
        // Then
        verify(mockRepository).addMessage(eq(conversationId), argThat { 
            timestamp >= beforeTime
        })
    }
}
