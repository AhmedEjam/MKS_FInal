# MKS Android App Architecture Map

> Finalization status correction (2026-05-25): active source is Room v17, not v15. Active entities now include flashcards, slideshow courses/slides, note blueprints, prompts, generic knowledge-study sessions, normalized question categories, and asset references. Keep `app/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt` as the schema source of truth.

## **🏗️ Application Architecture Overview**

```
MKS (Mobile Knowledge System)
├── Framework: Jetpack Compose + Material3
├── Architecture: MVVM with Manual DI
├── Database: Room v17 (16 migration steps)
├── Language: Kotlin
├── Target SDK: 35
└── Min SDK: 30
```

## **📱 App Entry Point & Initialization**

```
MainActivity
├── Extends: AppCompatActivity
├── Theme: Dynamic (Dawn/Forest/Midnight/Lavender)
├── Language: Dynamic (English/Arabic RTL)
├── Navigation: Single-activity with NavHostController
└── Dependencies: AppModule (manual DI container)

MksApplication
├── Implements: ImageLoaderFactory (Coil)
├── Memory Cache: 25% of available RAM
├── Disk Cache: Enabled
└── Crossfade: Enabled for smooth transitions
```

## **🗂️ Dependency Injection Container (AppModule)**

```
AppModule (Manual DI - 838 lines)
├── database: MksDatabase (Room v17)
├── fileManager: FileManager (I/O operations)
├── repository: MksRepository (data access)
├── exportManager: ExportManager (ZIP export)
├── importManager: ImportLibraryManager (multi-format import)
├── dataStoreManager: DataStoreManager (preferences)
├── focusManager: FocusManager (adaptive training)
└── applicationScope: CoroutineScope(Dispatchers.Default)
```

## **🗃️ Data Layer Architecture**

### **Database Schema (Room v17)**
```
MksDatabase
├── Entities:
│   ├── BookEntity (id, externalId, title, description, fields, coverImage, stats)
│   ├── QuizEntity (id, externalId, bookId, title, description, category, coverImage, stats)
│   ├── QuestionEntity (id, externalId, quizId, text, type, options, correctAnswers, metadata)
│   ├── SessionEntity (id, quizId, answers: Map<Long, List<String>>, isCompleted, timestamps)
│   └── CategoryMetadataEntity (name, emoji, color, pinned)
├── DAOs: BookDao, QuizDao, QuestionDao, SessionDao, CategoryMetadataDao
├── Converters: Type converters for complex types
└── Migrations: 16 incremental migrations (1→17)
```

### **Repository Layer**
```
MksRepository (single source of truth - 480 lines)
├── Data Operations: CRUD for all entities
├── Business Logic: Quiz scoring, statistics, adaptive training
├── Image Management: Download/save images, path resolution
├── Export/Import: JSON serialization, library export
└── Search: Full-text search across books/quizzes/questions

ImportLibraryManager (521 lines)
├── Format Detection: XLSX, JSON, ZIP, CSV/TSV, HTML, TEXT
├── Multi-format Parsing: Spreadsheet, JSON, HTML, Text parsers
├── Validation: ImportValidator for data integrity
├── Normalization: BundleNormalizer for data consistency
└── Atomic Transactions: Database.withTransaction for consistency
```

## **🎯 Navigation Structure (10 Routes)**

```
Navigation Graph (MksNavHost.kt - 328 lines)
├── welcome (conditional startup)
│   └── WelcomeScreen (language/theme selection)
├── library (main hub - default destination)
│   ├── LibraryScreen (books/quizzes grid + search)
│   ├── CompilerViewModel (spreadsheet import)
│   └── ImportViewModel (file import)
├── quiz_questions/{quizId}
│   └── QuizQuestionsScreen (question browser)
├── settings
│   └── SettingsScreen (app preferences)
├── category/{category}
│   └── CategoryQuestionsScreen (category questions)
├── quiz/{quizId}?sessionId={sessionId}
│   └── QuizPlayerScreen (main quiz interface)
├── sessions/{quizId}
│   └── SessionManagementScreen (session history)
├── scanner/{quizId}
│   └── ScannerScreen (QR/barcode scanner)
├── adaptive/{type}/{id} (type: BOOK|CATEGORY|QUIZ|QUESTION)
│   └── QuizPlayerScreen (adaptive training mode)
└── summary/{sessionId}
    └── SummaryScreen (post-quiz analytics)
```

## **🖥️ UI Layer Architecture**

### **Screen Components (by Feature)**

```
ui/
├── library/ (Library management - 868 lines)
│   ├── LibraryScreen.kt (main screen)
│   ├── LibraryViewModel.kt (447 lines)
│   └── LibraryComponents.kt (reusable components)
├── quiz/ (Quiz player - 1212 lines)
│   ├── QuizPlayerScreen.kt (main quiz interface)
│   ├── QuizViewModel.kt (1259 lines - largest file)
│   ├── QuizQuestionsScreen.kt (question browser)
│   ├── QuizQuestionsViewModel.kt
│   └── CompilerViewModel.kt (import orchestrator)
├── category/
│   ├── CategoryQuestionsScreen.kt
│   └── CategoryQuestionsViewModel.kt
├── session/
│   ├── SessionManagementScreen.kt
│   └── SessionViewModel.kt
├── summary/
│   ├── SummaryScreen.kt (501 lines)
│   └── SummaryViewModel.kt
├── scanner/
│   ├── ScannerScreen.kt
│   └── ScannerViewModel.kt
├── settings/
│   └── SettingsScreen.kt (484 lines)
├── welcome/
│   └── WelcomeScreen.kt
├── import/
│   └── ImportViewModel.kt
└── theme/
    ├── Color.kt (theme colors)
    ├── Type.kt (typography)
    └── Theme.kt (Material3 theme setup)
```

### **Reusable Component Library (LibraryComponents.kt - 1131 lines)**
```
LibraryComponents
├── LibraryBanner (animated carousel)
├── CategoryPreviewCard (category display)
├── BookItem/QuizItem (grid/list variants)
├── BookOptionsSheet/QuizOptionsSheet (modal sheets)
├── QuestionCountPill, FieldChip, etc. (utility components)
└── Search, Filter, Sort components
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
├── 4 Dynamic Themes: Dawn, Forest, Midnight, Lavender
├── Material3 Color Schemes (light/dark variants)
├── Custom Typography (scalable fonts)
├── Dynamic Font Scale (accessibility)
├── UI Density Control
└── RTL Support (Arabic localization)
```

## **📁 File Organization Map**

```
/app/src/main/java/com/ahmedyejam/mks/
├── MainActivity.kt (73 lines) - App entry point
├── MksApplication.kt (35 lines) - App initialization + Coil setup
├── di/AppModule.kt (838 lines) - Manual DI container
├── data/
│   ├── local/
│   │   ├── MksDatabase.kt - Room database setup
│   │   ├── dao/ - Data access objects
│   │   ├── entity/ - Room entities
│   │   ├── converters/ - Type converters
│   │   └── FileManager.kt - I/O operations
│   ├── repository/
│   │   ├── MksRepository.kt (480 lines) - Main data access
│   │   └── ExportManager.kt - Library export
│   ├── import/ - Multi-format import pipeline
│   │   ├── repository/ImportLibraryManager.kt (521 lines)
│   │   ├── detector/ - Format detection
│   │   ├── parser/ - Content parsers
│   │   ├── mapping/ - Data mapping
│   │   ├── normalization/ - Data normalization
│   │   └── xlsx/ - Excel processing
│   ├── preferences/DataStoreManager.kt - User preferences
│   ├── focus/FocusManager.kt - Adaptive training logic
│   └── model/ - Domain models
├── ui/
│   ├── MksNavHost.kt (328 lines) - Navigation graph
│   ├── library/ - Library management screens
│   ├── quiz/ - Quiz player screens
│   ├── category/ - Category screens
│   ├── session/ - Session management
│   ├── summary/ - Results screens
│   ├── scanner/ - QR scanner
│   ├── settings/ - Settings screen
│   ├── welcome/ - Welcome screen
│   ├── import/ - Import UI
│   └── theme/ - Theming system
└── res/ - Android resources
```

## **⚡ Performance Characteristics**

```
Performance Profile
├── Memory: 25% RAM for image cache
├── Database: Room with 13 migrations
├── UI: Jetpack Compose with lazy loading
├── Images: Coil with disk + memory cache
├── Lists: LazyVerticalGrid with stable keys
└── Animations: Crossfade enabled
```

## **🔧 Key Technical Decisions**

- **Manual DI**: Explicit dependencies (no Hilt/Dagger)
- **Single Activity**: Compose navigation (no fragments)
- **StateFlow**: Reactive UI state management
- **Room**: Local persistence with migrations
- **Coil**: Image loading and caching
- **Material3**: Modern design system
- **Multi-format Import**: XLSX, JSON, CSV, HTML, TEXT, ZIP
- **Adaptive Training**: FocusManager for spaced repetition
- **Bilingual Support**: English + Arabic (RTL)

This architecture provides a comprehensive, scalable foundation for a quiz application with advanced features like multi-format import, adaptive training, and rich analytics while maintaining clean separation of concerns and modern Android development practices.
