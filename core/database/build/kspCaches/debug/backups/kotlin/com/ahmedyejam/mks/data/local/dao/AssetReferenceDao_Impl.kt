package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.AssetReferenceEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AssetReferenceDao_Impl(
  __db: RoomDatabase,
) : AssetReferenceDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAssetReferenceEntity: EntityInsertAdapter<AssetReferenceEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAssetReferenceEntity = object :
        EntityInsertAdapter<AssetReferenceEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `asset_references` (`id`,`path`,`ownerType`,`ownerId`,`createdAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AssetReferenceEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.path)
        statement.bindText(3, entity.ownerType)
        statement.bindLong(4, entity.ownerId)
        statement.bindLong(5, entity.createdAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpDeletedAt)
        }
      }
    }
  }

  public override suspend fun insertReference(reference: AssetReferenceEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfAssetReferenceEntity.insertAndReturnId(_connection,
        reference)
    _result
  }

  public override suspend fun insertReferences(references: List<AssetReferenceEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAssetReferenceEntity.insert(_connection, references)
  }

  public override suspend fun replaceOwnerReferences(
    ownerType: String,
    ownerId: Long,
    paths: List<String?>,
  ): Unit = performInTransactionSuspending(__db) {
    super@AssetReferenceDao_Impl.replaceOwnerReferences(ownerType, ownerId, paths)
  }

  public override suspend fun countReferencesForPath(path: String): Int {
    val _sql: String = "SELECT COUNT(*) FROM asset_references WHERE path = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, path)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getReferencesForOwner(ownerType: String, ownerId: Long):
      List<AssetReferenceEntity> {
    val _sql: String =
        "SELECT * FROM asset_references WHERE ownerType = ? AND ownerId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, ownerId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AssetReferenceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AssetReferenceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AssetReferenceEntity(_tmpId,_tmpPath,_tmpOwnerType,_tmpOwnerId,_tmpCreatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllReferences(): List<AssetReferenceEntity> {
    val _sql: String = "SELECT * FROM asset_references WHERE deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AssetReferenceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AssetReferenceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AssetReferenceEntity(_tmpId,_tmpPath,_tmpOwnerType,_tmpOwnerId,_tmpCreatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllReferencesIncludingDeleted(): List<AssetReferenceEntity> {
    val _sql: String = "SELECT * FROM asset_references ORDER BY createdAt DESC, id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AssetReferenceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AssetReferenceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AssetReferenceEntity(_tmpId,_tmpPath,_tmpOwnerType,_tmpOwnerId,_tmpCreatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteReference(
    ownerType: String,
    ownerId: Long,
    path: String,
    deletedAt: Long,
  ) {
    val _sql: String =
        "UPDATE asset_references SET deletedAt = ? WHERE ownerType = ? AND ownerId = ? AND path = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 3
        _stmt.bindLong(_argIndex, ownerId)
        _argIndex = 4
        _stmt.bindText(_argIndex, path)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteReferencesForOwner(
    ownerType: String,
    ownerId: Long,
    deletedAt: Long,
  ) {
    val _sql: String =
        "UPDATE asset_references SET deletedAt = ? WHERE ownerType = ? AND ownerId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 3
        _stmt.bindLong(_argIndex, ownerId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllReferences(deletedAt: Long) {
    val _sql: String = "UPDATE asset_references SET deletedAt = ? WHERE deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
