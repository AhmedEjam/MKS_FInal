package com.ahmedyejam.mks.data.model

enum class StudyContentType {
    QUIZ,
    ADAPTIVE_QUIZ,
    FLASHCARD_DECK,
    SLIDESHOW,
    NOTE,
    REVIEW_QUEUE,
}

data class StudyRunState(
    /**
     * Identifier of the persisted run.
     *
     * Required to call [StudyRunRepository.saveProgress] or [StudyRunRepository.complete] after
     * resuming — without it a caller can read a run back but has no handle to keep writing to it.
     */
    val runId: Long,
    val contentType: StudyContentType,
    val contentId: Long,
    val orderedItemIds: List<Long>,
    val currentIndex: Int,
    val completedItemIds: Set<Long>,
    val startedAt: Long,
    val updatedAt: Long,
    val configurationJson: String?,
    val stateJson: String?,
)

data class StudyRunProgress(
    val currentIndex: Int,
    val completedItemIds: Set<Long>,
    val stateJson: String? = null,
)

data class StartStudyRun(
    val contentType: StudyContentType,
    val contentId: Long,
    val orderedItemIds: List<Long>,
    val configurationJson: String? = null,
)

interface StudyRunRepository {
    suspend fun start(request: StartStudyRun): Long
    suspend fun saveProgress(runId: Long, progress: StudyRunProgress)
    suspend fun resume(runId: Long): StudyRunState?
    suspend fun complete(runId: Long)
    suspend fun getLatestIncomplete(contentType: StudyContentType, contentId: Long): StudyRunState?
    suspend fun getAllIncomplete(): List<StudyRunState>
}
