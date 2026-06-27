package com.ahmedyejam.mks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE quizId = :quizId AND deletedAt IS NULL")
    fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE quizId = :quizId AND deletedAt IS NULL")
    suspend fun getQuestionsByQuizIdNow(quizId: Long): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE quizId = :quizId")
    suspend fun getQuestionsByQuizIdIncludingDeleted(quizId: Long): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id = :id AND deletedAt IS NULL")
    suspend fun getQuestionById(id: Long): QuestionEntity?

    @Query("SELECT * FROM questions WHERE quizId = :quizId AND externalId = :externalId AND deletedAt IS NULL")
    suspend fun getQuestionByExternalId(quizId: Long, externalId: String): QuestionEntity?

    @Query("SELECT * FROM questions WHERE id IN (:ids) AND deletedAt IS NULL")
    suspend fun getQuestionsByIds(ids: List<Long>): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long>

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Update
    suspend fun updateQuestions(questions: List<QuestionEntity>)

    @Query("UPDATE questions SET deletedAt = :deletedAt, updatedAt = :deletedAt, lastEditedAt = :deletedAt WHERE id = :questionId")
    suspend fun softDeleteQuestionById(questionId: Long, deletedAt: Long)


    @Query("UPDATE questions SET deletedAt = :deletedAt, updatedAt = :deletedAt, lastEditedAt = :deletedAt WHERE quizId = :quizId AND deletedAt IS NULL")
    suspend fun softDeleteQuestionsByQuizId(quizId: Long, deletedAt: Long)

    @Query("UPDATE questions SET deletedAt = NULL, updatedAt = :updatedAt, lastEditedAt = :updatedAt WHERE id = :questionId")
    suspend fun restoreQuestionById(questionId: Long, updatedAt: Long)

    @Query("UPDATE questions SET deletedAt = NULL, updatedAt = :updatedAt, lastEditedAt = :updatedAt WHERE quizId = :quizId AND deletedAt = :deletedAtFilter")
    suspend fun restoreQuestionsByQuizId(quizId: Long, updatedAt: Long, deletedAtFilter: Long)


    @Delete
    suspend fun hardDeleteQuestion(question: QuestionEntity)

    

    @Query("""
        UPDATE questions 
        SET attempts = attempts + 1, 
            correctCount = correctCount + :isCorrectInt,
            lastStudiedAt = :now,
            timeSpentMs = timeSpentMs + :timeSpentMs,
            lastAttemptResult = :isCorrect,
            consecutiveCorrect = CASE WHEN :isCorrectInt = 1 THEN consecutiveCorrect + 1 ELSE 0 END,
            updatedAt = :now
        WHERE id = :id
    """)
    suspend fun updatePerformanceMetrics(
        id: Long, 
        isCorrect: Boolean, 
        isCorrectInt: Int,
        timeSpentMs: Long, 
        now: Long
    )

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND (:bookId = -1 OR qz.bookId = :bookId)
        ORDER BY 
            q.attempts ASC, 
            (CAST(q.correctCount AS FLOAT) / CASE WHEN q.attempts = 0 THEN 0.1 ELSE q.attempts END) ASC, 
            q.lastStudiedAt ASC 
        LIMIT :limit
    """)
    suspend fun getAdaptiveQuestionsByBook(bookId: Long, limit: Int): List<QuestionEntity>


    @Query(
        """
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
    """
    )
    fun getAllQuestionsFlow(): Flow<List<QuestionEntity>>

    @Query(
        """
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
    """
    )
    suspend fun getAllQuestionsNow(): List<QuestionEntity>


    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
    """
    )
    suspend fun countAll(): Int

    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.attempts = 0
    """
    )
    suspend fun countUnanswered(): Int

    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.notes IS NOT NULL AND TRIM(q.notes) != ''
    """
    )
    suspend fun countWithNotes(): Int

    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.isMarked = 1
    """
    )
    suspend fun countMarked(): Int

    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.isDropped = 1
    """
    )
    suspend fun countDropped(): Int

    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.attempts > 0 AND (q.correctCount < q.attempts OR q.lastAttemptResult = 0)
    """
    )
    suspend fun countMissed(): Int

    @Query(
        """
        SELECT COUNT(q.id) FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.attempts >= 2 AND q.correctCount * 2 < q.attempts
    """
    )
    suspend fun countWeak(): Int

    @Query("UPDATE questions SET categories = '[]', updatedAt = :updatedAt, lastEditedAt = :updatedAt WHERE categories IS NOT NULL AND categories != '[]'")
    suspend fun clearAllQuestionCategories(updatedAt: Long)

    @Query(
        """
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.isMarked = 1 
          AND (q.markReviewAt IS NULL OR q.markReviewAt <= :now) 
        ORDER BY COALESCE(q.markReviewAt, q.markedAt, q.updatedAt) ASC 
        LIMIT :limit
    """
    )
    suspend fun getMarkedQuestionsForReview(now: Long, limit: Int = 50): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE quizId = :quizId AND deletedAt IS NULL AND isMarked = 1 ORDER BY markedAt DESC, updatedAt DESC")
    suspend fun getMarkedQuestionsByQuiz(quizId: Long): List<QuestionEntity>



    @Query("UPDATE questions SET isMarked = 0, markedAt = NULL, markReason = NULL, markReviewAt = NULL, updatedAt = :updatedAt, lastEditedAt = :updatedAt WHERE quizId = :quizId AND isMarked = 1")
    suspend fun clearMarksForQuiz(quizId: Long, updatedAt: Long)


    @Query("UPDATE questions SET isMarked = 0, markedAt = NULL, markReason = NULL, markReviewAt = NULL, updatedAt = :updatedAt, lastEditedAt = :updatedAt WHERE id = :questionId")
    suspend fun clearQuestionMark(questionId: Long, updatedAt: Long)

    @Query("UPDATE questions SET lastStudiedAt = :reviewedAt, updatedAt = :reviewedAt WHERE id = :questionId")
    suspend fun markQuestionReviewed(questionId: Long, reviewedAt: Long)

    @Query("UPDATE questions SET markReviewAt = :reviewAt, updatedAt = :updatedAt WHERE id = :questionId AND isMarked = 1")
    suspend fun snoozeMarkedQuestion(questionId: Long, reviewAt: Long, updatedAt: Long)


    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.attempts > 0
          AND q.correctCount < q.attempts
          AND q.lastStudiedAt <= :cutoff
        ORDER BY q.lastStudiedAt ASC, q.updatedAt DESC
        LIMIT :limit
    """)
    suspend fun getWeakQuestionsDue(cutoff: Long, limit: Int = 50): List<QuestionEntity>

    @Query(
        """
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND q.markReviewAt IS NOT NULL 
          AND q.markReviewAt <= :now 
        ORDER BY q.markReviewAt ASC 
        LIMIT :limit
    """
    )
    suspend fun getMarkedQuestionsDueForReview(now: Long, limit: Int = 50): List<QuestionEntity>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
          AND (q.text LIKE '%' || :searchQuery || '%' 
               OR q.explanation LIKE '%' || :searchQuery || '%'
               OR q.options LIKE '%' || :searchQuery || '%')
    """)
    fun searchAllQuestionsFlow(searchQuery: String): Flow<List<QuestionEntity>>
}
