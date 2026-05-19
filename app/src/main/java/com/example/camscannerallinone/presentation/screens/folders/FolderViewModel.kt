package com.example.camscannerallinone.presentation.screens.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.domain.model.Folder
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FolderState(
    val folders: List<Folder> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val repository: DocumentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FolderState())
    val state: StateFlow<FolderState> = _state.asStateFlow()

    init {
        loadFolders()
    }

    private fun loadFolders() {
        _state.value = _state.value.copy(isLoading = true)
        repository.getAllFolders()
            .onEach { folders ->
                _state.value = _state.value.copy(folders = folders, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    fun createFolder(name: String) {
        viewModelScope.launch {
            repository.insertFolder(Folder(name = name))
        }
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch {
            repository.deleteFolder(folder)
        }
    }

    fun renameFolder(folder: Folder, newName: String) {
        viewModelScope.launch {
            repository.updateFolder(folder.copy(name = newName))
        }
    }
}
