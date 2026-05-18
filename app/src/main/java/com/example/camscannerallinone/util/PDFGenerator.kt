package com.example.camscannerallinone.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PDFGenerator {

    fun generatePDF(context: Context, imagePaths: List<String>, outputFileName: String): File? {
        val pdfDocument = PdfDocument()
        
        try {
            imagePaths.forEachIndexed { index, path ->
                val bitmap = BitmapFactory.decodeFile(path) ?: return@forEachIndexed
                val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index + 1).create()
                val page = pdfDocument.startPage(pageInfo)
                
                page.canvas.drawBitmap(bitmap, 0f, 0f, null)
                pdfDocument.finishPage(page)
                bitmap.recycle()
            }
            
            val outputFile = File(context.getExternalFilesDir(null), outputFileName)
            pdfDocument.writeTo(FileOutputStream(outputFile))
            return outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            pdfDocument.close()
        }
    }
}
