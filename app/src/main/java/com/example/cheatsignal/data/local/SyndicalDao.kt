package com.example.cheatsignal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cheatsignal.data.model.Hashtag
import com.example.cheatsignal.data.model.JobSkill
import com.example.cheatsignal.data.model.SkillType
import kotlinx.coroutines.flow.Flow

@Dao
interface SyndicalDao {
    // Jobs & Skills
    @Query("SELECT * FROM jobs_skills ORDER BY lastModified DESC")
    fun getJobsAndSkills(): Flow<List<JobSkill>>
    
    @Query("SELECT * FROM jobs_skills WHERE type = :type ORDER BY title ASC")
    fun getJobsOrSkills(type: SkillType): Flow<List<JobSkill>>
    
    @Query("SELECT * FROM jobs_skills WHERE title LIKE '%' || :query || '%'")
    fun searchJobsAndSkills(query: String): Flow<List<JobSkill>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobSkill(jobSkill: JobSkill)

    @Delete
    suspend fun deleteJobSkill(jobSkill: JobSkill)
    
    // Hashtags
    @Query("SELECT * FROM hashtags ORDER BY usageCount DESC, lastUsed DESC")
    fun getTrendingHashtags(): Flow<List<Hashtag>>
    
    @Query("SELECT * FROM hashtags WHERE tag LIKE '%' || :query || '%'")
    fun searchHashtags(query: String): Flow<List<Hashtag>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHashtag(hashtag: Hashtag)

    @Delete
    suspend fun deleteHashtag(hashtag: Hashtag)
    
    @Query("UPDATE hashtags SET usageCount = usageCount + 1, lastUsed = :timestamp WHERE id = :hashtagId")
    suspend fun incrementHashtagUsage(hashtagId: String, timestamp: Long = System.currentTimeMillis())
}
