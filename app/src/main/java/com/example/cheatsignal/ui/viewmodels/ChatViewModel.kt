package com.example.cheatsignal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatsignal.data.ConversationRepository
import com.example.cheatsignal.data.Message
import com.example.cheatsignal.data.Conversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val conversation: Conversation? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ConversationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun sendMessage(conversationId: String, content: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.sendMessage(conversationId, content)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message: ${e.message}", e)
                _uiState.update { it.copy(error = "Failed to send message: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getConversation(conversationId).collect { conversation ->
                    if (conversation != null) {
                        _uiState.update { state ->
                            state.copy(
                                messages = conversation.messages,
                                conversation = conversation,
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update { it.copy(
                            error = "Conversation not found",
                            isLoading = false
                        ) }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading messages: ${e.message}", e)
                _uiState.update { it.copy(
                    error = "Failed to load messages: ${e.message}",
                    isLoading = false
                ) }
            }
        }
    }

    fun markConversationAsRead(conversationId: String) {
        viewModelScope.launch {
            try {
                repository.markConversationAsRead(conversationId)
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error marking conversation as read: ${e.message}", e)
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
