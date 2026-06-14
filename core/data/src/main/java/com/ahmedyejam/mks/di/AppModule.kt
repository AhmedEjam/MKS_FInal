package com.ahmedyejam.mks.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmedyejam.mks.data.focus.FocusManager
import com.ahmedyejam.mks.data.importer.mapping.LibraryMapper
import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.MksMigrations
import com.ahmedyejam.mks.data.local.WorkspaceDefaults
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuestionType
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.repository.ExportManager

import com.ahmedyejam.mks.data.review.ReviewRepository
import com.ahmedyejam.mks.data.search.GlobalSearchRepository
import com.ahmedyejam.mks.util.MksLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AppModule(val context: Context) {
    
    val database: MksDatabase by lazy {
        Room.databaseBuilder(
            context,
            MksDatabase::class.java,
            MksDatabase.DATABASE_NAME
        )
        .addMigrations(*MksMigrations.ALL)
        .addCallback(object : RoomDatabase.Callback() {

        })
        .build()
    }

    val reviewRepository: ReviewRepository by lazy {
        ReviewRepository(
            database.flashcardDao(),
            database.noteBlueprintDao(),
            database.mistakeLogDao(),
            database.questionDao(),
            database.courseSlideDao()
        )
    }

    val deletePreviewService: com.ahmedyejam.mks.data.preview.DeletePreviewService by lazy {
        com.ahmedyejam.mks.data.preview.DeletePreviewService(
            database.bookDao(),
            database.quizDao(),
            database.questionDao()
        )
    }

    val categoryMergePreviewService: com.ahmedyejam.mks.data.preview.CategoryMergePreviewService by lazy {
        com.ahmedyejam.mks.data.preview.CategoryMergePreviewService(
            database.questionCategoryDao()
        )
    }

    val clearMarksPreviewService: com.ahmedyejam.mks.data.preview.ClearMarksPreviewService by lazy {
        com.ahmedyejam.mks.data.preview.ClearMarksPreviewService(
            database.questionDao()
        )
    }

    val assetReferenceAuditService: com.ahmedyejam.mks.data.repair.AssetReferenceAuditService by lazy {
        com.ahmedyejam.mks.data.repair.AssetReferenceAuditService(
            database.assetReferenceDao(),
            database.bookDao(),
            database.quizDao(),
            database.questionDao(),
            database.flashcardDao(),
            database.courseSlideDao(),
            database.sourceDocumentDao(),
            database.questionAssetDao()
        )
    }

    val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(context)
    }

    val ollamaRepository: com.ahmedyejam.mks.data.repository.OllamaRepository by lazy {
        com.ahmedyejam.mks.data.repository.OllamaRepository()
    }

    val focusManager: FocusManager by lazy {
        FocusManager(context)
    }

    val applicationScope = CoroutineScope(Dispatchers.Default)


}
