package com.example.camscannerallinone.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.data.util.PdfManager
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.model.Folder
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.model.Tag
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DocumentDetailState(
    val document: Document? = null,
    val pages: List<Page> = emptyList(),
    val isExporting: Boolean = false,
    val exportedPdfPath: String? = null,
    val folders: List<Folder> = emptyList(),
    val showFolderSelection: Boolean = false
)

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    private val repository: DocumentRepository,
    private val pdfManager: PdfManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val documentId: Long = checkNotNull(savedStateHandle["documentId"])
    
    private val _state = MutableStateFlow(DocumentDetailState())
    val state: StateFlow<DocumentDetailState> = _state.asStateFlow()

    init {
        loadDocument()
    }

    private fun loadDocument() {
        viewModelScope.launch {
            val document = repository.getDocumentById(documentId).first()
            val pages = repository.getPagesForDocument(documentId).first()
            _state.value = _state.value.copy(document = document, pages = pages)
        }
    }

    fun exportToPdf() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isExporting = true)
            val pdfPath = pdfManager.createPdfFromImages(
                imagePaths = state.value.pages.map { it.originalImageUri },
                pdfName = state.value.document?.name ?: "scan"
            )
            state.value.document?.let { doc ->
                repository.updateDocument(doc.copy(pdfPath = pdfPath))
            }
            _state.value = _state.value.copy(isExporting = false, exportedPdfPath = pdfPath)
        }
    }

    fun showFolderSelection() {
        viewModelScope.launch {
            val folders = repository.getAllFolders().first()
            _state.value = _state.value.copy(folders = folders, showFolderSelection = true)
        }
    }

    fun hideFolderSelection() {
        _state.value = _state.value.copy(showFolderSelection = false)
    }

    fun moveToFolder(folderId: Long) {
        viewModelScope.launch {
            state.value.document?.let { doc ->
                repository.updateDocument(doc.copy(folderId = folderId))
                val folder = repository.getFolderById(folderId)
                folder?.let {
                    repository.updateFolder(it.copy(pdfCount = it.pdfCount + 1))
                }
            }
            hideFolderSelection()
            loadDocument()
        }
    }
}
