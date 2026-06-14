package com.ahmedyejam.mks.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.MksMigrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMksDatabase(
        @ApplicationContext context: Context
    ): MksDatabase {
        return Room.databaseBuilder(
            context,
            MksDatabase::class.java,
            MksDatabase.DATABASE_NAME
        )
        .addMigrations(*MksMigrations.ALL)
        .addCallback(object : RoomDatabase.Callback() {

        })
        .build()
    }

    @Provides
    fun provideWorkspaceDao(database: MksDatabase) = database.workspaceDao()

    @Provides
    fun provideBookDao(database: MksDatabase) = database.bookDao()

    @Provides
    fun provideQuizDao(database: MksDatabase) = database.quizDao()

    @Provides
    fun provideQuestionDao(database: MksDatabase) = database.questionDao()

    @Provides
    fun provideSessionDao(database: MksDatabase) = database.sessionDao()

    @Provides
    fun provideCategoryMetadataDao(database: MksDatabase) = database.categoryMetadataDao()

    @Provides
    fun provideFlashcardDeckDao(database: MksDatabase) = database.flashcardDeckDao()

    @Provides
    fun provideFlashcardDao(database: MksDatabase) = database.flashcardDao()

    @Provides
    fun provideLearningSessionDao(database: MksDatabase) = database.learningSessionDao()

    @Provides
    fun provideSlideshowCourseDao(database: MksDatabase) = database.slideshowCourseDao()

    @Provides
    fun provideCourseSlideDao(database: MksDatabase) = database.courseSlideDao()

    @Provides
    fun provideNoteCollectionDao(database: MksDatabase) = database.noteCollectionDao()

    @Provides
    fun provideNoteBlueprintDao(database: MksDatabase) = database.noteBlueprintDao()

    @Provides
    fun providePromptDao(database: MksDatabase) = database.promptDao()

    @Provides
    fun provideStudySessionDao(database: MksDatabase) = database.studySessionDao()

    @Provides
    fun provideKnowledgeStudySessionDao(database: MksDatabase) = database.knowledgeStudySessionDao()

    @Provides
    fun provideQuestionCategoryDao(database: MksDatabase) = database.questionCategoryDao()

    @Provides
    fun provideAssetReferenceDao(database: MksDatabase) = database.assetReferenceDao()

    @Provides
    fun provideQuestionAssetDao(database: MksDatabase) = database.questionAssetDao()

    @Provides
    fun provideSourceDocumentDao(database: MksDatabase) = database.sourceDocumentDao()

    @Provides
    fun providePromptDeckDao(database: MksDatabase) = database.promptDeckDao()

    @Provides
    fun providePromptCardDao(database: MksDatabase) = database.promptCardDao()

    @Provides
    fun providePromptRunDao(database: MksDatabase) = database.promptRunDao()

    @Provides
    fun provideMistakeLogDao(database: MksDatabase) = database.mistakeLogDao()

    @Provides
    fun provideAnnotationDao(database: MksDatabase) = database.annotationDao()
}
