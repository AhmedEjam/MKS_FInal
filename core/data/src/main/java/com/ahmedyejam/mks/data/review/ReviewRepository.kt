package com.ahmedyejam.mks.data.review

import com.ahmedyejam.mks.data.local.dao.AnnotationDao
import com.ahmedyejam.mks.data.local.dao.BookDao
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao
import com.ahmedyejam.mks.data.local.dao.FlashcardDao
import com.ahmedyejam.mks.data.local.dao.MistakeLogDao
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao
import com.ahmedyejam.mks.data.local.dao.QuestionDao
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.ui.MksRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val flashcardDao: FlashcardDao,
    private val noteBlueprintDao: NoteBlueprintDao,
    private val mistakeLogDao: MistakeLogDao,
    private val questionDao: QuestionDao,
    private val courseSlideDao: CourseSlideDao,
    private val annotationDao: AnnotationDao,
    private val slideshowCourseDao: SlideshowCourseDao,
    private val bookDao: BookDao,
) {
    suspend fun loadSummary(workspaceId: Long, now: Long = System.currentTimeMillis()): ReviewDashboardSummary =
        withContext(Dispatchers.IO) {
            if (workspaceId <= 0L) return@withContext ReviewDashboardSummary()
            val weakCutoff = now - 7L * 24L * 60L * 60L * 1000L
            ReviewDashboardSummary(
                dueFlashcards = flashcardDao.countDueFlashcardsByWorkspace(now, workspaceId),
                dueBlueprints = noteBlueprintDao.countDueBlueprintsByWorkspace(now, workspaceId),
                dueMistakes = mistakeLogDao.countDueMistakesByWorkspace(now, workspaceId),
                pendingMistakes = mistakeLogDao.countPendingMistakesByWorkspace(now, workspaceId),
                markedQuestions = questionDao.getMarkedQuestionsForReviewByWorkspace(now, workspaceId, 200).size,
                weakQuestions = questionDao.getWeakQuestionsDueByWorkspace(weakCutoff, workspaceId, 200).size,
                unfinishedSlides = courseSlideDao.countUnfinishedSlidesByWorkspace(workspaceId),
            )
        }

    suspend fun loadQueues(workspaceId: Long, now: Long = System.currentTimeMillis()): List<ReviewQueueItem> =
        withContext(Dispatchers.IO) {
            if (workspaceId <= 0L) return@withContext emptyList()
            val flashcards =
                flashcardDao.getDueFlashcardsByWorkspace(now, workspaceId, 20).map {
                    ReviewQueueItem(
                        it.id.toString(),
                        ReviewQueueType.FLASHCARD,
                        it.frontText.take(90),
                        it.backText.take(120),
                        it.dueAt,
                        MksRoutes.flashcards(it.deckId),
                    )
                }
            val blueprints =
                noteBlueprintDao.getDueBlueprintsByWorkspace(now, workspaceId, 20).map {
                    ReviewQueueItem(
                        it.id.toString(),
                        ReviewQueueType.BLUEPRINT,
                        it.title,
                        it.summary,
                        it.lastReviewedAt,
                        MksRoutes.blueprint(it.id),
                    )
                }
            val mistakes =
                mistakeLogDao.getDueMistakesByWorkspace(now, workspaceId, 20).map {
                    ReviewQueueItem(
                        it.id.toString(),
                        ReviewQueueType.MISTAKE,
                        it.correctConcept ?: "Mistake #${it.id}",
                        it.preventionNote ?: it.userReason,
                        it.reviewAt,
                        MksRoutes.REVIEW_DASHBOARD,
                    )
                }
            val marked =
                questionDao.getMarkedQuestionsForReviewByWorkspace(now, workspaceId, 20).map {
                    ReviewQueueItem(
                        it.id.toString(),
                        ReviewQueueType.MARKED_QUESTION,
                        it.text.take(90),
                        it.markReason,
                        it.markReviewAt,
                        MksRoutes.quizQuestions(it.quizId),
                    )
                }
            val weakCutoff = now - 7L * 24L * 60L * 60L * 1000L
            val weak =
                questionDao.getWeakQuestionsDueByWorkspace(weakCutoff, workspaceId, 20).map {
                    ReviewQueueItem(
                        it.id.toString(),
                        ReviewQueueType.WEAK_QUESTION,
                        it.text.take(90),
                        "${it.correctCount}/${it.attempts} correct",
                        it.lastStudiedAt,
                        MksRoutes.quizQuestions(it.quizId),
                    )
                }
            val unfinishedSlides =
                courseSlideDao.getUnfinishedSlidesByWorkspace(workspaceId, 20).mapNotNull { slide ->
                    val course = slideshowCourseDao.getCourseById(slide.courseId) ?: return@mapNotNull null
                    ReviewQueueItem(
                        slide.id.toString(),
                        ReviewQueueType.UNFINISHED_SLIDE,
                        slide.title.take(90),
                        course.title,
                        slide.updatedAt,
                        MksRoutes.slideshow(course.id, slide.id),
                    )
                }
            val annotations =
                annotationDao.getAnnotationsByWorkspaceId(workspaceId).first().take(20).map {
                    ReviewQueueItem(
                        it.id.toString(),
                        ReviewQueueType.ANNOTATION,
                        (it.selectedText ?: it.noteBody ?: "Annotation #${it.id}").take(90),
                        it.noteBody?.take(120),
                        it.updatedAt,
                        null,
                    )
                }
            flashcards + blueprints + mistakes + marked + weak + unfinishedSlides + annotations
        }

    suspend fun markReviewed(item: ReviewQueueItem) =
        withContext(Dispatchers.IO) {
            val itemId = item.id.toLongOrNull() ?: return@withContext
            val now = System.currentTimeMillis()
            when (item.type) {
                ReviewQueueType.FLASHCARD -> flashcardDao.markReviewed(itemId, now, now + 3L * 24L * 60L * 60L * 1000L)
                ReviewQueueType.BLUEPRINT -> noteBlueprintDao.markReviewed(itemId, now)
                ReviewQueueType.MISTAKE -> mistakeLogDao.markFixed(itemId, now)
                ReviewQueueType.MARKED_QUESTION -> questionDao.clearQuestionMark(itemId, now)
                ReviewQueueType.WEAK_QUESTION -> questionDao.markQuestionReviewed(itemId, now)
                ReviewQueueType.UNFINISHED_SLIDE -> {
                    val slide = courseSlideDao.getSlideById(itemId)
                    if (slide != null) courseSlideDao.updateSlide(slide.copy(isCompleted = true, updatedAt = now))
                }
                ReviewQueueType.ANNOTATION -> Unit
            }
        }

    suspend fun undoMarkReviewed(item: ReviewQueueItem) =
        withContext(Dispatchers.IO) {
            val itemId = item.id.toLongOrNull() ?: return@withContext
            when (item.type) {
                ReviewQueueType.UNFINISHED_SLIDE -> {
                    val slide = courseSlideDao.getSlideById(itemId)
                    if (slide != null) courseSlideDao.updateSlide(slide.copy(isCompleted = false, updatedAt = System.currentTimeMillis()))
                }
                else -> Unit
            }
        }

    suspend fun snooze(
        item: ReviewQueueItem,
        millis: Long,
    ) = withContext(Dispatchers.IO) {
        val itemId = item.id.toLongOrNull() ?: return@withContext
        val now = System.currentTimeMillis()
        val due = now + millis
        when (item.type) {
            ReviewQueueType.FLASHCARD -> flashcardDao.snooze(itemId, due, now)
            ReviewQueueType.MISTAKE -> mistakeLogDao.snooze(itemId, due, now)
            ReviewQueueType.BLUEPRINT -> noteBlueprintDao.snoozeBlueprint(itemId, due)
            ReviewQueueType.WEAK_QUESTION -> questionDao.markQuestionReviewed(itemId, now)
            ReviewQueueType.MARKED_QUESTION -> questionDao.snoozeMarkedQuestion(itemId, due, now)
            else -> Unit
        }
    }
}
