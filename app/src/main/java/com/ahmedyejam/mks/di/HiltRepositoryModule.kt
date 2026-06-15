package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.repository.OllamaRepository
import com.ahmedyejam.mks.data.review.ReviewRepository
import com.ahmedyejam.mks.data.search.GlobalSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltRepositoryModule {
    @Provides
    @Singleton
    fun provideOllamaRepository(appModule: AppModule): OllamaRepository {
        return appModule.ollamaRepository
    }

    @Provides
    @Singleton
    fun provideGlobalSearchRepository(database: MksDatabase): GlobalSearchRepository {
        return GlobalSearchRepository(database.globalSearchDao())
    }

    @Provides
    @Singleton
    fun provideReviewRepository(appModule: AppModule): ReviewRepository {
        return appModule.reviewRepository
    }
}
