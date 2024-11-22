package com.example.cheatsignal.ui.screens.menu.syndical

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import com.example.cheatsignal.data.repository.SyndicalRepository
import com.example.cheatsignal.ui.screens.menu.syndical.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SyndicalViewModel @Inject constructor(
    private val repository: SyndicalRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SyndicalScreenState())
    val uiState: StateFlow<SyndicalScreenState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: SyndicalEvent) {
        when (event) {
            is SyndicalEvent.NavigateUp -> {} // Handled by caller
            is SyndicalEvent.TabSelected -> updateTab(event.index)
            is SyndicalEvent.SearchQueryChanged -> updateSearch(event.query)
            is SyndicalEvent.AddJobSkillClicked -> showAddJobSkillDialog()
            is SyndicalEvent.JobSkillClicked -> showEditJobSkillDialog(event.jobSkill)
            is SyndicalEvent.DeleteJobSkillClicked -> deleteJobSkill(event.jobSkill)
            is SyndicalEvent.SaveJobSkill -> saveJobSkill(event)
            is SyndicalEvent.AddHashtagClicked -> showAddHashtagDialog()
            is SyndicalEvent.HashtagClicked -> showEditHashtagDialog(event.hashtag)
            is SyndicalEvent.DeleteHashtagClicked -> deleteHashtag(event.hashtag)
            is SyndicalEvent.SaveHashtag -> saveHashtag(event)
            is SyndicalEvent.DismissDialog -> dismissDialog()
            is SyndicalEvent.JobSkillTitleChanged -> updateJobSkillTitle(event.title)
            is SyndicalEvent.JobSkillTypeChanged -> updateJobSkillType(event.type)
            is SyndicalEvent.JobSkillDescriptionChanged -> updateJobSkillDescription(event.description)
            is SyndicalEvent.HashtagTextChanged -> updateHashtagText(event.tag)
            is SyndicalEvent.DismissError -> dismissError()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getJobsAndSkills().collect { jobSkills ->
                    _uiState.update { it.copy(jobSkills = jobSkills) }
                }
                repository.getTrendingHashtags().collect { hashtags ->
                    _uiState.update { it.copy(hashtags = hashtags) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to load data") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    private fun updateSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            try {
                when (_uiState.value.selectedTabIndex) {
                    0 -> repository.searchJobsAndSkills(query).collect { jobSkills ->
                        _uiState.update { it.copy(jobSkills = jobSkills) }
                    }
                    1 -> repository.searchHashtags(query).collect { hashtags ->
                        _uiState.update { it.copy(hashtags = hashtags) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to search") }
            }
        }
    }

    private fun showAddJobSkillDialog() {
        _uiState.update {
            it.copy(jobSkillDialogState = JobSkillDialogState())
        }
    }

    private fun showEditJobSkillDialog(jobSkill: JobSkill) {
        _uiState.update {
            it.copy(
                jobSkillDialogState = JobSkillDialogState(
                    jobSkill = jobSkill,
                    title = jobSkill.title,
                    type = jobSkill.type,
                    description = jobSkill.description,
                    isEditing = true
                )
            )
        }
    }

    private fun deleteJobSkill(jobSkill: JobSkill) {
        viewModelScope.launch {
            try {
                repository.deleteJobSkill(jobSkill)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to delete job/skill") }
            }
        }
    }

    private fun saveJobSkill(event: SyndicalEvent.SaveJobSkill) {
        viewModelScope.launch {
            try {
                val jobSkill = JobSkill(
                    id = event.existingId ?: UUID.randomUUID().toString(),
                    title = event.title,
                    type = event.type,
                    description = event.description
                )
                repository.upsertJobSkill(jobSkill)
                dismissDialog()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to save job/skill") }
            }
        }
    }

    private fun showAddHashtagDialog() {
        _uiState.update {
            it.copy(hashtagDialogState = HashtagDialogState())
        }
    }

    private fun showEditHashtagDialog(hashtag: Hashtag) {
        _uiState.update {
            it.copy(
                hashtagDialogState = HashtagDialogState(
                    hashtag = hashtag,
                    tag = hashtag.tag,
                    isEditing = true
                )
            )
        }
    }

    private fun deleteHashtag(hashtag: Hashtag) {
        viewModelScope.launch {
            try {
                repository.deleteHashtag(hashtag)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to delete hashtag") }
            }
        }
    }

    private fun saveHashtag(event: SyndicalEvent.SaveHashtag) {
        viewModelScope.launch {
            try {
                val hashtag = Hashtag(
                    id = event.existingId ?: UUID.randomUUID().toString(),
                    tag = event.tag
                )
                repository.upsertHashtag(hashtag)
                dismissDialog()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to save hashtag") }
            }
        }
    }

    private fun dismissDialog() {
        _uiState.update {
            it.copy(
                jobSkillDialogState = null,
                hashtagDialogState = null
            )
        }
    }

    private fun updateJobSkillTitle(title: String) {
        _uiState.update {
            it.copy(
                jobSkillDialogState = it.jobSkillDialogState?.copy(title = title)
            )
        }
    }

    private fun updateJobSkillType(type: SkillType) {
        _uiState.update {
            it.copy(
                jobSkillDialogState = it.jobSkillDialogState?.copy(type = type)
            )
        }
    }

    private fun updateJobSkillDescription(description: String) {
        _uiState.update {
            it.copy(
                jobSkillDialogState = it.jobSkillDialogState?.copy(description = description)
            )
        }
    }

    private fun updateHashtagText(tag: String) {
        _uiState.update {
            it.copy(
                hashtagDialogState = it.hashtagDialogState?.copy(tag = tag)
            )
        }
    }

    private fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
