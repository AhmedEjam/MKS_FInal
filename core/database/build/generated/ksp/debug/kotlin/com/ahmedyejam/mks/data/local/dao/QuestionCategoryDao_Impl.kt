package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.QuestionCategoryEntity
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class QuestionCategoryDao_Impl(
  __db: RoomDatabase,
) : QuestionCategoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfQuestionCategoryEntity: EntityInsertAdapter<QuestionCategoryEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfQuestionCategoryEntity = object :
        EntityInsertAdapter<QuestionCategoryEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `question_categories` (`questionId`,`category`,`deletedAt`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionCategoryEntity) {
        statement.bindLong(1, entity.questionId)
        statement.bindText(2, entity.category)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpDeletedAt)
        }
      }
    }
  }

  public override suspend fun insertCategories(categories: List<QuestionCategoryEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfQuestionCategoryEntity.insert(_connection, categories)
  }

  public override suspend fun replaceCategories(questionId: Long, categories: List<String>): Unit =
      performInTransactionSuspending(__db) {
    super@QuestionCategoryDao_Impl.replaceCategories(questionId, categories)
  }

  public override fun getAllQuestionCategories(): Flow<List<String>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT qc.category FROM question_categories qc
        |        INNER JOIN questions q ON qc.questionId = q.id
        |        INNER JOIN quizzes qz ON q.quizId = qz.id
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |        ORDER BY qc.category
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("question_categories", "questions", "quizzes", "books",
        "workspaces")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getQuestionsByCategoryFlow(category: String): Flow<List<QuestionEntity>> {
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
        |          AND (qz.category = ? OR q.id IN (SELECT questionId FROM question_categories WHERE category = ?))
        |        ORDER BY q.id
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("questions", "quizzes", "books", "workspaces",
        "question_categories")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, category)
        _argIndex = 2
        _stmt.bindText(_argIndex, category)
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

  public override suspend fun getQuestionsByCategory(category: String): List<QuestionEntity> {
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
        |          AND (qz.category = ? OR q.id IN (SELECT questionId FROM question_categories WHERE category = ?))
        |        ORDER BY q.id
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, category)
        _argIndex = 2
        _stmt.bindText(_argIndex, category)
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

  public override suspend fun deleteCategoriesForQuestion(questionId: Long) {
    val _sql: String = "DELETE FROM question_categories WHERE questionId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteCategory(category: String) {
    val _sql: String = "DELETE FROM question_categories WHERE category = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, category)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllCategories() {
    val _sql: String = "DELETE FROM question_categories"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
