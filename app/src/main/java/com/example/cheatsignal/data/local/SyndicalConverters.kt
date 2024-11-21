package com.example.cheatsignal.data.local

import androidx.room.TypeConverter
import com.example.cheatsignal.data.model.SkillType

class SyndicalConverters {
    @TypeConverter
    fun fromSkillType(value: SkillType): String {
        return value.name
    }

    @TypeConverter
    fun toSkillType(value: String): SkillType {
        return SkillType.valueOf(value)
    }
}
