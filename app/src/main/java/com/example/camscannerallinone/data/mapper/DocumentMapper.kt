package com.example.camscannerallinone.data.mapper

import com.example.camscannerallinone.data.local.entity.DocumentEntity
import com.example.camscannerallinone.domain.model.Document

fun DocumentEntity.toDomain(): Document {
    return Document(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        thumbnailUri = thumbnailUri,
        pageCount = pageCount,
        folderId = folderId,
        isFavorite = isFavorite,
        isLocked = isLocked,
        fileSize = fileSize
    )
}

fun Document.toEntity(): DocumentEntity {
    return DocumentEntity(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        thumbnailUri = thumbnailUri,
        pageCount = pageCount,
        folderId = folderId,
        isFavorite = isFavorite,
        isLocked = isLocked,
        fileSize = fileSize
    )
}
