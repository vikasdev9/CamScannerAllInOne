package com.example.camscannerallinone.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.usecase.GetDocumentsUseCase
import com.example.camscannerallinone.domain.usecase.SaveScanUseCase
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val documents: List<Document> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val saveScanUseCase: SaveScanUseCase,
    private val repository: DocumentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadDocuments()
    }

    fun onSearch(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        loadDocuments()
    }

    fun importImages(bitmaps: List<android.graphics.Bitmap>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            var currentDocumentId: Long? = null
            bitmaps.forEach { bitmap ->
                currentDocumentId = saveScanUseCase(bitmap, currentDocumentId, "Imported Scan")
            }
            loadDocuments()
        }
    }

    private fun loadDocuments() {
        getDocumentsUseCase(state.value.searchQuery)
            .onEach { documents ->
                _state.value = _state.value.copy(documents = documents, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            repository.deleteDocument(document)
        }
    }

    fun renameDocument(document: Document, newName: String) {
        viewModelScope.launch {
            repository.updateDocument(document.copy(name = newName))
        }
    }
}
