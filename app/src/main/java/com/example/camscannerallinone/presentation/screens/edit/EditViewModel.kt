package com.example.camscannerallinone.presentation.screens.edit

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.data.util.FileUtil
import com.example.camscannerallinone.data.util.ImageProcessor
import com.example.camscannerallinone.domain.model.FilterType
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditState(
    val page: Page? = null,
    val originalBitmap: Bitmap? = null,
    val currentBitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val currentFilter: FilterType = FilterType.ORIGINAL
)

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: DocumentRepository,
    private val imageProcessor: ImageProcessor,
    private val fileUtil: FileUtil,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pageId: Long = checkNotNull(savedStateHandle["pageId"])
    
    private val _state = MutableStateFlow(EditState())
    val state = _state.asStateFlow()

    init {
        loadPage()
    }

    private fun loadPage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getPageById(pageId).collect { page ->
                if (page != null) {
                    val bitmap = fileUtil.loadBitmap(page.originalImageUri)
                    _state.value = _state.value.copy(
                        page = page,
                        originalBitmap = bitmap,
                        currentBitmap = bitmap,
                        currentFilter = page.filterType,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun applyFilter(filterType: FilterType) {
        val original = state.value.originalBitmap ?: return
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val filtered = imageProcessor.applyFilter(original, filterType)
            _state.value = _state.value.copy(
                currentBitmap = filtered,
                currentFilter = filterType,
                isLoading = false
            )
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val bitmap = state.value.currentBitmap ?: return@launch
            val path = fileUtil.saveBitmap(bitmap, "processed")
            val updatedPage = state.value.page?.copy(
                processedImageUri = path,
                filterType = state.value.currentFilter
            )
            if (updatedPage != null) {
                repository.updatePage(updatedPage)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}
