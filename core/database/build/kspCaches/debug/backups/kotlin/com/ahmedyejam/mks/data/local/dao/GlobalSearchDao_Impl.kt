package com.ahmedyejam.mks.`data`.local.dao

import androidx.room.RoomDatabase
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.ahmedyejam.mks.`data`.search.GlobalSearchResultRow
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GlobalSearchDao_Impl(
  __db: RoomDatabase,
) : GlobalSearchDao {
  private val __db: RoomDatabase
  init {
    this.__db = __db
  }

  public override suspend fun search(likeQuery: String, limit: Int): List<GlobalSearchResultRow> {
    val _sql: String = """
        |
        |        SELECT CAST(b.id AS TEXT) AS id, 'BOOK' AS type, b.title AS title, b.description AS subtitle, NULL AS snippet, b.id AS bookId, NULL AS quizId, NULL AS questionId, NULL AS parentId, b.updatedAt AS updatedAt
        |        FROM books b JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE b.deletedAt IS NULL AND w.deletedAt IS NULL AND (b.title LIKE ? OR b.description LIKE ?)
        |        UNION ALL
        |        SELECT CAST(q.id AS TEXT), 'QUIZ', q.title, q.description, q.category, q.bookId, q.id, NULL, NULL, q.updatedAt
        |        FROM quizzes q JOIN books b ON b.id = q.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (q.title LIKE ? OR q.description LIKE ? OR q.category LIKE ?)
        |        UNION ALL
        |        SELECT CAST(q.id AS TEXT), 'QUESTION', q.text, z.title, q.explanation, z.bookId, q.quizId, q.id, NULL, q.updatedAt
        |        FROM questions q JOIN quizzes z ON z.id = q.quizId JOIN books b ON b.id = z.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL AND z.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (q.text LIKE ? OR q.reference LIKE ? OR q.notes LIKE ? OR q.additionalInfo LIKE ? OR q.options LIKE ? OR q.categories LIKE ?)
        |        UNION ALL
        |        SELECT CAST(q.id AS TEXT), 'EXPLANATION', q.text, z.title, q.explanation, z.bookId, q.quizId, q.id, NULL, q.updatedAt
        |        FROM questions q JOIN quizzes z ON z.id = q.quizId JOIN books b ON b.id = z.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE q.deletedAt IS NULL AND z.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (q.explanation LIKE ? OR q.hint LIKE ?)
        |        UNION ALL
        |        SELECT CAST(a.id AS TEXT), 'ASSET', a.title, a.description, COALESCE(a.textContent, a.externalUrl, a.localPath), a.bookId, a.quizId, a.questionId, NULL, a.updatedAt
        |        FROM question_assets a JOIN books b ON b.id = a.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE a.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (a.title LIKE ? OR a.description LIKE ? OR a.textContent LIKE ? OR a.externalUrl LIKE ? OR a.sourceQuote LIKE ?)
        |        UNION ALL
        |        SELECT CAST(s.id AS TEXT), 'SOURCE', s.title, s.author, COALESCE(s.description, s.externalUrl, s.localPath), s.bookId, NULL, NULL, NULL, s.updatedAt
        |        FROM source_documents s JOIN books b ON b.id = s.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE s.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (s.title LIKE ? OR s.author LIKE ? OR s.description LIKE ? OR s.externalUrl LIKE ?)
        |        UNION ALL
        |        SELECT CAST(f.id AS TEXT), 'FLASHCARD', f.frontText, d.title, f.backText, d.bookId, NULL, f.sourceQuestionId, d.id, f.updatedAt
        |        FROM flashcards f JOIN flashcard_decks d ON d.id = f.deckId JOIN books b ON b.id = d.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE f.deletedAt IS NULL AND d.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (f.frontText LIKE ? OR f.backText LIKE ? OR f.hint LIKE ? OR f.tags LIKE ?)
        |        UNION ALL
        |        SELECT CAST(bp.id AS TEXT), 'BLUEPRINT', bp.title, bp.summary, bp.body, c.bookId, NULL, bp.sourceQuestionId, NULL, bp.updatedAt
        |        FROM note_blueprints bp JOIN note_collections c ON c.id = bp.collectionId JOIN books bk ON bk.id = c.bookId JOIN workspaces w ON bk.workspaceId = w.id
        |        WHERE bp.deletedAt IS NULL AND c.deletedAt IS NULL AND bk.deletedAt IS NULL AND w.deletedAt IS NULL AND (bp.title LIKE ? OR bp.summary LIKE ? OR bp.body LIKE ? OR bp.bulletPoints LIKE ? OR bp.tags LIKE ?)
        |        UNION ALL
        |        SELECT CAST(s.id AS TEXT), 'SLIDE', s.title, c.title, s.body, c.bookId, NULL, s.sourceQuestionId, c.id, s.updatedAt
        |        FROM course_slides s JOIN slideshow_courses c ON c.id = s.courseId JOIN books b ON b.id = c.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE s.deletedAt IS NULL AND c.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (s.title LIKE ? OR s.body LIKE ? OR s.speakerNotes LIKE ?)
        |        UNION ALL
        |        SELECT CAST(p.id AS TEXT), 'PROMPT_DECK', p.title, p.description, p.tags, p.bookId, NULL, NULL, NULL, p.updatedAt
        |        FROM prompt_decks p JOIN books b ON b.id = p.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE p.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (p.title LIKE ? OR p.description LIKE ? OR p.tags LIKE ?)
        |        UNION ALL
        |        SELECT CAST(c.id AS TEXT), 'PROMPT_CARD', c.title, d.title, c.promptText, d.bookId, NULL, NULL, d.id, c.updatedAt
        |        FROM prompt_cards c JOIN prompt_decks d ON d.id = c.deckId JOIN books b ON b.id = d.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE c.deletedAt IS NULL AND d.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (c.title LIKE ? OR c.promptText LIKE ? OR c.variablesJson LIKE ?)
        |        UNION ALL
        |        SELECT CAST(r.id AS TEXT), 'PROMPT_RUN', c.title, d.title, COALESCE(r.outputText, r.renderedPrompt), d.bookId, NULL, NULL, d.id, r.createdAt
        |        FROM prompt_runs r JOIN prompt_cards c ON c.id = r.promptCardId JOIN prompt_decks d ON d.id = c.deckId JOIN books b ON b.id = d.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE r.deletedAt IS NULL AND c.deletedAt IS NULL AND d.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (r.renderedPrompt LIKE ? OR r.outputText LIKE ? OR r.inputValuesJson LIKE ?)
        |        UNION ALL
        |        SELECT CAST(m.id AS TEXT), 'MISTAKE', COALESCE(m.correctConcept, q.text), m.userReason, COALESCE(m.preventionNote, m.selectedAnswer, m.correctAnswer), m.bookId, m.quizId, m.questionId, NULL, m.updatedAt
        |        FROM mistake_log_entries m JOIN questions q ON q.id = m.questionId JOIN books b ON b.id = m.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE m.deletedAt IS NULL AND q.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (m.userReason LIKE ? OR m.correctConcept LIKE ? OR m.preventionNote LIKE ? OR m.selectedAnswer LIKE ? OR m.correctAnswer LIKE ? OR q.text LIKE ?)
        |        UNION ALL
        |        SELECT CAST(a.id AS TEXT), 'ANNOTATION', COALESCE(a.noteBody, a.selectedText, a.colorLabel), a.ownerType, a.selectedText, a.bookId, NULL, NULL, a.ownerId, a.updatedAt
        |        FROM annotations a JOIN books b ON b.id = a.bookId JOIN workspaces w ON b.workspaceId = w.id
        |        WHERE a.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (a.noteBody LIKE ? OR a.selectedText LIKE ? OR a.colorLabel LIKE ? OR a.ownerType LIKE ?)
        |        LIMIT ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 2
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 3
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 4
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 5
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 6
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 7
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 8
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 9
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 10
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 11
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 12
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 13
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 14
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 15
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 16
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 17
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 18
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 19
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 20
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 21
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 22
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 23
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 24
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 25
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 26
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 27
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 28
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 29
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 30
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 31
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 32
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 33
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 34
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 35
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 36
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 37
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 38
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 39
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 40
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 41
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 42
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 43
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 44
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 45
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 46
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 47
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 48
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 49
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 50
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 51
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 52
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 53
        _stmt.bindText(_argIndex, likeQuery)
        _argIndex = 54
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = 0
        val _columnIndexOfType: Int = 1
        val _columnIndexOfTitle: Int = 2
        val _columnIndexOfSubtitle: Int = 3
        val _columnIndexOfSnippet: Int = 4
        val _columnIndexOfBookId: Int = 5
        val _columnIndexOfQuizId: Int = 6
        val _columnIndexOfQuestionId: Int = 7
        val _columnIndexOfParentId: Int = 8
        val _columnIndexOfUpdatedAt: Int = 9
        val _result: MutableList<GlobalSearchResultRow> = mutableListOf()
        while (_stmt.step()) {
          val _item: GlobalSearchResultRow
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpSubtitle: String?
          if (_stmt.isNull(_columnIndexOfSubtitle)) {
            _tmpSubtitle = null
          } else {
            _tmpSubtitle = _stmt.getText(_columnIndexOfSubtitle)
          }
          val _tmpSnippet: String?
          if (_stmt.isNull(_columnIndexOfSnippet)) {
            _tmpSnippet = null
          } else {
            _tmpSnippet = _stmt.getText(_columnIndexOfSnippet)
          }
          val _tmpBookId: Long?
          if (_stmt.isNull(_columnIndexOfBookId)) {
            _tmpBookId = null
          } else {
            _tmpBookId = _stmt.getLong(_columnIndexOfBookId)
          }
          val _tmpQuizId: Long?
          if (_stmt.isNull(_columnIndexOfQuizId)) {
            _tmpQuizId = null
          } else {
            _tmpQuizId = _stmt.getLong(_columnIndexOfQuizId)
          }
          val _tmpQuestionId: Long?
          if (_stmt.isNull(_columnIndexOfQuestionId)) {
            _tmpQuestionId = null
          } else {
            _tmpQuestionId = _stmt.getLong(_columnIndexOfQuestionId)
          }
          val _tmpParentId: Long?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getLong(_columnIndexOfParentId)
          }
          val _tmpUpdatedAt: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmpUpdatedAt = null
          } else {
            _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          _item =
              GlobalSearchResultRow(_tmpId,_tmpType,_tmpTitle,_tmpSubtitle,_tmpSnippet,_tmpBookId,_tmpQuizId,_tmpQuestionId,_tmpParentId,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
