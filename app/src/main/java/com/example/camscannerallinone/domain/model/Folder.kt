package com.example.camscannerallinone.domain.model

import java.util.Date

data class Folder(
    val id: Long = 0,
    val name: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val pdfCount: Int = 0
)
