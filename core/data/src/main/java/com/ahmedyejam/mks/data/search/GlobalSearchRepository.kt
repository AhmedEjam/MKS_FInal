package com.ahmedyejam.mks.data.search

import com.ahmedyejam.mks.data.local.dao.GlobalSearchDao
import com.ahmedyejam.mks.data.local.dao.SearchIndexDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GlobalSearchRepository constructor(
    private val dao: GlobalSearchDao,
    private val searchIndexDao: SearchIndexDao? = null,
    private val searchIndexManager: SearchIndexManager? = null,
) {
    suspend fun search(query: String, workspaceId: Long): List<GlobalSearchResult> =
        withContext(Dispatchers.IO) {
            val cleaned = query.trim()
            if (cleaned.length < 2 || workspaceId <= 0L) return@withContext emptyList()

            // Prefer FTS index if available (much faster than 14-branch LIKE UNION)
            if (searchIndexDao != null && searchIndexManager != null) {
                val ftsQuery = searchIndexManager.prepareQuery(cleaned)
                if (ftsQuery.isNotEmpty()) {
                    try {
                        val results = searchIndexDao.search(ftsQuery, workspaceId)
                        return@withContext results
                            .map { it.toResult() }
                            .sortedWith(compareByDescending<GlobalSearchResult> { it.updatedAt ?: 0L }.thenBy { it.type.name })
                    } catch (e: Exception) {
                        // FTS query syntax error — fall through to LIKE fallback
                    }
                }
            }

            // Fallback: original 14-branch LIKE query (still workspace-scoped)
            dao.search("%$cleaned%", workspaceId)
                .map { it.toResult() }
                .sortedWith(compareByDescending<GlobalSearchResult> { it.updatedAt ?: 0L }.thenBy { it.type.name })
        }
}
