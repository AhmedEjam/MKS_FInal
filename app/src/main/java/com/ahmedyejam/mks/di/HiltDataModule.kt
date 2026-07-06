package com.ahmedyejam.mks.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmedyejam.mks.data.importer.mapping.LibraryMapper
import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.MksMigrations
import com.ahmedyejam.mks.data.repository.ExportManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/** Qualifier for the application-wide [CoroutineScope] that outlives ViewModels. */

@Module
@InstallIn(SingletonComponent::class)
object HiltDataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MksDatabase {
        return Room.databaseBuilder(
            context,
            MksDatabase::class.java,
            MksDatabase.DATABASE_NAME,
        )
            .addMigrations(*MksMigrations.ALL)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .addCallback(
                object : RoomDatabase.Callback() {
                },
            )
            .build()
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
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Default)
    }
}
