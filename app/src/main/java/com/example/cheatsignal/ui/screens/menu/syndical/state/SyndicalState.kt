package com.example.cheatsignal.ui.screens.menu.syndical.state

import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType

data class SyndicalScreenState(
    val selectedTab: Int = 0, // 0 for Jobs & Skills, 1 for Hashtags
    val searchQuery: String = "",
    val jobsAndSkills: List<JobSkill> = emptyList(),
    val hashtags: List<Hashtag> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false,
    val editingItem: EditingItem? = null,
    val jobSkillDialogState: JobSkillDialogState? = null,
    val hashtagDialogState: HashtagDialogState? = null
)

sealed class EditingItem {
    data class JobSkillItem(val jobSkill: JobSkill) : EditingItem()
    data class HashtagItem(val hashtag: Hashtag) : EditingItem()
}

data class JobSkillDialogState(
    val title: String = "",
    val type: SkillType = SkillType.JOB,
    val description: String = "",
    val error: String? = null,
    val isEditing: Boolean = false
)

data class HashtagDialogState(
    val tag: String = "",
    val error: String? = null,
    val isEditing: Boolean = false
)
