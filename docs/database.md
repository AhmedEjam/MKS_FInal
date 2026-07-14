# MKS Database Layer — Full Inspection

## Architecture Overview

The database layer is **modular** — split across three Gradle modules:

| Module           | Contents                                              |
|------------------|-------------------------------------------------------|
| `core/model/`    | 26 Entity data classes                                |
| `core/database/` | `MksDatabase`, `MksMigrations`, `Converters`, 26 DAOs |
| `app/di/`        | 6 Hilt DI modules wiring it all together              |

**Current version: 30** (29 migration steps from v1→v30), using Room with schema export enabled.

---

## Entity Patterns

All 26 entities follow consistent conventions:

- **Kotlin `data class`** with default values for every field
- **`@PrimaryKey(autoGenerate = true)`** (except `CategoryMetadataEntity` uses `name`,
  `QuestionCategoryEntity` uses composite PK)
- **`onDelete = ForeignKey.CASCADE`** on all foreign keys
- **Soft delete universal**: every entity has `deletedAt: Long? = null`; queries filter
  `WHERE deletedAt IS NULL`
- **Timestamps**: `createdAt`, `updatedAt`, `lastStudiedAt`, `lastEditedAt` on most entities
- **`externalId: String`** for import/export identity tracking, with unique indexes
- **Complex types as JSON strings**: `List<String>` (tags, options), `Map<Long, List<Int>>` (
  answers), etc. — all persisted via `Converters`
- **`@JsonClass(generateAdapter = true)`** from Moshi on many entities for export serialization

---

## DAO Patterns

DAOs provide **dual access methods**:

- **`Flow<List<...>>`** — reactive, for UI observation
- **`suspend fun ...Now(): List<...>`** — one-shot, for business logic

Every DAO includes:

- **Soft delete**: `softDeleteById(id, deletedAt)` + `restoreById(id, updatedAt)`
- **Hard delete**: for explicit cleanup
- **Including-deleted variants**: `getByIdIncludingDeleted()` for recovery/import
- **`@Insert(onConflict = OnConflictStrategy.REPLACE)`** on all inserts
- **Targeted partial-update queries** (e.g., `updateQuestionCount`, `updateCompletionPercentage`)

---

## Type Converters (Moshi-based)

`Converters.kt` uses **Moshi** (not Gson) for all JSON serialization:

| Kotlin Type                 | SQL Column Type | Converter Method                      |
|-----------------------------|-----------------|---------------------------------------|
| `List<String>?`             | TEXT            | `fromStringList` / `toStringList`     |
| `List<Int>?`, `List<Long>?` | TEXT            | `fromIntList` / `toIntList` etc.      |
| `Map<Long, List<Int>>?`     | TEXT            | `fromAnswersMap` / `toAnswersMap`     |
| `Map<Long, String>?`        | TEXT            | `fromNotesMap` / `toNotesMap`         |
| `QuestionType` enum         | TEXT            | `fromQuestionType` / `toQuestionType` |

Key design: backward compatibility with legacy comma-separated values, graceful failure (returns
`emptyList()`/`emptyMap()` on parse errors), enum fallback to `SINGLE_CHOICE`.

---

## Migrations (29 steps, v1→v30)

Key patterns used in `MksMigrations.kt`:

1. **`addColumnIfMissing()`** — checks `PRAGMA table_info` before `ALTER TABLE ADD COLUMN`,
   preventing duplicate-column crashes
2. **Table rewrites** — create-new → copy-data → drop-old → rename, used when ALTER TABLE can't
   handle the change (e.g., adding FK constraints, changing PK structure)
3. **`CREATE INDEX IF NOT EXISTS`** — makes migrations rerunnable
4. **Data seeding in migrations** — v22→v24 creates default workspace; v26→v27 creates default note
   collections
5. **`MksMigrations.ALL`** array — all 29 objects in order, passed to
   `Room.databaseBuilder().addMigrations(*MksMigrations.ALL)`

Notable: the `source_document_assets` table was created in v27→v28 then **dropped** in v28→v29.

| Migration | Summary                                                                                                                                                                                                      |
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1→2       | Add externalId, updatedAt, coverImage, reference, image fields to books/quizzes/questions; populate externalIds                                                                                              |
| 2→3       | Create `category_metadata` table                                                                                                                                                                             |
| 3→4       | Add isPinned, fields to books/quizzes                                                                                                                                                                        |
| 4→5       | Add session config fields (shuffle, timers, ranges)                                                                                                                                                          |
| 5→6       | Add includeFilters, sessionNotes to sessions                                                                                                                                                                 |
| 6→7       | Add lastModifiedAt to sessions                                                                                                                                                                               |
| 7→8       | Major: add stats columns (questionCount, completion, attempts, etc.) to books/quizzes/questions/sessions                                                                                                     |
| 8→9       | Add source IDs (sourceBookId, sourceQuizId, sourceQuestionId) to questions                                                                                                                                   |
| 9→10      | Add currentStreak, maxStreak to sessions                                                                                                                                                                     |
| 10→11     | Add questionIds, answersByIndex, droppedOptionsByIndex, etc. to sessions                                                                                                                                     |
| 11→12     | Add answeredCount, totalAttempts, accuracyPercentage to books/quizzes                                                                                                                                        |
| 12→13     | Add timeSpentMs, lastAttemptResult, consecutiveCorrect to questions                                                                                                                                          |
| 13→14     | Add isMarked, notes to questions (wrapped in try/catch)                                                                                                                                                      |
| 14→15     | **Knowledge Bank**: Create flashcard_decks, flashcards, learning_sessions tables                                                                                                                             |
| 15→16     | **Knowledge Bank**: Create slideshow_courses, course_slides, note_blueprints, prompts, knowledge_study_sessions; add sourceQuestionId, syncConfig to flashcards                                              |
| 16→17     | Create question_categories, asset_references tables                                                                                                                                                          |
| 17→18     | Create question_assets table                                                                                                                                                                                 |
| 18→19     | Create source_documents table; add blueprintMode, linkedQuestions/Assets, reviewStatus to note_blueprints                                                                                                    |
| 19→20     | Create prompt_decks, prompt_cards, prompt_runs tables                                                                                                                                                        |
| 20→21     | Add dropped/marked metadata to questions; add dueAt/reviewCount to flashcards; create mistake_log_entries                                                                                                    |
| 21→22     | Recreate note_blueprints (table rewrite to fix schema)                                                                                                                                                       |
| 22→23     | **Workspaces**: Create workspaces, workspace_settings; migrate books to include workspaceId (full table rewrite)                                                                                             |
| 23→24     | Populate default workspace, fix orphan books, create workspace settings                                                                                                                                      |
| 24→25     | **Soft deletes**: Add `deletedAt` to 21 tables                                                                                                                                                               |
| 25→26     | Create annotations table                                                                                                                                                                                     |
| 26→27     | Add tags/difficulty/spaced-repetition fields to quizzes, decks, courses, slides, questions; create note_collections, study_sessions; **migrate note_blueprints from bookId to collectionId** (major rewrite) |
| 27→28     | Create source_document_assets table                                                                                                                                                                          |
| 28→29     | Drop source_document_assets; add unique indexes on books.externalId, quizzes.externalId                                                                                                                      |
| 29→30     | Add resultTaxonomy (Map<Int, String>) to sessions                                                                                                                                                            |

---

## DI / Database Instantiation

**No `AppModule.kt`** — fully replaced by Hilt. Six modules in `app/di/`:

| Module                   | Provides                                                                                                                          |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| `HiltDataModule`         | `MksDatabase` singleton, `FileManager`, `Repository`, `ExportManager`, `ImportLibraryManager`, `@ApplicationScope CoroutineScope` |
| `HiltDaoModule`          | 6 core DAOs (Book, Quiz, Question, Session, CategoryMetadata, QuestionCategory)                                                   |
| `HiltKnowledgeDaoModule` | 10 knowledge-bank DAOs                                                                                                            |
| `HiltUtilityDaoModule`   | 10 utility/cross-cutting DAOs                                                                                                     |
| `HiltRepositoryModule`   | `GlobalSearchRepository` (explicitly provided). Note: `ReviewRepository` and `OllamaRepository` use `@Inject constructor` and are auto-provided by Hilt. |
| `HiltServiceModule`      | Placeholder                                                                                                                       |

Database builder config:

```kotlin
Room.databaseBuilder(context, MksDatabase::class.java, "mks_database")
    .addMigrations(*MksMigrations.ALL)
    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
    .build()
```

---

## Persistence Flow

1. **Hilt** creates `MksDatabase` singleton via `HiltDataModule`
2. DAOs extracted from database singleton via three DAO modules
3. DAOs injected into repositories → ViewModels
4. Complex types transparently serialized to JSON TEXT columns by `Converters` (Moshi)
5. Incremental upgrades via `MksMigrations.ALL`; downgrades fall back to destructive migration
6. Soft deletes (`deletedAt`) are universal; hard deletes reserved for explicit cleanup

---

## Key Differences from AGENTS.md Documentation

The AGENTS.md file previously described the project at version 28 with 27 migrations, an `AppModule.kt`, and
entities/DAOs in `data/local/entity/` and `data/local/dao/` under the app module. The actual current
state differs:

- **Database version is 30** (not 28), with **29 migrations** (1→2 through 29→30).
- **No AppModule.kt exists** — it has been fully replaced by the Hilt DI modules.
- **Entities** live in `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/` (a separate
  Gradle module).
- **DAOs** live in `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/`.
- **MksDatabase** and **MksMigrations** live in
  `core/database/src/main/java/com/ahmedyejam/mks/data/local/`.
- **Converters** lives alongside MksDatabase in `core/database/`.
- The `source_document_assets` table (added in v27→v28) was **dropped** in v28→v29 — it no longer
  exists in the current schema.
- The DI setup splits DAO provision across **three separate modules** (HiltDaoModule,
  HiltKnowledgeDaoModule, HiltUtilityDaoModule) rather than one.

> **Note (2026-07-10):** AGENTS.md has been updated to reflect the current state. The discrepancies
> listed above should now be resolved. This section is retained for historical reference.

---

## The 26 Tables

### Workspace & Settings

| # | Entity                    | Table                | Purpose                                               |
|---|---------------------------|----------------------|-------------------------------------------------------|
| 1 | `WorkspaceEntity`         | `workspaces`         | Multi-workspace support (top-level container)         |
| 2 | `WorkspaceSettingsEntity` | `workspace_settings` | Per-workspace preferences (language, theme, defaults) |

### Core Quiz / Library

| # | Entity                   | Table               | Purpose                                                        |
|---|--------------------------|---------------------|----------------------------------------------------------------|
| 3 | `BookEntity`             | `books`             | Book/library container (linked to workspace)                   |
| 4 | `QuizEntity`             | `quizzes`           | Quiz inside a book                                             |
| 5 | `QuestionEntity`         | `questions`         | Question content, options, answers, explanations               |
| 6 | `SessionEntity`          | `sessions`          | Quiz session — answers and completion state                    |
| 7 | `CategoryMetadataEntity` | `category_metadata` | Category labels (emoji, color, isPinned) — PK is `name` (TEXT) |

### Knowledge Bank / Study

| #  | Entity                        | Table                      | Purpose                                                       |
|----|-------------------------------|----------------------------|---------------------------------------------------------------|
| 8  | `FlashcardDeckEntity`         | `flashcard_decks`          | Flashcard deck metadata + progress                            |
| 9  | `FlashcardEntity`             | `flashcards`               | Individual front/back cards                                   |
| 10 | `LearningSessionEntity`       | `learning_sessions`        | Flashcard deck learning session state (JSON)                  |
| 11 | `SlideshowCourseEntity`       | `slideshow_courses`        | Slideshow course metadata + progress                          |
| 12 | `CourseSlideEntity`           | `course_slides`            | Slides within a course                                        |
| 13 | `NoteCollectionEntity`        | `note_collections`         | Grouping of note blueprints (linked to book)                  |
| 14 | `NoteBlueprintEntity`         | `note_blueprints`          | Article/note content, summary, bullets (linked to collection) |
| 15 | `PromptEntity`                | `prompts`                  | Legacy prompt model                                           |
| 16 | `PromptDeckEntity`            | `prompt_decks`             | AI prompt deck metadata                                       |
| 17 | `PromptCardEntity`            | `prompt_cards`             | Individual AI prompts within a deck                           |
| 18 | `PromptRunEntity`             | `prompt_runs`              | History of prompt executions                                  |
| 19 | `KnowledgeStudySessionEntity` | `knowledge_study_sessions` | Generic progress tracker for non-quiz content                 |
| 20 | `StudySessionEntity`          | `study_sessions`           | Non-quiz study session progress (newer model)                 |

### Cross-cutting / Index Tables

| #  | Entity                   | Table                 | Purpose                                                       |
|----|--------------------------|-----------------------|---------------------------------------------------------------|
| 21 | `QuestionCategoryEntity` | `question_categories` | Many-to-many join: question ↔ category (composite PK)         |
| 22 | `AssetReferenceEntity`   | `asset_references`    | Polymorphic local asset ownership index (ownerType + ownerId) |
| 23 | `QuestionAssetEntity`    | `question_assets`     | Assets linked to questions (images, docs, files)              |
| 24 | `SourceDocumentEntity`   | `source_documents`    | Reference materials linked to books (PDFs, URLs)              |

### Logging & Annotations

| #  | Entity                  | Table                 | Purpose                                                |
|----|-------------------------|-----------------------|--------------------------------------------------------|
| 25 | `MistakeLogEntryEntity` | `mistake_log_entries` | Tracks mistakes across quizzes with user explanations  |
| 26 | `AnnotationEntity`      | `annotations`         | Highlights and notes linked to different content types |

All 26 map to 26 abstract DAO accessors in the same class.

---

## Full Column Definitions for All 26 Tables

### 1. `workspaces`

| Column        | Type    | Default                      | Notes        |
|---------------|---------|------------------------------|--------------|
| `id`          | Long    | autoGenerate                 | PK           |
| `externalId`  | String  | —                            | unique index |
| `name`        | String  | —                            |              |
| `description` | String? | null                         |              |
| `isDefault`   | Boolean | false                        |              |
| `createdAt`   | Long    | `System.currentTimeMillis()` |              |
| `updatedAt`   | Long    | `System.currentTimeMillis()` |              |
| `deletedAt`   | Long?   | null                         | soft delete  |

**Indexes:** `externalId` (unique)

---

### 2. `workspace_settings`

| Column               | Type    | Default                      | Notes                     |
|----------------------|---------|------------------------------|---------------------------|
| `id`                 | Long    | autoGenerate                 | PK                        |
| `workspaceId`        | Long    | —                            | FK → workspaces (CASCADE) |
| `language`           | String? | null                         |                           |
| `theme`              | String? | null                         |                           |
| `defaultSort`        | String? | null                         |                           |
| `quizDefaultsJson`   | String? | null                         | JSON config               |
| `importDefaultsJson` | String? | null                         | JSON config               |
| `createdAt`          | Long    | `System.currentTimeMillis()` |                           |
| `updatedAt`          | Long    | `System.currentTimeMillis()` |                           |
| `deletedAt`          | Long?   | null                         | soft delete               |

**Indexes:** `workspaceId`

---

### 3. `books`

| Column                 | Type           | Default                                | Notes                     |
|------------------------|----------------|----------------------------------------|---------------------------|
| `id`                   | Long           | autoGenerate                           | PK                        |
| `workspaceId`          | Long           | 0                                      | FK → workspaces (CASCADE) |
| `externalId`           | String         | —                                      | unique index              |
| `title`                | String         | —                                      |                           |
| `description`          | String         | ""                                     |                           |
| `iconName`             | String?        | null                                   |                           |
| `coverImage`           | String?        | null                                   | local path or URL         |
| `createdAt`            | Long           | `System.currentTimeMillis()`           |                           |
| `updatedAt`            | Long           | `System.currentTimeMillis()`           |                           |
| `contentUpdatedAt`     | Long           | `System.currentTimeMillis()`           |                           |
| `lastStudiedAt`        | Long           | 0                                      |                           |
| `lastEditedAt`         | Long           | `System.currentTimeMillis()`           |                           |
| `isPinned`             | Boolean        | false                                  |                           |
| `isSystem`             | Boolean        | false                                  |                           |
| `fields`               | List\<String\> | emptyList() → JSON TEXT via Converters |                           |
| `questionCount`        | Int            | 0                                      | cached stat               |
| `answeredCount`        | Int            | 0                                      | cached stat               |
| `totalAttempts`        | Int            | 0                                      | cached stat               |
| `completionPercentage` | Float          | 0f                                     | cached stat               |
| `accuracyPercentage`   | Float          | 0f                                     | cached stat               |
| `deletedAt`            | Long?          | null                                   | soft delete               |

**Indexes:** `workspaceId`, `deletedAt`, `externalId` (unique)

---

### 4. `quizzes`

| Column                 | Type           | Default                      | Notes                    |
|------------------------|----------------|------------------------------|--------------------------|
| `id`                   | Long           | autoGenerate                 | PK                       |
| `externalId`           | String         | —                            | unique index             |
| `bookId`               | Long           | —                            | FK → books (CASCADE)     |
| `title`                | String         | —                            |                          |
| `description`          | String         | ""                           |                          |
| `category`             | String?        | null                         |                          |
| `tags`                 | List\<String\> | emptyList() → `'[]'` default | JSON TEXT via Converters |
| `iconName`             | String?        | null                         |                          |
| `coverImage`           | String?        | null                         |                          |
| `createdAt`            | Long           | `System.currentTimeMillis()` |                          |
| `updatedAt`            | Long           | `System.currentTimeMillis()` |                          |
| `contentUpdatedAt`     | Long           | `System.currentTimeMillis()` |                          |
| `lastStudiedAt`        | Long           | 0                            |                          |
| `lastEditedAt`         | Long           | `System.currentTimeMillis()` |                          |
| `isPinned`             | Boolean        | false                        |                          |
| `isSystem`             | Boolean        | false                        |                          |
| `questionCount`        | Int            | 0                            | cached stat              |
| `answeredCount`        | Int            | 0                            | cached stat              |
| `totalAttempts`        | Int            | 0                            | cached stat              |
| `completionPercentage` | Float          | 0f                           | cached stat              |
| `accuracyPercentage`   | Float          | 0f                           | cached stat              |
| `deletedAt`            | Long?          | null                         | soft delete              |

**Indexes:** `bookId`, `deletedAt`, `externalId` (unique)

---

### 5. `questions`

| Column               | Type           | Default                      | Notes                                 |
|----------------------|----------------|------------------------------|---------------------------------------|
| `id`                 | Long           | autoGenerate                 | PK                                    |
| `externalId`         | String         | —                            |                                       |
| `quizId`             | Long           | —                            | FK → quizzes (CASCADE)                |
| `text`               | String         | —                            | question stem                         |
| `type`               | QuestionType   | —                            | enum → TEXT via Converters            |
| `options`            | List\<String\> | —                            | JSON TEXT via Converters              |
| `correctAnswers`     | List\<Int\>    | —                            | indices of correct options, JSON TEXT |
| `explanation`        | String?        | null                         |                                       |
| `hint`               | String?        | null                         |                                       |
| `reference`          | String?        | null                         |                                       |
| `weight`             | Int            | 1                            |                                       |
| `imagePath`          | String?        | null                         |                                       |
| `imageName`          | String?        | null                         |                                       |
| `imageSource`        | String?        | null                         |                                       |
| `attempts`           | Int            | 0                            |                                       |
| `correctCount`       | Int            | 0                            |                                       |
| `isDropped`          | Boolean        | false                        |                                       |
| `droppedAt`          | Long?          | null                         |                                       |
| `droppedReason`      | String?        | null                         |                                       |
| `isMarked`           | Boolean        | false                        |                                       |
| `markedAt`           | Long?          | null                         |                                       |
| `markReason`         | String?        | null                         |                                       |
| `markReviewAt`       | Long?          | null                         |                                       |
| `notes`              | String?        | null                         |                                       |
| `categories`         | List\<String\> | emptyList()                  | JSON TEXT                             |
| `tags`               | List\<String\> | emptyList(), default `'[]'`  | JSON TEXT                             |
| `difficulty`         | String?        | null                         | spaced repetition                     |
| `dueAt`              | Long           | 0 (default `"0"`)            | spaced repetition                     |
| `reviewCount`        | Int            | 0 (default `"0"`)            | spaced repetition                     |
| `lastReviewedAt`     | Long           | 0 (default `"0"`)            | spaced repetition                     |
| `additionalInfo`     | String?        | null                         |                                       |
| `sourceBookId`       | String?        | null                         |                                       |
| `sourceQuizId`       | String?        | null                         |                                       |
| `sourceQuestionId`   | String?        | null                         |                                       |
| `createdAt`          | Long           | `System.currentTimeMillis()` |                                       |
| `updatedAt`          | Long           | `System.currentTimeMillis()` |                                       |
| `lastStudiedAt`      | Long           | 0                            |                                       |
| `lastEditedAt`       | Long           | `System.currentTimeMillis()` |                                       |
| `timeSpentMs`        | Long           | 0                            |                                       |
| `lastAttemptResult`  | Boolean?       | null                         |                                       |
| `consecutiveCorrect` | Int            | 0                            |                                       |
| `deletedAt`          | Long?          | null                         | soft delete                           |

**Indexes:** `quizId`, `isMarked`, `isDropped`, `markReviewAt`, `deletedAt`

---

### 6. `sessions`

| Column                       | Type                     | Default                      | Notes                    |
|------------------------------|--------------------------|------------------------------|--------------------------|
| `id`                         | Long                     | autoGenerate                 | PK                       |
| `quizId`                     | Long                     | —                            | FK → quizzes (CASCADE)   |
| `label`                      | String                   | —                            |                          |
| `currentQuestionIndex`       | Int                      | 0                            |                          |
| `score`                      | Int                      | 0                            |                          |
| `incorrectCount`             | Int                      | 0                            |                          |
| `answers`                    | Map\<Long, List\<Int\>\> | emptyMap()                   | JSON TEXT via Converters |
| `answersByIndex`             | Map\<Int, List\<Int\>\>  | emptyMap()                   | JSON TEXT                |
| `isCompleted`                | Boolean                  | false                        |                          |
| `createdAt`                  | Long                     | `System.currentTimeMillis()` |                          |
| `updatedAt`                  | Long                     | `System.currentTimeMillis()` |                          |
| `lastModifiedAt`             | Long                     | `System.currentTimeMillis()` |                          |
| `lastStudiedAt`              | Long                     | 0                            |                          |
| `lastEditedAt`               | Long                     | `System.currentTimeMillis()` |                          |
| `questionIds`                | List\<Long\>             | emptyList()                  | JSON TEXT                |
| `originalQuestionCount`      | Int                      | 0                            |                          |
| `shuffleQuestions`           | Boolean                  | true                         |                          |
| `shuffleOptions`             | Boolean                  | true                         |                          |
| `rapidMode`                  | Boolean                  | false                        |                          |
| `repeatWrong`                | Boolean                  | true                         |                          |
| `quizTimerSeconds`           | Int                      | 0                            | 0 = no timer             |
| `questionTimerSeconds`       | Int                      | 0                            | 0 = no timer             |
| `rangeFrom`                  | Int                      | 0                            | 0-based index            |
| `rangeTo`                    | Int                      | -1                           | -1 = until end           |
| `includeFilters`             | List\<String\>           | emptyList()                  | JSON TEXT                |
| `droppedOptions`             | Map\<Long, List\<Int\>\> | emptyMap()                   | JSON TEXT                |
| `droppedOptionsByIndex`      | Map\<Int, List\<Int\>\>  | emptyMap()                   | JSON TEXT                |
| `visibleOptionsCount`        | Map\<Long, Int\>         | emptyMap()                   | JSON TEXT                |
| `visibleOptionsCountByIndex` | Map\<Int, Int\>          | emptyMap()                   | JSON TEXT                |
| `currentStreak`              | Int                      | 0                            |                          |
| `maxStreak`                  | Int                      | 0                            |                          |
| `deletedAt`                  | Long?                    | null                         | soft delete              |
| `resultTaxonomy`             | Map\<Int, String\>       | emptyMap()                   | JSON TEXT (v30)          |

**Indexes:** `quizId`, `deletedAt`

---

### 7. `category_metadata`

| Column      | Type    | Default | Notes                           |
|-------------|---------|---------|---------------------------------|
| `name`      | String  | —       | **PK** (text, not autoGenerate) |
| `emoji`     | String? | null    |                                 |
| `color`     | Int?    | null    |                                 |
| `isPinned`  | Boolean | false   |                                 |
| `deletedAt` | Long?   | null    | soft delete                     |

**Indexes:** `deletedAt`

---

### 8. `flashcard_decks`

| Column              | Type           | Default                      | Notes                |
|---------------------|----------------|------------------------------|----------------------|
| `id`                | Long           | autoGenerate                 | PK                   |
| `externalId`        | String         | —                            |                      |
| `bookId`            | Long           | —                            | FK → books (CASCADE) |
| `title`             | String         | —                            |                      |
| `description`       | String?        | null                         |                      |
| `iconName`          | String?        | null                         |                      |
| `coverImage`        | String?        | null                         | local path or URL    |
| `tags`              | List\<String\> | emptyList(), default `'[]'`  | JSON TEXT            |
| `cardCount`         | Int            | 0                            | cached stat          |
| `studiedCount`      | Int            | 0                            | cached stat          |
| `masteryPercentage` | Float          | 0f                           | cached stat          |
| `isSystem`          | Boolean        | false                        |                      |
| `isPinned`          | Boolean        | false                        |                      |
| `createdAt`         | Long           | `System.currentTimeMillis()` |                      |
| `updatedAt`         | Long           | `System.currentTimeMillis()` |                      |
| `lastStudiedAt`     | Long           | 0                            |                      |
| `lastEditedAt`      | Long           | `System.currentTimeMillis()` |                      |
| `deletedAt`         | Long?          | null                         | soft delete          |

**Indexes:** `bookId`, `deletedAt`

---

### 9. `flashcards`

| Column             | Type                   | Default                      | Notes                          |
|--------------------|------------------------|------------------------------|--------------------------------|
| `id`               | Long                   | autoGenerate                 | PK                             |
| `externalId`       | String                 | —                            |                                |
| `deckId`           | Long                   | —                            | FK → flashcard_decks (CASCADE) |
| `frontText`        | String                 | —                            |                                |
| `backText`         | String                 | —                            |                                |
| `hint`             | String?                | null                         |                                |
| `imagePath`        | String?                | null                         |                                |
| `tags`             | List\<String\>         | emptyList()                  | JSON TEXT                      |
| `orderIndex`       | Int                    | 0                            |                                |
| `attempts`         | Int                    | 0                            |                                |
| `correctCount`     | Int                    | 0                            |                                |
| `difficulty`       | String?                | null                         |                                |
| `dueAt`            | Long                   | 0 (default `"0"`)            | spaced repetition              |
| `reviewCount`      | Int                    | 0 (default `"0"`)            | spaced repetition              |
| `lastReviewedAt`   | Long                   | 0                            |                                |
| `createdAt`        | Long                   | `System.currentTimeMillis()` |                                |
| `updatedAt`        | Long                   | `System.currentTimeMillis()` |                                |
| `sourceQuestionId` | Long?                  | null                         | optional link to question      |
| `syncConfig`       | Map\<String, Boolean\> | emptyMap()                   | JSON TEXT                      |
| `deletedAt`        | Long?                  | null                         | soft delete                    |

**Indexes:** `deckId`, `sourceQuestionId`, `dueAt`, `deletedAt`

---

### 10. `learning_sessions`

| Column        | Type    | Default                      | Notes                          |
|---------------|---------|------------------------------|--------------------------------|
| `id`          | Long    | autoGenerate                 | PK                             |
| `deckId`      | Long    | —                            | FK → flashcard_decks (CASCADE) |
| `label`       | String? | null                         |                                |
| `stateJson`   | String  | —                            | serialized session state       |
| `isCompleted` | Boolean | false                        |                                |
| `createdAt`   | Long    | `System.currentTimeMillis()` |                                |
| `updatedAt`   | Long    | `System.currentTimeMillis()` |                                |
| `deletedAt`   | Long?   | null                         | soft delete                    |

**Indexes:** `deckId`, `deletedAt`

---

### 11. `slideshow_courses`

| Column              | Type           | Default                      | Notes                |
|---------------------|----------------|------------------------------|----------------------|
| `id`                | Long           | autoGenerate                 | PK                   |
| `externalId`        | String         | —                            |                      |
| `bookId`            | Long           | —                            | FK → books (CASCADE) |
| `title`             | String         | —                            |                      |
| `description`       | String?        | null                         |                      |
| `iconName`          | String?        | null                         |                      |
| `coverImage`        | String?        | null                         |                      |
| `tags`              | List\<String\> | emptyList(), default `'[]'`  | JSON TEXT            |
| `slideCount`        | Int            | 0                            | cached stat          |
| `studiedSlideCount` | Int            | 0                            | cached stat          |
| `progress`          | Float          | 0f                           | cached stat          |
| `isSystem`          | Boolean        | false                        |                      |
| `isPinned`          | Boolean        | false                        |                      |
| `createdAt`         | Long           | `System.currentTimeMillis()` |                      |
| `updatedAt`         | Long           | `System.currentTimeMillis()` |                      |
| `lastStudiedAt`     | Long           | 0                            |                      |
| `lastEditedAt`      | Long           | `System.currentTimeMillis()` |                      |
| `isDerived`         | Boolean        | false                        |                      |
| `sourceQuizId`      | Long?          | null                         | optional quiz link   |
| `deletedAt`         | Long?          | null                         | soft delete          |

**Indexes:** `bookId`, `deletedAt`

---

### 12. `course_slides`

| Column             | Type                   | Default                      | Notes                            |
|--------------------|------------------------|------------------------------|----------------------------------|
| `id`               | Long                   | autoGenerate                 | PK                               |
| `externalId`       | String                 | —                            |                                  |
| `courseId`         | Long                   | —                            | FK → slideshow_courses (CASCADE) |
| `title`            | String                 | —                            |                                  |
| `body`             | String                 | —                            | slide content                    |
| `speakerNotes`     | String?                | null                         |                                  |
| `imagePath`        | String?                | null                         |                                  |
| `orderIndex`       | Int                    | 0                            |                                  |
| `isCompleted`      | Boolean                | false                        |                                  |
| `tags`             | List\<String\>         | emptyList(), default `'[]'`  | JSON TEXT                        |
| `difficulty`       | String?                | null                         | spaced repetition                |
| `dueAt`            | Long                   | 0 (default `"0"`)            | spaced repetition                |
| `reviewCount`      | Int                    | 0 (default `"0"`)            | spaced repetition                |
| `lastReviewedAt`   | Long                   | 0 (default `"0"`)            | spaced repetition                |
| `createdAt`        | Long                   | `System.currentTimeMillis()` |                                  |
| `updatedAt`        | Long                   | `System.currentTimeMillis()` |                                  |
| `sourceQuestionId` | Long?                  | null                         | optional question link           |
| `syncConfig`       | Map\<String, Boolean\> | emptyMap()                   | JSON TEXT                        |
| `deletedAt`        | Long?                  | null                         | soft delete                      |

**Indexes:** `courseId`, `sourceQuestionId`, `deletedAt`

---

### 13. `note_collections`

| Column          | Type           | Default                      | Notes                |
|-----------------|----------------|------------------------------|----------------------|
| `id`            | Long           | autoGenerate                 | PK                   |
| `externalId`    | String         | —                            |                      |
| `bookId`        | Long           | —                            | FK → books (CASCADE) |
| `title`         | String         | —                            |                      |
| `description`   | String?        | null                         |                      |
| `iconName`      | String?        | null                         |                      |
| `coverImage`    | String?        | null                         |                      |
| `tags`          | List\<String\> | emptyList(), default `'[]'`  | JSON TEXT            |
| `noteCount`     | Int            | 0                            | cached stat          |
| `isSystem`      | Boolean        | false                        |                      |
| `isPinned`      | Boolean        | false                        |                      |
| `createdAt`     | Long           | `System.currentTimeMillis()` |                      |
| `updatedAt`     | Long           | `System.currentTimeMillis()` |                      |
| `lastStudiedAt` | Long           | 0                            |                      |
| `lastEditedAt`  | Long           | `System.currentTimeMillis()` |                      |
| `deletedAt`     | Long?          | null                         | soft delete          |

**Indexes:** `bookId`, `deletedAt`

---

### 14. `note_blueprints`

| Column                | Type           | Default                                   | Notes                           |
|-----------------------|----------------|-------------------------------------------|---------------------------------|
| `id`                  | Long           | autoGenerate                              | PK                              |
| `externalId`          | String         | —                                         |                                 |
| `collectionId`        | Long           | —                                         | FK → note_collections (CASCADE) |
| `title`               | String         | —                                         |                                 |
| `description`         | String?        | null                                      |                                 |
| `summary`             | String?        | null                                      |                                 |
| `iconName`            | String?        | null                                      |                                 |
| `coverImage`          | String?        | null                                      |                                 |
| `body`                | String         | —                                         | markdown content                |
| `bulletPoints`        | List\<String\> | emptyList(), default `'[]'`               | JSON TEXT                       |
| `tags`                | List\<String\> | emptyList(), default `'[]'`               | JSON TEXT                       |
| `blueprintMode`       | String         | `"SIMPLE_NOTE"` (default `'SIMPLE_NOTE'`) | see BlueprintMode constants     |
| `linkedQuestionsJson` | String         | `"[]"` (default `'[]'`)                   | raw JSON string                 |
| `linkedAssetsJson`    | String         | `"[]"` (default `'[]'`)                   | raw JSON string                 |
| `reviewStatus`        | String         | `"NEW"` (default `'NEW'`)                 | see BlueprintReviewStatus       |
| `reviewCount`         | Int            | 0 (default `"0"`)                         |                                 |
| `lastReviewedAt`      | Long           | 0 (default `"0"`)                         |                                 |
| `createdAt`           | Long           | `System.currentTimeMillis()`              |                                 |
| `updatedAt`           | Long           | `System.currentTimeMillis()`              |                                 |
| `sourceQuestionId`    | Long?          | null                                      | optional question link          |
| `deletedAt`           | Long?          | null                                      | soft delete                     |

**Indexes:** `collectionId`, `sourceQuestionId`, `blueprintMode`, `reviewStatus`, `deletedAt`

---

### 15. `prompts` (legacy)

| Column              | Type           | Default                      | Notes                |
|---------------------|----------------|------------------------------|----------------------|
| `id`                | Long           | autoGenerate                 | PK                   |
| `externalId`        | String         | —                            |                      |
| `bookId`            | Long           | —                            | FK → books (CASCADE) |
| `title`             | String         | —                            |                      |
| `stem`              | String         | —                            | prompt text          |
| `conversationLinks` | List\<String\> | emptyList()                  | JSON TEXT            |
| `usageCount`        | Int            | 0                            |                      |
| `lastUsedAt`        | Long           | 0                            |                      |
| `createdAt`         | Long           | `System.currentTimeMillis()` |                      |
| `updatedAt`         | Long           | `System.currentTimeMillis()` |                      |
| `deletedAt`         | Long?          | null                         | soft delete          |

**Indexes:** `bookId`, `deletedAt`

---

### 16. `prompt_decks`

| Column        | Type           | Default                     | Notes                |
|---------------|----------------|-----------------------------|----------------------|
| `id`          | Long           | autoGenerate                | PK                   |
| `bookId`      | Long           | —                           | FK → books (CASCADE) |
| `title`       | String         | —                           |                      |
| `description` | String?        | null                        |                      |
| `iconName`    | String?        | null                        |                      |
| `coverImage`  | String?        | null                        |                      |
| `tags`        | List\<String\> | emptyList(), default `'[]'` | JSON TEXT            |
| `createdAt`   | Long           | 0 (default `"0"`)           |                      |
| `updatedAt`   | Long           | 0 (default `"0"`)           |                      |
| `deletedAt`   | Long?          | null                        | soft delete          |

**Indexes:** `bookId`, `deletedAt`

---

### 17. `prompt_cards`

| Column          | Type           | Default                     | Notes                           |
|-----------------|----------------|-----------------------------|---------------------------------|
| `id`            | Long           | autoGenerate                | PK                              |
| `deckId`        | Long           | —                           | FK → prompt_decks (CASCADE)     |
| `title`         | String         | —                           |                                 |
| `promptText`    | String         | —                           |                                 |
| `variablesJson` | String?        | null                        | raw JSON for template variables |
| `outputType`    | String         | `"OTHER"`                   | see PromptOutputType constants  |
| `tags`          | List\<String\> | emptyList(), default `'[]'` | JSON TEXT                       |
| `usageCount`    | Int            | 0 (default `"0"`)           |                                 |
| `lastUsedAt`    | Long?          | null                        |                                 |
| `sortOrder`     | Int            | 0 (default `"0"`)           |                                 |
| `createdAt`     | Long           | 0 (default `"0"`)           |                                 |
| `updatedAt`     | Long           | 0 (default `"0"`)           |                                 |
| `deletedAt`     | Long?          | null                        | soft delete                     |

**Indexes:** `deckId`, `outputType`, `deletedAt`

---

### 18. `prompt_runs`

| Column            | Type    | Default           | Notes                           |
|-------------------|---------|-------------------|---------------------------------|
| `id`              | Long    | autoGenerate      | PK                              |
| `promptCardId`    | Long    | —                 | FK → prompt_cards (CASCADE)     |
| `inputValuesJson` | String  | —                 | raw JSON of variable inputs     |
| `renderedPrompt`  | String  | —                 | final prompt after substitution |
| `outputText`      | String? | null              | AI output                       |
| `linkedAssetType` | String? | null              |                                 |
| `linkedAssetId`   | Long?   | null              |                                 |
| `createdAt`       | Long    | 0 (default `"0"`) |                                 |
| `deletedAt`       | Long?   | null              | soft delete                     |

**Indexes:** `promptCardId`, `createdAt`, `deletedAt`

---

### 19. `knowledge_study_sessions`

| Column        | Type    | Default                      | Notes                               |
|---------------|---------|------------------------------|-------------------------------------|
| `id`          | Long    | autoGenerate                 | PK                                  |
| `targetType`  | String  | —                            | "COURSE", "SLIDE", "NOTE", "PROMPT" |
| `targetId`    | Long    | —                            | polymorphic target                  |
| `stateJson`   | String  | —                            | serialized state                    |
| `isCompleted` | Boolean | false                        |                                     |
| `createdAt`   | Long    | `System.currentTimeMillis()` |                                     |
| `updatedAt`   | Long    | `System.currentTimeMillis()` |                                     |
| `deletedAt`   | Long?   | null                         | soft delete                         |

**Indexes:** `deletedAt`

---

### 20. `study_sessions`

| Column                 | Type    | Default                      | Notes                                                    |
|------------------------|---------|------------------------------|----------------------------------------------------------|
| `id`                   | Long    | autoGenerate                 | PK                                                       |
| `targetType`           | String  | —                            | "QUIZ", "DECK", "COURSE", "NOTE_COLLECTION", "BLUEPRINT" |
| `targetId`             | Long    | —                            | polymorphic target                                       |
| `label`                | String? | null                         |                                                          |
| `stateJson`            | String  | —                            | serialized state                                         |
| `timeSpentMs`          | Long    | 0                            |                                                          |
| `completionPercentage` | Float   | 0f                           |                                                          |
| `isCompleted`          | Boolean | false                        |                                                          |
| `correctCount`         | Int     | 0                            |                                                          |
| `incorrectCount`       | Int     | 0                            |                                                          |
| `createdAt`            | Long    | `System.currentTimeMillis()` |                                                          |
| `updatedAt`            | Long    | `System.currentTimeMillis()` |                                                          |
| `deletedAt`            | Long?   | null                         | soft delete                                              |

**Indexes:** `(targetId, targetType)` composite, `deletedAt`

---

### 21. `question_categories`

| Column       | Type   | Default | Notes                                             |
|--------------|--------|---------|---------------------------------------------------|
| `questionId` | Long   | —       | **composite PK** part 1, FK → questions (CASCADE) |
| `category`   | String | —       | **composite PK** part 2                           |
| `deletedAt`  | Long?  | null    | soft delete                                       |

**Indexes:** `questionId`, `category`, `deletedAt`

---

### 22. `asset_references`

| Column      | Type   | Default                      | Notes                  |
|-------------|--------|------------------------------|------------------------|
| `id`        | Long   | autoGenerate                 | PK                     |
| `path`      | String | —                            | file path              |
| `ownerType` | String | —                            | polymorphic owner type |
| `ownerId`   | Long   | —                            | polymorphic owner ID   |
| `createdAt` | Long   | `System.currentTimeMillis()` |                        |
| `deletedAt` | Long?  | null                         | soft delete            |

**Indexes:** `path`, `(ownerType, ownerId)`, `(ownerType, ownerId, path)` unique, `deletedAt`

---

### 23. `question_assets`

| Column             | Type    | Default                      | Notes                           |
|--------------------|---------|------------------------------|---------------------------------|
| `id`               | Long    | autoGenerate                 | PK                              |
| `bookId`           | Long    | —                            |                                 |
| `quizId`           | Long    | —                            |                                 |
| `questionId`       | Long    | —                            | FK → questions (CASCADE)        |
| `assetType`        | String  | —                            | see QuestionAssetType constants |
| `title`            | String  | —                            |                                 |
| `description`      | String? | null                         |                                 |
| `localPath`        | String? | null                         |                                 |
| `externalUrl`      | String? | null                         |                                 |
| `mimeType`         | String? | null                         |                                 |
| `fileName`         | String? | null                         |                                 |
| `fileSizeBytes`    | Long?   | null                         |                                 |
| `textContent`      | String? | null                         |                                 |
| `sourceDocumentId` | Long?   | null                         |                                 |
| `sourcePage`       | String? | null                         |                                 |
| `sourceQuote`      | String? | null                         |                                 |
| `sortOrder`        | Int     | 0 (default `"0"`)            |                                 |
| `isPinned`         | Boolean | false (default `"0"`)        |                                 |
| `isPrimary`        | Boolean | false (default `"0"`)        |                                 |
| `createdAt`        | Long    | `System.currentTimeMillis()` |                                 |
| `updatedAt`        | Long    | `System.currentTimeMillis()` |                                 |
| `deletedAt`        | Long?   | null                         | soft delete                     |

**Indexes:** `bookId`, `quizId`, `questionId`, `assetType`, `createdAt`, `sourceDocumentId`,
`deletedAt`

---

### 24. `source_documents`

| Column        | Type    | Default                      | Notes                   |
|---------------|---------|------------------------------|-------------------------|
| `id`          | Long    | autoGenerate                 | PK                      |
| `bookId`      | Long?   | null                         | FK → books (CASCADE)    |
| `title`       | String  | —                            |                         |
| `sourceType`  | String  | `"Others"`                   | see SourceDocumentTypes |
| `author`      | String? | null                         |                         |
| `edition`     | String? | null                         |                         |
| `year`        | String? | null                         |                         |
| `publisher`   | String? | null                         |                         |
| `localPath`   | String? | null                         |                         |
| `externalUrl` | String? | null                         |                         |
| `description` | String? | null                         |                         |
| `createdAt`   | Long    | `System.currentTimeMillis()` |                         |
| `updatedAt`   | Long    | `System.currentTimeMillis()` |                         |
| `deletedAt`   | Long?   | null                         | soft delete             |

**Indexes:** `bookId`, `title`, `sourceType`, `deletedAt`

---

### 25. `mistake_log_entries`

| Column              | Type    | Default               | Notes                       |
|---------------------|---------|-----------------------|-----------------------------|
| `id`                | Long    | autoGenerate          | PK                          |
| `bookId`            | Long    | —                     |                             |
| `quizId`            | Long    | —                     |                             |
| `questionId`        | Long    | —                     | FK → questions (CASCADE)    |
| `sessionId`         | Long?   | null                  |                             |
| `selectedAnswer`    | String? | null                  |                             |
| `correctAnswer`     | String? | null                  |                             |
| `userReason`        | String? | null                  | why user chose wrong answer |
| `correctConcept`    | String? | null                  |                             |
| `preventionNote`    | String? | null                  |                             |
| `linkedFlashcardId` | Long?   | null                  |                             |
| `linkedBlueprintId` | Long?   | null                  |                             |
| `linkedAssetId`     | Long?   | null                  |                             |
| `isFixed`           | Boolean | false (default `"0"`) |                             |
| `reviewAt`          | Long?   | null                  |                             |
| `createdAt`         | Long    | 0 (default `"0"`)     |                             |
| `updatedAt`         | Long    | 0 (default `"0"`)     |                             |
| `deletedAt`         | Long?   | null                  | soft delete                 |

**Indexes:** `bookId`, `quizId`, `questionId`, `sessionId`, `reviewAt`, `isFixed`, `createdAt`,
`deletedAt`

---

### 26. `annotations`

| Column             | Type    | Default                         | Notes                                    |
|--------------------|---------|---------------------------------|------------------------------------------|
| `id`               | Long    | autoGenerate                    | PK                                       |
| `workspaceId`      | Long    | —                               | FK → workspaces (CASCADE)                |
| `bookId`           | Long    | —                               | FK → books (CASCADE)                     |
| `ownerType`        | String  | —                               | QUESTION / SLIDE / NOTE / SOURCE / ASSET |
| `ownerId`          | Long    | —                               | polymorphic target                       |
| `selectedText`     | String? | null                            | highlighted text                         |
| `noteBody`         | String? | null                            | user note                                |
| `colorLabel`       | String  | `"YELLOW"` (default `'YELLOW'`) | see AnnotationColorLabel                 |
| `positionDataJson` | String? | null                            | raw JSON for position                    |
| `createdAt`        | Long    | `System.currentTimeMillis()`    |                                          |
| `updatedAt`        | Long    | `System.currentTimeMillis()`    |                                          |
| `deletedAt`        | Long?   | null                            | soft delete                              |

**Indexes:** `workspaceId`, `bookId`, `(ownerType, ownerId)`, `colorLabel`, `deletedAt`, `updatedAt`

---

## Key Observations Across All Tables

- Every table uses **soft delete** (`deletedAt: Long?`)
- All foreign keys use **CASCADE** deletion
- Complex Kotlin types (`List`, `Map`, enum) are persisted as **TEXT** columns via Moshi-based
  `Converters`
- `@ColumnInfo(defaultValue = ...)` is set on columns added in later migrations, ensuring Room knows
  the SQL default

---

*Last updated: 2026-07-10*
