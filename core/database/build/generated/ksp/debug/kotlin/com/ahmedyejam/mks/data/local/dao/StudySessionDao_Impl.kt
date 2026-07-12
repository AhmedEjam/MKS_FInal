package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.StudySessionEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Float
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
public class StudySessionDao_Impl(
  __db: RoomDatabase,
) : StudySessionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfStudySessionEntity: EntityInsertAdapter<StudySessionEntity>

  private val __updateAdapterOfStudySessionEntity: EntityDeleteOrUpdateAdapter<StudySessionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfStudySessionEntity = object : EntityInsertAdapter<StudySessionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `study_sessions` (`id`,`targetType`,`targetId`,`label`,`stateJson`,`timeSpentMs`,`completionPercentage`,`isCompleted`,`correctCount`,`incorrectCount`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StudySessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.targetType)
        statement.bindLong(3, entity.targetId)
        val _tmpLabel: String? = entity.label
        if (_tmpLabel == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpLabel)
        }
        statement.bindText(5, entity.stateJson)
        statement.bindLong(6, entity.timeSpentMs)
        statement.bindDouble(7, entity.completionPercentage.toDouble())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindLong(9, entity.correctCount.toLong())
        statement.bindLong(10, entity.incorrectCount.toLong())
        statement.bindLong(11, entity.createdAt)
        statement.bindLong(12, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpDeletedAt)
        }
      }
    }
    this.__updateAdapterOfStudySessionEntity = object :
        EntityDeleteOrUpdateAdapter<StudySessionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `study_sessions` SET `id` = ?,`targetType` = ?,`targetId` = ?,`label` = ?,`stateJson` = ?,`timeSpentMs` = ?,`completionPercentage` = ?,`isCompleted` = ?,`correctCount` = ?,`incorrectCount` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: StudySessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.targetType)
        statement.bindLong(3, entity.targetId)
        val _tmpLabel: String? = entity.label
        if (_tmpLabel == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpLabel)
        }
        statement.bindText(5, entity.stateJson)
        statement.bindLong(6, entity.timeSpentMs)
        statement.bindDouble(7, entity.completionPercentage.toDouble())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindLong(9, entity.correctCount.toLong())
        statement.bindLong(10, entity.incorrectCount.toLong())
        statement.bindLong(11, entity.createdAt)
        statement.bindLong(12, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpDeletedAt)
        }
        statement.bindLong(14, entity.id)
      }
    }
  }

  public override suspend fun insertSession(session: StudySessionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfStudySessionEntity.insertAndReturnId(_connection, session)
    _result
  }

  public override suspend fun updateSession(session: StudySessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfStudySessionEntity.handle(_connection, session)
  }

  public override fun getSessionsForTarget(targetId: Long, targetType: String):
      Flow<List<StudySessionEntity>> {
    val _sql: String =
        "SELECT * FROM study_sessions WHERE targetId = ? AND targetType = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("study_sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, targetId)
        _argIndex = 2
        _stmt.bindText(_argIndex, targetType)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<StudySessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StudySessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpLabel: String?
          if (_stmt.isNull(_columnIndexOfLabel)) {
            _tmpLabel = null
          } else {
            _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          }
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
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
              StudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpLabel,_tmpStateJson,_tmpTimeSpentMs,_tmpCompletionPercentage,_tmpIsCompleted,_tmpCorrectCount,_tmpIncorrectCount,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSessionById(sessionId: Long): StudySessionEntity? {
    val _sql: String = "SELECT * FROM study_sessions WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, sessionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: StudySessionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpLabel: String?
          if (_stmt.isNull(_columnIndexOfLabel)) {
            _tmpLabel = null
          } else {
            _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          }
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
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
              StudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpLabel,_tmpStateJson,_tmpTimeSpentMs,_tmpCompletionPercentage,_tmpIsCompleted,_tmpCorrectCount,_tmpIncorrectCount,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getActiveSessionForTarget(targetId: Long, targetType: String):
      StudySessionEntity? {
    val _sql: String =
        "SELECT * FROM study_sessions WHERE targetId = ? AND targetType = ? AND isCompleted = 0 AND deletedAt IS NULL ORDER BY updatedAt DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, targetId)
        _argIndex = 2
        _stmt.bindText(_argIndex, targetType)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfStateJson: Int = getColumnIndexOrThrow(_stmt, "stateJson")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: StudySessionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetId: Long
          _tmpTargetId = _stmt.getLong(_columnIndexOfTargetId)
          val _tmpLabel: String?
          if (_stmt.isNull(_columnIndexOfLabel)) {
            _tmpLabel = null
          } else {
            _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          }
          val _tmpStateJson: String
          _tmpStateJson = _stmt.getText(_columnIndexOfStateJson)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
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
              StudySessionEntity(_tmpId,_tmpTargetType,_tmpTargetId,_tmpLabel,_tmpStateJson,_tmpTimeSpentMs,_tmpCompletionPercentage,_tmpIsCompleted,_tmpCorrectCount,_tmpIncorrectCount,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSession(sessionId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE study_sessions SET deletedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
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
