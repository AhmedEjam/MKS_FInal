package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.PromptEntity
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
public class PromptDao_Impl(
  __db: RoomDatabase,
) : PromptDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPromptEntity: EntityInsertAdapter<PromptEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfPromptEntity: EntityDeleteOrUpdateAdapter<PromptEntity>

  private val __updateAdapterOfPromptEntity: EntityDeleteOrUpdateAdapter<PromptEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPromptEntity = object : EntityInsertAdapter<PromptEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `prompts` (`id`,`externalId`,`bookId`,`title`,`stem`,`conversationLinks`,`usageCount`,`lastUsedAt`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PromptEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.stem)
        val _tmp: String = __converters.fromStringList(entity.conversationLinks)
        statement.bindText(6, _tmp)
        statement.bindLong(7, entity.usageCount.toLong())
        statement.bindLong(8, entity.lastUsedAt)
        statement.bindLong(9, entity.createdAt)
        statement.bindLong(10, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfPromptEntity = object : EntityDeleteOrUpdateAdapter<PromptEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `prompts` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfPromptEntity = object : EntityDeleteOrUpdateAdapter<PromptEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `prompts` SET `id` = ?,`externalId` = ?,`bookId` = ?,`title` = ?,`stem` = ?,`conversationLinks` = ?,`usageCount` = ?,`lastUsedAt` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.bookId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.stem)
        val _tmp: String = __converters.fromStringList(entity.conversationLinks)
        statement.bindText(6, _tmp)
        statement.bindLong(7, entity.usageCount.toLong())
        statement.bindLong(8, entity.lastUsedAt)
        statement.bindLong(9, entity.createdAt)
        statement.bindLong(10, entity.updatedAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmpDeletedAt)
        }
        statement.bindLong(12, entity.id)
      }
    }
  }

  public override suspend fun insertPrompt(prompt: PromptEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfPromptEntity.insertAndReturnId(_connection, prompt)
    _result
  }

  public override suspend fun hardDeletePrompt(prompt: PromptEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfPromptEntity.handle(_connection, prompt)
  }

  public override suspend fun updatePrompt(prompt: PromptEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfPromptEntity.handle(_connection, prompt)
  }

  public override fun getPromptsByBookId(bookId: Long): Flow<List<PromptEntity>> {
    val _sql: String =
        "SELECT * FROM prompts WHERE bookId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("prompts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfStem: Int = getColumnIndexOrThrow(_stmt, "stem")
        val _columnIndexOfConversationLinks: Int = getColumnIndexOrThrow(_stmt, "conversationLinks")
        val _columnIndexOfUsageCount: Int = getColumnIndexOrThrow(_stmt, "usageCount")
        val _columnIndexOfLastUsedAt: Int = getColumnIndexOrThrow(_stmt, "lastUsedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpStem: String
          _tmpStem = _stmt.getText(_columnIndexOfStem)
          val _tmpConversationLinks: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfConversationLinks)
          _tmpConversationLinks = __converters.toStringList(_tmp)
          val _tmpUsageCount: Int
          _tmpUsageCount = _stmt.getLong(_columnIndexOfUsageCount).toInt()
          val _tmpLastUsedAt: Long
          _tmpLastUsedAt = _stmt.getLong(_columnIndexOfLastUsedAt)
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
              PromptEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpStem,_tmpConversationLinks,_tmpUsageCount,_tmpLastUsedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPromptById(id: Long): PromptEntity? {
    val _sql: String = "SELECT * FROM prompts WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfStem: Int = getColumnIndexOrThrow(_stmt, "stem")
        val _columnIndexOfConversationLinks: Int = getColumnIndexOrThrow(_stmt, "conversationLinks")
        val _columnIndexOfUsageCount: Int = getColumnIndexOrThrow(_stmt, "usageCount")
        val _columnIndexOfLastUsedAt: Int = getColumnIndexOrThrow(_stmt, "lastUsedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: PromptEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpStem: String
          _tmpStem = _stmt.getText(_columnIndexOfStem)
          val _tmpConversationLinks: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfConversationLinks)
          _tmpConversationLinks = __converters.toStringList(_tmp)
          val _tmpUsageCount: Int
          _tmpUsageCount = _stmt.getLong(_columnIndexOfUsageCount).toInt()
          val _tmpLastUsedAt: Long
          _tmpLastUsedAt = _stmt.getLong(_columnIndexOfLastUsedAt)
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
              PromptEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpStem,_tmpConversationLinks,_tmpUsageCount,_tmpLastUsedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPromptByIdIncludingDeleted(id: Long): PromptEntity? {
    val _sql: String = "SELECT * FROM prompts WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfStem: Int = getColumnIndexOrThrow(_stmt, "stem")
        val _columnIndexOfConversationLinks: Int = getColumnIndexOrThrow(_stmt, "conversationLinks")
        val _columnIndexOfUsageCount: Int = getColumnIndexOrThrow(_stmt, "usageCount")
        val _columnIndexOfLastUsedAt: Int = getColumnIndexOrThrow(_stmt, "lastUsedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: PromptEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpBookId: Long
          _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpStem: String
          _tmpStem = _stmt.getText(_columnIndexOfStem)
          val _tmpConversationLinks: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfConversationLinks)
          _tmpConversationLinks = __converters.toStringList(_tmp)
          val _tmpUsageCount: Int
          _tmpUsageCount = _stmt.getLong(_columnIndexOfUsageCount).toInt()
          val _tmpLastUsedAt: Long
          _tmpLastUsedAt = _stmt.getLong(_columnIndexOfLastUsedAt)
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
              PromptEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpStem,_tmpConversationLinks,_tmpUsageCount,_tmpLastUsedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeletePromptById(promptId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE prompts SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, promptId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restorePromptById(promptId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE prompts SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, promptId)
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
