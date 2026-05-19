package com.example.camscannerallinone.domain.model

import java.util.Date

data class Document(
    val id: Long = 0,
    val name: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val thumbnailUri: String? = null,
    val pageCount: Int = 0,
    val folderId: Long? = null,
    val pdfPath: String? = null,
    val isFavorite: Boolean = false,
    val isLocked: Boolean = false,
    val tags: List<Tag> = emptyList(),
    val fileSize: Long = 0
)

data class Tag(
    val id: Long = 0,
    val name: String,
    val color: Int
)
