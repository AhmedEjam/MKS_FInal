# CLAUDE.md

> Last updated: 2026-06-14. Active source is Room v28 with 27 migration steps (1→28). Schema source of truth: `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`.

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MKS (Mobile Knowledge System) is a native Android quiz and knowledge-bank application built with Kotlin and Jetpack Compose. It imports educational content from spreadsheets and documents, then presents interactive quizzes, flashcards, slideshows, and study materials with image support. Features hierarchical data management (Books → Quizzes → Questions + Knowledge Bank assets), adaptive training, and session persistence.

## Build & Development Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install debug APK on connected device
./gradlew installDebug

# Run all tests
./gradlew test

# Run unit tests only
./gradlew testDebugUnitTest

# Run instrumented tests only
./gradlew connectedAndroidTest

# Clean and rebuild
./gradlew clean build

# Check for dependency updates
./gradlew dependencyUpdates
```

## Architecture

**Tech Stack:**
- Language: Kotlin
- UI: Jetpack Compose with Material 3
- DI: Dagger Hilt (with legacy `AppModule` for MainActivity early startup configuration)
- Database: Room v28 (with KSP, 27 migration steps)
- Preferences: Jetpack DataStore
- JSON: Moshi (with KSP)
- Images: Coil (25% RAM memory cache + disk cache)
- Navigation: Compose Navigation

**Project Structure (Multi-Module):**
```text
/Users/ahmedy.ajam/Android MKS
├── app/src/main/java/com/ahmedyejam/mks/
│   ├── MainActivity.kt        # Single-activity entry point (loads settings, applies density, locale, and themes)
│   ├── MksApplication.kt      # Hilt Application class hosting legay AppModule & Coil image cache settings
│   └── di/
│       └── HiltDataModule.kt  # Hilt module binding dependencies (Repositories, DAOs, services) for injection
│
├── core/
│   ├── database/src/main/java/com/ahmedyejam/mks/
│   │   ├── core/database/di/
│   │   │   └── DatabaseModule.kt  # Database Hilt providers
│   │   └── data/local/
│   │       ├── MksDatabase.kt     # Room v28 database instance declaring all 27 entities
│   │       ├── MksMigrations.kt   # Centralized migration registry (27 migrations, v1→v28)
│   │       ├── Converters.kt      # Room TypeConverters (JSON serialization, Lists, and Enums)
│   │       ├── WorkspaceDefaults.kt
│   │       └── dao/               # 27 Room DAOs (BookDao, QuizDao, FlashcardDao, PromptDeckDao, etc.)
│   │
│   ├── data/src/main/java/com/ahmedyejam/mks/
│   │   ├── di/
│   │   │   └── AppModule.kt       # Legacy central DI container retained for low-level application startup preferences
│   │   └── data/
│   │       ├── repository/        # Main repository (MksRepository) split by data layers (Book, Study, Workspace, etc.)
│   │       │   └── ExportManager.kt # ZIP archiver exporting quizzes and knowledge bank assets
│   │       ├── preferences/       # Jetpack DataStore preferences (theme mode, font scale, UI density, language)
│   │       ├── exchange/v7/       # Bundle exchange model parsing and ZIP archive reading
│   │       ├── importer/          # Multi-format import compilers (JSON, XLSX POI compiler, CSV quote handler)
│   │       ├── focus/             # FocusManager implementing adaptive mistake training logic
│   │       ├── search/            # GlobalSearchRepository performing full-text search across all entities
│   │       ├── review/            # ReviewRepository for unified review queues
│   │       ├── preview/           # Delete preview, category merge preview, and clear marks preview services
│   │       ├── repair/            # AssetReferenceAuditService verifying asset reference integrity
│   │       ├── validation/        # Validation services for study session state and question structure
│   │       └── local/
│   │           └── FileManager.kt # Low-level file, Base64, and image I/O downloader
│   │
│   ├── model/src/main/java/com/ahmedyejam/mks/
│   │   ├── ui/
│   │   │   └── MksRoutes.kt       # Navigation routes and argument keys (22+ screens)
│   │   ├── util/                  # MksLogger and bounded stream utilities
│   │   └── data/
│   │       ├── local/entity/      # 27 Room Entities (BookEntity, QuizEntity, SlideshowCourseEntity, etc.)
│   │       ├── model/             # Pure Kotlin data models (CategoryWithMetadata, ExportResult, etc.)
│   │       └── search/            # Search query criteria and result data structures
│   │
│   ├── network/src/main/java/com/ahmedyejam/mks/
│   │   └── data/
│   │       ├── repository/
│   │       │   └── OllamaRepository.kt # Ollama local LLM integration and generation configurations
│   │       └── network/
│   │           ├── RemoteAssetFetcher.kt # Remote HTTPS asset network downloader
│   │           └── RemoteAssetPolicy.kt
│   │
│   └── ui/src/main/res/ & src/main/java/com/ahmedyejam/mks/ui/
│       ├── common/
│       │   └── InvalidRouteScreen.kt # Fallback route validation screen
│       ├── components/
│       │   ├── MksReusableComponents.kt # Common UI components (buttons, custom cards, dynamic animations)
│       │   ├── EntityEditDialog.kt     # Shared dialogs for creating/editing books, quizzes, and decks
│       │   ├── StudyTopAppBar.kt       # Study app bar with integrated timer
│       │   └── ChangePreviewDialog.kt  # Pre-action dialogs presenting merge/deletion counts
│       ├── theme/                  # 7 App Themes (Dawn, Lavender, Forest, Midnight, etc.) and custom Design Tokens
│       └── utils/
│           └── TtsManager.kt       # Text-to-Speech narration manager (used in note reading player)
│
└── feature/
    └── ui/src/main/java/com/ahmedyejam/mks/ui/
        ├── MksNavHost.kt           # NavHost graph binding all viewmodels using hiltViewModel()
        ├── library/                # Main Library screens & components (LibraryContentGrid, SortDialog, FabMenu)
        ├── quiz/                   # Quiz players (rapid mode, option elimination) & Compiler UI (interactive spreadsheet mapping)
        ├── flashcard/              # Flashcard deck viewer & study player with spaced-repetition metrics
        ├── slideshow/              # Slideshow player with auto-scroll and completion tracking
        ├── booktools/              # Book knowledge dashboard (dashboard, prompts editor, mistake logs, note reader)
        ├── category/               # Category questions list, tag editor, and asset viewer
        ├── scanner/                # Camera OCR text-recognition scanner
        ├── search/                 # Cross-entity global search screen
        ├── review/                 # Spaced repetition review queue dashboard
        ├── session/                # Quiz study session manager and resume log
        ├── data/                   # Data tools (bulk library backup, restore, merge strategies)
        ├── settings/               # App configuration (theme selection, Local LLM Ollama settings, data wipe)
        └── welcome/                # Onboarding welcome screen
```

**Key Components:**

- **`MksApplication`**: Application class holding legacy `AppModule` instance + Coil ImageLoader, annotated with `@HiltAndroidApp`.
- **`AppModule`**: Legacy central DI container retained for low-level application and main activity startup preferences.
- **`MksDatabase`**: Room database (v28) with 27 migration steps, 27 entity classes, and DAOs.
- **`MksRepository`**: Single source of truth for all quiz and study session data operations.
- **`GlobalSearchRepository`**: Cross-entity full-text search.
- **`ReviewRepository`**: Unified review queue (flashcards, blueprints, mistakes).
- **ViewModels**: Managed by Hilt via `@HiltViewModel` and injected with constructor parameters, resolved using `hiltViewModel()` inside Compose NavHost.

**Data Flow:**
UI → ViewModel (Hilt resolved) → Repository → DAO/DataStore → Database

**Navigation (22+ routes):**
Single-activity architecture with `NavHostController`. Key routes:
- `library` (start) → `category/{category}` → `quiz/{quizId}?sessionId={sessionId}` → `summary/{sessionId}`
- `library` → `sessions/{quizId}` → resume quiz
- `library` → `scanner/{quizId}` (OCR import)
- `library` → `adaptive/{type}/{id}` (adaptive training, type: BOOK|CATEGORY|QUIZ|ALL)
- `library` → `book_dashboard/{bookId}` (knowledge dashboard)
- `library` → `flashcards/{deckId}?cardId={cardId}` (flashcard study)
- `library` → `slideshow/{courseId}?slideId={slideId}` (slideshow)
- `library` → `blueprint/{noteId}` (review blueprint)
- `library` → `prompt_deck/{promptId}?cardId={cardId}&runId={runId}` (AI prompts)
- `library` → `book_slideshows/{bookId}` / `book_blueprints/{bookId}` / `book_sources/{bookId}` / `book_notes/{bookId}` / `book_prompts/{bookId}` (book-level lists)
- `settings` → `global_search` / `review_dashboard?mistakeId={id}` / `data_tools`

## Database Schema

**Entities (27 total):**

Workspace:
- `WorkspaceEntity` - Multi-workspace support with soft deletes
- `WorkspaceSettingsEntity` - Per-workspace preferences

Core Quiz/Library:
- `BookEntity` - Top-level container with cover image, stats, pin/system flags
- `QuizEntity` - Belongs to Book, contains Questions
- `QuestionEntity` - Supports Single/Multiple Choice, Boolean types; options, metadata, weight
- `SessionEntity` - User progress, timer settings, streaks, answers
- `CategoryMetadataEntity` - Category emojis, colors, pin state
- `QuestionCategoryEntity` - Normalized many-to-many question/category join table
- `QuestionAssetEntity` - Generic assets (images, docs, files) linked to questions

Knowledge Bank:
- `FlashcardDeckEntity` - Deck metadata, progress, pin flags
- `FlashcardEntity` - Front/back card, hint, tags, review metrics, optional sourceQuestionId
- `LearningSessionEntity` - Flashcard-deck learning session state (JSON)
- `SlideshowCourseEntity` - Course metadata, progress, derivation flags
- `CourseSlideEntity` - Slide body, notes, image, order, completion state
- `NoteBlueprintEntity` - Note body, summary, bullet points, tags, review counters
- `NoteCollectionEntity` - Grouping for note blueprints
- `PromptEntity` - Legacy prompt entity
- `PromptDeckEntity` - AI prompt deck metadata
- `PromptCardEntity` - Individual prompt card with variables and output type
- `PromptRunEntity` - Prompt execution history
- `KnowledgeStudySessionEntity` - Generic progress tracker for non-quiz content
- `StudySessionEntity` - Non-quiz study session progress tracker

Assets & Additional:
- `AssetReferenceEntity` - Normalized local asset ownership index
- `SourceDocumentEntity` - Source materials linked to books
- `SourceDocumentAssetEntity` - Assets associated with reference documents
- `MistakeLogEntryEntity` - Mistake tracking with user explanations
- `AnnotationEntity` - Highlights and notes on content

**Migrations:** Centralized in `MksMigrations.kt`. All migrations must be registered in both the migration list and verified through migration test cases.

## Key Features

- **Quiz Engine**: Shuffling, timers, "Rapid Mode", option elimination, adaptive training, streaks
- **Knowledge Bank**: Flashcard decks, slideshow courses, note blueprints, AI prompt decks per book
- **Import/Export**: Multi-format (XLSX, CSV/TSV, JSON, HTML, TEXT, ZIP) with validation, security, and preview
- **Session Persistence**: Unfinished quizzes saved to Room/DataStore
- **Image Support**: Questions can have images (URLs or local paths); Coil caching
- **Analytics**: Post-quiz summaries, streaks, per-category performance metrics
- **Global Search**: Cross-entity search across books, quizzes, questions, flashcards, notes, etc.
- **Review Dashboard**: Unified queue for flashcard reviews, blueprint reviews, and mistake logs
- **Data Tools**: Full library import/export with preview and merge strategies
- **Bilingual**: English + Arabic (RTL layout support)

## Notes

- KSP is used for Room and Moshi code generation
- Min SDK: 30, Target SDK: 35, Compile SDK: 35
- JVM target: Java 11
- 7 themes: Dawn, Forest, Midnight, Lavender, Plain Light, Plain Dark, System
- Release R8 minification and resource shrinking enabled
- Room schema export enabled
- Apache POI 5.5.1 for XLSX processing
- Route validation guards (requirePositiveLongArg, requireNonBlankStringArg) with InvalidRouteScreen fallback
