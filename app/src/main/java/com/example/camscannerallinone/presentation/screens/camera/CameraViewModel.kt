package com.example.camscannerallinone.presentation.screens.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.data.util.FileUtil
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class CameraState(
    val capturedBitmaps: List<Bitmap> = emptyList(),
    val isSaving: Boolean = false,
    val isFlashOn: Boolean = false,
    val error: String? = null,
    val savedDocumentId: Long? = null
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: DocumentRepository,
    private val fileUtil: FileUtil
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun onImageCaptured(bitmap: Bitmap) {
        val currentList = _state.value.capturedBitmaps.toMutableList()
        currentList.add(bitmap)
        _state.value = _state.value.copy(capturedBitmaps = currentList)
    }

    fun removeCapturedImage(index: Int) {
        val currentList = _state.value.capturedBitmaps.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _state.value = _state.value.copy(capturedBitmaps = currentList)
        }
    }

    fun toggleFlash() {
        _state.value = _state.value.copy(isFlashOn = !state.value.isFlashOn)
    }

    fun saveAllCaptures(documentName: String) {
        if (state.value.capturedBitmaps.isEmpty()) return

        _state.value = _state.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                val firstBitmap = state.value.capturedBitmaps.first()
                val thumbnailPath = fileUtil.saveBitmap(fileUtil.createThumbnail(firstBitmap), "thumbnails")
                
                val documentId = repository.insertDocument(
                    Document(
                        name = documentName,
                        thumbnailUri = thumbnailPath,
                        createdAt = Date(),
                        updatedAt = Date(),
                        pageCount = state.value.capturedBitmaps.size
                    )
                )

                state.value.capturedBitmaps.forEachIndexed { index, bitmap ->
                    val path = fileUtil.saveBitmap(bitmap)
                    repository.insertPage(
                        Page(
                            documentId = documentId,
                            pageNumber = index + 1,
                            originalImageUri = path
                        )
                    )
                }
                
                _state.value = _state.value.copy(
                    isSaving = false, 
                    capturedBitmaps = emptyList(),
                    savedDocumentId = documentId
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false, error = e.message)
            }
        }
    }
}
