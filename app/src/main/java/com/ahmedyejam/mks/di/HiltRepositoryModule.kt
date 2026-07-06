package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.MksDatabase
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
    fun provideGlobalSearchRepository(database: MksDatabase): GlobalSearchRepository {
        return GlobalSearchRepository(database.globalSearchDao())
    }
}
