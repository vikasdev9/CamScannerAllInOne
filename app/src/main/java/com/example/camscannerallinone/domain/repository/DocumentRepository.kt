package com.example.camscannerallinone.domain.repository

import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.model.Folder
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    // Document operations
    fun getAllDocuments(): Flow<List<Document>>
    fun getDocumentById(id: Long): Flow<Document?>
    suspend fun insertDocument(document: Document): Long
    suspend fun updateDocument(document: Document)
    suspend fun deleteDocument(document: Document)
    fun searchDocuments(query: String): Flow<List<Document>>

    // Page operations
    fun getPagesForDocument(documentId: Long): Flow<List<Page>>
    fun getPageById(pageId: Long): Flow<Page?>
    suspend fun insertPage(page: Page): Long
    suspend fun updatePage(page: Page)
    suspend fun deletePage(page: Page)
    suspend fun reorderPages(pages: List<Page>)

    // Tag operations
    fun getAllTags(): Flow<List<Tag>>
    suspend fun insertTag(tag: Tag): Long
    suspend fun addTagToDocument(documentId: Long, tagId: Long)
    suspend fun removeTagFromDocument(documentId: Long, tagId: Long)

    // Folder operations
    fun getAllFolders(): Flow<List<Folder>>
    suspend fun getFolderById(id: Long): Folder?
    suspend fun insertFolder(folder: Folder): Long
    suspend fun updateFolder(folder: Folder)
    suspend fun deleteFolder(folder: Folder)
    fun getDocumentsInFolder(folderId: Long): Flow<List<Document>>
}
