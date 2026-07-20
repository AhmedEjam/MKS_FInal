package com.ahmedyejam.mks.data.repository

import com.ahmedyejam.mks.data.local.dao.StudyRunDao
import com.ahmedyejam.mks.data.local.entity.StudyRunEntity
import com.ahmedyejam.mks.data.model.StartStudyRun
import com.ahmedyejam.mks.data.model.StudyContentType
import com.ahmedyejam.mks.data.model.StudyRunProgress
import com.ahmedyejam.mks.data.model.StudyRunRepository
import com.ahmedyejam.mks.data.model.StudyRunState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyRunRepositoryImpl @Inject constructor(
    private val studyRunDao: StudyRunDao,
) : StudyRunRepository {

    override suspend fun start(request: StartStudyRun): Long = withContext(Dispatchers.IO) {
        val entity = StudyRunEntity(
            contentType = request.contentType.name,
            contentId = request.contentId,
            orderedItemIds = request.orderedItemIds,
            configurationJson = request.configurationJson,
            startedAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        )
        studyRunDao.insert(entity)
    }

    override suspend fun saveProgress(runId: Long, progress: StudyRunProgress) = withContext(Dispatchers.IO) {
        val entity = studyRunDao.getById(runId) ?: return@withContext
        studyRunDao.update(entity.copy(
            currentIndex = progress.currentIndex,
            completedItemIds = progress.completedItemIds.toList(),
            stateJson = progress.stateJson ?: entity.stateJson,
            updatedAt = System.currentTimeMillis(),
        ))
    }

    override suspend fun resume(runId: Long): StudyRunState? = withContext(Dispatchers.IO) {
        studyRunDao.getById(runId)?.toState()
    }

    override suspend fun complete(runId: Long) = withContext(Dispatchers.IO) {
        studyRunDao.markCompleted(runId)
    }

    override suspend fun getLatestIncomplete(contentType: StudyContentType, contentId: Long): StudyRunState? =
        withContext(Dispatchers.IO) {
            studyRunDao.getLatestIncomplete(contentType.name, contentId)?.toState()
        }

    override suspend fun getAllIncomplete(): List<StudyRunState> = withContext(Dispatchers.IO) {
        studyRunDao.getAllIncomplete().map { it.toState() }
    }

    private fun StudyRunEntity.toState() = StudyRunState(
        runId = id,
        contentType = StudyContentType.valueOf(contentType),
        contentId = contentId,
        orderedItemIds = orderedItemIds,
        currentIndex = currentIndex,
        completedItemIds = completedItemIds.toSet(),
        startedAt = startedAt,
        updatedAt = updatedAt,
        configurationJson = configurationJson,
        stateJson = stateJson,
    )
}
