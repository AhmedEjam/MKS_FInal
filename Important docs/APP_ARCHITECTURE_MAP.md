# MKS Android App Architecture Map

> Last updated: 2026-07-10. Active source is Room v30, 29 migration steps (1→30). Schema source of truth: `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`.

## **🏗️ Application Architecture Overview**

```
MKS (Mobile Knowledge System)
├── Framework: Jetpack Compose + Material3
├── Architecture: MVVM with Dagger Hilt DI (modular multi-module)
├── Database: Room v30 (29 migration steps)
├── Language: Kotlin
├── Target SDK: 35
└── Min SDK: 30
```

## **📱 App Entry Point & Initialization**

```
MainActivity (99 lines)
├── Extends: AppCompatActivity
├── Theme: Dynamic (Dawn/Forest/Midnight/Lavender/Plain Light/Plain Dark/System)
├── Language: Dynamic (English/Arabic RTL)
├── Navigation: Single-activity with NavHostController
└── Dependencies: Dagger Hilt (DI container)

MksApplication (34 lines)
├── Implements: ImageLoaderFactory (Coil)
├── Memory Cache: 25% of available RAM
├── Disk Cache: Enabled
└── Crossfade: Enabled for smooth transitions
```

## **🗂️ Dependency Injection Container (Dagger Hilt)**

```
Hilt DI Modules (app/src/main/java/.../di/)
├── HiltDataModule        → MksDatabase, DataStoreManager, FileManager, ApplicationScope
├── HiltDaoModule         → Core DAOs (BookDao, QuizDao, QuestionDao, SessionDao, etc.)
├── HiltKnowledgeDaoModule → Knowledge-bank DAOs (FlashcardDao, NoteBlueprintDao, etc.)
├── HiltUtilityDaoModule  → Utility DAOs (GlobalSearchDao, MistakeLogDao, AnnotationDao, etc.)
├── HiltRepositoryModule  → Split repositories (BookRepository, QuizRepository, etc.)
└── HiltServiceModule     → Services (ExportManager, ImportLibraryManager, ReviewRepository, etc.)
```

## **🗃️ Data Layer Architecture**

### **Database Schema (Room v30 — 26 Entities, 26 DAOs)**
```
MksDatabase
├── Workspace Entities:
│   ├── WorkspaceEntity (multi-workspace support, soft deletes)
│   └── WorkspaceSettingsEntity (language, theme, defaults)
├── Core Quiz/Library Entities:
│   ├── BookEntity (id, externalId, title, description, fields, coverImage, stats)
│   ├── QuizEntity (id, externalId, bookId, title, description, category, coverImage, stats)
│   ├── QuestionEntity (id, externalId, quizId, text, type, options, correctAnswers, metadata)
│   ├── SessionEntity (id, quizId, answers, isCompleted, timestamps, timer, streak)
│   ├── CategoryMetadataEntity (name, emoji, color, isPinned)
│   ├── QuestionCategoryEntity (normalized many-to-many question/category index)
│   └── QuestionAssetEntity (generic assets linked to questions)
├── Knowledge Bank Entities:
│   ├── FlashcardDeckEntity (deck metadata, progress, pin/system flags)
│   ├── FlashcardEntity (front/back, hint/tags, review metrics, sourceQuestionId)
│   ├── LearningSessionEntity (flashcard-deck learning session state as JSON)
│   ├── SlideshowCourseEntity (course metadata, progress, derivation flags)
│   ├── CourseSlideEntity (slide body, notes, image, order, completion)
│   ├── NoteBlueprintEntity (note body, summary, bullet points, tags, review counters)
│   ├── NoteCollectionEntity (collection grouping of note blueprints)
│   ├── PromptEntity (legacy prompt)
│   ├── PromptDeckEntity (deck metadata for AI prompts)
│   ├── PromptCardEntity (individual prompt card, stem, variables, output type)
│   ├── PromptRunEntity (execution history with variables and output)
│   ├── KnowledgeStudySessionEntity (generic progress tracker for non-quiz content)
│   └── StudySessionEntity (non-quiz study session progress)
├── Assets & Reference Entities:
│   ├── AssetReferenceEntity (normalized local asset ownership index)
│   └── SourceDocumentEntity (source materials linked to books)
├── Additional Study Entities:
│   ├── MistakeLogEntryEntity (tracks mistakes with user explanations)
│   └── AnnotationEntity (highlights and notes on different content types)
├── DAOs (26): BookDao, QuizDao, QuestionDao, SessionDao, CategoryMetadataDao,
│         WorkspaceDao, FlashcardDeckDao, FlashcardDao, LearningSessionDao,
│         SlideshowCourseDao, CourseSlideDao, NoteBlueprintDao, NoteCollectionDao,
│         PromptDao, PromptDeckDao, PromptCardDao, PromptRunDao,
│         KnowledgeStudySessionDao, StudySessionDao, QuestionCategoryDao,
│         AssetReferenceDao, QuestionAssetDao, SourceDocumentDao, MistakeLogDao,
│         GlobalSearchDao, AnnotationDao
├── Converters: Type converters for complex types
└── Migrations: 29 incremental migrations (1→30)
```

### **Repository Layer**
```
Split Repositories (core/data/src/main/java/.../repository/)
├── BookRepository         → Book CRUD, stats, cover images
├── QuizRepository         → Quiz CRUD, question management, scoring
├── KnowledgeRepository    → Flashcards, slideshows, blueprints, prompts
├── StudyRepository        → Sessions, adaptive training, study progress
├── AssetRepository        → Question assets, source documents, annotations
├── WorkspaceRepository    → Workspace management, settings
├── OllamaRepository       → Local LLM integration (Ollama API)
└── ExportManager          → Library export logic

ImportLibraryManager (934 lines)
├── Format Detection: XLSX, JSON, ZIP, CSV/TSV, HTML, TEXT
├── Multi-format Parsing: Spreadsheet, JSON, HTML, Text parsers
├── Validation: ImportValidator for data integrity
├── Normalization: BundleNormalizer for data consistency
└── Atomic Transactions: Database.withTransaction for consistency

GlobalSearchRepository (cross-entity full-text search)
ReviewRepository (unified review queue for flashcards, blueprints, mistakes)
MksFullImportExportService (bulk data import/export with preview)
```

## **🎯 Navigation Structure (24 Routes)**

```
Navigation Graph (MksNavHost.kt - 708 lines)
├── welcome (conditional startup)
│   └── WelcomeScreen (language/theme selection)
├── library (main hub - default destination)
│   ├── LibraryScreen (books/quizzes grid + search)
│   ├── CompilerViewModel (spreadsheet import)
│   └── ImportViewModel (file import)
├── quiz_questions/{quizId}?questionId={questionId}
│   └── QuizQuestionsScreen (question browser)
├── flashcards/{deckId}?cardId={cardId}
│   └── FlashcardDeckScreen (list & study modes)
├── book_dashboard/{bookId}
│   └── BookKnowledgeDashboardScreen (book overview)
├── book_slideshows/{bookId}
│   └── SlideshowCourseListScreen (course list)
├── book_blueprints/{bookId}
│   └── ReviewBlueprintListScreen (blueprint list)
├── book_sources/{bookId}?sourceId={sourceId}
│   └── SourceDocumentListScreen (source docs)
├── book_prompts/{bookId}
│   └── AiPromptDeckListScreen (prompt deck list)
├── book_notes/{bookId}
│   └── BookNotesScreen (all question notes)
├── slideshow/{courseId}?slideId={slideId}
│   └── SlideshowCourseScreen (individual course)
├── blueprint/{noteId}
│   └── ReviewBlueprintScreen (individual blueprint)
├── prompt_deck/{promptId}?cardId={cardId}&runId={runId}
│   └── AiPromptDeckScreen (individual prompt deck)
├── global_search
│   └── GlobalSearchScreen (cross-entity search)
├── review_dashboard?mistakeId={mistakeId}
│   └── ReviewDashboardScreen (unified review queue)
├── data_tools
│   └── DataToolsScreen (bulk import/export)
├── settings
│   └── SettingsScreen (app preferences)
├── category/{category}
│   └── CategoryQuestionsScreen (category questions)
├── quiz/{quizId}?sessionId={sessionId}
│   └── QuizPlayerScreen (main quiz interface)
├── sessions/{quizId}
│   └── SessionManagementScreen (session history)
├── scanner/{quizId}
│   └── ScannerScreen (camera OCR scanner)
├── adaptive/{type}/{id} (type: BOOK|CATEGORY|QUIZ|ALL)
│   └── QuizPlayerScreen (adaptive training mode)
├── summary/{sessionId}
│   └── SummaryScreen (post-quiz analytics)
├── ai_mcq_generator/{bookId}
│   └── AiMcqGeneratorScreen (AI-powered MCQ generation from source docs)
└── pdf_extraction/{sourceId}
    └── PdfExtractionScreen (PDF text extraction from source documents)
```

## **🖥️ UI Layer Architecture**

### **Screen Components (by Feature)**

```
ui/
├── MksNavHost.kt (708 lines) - Navigation graph
├── MksRoutes.kt - Route constants & builders
├── library/ (Library management)
│   ├── LibraryScreen.kt (756 lines)
│   ├── LibraryViewModel.kt (641 lines)
│   └── components/
│       ├── LibraryComponents.kt (1128 lines)
│       ├── LibraryContentGrid.kt
│       ├── LibraryTopBar.kt
│       ├── LibraryFabMenu.kt
│       ├── LibraryDialogs.kt
│       └── SortDialog.kt
├── quiz/ (Quiz player)
│   ├── QuizPlayerScreen.kt (1336 lines)
│   ├── QuizViewModel.kt (1396 lines - largest ViewModel)
│   ├── QuizQuestionsScreen.kt (question browser)
│   ├── QuizQuestionsViewModel.kt
│   └── CompilerViewModel.kt (import orchestrator)
├── flashcard/ (Flashcard deck)
│   └── FlashcardDeckScreen.kt (list & study modes)
├── slideshow/ (Slideshow courses)
│   ├── SlideshowCourseScreen.kt
│   └── SlideshowCourseViewModel.kt
├── booktools/ (Knowledge bank tools)
│   ├── BookToolsViewModel.kt
│   ├── BookKnowledgeDashboardScreen.kt
│   ├── AiPromptDeckScreen.kt
│   ├── AiPromptDeckListScreen.kt
│   ├── BookNotesScreen.kt
│   ├── ReviewBlueprintScreen.kt
│   ├── ReviewBlueprintListScreen.kt
│   ├── SlideshowCourseListScreen.kt
│   └── SourceDocumentListScreen.kt
├── category/
│   ├── CategoryQuestionsScreen.kt
│   └── CategoryQuestionsViewModel.kt
├── session/
│   ├── SessionManagementScreen.kt
│   └── SessionViewModel.kt
├── summary/
│   ├── SummaryScreen.kt (500 lines)
│   └── SummaryViewModel.kt
├── scanner/
│   ├── ScannerScreen.kt
│   └── ScannerViewModel.kt
├── settings/
│   └── SettingsScreen.kt (525 lines)
├── welcome/
│   └── WelcomeScreen.kt
├── search/
│   ├── GlobalSearchScreen.kt
│   └── GlobalSearchViewModel.kt
├── review/
│   ├── ReviewDashboardScreen.kt
│   └── ReviewDashboardViewModel.kt
├── data/
│   ├── DataToolsScreen.kt
│   └── DataToolsViewModel.kt
├── import/
│   └── ImportViewModel.kt
├── common/
│   └── InvalidRouteScreen.kt (route error fallback)
├── components/ (shared UI components)
├── navigation/
│   ├── MksRouteBuilders.kt
│   └── requireNonBlankStringArg / requirePositiveLongArg helpers
└── theme/
    ├── Color.kt (theme colors)
    ├── Type.kt (typography)
    └── Theme.kt (Material3 theme setup + design tokens)
```

## **📊 Data Flow Architecture**

```
User Interaction
    ↓
UI Layer (Composable Screens)
    ↓ (StateFlow.collectAsState())
ViewModel Layer (StateFlow emission)
    ↓ (suspend functions)
Repository Layer (business logic)
    ↓ (DAO operations)
Database Layer (Room entities)
    ↙        ↘
Read Operations  Write Operations
    ↗        ↙
UI Updates (recomposition)
```

## **🔄 Import Pipeline Architecture**

```
File Selection (URI)
    ↓
Format Detection (ImportFormatDetector)
    ├─ XLSX → XlsxLibraryCompiler
    ├─ JSON → JsonLibraryParser
    ├─ ZIP → ZipLibraryParser + JsonLibraryParser
    ├─ CSV/TSV/HTML/TEXT → Format-specific parsers
    ↓
Validation (ImportValidator)
    ↓
Normalization (BundleNormalizer)
    ↓
Mapping (LibraryMapper) → Entity conversion
    ↓
Atomic Transaction (database.withTransaction)
    ↓
Result: ImportResult (success/errors/warnings/counts)
```

## **🎨 Theming & Design System**

```
Theme System
├── 7 Themes: Dawn, Forest, Midnight, Lavender, Plain Light, Plain Dark, System
├── Material3 Color Schemes (light/dark variants)
├── Custom Typography (scalable fonts)
├── Dynamic Font Scale (accessibility, 0.5×–2.0×)
├── UI Density Control (0.5×–1.5×)
├── Design Tokens (LocalMksDesignTokens)
└── RTL Support (Arabic localization)
```

## **📁 File Organization Map**

```
MKS Project (Multi-module)
├── app/src/main/java/com/ahmedyejam/mks/
│   ├── MainActivity.kt              - App entry point
│   ├── MksApplication.kt            - App init + Coil setup (@HiltAndroidApp)
│   └── di/                          - Dagger Hilt DI modules
│       ├── HiltDataModule.kt        - Database, DataStore, FileManager
│       ├── HiltDaoModule.kt         - Core DAOs
│       ├── HiltKnowledgeDaoModule.kt - Knowledge-bank DAOs
│       ├── HiltUtilityDaoModule.kt  - Utility DAOs
│       ├── HiltRepositoryModule.kt  - Repositories
│       └── HiltServiceModule.kt     - Services
├── core/
│   ├── database/src/main/java/.../data/local/
│   │   ├── MksDatabase.kt           - Room database setup (v30)
│   │   ├── MksMigrations.kt         - 29 migration steps
│   │   ├── Converters.kt            - Type converters
│   │   └── dao/ (26 DAO interfaces)
│   ├── model/src/main/java/.../
│   │   ├── data/local/entity/ (26 entity classes)
│   │   ├── data/model/              - Domain models (OllamaModels, etc.)
│   │   ├── ui/MksRoutes.kt          - Route constants & builders
│   │   └── util/                    - Utilities (BoundedStreams, MksLogger)
│   ├── data/src/main/java/.../data/
│   │   ├── repository/              - Split repositories
│   │   │   ├── BookRepository.kt
│   │   │   ├── QuizRepository.kt
│   │   │   ├── KnowledgeRepository.kt
│   │   │   ├── StudyRepository.kt
│   │   │   ├── AssetRepository.kt
│   │   │   ├── WorkspaceRepository.kt
│   │   │   └── ExportManager.kt
│   │   ├── importer/                - Multi-format import pipeline
│   │   ├── exchange/                - Bundle exchange format
│   │   ├── exportfull/              - Full library export
│   │   ├── focus/FocusManager.kt    - Adaptive training logic
│   │   ├── preferences/             - DataStoreManager
│   │   ├── preview/                 - Delete/merge preview services
│   │   ├── repair/                  - Asset reference audit
│   │   ├── review/                  - ReviewRepository
│   │   ├── search/                  - GlobalSearchRepository
│   │   ├── seeder/MksDatabaseSeeder.kt - DB seed data
│   │   └── validation/              - Data validation
│   ├── network/src/main/java/.../
│   │   ├── data/network/            - RemoteAssetFetcher, RemoteAssetPolicy
│   │   └── data/repository/OllamaRepository.kt - Local LLM integration
│   └── ui/src/main/java/.../ui/
│       ├── components/              - Shared UI components
│       ├── common/InvalidRouteScreen.kt
│       ├── theme/                   - Color, Type, Theme, MksDesignTokens
│       └── utils/TtsManager.kt      - Text-to-Speech integration
└── feature/
    └── ui/src/main/java/.../ui/
        ├── MksNavHost.kt            - Navigation graph (24 routes)
        ├── library/                 - Library management screens
        ├── quiz/                    - Quiz player screens
        ├── flashcard/               - Flashcard deck screen
        ├── slideshow/               - Slideshow course screens
        ├── booktools/               - Knowledge bank tool screens
        ├── category/                - Category screens
        ├── session/                 - Session management
        ├── summary/                 - Results screens
        ├── scanner/                 - Camera OCR scanner
        ├── search/                  - Global search
        ├── review/                  - Review dashboard
        ├── data/                    - Data tools (import/export)
        ├── settings/                - Settings screen
        ├── welcome/                 - Welcome screen
        ├── importer/                - Import UI
        ├── trash/                   - Trash bin dialog
        ├── workspace/               - Workspace manager dialog
        └── navigation/              - Route builders & validators
```

## **⚡ Performance Characteristics**

```
Performance Profile
├── Memory: 25% RAM for image cache
├── Database: Room v30 with 29 incremental migrations
├── UI: Jetpack Compose with lazy loading
├── Images: Coil with disk + memory cache
├── Lists: LazyVerticalGrid with stable keys
├── Animations: Crossfade + slide transitions (disable on Plain themes)
└── Route Validation: requirePositiveLongArg / requireNonBlankStringArg guards
```

## **🔧 Key Technical Decisions**

- **Dagger Hilt DI**: Standardized scope management with `@HiltViewModel` and `hiltViewModel()`
- **Multi-module Architecture**: `app/`, `core/` (database, model, data, network, ui), `feature/` (ui)
- **Single Activity**: Compose navigation (no fragments)
- **StateFlow**: Reactive UI state management
- **Room v30**: Local persistence with 29 incremental migrations
- **Coil**: Image loading and caching (25% RAM)
- **Material3**: Modern design system with 7 themes
- **Multi-format Import**: XLSX, JSON, CSV, HTML, TEXT, ZIP with validation & security
- **Adaptive Training**: FocusManager for spaced repetition
- **Bilingual Support**: English + Arabic (RTL)
- **Knowledge Bank**: Books contain quizzes, flashcard decks, slideshow courses, note blueprints, prompt decks
- **Global Search**: Cross-entity search via GlobalSearchDao
- **Review Dashboard**: Unified review queue for flashcards, blueprints, and mistake logs
- **Data Tools**: Full library import/export with preview
- **Route Guards**: Invalid route fallback screen with argument validation
- **Local LLM**: Ollama integration for on-device AI inference
- **TTS**: Text-to-Speech for article/note narration

This architecture provides a comprehensive, scalable foundation for a quiz and knowledge-bank application with advanced features like multi-format import, adaptive training, rich analytics, and integrated learning tools while maintaining clean separation of concerns and modern Android development practices.
