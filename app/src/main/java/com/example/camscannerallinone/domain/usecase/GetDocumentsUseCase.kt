package com.example.camscannerallinone.domain.usecase

import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(
    private val repository: DocumentRepository
) {
    operator fun invoke(query: String = ""): Flow<List<Document>> {
        return if (query.isEmpty()) {
            repository.getAllDocuments()
        } else {
            repository.searchDocuments(query)
        }
    }
}
