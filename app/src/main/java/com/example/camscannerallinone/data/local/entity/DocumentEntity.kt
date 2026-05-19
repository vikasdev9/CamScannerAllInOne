package com.example.camscannerallinone.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Date,
    val updatedAt: Date,
    val thumbnailUri: String?,
    val pageCount: Int,
    val folderId: Long? = null, // Linked to FolderEntity
    val pdfPath: String? = null, // Path to generated PDF
    val isFavorite: Boolean = false,
    val isLocked: Boolean = false,
    val fileSize: Long = 0
)
