package com.example.camscannerallinone.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DocumentRepository
) : ViewModel() {

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        observeDocuments()
    }

    private fun observeDocuments() {
        _searchQuery
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isEmpty()) {
                    repository.getAllDocuments()
                } else {
                    repository.searchDocuments(query)
                }
            }
            .onEach { _documents.value = it }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun deleteDocument(document: Document) {
        viewModelScope.launch {
            repository.deleteDocument(document)
        }
    }
    
    fun toggleFavorite(document: Document) {
        viewModelScope.launch {
            repository.updateDocument(document.copy(isFavorite = !document.isFavorite))
        }
    }
}
