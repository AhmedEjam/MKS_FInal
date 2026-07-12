package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.FlashcardDeckEntity
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
public class FlashcardDeckDao_Impl(
  __db: RoomDatabase,
) : FlashcardDeckDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFlashcardDeckEntity: EntityInsertAdapter<FlashcardDeckEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfFlashcardDeckEntity: EntityDeleteOrUpdateAdapter<FlashcardDeckEntity>

  private val __updateAdapterOfFlashcardDeckEntity: EntityDeleteOrUpdateAdapter<FlashcardDeckEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFlashcardDeckEntity = object : EntityInsertAdapter<FlashcardDeckEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `flashcard_decks` (`id`,`externalId`,`bookId`,`title`,`description`,`iconName`,`coverImage`,`tags`,`cardCount`,`studiedCount`,`masteryPercentage`,`isSystem`,`isPinned`,`createdAt`,`updatedAt`,`lastStudiedAt`,`lastEditedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FlashcardDeckEntity) {
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
        statement.bindLong(9, entity.cardCount.toLong())
        statement.bindLong(10, entity.studiedCount.toLong())
        statement.bindDouble(11, entity.masteryPercentage.toDouble())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(13, _tmp_2.toLong())
        statement.bindLong(14, entity.createdAt)
        statement.bindLong(15, entity.updatedAt)
        statement.bindLong(16, entity.lastStudiedAt)
        statement.bindLong(17, entity.lastEditedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(18)
        } else {
          statement.bindLong(18, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfFlashcardDeckEntity = object :
        EntityDeleteOrUpdateAdapter<FlashcardDeckEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `flashcard_decks` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FlashcardDeckEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfFlashcardDeckEntity = object :
        EntityDeleteOrUpdateAdapter<FlashcardDeckEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `flashcard_decks` SET `id` = ?,`externalId` = ?,`bookId` = ?,`title` = ?,`description` = ?,`iconName` = ?,`coverImage` = ?,`tags` = ?,`cardCount` = ?,`studiedCount` = ?,`masteryPercentage` = ?,`isSystem` = ?,`isPinned` = ?,`createdAt` = ?,`updatedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FlashcardDeckEntity) {
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
        statement.bindLong(9, entity.cardCount.toLong())
        statement.bindLong(10, entity.studiedCount.toLong())
        statement.bindDouble(11, entity.masteryPercentage.toDouble())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(13, _tmp_2.toLong())
        statement.bindLong(14, entity.createdAt)
        statement.bindLong(15, entity.updatedAt)
        statement.bindLong(16, entity.lastStudiedAt)
        statement.bindLong(17, entity.lastEditedAt)
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

  public override suspend fun insertFlashcardDeck(deck: FlashcardDeckEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfFlashcardDeckEntity.insertAndReturnId(_connection, deck)
    _result
  }

  public override suspend fun hardDeleteFlashcardDeck(deck: FlashcardDeckEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfFlashcardDeckEntity.handle(_connection, deck)
  }

  public override suspend fun updateFlashcardDeck(deck: FlashcardDeckEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfFlashcardDeckEntity.handle(_connection, deck)
  }

  public override fun getFlashcardDecksByBookId(bookId: Long): Flow<List<FlashcardDeckEntity>> {
    val _sql: String =
        "SELECT * FROM flashcard_decks WHERE bookId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("flashcard_decks")) { _connection ->
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
        val _columnIndexOfCardCount: Int = getColumnIndexOrThrow(_stmt, "cardCount")
        val _columnIndexOfStudiedCount: Int = getColumnIndexOrThrow(_stmt, "studiedCount")
        val _columnIndexOfMasteryPercentage: Int = getColumnIndexOrThrow(_stmt, "masteryPercentage")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardDeckEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardDeckEntity
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
          val _tmpCardCount: Int
          _tmpCardCount = _stmt.getLong(_columnIndexOfCardCount).toInt()
          val _tmpStudiedCount: Int
          _tmpStudiedCount = _stmt.getLong(_columnIndexOfStudiedCount).toInt()
          val _tmpMasteryPercentage: Float
          _tmpMasteryPercentage = _stmt.getDouble(_columnIndexOfMasteryPercentage).toFloat()
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
              FlashcardDeckEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCardCount,_tmpStudiedCount,_tmpMasteryPercentage,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardDecksByBookIdNow(bookId: Long):
      List<FlashcardDeckEntity> {
    val _sql: String =
        "SELECT * FROM flashcard_decks WHERE bookId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
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
        val _columnIndexOfCardCount: Int = getColumnIndexOrThrow(_stmt, "cardCount")
        val _columnIndexOfStudiedCount: Int = getColumnIndexOrThrow(_stmt, "studiedCount")
        val _columnIndexOfMasteryPercentage: Int = getColumnIndexOrThrow(_stmt, "masteryPercentage")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardDeckEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardDeckEntity
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
          val _tmpCardCount: Int
          _tmpCardCount = _stmt.getLong(_columnIndexOfCardCount).toInt()
          val _tmpStudiedCount: Int
          _tmpStudiedCount = _stmt.getLong(_columnIndexOfStudiedCount).toInt()
          val _tmpMasteryPercentage: Float
          _tmpMasteryPercentage = _stmt.getDouble(_columnIndexOfMasteryPercentage).toFloat()
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
              FlashcardDeckEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCardCount,_tmpStudiedCount,_tmpMasteryPercentage,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardDeckById(id: Long): FlashcardDeckEntity? {
    val _sql: String = "SELECT * FROM flashcard_decks WHERE id = ? AND deletedAt IS NULL"
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
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCardCount: Int = getColumnIndexOrThrow(_stmt, "cardCount")
        val _columnIndexOfStudiedCount: Int = getColumnIndexOrThrow(_stmt, "studiedCount")
        val _columnIndexOfMasteryPercentage: Int = getColumnIndexOrThrow(_stmt, "masteryPercentage")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: FlashcardDeckEntity?
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
          val _tmpCardCount: Int
          _tmpCardCount = _stmt.getLong(_columnIndexOfCardCount).toInt()
          val _tmpStudiedCount: Int
          _tmpStudiedCount = _stmt.getLong(_columnIndexOfStudiedCount).toInt()
          val _tmpMasteryPercentage: Float
          _tmpMasteryPercentage = _stmt.getDouble(_columnIndexOfMasteryPercentage).toFloat()
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
              FlashcardDeckEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCardCount,_tmpStudiedCount,_tmpMasteryPercentage,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getFlashcardDeckByIdIncludingDeleted(id: Long): FlashcardDeckEntity? {
    val _sql: String = "SELECT * FROM flashcard_decks WHERE id = ? LIMIT 1"
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
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCardCount: Int = getColumnIndexOrThrow(_stmt, "cardCount")
        val _columnIndexOfStudiedCount: Int = getColumnIndexOrThrow(_stmt, "studiedCount")
        val _columnIndexOfMasteryPercentage: Int = getColumnIndexOrThrow(_stmt, "masteryPercentage")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: FlashcardDeckEntity?
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
          val _tmpCardCount: Int
          _tmpCardCount = _stmt.getLong(_columnIndexOfCardCount).toInt()
          val _tmpStudiedCount: Int
          _tmpStudiedCount = _stmt.getLong(_columnIndexOfStudiedCount).toInt()
          val _tmpMasteryPercentage: Float
          _tmpMasteryPercentage = _stmt.getDouble(_columnIndexOfMasteryPercentage).toFloat()
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
              FlashcardDeckEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCardCount,_tmpStudiedCount,_tmpMasteryPercentage,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeFlashcardDeckById(id: Long): Flow<FlashcardDeckEntity?> {
    val _sql: String = "SELECT * FROM flashcard_decks WHERE id = ? AND deletedAt IS NULL"
    return createFlow(__db, false, arrayOf("flashcard_decks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCardCount: Int = getColumnIndexOrThrow(_stmt, "cardCount")
        val _columnIndexOfStudiedCount: Int = getColumnIndexOrThrow(_stmt, "studiedCount")
        val _columnIndexOfMasteryPercentage: Int = getColumnIndexOrThrow(_stmt, "masteryPercentage")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: FlashcardDeckEntity?
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
          val _tmpCardCount: Int
          _tmpCardCount = _stmt.getLong(_columnIndexOfCardCount).toInt()
          val _tmpStudiedCount: Int
          _tmpStudiedCount = _stmt.getLong(_columnIndexOfStudiedCount).toInt()
          val _tmpMasteryPercentage: Float
          _tmpMasteryPercentage = _stmt.getDouble(_columnIndexOfMasteryPercentage).toFloat()
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
              FlashcardDeckEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCardCount,_tmpStudiedCount,_tmpMasteryPercentage,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
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
    val _sql: String = "SELECT COUNT(*) FROM flashcard_decks WHERE deletedAt IS NULL"
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

  public override fun getDeletedDecksByWorkspaceFlow(workspaceId: Long):
      Flow<List<FlashcardDeckEntity>> {
    val _sql: String =
        "SELECT * FROM flashcard_decks WHERE deletedAt IS NOT NULL AND bookId IN (SELECT id FROM books WHERE workspaceId = ?)"
    return createFlow(__db, false, arrayOf("flashcard_decks", "books")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfCardCount: Int = getColumnIndexOrThrow(_stmt, "cardCount")
        val _columnIndexOfStudiedCount: Int = getColumnIndexOrThrow(_stmt, "studiedCount")
        val _columnIndexOfMasteryPercentage: Int = getColumnIndexOrThrow(_stmt, "masteryPercentage")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<FlashcardDeckEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FlashcardDeckEntity
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
          val _tmpCardCount: Int
          _tmpCardCount = _stmt.getLong(_columnIndexOfCardCount).toInt()
          val _tmpStudiedCount: Int
          _tmpStudiedCount = _stmt.getLong(_columnIndexOfStudiedCount).toInt()
          val _tmpMasteryPercentage: Float
          _tmpMasteryPercentage = _stmt.getDouble(_columnIndexOfMasteryPercentage).toFloat()
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
              FlashcardDeckEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpCardCount,_tmpStudiedCount,_tmpMasteryPercentage,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteFlashcardDeckById(deckId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE flashcard_decks SET deletedAt = ?, updatedAt = ? WHERE id = ?"
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

  public override suspend fun restoreFlashcardDeckById(deckId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE flashcard_decks SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
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
