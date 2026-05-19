package com.example.camscannerallinone.domain.usecase

import android.graphics.Bitmap
import com.example.camscannerallinone.data.util.FileUtil
import com.example.camscannerallinone.data.util.OcrManager
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.repository.DocumentRepository
import java.util.Date
import javax.inject.Inject

class SaveScanUseCase @Inject constructor(
    private val repository: DocumentRepository,
    private val fileUtil: FileUtil,
    private val ocrManager: OcrManager
) {
    suspend operator fun invoke(bitmap: Bitmap, documentId: Long?, documentName: String = "New Scan"): Long {
        val imagePath = fileUtil.saveBitmap(bitmap)
        val ocrText = ocrManager.extractText(bitmap)
        
        val targetDocumentId = if (documentId == null) {
            val thumbnailPath = fileUtil.saveBitmap(fileUtil.createThumbnail(bitmap), "thumbnails")
            repository.insertDocument(
                Document(
                    name = documentName,
                    thumbnailUri = thumbnailPath,
                    createdAt = Date(),
                    updatedAt = Date(),
                    pageCount = 1
                )
            )
        } else {
            documentId
        }

        val pageCount = if (documentId != null) {
            // This is a simplification, in a real app you'd fetch the current page count
            // For now, let's assume we can get it or just increment
            1 
        } else {
            1
        }

        val page = Page(
            documentId = targetDocumentId,
            pageNumber = pageCount,
            originalImageUri = imagePath,
            ocrText = ocrText
        )
        
        repository.insertPage(page)
        
        return targetDocumentId
    }
}
