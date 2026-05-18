package com.example.camscannerallinone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Int
)

@Entity(
    tableName = "document_tag_cross_ref",
    primaryKeys = ["documentId", "tagId"]
)
data class DocumentTagCrossRef(
    val documentId: Long,
    val tagId: Long
)
