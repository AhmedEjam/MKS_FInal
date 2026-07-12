package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.CourseSlideEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CourseSlideDao_Impl(
  __db: RoomDatabase,
) : CourseSlideDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCourseSlideEntity: EntityInsertAdapter<CourseSlideEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfCourseSlideEntity: EntityDeleteOrUpdateAdapter<CourseSlideEntity>

  private val __updateAdapterOfCourseSlideEntity: EntityDeleteOrUpdateAdapter<CourseSlideEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCourseSlideEntity = object : EntityInsertAdapter<CourseSlideEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `course_slides` (`id`,`externalId`,`courseId`,`title`,`body`,`speakerNotes`,`imagePath`,`orderIndex`,`isCompleted`,`tags`,`difficulty`,`dueAt`,`reviewCount`,`lastReviewedAt`,`createdAt`,`updatedAt`,`sourceQuestionId`,`syncConfig`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CourseSlideEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.courseId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.body)
        val _tmpSpeakerNotes: String? = entity.speakerNotes
        if (_tmpSpeakerNotes == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSpeakerNotes)
        }
        val _tmpImagePath: String? = entity.imagePath
        if (_tmpImagePath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpImagePath)
        }
        statement.bindLong(8, entity.orderIndex.toLong())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        val _tmp_1: String = __converters.fromStringList(entity.tags)
        statement.bindText(10, _tmp_1)
        val _tmpDifficulty: String? = entity.difficulty
        if (_tmpDifficulty == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDifficulty)
        }
        statement.bindLong(12, entity.dueAt)
        statement.bindLong(13, entity.reviewCount.toLong())
        statement.bindLong(14, entity.lastReviewedAt)
        statement.bindLong(15, entity.createdAt)
        statement.bindLong(16, entity.updatedAt)
        val _tmpSourceQuestionId: Long? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(17)
        } else {
          statement.bindLong(17, _tmpSourceQuestionId)
        }
        val _tmp_2: String = __converters.fromStringMap(entity.syncConfig)
        statement.bindText(18, _tmp_2)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(19)
        } else {
          statement.bindLong(19, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfCourseSlideEntity = object :
        EntityDeleteOrUpdateAdapter<CourseSlideEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `course_slides` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CourseSlideEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfCourseSlideEntity = object :
        EntityDeleteOrUpdateAdapter<CourseSlideEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `course_slides` SET `id` = ?,`externalId` = ?,`courseId` = ?,`title` = ?,`body` = ?,`speakerNotes` = ?,`imagePath` = ?,`orderIndex` = ?,`isCompleted` = ?,`tags` = ?,`difficulty` = ?,`dueAt` = ?,`reviewCount` = ?,`lastReviewedAt` = ?,`createdAt` = ?,`updatedAt` = ?,`sourceQuestionId` = ?,`syncConfig` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CourseSlideEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.courseId)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.body)
        val _tmpSpeakerNotes: String? = entity.speakerNotes
        if (_tmpSpeakerNotes == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSpeakerNotes)
        }
        val _tmpImagePath: String? = entity.imagePath
        if (_tmpImagePath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpImagePath)
        }
        statement.bindLong(8, entity.orderIndex.toLong())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        val _tmp_1: String = __converters.fromStringList(entity.tags)
        statement.bindText(10, _tmp_1)
        val _tmpDifficulty: String? = entity.difficulty
        if (_tmpDifficulty == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDifficulty)
        }
        statement.bindLong(12, entity.dueAt)
        statement.bindLong(13, entity.reviewCount.toLong())
        statement.bindLong(14, entity.lastReviewedAt)
        statement.bindLong(15, entity.createdAt)
        statement.bindLong(16, entity.updatedAt)
        val _tmpSourceQuestionId: Long? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(17)
        } else {
          statement.bindLong(17, _tmpSourceQuestionId)
        }
        val _tmp_2: String = __converters.fromStringMap(entity.syncConfig)
        statement.bindText(18, _tmp_2)
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(19)
        } else {
          statement.bindLong(19, _tmpDeletedAt)
        }
        statement.bindLong(20, entity.id)
      }
    }
  }

  public override suspend fun insertSlide(slide: CourseSlideEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfCourseSlideEntity.insertAndReturnId(_connection, slide)
    _result
  }

  public override suspend fun insertSlides(slides: List<CourseSlideEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCourseSlideEntity.insert(_connection, slides)
  }

  public override suspend fun hardDeleteSlide(slide: CourseSlideEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfCourseSlideEntity.handle(_connection, slide)
  }

  public override suspend fun updateSlide(slide: CourseSlideEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfCourseSlideEntity.handle(_connection, slide)
  }

  public override suspend fun updateSlides(slides: List<CourseSlideEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfCourseSlideEntity.handleMultiple(_connection, slides)
  }

  public override fun getSlidesByCourseId(courseId: Long): Flow<List<CourseSlideEntity>> {
    val _sql: String =
        "SELECT * FROM course_slides WHERE courseId = ? AND deletedAt IS NULL ORDER BY orderIndex ASC"
    return createFlow(__db, false, arrayOf("course_slides")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, courseId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCourseId: Int = getColumnIndexOrThrow(_stmt, "courseId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfSpeakerNotes: Int = getColumnIndexOrThrow(_stmt, "speakerNotes")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<CourseSlideEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CourseSlideEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCourseId: Long
          _tmpCourseId = _stmt.getLong(_columnIndexOfCourseId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpSpeakerNotes: String?
          if (_stmt.isNull(_columnIndexOfSpeakerNotes)) {
            _tmpSpeakerNotes = null
          } else {
            _tmpSpeakerNotes = _stmt.getText(_columnIndexOfSpeakerNotes)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_2)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              CourseSlideEntity(_tmpId,_tmpExternalId,_tmpCourseId,_tmpTitle,_tmpBody,_tmpSpeakerNotes,_tmpImagePath,_tmpOrderIndex,_tmpIsCompleted,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSlidesByCourseIdNow(courseId: Long): List<CourseSlideEntity> {
    val _sql: String =
        "SELECT * FROM course_slides WHERE courseId = ? AND deletedAt IS NULL ORDER BY orderIndex ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, courseId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCourseId: Int = getColumnIndexOrThrow(_stmt, "courseId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfSpeakerNotes: Int = getColumnIndexOrThrow(_stmt, "speakerNotes")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<CourseSlideEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CourseSlideEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCourseId: Long
          _tmpCourseId = _stmt.getLong(_columnIndexOfCourseId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpSpeakerNotes: String?
          if (_stmt.isNull(_columnIndexOfSpeakerNotes)) {
            _tmpSpeakerNotes = null
          } else {
            _tmpSpeakerNotes = _stmt.getText(_columnIndexOfSpeakerNotes)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_2)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              CourseSlideEntity(_tmpId,_tmpExternalId,_tmpCourseId,_tmpTitle,_tmpBody,_tmpSpeakerNotes,_tmpImagePath,_tmpOrderIndex,_tmpIsCompleted,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSlidesByCourseIdIncludingDeleted(courseId: Long):
      List<CourseSlideEntity> {
    val _sql: String = "SELECT * FROM course_slides WHERE courseId = ? ORDER BY orderIndex ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, courseId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCourseId: Int = getColumnIndexOrThrow(_stmt, "courseId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfSpeakerNotes: Int = getColumnIndexOrThrow(_stmt, "speakerNotes")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<CourseSlideEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CourseSlideEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCourseId: Long
          _tmpCourseId = _stmt.getLong(_columnIndexOfCourseId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpSpeakerNotes: String?
          if (_stmt.isNull(_columnIndexOfSpeakerNotes)) {
            _tmpSpeakerNotes = null
          } else {
            _tmpSpeakerNotes = _stmt.getText(_columnIndexOfSpeakerNotes)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_2)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              CourseSlideEntity(_tmpId,_tmpExternalId,_tmpCourseId,_tmpTitle,_tmpBody,_tmpSpeakerNotes,_tmpImagePath,_tmpOrderIndex,_tmpIsCompleted,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSlideById(id: Long): CourseSlideEntity? {
    val _sql: String = "SELECT * FROM course_slides WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCourseId: Int = getColumnIndexOrThrow(_stmt, "courseId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfSpeakerNotes: Int = getColumnIndexOrThrow(_stmt, "speakerNotes")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: CourseSlideEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCourseId: Long
          _tmpCourseId = _stmt.getLong(_columnIndexOfCourseId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpSpeakerNotes: String?
          if (_stmt.isNull(_columnIndexOfSpeakerNotes)) {
            _tmpSpeakerNotes = null
          } else {
            _tmpSpeakerNotes = _stmt.getText(_columnIndexOfSpeakerNotes)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_2)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              CourseSlideEntity(_tmpId,_tmpExternalId,_tmpCourseId,_tmpTitle,_tmpBody,_tmpSpeakerNotes,_tmpImagePath,_tmpOrderIndex,_tmpIsCompleted,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSlidesBySourceQuestionId(questionId: Long):
      List<CourseSlideEntity> {
    val _sql: String =
        "SELECT * FROM course_slides WHERE sourceQuestionId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCourseId: Int = getColumnIndexOrThrow(_stmt, "courseId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfSpeakerNotes: Int = getColumnIndexOrThrow(_stmt, "speakerNotes")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<CourseSlideEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CourseSlideEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCourseId: Long
          _tmpCourseId = _stmt.getLong(_columnIndexOfCourseId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpSpeakerNotes: String?
          if (_stmt.isNull(_columnIndexOfSpeakerNotes)) {
            _tmpSpeakerNotes = null
          } else {
            _tmpSpeakerNotes = _stmt.getText(_columnIndexOfSpeakerNotes)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_2)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              CourseSlideEntity(_tmpId,_tmpExternalId,_tmpCourseId,_tmpTitle,_tmpBody,_tmpSpeakerNotes,_tmpImagePath,_tmpOrderIndex,_tmpIsCompleted,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countUnfinishedSlides(): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM course_slides WHERE deletedAt IS NULL AND isCompleted = 0"
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

  public override suspend fun countSlidesInCourse(courseId: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM course_slides WHERE courseId = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, courseId)
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

  public override suspend fun getSlidesByIds(ids: List<Long>): List<CourseSlideEntity> {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT * FROM course_slides WHERE id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(") AND deletedAt IS NULL")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Long in ids) {
          _stmt.bindLong(_argIndex, _item)
          _argIndex++
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCourseId: Int = getColumnIndexOrThrow(_stmt, "courseId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfSpeakerNotes: Int = getColumnIndexOrThrow(_stmt, "speakerNotes")
        val _columnIndexOfImagePath: Int = getColumnIndexOrThrow(_stmt, "imagePath")
        val _columnIndexOfOrderIndex: Int = getColumnIndexOrThrow(_stmt, "orderIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfSyncConfig: Int = getColumnIndexOrThrow(_stmt, "syncConfig")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<CourseSlideEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item_1: CourseSlideEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCourseId: Long
          _tmpCourseId = _stmt.getLong(_columnIndexOfCourseId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpSpeakerNotes: String?
          if (_stmt.isNull(_columnIndexOfSpeakerNotes)) {
            _tmpSpeakerNotes = null
          } else {
            _tmpSpeakerNotes = _stmt.getText(_columnIndexOfSpeakerNotes)
          }
          val _tmpImagePath: String?
          if (_stmt.isNull(_columnIndexOfImagePath)) {
            _tmpImagePath = null
          } else {
            _tmpImagePath = _stmt.getText(_columnIndexOfImagePath)
          }
          val _tmpOrderIndex: Int
          _tmpOrderIndex = _stmt.getLong(_columnIndexOfOrderIndex).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpDifficulty: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmpDifficulty = null
          } else {
            _tmpDifficulty = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Long
          _tmpLastReviewedAt = _stmt.getLong(_columnIndexOfLastReviewedAt)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpSourceQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfSourceQuestionId)) {
            _tmpSourceQuestionId = null
          } else {
            _tmpSourceQuestionId = _stmt.getLong(_columnIndexOfSourceQuestionId)
          }
          val _tmpSyncConfig: Map<String, Boolean>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfSyncConfig)
          _tmpSyncConfig = __converters.toStringMap(_tmp_2)
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item_1 =
              CourseSlideEntity(_tmpId,_tmpExternalId,_tmpCourseId,_tmpTitle,_tmpBody,_tmpSpeakerNotes,_tmpImagePath,_tmpOrderIndex,_tmpIsCompleted,_tmpTags,_tmpDifficulty,_tmpDueAt,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpSyncConfig,_tmpDeletedAt)
          _result.add(_item_1)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSlideById(slideId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE course_slides SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, slideId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSlidesByCourseId(courseId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE course_slides SET deletedAt = ?, updatedAt = ? WHERE courseId = ? AND deletedAt IS NULL"
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

  public override suspend fun restoreSlideById(slideId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE course_slides SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, slideId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreSlidesByCourseId(
    courseId: Long,
    updatedAt: Long,
    deletedAtFilter: Long,
  ) {
    val _sql: String =
        "UPDATE course_slides SET deletedAt = NULL, updatedAt = ? WHERE courseId = ? AND deletedAt = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, courseId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAtFilter)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun moveSlidesToCourse(
    ids: List<Long>,
    targetCourseId: Long,
    updatedAt: Long,
  ) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("UPDATE course_slides SET courseId = ")
    _stringBuilder.append("?")
    _stringBuilder.append(", updatedAt = ")
    _stringBuilder.append("?")
    _stringBuilder.append(" WHERE id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, targetCourseId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        for (_item: Long in ids) {
          _stmt.bindLong(_argIndex, _item)
          _argIndex++
        }
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
