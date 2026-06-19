package com.ahmedyejam.mks.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ahmedyejam.mks.data.search.GlobalSearchResultRow

@Dao
interface GlobalSearchDao {
    @Query("""
        SELECT CAST(b.id AS TEXT) AS id, 'BOOK' AS type, b.title AS title, b.description AS subtitle, NULL AS snippet, b.id AS bookId, NULL AS quizId, NULL AS questionId, NULL AS parentId, b.updatedAt AS updatedAt
        FROM books b JOIN workspaces w ON b.workspaceId = w.id
        WHERE b.deletedAt IS NULL AND w.deletedAt IS NULL AND (b.title LIKE :likeQuery OR b.description LIKE :likeQuery)
        UNION ALL
        SELECT CAST(q.id AS TEXT), 'QUIZ', q.title, q.description, q.category, q.bookId, q.id, NULL, NULL, q.updatedAt
        FROM quizzes q JOIN books b ON b.id = q.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (q.title LIKE :likeQuery OR q.description LIKE :likeQuery OR q.category LIKE :likeQuery)
        UNION ALL
        SELECT CAST(q.id AS TEXT), 'QUESTION', q.text, z.title, q.explanation, z.bookId, q.quizId, q.id, NULL, q.updatedAt
        FROM questions q JOIN quizzes z ON z.id = q.quizId JOIN books b ON b.id = z.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL AND z.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (q.text LIKE :likeQuery OR q.reference LIKE :likeQuery OR q.notes LIKE :likeQuery OR q.additionalInfo LIKE :likeQuery OR q.options LIKE :likeQuery OR q.categories LIKE :likeQuery)
        UNION ALL
        SELECT CAST(q.id AS TEXT), 'EXPLANATION', q.text, z.title, q.explanation, z.bookId, q.quizId, q.id, NULL, q.updatedAt
        FROM questions q JOIN quizzes z ON z.id = q.quizId JOIN books b ON b.id = z.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE q.deletedAt IS NULL AND z.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (q.explanation LIKE :likeQuery OR q.hint LIKE :likeQuery)
        UNION ALL
        SELECT CAST(a.id AS TEXT), 'ASSET', a.title, a.description, COALESCE(a.textContent, a.externalUrl, a.localPath), a.bookId, a.quizId, a.questionId, NULL, a.updatedAt
        FROM question_assets a JOIN books b ON b.id = a.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE a.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (a.title LIKE :likeQuery OR a.description LIKE :likeQuery OR a.textContent LIKE :likeQuery OR a.externalUrl LIKE :likeQuery OR a.sourceQuote LIKE :likeQuery)
        UNION ALL
        SELECT CAST(s.id AS TEXT), 'SOURCE', s.title, s.author, COALESCE(s.description, s.externalUrl, s.localPath), s.bookId, NULL, NULL, NULL, s.updatedAt
        FROM source_documents s JOIN books b ON b.id = s.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE s.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (s.title LIKE :likeQuery OR s.author LIKE :likeQuery OR s.description LIKE :likeQuery OR s.externalUrl LIKE :likeQuery)
        UNION ALL
        SELECT CAST(f.id AS TEXT), 'FLASHCARD', f.frontText, d.title, f.backText, d.bookId, NULL, f.sourceQuestionId, d.id, f.updatedAt
        FROM flashcards f JOIN flashcard_decks d ON d.id = f.deckId JOIN books b ON b.id = d.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE f.deletedAt IS NULL AND d.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (f.frontText LIKE :likeQuery OR f.backText LIKE :likeQuery OR f.hint LIKE :likeQuery OR f.tags LIKE :likeQuery)
        UNION ALL
        SELECT CAST(bp.id AS TEXT), 'BLUEPRINT', bp.title, bp.summary, bp.body, c.bookId, NULL, bp.sourceQuestionId, NULL, bp.updatedAt
        FROM note_blueprints bp JOIN note_collections c ON c.id = bp.collectionId JOIN books bk ON bk.id = c.bookId JOIN workspaces w ON bk.workspaceId = w.id
        WHERE bp.deletedAt IS NULL AND c.deletedAt IS NULL AND bk.deletedAt IS NULL AND w.deletedAt IS NULL AND (bp.title LIKE :likeQuery OR bp.summary LIKE :likeQuery OR bp.body LIKE :likeQuery OR bp.bulletPoints LIKE :likeQuery OR bp.tags LIKE :likeQuery)
        UNION ALL
        SELECT CAST(s.id AS TEXT), 'SLIDE', s.title, c.title, s.body, c.bookId, NULL, s.sourceQuestionId, c.id, s.updatedAt
        FROM course_slides s JOIN slideshow_courses c ON c.id = s.courseId JOIN books b ON b.id = c.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE s.deletedAt IS NULL AND c.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (s.title LIKE :likeQuery OR s.body LIKE :likeQuery OR s.speakerNotes LIKE :likeQuery)
        UNION ALL
        SELECT CAST(p.id AS TEXT), 'PROMPT_DECK', p.title, p.description, p.tags, p.bookId, NULL, NULL, NULL, p.updatedAt
        FROM prompt_decks p JOIN books b ON b.id = p.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE p.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (p.title LIKE :likeQuery OR p.description LIKE :likeQuery OR p.tags LIKE :likeQuery)
        UNION ALL
        SELECT CAST(c.id AS TEXT), 'PROMPT_CARD', c.title, d.title, c.promptText, d.bookId, NULL, NULL, d.id, c.updatedAt
        FROM prompt_cards c JOIN prompt_decks d ON d.id = c.deckId JOIN books b ON b.id = d.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE c.deletedAt IS NULL AND d.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (c.title LIKE :likeQuery OR c.promptText LIKE :likeQuery OR c.variablesJson LIKE :likeQuery)
        UNION ALL
        SELECT CAST(r.id AS TEXT), 'PROMPT_RUN', c.title, d.title, COALESCE(r.outputText, r.renderedPrompt), d.bookId, NULL, NULL, d.id, r.createdAt
        FROM prompt_runs r JOIN prompt_cards c ON c.id = r.promptCardId JOIN prompt_decks d ON d.id = c.deckId JOIN books b ON b.id = d.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE r.deletedAt IS NULL AND c.deletedAt IS NULL AND d.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (r.renderedPrompt LIKE :likeQuery OR r.outputText LIKE :likeQuery OR r.inputValuesJson LIKE :likeQuery)
        UNION ALL
        SELECT CAST(m.id AS TEXT), 'MISTAKE', COALESCE(m.correctConcept, q.text), m.userReason, COALESCE(m.preventionNote, m.selectedAnswer, m.correctAnswer), m.bookId, m.quizId, m.questionId, NULL, m.updatedAt
        FROM mistake_log_entries m JOIN questions q ON q.id = m.questionId JOIN books b ON b.id = m.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE m.deletedAt IS NULL AND q.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (m.userReason LIKE :likeQuery OR m.correctConcept LIKE :likeQuery OR m.preventionNote LIKE :likeQuery OR m.selectedAnswer LIKE :likeQuery OR m.correctAnswer LIKE :likeQuery OR q.text LIKE :likeQuery)
        UNION ALL
        SELECT CAST(a.id AS TEXT), 'ANNOTATION', COALESCE(a.noteBody, a.selectedText, a.colorLabel), a.ownerType, a.selectedText, a.bookId, NULL, NULL, a.ownerId, a.updatedAt
        FROM annotations a JOIN books b ON b.id = a.bookId JOIN workspaces w ON b.workspaceId = w.id
        WHERE a.deletedAt IS NULL AND b.deletedAt IS NULL AND w.deletedAt IS NULL AND (a.noteBody LIKE :likeQuery OR a.selectedText LIKE :likeQuery OR a.colorLabel LIKE :likeQuery OR a.ownerType LIKE :likeQuery)
        LIMIT :limit
    """)
    suspend fun search(likeQuery: String, limit: Int = 120): List<GlobalSearchResultRow>
}
