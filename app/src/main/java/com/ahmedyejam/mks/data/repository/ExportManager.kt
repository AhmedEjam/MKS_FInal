package com.ahmedyejam.mks.data.repository

import androidx.room.withTransaction
import com.ahmedyejam.mks.data.exchange.v7.*
import com.ahmedyejam.mks.data.import.dto.*
import com.ahmedyejam.mks.data.import.mapping.LibraryMapper
import com.ahmedyejam.mks.data.local.FileManager
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.dao.*
import com.ahmedyejam.mks.data.local.entity.*
import com.ahmedyejam.mks.data.model.ExportResult
import com.ahmedyejam.mks.data.model.ExportWarning
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.OutputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExportManager(
    private val database: MksDatabase,
    private val bookDao: BookDao,
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao,
    private val sessionDao: SessionDao,
    private val categoryMetadataDao: CategoryMetadataDao,
    private val fileManager: FileManager,
    private val mapper: LibraryMapper
) {

    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val okHttpClient by lazy { okhttp3.OkHttpClient() }

    companion object {
    }

    suspend fun exportQuizAsBundle(quizId: Long): LibraryBundleDto? {
        return database.withTransaction {
            val quiz = quizDao.getQuizById(quizId) ?: return@withTransaction null
            val book = bookDao.getBookById(quiz.bookId) ?: return@withTransaction null
            val questions = questionDao.getQuestionsByQuizId(quiz.id).first()
            val workspaceExternalId = database.workspaceDao().getWorkspaceById(book.workspaceId)?.externalId

            val exportBook = mapper.mapToBookDto(book, workspaceExternalId)
            val exportQuiz = mapper.mapToQuizDto(quiz, questions, book.externalId)

            val categoryNames = (questions.flatMap { it.categories } + quiz.category)
                .distinct()
                .filterNotNull()
                .filter { it.isNotBlank() }

            val exportCategories = categoryNames.mapNotNull { catName ->
                categoryMetadataDao.getMetadataForCategory(catName)?.let { mapper.mapToCategoryMetadataDto(it) }
                    ?: CategoryMetadataDto(name = catName)
            }

            LibraryBundleDto(
                kind = "quiz-bundle",
                books = listOf(exportBook),
                quizzes = listOf(exportQuiz),
                categories = exportCategories
            )
        }
    }

    suspend fun exportQuizToZip(quizId: Long, outputStream: OutputStream): ExportResult {
        val bundle = exportQuizAsBundle(quizId)
            ?: return ExportResult(success = false, errorMessage = "Quiz not found.")
        return writeBundleToZip(bundle, outputStream)
    }



    suspend fun exportBookAsBundle(bookId: Long): LibraryBundleDto? {
        return database.withTransaction {
            val book = bookDao.getBookById(bookId) ?: return@withTransaction null
            val workspaceExternalId = database.workspaceDao().getWorkspaceById(book.workspaceId)?.externalId
            val exportBook = mapper.mapToBookDto(book, workspaceExternalId)
            val exportQuizzes = mutableListOf<QuizDto>()
            val exportSessions = mutableListOf<SessionDto>()

            val quizzes = quizDao.getQuizzesByBookId(book.id).first()
            val allQuestions = mutableListOf<QuestionEntity>()
            for (quiz in quizzes) {
                val questions = questionDao.getQuestionsByQuizId(quiz.id).first()
                allQuestions.addAll(questions)
                val sessions = sessionDao.getSessionsByQuizId(quiz.id).first()

                exportQuizzes.add(mapper.mapToQuizDto(quiz, questions, book.externalId))
                exportSessions.addAll(sessions.map { mapper.mapToSessionDto(it, quiz.externalId, questions) })
            }

            // Knowledge Bank Assets
            val flashcardDecks = database.flashcardDeckDao().getFlashcardDecksByBookIdNow(book.id)
            val exportFlashcardDecks = flashcardDecks.map { deck ->
                val cards = database.flashcardDao().getFlashcardsByDeckIdNow(deck.id)
                mapper.mapToFlashcardDeckDto(deck, cards, book.externalId)
            }

            val slideshowCourses = database.slideshowCourseDao().getSlideshowCoursesByBookIdNow(book.id)
            val exportSlideshowCourses = slideshowCourses.map { course ->
                val slides = database.courseSlideDao().getSlidesByCourseIdNow(course.id)
                mapper.mapToSlideshowCourseDto(course, slides, book.externalId)
            }

            val noteBlueprints = database.noteBlueprintDao().getNotesByBookIdNow(book.id)
            val exportNoteBlueprints = noteBlueprints.map { mapper.mapToNoteBlueprintDto(it, book.externalId) }

            val promptDecks = database.promptDeckDao().getDecksByBookIdNow(book.id)
            val exportPromptDecks = promptDecks.map { deck ->
                val cards = database.promptCardDao().getCardsByDeckIdNow(deck.id)
                mapper.mapToPromptDeckDto(deck, cards, book.externalId)
            }

            val studySessions = database.knowledgeStudySessionDao().getSessionsByBookIdNow(book.id)
            val exportStudySessions = studySessions.map { session ->
                val contentExtId = when (session.targetType) {
                    "FLASHCARD_DECK" -> flashcardDecks.find { it.id == session.targetId }?.externalId
                    "SLIDESHOW" -> slideshowCourses.find { it.id == session.targetId }?.externalId
                    "NOTE" -> noteBlueprints.find { it.id == session.targetId }?.externalId
                    "PROMPT" -> promptDecks.find { it.id == session.targetId }?.let { "prompt-deck-${it.id}" }
                    else -> session.targetId.toString()
                } ?: session.targetId.toString()

                KnowledgeStudySessionDto(
                    bookId = book.externalId,
                    contentId = contentExtId,
                    type = session.targetType,
                    progress = 0f,
                    isCompleted = session.isCompleted,
                    lastAccessedAt = session.updatedAt
                )
            }

            val categoryNames = (allQuestions.flatMap { it.categories } + exportQuizzes.mapNotNull { it.storageKey })
                .distinct()
                .filter { it.isNotBlank() }

            val exportCategories = categoryNames.mapNotNull { catName ->
                categoryMetadataDao.getMetadataForCategory(catName)?.let { mapper.mapToCategoryMetadataDto(it) }
                    ?: CategoryMetadataDto(name = catName)
            }

            LibraryBundleDto(
                kind = "book-bundle",
                books = listOf(exportBook),
                quizzes = exportQuizzes,
                flashcardDecks = exportFlashcardDecks,
                slideshowCourses = exportSlideshowCourses,
                noteBlueprints = exportNoteBlueprints,
                promptDecks = exportPromptDecks,
                studySessions = exportStudySessions,
                sessions = exportSessions,
                categories = exportCategories
            )
        }
    }

    suspend fun exportAllBooksAsBundle(): LibraryBundleDto {
        return database.withTransaction {
            val books = bookDao.getAllBooksFlow().first()
            val workspaceExternalIds = books
                .map { it.workspaceId }
                .distinct()
                .associateWith { workspaceId -> database.workspaceDao().getWorkspaceById(workspaceId)?.externalId }
            val exportBooks = books.map { mapper.mapToBookDto(it, workspaceExternalIds[it.workspaceId]) }
            val exportQuizzes = mutableListOf<QuizDto>()
            val exportSessions = mutableListOf<SessionDto>()
            val allQuestionEntities = mutableListOf<QuestionEntity>()

            val exportFlashcardDecks = mutableListOf<FlashcardDeckDto>()
            val exportSlideshowCourses = mutableListOf<SlideshowCourseDto>()
            val exportNoteBlueprints = mutableListOf<NoteBlueprintDto>()
            val exportPromptDecks = mutableListOf<PromptDeckDto>()
            val exportStudySessions = mutableListOf<KnowledgeStudySessionDto>()

            for (book in books) {
                val quizzes = quizDao.getQuizzesByBookId(book.id).first()
                for (quiz in quizzes) {
                    val questions = questionDao.getQuestionsByQuizId(quiz.id).first()
                    allQuestionEntities.addAll(questions)
                    val sessions = sessionDao.getSessionsByQuizId(quiz.id).first()

                    exportQuizzes.add(mapper.mapToQuizDto(quiz, questions, book.externalId))
                    exportSessions.addAll(sessions.map { mapper.mapToSessionDto(it, quiz.externalId, questions) })
                }

                val flashcardDecks = database.flashcardDeckDao().getFlashcardDecksByBookIdNow(book.id)
                exportFlashcardDecks.addAll(flashcardDecks.map { deck ->
                    val cards = database.flashcardDao().getFlashcardsByDeckIdNow(deck.id)
                    mapper.mapToFlashcardDeckDto(deck, cards, book.externalId)
                })

                val slideshowCourses = database.slideshowCourseDao().getSlideshowCoursesByBookIdNow(book.id)
                exportSlideshowCourses.addAll(slideshowCourses.map { course ->
                    val slides = database.courseSlideDao().getSlidesByCourseIdNow(course.id)
                    mapper.mapToSlideshowCourseDto(course, slides, book.externalId)
                })

                val noteBlueprints = database.noteBlueprintDao().getNotesByBookIdNow(book.id)
                exportNoteBlueprints.addAll(noteBlueprints.map { mapper.mapToNoteBlueprintDto(it, book.externalId) })

                val promptDecks = database.promptDeckDao().getDecksByBookIdNow(book.id)
                exportPromptDecks.addAll(promptDecks.map { deck ->
                    val cards = database.promptCardDao().getCardsByDeckIdNow(deck.id)
                    mapper.mapToPromptDeckDto(deck, cards, book.externalId)
                })

                val studySessions = database.knowledgeStudySessionDao().getSessionsByBookIdNow(book.id)
                exportStudySessions.addAll(studySessions.map { session ->
                    val contentExtId = when (session.targetType) {
                        "FLASHCARD_DECK" -> flashcardDecks.find { it.id == session.targetId }?.externalId
                        "SLIDESHOW" -> slideshowCourses.find { it.id == session.targetId }?.externalId
                        "NOTE" -> noteBlueprints.find { it.id == session.targetId }?.externalId
                        "PROMPT" -> promptDecks.find { it.id == session.targetId }?.let { "prompt-deck-${it.id}" }
                        else -> session.targetId.toString()
                    } ?: session.targetId.toString()

                    KnowledgeStudySessionDto(
                        bookId = book.externalId,
                        contentId = contentExtId,
                        type = session.targetType,
                        progress = 0f, // Need calculation if needed
                        isCompleted = session.isCompleted,
                        lastAccessedAt = session.updatedAt
                    )
                })
            }

            val categoryNames = (allQuestionEntities.flatMap { it.categories } + exportQuizzes.mapNotNull { it.storageKey })
                .distinct()
                .filter { it.isNotBlank() }

            val exportCategories = categoryNames.mapNotNull { catName ->
                categoryMetadataDao.getMetadataForCategory(catName)?.let { mapper.mapToCategoryMetadataDto(it) }
                    ?: CategoryMetadataDto(name = catName)
            }

            LibraryBundleDto(
                kind = "full-library",
                books = exportBooks,
                quizzes = exportQuizzes,
                flashcardDecks = exportFlashcardDecks,
                slideshowCourses = exportSlideshowCourses,
                noteBlueprints = exportNoteBlueprints,
                promptDecks = exportPromptDecks,
                studySessions = exportStudySessions,
                sessions = exportSessions,
                categories = exportCategories
            )
        }
    }

    suspend fun exportBundleToZip(bookId: Long, outputStream: OutputStream): ExportResult {
        val bundle = exportBookAsBundle(bookId)
            ?: return ExportResult(success = false, errorMessage = "Book not found.")
        return writeBundleToZip(bundle, outputStream)
    }

    suspend fun exportAllToZip(outputStream: OutputStream): ExportResult {
        val bundle = exportAllBooksAsBundle()
        return writeBundleToZip(bundle, outputStream)
    }

    internal suspend fun writeBundleToZip(bundle: LibraryBundleDto, outputStream: OutputStream): ExportResult = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val successfullyProcessedAssets = mutableMapOf<String, File>() // sourcePathOrUrl -> tempFile
        val assetsToProcess = mutableSetOf<String>()
        val warnings = mutableListOf<ExportWarning>()

        // Pass 1: Collect all potential assets
        bundle.books.forEach { book ->
            val img = when {
                !book.coverImage.isNullOrBlank() -> book.coverImage
                !book.coverIcon.isNullOrBlank() && (book.coverIcon.startsWith("http") || book.coverIcon.contains("/") || book.coverIcon.contains("\\")) -> book.coverIcon
                else -> null
            }
            if (!img.isNullOrBlank() && !img.startsWith("data:") && !img.startsWith("assets/")) {
                assetsToProcess.add(img)
            }
        }
        bundle.quizzes.forEach { quiz ->
            val img = when {
                !quiz.coverImage.isNullOrBlank() -> quiz.coverImage
                !quiz.coverIcon.isNullOrBlank() && (quiz.coverIcon.startsWith("http") || quiz.coverIcon.contains("/") || quiz.coverIcon.contains("\\")) -> quiz.coverIcon
                else -> null
            }
            if (!img.isNullOrBlank() && !img.startsWith("data:") && !img.startsWith("assets/")) {
                assetsToProcess.add(img)
            }
            quiz.questions.forEach { q ->
                val qImg = when {
                    !q.imageDataUrl.isNullOrBlank() -> q.imageDataUrl
                    !q.imageSource.isNullOrBlank() -> q.imageSource
                    else -> null
                }
                if (!qImg.isNullOrBlank() && !qImg.startsWith("data:") && !qImg.startsWith("assets/")) {
                    assetsToProcess.add(qImg)
                }
            }
        }

        bundle.flashcardDecks.forEach { deck ->
            if (!deck.coverImage.isNullOrBlank() && !deck.coverImage.startsWith("data:") && !deck.coverImage.startsWith("assets/")) {
                assetsToProcess.add(deck.coverImage)
            }
            deck.cards.forEach { card ->
                if (!card.imagePath.isNullOrBlank() && !card.imagePath.startsWith("data:") && !card.imagePath.startsWith("assets/")) {
                    assetsToProcess.add(card.imagePath)
                }
            }
        }

        bundle.slideshowCourses.forEach { course ->
            if (!course.coverImage.isNullOrBlank() && !course.coverImage.startsWith("data:") && !course.coverImage.startsWith("assets/")) {
                assetsToProcess.add(course.coverImage)
            }
            course.slides.forEach { slide ->
                if (!slide.imagePath.isNullOrBlank() && !slide.imagePath.startsWith("data:") && !slide.imagePath.startsWith("assets/")) {
                    assetsToProcess.add(slide.imagePath)
                }
            }
        }

        // Pass 2: Process assets (download/copy to temp files)
        assetsToProcess.forEach { source ->
            var tempFile: File? = null
            try {
                val assetTempFile = File.createTempFile("mks_asset_", ".tmp", fileManager.getContext().cacheDir)
                tempFile = assetTempFile
                var success = false
                if (source.startsWith("http")) {
                    val request = okhttp3.Request.Builder().url(source).build()
                    okHttpClient.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            response.body?.byteStream()?.use { input ->
                                assetTempFile.outputStream().use { output -> input.copyTo(output) }
                                success = true
                            }
                        } else {
                            warnings += ExportWarning(
                                source = source,
                                message = "Download failed with HTTP ${response.code}."
                            )
                        }
                    }
                } else {
                    val file = fileManager.getFile(source) ?: run {
                        // Fallback for simple names that might be in images dir
                        if (!source.contains("/") && !source.contains("\\")) {
                            fileManager.getFile(File(File(fileManager.getContext().filesDir, "images"), source).absolutePath)
                        } else null
                    }
                    if (file != null && file.exists()) {
                        file.inputStream().use { input ->
                            assetTempFile.outputStream().use { output -> input.copyTo(output) }
                            success = true
                        }
                    } else {
                        warnings += ExportWarning(
                            source = source,
                            message = "Local asset file could not be found."
                        )
                    }
                }

                if (success) {
                    successfullyProcessedAssets[source] = assetTempFile
                } else {
                    assetTempFile.delete()
                }
            } catch (e: Exception) {
                tempFile?.delete()
                warnings += ExportWarning(
                    source = source,
                    message = e.message ?: "Asset export failed."
                )
            }
        }

        // Pass 3: Update Bundle and identify final zip paths
        val zipPathsToInclude = mutableMapOf<String, File>() // zipPath -> tempFile
        val manifestAssets = mutableMapOf<String, String>() // sourceRef or bundleRef -> zipPath

        val updatedBooks = bundle.books.map { book ->
            val img = when {
                !book.coverImage.isNullOrBlank() -> book.coverImage
                !book.coverIcon.isNullOrBlank() && (book.coverIcon.startsWith("http") || book.coverIcon.contains("/") || book.coverIcon.contains("\\")) -> book.coverIcon
                else -> null
            }
            if (!img.isNullOrBlank()) {
                successfullyProcessedAssets[img]?.let { processedFile ->
                    val fileName = generateSafeFileName("book", book.id, img)
                    val zipPath = "assets/$fileName"
                    zipPathsToInclude[zipPath] = processedFile
                    manifestAssets[img] = zipPath
                    manifestAssets[zipPath] = zipPath
                    book.copy(coverImage = zipPath)
                } ?: run {
                    // Portability: only remove non-portable path if processing failed
                    // If it's a URL or relative path, keep it so it's not silent data loss
                    if (img.startsWith("/") || img.contains("\\")) {
                        book.copy(coverImage = "")
                    } else book
                }
            } else book
        }

        val updatedQuizzes = bundle.quizzes.map { quiz ->
            val quizImage = when {
                !quiz.coverImage.isNullOrBlank() -> quiz.coverImage
                !quiz.coverIcon.isNullOrBlank() && (quiz.coverIcon.startsWith("http") || quiz.coverIcon.contains("/") || quiz.coverIcon.contains("\\")) -> quiz.coverIcon
                else -> null
            }

            var currentQuiz = quiz
            if (!quizImage.isNullOrBlank()) {
                successfullyProcessedAssets[quizImage]?.let { processedFile ->
                    val fileName = generateSafeFileName("quiz", quiz.id, quizImage)
                    val zipPath = "assets/$fileName"
                    zipPathsToInclude[zipPath] = processedFile
                    manifestAssets[quizImage] = zipPath
                    manifestAssets[zipPath] = zipPath
                    currentQuiz = currentQuiz.copy(coverImage = zipPath)
                } ?: run {
                    if (quizImage.startsWith("/") || quizImage.contains("\\")) {
                        currentQuiz = currentQuiz.copy(coverImage = "")
                    }
                }
            }

            val updatedQuestions = currentQuiz.questions.map { q ->
                val qImage = when {
                    !q.imageDataUrl.isNullOrBlank() -> q.imageDataUrl
                    !q.imageSource.isNullOrBlank() -> q.imageSource
                    else -> null
                }

                if (!qImage.isNullOrBlank()) {
                    successfullyProcessedAssets[qImage]?.let { processedFile ->
                        val fileName = generateSafeFileName("q", q.id, qImage)
                        val zipPath = "assets/$fileName"
                        zipPathsToInclude[zipPath] = processedFile
                        manifestAssets[qImage] = zipPath
                        manifestAssets[zipPath] = zipPath
                        q.copy(imageDataUrl = zipPath, imageSource = zipPath)
                    } ?: run {
                        if (qImage.startsWith("/") || qImage.contains("\\")) {
                            q.copy(imageDataUrl = "", imageSource = "")
                        } else q
                    }
                } else q
            }
            currentQuiz.copy(questions = updatedQuestions)
        }

        val updatedFlashcardDecks = bundle.flashcardDecks.map { deck ->
            var currentDeck = deck
            if (!deck.coverImage.isNullOrBlank()) {
                successfullyProcessedAssets[deck.coverImage]?.let { processedFile ->
                    val fileName = generateSafeFileName("deck", deck.id, deck.coverImage)
                    val zipPath = "assets/$fileName"
                    zipPathsToInclude[zipPath] = processedFile
                    manifestAssets[deck.coverImage] = zipPath
                    currentDeck = currentDeck.copy(coverImage = zipPath)
                }
            }
            val updatedCards = currentDeck.cards.map { card ->
                if (!card.imagePath.isNullOrBlank()) {
                    successfullyProcessedAssets[card.imagePath]?.let { processedFile ->
                        val fileName = generateSafeFileName("card", card.id, card.imagePath)
                        val zipPath = "assets/$fileName"
                        zipPathsToInclude[zipPath] = processedFile
                        manifestAssets[card.imagePath] = zipPath
                        card.copy(imagePath = zipPath)
                    } ?: card
                } else card
            }
            currentDeck.copy(cards = updatedCards)
        }

        val updatedSlideshowCourses = bundle.slideshowCourses.map { course ->
            var currentCourse = course
            if (!course.coverImage.isNullOrBlank()) {
                successfullyProcessedAssets[course.coverImage]?.let { processedFile ->
                    val fileName = generateSafeFileName("course", course.id, course.coverImage)
                    val zipPath = "assets/$fileName"
                    zipPathsToInclude[zipPath] = processedFile
                    manifestAssets[course.coverImage] = zipPath
                    currentCourse = currentCourse.copy(coverImage = zipPath)
                }
            }
            val updatedSlides = currentCourse.slides.map { slide ->
                if (!slide.imagePath.isNullOrBlank()) {
                    successfullyProcessedAssets[slide.imagePath]?.let { processedFile ->
                        val fileName = generateSafeFileName("slide", slide.id, slide.imagePath)
                        val zipPath = "assets/$fileName"
                        zipPathsToInclude[zipPath] = processedFile
                        manifestAssets[slide.imagePath] = zipPath
                        slide.copy(imagePath = zipPath)
                    } ?: slide
                } else slide
            }
            currentCourse.copy(slides = updatedSlides)
        }

        val finalBundle = bundle.copy(
            books = updatedBooks,
            quizzes = updatedQuizzes,
            flashcardDecks = updatedFlashcardDecks,
            slideshowCourses = updatedSlideshowCourses,
            exportedAt = System.currentTimeMillis()
        )

        val jsonStr = json.encodeToString(finalBundle)
        val manifestJsonStr = json.encodeToString(ManifestDto(assets = manifestAssets))
        
        // Create a temporary file to build the zip
        val tempZipFile = File.createTempFile("mks_export_", ".zip", fileManager.getContext().cacheDir)
        
        try {
            val zipParameters = ZipParameters().apply {
                isEncryptFiles = true
                encryptionMethod = EncryptionMethod.AES
                aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256
            }

            ZipFile(tempZipFile, "mks_secure_bundle_2024".toCharArray()).use { zipFile ->
                // 1. Write library.json
                val libraryJsonFile = File(fileManager.getContext().cacheDir, "library_${System.currentTimeMillis()}.json")
                libraryJsonFile.writeText(jsonStr)
                zipParameters.fileNameInZip = "library.json"
                zipFile.addFile(libraryJsonFile, zipParameters)
                libraryJsonFile.delete()

                // 2. Write manifest.json
                val manifestJsonFile = File(fileManager.getContext().cacheDir, "manifest_${System.currentTimeMillis()}.json")
                manifestJsonFile.writeText(manifestJsonStr)
                zipParameters.fileNameInZip = "manifest.json"
                zipFile.addFile(manifestJsonFile, zipParameters)
                manifestJsonFile.delete()

                // 3. Add physical assets
                zipPathsToInclude.forEach { (zipPath, tempAssetFile) ->
                    zipParameters.fileNameInZip = zipPath
                    zipFile.addFile(tempAssetFile, zipParameters)
                }
            }

            // Copy temp zip to outputStream
            tempZipFile.inputStream().use { input ->
                input.copyTo(outputStream)
            }

            ExportResult(
                success = true,
                exportedAssetCount = zipPathsToInclude.size,
                failedAssetCount = warnings.size,
                warnings = warnings.toList()
            )
        } finally {
            successfullyProcessedAssets.values.forEach { it.delete() }
            tempZipFile.delete()
        }
    }

    private fun generateSafeFileName(prefix: String, id: String, originalSource: String): String {
        val extension = originalSource.substringAfterLast('.', "webp").lowercase().split('?').first()
        val safeId = id.filter { it.isLetterOrDigit() || it == '-' || it == '_' }.take(20)
        val unique = System.currentTimeMillis().toString().takeLast(5)
        return "${prefix}_${safeId}_$unique.$extension"
    }
}
