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
class StudyRepository @Inject constructor(
    private val mistakeLogDao: MistakeLogDao,
    private val annotationDao: AnnotationDao,
    private val bookDao: BookDao,
    private val bookRepository: BookRepository,
    private val assetRepository: AssetRepository
) {

    fun getAllMistakes(): Flow<List<MistakeLogEntryEntity>> = mistakeLogDao.getAllMistakes()

    fun getMistakesByBookId(bookId: Long): Flow<List<MistakeLogEntryEntity>> = mistakeLogDao.getMistakesByBookId(bookId)

    fun getMistakesByQuizId(quizId: Long): Flow<List<MistakeLogEntryEntity>> = mistakeLogDao.getMistakesByQuizId(quizId)


    suspend fun insertMistake(entry: MistakeLogEntryEntity): Long = mistakeLogDao.insertMistake(entry)

    // Annotations: polymorphic highlights/margin notes owned by QUESTION, SLIDE, NOTE, SOURCE, or ASSET.

    suspend fun deleteMistake(entry: MistakeLogEntryEntity) {
        mistakeLogDao.softDeleteMistakeById(entry.id, System.currentTimeMillis())
    }


    fun getAnnotationsByWorkspaceId(workspaceId: Long): Flow<List<AnnotationEntity>> =
        annotationDao.getAnnotationsByWorkspaceId(workspaceId)


    fun getAnnotationsByBookId(bookId: Long): Flow<List<AnnotationEntity>> =
        annotationDao.getAnnotationsByBookId(bookId)


    fun getAnnotationsByOwner(ownerType: String, ownerId: Long): Flow<List<AnnotationEntity>> =
        annotationDao.getAnnotationsByOwner(ownerType, ownerId)


    suspend fun getAnnotationById(annotationId: Long): AnnotationEntity? =
        annotationDao.getAnnotationById(annotationId)


    suspend fun insertAnnotation(annotation: AnnotationEntity): Long {
        require(annotation.ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: ${annotation.ownerType}" }
        val now = System.currentTimeMillis()
        return annotationDao.insertAnnotation(annotation.copy(createdAt = annotation.createdAt.takeIf { it > 0 } ?: now, updatedAt = now))
    }


    suspend fun updateAnnotation(annotation: AnnotationEntity) {
        require(annotation.ownerType in AnnotationOwnerType.all) { "Unsupported annotation ownerType: ${annotation.ownerType}" }
        annotationDao.updateAnnotation(annotation.copy(updatedAt = System.currentTimeMillis()))
    }


}
