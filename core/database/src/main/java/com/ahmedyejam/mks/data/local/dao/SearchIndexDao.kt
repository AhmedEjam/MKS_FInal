package com.ahmedyejam.mks.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.SkipQueryVerification
import com.ahmedyejam.mks.data.search.GlobalSearchResultRow

@Dao
@SkipQueryVerification
interface SearchIndexDao {
    @Query("DELETE FROM search_index")
    suspend fun clearAll()

    @Query("DELETE FROM search_index WHERE entityType = :entityType AND entityId = :entityId")
    suspend fun deleteByEntity(entityType: String, entityId: String)

    @Query("""
        SELECT 
            entityId AS id,
            entityType AS type,
            title AS title,
            subtitle AS subtitle,
            content AS snippet,
            bookId AS bookId,
            quizId AS quizId,
            NULL AS questionId,
            parentId AS parentId,
            updatedAt AS updatedAt
        FROM search_index
        WHERE search_index MATCH :query AND workspaceId = :workspaceId
        LIMIT :limit
    """)
    suspend fun search(query: String, workspaceId: Long, limit: Int = 120): List<GlobalSearchResultRow>

    @Query("SELECT COUNT(*) FROM search_index")
    suspend fun count(): Int
}
