package com.ahmedyejam.mks.data.local.entity

data class SearchIndexEntry(
    val rowId: Long = 0,
    val title: String,
    val subtitle: String = "",
    val content: String = "",
    val entityType: String,
    val entityId: String,
    val workspaceId: Long,
    val bookId: Long = 0,
    val quizId: Long = 0,
    val parentId: Long = 0,
    val updatedAt: Long = 0,
)
