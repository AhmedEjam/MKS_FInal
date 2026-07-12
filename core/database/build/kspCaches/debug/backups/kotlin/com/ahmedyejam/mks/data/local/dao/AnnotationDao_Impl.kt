package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.getTotalChangedRows
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.AnnotationEntity
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AnnotationDao_Impl(
  __db: RoomDatabase,
) : AnnotationDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAnnotationEntity: EntityInsertAdapter<AnnotationEntity>

  private val __deleteAdapterOfAnnotationEntity: EntityDeleteOrUpdateAdapter<AnnotationEntity>

  private val __updateAdapterOfAnnotationEntity: EntityDeleteOrUpdateAdapter<AnnotationEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAnnotationEntity = object : EntityInsertAdapter<AnnotationEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `annotations` (`id`,`workspaceId`,`bookId`,`ownerType`,`ownerId`,`selectedText`,`noteBody`,`colorLabel`,`positionDataJson`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AnnotationEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.workspaceId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.ownerType)
        statement.bindLong(5, entity.ownerId)
        val _tmpSelectedText: String? = entity.selectedText
        if (_tmpSelectedText == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSelectedText)
        }
        val _tmpNoteBody: String? = entity.noteBody
        if (_tmpNoteBody == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpNoteBody)
        }
        statement.bindText(8, entity.colorLabel)
        val _tmpPositionDataJson: String? = entity.positionDataJson
        if (_tmpPositionDataJson == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpPositionDataJson)
        }
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfAnnotationEntity = object :
        EntityDeleteOrUpdateAdapter<AnnotationEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `annotations` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AnnotationEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfAnnotationEntity = object :
        EntityDeleteOrUpdateAdapter<AnnotationEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `annotations` SET `id` = ?,`workspaceId` = ?,`bookId` = ?,`ownerType` = ?,`ownerId` = ?,`selectedText` = ?,`noteBody` = ?,`colorLabel` = ?,`positionDataJson` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AnnotationEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.workspaceId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.ownerType)
        statement.bindLong(5, entity.ownerId)
        val _tmpSelectedText: String? = entity.selectedText
        if (_tmpSelectedText == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSelectedText)
        }
        val _tmpNoteBody: String? = entity.noteBody
        if (_tmpNoteBody == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpNoteBody)
        }
        statement.bindText(8, entity.colorLabel)
        val _tmpPositionDataJson: String? = entity.positionDataJson
        if (_tmpPositionDataJson == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpPositionDataJson)
        }
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpDeletedAt)
        }
        statement.bindLong(13, entity.id)
      }
    }
  }

  public override suspend fun insertAnnotation(`annotation`: AnnotationEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfAnnotationEntity.insertAndReturnId(_connection, annotation)
    _result
  }

  public override suspend fun insertAnnotations(annotations: List<AnnotationEntity>): List<Long> =
      performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfAnnotationEntity.insertAndReturnIdsList(_connection,
        annotations)
    _result
  }

  public override suspend fun hardDeleteAnnotation(`annotation`: AnnotationEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfAnnotationEntity.handle(_connection, annotation)
  }

  public override suspend fun updateAnnotation(`annotation`: AnnotationEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfAnnotationEntity.handle(_connection, annotation)
  }

  public override fun getAllAnnotations(): Flow<List<AnnotationEntity>> {
    val _sql: String = "SELECT * FROM annotations WHERE deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("annotations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAnnotationsByWorkspaceId(workspaceId: Long): Flow<List<AnnotationEntity>> {
    val _sql: String =
        "SELECT * FROM annotations WHERE workspaceId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("annotations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAnnotationsByBookId(bookId: Long): Flow<List<AnnotationEntity>> {
    val _sql: String =
        "SELECT * FROM annotations WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("annotations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAnnotationsByOwner(ownerType: String, ownerId: Long):
      Flow<List<AnnotationEntity>> {
    val _sql: String =
        "SELECT * FROM annotations WHERE ownerType = ? AND ownerId = ? AND deletedAt IS NULL ORDER BY createdAt ASC"
    return createFlow(__db, false, arrayOf("annotations")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, ownerId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAnnotationsByOwnerNow(ownerType: String, ownerId: Long):
      List<AnnotationEntity> {
    val _sql: String =
        "SELECT * FROM annotations WHERE ownerType = ? AND ownerId = ? AND deletedAt IS NULL ORDER BY createdAt ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, ownerId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAnnotationById(id: Long): AnnotationEntity? {
    val _sql: String = "SELECT * FROM annotations WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: AnnotationEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAnnotationByIdIncludingDeleted(id: Long): AnnotationEntity? {
    val _sql: String = "SELECT * FROM annotations WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: AnnotationEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllAnnotationsIncludingDeleted(): List<AnnotationEntity> {
    val _sql: String = "SELECT * FROM annotations ORDER BY updatedAt DESC, id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAnnotationsByBookIdIncludingDeleted(bookId: Long):
      List<AnnotationEntity> {
    val _sql: String = "SELECT * FROM annotations WHERE bookId = ? ORDER BY updatedAt DESC, id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun searchAnnotations(likeQuery: String, limit: Int):
      List<AnnotationEntity> {
    val _sql: String =
        "SELECT * FROM annotations WHERE deletedAt IS NULL AND (selectedText LIKE ? OR noteBody LIKE ? OR colorLabel LIKE ?) ORDER BY updatedAt DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 2
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 3
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 4
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfOwnerType: Int = getColumnIndexOrThrow(_stmt, "ownerType")
        val _columnIndexOfOwnerId: Int = getColumnIndexOrThrow(_stmt, "ownerId")
        val _columnIndexOfSelectedText: Int = getColumnIndexOrThrow(_stmt, "selectedText")
        val _columnIndexOfNoteBody: Int = getColumnIndexOrThrow(_stmt, "noteBody")
        val _columnIndexOfColorLabel: Int = getColumnIndexOrThrow(_stmt, "colorLabel")
        val _columnIndexOfPositionDataJson: Int = getColumnIndexOrThrow(_stmt, "positionDataJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<AnnotationEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AnnotationEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpOwnerType: String
          _tmpOwnerType = _stmt.getText(_columnIndexOfOwnerType)
          val _tmpOwnerId: Long
          _tmpOwnerId = _stmt.getLong(_columnIndexOfOwnerId)
          val _tmpSelectedText: String?
          if (_stmt.isNull(_columnIndexOfSelectedText)) {
            _tmpSelectedText = null
          } else {
            _tmpSelectedText = _stmt.getText(_columnIndexOfSelectedText)
          }
          val _tmpNoteBody: String?
          if (_stmt.isNull(_columnIndexOfNoteBody)) {
            _tmpNoteBody = null
          } else {
            _tmpNoteBody = _stmt.getText(_columnIndexOfNoteBody)
          }
          val _tmpColorLabel: String
          _tmpColorLabel = _stmt.getText(_columnIndexOfColorLabel)
          val _tmpPositionDataJson: String?
          if (_stmt.isNull(_columnIndexOfPositionDataJson)) {
            _tmpPositionDataJson = null
          } else {
            _tmpPositionDataJson = _stmt.getText(_columnIndexOfPositionDataJson)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              AnnotationEntity(_tmpId,_tmpWorkspaceId,_tmpBookId,_tmpOwnerType,_tmpOwnerId,_tmpSelectedText,_tmpNoteBody,_tmpColorLabel,_tmpPositionDataJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAnnotationsByBookId(bookId: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM annotations WHERE bookId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
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

  public override suspend fun softDeleteAnnotationById(annotationId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE annotations SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, annotationId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteAnnotationsByOwner(
    ownerType: String,
    ownerId: Long,
    deletedAt: Long,
  ) {
    val _sql: String =
        "UPDATE annotations SET deletedAt = ?, updatedAt = ? WHERE ownerType = ? AND ownerId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 4
        _stmt.bindLong(_argIndex, ownerId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteAnnotationsByBookId(bookId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE annotations SET deletedAt = ?, updatedAt = ? WHERE bookId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreAnnotationById(annotationId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE annotations SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, annotationId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreAnnotationsByOwner(
    ownerType: String,
    ownerId: Long,
    updatedAt: Long,
  ) {
    val _sql: String =
        "UPDATE annotations SET deletedAt = NULL, updatedAt = ? WHERE ownerType = ? AND ownerId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
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

  public override suspend fun restoreAnnotationsByBookId(bookId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE annotations SET deletedAt = NULL, updatedAt = ? WHERE bookId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun permanentlyDeleteAnnotationById(annotationId: Long) {
    val _sql: String = "DELETE FROM annotations WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, annotationId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun permanentlyDeleteAnnotationsByOwner(ownerType: String,
      ownerId: Long) {
    val _sql: String = "DELETE FROM annotations WHERE ownerType = ? AND ownerId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ownerType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, ownerId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun permanentlyDeleteAnnotationsByBookId(bookId: Long) {
    val _sql: String = "DELETE FROM annotations WHERE bookId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun cleanupDeletedAnnotationsOlderThan(olderThan: Long): Int {
    val _sql: String = "DELETE FROM annotations WHERE deletedAt IS NOT NULL AND deletedAt < ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, olderThan)
        _stmt.step()
        getTotalChangedRows(_connection)
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
