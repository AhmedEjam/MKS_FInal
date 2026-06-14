package com.ahmedyejam.mks.di

import android.content.Context
import com.ahmedyejam.mks.data.focus.FocusManager
import com.ahmedyejam.mks.data.importer.mapping.LibraryMapper
import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.*
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.preview.CategoryMergePreviewService
import com.ahmedyejam.mks.data.preview.ClearMarksPreviewService
import com.ahmedyejam.mks.data.preview.DeletePreviewService
import com.ahmedyejam.mks.data.repair.AssetReferenceAuditService
import com.ahmedyejam.mks.data.repository.ExportManager
import com.ahmedyejam.mks.data.repository.MksRepository
import com.ahmedyejam.mks.data.repository.OllamaRepository
import com.ahmedyejam.mks.data.review.ReviewRepository
import com.ahmedyejam.mks.data.search.GlobalSearchRepository
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
    fun provideAppModule(@ApplicationContext context: Context): AppModule {
        return AppModule(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(appModule: AppModule): MksDatabase {
        return appModule.database
    }

    @Provides
    @Singleton
    fun provideFileManager(appModule: AppModule): FileManager {
        return appModule.fileManager
    }

    @Provides
    @Singleton
    fun provideLibraryMapper(appModule: AppModule): LibraryMapper {
        return appModule.libraryMapper
    }

    @Provides
    @Singleton
    fun provideExportManager(appModule: AppModule): ExportManager {
        return appModule.exportManager
    }

    @Provides
    @Singleton
    fun provideImportLibraryManager(appModule: AppModule): ImportLibraryManager {
        return appModule.importManager
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

    @Provides
    @Singleton
    fun provideOllamaRepository(appModule: AppModule): OllamaRepository {
        return appModule.ollamaRepository
    }

    @Provides
    @Singleton
    fun provideMksRepository(appModule: AppModule): MksRepository {
        return appModule.repository
    }

    @Provides
    @Singleton
    fun provideGlobalSearchRepository(appModule: AppModule): GlobalSearchRepository {
        return appModule.globalSearchRepository
    }

    @Provides
    @Singleton
    fun provideReviewRepository(appModule: AppModule): ReviewRepository {
        return appModule.reviewRepository
    }

    // Services
    @Provides
    @Singleton
    fun provideDeletePreviewService(appModule: AppModule): DeletePreviewService {
        return appModule.deletePreviewService
    }

    @Provides
    @Singleton
    fun provideCategoryMergePreviewService(appModule: AppModule): CategoryMergePreviewService {
        return appModule.categoryMergePreviewService
    }

    @Provides
    @Singleton
    fun provideClearMarksPreviewService(appModule: AppModule): ClearMarksPreviewService {
        return appModule.clearMarksPreviewService
    }

    @Provides
    @Singleton
    fun provideAssetReferenceAuditService(appModule: AppModule): AssetReferenceAuditService {
        return appModule.assetReferenceAuditService
    }

    // DAOs

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
    fun provideSlideshowCourseDao(appModule: AppModule): SlideshowCourseDao {
        return appModule.database.slideshowCourseDao()
    }

    @Provides
    @Singleton
    fun provideCourseSlideDao(appModule: AppModule): CourseSlideDao {
        return appModule.database.courseSlideDao()
    }

    @Provides
    @Singleton
    fun provideNoteBlueprintDao(appModule: AppModule): NoteBlueprintDao {
        return appModule.database.noteBlueprintDao()
    }

    @Provides
    @Singleton
    fun providePromptDao(appModule: AppModule): PromptDao {
        return appModule.database.promptDao()
    }

    @Provides
    @Singleton
    fun provideKnowledgeStudySessionDao(appModule: AppModule): KnowledgeStudySessionDao {
        return appModule.database.knowledgeStudySessionDao()
    }

    @Provides
    @Singleton
    fun provideQuestionCategoryDao(appModule: AppModule): QuestionCategoryDao {
        return appModule.database.questionCategoryDao()
    }

    @Provides
    @Singleton
    fun provideAssetReferenceDao(appModule: AppModule): AssetReferenceDao {
        return appModule.database.assetReferenceDao()
    }

    @Provides
    @Singleton
    fun provideQuestionAssetDao(appModule: AppModule): QuestionAssetDao {
        return appModule.database.questionAssetDao()
    }

    @Provides
    @Singleton
    fun provideSourceDocumentDao(appModule: AppModule): SourceDocumentDao {
        return appModule.database.sourceDocumentDao()
    }

    @Provides
    @Singleton
    fun provideSourceDocumentAssetDao(appModule: AppModule): SourceDocumentAssetDao {
        return appModule.database.sourceDocumentAssetDao()
    }

    @Provides
    @Singleton
    fun provideGlobalSearchDao(appModule: AppModule): GlobalSearchDao {
        return appModule.database.globalSearchDao()
    }

    @Provides
    @Singleton
    fun provideAnnotationDao(appModule: AppModule): AnnotationDao {
        return appModule.database.annotationDao()
    }

    @Provides
    @Singleton
    fun provideNoteCollectionDao(appModule: AppModule): NoteCollectionDao {
        return appModule.database.noteCollectionDao()
    }

    @Provides
    @Singleton
    fun provideStudySessionDao(appModule: AppModule): StudySessionDao {
        return appModule.database.studySessionDao()
    }

    @Provides
    @Singleton
    fun provideWorkspaceDao(appModule: AppModule): WorkspaceDao {
        return appModule.workspaceDao
    }

    @Provides
    @Singleton
    fun provideFlashcardDeckDao(appModule: AppModule): FlashcardDeckDao {
        return appModule.flashcardDeckDao
    }

    @Provides
    @Singleton
    fun provideFlashcardDao(appModule: AppModule): FlashcardDao {
        return appModule.flashcardDao
    }

    @Provides
    @Singleton
    fun provideLearningSessionDao(appModule: AppModule): LearningSessionDao {
        return appModule.learningSessionDao
    }

    @Provides
    @Singleton
    fun providePromptDeckDao(appModule: AppModule): PromptDeckDao {
        return appModule.promptDeckDao
    }

    @Provides
    @Singleton
    fun providePromptCardDao(appModule: AppModule): PromptCardDao {
        return appModule.promptCardDao
    }

    @Provides
    @Singleton
    fun providePromptRunDao(appModule: AppModule): PromptRunDao {
        return appModule.promptRunDao
    }

    @Provides
    @Singleton
    fun provideMistakeLogDao(appModule: AppModule): MistakeLogDao {
        return appModule.mistakeLogDao
    }
}
