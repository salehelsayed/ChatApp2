package com.example.cheatsignal.ui.screens.menu.syndical

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import com.example.cheatsignal.ui.screens.menu.syndical.components.*
import com.example.cheatsignal.ui.screens.menu.syndical.state.HashtagDialogState
import com.example.cheatsignal.ui.screens.menu.syndical.state.JobSkillDialogState
import com.example.cheatsignal.ui.screens.menu.syndical.state.SyndicalEvent

@Composable
fun SyndicalScreen(
    viewModel: SyndicalViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Syndical") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onEvent(SyndicalEvent.SearchQueryChanged(it)) }
            )
            
            // Tabs
            TabRow(selectedTabIndex = uiState.selectedTabIndex) {
                Tab(
                    selected = uiState.selectedTabIndex == 0,
                    onClick = { viewModel.onEvent(SyndicalEvent.TabSelected(0)) }
                ) {
                    Text("Jobs & Skills")
                }
                Tab(
                    selected = uiState.selectedTabIndex == 1,
                    onClick = { viewModel.onEvent(SyndicalEvent.TabSelected(1)) }
                ) {
                    Text("Hashtags")
                }
            }
            
            // Content
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorMessage(
                    message = uiState.error,
                    onDismiss = { viewModel.onEvent(SyndicalEvent.DismissError) }
                )
                else -> when (uiState.selectedTabIndex) {
                    0 -> JobsSkillsTab(
                        items = uiState.jobSkills,
                        onAddClick = { viewModel.onEvent(SyndicalEvent.AddJobSkillClicked) },
                        onItemClick = { viewModel.onEvent(SyndicalEvent.JobSkillClicked(it)) },
                        onDeleteClick = { viewModel.onEvent(SyndicalEvent.DeleteJobSkillClicked(it)) }
                    )
                    1 -> HashtagsTab(
                        hashtags = uiState.hashtags,
                        onAddClick = { viewModel.onEvent(SyndicalEvent.AddHashtagClicked) },
                        onItemClick = { viewModel.onEvent(SyndicalEvent.HashtagClicked(it)) },
                        onDeleteClick = { viewModel.onEvent(SyndicalEvent.DeleteHashtagClicked(it)) }
                    )
                }
            }
        }
    }
    
    // Dialogs
    uiState.jobSkillDialogState?.let { dialogState ->
        JobSkillDialog(
            state = dialogState,
            onDismiss = { viewModel.onEvent(SyndicalEvent.DismissDialog) },
            onTitleChange = { viewModel.onEvent(SyndicalEvent.JobSkillTitleChanged(it)) },
            onTypeChange = { viewModel.onEvent(SyndicalEvent.JobSkillTypeChanged(it)) },
            onDescriptionChange = { viewModel.onEvent(SyndicalEvent.JobSkillDescriptionChanged(it)) },
            onSave = { title, type, description ->
                viewModel.onEvent(SyndicalEvent.SaveJobSkill(
                    title = title,
                    type = type,
                    description = description,
                    existingId = dialogState.jobSkill?.id
                ))
            }
        )
    }
    
    uiState.hashtagDialogState?.let { dialogState ->
        HashtagDialog(
            state = dialogState,
            onDismiss = { viewModel.onEvent(SyndicalEvent.DismissDialog) },
            onTagChange = { viewModel.onEvent(SyndicalEvent.HashtagTextChanged(it)) },
            onSave = { tag ->
                viewModel.onEvent(SyndicalEvent.SaveHashtag(
                    tag = tag,
                    existingId = dialogState.hashtag?.id
                ))
            }
        )
    }
}
