package com.example.camscannerallinone.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun saveBitmap(bitmap: Bitmap, folderName: String = "scanned"): String = withContext(Dispatchers.IO) {
        val folder = File(context.filesDir, folderName)
        if (!folder.exists()) folder.mkdirs()

        val fileName = "IMG_${UUID.randomUUID()}.jpg"
        val file = File(folder, fileName)
        
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        
        file.absolutePath
    }

    suspend fun loadBitmap(path: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createThumbnail(bitmap: Bitmap): Bitmap = withContext(Dispatchers.Default) {
        val width = 200
        val height = (bitmap.height * (width.toFloat() / bitmap.width)).toInt()
        Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    fun deleteFile(path: String): Boolean {
        return File(path).delete()
    }

    fun getInternalFileUri(path: String): String {
        return "file://$path"
    }
}
