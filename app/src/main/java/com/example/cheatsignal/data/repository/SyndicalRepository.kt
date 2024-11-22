package com.example.cheatsignal.data.repository

import com.example.cheatsignal.data.local.SyndicalDao
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyndicalRepository @Inject constructor(
    private val syndicalDao: SyndicalDao
) {
    // Jobs & Skills
    fun getJobsAndSkills(): Flow<List<JobSkill>> = syndicalDao.getJobsAndSkills()
    
    fun getJobsOrSkills(type: SkillType): Flow<List<JobSkill>> = syndicalDao.getJobsOrSkills(type)
    
    fun searchJobsAndSkills(query: String): Flow<List<JobSkill>> = syndicalDao.searchJobsAndSkills(query)
    
    suspend fun upsertJobSkill(jobSkill: JobSkill) {
        syndicalDao.insertJobSkill(jobSkill)
    }
    
    suspend fun deleteJobSkill(jobSkill: JobSkill) {
        syndicalDao.deleteJobSkill(jobSkill)
    }
    
    // Hashtags
    fun getTrendingHashtags(): Flow<List<Hashtag>> = syndicalDao.getTrendingHashtags()
    
    fun searchHashtags(query: String): Flow<List<Hashtag>> = syndicalDao.searchHashtags(query)
    
    suspend fun upsertHashtag(hashtag: Hashtag) {
        syndicalDao.insertHashtag(hashtag)
    }
    
    suspend fun deleteHashtag(hashtag: Hashtag) {
        syndicalDao.deleteHashtag(hashtag)
    }
    
    suspend fun incrementHashtagUsage(hashtagId: String) {
        syndicalDao.incrementHashtagUsage(hashtagId)
    }
}
