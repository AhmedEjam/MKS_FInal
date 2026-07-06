package com.ahmedyejam.mks.data.import.normalization

import com.ahmedyejam.mks.data.import.dto.LibraryBundleDto
import com.ahmedyejam.mks.data.import.dto.OptionDto
import com.ahmedyejam.mks.data.import.dto.QuestionDto

class BundleNormalizer {
    fun normalize(bundle: LibraryBundleDto): LibraryBundleDto {
        return bundle.copy(
            books = bundle.books.map { it.copy(title = it.title.trim(), note = it.note.trim(), coverImage = it.coverImage.trim()) },
            quizzes = bundle.quizzes.map { quiz ->
                quiz.copy(
                    title = quiz.title.trim(),
                    note = quiz.note.trim(),
                    coverImage = quiz.coverImage.trim(),
                    questions = quiz.questions.map { normalizeQuestion(it) }
                )
            }
        )
    }

    private fun normalizeQuestion(question: QuestionDto): QuestionDto {
        val normalizedOptions = question.options.map { OptionDto(it.id.trim(), it.text.trim()) }.filter { it.id.isNotBlank() || it.text.isNotBlank() }
        val normalizedCorrect = question.correct.map { it.trim() }.filter { it.isNotBlank() }.distinct()
        val inferredAnswerMode = if (normalizedCorrect.size > 1) "multiple" else "single"
        return question.copy(
            answerMode = inferredAnswerMode,
            stem = question.stem.trim(),
            options = normalizedOptions,
            correct = normalizedCorrect,
            explanation = question.explanation.trim(),
            hint = question.hint.trim(),
            reference = question.reference.trim(),
            imageDataUrl = question.imageDataUrl.trim(),
            imageSource = question.imageSource.trim(),
            imageName = question.imageName.trim(),
            categories = question.categories.map { it.trim() }.filter { it.isNotBlank() }.distinct(),
            additionalInfo = question.additionalInfo.trim()
        )
    }
}
