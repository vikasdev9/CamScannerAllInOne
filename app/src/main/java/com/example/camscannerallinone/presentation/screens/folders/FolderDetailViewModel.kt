package com.example.camscannerallinone.presentation.screens.folders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.model.Folder
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FolderDetailState(
    val folder: Folder? = null,
    val documents: List<Document> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class FolderDetailViewModel @Inject constructor(
    private val repository: DocumentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val folderId: Long = checkNotNull(savedStateHandle["folderId"])

    private val _state = MutableStateFlow(FolderDetailState())
    val state: StateFlow<FolderDetailState> = _state.asStateFlow()

    init {
        loadFolderDetail()
    }

    private fun loadFolderDetail() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val folder = repository.getFolderById(folderId)
            repository.getDocumentsInFolder(folderId)
                .onEach { documents ->
                    _state.value = _state.value.copy(
                        folder = folder,
                        documents = documents,
                        isLoading = false
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    fun removeDocumentFromFolder(document: Document) {
        viewModelScope.launch {
            repository.updateDocument(document.copy(folderId = null))
            // Update folder pdf count if needed
        }
    }
}
