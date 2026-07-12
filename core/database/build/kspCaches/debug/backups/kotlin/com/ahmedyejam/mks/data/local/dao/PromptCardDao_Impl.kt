package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.PromptCardEntity
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
public class PromptCardDao_Impl(
  __db: RoomDatabase,
) : PromptCardDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPromptCardEntity: EntityInsertAdapter<PromptCardEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfPromptCardEntity: EntityDeleteOrUpdateAdapter<PromptCardEntity>

  private val __updateAdapterOfPromptCardEntity: EntityDeleteOrUpdateAdapter<PromptCardEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPromptCardEntity = object : EntityInsertAdapter<PromptCardEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `prompt_cards` (`id`,`deckId`,`title`,`promptText`,`variablesJson`,`outputType`,`tags`,`usageCount`,`lastUsedAt`,`sortOrder`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PromptCardEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.deckId)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.promptText)
        val _tmpVariablesJson: String? = entity.variablesJson
        if (_tmpVariablesJson == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpVariablesJson)
        }
        statement.bindText(6, entity.outputType)
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(7, _tmp)
        statement.bindLong(8, entity.usageCount.toLong())
        val _tmpLastUsedAt: Long? = entity.lastUsedAt
        if (_tmpLastUsedAt == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpLastUsedAt)
        }
        statement.bindLong(10, entity.sortOrder.toLong())
        statement.bindLong(11, entity.createdAt)
        statement.bindLong(12, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfPromptCardEntity = object :
        EntityDeleteOrUpdateAdapter<PromptCardEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `prompt_cards` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptCardEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfPromptCardEntity = object :
        EntityDeleteOrUpdateAdapter<PromptCardEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `prompt_cards` SET `id` = ?,`deckId` = ?,`title` = ?,`promptText` = ?,`variablesJson` = ?,`outputType` = ?,`tags` = ?,`usageCount` = ?,`lastUsedAt` = ?,`sortOrder` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptCardEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.deckId)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.promptText)
        val _tmpVariablesJson: String? = entity.variablesJson
        if (_tmpVariablesJson == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpVariablesJson)
        }
        statement.bindText(6, entity.outputType)
        val _tmp: String = __converters.fromStringList(entity.tags)
        statement.bindText(7, _tmp)
        statement.bindLong(8, entity.usageCount.toLong())
        val _tmpLastUsedAt: Long? = entity.lastUsedAt
        if (_tmpLastUsedAt == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpLastUsedAt)
        }
        statement.bindLong(10, entity.sortOrder.toLong())
        statement.bindLong(11, entity.createdAt)
        statement.bindLong(12, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(13)
        } else {
          statement.bindLong(13, _tmpDeletedAt)
        }
        statement.bindLong(14, entity.id)
      }
    }
  }

  public override suspend fun insertCard(card: PromptCardEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfPromptCardEntity.insertAndReturnId(_connection, card)
    _result
  }

  public override suspend fun insertCards(cards: List<PromptCardEntity>): List<Long> =
      performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfPromptCardEntity.insertAndReturnIdsList(_connection,
        cards)
    _result
  }

  public override suspend fun hardDeleteCard(card: PromptCardEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfPromptCardEntity.handle(_connection, card)
  }

  public override suspend fun updateCard(card: PromptCardEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfPromptCardEntity.handle(_connection, card)
  }

  public override suspend fun updateCards(cards: List<PromptCardEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfPromptCardEntity.handleMultiple(_connection, cards)
  }

  public override fun getCardsByDeckId(deckId: Long): Flow<List<PromptCardEntity>> {
    val _sql: String =
        "SELECT * FROM prompt_cards WHERE deckId = ? AND deletedAt IS NULL ORDER BY sortOrder ASC, createdAt ASC"
    return createFlow(__db, false, arrayOf("prompt_cards")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfPromptText: Int = getColumnIndexOrThrow(_stmt, "promptText")
        val _columnIndexOfVariablesJson: Int = getColumnIndexOrThrow(_stmt, "variablesJson")
        val _columnIndexOfOutputType: Int = getColumnIndexOrThrow(_stmt, "outputType")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfUsageCount: Int = getColumnIndexOrThrow(_stmt, "usageCount")
        val _columnIndexOfLastUsedAt: Int = getColumnIndexOrThrow(_stmt, "lastUsedAt")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptCardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptCardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpPromptText: String
          _tmpPromptText = _stmt.getText(_columnIndexOfPromptText)
          val _tmpVariablesJson: String?
          if (_stmt.isNull(_columnIndexOfVariablesJson)) {
            _tmpVariablesJson = null
          } else {
            _tmpVariablesJson = _stmt.getText(_columnIndexOfVariablesJson)
          }
          val _tmpOutputType: String
          _tmpOutputType = _stmt.getText(_columnIndexOfOutputType)
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpUsageCount: Int
          _tmpUsageCount = _stmt.getLong(_columnIndexOfUsageCount).toInt()
          val _tmpLastUsedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastUsedAt)) {
            _tmpLastUsedAt = null
          } else {
            _tmpLastUsedAt = _stmt.getLong(_columnIndexOfLastUsedAt)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
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
              PromptCardEntity(_tmpId,_tmpDeckId,_tmpTitle,_tmpPromptText,_tmpVariablesJson,_tmpOutputType,_tmpTags,_tmpUsageCount,_tmpLastUsedAt,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCardsByDeckIdNow(deckId: Long): List<PromptCardEntity> {
    val _sql: String =
        "SELECT * FROM prompt_cards WHERE deckId = ? AND deletedAt IS NULL ORDER BY sortOrder ASC, createdAt ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfPromptText: Int = getColumnIndexOrThrow(_stmt, "promptText")
        val _columnIndexOfVariablesJson: Int = getColumnIndexOrThrow(_stmt, "variablesJson")
        val _columnIndexOfOutputType: Int = getColumnIndexOrThrow(_stmt, "outputType")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfUsageCount: Int = getColumnIndexOrThrow(_stmt, "usageCount")
        val _columnIndexOfLastUsedAt: Int = getColumnIndexOrThrow(_stmt, "lastUsedAt")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptCardEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptCardEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpPromptText: String
          _tmpPromptText = _stmt.getText(_columnIndexOfPromptText)
          val _tmpVariablesJson: String?
          if (_stmt.isNull(_columnIndexOfVariablesJson)) {
            _tmpVariablesJson = null
          } else {
            _tmpVariablesJson = _stmt.getText(_columnIndexOfVariablesJson)
          }
          val _tmpOutputType: String
          _tmpOutputType = _stmt.getText(_columnIndexOfOutputType)
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpUsageCount: Int
          _tmpUsageCount = _stmt.getLong(_columnIndexOfUsageCount).toInt()
          val _tmpLastUsedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastUsedAt)) {
            _tmpLastUsedAt = null
          } else {
            _tmpLastUsedAt = _stmt.getLong(_columnIndexOfLastUsedAt)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
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
              PromptCardEntity(_tmpId,_tmpDeckId,_tmpTitle,_tmpPromptText,_tmpVariablesJson,_tmpOutputType,_tmpTags,_tmpUsageCount,_tmpLastUsedAt,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCardById(id: Long): PromptCardEntity? {
    val _sql: String = "SELECT * FROM prompt_cards WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeckId: Int = getColumnIndexOrThrow(_stmt, "deckId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfPromptText: Int = getColumnIndexOrThrow(_stmt, "promptText")
        val _columnIndexOfVariablesJson: Int = getColumnIndexOrThrow(_stmt, "variablesJson")
        val _columnIndexOfOutputType: Int = getColumnIndexOrThrow(_stmt, "outputType")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfUsageCount: Int = getColumnIndexOrThrow(_stmt, "usageCount")
        val _columnIndexOfLastUsedAt: Int = getColumnIndexOrThrow(_stmt, "lastUsedAt")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: PromptCardEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeckId: Long
          _tmpDeckId = _stmt.getLong(_columnIndexOfDeckId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpPromptText: String
          _tmpPromptText = _stmt.getText(_columnIndexOfPromptText)
          val _tmpVariablesJson: String?
          if (_stmt.isNull(_columnIndexOfVariablesJson)) {
            _tmpVariablesJson = null
          } else {
            _tmpVariablesJson = _stmt.getText(_columnIndexOfVariablesJson)
          }
          val _tmpOutputType: String
          _tmpOutputType = _stmt.getText(_columnIndexOfOutputType)
          val _tmpTags: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp)
          val _tmpUsageCount: Int
          _tmpUsageCount = _stmt.getLong(_columnIndexOfUsageCount).toInt()
          val _tmpLastUsedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastUsedAt)) {
            _tmpLastUsedAt = null
          } else {
            _tmpLastUsedAt = _stmt.getLong(_columnIndexOfLastUsedAt)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
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
              PromptCardEntity(_tmpId,_tmpDeckId,_tmpTitle,_tmpPromptText,_tmpVariablesJson,_tmpOutputType,_tmpTags,_tmpUsageCount,_tmpLastUsedAt,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
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
    val _sql: String = "SELECT COUNT(*) FROM prompt_cards WHERE deletedAt IS NULL"
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
    val _sql: String =
        "SELECT COUNT(*) FROM prompt_cards WHERE deletedAt IS NULL AND deckId IN (SELECT id FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL)"
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

  public override suspend fun softDeleteCardById(cardId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE prompt_cards SET deletedAt = ?, updatedAt = ? WHERE id = ?"
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

  public override suspend fun restoreCardById(cardId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE prompt_cards SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
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

  public override suspend fun recordUse(
    cardId: Long,
    lastUsedAt: Long,
    updatedAt: Long,
  ) {
    val _sql: String =
        "UPDATE prompt_cards SET usageCount = usageCount + 1, lastUsedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, lastUsedAt)
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
