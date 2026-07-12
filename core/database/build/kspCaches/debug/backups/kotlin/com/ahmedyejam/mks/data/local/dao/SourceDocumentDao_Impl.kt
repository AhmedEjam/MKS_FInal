package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.SourceDocumentEntity
import javax.`annotation`.processing.Generated
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
public class SourceDocumentDao_Impl(
  __db: RoomDatabase,
) : SourceDocumentDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSourceDocumentEntity: EntityInsertAdapter<SourceDocumentEntity>

  private val __deleteAdapterOfSourceDocumentEntity:
      EntityDeleteOrUpdateAdapter<SourceDocumentEntity>

  private val __updateAdapterOfSourceDocumentEntity:
      EntityDeleteOrUpdateAdapter<SourceDocumentEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSourceDocumentEntity = object :
        EntityInsertAdapter<SourceDocumentEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `source_documents` (`id`,`bookId`,`title`,`sourceType`,`author`,`edition`,`year`,`publisher`,`localPath`,`externalUrl`,`description`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SourceDocumentEntity) {
        statement.bindLong(1, entity.id)
        val _tmpBookId: Long? = entity.bookId
        if (_tmpBookId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpBookId)
        }
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.sourceType)
        val _tmpAuthor: String? = entity.author
        if (_tmpAuthor == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpAuthor)
        }
        val _tmpEdition: String? = entity.edition
        if (_tmpEdition == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpEdition)
        }
        val _tmpYear: String? = entity.year
        if (_tmpYear == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpYear)
        }
        val _tmpPublisher: String? = entity.publisher
        if (_tmpPublisher == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpPublisher)
        }
        val _tmpLocalPath: String? = entity.localPath
        if (_tmpLocalPath == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpLocalPath)
        }
        val _tmpExternalUrl: String? = entity.externalUrl
        if (_tmpExternalUrl == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpExternalUrl)
        }
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDescription)
        }
        statement.bindLong(12, entity.createdAt)
        statement.bindLong(13, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(14)
        } else {
          statement.bindLong(14, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfSourceDocumentEntity = object :
        EntityDeleteOrUpdateAdapter<SourceDocumentEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `source_documents` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SourceDocumentEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfSourceDocumentEntity = object :
        EntityDeleteOrUpdateAdapter<SourceDocumentEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `source_documents` SET `id` = ?,`bookId` = ?,`title` = ?,`sourceType` = ?,`author` = ?,`edition` = ?,`year` = ?,`publisher` = ?,`localPath` = ?,`externalUrl` = ?,`description` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SourceDocumentEntity) {
        statement.bindLong(1, entity.id)
        val _tmpBookId: Long? = entity.bookId
        if (_tmpBookId == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpBookId)
        }
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.sourceType)
        val _tmpAuthor: String? = entity.author
        if (_tmpAuthor == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpAuthor)
        }
        val _tmpEdition: String? = entity.edition
        if (_tmpEdition == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpEdition)
        }
        val _tmpYear: String? = entity.year
        if (_tmpYear == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpYear)
        }
        val _tmpPublisher: String? = entity.publisher
        if (_tmpPublisher == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpPublisher)
        }
        val _tmpLocalPath: String? = entity.localPath
        if (_tmpLocalPath == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpLocalPath)
        }
        val _tmpExternalUrl: String? = entity.externalUrl
        if (_tmpExternalUrl == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpExternalUrl)
        }
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDescription)
        }
        statement.bindLong(12, entity.createdAt)
        statement.bindLong(13, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(14)
        } else {
          statement.bindLong(14, _tmpDeletedAt)
        }
        statement.bindLong(15, entity.id)
      }
    }
  }

  public override suspend fun insertSource(source: SourceDocumentEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfSourceDocumentEntity.insertAndReturnId(_connection, source)
    _result
  }

  public override suspend fun hardDeleteSource(source: SourceDocumentEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfSourceDocumentEntity.handle(_connection, source)
  }

  public override suspend fun updateSource(source: SourceDocumentEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfSourceDocumentEntity.handle(_connection, source)
  }

  public override fun getSourcesByBookId(bookId: Long): Flow<List<SourceDocumentEntity>> {
    val _sql: String =
        "SELECT * FROM source_documents WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC, title COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("source_documents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSourceType: Int = getColumnIndexOrThrow(_stmt, "sourceType")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfEdition: Int = getColumnIndexOrThrow(_stmt, "edition")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPublisher: Int = getColumnIndexOrThrow(_stmt, "publisher")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SourceDocumentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SourceDocumentEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSourceType: String
          _tmpSourceType = _stmt.getText(_columnIndexOfSourceType)
          val _tmpAuthor: String?
          if (_stmt.isNull(_columnIndexOfAuthor)) {
            _tmpAuthor = null
          } else {
            _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          }
          val _tmpEdition: String?
          if (_stmt.isNull(_columnIndexOfEdition)) {
            _tmpEdition = null
          } else {
            _tmpEdition = _stmt.getText(_columnIndexOfEdition)
          }
          val _tmpYear: String?
          if (_stmt.isNull(_columnIndexOfYear)) {
            _tmpYear = null
          } else {
            _tmpYear = _stmt.getText(_columnIndexOfYear)
          }
          val _tmpPublisher: String?
          if (_stmt.isNull(_columnIndexOfPublisher)) {
            _tmpPublisher = null
          } else {
            _tmpPublisher = _stmt.getText(_columnIndexOfPublisher)
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
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
              SourceDocumentEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpSourceType,_tmpAuthor,_tmpEdition,_tmpYear,_tmpPublisher,_tmpLocalPath,_tmpExternalUrl,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSourcesByBookIdNow(bookId: Long): List<SourceDocumentEntity> {
    val _sql: String =
        "SELECT * FROM source_documents WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC, title COLLATE NOCASE ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSourceType: Int = getColumnIndexOrThrow(_stmt, "sourceType")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfEdition: Int = getColumnIndexOrThrow(_stmt, "edition")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPublisher: Int = getColumnIndexOrThrow(_stmt, "publisher")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SourceDocumentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SourceDocumentEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSourceType: String
          _tmpSourceType = _stmt.getText(_columnIndexOfSourceType)
          val _tmpAuthor: String?
          if (_stmt.isNull(_columnIndexOfAuthor)) {
            _tmpAuthor = null
          } else {
            _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          }
          val _tmpEdition: String?
          if (_stmt.isNull(_columnIndexOfEdition)) {
            _tmpEdition = null
          } else {
            _tmpEdition = _stmt.getText(_columnIndexOfEdition)
          }
          val _tmpYear: String?
          if (_stmt.isNull(_columnIndexOfYear)) {
            _tmpYear = null
          } else {
            _tmpYear = _stmt.getText(_columnIndexOfYear)
          }
          val _tmpPublisher: String?
          if (_stmt.isNull(_columnIndexOfPublisher)) {
            _tmpPublisher = null
          } else {
            _tmpPublisher = _stmt.getText(_columnIndexOfPublisher)
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
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
              SourceDocumentEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpSourceType,_tmpAuthor,_tmpEdition,_tmpYear,_tmpPublisher,_tmpLocalPath,_tmpExternalUrl,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSourceById(id: Long): SourceDocumentEntity? {
    val _sql: String = "SELECT * FROM source_documents WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSourceType: Int = getColumnIndexOrThrow(_stmt, "sourceType")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfEdition: Int = getColumnIndexOrThrow(_stmt, "edition")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPublisher: Int = getColumnIndexOrThrow(_stmt, "publisher")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: SourceDocumentEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSourceType: String
          _tmpSourceType = _stmt.getText(_columnIndexOfSourceType)
          val _tmpAuthor: String?
          if (_stmt.isNull(_columnIndexOfAuthor)) {
            _tmpAuthor = null
          } else {
            _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          }
          val _tmpEdition: String?
          if (_stmt.isNull(_columnIndexOfEdition)) {
            _tmpEdition = null
          } else {
            _tmpEdition = _stmt.getText(_columnIndexOfEdition)
          }
          val _tmpYear: String?
          if (_stmt.isNull(_columnIndexOfYear)) {
            _tmpYear = null
          } else {
            _tmpYear = _stmt.getText(_columnIndexOfYear)
          }
          val _tmpPublisher: String?
          if (_stmt.isNull(_columnIndexOfPublisher)) {
            _tmpPublisher = null
          } else {
            _tmpPublisher = _stmt.getText(_columnIndexOfPublisher)
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
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
              SourceDocumentEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpSourceType,_tmpAuthor,_tmpEdition,_tmpYear,_tmpPublisher,_tmpLocalPath,_tmpExternalUrl,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllSourcesIncludingDeleted(): List<SourceDocumentEntity> {
    val _sql: String =
        "SELECT * FROM source_documents ORDER BY updatedAt DESC, title COLLATE NOCASE ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSourceType: Int = getColumnIndexOrThrow(_stmt, "sourceType")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfEdition: Int = getColumnIndexOrThrow(_stmt, "edition")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPublisher: Int = getColumnIndexOrThrow(_stmt, "publisher")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SourceDocumentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SourceDocumentEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSourceType: String
          _tmpSourceType = _stmt.getText(_columnIndexOfSourceType)
          val _tmpAuthor: String?
          if (_stmt.isNull(_columnIndexOfAuthor)) {
            _tmpAuthor = null
          } else {
            _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          }
          val _tmpEdition: String?
          if (_stmt.isNull(_columnIndexOfEdition)) {
            _tmpEdition = null
          } else {
            _tmpEdition = _stmt.getText(_columnIndexOfEdition)
          }
          val _tmpYear: String?
          if (_stmt.isNull(_columnIndexOfYear)) {
            _tmpYear = null
          } else {
            _tmpYear = _stmt.getText(_columnIndexOfYear)
          }
          val _tmpPublisher: String?
          if (_stmt.isNull(_columnIndexOfPublisher)) {
            _tmpPublisher = null
          } else {
            _tmpPublisher = _stmt.getText(_columnIndexOfPublisher)
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
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
              SourceDocumentEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpSourceType,_tmpAuthor,_tmpEdition,_tmpYear,_tmpPublisher,_tmpLocalPath,_tmpExternalUrl,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSourcesByBookIdIncludingDeleted(bookId: Long):
      List<SourceDocumentEntity> {
    val _sql: String =
        "SELECT * FROM source_documents WHERE bookId = ? ORDER BY updatedAt DESC, title COLLATE NOCASE ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSourceType: Int = getColumnIndexOrThrow(_stmt, "sourceType")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfEdition: Int = getColumnIndexOrThrow(_stmt, "edition")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPublisher: Int = getColumnIndexOrThrow(_stmt, "publisher")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SourceDocumentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SourceDocumentEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSourceType: String
          _tmpSourceType = _stmt.getText(_columnIndexOfSourceType)
          val _tmpAuthor: String?
          if (_stmt.isNull(_columnIndexOfAuthor)) {
            _tmpAuthor = null
          } else {
            _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          }
          val _tmpEdition: String?
          if (_stmt.isNull(_columnIndexOfEdition)) {
            _tmpEdition = null
          } else {
            _tmpEdition = _stmt.getText(_columnIndexOfEdition)
          }
          val _tmpYear: String?
          if (_stmt.isNull(_columnIndexOfYear)) {
            _tmpYear = null
          } else {
            _tmpYear = _stmt.getText(_columnIndexOfYear)
          }
          val _tmpPublisher: String?
          if (_stmt.isNull(_columnIndexOfPublisher)) {
            _tmpPublisher = null
          } else {
            _tmpPublisher = _stmt.getText(_columnIndexOfPublisher)
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
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
              SourceDocumentEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpSourceType,_tmpAuthor,_tmpEdition,_tmpYear,_tmpPublisher,_tmpLocalPath,_tmpExternalUrl,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun searchSources(query: String): Flow<List<SourceDocumentEntity>> {
    val _sql: String =
        "SELECT * FROM source_documents WHERE deletedAt IS NULL AND (title LIKE '%' || ? || '%' OR author LIKE '%' || ? || '%' OR description LIKE '%' || ? || '%') ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("source_documents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        _argIndex = 3
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfSourceType: Int = getColumnIndexOrThrow(_stmt, "sourceType")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfEdition: Int = getColumnIndexOrThrow(_stmt, "edition")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfPublisher: Int = getColumnIndexOrThrow(_stmt, "publisher")
        val _columnIndexOfLocalPath: Int = getColumnIndexOrThrow(_stmt, "localPath")
        val _columnIndexOfExternalUrl: Int = getColumnIndexOrThrow(_stmt, "externalUrl")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SourceDocumentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SourceDocumentEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSourceType: String
          _tmpSourceType = _stmt.getText(_columnIndexOfSourceType)
          val _tmpAuthor: String?
          if (_stmt.isNull(_columnIndexOfAuthor)) {
            _tmpAuthor = null
          } else {
            _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          }
          val _tmpEdition: String?
          if (_stmt.isNull(_columnIndexOfEdition)) {
            _tmpEdition = null
          } else {
            _tmpEdition = _stmt.getText(_columnIndexOfEdition)
          }
          val _tmpYear: String?
          if (_stmt.isNull(_columnIndexOfYear)) {
            _tmpYear = null
          } else {
            _tmpYear = _stmt.getText(_columnIndexOfYear)
          }
          val _tmpPublisher: String?
          if (_stmt.isNull(_columnIndexOfPublisher)) {
            _tmpPublisher = null
          } else {
            _tmpPublisher = _stmt.getText(_columnIndexOfPublisher)
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
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
              SourceDocumentEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpSourceType,_tmpAuthor,_tmpEdition,_tmpYear,_tmpPublisher,_tmpLocalPath,_tmpExternalUrl,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSourceById(sourceId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE source_documents SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, sourceId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreSourceById(sourceId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE source_documents SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
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
