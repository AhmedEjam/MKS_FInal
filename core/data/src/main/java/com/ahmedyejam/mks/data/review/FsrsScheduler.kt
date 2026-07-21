package com.ahmedyejam.mks.data.review

data class FsrsState(
    val stability: Float,
    val difficulty: Float,
    val reps: Int,
)

data class FsrsReviewResult(
    val state: FsrsState,
    val nextDueAt: Long,
)

/**
 * A lightweight, FSRS-inspired spaced-repetition scheduler.
 *
 * This is **not** a faithful implementation of FSRS-4.5/6 (those carry 17–19 fitted parameters). It
 * is a three-parameter approximation — stability, difficulty, reps — tuned to produce sensible
 * day-scale review intervals without a training corpus.
 *
 * Key design point: **`stability` is expressed directly in days.** The initial stabilities below
 * (0.4d for a lapse, up to 4d for "easy") are the review interval a card earns on its first review,
 * and [computeIntervalDays] returns stability as-is. An earlier version multiplied stability by the
 * exponential-forgetting factor `-ln(0.9) ≈ 0.105`, which collapsed every first interval to a few
 * hours — a "good" card reappeared in ~5 hours instead of ~2 days, defeating the point of spacing.
 */
object FsrsScheduler {
    private const val DAY_MS = 24L * 60L * 60L * 1000L

    // Difficulty is clamped to [1, 10]; higher = harder = slower stability growth.
    private const val MIN_DIFFICULTY = 1f
    private const val MAX_DIFFICULTY = 10f
    private const val DIFFICULTY_STEP = 0.4f

    // First-review stability (= first interval, in days) by rating.
    private const val INITIAL_STABILITY_AGAIN = 0.4f
    private const val INITIAL_STABILITY_HARD = 1.0f
    private const val INITIAL_STABILITY_GOOD = 2.0f
    private const val INITIAL_STABILITY_EASY = 4.0f

    // Stability multipliers on subsequent reviews.
    private const val LAPSE_STABILITY_FACTOR = 0.5f
    private const val MIN_STABILITY = 0.1f
    private const val HARD_GROWTH = 1.2f
    private const val GOOD_GROWTH_NUMERATOR = 3f
    private const val EASY_GROWTH_NUMERATOR = 6f

    private const val RATING_AGAIN = 1
    private const val RATING_HARD = 2
    private const val RATING_GOOD = 3
    private const val RATING_EASY = 4

    /** Rating: 1=Again, 2=Hard, 3=Good, 4=Easy */
    fun review(
        currentStability: Float,
        currentDifficulty: Float,
        currentReps: Int,
        rating: Int,
        now: Long = System.currentTimeMillis(),
    ): FsrsReviewResult {
        val newReps = currentReps + 1
        val newDifficulty =
            (currentDifficulty + (RATING_GOOD - rating) * DIFFICULTY_STEP).coerceIn(MIN_DIFFICULTY, MAX_DIFFICULTY)

        val newStability = when {
            currentReps == 0 -> when (rating) {
                RATING_AGAIN -> INITIAL_STABILITY_AGAIN
                RATING_HARD -> INITIAL_STABILITY_HARD
                RATING_GOOD -> INITIAL_STABILITY_GOOD
                RATING_EASY -> INITIAL_STABILITY_EASY
                else -> INITIAL_STABILITY_GOOD
            }
            rating == RATING_AGAIN -> (currentStability * LAPSE_STABILITY_FACTOR).coerceAtLeast(MIN_STABILITY)
            rating == RATING_HARD -> currentStability * HARD_GROWTH
            rating == RATING_GOOD -> currentStability * (1f + GOOD_GROWTH_NUMERATOR / newDifficulty)
            rating == RATING_EASY -> currentStability * (1f + EASY_GROWTH_NUMERATOR / newDifficulty)
            else -> currentStability
        }

        val days = computeIntervalDays(newStability)
        val nextDueAt = now + (days.toLong() * DAY_MS)

        return FsrsReviewResult(
            state = FsrsState(newStability, newDifficulty, newReps),
            nextDueAt = nextDueAt,
        )
    }

    /**
     * Interval in days for a given stability. Stability is already calibrated as a day-scale
     * interval (see the class doc), so this returns it directly, floored so a card is never due in
     * the past.
     */
    private fun computeIntervalDays(stability: Float): Float = stability.coerceAtLeast(MIN_STABILITY)

    /** Converts a string rating ("again", "hard", "good", "easy") to FSRS numeric rating. */
    fun ratingFromString(rating: String): Int = when (rating.lowercase().trim()) {
        "again" -> RATING_AGAIN
        "hard" -> RATING_HARD
        "good" -> RATING_GOOD
        "easy" -> RATING_EASY
        else -> RATING_GOOD
    }

    /** Formats the next interval for display (e.g., "3d", "2w", "1mo"). */
    fun formatInterval(dueAt: Long, now: Long = System.currentTimeMillis()): String {
        val diffMs = dueAt - now
        if (diffMs <= 0) return "now"
        val days = (diffMs.toFloat() / DAY_MS).toInt()
        return when {
            days < DAYS_PER_WEEK -> if (days < 1) "${diffMs / MINUTE_MS}m" else "${days}d"
            days < DAYS_PER_MONTH -> "${days / DAYS_PER_WEEK}w"
            days < DAYS_PER_YEAR -> "${days / DAYS_PER_MONTH}mo"
            else -> "${days / DAYS_PER_YEAR}y"
        }
    }

    private const val MINUTE_MS = 60_000L
    private const val DAYS_PER_WEEK = 7
    private const val DAYS_PER_MONTH = 30
    private const val DAYS_PER_YEAR = 365
}
