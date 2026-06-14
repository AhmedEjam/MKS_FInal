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
class KnowledgeRepository @Inject constructor(
    private val flashcardDeckDao: FlashcardDeckDao,
    private val flashcardDao: FlashcardDao,
    private val slideshowCourseDao: SlideshowCourseDao,
    private val courseSlideDao: CourseSlideDao,
    private val noteBlueprintDao: NoteBlueprintDao,
    private val noteCollectionDao: com.ahmedyejam.mks.data.local.dao.NoteCollectionDao,
    private val promptDao: PromptDao,
    private val promptDeckDao: PromptDeckDao,
    private val promptCardDao: PromptCardDao,
    private val promptRunDao: PromptRunDao,
    private val knowledgeStudySessionDao: KnowledgeStudySessionDao,
    private val fileManager: FileManager,
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val sourceDocumentDao: SourceDocumentDao,
    private val bookRepository: BookRepository,
    private val assetRepository: AssetRepository
) {

    fun getFlashcardDecksByBookId(bookId: Long): Flow<List<FlashcardDeckEntity>> =
        flashcardDeckDao.getFlashcardDecksByBookId(bookId)


    suspend fun getFlashcardDeckById(id: Long) = flashcardDeckDao.getFlashcardDeckById(id)


    suspend fun insertFlashcardDeck(deck: FlashcardDeckEntity): Long {
        var finalDeck = deck
        if (deck.coverImage?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(deck.coverImage!!)
            if (localPath != null) finalDeck = deck.copy(coverImage = localPath)
        }
        val id = flashcardDeckDao.insertFlashcardDeck(finalDeck)
        assetRepository.replaceOwnerAssetReferences("flashcard_deck", id, listOf(finalDeck.coverImage))
        refreshFlashcardDeckStats(id)
        bookRepository.refreshBookStats(finalDeck.bookId)
        return id
    }


    suspend fun updateFlashcardDeck(deck: FlashcardDeckEntity) {
        val updated = deck.copy(updatedAt = System.currentTimeMillis(), lastEditedAt = System.currentTimeMillis())
        flashcardDeckDao.updateFlashcardDeck(updated)
        assetRepository.replaceOwnerAssetReferences("flashcard_deck", updated.id, listOf(updated.coverImage))
        val refreshedDeck = refreshFlashcardDeckStats(deck.id) ?: updated
        bookRepository.refreshBookStats(refreshedDeck.bookId)
    }


    suspend fun deleteFlashcardDeck(deck: FlashcardDeckEntity) {
        val now = System.currentTimeMillis()
        flashcardDao.softDeleteAllCardsInDeck(deck.id, now)
        flashcardDeckDao.softDeleteFlashcardDeckById(deck.id, now)
        bookRepository.refreshBookStats(deck.bookId)
    }


    suspend fun restoreFlashcardDeck(deckId: Long) {
        val deck = flashcardDeckDao.getFlashcardDeckByIdIncludingDeleted(deckId) ?: return
        val book = bookDao.getBookByIdIncludingDeleted(deck.bookId)
        if (book != null && book.deletedAt != null) {
            bookRepository.restoreBook(book.id)
        }
        val now = System.currentTimeMillis()
        val deletedAtFilter = deck.deletedAt ?: now
        flashcardDeckDao.restoreFlashcardDeckById(deckId, now)
        flashcardDao.restoreAllCardsInDeck(deckId, now, deletedAtFilter)
        bookRepository.refreshBookStats(deck.bookId)
    }


    suspend fun permanentlyDeleteFlashcardDeck(deck: FlashcardDeckEntity) {
        val cards = flashcardDao.getFlashcardsByDeckId(deck.id).first()
        cards.forEach { assetRepository.releaseOwnerAssets("flashcard", it.id) }
        assetRepository.releaseOwnerAssets("flashcard_deck", deck.id)
        flashcardDeckDao.hardDeleteFlashcardDeck(deck)
        bookRepository.refreshBookStats(deck.bookId)
    }


    fun getFlashcardsByDeckId(deckId: Long): Flow<List<FlashcardEntity>> = flashcardDao.getFlashcardsByDeckId(deckId)


    suspend fun insertFlashcard(card: FlashcardEntity): Long {
        var finalCard = card
        if (card.imagePath?.startsWith("http", ignoreCase = true) == true) {
            val localPath = fileManager.downloadAndSaveImage(card.imagePath!!)
            if (localPath != null) finalCard = card.copy(imagePath = localPath)
        }
        val id = flashcardDao.insertFlashcard(finalCard)
        assetRepository.replaceOwnerAssetReferences("flashcard", id, listOf(finalCard.imagePath))
        val deck = refreshFlashcardDeckStats(finalCard.deckId)
        deck?.let { bookRepository.refreshBookStats(it.bookId) }
        return id
    }


    suspend fun updateFlashcard(card: FlashcardEntity) {
        val updated = card.copy(updatedAt = System.currentTimeMillis())
        flashcardDao.updateFlashcard(updated)
        assetRepository.replaceOwnerAssetReferences("flashcard", updated.id, listOf(updated.imagePath))
        val deck = refreshFlashcardDeckStats(card.deckId)
        deck?.let { bookRepository.refreshBookStats(it.bookId) }
    }


    suspend fun deleteFlashcard(card: FlashcardEntity) {
        flashcardDao.softDeleteFlashcardById(card.id, System.currentTimeMillis())
        val deck = refreshFlashcardDeckStats(card.deckId)
        deck?.let { bookRepository.refreshBookStats(it.bookId) }
    }


    suspend fun restoreFlashcard(cardId: Long) {
        flashcardDao.restoreFlashcardById(cardId, System.currentTimeMillis())
    }


    suspend fun permanentlyDeleteFlashcard(card: FlashcardEntity) {
        assetRepository.releaseOwnerAssets("flashcard", card.id)
        flashcardDao.hardDeleteFlashcard(card)
        val deck = refreshFlashcardDeckStats(card.deckId)
        deck?.let { bookRepository.refreshBookStats(it.bookId) }
    }


    fun getSlidesByCourseId(courseId: Long): Flow<List<CourseSlideEntity>> = courseSlideDao.getSlidesByCourseId(courseId)

    fun getPromptsByBookId(bookId: Long): Flow<List<PromptEntity>> = promptDao.getPromptsByBookId(bookId)

    suspend fun getPromptById(id: Long) = promptDao.getPromptById(id)

    suspend fun insertPrompt(prompt: PromptEntity): Long {
        return promptDao.insertPrompt(prompt.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
    }

    suspend fun updatePrompt(prompt: PromptEntity) {
        promptDao.updatePrompt(prompt.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deletePrompt(prompt: PromptEntity) = promptDao.softDeletePromptById(prompt.id, System.currentTimeMillis())
    suspend fun refreshFlashcardDeckStats(deckId: Long): FlashcardDeckEntity? {
        val deck = flashcardDeckDao.getFlashcardDeckById(deckId) ?: return null
        val cards = flashcardDao.getFlashcardsByDeckId(deckId).first()
        val totalCards = cards.size
        val studiedCards = cards.count { it.attempts > 0 }
        val totalAttempts = cards.sumOf { it.attempts }
        val totalCorrect = cards.sumOf { it.correctCount }
        val mastery = if (totalAttempts == 0) 0f else totalCorrect.toFloat() / totalAttempts
        val now = System.currentTimeMillis()
        val refreshed = deck.copy(
            cardCount = totalCards,
            studiedCount = studiedCards,
            masteryPercentage = mastery.coerceIn(0f, 1f),
            updatedAt = now
        )
        flashcardDeckDao.updateFlashcardDeck(refreshed)
        return refreshed
    }


    @OptIn(ExperimentalCoroutinesApi::class)

}
