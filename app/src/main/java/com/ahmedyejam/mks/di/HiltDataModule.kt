package com.ahmedyejam.mks.di

import android.content.Context
import com.ahmedyejam.mks.data.focus.FocusManager
import com.ahmedyejam.mks.data.importer.mapping.LibraryMapper
import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.ExportManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltDataModule {
    @Provides
    @Singleton
    fun provideAppModule(
        @ApplicationContext context: Context,
    ): AppModule {
        return AppModule(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(appModule: AppModule): MksDatabase {
        return appModule.database
    }

    @Provides
    @Singleton
    fun provideFileManager(
        @ApplicationContext context: Context,
    ): FileManager {
        return FileManager(context)
    }

    @Provides
    @Singleton
    fun provideLibraryMapper(): LibraryMapper {
        return LibraryMapper()
    }

    @Provides
    @Singleton
    fun provideExportManager(
        database: MksDatabase,
        fileManager: FileManager,
        libraryMapper: LibraryMapper,
    ): ExportManager {
        return ExportManager(
            database = database,
            bookDao = database.bookDao(),
            quizDao = database.quizDao(),
            questionDao = database.questionDao(),
            sessionDao = database.sessionDao(),
            categoryMetadataDao = database.categoryMetadataDao(),
            fileManager = fileManager,
            mapper = libraryMapper,
        )
    }

    @Provides
    @Singleton
    fun provideImportLibraryManager(
        @ApplicationContext context: Context,
        database: MksDatabase,
        fileManager: FileManager,
    ): ImportLibraryManager {
        return ImportLibraryManager(context, database, fileManager)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(appModule: AppModule): DataStoreManager {
        return appModule.dataStoreManager
    }

    @Provides
    @Singleton
    fun provideFocusManager(appModule: AppModule): FocusManager {
        return appModule.focusManager
    }
}
