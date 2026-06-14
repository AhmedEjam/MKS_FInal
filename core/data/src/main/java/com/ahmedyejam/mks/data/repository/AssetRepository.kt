package com.ahmedyejam.mks.data.repository

import android.net.Uri
import com.ahmedyejam.mks.data.importer.model.ImportFormat
import com.ahmedyejam.mks.data.importer.model.ImportResult
import com.ahmedyejam.mks.data.importer.model.MergeStrategy
import com.ahmedyejam.mks.data.importer.model.ParsedQuestion
import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.WorkspaceDefaults
import com.ahmedyejam.mks.data.local.dao.AnnotationDao
import com.ahmedyejam.mks.data.local.dao.AssetReferenceDao
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.CategoryMetadataDao
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDeckDao
import com.ahmedyejam.mks.data.local.dao.KnowledgeStudySessionDao
import com.ahmedyejam.mks.data.local.dao.LearningSessionDao
import com.ahmedyejam.mks.data.local.dao.MistakeLogDao
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao
import com.ahmedyejam.mks.data.local.dao.PromptCardDao
import com.ahmedyejam.mks.data.local.dao.PromptDao
import com.ahmedyejam.mks.data.local.dao.PromptDeckDao
import com.ahmedyejam.mks.data.local.dao.PromptRunDao
import com.ahmedyejam.mks.data.local.dao.QuestionAssetDao
import com.ahmedyejam.mks.data.local.dao.QuestionCategoryDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.QuizDao
import com.ahmedyejam.mks.data.local.dao.SessionDao
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.data.local.dao.SourceDocumentDao
import com.ahmedyejam.mks.data.local.dao.WorkspaceDao
import com.ahmedyejam.mks.data.local.entity.AnnotationColorLabel
import com.ahmedyejam.mks.data.local.entity.AnnotationEntity
import com.ahmedyejam.mks.data.local.entity.AnnotationOwnerType
import com.ahmedyejam.mks.data.local.entity.AssetReferenceEntity
import com.ahmedyejam.mks.data.local.entity.BlueprintMode
import com.ahmedyejam.mks.data.local.entity.BlueprintReviewStatus
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity
import com.ahmedyejam.mks.data.local.entity.CourseSlideEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
import com.ahmedyejam.mks.data.local.entity.FlashcardEntity
import com.ahmedyejam.mks.data.local.entity.KnowledgeStudySessionEntity
import com.ahmedyejam.mks.data.local.entity.MistakeLogEntryEntity
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptCardEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.local.entity.PromptEntity
import com.ahmedyejam.mks.data.local.entity.PromptOutputType
import com.ahmedyejam.mks.data.local.entity.PromptRunEntity
import com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity
import com.ahmedyejam.mks.data.local.entity.QuestionAssetType
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuestionType
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SessionEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentAssetEntity
import com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
import com.ahmedyejam.mks.data.local.entity.WorkspaceEntity
import com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity
import com.ahmedyejam.mks.data.model.ArticleGenerationConfig
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.data.model.ExportResult
import com.ahmedyejam.mks.data.model.FlashcardGenerationConfig
import com.ahmedyejam.mks.data.model.SlideGenerationConfig
import com.ahmedyejam.mks.data.preview.CategoryMergePreviewService
import com.ahmedyejam.mks.data.preview.ClearMarksPreviewService
import com.ahmedyejam.mks.data.preview.DeletePreviewService
import com.ahmedyejam.mks.data.repair.AssetReferenceAuditService
import com.ahmedyejam.mks.data.simulation.ChangeSimulationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val questionAssetDao: QuestionAssetDao,
    private val sourceDocumentDao: SourceDocumentDao,
    private val sourceDocumentAssetDao: com.ahmedyejam.mks.data.local.dao.SourceDocumentAssetDao,
    private val assetReferenceDao: AssetReferenceDao,
    private val fileManager: FileManager,
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val questionCategoryDao: QuestionCategoryDao,
    private val flashcardDeckDao: FlashcardDeckDao,
    private val flashcardDao: FlashcardDao,
    private val slideshowCourseDao: SlideshowCourseDao,
    private val courseSlideDao: CourseSlideDao,
    private val annotationDao: AnnotationDao
) {

    fun getQuestionAssets(questionId: Long): Flow<List<QuestionAssetEntity>> =
        questionAssetDao.getAssetsByQuestionId(questionId)


    suspend fun deleteQuestionAsset(asset: QuestionAssetEntity) {
        val now = System.currentTimeMillis()
        softDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id, now)
        questionAssetDao.softDeleteAssetById(asset.id, now)
    }
    suspend fun replaceOwnerAssetReferences(ownerType: String, ownerId: Long, paths: List<String?>) {
        val cleaned = paths
            .mapNotNull { it?.trim()?.takeIf { value -> isTrackableLocalAsset(value) } }
            .distinct()
        val previousPaths = assetReferenceDao.getReferencesForOwner(ownerType, ownerId)
            .map { it.path }
            .distinct()
        assetReferenceDao.replaceOwnerReferences(ownerType, ownerId, cleaned)
        previousPaths
            .filterNot { it in cleaned }
            .forEach { path ->
                if (assetReferenceDao.countReferencesForPath(path) == 0) {
                    fileManager.deleteImage(path)
                }
            }
    }
    suspend fun releaseOwnerAssets(ownerType: String, ownerId: Long) {
        val references = assetReferenceDao.getReferencesForOwner(ownerType, ownerId)
        assetReferenceDao.deleteReferencesForOwner(ownerType, ownerId)
        references.map { it.path }.distinct().forEach { path ->
            if (assetReferenceDao.countReferencesForPath(path) == 0) {
                fileManager.deleteImage(path)
            }
        }
    }
    suspend fun softDeleteOwnerAnnotations(ownerType: String, ownerId: Long, deletedAt: Long) {
        if (ownerType in AnnotationOwnerType.all) {
            annotationDao.softDeleteAnnotationsByOwner(ownerType, ownerId, deletedAt)
        }
    }
    suspend fun restoreOwnerAnnotations(ownerType: String, ownerId: Long, updatedAt: Long) {
        if (ownerType in AnnotationOwnerType.all) {
            annotationDao.restoreAnnotationsByOwner(ownerType, ownerId, updatedAt)
        }
    }
    suspend fun permanentlyDeleteOwnerAnnotations(ownerType: String, ownerId: Long) {
        if (ownerType in AnnotationOwnerType.all) {
            annotationDao.permanentlyDeleteAnnotationsByOwner(ownerType, ownerId)
        }
    }
    suspend fun softDeleteQuestionAnnotationTree(questionId: Long, deletedAt: Long) {
        questionAssetDao.getAssetsByQuestionIdNow(questionId).forEach { asset ->
            softDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id, deletedAt)
        }
        softDeleteOwnerAnnotations(AnnotationOwnerType.QUESTION, questionId, deletedAt)
    }
    suspend fun permanentlyDeleteQuestionAnnotationTree(questionId: Long) {
        questionAssetDao.getAssetsByQuestionIdIncludingDeleted(questionId).forEach { asset ->
            permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.ASSET, asset.id)
        }
        permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.QUESTION, questionId)
    }
    suspend fun softDeleteQuizAnnotationTree(quizId: Long, deletedAt: Long) {
        questionDao.getQuestionsByQuizIdNow(quizId).forEach { question ->
            softDeleteQuestionAnnotationTree(question.id, deletedAt)
        }
    }
    suspend fun permanentlyDeleteQuizAnnotationTree(quizId: Long) {
        questionDao.getQuestionsByQuizIdIncludingDeleted(quizId).forEach { question ->
            permanentlyDeleteQuestionAnnotationTree(question.id)
        }
    }
    suspend fun softDeleteSlideshowAnnotationTree(courseId: Long, deletedAt: Long) {
        courseSlideDao.getSlidesByCourseIdNow(courseId).forEach { slide ->
            softDeleteOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id, deletedAt)
        }
    }
    suspend fun restoreSlideshowAnnotationTree(courseId: Long, updatedAt: Long) {
        courseSlideDao.getSlidesByCourseIdNow(courseId).forEach { slide ->
            restoreOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id, updatedAt)
        }
    }
    suspend fun permanentlyDeleteSlideshowAnnotationTree(courseId: Long) {
        courseSlideDao.getSlidesByCourseIdIncludingDeleted(courseId).forEach { slide ->
            permanentlyDeleteOwnerAnnotations(AnnotationOwnerType.SLIDE, slide.id)
        }
    }
    suspend fun syncQuestionCategories(questionId: Long, categories: List<String>) {
        questionCategoryDao.replaceCategories(questionId, categories)
    }
    suspend fun releaseQuestionAssets(question: QuestionEntity) {
        questionAssetDao.getAssetsByQuestionIdIncludingDeleted(question.id).forEach { asset ->
            releaseOwnerAssets("question_asset", asset.id)
        }
        releaseOwnerAssets("question", question.id)
    }
    suspend fun releaseQuizTreeAssets(quiz: QuizEntity) {
        releaseOwnerAssets("quiz", quiz.id)
        questionDao.getQuestionsByQuizIdIncludingDeleted(quiz.id).forEach { releaseQuestionAssets(it) }
    }
    suspend fun releaseBookTreeAssets(book: BookEntity) {
        releaseOwnerAssets("book", book.id)
        quizDao.getQuizzesByBookId(book.id).first().forEach { releaseQuizTreeAssets(it) }
        flashcardDeckDao.getFlashcardDecksByBookId(book.id).first().forEach { deck ->
            releaseOwnerAssets("flashcard_deck", deck.id)
            flashcardDao.getFlashcardsByDeckId(deck.id).first().forEach { card ->
                releaseOwnerAssets("flashcard", card.id)
            }
        }
        slideshowCourseDao.getCoursesByBookId(book.id).first().forEach { course ->
            releaseOwnerAssets("slideshow_course", course.id)
            courseSlideDao.getSlidesByCourseId(course.id).first().forEach { slide ->
                releaseOwnerAssets("course_slide", slide.id)
            }
        }
        sourceDocumentDao.getSourcesByBookIdNow(book.id).forEach { source ->
            releaseOwnerAssets("source_document", source.id)
            sourceDocumentAssetDao.getAssetsBySourceIdIncludingDeleted(source.id).forEach { asset ->
                releaseOwnerAssets("source_document_asset", asset.id)
            }
        }
    }


}
