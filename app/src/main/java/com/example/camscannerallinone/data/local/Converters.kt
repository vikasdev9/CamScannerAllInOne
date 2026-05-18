package com.example.camscannerallinone.data.local

import androidx.room.TypeConverter
import com.example.camscannerallinone.domain.model.FilterType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromFilterType(value: FilterType): String {
        return value.name
    }

    @TypeConverter
    fun toFilterType(value: String): FilterType {
        return FilterType.valueOf(value)
    }
}
