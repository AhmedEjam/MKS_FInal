package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.SlideshowCourseEntity
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
public class SlideshowCourseDao_Impl(
  __db: RoomDatabase,
) : SlideshowCourseDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSlideshowCourseEntity: EntityInsertAdapter<SlideshowCourseEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfSlideshowCourseEntity:
      EntityDeleteOrUpdateAdapter<SlideshowCourseEntity>

  private val __updateAdapterOfSlideshowCourseEntity:
      EntityDeleteOrUpdateAdapter<SlideshowCourseEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSlideshowCourseEntity = object :
        EntityInsertAdapter<SlideshowCourseEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `slideshow_courses` (`id`,`externalId`,`bookId`,`title`,`description`,`iconName`,`coverImage`,`tags`,`slideCount`,`studiedSlideCount`,`progress`,`isSystem`,`isPinned`,`createdAt`,`updatedAt`,`lastStudiedAt`,`lastEditedAt`,`isDerived`,`sourceQuizId`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SlideshowCourseEntity) {
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
        statement.bindLong(9, entity.slideCount.toLong())
        statement.bindLong(10, entity.studiedSlideCount.toLong())
        statement.bindDouble(11, entity.progress.toDouble())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(13, _tmp_2.toLong())
        statement.bindLong(14, entity.createdAt)
        statement.bindLong(15, entity.updatedAt)
        statement.bindLong(16, entity.lastStudiedAt)
        statement.bindLong(17, entity.lastEditedAt)
        val _tmp_3: Int = if (entity.isDerived) 1 else 0
        statement.bindLong(18, _tmp_3.toLong())
        val _tmpSourceQuizId: Long? = entity.sourceQuizId
        if (_tmpSourceQuizId == null) {
          statement.bindNull(19)
        } else {
          statement.bindLong(19, _tmpSourceQuizId)
        }
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfSlideshowCourseEntity = object :
        EntityDeleteOrUpdateAdapter<SlideshowCourseEntity>() {
      protected override fun createQuery(): String =
          "DELETE FROM `slideshow_courses` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SlideshowCourseEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfSlideshowCourseEntity = object :
        EntityDeleteOrUpdateAdapter<SlideshowCourseEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `slideshow_courses` SET `id` = ?,`externalId` = ?,`bookId` = ?,`title` = ?,`description` = ?,`iconName` = ?,`coverImage` = ?,`tags` = ?,`slideCount` = ?,`studiedSlideCount` = ?,`progress` = ?,`isSystem` = ?,`isPinned` = ?,`createdAt` = ?,`updatedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`isDerived` = ?,`sourceQuizId` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SlideshowCourseEntity) {
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
        statement.bindLong(9, entity.slideCount.toLong())
        statement.bindLong(10, entity.studiedSlideCount.toLong())
        statement.bindDouble(11, entity.progress.toDouble())
        val _tmp_1: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isPinned) 1 else 0
        statement.bindLong(13, _tmp_2.toLong())
        statement.bindLong(14, entity.createdAt)
        statement.bindLong(15, entity.updatedAt)
        statement.bindLong(16, entity.lastStudiedAt)
        statement.bindLong(17, entity.lastEditedAt)
        val _tmp_3: Int = if (entity.isDerived) 1 else 0
        statement.bindLong(18, _tmp_3.toLong())
        val _tmpSourceQuizId: Long? = entity.sourceQuizId
        if (_tmpSourceQuizId == null) {
          statement.bindNull(19)
        } else {
          statement.bindLong(19, _tmpSourceQuizId)
        }
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmpDeletedAt)
        }
        statement.bindLong(21, entity.id)
      }
    }
  }

  public override suspend fun insertCourse(course: SlideshowCourseEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfSlideshowCourseEntity.insertAndReturnId(_connection,
        course)
    _result
  }

  public override suspend fun hardDeleteCourse(course: SlideshowCourseEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfSlideshowCourseEntity.handle(_connection, course)
  }

  public override suspend fun updateCourse(course: SlideshowCourseEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfSlideshowCourseEntity.handle(_connection, course)
  }

  public override fun getCoursesByBookId(bookId: Long): Flow<List<SlideshowCourseEntity>> {
    val _sql: String =
        "SELECT * FROM slideshow_courses WHERE bookId = ? AND deletedAt IS NULL ORDER BY lastEditedAt DESC"
    return createFlow(__db, false, arrayOf("slideshow_courses")) { _connection ->
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
        val _columnIndexOfSlideCount: Int = getColumnIndexOrThrow(_stmt, "slideCount")
        val _columnIndexOfStudiedSlideCount: Int = getColumnIndexOrThrow(_stmt, "studiedSlideCount")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsDerived: Int = getColumnIndexOrThrow(_stmt, "isDerived")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SlideshowCourseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SlideshowCourseEntity
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
          val _tmpSlideCount: Int
          _tmpSlideCount = _stmt.getLong(_columnIndexOfSlideCount).toInt()
          val _tmpStudiedSlideCount: Int
          _tmpStudiedSlideCount = _stmt.getLong(_columnIndexOfStudiedSlideCount).toInt()
          val _tmpProgress: Float
          _tmpProgress = _stmt.getDouble(_columnIndexOfProgress).toFloat()
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
          val _tmpIsDerived: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDerived).toInt()
          _tmpIsDerived = _tmp_3 != 0
          val _tmpSourceQuizId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getLong(_columnIndexOfSourceQuizId)
          }
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              SlideshowCourseEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpSlideCount,_tmpStudiedSlideCount,_tmpProgress,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsDerived,_tmpSourceQuizId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSlideshowCoursesByBookIdNow(bookId: Long):
      List<SlideshowCourseEntity> {
    val _sql: String =
        "SELECT * FROM slideshow_courses WHERE bookId = ? AND deletedAt IS NULL ORDER BY lastEditedAt DESC"
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
        val _columnIndexOfSlideCount: Int = getColumnIndexOrThrow(_stmt, "slideCount")
        val _columnIndexOfStudiedSlideCount: Int = getColumnIndexOrThrow(_stmt, "studiedSlideCount")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsDerived: Int = getColumnIndexOrThrow(_stmt, "isDerived")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SlideshowCourseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SlideshowCourseEntity
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
          val _tmpSlideCount: Int
          _tmpSlideCount = _stmt.getLong(_columnIndexOfSlideCount).toInt()
          val _tmpStudiedSlideCount: Int
          _tmpStudiedSlideCount = _stmt.getLong(_columnIndexOfStudiedSlideCount).toInt()
          val _tmpProgress: Float
          _tmpProgress = _stmt.getDouble(_columnIndexOfProgress).toFloat()
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
          val _tmpIsDerived: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDerived).toInt()
          _tmpIsDerived = _tmp_3 != 0
          val _tmpSourceQuizId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getLong(_columnIndexOfSourceQuizId)
          }
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              SlideshowCourseEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpSlideCount,_tmpStudiedSlideCount,_tmpProgress,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsDerived,_tmpSourceQuizId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCourseById(id: Long): SlideshowCourseEntity? {
    val _sql: String = "SELECT * FROM slideshow_courses WHERE id = ? AND deletedAt IS NULL"
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
        val _columnIndexOfSlideCount: Int = getColumnIndexOrThrow(_stmt, "slideCount")
        val _columnIndexOfStudiedSlideCount: Int = getColumnIndexOrThrow(_stmt, "studiedSlideCount")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsDerived: Int = getColumnIndexOrThrow(_stmt, "isDerived")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: SlideshowCourseEntity?
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
          val _tmpSlideCount: Int
          _tmpSlideCount = _stmt.getLong(_columnIndexOfSlideCount).toInt()
          val _tmpStudiedSlideCount: Int
          _tmpStudiedSlideCount = _stmt.getLong(_columnIndexOfStudiedSlideCount).toInt()
          val _tmpProgress: Float
          _tmpProgress = _stmt.getDouble(_columnIndexOfProgress).toFloat()
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
          val _tmpIsDerived: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDerived).toInt()
          _tmpIsDerived = _tmp_3 != 0
          val _tmpSourceQuizId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getLong(_columnIndexOfSourceQuizId)
          }
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              SlideshowCourseEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpSlideCount,_tmpStudiedSlideCount,_tmpProgress,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsDerived,_tmpSourceQuizId,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCourseByIdIncludingDeleted(id: Long): SlideshowCourseEntity? {
    val _sql: String = "SELECT * FROM slideshow_courses WHERE id = ? LIMIT 1"
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
        val _columnIndexOfSlideCount: Int = getColumnIndexOrThrow(_stmt, "slideCount")
        val _columnIndexOfStudiedSlideCount: Int = getColumnIndexOrThrow(_stmt, "studiedSlideCount")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsDerived: Int = getColumnIndexOrThrow(_stmt, "isDerived")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: SlideshowCourseEntity?
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
          val _tmpSlideCount: Int
          _tmpSlideCount = _stmt.getLong(_columnIndexOfSlideCount).toInt()
          val _tmpStudiedSlideCount: Int
          _tmpStudiedSlideCount = _stmt.getLong(_columnIndexOfStudiedSlideCount).toInt()
          val _tmpProgress: Float
          _tmpProgress = _stmt.getDouble(_columnIndexOfProgress).toFloat()
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
          val _tmpIsDerived: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDerived).toInt()
          _tmpIsDerived = _tmp_3 != 0
          val _tmpSourceQuizId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getLong(_columnIndexOfSourceQuizId)
          }
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              SlideshowCourseEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpSlideCount,_tmpStudiedSlideCount,_tmpProgress,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsDerived,_tmpSourceQuizId,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDeletedCoursesByWorkspaceFlow(workspaceId: Long):
      Flow<List<SlideshowCourseEntity>> {
    val _sql: String =
        "SELECT * FROM slideshow_courses WHERE deletedAt IS NOT NULL AND bookId IN (SELECT id FROM books WHERE workspaceId = ?)"
    return createFlow(__db, false, arrayOf("slideshow_courses", "books")) { _connection ->
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
        val _columnIndexOfSlideCount: Int = getColumnIndexOrThrow(_stmt, "slideCount")
        val _columnIndexOfStudiedSlideCount: Int = getColumnIndexOrThrow(_stmt, "studiedSlideCount")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsPinned: Int = getColumnIndexOrThrow(_stmt, "isPinned")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfIsDerived: Int = getColumnIndexOrThrow(_stmt, "isDerived")
        val _columnIndexOfSourceQuizId: Int = getColumnIndexOrThrow(_stmt, "sourceQuizId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<SlideshowCourseEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SlideshowCourseEntity
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
          val _tmpSlideCount: Int
          _tmpSlideCount = _stmt.getLong(_columnIndexOfSlideCount).toInt()
          val _tmpStudiedSlideCount: Int
          _tmpStudiedSlideCount = _stmt.getLong(_columnIndexOfStudiedSlideCount).toInt()
          val _tmpProgress: Float
          _tmpProgress = _stmt.getDouble(_columnIndexOfProgress).toFloat()
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
          val _tmpIsDerived: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsDerived).toInt()
          _tmpIsDerived = _tmp_3 != 0
          val _tmpSourceQuizId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuizId)) {
            _tmpSourceQuizId = null
          } else {
            _tmpSourceQuizId = _stmt.getLong(_columnIndexOfSourceQuizId)
          }
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              SlideshowCourseEntity(_tmpId,_tmpExternalId,_tmpBookId,_tmpTitle,_tmpDescription,_tmpIconName,_tmpCoverImage,_tmpTags,_tmpSlideCount,_tmpStudiedSlideCount,_tmpProgress,_tmpIsSystem,_tmpIsPinned,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpIsDerived,_tmpSourceQuizId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteCourseById(courseId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE slideshow_courses SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, courseId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreCourseById(courseId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE slideshow_courses SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, courseId)
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
