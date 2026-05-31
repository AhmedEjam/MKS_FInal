package com.ahmedyejam.mks.data.import.mapping

import com.ahmedyejam.mks.data.import.dto.*
import com.ahmedyejam.mks.data.local.entity.*

class LibraryMapper {

    fun mapToBookEntity(dto: BookDto, coverPath: String?): BookEntity {
        return BookEntity(
            externalId = dto.id,
            title = dto.title,
            description = dto.note,
            iconName = dto.coverIcon,
            coverImage = coverPath,
            createdAt = dto.createdAt ?: System.currentTimeMillis(),
            updatedAt = dto.updatedAt ?: System.currentTimeMillis(),
            contentUpdatedAt = dto.contentUpdatedAt ?: System.currentTimeMillis(),
            lastStudiedAt = dto.lastStudiedAt ?: 0
        )
    }

    fun mapToQuizEntity(dto: QuizDto, localBookId: Long, coverPath: String?): QuizEntity {
        return QuizEntity(
            externalId = dto.id,
            bookId = localBookId,
            title = dto.title,
            description = dto.note,
            category = dto.storageKey,
            iconName = dto.coverIcon,
            coverImage = coverPath,
            createdAt = dto.createdAt ?: System.currentTimeMillis(),
            updatedAt = dto.updatedAt ?: System.currentTimeMillis(),
            contentUpdatedAt = dto.contentUpdatedAt ?: System.currentTimeMillis(),
            lastStudiedAt = dto.lastStudiedAt ?: 0
        )
    }

    fun mapToQuestionEntity(dto: QuestionDto, localQuizId: Long, imagePath: String?): QuestionEntity {
        val options = dto.options.map { it.text.trim() }
        val correctAnswers = dto.correct.mapNotNull { correctId ->
            dto.options.indexOfFirst { it.id == correctId }.takeIf { it >= 0 }
        }.distinct().sorted()
        val type = if (correctAnswers.size > 1) QuestionType.MULTIPLE_CHOICE else QuestionType.SINGLE_CHOICE

        val rawSource = dto.imageSource.ifBlank { dto.imageDataUrl }
        val sanitizedSource = rawSource
            ?.takeIf { it.isNotBlank() }
            ?.takeUnless { it.startsWith("data:") || it.length > 1000 }

        return QuestionEntity(
            externalId = dto.id,
            quizId = localQuizId,
            text = dto.stem.trim(),
            type = type,
            options = options,
            correctAnswers = correctAnswers,
            explanation = dto.explanation.trim(),
            hint = dto.hint.trim(),
            reference = dto.reference.trim(),
            imagePath = imagePath ?: dto.imageName.takeIf { it.isNotBlank() },
            imageName = dto.imageName.takeIf { it.isNotBlank() },
            imageSource = sanitizedSource,
            categories = dto.categories.map { it.trim() }.filter { it.isNotBlank() }.distinct(),
            additionalInfo = dto.additionalInfo.trim().ifBlank { null },
            sourceBookId = dto.sourceBookId.takeIf { it.isNotBlank() },
            sourceQuizId = dto.sourceQuizId.takeIf { it.isNotBlank() },
            sourceQuestionId = dto.sourceQuestionId.takeIf { it.isNotBlank() }
        )
    }

    fun mapToSessionEntity(dto: SessionDto, localQuizId: Long, questionIdMap: Map<String, Long>): SessionEntity {
        // Map answer indices from external IDs to local indices
        val mappedAnswers = dto.answers.mapNotNull { (qExtId, optIndices) ->
            questionIdMap[qExtId]?.let { it to optIndices }
        }.toMap()

        val mappedDroppedOptions = dto.droppedOptions?.mapNotNull { (qExtId, optIndices) ->
            questionIdMap[qExtId]?.let { it to optIndices }
        }?.toMap() ?: emptyMap()

        val mappedVisibleOptionsCount = dto.visibleOptionsCount?.mapNotNull { (qExtId, count) ->
            questionIdMap[qExtId]?.let { it to count }
        }?.toMap() ?: emptyMap()

        val mappedSessionNotes = dto.sessionNotes?.mapNotNull { (qExtId, note) ->
            questionIdMap[qExtId]?.let { it to note }
        }?.toMap() ?: emptyMap()

        return SessionEntity(
            quizId = localQuizId,
            label = dto.label ?: "Imported Session",
            currentQuestionIndex = dto.cursor ?: 0,
            score = dto.score,
            incorrectCount = dto.incorrectCount,
            answers = mappedAnswers,
            isCompleted = dto.finishedAt != null,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            lastModifiedAt = dto.lastModifiedAt ?: dto.updatedAt,
            lastStudiedAt = dto.lastStudiedAt ?: 0,
            shuffleQuestions = dto.shuffleQuestions ?: true,
            shuffleOptions = dto.shuffleOptions ?: true,
            rapidMode = dto.rapidMode ?: false,
            repeatWrong = dto.repeatWrong ?: true,
            quizTimerSeconds = dto.quizTimerSeconds ?: 0,
            questionTimerSeconds = dto.questionTimerSeconds ?: 0,
            rangeFrom = dto.rangeFrom ?: 0,
            rangeTo = dto.rangeTo ?: -1,
            includeFilters = dto.includeFilters ?: emptyList(),
            sessionNotes = mappedSessionNotes,
            droppedOptions = mappedDroppedOptions,
            visibleOptionsCount = mappedVisibleOptionsCount
        )
    }

    fun mapToCategoryMetadataEntity(dto: CategoryMetadataDto): CategoryMetadataEntity {
        return CategoryMetadataEntity(
            name = dto.name,
            emoji = dto.emoji,
            color = dto.color,
            isPinned = dto.isPinned
        )
    }

    fun mapToBookDto(entity: BookEntity): BookDto {
        return BookDto(
            id = entity.externalId,
            title = entity.title,
            note = entity.description,
            coverIcon = entity.iconName ?: "📚",
            coverImage = entity.coverImage ?: "",
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            contentUpdatedAt = entity.contentUpdatedAt,
            lastStudiedAt = entity.lastStudiedAt
        )
    }

    fun mapToQuizDto(entity: QuizEntity, questions: List<QuestionEntity>): QuizDto {
        return QuizDto(
            id = entity.externalId,
            storageKey = entity.category,
            bookId = "", // Should be set by caller if needed
            title = entity.title,
            note = entity.description,
            coverIcon = entity.iconName ?: "🎯",
            coverImage = entity.coverImage ?: "",
            createdAt = entity.createdAt,
            contentUpdatedAt = entity.contentUpdatedAt,
            updatedAt = entity.updatedAt,
            lastStudiedAt = entity.lastStudiedAt,
            questions = questions.map { mapToQuestionDto(it) }
        )
    }

    fun mapToQuestionDto(entity: QuestionEntity): QuestionDto {
        return QuestionDto(
            id = entity.externalId,
            stem = entity.text,
            options = entity.options.mapIndexed { i, opt -> OptionDto(id = "opt_$i", text = opt) },
            correct = entity.correctAnswers.map { "opt_$it" },
            explanation = entity.explanation ?: "",
            hint = entity.hint ?: "",
            reference = entity.reference ?: "",
            imageDataUrl = entity.imagePath ?: "",
            imageSource = entity.imageSource ?: "",
            imageName = entity.imageName ?: "",
            categories = entity.categories,
            answerMode = if (entity.type == QuestionType.MULTIPLE_CHOICE) "multiple" else "single",
            sourceQuizId = entity.sourceQuizId ?: "",
            sourceQuestionId = entity.sourceQuestionId ?: "",
            sourceBookId = entity.sourceBookId ?: "",
            additionalInfo = entity.additionalInfo ?: ""
        )
    }

    fun mapToSessionDto(entity: SessionEntity, quizExternalId: String, questions: List<QuestionEntity>): SessionDto {
        val questionIdMap = questions.associate { it.id to it.externalId }

        val mappedAnswers = entity.answers.mapNotNull { (qId, optIndices) ->
            questionIdMap[qId]?.let { it to optIndices }
        }.toMap()

        val mappedDroppedOptions = entity.droppedOptions.mapNotNull { (qId, optIndices) ->
            questionIdMap[qId]?.let { it to optIndices }
        }.toMap()

        val mappedVisibleOptionsCount = entity.visibleOptionsCount.mapNotNull { (qId, count) ->
            questionIdMap[qId]?.let { it to count }
        }.toMap()

        val mappedSessionNotes = entity.sessionNotes.mapNotNull { (qId, note) ->
            questionIdMap[qId]?.let { it to note }
        }.toMap()

        return SessionDto(
            id = "session_${entity.id}",
            quizId = quizExternalId,
            label = entity.label,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            finishedAt = if (entity.isCompleted) entity.lastModifiedAt else null,
            cursor = entity.currentQuestionIndex,
            score = entity.score,
            incorrectCount = entity.incorrectCount,
            answers = mappedAnswers,
            lastModifiedAt = entity.lastModifiedAt,
            lastStudiedAt = entity.lastStudiedAt,
            lastEditedAt = entity.lastModifiedAt,
            shuffleQuestions = entity.shuffleQuestions,
            shuffleOptions = entity.shuffleOptions,
            rapidMode = entity.rapidMode,
            repeatWrong = entity.repeatWrong,
            quizTimerSeconds = entity.quizTimerSeconds,
            questionTimerSeconds = entity.questionTimerSeconds,
            rangeFrom = entity.rangeFrom,
            rangeTo = entity.rangeTo,
            includeFilters = entity.includeFilters,
            sessionNotes = mappedSessionNotes,
            droppedOptions = mappedDroppedOptions,
            visibleOptionsCount = mappedVisibleOptionsCount
        )
    }

    fun mapToCategoryMetadataDto(entity: CategoryMetadataEntity): CategoryMetadataDto {
        return CategoryMetadataDto(
            name = entity.name,
            emoji = entity.emoji,
            color = entity.color,
            isPinned = entity.isPinned
        )
    }
}
