package com.ahmedyejam.mks.di

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.SearchIndexDao
import com.ahmedyejam.mks.data.model.StudyRunRepository
import com.ahmedyejam.mks.data.repository.StudyRunRepositoryImpl
import com.ahmedyejam.mks.data.search.GlobalSearchRepository
import com.ahmedyejam.mks.data.search.SearchIndexManager
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
    fun provideGlobalSearchRepository(
        database: MksDatabase,
        searchIndexManager: SearchIndexManager,
    ): GlobalSearchRepository {
        return GlobalSearchRepository(
            dao = database.globalSearchDao(),
            searchIndexDao = database.searchIndexDao(),
            searchIndexManager = searchIndexManager,
        )
    }

    @Provides
    @Singleton
    fun provideStudyRunRepository(impl: StudyRunRepositoryImpl): StudyRunRepository {
        return impl
    }
}
