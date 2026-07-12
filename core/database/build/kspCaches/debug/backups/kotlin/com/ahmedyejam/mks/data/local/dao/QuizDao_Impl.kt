package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.QuizEntity
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
public class QuizDao_Impl(
  __db: RoomDatabase,
) : QuizDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfQuizEntity: EntityInsertAdapter<QuizEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfQuizEntity: EntityDeleteOrUpdateAdapter<QuizEntity>

  private val __updateAdapterOfQuizEntity: EntityDeleteOrUpdateAdapter<QuizEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfQuizEntity = object : EntityInsertAdapter<QuizEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `quizzes` (`id`,`externalId`,`bookId`,`title`,`description`,`category`,`tags`,`iconName`,`coverImage`,`createdAt`,`updatedAt`,`contentUpdatedAt`,`lastStudiedAt`,`lastEditedAt`,`isPinned`,`isSystem`,`questionCount`,`answeredCount`,`totalAttempts`,`completionPercentage`,`accuracyPercentage`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: QuizEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.description)
        val _tmpCategory: String? = entity.category
        if (_tmpCategory == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpCategory)
        }
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(7, _tmp)
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpCoverImage)
        }
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.updatedAt)
        statement.bindLong(12, entity.contentUpdatedAt)
        statement.bindLong(13, entity.lastStudiedAt)
        statement.bindLong(14, entity.lastEditedAt)
        val _tmp_1: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(15, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(16, _tmp_2.toLong())
        statement.bindLong(17, entity.questionCount.toLong())
        statement.bindLong(18, entity.answeredCount.toLong())
        statement.bindLong(19, entity.totalAttempts.toLong())
        statement.bindDouble(20, entity.completionPercentage.toDouble())
        statement.bindDouble(21, entity.accuracyPercentage.toDouble())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(22)
        } else {
          statement.bindLong(22, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfQuizEntity = object : EntityDeleteOrUpdateAdapter<QuizEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `quizzes` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QuizEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfQuizEntity = object : EntityDeleteOrUpdateAdapter<QuizEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `quizzes` SET `id` = ?,`externalId` = ?,`bookId` = ?,`title` = ?,`description` = ?,`category` = ?,`tags` = ?,`iconName` = ?,`coverImage` = ?,`createdAt` = ?,`updatedAt` = ?,`contentUpdatedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`isPinned` = ?,`isSystem` = ?,`questionCount` = ?,`answeredCount` = ?,`totalAttempts` = ?,`completionPercentage` = ?,`accuracyPercentage` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QuizEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.description)
        val _tmpCategory: String? = entity.category
        if (_tmpCategory == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpCategory)
        }
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(7, _tmp)
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpCoverImage)
        }
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.updatedAt)
        statement.bindLong(12, entity.contentUpdatedAt)
        statement.bindLong(13, entity.lastStudiedAt)
        statement.bindLong(14, entity.lastEditedAt)
        val _tmp_1: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(15, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(16, _tmp_2.toLong())
        statement.bindLong(17, entity.questionCount.toLong())
        statement.bindLong(18, entity.answeredCount.toLong())
        statement.bindLong(19, entity.totalAttempts.toLong())
        statement.bindDouble(20, entity.completionPercentage.toDouble())
        statement.bindDouble(21, entity.accuracyPercentage.toDouble())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(22)
        } else {
          statement.bindLong(22, _tmpDeletedAt)
        }
        statement.bindLong(23, entity.id)
      }
    }
  }

  public override suspend fun insertQuiz(quiz: QuizEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfQuizEntity.insertAndReturnId(_connection, quiz)
    _result
  }

  public override suspend fun hardDeleteQuiz(quiz: QuizEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfQuizEntity.handle(_connection, quiz)
  }

  public override suspend fun updateQuiz(quiz: QuizEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfQuizEntity.handle(_connection, quiz)
  }

  public override fun getQuizzesByBookId(bookId: Long): Flow<List<QuizEntity>> {
    val _sql: String =
        "SELECT * FROM quizzes WHERE bookId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("quizzes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getQuizzesByBookIdSorted(bookId: Long, sortBy: String):
      Flow<List<QuizEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM quizzes 
        |        WHERE bookId = ? AND deletedAt IS NULL
        |        ORDER BY 
        |        CASE WHEN ? = 'TITLE' THEN title END ASC,
        |        CASE WHEN ? = 'QUESTION_COUNT' THEN questionCount END DESC,
        |        CASE WHEN ? = 'COMPLETION' OR ? = 'PROGRESS' THEN completionPercentage END DESC,
        |        CASE WHEN ? = 'ACCURACY' THEN accuracyPercentage END DESC,
        |        CASE WHEN ? = 'LAST_STUDIED' THEN lastStudiedAt END DESC,
        |        CASE WHEN ? = 'LAST_EDIT' THEN lastEditedAt END DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("quizzes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        _argIndex = 2
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 3
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 4
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 5
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 6
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 7
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 8
        _stmt.bindText(_argIndex, sortBy)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getQuizzesByCategory(category: String): Flow<List<QuizEntity>> {
    val _sql: String =
        "SELECT * FROM quizzes WHERE category = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("quizzes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, category)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getQuizzesByCategorySorted(category: String, sortBy: String):
      Flow<List<QuizEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM quizzes 
        |        WHERE category = ? AND deletedAt IS NULL
        |        ORDER BY 
        |        CASE WHEN ? = 'TITLE' THEN title END ASC,
        |        CASE WHEN ? = 'QUESTION_COUNT' THEN questionCount END DESC,
        |        CASE WHEN ? = 'COMPLETION' OR ? = 'PROGRESS' THEN completionPercentage END DESC,
        |        CASE WHEN ? = 'ACCURACY' THEN accuracyPercentage END DESC,
        |        CASE WHEN ? = 'LAST_STUDIED' THEN lastStudiedAt END DESC,
        |        CASE WHEN ? = 'LAST_EDIT' THEN lastEditedAt END DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("quizzes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, category)
        _argIndex = 2
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 3
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 4
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 5
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 6
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 7
        _stmt.bindText(_argIndex, sortBy)
        _argIndex = 8
        _stmt.bindText(_argIndex, sortBy)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuizById(id: Long): QuizEntity? {
    val _sql: String = "SELECT * FROM quizzes WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: QuizEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuizByIdIncludingDeleted(id: Long): QuizEntity? {
    val _sql: String = "SELECT * FROM quizzes WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: QuizEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getQuizByExternalId(externalId: String): QuizEntity? {
    val _sql: String = "SELECT * FROM quizzes WHERE externalId = ? AND deletedAt IS NULL LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, externalId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: QuizEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllQuizzesFlow(): Flow<List<QuizEntity>> {
    val _sql: String = """
        |
        |        SELECT qz.* FROM quizzes qz
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("quizzes", "books", "workspaces")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllQuizzesNow(): List<QuizEntity> {
    val _sql: String = """
        |
        |        SELECT qz.* FROM quizzes qz
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = "SELECT COUNT(*) FROM quizzes WHERE deletedAt IS NULL"
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

  public override fun getAllCategories(): Flow<List<String>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT qz.category FROM quizzes qz
        |        INNER JOIN books b ON qz.bookId = b.id
        |        INNER JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE qz.category IS NOT NULL 
        |          AND qz.deletedAt IS NULL 
        |          AND b.deletedAt IS NULL 
        |          AND w.deletedAt IS NULL
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("quizzes", "books", "workspaces")) { _connection ->
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

  public override fun getQuestionCount(quizId: Long): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM questions WHERE quizId = ? AND deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("questions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
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

  public override fun getBookQuestionCount(bookId: Long): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM questions WHERE deletedAt IS NULL AND quizId IN (SELECT id FROM quizzes WHERE bookId = ? AND deletedAt IS NULL)"
    return createFlow(__db, false, arrayOf("questions", "quizzes")) { _connection ->
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

  public override suspend fun getBookQuestionCountNow(bookId: Long): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM questions WHERE quizId IN (SELECT id FROM quizzes WHERE bookId = ? AND deletedAt IS NULL) AND deletedAt IS NULL"
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

  public override suspend fun getQuizzesByBookIdNow(bookId: Long): List<QuizEntity> {
    val _sql: String = "SELECT * FROM quizzes WHERE bookId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDeletedQuizzesByWorkspaceFlow(workspaceId: Long): Flow<List<QuizEntity>> {
    val _sql: String =
        "SELECT * FROM quizzes WHERE deletedAt IS NOT NULL AND bookId IN (SELECT id FROM books WHERE workspaceId = ?)"
    return createFlow(__db, false, arrayOf("quizzes", "books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuizEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuizEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpIconName: String?
          if (_stmt.isNull(_columnIndexOfIconName)) {
            _tmpIconName = null
          } else {
            _tmpIconName = _stmt.getText(_columnIndexOfIconName)
          }
          val _tmpCoverImage: String?
          if (_stmt.isNull(_columnIndexOfCoverImage)) {
            _tmpCoverImage = null
          } else {
            _tmpCoverImage = _stmt.getText(_columnIndexOfCoverImage)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpContentUpdatedAt: Long
          _tmpContentUpdatedAt = _stmt.getLong(_columnIndexOfContentUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpIsPinned: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_1 != 0
          val _tmpIsSystem: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_2 != 0
          val _tmpQuestionCount: Int
          _tmpQuestionCount = _stmt.getLong(_columnIndexOfQuestionCount).toInt()
          val _tmpAnsweredCount: Int
          _tmpAnsweredCount = _stmt.getLong(_columnIndexOfAnsweredCount).toInt()
          val _tmpTotalAttempts: Int
          _tmpTotalAttempts = _stmt.getLong(_columnIndexOfTotalAttempts).toInt()
          val _tmpCompletionPercentage: Float
          _tmpCompletionPercentage = _stmt.getDouble(_columnIndexOfCompletionPercentage).toFloat()
          val _tmpAccuracyPercentage: Float
          _tmpAccuracyPercentage = _stmt.getDouble(_columnIndexOfAccuracyPercentage).toFloat()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              QuizEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpTags,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteQuizById(quizId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE quizzes SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreQuizById(quizId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE quizzes SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun refreshQuestionCount(quizId: Long) {
    val _sql: String =
        "UPDATE quizzes SET questionCount = (SELECT COUNT(*) FROM questions WHERE quizId = ? AND deletedAt IS NULL) WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateQuestionCount(quizId: Long, count: Int) {
    val _sql: String = "UPDATE quizzes SET questionCount = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, count.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCompletionPercentage(quizId: Long, percentage: Float) {
    val _sql: String = "UPDATE quizzes SET completionPercentage = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, percentage.toDouble())
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateAnsweredCount(quizId: Long, count: Int) {
    val _sql: String = "UPDATE quizzes SET answeredCount = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, count.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateTotalAttempts(quizId: Long, count: Int) {
    val _sql: String = "UPDATE quizzes SET totalAttempts = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, count.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateAccuracyPercentage(quizId: Long, percentage: Float) {
    val _sql: String = "UPDATE quizzes SET accuracyPercentage = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, percentage.toDouble())
        _argIndex = 2
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllQuizCategories(updatedAt: Long) {
    val _sql: String =
        "UPDATE quizzes SET category = NULL, updatedAt = ?, lastEditedAt = ? WHERE category IS NOT NULL AND category != ''"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
