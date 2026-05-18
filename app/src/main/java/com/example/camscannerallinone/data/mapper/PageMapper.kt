package com.example.camscannerallinone.data.mapper

import com.example.camscannerallinone.data.local.entity.PageEntity
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.model.Point
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

fun PageEntity.toDomain(): Page {
    val type = object : TypeToken<List<Point>>() {}.type
    val corners: List<Point> = gson.fromJson(cornersJson, type) ?: emptyList()
    
    return Page(
        id = id,
        documentId = documentId,
        pageNumber = pageNumber,
        originalImageUri = originalImageUri,
        processedImageUri = processedImageUri,
        ocrText = ocrText,
        filterType = filterType,
        rotationAngle = rotationAngle,
        corners = corners
    )
}

fun Page.toEntity(): PageEntity {
    val cornersJson = gson.toJson(corners)
    
    return PageEntity(
        id = id,
        documentId = documentId,
        pageNumber = pageNumber,
        originalImageUri = originalImageUri,
        processedImageUri = processedImageUri,
        ocrText = ocrText,
        filterType = filterType,
        rotationAngle = rotationAngle,
        cornersJson = cornersJson
    )
}
