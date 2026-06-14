package com.ahmedyejam.mks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ahmedyejam.mks.data.local.entity.SourceDocumentAssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDocumentAssetDao {
    @Query("SELECT * FROM source_document_assets WHERE sourceDocumentId = :sourceDocumentId AND deletedAt IS NULL ORDER BY isPinned DESC, sortOrder ASC, createdAt ASC")
    fun getAssetsBySourceId(sourceDocumentId: Long): Flow<List<SourceDocumentAssetEntity>>

    @Query("SELECT * FROM source_document_assets WHERE sourceDocumentId = :sourceDocumentId AND deletedAt IS NULL ORDER BY isPinned DESC, sortOrder ASC, createdAt ASC")
    suspend fun getAssetsBySourceIdNow(sourceDocumentId: Long): List<SourceDocumentAssetEntity>

    @Query("SELECT * FROM source_document_assets WHERE sourceDocumentId = :sourceDocumentId ORDER BY isPinned DESC, sortOrder ASC, createdAt ASC")
    suspend fun getAssetsBySourceIdIncludingDeleted(sourceDocumentId: Long): List<SourceDocumentAssetEntity>

    @Query("SELECT * FROM source_document_assets WHERE id = :id AND deletedAt IS NULL")
    suspend fun getAssetById(id: Long): SourceDocumentAssetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: SourceDocumentAssetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<SourceDocumentAssetEntity>): List<Long>

    @Update
    suspend fun updateAsset(asset: SourceDocumentAssetEntity)

    @Query("UPDATE source_document_assets SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :assetId")
    suspend fun softDeleteAssetById(assetId: Long, deletedAt: Long)

    @Query("UPDATE source_document_assets SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE sourceDocumentId = :sourceDocumentId AND deletedAt IS NULL")
    suspend fun softDeleteAssetsForSource(sourceDocumentId: Long, deletedAt: Long)

    @Query("UPDATE source_document_assets SET deletedAt = NULL, updatedAt = :updatedAt WHERE id = :assetId")
    suspend fun restoreAssetById(assetId: Long, updatedAt: Long)

    @Delete
    suspend fun hardDeleteAsset(asset: SourceDocumentAssetEntity)

    @Query("DELETE FROM source_document_assets WHERE sourceDocumentId = :sourceDocumentId")
    suspend fun hardDeleteAssetsForSource(sourceDocumentId: Long)

    @Query("UPDATE source_document_assets SET sortOrder = :sortOrder, updatedAt = :updatedAt WHERE id = :assetId")
    suspend fun updateAssetOrder(assetId: Long, sortOrder: Int, updatedAt: Long)
}
