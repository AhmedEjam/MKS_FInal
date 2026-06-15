package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.dao.CourseSlideDao
import com.ahmedyejam.mks.data.local.dao.KnowledgeStudySessionDao
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao
import com.ahmedyejam.mks.data.local.dao.NoteCollectionDao
import com.ahmedyejam.mks.data.local.dao.PromptCardDao
import com.ahmedyejam.mks.data.local.dao.PromptDao
import com.ahmedyejam.mks.data.local.dao.PromptDeckDao
import com.ahmedyejam.mks.data.local.dao.PromptRunDao
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.data.local.dao.StudySessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltKnowledgeDaoModule {
    @Provides
    @Singleton
    fun provideSlideshowCourseDao(appModule: AppModule): SlideshowCourseDao {
        return appModule.database.slideshowCourseDao()
    }

    @Provides
    @Singleton
    fun provideCourseSlideDao(appModule: AppModule): CourseSlideDao {
        return appModule.database.courseSlideDao()
    }

    @Provides
    @Singleton
    fun provideNoteBlueprintDao(appModule: AppModule): NoteBlueprintDao {
        return appModule.database.noteBlueprintDao()
    }

    @Provides
    @Singleton
    fun provideNoteCollectionDao(appModule: AppModule): NoteCollectionDao {
        return appModule.database.noteCollectionDao()
    }

    @Provides
    @Singleton
    fun providePromptDao(appModule: AppModule): PromptDao {
        return appModule.database.promptDao()
    }

    @Provides
    @Singleton
    fun providePromptDeckDao(appModule: AppModule): PromptDeckDao {
        return appModule.database.promptDeckDao()
    }

    @Provides
    @Singleton
    fun providePromptCardDao(appModule: AppModule): PromptCardDao {
        return appModule.database.promptCardDao()
    }

    @Provides
    @Singleton
    fun providePromptRunDao(appModule: AppModule): PromptRunDao {
        return appModule.database.promptRunDao()
    }

    @Provides
    @Singleton
    fun provideKnowledgeStudySessionDao(appModule: AppModule): KnowledgeStudySessionDao {
        return appModule.database.knowledgeStudySessionDao()
    }

    @Provides
    @Singleton
    fun provideStudySessionDao(appModule: AppModule): StudySessionDao {
        return appModule.database.studySessionDao()
    }
}
