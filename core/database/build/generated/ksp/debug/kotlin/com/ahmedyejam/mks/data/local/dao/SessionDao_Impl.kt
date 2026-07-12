package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.local.Converters
import com.ahmedyejam.mks.`data`.local.entity.SessionEntity
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SessionDao_Impl(
  __db: RoomDatabase,
) : SessionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSessionEntity: EntityInsertAdapter<SessionEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfSessionEntity: EntityDeleteOrUpdateAdapter<SessionEntity>

  private val __updateAdapterOfSessionEntity: EntityDeleteOrUpdateAdapter<SessionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSessionEntity = object : EntityInsertAdapter<SessionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `sessions` (`id`,`quizId`,`label`,`currentQuestionIndex`,`score`,`incorrectCount`,`answers`,`answersByIndex`,`isCompleted`,`createdAt`,`updatedAt`,`lastModifiedAt`,`lastStudiedAt`,`lastEditedAt`,`questionIds`,`originalQuestionCount`,`shuffleQuestions`,`shuffleOptions`,`rapidMode`,`repeatWrong`,`quizTimerSeconds`,`questionTimerSeconds`,`rangeFrom`,`rangeTo`,`includeFilters`,`droppedOptions`,`droppedOptionsByIndex`,`visibleOptionsCount`,`visibleOptionsCountByIndex`,`currentStreak`,`maxStreak`,`deletedAt`,`resultTaxonomy`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.quizId)
        statement.bindText(3, entity.label)
        statement.bindLong(4, entity.currentQuestionIndex.toLong())
        statement.bindLong(5, entity.score.toLong())
        statement.bindLong(6, entity.incorrectCount.toLong())
        val _tmp: String = __converters.fromAnswersMap(entity.answers)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.fromIntListIntMap(entity.answersByIndex)
        statement.bindText(8, _tmp_1)
        val _tmp_2: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(9, _tmp_2.toLong())
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.updatedAt)
        statement.bindLong(12, entity.lastModifiedAt)
        statement.bindLong(13, entity.lastStudiedAt)
        statement.bindLong(14, entity.lastEditedAt)
        val _tmp_3: String = __converters.fromLongList(entity.questionIds)
        statement.bindText(15, _tmp_3)
        statement.bindLong(16, entity.originalQuestionCount.toLong())
        val _tmp_4: Int = if (entity.shuffleQuestions) 1 else 0
        statement.bindLong(17, _tmp_4.toLong())
        val _tmp_5: Int = if (entity.shuffleOptions) 1 else 0
        statement.bindLong(18, _tmp_5.toLong())
        val _tmp_6: Int = if (entity.rapidMode) 1 else 0
        statement.bindLong(19, _tmp_6.toLong())
        val _tmp_7: Int = if (entity.repeatWrong) 1 else 0
        statement.bindLong(20, _tmp_7.toLong())
        statement.bindLong(21, entity.quizTimerSeconds.toLong())
        statement.bindLong(22, entity.questionTimerSeconds.toLong())
        statement.bindLong(23, entity.rangeFrom.toLong())
        statement.bindLong(24, entity.rangeTo.toLong())
        val _tmp_8: String = __converters.fromStringList(entity.includeFilters)
        statement.bindText(25, _tmp_8)
        val _tmp_9: String = __converters.fromAnswersMap(entity.droppedOptions)
        statement.bindText(26, _tmp_9)
        val _tmp_10: String = __converters.fromIntListIntMap(entity.droppedOptionsByIndex)
        statement.bindText(27, _tmp_10)
        val _tmp_11: String = __converters.fromIntMap(entity.visibleOptionsCount)
        statement.bindText(28, _tmp_11)
        val _tmp_12: String = __converters.fromIntIntMap(entity.visibleOptionsCountByIndex)
        statement.bindText(29, _tmp_12)
        statement.bindLong(30, entity.currentStreak.toLong())
        statement.bindLong(31, entity.maxStreak.toLong())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(32)
        } else {
          statement.bindLong(32, _tmpDeletedAt)
        }
        val _tmp_13: String = __converters.fromIntStringMap(entity.resultTaxonomy)
        statement.bindText(33, _tmp_13)
      }
    }
    this.__deleteAdapterOfSessionEntity = object : EntityDeleteOrUpdateAdapter<SessionEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `sessions` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SessionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfSessionEntity = object : EntityDeleteOrUpdateAdapter<SessionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `sessions` SET `id` = ?,`quizId` = ?,`label` = ?,`currentQuestionIndex` = ?,`score` = ?,`incorrectCount` = ?,`answers` = ?,`answersByIndex` = ?,`isCompleted` = ?,`createdAt` = ?,`updatedAt` = ?,`lastModifiedAt` = ?,`lastStudiedAt` = ?,`lastEditedAt` = ?,`questionIds` = ?,`originalQuestionCount` = ?,`shuffleQuestions` = ?,`shuffleOptions` = ?,`rapidMode` = ?,`repeatWrong` = ?,`quizTimerSeconds` = ?,`questionTimerSeconds` = ?,`rangeFrom` = ?,`rangeTo` = ?,`includeFilters` = ?,`droppedOptions` = ?,`droppedOptionsByIndex` = ?,`visibleOptionsCount` = ?,`visibleOptionsCountByIndex` = ?,`currentStreak` = ?,`maxStreak` = ?,`deletedAt` = ?,`resultTaxonomy` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SessionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.quizId)
        statement.bindText(3, entity.label)
        statement.bindLong(4, entity.currentQuestionIndex.toLong())
        statement.bindLong(5, entity.score.toLong())
        statement.bindLong(6, entity.incorrectCount.toLong())
        val _tmp: String = __converters.fromAnswersMap(entity.answers)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.fromIntListIntMap(entity.answersByIndex)
        statement.bindText(8, _tmp_1)
        val _tmp_2: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(9, _tmp_2.toLong())
        statement.bindLong(10, entity.createdAt)
        statement.bindLong(11, entity.updatedAt)
        statement.bindLong(12, entity.lastModifiedAt)
        statement.bindLong(13, entity.lastStudiedAt)
        statement.bindLong(14, entity.lastEditedAt)
        val _tmp_3: String = __converters.fromLongList(entity.questionIds)
        statement.bindText(15, _tmp_3)
        statement.bindLong(16, entity.originalQuestionCount.toLong())
        val _tmp_4: Int = if (entity.shuffleQuestions) 1 else 0
        statement.bindLong(17, _tmp_4.toLong())
        val _tmp_5: Int = if (entity.shuffleOptions) 1 else 0
        statement.bindLong(18, _tmp_5.toLong())
        val _tmp_6: Int = if (entity.rapidMode) 1 else 0
        statement.bindLong(19, _tmp_6.toLong())
        val _tmp_7: Int = if (entity.repeatWrong) 1 else 0
        statement.bindLong(20, _tmp_7.toLong())
        statement.bindLong(21, entity.quizTimerSeconds.toLong())
        statement.bindLong(22, entity.questionTimerSeconds.toLong())
        statement.bindLong(23, entity.rangeFrom.toLong())
        statement.bindLong(24, entity.rangeTo.toLong())
        val _tmp_8: String = __converters.fromStringList(entity.includeFilters)
        statement.bindText(25, _tmp_8)
        val _tmp_9: String = __converters.fromAnswersMap(entity.droppedOptions)
        statement.bindText(26, _tmp_9)
        val _tmp_10: String = __converters.fromIntListIntMap(entity.droppedOptionsByIndex)
        statement.bindText(27, _tmp_10)
        val _tmp_11: String = __converters.fromIntMap(entity.visibleOptionsCount)
        statement.bindText(28, _tmp_11)
        val _tmp_12: String = __converters.fromIntIntMap(entity.visibleOptionsCountByIndex)
        statement.bindText(29, _tmp_12)
        statement.bindLong(30, entity.currentStreak.toLong())
        statement.bindLong(31, entity.maxStreak.toLong())
        val _tmpDeletedAt: Long? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(32)
        } else {
          statement.bindLong(32, _tmpDeletedAt)
        }
        val _tmp_13: String = __converters.fromIntStringMap(entity.resultTaxonomy)
        statement.bindText(33, _tmp_13)
        statement.bindLong(34, entity.id)
      }
    }
  }

  public override suspend fun insertSession(session: SessionEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfSessionEntity.insertAndReturnId(_connection, session)
    _result
  }

  public override suspend fun hardDeleteSession(session: SessionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfSessionEntity.handle(_connection, session)
  }

  public override suspend fun updateSession(session: SessionEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfSessionEntity.handle(_connection, session)
  }

  public override fun getSessionsByQuizId(quizId: Long): Flow<List<SessionEntity>> {
    val _sql: String =
        "SELECT * FROM sessions WHERE quizId = ? AND deletedAt IS NULL ORDER BY lastModifiedAt DESC"
    return createFlow(__db, false, arrayOf("sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCurrentQuestionIndex: Int = getColumnIndexOrThrow(_stmt,
            "currentQuestionIndex")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfAnswers: Int = getColumnIndexOrThrow(_stmt, "answers")
        val _columnIndexOfAnswersByIndex: Int = getColumnIndexOrThrow(_stmt, "answersByIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastModifiedAt: Int = getColumnIndexOrThrow(_stmt, "lastModifiedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfQuestionIds: Int = getColumnIndexOrThrow(_stmt, "questionIds")
        val _columnIndexOfOriginalQuestionCount: Int = getColumnIndexOrThrow(_stmt,
            "originalQuestionCount")
        val _columnIndexOfShuffleQuestions: Int = getColumnIndexOrThrow(_stmt, "shuffleQuestions")
        val _columnIndexOfShuffleOptions: Int = getColumnIndexOrThrow(_stmt, "shuffleOptions")
        val _columnIndexOfRapidMode: Int = getColumnIndexOrThrow(_stmt, "rapidMode")
        val _columnIndexOfRepeatWrong: Int = getColumnIndexOrThrow(_stmt, "repeatWrong")
        val _columnIndexOfQuizTimerSeconds: Int = getColumnIndexOrThrow(_stmt, "quizTimerSeconds")
        val _columnIndexOfQuestionTimerSeconds: Int = getColumnIndexOrThrow(_stmt,
            "questionTimerSeconds")
        val _columnIndexOfRangeFrom: Int = getColumnIndexOrThrow(_stmt, "rangeFrom")
        val _columnIndexOfRangeTo: Int = getColumnIndexOrThrow(_stmt, "rangeTo")
        val _columnIndexOfIncludeFilters: Int = getColumnIndexOrThrow(_stmt, "includeFilters")
        val _columnIndexOfDroppedOptions: Int = getColumnIndexOrThrow(_stmt, "droppedOptions")
        val _columnIndexOfDroppedOptionsByIndex: Int = getColumnIndexOrThrow(_stmt,
            "droppedOptionsByIndex")
        val _columnIndexOfVisibleOptionsCount: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCount")
        val _columnIndexOfVisibleOptionsCountByIndex: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCountByIndex")
        val _columnIndexOfCurrentStreak: Int = getColumnIndexOrThrow(_stmt, "currentStreak")
        val _columnIndexOfMaxStreak: Int = getColumnIndexOrThrow(_stmt, "maxStreak")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfResultTaxonomy: Int = getColumnIndexOrThrow(_stmt, "resultTaxonomy")
        val _result: MutableList<SessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCurrentQuestionIndex: Int
          _tmpCurrentQuestionIndex = _stmt.getLong(_columnIndexOfCurrentQuestionIndex).toInt()
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
          val _tmpAnswers: Map<Long, List<Int>>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAnswers)
          _tmpAnswers = __converters.toAnswersMap(_tmp)
          val _tmpAnswersByIndex: Map<Int, List<Int>>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfAnswersByIndex)
          _tmpAnswersByIndex = __converters.toIntListIntMap(_tmp_1)
          val _tmpIsCompleted: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastModifiedAt: Long
          _tmpLastModifiedAt = _stmt.getLong(_columnIndexOfLastModifiedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpQuestionIds: List<Long>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfQuestionIds)
          _tmpQuestionIds = __converters.toLongList(_tmp_3)
          val _tmpOriginalQuestionCount: Int
          _tmpOriginalQuestionCount = _stmt.getLong(_columnIndexOfOriginalQuestionCount).toInt()
          val _tmpShuffleQuestions: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfShuffleQuestions).toInt()
          _tmpShuffleQuestions = _tmp_4 != 0
          val _tmpShuffleOptions: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_columnIndexOfShuffleOptions).toInt()
          _tmpShuffleOptions = _tmp_5 != 0
          val _tmpRapidMode: Boolean
          val _tmp_6: Int
          _tmp_6 = _stmt.getLong(_columnIndexOfRapidMode).toInt()
          _tmpRapidMode = _tmp_6 != 0
          val _tmpRepeatWrong: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_columnIndexOfRepeatWrong).toInt()
          _tmpRepeatWrong = _tmp_7 != 0
          val _tmpQuizTimerSeconds: Int
          _tmpQuizTimerSeconds = _stmt.getLong(_columnIndexOfQuizTimerSeconds).toInt()
          val _tmpQuestionTimerSeconds: Int
          _tmpQuestionTimerSeconds = _stmt.getLong(_columnIndexOfQuestionTimerSeconds).toInt()
          val _tmpRangeFrom: Int
          _tmpRangeFrom = _stmt.getLong(_columnIndexOfRangeFrom).toInt()
          val _tmpRangeTo: Int
          _tmpRangeTo = _stmt.getLong(_columnIndexOfRangeTo).toInt()
          val _tmpIncludeFilters: List<String>
          val _tmp_8: String
          _tmp_8 = _stmt.getText(_columnIndexOfIncludeFilters)
          _tmpIncludeFilters = __converters.toStringList(_tmp_8)
          val _tmpDroppedOptions: Map<Long, List<Int>>
          val _tmp_9: String
          _tmp_9 = _stmt.getText(_columnIndexOfDroppedOptions)
          _tmpDroppedOptions = __converters.toAnswersMap(_tmp_9)
          val _tmpDroppedOptionsByIndex: Map<Int, List<Int>>
          val _tmp_10: String
          _tmp_10 = _stmt.getText(_columnIndexOfDroppedOptionsByIndex)
          _tmpDroppedOptionsByIndex = __converters.toIntListIntMap(_tmp_10)
          val _tmpVisibleOptionsCount: Map<Long, Int>
          val _tmp_11: String
          _tmp_11 = _stmt.getText(_columnIndexOfVisibleOptionsCount)
          _tmpVisibleOptionsCount = __converters.toIntMap(_tmp_11)
          val _tmpVisibleOptionsCountByIndex: Map<Int, Int>
          val _tmp_12: String
          _tmp_12 = _stmt.getText(_columnIndexOfVisibleOptionsCountByIndex)
          _tmpVisibleOptionsCountByIndex = __converters.toIntIntMap(_tmp_12)
          val _tmpCurrentStreak: Int
          _tmpCurrentStreak = _stmt.getLong(_columnIndexOfCurrentStreak).toInt()
          val _tmpMaxStreak: Int
          _tmpMaxStreak = _stmt.getLong(_columnIndexOfMaxStreak).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          val _tmpResultTaxonomy: Map<Int, String>
          val _tmp_13: String
          _tmp_13 = _stmt.getText(_columnIndexOfResultTaxonomy)
          _tmpResultTaxonomy = __converters.toIntStringMap(_tmp_13)
          _item =
              SessionEntity(_tmpId,_tmpQuizId,_tmpLabel,_tmpCurrentQuestionIndex,_tmpScore,_tmpIncorrectCount,_tmpAnswers,_tmpAnswersByIndex,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastModifiedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpQuestionIds,_tmpOriginalQuestionCount,_tmpShuffleQuestions,_tmpShuffleOptions,_tmpRapidMode,_tmpRepeatWrong,_tmpQuizTimerSeconds,_tmpQuestionTimerSeconds,_tmpRangeFrom,_tmpRangeTo,_tmpIncludeFilters,_tmpDroppedOptions,_tmpDroppedOptionsByIndex,_tmpVisibleOptionsCount,_tmpVisibleOptionsCountByIndex,_tmpCurrentStreak,_tmpMaxStreak,_tmpDeletedAt,_tmpResultTaxonomy)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSessionsByQuizIdNow(quizId: Long): List<SessionEntity> {
    val _sql: String =
        "SELECT * FROM sessions WHERE quizId = ? AND deletedAt IS NULL ORDER BY lastModifiedAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCurrentQuestionIndex: Int = getColumnIndexOrThrow(_stmt,
            "currentQuestionIndex")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfAnswers: Int = getColumnIndexOrThrow(_stmt, "answers")
        val _columnIndexOfAnswersByIndex: Int = getColumnIndexOrThrow(_stmt, "answersByIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastModifiedAt: Int = getColumnIndexOrThrow(_stmt, "lastModifiedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfQuestionIds: Int = getColumnIndexOrThrow(_stmt, "questionIds")
        val _columnIndexOfOriginalQuestionCount: Int = getColumnIndexOrThrow(_stmt,
            "originalQuestionCount")
        val _columnIndexOfShuffleQuestions: Int = getColumnIndexOrThrow(_stmt, "shuffleQuestions")
        val _columnIndexOfShuffleOptions: Int = getColumnIndexOrThrow(_stmt, "shuffleOptions")
        val _columnIndexOfRapidMode: Int = getColumnIndexOrThrow(_stmt, "rapidMode")
        val _columnIndexOfRepeatWrong: Int = getColumnIndexOrThrow(_stmt, "repeatWrong")
        val _columnIndexOfQuizTimerSeconds: Int = getColumnIndexOrThrow(_stmt, "quizTimerSeconds")
        val _columnIndexOfQuestionTimerSeconds: Int = getColumnIndexOrThrow(_stmt,
            "questionTimerSeconds")
        val _columnIndexOfRangeFrom: Int = getColumnIndexOrThrow(_stmt, "rangeFrom")
        val _columnIndexOfRangeTo: Int = getColumnIndexOrThrow(_stmt, "rangeTo")
        val _columnIndexOfIncludeFilters: Int = getColumnIndexOrThrow(_stmt, "includeFilters")
        val _columnIndexOfDroppedOptions: Int = getColumnIndexOrThrow(_stmt, "droppedOptions")
        val _columnIndexOfDroppedOptionsByIndex: Int = getColumnIndexOrThrow(_stmt,
            "droppedOptionsByIndex")
        val _columnIndexOfVisibleOptionsCount: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCount")
        val _columnIndexOfVisibleOptionsCountByIndex: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCountByIndex")
        val _columnIndexOfCurrentStreak: Int = getColumnIndexOrThrow(_stmt, "currentStreak")
        val _columnIndexOfMaxStreak: Int = getColumnIndexOrThrow(_stmt, "maxStreak")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfResultTaxonomy: Int = getColumnIndexOrThrow(_stmt, "resultTaxonomy")
        val _result: MutableList<SessionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SessionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCurrentQuestionIndex: Int
          _tmpCurrentQuestionIndex = _stmt.getLong(_columnIndexOfCurrentQuestionIndex).toInt()
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
          val _tmpAnswers: Map<Long, List<Int>>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAnswers)
          _tmpAnswers = __converters.toAnswersMap(_tmp)
          val _tmpAnswersByIndex: Map<Int, List<Int>>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfAnswersByIndex)
          _tmpAnswersByIndex = __converters.toIntListIntMap(_tmp_1)
          val _tmpIsCompleted: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastModifiedAt: Long
          _tmpLastModifiedAt = _stmt.getLong(_columnIndexOfLastModifiedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpQuestionIds: List<Long>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfQuestionIds)
          _tmpQuestionIds = __converters.toLongList(_tmp_3)
          val _tmpOriginalQuestionCount: Int
          _tmpOriginalQuestionCount = _stmt.getLong(_columnIndexOfOriginalQuestionCount).toInt()
          val _tmpShuffleQuestions: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfShuffleQuestions).toInt()
          _tmpShuffleQuestions = _tmp_4 != 0
          val _tmpShuffleOptions: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_columnIndexOfShuffleOptions).toInt()
          _tmpShuffleOptions = _tmp_5 != 0
          val _tmpRapidMode: Boolean
          val _tmp_6: Int
          _tmp_6 = _stmt.getLong(_columnIndexOfRapidMode).toInt()
          _tmpRapidMode = _tmp_6 != 0
          val _tmpRepeatWrong: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_columnIndexOfRepeatWrong).toInt()
          _tmpRepeatWrong = _tmp_7 != 0
          val _tmpQuizTimerSeconds: Int
          _tmpQuizTimerSeconds = _stmt.getLong(_columnIndexOfQuizTimerSeconds).toInt()
          val _tmpQuestionTimerSeconds: Int
          _tmpQuestionTimerSeconds = _stmt.getLong(_columnIndexOfQuestionTimerSeconds).toInt()
          val _tmpRangeFrom: Int
          _tmpRangeFrom = _stmt.getLong(_columnIndexOfRangeFrom).toInt()
          val _tmpRangeTo: Int
          _tmpRangeTo = _stmt.getLong(_columnIndexOfRangeTo).toInt()
          val _tmpIncludeFilters: List<String>
          val _tmp_8: String
          _tmp_8 = _stmt.getText(_columnIndexOfIncludeFilters)
          _tmpIncludeFilters = __converters.toStringList(_tmp_8)
          val _tmpDroppedOptions: Map<Long, List<Int>>
          val _tmp_9: String
          _tmp_9 = _stmt.getText(_columnIndexOfDroppedOptions)
          _tmpDroppedOptions = __converters.toAnswersMap(_tmp_9)
          val _tmpDroppedOptionsByIndex: Map<Int, List<Int>>
          val _tmp_10: String
          _tmp_10 = _stmt.getText(_columnIndexOfDroppedOptionsByIndex)
          _tmpDroppedOptionsByIndex = __converters.toIntListIntMap(_tmp_10)
          val _tmpVisibleOptionsCount: Map<Long, Int>
          val _tmp_11: String
          _tmp_11 = _stmt.getText(_columnIndexOfVisibleOptionsCount)
          _tmpVisibleOptionsCount = __converters.toIntMap(_tmp_11)
          val _tmpVisibleOptionsCountByIndex: Map<Int, Int>
          val _tmp_12: String
          _tmp_12 = _stmt.getText(_columnIndexOfVisibleOptionsCountByIndex)
          _tmpVisibleOptionsCountByIndex = __converters.toIntIntMap(_tmp_12)
          val _tmpCurrentStreak: Int
          _tmpCurrentStreak = _stmt.getLong(_columnIndexOfCurrentStreak).toInt()
          val _tmpMaxStreak: Int
          _tmpMaxStreak = _stmt.getLong(_columnIndexOfMaxStreak).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          val _tmpResultTaxonomy: Map<Int, String>
          val _tmp_13: String
          _tmp_13 = _stmt.getText(_columnIndexOfResultTaxonomy)
          _tmpResultTaxonomy = __converters.toIntStringMap(_tmp_13)
          _item =
              SessionEntity(_tmpId,_tmpQuizId,_tmpLabel,_tmpCurrentQuestionIndex,_tmpScore,_tmpIncorrectCount,_tmpAnswers,_tmpAnswersByIndex,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastModifiedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpQuestionIds,_tmpOriginalQuestionCount,_tmpShuffleQuestions,_tmpShuffleOptions,_tmpRapidMode,_tmpRepeatWrong,_tmpQuizTimerSeconds,_tmpQuestionTimerSeconds,_tmpRangeFrom,_tmpRangeTo,_tmpIncludeFilters,_tmpDroppedOptions,_tmpDroppedOptionsByIndex,_tmpVisibleOptionsCount,_tmpVisibleOptionsCountByIndex,_tmpCurrentStreak,_tmpMaxStreak,_tmpDeletedAt,_tmpResultTaxonomy)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSessionById(sessionId: Long): SessionEntity? {
    val _sql: String = "SELECT * FROM sessions WHERE id = ? AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, sessionId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCurrentQuestionIndex: Int = getColumnIndexOrThrow(_stmt,
            "currentQuestionIndex")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfAnswers: Int = getColumnIndexOrThrow(_stmt, "answers")
        val _columnIndexOfAnswersByIndex: Int = getColumnIndexOrThrow(_stmt, "answersByIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastModifiedAt: Int = getColumnIndexOrThrow(_stmt, "lastModifiedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfQuestionIds: Int = getColumnIndexOrThrow(_stmt, "questionIds")
        val _columnIndexOfOriginalQuestionCount: Int = getColumnIndexOrThrow(_stmt,
            "originalQuestionCount")
        val _columnIndexOfShuffleQuestions: Int = getColumnIndexOrThrow(_stmt, "shuffleQuestions")
        val _columnIndexOfShuffleOptions: Int = getColumnIndexOrThrow(_stmt, "shuffleOptions")
        val _columnIndexOfRapidMode: Int = getColumnIndexOrThrow(_stmt, "rapidMode")
        val _columnIndexOfRepeatWrong: Int = getColumnIndexOrThrow(_stmt, "repeatWrong")
        val _columnIndexOfQuizTimerSeconds: Int = getColumnIndexOrThrow(_stmt, "quizTimerSeconds")
        val _columnIndexOfQuestionTimerSeconds: Int = getColumnIndexOrThrow(_stmt,
            "questionTimerSeconds")
        val _columnIndexOfRangeFrom: Int = getColumnIndexOrThrow(_stmt, "rangeFrom")
        val _columnIndexOfRangeTo: Int = getColumnIndexOrThrow(_stmt, "rangeTo")
        val _columnIndexOfIncludeFilters: Int = getColumnIndexOrThrow(_stmt, "includeFilters")
        val _columnIndexOfDroppedOptions: Int = getColumnIndexOrThrow(_stmt, "droppedOptions")
        val _columnIndexOfDroppedOptionsByIndex: Int = getColumnIndexOrThrow(_stmt,
            "droppedOptionsByIndex")
        val _columnIndexOfVisibleOptionsCount: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCount")
        val _columnIndexOfVisibleOptionsCountByIndex: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCountByIndex")
        val _columnIndexOfCurrentStreak: Int = getColumnIndexOrThrow(_stmt, "currentStreak")
        val _columnIndexOfMaxStreak: Int = getColumnIndexOrThrow(_stmt, "maxStreak")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfResultTaxonomy: Int = getColumnIndexOrThrow(_stmt, "resultTaxonomy")
        val _result: SessionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCurrentQuestionIndex: Int
          _tmpCurrentQuestionIndex = _stmt.getLong(_columnIndexOfCurrentQuestionIndex).toInt()
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
          val _tmpAnswers: Map<Long, List<Int>>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAnswers)
          _tmpAnswers = __converters.toAnswersMap(_tmp)
          val _tmpAnswersByIndex: Map<Int, List<Int>>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfAnswersByIndex)
          _tmpAnswersByIndex = __converters.toIntListIntMap(_tmp_1)
          val _tmpIsCompleted: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastModifiedAt: Long
          _tmpLastModifiedAt = _stmt.getLong(_columnIndexOfLastModifiedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpQuestionIds: List<Long>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfQuestionIds)
          _tmpQuestionIds = __converters.toLongList(_tmp_3)
          val _tmpOriginalQuestionCount: Int
          _tmpOriginalQuestionCount = _stmt.getLong(_columnIndexOfOriginalQuestionCount).toInt()
          val _tmpShuffleQuestions: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfShuffleQuestions).toInt()
          _tmpShuffleQuestions = _tmp_4 != 0
          val _tmpShuffleOptions: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_columnIndexOfShuffleOptions).toInt()
          _tmpShuffleOptions = _tmp_5 != 0
          val _tmpRapidMode: Boolean
          val _tmp_6: Int
          _tmp_6 = _stmt.getLong(_columnIndexOfRapidMode).toInt()
          _tmpRapidMode = _tmp_6 != 0
          val _tmpRepeatWrong: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_columnIndexOfRepeatWrong).toInt()
          _tmpRepeatWrong = _tmp_7 != 0
          val _tmpQuizTimerSeconds: Int
          _tmpQuizTimerSeconds = _stmt.getLong(_columnIndexOfQuizTimerSeconds).toInt()
          val _tmpQuestionTimerSeconds: Int
          _tmpQuestionTimerSeconds = _stmt.getLong(_columnIndexOfQuestionTimerSeconds).toInt()
          val _tmpRangeFrom: Int
          _tmpRangeFrom = _stmt.getLong(_columnIndexOfRangeFrom).toInt()
          val _tmpRangeTo: Int
          _tmpRangeTo = _stmt.getLong(_columnIndexOfRangeTo).toInt()
          val _tmpIncludeFilters: List<String>
          val _tmp_8: String
          _tmp_8 = _stmt.getText(_columnIndexOfIncludeFilters)
          _tmpIncludeFilters = __converters.toStringList(_tmp_8)
          val _tmpDroppedOptions: Map<Long, List<Int>>
          val _tmp_9: String
          _tmp_9 = _stmt.getText(_columnIndexOfDroppedOptions)
          _tmpDroppedOptions = __converters.toAnswersMap(_tmp_9)
          val _tmpDroppedOptionsByIndex: Map<Int, List<Int>>
          val _tmp_10: String
          _tmp_10 = _stmt.getText(_columnIndexOfDroppedOptionsByIndex)
          _tmpDroppedOptionsByIndex = __converters.toIntListIntMap(_tmp_10)
          val _tmpVisibleOptionsCount: Map<Long, Int>
          val _tmp_11: String
          _tmp_11 = _stmt.getText(_columnIndexOfVisibleOptionsCount)
          _tmpVisibleOptionsCount = __converters.toIntMap(_tmp_11)
          val _tmpVisibleOptionsCountByIndex: Map<Int, Int>
          val _tmp_12: String
          _tmp_12 = _stmt.getText(_columnIndexOfVisibleOptionsCountByIndex)
          _tmpVisibleOptionsCountByIndex = __converters.toIntIntMap(_tmp_12)
          val _tmpCurrentStreak: Int
          _tmpCurrentStreak = _stmt.getLong(_columnIndexOfCurrentStreak).toInt()
          val _tmpMaxStreak: Int
          _tmpMaxStreak = _stmt.getLong(_columnIndexOfMaxStreak).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          val _tmpResultTaxonomy: Map<Int, String>
          val _tmp_13: String
          _tmp_13 = _stmt.getText(_columnIndexOfResultTaxonomy)
          _tmpResultTaxonomy = __converters.toIntStringMap(_tmp_13)
          _result =
              SessionEntity(_tmpId,_tmpQuizId,_tmpLabel,_tmpCurrentQuestionIndex,_tmpScore,_tmpIncorrectCount,_tmpAnswers,_tmpAnswersByIndex,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastModifiedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpQuestionIds,_tmpOriginalQuestionCount,_tmpShuffleQuestions,_tmpShuffleOptions,_tmpRapidMode,_tmpRepeatWrong,_tmpQuizTimerSeconds,_tmpQuestionTimerSeconds,_tmpRangeFrom,_tmpRangeTo,_tmpIncludeFilters,_tmpDroppedOptions,_tmpDroppedOptionsByIndex,_tmpVisibleOptionsCount,_tmpVisibleOptionsCountByIndex,_tmpCurrentStreak,_tmpMaxStreak,_tmpDeletedAt,_tmpResultTaxonomy)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLatestSessionForQuiz(quizId: Long): Flow<SessionEntity?> {
    val _sql: String =
        "SELECT * FROM sessions WHERE quizId = ? AND deletedAt IS NULL ORDER BY lastModifiedAt DESC LIMIT 1"
    return createFlow(__db, false, arrayOf("sessions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuizId: Int = getColumnIndexOrThrow(_stmt, "quizId")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCurrentQuestionIndex: Int = getColumnIndexOrThrow(_stmt,
            "currentQuestionIndex")
        val _columnIndexOfScore: Int = getColumnIndexOrThrow(_stmt, "score")
        val _columnIndexOfIncorrectCount: Int = getColumnIndexOrThrow(_stmt, "incorrectCount")
        val _columnIndexOfAnswers: Int = getColumnIndexOrThrow(_stmt, "answers")
        val _columnIndexOfAnswersByIndex: Int = getColumnIndexOrThrow(_stmt, "answersByIndex")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfLastModifiedAt: Int = getColumnIndexOrThrow(_stmt, "lastModifiedAt")
        val _columnIndexOfLastStudiedAt: Int = getColumnIndexOrThrow(_stmt, "lastStudiedAt")
        val _columnIndexOfLastEditedAt: Int = getColumnIndexOrThrow(_stmt, "lastEditedAt")
        val _columnIndexOfQuestionIds: Int = getColumnIndexOrThrow(_stmt, "questionIds")
        val _columnIndexOfOriginalQuestionCount: Int = getColumnIndexOrThrow(_stmt,
            "originalQuestionCount")
        val _columnIndexOfShuffleQuestions: Int = getColumnIndexOrThrow(_stmt, "shuffleQuestions")
        val _columnIndexOfShuffleOptions: Int = getColumnIndexOrThrow(_stmt, "shuffleOptions")
        val _columnIndexOfRapidMode: Int = getColumnIndexOrThrow(_stmt, "rapidMode")
        val _columnIndexOfRepeatWrong: Int = getColumnIndexOrThrow(_stmt, "repeatWrong")
        val _columnIndexOfQuizTimerSeconds: Int = getColumnIndexOrThrow(_stmt, "quizTimerSeconds")
        val _columnIndexOfQuestionTimerSeconds: Int = getColumnIndexOrThrow(_stmt,
            "questionTimerSeconds")
        val _columnIndexOfRangeFrom: Int = getColumnIndexOrThrow(_stmt, "rangeFrom")
        val _columnIndexOfRangeTo: Int = getColumnIndexOrThrow(_stmt, "rangeTo")
        val _columnIndexOfIncludeFilters: Int = getColumnIndexOrThrow(_stmt, "includeFilters")
        val _columnIndexOfDroppedOptions: Int = getColumnIndexOrThrow(_stmt, "droppedOptions")
        val _columnIndexOfDroppedOptionsByIndex: Int = getColumnIndexOrThrow(_stmt,
            "droppedOptionsByIndex")
        val _columnIndexOfVisibleOptionsCount: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCount")
        val _columnIndexOfVisibleOptionsCountByIndex: Int = getColumnIndexOrThrow(_stmt,
            "visibleOptionsCountByIndex")
        val _columnIndexOfCurrentStreak: Int = getColumnIndexOrThrow(_stmt, "currentStreak")
        val _columnIndexOfMaxStreak: Int = getColumnIndexOrThrow(_stmt, "maxStreak")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfResultTaxonomy: Int = getColumnIndexOrThrow(_stmt, "resultTaxonomy")
        val _result: SessionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpQuizId: Long
          _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCurrentQuestionIndex: Int
          _tmpCurrentQuestionIndex = _stmt.getLong(_columnIndexOfCurrentQuestionIndex).toInt()
          val _tmpScore: Int
          _tmpScore = _stmt.getLong(_columnIndexOfScore).toInt()
          val _tmpIncorrectCount: Int
          _tmpIncorrectCount = _stmt.getLong(_columnIndexOfIncorrectCount).toInt()
          val _tmpAnswers: Map<Long, List<Int>>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfAnswers)
          _tmpAnswers = __converters.toAnswersMap(_tmp)
          val _tmpAnswersByIndex: Map<Int, List<Int>>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfAnswersByIndex)
          _tmpAnswersByIndex = __converters.toIntListIntMap(_tmp_1)
          val _tmpIsCompleted: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_2 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpLastModifiedAt: Long
          _tmpLastModifiedAt = _stmt.getLong(_columnIndexOfLastModifiedAt)
          val _tmpLastStudiedAt: Long
          _tmpLastStudiedAt = _stmt.getLong(_columnIndexOfLastStudiedAt)
          val _tmpLastEditedAt: Long
          _tmpLastEditedAt = _stmt.getLong(_columnIndexOfLastEditedAt)
          val _tmpQuestionIds: List<Long>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfQuestionIds)
          _tmpQuestionIds = __converters.toLongList(_tmp_3)
          val _tmpOriginalQuestionCount: Int
          _tmpOriginalQuestionCount = _stmt.getLong(_columnIndexOfOriginalQuestionCount).toInt()
          val _tmpShuffleQuestions: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfShuffleQuestions).toInt()
          _tmpShuffleQuestions = _tmp_4 != 0
          val _tmpShuffleOptions: Boolean
          val _tmp_5: Int
          _tmp_5 = _stmt.getLong(_columnIndexOfShuffleOptions).toInt()
          _tmpShuffleOptions = _tmp_5 != 0
          val _tmpRapidMode: Boolean
          val _tmp_6: Int
          _tmp_6 = _stmt.getLong(_columnIndexOfRapidMode).toInt()
          _tmpRapidMode = _tmp_6 != 0
          val _tmpRepeatWrong: Boolean
          val _tmp_7: Int
          _tmp_7 = _stmt.getLong(_columnIndexOfRepeatWrong).toInt()
          _tmpRepeatWrong = _tmp_7 != 0
          val _tmpQuizTimerSeconds: Int
          _tmpQuizTimerSeconds = _stmt.getLong(_columnIndexOfQuizTimerSeconds).toInt()
          val _tmpQuestionTimerSeconds: Int
          _tmpQuestionTimerSeconds = _stmt.getLong(_columnIndexOfQuestionTimerSeconds).toInt()
          val _tmpRangeFrom: Int
          _tmpRangeFrom = _stmt.getLong(_columnIndexOfRangeFrom).toInt()
          val _tmpRangeTo: Int
          _tmpRangeTo = _stmt.getLong(_columnIndexOfRangeTo).toInt()
          val _tmpIncludeFilters: List<String>
          val _tmp_8: String
          _tmp_8 = _stmt.getText(_columnIndexOfIncludeFilters)
          _tmpIncludeFilters = __converters.toStringList(_tmp_8)
          val _tmpDroppedOptions: Map<Long, List<Int>>
          val _tmp_9: String
          _tmp_9 = _stmt.getText(_columnIndexOfDroppedOptions)
          _tmpDroppedOptions = __converters.toAnswersMap(_tmp_9)
          val _tmpDroppedOptionsByIndex: Map<Int, List<Int>>
          val _tmp_10: String
          _tmp_10 = _stmt.getText(_columnIndexOfDroppedOptionsByIndex)
          _tmpDroppedOptionsByIndex = __converters.toIntListIntMap(_tmp_10)
          val _tmpVisibleOptionsCount: Map<Long, Int>
          val _tmp_11: String
          _tmp_11 = _stmt.getText(_columnIndexOfVisibleOptionsCount)
          _tmpVisibleOptionsCount = __converters.toIntMap(_tmp_11)
          val _tmpVisibleOptionsCountByIndex: Map<Int, Int>
          val _tmp_12: String
          _tmp_12 = _stmt.getText(_columnIndexOfVisibleOptionsCountByIndex)
          _tmpVisibleOptionsCountByIndex = __converters.toIntIntMap(_tmp_12)
          val _tmpCurrentStreak: Int
          _tmpCurrentStreak = _stmt.getLong(_columnIndexOfCurrentStreak).toInt()
          val _tmpMaxStreak: Int
          _tmpMaxStreak = _stmt.getLong(_columnIndexOfMaxStreak).toInt()
          val _tmpDeletedAt: Long?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          }
          val _tmpResultTaxonomy: Map<Int, String>
          val _tmp_13: String
          _tmp_13 = _stmt.getText(_columnIndexOfResultTaxonomy)
          _tmpResultTaxonomy = __converters.toIntStringMap(_tmp_13)
          _result =
              SessionEntity(_tmpId,_tmpQuizId,_tmpLabel,_tmpCurrentQuestionIndex,_tmpScore,_tmpIncorrectCount,_tmpAnswers,_tmpAnswersByIndex,_tmpIsCompleted,_tmpCreatedAt,_tmpUpdatedAt,_tmpLastModifiedAt,_tmpLastStudiedAt,_tmpLastEditedAt,_tmpQuestionIds,_tmpOriginalQuestionCount,_tmpShuffleQuestions,_tmpShuffleOptions,_tmpRapidMode,_tmpRepeatWrong,_tmpQuizTimerSeconds,_tmpQuestionTimerSeconds,_tmpRangeFrom,_tmpRangeTo,_tmpIncludeFilters,_tmpDroppedOptions,_tmpDroppedOptionsByIndex,_tmpVisibleOptionsCount,_tmpVisibleOptionsCountByIndex,_tmpCurrentStreak,_tmpMaxStreak,_tmpDeletedAt,_tmpResultTaxonomy)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSessionById(sessionId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE sessions SET deletedAt = ?, updatedAt = ?, lastModifiedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 4
        _stmt.bindLong(_argIndex, sessionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun softDeleteSessionsByQuizId(quizId: Long, deletedAt: Long) {
    val _sql: String =
        "UPDATE sessions SET deletedAt = ?, updatedAt = ?, lastModifiedAt = ? WHERE quizId = ? AND deletedAt IS NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, deletedAt)
        _argIndex = 4
        _stmt.bindLong(_argIndex, quizId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreSessionById(sessionId: Long, updatedAt: Long) {
    val _sql: String =
        "UPDATE sessions SET deletedAt = NULL, updatedAt = ?, lastModifiedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, sessionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun restoreSessionsByQuizId(
    quizId: Long,
    updatedAt: Long,
    deletedAtFilter: Long,
  ) {
    val _sql: String =
        "UPDATE sessions SET deletedAt = NULL, updatedAt = ?, lastModifiedAt = ? WHERE quizId = ? AND deletedAt = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 2
        _stmt.bindLong(_argIndex, updatedAt)
        _argIndex = 3
        _stmt.bindLong(_argIndex, quizId)
        _argIndex = 4
        _stmt.bindLong(_argIndex, deletedAtFilter)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun hardDeleteSessionsByQuizId(quizId: Long) {
    val _sql: String = "DELETE FROM sessions WHERE quizId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, quizId)
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
