package com.example.camscannerallinone.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.camscannerallinone.domain.model.FilterType

@Entity(
    tableName = "pages",
    foreignKeys = [
        ForeignKey(
            entity = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("documentId")]
)
data class PageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val documentId: Long,
    val pageNumber: Int,
    val originalImageUri: String,
    val processedImageUri: String?,
    val ocrText: String?,
    val filterType: FilterType,
    val rotationAngle: Float,
    val cornersJson: String // Serialized list of points
)
