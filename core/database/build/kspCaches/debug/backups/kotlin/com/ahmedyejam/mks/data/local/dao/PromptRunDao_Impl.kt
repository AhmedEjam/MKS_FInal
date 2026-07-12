package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.PromptRunEntity
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
public class PromptRunDao_Impl(
  __db: RoomDatabase,
) : PromptRunDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPromptRunEntity: EntityInsertAdapter<PromptRunEntity>

  private val __deleteAdapterOfPromptRunEntity: EntityDeleteOrUpdateAdapter<PromptRunEntity>

  private val __updateAdapterOfPromptRunEntity: EntityDeleteOrUpdateAdapter<PromptRunEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPromptRunEntity = object : EntityInsertAdapter<PromptRunEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `prompt_runs` (`id`,`promptCardId`,`inputValuesJson`,`renderedPrompt`,`outputText`,`linkedAssetType`,`linkedAssetId`,`createdAt`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PromptRunEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.promptCardId)
        statement.bindText(3, entity.inputValuesJson)
        statement.bindText(4, entity.renderedPrompt)
        val _tmpOutputText: String? = entity.outputText
        if (_tmpOutputText == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpOutputText)
        }
        val _tmpLinkedAssetType: String? = entity.linkedAssetType
        if (_tmpLinkedAssetType == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpLinkedAssetType)
        }
        val _tmpLinkedAssetId: Long? = entity.linkedAssetId
        if (_tmpLinkedAssetId == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpLinkedAssetId)
        }
        statement.bindLong(8, entity.createdAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfPromptRunEntity = object : EntityDeleteOrUpdateAdapter<PromptRunEntity>()
        {
      protected override fun createQuery(): String = "DELETE FROM `prompt_runs` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptRunEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfPromptRunEntity = object : EntityDeleteOrUpdateAdapter<PromptRunEntity>()
        {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `prompt_runs` SET `id` = ?,`promptCardId` = ?,`inputValuesJson` = ?,`renderedPrompt` = ?,`outputText` = ?,`linkedAssetType` = ?,`linkedAssetId` = ?,`createdAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PromptRunEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.promptCardId)
        statement.bindText(3, entity.inputValuesJson)
        statement.bindText(4, entity.renderedPrompt)
        val _tmpOutputText: String? = entity.outputText
        if (_tmpOutputText == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpOutputText)
        }
        val _tmpLinkedAssetType: String? = entity.linkedAssetType
        if (_tmpLinkedAssetType == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpLinkedAssetType)
        }
        val _tmpLinkedAssetId: Long? = entity.linkedAssetId
        if (_tmpLinkedAssetId == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpLinkedAssetId)
        }
        statement.bindLong(8, entity.createdAt)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpDeletedAt)
        }
        statement.bindLong(10, entity.id)
      }
    }
  }

  public override suspend fun insertRun(run: PromptRunEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfPromptRunEntity.insertAndReturnId(_connection, run)
    _result
  }

  public override suspend fun hardDeleteRun(run: PromptRunEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfPromptRunEntity.handle(_connection, run)
  }

  public override suspend fun updateRun(run: PromptRunEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfPromptRunEntity.handle(_connection, run)
  }

  public override fun getRunsByCardId(promptCardId: Long): Flow<List<PromptRunEntity>> {
    val _sql: String =
        "SELECT * FROM prompt_runs WHERE promptCardId = ? AND deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("prompt_runs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, promptCardId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPromptCardId: Int = getColumnIndexOrThrow(_stmt, "promptCardId")
        val _columnIndexOfInputValuesJson: Int = getColumnIndexOrThrow(_stmt, "inputValuesJson")
        val _columnIndexOfRenderedPrompt: Int = getColumnIndexOrThrow(_stmt, "renderedPrompt")
        val _columnIndexOfOutputText: Int = getColumnIndexOrThrow(_stmt, "outputText")
        val _columnIndexOfLinkedAssetType: Int = getColumnIndexOrThrow(_stmt, "linkedAssetType")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptRunEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptRunEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPromptCardId: Long
          _tmpPromptCardId = _stmt.getLong(_columnIndexOfPromptCardId)
          val _tmpInputValuesJson: String
          _tmpInputValuesJson = _stmt.getText(_columnIndexOfInputValuesJson)
          val _tmpRenderedPrompt: String
          _tmpRenderedPrompt = _stmt.getText(_columnIndexOfRenderedPrompt)
          val _tmpOutputText: String?
          if (_stmt.isNull(_columnIndexOfOutputText)) {
            _tmpOutputText = null
          } else {
            _tmpOutputText = _stmt.getText(_columnIndexOfOutputText)
          }
          val _tmpLinkedAssetType: String?
          if (_stmt.isNull(_columnIndexOfLinkedAssetType)) {
            _tmpLinkedAssetType = null
          } else {
            _tmpLinkedAssetType = _stmt.getText(_columnIndexOfLinkedAssetType)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              PromptRunEntity(_tmpId,_tmpPromptCardId,_tmpInputValuesJson,_tmpRenderedPrompt,_tmpOutputText,_tmpLinkedAssetType,_tmpLinkedAssetId,_tmpCreatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRunsByDeckId(deckId: Long): Flow<List<PromptRunEntity>> {
    val _sql: String =
        "SELECT * FROM prompt_runs WHERE deletedAt IS NULL AND promptCardId IN (SELECT id FROM prompt_cards WHERE deckId = ? AND deletedAt IS NULL) ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("prompt_runs", "prompt_cards")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPromptCardId: Int = getColumnIndexOrThrow(_stmt, "promptCardId")
        val _columnIndexOfInputValuesJson: Int = getColumnIndexOrThrow(_stmt, "inputValuesJson")
        val _columnIndexOfRenderedPrompt: Int = getColumnIndexOrThrow(_stmt, "renderedPrompt")
        val _columnIndexOfOutputText: Int = getColumnIndexOrThrow(_stmt, "outputText")
        val _columnIndexOfLinkedAssetType: Int = getColumnIndexOrThrow(_stmt, "linkedAssetType")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptRunEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptRunEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPromptCardId: Long
          _tmpPromptCardId = _stmt.getLong(_columnIndexOfPromptCardId)
          val _tmpInputValuesJson: String
          _tmpInputValuesJson = _stmt.getText(_columnIndexOfInputValuesJson)
          val _tmpRenderedPrompt: String
          _tmpRenderedPrompt = _stmt.getText(_columnIndexOfRenderedPrompt)
          val _tmpOutputText: String?
          if (_stmt.isNull(_columnIndexOfOutputText)) {
            _tmpOutputText = null
          } else {
            _tmpOutputText = _stmt.getText(_columnIndexOfOutputText)
          }
          val _tmpLinkedAssetType: String?
          if (_stmt.isNull(_columnIndexOfLinkedAssetType)) {
            _tmpLinkedAssetType = null
          } else {
            _tmpLinkedAssetType = _stmt.getText(_columnIndexOfLinkedAssetType)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              PromptRunEntity(_tmpId,_tmpPromptCardId,_tmpInputValuesJson,_tmpRenderedPrompt,_tmpOutputText,_tmpLinkedAssetType,_tmpLinkedAssetId,_tmpCreatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getRunsByDeckIdNow(deckId: Long): List<PromptRunEntity> {
    val _sql: String =
        "SELECT * FROM prompt_runs WHERE deletedAt IS NULL AND promptCardId IN (SELECT id FROM prompt_cards WHERE deckId = ? AND deletedAt IS NULL) ORDER BY createdAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deckId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPromptCardId: Int = getColumnIndexOrThrow(_stmt, "promptCardId")
        val _columnIndexOfInputValuesJson: Int = getColumnIndexOrThrow(_stmt, "inputValuesJson")
        val _columnIndexOfRenderedPrompt: Int = getColumnIndexOrThrow(_stmt, "renderedPrompt")
        val _columnIndexOfOutputText: Int = getColumnIndexOrThrow(_stmt, "outputText")
        val _columnIndexOfLinkedAssetType: Int = getColumnIndexOrThrow(_stmt, "linkedAssetType")
        val _columnIndexOfLinkedAssetId: Int = getColumnIndexOrThrow(_stmt, "linkedAssetId")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<PromptRunEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PromptRunEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpPromptCardId: Long
          _tmpPromptCardId = _stmt.getLong(_columnIndexOfPromptCardId)
          val _tmpInputValuesJson: String
          _tmpInputValuesJson = _stmt.getText(_columnIndexOfInputValuesJson)
          val _tmpRenderedPrompt: String
          _tmpRenderedPrompt = _stmt.getText(_columnIndexOfRenderedPrompt)
          val _tmpOutputText: String?
          if (_stmt.isNull(_columnIndexOfOutputText)) {
            _tmpOutputText = null
          } else {
            _tmpOutputText = _stmt.getText(_columnIndexOfOutputText)
          }
          val _tmpLinkedAssetType: String?
          if (_stmt.isNull(_columnIndexOfLinkedAssetType)) {
            _tmpLinkedAssetType = null
          } else {
            _tmpLinkedAssetType = _stmt.getText(_columnIndexOfLinkedAssetType)
          }
          val _tmpLinkedAssetId: Long?
          if (_stmt.isNull(_columnIndexOfLinkedAssetId)) {
            _tmpLinkedAssetId = null
          } else {
            _tmpLinkedAssetId = _stmt.getLong(_columnIndexOfLinkedAssetId)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              PromptRunEntity(_tmpId,_tmpPromptCardId,_tmpInputValuesJson,_tmpRenderedPrompt,_tmpOutputText,_tmpLinkedAssetType,_tmpLinkedAssetId,_tmpCreatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = "SELECT COUNT(*) FROM prompt_runs WHERE deletedAt IS NULL"
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
        "SELECT COUNT(*) FROM prompt_runs WHERE deletedAt IS NULL AND promptCardId IN (SELECT id FROM prompt_cards WHERE deletedAt IS NULL AND deckId IN (SELECT id FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL))"
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

  public override suspend fun countSavedOutputs(): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM prompt_runs WHERE deletedAt IS NULL AND outputText IS NOT NULL AND outputText != ''"
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

  public override suspend fun countSavedOutputsByBookId(bookId: Long): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM prompt_runs WHERE deletedAt IS NULL AND outputText IS NOT NULL AND outputText != '' AND promptCardId IN (SELECT id FROM prompt_cards WHERE deletedAt IS NULL AND deckId IN (SELECT id FROM prompt_decks WHERE bookId = ? AND deletedAt IS NULL))"
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

  public override suspend fun softDeleteRunById(runId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE prompt_runs SET deletedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, runId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreRunById(runId: Long) {
    val _sql: String = "UPDATE prompt_runs SET deletedAt = NULL WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, runId)
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
