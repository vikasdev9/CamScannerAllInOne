package com.example.camscannerallinone.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.usecase.GetDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class HomeState(
    val documents: List<Document> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDocumentsUseCase: GetDocumentsUseCase
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

    private fun loadDocuments() {
        getDocumentsUseCase(state.value.searchQuery)
            .onEach { documents ->
                _state.value = _state.value.copy(documents = documents, isLoading = false)
            }
            .launchIn(viewModelScope)
    }
}
