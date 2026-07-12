package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.NoteBlueprintEntity
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
public class NoteBlueprintDao_Impl(
  __db: RoomDatabase,
) : NoteBlueprintDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNoteBlueprintEntity: EntityInsertAdapter<NoteBlueprintEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfNoteBlueprintEntity: EntityDeleteOrUpdateAdapter<NoteBlueprintEntity>

  private val __updateAdapterOfNoteBlueprintEntity: EntityDeleteOrUpdateAdapter<NoteBlueprintEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfNoteBlueprintEntity = object : EntityInsertAdapter<NoteBlueprintEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `note_blueprints` (`id`,`externalId`,`collectionId`,`title`,`description`,`summary`,`iconName`,`coverImage`,`body`,`bulletPoints`,`tags`,`blueprintMode`,`linkedQuestionsJson`,`linkedAssetsJson`,`reviewStatus`,`reviewCount`,`lastReviewedAt`,`createdAt`,`updatedAt`,`sourceQuestionId`,`deletedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NoteBlueprintEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.collectionId)
        statement.bindText(4, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDescription)
        }
        val _tmpSummary: String? = entity.summary
        if (_tmpSummary == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSummary)
        }
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpCoverImage)
        }
        statement.bindText(9, entity.body)
        val _tmp: String = __converters.fromStringList(entity.bulletPoints)
        statement.bindText(10, _tmp)
        val _tmp_1: String = __converters.fromStringList(entity.tags)
        statement.bindText(11, _tmp_1)
        statement.bindText(12, entity.blueprintMode)
        statement.bindText(13, entity.linkedQuestionsJson)
        statement.bindText(14, entity.linkedAssetsJson)
        statement.bindText(15, entity.reviewStatus)
        statement.bindLong(16, entity.reviewCount.toLong())
        statement.bindLong(17, entity.lastReviewedAt)
        statement.bindLong(18, entity.createdAt)
        statement.bindLong(19, entity.updatedAt)
        val _tmpSourceQuestionId: Long? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmpSourceQuestionId)
        }
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindLong(21, _tmpDeletedAt)
        }
      }
    }
    this.__deleteAdapterOfNoteBlueprintEntity = object :
        EntityDeleteOrUpdateAdapter<NoteBlueprintEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `note_blueprints` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteBlueprintEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfNoteBlueprintEntity = object :
        EntityDeleteOrUpdateAdapter<NoteBlueprintEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `note_blueprints` SET `id` = ?,`externalId` = ?,`collectionId` = ?,`title` = ?,`description` = ?,`summary` = ?,`iconName` = ?,`coverImage` = ?,`body` = ?,`bulletPoints` = ?,`tags` = ?,`blueprintMode` = ?,`linkedQuestionsJson` = ?,`linkedAssetsJson` = ?,`reviewStatus` = ?,`reviewCount` = ?,`lastReviewedAt` = ?,`createdAt` = ?,`updatedAt` = ?,`sourceQuestionId` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NoteBlueprintEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.externalId)
        statement.bindLong(3, entity.collectionId)
        statement.bindText(4, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDescription)
        }
        val _tmpSummary: String? = entity.summary
        if (_tmpSummary == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpSummary)
        }
        val _tmpIconName: String? = entity.iconName
        if (_tmpIconName == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpIconName)
        }
        val _tmpCoverImage: String? = entity.coverImage
        if (_tmpCoverImage == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpCoverImage)
        }
        statement.bindText(9, entity.body)
        val _tmp: String = __converters.fromStringList(entity.bulletPoints)
        statement.bindText(10, _tmp)
        val _tmp_1: String = __converters.fromStringList(entity.tags)
        statement.bindText(11, _tmp_1)
        statement.bindText(12, entity.blueprintMode)
        statement.bindText(13, entity.linkedQuestionsJson)
        statement.bindText(14, entity.linkedAssetsJson)
        statement.bindText(15, entity.reviewStatus)
        statement.bindLong(16, entity.reviewCount.toLong())
        statement.bindLong(17, entity.lastReviewedAt)
        statement.bindLong(18, entity.createdAt)
        statement.bindLong(19, entity.updatedAt)
        val _tmpSourceQuestionId: Long? = entity.sourceQuestionId
        if (_tmpSourceQuestionId == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmpSourceQuestionId)
        }
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(21)
        } else {
          statement.bindLong(21, _tmpDeletedAt)
        }
        statement.bindLong(22, entity.id)
      }
    }
  }

  public override suspend fun insertNote(note: NoteBlueprintEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfNoteBlueprintEntity.insertAndReturnId(_connection, note)
    _result
  }

  public override suspend fun hardDeleteNote(note: NoteBlueprintEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfNoteBlueprintEntity.handle(_connection, note)
  }

  public override suspend fun updateNote(note: NoteBlueprintEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfNoteBlueprintEntity.handle(_connection, note)
  }

  public override fun getNotesByCollectionId(collectionId: Long): Flow<List<NoteBlueprintEntity>> {
    val _sql: String =
        "SELECT * FROM note_blueprints WHERE collectionId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("note_blueprints")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, collectionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNotesByCollectionIdNow(collectionId: Long):
      List<NoteBlueprintEntity> {
    val _sql: String =
        "SELECT * FROM note_blueprints WHERE collectionId = ? AND deletedAt IS NULL ORDER BY updatedAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, collectionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getNotesByBookId(bookId: Long): Flow<List<NoteBlueprintEntity>> {
    val _sql: String =
        "SELECT b.* FROM note_blueprints b JOIN note_collections c ON b.collectionId = c.id WHERE c.bookId = ? AND b.deletedAt IS NULL AND c.deletedAt IS NULL ORDER BY b.updatedAt DESC"
    return createFlow(__db, false, arrayOf("note_blueprints", "note_collections")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNotesByBookIdNow(bookId: Long): List<NoteBlueprintEntity> {
    val _sql: String =
        "SELECT b.* FROM note_blueprints b JOIN note_collections c ON b.collectionId = c.id WHERE c.bookId = ? AND b.deletedAt IS NULL AND c.deletedAt IS NULL ORDER BY b.updatedAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNoteById(id: Long): NoteBlueprintEntity? {
    val _sql: String = "SELECT * FROM note_blueprints WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: NoteBlueprintEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNoteByIdIncludingDeleted(id: Long): NoteBlueprintEntity? {
    val _sql: String = "SELECT * FROM note_blueprints WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: NoteBlueprintEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _result =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getNotesBySourceQuestionId(questionId: Long):
      List<NoteBlueprintEntity> {
    val _sql: String =
        "SELECT * FROM note_blueprints WHERE deletedAt IS NULL AND (sourceQuestionId = ? OR linkedQuestionsJson LIKE '%' || ? || '%') ORDER BY updatedAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, questionId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getNotesByLinkedQuestion(collectionId: Long, questionId: Long):
      Flow<List<NoteBlueprintEntity>> {
    val _sql: String =
        "SELECT * FROM note_blueprints WHERE collectionId = ? AND deletedAt IS NULL AND (sourceQuestionId = ? OR linkedQuestionsJson LIKE '%' || ? || '%') ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("note_blueprints")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, collectionId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, questionId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, questionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countAll(): Int {
    val _sql: String = "SELECT COUNT(*) FROM note_blueprints WHERE deletedAt IS NULL"
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

  public override suspend fun countLinkedBlueprints(): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM note_blueprints WHERE deletedAt IS NULL AND (sourceQuestionId IS NOT NULL OR linkedQuestionsJson != '[]' OR linkedAssetsJson != '[]')"
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

  public override suspend fun getDueBlueprints(now: Long, limit: Int): List<NoteBlueprintEntity> {
    val _sql: String =
        "SELECT * FROM note_blueprints WHERE deletedAt IS NULL AND reviewStatus != 'REVIEWED' AND updatedAt <= ? ORDER BY lastReviewedAt ASC, updatedAt DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countDueBlueprints(now: Long): Int {
    val _sql: String =
        "SELECT COUNT(*) FROM note_blueprints WHERE deletedAt IS NULL AND reviewStatus != 'REVIEWED' AND updatedAt <= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
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

  public override fun getDeletedNotesByWorkspaceFlow(workspaceId: Long):
      Flow<List<NoteBlueprintEntity>> {
    val _sql: String =
        "SELECT * FROM note_blueprints WHERE deletedAt IS NOT NULL AND collectionId IN (SELECT id FROM note_collections WHERE bookId IN (SELECT id FROM books WHERE workspaceId = ?))"
    return createFlow(__db, false, arrayOf("note_blueprints", "note_collections", "books")) {
        _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, workspaceId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfExternalId: Int = getColumnIndexOrThrow(_stmt, "externalId")
        val _columnIndexOfCollectionId: Int = getColumnIndexOrThrow(_stmt, "collectionId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _columnIndexOfCoverImage: Int = getColumnIndexOrThrow(_stmt, "coverImage")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfBulletPoints: Int = getColumnIndexOrThrow(_stmt, "bulletPoints")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfBlueprintMode: Int = getColumnIndexOrThrow(_stmt, "blueprintMode")
        val _columnIndexOfLinkedQuestionsJson: Int = getColumnIndexOrThrow(_stmt,
            "linkedQuestionsJson")
        val _columnIndexOfLinkedAssetsJson: Int = getColumnIndexOrThrow(_stmt, "linkedAssetsJson")
        val _columnIndexOfReviewStatus: Int = getColumnIndexOrThrow(_stmt, "reviewStatus")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSourceQuestionId: Int = getColumnIndexOrThrow(_stmt, "sourceQuestionId")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<NoteBlueprintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NoteBlueprintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpExternalId: String
          _tmpExternalId = _stmt.getText(_columnIndexOfExternalId)
          val _tmpCollectionId: Long
          _tmpCollectionId = _stmt.getLong(_columnIndexOfCollectionId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSummary: String?
          if (_stmt.isNull(_columnIndexOfSummary)) {
            _tmpSummary = null
          } else {
            _tmpSummary = _stmt.getText(_columnIndexOfSummary)
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
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpBulletPoints: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfBulletPoints)
          _tmpBulletPoints = __converters.toStringList(_tmp)
          val _tmpTags: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfTags)
          _tmpTags = __converters.toStringList(_tmp_1)
          val _tmpBlueprintMode: String
          _tmpBlueprintMode = _stmt.getText(_columnIndexOfBlueprintMode)
          val _tmpLinkedQuestionsJson: String
          _tmpLinkedQuestionsJson = _stmt.getText(_columnIndexOfLinkedQuestionsJson)
          val _tmpLinkedAssetsJson: String
          _tmpLinkedAssetsJson = _stmt.getText(_columnIndexOfLinkedAssetsJson)
          val _tmpReviewStatus: String
          _tmpReviewStatus = _stmt.getText(_columnIndexOfReviewStatus)
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
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          _item =
              NoteBlueprintEntity(_tmpId,_tmpExternalId,_tmpCollectionId,_tmpTitle,_tmpDescription,_tmpSummary,_tmpIconName,_tmpCoverImage,_tmpBody,_tmpBulletPoints,_tmpTags,_tmpBlueprintMode,_tmpLinkedQuestionsJson,_tmpLinkedAssetsJson,_tmpReviewStatus,_tmpReviewCount,_tmpLastReviewedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSourceQuestionId,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markReviewed(id: Long, reviewedAt: Long) {
    val _sql: String =
        "UPDATE note_blueprints SET reviewStatus = 'REVIEWED', reviewCount = reviewCount + 1, lastReviewedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, reviewedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, reviewedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun snoozeBlueprint(id: Long, nextReviewAt: Long) {
    val _sql: String =
        "UPDATE note_blueprints SET reviewStatus = 'REVIEWING', updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, nextReviewAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteNoteById(noteId: Long, deletedAt: Long) {
    val _sql: String = "UPDATE note_blueprints SET deletedAt = ?, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, noteId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreNoteById(noteId: Long, updatedAt: Long) {
    val _sql: String = "UPDATE note_blueprints SET deletedAt = NULL, updatedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, noteId)
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
