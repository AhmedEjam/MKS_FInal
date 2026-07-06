# CLAUDE.md

> Last updated: 2026-06-08. Active source is Room v26 with 25 migration steps (1→26). Schema source of truth: `app/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`.

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
- DI: Manual dependency injection via `AppModule` (no Hilt/Dagger)
- Database: Room v26 (with KSP, 25 migration steps)
- Preferences: Jetpack DataStore
- JSON: Moshi (with KSP)
- Images: Coil (25% RAM memory cache + disk cache)
- Navigation: Compose Navigation

**Project Structure:**
```
app/src/main/java/com/ahmedyejam/mks/
├── di/                  # AppModule - manual DI container (1403 lines)
├── data/
│   ├── local/           # Room database (v26), 24 DAOs, 24 entities, converters
│   ├── model/           # Data models
│   ├── preferences/     # DataStore manager
│   ├── repository/      # MksRepository (2632 lines), ExportManager
│   ├── import/          # Import pipeline (parser, validator, mapper, security)
│   ├── exchange/        # Bundle exchange format (v7)
│   ├── exportfull/      # Full library export service
│   ├── network/         # RemoteAssetFetcher
│   ├── focus/           # Adaptive training (FocusManager)
│   ├── search/          # GlobalSearchRepository
│   ├── review/          # ReviewRepository
│   ├── preview/         # Delete/merge preview services
│   ├── repair/          # Asset reference audit
│   ├── simulation/      # Training simulation
│   └── validation/      # Data validation
├── ui/
│   ├── library/         # Library management screens + components/
│   ├── quiz/            # Quiz player, compiler
│   ├── flashcard/       # Flashcard deck (list & study modes)
│   ├── slideshow/       # Slideshow course screen + ViewModel
│   ├── booktools/       # Knowledge bank tools (dashboard, blueprints, prompts, etc.)
│   ├── category/        # Category question management
│   ├── session/         # Session management
│   ├── summary/         # Post-quiz analytics
│   ├── scanner/         # Camera OCR scanner
│   ├── search/          # Global search screen
│   ├── review/          # Review dashboard
│   ├── data/            # Data tools (bulk import/export)
│   ├── settings/        # App settings
│   ├── welcome/         # Welcome/onboarding
│   ├── import/          # Import UI
│   ├── common/          # Shared screens (InvalidRouteScreen)
│   ├── components/      # Shared UI components
│   ├── navigation/      # Route builders & argument helpers
│   ├── theme/           # Theme, colors, typography, design tokens
│   ├── MksNavHost.kt    # Navigation graph (708 lines, 22+ routes)
│   └── MksRoutes.kt     # Route constants
├── util/                # Utilities
└── MainActivity.kt      # Single-activity entry (99 lines)
```

**Key Components:**

- **`MksApplication`**: Application class holding `AppModule` instance + Coil ImageLoader
- **`AppModule`**: Central DI container (1403 lines) providing database, repositories, managers, preview services
- **`MksDatabase`**: Room database (v26) with 25 migration steps, 24 entity classes, 24 DAOs
- **`MksRepository`**: Single source of truth (2632 lines) for all data operations
- **`GlobalSearchRepository`**: Cross-entity full-text search
- **`ReviewRepository`**: Unified review queue (flashcards, blueprints, mistakes)
- **ViewModels**: One per screen, receiving dependencies from `AppModule` via manual `ViewModelProvider.Factory`

**Data Flow:**
UI → ViewModel → Repository → DAO/DataStore → Database

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

**Entities (24 total):**

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
- `PromptEntity` - Legacy prompt entity
- `PromptDeckEntity` - AI prompt deck metadata
- `PromptCardEntity` - Individual prompt card with variables and output type
- `PromptRunEntity` - Prompt execution history
- `KnowledgeStudySessionEntity` - Generic progress tracker for non-quiz content

Assets & Additional:
- `AssetReferenceEntity` - Normalized local asset ownership index
- `SourceDocumentEntity` - Source materials linked to books
- `MistakeLogEntryEntity` - Mistake tracking with user explanations
- `AnnotationEntity` - Highlights and notes on content

**Migrations:** 25 schema versions with incremental migrations (1→26). Adding new columns requires a new migration in `MksMigrations.kt`.

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
