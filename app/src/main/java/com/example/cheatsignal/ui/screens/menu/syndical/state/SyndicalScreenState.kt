package com.example.cheatsignal.ui.screens.menu.syndical.state

import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill

data class SyndicalScreenState(
    val selectedTabIndex: Int = 0, // 0 for Jobs & Skills, 1 for Hashtags
    val searchQuery: String = "",
    val jobSkills: List<JobSkill> = emptyList(),
    val hashtags: List<Hashtag> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val jobSkillDialogState: JobSkillDialogState? = null,
    val hashtagDialogState: HashtagDialogState? = null
)
