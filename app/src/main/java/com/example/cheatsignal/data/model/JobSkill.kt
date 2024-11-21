package com.example.cheatsignal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "jobs_skills")
data class JobSkill(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: SkillType,
    val description: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)

enum class SkillType {
    JOB,
    SKILL
}
