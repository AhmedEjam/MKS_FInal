package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.PromptDeckEntity
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
public class PromptDeckDao_Impl(
  __db: RoomDatabase,
) : PromptDeckDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPromptDeckEntity: EntityInsertAdapter<PromptDeckEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfPromptDeckEntity: EntityDeleteOrUpdateAdapter<PromptDeckEntity>

  private val __updateAdapterOfPromptDeckEntity: EntityDeleteOrUpdateAdapter<PromptDeckEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPromptDeckEntity = object : EntityInsertAdapter<PromptDeckEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `prompt_decks` (`id`,`bookId`,`title`,`description`,`iconName`,`coverImage`,`tags`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PromptDeckEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.bookId)
        statement.bindText(3, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDescription)
        }
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpCoverImage)
        }
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(7, _tmp)
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfPromptDeckEntity = object :
        EntityDeleteOrUpdateAdapter<PromptDeckEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `prompt_decks` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptDeckEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfPromptDeckEntity = object :
        EntityDeleteOrUpdateAdapter<PromptDeckEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `prompt_decks` SET `id` = ?,`bookId` = ?,`title` = ?,`description` = ?,`iconName` = ?,`coverImage` = ?,`tags` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptDeckEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.bookId)
        statement.bindText(3, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDescription)
        }
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpCoverImage)
        }
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(7, _tmp)
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpDeletedAt)
        }
        statement.bindLong(11, entity.id)
      }
    }
  }

  public override suspend fun insertDeck(deck: PromptDeckEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfPromptDeckEntity.insertAndReturnId(_connection, deck)
    _result
  }

  public override suspend fun hardDeleteDeck(deck: PromptDeckEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfPromptDeckEntity.handle(_connection, deck)
  }

  public override suspend fun updateDeck(deck: PromptDeckEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfPromptDeckEntity.handle(_connection, deck)
  }

  public override fun getDecksByBookId(bookId: Long): Flow<List<PromptDeckEntity>> {
    val _sql: String =
        "SELECT * FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC, title COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("prompt_decks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptDeckEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptDeckEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
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
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
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
              PromptDeckEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDecksByBookIdNow(bookId: Long): List<PromptDeckEntity> {
    val _sql: String =
        "SELECT * FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC, title COLLATE NOCASE ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptDeckEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptDeckEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
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
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
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
              PromptDeckEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDeckById(id: Long): PromptDeckEntity? {
    val _sql: String = "SELECT * FROM prompt_decks WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: PromptDeckEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
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
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
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
              PromptDeckEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDeckByIdIncludingDeleted(id: Long): PromptDeckEntity? {
    val _sql: String = "SELECT * FROM prompt_decks WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: PromptDeckEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
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
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
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
              PromptDeckEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = "SELECT COUNT(*) FROM prompt_decks WHERE deletedAt IS NULL"
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

  public override suspend fun countByBookId(bookId: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL"
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

  public override fun getDeletedDecksByWorkspaceFlow(workspaceId: Long):
      Flow<List<PromptDeckEntity>> {
    val _sql: String =
        "SELECT * FROM prompt_decks WHERE deletedAt IS NOT NULL AND bookId IN (SELECT id FROM books WHERE workspaceId = ?)"
    return createFlow(__db, false, arrayOf("prompt_decks", "books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptDeckEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptDeckEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
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
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
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
              PromptDeckEntity(_tmpId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteDeckById(deckId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE prompt_decks SET deletedAt = ?, updatedAt = ? WHERE id = ?"
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

  public override suspend fun restoreDeckById(deckId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE prompt_decks SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deckId)
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
