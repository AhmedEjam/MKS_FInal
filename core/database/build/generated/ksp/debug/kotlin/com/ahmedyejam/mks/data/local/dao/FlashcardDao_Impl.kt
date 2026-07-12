package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.FlashcardEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FlashcardDao_Impl(
  __db: RoomDatabase,
) : FlashcardDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFlashcardEntity: EntityInsertAdapter<FlashcardEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfFlashcardEntity: EntityDeleteOrUpdateAdapter<FlashcardEntity>

  private val __updateAdapterOfFlashcardEntity: EntityDeleteOrUpdateAdapter<FlashcardEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFlashcardEntity = object : EntityInsertAdapter<FlashcardEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `flashcards` (`id`,`externalId`,`deckId`,`frontText`,`backText`,`hint`,`imagePath`,`tags`,`orderIndex`,`attempts`,`correctCount`,`difficulty`,`dueAt`,`reviewCount`,`lastReviewedAt`,`createdAt`,`updatedAt`,`sourceQuestionId`,`syncConfig`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FlashcardEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.deckId)
        statement.bindText(4, entity.frontText)
        statement.bindText(5, entity.backText)
        val _tmpHint: String? = entity.hint
        if (_tmpHint == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpHint)
        }
        val _tmpImagePath: String? = entity.imagePath
        if (_tmpImagePath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpImagePath)
        }
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(8, _tmp)
        statement.bindLong(9, entity.orderIndex.toLong())
        statement.bindLong(10, entity.attempts.toLong())
        statement.bindLong(11, entity.correctCount.toLong())
        val _tmpDifficulty: String? = entity.difficulty
        if (_tmpDifficulty == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpDifficulty)
        }
        statement.bindLong(13, entity.dueAt)
        statement.bindLong(14, entity.reviewCount.toLong())
        statement.bindLong(15, entity.lastReviewedAt)
        statement.bindLong(16, entity.createdAt)
        statement.bindLong(17, entity.updatedAt)
        val _tmpSourceQuestionId: Long? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpSourceQuestionId)
        }
        val _tmp_1: String = __converters.fromStringMap(entity.syncConfig)
        statement.bindText(19, _tmp_1)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfFlashcardEntity = object : EntityDeleteOrUpdateAdapter<FlashcardEntity>()
        {
      protected override fun createQuery(): String = "DELETE FROM `flashcards` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FlashcardEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfFlashcardEntity = object : EntityDeleteOrUpdateAdapter<FlashcardEntity>()
        {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `flashcards` SET `id` = ?,`externalId` = ?,`deckId` = ?,`frontText` = ?,`backText` = ?,`hint` = ?,`imagePath` = ?,`tags` = ?,`orderIndex` = ?,`attempts` = ?,`correctCount` = ?,`difficulty` = ?,`dueAt` = ?,`reviewCount` = ?,`lastReviewedAt` = ?,`createdAt` = ?,`updatedAt` = ?,`sourceQuestionId` = ?,`syncConfig` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FlashcardEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.deckId)
        statement.bindText(4, entity.frontText)
        statement.bindText(5, entity.backText)
        val _tmpHint: String? = entity.hint
        if (_tmpHint == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpHint)
        }
        val _tmpImagePath: String? = entity.imagePath
        if (_tmpImagePath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpImagePath)
        }
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(8, _tmp)
        statement.bindLong(9, entity.orderIndex.toLong())
        statement.bindLong(10, entity.attempts.toLong())
        statement.bindLong(11, entity.correctCount.toLong())
        val _tmpDifficulty: String? = entity.difficulty
        if (_tmpDifficulty == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpDifficulty)
        }
        statement.bindLong(13, entity.dueAt)
        statement.bindLong(14, entity.reviewCount.toLong())
        statement.bindLong(15, entity.lastReviewedAt)
        statement.bindLong(16, entity.createdAt)
        statement.bindLong(17, entity.updatedAt)
        val _tmpSourceQuestionId: Long? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpSourceQuestionId)
        }
        val _tmp_1: String = __converters.fromStringMap(entity.syncConfig)
        statement.bindText(19, _tmp_1)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmpDeletedAt)
        }
        statement.bindLong(21, entity.id)
      }
    }
  }

  public override suspend fun insertFlashcard(card: FlashcardEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfFlashcardEntity.insertAndReturnId(_connection, card)
    _result
  }

  public override suspend fun insertFlashcards(cards: List<FlashcardEntity>): List<Long> =
      performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfFlashcardEntity.insertAndReturnIdsList(_connection,
        cards)
    _result
  }

  public override suspend fun hardDeleteFlashcard(card: FlashcardEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfFlashcardEntity.handle(_connection, card)
  }

  public override suspend fun updateFlashcard(card: FlashcardEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfFlashcardEntity.handle(_connection, card)
  }

  public override fun getFlashcardsByDeckId(deckId: Long): Flow<List<FlashcardEntity>> {
    val _sql: String =
        "SELECT * FROM flashcards WHERE deckId = ? AND deletedAt IS NULL ORDER BY orderIndex ASC"
    return createFlow(__db, false, arrayOf("flashcards")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfFrontText: Int = getColumnIndexOrThrow(_stmt, "frontText")
        val _columnIndexOfBackText: Int = getColumnIndexOrThrow(_stmt, "backText")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpFrontText: String
          _tmpFrontText = _stmt.getText(_columnIndexOfFrontText)
          val _tmpBackText: String
          _tmpBackText = _stmt.getText(_columnIndexOfBackText)
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_1)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              FlashcardEntity(_tmpId,_tmpExternalId,_tmpDeckId,_tmpFrontText,_tmpBackText,_tmpHint,_tmpImagePath,_tmpTags,_tmpOrderIndex,_tmpAttempts,_tmpCorrectCount,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardsByDeckIdNow(deckId: Long): List<FlashcardEntity> {
    val _sql: String =
        "SELECT * FROM flashcards WHERE deckId = ? AND deletedAt IS NULL ORDER BY orderIndex ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfFrontText: Int = getColumnIndexOrThrow(_stmt, "frontText")
        val _columnIndexOfBackText: Int = getColumnIndexOrThrow(_stmt, "backText")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpFrontText: String
          _tmpFrontText = _stmt.getText(_columnIndexOfFrontText)
          val _tmpBackText: String
          _tmpBackText = _stmt.getText(_columnIndexOfBackText)
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_1)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              FlashcardEntity(_tmpId,_tmpExternalId,_tmpDeckId,_tmpFrontText,_tmpBackText,_tmpHint,_tmpImagePath,_tmpTags,_tmpOrderIndex,_tmpAttempts,_tmpCorrectCount,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardById(id: Long): FlashcardEntity? {
    val _sql: String = "SELECT * FROM flashcards WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfFrontText: Int = getColumnIndexOrThrow(_stmt, "frontText")
        val _columnIndexOfBackText: Int = getColumnIndexOrThrow(_stmt, "backText")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: FlashcardEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpFrontText: String
          _tmpFrontText = _stmt.getText(_columnIndexOfFrontText)
          val _tmpBackText: String
          _tmpBackText = _stmt.getText(_columnIndexOfBackText)
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_1)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              FlashcardEntity(_tmpId,_tmpExternalId,_tmpDeckId,_tmpFrontText,_tmpBackText,_tmpHint,_tmpImagePath,_tmpTags,_tmpOrderIndex,_tmpAttempts,_tmpCorrectCount,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardsBySourceQuestionId(questionId: Long):
      List<FlashcardEntity> {
    val _sql: String = "SELECT * FROM flashcards WHERE sourceQuestionId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfFrontText: Int = getColumnIndexOrThrow(_stmt, "frontText")
        val _columnIndexOfBackText: Int = getColumnIndexOrThrow(_stmt, "backText")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpFrontText: String
          _tmpFrontText = _stmt.getText(_columnIndexOfFrontText)
          val _tmpBackText: String
          _tmpBackText = _stmt.getText(_columnIndexOfBackText)
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_1)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              FlashcardEntity(_tmpId,_tmpExternalId,_tmpDeckId,_tmpFrontText,_tmpBackText,_tmpHint,_tmpImagePath,_tmpTags,_tmpOrderIndex,_tmpAttempts,_tmpCorrectCount,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardsByIds(ids: List<Long>): List<FlashcardEntity> {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT * FROM flashcards WHERE id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(") AND deletedAt IS NULL ORDER BY orderIndex ASC")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Long in ids) {
          _stmt.bindLong(_argIndex, _item)
          _argIndex++
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfFrontText: Int = getColumnIndexOrThrow(_stmt, "frontText")
        val _columnIndexOfBackText: Int = getColumnIndexOrThrow(_stmt, "backText")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item_1: FlashcardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpFrontText: String
          _tmpFrontText = _stmt.getText(_columnIndexOfFrontText)
          val _tmpBackText: String
          _tmpBackText = _stmt.getText(_columnIndexOfBackText)
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_1)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item_1 =
              FlashcardEntity(_tmpId,_tmpExternalId,_tmpDeckId,_tmpFrontText,_tmpBackText,_tmpHint,_tmpImagePath,_tmpTags,_tmpOrderIndex,_tmpAttempts,_tmpCorrectCount,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item_1)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = "SELECT COUNT(*) FROM flashcards WHERE deletedAt IS NULL"
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

  public override suspend fun countCardsInDeck(deckId: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM flashcards WHERE deckId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
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

  public override suspend fun countWeakFlashcards(): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM flashcards WHERE deletedAt IS NULL AND attempts >= 2 AND correctCount * 2 < attempts"
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

  public override suspend fun getDueFlashcards(now: Long, limit: Int): List<FlashcardEntity> {
    val _sql: String = """
        |
        |        SELECT * FROM flashcards
        |        WHERE deletedAt IS NULL AND ((dueAt > 0 AND dueAt <= ?)
        |           OR (dueAt = 0 AND lastReviewedAt = 0))
        |        ORDER BY dueAt ASC, createdAt ASC
        |        LIMIT ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfFrontText: Int = getColumnIndexOrThrow(_stmt, "frontText")
        val _columnIndexOfBackText: Int = getColumnIndexOrThrow(_stmt, "backText")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpFrontText: String
          _tmpFrontText = _stmt.getText(_columnIndexOfFrontText)
          val _tmpBackText: String
          _tmpBackText = _stmt.getText(_columnIndexOfBackText)
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_1)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              FlashcardEntity(_tmpId,_tmpExternalId,_tmpDeckId,_tmpFrontText,_tmpBackText,_tmpHint,_tmpImagePath,_tmpTags,_tmpOrderIndex,_tmpAttempts,_tmpCorrectCount,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countDueFlashcards(now: Long): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(*) FROM flashcards
        |        WHERE deletedAt IS NULL AND ((dueAt > 0 AND dueAt <= ?)
        |           OR (dueAt = 0 AND lastReviewedAt = 0))
        |    
        """.trimMargin()
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

  public override suspend fun markReviewed(
    id: Long,
    reviewedAt: Long,
    nextDueAt: Long,
  ) {
    val _sql: String =
        "UPDATE flashcards SET lastReviewedAt = ?, dueAt = ?, reviewCount = reviewCount + 1, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, reviewedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, nextDueAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, reviewedAt)
        _argIndex = 4
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun snooze(
    id: Long,
    dueAt: Long,
    updatedAt: Long,
  ) {
    val _sql: String = "UPDATE flashcards SET dueAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dueAt)
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

  public override suspend fun softDeleteFlashcardById(cardId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE flashcards SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, cardId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteAllCardsInDeck(deckId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE flashcards SET deletedAt = ?, updatedAt = ? WHERE deckId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deckId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreFlashcardById(cardId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE flashcards SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, cardId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreAllCardsInDeck(
    deckId: Long,
    updatedAt: Long,
    deletedAtFilter: Long,
  ) {
    val _sql: String =
        "UPDATE flashcards SET deletedAt = NULL, updatedAt = ? WHERE deckId = ? AND deletedAt = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deckId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAtFilter)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun hardDeleteAllCardsInDeck(deckId: Long) {
    val _sql: String = "DELETE FROM flashcards WHERE deckId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCardOrder(
    cardId: Long,
    orderIndex: Int,
    updatedAt: Long,
  ) {
    val _sql: String = "UPDATE flashcards SET orderIndex = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, orderIndex.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, cardId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun moveCardsToDeck(
    cardIds: List<Long>,
    deckId: Long,
    updatedAt: Long,
  ) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("UPDATE flashcards SET deckId = ")
    _stringBuilder.append("?")
    _stringBuilder.append(", updatedAt = ")
    _stringBuilder.append("?")
    _stringBuilder.append(" WHERE id IN (")
    val _inputSize: Int = cardIds.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        for (_item: Long in cardIds) {
          _stmt.bindLong(_argIndex, _item)
          _argIndex++
        }
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
