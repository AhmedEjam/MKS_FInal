package com.ahmedyejam.mks.di

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
    fun provideBookDao(appModule: AppModule): BookDao {
        return appModule.database.bookDao()
    }

    @Provides
    @Singleton
    fun provideQuizDao(appModule: AppModule): QuizDao {
        return appModule.database.quizDao()
    }

    @Provides
    @Singleton
    fun provideQuestionDao(appModule: AppModule): QuestionDao {
        return appModule.database.questionDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(appModule: AppModule): SessionDao {
        return appModule.database.sessionDao()
    }

    @Provides
    @Singleton
    fun provideCategoryMetadataDao(appModule: AppModule): CategoryMetadataDao {
        return appModule.database.categoryMetadataDao()
    }

    @Provides
    @Singleton
    fun provideQuestionCategoryDao(appModule: AppModule): QuestionCategoryDao {
        return appModule.database.questionCategoryDao()
    }
}
