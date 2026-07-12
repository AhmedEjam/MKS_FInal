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
import com.ahmedyejam.mks.`data`.local.entity.QuestionEntity
import com.ahmedyejam.mks.`data`.local.entity.QuestionType
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
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class QuestionDao_Impl(
  __db: RoomDatabase,
) : QuestionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfQuestionEntity: EntityInsertAdapter<QuestionEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfQuestionEntity: EntityDeleteOrUpdateAdapter<QuestionEntity>

  private val __updateAdapterOfQuestionEntity: EntityDeleteOrUpdateAdapter<QuestionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfQuestionEntity = object : EntityInsertAdapter<QuestionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `questions` (`id`,`externalId`,`quizId`,`text`,`type`,`options`,`correctAnswers`,`explanation`,`hint`,`reference`,`weight`,`imagePath`,`imageName`,`imageSource`,`attempts`,`correctCount`,`isDropped`,`droppedAt`,`droppedReason`,`isMarked`,`markedAt`,`markReason`,`markReviewAt`,`notes`,`categories`,`tags`,`difficulty`,`dueAt`,`reviewCount`,`lastReviewedAt`,`additionalInfo`,`sourceBookId`,`sourceQuizId`,`sourceQuestionId`,`createdAt`,`updatedAt`,`lastStudiedAt`,`lastEditedAt`,`timeSpentMs`,`lastAttemptResult`,`consecutiveCorrect`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.quizId)
        statement.bindText(4, entity.text)
        val _tmp: String = __converters.fromQuestionType(entity.type)
        statement.bindText(5, _tmp)
        val _tmp_1: String = __converters.fromStringList(entity.options)
        statement.bindText(6, _tmp_1)
        val _tmp_2: String = __converters.fromIntList(entity.correctAnswers)
        statement.bindText(7, _tmp_2)
        val _tmpExplanation: String? = entity.explanation
        if (_tmpExplanation == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpExplanation)
        }
        val _tmpHint: String? = entity.hint
        if (_tmpHint == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpHint)
        }
        val _tmpReference: String? = entity.reference
        if (_tmpReference == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpReference)
        }
        statement.bindLong(11, entity.weight.toLong())
        val _tmpImagePath: String? = entity.imagePath
        if (_tmpImagePath == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpImagePath)
        }
        val _tmpImageName: String? = entity.imageName
        if (_tmpImageName == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpImageName)
        }
        val _tmpImageSource: String? = entity.imageSource
        if (_tmpImageSource == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmpImageSource)
        }
        statement.bindLong(15, entity.attempts.toLong())
        statement.bindLong(16, entity.correctCount.toLong())
        val _tmp_3: Int = if (entity.isDropped) 1 else 0
        statement.bindLong(17, _tmp_3.toLong())
        val _tmpDroppedAt: Long? = entity.droppedAt
        if (_tmpDroppedAt == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpDroppedAt)
        }
        val _tmpDroppedReason: String? = entity.droppedReason
        if (_tmpDroppedReason == null) {
          statement.bindNull(19)
        } else {
          statement.bindText(19, _tmpDroppedReason)
        }
        val _tmp_4: Int = if (entity.isMarked) 1 else 0
        statement.bindLong(20, _tmp_4.toLong())
        val _tmpMarkedAt: Long? = entity.markedAt
        if (_tmpMarkedAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindLong(21, _tmpMarkedAt)
        }
        val _tmpMarkReason: String? = entity.markReason
        if (_tmpMarkReason == null) {
          statement.bindNull(22)
        } else {
          statement.bindText(22, _tmpMarkReason)
        }
        val _tmpMarkReviewAt: Long? = entity.markReviewAt
        if (_tmpMarkReviewAt == null) {
          statement.bindNull(23)
        } else {
          statement.bindLong(23, _tmpMarkReviewAt)
        }
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmpNotes)
        }
        val _tmp_5: String = __converters.fromStringList(entity.categories)
        statement.bindText(25, _tmp_5)
        val _tmp_6: String = __converters.fromStringList(entity.tags)
        statement.bindText(26, _tmp_6)
        val _tmpDifficulty: String? = entity.difficulty
        if (_tmpDifficulty == null) {
          statement.bindNull(27)
        } else {
          statement.bindText(27, _tmpDifficulty)
        }
        statement.bindLong(28, entity.dueAt)
        statement.bindLong(29, entity.reviewCount.toLong())
        statement.bindLong(30, entity.lastReviewedAt)
        val _tmpAdditionalInfo: String? = entity.additionalInfo
        if (_tmpAdditionalInfo == null) {
          statement.bindNull(31)
        } else {
          statement.bindText(31, _tmpAdditionalInfo)
        }
        val _tmpSourceBookId: String? = entity.sourceBookId
        if (_tmpSourceBookId == null) {
          statement.bindNull(32)
        } else {
          statement.bindText(32, _tmpSourceBookId)
        }
        val _tmpSourceQuizId: String? = entity.sourceQuizId
        if (_tmpSourceQuizId == null) {
          statement.bindNull(33)
        } else {
          statement.bindText(33, _tmpSourceQuizId)
        }
        val _tmpSourceQuestionId: String? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(34)
        } else {
          statement.bindText(34, _tmpSourceQuestionId)
        }
        statement.bindLong(35, entity.createdAt)
        statement.bindLong(36, entity.updatedAt)
        statement.bindLong(37, entity.lastStudiedAt)
        statement.bindLong(38, entity.lastEditedAt)
        statement.bindLong(39, entity.timeSpentMs)
        val _tmpLastAttemptResult: Boolean? = entity.lastAttemptResult
        val _tmp_7: Int? = _tmpLastAttemptResult?.let { if (it) 1 else 0 }
        if (_tmp_7 == null) {
          statement.bindNull(40)
        } else {
          statement.bindLong(40, _tmp_7.toLong())
        }
        statement.bindLong(41, entity.consecutiveCorrect.toLong())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(42)
        } else {
          statement.bindLong(42, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfQuestionEntity = object : EntityDeleteOrUpdateAdapter<QuestionEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `questions` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfQuestionEntity = object : EntityDeleteOrUpdateAdapter<QuestionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `questions` SET `id` = ?,`externalId` = ?,`quizId` = ?,`text` = ?,`type` = ?,`options` = ?,`correctAnswers` = ?,`explanation` = ?,`hint` = ?,`reference` = ?,`weight` = ?,`imagePath` = ?,`imageName` = ?,`imageSource` = ?,`attempts` = ?,`correctCount` = ?,`isDropped` = ?,`droppedAt` = ?,`droppedReason` = ?,`isMarked` = ?,`markedAt` = ?,`markReason` = ?,`markReviewAt` = ?,`notes` = ?,`categories` = ?,`tags` = ?,`difficulty` = ?,`dueAt` = ?,`reviewCount` = ?,`lastReviewedAt` = ?,`additionalInfo` = ?,`sourceBookId` = ?,`sourceQuizId` = ?,`sourceQuestionId` = ?,`createdAt` = ?,`updatedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`timeSpentMs` = ?,`lastAttemptResult` = ?,`consecutiveCorrect` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.quizId)
        statement.bindText(4, entity.text)
        val _tmp: String = __converters.fromQuestionType(entity.type)
        statement.bindText(5, _tmp)
        val _tmp_1: String = __converters.fromStringList(entity.options)
        statement.bindText(6, _tmp_1)
        val _tmp_2: String = __converters.fromIntList(entity.correctAnswers)
        statement.bindText(7, _tmp_2)
        val _tmpExplanation: String? = entity.explanation
        if (_tmpExplanation == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpExplanation)
        }
        val _tmpHint: String? = entity.hint
        if (_tmpHint == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpHint)
        }
        val _tmpReference: String? = entity.reference
        if (_tmpReference == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpReference)
        }
        statement.bindLong(11, entity.weight.toLong())
        val _tmpImagePath: String? = entity.imagePath
        if (_tmpImagePath == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpImagePath)
        }
        val _tmpImageName: String? = entity.imageName
        if (_tmpImageName == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpImageName)
        }
        val _tmpImageSource: String? = entity.imageSource
        if (_tmpImageSource == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmpImageSource)
        }
        statement.bindLong(15, entity.attempts.toLong())
        statement.bindLong(16, entity.correctCount.toLong())
        val _tmp_3: Int = if (entity.isDropped) 1 else 0
        statement.bindLong(17, _tmp_3.toLong())
        val _tmpDroppedAt: Long? = entity.droppedAt
        if (_tmpDroppedAt == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpDroppedAt)
        }
        val _tmpDroppedReason: String? = entity.droppedReason
        if (_tmpDroppedReason == null) {
          statement.bindNull(19)
        } else {
          statement.bindText(19, _tmpDroppedReason)
        }
        val _tmp_4: Int = if (entity.isMarked) 1 else 0
        statement.bindLong(20, _tmp_4.toLong())
        val _tmpMarkedAt: Long? = entity.markedAt
        if (_tmpMarkedAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindLong(21, _tmpMarkedAt)
        }
        val _tmpMarkReason: String? = entity.markReason
        if (_tmpMarkReason == null) {
          statement.bindNull(22)
        } else {
          statement.bindText(22, _tmpMarkReason)
        }
        val _tmpMarkReviewAt: Long? = entity.markReviewAt
        if (_tmpMarkReviewAt == null) {
          statement.bindNull(23)
        } else {
          statement.bindLong(23, _tmpMarkReviewAt)
        }
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmpNotes)
        }
        val _tmp_5: String = __converters.fromStringList(entity.categories)
        statement.bindText(25, _tmp_5)
        val _tmp_6: String = __converters.fromStringList(entity.tags)
        statement.bindText(26, _tmp_6)
        val _tmpDifficulty: String? = entity.difficulty
        if (_tmpDifficulty == null) {
          statement.bindNull(27)
        } else {
          statement.bindText(27, _tmpDifficulty)
        }
        statement.bindLong(28, entity.dueAt)
        statement.bindLong(29, entity.reviewCount.toLong())
        statement.bindLong(30, entity.lastReviewedAt)
        val _tmpAdditionalInfo: String? = entity.additionalInfo
        if (_tmpAdditionalInfo == null) {
          statement.bindNull(31)
        } else {
          statement.bindText(31, _tmpAdditionalInfo)
        }
        val _tmpSourceBookId: String? = entity.sourceBookId
        if (_tmpSourceBookId == null) {
          statement.bindNull(32)
        } else {
          statement.bindText(32, _tmpSourceBookId)
        }
        val _tmpSourceQuizId: String? = entity.sourceQuizId
        if (_tmpSourceQuizId == null) {
          statement.bindNull(33)
        } else {
          statement.bindText(33, _tmpSourceQuizId)
        }
        val _tmpSourceQuestionId: String? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(34)
        } else {
          statement.bindText(34, _tmpSourceQuestionId)
        }
        statement.bindLong(35, entity.createdAt)
        statement.bindLong(36, entity.updatedAt)
        statement.bindLong(37, entity.lastStudiedAt)
        statement.bindLong(38, entity.lastEditedAt)
        statement.bindLong(39, entity.timeSpentMs)
        val _tmpLastAttemptResult: Boolean? = entity.lastAttemptResult
        val _tmp_7: Int? = _tmpLastAttemptResult?.let { if (it) 1 else 0 }
        if (_tmp_7 == null) {
          statement.bindNull(40)
        } else {
          statement.bindLong(40, _tmp_7.toLong())
        }
        statement.bindLong(41, entity.consecutiveCorrect.toLong())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(42)
        } else {
          statement.bindLong(42, _tmpDeletedAt)
        }
        statement.bindLong(43, entity.id)
      }
    }
  }

  public override suspend fun insertQuestion(question: QuestionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfQuestionEntity.insertAndReturnId(_connection, question)
    _result
  }

  public override suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> =
      performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfQuestionEntity.insertAndReturnIdsList(_connection,
        questions)
    _result
  }

  public override suspend fun hardDeleteQuestion(question: QuestionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfQuestionEntity.handle(_connection, question)
  }

  public override suspend fun updateQuestion(question: QuestionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfQuestionEntity.handle(_connection, question)
  }

  public override suspend fun updateQuestions(questions: List<QuestionEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfQuestionEntity.handleMultiple(_connection, questions)
  }

  public override fun getQuestionsByQuizId(quizId: Long): Flow<List<QuestionEntity>> {
    val _sql: String = "SELECT * FROM questions WHERE quizId = ? AND deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("questions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuestionsByQuizIdNow(quizId: Long): List<QuestionEntity> {
    val _sql: String = "SELECT * FROM questions WHERE quizId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuestionsByQuizIdIncludingDeleted(quizId: Long):
      List<QuestionEntity> {
    val _sql: String = "SELECT * FROM questions WHERE quizId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuestionById(id: Long): QuestionEntity? {
    val _sql: String = "SELECT * FROM questions WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: QuestionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuestionByExternalId(quizId: Long, externalId: String):
      QuestionEntity? {
    val _sql: String =
        "SELECT * FROM questions WHERE quizId = ? AND externalId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        _argIndex = 2
        _stmt.bindText(_argIndex, externalId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: QuestionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuestionsByIds(ids: List<Long>): List<QuestionEntity> {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT * FROM questions WHERE id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(") AND deletedAt IS NULL")
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
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item_1: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item_1 =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item_1)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAdaptiveQuestionsByBook(bookId: Long, limit: Int):
      List<QuestionEntity> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND (? = -1 OR qz.bookId = ?)
        |        ORDER BY 
        |            q.attempts ASC, 
        |            (CAST(q.correctCount AS FLOAT) / CASE WHEN q.attempts = 0 THEN 0.1 ELSE q.attempts END) ASC, 
        |            q.lastStudiedAt ASC 
        |        LIMIT ?
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
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllQuestionsFlow(): Flow<List<QuestionEntity>> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("questions", "quizzes", "books", "workspaces")) {
        _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllQuestionsNow(): List<QuestionEntity> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |    
        """.trimMargin()
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

  public override suspend fun countUnanswered(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.attempts = 0
        |    
        """.trimMargin()
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

  public override suspend fun countWithNotes(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.notes IS NOT NULL AND TRIM(q.notes) != ''
        |    
        """.trimMargin()
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

  public override suspend fun countMarked(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.isMarked = 1
        |    
        """.trimMargin()
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

  public override suspend fun countDropped(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.isDropped = 1
        |    
        """.trimMargin()
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

  public override suspend fun countMissed(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.attempts > 0 AND (q.correctCount < q.attempts OR q.lastAttemptResult = 0)
        |    
        """.trimMargin()
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

  public override suspend fun countWeak(): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(q.id) FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.attempts >= 2 AND q.correctCount * 2 < q.attempts
        |    
        """.trimMargin()
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

  public override suspend fun getMarkedQuestionsForReview(now: Long, limit: Int):
      List<QuestionEntity> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.isMarked = 1 
        |          AND (q.markReviewAt IS NULL OR q.markReviewAt <= ?) 
        |        ORDER BY COALESCE(q.markReviewAt, q.markedAt, q.updatedAt) ASC 
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
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMarkedQuestionsByQuiz(quizId: Long): List<QuestionEntity> {
    val _sql: String =
        "SELECT * FROM questions WHERE quizId = ? AND deletedAt IS NULL AND isMarked = 1 ORDER BY markedAt DESC, updatedAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getWeakQuestionsDue(cutoff: Long, limit: Int): List<QuestionEntity> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.attempts > 0
        |          AND q.correctCount < q.attempts
        |          AND q.lastStudiedAt <= ?
        |        ORDER BY q.lastStudiedAt ASC, q.updatedAt DESC
        |        LIMIT ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, cutoff)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMarkedQuestionsDueForReview(now: Long, limit: Int):
      List<QuestionEntity> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND q.markReviewAt IS NOT NULL 
        |          AND q.markReviewAt <= ? 
        |        ORDER BY q.markReviewAt ASC 
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
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun searchAllQuestionsFlow(searchQuery: String): Flow<List<QuestionEntity>> {
    val _sql: String = """
        |
        |        SELECT q.* FROM questions q
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |          AND (q.text LIKE '%' || ? || '%' 
        |               OR q.explanation LIKE '%' || ? || '%'
        |               OR q.options LIKE '%' || ? || '%')
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("questions", "quizzes", "books", "workspaces")) {
        _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, searchQuery)
        _argIndex = 2
        _stmt.bindText(_argIndex, searchQuery)
        _argIndex = 3
        _stmt.bindText(_argIndex, searchQuery)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfOptions: Int = getColumnIndexOrThrow(_stmt, "options")
        val _columnIndexOfCorrectAnswers: Int = getColumnIndexOrThrow(_stmt, "correctAnswers")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfHint: Int = getColumnIndexOrThrow(_stmt, "hint")
        val _columnIndexOfReference: Int = getColumnIndexOrThrow(_stmt, "reference")
        val _columnIndexOfWeight: Int = getColumnIndexOrThrow(_stmt, "weight")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfImageName: Int = getColumnIndexOrThrow(_stmt, "imageName")
        val _columnIndexOfImageSource: Int = getColumnIndexOrThrow(_stmt, "imageSource")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfCorrectCount: Int = getColumnIndexOrThrow(_stmt, "correctCount")
        val _columnIndexOfIsDropped: Int = getColumnIndexOrThrow(_stmt, "isDropped")
        val _columnIndexOfDroppedAt: Int = getColumnIndexOrThrow(_stmt, "droppedAt")
        val _columnIndexOfDroppedReason: Int = getColumnIndexOrThrow(_stmt, "droppedReason")
        val _columnIndexOfIsMarked: Int = getColumnIndexOrThrow(_stmt, "isMarked")
        val _columnIndexOfMarkedAt: Int = getColumnIndexOrThrow(_stmt, "markedAt")
        val _columnIndexOfMarkReason: Int = getColumnIndexOrThrow(_stmt, "markReason")
        val _columnIndexOfMarkReviewAt: Int = getColumnIndexOrThrow(_stmt, "markReviewAt")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCategories: Int = getColumnIndexOrThrow(_stmt, "categories")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfAdditionalInfo: Int = getColumnIndexOrThrow(_stmt, "additionalInfo")
        val _columnIndexOfSourceBookId: Int = getColumnIndexOrThrow(_stmt, "sourceBookId")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfTimeSpentMs: Int = getColumnIndexOrThrow(_stmt, "timeSpentMs")
        val _columnIndexOfLastAttemptResult: Int = getColumnIndexOrThrow(_stmt, "lastAttemptResult")
        val _columnIndexOfConsecutiveCorrect: Int = getColumnIndexOrThrow(_stmt,
            "consecutiveCorrect")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpType: QuestionType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toQuestionType(_tmp)
          val _tmpOptions: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfOptions)
          _tmpOptions = __converters.toStringList(_tmp_1)
          val _tmpCorrectAnswers: List<Int>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfCorrectAnswers)
          _tmpCorrectAnswers = __converters.toIntList(_tmp_2)
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpHint: String?
          if (_stmt.isNull(_columnIndexOfHint)) {
            _tmpHint = null
          } else {
            _tmpHint = _stmt.getText(_columnIndexOfHint)
          }
          val _tmpReference: String?
          if (_stmt.isNull(_columnIndexOfReference)) {
            _tmpReference = null
          } else {
            _tmpReference = _stmt.getText(_columnIndexOfReference)
          }
          val _tmpWeight: Int
          _tmpWeight = _stmt.getLong(_columnIndexOfWeight).toInt()
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpImageName: String?
          if (_stmt.isNull(_columnIndexOfImageName)) {
            _tmpImageName = null
          } else {
            _tmpImageName = _stmt.getText(_columnIndexOfImageName)
          }
          val _tmpImageSource: String?
          if (_stmt.isNull(_columnIndexOfImageSource)) {
            _tmpImageSource = null
          } else {
            _tmpImageSource = _stmt.getText(_columnIndexOfImageSource)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpCorrectCount: Int
          _tmpCorrectCount = _stmt.getLong(_columnIndexOfCorrectCount).toInt()
          val _tmpIsDropped: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDropped).toInt()
          _tmpIsDropped = _tmp_3 != 0
          val _tmpDroppedAt: Long?
          if (_stmt.isNull(_columnIndexOfDroppedAt)) {
            _tmpDroppedAt = null
          } else {
            _tmpDroppedAt = _stmt.getLong(_columnIndexOfDroppedAt)
          }
          val _tmpDroppedReason: String?
          if (_stmt.isNull(_columnIndexOfDroppedReason)) {
            _tmpDroppedReason = null
          } else {
            _tmpDroppedReason = _stmt.getText(_columnIndexOfDroppedReason)
          }
          val _tmpIsMarked: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfIsMarked).toInt()
          _tmpIsMarked = _tmp_4 != 0
          val _tmpMarkedAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkedAt)) {
            _tmpMarkedAt = null
          } else {
            _tmpMarkedAt = _stmt.getLong(_columnIndexOfMarkedAt)
          }
          val _tmpMarkReason: String?
          if (_stmt.isNull(_columnIndexOfMarkReason)) {
            _tmpMarkReason = null
          } else {
            _tmpMarkReason = _stmt.getText(_columnIndexOfMarkReason)
          }
          val _tmpMarkReviewAt: Long?
          if (_stmt.isNull(_columnIndexOfMarkReviewAt)) {
            _tmpMarkReviewAt = null
          } else {
            _tmpMarkReviewAt = _stmt.getLong(_columnIndexOfMarkReviewAt)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCategories: List<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfCategories)
          _tmpCategories = __converters.toStringList(_tmp_5)
          val _tmpTags: List<String>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_6)
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
          val _tmpAdditionalInfo: String?
          if (_stmt.isNull(_columnIndexOfAdditionalInfo)) {
            _tmpAdditionalInfo = null
          } else {
            _tmpAdditionalInfo = _stmt.getText(_columnIndexOfAdditionalInfo)
          }
          val _tmpSourceBookId: String?
          if (_stmt.isNull(_columnIndexOfSourceBookId)) {
            _tmpSourceBookId = null
          } else {
            _tmpSourceBookId = _stmt.getText(_columnIndexOfSourceBookId)
          }
          val _tmpSourceQuizId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getText(_columnIndexOfSourceQuizId)
          }
          val _tmpSourceQuestionId: String?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getText(_columnIndexOfSourceQuestionId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpTimeSpentMs: Long
          _tmpTimeSpentMs = _stmt.getLong(_columnIndexOfTimeSpentMs)
          val _tmpLastAttemptResult: Boolean?
          val _tmp_7: Int?
          if (_stmt.isNull(_columnIndexOfLastAttemptResult)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfLastAttemptResult).toInt()
          }
          _tmpLastAttemptResult = _tmp_7?.let { it != 0 }
          val _tmpConsecutiveCorrect: Int
          _tmpConsecutiveCorrect = _stmt.getLong(_columnIndexOfConsecutiveCorrect).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuestionEntity(_tmpId,_tmpExternalId,_tmpQuizId,_tmpText,_tmpType,_tmpOptions,_tmpCorrectAnswers,_tmpExplanation,_tmpHint,_tmpReference,_tmpWeight,_tmpImagePath,_tmpImageName,_tmpImageSource,_tmpAttempts,_tmpCorrectCount,_tmpIsDropped,_tmpDroppedAt,_tmpDroppedReason,_tmpIsMarked,_tmpMarkedAt,_tmpMarkReason,_tmpMarkReviewAt,_tmpNotes,_tmpCategories,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpAdditionalInfo,_tmpSourceBookId,_tmpSourceQuizId,_tmpSourceQuestionId,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpTimeSpentMs,_tmpLastAttemptResult,_tmpConsecutiveCorrect,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteQuestionById(questionId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE questions SET deletedAt = ?, updatedAt = ?, lastEditedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 4
        _stmt.bindLong(_argIndex, questionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteQuestionsByQuizId(quizId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE questions SET deletedAt = ?, updatedAt = ?, lastEditedAt = ? WHERE quizId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 4
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreQuestionById(questionId: Long, updatedAt: Long) {
    val _sql: String =
        "UPDATE questions SET deletedAt = NULL, updatedAt = ?, lastEditedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, questionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreQuestionsByQuizId(
    quizId: Long,
    updatedAt: Long,
    deletedAtFilter: Long,
  ) {
    val _sql: String =
        "UPDATE questions SET deletedAt = NULL, updatedAt = ?, lastEditedAt = ? WHERE quizId = ? AND deletedAt = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, quizId)
        _argIndex = 4
        _stmt.bindLong(_argIndex, deletedAtFilter)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updatePerformanceMetrics(
    id: Long,
    isCorrect: Boolean,
    isCorrectInt: Int,
    timeSpentMs: Long,
    now: Long,
  ) {
    val _sql: String = """
        |
        |        UPDATE questions 
        |        SET attempts = attempts + 1, 
        |            correctCount = correctCount + ?,
        |            lastStudiedAt = ?,
        |            timeSpentMs = timeSpentMs + ?,
        |            lastAttemptResult = ?,
        |            consecutiveCorrect = CASE WHEN ? = 1 THEN consecutiveCorrect + 1 ELSE 0 END,
        |            updatedAt = ?
        |        WHERE id = ?
        |    
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, isCorrectInt.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, now)
        _argIndex = 3
        _stmt.bindLong(_argIndex, timeSpentMs)
        _argIndex = 4
        val _tmp: Int = if (isCorrect) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 5
        _stmt.bindLong(_argIndex, isCorrectInt.toLong())
        _argIndex = 6
        _stmt.bindLong(_argIndex, now)
        _argIndex = 7
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllQuestionCategories(updatedAt: Long) {
    val _sql: String =
        "UPDATE questions SET categories = '[]', updatedAt = ?, lastEditedAt = ? WHERE categories IS NOT NULL AND categories != '[]'"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearMarksForQuiz(quizId: Long, updatedAt: Long) {
    val _sql: String =
        "UPDATE questions SET isMarked = 0, markedAt = NULL, markReason = NULL, markReviewAt = NULL, updatedAt = ?, lastEditedAt = ? WHERE quizId = ? AND isMarked = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearQuestionMark(questionId: Long, updatedAt: Long) {
    val _sql: String =
        "UPDATE questions SET isMarked = 0, markedAt = NULL, markReason = NULL, markReviewAt = NULL, updatedAt = ?, lastEditedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, questionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markQuestionReviewed(questionId: Long, reviewedAt: Long) {
    val _sql: String = "UPDATE questions SET lastStudiedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, reviewedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, reviewedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, questionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun snoozeMarkedQuestion(
    questionId: Long,
    reviewAt: Long,
    updatedAt: Long,
  ) {
    val _sql: String =
        "UPDATE questions SET markReviewAt = ?, updatedAt = ? WHERE id = ? AND isMarked = 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, reviewAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, questionId)
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
