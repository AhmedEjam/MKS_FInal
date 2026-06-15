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
    fun provideAssetReferenceDao(appModule: AppModule): AssetReferenceDao {
        return appModule.database.assetReferenceDao()
    }

    @Provides
    @Singleton
    fun provideQuestionAssetDao(appModule: AppModule): QuestionAssetDao {
        return appModule.database.questionAssetDao()
    }

    @Provides
    @Singleton
    fun provideSourceDocumentDao(appModule: AppModule): SourceDocumentDao {
        return appModule.database.sourceDocumentDao()
    }

    @Provides
    @Singleton
    fun provideGlobalSearchDao(appModule: AppModule): GlobalSearchDao {
        return appModule.database.globalSearchDao()
    }

    @Provides
    @Singleton
    fun provideAnnotationDao(appModule: AppModule): AnnotationDao {
        return appModule.database.annotationDao()
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
