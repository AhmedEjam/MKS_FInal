package com.ahmedyejam.mks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ahmedyejam.mks.data.local.entity.QuestionCategoryEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<QuestionCategoryEntity>)

    @Query("DELETE FROM question_categories WHERE questionId = :questionId")
    suspend fun deleteCategoriesForQuestion(questionId: Long)

    @Query("DELETE FROM question_categories WHERE category = :category")
    suspend fun deleteCategory(category: String)

    @Query("DELETE FROM question_categories")
    suspend fun clearAllCategories()

    @Query(
        """
        SELECT DISTINCT qc.category FROM question_categories qc
        INNER JOIN questions q ON qc.questionId = q.id
        INNER JOIN quizzes qz ON q.quizId = qz.id
        INNER JOIN books b ON qz.bookId = b.id
        INNER JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL 
          AND qz.deletedAt IS NULL 
          AND b.deletedAt IS NULL 
          AND w.deletedAt IS NULL
        ORDER BY qc.category
        """
    )
    fun getAllQuestionCategories(): Flow<List<String>>

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
          AND (qz.category = :category OR q.id IN (SELECT questionId FROM question_categories WHERE category = :category))
        ORDER BY q.id
        """
    )
    fun getQuestionsByCategoryFlow(category: String): Flow<List<QuestionEntity>>

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
          AND (qz.category = :category OR q.id IN (SELECT questionId FROM question_categories WHERE category = :category))
        ORDER BY q.id
        """
    )
    suspend fun getQuestionsByCategory(category: String): List<QuestionEntity>


    @Transaction
    suspend fun replaceCategories(questionId: Long, categories: List<String>) {
        deleteCategoriesForQuestion(questionId)
        val cleaned = categories
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .map { QuestionCategoryEntity(questionId = questionId, category = it) }
        if (cleaned.isNotEmpty()) {
            insertCategories(cleaned)
        }
    }
}
