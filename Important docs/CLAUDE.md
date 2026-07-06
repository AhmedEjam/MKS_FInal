# CLAUDE.md

> Last updated: 2026-06-15. Active source is Room v29 with 28 migration steps (1→29). Schema source of truth: `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`. DI: 6 Hilt modules in `app/di/` + all 6 domain repositories use `@Inject constructor` (auto-wired).

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MKS (My Knowledge Space) is a native Android quiz and knowledge-bank application built with Kotlin and Jetpack Compose. It imports educational content from spreadsheets, presentations, and documents, then presents interactive quizzes, flashcards, slideshows, and study materials with image support. Features hierarchical data management (Workspaces → Books → Quizzes → Questions + Knowledge Bank assets), multi-workspace isolation, adaptive training, session persistence, a unified trash bin, and a full review/analytics dashboard.

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

# Run core:data module only (useful for fast iteration)
./gradlew :core:data:assembleDebug

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
- Database: Room v29 (with KSP, 28 migration steps)
- Preferences: Jetpack DataStore
- JSON: Moshi (with KSP) + kotlinx.serialization (import DTOs)
- Images: Coil (25% RAM memory cache + disk cache)
- Navigation: Compose Navigation
- Presentations: Apache POI (XLSX + PPTX parsing)

**Project Structure (6-Module):**
```text
/Users/ahmedy.ajam/Android MKS
├── app/src/main/java/com/ahmedyejam/mks/
│   ├── MainActivity.kt        # Single-activity entry point (loads settings, applies density, locale, and themes)
│   ├── MksApplication.kt      # @HiltAndroidApp Application class hosting legacy AppModule & Coil image loader
│   └── di/                    # 6 Hilt modules (all @InstallIn(SingletonComponent))
│       ├── HiltDataModule.kt          # Infrastructure: AppModule, MksDatabase, FileManager, ExportManager,
│       │                              #   ImportLibraryManager, LibraryMapper, DataStoreManager, FocusManager
│       ├── HiltDaoModule.kt           # Core quiz DAOs: BookDao, QuizDao, QuestionDao, SessionDao,
│       │                              #   CategoryMetadataDao, QuestionCategoryDao
│       ├── HiltKnowledgeDaoModule.kt  # Knowledge bank DAOs: SlideshowCourseDao, CourseSlideDao,
│       │                              #   NoteBlueprintDao, NoteCollectionDao, PromptDao, PromptDeckDao,
│       │                              #   PromptCardDao, PromptRunDao, KnowledgeStudySessionDao, StudySessionDao
│       ├── HiltUtilityDaoModule.kt    # Asset/utility DAOs: AssetReferenceDao, QuestionAssetDao,
│       │                              #   SourceDocumentDao, GlobalSearchDao, AnnotationDao,
│       │                              #   WorkspaceDao, FlashcardDeckDao, FlashcardDao,
│       │                              #   LearningSessionDao, MistakeLogDao
│       ├── HiltRepositoryModule.kt    # Singleton repos not using @Inject: OllamaRepository,
│       │                              #   GlobalSearchRepository, ReviewRepository
│       └── HiltServiceModule.kt       # Preview and repair services: DeletePreviewService,
│                                      #   CategoryMergePreviewService, ClearMarksPreviewService,
│                                      #   AssetReferenceAuditService
│
├── core/
│   ├── database/src/main/java/com/ahmedyejam/mks/
│   │   └── data/local/
│   │       ├── MksDatabase.kt     # Room v29 database instance declaring 26 entities, 26 DAOs
│   │       ├── MksMigrations.kt   # Centralized migration registry (28 migrations, v1→v29)
│   │       ├── Converters.kt      # Room TypeConverters (JSON serialization, Lists, Enums)
│   │       ├── WorkspaceDefaults.kt
│   │       └── dao/               # 26 Room DAOs:
│   │           ├── WorkspaceDao, BookDao, QuizDao, QuestionDao, SessionDao
│   │           ├── CategoryMetadataDao, QuestionCategoryDao, QuestionAssetDao
│   │           ├── FlashcardDeckDao, FlashcardDao, LearningSessionDao
│   │           ├── SlideshowCourseDao, CourseSlideDao
│   │           ├── NoteBlueprintDao, NoteCollectionDao
│   │           ├── PromptDao, PromptDeckDao, PromptCardDao, PromptRunDao
│   │           ├── KnowledgeStudySessionDao, StudySessionDao
│   │           ├── AssetReferenceDao, SourceDocumentDao
│   │           ├── MistakeLogDao, AnnotationDao
│   │           └── GlobalSearchDao  # Full-text search via raw SQL union queries
│   │
│   ├── data/src/main/java/com/ahmedyejam/mks/
│   │   ├── di/
│   │   │   └── AppModule.kt       # Trimmed legacy class: builds MksDatabase (Room.databaseBuilder +
│   │   │                          #   all migrations), constructs ReviewRepository, DeletePreviewService,
│   │   │                          #   CategoryMergePreviewService, ClearMarksPreviewService,
│   │   │                          #   AssetReferenceAuditService, DataStoreManager, OllamaRepository,
│   │   │                          #   FocusManager. Holds applicationScope. Provided to Hilt as singleton
│   │   │                          #   via HiltDataModule.provideAppModule(). No longer contains repository
│   │   │                          #   creation or seeding logic.
│   │   └── data/
│   │       ├── repository/         # 6 domain repositories (all use @Inject constructor — auto-wired by Hilt)
│   │       │   ├── BookRepository.kt       # Book CRUD, cover images, stats refresh, study bundles
│   │       │   │                           #   Uses Provider<QuizRepository>, Provider<AssetRepository> to break cycles
│   │       │   ├── QuizRepository.kt       # Quiz/Question CRUD, answers, scoring, category management
│   │       │   │                           #   Uses Provider<BookRepository>, Provider<AssetRepository>
│   │       │   ├── KnowledgeRepository.kt  # Flashcards, slideshows, notes, prompts, learning sessions, derived sync
│   │       │   │                           #   Uses Provider<QuizRepository>, Provider<BookRepository>, Provider<AssetRepository>
│   │       │   ├── StudyRepository.kt      # Quiz sessions, knowledge study sessions, mistake logs
│   │       │   │                           #   Uses Provider<QuizRepository>
│   │       │   ├── WorkspaceRepository.kt  # Workspace CRUD, default workspace, soft deletes
│   │       │   ├── AssetRepository.kt      # Asset references, question assets, source documents, annotations
│   │       │   ├── ExportManager.kt        # ZIP archiver exporting quizzes and knowledge bank assets
│   │       │   └── MksRepositoryModels.kt  # Shared models: KnowledgeSummary, BookKnowledgeSummary, SortOption, etc.
│   │       ├── preferences/       # Jetpack DataStore preferences (theme, font scale, UI density, language, sort, view mode)
│   │       ├── exchange/v7/       # Bundle exchange model parsing and ZIP archive reading
│   │       ├── importer/          # Multi-format import pipeline:
│   │       │   ├── detector/ImportFormatDetector.kt    # Magic-byte + extension format detection
│   │       │   ├── dto/LibraryBundleDto.kt             # Full library DTO (20 data classes for serialization)
│   │       │   ├── mapping/LibraryMapper.kt            # DTO ↔ Entity bidirectional mapping
│   │       │   ├── normalization/BundleNormalizer.kt   # Post-parse normalization (answer mode inference, trimming)
│   │       │   ├── model/                              # ImportFormat, MergeStrategy, ImportResult, ImportWarning, ImportError
│   │       │   ├── parser/                             # 12 parsers:
│   │       │   │   ├── SpreadsheetHeaderMapper.kt      #   Header row auto-detection (EN + AR aliases)
│   │       │   │   ├── SpreadsheetQuestionParser.kt    #   Row-by-row question extraction
│   │       │   │   ├── CsvParser.kt                    #   RFC 4180 CSV/TSV with delimiter inference
│   │       │   │   ├── JsonQuestionParser.kt           #   Nested JSON question objects
│   │       │   │   ├── JsonLibraryParser.kt            #   Full library JSON bundle parser
│   │       │   │   ├── HtmlQuestionParser.kt           #   HTML table/div extraction
│   │       │   │   ├── TextQuestionParser.kt           #   Line-by-line plain text
│   │       │   │   ├── TextFlashcardParser.kt          #   Plain text → FlashcardEntity (3 parse modes)
│   │       │   │   ├── TextSlideParser.kt              #   Plain text → CourseSlideEntity (3 parse modes)
│   │       │   │   ├── TextArticleParser.kt            #   Plain text → NoteBlueprintEntity (2 parse modes)
│   │       │   │   ├── PptxSlideParser.kt              #   PowerPoint PPTX → CourseSlideEntity (via Apache POI)
│   │       │   │   ├── ZipLibraryParser.kt             #   MKS ZIP bundle with manifest + assets
│   │       │   │   ├── GenericImageExtractor.kt        #   Data URL, HTTP URL, HTML img extraction
│   │       │   │   └── SourceDetector.kt               #   Smart format/mode detection from content
│   │       │   ├── xlsx/XlsxImageResolver.kt           #   Embedded cell image extraction from XLSX
│   │       │   ├── xlsx/XlsxLibraryCompiler.kt         #   Full XLSX → LibraryBundleDto compilation
│   │       │   ├── repository/ImportLibraryManager.kt  #   Orchestrates import: detect → parse → validate → persist
│   │       │   ├── security/ImportLimits.kt            #   File size and row count limits
│   │       │   └── validation/ImportValidator.kt       #   Pre-persist validation rules
│   │       ├── focus/             # FocusManager implementing adaptive mistake training logic
│   │       ├── search/            # GlobalSearchRepository performing full-text search across all entities
│   │       ├── review/            # ReviewRepository + ReviewModels (7 queue types: flashcard, blueprint, mistake, marked, weak, slide, annotation)
│   │       ├── preview/           # Delete preview, category merge preview, and clear marks preview services
│   │       ├── repair/            # AssetReferenceAuditService verifying asset reference integrity
│   │       ├── seeder/            # MksDatabaseSeeder (sample workspace + bilingual book + quiz seeding)
│   │       ├── validation/        # QuestionValidator + SessionStateValidator
│   │       └── local/
│   │           └── FileManager.kt # Low-level file, Base64, and image I/O downloader
│   │
│   ├── model/src/main/java/com/ahmedyejam/mks/
│   │   ├── ui/
│   │   │   └── MksRoutes.kt       # Navigation routes and argument keys (22+ screens)
│   │   ├── util/                  # MksLogger and bounded stream utilities
│   │   └── data/
│   │       ├── local/entity/      # 26 Room Entities
│   │       ├── model/             # Pure Kotlin data models (CategoryWithMetadata, ExportResult, generation configs, etc.)
│   │       ├── search/            # GlobalSearchModels (SearchResultType enum, GlobalSearchResultRow, SearchQuery)
│   │       └── simulation/        # ChangeSimulationModels for delete/merge previews
│   │
│   ├── network/src/main/java/com/ahmedyejam/mks/
│   │   └── data/
│   │       ├── repository/
│   │       │   └── OllamaRepository.kt # Ollama local LLM integration and generation configurations
│   │       └── network/
│   │           ├── RemoteAssetFetcher.kt # Remote HTTPS asset network downloader
│   │           └── RemoteAssetPolicy.kt
│   │
│   └── ui/src/main/java/com/ahmedyejam/mks/ui/
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
        ├── navigation/MksRouteValidator.kt  # Route argument validation utilities
        ├── library/                # Main Library screens & components:
        │   ├── LibraryScreen.kt        # Main hub with categories and books
        │   ├── LibraryViewModel.kt     # Library state, sorting, filtering, workspace switching, trash management
        │   └── components/             # LibraryContentGrid, SortDialog, FabMenu, LibraryDialogs, TopBar
        ├── quiz/                   # Quiz system:
        │   ├── QuizPlayerScreen.kt     # Quiz players (rapid mode, option elimination)
        │   ├── QuizViewModel.kt        # Quiz engine with shuffling, timers, streaks, adaptive training
        │   ├── QuizQuestionsScreen.kt  # Question browser & editor
        │   ├── QuizDetailTabsScreen.kt # Tabbed quiz detail view
        │   ├── CompilerDialog.kt       # Interactive spreadsheet mapping compiler UI
        │   ├── CompilerViewModel.kt    # Import compilation state machine
        │   └── ZoomableImageDialog.kt  # Full-screen pinch-to-zoom image viewer
        ├── flashcard/              # Flashcard deck viewer & study player with spaced-repetition metrics
        ├── slideshow/              # Slideshow player with auto-scroll and completion tracking
        ├── booktools/              # Book knowledge dashboard:
        │   ├── BookKnowledgeDashboardScreen.kt  # Main dashboard with HorizontalPager (8 tabs)
        │   ├── BookDashboardTabs.kt             # Tab composables (Dashboard, Quizzes, Slides, Cards, Notes, Prompts, Mistakes, Sources)
        │   ├── BookToolScreens.kt               # Supporting screens (DashboardSummaryCard, MagicActions, BookToolListItem)
        │   └── BookToolsViewModel.kt            # Knowledge bank state and CRUD operations
        ├── category/               # Category questions list, tag editor, and asset viewer
        ├── scanner/                # Camera OCR text-recognition scanner
        ├── search/                 # Cross-entity global search screen
        ├── review/                 # Spaced repetition review queue dashboard (7 queue types)
        ├── session/                # Quiz study session manager and resume log
        ├── summary/                # Post-quiz summary with analytics
        ├── data/                   # Data tools (bulk library backup, restore, merge strategies)
        ├── importer/               # ImportViewModel for full-library bundle import orchestration
        ├── settings/               # App configuration (theme, density, font scale, language, Ollama LLM, data wipe)
        ├── trash/                  # TrashBinDialog (6 tabs: Books, Quizzes, Flashcards, Slideshows, Notes, Prompts)
        ├── workspace/              # WorkspaceManagerDialog (create, edit, switch, delete, restore workspaces)
        └── welcome/                # Onboarding welcome screen
```

**Key Components:**

- **`MksApplication`**: Application class holding `AppModule` instance + Coil ImageLoader, annotated with `@HiltAndroidApp`.
- **Hilt DI (6 modules in `app/di/`):**
  - `HiltDataModule` — Infrastructure singletons: `AppModule`, `MksDatabase`, `FileManager`, `ExportManager`, `ImportLibraryManager`, `LibraryMapper`, `DataStoreManager`, `FocusManager`
  - `HiltDaoModule` — Core quiz DAOs (6): `BookDao`, `QuizDao`, `QuestionDao`, `SessionDao`, `CategoryMetadataDao`, `QuestionCategoryDao`
  - `HiltKnowledgeDaoModule` — Knowledge bank DAOs (10): `SlideshowCourseDao`, `CourseSlideDao`, `NoteBlueprintDao`, `NoteCollectionDao`, `PromptDao`, `PromptDeckDao`, `PromptCardDao`, `PromptRunDao`, `KnowledgeStudySessionDao`, `StudySessionDao`
  - `HiltUtilityDaoModule` — Asset/utility DAOs (10): `AssetReferenceDao`, `QuestionAssetDao`, `SourceDocumentDao`, `GlobalSearchDao`, `AnnotationDao`, `WorkspaceDao`, `FlashcardDeckDao`, `FlashcardDao`, `LearningSessionDao`, `MistakeLogDao`
  - `HiltRepositoryModule` — Non-`@Inject` repos: `OllamaRepository`, `GlobalSearchRepository`, `ReviewRepository`
  - `HiltServiceModule` — Preview & repair services: `DeletePreviewService`, `CategoryMergePreviewService`, `ClearMarksPreviewService`, `AssetReferenceAuditService`
- **`AppModule`** (legacy, `core/data`): Trimmed to: database builder (with all migrations), `ReviewRepository`, preview services, `AssetReferenceAuditService`, `DataStoreManager`, `OllamaRepository`, `FocusManager`, `applicationScope`. No longer creates repositories or seeds data.
- **`MksDatabase`**: Room database (v29) with 28 migration steps, 26 entity classes, and 26 DAOs. Includes `addColumnIfMissing()` / `columnExists()` helpers for safe migrations.
- **Domain Repositories** (6 total, all `@Inject constructor` — auto-wired by Hilt, circular deps broken via `javax.inject.Provider<T>`):
  - `BookRepository` — Book CRUD, cover images, stats refresh, study bundles
  - `QuizRepository` — Quiz/Question CRUD, answers, scoring, category management
  - `KnowledgeRepository` — Flashcards, slideshows, notes, prompts, prompt output conversion, learning sessions, derived asset sync
  - `StudyRepository` — Quiz sessions, knowledge study sessions, mistake logs
  - `WorkspaceRepository` — Workspace CRUD, default workspace, soft deletes
  - `AssetRepository` — Asset references, question assets, source documents, annotations, image resolution
- **`GlobalSearchRepository`**: Cross-entity full-text search via raw SQL union queries (26 DAOs total including `GlobalSearchDao`).
- **`ReviewRepository`**: Unified review queue (7 types: flashcard, blueprint, mistake, marked question, weak question, unfinished slide, annotation).
- **ViewModels** (16 total): All annotated `@HiltViewModel` + `@Inject constructor`. Receive domain repositories directly. Resolved using `hiltViewModel()` inside Compose NavHost.
  - `FlashcardDeckViewModel` and `SlideshowCourseViewModel` still receive `AppModule` directly for `applicationScope` access.
  - `SettingsScreen` composable also receives `AppModule` directly for DataStore/FocusManager access.

**Data Flow:**
UI → ViewModel (`@HiltViewModel` + `@Inject constructor`) → Domain Repository (`@Inject constructor`) → DAO (Hilt-provided) → Database

**Navigation (22+ routes):**
Single-activity architecture with `NavHostController`. Key routes:
- `library` (start) → `category/{category}` → `quiz/{quizId}?sessionId={sessionId}` → `summary/{sessionId}`
- `library` → `sessions/{quizId}` → resume quiz
- `library` → `scanner/{quizId}` (OCR import)
- `library` → `adaptive/{type}/{id}` (adaptive training, type: BOOK|CATEGORY|QUIZ|ALL)
- `library` → `book_dashboard/{bookId}` (knowledge dashboard with 8 tabs)
- `library` → `flashcards/{deckId}?cardId={cardId}` (flashcard study)
- `library` → `slideshow/{courseId}?slideId={slideId}` (slideshow)
- `library` → `blueprint/{noteId}` (review blueprint/article reader)
- `library` → `prompt_deck/{promptId}?cardId={cardId}&runId={runId}` (AI prompts)
- `library` → `book_slideshows/{bookId}` / `book_blueprints/{bookId}` / `book_sources/{bookId}` / `book_notes/{bookId}` / `book_prompts/{bookId}` (book-level lists)
- `settings` → `global_search` / `review_dashboard?mistakeId={id}` / `data_tools`

## Database Schema

**Entities (26 total — Room v29):**

Workspace:
- `WorkspaceEntity` - Multi-workspace support with soft deletes
- `WorkspaceSettingsEntity` - Per-workspace preferences

Core Quiz/Library:
- `BookEntity` - Top-level container with cover image, stats, pin/system flags, unique externalId index
- `QuizEntity` - Belongs to Book, with tags, cover image, cached stats, unique externalId index
- `QuestionEntity` - Supports Single/Multiple Choice, Boolean types; options, metadata, weight
- `SessionEntity` - User progress, timer settings, streaks, answers
- `CategoryMetadataEntity` - Category emojis, colors, pin state
- `QuestionCategoryEntity` - Normalized many-to-many question/category join table
- `QuestionAssetEntity` - Generic assets (images, docs, files) linked to questions

Knowledge Bank:
- `FlashcardDeckEntity` - Deck metadata, progress, pin flags
- `FlashcardEntity` - Front/back card, hint, tags, review metrics, optional sourceQuestionId, syncConfig
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
- `MistakeLogEntryEntity` - Mistake tracking with user explanations
- `AnnotationEntity` - Highlights and notes on content

> **Note:** `SourceDocumentAssetEntity` was dropped in migration v28→v29.

**Migrations:** Centralized in `MksMigrations.kt` (28 steps total: v1→v29). All migrations must be registered in both the migration list and verified through migration test cases.

## Key Features

### Quiz Engine
- Shuffling, timers, "Rapid Mode", option elimination, adaptive training, streaks
- Post-quiz summary with per-category performance metrics
- Session persistence (unfinished quizzes saved to Room/DataStore)
- Zoomable image viewer for question images

### Knowledge Bank (per Book)
- Flashcard decks with spaced repetition metrics and learning sessions
- Slideshow courses with auto-scroll, per-slide completion tracking
- Note blueprints / articles with immersive reader (autoscroll, TTS, pinned title)
- AI prompt decks with variable extraction, live rendering, output routing (→ Note, Flashcard, Blueprint, Quiz)
- Prompt run history tracking
- Derived asset sync (question changes propagate to linked flashcards/slides/notes)

### Import/Export Pipeline
- **12 format-specific parsers**: XLSX, CSV/TSV, JSON, HTML, TEXT (questions + flashcards + slides + articles), PPTX, ZIP bundles
- Smart format detection (extension → MIME → magic bytes → heuristic)
- Interactive compiler UI with header row auto-detection, column mapping, and preview
- Full library bundle import/export with merge strategies (REPLACE, MERGE, SKIP)
- Import validation, security limits, and normalization
- LibraryBundleDto system (20 DTOs for complete serialization)

### Workspace Management
- Multi-workspace support with isolation of books and content
- Create, edit, switch, delete, and restore workspaces
- Default workspace auto-creation and protection
- Workspace-scoped settings (language, theme, defaults)

### Trash Bin & Soft Deletes
- Unified trash bin with 6 tabs (Books, Quizzes, Flashcards, Slideshows, Notes, Prompts)
- Individual restore and permanent delete
- "Empty Trash" bulk purge with confirmation
- Cascading soft deletes (book → quizzes → questions → annotations)

### Image Support
- Questions, slides, and flashcards can have images (URLs or local paths)
- Coil caching with 25% RAM memory cache + disk cache
- Automatic HTTP image download to local storage on insert
- Embedded XLSX image extraction, Data URL / Base64 / HTML img parsing
- Full-screen pinch-to-zoom image viewer

### Analytics & Review
- Post-quiz summaries, streaks, per-category performance metrics
- Book-level knowledge summaries (across all content types)
- Library-level knowledge summaries
- Unified review dashboard with 7 queue types (flashcard, blueprint, mistake, marked question, weak question, unfinished slide, annotation)

### Global Search
- Cross-entity search across 16 result types (book, quiz, question, answer, explanation, hint, note, asset, source, flashcard, blueprint, slide, prompt deck/card/run, mistake, annotation, category/tag)
- Navigable results (each result links to its parent screen)

### Data Tools
- Full library export to ZIP with asset bundling
- Selective quiz/book export
- Bulk import with preview and merge strategies

### Accessibility & Localization
- Bilingual: English + Arabic (RTL layout support)
- Text-to-Speech (TTS) narration with adjustable pitch/rate
- Adjustable font scale (0.8x–1.5x) and UI density
- 7 themes: Dawn, Forest, Midnight, Lavender, Plain Light, Plain Dark, System

### AI Integration
- Local LLM via Ollama (configurable base URL + model name)
- Prompt deck editor with variable detection (`{}`, `[]`, `()` syntax)
- Output routing to Notes, Flashcards, Blueprints, or Quizzes

## Testing

**Test Suite (current coverage):**

⚠️ **Known gaps:** v26→v27, v27→v28, v28→v29 migrations are untested (database is currently v29). Add `Migration26To27Test`, `Migration27To28Test`, `Migration28To29Test` + a full `MigrateAll1To29Test` chain test to verify end-to-end migration integrity.

Instrumented (androidTest):
- `Migration15To16Test`, `Migration16To17Test`, `Migration22To23Test`, `Migration23To24Test`, `Migration24To25Test`, `Migration25To26Test` — Migration regression tests
- `AnnotationDaoTest`, `QuestionDaoTest`, `QuestionCategoryDaoTest`, `AssetReferenceDaoTest` — DAO integration tests
- `ImportReconciliationTest`, `XlsxImportTest`, `ImportTargetQuizTest` — Import pipeline integration tests

Unit (test):
- `SpreadsheetHeaderMapperTest` — Header detection and column mapping
- `ParserBugFixTest` — Parser regression tests
- `BoundedStreamsTest` — Security stream limits
- `ImportValidatorTest` — Pre-persist validation
- `FileManagerTest` — File I/O operations
- `SettingsSanitizerTest` — Preference validation
- `QuestionValidatorTest`, `SessionStateValidatorTest` — Data integrity validation
- `MksRouteBuildersTest` — Navigation route builder correctness

## Hilt Injection Map (Quick Reference)

| ViewModel | Injected Domain Repos | Notes |
|---|---|---|
| `LibraryViewModel` | Book, Quiz, Knowledge, Workspace, Asset | + ExportManager, DataStoreManager |
| `BookToolsViewModel` | Book, Quiz, Knowledge, Study, Asset | + Ollama, DataStoreManager, FileManager |
| `QuizViewModel` | Quiz, Knowledge, Study, Asset | + DataStoreManager, FocusManager |
| `FlashcardDeckViewModel` | Knowledge, Study | + **AppModule** (applicationScope) |
| `SlideshowCourseViewModel` | Knowledge | + **AppModule** (applicationScope) |
| `CompilerViewModel` | Knowledge, Quiz | + @ApplicationContext |
| `CategoryQuestionsViewModel` | Quiz, Knowledge, Asset | |
| `QuizQuestionsViewModel` | Quiz, Knowledge, Asset | |
| `ReviewDashboardViewModel` | Study | + ReviewRepository |
| `GlobalSearchViewModel` | — | + GlobalSearchRepository |
| `SummaryViewModel` | Study, Quiz | + DataStoreManager |
| `SessionViewModel` | Study | |
| `DataToolsViewModel` | Book, Quiz | + ExportManager, ImportLibraryManager |
| `ScannerViewModel` | Quiz | + @ApplicationContext |
| `SettingsViewModel` | — | + DataStoreManager |
| `ImportViewModel` | Book, Quiz, Knowledge | + ImportLibraryManager |

> **Remaining `AppModule` coupling:** `FlashcardDeckViewModel`, `SlideshowCourseViewModel`, and `SettingsScreen` composable still reference `AppModule` directly. Target for Phase 2 cleanup.

## Notes

- KSP is used for Room and Moshi code generation
- Min SDK: 30, Target SDK: 35, Compile SDK: 35
- JVM target: Java 11
- Gradle modules: `:app`, `:core:model`, `:core:database`, `:core:data`, `:core:network`, `:core:ui`, `:feature:ui`
- 7 themes: Dawn, Forest, Midnight, Lavender, Plain Light, Plain Dark, System
- Release R8 minification and resource shrinking enabled
- Room schema export enabled
- Apache POI 5.5.1 for XLSX + PPTX processing
- Route validation guards (`requirePositiveLongArg`, `requireNonBlankStringArg`) with `InvalidRouteScreen` fallback
- kotlinx.serialization used alongside Moshi for import DTO serialization
- `MksDatabase` includes `addColumnIfMissing()` / `columnExists()` helpers for safe migrations
- Circular dependencies between repositories resolved with `javax.inject.Provider<T>` (not `@Lazy`)

## Infrastructure Status

✅ **Version Catalog**: `gradle/libs.versions.toml` exists and is in use.

✅ **CI Pipeline**: `.github/workflows/android-ci.yml` exists and runs lint→test→build. Consider extending with detekt + ktlint, Kover coverage thresholds (40%+ on `core/data`), and Room schema-export diff checks.

🔴 **Repository Hygiene Debt**: ~25 Python/patch scripts remain in repo root (`add_hilt*.py`, `fix_*.py`, `patch_*.py`, `update_*.py`, `test_*.py`) along with stray Kotlin files (`query_books.kt`, `test_parser.kt`) and generated reports. These should be moved to `scripts/archive/` and documented in `CONTRIBUTING.md` for onboarding clarity and security.
