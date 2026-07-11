# AGENTS.md - MKS Project Guidance

> **Last updated:** 2026-07-11 | Room v30 | 29 migrations | 26 entities | 26 DAOs | 6-module architecture
>
> **This file is the single canonical context guide for AI agents.** It consolidates project overview, AI navigation rules, architecture, import pipeline, UI reference, and common tasks. Read the **AI Navigation Guide** section first; it contains the authoritative file paths.

**MKS** (My Knowledge Space) is an Android quiz and knowledge-bank application that imports educational content from spreadsheets and documents, then presents interactive quizzes, flashcards, slideshows, note blueprints, and AI prompt decks with image support. Features hierarchical data management (Workspaces → Books → Quizzes → Questions + Knowledge Bank assets), multi-workspace isolation, adaptive training, session persistence, a unified trash bin, and a full review/analytics dashboard.

---

## AI Navigation Guide (READ FIRST)

> **Do not guess file locations or architectures.** Use the paths below. The project is modularized into `app/`, `core/`, and `feature/` directories. Base package: `com.ahmedyejam.mks`.

**Do NOT search this directory** — it contains build output that wastes context:
- `build/`

### File Navigation Strategy (authoritative paths)

| What to find | Where to look |
|---|---|
| **UI & Screens** | `feature/ui/src/main/java/com/ahmedyejam/mks/ui/` — screens end with `Screen.kt`, ViewModels with `ViewModel.kt`. Shared UI components/theme in `core/ui/src/main/java/com/ahmedyejam/mks/ui/`. |
| **Database schema & migrations** | `core/database/src/main/java/com/ahmedyejam/mks/data/local/` — `MksDatabase.kt` (schema), `MksMigrations.kt` (all migrations). Always check here before altering entities. |
| **Entities** | `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/` — 26 entity classes. |
| **DAOs** | `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/` — 26 DAO interfaces. |
| **Repositories** | `core/data/src/main/java/com/ahmedyejam/mks/data/repository/` — 6 domain repositories + `ExportManager`, `MksRepositoryModels`. |
| **Import pipeline** | `core/data/src/main/java/com/ahmedyejam/mks/data/importer/` — `ImportFormatDetector`, parsers, xlsx, `ImportLibraryManager`. |
| **Network & LLM** | `core/network/src/main/java/com/ahmedyejam/mks/` — `OllamaRepository`, `RemoteAssetFetcher`, `RemoteAssetPolicy`. |
| **Dependency Injection** | `app/src/main/java/com/ahmedyejam/mks/di/` — 6 Hilt modules. |
| **Domain Models** | `core/model/src/main/java/com/ahmedyejam/mks/data/model/` — `CategoryWithMetadata`, `LearningSessionState`, export/generation configs, etc. |
| **Route Constants** | `core/model/src/main/java/com/ahmedyejam/mks/ui/MksRoutes.kt` |
| **NavHost** | `feature/ui/src/main/java/com/ahmedyejam/mks/ui/MksNavHost.kt` |

### Dynamic Inspection Workflow

1. **Search context:** UI bug → `grep` the Compose screen name in `feature/ui/`. Data bug → `grep` the entity in `core/model/.../data/local/entity/` or the DAO in `core/database/.../dao/`.
2. **Trace dependencies:** All dependencies flow through Dagger Hilt. ViewModels are `@HiltViewModel` with `@Inject constructor(...)`; screens resolve them with `hiltViewModel()`.
3. **Verify DB state:** If modifying data, check whether a Room migration is required by inspecting `MksDatabase.kt`'s `MKS_DATABASE_VERSION` constant and `MksMigrations.kt`.
4. **Read before writing:** If modifying a screen, read its ViewModel. If modifying an entity, read its DAO and migration history. Trust the rules in this document over standard Android documentation when conflicts arise (especially regarding DI and module boundaries).

---

## Project Overview

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (single-activity, Material 3)
- **DI:** Dagger Hilt (6 modules in `app/di/`; legacy `AppModule` retained only for startup settings)
- **Database:** Room v30 (29 migration steps, v1→v30, KSP)
- **File Import:** Multi-format (XLSX, CSV/TSV, JSON, HTML, TEXT, PPTX, ZIP)
- **Images:** Coil (25% RAM memory cache + disk cache), embedded XLSX images, HTTP downloads
- **Preferences:** Jetpack DataStore
- **JSON:** Moshi (with KSP) + kotlinx.serialization (import DTOs)
- **Navigation:** Compose Navigation
- **Presentations:** Apache POI 5.5.1 (XLSX + PPTX parsing)
- **Infrastructure:** Firebase (FCM, Remote Config) + WorkManager for offline-first sync
- **Localization:** English + Arabic (RTL support)
- **Knowledge Bank:** Books contain quizzes, flashcard decks, slideshow courses, note blueprints, and prompt decks
- **Min SDK:** 30 | **Target SDK:** 37 | **Compile SDK:** 37 | **JVM target:** Java 11

---

## Project Structure (Multi-Module)

```text
MKS android/
├── app/src/main/java/com/ahmedyejam/mks/
│   ├── MainActivity.kt        # Single-activity entry point (loads settings, applies density, locale, themes)
│   ├── MksApplication.kt      # @HiltAndroidApp Application class hosting legacy AppModule & Coil image loader
│   ├── di/                    # 6 Hilt modules (all @InstallIn(SingletonComponent))
│   │   ├── HiltDataModule.kt          # Infrastructure: AppModule, MksDatabase, FileManager, ExportManager,
│   │   │                              #   ImportLibraryManager, LibraryMapper, DataStoreManager, FocusManager
│   │   ├── HiltDaoModule.kt           # Core quiz DAOs: BookDao, QuizDao, QuestionDao, SessionDao,
│   │   │                              #   CategoryMetadataDao, QuestionCategoryDao
│   │   ├── HiltKnowledgeDaoModule.kt  # Knowledge bank DAOs: SlideshowCourseDao, CourseSlideDao,
│   │   │                              #   NoteBlueprintDao, NoteCollectionDao, PromptDao, PromptDeckDao,
│   │   │                              #   PromptCardDao, PromptRunDao, KnowledgeStudySessionDao, StudySessionDao
│   │   ├── HiltUtilityDaoModule.kt    # Asset/utility DAOs: AssetReferenceDao, QuestionAssetDao,
│   │   │                              #   SourceDocumentDao, GlobalSearchDao, AnnotationDao,
│   │   │                              #   WorkspaceDao, FlashcardDeckDao, FlashcardDao,
│   │   │                              #   LearningSessionDao, MistakeLogDao
│   │   ├── HiltRepositoryModule.kt    # Singleton repos not using @Inject: OllamaRepository,
│   │   │                              #   GlobalSearchRepository, ReviewRepository
│   │   └── HiltServiceModule.kt       # Preview and repair services: DeletePreviewService,
│   │                                  #   CategoryMergePreviewService, ClearMarksPreviewService,
│   │                                  #   AssetReferenceAuditService
│   └── service/              # AppFirebaseMessagingService, RemoteConfigManager, TokenSyncWorker
│
├── core/
│   ├── database/src/main/java/com/ahmedyejam/mks/data/local/
│   │   ├── MksDatabase.kt     # Room v30 database instance declaring 26 entities, 26 DAOs
│   │   ├── MksMigrations.kt   # Centralized migration registry (29 migrations, v1→v30)
│   │   ├── Converters.kt      # Room TypeConverters (JSON serialization, Lists, Enums)
│   │   ├── WorkspaceDefaults.kt
│   │   └── dao/               # 26 Room DAOs:
│   │       ├── WorkspaceDao, BookDao, QuizDao, QuestionDao, SessionDao
│   │       ├── CategoryMetadataDao, QuestionCategoryDao, QuestionAssetDao
│   │       ├── FlashcardDeckDao, FlashcardDao, LearningSessionDao
│   │       ├── SlideshowCourseDao, CourseSlideDao
│   │       ├── NoteBlueprintDao, NoteCollectionDao
│   │       ├── PromptDao, PromptDeckDao, PromptCardDao, PromptRunDao
│   │       ├── KnowledgeStudySessionDao, StudySessionDao
│   │       ├── AssetReferenceDao, SourceDocumentDao
│   │       ├── MistakeLogDao, AnnotationDao
│   │       └── GlobalSearchDao  # Full-text search via raw SQL union queries
│   │
│   ├── data/src/main/java/com/ahmedyejam/mks/
│   │   ├── di/AppModule.kt    # Trimmed legacy class: builds MksDatabase (Room.databaseBuilder +
│   │   │                          #   all migrations), constructs ReviewRepository, preview services,
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
│   │   ├── ui/MksRoutes.kt       # Navigation routes and argument keys (24 screens)
│   │   ├── util/                  # MksLogger and bounded stream utilities
│   │   └── data/
│   │       ├── local/entity/      # 26 Room Entities
│   │       ├── model/             # Pure Kotlin data models (CategoryWithMetadata, ExportResult, generation configs, etc.)
│   │       ├── search/            # GlobalSearchModels (SearchResultType enum, GlobalSearchResultRow, SearchQuery)
│   │       └── simulation/        # ChangeSimulationModels for delete/merge previews
│   │
│   ├── network/src/main/java/com/ahmedyejam/mks/
│   │   └── data/
│   │       ├── repository/OllamaRepository.kt # Ollama local LLM integration and generation configs
│   │       └── network/
│   │           ├── RemoteAssetFetcher.kt # Remote HTTPS asset network downloader
│   │           └── RemoteAssetPolicy.kt
│   │
│   └── ui/src/main/java/com/ahmedyejam/mks/ui/
│       ├── common/InvalidRouteScreen.kt # Fallback route validation screen
│       ├── components/                  # MksReusableComponents, EntityEditDialog, StudyTopAppBar,
│       │                                #   ChangePreviewDialog, CategoryComponents, CategoryEditDialog, QuestionAssetsDialog
│       ├── theme/                       # 7 App Themes (Dawn, Lavender, Forest, Midnight, etc.) and custom Design Tokens
│       └── utils/TtsManager.kt          # Text-to-Speech narration manager (used in note reading player)
│
└── feature/ui/src/main/java/com/ahmedyejam/mks/ui/
    ├── MksNavHost.kt           # NavHost graph binding all viewmodels using hiltViewModel()
    ├── navigation/MksRouteValidator.kt  # Route argument validation utilities
    ├── library/                # Main Library screens & components (LibraryScreen, LibraryViewModel, components/)
    ├── quiz/                   # Quiz system (QuizPlayerScreen, QuizViewModel, QuizQuestionsScreen,
    │                           #   QuizDetailTabsScreen, CompilerDialog, CompilerViewModel, ZoomableImageDialog)
    ├── aimcq/                  # AI MCQ generation (AiMcqGeneratorScreen, AiMcqGeneratorViewModel)
    ├── pdfextraction/          # PDF extraction (PdfExtractionScreen, PdfExtractionViewModel)
    ├── flashcard/              # Flashcard deck viewer & study player with spaced-repetition metrics
    ├── slideshow/              # Slideshow player with auto-scroll and completion tracking
    ├── booktools/              # Book knowledge dashboard (BookKnowledgeDashboardScreen, BookDashboardTabs,
    │                           #   BookToolScreens, BookToolsViewModel)
    ├── category/               # Category questions list, tag editor, asset viewer
    ├── scanner/                # Camera OCR text-recognition scanner
    ├── search/                 # Cross-entity global search screen
    ├── review/                 # Spaced repetition review queue dashboard (7 queue types)
    ├── session/                # Quiz study session manager and resume log
    ├── summary/                # Post-quiz summary with analytics
    ├── data/                   # Data tools (bulk library backup, restore, merge strategies)
    ├── importer/               # ImportViewModel for full-library bundle import orchestration
    ├── settings/               # App configuration (SettingsViewModel, ProviderConfigDialog)
    ├── trash/                  # TrashBinDialog (6 tabs: Books, Quizzes, Flashcards, Slideshows, Notes, Prompts)
    ├── workspace/              # WorkspaceManagerDialog (create, edit, switch, delete, restore workspaces)
    └── welcome/                # Onboarding welcome screen
```

---

## Architecture Fundamentals

### Data Flow

```
UI (Composable) → ViewModel (@HiltViewModel + @Inject constructor)
  → Domain Repository (@Inject constructor) → DAO (Hilt-provided) → Room Database
```

### Dependency Injection (Dagger Hilt + Legacy AppModule)

MKS uses **Dagger Hilt** for dependency injection. The application class is annotated with `@HiltAndroidApp`:

```kotlin
@HiltAndroidApp
class MksApplication : Application(), ImageLoaderFactory, Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var remoteConfigManager: RemoteConfigManager
    lateinit var appModule: AppModule // Legacy container for startup settings

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
        // Offload Firebase initialization to prevent main thread blocking
        applicationScope.launch { remoteConfigManager.fetchAndActivate() }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()
}
```

**Key insight:** ViewModels and entry points are fully managed by Hilt. They are annotated with `@HiltViewModel` and inject dependencies via constructor injection (e.g., `@Inject constructor(private val repository: MksRepository)`). In Compose screens, ViewModels are resolved using `hiltViewModel()` instead of manual factories. A legacy `AppModule` is retained to load preferences (language, theme) in `MainActivity` before the Compose context starts.

#### Hilt Modules (6, all in `app/di/`)

| Module | Provides |
|---|---|
| `HiltDataModule` | Infrastructure singletons: `AppModule`, `MksDatabase`, `FileManager`, `ExportManager`, `ImportLibraryManager`, `LibraryMapper`, `DataStoreManager`, `FocusManager` |
| `HiltDaoModule` | Core quiz DAOs (6): `BookDao`, `QuizDao`, `QuestionDao`, `SessionDao`, `CategoryMetadataDao`, `QuestionCategoryDao` |
| `HiltKnowledgeDaoModule` | Knowledge bank DAOs (10): `SlideshowCourseDao`, `CourseSlideDao`, `NoteBlueprintDao`, `NoteCollectionDao`, `PromptDao`, `PromptDeckDao`, `PromptCardDao`, `PromptRunDao`, `KnowledgeStudySessionDao`, `StudySessionDao` |
| `HiltUtilityDaoModule` | Asset/utility DAOs (10): `AssetReferenceDao`, `QuestionAssetDao`, `SourceDocumentDao`, `GlobalSearchDao`, `AnnotationDao`, `WorkspaceDao`, `FlashcardDeckDao`, `FlashcardDao`, `LearningSessionDao`, `MistakeLogDao` |
| `HiltRepositoryModule` | Non-`@Inject` repos: `OllamaRepository`, `GlobalSearchRepository`, `ReviewRepository` |
| `HiltServiceModule` | Preview & repair services: `DeletePreviewService`, `CategoryMergePreviewService`, `ClearMarksPreviewService`, `AssetReferenceAuditService` |

#### Domain Repositories (6, all `@Inject constructor` — auto-wired by Hilt)

Circular dependencies between repositories are broken via `javax.inject.Provider<T>` (not `@Lazy`).

| Repository | Responsibility | Cycle-breaking |
|---|---|---|
| `BookRepository` | Book CRUD, cover images, stats refresh, study bundles | `Provider<QuizRepository>`, `Provider<AssetRepository>` |
| `QuizRepository` | Quiz/Question CRUD, answers, scoring, category management | `Provider<BookRepository>`, `Provider<AssetRepository>` |
| `KnowledgeRepository` | Flashcards, slideshows, notes, prompts, learning sessions, derived asset sync | `Provider<QuizRepository>`, `Provider<BookRepository>`, `Provider<AssetRepository>` |
| `StudyRepository` | Quiz sessions, knowledge study sessions, mistake logs | `Provider<QuizRepository>` |
| `WorkspaceRepository` | Workspace CRUD, default workspace, soft deletes | — |
| `AssetRepository` | Asset references, question assets, source documents, annotations, image resolution | — |

Additional service/utility classes (in `core/data`): `ExportManager` (quiz/book/knowledge-bank ZIP export), `ReviewRepository` (unified review queue: 7 types — flashcard, blueprint, mistake, marked question, weak question, unfinished slide, annotation), `GlobalSearchRepository` (cross-entity full-text search via raw SQL union queries), `AiMcqRepository` (AI-powered MCQ generation orchestration), `OllamaRepository` (in `core/network` — Ollama native API).

**Legacy `AppModule`** (in `core/data/.../di/`): Trimmed to database builder (with all migrations), `ReviewRepository`, preview services, `AssetReferenceAuditService`, `DataStoreManager`, `OllamaRepository`, `FocusManager`, `applicationScope`. No longer creates repositories or seeds data. Provided to Hilt as singleton via `HiltDataModule.provideAppModule()`.

#### Hilt Injection Map (16 ViewModels — Quick Reference)

All ViewModels are annotated `@HiltViewModel` + `@Inject constructor`, resolved via `hiltViewModel()` inside `MksNavHost.kt`.

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

> **Remaining `AppModule` coupling:** `FlashcardDeckViewModel`, `SlideshowCourseViewModel`, and `SettingsScreen` composable still reference `AppModule` directly. Target for cleanup.

---

## Database & Entities

### Schema (Room v30)

The active schema is defined in `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt` and exported through Room/KSP when the Android build runs. `MksDatabase` includes `addColumnIfMissing()` / `columnExists()` helpers for safe migrations.

**26 entities across 4 tiers:**

**Workspace Tier (2):**
- `WorkspaceEntity`: multi-workspace support with defaults, soft deletes.
- `WorkspaceSettingsEntity`: workspace-specific preferences (language, theme, defaults).

**Core Quiz/Library Tier (7):**
- `BookEntity`: book/library container linked to workspace with `externalId`, title/description, field metadata, cover image, study stats, pin/system flags, and timestamps.
- `QuizEntity`: quiz inside a book with category, cover image, study stats, pin/system flags, and timestamps.
- `QuestionEntity`: question content, type (Single/Multiple Choice, Boolean), options/correct answers, explanation/hint/reference/image metadata, categories, attempts, and weight.
- `SessionEntity`: user progress, timer settings, streaks, answers, completion state.
- `CategoryMetadataEntity`: category label metadata (`emoji`, `color`, `isPinned`).
- `QuestionCategoryEntity`: normalized many-to-many question/category index.
- `QuestionAssetEntity`: generic assets (images, docs, files) linked to questions.

**Knowledge Bank Tier (10):**
- `FlashcardDeckEntity`: deck metadata, progress counters, pin/system flags, and timestamps.
- `FlashcardEntity`: front/back card content, hint/image/tags, review metrics, optional `sourceQuestionId`, and `syncConfig`.
- `LearningSessionEntity`: flashcard-deck learning session state stored as JSON.
- `SlideshowCourseEntity`: slideshow course metadata, progress, pin/system flags, derivation flags, optional source quiz, and timestamps.
- `CourseSlideEntity`: slide body, notes, image, order, completion state, optional source question, and sync config.
- `NoteBlueprintEntity`: note/blueprint body, summary, bullet points, tags, review counters, optional source question, and timestamps.
- `NoteCollectionEntity`: collection grouping of note blueprints.
- `PromptDeckEntity`: deck metadata for AI prompts, tags, and timestamps.
- `PromptCardEntity`: individual prompt within a deck, stem, variables, output type, and timestamps.
- `PromptRunEntity`: history of prompt execution with variables and output.
- `KnowledgeStudySessionEntity`: generic progress tracker for non-quiz content.
- `StudySessionEntity`: non-quiz study session progress.

**Assets & Additional Tier (4):**
- `AssetReferenceEntity`: normalized local asset ownership index.
- `SourceDocumentEntity`: source materials linked to books for reference.
- `MistakeLogEntryEntity`: tracks mistakes across quizzes with user explanations.
- `AnnotationEntity`: highlights and notes linked to different content types.

> **Note:** `SourceDocumentAssetEntity` was dropped in migration v28→v29 and is NOT a current entity. Any remaining references are historical.

### Migrations

Currently at v30 (29 migration steps total: 1→2, 2→3, ..., 29→30).
Active schema source of truth: `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`.
All migrations are centralized in `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksMigrations.kt`.

Migration regression test coverage (instrumented):
- `Migration15To16Test`: verifies the v15→v16 knowledge-bank tables/columns/indexes and guards against duplicate `slideshow_courses.isPinned` columns.
- `Migration16To17Test`: verifies the v16→v17 category/asset tables and indexes.
- `Migration22To23Test`: verifies v22→v23 changes.
- `Migration23To24Test`: verifies v23→v24 changes.
- `Migration24To25Test`: verifies v24→v25 soft-delete (`deletedAt`) additions.
- `Migration25To26Test`: verifies v25→v26 changes.
- `Migration26To27Test`: verifies addition of tags, Spaced Repetition tracking fields, note collections, and study sessions tables.
- `Migration27To28Test`: verifies creation of the `source_document_assets` table.
- `Migration28To29Test`: verifies dropping of `source_document_assets` table.
- `Migration29To30Test`: verifies addition of `resultTaxonomy` column to sessions table.

> ⚠️ **Known gaps:** Migrations v1→v15 and v17→v22 have no dedicated test coverage. A full `MigrateAll1To30Test` chain test is recommended to verify end-to-end migration integrity.

DAO test coverage:
- `AnnotationDaoTest`, `QuestionDaoTest`, `QuestionCategoryDaoTest`, `AssetReferenceDaoTest` — DAO integration tests.

Import pipeline integration tests:
- `ImportReconciliationTest`, `XlsxImportTest`, `ImportTargetQuizTest`.

Unit tests:
- `SpreadsheetHeaderMapperTest` (header detection), `ParserBugFixTest` (parser regressions), `BoundedStreamsTest` (security stream limits), `ImportValidatorTest` (pre-persist validation), `FileManagerTest` (file I/O), `SettingsSanitizerTest` (preference validation), `QuestionValidatorTest`, `SessionStateValidatorTest` (data integrity), `MksRouteBuildersTest` (navigation route builders).

Pattern for adding columns to existing tables:

```kotlin
val MIGRATION_N_M = object : Migration(N, M) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE table_name ADD COLUMN new_column TYPE DEFAULT value")
    }
}
```

Pattern for adding new tables (complex migrations):

```kotlin
database.execSQL("""
    CREATE TABLE IF NOT EXISTS new_table (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        ...
        FOREIGN KEY(parentId) REFERENCES parent_table(id) ON DELETE CASCADE
    )
""".trimIndent())
database.execSQL("CREATE INDEX IF NOT EXISTS index_new_table_parentId ON new_table(parentId)")
```

Add all migrations to `HiltDataModule.provideMksDatabase()` builder via `.addMigrations(...)`. All migrations are defined in `MksMigrations.kt`.

---

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
- `LibraryBundleDto` system (20 DTOs for complete serialization)

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

---

## Import Pipeline Architecture

### Multi-Format Orchestration (CompilerViewModel)

The import process is stateful and user-adjustable:

```
User selects file (URI)
  ↓
CompilerViewModel.onFileSelected()
  ├─ getDisplayName() from ContentResolver
  ├─ ImportFormatDetector.detectFormat() → ImportFormat enum
  └─ Route to format-specific loader
     ├─ XLSX → loadWorkbookSpreadsheet()
     ├─ CSV/TSV → loadDelimitedSpreadsheet()
     └─ JSON/HTML/TEXT → loadNonSpreadsheet()
  ↓
Auto-detect header row (score first 25 rows)
  ↓
SpreadsheetHeaderMapper.mapHeaders() → column indices by field name
  ↓
User adjusts mapping (header row, column assignments) if needed
  ↓
Parse rows: SpreadsheetQuestionParser.parseRow()
  ↓
Emit ParsedQuestion[] to UI for review
  ↓
saveParsedQuestions() → ImportLibraryManager.importParsedQuestions()
  ↓
Persisted to Room database via domain repositories
```

### UI State Management

```kotlin
data class CompilerUiState(
    val questions: List<ParsedQuestion> = emptyList(),    // Preview
    val isLoading: Boolean = false,
    val error: String? = null,
    val mode: ImportMode = ImportMode.AUTO,               // Detected format
    val detectedMode: ImportMode? = null,
    val sheetNames: List<String> = emptyList(),           // XLSX sheets
    val selectedSheet: String? = null,
    val headerRow: Int = 0,                                // User-selected
    val mapping: Map<String, Int> = emptyMap(),           // column → field
    val optionColumns: List<Int> = emptyList(),           // Option column indices
    val availableColumns: List<String> = emptyList()      // Header row for UI
)
```

**Key methods:**
- `onFileSelected(uri)` - Detect format and load
- `onSheetSelected(name)` - Choose XLSX sheet
- `updateHeaderRow(index)` - Re-detect columns
- `updateMapping(mapping, optionCols)` - Re-parse with new mapping
- `saveParsedQuestions(title, bookId)` - Persist to database

## File Format Support

### Format Detection (ImportFormatDetector)

Priority order:
1. **Extension:** `.xlsx`, `.xls`, `.csv`, `.tsv`, `.json`, `.html`, `.txt`, `.zip`
2. **MIME type:** Check ContentResolver type
3. **Stream magic bytes:**
   - `PK\x03\x04` + "xl/" or "[Content_Types].xml" → XLSX
   - `PK\x03\x04` (no xl/) → ZIP
   - `\xD0\xCF\x11\xE0` → OLE2 format (old XLS)
   - `{` or `[` at start → JSON
   - `<!doctype html>` or `<html>` → HTML
4. **Tabular heuristic:** Consistent separator count → CSV/TSV

Result: `ImportFormat` enum (XLSX, CSV_TSV, JSON, HTML, ZIP, TEXT, UNKNOWN)

### XLSX Parsing

**Special handling:**
- `prepareTempFile()` to app cache dir (deleted on ViewModel cleanup)
- `ZipFile` extraction for embedded cell images
- `WorkbookFactory.create()` from source file
- `DataFormatter + FormulaEvaluator` for cell values and formulas
- **Merged cells:** If cell empty, search merged regions and read first cell
- Sheet discovery: extract all sheet names for user to choose

**MultiSheet workflow:**
1. Load workbook → list sheets
2. User selects sheet
3. Auto-detect header row via scoring
4. User adjusts if needed
5. Parse questions row-by-row from header+1 to lastRowNum

### CSV/TSV Parsing

**Delimiter inference:**
```
Test delimiters: ',', '\t', ';' on first 10 lines
Pick delimiter with most consistent column count (average)
```

**Quote handling (RFC 4180):**
```
Track inQuotes state
At '"': if inQuotes and next='"' → escaped quote (skip both)
        else → toggle inQuotes
At delimiter: only cell boundary if !inQuotes
Trim whitespace from final cells
```

### JSON/HTML/TEXT/PPTX Parsing

- Read entire content via `ContentResolver.openInputStream()`
- Route to:
  - `JsonQuestionParser` / `JsonLibraryParser` - parse nested JSON question objects or full library bundles
  - `HtmlQuestionParser` - extract from HTML tables/divs
  - `TextQuestionParser` - line-by-line plain text → questions
  - `TextFlashcardParser` - plain text → `FlashcardEntity` (3 parse modes)
  - `TextSlideParser` - plain text → `CourseSlideEntity` (3 parse modes)
  - `TextArticleParser` - plain text → `NoteBlueprintEntity` (2 parse modes)
  - `PptxSlideParser` - PowerPoint PPTX → `CourseSlideEntity` (via Apache POI)
  - `ZipLibraryParser` - MKS ZIP bundle with manifest + assets

## Spreadsheet Parsing Deep Dive

### Header Row Detection Algorithm

Scores each of first 25 rows:
- +5 if "question" field detected (multi-language aliases)
- +4 if "answer" field detected
- +2 if "explanation" detected
- +1 if "hint" detected
- +2 per auto-detected option column
- Highest score = header row

**Aliases support English + Arabic:**
```
Question: "question", "stem", "سؤال", "نص السؤال"
Answer: "answer", "ans", "الإجابة", "الجواب"
Explanation: "explanation", "rationale", "الشرح"
Hint: "hint", "تلميح"
Reference: "reference", "مرجع"
Image: "image", "صورة"
Categories: "categories", "tags", "تصنيف"
```

### Column Mapping

`SpreadsheetHeaderMapper.mapHeaders(headerRow)` → `Map<String, Int>`:
- Normalizes header text (lowercase, trim, diacritics)
- Matches against aliases
- Returns first match per field (question takes priority)

`guessOptionColumns(headerRow, mapping)` detects remaining columns:
1. Look for "Option A", "Choice 1", "الخيار ب" patterns
2. If none, use heuristic: columns between question and answer
3. Cap at 26 columns

### Question Parsing (Per Row)

```kotlin
SpreadsheetQuestionParser.parseRow(row: List<String>, rowNumber: Int)
```

Returns `ParsedQuestion`:

1. **Stem:** Get question column, strip `=DISPIMG()` formulas
2. **Options:**
   - Read option columns
   - Detect marked cells: `*`, `✓`, `☑`, `✅`
   - Create `ParsedOption(id="opt_A"|"opt_B"|..., text, marked)`
3. **Correct answers:**
   - Parse answer column (multiple formats supported)
   - "A" or "A, C" → letter-based
   - "1", "2" → 1-based index
   - "Paris" → fuzzy text match
   - Marked cells → auto-include
   - Priority: explicit answer > marked cells
4. **Image:**
   - Check image column first
   - If empty, check question column (embedded URL/Base64)
   - If empty, check option cells
   - Fallback: row-level image (XLSX only)
   - Fallback: cell-address image from merged regions
5. **Categories:** Split by `[,،;؛/|]` → trim → distinct
6. **Type:** Multiple correct answers → MULTIPLE_CHOICE, else SINGLE_CHOICE

### Question Filtering

`shouldSkipQuestion(question)` returns true if:
- stem.isBlank() AND
- options.isEmpty() AND
- no image AND
- no explanation/hint/reference/additionalInfo/categories

## Image Management

### Image Sources (Priority Order)

1. **XLSX embedded images** (extracted via ZipFile)
   - `XlsxImageResolver.getCellImagePathMap()` → cell address → image data
   - Used by `SpreadsheetQuestionParser`
2. **Image column** (URL or Base64)
3. **Question/answer cells** (embedded URL or Base64 data URI)
4. **Option cells** (fallback if no image column)
5. **Row-level images** (XLSX only)
6. **Merged cell regions** (if original cell empty)

### FileManager I/O

All image operations centralized in `core/data/src/main/java/com/ahmedyejam/mks/data/local/FileManager.kt`:

```kotlin
// Base64 or data URI
saveBase64AsImage(base64String: String): String?
  → Base64.decode() → saveImage(bytes)

// HTTP URL
downloadAndSaveImage(url: String): String?
  → OkHttp.newCall() → file

// Streams
saveImage(inputStream: InputStream): String?
  → copy to file
saveImage(uri: Uri): String?
  → ContentResolver.openInputStream() → copy to file

// Storage
getImagesDir(): File
  → context.filesDir/images/
```

**All saved images:** `img_<UUID>.<ext>` in images directory. Never overwrite.

### Coil Caching (MksApplication)

```kotlin
override fun newImageLoader(): ImageLoader {
    return ImageLoader.Builder(this)
        .memoryCache { MemoryCache.Builder(this).maxSizePercent(0.25).build() }
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .build()
}
```

- 25% of available RAM
- Both disk + memory caching enabled
- Crossfade transitions on load

**MksRepository.insertQuestion()** auto-downloads HTTPS URLs to local paths before storing.

---

## User Interface (UI) Detailed Reference

### MKS Library Screen (Main Hub)
- **Top Bar**: Displays "MKS Library" with quick access to Contact, Settings, and a search/sort menu.
- **Categories Section**: Horizontal scroll showing question categories with "View all" access. "View all" opens a dialog with a grid of category chips and their question counts.
- **Books Section**: Toggleable between List and Grid views.
- **Book Cards**: Display book title, description, and total question count.
- **Book Context Menu**: Triggered by the "More" (three dots) icon on a book card. Offers actions: Knowledge Dashboard, Show Info & Stats (toggle), Pin to top, Edit, Export, Import, and Delete.
- **Floating Action Button (FAB)**: Hub for importing files or creating new books manually.

### Book Knowledge Dashboard
Accessed by selecting a book from the library. Uses a `HorizontalPager` with 8 scrollable tabs:
1. **Dashboard**: Summary overview with "Study Progress" card (Completion %, Due, Weak, Marked, Mistakes stats) and "Magic Actions" (dynamic chips like "Draft Note from Marked", "Generate Slides from Questions" based on available data).
2. **Prompts**: Management for AI prompt decks.
3. **Mistakes**: Log of quiz mistakes using `MistakeCard` (Toggle Fixed, Delete, Snooze).
4. **Slides**: Slideshow courses management.
5. **Quizzes**: List of quizzes specifically for this book.
6. **Cards**: Flashcard decks management.
7. **Articles & short notes**: Note blueprints management.
8. **Sources**: Reference documents (PDFs, URLs, Textbooks).

**Contextual FAB**: The FAB action changes dynamically based on the active tab (e.g., "Create Slides", "Create Prompts").

### Article & Note Player (ReviewBlueprintScreen)
Specialized immersive reader for knowledge bank articles:
- **Dual Modes**: Seamless toggle between Player (Reader) and Edit modes.
- **Immersive UI**: Hides system bars in reader mode for focused study; toggleable via screen tap.
- **Pinned Title**: Option to keep the article title visible while scrolling.
- **Autoscroll**: Hands-free reading with adjustable speed (10 px/s to 200 px/s).
- **Text-to-Speech (TTS)**: Integrated narration with adjustable Pitch and Rate (0.5x to 2.0x).
- **Integration**: Actions to convert notes to flashcards or append to question-specific notes.

### AI Prompt Deck Editor
Advanced interface for designing and testing AI prompts:
- **Variable Extraction**: Automatically detects variables in prompt templates using `{}`, `[]`, or `()` syntax.
- **Live Rendering**: Real-time preview of the prompt with variable substitution and a one-tap copy button.
- **Output Routing**: AI-generated output can be saved directly as a New Note, Article, Flashcard Deck, or Quiz.
- **Run History**: Tracks past executions of prompts within the deck.

### Question Notes & Components
Unified view for managing notes linked to specific quiz questions:
- **Component Filtering**: Toggle visibility of question elements like Options, Explanation, Hint, Reference, Images, and Categories.
- **Direct Editing**: Inline editing and saving of notes for individual questions.
- **Quiz Selection**: Filter notes by specific quizzes within the book or view all existing notes.

### Settings Screen
Organized into logical functional groups:
- **Library & Backup**: Full library export, Advanced import/export preview, Global search, and Review dashboard.
- **Appearance**: Theme selection (Dawn, etc.), Font Scale (0.8x-1.5x), and UI Density adjustment.
- **Language**: One-tap toggle between English and Arabic (RTL support).
- **Global Configuration**: Toggle welcome screen, Focus Mode (Do Not Disturb), "Skip Answered Questions", and "Double Tap to Submit".
- **AI Integrations**: Local LLM configuration (Ollama Base URL and Model Name).
- **Danger Zone**: "Clear Categories" (metadata only) and "Reset Database" (wipe & re-seed).

---

## Navigation & Routing

### Route Structure (24 routes)

```
welcome                                 # WelcomeScreen (conditional start)
library                                 # LibraryScreen (start after welcome)
├─ quiz/{quizId}?sessionId={sessionId} # QuizPlayerScreen
│  └─ summary/{sessionId}               # SummaryScreen
├─ quiz_questions/{quizId}?questionId={questionId} # QuizQuestionsScreen
├─ sessions/{quizId}                   # QuizDetailTabsScreen (session history + tabs)
├─ flashcards/{deckId}?cardId={cardId} # FlashcardDeckScreen (Knowledge Bank)
├─ book_dashboard/{bookId}             # BookKnowledgeDashboardScreen
├─ book_slideshows/{bookId}            # SlideshowCourseListScreen
├─ book_blueprints/{bookId}            # ReviewBlueprintListScreen
├─ book_notes/{bookId}                 # BookNotesScreen (Knowledge Bank)
├─ book_prompts/{bookId}               # AiPromptDeckListScreen
├─ slideshow/{courseId}?slideId={slideId} # SlideshowCourseScreen
├─ blueprint/{noteId}                  # ReviewBlueprintScreen (Knowledge Bank)
├─ prompt_deck/{promptId}?cardId=&runId=&questionId= # AiPromptDeckScreen
├─ ai_mcq_generator/{bookId}           # AiMcqGeneratorScreen (AI quiz generation)
├─ pdf_extraction/{sourceId}           # PdfExtractionScreen (PDF text extraction)
├─ global_search                       # GlobalSearchScreen
├─ review_dashboard?mistakeId={mistakeId} # ReviewDashboardScreen
├─ data_tools                          # DataToolsScreen
├─ category/{category}                 # CategoryQuestionsScreen
├─ adaptive/{type}/{id}                # AdaptiveTrainingScreen
│  (type = "BOOK"|"CATEGORY"|"QUIZ"|"ALL")
├─ scanner/{quizId}                    # ScannerScreen
└─ settings                             # SettingsScreen
```

**Dialogs (accessed from within screens):**
- `TrashBinDialog` — recovery of soft-deleted items
- `WorkspaceManagerDialog` — workspace CRUD and switching
- `CompilerDialog` — file import wizard
- `ProviderConfigDialog` — AI provider configuration
- `ZoomableImageDialog` — full-screen image viewer

**Knowledge Bank Screens:**
- Routed through `BookToolsViewModel` or dedicated ViewModels using Hilt DI
- Integrated into `MksNavHost.kt` with appropriate route parameters

**Utility Screens:**
- `global_search` — cross-entity search via `GlobalSearchViewModel`
- `review_dashboard` — unified review queue via `ReviewDashboardViewModel`
- `data_tools` — bulk import/export via `DataToolsViewModel`

### Hilt Dependency Injection in Navigation

All ViewModels are instantiated using Hilt's `hiltViewModel()` inside `MksNavHost.kt` rather than custom factories:

```kotlin
val viewModel: CompilerViewModel = hiltViewModel()
```

Route validation guards (`requirePositiveLongArg`, `requireNonBlankStringArg`) with `InvalidRouteScreen` fallback are used for argument validation.

### Data Loading in Effects

Use `LaunchedEffect` to trigger ViewModel data loads after recomposition:

```kotlin
LaunchedEffect(quizId) {
    viewModel.loadQuiz(quizId)
}
```

---

## Knowledge Bank Architecture

Books serve as knowledge containers that can hold multiple learning formats beyond quizzes: flashcard decks, slideshow courses, note blueprints, and AI prompt decks. This unified model enables cohesive learning experiences where all assets share the same book-level metadata, progress tracking, and export/import capabilities.

### Knowledge Bank Entities

Each book can own:

1. **Flashcard Decks** - Collections of front/back cards for spaced repetition
   - Repository methods: `createFlashcardDeck()`, `addFlashcards()`, `getFlashcardsByDeck()`, `updateFlashcardProgress()`
   - Optional link to QuestionEntity via `sourceQuestionId` (allows converting quiz questions to flashcards)
   - Sync metadata via `syncConfig` JSON field

2. **Slideshow Courses** - Ordered, progressive learning sequences
   - Repository methods: `createSlideshowCourse()`, `addCourseSlides()`, `updateSlideProgress()`, `getCourseWithSlides()`
   - Optional `sourceQuizId` link (courseId can derive from existing quiz)
   - Per-slide completion tracking

3. **Note Blueprints** - Compressed review documents (markdown + bullet summaries)
   - Repository methods: `createNoteBlueprint()`, `getNoteWithBullets()`, `updateReviewCount()`
   - Supports markdown body + JSON-serialized bullet points

4. **Prompt Decks** - Ordered collections of AI agent prompts/cards
   - Repository methods: `createPromptDeck()`, `addPromptCards()`, `updatePromptUsage()`
   - Configurable agent role, system context, output format
   - Per-card variables and expected outputs for structured agent interactions

### Knowledge Study Sessions

Generic progress tracking for non-quiz content. Links to specific book assets while recording:
- Time spent and completion status
- Streak counters (consecutive studies)
- Last access timestamps
- Generic state for any knowledge-bank content type

**Repository integration:** All knowledge asset edits/creation/study automatically:
- Touch parent book's `lastEditedAt` and `lastStudiedAt`
- Update asset parent's stats (slide count → studied count, progress percentage)
- Create optional KnowledgeStudySessionEntity for analytics

---

## UI Patterns & State Management

### StateFlow Usage

All ViewModels emit UI state via `StateFlow`:

```kotlin
private val _uiState = MutableStateFlow(CompilerUiState())
val uiState = _uiState.asStateFlow()

// Update: _uiState.value = _uiState.value.copy(questions = newList, isLoading = false)
```

Screens observe via `uiState.collectAsState()` in Compose.

### Unified ViewModel Pattern (BookToolsViewModel)

For Knowledge Bank features with multiple related screens, use a single ViewModel that manages different states based on content type:

```kotlin
@HiltViewModel
class BookToolsViewModel @Inject constructor(
    private val knowledgeRepository: KnowledgeRepository,
    private val assetRepository: AssetRepository
) : ViewModel() {
    private val _slideshowState = MutableStateFlow<SlideshowUiState>(SlideshowUiState())
    private val _noteState = MutableStateFlow<NoteUiState>(NoteUiState())
    private val _promptState = MutableStateFlow<PromptUiState>(PromptUiState())

    // Separate functions to load/update each asset type
    fun loadSlideshow(courseId: Long) { /*...*/ }
    fun loadNoteBlueprint(noteId: Long) { /*...*/ }
    fun loadPromptDeck(promptId: Long) { /*...*/ }
}
```

Inject into multiple composables via Hilt:

```kotlin
val viewModel: BookToolsViewModel = hiltViewModel()

// Use in multiple screens
SlideshowCourseScreen(viewModel)
ReviewBlueprintScreen(viewModel)
BookNotesScreen(viewModel)
AiPromptDeckScreen(viewModel)
```

### Coroutine Scoping

- `viewModelScope` for UI-bound coroutines
- `Dispatchers.IO` for blocking operations (file I/O, DB)
- `Dispatchers.Main` for UI updates (implicit in StateFlow)

---

## Localization & Multi-Language Support

### Arabic Support

- Resource strings: `app/src/main/res/values-ar/strings.xml`
- Header aliases in `SpreadsheetHeaderMapper`: Arabic field names recognized
- RTL layout support: Compose handles automatically

### Header Aliases (Multi-Language)

`SpreadsheetHeaderMapper.aliases` map field names to multi-language lists. Examples:

```kotlin
"question" to listOf(
    "question", "stem", "السؤال", "سؤال", "نص السؤال", ...
)
"answer" to listOf(
    "answer", "ans", "الإجابة", "الجواب", ...
)
```

Enables importing spreadsheets in any supported language.

---

## Development Workflow

### Build Commands

```bash
./gradlew assembleDebug              # Compile debug APK
./gradlew installDebug               # Install debug APK on connected device
./gradlew test                       # Run all tests
./gradlew testDebugUnitTest          # Run unit tests only
./gradlew connectedAndroidTest       # Run instrumented tests (requires emulator/device)
./gradlew :core:data:assembleDebug   # Build core:data module only (fast iteration)
./gradlew clean build                # Clean and rebuild (full build + tests)
./gradlew dependencyUpdates          # Check for dependency updates
```

### Sample Data & Testing

`MksDatabaseSeeder.seed()` (in `core/data/.../seeder/`) on first app launch:
- 1 sample Book with comprehensive content:
  - 1 sample Quiz with 13 questions (various types)
  - Sample flashcard deck with linked cards
  - Sample slideshow course with slides
  - Notes and prompt examples
- Questions include:
  - Various question types (single choice, multiple choice, boolean)
  - Text explanations
  - Remote images (Unsplash URLs)
  - Local file paths (`/storage/emulated/0/Download/test_image.jpg`)
- Knowledge Bank assets include sample metadata and progress states

**Reset for testing:** Database reset triggers re-seeding via `MksDatabaseSeeder`.

### WelcomeScreen & Onboarding

A conditional first-run experience:

```kotlin
val showWelcomeOnStartup = appModule.dataStoreManager.showWelcomeOnStartup.collectAsState(initial = true)

// In MainActivity or MksNavHost
val startDestination = if (showWelcomeOnStartup) "welcome" else "library"

NavHost(navController = navController, startDestination = startDestination) {
    composable("welcome") {
        WelcomeScreen(
            onLanguageChanged = { lang -> appModule.dataStoreManager.setLanguage(lang) },
            onComplete = { appModule.dataStoreManager.setShowWelcomeOnStartup(false) }
        )
    }
}
```

- Language selection (persisted to DataStore)
- Theme preview (Day/Night toggles)
- One-time display per app install (controlled by DataStore flag)

---

## Architecture Decisions

### Why Dagger Hilt Instead of Manual DI?

- **Standardized scope management** using Android Jetpack integration.
- **Automatic ViewModel lifecycle injection** via `@HiltViewModel` and `hiltViewModel()`.
- **Improved scalability and boilerplate reduction** across modular architecture.
- **Legacy AppModule compatibility** retained specifically for low-level application and main activity startup preferences.

### Why Multi-Stage Parsing?

1. **Format detection first** - prevents wrong parser
2. **Header mapping separate** - user adjustable
3. **Question parsing separate** - replayable if user corrects mapping
4. **Image resolution deferred** - may depend on question IDs

### Why CompilerViewModel?

- Stateful parsing with user corrections
- Live preview of imported questions
- Separate from QuizViewModel (different lifecycle)
- Reusable for multiple import scenarios

### Why FileManager Abstraction?

- Centralized I/O (caching, permissions, cleanup)
- Easier to mock/test
- Consistent image storage strategy
- Future: extensible for remote storage providers

### Why Multi-Language Header Detection?

- Arabic educational market significant
- RTL layout support already in Compose
- Bilingual content common in education
- Minor effort, high value

### Why Unified Knowledge Bank Model?

- Single source of truth for all book content types
- Unified timestamping and metadata
- Shared progress/study tracking via KnowledgeStudySessionEntity
- Cohesive export/import at book level
- Easy to extend with new asset types

---

## Infrastructure Status

✅ **Version Catalog**: `gradle/libs.versions.toml` exists and is in use.

✅ **CI Pipeline**: `.github/workflows/android-ci.yml` exists and runs lint→test→build. Consider extending with detekt + ktlint, Kover coverage thresholds (40%+ on `core/data`), and Room schema-export diff checks.

🟡 **Repository Hygiene Debt**: Python/patch scripts previously in repo root have been cleaned up. One stray Kotlin file remains (`test_zip.kts`). It should be moved to `scripts/archive/` or removed, and a `CONTRIBUTING.md` should document repo hygiene expectations for onboarding clarity.

### Build Notes

- KSP is used for Room and Moshi code generation
- Gradle modules: `:app`, `:core:model`, `:core:database`, `:core:data`, `:core:network`, `:core:ui`, `:feature:ui`
- Release R8 minification and resource shrinking enabled
- Room schema export enabled
- Apache POI 5.5.1 for XLSX + PPTX processing
- kotlinx.serialization used alongside Moshi for import DTO serialization
- `MksDatabase` includes `addColumnIfMissing()` / `columnExists()` helpers for safe migrations
- Circular dependencies between repositories resolved with `javax.inject.Provider<T>` (not `@Lazy`)

---

## Common Tasks

### Add New Knowledge Bank Asset Type

To add a new learning asset (similar to flashcards, slideshows, etc.):

1. Create entity in `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/YourAssetEntity.kt`
   - Include `bookId`, timestamps, stats fields
   - Use `@PrimaryKey(autoGenerate = true)` for id
   - For ordered content (slides, cards), include `order: Int` field

2. Create DAO in `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/YourAssetDao.kt`
   - Queries for CRUD, list by book, update progress
   - Foreign key on bookId or parent asset

3. Update `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`:
   - Add entity to `@Database(entities = [...])` annotation
   - Add abstract dao accessor: `abstract fun yourAssetDao(): YourAssetDao`
   - Create migration in `MksMigrations.kt` with `CREATE TABLE IF NOT EXISTS`

4. Add repository methods in the appropriate domain repository (e.g., `KnowledgeRepository`):
   - `createYourAsset()`, `updateYourAsset()`, `deleteYourAsset()`
   - `getYourAsset(id)`, `getYourAssetsByBook(bookId)`
   - Touch parent book's `lastEditedAt` on mutations
   - Create optional `KnowledgeStudySessionEntity` for progress

5. Provide the DAO via the appropriate Hilt module in `app/di/` (e.g., `HiltKnowledgeDaoModule`)

6. Create Screen and ViewModel in `feature/ui/src/main/java/com/ahmedyejam/mks/ui/`:
   - Use existing `BookToolsViewModel` or create dedicated `@HiltViewModel`
   - Add route to `MksNavHost.kt`
   - Implement StateFlow-based UI state

7. Update seed data in `core/data/src/main/java/com/ahmedyejam/mks/data/seeder/MksDatabaseSeeder.kt`
   - Add sample asset instances to test database

### Manage Knowledge Asset Progress

Track `KnowledgeStudySessionEntity` for any non-quiz asset:

```kotlin
// Mark as studied
val session = KnowledgeStudySessionEntity(
    bookId = bookId,
    contentId = assetId,
    type = "SLIDESHOW", // or "FLASHCARD", "PROMPT", etc.
    progress = 0.5f,
    isCompleted = false,
    streakCount = 1,
    lastAccessedAt = System.currentTimeMillis()
)
repository.insertKnowledgeStudySession(session)

// Query progress
val sessions = repository.getStudySessionsByBook(bookId)
```

### Add New Import Format

1. Create parser in `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/YourFormatParser.kt`
   - Implement `parse(content: String): List<ParsedQuestion>`
2. Add `ImportFormat.YOUR_FORMAT` enum variant
3. Update `ImportFormatDetector.detectByExtension()` / `detectByMimeType()`
4. Add route in `CompilerViewModel.loadNonSpreadsheet()` when block
5. Add UI affordance in LibraryScreen

### Add New Database Column

1. Create migration in `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksMigrations.kt`:
   ```kotlin
   val MIGRATION_N_(N+1) = object : Migration(N, N+1) {
       override fun migrate(database: SupportSQLiteDatabase) {
           database.execSQL("ALTER TABLE table_name ADD COLUMN new_column TYPE DEFAULT value")
       }
   }
   ```
2. Add to `HiltDataModule.provideMksDatabase()` builder via `.addMigrations(...)`
3. Update entity class in `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/` with new field

### Add New Screen

1. Create `feature/ui/src/main/java/com/ahmedyejam/mks/ui/{feature}/{Feature}Screen.kt` (Composable)
2. Create `feature/ui/src/main/java/com/ahmedyejam/mks/ui/{feature}/{Feature}ViewModel.kt` with `@HiltViewModel`
3. Add route to `MksNavHost.kt` using `hiltViewModel()`
4. Add navigation calls from existing screens

### Customize Header Detection

Edit `SpreadsheetHeaderMapper.aliases` to add field names:

```kotlin
"custom_field" to listOf("custom", "my_field", "حقلي")
```

Update `scoreHeaderRow()` scoring if needed (e.g., +5 for critical fields).

### Core Architectural Patterns to Respect

- **Room Migrations:** Never drop tables or destructively alter schemas. Always write explicit migrations. When adding a column, increment `MKS_DATABASE_VERSION` in `MksDatabase.kt` and add a `Migration(N, M)` block in `MksMigrations.kt`, then register it in the database builder's `addMigrations(...)` chain.
- **Navigation:** Routing is handled centrally in `MksNavHost.kt`. Route constants are in `core/model/src/main/java/com/ahmedyejam/mks/ui/MksRoutes.kt`. All route arguments must be extracted from `NavBackStackEntry`.
- **Knowledge Bank Hierarchy:** `BookEntity` is the root container. When creating new learning formats, they must link to a `BookEntity` parent.

---

## Additional Resources

The following complementary documentation exists in `Important docs/`:

| File | Purpose |
|---|---|
| `Important docs/APP_ARCHITECTURE_MAP.md` | High-level system architecture with component diagrams |
| `Important docs/USER_JOURNEY_MAP_claudeopus.md` | Detailed screen-by-screen UI interaction map (comprehensive) |
| `Important docs/user_Jour_Geminipro.md` | Condensed user journey and UI map |
| `Important docs/DATABASE_INSPECTION.md` | Database inspection notes |
| `Important docs/IMPORT_INPUT_PATHS.md` | Import input path documentation |
| `Important docs/full_inspection_multi_phases_map.md` | Master architecture & codebase inspection protocol (multi-phase) |
| `Important docs/mks_enhancement_plan.md` | Planned enhancement roadmap |
| `Important docs/GEMINI.md` | Gemini AI tool (MCP) integration guidance |

For most tasks, read the relevant sections in this AGENTS.md. For deep architectural questions, consult the additional markdown files in `Important docs/`.

---

## AI Agent Tools (MCP)

To enhance Gemini's capabilities with external tools (GitHub, Figma, custom servers), follow the
instructions in [GEMINI.md](file:///Users/ahmedejam/Projects/MKS%20android/Important%20docs/GEMINI.md).
