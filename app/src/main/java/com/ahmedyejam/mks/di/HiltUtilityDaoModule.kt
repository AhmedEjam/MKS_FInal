package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.AnnotationDao
import com.ahmedyejam.mks.data.local.dao.AssetReferenceDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDeckDao
import com.ahmedyejam.mks.data.local.dao.GlobalSearchDao
import com.ahmedyejam.mks.data.local.dao.LearningSessionDao
import com.ahmedyejam.mks.data.local.dao.MistakeLogDao
import com.ahmedyejam.mks.data.local.dao.QuestionAssetDao
import com.ahmedyejam.mks.data.local.dao.SearchIndexDao
import com.ahmedyejam.mks.data.local.dao.SourceDocumentDao
import com.ahmedyejam.mks.data.local.dao.WorkspaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltUtilityDaoModule {
    @Provides
    @Singleton
    fun provideAssetReferenceDao(database: MksDatabase): AssetReferenceDao {
        return database.assetReferenceDao()
    }

    @Provides
    @Singleton
    fun provideQuestionAssetDao(database: MksDatabase): QuestionAssetDao {
        return database.questionAssetDao()
    }

    @Provides
    @Singleton
    fun provideSourceDocumentDao(database: MksDatabase): SourceDocumentDao {
        return database.sourceDocumentDao()
    }

    @Provides
    @Singleton
    fun provideGlobalSearchDao(database: MksDatabase): GlobalSearchDao {
        return database.globalSearchDao()
    }

    @Provides
    @Singleton
    fun provideAnnotationDao(database: MksDatabase): AnnotationDao {
        return database.annotationDao()
    }

    @Provides
    @Singleton
    fun provideSearchIndexDao(database: MksDatabase): SearchIndexDao {
        return database.searchIndexDao()
    }

    @Provides
    @Singleton
    fun provideWorkspaceDao(database: MksDatabase): WorkspaceDao {
        return database.workspaceDao()
    }

    @Provides
    @Singleton
    fun provideFlashcardDeckDao(database: MksDatabase): FlashcardDeckDao {
        return database.flashcardDeckDao()
    }

    @Provides
    @Singleton
    fun provideFlashcardDao(database: MksDatabase): FlashcardDao {
        return database.flashcardDao()
    }

    @Provides
    @Singleton
    fun provideLearningSessionDao(database: MksDatabase): LearningSessionDao {
        return database.learningSessionDao()
    }

    @Provides
    @Singleton
    fun provideMistakeLogDao(database: MksDatabase): MistakeLogDao {
        return database.mistakeLogDao()
    }
}
