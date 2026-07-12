package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.NoteCollectionEntity
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
public class NoteCollectionDao_Impl(
  __db: RoomDatabase,
) : NoteCollectionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNoteCollectionEntity: EntityInsertAdapter<NoteCollectionEntity>

  private val __converters: Converters = Converters()

  private val __updateAdapterOfNoteCollectionEntity:
      EntityDeleteOrUpdateAdapter<NoteCollectionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfNoteCollectionEntity = object :
        EntityInsertAdapter<NoteCollectionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `note_collections` (`id`,`externalId`,`bookId`,`title`,`description`,`iconName`,`coverImage`,`tags`,`noteCount`,`isSystem`,`isPinned`,`createdAt`,`updatedAt`,`lastStudiedAt`,`lastEditedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NoteCollectionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDescription)
        }
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
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(8, _tmp)
        statement.bindLong(9, entity.noteCount.toLong())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(10, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(11, _tmp_2.toLong())
        statement.bindLong(12, entity.createdAt)
        statement.bindLong(13, entity.updatedAt)
        statement.bindLong(14, entity.lastStudiedAt)
        statement.bindLong(15, entity.lastEditedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(16)
        } else {
          statement.bindLong(16, _tmpDeletedAt)
        }
      }
    }
    this.__updateAdapterOfNoteCollectionEntity = object :
        EntityDeleteOrUpdateAdapter<NoteCollectionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `note_collections` SET `id` = ?,`externalId` = ?,`bookId` = ?,`title` = ?,`description` = ?,`iconName` = ?,`coverImage` = ?,`tags` = ?,`noteCount` = ?,`isSystem` = ?,`isPinned` = ?,`createdAt` = ?,`updatedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteCollectionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDescription)
        }
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
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(8, _tmp)
        statement.bindLong(9, entity.noteCount.toLong())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(10, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(11, _tmp_2.toLong())
        statement.bindLong(12, entity.createdAt)
        statement.bindLong(13, entity.updatedAt)
        statement.bindLong(14, entity.lastStudiedAt)
        statement.bindLong(15, entity.lastEditedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(16)
        } else {
          statement.bindLong(16, _tmpDeletedAt)
        }
        statement.bindLong(17, entity.id)
      }
    }
  }

  public override suspend fun insertCollection(collection: NoteCollectionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfNoteCollectionEntity.insertAndReturnId(_connection,
        collection)
    _result
  }

  public override suspend fun updateCollection(collection: NoteCollectionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfNoteCollectionEntity.handle(_connection, collection)
  }

  public override fun getCollectionsByBookId(bookId: Long): Flow<List<NoteCollectionEntity>> {
    val _sql: String =
        "SELECT * FROM note_collections WHERE bookId = ? AND deletedAt IS NULL ORDER BY isPinned DESC, updatedAt DESC"
    return createFlow(__db, false, arrayOf("note_collections")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfNoteCount: Int = getColumnIndexOrThrow(_stmt, "noteCount")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteCollectionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteCollectionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
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
          val _tmpNoteCount: Int
          _tmpNoteCount = _stmt.getLong(_columnIndexOfNoteCount).toInt()
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpIsPinned: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteCollectionEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpNoteCount,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCollectionsByBookIdNow(bookId: Long): List<NoteCollectionEntity> {
    val _sql: String =
        "SELECT * FROM note_collections WHERE bookId = ? AND deletedAt IS NULL ORDER BY isPinned DESC, updatedAt DESC"
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
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfNoteCount: Int = getColumnIndexOrThrow(_stmt, "noteCount")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteCollectionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteCollectionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
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
          val _tmpNoteCount: Int
          _tmpNoteCount = _stmt.getLong(_columnIndexOfNoteCount).toInt()
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpIsPinned: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteCollectionEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpNoteCount,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCollectionById(collectionId: Long): NoteCollectionEntity? {
    val _sql: String = "SELECT * FROM note_collections WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, collectionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfNoteCount: Int = getColumnIndexOrThrow(_stmt, "noteCount")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: NoteCollectionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
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
          val _tmpNoteCount: Int
          _tmpNoteCount = _stmt.getLong(_columnIndexOfNoteCount).toInt()
          val _tmpIsSystem: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp_1 != 0
          val _tmpIsPinned: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              NoteCollectionEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpNoteCount,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteCollection(collectionId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE note_collections SET deletedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, collectionId)
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
