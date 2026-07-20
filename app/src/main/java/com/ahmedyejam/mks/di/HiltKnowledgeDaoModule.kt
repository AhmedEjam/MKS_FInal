package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao
import com.ahmedyejam.mks.data.local.dao.KnowledgeStudySessionDao
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao
import com.ahmedyejam.mks.data.local.dao.NoteCollectionDao
import com.ahmedyejam.mks.data.local.dao.PromptCardDao
import com.ahmedyejam.mks.data.local.dao.PromptDao
import com.ahmedyejam.mks.data.local.dao.PromptDeckDao
import com.ahmedyejam.mks.data.local.dao.PromptRunDao
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.data.local.dao.StudyRunDao
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
    fun provideSlideshowCourseDao(database: MksDatabase): SlideshowCourseDao {
        return database.slideshowCourseDao()
    }

    @Provides
    @Singleton
    fun provideCourseSlideDao(database: MksDatabase): CourseSlideDao {
        return database.courseSlideDao()
    }

    @Provides
    @Singleton
    fun provideNoteBlueprintDao(database: MksDatabase): NoteBlueprintDao {
        return database.noteBlueprintDao()
    }

    @Provides
    @Singleton
    fun provideNoteCollectionDao(database: MksDatabase): NoteCollectionDao {
        return database.noteCollectionDao()
    }

    @Provides
    @Singleton
    fun providePromptDao(database: MksDatabase): PromptDao {
        return database.promptDao()
    }

    @Provides
    @Singleton
    fun providePromptDeckDao(database: MksDatabase): PromptDeckDao {
        return database.promptDeckDao()
    }

    @Provides
    @Singleton
    fun providePromptCardDao(database: MksDatabase): PromptCardDao {
        return database.promptCardDao()
    }

    @Provides
    @Singleton
    fun providePromptRunDao(database: MksDatabase): PromptRunDao {
        return database.promptRunDao()
    }

    @Provides
    @Singleton
    fun provideKnowledgeStudySessionDao(database: MksDatabase): KnowledgeStudySessionDao {
        return database.knowledgeStudySessionDao()
    }

    @Provides
    @Singleton
    fun provideStudySessionDao(database: MksDatabase): StudySessionDao {
        return database.studySessionDao()
    }

    @Provides
    @Singleton
    fun provideStudyRunDao(database: MksDatabase): StudyRunDao {
        return database.studyRunDao()
    }
}
