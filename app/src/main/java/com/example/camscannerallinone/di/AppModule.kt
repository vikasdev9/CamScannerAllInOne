package com.example.camscannerallinone.di

import android.content.Context
import androidx.room.Room
import com.example.camscannerallinone.data.local.AppDatabase
import com.example.camscannerallinone.data.local.dao.DocumentDao
import com.example.camscannerallinone.data.local.dao.PageDao
import com.example.camscannerallinone.data.repository.DocumentRepositoryImpl
import com.example.camscannerallinone.domain.repository.DocumentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "scanflow_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDocumentDao(database: AppDatabase): DocumentDao {
        return database.documentDao()
    }

    @Provides
    @Singleton
    fun providePageDao(database: AppDatabase): PageDao {
        return database.pageDao()
    }

    @Provides
    @Singleton
    fun provideDocumentRepository(
        documentDao: DocumentDao,
        pageDao: PageDao
    ): DocumentRepository {
        return DocumentRepositoryImpl(documentDao, pageDao)
    }
}
