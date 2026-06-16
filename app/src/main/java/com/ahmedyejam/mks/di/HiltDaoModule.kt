package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.CategoryMetadataDao
import com.ahmedyejam.mks.data.local.dao.QuestionCategoryDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import com.ahmedyejam.mks.data.local.dao.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltDaoModule {
    @Provides
    @Singleton
    fun provideBookDao(database: MksDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideQuizDao(database: MksDatabase): QuizDao {
        return database.quizDao()
    }

    @Provides
    @Singleton
    fun provideQuestionDao(database: MksDatabase): QuestionDao {
        return database.questionDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: MksDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    @Singleton
    fun provideCategoryMetadataDao(database: MksDatabase): CategoryMetadataDao {
        return database.categoryMetadataDao()
    }

    @Provides
    @Singleton
    fun provideQuestionCategoryDao(database: MksDatabase): QuestionCategoryDao {
        return database.questionCategoryDao()
    }
}
