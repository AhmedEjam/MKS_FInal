package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.KnowledgeStudySessionEntity
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
public class KnowledgeStudySessionDao_Impl(
  __db: RoomDatabase,
) : KnowledgeStudySessionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfKnowledgeStudySessionEntity:
      EntityInsertAdapter<KnowledgeStudySessionEntity>

  private val __deleteAdapterOfKnowledgeStudySessionEntity:
      EntityDeleteOrUpdateAdapter<KnowledgeStudySessionEntity>

  private val __updateAdapterOfKnowledgeStudySessionEntity:
      EntityDeleteOrUpdateAdapter<KnowledgeStudySessionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfKnowledgeStudySessionEntity = object :
        EntityInsertAdapter<KnowledgeStudySessionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `knowledge_study_sessions` (`id`,`targetType`,`targetId`,`stateJson`,`isCompleted`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: KnowledgeStudySessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.targetType)
        statement.bindLong(3, entity.targetId)
        statement.bindText(4, entity.stateJson)
        val _tmp: Int = if (entity.isCompleted) 1 else 0
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
    this.__deleteAdapterOfKnowledgeStudySessionEntity = object :
        EntityDeleteOrUpdateAdapter<KnowledgeStudySessionEntity>() {
      protected override fun createQuery(): String =
          "DELETE FROM `knowledge_study_sessions` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: KnowledgeStudySessionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfKnowledgeStudySessionEntity = object :
        EntityDeleteOrUpdateAdapter<KnowledgeStudySessionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `knowledge_study_sessions` SET `id` = ?,`targetType` = ?,`targetId` = ?,`stateJson` = ?,`isCompleted` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: KnowledgeStudySessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.targetType)
        statement.bindLong(3, entity.targetId)
        statement.bindText(4, entity.stateJson)
        val _tmp: Int = if (entity.isCompleted) 1 else 0
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
  }

  public override suspend fun insertSession(session: KnowledgeStudySessionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfKnowledgeStudySessionEntity.insertAndReturnId(_connection,
        session)
    _result
  }

  public override suspend fun hardDeleteSession(session: KnowledgeStudySessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfKnowledgeStudySessionEntity.handle(_connection, session)
  }

  public override suspend fun updateSession(session: KnowledgeStudySessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfKnowledgeStudySessionEntity.handle(_connection, session)
  }

  public override fun getSessionsByTarget(targetType: String, targetId: Long):
      Flow<List<KnowledgeStudySessionEntity>> {
    val _sql: String =
        "SELECT * FROM knowledge_study_sessions WHERE targetType = ? AND targetId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("knowledge_study_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, targetType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, targetId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<KnowledgeStudySessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: KnowledgeStudySessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
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
              KnowledgeStudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSessionsByTargetType(targetType: String):
      Flow<List<KnowledgeStudySessionEntity>> {
    val _sql: String =
        "SELECT * FROM knowledge_study_sessions WHERE targetType = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("knowledge_study_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, targetType)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<KnowledgeStudySessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: KnowledgeStudySessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
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
              KnowledgeStudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSessionsByTargetId(targetId: Long):
      Flow<List<KnowledgeStudySessionEntity>> {
    val _sql: String =
        "SELECT * FROM knowledge_study_sessions WHERE targetId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("knowledge_study_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, targetId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<KnowledgeStudySessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: KnowledgeStudySessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
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
              KnowledgeStudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSessionsByBookIdNow(bookId: Long):
      List<KnowledgeStudySessionEntity> {
    val _sql: String = """
        |
        |        SELECT * FROM knowledge_study_sessions 
        |        WHERE deletedAt IS NULL AND ((targetType = 'FLASHCARD_DECK' AND targetId IN (SELECT id FROM flashcard_decks WHERE bookId = ? AND deletedAt IS NULL))
        |           OR (targetType = 'SLIDESHOW' AND targetId IN (SELECT id FROM slideshow_courses WHERE bookId = ? AND deletedAt IS NULL))
        |           OR (targetType = 'NOTE' AND targetId IN (SELECT id FROM note_blueprints WHERE collectionId IN (SELECT id FROM note_collections WHERE bookId = ? AND deletedAt IS NULL) AND deletedAt IS NULL))
        |           OR (targetType = 'PROMPT' AND targetId IN (SELECT id FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL)))
        |        ORDER BY updatedAt DESC
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, bookId)
        _argIndex = 4
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<KnowledgeStudySessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: KnowledgeStudySessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
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
              KnowledgeStudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getActiveSessionByTarget(targetType: String, targetId: Long):
      KnowledgeStudySessionEntity? {
    val _sql: String =
        "SELECT * FROM knowledge_study_sessions WHERE targetType = ? AND targetId = ? AND isCompleted = 0 AND deletedAt IS NULL ORDER BY updatedAt DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, targetType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, targetId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: KnowledgeStudySessionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
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
              KnowledgeStudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSessionById(id: Long): KnowledgeStudySessionEntity? {
    val _sql: String = "SELECT * FROM knowledge_study_sessions WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: KnowledgeStudySessionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
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
              KnowledgeStudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSessionById(sessionId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE knowledge_study_sessions SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, sessionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreSessionById(sessionId: Long, updatedAt: Long) {
    val _sql: String =
        "UPDATE knowledge_study_sessions SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, sessionId)
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
