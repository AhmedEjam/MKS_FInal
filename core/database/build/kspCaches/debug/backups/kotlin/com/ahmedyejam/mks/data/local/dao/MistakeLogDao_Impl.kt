package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.MistakeLogEntryEntity
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
public class MistakeLogDao_Impl(
  __db: RoomDatabase,
) : MistakeLogDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMistakeLogEntryEntity: EntityInsertAdapter<MistakeLogEntryEntity>

  private val __deleteAdapterOfMistakeLogEntryEntity:
      EntityDeleteOrUpdateAdapter<MistakeLogEntryEntity>

  private val __updateAdapterOfMistakeLogEntryEntity:
      EntityDeleteOrUpdateAdapter<MistakeLogEntryEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfMistakeLogEntryEntity = object :
        EntityInsertAdapter<MistakeLogEntryEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `mistake_log_entries` (`id`,`bookId`,`quizId`,`questionId`,`sessionId`,`selectedAnswer`,`correctAnswer`,`userReason`,`correctConcept`,`preventionNote`,`linkedFlashcardId`,`linkedBlueprintId`,`linkedAssetId`,`isFixed`,`reviewAt`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MistakeLogEntryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.bookId)
        statement.bindLong(3, entity.quizId)
        statement.bindLong(4, entity.questionId)
        val _tmpSessionId: Long? = entity.sessionId
        if (_tmpSessionId == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpSessionId)
        }
        val _tmpSelectedAnswer: String? = entity.selectedAnswer
        if (_tmpSelectedAnswer == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSelectedAnswer)
        }
        val _tmpCorrectAnswer: String? = entity.correctAnswer
        if (_tmpCorrectAnswer == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpCorrectAnswer)
        }
        val _tmpUserReason: String? = entity.userReason
        if (_tmpUserReason == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpUserReason)
        }
        val _tmpCorrectConcept: String? = entity.correctConcept
        if (_tmpCorrectConcept == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpCorrectConcept)
        }
        val _tmpPreventionNote: String? = entity.preventionNote
        if (_tmpPreventionNote == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpPreventionNote)
        }
        val _tmpLinkedFlashcardId: Long? = entity.linkedFlashcardId
        if (_tmpLinkedFlashcardId == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmpLinkedFlashcardId)
        }
        val _tmpLinkedBlueprintId: Long? = entity.linkedBlueprintId
        if (_tmpLinkedBlueprintId == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpLinkedBlueprintId)
        }
        val _tmpLinkedAssetId: Long? = entity.linkedAssetId
        if (_tmpLinkedAssetId == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpLinkedAssetId)
        }
        val _tmp: Int = if (entity.isFixed) 1 else 0
        statement.bindLong(14, _tmp.toLong())
        val _tmpReviewAt: Long? = entity.reviewAt
        if (_tmpReviewAt == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmpReviewAt)
        }
        statement.bindLong(16, entity.createdAt)
        statement.bindLong(17, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfMistakeLogEntryEntity = object :
        EntityDeleteOrUpdateAdapter<MistakeLogEntryEntity>() {
      protected override fun createQuery(): String =
          "DELETE FROM `mistake_log_entries` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MistakeLogEntryEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfMistakeLogEntryEntity = object :
        EntityDeleteOrUpdateAdapter<MistakeLogEntryEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `mistake_log_entries` SET `id` = ?,`bookId` = ?,`quizId` = ?,`questionId` = ?,`sessionId` = ?,`selectedAnswer` = ?,`correctAnswer` = ?,`userReason` = ?,`correctConcept` = ?,`preventionNote` = ?,`linkedFlashcardId` = ?,`linkedBlueprintId` = ?,`linkedAssetId` = ?,`isFixed` = ?,`reviewAt` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: MistakeLogEntryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.bookId)
        statement.bindLong(3, entity.quizId)
        statement.bindLong(4, entity.questionId)
        val _tmpSessionId: Long? = entity.sessionId
        if (_tmpSessionId == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpSessionId)
        }
        val _tmpSelectedAnswer: String? = entity.selectedAnswer
        if (_tmpSelectedAnswer == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSelectedAnswer)
        }
        val _tmpCorrectAnswer: String? = entity.correctAnswer
        if (_tmpCorrectAnswer == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpCorrectAnswer)
        }
        val _tmpUserReason: String? = entity.userReason
        if (_tmpUserReason == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpUserReason)
        }
        val _tmpCorrectConcept: String? = entity.correctConcept
        if (_tmpCorrectConcept == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpCorrectConcept)
        }
        val _tmpPreventionNote: String? = entity.preventionNote
        if (_tmpPreventionNote == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpPreventionNote)
        }
        val _tmpLinkedFlashcardId: Long? = entity.linkedFlashcardId
        if (_tmpLinkedFlashcardId == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmpLinkedFlashcardId)
        }
        val _tmpLinkedBlueprintId: Long? = entity.linkedBlueprintId
        if (_tmpLinkedBlueprintId == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpLinkedBlueprintId)
        }
        val _tmpLinkedAssetId: Long? = entity.linkedAssetId
        if (_tmpLinkedAssetId == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpLinkedAssetId)
        }
        val _tmp: Int = if (entity.isFixed) 1 else 0
        statement.bindLong(14, _tmp.toLong())
        val _tmpReviewAt: Long? = entity.reviewAt
        if (_tmpReviewAt == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmpReviewAt)
        }
        statement.bindLong(16, entity.createdAt)
        statement.bindLong(17, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpDeletedAt)
        }
        statement.bindLong(19, entity.id)
      }
    }
  }

  public override suspend fun insertMistake(entry: MistakeLogEntryEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfMistakeLogEntryEntity.insertAndReturnId(_connection, entry)
    _result
  }

  public override suspend fun hardDeleteMistake(entry: MistakeLogEntryEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfMistakeLogEntryEntity.handle(_connection, entry)
  }

  public override suspend fun updateMistake(entry: MistakeLogEntryEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfMistakeLogEntryEntity.handle(_connection, entry)
  }

  public override fun getAllMistakes(): Flow<List<MistakeLogEntryEntity>> {
    val _sql: String =
        "SELECT * FROM mistake_log_entries WHERE deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("mistake_log_entries")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<MistakeLogEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MistakeLogEntryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMistakesByBookId(bookId: Long): Flow<List<MistakeLogEntryEntity>> {
    val _sql: String =
        "SELECT * FROM mistake_log_entries WHERE bookId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("mistake_log_entries")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<MistakeLogEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MistakeLogEntryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMistakesByQuizId(quizId: Long): Flow<List<MistakeLogEntryEntity>> {
    val _sql: String =
        "SELECT * FROM mistake_log_entries WHERE quizId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("mistake_log_entries")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<MistakeLogEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MistakeLogEntryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMistakesByQuestionId(questionId: Long):
      List<MistakeLogEntryEntity> {
    val _sql: String =
        "SELECT * FROM mistake_log_entries WHERE questionId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<MistakeLogEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MistakeLogEntryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMistakeById(id: Long): MistakeLogEntryEntity? {
    val _sql: String = "SELECT * FROM mistake_log_entries WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MistakeLogEntryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findByQuestionAndSession(questionId: Long, sessionId: Long?):
      MistakeLogEntryEntity? {
    val _sql: String =
        "SELECT * FROM mistake_log_entries WHERE questionId = ? AND deletedAt IS NULL AND ((? IS NULL AND sessionId IS NULL) OR sessionId = ?) LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        _argIndex = 2
        if (sessionId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, sessionId)
        }
        _argIndex = 3
        if (sessionId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, sessionId)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MistakeLogEntryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDueMistakes(now: Long, limit: Int): List<MistakeLogEntryEntity> {
    val _sql: String =
        "SELECT * FROM mistake_log_entries WHERE deletedAt IS NULL AND isFixed = 0 AND reviewAt IS NOT NULL AND reviewAt <= ? ORDER BY reviewAt ASC, createdAt DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfSessionId: Int = getColumnIndexOrThrow(_stmt, "sessionId")
        val _columnIndexOfSelectedAnswer: Int = getColumnIndexOrThrow(_stmt, "selectedAnswer")
        val _columnIndexOfCorrectAnswer: Int = getColumnIndexOrThrow(_stmt, "correctAnswer")
        val _columnIndexOfUserReason: Int = getColumnIndexOrThrow(_stmt, "userReason")
        val _columnIndexOfCorrectConcept: Int = getColumnIndexOrThrow(_stmt, "correctConcept")
        val _columnIndexOfPreventionNote: Int = getColumnIndexOrThrow(_stmt, "preventionNote")
        val _columnIndexOfLinkedFlashcardId: Int = getColumnIndexOrThrow(_stmt, "linkedFlashcardId")
        val _columnIndexOfLinkedBlueprintId: Int = getColumnIndexOrThrow(_stmt, "linkedBlueprintId")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfIsFixed: Int = getColumnIndexOrThrow(_stmt, "isFixed")
        val _columnIndexOfReviewAt: Int = getColumnIndexOrThrow(_stmt, "reviewAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<MistakeLogEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MistakeLogEntryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpSessionId: Long?
          if (_stmt.isNull(_columnIndexOfSessionId)) {
            _tmpSessionId = null
          } else {
            _tmpSessionId = _stmt.getLong(_columnIndexOfSessionId)
          }
          val _tmpSelectedAnswer: String?
          if (_stmt.isNull(_columnIndexOfSelectedAnswer)) {
            _tmpSelectedAnswer = null
          } else {
            _tmpSelectedAnswer = _stmt.getText(_columnIndexOfSelectedAnswer)
          }
          val _tmpCorrectAnswer: String?
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _tmpCorrectAnswer = null
          } else {
            _tmpCorrectAnswer = _stmt.getText(_columnIndexOfCorrectAnswer)
          }
          val _tmpUserReason: String?
          if (_stmt.isNull(_columnIndexOfUserReason)) {
            _tmpUserReason = null
          } else {
            _tmpUserReason = _stmt.getText(_columnIndexOfUserReason)
          }
          val _tmpCorrectConcept: String?
          if (_stmt.isNull(_columnIndexOfCorrectConcept)) {
            _tmpCorrectConcept = null
          } else {
            _tmpCorrectConcept = _stmt.getText(_columnIndexOfCorrectConcept)
          }
          val _tmpPreventionNote: String?
          if (_stmt.isNull(_columnIndexOfPreventionNote)) {
            _tmpPreventionNote = null
          } else {
            _tmpPreventionNote = _stmt.getText(_columnIndexOfPreventionNote)
          }
          val _tmpLinkedFlashcardId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedFlashcardId)) {
            _tmpLinkedFlashcardId = null
          } else {
            _tmpLinkedFlashcardId = _stmt.getLong(_columnIndexOfLinkedFlashcardId)
          }
          val _tmpLinkedBlueprintId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedBlueprintId)) {
            _tmpLinkedBlueprintId = null
          } else {
            _tmpLinkedBlueprintId = _stmt.getLong(_columnIndexOfLinkedBlueprintId)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpIsFixed: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFixed).toInt()
          _tmpIsFixed = _tmp != 0
          val _tmpReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfReviewAt)) {
            _tmpReviewAt = null
          } else {
            _tmpReviewAt = _stmt.getLong(_columnIndexOfReviewAt)
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
              MistakeLogEntryEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpSessionId,_tmpSelectedAnswer,_tmpCorrectAnswer,_tmpUserReason,_tmpCorrectConcept,_tmpPreventionNote,_tmpLinkedFlashcardId,_tmpLinkedBlueprintId,_tmpLinkedAssetId,_tmpIsFixed,_tmpReviewAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countOpenMistakes(): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM mistake_log_entries WHERE deletedAt IS NULL AND isFixed = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun countFixedMistakes(): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM mistake_log_entries WHERE deletedAt IS NULL AND isFixed = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun countDueMistakes(now: Long): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM mistake_log_entries WHERE deletedAt IS NULL AND isFixed = 0 AND reviewAt IS NOT NULL AND reviewAt <= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
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

  public override suspend fun countPendingMistakes(now: Long): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM mistake_log_entries WHERE deletedAt IS NULL AND isFixed = 0 AND reviewAt IS NOT NULL AND reviewAt > ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
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

  public override suspend fun softDeleteMistakeById(entryId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE mistake_log_entries SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, entryId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreMistakeById(entryId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE mistake_log_entries SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, entryId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markFixed(id: Long, updatedAt: Long) {
    val _sql: String = "UPDATE mistake_log_entries SET isFixed = 1, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun snooze(
    id: Long,
    reviewAt: Long,
    updatedAt: Long,
  ) {
    val _sql: String = "UPDATE mistake_log_entries SET reviewAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, reviewAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, id)
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
