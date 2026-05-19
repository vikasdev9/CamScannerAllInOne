package com.example.camscannerallinone.data.repository

import com.example.camscannerallinone.data.local.dao.DocumentDao
import com.example.camscannerallinone.data.local.dao.FolderDao
import com.example.camscannerallinone.data.local.dao.PageDao
import com.example.camscannerallinone.data.mapper.toDomain
import com.example.camscannerallinone.data.mapper.toEntity
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.domain.model.Folder
import com.example.camscannerallinone.domain.model.Page
import com.example.camscannerallinone.domain.model.Tag
import com.example.camscannerallinone.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val documentDao: DocumentDao,
    private val pageDao: PageDao,
    private val folderDao: FolderDao
) : DocumentRepository {

    override fun getAllDocuments(): Flow<List<Document>> {
        return documentDao.getAllDocuments().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getDocumentById(id: Long): Flow<Document?> {
        return documentDao.getDocumentById(id).map { it?.toDomain() }
    }

    override suspend fun insertDocument(document: Document): Long {
        return documentDao.insertDocument(document.toEntity())
    }

    override suspend fun updateDocument(document: Document) {
        documentDao.updateDocument(document.toEntity())
    }

    override suspend fun deleteDocument(document: Document) {
        documentDao.deleteDocument(document.toEntity())
    }

    override fun searchDocuments(query: String): Flow<List<Document>> {
        return documentDao.searchDocuments(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPagesForDocument(documentId: Long): Flow<List<Page>> {
        return pageDao.getPagesForDocument(documentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPageById(pageId: Long): Flow<Page?> {
        return pageDao.getPageById(pageId).map { it?.toDomain() }
    }

    override suspend fun insertPage(page: Page): Long {
        return pageDao.insertPage(page.toEntity())
    }

    override suspend fun updatePage(page: Page) {
        pageDao.updatePage(page.toEntity())
    }

    override suspend fun deletePage(page: Page) {
        pageDao.deletePage(page.toEntity())
    }

    override suspend fun reorderPages(pages: List<Page>) {
        pageDao.updatePages(pages.map { it.toEntity() })
    }

    override fun getAllTags(): Flow<List<Tag>> {
        return flowOf(emptyList())
    }

    override suspend fun insertTag(tag: Tag): Long {
        return 0
    }

    override suspend fun addTagToDocument(documentId: Long, tagId: Long) {}

    override suspend fun removeTagFromDocument(documentId: Long, tagId: Long) {}

    override fun getAllFolders(): Flow<List<Folder>> {
        return folderDao.getAllFolders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getFolderById(id: Long): Folder? {
        return folderDao.getFolderById(id)?.toDomain()
    }

    override suspend fun insertFolder(folder: Folder): Long {
        return folderDao.insertFolder(folder.toEntity())
    }

    override suspend fun updateFolder(folder: Folder) {
        folderDao.updateFolder(folder.toEntity())
    }

    override suspend fun deleteFolder(folder: Folder) {
        folderDao.deleteFolder(folder.toEntity())
    }

    override fun getDocumentsInFolder(folderId: Long): Flow<List<Document>> {
        return documentDao.getDocumentsInFolder(folderId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
