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
    suspend operator fun invoke(bitmap: Bitmap, documentId: Long?, documentName: String = "New Scan") {
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

        val page = Page(
            documentId = targetDocumentId,
            pageNumber = 1, // Need to fetch existing count if adding to doc
            originalImageUri = imagePath,
            ocrText = ocrText
        )
        
        repository.insertPage(page)
        
        if (documentId != null) {
            // Update document updatedAt and pageCount
            // repository.updateDocument(...)
        }
    }
}
