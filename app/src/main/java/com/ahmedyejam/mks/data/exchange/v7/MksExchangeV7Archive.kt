package com.ahmedyejam.mks.data.exchange.v7

import com.ahmedyejam.mks.data.import.dto.BookDto
import com.ahmedyejam.mks.data.import.dto.CategoryMetadataDto
import com.ahmedyejam.mks.data.import.dto.LibraryBundleDto
import com.ahmedyejam.mks.data.import.dto.OptionDto
import com.ahmedyejam.mks.data.import.dto.QuestionDto
import com.ahmedyejam.mks.data.import.dto.QuizDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.OutputStream
import java.security.MessageDigest
import java.util.Base64
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Stage 4C Android bridge for the iOS V04 schema-7 exchange archive.
 *
 * This object deliberately avoids changing the Room schema. It converts between:
 * - Android's existing LibraryBundleDto import/export surface, and
 * - the split schema-7 archive used by the iOS V04 exchange implementation.
 *
 * Full DAO-backed Android import/export can be wired after this contract bridge is accepted.
 */
object MksExchangeV7Archive {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        coerceInputValues = true
        isLenient = true
    }

    private data class MediaPayload(
        val manifestFile: MksExchangeV7MediaFile,
        val bytes: ByteArray,
        val source: String
    )

    fun isSchema7Manifest(manifestFile: File): Boolean {
        if (!manifestFile.exists()) return false
        return runCatching {
            val root = json.parseToJsonElement(manifestFile.readText()) as? JsonObject ?: return false
            root["format"]?.jsonPrimitive?.content == MksExchangeV7Paths.FORMAT &&
                root["schemaVersion"]?.jsonPrimitive?.content?.toIntOrNull() == MksExchangeV7Paths.SCHEMA_VERSION
        }.getOrDefault(false)
    }

    fun readLegacyBundleFromDirectory(rootDir: File): LibraryBundleDto {
        val workspace = readOrDefault(rootDir, MksExchangeV7Paths.WORKSPACE, MksExchangeV7WorkspaceEnvelope.serializer(), MksExchangeV7WorkspaceEnvelope())
        val books = readList(rootDir, MksExchangeV7Paths.BOOKS, MksExchangeV7Book.serializer())
        val quizzes = readList(rootDir, MksExchangeV7Paths.QUIZZES, MksExchangeV7Quiz.serializer())
        val questions = readList(rootDir, MksExchangeV7Paths.QUESTIONS, MksExchangeV7Question.serializer())
        val questionCategories = readList(rootDir, MksExchangeV7Paths.QUESTION_CATEGORIES, MksExchangeV7QuestionCategory.serializer())

        val bookExternalById = books.associate { it.id to it.externalId }
        val quizExternalById = quizzes.associate { it.id to it.externalId }
        val questionsByQuizId = questions.groupBy { it.quizId }
        val categoriesByQuestionId = questionCategories.groupBy { it.questionId }
        val defaultWorkspaceExternalId = workspace.workspaces.firstOrNull { it.isDefault }?.externalId
            ?: workspace.workspaces.firstOrNull()?.externalId

        val bookDtos = books.map { book ->
            BookDto(
                id = book.externalId,
                workspaceExternalId = defaultWorkspaceExternalId,
                title = book.title,
                note = book.description,
                coverIcon = book.iconName ?: "📚",
                coverImage = book.coverImage.orEmpty(),
                createdAt = book.createdAt.takeIf { it > 0 },
                contentUpdatedAt = book.contentUpdatedAt.takeIf { it > 0 },
                updatedAt = book.updatedAt.takeIf { it > 0 },
                lastStudiedAt = book.lastStudiedAt.takeIf { it > 0 }
            )
        }

        val quizDtos = quizzes.map { quiz ->
            val quizQuestions = questionsByQuizId[quiz.id].orEmpty().map { question ->
                val categories = (question.categories + categoriesByQuestionId[question.id].orEmpty().map { it.category })
                    .filter { it.isNotBlank() }
                    .distinct()
                QuestionDto(
                    id = question.externalId,
                    stem = question.text,
                    options = question.options.mapIndexed { index, text -> OptionDto(id = index.toString(), text = text) },
                    correct = question.correctAnswers.map { it.toString() },
                    explanation = question.explanation.orEmpty(),
                    hint = question.hint.orEmpty(),
                    reference = question.reference.orEmpty(),
                    imageDataUrl = question.imagePath.orEmpty(),
                    imageSource = question.imageSource.orEmpty(),
                    imageName = question.imageName.orEmpty(),
                    categories = categories,
                    answerMode = question.type,
                    sourceQuizId = question.sourceQuizId.orEmpty(),
                    sourceQuestionId = question.sourceQuestionId.orEmpty(),
                    sourceBookId = question.sourceBookId.orEmpty(),
                    droppedAt = question.droppedAt ?: 0,
                    additionalInfo = question.additionalInfo.orEmpty()
                )
            }

            QuizDto(
                id = quiz.externalId,
                storageKey = quiz.category,
                bookId = bookExternalById[quiz.bookId] ?: quiz.bookId.toString(),
                title = quiz.title,
                note = quiz.description,
                coverIcon = quiz.iconName ?: "🎯",
                coverImage = quiz.coverImage.orEmpty(),
                createdAt = quiz.createdAt.takeIf { it > 0 },
                contentUpdatedAt = quiz.contentUpdatedAt.takeIf { it > 0 },
                updatedAt = quiz.updatedAt.takeIf { it > 0 },
                lastStudiedAt = quiz.lastStudiedAt.takeIf { it > 0 },
                questions = quizQuestions
            )
        }

        val categoryDtos = (questionCategories.map { it.category } + quizzes.mapNotNull { it.category })
            .filter { it.isNotBlank() }
            .distinct()
            .map { CategoryMetadataDto(name = it) }

        return LibraryBundleDto(
            schema = MksExchangeV7Paths.SCHEMA_VERSION,
            kind = "schema7-exchange-bridge",
            exportedAt = System.currentTimeMillis(),
            books = bookDtos,
            quizzes = quizDtos,
            categories = categoryDtos
        )
    }

    fun writeLegacyBundleToSchema7Zip(
        bundle: LibraryBundleDto,
        outputStream: OutputStream,
        password: String,
        supplemental: MksExchangeV7SupplementalData = MksExchangeV7SupplementalData()
    ) {
        // Stage 4D: schema-7 exchange archives must be standard, unencrypted ZIPs.
        // iOS V04 reads a normal stored/deflated ZIP and cannot open Android legacy
        // encrypted bundle output. The password parameter remains for source compatibility
        // with Stage 4C callers, but it is intentionally ignored here.
        val tempDir = createTempDir(prefix = "mks_schema7_")
        try {
            writeLegacyBundleToDirectory(bundle, tempDir, supplemental)
            ZipOutputStream(outputStream).use { zip ->
                tempDir.walkTopDown()
                    .filter { it.isFile }
                    .sortedBy { it.relativeTo(tempDir).invariantSeparatorsPath }
                    .forEach { file ->
                        val relativePath = file.relativeTo(tempDir).invariantSeparatorsPath
                        zip.putNextEntry(ZipEntry(relativePath))
                        file.inputStream().use { input -> input.copyTo(zip) }
                        zip.closeEntry()
                    }
            }
        } finally {
            tempDir.deleteRecursively()
        }
    }

    fun writeLegacyBundleToDirectory(
        bundle: LibraryBundleDto,
        rootDir: File,
        supplemental: MksExchangeV7SupplementalData = MksExchangeV7SupplementalData()
    ) {
        val now = System.currentTimeMillis()
        val workspaceExternalId = bundle.books.firstOrNull()?.workspaceExternalId ?: "default-workspace"
        val workspace = MksExchangeV7WorkspaceEnvelope(
            workspaces = listOf(MksExchangeV7Workspace(id = 1, externalId = workspaceExternalId, name = "Imported Workspace", isDefault = true, createdAt = now, updatedAt = now)),
            workspaceSettings = listOf(MksExchangeV7WorkspaceSettings(id = 1, workspaceId = 1, createdAt = now, updatedAt = now))
        )

        val bookIdByExternalId = bundle.books.mapIndexed { index, book -> book.id to (index + 1L) }.toMap()
        val quizIdByExternalId = bundle.quizzes.mapIndexed { index, quiz -> quiz.id to (index + 1L) }.toMap()
        val mediaPayloads = mutableListOf<MediaPayload>()
        val missingMedia = mutableListOf<String>()

        fun payloadFor(source: String?, ownerKind: String, ownerExternalId: String, ownerId: Long, fallbackName: String, mimeType: String? = null): MediaPayload? {
            if (source.isNullOrBlank()) return null
            val payload = buildMediaPayload(source, ownerKind, ownerExternalId, ownerId, fallbackName, mimeType)
            if (payload == null && isLocalMediaReference(source)) missingMedia += source
            return payload
        }

        val books = bundle.books.map { book ->
            val localId = bookIdByExternalId[book.id] ?: 0L
            val media = payloadFor(book.coverImage, "book", book.id, localId, "book_${book.id}.bin")
            if (media != null) mediaPayloads += media
            MksExchangeV7Book(
                id = localId,
                workspaceId = 1,
                externalId = book.id,
                title = book.title,
                description = book.note,
                iconName = book.coverIcon,
                coverImage = media?.manifestFile?.archivePath ?: book.coverImage.takeIf { it.isNotBlank() },
                createdAt = book.createdAt ?: now,
                updatedAt = book.updatedAt ?: now,
                contentUpdatedAt = book.contentUpdatedAt ?: now,
                lastStudiedAt = book.lastStudiedAt ?: 0L,
                lastEditedAt = book.updatedAt ?: now
            )
        }

        val quizzes = bundle.quizzes.map { quiz ->
            val quizLocalId = quizIdByExternalId[quiz.id] ?: 0L
            val quizMedia = payloadFor(quiz.coverImage, "quiz", quiz.id, quizLocalId, "quiz_${quiz.id}.bin")
            if (quizMedia != null) mediaPayloads += quizMedia
            MksExchangeV7Quiz(
                id = quizLocalId,
                externalId = quiz.id,
                bookId = bookIdByExternalId[quiz.bookId] ?: 0L,
                title = quiz.title,
                description = quiz.note,
                category = quiz.storageKey,
                iconName = quiz.coverIcon,
                coverImage = quizMedia?.manifestFile?.archivePath ?: quiz.coverImage.takeIf { it.isNotBlank() },
                createdAt = quiz.createdAt ?: now,
                updatedAt = quiz.updatedAt ?: now,
                contentUpdatedAt = quiz.contentUpdatedAt ?: now,
                lastStudiedAt = quiz.lastStudiedAt ?: 0L,
                lastEditedAt = quiz.updatedAt ?: now,
                questionCount = quiz.questions.size
            )
        }

        val questions = mutableListOf<MksExchangeV7Question>()
        val questionCategories = mutableListOf<MksExchangeV7QuestionCategory>()
        var nextQuestionId = 1L
        bundle.quizzes.forEach { quiz ->
            val quizLocalId = quizIdByExternalId[quiz.id] ?: 0L
            quiz.questions.forEach { question ->
                val localQuestionId = nextQuestionId++
                val correctAnswers = question.correct.mapNotNull { it.toIntOrNull() }
                val imageSource = question.imageDataUrl.takeIf { it.isNotBlank() } ?: question.imageSource.takeIf { it.isNotBlank() }
                val imagePayload = payloadFor(imageSource, "question", question.id, localQuestionId, question.imageName.takeIf { it.isNotBlank() } ?: "question_${question.id}.bin")
                if (imagePayload != null) mediaPayloads += imagePayload
                val portableImagePath = imagePayload?.manifestFile?.archivePath ?: question.imageDataUrl.takeIf { it.isNotBlank() }
                questions += MksExchangeV7Question(
                    id = localQuestionId,
                    externalId = question.id,
                    quizId = quizLocalId,
                    text = question.stem,
                    type = question.answerMode ?: if (correctAnswers.size > 1) "MULTIPLE_CHOICE" else "SINGLE_CHOICE",
                    options = question.options.map { it.text },
                    correctAnswers = correctAnswers,
                    explanation = question.explanation.takeIf { it.isNotBlank() },
                    hint = question.hint.takeIf { it.isNotBlank() },
                    reference = question.reference.takeIf { it.isNotBlank() },
                    imagePath = portableImagePath,
                    imageName = imagePayload?.manifestFile?.fileName ?: question.imageName.takeIf { it.isNotBlank() },
                    imageSource = imagePayload?.manifestFile?.archivePath ?: question.imageSource.takeIf { it.isNotBlank() },
                    isDropped = question.droppedAt > 0,
                    droppedAt = question.droppedAt.takeIf { it > 0 },
                    categories = question.categories,
                    additionalInfo = question.additionalInfo.takeIf { it.isNotBlank() },
                    sourceBookId = question.sourceBookId.takeIf { it.isNotBlank() },
                    sourceQuizId = question.sourceQuizId.takeIf { it.isNotBlank() },
                    sourceQuestionId = question.sourceQuestionId.takeIf { it.isNotBlank() },
                    createdAt = now,
                    updatedAt = now,
                    lastEditedAt = now
                )
                question.categories.filter { it.isNotBlank() }.distinct().forEach { category ->
                    questionCategories += MksExchangeV7QuestionCategory(questionId = localQuestionId, category = category)
                }
            }
        }

        val questionIdByExternalId = questions.associate { it.externalId to it.id }

        fun localBookId(externalId: String?, fallback: Long? = null): Long =
            externalId?.let { bookIdByExternalId[it] } ?: fallback ?: 0L

        fun localQuizId(externalId: String?, fallback: Long? = null): Long =
            externalId?.let { quizIdByExternalId[it] } ?: fallback ?: 0L

        fun localQuestionId(externalId: String?, fallback: Long? = null): Long =
            externalId?.let { questionIdByExternalId[it] } ?: fallback ?: 0L

        fun localOwnerId(ownerType: String, ownerExternalId: String?, fallback: Long): Long = when (ownerType.uppercase()) {
            "BOOK" -> localBookId(ownerExternalId, fallback)
            "QUIZ" -> localQuizId(ownerExternalId, fallback)
            "QUESTION" -> localQuestionId(ownerExternalId, fallback)
            else -> fallback
        }

        val sourceDocuments = supplemental.sourceDocuments.map { source ->
            val bookLocalId = localBookId(source.bookExternalId, source.bookId)
            val sourcePayload = payloadFor(
                source.localPath,
                "source_document",
                source.id.toString(),
                source.id,
                source.title.ifBlank { "source_${source.id}.bin" }
            )
            if (sourcePayload != null) mediaPayloads += sourcePayload
            MksExchangeV7SourceDocument(
                id = source.id,
                bookId = bookLocalId.takeIf { it > 0 },
                title = source.title,
                sourceType = source.sourceType,
                author = source.author,
                edition = source.edition,
                year = source.year,
                publisher = source.publisher,
                localPath = sourcePayload?.manifestFile?.archivePath ?: source.localPath,
                externalUrl = source.externalUrl,
                description = source.description,
                createdAt = source.createdAt,
                updatedAt = source.updatedAt,
                deletedAt = source.deletedAt
            )
        }

        val questionAssets = supplemental.questionAssets.map { asset ->
            val bookLocalId = localBookId(asset.bookExternalId, asset.bookId)
            val quizLocalId = localQuizId(asset.quizExternalId, asset.quizId)
            val questionLocalId = localQuestionId(asset.questionExternalId, asset.questionId)
            val assetPayload = payloadFor(
                asset.localPath,
                "question_asset",
                asset.id.toString(),
                asset.id,
                asset.fileName ?: asset.title.ifBlank { "question_asset_${asset.id}.bin" },
                asset.mimeType
            )
            if (assetPayload != null) mediaPayloads += assetPayload
            MksExchangeV7QuestionAsset(
                id = asset.id,
                bookId = bookLocalId,
                quizId = quizLocalId,
                questionId = questionLocalId,
                assetType = asset.assetType,
                title = asset.title,
                description = asset.description,
                localPath = assetPayload?.manifestFile?.archivePath ?: asset.localPath,
                externalUrl = asset.externalUrl,
                mimeType = asset.mimeType ?: assetPayload?.manifestFile?.mimeType,
                fileName = asset.fileName ?: assetPayload?.manifestFile?.fileName,
                fileSizeBytes = asset.fileSizeBytes ?: assetPayload?.manifestFile?.sizeBytes,
                textContent = asset.textContent,
                sourceDocumentId = asset.sourceDocumentId,
                sourcePage = asset.sourcePage,
                sourceQuote = asset.sourceQuote,
                sortOrder = asset.sortOrder,
                isPinned = asset.isPinned,
                isPrimary = asset.isPrimary,
                createdAt = asset.createdAt,
                updatedAt = asset.updatedAt,
                deletedAt = asset.deletedAt
            )
        }

        val annotations = supplemental.annotations.map { annotation ->
            val bookLocalId = localBookId(annotation.bookExternalId, annotation.bookId)
            val ownerLocalId = localOwnerId(annotation.ownerType, annotation.ownerExternalId, annotation.ownerId)
            MksExchangeV7Annotation(
                id = annotation.id,
                workspaceId = annotation.workspaceId.takeIf { it > 0 } ?: 1L,
                bookId = bookLocalId,
                ownerType = annotation.ownerType,
                ownerId = ownerLocalId,
                selectedText = annotation.selectedText,
                noteBody = annotation.noteBody,
                colorLabel = annotation.colorLabel,
                positionDataJson = annotation.positionDataJson,
                createdAt = annotation.createdAt,
                updatedAt = annotation.updatedAt,
                deletedAt = annotation.deletedAt
            )
        }

        val assetReferences = supplemental.assetReferences.map { reference ->
            MksExchangeV7AssetReference(
                id = reference.id,
                path = reference.path,
                ownerType = reference.ownerType,
                ownerId = localOwnerId(reference.ownerType, reference.ownerExternalId, reference.ownerId),
                createdAt = reference.createdAt,
                deletedAt = reference.deletedAt
            )
        }

        val softDeletes = buildList {
            books.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("book", it.id, it.externalId, it.deletedAt ?: 0L)) }
            quizzes.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("quiz", it.id, it.externalId, it.deletedAt ?: 0L)) }
            questions.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("question", it.id, it.externalId, it.deletedAt ?: 0L)) }
            assetReferences.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("asset_reference", it.id, null, it.deletedAt ?: 0L)) }
            questionAssets.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("question_asset", it.id, null, it.deletedAt ?: 0L)) }
            sourceDocuments.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("source_document", it.id, null, it.deletedAt ?: 0L)) }
            annotations.filter { it.deletedAt != null }.forEach { add(MksExchangeV7SoftDeleteRecord("annotation", it.id, null, it.deletedAt ?: 0L)) }
        }

        val mediaManifest = MksExchangeV7MediaManifest(
            files = mediaPayloads.map { it.manifestFile }.distinctBy { it.archivePath }.sortedBy { it.archivePath },
            missingFiles = missingMedia.distinct().sorted()
        )
        mediaPayloads.distinctBy { it.manifestFile.archivePath }.forEach { payload ->
            val mediaFile = File(rootDir, payload.manifestFile.archivePath)
            mediaFile.parentFile?.mkdirs()
            mediaFile.writeBytes(payload.bytes)
        }

        val entries = listOf(
            MksExchangeV7Paths.MANIFEST,
            MksExchangeV7Paths.WORKSPACE,
            MksExchangeV7Paths.BOOKS,
            MksExchangeV7Paths.QUIZZES,
            MksExchangeV7Paths.QUESTIONS,
            MksExchangeV7Paths.QUESTION_CATEGORIES,
            MksExchangeV7Paths.ASSET_REFERENCES,
            MksExchangeV7Paths.QUESTION_ASSETS,
            MksExchangeV7Paths.SOURCE_DOCUMENTS,
            MksExchangeV7Paths.ANNOTATIONS,
            MksExchangeV7Paths.MEDIA_MANIFEST,
            MksExchangeV7Paths.SOFT_DELETES
        ) + mediaManifest.files.map { it.archivePath }
        val warnings = buildList {
            if (mediaManifest.missingFiles.isNotEmpty()) add("Some local media references could not be bundled: ${mediaManifest.missingFiles.size}.")
            if (questionAssets.any { it.questionId == 0L }) add("Some question assets could not be mapped to exported schema-7 question ids.")
            if (annotations.any { it.bookId == 0L || it.ownerId == 0L }) add("Some annotations have owner/book ids outside the exported graph.")
        }
        val manifest = MksExchangeV7Manifest(
            exportedAt = now,
            archiveKind = "r2-schema7-dao-native-coverage",
            includesMedia = mediaManifest.files.isNotEmpty(),
            entries = entries,
            counts = MksExchangeV7Counts(
                workspaces = workspace.workspaces.size,
                workspaceSettings = workspace.workspaceSettings.size,
                books = books.size,
                quizzes = quizzes.size,
                questions = questions.size,
                questionCategories = questionCategories.size,
                assetReferences = assetReferences.size,
                questionAssets = questionAssets.size,
                sourceDocuments = sourceDocuments.size,
                annotations = annotations.size,
                mediaFiles = mediaManifest.files.size,
                softDeletedRecords = softDeletes.size
            ),
            warnings = warnings
        )

        writeJson(rootDir, MksExchangeV7Paths.MANIFEST, manifest, MksExchangeV7Manifest.serializer())
        writeJson(rootDir, MksExchangeV7Paths.WORKSPACE, workspace, MksExchangeV7WorkspaceEnvelope.serializer())
        writeJson(rootDir, MksExchangeV7Paths.BOOKS, books, ListSerializer(MksExchangeV7Book.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.QUIZZES, quizzes, ListSerializer(MksExchangeV7Quiz.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.QUESTIONS, questions, ListSerializer(MksExchangeV7Question.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.QUESTION_CATEGORIES, questionCategories, ListSerializer(MksExchangeV7QuestionCategory.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.ASSET_REFERENCES, assetReferences, ListSerializer(MksExchangeV7AssetReference.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.QUESTION_ASSETS, questionAssets, ListSerializer(MksExchangeV7QuestionAsset.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.SOURCE_DOCUMENTS, sourceDocuments, ListSerializer(MksExchangeV7SourceDocument.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.ANNOTATIONS, annotations, ListSerializer(MksExchangeV7Annotation.serializer()))
        writeJson(rootDir, MksExchangeV7Paths.MEDIA_MANIFEST, mediaManifest, MksExchangeV7MediaManifest.serializer())
        writeJson(rootDir, MksExchangeV7Paths.SOFT_DELETES, softDeletes, ListSerializer(MksExchangeV7SoftDeleteRecord.serializer()))
    }

    private fun buildMediaPayload(source: String, ownerKind: String, ownerExternalId: String, ownerId: Long, fallbackName: String, mimeType: String? = null): MediaPayload? {
        val bytes = when {
            source.startsWith("data:") -> source.substringAfter(',', missingDelimiterValue = "").takeIf { it.isNotBlank() }?.let { runCatching { Base64.getDecoder().decode(it) }.getOrNull() }
            source.startsWith("http://", ignoreCase = true) || source.startsWith("https://", ignoreCase = true) || source.startsWith("media/") -> null
            else -> runCatching { File(source).takeIf { it.exists() && it.isFile }?.readBytes() }.getOrNull()
        } ?: return null
        val fileName = sanitizeFileName(source.substringAfterLast('/').substringAfterLast('\\').takeIf { it.isNotBlank() && !it.startsWith("data:") } ?: fallbackName)
        val archivePath = "${MksExchangeV7Paths.MEDIA_DIRECTORY}/$ownerKind/${sanitizePathComponent(ownerExternalId)}/$fileName"
        return MediaPayload(
            manifestFile = MksExchangeV7MediaFile(
                archivePath = archivePath,
                originalPath = source,
                fileName = fileName,
                mimeType = mimeType ?: guessMimeType(fileName),
                sizeBytes = bytes.size.toLong(),
                ownerKind = ownerKind,
                ownerExternalId = ownerExternalId,
                ownerId = ownerId,
                sha256 = sha256(bytes)
            ),
            bytes = bytes,
            source = source
        )
    }

    private fun isLocalMediaReference(source: String): Boolean {
        return source.isNotBlank() && !source.startsWith("data:") && !source.startsWith("http://", ignoreCase = true) && !source.startsWith("https://", ignoreCase = true) && !source.startsWith("media/")
    }

    private fun sanitizePathComponent(value: String): String = value.map { ch ->
        if (ch.isLetterOrDigit() || ch == '-' || ch == '_') ch else '_'
    }.joinToString("").trim('_').ifBlank { "record" }

    private fun sanitizeFileName(value: String): String {
        val sanitized = value.map { ch ->
            if (ch.isLetterOrDigit() || ch == '-' || ch == '_' || ch == '.') ch else '_'
        }.joinToString("").trim('_', '.')
        return sanitized.ifBlank { "media.bin" }
    }

    private fun guessMimeType(fileName: String): String? = when (fileName.substringAfterLast('.', "").lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "pdf" -> "application/pdf"
        "txt", "md" -> "text/plain"
        else -> null
    }

    private fun sha256(bytes: ByteArray): String {
        return MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
    }

    private fun <T> readOrDefault(rootDir: File, relativePath: String, serializer: KSerializer<T>, defaultValue: T): T {
        val file = File(rootDir, relativePath)
        if (!file.exists()) return defaultValue
        return runCatching { json.decodeFromString(serializer, file.readText()) }.getOrElse { defaultValue }
    }

    private fun <T> readList(rootDir: File, relativePath: String, serializer: KSerializer<T>): List<T> {
        val file = File(rootDir, relativePath)
        if (!file.exists()) return emptyList()
        return runCatching { json.decodeFromString(ListSerializer(serializer), file.readText()) }.getOrElse { emptyList() }
    }

    private fun <T> writeJson(rootDir: File, relativePath: String, value: T, serializer: KSerializer<T>) {
        val file = File(rootDir, relativePath)
        file.parentFile?.mkdirs()
        file.writeText(json.encodeToString(serializer, value))
    }
}
