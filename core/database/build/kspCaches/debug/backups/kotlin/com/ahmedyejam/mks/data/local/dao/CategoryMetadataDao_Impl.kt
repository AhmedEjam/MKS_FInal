package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.entity.CategoryMetadataEntity
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
public class CategoryMetadataDao_Impl(
  __db: RoomDatabase,
) : CategoryMetadataDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCategoryMetadataEntity: EntityInsertAdapter<CategoryMetadataEntity>

  private val __deleteAdapterOfCategoryMetadataEntity:
      EntityDeleteOrUpdateAdapter<CategoryMetadataEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCategoryMetadataEntity = object :
        EntityInsertAdapter<CategoryMetadataEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `category_metadata` (`name`,`emoji`,`color`,`isPinned`,`deletedAt`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryMetadataEntity) {
        statement.bindText(1, entity.name)
        val _tmpEmoji: String? = entity.emoji
        if (_tmpEmoji == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpEmoji)
        }
        val _tmpColor: Int? = entity.color
        if (_tmpColor == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpColor.toLong())
        }
        val _tmp: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfCategoryMetadataEntity = object :
        EntityDeleteOrUpdateAdapter<CategoryMetadataEntity>() {
      protected override fun createQuery(): String =
          "DELETE FROM `category_metadata` WHERE `name` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryMetadataEntity) {
        statement.bindText(1, entity.name)
      }
    }
  }

  public override suspend fun insertMetadata(metadata: CategoryMetadataEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCategoryMetadataEntity.insert(_connection, metadata)
  }

  public override suspend fun deleteMetadata(metadata: CategoryMetadataEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfCategoryMetadataEntity.handle(_connection, metadata)
  }

  public override fun getAllMetadata(): Flow<List<CategoryMetadataEntity>> {
    val _sql: String = "SELECT * FROM category_metadata"
    return createFlow(__db, false, arrayOf("category_metadata")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmoji: Int = getColumnIndexOrThrow(_stmt, "emoji")
        val _columnIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<CategoryMetadataEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryMetadataEntity
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmoji: String?
          if (_stmt.isNull(_columnIndexOfEmoji)) {
            _tmpEmoji = null
          } else {
            _tmpEmoji = _stmt.getText(_columnIndexOfEmoji)
          }
          val _tmpColor: Int?
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null
          } else {
            _tmpColor = _stmt.getLong(_columnIndexOfColor).toInt()
          }
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item = CategoryMetadataEntity(_tmpName,_tmpEmoji,_tmpColor,_tmpIsPinned,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMetadataForCategory(name: String): CategoryMetadataEntity? {
    val _sql: String = "SELECT * FROM category_metadata WHERE name = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, name)
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmoji: Int = getColumnIndexOrThrow(_stmt, "emoji")
        val _columnIndexOfColor: Int = getColumnIndexOrThrow(_stmt, "color")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: CategoryMetadataEntity?
        if (_stmt.step()) {
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmoji: String?
          if (_stmt.isNull(_columnIndexOfEmoji)) {
            _tmpEmoji = null
          } else {
            _tmpEmoji = _stmt.getText(_columnIndexOfEmoji)
          }
          val _tmpColor: Int?
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null
          } else {
            _tmpColor = _stmt.getLong(_columnIndexOfColor).toInt()
          }
          val _tmpIsPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPinned).toInt()
          _tmpIsPinned = _tmp != 0
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result = CategoryMetadataEntity(_tmpName,_tmpEmoji,_tmpColor,_tmpIsPinned,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteMetadataByName(name: String) {
    val _sql: String = "DELETE FROM category_metadata WHERE name = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, name)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllMetadata() {
    val _sql: String = "DELETE FROM category_metadata"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
