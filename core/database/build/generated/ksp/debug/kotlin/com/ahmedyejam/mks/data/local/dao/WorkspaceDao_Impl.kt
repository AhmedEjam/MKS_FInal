package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.WorkspaceEntity
import com.ahmedyejam.mks.`data`.local.entity.WorkspaceSettingsEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class WorkspaceDao_Impl(
  __db: RoomDatabase,
) : WorkspaceDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfWorkspaceEntity: EntityInsertAdapter<WorkspaceEntity>

  private val __insertAdapterOfWorkspaceSettingsEntity: EntityInsertAdapter<WorkspaceSettingsEntity>

  private val __deleteAdapterOfWorkspaceEntity: EntityDeleteOrUpdateAdapter<WorkspaceEntity>

  private val __updateAdapterOfWorkspaceEntity: EntityDeleteOrUpdateAdapter<WorkspaceEntity>

  private val __updateAdapterOfWorkspaceSettingsEntity:
      EntityDeleteOrUpdateAdapter<WorkspaceSettingsEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfWorkspaceEntity = object : EntityInsertAdapter<WorkspaceEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `workspaces` (`id`,`externalId`,`name`,`description`,`isDefault`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WorkspaceEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindText(3, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDescription)
        }
        val _tmp: Int = if (entity.isDefault) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindLong(6, entity.createdAt)
        statement.bindLong(7, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpDeletedAt)
        }
      }
    }
    this.__insertAdapterOfWorkspaceSettingsEntity = object :
        EntityInsertAdapter<WorkspaceSettingsEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `workspace_settings` (`id`,`workspaceId`,`language`,`theme`,`defaultSort`,`quizDefaultsJson`,`importDefaultsJson`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: WorkspaceSettingsEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.workspaceId)
        val _tmpLanguage: String? = entity.language
        if (_tmpLanguage == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpLanguage)
        }
        val _tmpTheme: String? = entity.theme
        if (_tmpTheme == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpTheme)
        }
        val _tmpDefaultSort: String? = entity.defaultSort
        if (_tmpDefaultSort == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDefaultSort)
        }
        val _tmpQuizDefaultsJson: String? = entity.quizDefaultsJson
        if (_tmpQuizDefaultsJson == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpQuizDefaultsJson)
        }
        val _tmpImportDefaultsJson: String? = entity.importDefaultsJson
        if (_tmpImportDefaultsJson == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpImportDefaultsJson)
        }
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfWorkspaceEntity = object : EntityDeleteOrUpdateAdapter<WorkspaceEntity>()
        {
      protected override fun createQuery(): String = "DELETE FROM `workspaces` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: WorkspaceEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfWorkspaceEntity = object : EntityDeleteOrUpdateAdapter<WorkspaceEntity>()
        {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `workspaces` SET `id` = ?,`externalId` = ?,`name` = ?,`description` = ?,`isDefault` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: WorkspaceEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindText(3, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDescription)
        }
        val _tmp: Int = if (entity.isDefault) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindLong(6, entity.createdAt)
        statement.bindLong(7, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpDeletedAt)
        }
        statement.bindLong(9, entity.id)
      }
    }
    this.__updateAdapterOfWorkspaceSettingsEntity = object :
        EntityDeleteOrUpdateAdapter<WorkspaceSettingsEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `workspace_settings` SET `id` = ?,`workspaceId` = ?,`language` = ?,`theme` = ?,`defaultSort` = ?,`quizDefaultsJson` = ?,`importDefaultsJson` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: WorkspaceSettingsEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.workspaceId)
        val _tmpLanguage: String? = entity.language
        if (_tmpLanguage == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpLanguage)
        }
        val _tmpTheme: String? = entity.theme
        if (_tmpTheme == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpTheme)
        }
        val _tmpDefaultSort: String? = entity.defaultSort
        if (_tmpDefaultSort == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDefaultSort)
        }
        val _tmpQuizDefaultsJson: String? = entity.quizDefaultsJson
        if (_tmpQuizDefaultsJson == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpQuizDefaultsJson)
        }
        val _tmpImportDefaultsJson: String? = entity.importDefaultsJson
        if (_tmpImportDefaultsJson == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpImportDefaultsJson)
        }
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpDeletedAt)
        }
        statement.bindLong(11, entity.id)
      }
    }
  }

  public override suspend fun insertWorkspace(workspace: WorkspaceEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfWorkspaceEntity.insertAndReturnId(_connection, workspace)
    _result
  }

  public override suspend fun insertSettings(settings: WorkspaceSettingsEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfWorkspaceSettingsEntity.insertAndReturnId(_connection,
        settings)
    _result
  }

  public override suspend fun hardDeleteWorkspace(workspace: WorkspaceEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfWorkspaceEntity.handle(_connection, workspace)
  }

  public override suspend fun updateWorkspace(workspace: WorkspaceEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfWorkspaceEntity.handle(_connection, workspace)
  }

  public override suspend fun updateSettings(settings: WorkspaceSettingsEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfWorkspaceSettingsEntity.handle(_connection, settings)
  }

  public override fun getAllWorkspacesFlow(): Flow<List<WorkspaceEntity>> {
    val _sql: String = "SELECT * FROM workspaces WHERE deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("workspaces")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<WorkspaceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WorkspaceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
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
              WorkspaceEntity(_tmpId,_tmpExternalId,_tmpName,_tmpDescription,_tmpIsDefault,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDeletedWorkspacesFlow(): Flow<List<WorkspaceEntity>> {
    val _sql: String = "SELECT * FROM workspaces WHERE deletedAt IS NOT NULL"
    return createFlow(__db, false, arrayOf("workspaces")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<WorkspaceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: WorkspaceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
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
              WorkspaceEntity(_tmpId,_tmpExternalId,_tmpName,_tmpDescription,_tmpIsDefault,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDefaultWorkspace(): WorkspaceEntity? {
    val _sql: String = "SELECT * FROM workspaces WHERE isDefault = 1 AND deletedAt IS NULL LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: WorkspaceEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
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
              WorkspaceEntity(_tmpId,_tmpExternalId,_tmpName,_tmpDescription,_tmpIsDefault,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getWorkspaceById(id: Long): WorkspaceEntity? {
    val _sql: String = "SELECT * FROM workspaces WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: WorkspaceEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
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
              WorkspaceEntity(_tmpId,_tmpExternalId,_tmpName,_tmpDescription,_tmpIsDefault,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getWorkspaceByIdIncludingDeleted(id: Long): WorkspaceEntity? {
    val _sql: String = "SELECT * FROM workspaces WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: WorkspaceEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
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
              WorkspaceEntity(_tmpId,_tmpExternalId,_tmpName,_tmpDescription,_tmpIsDefault,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getWorkspaceByExternalId(externalId: String): WorkspaceEntity? {
    val _sql: String = "SELECT * FROM workspaces WHERE externalId = ? AND deletedAt IS NULL LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, externalId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: WorkspaceEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpIsDefault: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp != 0
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
              WorkspaceEntity(_tmpId,_tmpExternalId,_tmpName,_tmpDescription,_tmpIsDefault,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSettingsByWorkspaceId(workspaceId: Long):
      WorkspaceSettingsEntity? {
    val _sql: String =
        "SELECT * FROM workspace_settings WHERE workspaceId = ? AND deletedAt IS NULL LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _columnIndexOfTheme: Int = getColumnIndexOrThrow(_stmt, "theme")
        val _columnIndexOfDefaultSort: Int = getColumnIndexOrThrow(_stmt, "defaultSort")
        val _columnIndexOfQuizDefaultsJson: Int = getColumnIndexOrThrow(_stmt, "quizDefaultsJson")
        val _columnIndexOfImportDefaultsJson: Int = getColumnIndexOrThrow(_stmt,
            "importDefaultsJson")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: WorkspaceSettingsEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpLanguage: String?
          if (_stmt.isNull(_columnIndexOfLanguage)) {
            _tmpLanguage = null
          } else {
            _tmpLanguage = _stmt.getText(_columnIndexOfLanguage)
          }
          val _tmpTheme: String?
          if (_stmt.isNull(_columnIndexOfTheme)) {
            _tmpTheme = null
          } else {
            _tmpTheme = _stmt.getText(_columnIndexOfTheme)
          }
          val _tmpDefaultSort: String?
          if (_stmt.isNull(_columnIndexOfDefaultSort)) {
            _tmpDefaultSort = null
          } else {
            _tmpDefaultSort = _stmt.getText(_columnIndexOfDefaultSort)
          }
          val _tmpQuizDefaultsJson: String?
          if (_stmt.isNull(_columnIndexOfQuizDefaultsJson)) {
            _tmpQuizDefaultsJson = null
          } else {
            _tmpQuizDefaultsJson = _stmt.getText(_columnIndexOfQuizDefaultsJson)
          }
          val _tmpImportDefaultsJson: String?
          if (_stmt.isNull(_columnIndexOfImportDefaultsJson)) {
            _tmpImportDefaultsJson = null
          } else {
            _tmpImportDefaultsJson = _stmt.getText(_columnIndexOfImportDefaultsJson)
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
              WorkspaceSettingsEntity(_tmpId,_tmpWorkspaceId,_tmpLanguage,_tmpTheme,_tmpDefaultSort,_tmpQuizDefaultsJson,_tmpImportDefaultsJson,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteWorkspaceById(workspaceId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE workspaces SET deletedAt = ?, updatedAt = ?, isDefault = 0 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, workspaceId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreWorkspaceById(workspaceId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE workspaces SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, workspaceId)
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
