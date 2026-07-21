package com.ahmedyejam.mks.data.review

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [FsrsScheduler], with the interval length as the headline guard.
 *
 * The scheduler shipped with `computeIntervalDays` multiplying stability by ~0.105, which collapsed
 * every first interval to a few hours — a "good" card came due in ~5h instead of ~2 days, defeating
 * spaced repetition. These tests pin the corrected day-scale intervals so that regression cannot
 * return silently. Pure JVM: [FsrsScheduler] has no Android dependencies.
 */
class FsrsSchedulerTest {

    private val now = 1_000_000_000_000L
    private val dayMs = 24L * 60L * 60L * 1000L

    private fun intervalDays(result: FsrsReviewResult): Long = (result.nextDueAt - now) / dayMs

    @Test
    fun `first good review schedules days out, not hours`() {
        // The regression guard. Reps=0, rating Good(3) → stability 2.0 → interval ~2 days.
        val result = FsrsScheduler.review(0f, 0f, 0, rating = 3, now = now)
        assertEquals("first 'good' should be ~2 days", 2L, intervalDays(result))
        assertTrue("must not come due within the same day", result.nextDueAt - now >= dayMs)
    }

    @Test
    fun `first easy review is longer than first good`() {
        val easy = FsrsScheduler.review(0f, 0f, 0, rating = 4, now = now)
        val good = FsrsScheduler.review(0f, 0f, 0, rating = 3, now = now)
        assertEquals("first 'easy' should be ~4 days", 4L, intervalDays(easy))
        assertTrue("easy must schedule further out than good", easy.nextDueAt > good.nextDueAt)
    }

    @Test
    fun `first again review is a short sub-day lapse interval`() {
        val result = FsrsScheduler.review(0f, 0f, 0, rating = 1, now = now)
        assertEquals("initial stability for a lapse is 0.4d", 0.4f, result.state.stability, 0.001f)
        // 0.4 days floored to whole days for scheduling is 0, i.e. due almost immediately.
        assertTrue("again should come due soon", result.nextDueAt - now < dayMs)
    }

    @Test
    fun `stability grows across repeated good reviews`() {
        var stability = 0f
        var difficulty = 0f
        var reps = 0
        val intervals = mutableListOf<Long>()
        repeat(4) {
            val r = FsrsScheduler.review(stability, difficulty, reps, rating = 3, now = now)
            stability = r.state.stability
            difficulty = r.state.difficulty
            reps = r.state.reps
            intervals += intervalDays(r)
        }
        // Each successive "good" must schedule at least as far out as the previous — intervals expand.
        for (i in 1 until intervals.size) {
            assertTrue("interval $i (${intervals[i]}d) should be >= interval ${i - 1} (${intervals[i - 1]}d)",
                intervals[i] >= intervals[i - 1])
        }
        assertTrue("reps should count up", reps == 4)
    }

    @Test
    fun `a lapse halves stability`() {
        // Build up some stability, then fail.
        val built = FsrsScheduler.review(10f, 5f, 3, rating = 3, now = now)
        val lapsed = FsrsScheduler.review(built.state.stability, built.state.difficulty, built.state.reps, rating = 1, now = now)
        assertTrue("a lapse must reduce stability", lapsed.state.stability < built.state.stability)
    }

    @Test
    fun `ratingFromString maps the four ratings and defaults to good`() {
        assertEquals(1, FsrsScheduler.ratingFromString("again"))
        assertEquals(2, FsrsScheduler.ratingFromString("Hard"))
        assertEquals(3, FsrsScheduler.ratingFromString("GOOD"))
        assertEquals(4, FsrsScheduler.ratingFromString(" easy "))
        assertEquals("unknown ratings fall back to good", 3, FsrsScheduler.ratingFromString("whatever"))
    }

    @Test
    fun `formatInterval renders human units`() {
        assertEquals("now", FsrsScheduler.formatInterval(now - 1, now))
        assertEquals("3d", FsrsScheduler.formatInterval(now + 3 * dayMs, now))
        assertEquals("2w", FsrsScheduler.formatInterval(now + 14 * dayMs, now))
        assertEquals("2mo", FsrsScheduler.formatInterval(now + 60 * dayMs, now))
        assertEquals("1y", FsrsScheduler.formatInterval(now + 400 * dayMs, now))
    }
}
