package com.example.cheatsignal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatsignal.data.Conversation
import com.example.cheatsignal.data.GroupChat
import com.example.cheatsignal.data.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConversationListUiState(
    val conversations: List<Conversation> = emptyList(),
    val groupChats: List<GroupChat> = emptyList()
)

@HiltViewModel
class ConversationListViewModel @Inject constructor(
    private val repository: ConversationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConversationListUiState())
    val uiState: StateFlow<ConversationListUiState> = _uiState

    init {
        loadConversations()
        loadGroupChats()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            repository.getConversations().collect { conversations ->
                _uiState.update { it.copy(conversations = conversations) }
            }
        }
    }

    private fun loadGroupChats() {
        viewModelScope.launch {
            // TODO: Replace with actual repository call when implemented
            val mockGroupChats = listOf(
                GroupChat("1", "Family Group", null),
                GroupChat("2", "Work Team", null),
                GroupChat("3", "Friends", null)
            )
            _uiState.update { it.copy(groupChats = mockGroupChats) }
        }
    }
}
