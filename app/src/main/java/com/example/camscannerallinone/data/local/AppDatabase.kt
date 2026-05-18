package com.example.camscannerallinone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.camscannerallinone.data.local.dao.DocumentDao
import com.example.camscannerallinone.data.local.dao.PageDao
import com.example.camscannerallinone.data.local.entity.DocumentEntity
import com.example.camscannerallinone.data.local.entity.DocumentTagCrossRef
import com.example.camscannerallinone.data.local.entity.PageEntity
import com.example.camscannerallinone.data.local.entity.TagEntity

@Database(
    entities = [
        DocumentEntity::class,
        PageEntity::class,
        TagEntity::class,
        DocumentTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
    abstract fun pageDao(): PageDao
}
