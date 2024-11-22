package com.example.cheatsignal.ui.screens.menu.syndical.state

import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType

sealed class SyndicalEvent {
    // Navigation Events
    object NavigateUp : SyndicalEvent()
    
    // Tab Events
    data class TabSelected(val index: Int) : SyndicalEvent()
    
    // Search Events
    data class SearchQueryChanged(val query: String) : SyndicalEvent()
    
    // Jobs & Skills Events
    object AddJobSkillClicked : SyndicalEvent()
    data class JobSkillClicked(val jobSkill: JobSkill) : SyndicalEvent()
    data class DeleteJobSkillClicked(val jobSkill: JobSkill) : SyndicalEvent()
    data class SaveJobSkill(
        val title: String,
        val type: SkillType,
        val description: String,
        val existingId: String? = null
    ) : SyndicalEvent()
    
    // Hashtag Events
    object AddHashtagClicked : SyndicalEvent()
    data class HashtagClicked(val hashtag: Hashtag) : SyndicalEvent()
    data class DeleteHashtagClicked(val hashtag: Hashtag) : SyndicalEvent()
    data class SaveHashtag(
        val tag: String,
        val existingId: String? = null
    ) : SyndicalEvent()
    
    // Dialog Events
    object DismissDialog : SyndicalEvent()
    data class JobSkillTitleChanged(val title: String) : SyndicalEvent()
    data class JobSkillTypeChanged(val type: SkillType) : SyndicalEvent()
    data class JobSkillDescriptionChanged(val description: String) : SyndicalEvent()
    data class HashtagTextChanged(val tag: String) : SyndicalEvent()
    
    // Error Events
    object DismissError : SyndicalEvent()
}
