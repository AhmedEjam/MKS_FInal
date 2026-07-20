package com.ahmedyejam.mks.data.repository

import com.ahmedyejam.mks.data.local.dao.StudyRunDao
import com.ahmedyejam.mks.data.local.entity.StudyRunEntity
import com.ahmedyejam.mks.data.model.StartStudyRun
import com.ahmedyejam.mks.data.model.StudyContentType
import com.ahmedyejam.mks.data.model.StudyRunProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [StudyRunRepositoryImpl].
 *
 * `study_runs` shipped as a schema migration (v31 -> v32) but currently has no consumer outside DI
 * wiring, so none of this logic has ever executed against real usage. These tests pin the contract
 * before anything adopts it — in particular the round-trip through the entity/state mapping, where
 * `completedItemIds` crosses a List/Set boundary in both directions.
 *
 * Deliberately a plain JVM test with an in-memory fake DAO: no Robolectric, so it runs without an
 * Android runtime or a downloaded android-all artifact.
 */
class StudyRunRepositoryImplTest {

    /** In-memory stand-in that mirrors the real DAO's filtering semantics. */
    private class FakeStudyRunDao : StudyRunDao {
        val rows = mutableMapOf<Long, StudyRunEntity>()
        private var nextId = 1L

        override suspend fun insert(run: StudyRunEntity): Long {
            val id = nextId++
            rows[id] = run.copy(id = id)
            return id
        }

        override suspend fun update(run: StudyRunEntity) {
            rows[run.id] = run
        }

        override suspend fun getById(id: Long): StudyRunEntity? =
            rows[id]?.takeIf { it.deletedAt == null }

        override suspend fun getLatestIncomplete(contentType: String, contentId: Long): StudyRunEntity? =
            rows.values
                .filter {
                    it.contentType == contentType &&
                        it.contentId == contentId &&
                        !it.isCompleted &&
                        it.deletedAt == null
                }
                .maxByOrNull { it.updatedAt }

        override suspend fun getAllIncomplete(): List<StudyRunEntity> =
            rows.values
                .filter { !it.isCompleted && it.deletedAt == null }
                .sortedByDescending { it.updatedAt }

        override suspend fun markCompleted(id: Long, completedAt: Long) {
            rows[id]?.let { rows[id] = it.copy(isCompleted = true, completedAt = completedAt, updatedAt = completedAt) }
        }

        override suspend fun softDelete(id: Long, deletedAt: Long) {
            rows[id]?.let { rows[id] = it.copy(deletedAt = deletedAt, updatedAt = deletedAt) }
        }

        override fun getDeletedRuns(): Flow<List<StudyRunEntity>> =
            flowOf(rows.values.filter { it.deletedAt != null })
    }

    private fun repo(dao: StudyRunDao) = StudyRunRepositoryImpl(dao)

    @Test
    fun `start persists the requested run and returns its id`() = runTest {
        val dao = FakeStudyRunDao()
        val id = repo(dao).start(
            StartStudyRun(
                contentType = StudyContentType.FLASHCARD_DECK,
                contentId = 7L,
                orderedItemIds = listOf(10L, 11L, 12L),
                configurationJson = """{"shuffle":true}""",
            ),
        )

        val stored = dao.rows.getValue(id)
        assertEquals("FLASHCARD_DECK", stored.contentType)
        assertEquals(7L, stored.contentId)
        assertEquals(listOf(10L, 11L, 12L), stored.orderedItemIds)
        assertEquals("""{"shuffle":true}""", stored.configurationJson)
        assertTrue("a freshly started run must not be complete", !stored.isCompleted)
    }

    @Test
    fun `saveProgress then resume round-trips index and completed items`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)
        val id = repository.start(
            StartStudyRun(StudyContentType.SLIDESHOW, contentId = 3L, orderedItemIds = listOf(1L, 2L, 3L)),
        )

        repository.saveProgress(
            id,
            StudyRunProgress(currentIndex = 2, completedItemIds = setOf(1L, 2L), stateJson = """{"note":"x"}"""),
        )

        val resumed = repository.resume(id)
        requireNotNull(resumed)
        assertEquals(StudyContentType.SLIDESHOW, resumed.contentType)
        assertEquals(2, resumed.currentIndex)
        assertEquals(setOf(1L, 2L), resumed.completedItemIds)
        assertEquals("""{"note":"x"}""", resumed.stateJson)
        assertEquals(listOf(1L, 2L, 3L), resumed.orderedItemIds)
    }

    @Test
    fun `saveProgress preserves existing stateJson when none is supplied`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)
        val id = repository.start(StartStudyRun(StudyContentType.NOTE, 1L, emptyList()))

        repository.saveProgress(id, StudyRunProgress(0, emptySet(), stateJson = """{"keep":"me"}"""))
        // Second save omits stateJson; the previous value must survive rather than being nulled.
        repository.saveProgress(id, StudyRunProgress(1, setOf(5L), stateJson = null))

        val resumed = requireNotNull(repository.resume(id))
        assertEquals("""{"keep":"me"}""", resumed.stateJson)
        assertEquals(1, resumed.currentIndex)
    }

    @Test
    fun `saveProgress on a missing run is a no-op rather than a crash`() = runTest {
        val dao = FakeStudyRunDao()
        repo(dao).saveProgress(999L, StudyRunProgress(1, setOf(1L)))
        assertTrue("no row should be created for an unknown id", dao.rows.isEmpty())
    }

    @Test
    fun `complete removes the run from the incomplete queries`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)
        val id = repository.start(StartStudyRun(StudyContentType.QUIZ, 4L, listOf(1L)))

        assertEquals(1, repository.getAllIncomplete().size)

        repository.complete(id)

        assertTrue("completed run should leave the incomplete queue", repository.getAllIncomplete().isEmpty())
        assertNull(
            "completed run should not be offered for resume",
            repository.getLatestIncomplete(StudyContentType.QUIZ, 4L),
        )
    }

    @Test
    fun `getLatestIncomplete returns the most recently updated run for that content`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)

        val older = repository.start(StartStudyRun(StudyContentType.FLASHCARD_DECK, 1L, listOf(1L)))
        val newer = repository.start(StartStudyRun(StudyContentType.FLASHCARD_DECK, 1L, listOf(2L)))
        dao.rows[older] = dao.rows.getValue(older).copy(updatedAt = 1_000L)
        dao.rows[newer] = dao.rows.getValue(newer).copy(updatedAt = 2_000L)

        val latest = requireNotNull(repository.getLatestIncomplete(StudyContentType.FLASHCARD_DECK, 1L))
        assertEquals(listOf(2L), latest.orderedItemIds)
    }

    @Test
    fun `getLatestIncomplete does not cross content type or content id`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)
        repository.start(StartStudyRun(StudyContentType.FLASHCARD_DECK, 1L, listOf(1L)))

        assertNull(
            "a slideshow must not resume a flashcard run",
            repository.getLatestIncomplete(StudyContentType.SLIDESHOW, 1L),
        )
        assertNull(
            "deck 2 must not resume deck 1's run",
            repository.getLatestIncomplete(StudyContentType.FLASHCARD_DECK, 2L),
        )
    }

    @Test
    fun `soft deleted runs are excluded from resume and the incomplete queue`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)
        val id = repository.start(StartStudyRun(StudyContentType.REVIEW_QUEUE, 9L, listOf(1L)))

        dao.softDelete(id, deletedAt = 5_000L)

        assertNull("soft-deleted run should not resume", repository.resume(id))
        assertTrue("soft-deleted run should leave the queue", repository.getAllIncomplete().isEmpty())
    }

    @Test
    fun `every declared content type survives the enum round trip`() = runTest {
        val dao = FakeStudyRunDao()
        val repository = repo(dao)
        // toState() calls StudyContentType.valueOf on a stored string; a rename on either side
        // would throw here rather than at runtime in a player.
        StudyContentType.entries.forEachIndexed { index, type ->
            val id = repository.start(StartStudyRun(type, index.toLong(), emptyList()))
            assertEquals(type, requireNotNull(repository.resume(id)).contentType)
        }
    }
}
