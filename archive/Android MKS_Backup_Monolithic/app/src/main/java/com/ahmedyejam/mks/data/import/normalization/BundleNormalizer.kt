package com.ahmedyejam.mks.data.import.normalization

import com.ahmedyejam.mks.data.import.dto.LibraryBundleDto
import com.ahmedyejam.mks.data.import.dto.QuestionDto

class BundleNormalizer {
    fun normalize(bundle: LibraryBundleDto): LibraryBundleDto {
        return bundle.copy(
            quizzes = bundle.quizzes.map { quiz ->
                quiz.copy(
                    questions = quiz.questions.map { normalizeQuestion(it) }
                )
            }
        )
    }

    private fun normalizeQuestion(question: QuestionDto): QuestionDto {
        val inferredAnswerMode = when {
            question.answerMode != null -> question.answerMode
            question.correct.size > 1 -> "multiple"
            else -> "single"
        }

        return question.copy(
            answerMode = inferredAnswerMode,
            stem = question.stem.trim(),
            explanation = question.explanation.trim(),
            hint = question.hint.trim(),
            reference = question.reference.trim()
        )
    }
}
