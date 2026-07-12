package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.QuestionAssetEntity
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
public class QuestionAssetDao_Impl(
  __db: RoomDatabase,
) : QuestionAssetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfQuestionAssetEntity: EntityInsertAdapter<QuestionAssetEntity>

  private val __deleteAdapterOfQuestionAssetEntity: EntityDeleteOrUpdateAdapter<QuestionAssetEntity>

  private val __updateAdapterOfQuestionAssetEntity: EntityDeleteOrUpdateAdapter<QuestionAssetEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfQuestionAssetEntity = object : EntityInsertAdapter<QuestionAssetEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `question_assets` (`id`,`bookId`,`quizId`,`questionId`,`assetType`,`title`,`description`,`localPath`,`externalUrl`,`mimeType`,`fileName`,`fileSizeBytes`,`textContent`,`sourceDocumentId`,`sourcePage`,`sourceQuote`,`sortOrder`,`isPinned`,`isPrimary`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionAssetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.bookId)
        statement.bindLong(3, entity.quizId)
        statement.bindLong(4, entity.questionId)
        statement.bindText(5, entity.assetType)
        statement.bindText(6, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpDescription)
        }
        val _tmpLocalPath: String? = entity.localPath
        if (_tmpLocalPath == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpLocalPath)
        }
        val _tmpExternalUrl: String? = entity.externalUrl
        if (_tmpExternalUrl == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpExternalUrl)
        }
        val _tmpMimeType: String? = entity.mimeType
        if (_tmpMimeType == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpMimeType)
        }
        val _tmpFileName: String? = entity.fileName
        if (_tmpFileName == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpFileName)
        }
        val _tmpFileSizeBytes: Long? = entity.fileSizeBytes
        if (_tmpFileSizeBytes == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpFileSizeBytes)
        }
        val _tmpTextContent: String? = entity.textContent
        if (_tmpTextContent == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpTextContent)
        }
        val _tmpSourceDocumentId: Long? = entity.sourceDocumentId
        if (_tmpSourceDocumentId == null) {
          statement.bindNull(14)
        } else {
          statement.bindLong(14, _tmpSourceDocumentId)
        }
        val _tmpSourcePage: String? = entity.sourcePage
        if (_tmpSourcePage == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpSourcePage)
        }
        val _tmpSourceQuote: String? = entity.sourceQuote
        if (_tmpSourceQuote == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpSourceQuote)
        }
        statement.bindLong(17, entity.sortOrder.toLong())
        val _tmp: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(18, _tmp.toLong())
        val _tmp_1: Int = if (entity.isPrimary) 1 else 0
        statement.bindLong(19, _tmp_1.toLong())
        statement.bindLong(20, entity.createdAt)
        statement.bindLong(21, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(22)
        } else {
          statement.bindLong(22, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfQuestionAssetEntity = object :
        EntityDeleteOrUpdateAdapter<QuestionAssetEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `question_assets` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionAssetEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfQuestionAssetEntity = object :
        EntityDeleteOrUpdateAdapter<QuestionAssetEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `question_assets` SET `id` = ?,`bookId` = ?,`quizId` = ?,`questionId` = ?,`assetType` = ?,`title` = ?,`description` = ?,`localPath` = ?,`externalUrl` = ?,`mimeType` = ?,`fileName` = ?,`fileSizeBytes` = ?,`textContent` = ?,`sourceDocumentId` = ?,`sourcePage` = ?,`sourceQuote` = ?,`sortOrder` = ?,`isPinned` = ?,`isPrimary` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionAssetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.bookId)
        statement.bindLong(3, entity.quizId)
        statement.bindLong(4, entity.questionId)
        statement.bindText(5, entity.assetType)
        statement.bindText(6, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpDescription)
        }
        val _tmpLocalPath: String? = entity.localPath
        if (_tmpLocalPath == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpLocalPath)
        }
        val _tmpExternalUrl: String? = entity.externalUrl
        if (_tmpExternalUrl == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpExternalUrl)
        }
        val _tmpMimeType: String? = entity.mimeType
        if (_tmpMimeType == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpMimeType)
        }
        val _tmpFileName: String? = entity.fileName
        if (_tmpFileName == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpFileName)
        }
        val _tmpFileSizeBytes: Long? = entity.fileSizeBytes
        if (_tmpFileSizeBytes == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpFileSizeBytes)
        }
        val _tmpTextContent: String? = entity.textContent
        if (_tmpTextContent == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpTextContent)
        }
        val _tmpSourceDocumentId: Long? = entity.sourceDocumentId
        if (_tmpSourceDocumentId == null) {
          statement.bindNull(14)
        } else {
          statement.bindLong(14, _tmpSourceDocumentId)
        }
        val _tmpSourcePage: String? = entity.sourcePage
        if (_tmpSourcePage == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpSourcePage)
        }
        val _tmpSourceQuote: String? = entity.sourceQuote
        if (_tmpSourceQuote == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpSourceQuote)
        }
        statement.bindLong(17, entity.sortOrder.toLong())
        val _tmp: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(18, _tmp.toLong())
        val _tmp_1: Int = if (entity.isPrimary) 1 else 0
        statement.bindLong(19, _tmp_1.toLong())
        statement.bindLong(20, entity.createdAt)
        statement.bindLong(21, entity.updatedAt)
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

  public override suspend fun insertAsset(asset: QuestionAssetEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfQuestionAssetEntity.insertAndReturnId(_connection, asset)
    _result
  }

  public override suspend fun insertAssets(assets: List<QuestionAssetEntity>): List<Long> =
      performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> =
        __insertAdapterOfQuestionAssetEntity.insertAndReturnIdsList(_connection, assets)
    _result
  }

  public override suspend fun hardDeleteAsset(asset: QuestionAssetEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfQuestionAssetEntity.handle(_connection, asset)
  }

  public override suspend fun updateAsset(asset: QuestionAssetEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfQuestionAssetEntity.handle(_connection, asset)
  }

  public override fun getAssetsByQuestionId(questionId: Long): Flow<List<QuestionAssetEntity>> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE questionId = ? AND deletedAt IS NULL ORDER BY isPinned DESC, sortOrder ASC, createdAt ASC"
    return createFlow(__db, false, arrayOf("question_assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAssetsByQuestionIdNow(questionId: Long):
      List<QuestionAssetEntity> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE questionId = ? AND deletedAt IS NULL ORDER BY isPinned DESC, sortOrder ASC, createdAt ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAssetsByQuestionIdIncludingDeleted(questionId: Long):
      List<QuestionAssetEntity> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE questionId = ? ORDER BY isPinned DESC, sortOrder ASC, createdAt ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAssetsByQuizId(quizId: Long): Flow<List<QuestionAssetEntity>> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE quizId = ? AND deletedAt IS NULL ORDER BY questionId ASC, isPinned DESC, sortOrder ASC, createdAt ASC"
    return createFlow(__db, false, arrayOf("question_assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAssetsByQuizIdNow(quizId: Long): List<QuestionAssetEntity> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE quizId = ? AND deletedAt IS NULL ORDER BY questionId ASC, isPinned DESC, sortOrder ASC, createdAt ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAssetsByBookId(bookId: Long): Flow<List<QuestionAssetEntity>> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("question_assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAssetById(id: Long): QuestionAssetEntity? {
    val _sql: String = "SELECT * FROM question_assets WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: QuestionAssetEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllAssetsIncludingDeleted(): List<QuestionAssetEntity> {
    val _sql: String = "SELECT * FROM question_assets ORDER BY updatedAt DESC, id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAssetsByBookIdIncludingDeleted(bookId: Long):
      List<QuestionAssetEntity> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE bookId = ? ORDER BY updatedAt DESC, id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAssetsByQuizIdIncludingDeleted(quizId: Long):
      List<QuestionAssetEntity> {
    val _sql: String =
        "SELECT * FROM question_assets WHERE quizId = ? ORDER BY questionId ASC, isPinned DESC, sortOrder ASC, createdAt ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfQuestionId: Int = getColumnIndexOrThrow(_stmt, "questionId")
        val _columnIndexOfAssetType: Int = getColumnIndexOrThrow(_stmt, "assetType")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfFileName: Int = getColumnIndexOrThrow(_stmt, "fileName")
        val _columnIndexOfFileSizeBytes: Int = getColumnIndexOrThrow(_stmt, "fileSizeBytes")
        val _columnIndexOfTextContent: Int = getColumnIndexOrThrow(_stmt, "textContent")
        val _columnIndexOfSourceDocumentId: Int = getColumnIndexOrThrow(_stmt, "sourceDocumentId")
        val _columnIndexOfSourcePage: Int = getColumnIndexOrThrow(_stmt, "sourcePage")
        val _columnIndexOfSourceQuote: Int = getColumnIndexOrThrow(_stmt, "sourceQuote")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "isPrimary")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<QuestionAssetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionAssetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpQuestionId: Long
          _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          val _tmpAssetType: String
          _tmpAssetType = _stmt.getText(_columnIndexOfAssetType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpLocalPath: String?
          if (_stmt.isNull(_columnIndexOfLocalPath)) {
            _tmpLocalPath = null
          } else {
            _tmpLocalPath = _stmt.getText(_columnIndexOfLocalPath)
          }
          val _tmpExternalUrl: String?
          if (_stmt.isNull(_columnIndexOfExternalUrl)) {
            _tmpExternalUrl = null
          } else {
            _tmpExternalUrl = _stmt.getText(_columnIndexOfExternalUrl)
          }
          val _tmpMimeType: String?
          if (_stmt.isNull(_columnIndexOfMimeType)) {
            _tmpMimeType = null
          } else {
            _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          }
          val _tmpFileName: String?
          if (_stmt.isNull(_columnIndexOfFileName)) {
            _tmpFileName = null
          } else {
            _tmpFileName = _stmt.getText(_columnIndexOfFileName)
          }
          val _tmpFileSizeBytes: Long?
          if (_stmt.isNull(_columnIndexOfFileSizeBytes)) {
            _tmpFileSizeBytes = null
          } else {
            _tmpFileSizeBytes = _stmt.getLong(_columnIndexOfFileSizeBytes)
          }
          val _tmpTextContent: String?
          if (_stmt.isNull(_columnIndexOfTextContent)) {
            _tmpTextContent = null
          } else {
            _tmpTextContent = _stmt.getText(_columnIndexOfTextContent)
          }
          val _tmpSourceDocumentId: Long?
          if (_stmt.isNull(_columnIndexOfSourceDocumentId)) {
            _tmpSourceDocumentId = null
          } else {
            _tmpSourceDocumentId = _stmt.getLong(_columnIndexOfSourceDocumentId)
          }
          val _tmpSourcePage: String?
          if (_stmt.isNull(_columnIndexOfSourcePage)) {
            _tmpSourcePage = null
          } else {
            _tmpSourcePage = _stmt.getText(_columnIndexOfSourcePage)
          }
          val _tmpSourceQuote: String?
          if (_stmt.isNull(_columnIndexOfSourceQuote)) {
            _tmpSourceQuote = null
          } else {
            _tmpSourceQuote = _stmt.getText(_columnIndexOfSourceQuote)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpIsPrimary: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp_1 != 0
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
              QuestionAssetEntity(_tmpId,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpAssetType,_tmpTitle,_tmpDescription,_tmpLocalPath,_tmpExternalUrl,_tmpMimeType,_tmpFileName,_tmpFileSizeBytes,_tmpTextContent,_tmpSourceDocumentId,_tmpSourcePage,_tmpSourceQuote,_tmpSortOrder,_tmpIsPinned,_tmpIsPrimary,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getQuestionIdsWithAssetsFlow(): Flow<List<Long>> {
    val _sql: String = "SELECT DISTINCT questionId FROM question_assets WHERE deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("question_assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<Long> = mutableListOf()
        while (_stmt.step()) {
          val _item: Long
          _item = _stmt.getLong(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getQuestionIdsWithAssetsForQuizFlow(quizId: Long): Flow<List<Long>> {
    val _sql: String =
        "SELECT DISTINCT questionId FROM question_assets WHERE quizId = ? AND deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("question_assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _result: MutableList<Long> = mutableListOf()
        while (_stmt.step()) {
          val _item: Long
          _item = _stmt.getLong(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countDistinctQuestionsWithAssets(): Int {
    val _sql: String =
        "SELECT COUNT(DISTINCT questionId) FROM question_assets WHERE deletedAt IS NULL"
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

  public override suspend fun countDistinctQuestionsWithSources(): Int {
    val _sql: String =
        "SELECT COUNT(DISTINCT questionId) FROM question_assets WHERE deletedAt IS NULL AND sourceDocumentId IS NOT NULL"
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

  public override fun getAssetCountForQuestion(questionId: Long): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM question_assets WHERE questionId = ? AND deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("question_assets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
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

  public override suspend fun softDeleteAssetById(assetId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE question_assets SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, assetId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteAssetsForQuestion(questionId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE question_assets SET deletedAt = ?, updatedAt = ? WHERE questionId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, questionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreAssetById(assetId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE question_assets SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, assetId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun hardDeleteAssetsForQuestion(questionId: Long) {
    val _sql: String = "DELETE FROM question_assets WHERE questionId = ?"
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

  public override suspend fun updateAssetOrder(
    assetId: Long,
    sortOrder: Int,
    updatedAt: Long,
  ) {
    val _sql: String = "UPDATE question_assets SET sortOrder = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, sortOrder.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, assetId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearOtherPrimaryAssets(
    questionId: Long,
    assetId: Long,
    updatedAt: Long,
  ) {
    val _sql: String =
        "UPDATE question_assets SET isPrimary = 0, updatedAt = ? WHERE questionId = ? AND id != ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, questionId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, assetId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSourceReference(sourceId: Long, updatedAt: Long) {
    val _sql: String =
        "UPDATE question_assets SET sourceDocumentId = NULL, updatedAt = ? WHERE sourceDocumentId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, sourceId)
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
