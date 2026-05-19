package com.example.camscannerallinone.data.mapper

import com.example.camscannerallinone.data.local.entity.FolderEntity
import com.example.camscannerallinone.domain.model.Folder

fun FolderEntity.toDomain(): Folder {
    return Folder(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        pdfCount = pdfCount
    )
}

fun Folder.toEntity(): FolderEntity {
    return FolderEntity(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        pdfCount = pdfCount
    )
}
