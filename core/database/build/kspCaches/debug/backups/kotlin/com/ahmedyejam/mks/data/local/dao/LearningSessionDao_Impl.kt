package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.LearningSessionEntity
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
public class LearningSessionDao_Impl(
  __db: RoomDatabase,
) : LearningSessionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLearningSessionEntity: EntityInsertAdapter<LearningSessionEntity>

  private val __deleteAdapterOfLearningSessionEntity:
      EntityDeleteOrUpdateAdapter<LearningSessionEntity>

  private val __updateAdapterOfLearningSessionEntity:
      EntityDeleteOrUpdateAdapter<LearningSessionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfLearningSessionEntity = object :
        EntityInsertAdapter<LearningSessionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `learning_sessions` (`id`,`deckId`,`label`,`stateJson`,`isCompleted`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LearningSessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.deckId)
        val _tmpLabel: String? = entity.label
        if (_tmpLabel == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpLabel)
        }
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
    this.__deleteAdapterOfLearningSessionEntity = object :
        EntityDeleteOrUpdateAdapter<LearningSessionEntity>() {
      protected override fun createQuery(): String =
          "DELETE FROM `learning_sessions` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: LearningSessionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfLearningSessionEntity = object :
        EntityDeleteOrUpdateAdapter<LearningSessionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `learning_sessions` SET `id` = ?,`deckId` = ?,`label` = ?,`stateJson` = ?,`isCompleted` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: LearningSessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.deckId)
        val _tmpLabel: String? = entity.label
        if (_tmpLabel == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpLabel)
        }
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

  public override suspend fun insertSession(session: LearningSessionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfLearningSessionEntity.insertAndReturnId(_connection,
        session)
    _result
  }

  public override suspend fun hardDeleteSession(session: LearningSessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfLearningSessionEntity.handle(_connection, session)
  }

  public override suspend fun updateSession(session: LearningSessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfLearningSessionEntity.handle(_connection, session)
  }

  public override fun getSessionsByDeckId(deckId: Long): Flow<List<LearningSessionEntity>> {
    val _sql: String =
        "SELECT * FROM learning_sessions WHERE deckId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("learning_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<LearningSessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: LearningSessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpLabel: String?
          if (_stmt.isNull(_columnIndexOfLabel)) {
            _tmpLabel = null
          } else {
            _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          }
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
              LearningSessionEntity(_tmpId,_tmpDeckId,_tmpLabel,_tmpStateJson,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSessionById(sessionId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE learning_sessions SET deletedAt = ?, updatedAt = ? WHERE id = ?"
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
    val _sql: String = "UPDATE learning_sessions SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
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
