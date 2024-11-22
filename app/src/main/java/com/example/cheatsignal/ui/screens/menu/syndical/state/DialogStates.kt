package com.example.cheatsignal.ui.screens.menu.syndical.state

import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType

data class JobSkillDialogState(
    val jobSkill: JobSkill? = null,
    val title: String = "",
    val type: SkillType = SkillType.SKILL,
    val description: String = "",
    val isEditing: Boolean = false,
    val error: String? = null
)

data class HashtagDialogState(
    val hashtag: Hashtag? = null,
    val tag: String = "",
    val isEditing: Boolean = false,
    val error: String? = null
)
