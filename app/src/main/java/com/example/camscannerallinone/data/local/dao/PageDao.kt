package com.example.camscannerallinone.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.camscannerallinone.data.local.entity.PageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {
    @Query("SELECT * FROM pages WHERE documentId = :documentId ORDER BY pageNumber ASC")
    fun getPagesForDocument(documentId: Long): Flow<List<PageEntity>>

    @Query("SELECT * FROM pages WHERE id = :id")
    fun getPageById(id: Long): Flow<PageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: PageEntity): Long

    @Update
    suspend fun updatePage(page: PageEntity)

    @Delete
    suspend fun deletePage(page: PageEntity)

    @Update
    suspend fun updatePages(pages: List<PageEntity>)
}
