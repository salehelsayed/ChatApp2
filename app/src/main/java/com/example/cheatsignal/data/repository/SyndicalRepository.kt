package com.example.cheatsignal.data.repository

import com.example.cheatsignal.data.local.SyndicalDao
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyndicalRepository @Inject constructor(
    private val syndicalDao: SyndicalDao
) {
    // Jobs & Skills
    fun getJobsAndSkills() = syndicalDao.getJobsAndSkills()
    
    fun getJobsOrSkills(type: SkillType) = syndicalDao.getJobsOrSkills(type)
    
    fun searchJobsAndSkills(query: String) = syndicalDao.searchJobsAndSkills(query)
    
    suspend fun addJobSkill(jobSkill: JobSkill) {
        syndicalDao.insertJobSkill(jobSkill)
    }
    
    suspend fun deleteJobSkill(jobSkill: JobSkill) {
        syndicalDao.deleteJobSkill(jobSkill)
    }
    
    // Hashtags
    fun getTrendingHashtags() = syndicalDao.getTrendingHashtags()
    
    fun searchHashtags(query: String) = syndicalDao.searchHashtags(query)
    
    suspend fun addHashtag(hashtag: Hashtag) {
        syndicalDao.insertHashtag(hashtag)
    }
    
    suspend fun deleteHashtag(hashtag: Hashtag) {
        syndicalDao.deleteHashtag(hashtag)
    }
    
    suspend fun incrementHashtagUsage(hashtagId: String) {
        syndicalDao.incrementHashtagUsage(hashtagId)
    }
}
