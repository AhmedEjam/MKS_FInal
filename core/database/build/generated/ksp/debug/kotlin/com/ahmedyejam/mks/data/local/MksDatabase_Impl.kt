package com.ahmedyejam.mks.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.ahmedyejam.mks.`data`.local.dao.AnnotationDao
import com.ahmedyejam.mks.`data`.local.dao.AnnotationDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.AssetReferenceDao
import com.ahmedyejam.mks.`data`.local.dao.AssetReferenceDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.BookDao
import com.ahmedyejam.mks.`data`.local.dao.BookDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.CategoryMetadataDao
import com.ahmedyejam.mks.`data`.local.dao.CategoryMetadataDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.CourseSlideDao
import com.ahmedyejam.mks.`data`.local.dao.CourseSlideDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.FlashcardDao
import com.ahmedyejam.mks.`data`.local.dao.FlashcardDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.FlashcardDeckDao
import com.ahmedyejam.mks.`data`.local.dao.FlashcardDeckDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.GlobalSearchDao
import com.ahmedyejam.mks.`data`.local.dao.GlobalSearchDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.KnowledgeStudySessionDao
import com.ahmedyejam.mks.`data`.local.dao.KnowledgeStudySessionDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.LearningSessionDao
import com.ahmedyejam.mks.`data`.local.dao.LearningSessionDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.MistakeLogDao
import com.ahmedyejam.mks.`data`.local.dao.MistakeLogDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.NoteBlueprintDao
import com.ahmedyejam.mks.`data`.local.dao.NoteBlueprintDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.NoteCollectionDao
import com.ahmedyejam.mks.`data`.local.dao.NoteCollectionDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.PromptCardDao
import com.ahmedyejam.mks.`data`.local.dao.PromptCardDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.PromptDao
import com.ahmedyejam.mks.`data`.local.dao.PromptDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.PromptDeckDao
import com.ahmedyejam.mks.`data`.local.dao.PromptDeckDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.PromptRunDao
import com.ahmedyejam.mks.`data`.local.dao.PromptRunDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.QuestionAssetDao
import com.ahmedyejam.mks.`data`.local.dao.QuestionAssetDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.QuestionCategoryDao
import com.ahmedyejam.mks.`data`.local.dao.QuestionCategoryDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.QuestionDao
import com.ahmedyejam.mks.`data`.local.dao.QuestionDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.QuizDao
import com.ahmedyejam.mks.`data`.local.dao.QuizDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.SessionDao
import com.ahmedyejam.mks.`data`.local.dao.SessionDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.SlideshowCourseDao
import com.ahmedyejam.mks.`data`.local.dao.SlideshowCourseDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.SourceDocumentDao
import com.ahmedyejam.mks.`data`.local.dao.SourceDocumentDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.StudySessionDao
import com.ahmedyejam.mks.`data`.local.dao.StudySessionDao_Impl
import com.ahmedyejam.mks.`data`.local.dao.WorkspaceDao
import com.ahmedyejam.mks.`data`.local.dao.WorkspaceDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MksDatabase_Impl : MksDatabase() {
  private val _workspaceDao: Lazy<WorkspaceDao> = lazy {
    WorkspaceDao_Impl(this)
  }

  private val _bookDao: Lazy<BookDao> = lazy {
    BookDao_Impl(this)
  }

  private val _quizDao: Lazy<QuizDao> = lazy {
    QuizDao_Impl(this)
  }

  private val _questionDao: Lazy<QuestionDao> = lazy {
    QuestionDao_Impl(this)
  }

  private val _sessionDao: Lazy<SessionDao> = lazy {
    SessionDao_Impl(this)
  }

  private val _categoryMetadataDao: Lazy<CategoryMetadataDao> = lazy {
    CategoryMetadataDao_Impl(this)
  }

  private val _flashcardDeckDao: Lazy<FlashcardDeckDao> = lazy {
    FlashcardDeckDao_Impl(this)
  }

  private val _flashcardDao: Lazy<FlashcardDao> = lazy {
    FlashcardDao_Impl(this)
  }

  private val _learningSessionDao: Lazy<LearningSessionDao> = lazy {
    LearningSessionDao_Impl(this)
  }

  private val _slideshowCourseDao: Lazy<SlideshowCourseDao> = lazy {
    SlideshowCourseDao_Impl(this)
  }

  private val _courseSlideDao: Lazy<CourseSlideDao> = lazy {
    CourseSlideDao_Impl(this)
  }

  private val _noteBlueprintDao: Lazy<NoteBlueprintDao> = lazy {
    NoteBlueprintDao_Impl(this)
  }

  private val _promptDao: Lazy<PromptDao> = lazy {
    PromptDao_Impl(this)
  }

  private val _promptDeckDao: Lazy<PromptDeckDao> = lazy {
    PromptDeckDao_Impl(this)
  }

  private val _promptCardDao: Lazy<PromptCardDao> = lazy {
    PromptCardDao_Impl(this)
  }

  private val _promptRunDao: Lazy<PromptRunDao> = lazy {
    PromptRunDao_Impl(this)
  }

  private val _knowledgeStudySessionDao: Lazy<KnowledgeStudySessionDao> = lazy {
    KnowledgeStudySessionDao_Impl(this)
  }

  private val _questionCategoryDao: Lazy<QuestionCategoryDao> = lazy {
    QuestionCategoryDao_Impl(this)
  }

  private val _assetReferenceDao: Lazy<AssetReferenceDao> = lazy {
    AssetReferenceDao_Impl(this)
  }

  private val _questionAssetDao: Lazy<QuestionAssetDao> = lazy {
    QuestionAssetDao_Impl(this)
  }

  private val _sourceDocumentDao: Lazy<SourceDocumentDao> = lazy {
    SourceDocumentDao_Impl(this)
  }

  private val _mistakeLogDao: Lazy<MistakeLogDao> = lazy {
    MistakeLogDao_Impl(this)
  }

  private val _globalSearchDao: Lazy<GlobalSearchDao> = lazy {
    GlobalSearchDao_Impl(this)
  }

  private val _annotationDao: Lazy<AnnotationDao> = lazy {
    AnnotationDao_Impl(this)
  }

  private val _noteCollectionDao: Lazy<NoteCollectionDao> = lazy {
    NoteCollectionDao_Impl(this)
  }

  private val _studySessionDao: Lazy<StudySessionDao> = lazy {
    StudySessionDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(30,
        "fc316237538199356adceac16833d0d8", "5d4c00575515a17d1b5cfca1f6fadb99") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `workspaces` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `isDefault` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_workspaces_externalId` ON `workspaces` (`externalId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `workspace_settings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workspaceId` INTEGER NOT NULL, `language` TEXT, `theme` TEXT, `defaultSort` TEXT, `quizDefaultsJson` TEXT, `importDefaultsJson` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`workspaceId`) REFERENCES `workspaces`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_workspace_settings_workspaceId` ON `workspace_settings` (`workspaceId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `books` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workspaceId` INTEGER NOT NULL, `externalId` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `iconName` TEXT, `coverImage` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `contentUpdatedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `isSystem` INTEGER NOT NULL, `fields` TEXT NOT NULL, `questionCount` INTEGER NOT NULL, `answeredCount` INTEGER NOT NULL, `totalAttempts` INTEGER NOT NULL, `completionPercentage` REAL NOT NULL, `accuracyPercentage` REAL NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`workspaceId`) REFERENCES `workspaces`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_books_workspaceId` ON `books` (`workspaceId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_books_deletedAt` ON `books` (`deletedAt`)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_books_externalId` ON `books` (`externalId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `quizzes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `bookId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `category` TEXT, `tags` TEXT NOT NULL DEFAULT '[]', `iconName` TEXT, `coverImage` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `contentUpdatedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `isSystem` INTEGER NOT NULL, `questionCount` INTEGER NOT NULL, `answeredCount` INTEGER NOT NULL, `totalAttempts` INTEGER NOT NULL, `completionPercentage` REAL NOT NULL, `accuracyPercentage` REAL NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_quizzes_bookId` ON `quizzes` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_quizzes_deletedAt` ON `quizzes` (`deletedAt`)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_quizzes_externalId` ON `quizzes` (`externalId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `questions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `quizId` INTEGER NOT NULL, `text` TEXT NOT NULL, `type` TEXT NOT NULL, `options` TEXT NOT NULL, `correctAnswers` TEXT NOT NULL, `explanation` TEXT, `hint` TEXT, `reference` TEXT, `weight` INTEGER NOT NULL, `imagePath` TEXT, `imageName` TEXT, `imageSource` TEXT, `attempts` INTEGER NOT NULL, `correctCount` INTEGER NOT NULL, `isDropped` INTEGER NOT NULL, `droppedAt` INTEGER, `droppedReason` TEXT, `isMarked` INTEGER NOT NULL, `markedAt` INTEGER, `markReason` TEXT, `markReviewAt` INTEGER, `notes` TEXT, `categories` TEXT NOT NULL, `tags` TEXT NOT NULL DEFAULT '[]', `difficulty` TEXT, `dueAt` INTEGER NOT NULL DEFAULT 0, `reviewCount` INTEGER NOT NULL DEFAULT 0, `lastReviewedAt` INTEGER NOT NULL DEFAULT 0, `additionalInfo` TEXT, `sourceBookId` TEXT, `sourceQuizId` TEXT, `sourceQuestionId` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `timeSpentMs` INTEGER NOT NULL, `lastAttemptResult` INTEGER, `consecutiveCorrect` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`quizId`) REFERENCES `quizzes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_quizId` ON `questions` (`quizId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_isMarked` ON `questions` (`isMarked`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_isDropped` ON `questions` (`isDropped`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_markReviewAt` ON `questions` (`markReviewAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_deletedAt` ON `questions` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `quizId` INTEGER NOT NULL, `label` TEXT NOT NULL, `currentQuestionIndex` INTEGER NOT NULL, `score` INTEGER NOT NULL, `incorrectCount` INTEGER NOT NULL, `answers` TEXT NOT NULL, `answersByIndex` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `lastModifiedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `questionIds` TEXT NOT NULL, `originalQuestionCount` INTEGER NOT NULL, `shuffleQuestions` INTEGER NOT NULL, `shuffleOptions` INTEGER NOT NULL, `rapidMode` INTEGER NOT NULL, `repeatWrong` INTEGER NOT NULL, `quizTimerSeconds` INTEGER NOT NULL, `questionTimerSeconds` INTEGER NOT NULL, `rangeFrom` INTEGER NOT NULL, `rangeTo` INTEGER NOT NULL, `includeFilters` TEXT NOT NULL, `droppedOptions` TEXT NOT NULL, `droppedOptionsByIndex` TEXT NOT NULL, `visibleOptionsCount` TEXT NOT NULL, `visibleOptionsCountByIndex` TEXT NOT NULL, `currentStreak` INTEGER NOT NULL, `maxStreak` INTEGER NOT NULL, `deletedAt` INTEGER, `resultTaxonomy` TEXT NOT NULL, FOREIGN KEY(`quizId`) REFERENCES `quizzes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_sessions_quizId` ON `sessions` (`quizId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_sessions_deletedAt` ON `sessions` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `category_metadata` (`name` TEXT NOT NULL, `emoji` TEXT, `color` INTEGER, `isPinned` INTEGER NOT NULL, `deletedAt` INTEGER, PRIMARY KEY(`name`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_category_metadata_deletedAt` ON `category_metadata` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `flashcard_decks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `bookId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `iconName` TEXT, `coverImage` TEXT, `tags` TEXT NOT NULL DEFAULT '[]', `cardCount` INTEGER NOT NULL, `studiedCount` INTEGER NOT NULL, `masteryPercentage` REAL NOT NULL, `isSystem` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_flashcard_decks_bookId` ON `flashcard_decks` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_flashcard_decks_deletedAt` ON `flashcard_decks` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `flashcards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `deckId` INTEGER NOT NULL, `frontText` TEXT NOT NULL, `backText` TEXT NOT NULL, `hint` TEXT, `imagePath` TEXT, `tags` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, `attempts` INTEGER NOT NULL, `correctCount` INTEGER NOT NULL, `difficulty` TEXT, `dueAt` INTEGER NOT NULL DEFAULT 0, `reviewCount` INTEGER NOT NULL DEFAULT 0, `lastReviewedAt` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `sourceQuestionId` INTEGER, `syncConfig` TEXT NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`deckId`) REFERENCES `flashcard_decks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_flashcards_deckId` ON `flashcards` (`deckId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_flashcards_sourceQuestionId` ON `flashcards` (`sourceQuestionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_flashcards_dueAt` ON `flashcards` (`dueAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_flashcards_deletedAt` ON `flashcards` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `learning_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `label` TEXT, `stateJson` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`deckId`) REFERENCES `flashcard_decks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_learning_sessions_deckId` ON `learning_sessions` (`deckId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_learning_sessions_deletedAt` ON `learning_sessions` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `slideshow_courses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `bookId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `iconName` TEXT, `coverImage` TEXT, `tags` TEXT NOT NULL DEFAULT '[]', `slideCount` INTEGER NOT NULL, `studiedSlideCount` INTEGER NOT NULL, `progress` REAL NOT NULL, `isSystem` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `isDerived` INTEGER NOT NULL, `sourceQuizId` INTEGER, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_slideshow_courses_bookId` ON `slideshow_courses` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_slideshow_courses_deletedAt` ON `slideshow_courses` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `course_slides` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `courseId` INTEGER NOT NULL, `title` TEXT NOT NULL, `body` TEXT NOT NULL, `speakerNotes` TEXT, `imagePath` TEXT, `orderIndex` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `tags` TEXT NOT NULL DEFAULT '[]', `difficulty` TEXT, `dueAt` INTEGER NOT NULL DEFAULT 0, `reviewCount` INTEGER NOT NULL DEFAULT 0, `lastReviewedAt` INTEGER NOT NULL DEFAULT 0, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `sourceQuestionId` INTEGER, `syncConfig` TEXT NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`courseId`) REFERENCES `slideshow_courses`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_course_slides_courseId` ON `course_slides` (`courseId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_course_slides_sourceQuestionId` ON `course_slides` (`sourceQuestionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_course_slides_deletedAt` ON `course_slides` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `note_blueprints` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `collectionId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `summary` TEXT, `iconName` TEXT, `coverImage` TEXT, `body` TEXT NOT NULL, `bulletPoints` TEXT NOT NULL DEFAULT '[]', `tags` TEXT NOT NULL DEFAULT '[]', `blueprintMode` TEXT NOT NULL DEFAULT 'SIMPLE_NOTE', `linkedQuestionsJson` TEXT NOT NULL DEFAULT '[]', `linkedAssetsJson` TEXT NOT NULL DEFAULT '[]', `reviewStatus` TEXT NOT NULL DEFAULT 'NEW', `reviewCount` INTEGER NOT NULL DEFAULT 0, `lastReviewedAt` INTEGER NOT NULL DEFAULT 0, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `sourceQuestionId` INTEGER, `deletedAt` INTEGER, FOREIGN KEY(`collectionId`) REFERENCES `note_collections`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_blueprints_collectionId` ON `note_blueprints` (`collectionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_blueprints_sourceQuestionId` ON `note_blueprints` (`sourceQuestionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_blueprints_blueprintMode` ON `note_blueprints` (`blueprintMode`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_blueprints_reviewStatus` ON `note_blueprints` (`reviewStatus`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_blueprints_deletedAt` ON `note_blueprints` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `prompts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `bookId` INTEGER NOT NULL, `title` TEXT NOT NULL, `stem` TEXT NOT NULL, `conversationLinks` TEXT NOT NULL, `usageCount` INTEGER NOT NULL, `lastUsedAt` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompts_bookId` ON `prompts` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompts_deletedAt` ON `prompts` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `prompt_decks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `iconName` TEXT, `coverImage` TEXT, `tags` TEXT NOT NULL DEFAULT '[]', `createdAt` INTEGER NOT NULL DEFAULT 0, `updatedAt` INTEGER NOT NULL DEFAULT 0, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_decks_bookId` ON `prompt_decks` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_decks_deletedAt` ON `prompt_decks` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `prompt_cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `title` TEXT NOT NULL, `promptText` TEXT NOT NULL, `variablesJson` TEXT, `outputType` TEXT NOT NULL, `tags` TEXT NOT NULL DEFAULT '[]', `usageCount` INTEGER NOT NULL DEFAULT 0, `lastUsedAt` INTEGER, `sortOrder` INTEGER NOT NULL DEFAULT 0, `createdAt` INTEGER NOT NULL DEFAULT 0, `updatedAt` INTEGER NOT NULL DEFAULT 0, `deletedAt` INTEGER, FOREIGN KEY(`deckId`) REFERENCES `prompt_decks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_cards_deckId` ON `prompt_cards` (`deckId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_cards_outputType` ON `prompt_cards` (`outputType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_cards_deletedAt` ON `prompt_cards` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `prompt_runs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `promptCardId` INTEGER NOT NULL, `inputValuesJson` TEXT NOT NULL, `renderedPrompt` TEXT NOT NULL, `outputText` TEXT, `linkedAssetType` TEXT, `linkedAssetId` INTEGER, `createdAt` INTEGER NOT NULL DEFAULT 0, `deletedAt` INTEGER, FOREIGN KEY(`promptCardId`) REFERENCES `prompt_cards`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_runs_promptCardId` ON `prompt_runs` (`promptCardId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_runs_createdAt` ON `prompt_runs` (`createdAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_prompt_runs_deletedAt` ON `prompt_runs` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `knowledge_study_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `targetType` TEXT NOT NULL, `targetId` INTEGER NOT NULL, `stateJson` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_knowledge_study_sessions_deletedAt` ON `knowledge_study_sessions` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `question_categories` (`questionId` INTEGER NOT NULL, `category` TEXT NOT NULL, `deletedAt` INTEGER, PRIMARY KEY(`questionId`, `category`), FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_categories_questionId` ON `question_categories` (`questionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_categories_category` ON `question_categories` (`category`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_categories_deletedAt` ON `question_categories` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `asset_references` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `path` TEXT NOT NULL, `ownerType` TEXT NOT NULL, `ownerId` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `deletedAt` INTEGER)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_asset_references_path` ON `asset_references` (`path`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_asset_references_ownerType_ownerId` ON `asset_references` (`ownerType`, `ownerId`)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_asset_references_ownerType_ownerId_path` ON `asset_references` (`ownerType`, `ownerId`, `path`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_asset_references_deletedAt` ON `asset_references` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `question_assets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER NOT NULL, `quizId` INTEGER NOT NULL, `questionId` INTEGER NOT NULL, `assetType` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `localPath` TEXT, `externalUrl` TEXT, `mimeType` TEXT, `fileName` TEXT, `fileSizeBytes` INTEGER, `textContent` TEXT, `sourceDocumentId` INTEGER, `sourcePage` TEXT, `sourceQuote` TEXT, `sortOrder` INTEGER NOT NULL DEFAULT 0, `isPinned` INTEGER NOT NULL DEFAULT 0, `isPrimary` INTEGER NOT NULL DEFAULT 0, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_bookId` ON `question_assets` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_quizId` ON `question_assets` (`quizId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_questionId` ON `question_assets` (`questionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_assetType` ON `question_assets` (`assetType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_createdAt` ON `question_assets` (`createdAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_sourceDocumentId` ON `question_assets` (`sourceDocumentId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_question_assets_deletedAt` ON `question_assets` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `source_documents` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER, `title` TEXT NOT NULL, `sourceType` TEXT NOT NULL, `author` TEXT, `edition` TEXT, `year` TEXT, `publisher` TEXT, `localPath` TEXT, `externalUrl` TEXT, `description` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_source_documents_bookId` ON `source_documents` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_source_documents_title` ON `source_documents` (`title`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_source_documents_sourceType` ON `source_documents` (`sourceType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_source_documents_deletedAt` ON `source_documents` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `mistake_log_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bookId` INTEGER NOT NULL, `quizId` INTEGER NOT NULL, `questionId` INTEGER NOT NULL, `sessionId` INTEGER, `selectedAnswer` TEXT, `correctAnswer` TEXT, `userReason` TEXT, `correctConcept` TEXT, `preventionNote` TEXT, `linkedFlashcardId` INTEGER, `linkedBlueprintId` INTEGER, `linkedAssetId` INTEGER, `isFixed` INTEGER NOT NULL DEFAULT 0, `reviewAt` INTEGER, `createdAt` INTEGER NOT NULL DEFAULT 0, `updatedAt` INTEGER NOT NULL DEFAULT 0, `deletedAt` INTEGER, FOREIGN KEY(`questionId`) REFERENCES `questions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_bookId` ON `mistake_log_entries` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_quizId` ON `mistake_log_entries` (`quizId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_questionId` ON `mistake_log_entries` (`questionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_sessionId` ON `mistake_log_entries` (`sessionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_reviewAt` ON `mistake_log_entries` (`reviewAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_isFixed` ON `mistake_log_entries` (`isFixed`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_createdAt` ON `mistake_log_entries` (`createdAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mistake_log_entries_deletedAt` ON `mistake_log_entries` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `annotations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workspaceId` INTEGER NOT NULL, `bookId` INTEGER NOT NULL, `ownerType` TEXT NOT NULL, `ownerId` INTEGER NOT NULL, `selectedText` TEXT, `noteBody` TEXT, `colorLabel` TEXT NOT NULL DEFAULT 'YELLOW', `positionDataJson` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`workspaceId`) REFERENCES `workspaces`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_annotations_workspaceId` ON `annotations` (`workspaceId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_annotations_bookId` ON `annotations` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_annotations_ownerType_ownerId` ON `annotations` (`ownerType`, `ownerId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_annotations_colorLabel` ON `annotations` (`colorLabel`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_annotations_deletedAt` ON `annotations` (`deletedAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_annotations_updatedAt` ON `annotations` (`updatedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `note_collections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `externalId` TEXT NOT NULL, `bookId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `iconName` TEXT, `coverImage` TEXT, `tags` TEXT NOT NULL DEFAULT '[]', `noteCount` INTEGER NOT NULL, `isSystem` INTEGER NOT NULL, `isPinned` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `lastStudiedAt` INTEGER NOT NULL, `lastEditedAt` INTEGER NOT NULL, `deletedAt` INTEGER, FOREIGN KEY(`bookId`) REFERENCES `books`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_collections_bookId` ON `note_collections` (`bookId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_note_collections_deletedAt` ON `note_collections` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `study_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `targetType` TEXT NOT NULL, `targetId` INTEGER NOT NULL, `label` TEXT, `stateJson` TEXT NOT NULL, `timeSpentMs` INTEGER NOT NULL, `completionPercentage` REAL NOT NULL, `isCompleted` INTEGER NOT NULL, `correctCount` INTEGER NOT NULL, `incorrectCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `deletedAt` INTEGER)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_study_sessions_targetId_targetType` ON `study_sessions` (`targetId`, `targetType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_study_sessions_deletedAt` ON `study_sessions` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fc316237538199356adceac16833d0d8')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `workspaces`")
        connection.execSQL("DROP TABLE IF EXISTS `workspace_settings`")
        connection.execSQL("DROP TABLE IF EXISTS `books`")
        connection.execSQL("DROP TABLE IF EXISTS `quizzes`")
        connection.execSQL("DROP TABLE IF EXISTS `questions`")
        connection.execSQL("DROP TABLE IF EXISTS `sessions`")
        connection.execSQL("DROP TABLE IF EXISTS `category_metadata`")
        connection.execSQL("DROP TABLE IF EXISTS `flashcard_decks`")
        connection.execSQL("DROP TABLE IF EXISTS `flashcards`")
        connection.execSQL("DROP TABLE IF EXISTS `learning_sessions`")
        connection.execSQL("DROP TABLE IF EXISTS `slideshow_courses`")
        connection.execSQL("DROP TABLE IF EXISTS `course_slides`")
        connection.execSQL("DROP TABLE IF EXISTS `note_blueprints`")
        connection.execSQL("DROP TABLE IF EXISTS `prompts`")
        connection.execSQL("DROP TABLE IF EXISTS `prompt_decks`")
        connection.execSQL("DROP TABLE IF EXISTS `prompt_cards`")
        connection.execSQL("DROP TABLE IF EXISTS `prompt_runs`")
        connection.execSQL("DROP TABLE IF EXISTS `knowledge_study_sessions`")
        connection.execSQL("DROP TABLE IF EXISTS `question_categories`")
        connection.execSQL("DROP TABLE IF EXISTS `asset_references`")
        connection.execSQL("DROP TABLE IF EXISTS `question_assets`")
        connection.execSQL("DROP TABLE IF EXISTS `source_documents`")
        connection.execSQL("DROP TABLE IF EXISTS `mistake_log_entries`")
        connection.execSQL("DROP TABLE IF EXISTS `annotations`")
        connection.execSQL("DROP TABLE IF EXISTS `note_collections`")
        connection.execSQL("DROP TABLE IF EXISTS `study_sessions`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsWorkspaces: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWorkspaces.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("isDefault", TableInfo.Column("isDefault", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaces.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWorkspaces: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWorkspaces: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesWorkspaces.add(TableInfo.Index("index_workspaces_externalId", true,
            listOf("externalId"), listOf("ASC")))
        val _infoWorkspaces: TableInfo = TableInfo("workspaces", _columnsWorkspaces,
            _foreignKeysWorkspaces, _indicesWorkspaces)
        val _existingWorkspaces: TableInfo = read(connection, "workspaces")
        if (!_infoWorkspaces.equals(_existingWorkspaces)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |workspaces(com.ahmedyejam.mks.data.local.entity.WorkspaceEntity).
              | Expected:
              |""".trimMargin() + _infoWorkspaces + """
              |
              | Found:
              |""".trimMargin() + _existingWorkspaces)
        }
        val _columnsWorkspaceSettings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWorkspaceSettings.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("workspaceId", TableInfo.Column("workspaceId", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("language", TableInfo.Column("language", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("theme", TableInfo.Column("theme", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("defaultSort", TableInfo.Column("defaultSort", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("quizDefaultsJson", TableInfo.Column("quizDefaultsJson",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("importDefaultsJson", TableInfo.Column("importDefaultsJson",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWorkspaceSettings.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWorkspaceSettings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysWorkspaceSettings.add(TableInfo.ForeignKey("workspaces", "CASCADE", "NO ACTION",
            listOf("workspaceId"), listOf("id")))
        val _indicesWorkspaceSettings: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesWorkspaceSettings.add(TableInfo.Index("index_workspace_settings_workspaceId", false,
            listOf("workspaceId"), listOf("ASC")))
        val _infoWorkspaceSettings: TableInfo = TableInfo("workspace_settings",
            _columnsWorkspaceSettings, _foreignKeysWorkspaceSettings, _indicesWorkspaceSettings)
        val _existingWorkspaceSettings: TableInfo = read(connection, "workspace_settings")
        if (!_infoWorkspaceSettings.equals(_existingWorkspaceSettings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |workspace_settings(com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity).
              | Expected:
              |""".trimMargin() + _infoWorkspaceSettings + """
              |
              | Found:
              |""".trimMargin() + _existingWorkspaceSettings)
        }
        val _columnsBooks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBooks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("workspaceId", TableInfo.Column("workspaceId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("contentUpdatedAt", TableInfo.Column("contentUpdatedAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("isSystem", TableInfo.Column("isSystem", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("fields", TableInfo.Column("fields", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("questionCount", TableInfo.Column("questionCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("answeredCount", TableInfo.Column("answeredCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("totalAttempts", TableInfo.Column("totalAttempts", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("completionPercentage", TableInfo.Column("completionPercentage", "REAL",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("accuracyPercentage", TableInfo.Column("accuracyPercentage", "REAL", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBooks.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBooks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysBooks.add(TableInfo.ForeignKey("workspaces", "CASCADE", "NO ACTION",
            listOf("workspaceId"), listOf("id")))
        val _indicesBooks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBooks.add(TableInfo.Index("index_books_workspaceId", false, listOf("workspaceId"),
            listOf("ASC")))
        _indicesBooks.add(TableInfo.Index("index_books_deletedAt", false, listOf("deletedAt"),
            listOf("ASC")))
        _indicesBooks.add(TableInfo.Index("index_books_externalId", true, listOf("externalId"),
            listOf("ASC")))
        val _infoBooks: TableInfo = TableInfo("books", _columnsBooks, _foreignKeysBooks,
            _indicesBooks)
        val _existingBooks: TableInfo = read(connection, "books")
        if (!_infoBooks.equals(_existingBooks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |books(com.ahmedyejam.mks.data.local.entity.BookEntity).
              | Expected:
              |""".trimMargin() + _infoBooks + """
              |
              | Found:
              |""".trimMargin() + _existingBooks)
        }
        val _columnsQuizzes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsQuizzes.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("category", TableInfo.Column("category", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("contentUpdatedAt", TableInfo.Column("contentUpdatedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("isSystem", TableInfo.Column("isSystem", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("questionCount", TableInfo.Column("questionCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("answeredCount", TableInfo.Column("answeredCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("totalAttempts", TableInfo.Column("totalAttempts", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("completionPercentage", TableInfo.Column("completionPercentage", "REAL",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("accuracyPercentage", TableInfo.Column("accuracyPercentage", "REAL",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuizzes.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysQuizzes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysQuizzes.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesQuizzes: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesQuizzes.add(TableInfo.Index("index_quizzes_bookId", false, listOf("bookId"),
            listOf("ASC")))
        _indicesQuizzes.add(TableInfo.Index("index_quizzes_deletedAt", false, listOf("deletedAt"),
            listOf("ASC")))
        _indicesQuizzes.add(TableInfo.Index("index_quizzes_externalId", true, listOf("externalId"),
            listOf("ASC")))
        val _infoQuizzes: TableInfo = TableInfo("quizzes", _columnsQuizzes, _foreignKeysQuizzes,
            _indicesQuizzes)
        val _existingQuizzes: TableInfo = read(connection, "quizzes")
        if (!_infoQuizzes.equals(_existingQuizzes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |quizzes(com.ahmedyejam.mks.data.local.entity.QuizEntity).
              | Expected:
              |""".trimMargin() + _infoQuizzes + """
              |
              | Found:
              |""".trimMargin() + _existingQuizzes)
        }
        val _columnsQuestions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsQuestions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("quizId", TableInfo.Column("quizId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("text", TableInfo.Column("text", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("options", TableInfo.Column("options", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("correctAnswers", TableInfo.Column("correctAnswers", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("explanation", TableInfo.Column("explanation", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("hint", TableInfo.Column("hint", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("reference", TableInfo.Column("reference", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("weight", TableInfo.Column("weight", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("imagePath", TableInfo.Column("imagePath", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("imageName", TableInfo.Column("imageName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("imageSource", TableInfo.Column("imageSource", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("attempts", TableInfo.Column("attempts", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("correctCount", TableInfo.Column("correctCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("isDropped", TableInfo.Column("isDropped", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("droppedAt", TableInfo.Column("droppedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("droppedReason", TableInfo.Column("droppedReason", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("isMarked", TableInfo.Column("isMarked", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("markedAt", TableInfo.Column("markedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("markReason", TableInfo.Column("markReason", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("markReviewAt", TableInfo.Column("markReviewAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("notes", TableInfo.Column("notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("categories", TableInfo.Column("categories", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("difficulty", TableInfo.Column("difficulty", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("dueAt", TableInfo.Column("dueAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("reviewCount", TableInfo.Column("reviewCount", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("lastReviewedAt", TableInfo.Column("lastReviewedAt", "INTEGER", true,
            0, "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("additionalInfo", TableInfo.Column("additionalInfo", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("sourceBookId", TableInfo.Column("sourceBookId", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("sourceQuizId", TableInfo.Column("sourceQuizId", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("sourceQuestionId", TableInfo.Column("sourceQuestionId", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("timeSpentMs", TableInfo.Column("timeSpentMs", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("lastAttemptResult", TableInfo.Column("lastAttemptResult", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("consecutiveCorrect", TableInfo.Column("consecutiveCorrect",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestions.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysQuestions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysQuestions.add(TableInfo.ForeignKey("quizzes", "CASCADE", "NO ACTION",
            listOf("quizId"), listOf("id")))
        val _indicesQuestions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesQuestions.add(TableInfo.Index("index_questions_quizId", false, listOf("quizId"),
            listOf("ASC")))
        _indicesQuestions.add(TableInfo.Index("index_questions_isMarked", false, listOf("isMarked"),
            listOf("ASC")))
        _indicesQuestions.add(TableInfo.Index("index_questions_isDropped", false,
            listOf("isDropped"), listOf("ASC")))
        _indicesQuestions.add(TableInfo.Index("index_questions_markReviewAt", false,
            listOf("markReviewAt"), listOf("ASC")))
        _indicesQuestions.add(TableInfo.Index("index_questions_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoQuestions: TableInfo = TableInfo("questions", _columnsQuestions,
            _foreignKeysQuestions, _indicesQuestions)
        val _existingQuestions: TableInfo = read(connection, "questions")
        if (!_infoQuestions.equals(_existingQuestions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |questions(com.ahmedyejam.mks.data.local.entity.QuestionEntity).
              | Expected:
              |""".trimMargin() + _infoQuestions + """
              |
              | Found:
              |""".trimMargin() + _existingQuestions)
        }
        val _columnsSessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSessions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("quizId", TableInfo.Column("quizId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("label", TableInfo.Column("label", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("currentQuestionIndex", TableInfo.Column("currentQuestionIndex",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("score", TableInfo.Column("score", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("incorrectCount", TableInfo.Column("incorrectCount", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("answers", TableInfo.Column("answers", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("answersByIndex", TableInfo.Column("answersByIndex", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("lastModifiedAt", TableInfo.Column("lastModifiedAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("questionIds", TableInfo.Column("questionIds", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("originalQuestionCount", TableInfo.Column("originalQuestionCount",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("shuffleQuestions", TableInfo.Column("shuffleQuestions", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("shuffleOptions", TableInfo.Column("shuffleOptions", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("rapidMode", TableInfo.Column("rapidMode", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("repeatWrong", TableInfo.Column("repeatWrong", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("quizTimerSeconds", TableInfo.Column("quizTimerSeconds", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("questionTimerSeconds", TableInfo.Column("questionTimerSeconds",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("rangeFrom", TableInfo.Column("rangeFrom", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("rangeTo", TableInfo.Column("rangeTo", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("includeFilters", TableInfo.Column("includeFilters", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("droppedOptions", TableInfo.Column("droppedOptions", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("droppedOptionsByIndex", TableInfo.Column("droppedOptionsByIndex",
            "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("visibleOptionsCount", TableInfo.Column("visibleOptionsCount", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("visibleOptionsCountByIndex",
            TableInfo.Column("visibleOptionsCountByIndex", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("currentStreak", TableInfo.Column("currentStreak", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("maxStreak", TableInfo.Column("maxStreak", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSessions.put("resultTaxonomy", TableInfo.Column("resultTaxonomy", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysSessions.add(TableInfo.ForeignKey("quizzes", "CASCADE", "NO ACTION",
            listOf("quizId"), listOf("id")))
        val _indicesSessions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSessions.add(TableInfo.Index("index_sessions_quizId", false, listOf("quizId"),
            listOf("ASC")))
        _indicesSessions.add(TableInfo.Index("index_sessions_deletedAt", false, listOf("deletedAt"),
            listOf("ASC")))
        val _infoSessions: TableInfo = TableInfo("sessions", _columnsSessions, _foreignKeysSessions,
            _indicesSessions)
        val _existingSessions: TableInfo = read(connection, "sessions")
        if (!_infoSessions.equals(_existingSessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sessions(com.ahmedyejam.mks.data.local.entity.SessionEntity).
              | Expected:
              |""".trimMargin() + _infoSessions + """
              |
              | Found:
              |""".trimMargin() + _existingSessions)
        }
        val _columnsCategoryMetadata: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCategoryMetadata.put("name", TableInfo.Column("name", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryMetadata.put("emoji", TableInfo.Column("emoji", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryMetadata.put("color", TableInfo.Column("color", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryMetadata.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryMetadata.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCategoryMetadata: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCategoryMetadata: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCategoryMetadata.add(TableInfo.Index("index_category_metadata_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoCategoryMetadata: TableInfo = TableInfo("category_metadata",
            _columnsCategoryMetadata, _foreignKeysCategoryMetadata, _indicesCategoryMetadata)
        val _existingCategoryMetadata: TableInfo = read(connection, "category_metadata")
        if (!_infoCategoryMetadata.equals(_existingCategoryMetadata)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |category_metadata(com.ahmedyejam.mks.data.local.entity.CategoryMetadataEntity).
              | Expected:
              |""".trimMargin() + _infoCategoryMetadata + """
              |
              | Found:
              |""".trimMargin() + _existingCategoryMetadata)
        }
        val _columnsFlashcardDecks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFlashcardDecks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("cardCount", TableInfo.Column("cardCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("studiedCount", TableInfo.Column("studiedCount", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("masteryPercentage", TableInfo.Column("masteryPercentage",
            "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("isSystem", TableInfo.Column("isSystem", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcardDecks.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFlashcardDecks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysFlashcardDecks.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesFlashcardDecks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFlashcardDecks.add(TableInfo.Index("index_flashcard_decks_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesFlashcardDecks.add(TableInfo.Index("index_flashcard_decks_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoFlashcardDecks: TableInfo = TableInfo("flashcard_decks", _columnsFlashcardDecks,
            _foreignKeysFlashcardDecks, _indicesFlashcardDecks)
        val _existingFlashcardDecks: TableInfo = read(connection, "flashcard_decks")
        if (!_infoFlashcardDecks.equals(_existingFlashcardDecks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |flashcard_decks(com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity).
              | Expected:
              |""".trimMargin() + _infoFlashcardDecks + """
              |
              | Found:
              |""".trimMargin() + _existingFlashcardDecks)
        }
        val _columnsFlashcards: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFlashcards.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("deckId", TableInfo.Column("deckId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("frontText", TableInfo.Column("frontText", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("backText", TableInfo.Column("backText", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("hint", TableInfo.Column("hint", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("imagePath", TableInfo.Column("imagePath", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("tags", TableInfo.Column("tags", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("orderIndex", TableInfo.Column("orderIndex", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("attempts", TableInfo.Column("attempts", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("correctCount", TableInfo.Column("correctCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("difficulty", TableInfo.Column("difficulty", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("dueAt", TableInfo.Column("dueAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("reviewCount", TableInfo.Column("reviewCount", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("lastReviewedAt", TableInfo.Column("lastReviewedAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("sourceQuestionId", TableInfo.Column("sourceQuestionId", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("syncConfig", TableInfo.Column("syncConfig", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFlashcards.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFlashcards: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysFlashcards.add(TableInfo.ForeignKey("flashcard_decks", "CASCADE", "NO ACTION",
            listOf("deckId"), listOf("id")))
        val _indicesFlashcards: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFlashcards.add(TableInfo.Index("index_flashcards_deckId", false, listOf("deckId"),
            listOf("ASC")))
        _indicesFlashcards.add(TableInfo.Index("index_flashcards_sourceQuestionId", false,
            listOf("sourceQuestionId"), listOf("ASC")))
        _indicesFlashcards.add(TableInfo.Index("index_flashcards_dueAt", false, listOf("dueAt"),
            listOf("ASC")))
        _indicesFlashcards.add(TableInfo.Index("index_flashcards_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoFlashcards: TableInfo = TableInfo("flashcards", _columnsFlashcards,
            _foreignKeysFlashcards, _indicesFlashcards)
        val _existingFlashcards: TableInfo = read(connection, "flashcards")
        if (!_infoFlashcards.equals(_existingFlashcards)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |flashcards(com.ahmedyejam.mks.data.local.entity.FlashcardEntity).
              | Expected:
              |""".trimMargin() + _infoFlashcards + """
              |
              | Found:
              |""".trimMargin() + _existingFlashcards)
        }
        val _columnsLearningSessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsLearningSessions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("deckId", TableInfo.Column("deckId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("label", TableInfo.Column("label", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("stateJson", TableInfo.Column("stateJson", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLearningSessions.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysLearningSessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysLearningSessions.add(TableInfo.ForeignKey("flashcard_decks", "CASCADE",
            "NO ACTION", listOf("deckId"), listOf("id")))
        val _indicesLearningSessions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesLearningSessions.add(TableInfo.Index("index_learning_sessions_deckId", false,
            listOf("deckId"), listOf("ASC")))
        _indicesLearningSessions.add(TableInfo.Index("index_learning_sessions_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoLearningSessions: TableInfo = TableInfo("learning_sessions",
            _columnsLearningSessions, _foreignKeysLearningSessions, _indicesLearningSessions)
        val _existingLearningSessions: TableInfo = read(connection, "learning_sessions")
        if (!_infoLearningSessions.equals(_existingLearningSessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |learning_sessions(com.ahmedyejam.mks.data.local.entity.LearningSessionEntity).
              | Expected:
              |""".trimMargin() + _infoLearningSessions + """
              |
              | Found:
              |""".trimMargin() + _existingLearningSessions)
        }
        val _columnsSlideshowCourses: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSlideshowCourses.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("description", TableInfo.Column("description", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("slideCount", TableInfo.Column("slideCount", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("studiedSlideCount", TableInfo.Column("studiedSlideCount",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("progress", TableInfo.Column("progress", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("isSystem", TableInfo.Column("isSystem", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("isDerived", TableInfo.Column("isDerived", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("sourceQuizId", TableInfo.Column("sourceQuizId", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSlideshowCourses.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSlideshowCourses: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysSlideshowCourses.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesSlideshowCourses: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSlideshowCourses.add(TableInfo.Index("index_slideshow_courses_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesSlideshowCourses.add(TableInfo.Index("index_slideshow_courses_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoSlideshowCourses: TableInfo = TableInfo("slideshow_courses",
            _columnsSlideshowCourses, _foreignKeysSlideshowCourses, _indicesSlideshowCourses)
        val _existingSlideshowCourses: TableInfo = read(connection, "slideshow_courses")
        if (!_infoSlideshowCourses.equals(_existingSlideshowCourses)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |slideshow_courses(com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity).
              | Expected:
              |""".trimMargin() + _infoSlideshowCourses + """
              |
              | Found:
              |""".trimMargin() + _existingSlideshowCourses)
        }
        val _columnsCourseSlides: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCourseSlides.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("courseId", TableInfo.Column("courseId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("body", TableInfo.Column("body", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("speakerNotes", TableInfo.Column("speakerNotes", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("imagePath", TableInfo.Column("imagePath", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("orderIndex", TableInfo.Column("orderIndex", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("difficulty", TableInfo.Column("difficulty", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("dueAt", TableInfo.Column("dueAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("reviewCount", TableInfo.Column("reviewCount", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("lastReviewedAt", TableInfo.Column("lastReviewedAt", "INTEGER",
            true, 0, "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("sourceQuestionId", TableInfo.Column("sourceQuestionId", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("syncConfig", TableInfo.Column("syncConfig", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCourseSlides.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCourseSlides: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCourseSlides.add(TableInfo.ForeignKey("slideshow_courses", "CASCADE",
            "NO ACTION", listOf("courseId"), listOf("id")))
        val _indicesCourseSlides: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCourseSlides.add(TableInfo.Index("index_course_slides_courseId", false,
            listOf("courseId"), listOf("ASC")))
        _indicesCourseSlides.add(TableInfo.Index("index_course_slides_sourceQuestionId", false,
            listOf("sourceQuestionId"), listOf("ASC")))
        _indicesCourseSlides.add(TableInfo.Index("index_course_slides_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoCourseSlides: TableInfo = TableInfo("course_slides", _columnsCourseSlides,
            _foreignKeysCourseSlides, _indicesCourseSlides)
        val _existingCourseSlides: TableInfo = read(connection, "course_slides")
        if (!_infoCourseSlides.equals(_existingCourseSlides)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |course_slides(com.ahmedyejam.mks.data.local.entity.CourseSlideEntity).
              | Expected:
              |""".trimMargin() + _infoCourseSlides + """
              |
              | Found:
              |""".trimMargin() + _existingCourseSlides)
        }
        val _columnsNoteBlueprints: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsNoteBlueprints.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("collectionId", TableInfo.Column("collectionId", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("summary", TableInfo.Column("summary", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("body", TableInfo.Column("body", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("bulletPoints", TableInfo.Column("bulletPoints", "TEXT", true, 0,
            "'[]'", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("blueprintMode", TableInfo.Column("blueprintMode", "TEXT", true,
            0, "'SIMPLE_NOTE'", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("linkedQuestionsJson", TableInfo.Column("linkedQuestionsJson",
            "TEXT", true, 0, "'[]'", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("linkedAssetsJson", TableInfo.Column("linkedAssetsJson", "TEXT",
            true, 0, "'[]'", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("reviewStatus", TableInfo.Column("reviewStatus", "TEXT", true, 0,
            "'NEW'", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("reviewCount", TableInfo.Column("reviewCount", "INTEGER", true,
            0, "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("lastReviewedAt", TableInfo.Column("lastReviewedAt", "INTEGER",
            true, 0, "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("sourceQuestionId", TableInfo.Column("sourceQuestionId",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteBlueprints.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysNoteBlueprints: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysNoteBlueprints.add(TableInfo.ForeignKey("note_collections", "CASCADE",
            "NO ACTION", listOf("collectionId"), listOf("id")))
        val _indicesNoteBlueprints: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesNoteBlueprints.add(TableInfo.Index("index_note_blueprints_collectionId", false,
            listOf("collectionId"), listOf("ASC")))
        _indicesNoteBlueprints.add(TableInfo.Index("index_note_blueprints_sourceQuestionId", false,
            listOf("sourceQuestionId"), listOf("ASC")))
        _indicesNoteBlueprints.add(TableInfo.Index("index_note_blueprints_blueprintMode", false,
            listOf("blueprintMode"), listOf("ASC")))
        _indicesNoteBlueprints.add(TableInfo.Index("index_note_blueprints_reviewStatus", false,
            listOf("reviewStatus"), listOf("ASC")))
        _indicesNoteBlueprints.add(TableInfo.Index("index_note_blueprints_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoNoteBlueprints: TableInfo = TableInfo("note_blueprints", _columnsNoteBlueprints,
            _foreignKeysNoteBlueprints, _indicesNoteBlueprints)
        val _existingNoteBlueprints: TableInfo = read(connection, "note_blueprints")
        if (!_infoNoteBlueprints.equals(_existingNoteBlueprints)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |note_blueprints(com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity).
              | Expected:
              |""".trimMargin() + _infoNoteBlueprints + """
              |
              | Found:
              |""".trimMargin() + _existingNoteBlueprints)
        }
        val _columnsPrompts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPrompts.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("stem", TableInfo.Column("stem", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("conversationLinks", TableInfo.Column("conversationLinks", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("usageCount", TableInfo.Column("usageCount", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("lastUsedAt", TableInfo.Column("lastUsedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrompts.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPrompts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPrompts.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesPrompts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPrompts.add(TableInfo.Index("index_prompts_bookId", false, listOf("bookId"),
            listOf("ASC")))
        _indicesPrompts.add(TableInfo.Index("index_prompts_deletedAt", false, listOf("deletedAt"),
            listOf("ASC")))
        val _infoPrompts: TableInfo = TableInfo("prompts", _columnsPrompts, _foreignKeysPrompts,
            _indicesPrompts)
        val _existingPrompts: TableInfo = read(connection, "prompts")
        if (!_infoPrompts.equals(_existingPrompts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |prompts(com.ahmedyejam.mks.data.local.entity.PromptEntity).
              | Expected:
              |""".trimMargin() + _infoPrompts + """
              |
              | Found:
              |""".trimMargin() + _existingPrompts)
        }
        val _columnsPromptDecks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPromptDecks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptDecks.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPromptDecks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPromptDecks.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesPromptDecks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPromptDecks.add(TableInfo.Index("index_prompt_decks_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesPromptDecks.add(TableInfo.Index("index_prompt_decks_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoPromptDecks: TableInfo = TableInfo("prompt_decks", _columnsPromptDecks,
            _foreignKeysPromptDecks, _indicesPromptDecks)
        val _existingPromptDecks: TableInfo = read(connection, "prompt_decks")
        if (!_infoPromptDecks.equals(_existingPromptDecks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |prompt_decks(com.ahmedyejam.mks.data.local.entity.PromptDeckEntity).
              | Expected:
              |""".trimMargin() + _infoPromptDecks + """
              |
              | Found:
              |""".trimMargin() + _existingPromptDecks)
        }
        val _columnsPromptCards: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPromptCards.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("deckId", TableInfo.Column("deckId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("promptText", TableInfo.Column("promptText", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("variablesJson", TableInfo.Column("variablesJson", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("outputType", TableInfo.Column("outputType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("usageCount", TableInfo.Column("usageCount", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("lastUsedAt", TableInfo.Column("lastUsedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("sortOrder", TableInfo.Column("sortOrder", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptCards.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPromptCards: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPromptCards.add(TableInfo.ForeignKey("prompt_decks", "CASCADE", "NO ACTION",
            listOf("deckId"), listOf("id")))
        val _indicesPromptCards: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPromptCards.add(TableInfo.Index("index_prompt_cards_deckId", false,
            listOf("deckId"), listOf("ASC")))
        _indicesPromptCards.add(TableInfo.Index("index_prompt_cards_outputType", false,
            listOf("outputType"), listOf("ASC")))
        _indicesPromptCards.add(TableInfo.Index("index_prompt_cards_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoPromptCards: TableInfo = TableInfo("prompt_cards", _columnsPromptCards,
            _foreignKeysPromptCards, _indicesPromptCards)
        val _existingPromptCards: TableInfo = read(connection, "prompt_cards")
        if (!_infoPromptCards.equals(_existingPromptCards)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |prompt_cards(com.ahmedyejam.mks.data.local.entity.PromptCardEntity).
              | Expected:
              |""".trimMargin() + _infoPromptCards + """
              |
              | Found:
              |""".trimMargin() + _existingPromptCards)
        }
        val _columnsPromptRuns: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPromptRuns.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("promptCardId", TableInfo.Column("promptCardId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("inputValuesJson", TableInfo.Column("inputValuesJson", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("renderedPrompt", TableInfo.Column("renderedPrompt", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("outputText", TableInfo.Column("outputText", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("linkedAssetType", TableInfo.Column("linkedAssetType", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("linkedAssetId", TableInfo.Column("linkedAssetId", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPromptRuns.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPromptRuns: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPromptRuns.add(TableInfo.ForeignKey("prompt_cards", "CASCADE", "NO ACTION",
            listOf("promptCardId"), listOf("id")))
        val _indicesPromptRuns: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPromptRuns.add(TableInfo.Index("index_prompt_runs_promptCardId", false,
            listOf("promptCardId"), listOf("ASC")))
        _indicesPromptRuns.add(TableInfo.Index("index_prompt_runs_createdAt", false,
            listOf("createdAt"), listOf("ASC")))
        _indicesPromptRuns.add(TableInfo.Index("index_prompt_runs_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoPromptRuns: TableInfo = TableInfo("prompt_runs", _columnsPromptRuns,
            _foreignKeysPromptRuns, _indicesPromptRuns)
        val _existingPromptRuns: TableInfo = read(connection, "prompt_runs")
        if (!_infoPromptRuns.equals(_existingPromptRuns)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |prompt_runs(com.ahmedyejam.mks.data.local.entity.PromptRunEntity).
              | Expected:
              |""".trimMargin() + _infoPromptRuns + """
              |
              | Found:
              |""".trimMargin() + _existingPromptRuns)
        }
        val _columnsKnowledgeStudySessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsKnowledgeStudySessions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("targetType", TableInfo.Column("targetType", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("targetId", TableInfo.Column("targetId", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("stateJson", TableInfo.Column("stateJson", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("createdAt", TableInfo.Column("createdAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsKnowledgeStudySessions.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysKnowledgeStudySessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesKnowledgeStudySessions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesKnowledgeStudySessions.add(TableInfo.Index("index_knowledge_study_sessions_deletedAt",
            false, listOf("deletedAt"), listOf("ASC")))
        val _infoKnowledgeStudySessions: TableInfo = TableInfo("knowledge_study_sessions",
            _columnsKnowledgeStudySessions, _foreignKeysKnowledgeStudySessions,
            _indicesKnowledgeStudySessions)
        val _existingKnowledgeStudySessions: TableInfo = read(connection,
            "knowledge_study_sessions")
        if (!_infoKnowledgeStudySessions.equals(_existingKnowledgeStudySessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |knowledge_study_sessions(com.ahmedyejam.mks.data.local.entity.KnowledgeStudySessionEntity).
              | Expected:
              |""".trimMargin() + _infoKnowledgeStudySessions + """
              |
              | Found:
              |""".trimMargin() + _existingKnowledgeStudySessions)
        }
        val _columnsQuestionCategories: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsQuestionCategories.put("questionId", TableInfo.Column("questionId", "INTEGER", true,
            1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionCategories.put("category", TableInfo.Column("category", "TEXT", true, 2,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionCategories.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysQuestionCategories: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysQuestionCategories.add(TableInfo.ForeignKey("questions", "CASCADE", "NO ACTION",
            listOf("questionId"), listOf("id")))
        val _indicesQuestionCategories: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesQuestionCategories.add(TableInfo.Index("index_question_categories_questionId",
            false, listOf("questionId"), listOf("ASC")))
        _indicesQuestionCategories.add(TableInfo.Index("index_question_categories_category", false,
            listOf("category"), listOf("ASC")))
        _indicesQuestionCategories.add(TableInfo.Index("index_question_categories_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoQuestionCategories: TableInfo = TableInfo("question_categories",
            _columnsQuestionCategories, _foreignKeysQuestionCategories, _indicesQuestionCategories)
        val _existingQuestionCategories: TableInfo = read(connection, "question_categories")
        if (!_infoQuestionCategories.equals(_existingQuestionCategories)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |question_categories(com.ahmedyejam.mks.data.local.entity.QuestionCategoryEntity).
              | Expected:
              |""".trimMargin() + _infoQuestionCategories + """
              |
              | Found:
              |""".trimMargin() + _existingQuestionCategories)
        }
        val _columnsAssetReferences: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAssetReferences.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssetReferences.put("path", TableInfo.Column("path", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssetReferences.put("ownerType", TableInfo.Column("ownerType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAssetReferences.put("ownerId", TableInfo.Column("ownerId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAssetReferences.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAssetReferences.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAssetReferences: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAssetReferences: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesAssetReferences.add(TableInfo.Index("index_asset_references_path", false,
            listOf("path"), listOf("ASC")))
        _indicesAssetReferences.add(TableInfo.Index("index_asset_references_ownerType_ownerId",
            false, listOf("ownerType", "ownerId"), listOf("ASC", "ASC")))
        _indicesAssetReferences.add(TableInfo.Index("index_asset_references_ownerType_ownerId_path",
            true, listOf("ownerType", "ownerId", "path"), listOf("ASC", "ASC", "ASC")))
        _indicesAssetReferences.add(TableInfo.Index("index_asset_references_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoAssetReferences: TableInfo = TableInfo("asset_references", _columnsAssetReferences,
            _foreignKeysAssetReferences, _indicesAssetReferences)
        val _existingAssetReferences: TableInfo = read(connection, "asset_references")
        if (!_infoAssetReferences.equals(_existingAssetReferences)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |asset_references(com.ahmedyejam.mks.data.local.entity.AssetReferenceEntity).
              | Expected:
              |""".trimMargin() + _infoAssetReferences + """
              |
              | Found:
              |""".trimMargin() + _existingAssetReferences)
        }
        val _columnsQuestionAssets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsQuestionAssets.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("quizId", TableInfo.Column("quizId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("questionId", TableInfo.Column("questionId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("assetType", TableInfo.Column("assetType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("localPath", TableInfo.Column("localPath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("externalUrl", TableInfo.Column("externalUrl", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("mimeType", TableInfo.Column("mimeType", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("fileName", TableInfo.Column("fileName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("fileSizeBytes", TableInfo.Column("fileSizeBytes", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("textContent", TableInfo.Column("textContent", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("sourceDocumentId", TableInfo.Column("sourceDocumentId",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("sourcePage", TableInfo.Column("sourcePage", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("sourceQuote", TableInfo.Column("sourceQuote", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("sortOrder", TableInfo.Column("sortOrder", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0, "0",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("isPrimary", TableInfo.Column("isPrimary", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionAssets.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysQuestionAssets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysQuestionAssets.add(TableInfo.ForeignKey("questions", "CASCADE", "NO ACTION",
            listOf("questionId"), listOf("id")))
        val _indicesQuestionAssets: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_quizId", false,
            listOf("quizId"), listOf("ASC")))
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_questionId", false,
            listOf("questionId"), listOf("ASC")))
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_assetType", false,
            listOf("assetType"), listOf("ASC")))
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_createdAt", false,
            listOf("createdAt"), listOf("ASC")))
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_sourceDocumentId", false,
            listOf("sourceDocumentId"), listOf("ASC")))
        _indicesQuestionAssets.add(TableInfo.Index("index_question_assets_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoQuestionAssets: TableInfo = TableInfo("question_assets", _columnsQuestionAssets,
            _foreignKeysQuestionAssets, _indicesQuestionAssets)
        val _existingQuestionAssets: TableInfo = read(connection, "question_assets")
        if (!_infoQuestionAssets.equals(_existingQuestionAssets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |question_assets(com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity).
              | Expected:
              |""".trimMargin() + _infoQuestionAssets + """
              |
              | Found:
              |""".trimMargin() + _existingQuestionAssets)
        }
        val _columnsSourceDocuments: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSourceDocuments.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("bookId", TableInfo.Column("bookId", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("sourceType", TableInfo.Column("sourceType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("author", TableInfo.Column("author", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("edition", TableInfo.Column("edition", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("year", TableInfo.Column("year", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("publisher", TableInfo.Column("publisher", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("localPath", TableInfo.Column("localPath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("externalUrl", TableInfo.Column("externalUrl", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSourceDocuments.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSourceDocuments: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysSourceDocuments.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesSourceDocuments: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSourceDocuments.add(TableInfo.Index("index_source_documents_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesSourceDocuments.add(TableInfo.Index("index_source_documents_title", false,
            listOf("title"), listOf("ASC")))
        _indicesSourceDocuments.add(TableInfo.Index("index_source_documents_sourceType", false,
            listOf("sourceType"), listOf("ASC")))
        _indicesSourceDocuments.add(TableInfo.Index("index_source_documents_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoSourceDocuments: TableInfo = TableInfo("source_documents", _columnsSourceDocuments,
            _foreignKeysSourceDocuments, _indicesSourceDocuments)
        val _existingSourceDocuments: TableInfo = read(connection, "source_documents")
        if (!_infoSourceDocuments.equals(_existingSourceDocuments)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |source_documents(com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity).
              | Expected:
              |""".trimMargin() + _infoSourceDocuments + """
              |
              | Found:
              |""".trimMargin() + _existingSourceDocuments)
        }
        val _columnsMistakeLogEntries: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMistakeLogEntries.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("quizId", TableInfo.Column("quizId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("questionId", TableInfo.Column("questionId", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("sessionId", TableInfo.Column("sessionId", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("selectedAnswer", TableInfo.Column("selectedAnswer", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("correctAnswer", TableInfo.Column("correctAnswer", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("userReason", TableInfo.Column("userReason", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("correctConcept", TableInfo.Column("correctConcept", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("preventionNote", TableInfo.Column("preventionNote", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("linkedFlashcardId", TableInfo.Column("linkedFlashcardId",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("linkedBlueprintId", TableInfo.Column("linkedBlueprintId",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("linkedAssetId", TableInfo.Column("linkedAssetId", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("isFixed", TableInfo.Column("isFixed", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("reviewAt", TableInfo.Column("reviewAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            "0", TableInfo.CREATED_FROM_ENTITY))
        _columnsMistakeLogEntries.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMistakeLogEntries: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysMistakeLogEntries.add(TableInfo.ForeignKey("questions", "CASCADE", "NO ACTION",
            listOf("questionId"), listOf("id")))
        val _indicesMistakeLogEntries: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_quizId", false,
            listOf("quizId"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_questionId", false,
            listOf("questionId"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_sessionId", false,
            listOf("sessionId"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_reviewAt", false,
            listOf("reviewAt"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_isFixed", false,
            listOf("isFixed"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_createdAt", false,
            listOf("createdAt"), listOf("ASC")))
        _indicesMistakeLogEntries.add(TableInfo.Index("index_mistake_log_entries_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoMistakeLogEntries: TableInfo = TableInfo("mistake_log_entries",
            _columnsMistakeLogEntries, _foreignKeysMistakeLogEntries, _indicesMistakeLogEntries)
        val _existingMistakeLogEntries: TableInfo = read(connection, "mistake_log_entries")
        if (!_infoMistakeLogEntries.equals(_existingMistakeLogEntries)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |mistake_log_entries(com.ahmedyejam.mks.data.local.entity.MistakeLogEntryEntity).
              | Expected:
              |""".trimMargin() + _infoMistakeLogEntries + """
              |
              | Found:
              |""".trimMargin() + _existingMistakeLogEntries)
        }
        val _columnsAnnotations: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAnnotations.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("workspaceId", TableInfo.Column("workspaceId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("ownerType", TableInfo.Column("ownerType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("ownerId", TableInfo.Column("ownerId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("selectedText", TableInfo.Column("selectedText", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("noteBody", TableInfo.Column("noteBody", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("colorLabel", TableInfo.Column("colorLabel", "TEXT", true, 0,
            "'YELLOW'", TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("positionDataJson", TableInfo.Column("positionDataJson", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAnnotations.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAnnotations: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysAnnotations.add(TableInfo.ForeignKey("workspaces", "CASCADE", "NO ACTION",
            listOf("workspaceId"), listOf("id")))
        _foreignKeysAnnotations.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesAnnotations: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesAnnotations.add(TableInfo.Index("index_annotations_workspaceId", false,
            listOf("workspaceId"), listOf("ASC")))
        _indicesAnnotations.add(TableInfo.Index("index_annotations_bookId", false, listOf("bookId"),
            listOf("ASC")))
        _indicesAnnotations.add(TableInfo.Index("index_annotations_ownerType_ownerId", false,
            listOf("ownerType", "ownerId"), listOf("ASC", "ASC")))
        _indicesAnnotations.add(TableInfo.Index("index_annotations_colorLabel", false,
            listOf("colorLabel"), listOf("ASC")))
        _indicesAnnotations.add(TableInfo.Index("index_annotations_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        _indicesAnnotations.add(TableInfo.Index("index_annotations_updatedAt", false,
            listOf("updatedAt"), listOf("ASC")))
        val _infoAnnotations: TableInfo = TableInfo("annotations", _columnsAnnotations,
            _foreignKeysAnnotations, _indicesAnnotations)
        val _existingAnnotations: TableInfo = read(connection, "annotations")
        if (!_infoAnnotations.equals(_existingAnnotations)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |annotations(com.ahmedyejam.mks.data.local.entity.AnnotationEntity).
              | Expected:
              |""".trimMargin() + _infoAnnotations + """
              |
              | Found:
              |""".trimMargin() + _existingAnnotations)
        }
        val _columnsNoteCollections: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsNoteCollections.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("externalId", TableInfo.Column("externalId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("bookId", TableInfo.Column("bookId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("iconName", TableInfo.Column("iconName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("coverImage", TableInfo.Column("coverImage", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("tags", TableInfo.Column("tags", "TEXT", true, 0, "'[]'",
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("noteCount", TableInfo.Column("noteCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("isSystem", TableInfo.Column("isSystem", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("isPinned", TableInfo.Column("isPinned", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("lastStudiedAt", TableInfo.Column("lastStudiedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("lastEditedAt", TableInfo.Column("lastEditedAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNoteCollections.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysNoteCollections: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysNoteCollections.add(TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",
            listOf("bookId"), listOf("id")))
        val _indicesNoteCollections: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesNoteCollections.add(TableInfo.Index("index_note_collections_bookId", false,
            listOf("bookId"), listOf("ASC")))
        _indicesNoteCollections.add(TableInfo.Index("index_note_collections_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoNoteCollections: TableInfo = TableInfo("note_collections", _columnsNoteCollections,
            _foreignKeysNoteCollections, _indicesNoteCollections)
        val _existingNoteCollections: TableInfo = read(connection, "note_collections")
        if (!_infoNoteCollections.equals(_existingNoteCollections)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |note_collections(com.ahmedyejam.mks.data.local.entity.NoteCollectionEntity).
              | Expected:
              |""".trimMargin() + _infoNoteCollections + """
              |
              | Found:
              |""".trimMargin() + _existingNoteCollections)
        }
        val _columnsStudySessions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStudySessions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("targetType", TableInfo.Column("targetType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("targetId", TableInfo.Column("targetId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("label", TableInfo.Column("label", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("stateJson", TableInfo.Column("stateJson", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("timeSpentMs", TableInfo.Column("timeSpentMs", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("completionPercentage", TableInfo.Column("completionPercentage",
            "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("correctCount", TableInfo.Column("correctCount", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("incorrectCount", TableInfo.Column("incorrectCount", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStudySessions.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStudySessions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesStudySessions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesStudySessions.add(TableInfo.Index("index_study_sessions_targetId_targetType", false,
            listOf("targetId", "targetType"), listOf("ASC", "ASC")))
        _indicesStudySessions.add(TableInfo.Index("index_study_sessions_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoStudySessions: TableInfo = TableInfo("study_sessions", _columnsStudySessions,
            _foreignKeysStudySessions, _indicesStudySessions)
        val _existingStudySessions: TableInfo = read(connection, "study_sessions")
        if (!_infoStudySessions.equals(_existingStudySessions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |study_sessions(com.ahmedyejam.mks.data.local.entity.StudySessionEntity).
              | Expected:
              |""".trimMargin() + _infoStudySessions + """
              |
              | Found:
              |""".trimMargin() + _existingStudySessions)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "workspaces",
        "workspace_settings", "books", "quizzes", "questions", "sessions", "category_metadata",
        "flashcard_decks", "flashcards", "learning_sessions", "slideshow_courses", "course_slides",
        "note_blueprints", "prompts", "prompt_decks", "prompt_cards", "prompt_runs",
        "knowledge_study_sessions", "question_categories", "asset_references", "question_assets",
        "source_documents", "mistake_log_entries", "annotations", "note_collections",
        "study_sessions")
  }

  public override fun clearAllTables() {
    super.performClear(true, "workspaces", "workspace_settings", "books", "quizzes", "questions",
        "sessions", "category_metadata", "flashcard_decks", "flashcards", "learning_sessions",
        "slideshow_courses", "course_slides", "note_blueprints", "prompts", "prompt_decks",
        "prompt_cards", "prompt_runs", "knowledge_study_sessions", "question_categories",
        "asset_references", "question_assets", "source_documents", "mistake_log_entries",
        "annotations", "note_collections", "study_sessions")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(WorkspaceDao::class, WorkspaceDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(BookDao::class, BookDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(QuizDao::class, QuizDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(QuestionDao::class, QuestionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SessionDao::class, SessionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CategoryMetadataDao::class,
        CategoryMetadataDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FlashcardDeckDao::class, FlashcardDeckDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(FlashcardDao::class, FlashcardDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(LearningSessionDao::class,
        LearningSessionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SlideshowCourseDao::class,
        SlideshowCourseDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CourseSlideDao::class, CourseSlideDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(NoteBlueprintDao::class, NoteBlueprintDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PromptDao::class, PromptDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PromptDeckDao::class, PromptDeckDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PromptCardDao::class, PromptCardDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PromptRunDao::class, PromptRunDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(KnowledgeStudySessionDao::class,
        KnowledgeStudySessionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(QuestionCategoryDao::class,
        QuestionCategoryDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AssetReferenceDao::class, AssetReferenceDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(QuestionAssetDao::class, QuestionAssetDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SourceDocumentDao::class, SourceDocumentDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MistakeLogDao::class, MistakeLogDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GlobalSearchDao::class, GlobalSearchDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AnnotationDao::class, AnnotationDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(NoteCollectionDao::class, NoteCollectionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(StudySessionDao::class, StudySessionDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun workspaceDao(): WorkspaceDao = _workspaceDao.value

  public override fun bookDao(): BookDao = _bookDao.value

  public override fun quizDao(): QuizDao = _quizDao.value

  public override fun questionDao(): QuestionDao = _questionDao.value

  public override fun sessionDao(): SessionDao = _sessionDao.value

  public override fun categoryMetadataDao(): CategoryMetadataDao = _categoryMetadataDao.value

  public override fun flashcardDeckDao(): FlashcardDeckDao = _flashcardDeckDao.value

  public override fun flashcardDao(): FlashcardDao = _flashcardDao.value

  public override fun learningSessionDao(): LearningSessionDao = _learningSessionDao.value

  public override fun slideshowCourseDao(): SlideshowCourseDao = _slideshowCourseDao.value

  public override fun courseSlideDao(): CourseSlideDao = _courseSlideDao.value

  public override fun noteBlueprintDao(): NoteBlueprintDao = _noteBlueprintDao.value

  public override fun promptDao(): PromptDao = _promptDao.value

  public override fun promptDeckDao(): PromptDeckDao = _promptDeckDao.value

  public override fun promptCardDao(): PromptCardDao = _promptCardDao.value

  public override fun promptRunDao(): PromptRunDao = _promptRunDao.value

  public override fun knowledgeStudySessionDao(): KnowledgeStudySessionDao =
      _knowledgeStudySessionDao.value

  public override fun questionCategoryDao(): QuestionCategoryDao = _questionCategoryDao.value

  public override fun assetReferenceDao(): AssetReferenceDao = _assetReferenceDao.value

  public override fun questionAssetDao(): QuestionAssetDao = _questionAssetDao.value

  public override fun sourceDocumentDao(): SourceDocumentDao = _sourceDocumentDao.value

  public override fun mistakeLogDao(): MistakeLogDao = _mistakeLogDao.value

  public override fun globalSearchDao(): GlobalSearchDao = _globalSearchDao.value

  public override fun annotationDao(): AnnotationDao = _annotationDao.value

  public override fun noteCollectionDao(): NoteCollectionDao = _noteCollectionDao.value

  public override fun studySessionDao(): StudySessionDao = _studySessionDao.value
}
