package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.BookEntity
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
public class BookDao_Impl(
  __db: RoomDatabase,
) : BookDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBookEntity: EntityInsertAdapter<BookEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfBookEntity: EntityDeleteOrUpdateAdapter<BookEntity>

  private val __updateAdapterOfBookEntity: EntityDeleteOrUpdateAdapter<BookEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfBookEntity = object : EntityInsertAdapter<BookEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `books` (`id`,`workspaceId`,`externalId`,`title`,`description`,`iconName`,`coverImage`,`createdAt`,`updatedAt`,`contentUpdatedAt`,`lastStudiedAt`,`lastEditedAt`,`isPinned`,`isSystem`,`fields`,`questionCount`,`answeredCount`,`totalAttempts`,`completionPercentage`,`accuracyPercentage`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BookEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.workspaceId)
        statement.bindText(3, entity.externalId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.description)
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpCoverImage)
        }
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        statement.bindLong(10, entity.contentUpdatedAt)
        statement.bindLong(11, entity.lastStudiedAt)
        statement.bindLong(12, entity.lastEditedAt)
        val _tmp: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(13, _tmp.toLong())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(14, _tmp_1.toLong())
        val _tmp_2: String = __converters.fromStringList(entity.fields)
        statement.bindText(15, _tmp_2)
        statement.bindLong(16, entity.questionCount.toLong())
        statement.bindLong(17, entity.answeredCount.toLong())
        statement.bindLong(18, entity.totalAttempts.toLong())
        statement.bindDouble(19, entity.completionPercentage.toDouble())
        statement.bindDouble(20, entity.accuracyPercentage.toDouble())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindLong(21, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfBookEntity = object : EntityDeleteOrUpdateAdapter<BookEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `books` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BookEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfBookEntity = object : EntityDeleteOrUpdateAdapter<BookEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `books` SET `id` = ?,`workspaceId` = ?,`externalId` = ?,`title` = ?,`description` = ?,`iconName` = ?,`coverImage` = ?,`createdAt` = ?,`updatedAt` = ?,`contentUpdatedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`isPinned` = ?,`isSystem` = ?,`fields` = ?,`questionCount` = ?,`answeredCount` = ?,`totalAttempts` = ?,`completionPercentage` = ?,`accuracyPercentage` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BookEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.workspaceId)
        statement.bindText(3, entity.externalId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.description)
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpCoverImage)
        }
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        statement.bindLong(10, entity.contentUpdatedAt)
        statement.bindLong(11, entity.lastStudiedAt)
        statement.bindLong(12, entity.lastEditedAt)
        val _tmp: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(13, _tmp.toLong())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(14, _tmp_1.toLong())
        val _tmp_2: String = __converters.fromStringList(entity.fields)
        statement.bindText(15, _tmp_2)
        statement.bindLong(16, entity.questionCount.toLong())
        statement.bindLong(17, entity.answeredCount.toLong())
        statement.bindLong(18, entity.totalAttempts.toLong())
        statement.bindDouble(19, entity.completionPercentage.toDouble())
        statement.bindDouble(20, entity.accuracyPercentage.toDouble())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindLong(21, _tmpDeletedAt)
        }
        statement.bindLong(22, entity.id)
      }
    }
  }

  public override suspend fun insertBook(book: BookEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfBookEntity.insertAndReturnId(_connection, book)
    _result
  }

  public override suspend fun hardDeleteBook(book: BookEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfBookEntity.handle(_connection, book)
  }

  public override suspend fun updateBook(book: BookEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfBookEntity.handle(_connection, book)
  }

  public override fun getAllBooksFlow(): Flow<List<BookEntity>> {
    val _sql: String = "SELECT * FROM books WHERE deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<BookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllBooksNow(): List<BookEntity> {
    val _sql: String = "SELECT * FROM books WHERE deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<BookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getBooksByWorkspaceFlow(workspaceId: Long): Flow<List<BookEntity>> {
    val _sql: String = "SELECT * FROM books WHERE workspaceId = ? AND deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<BookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = "SELECT COUNT(*) FROM books WHERE deletedAt IS NULL"
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

  public override fun getAllBooksSortedFlow(sortBy: String): Flow<List<BookEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM books 
        |        WHERE deletedAt IS NULL
        |        ORDER BY 
        |        CASE WHEN ? = 'TITLE' THEN title END ASC,
        |        CASE WHEN ? = 'QUESTION_COUNT' THEN questionCount END DESC,
        |        CASE WHEN ? = 'COMPLETION' OR ? = 'PROGRESS' THEN completionPercentage END DESC,
        |        CASE WHEN ? = 'ACCURACY' THEN accuracyPercentage END DESC,
        |        CASE WHEN ? = 'LAST_STUDIED' THEN lastStudiedAt END DESC,
        |        CASE WHEN ? = 'LAST_EDIT' THEN lastEditedAt END DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, sortBy)
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
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<BookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getBooksByWorkspaceSortedFlow(workspaceId: Long, sortBy: String):
      Flow<List<BookEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM books
        |        WHERE workspaceId = ? AND deletedAt IS NULL
        |        ORDER BY
        |        CASE WHEN ? = 'TITLE' THEN title END ASC,
        |        CASE WHEN ? = 'QUESTION_COUNT' THEN questionCount END DESC,
        |        CASE WHEN ? = 'COMPLETION' OR ? = 'PROGRESS' THEN completionPercentage END DESC,
        |        CASE WHEN ? = 'ACCURACY' THEN accuracyPercentage END DESC,
        |        CASE WHEN ? = 'LAST_STUDIED' THEN lastStudiedAt END DESC,
        |        CASE WHEN ? = 'LAST_EDIT' THEN lastEditedAt END DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
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
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<BookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookById(id: Long): BookEntity? {
    val _sql: String = "SELECT * FROM books WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: BookEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookByIdIncludingDeleted(id: Long): BookEntity? {
    val _sql: String = "SELECT * FROM books WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: BookEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookByExternalId(externalId: String): BookEntity? {
    val _sql: String = "SELECT * FROM books WHERE externalId = ? AND deletedAt IS NULL LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, externalId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: BookEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookByExternalIdInWorkspace(externalId: String, workspaceId: Long):
      BookEntity? {
    val _sql: String =
        "SELECT * FROM books WHERE externalId = ? AND workspaceId = ? AND deletedAt IS NULL LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, externalId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: BookEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDeletedBooksByWorkspaceFlow(workspaceId: Long): Flow<List<BookEntity>> {
    val _sql: String = "SELECT * FROM books WHERE workspaceId = ? AND deletedAt IS NOT NULL"
    return createFlow(__db, false, arrayOf("books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfWorkspaceId: Int = getColumnIndexOrThrow(_stmt, "workspaceId")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfContentUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "contentUpdatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfFields: Int = getColumnIndexOrThrow(_stmt, "fields")
        val _columnIndexOfQuestionCount: Int = getColumnIndexOrThrow(_stmt, "questionCount")
        val _columnIndexOfAnsweredCount: Int = getColumnIndexOrThrow(_stmt, "answeredCount")
        val _columnIndexOfTotalAttempts: Int = getColumnIndexOrThrow(_stmt, "totalAttempts")
        val _columnIndexOfCompletionPercentage: Int = getColumnIndexOrThrow(_stmt,
            "completionPercentage")
        val _columnIndexOfAccuracyPercentage: Int = getColumnIndexOrThrow(_stmt,
            "accuracyPercentage")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<BookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpWorkspaceId: Long
          _tmpWorkspaceId = _stmt.getLong(_columnIndexOfWorkspaceId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpFields: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfFields)
          _tmpFields = __converters.toStringList(_tmp_2)
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
              BookEntity(_tmpId,_tmpWorkspaceId,_tmpExternalId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpCreatedAt,_tmpUpdatedAt,_tmpContentUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsPinned,_tmpIsSystem,_tmpFields,_tmpQuestionCount,_tmpAnsweredCount,_tmpTotalAttempts,_tmpCompletionPercentage,_tmpAccuracyPercentage,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateQuestionCount(bookId: Long, count: Int) {
    val _sql: String = "UPDATE books SET questionCount = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, count.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateCompletionPercentage(bookId: Long, percentage: Float) {
    val _sql: String = "UPDATE books SET completionPercentage = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, percentage.toDouble())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateAnsweredCount(bookId: Long, count: Int) {
    val _sql: String = "UPDATE books SET answeredCount = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, count.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateTotalAttempts(bookId: Long, count: Int) {
    val _sql: String = "UPDATE books SET totalAttempts = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, count.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateAccuracyPercentage(bookId: Long, percentage: Float) {
    val _sql: String = "UPDATE books SET accuracyPercentage = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, percentage.toDouble())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteBookById(bookId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE books SET deletedAt = ?, updatedAt = ? WHERE id = ?"
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

  public override suspend fun restoreBookById(bookId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE books SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
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

  public override suspend fun softDeleteBooksByWorkspaceId(workspaceId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE books SET deletedAt = ?, updatedAt = ? WHERE workspaceId = ? AND deletedAt IS NULL"
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

  public override suspend fun restoreBooksByWorkspaceId(
    workspaceId: Long,
    updatedAt: Long,
    deletedAtFilter: Long,
  ) {
    val _sql: String =
        "UPDATE books SET deletedAt = NULL, updatedAt = ? WHERE workspaceId = ? AND deletedAt = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, workspaceId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAtFilter)
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
