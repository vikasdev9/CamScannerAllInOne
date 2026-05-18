package com.example.camscannerallinone.data.util

import android.content.Context
import android.graphics.Bitmap
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PdfManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileUtil: FileUtil
) {

    suspend fun createPdfFromImages(imagePaths: List<String>, pdfName: String): String = withContext(Dispatchers.IO) {
        val document = PDDocument()
        
        imagePaths.forEach { path ->
            val bitmap = fileUtil.loadBitmap(path) ?: return@forEach
            val page = PDPage(PDRectangle.A4)
            document.addPage(page)
            
            val pdImage = LosslessFactory.createFromImage(document, bitmap)
            PDPageContentStream(document, page).use { contentStream ->
                // Scale image to fit A4 page
                val width = page.mediaBox.width
                val height = page.mediaBox.height
                contentStream.drawImage(pdImage, 0f, 0f, width, height)
            }
        }
        
        val folder = File(context.filesDir, "pdfs")
        if (!folder.exists()) folder.mkdirs()
        
        val file = File(folder, "$pdfName.pdf")
        document.save(file)
        document.close()
        
        file.absolutePath
    }
}
