package com.example.camscannerallinone.domain.model

data class Page(
    val id: Long = 0,
    val documentId: Long,
    val pageNumber: Int,
    val originalImageUri: String,
    val processedImageUri: String? = null,
    val ocrText: String? = null,
    val filterType: FilterType = FilterType.ORIGINAL,
    val rotationAngle: Float = 0f,
    val corners: List<Point> = emptyList()
)

enum class FilterType {
    ORIGINAL, BLACK_AND_WHITE, MAGIC_COLOR, GRAYSCALE, SHARPEN, HIGH_CONTRAST
}

data class Point(val x: Float, val y: Float)
