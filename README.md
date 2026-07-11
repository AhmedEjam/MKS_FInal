# 📚 MKS - My Knowledge Space

**MKS** is an advanced Android quiz and knowledge-bank application that imports educational content from multiple file formats (XLSX, CSV, JSON, HTML, etc.) and presents interactive learning experiences through quizzes, flashcards, slideshows, notes, and AI prompt decks.

**Repository**: `AhmedEjam/MKS_FInal`  
**Language**: Kotlin 99.5%  
**Framework**: Jetpack Compose + Material3  
**Database**: Room v30 (29 migration steps)

---

## 🎯 Quick Navigation for AI Agents

This README provides a comprehensive index to help AI agents (Gemini Pro, Claude, etc.) navigate and understand the entire codebase.

### **Core Project Documentation**

| File | Purpose | Best For |
|------|---------|----------|
| **[AGENTS.md](AGENTS.md)** | Detailed project guidance (1403 lines) | Architecture, patterns, common tasks |
| **[APP_ARCHITECTURE_MAP.md](APP_ARCHITECTURE_MAP.md)** | High-level system architecture | System overview, component diagrams |
| **[USER_JOURNEY_MAP_claudeopus.md](USER_JOURNEY_MAP_claudeopus.md)** | Screen-by-screen UI interaction map | UI flows, user interactions |
| **[user_Jour_Geminipro.md](user_Jour_Geminipro.md)** | Condensed user journey | Quick UI reference |

---

## 🏗️ Architecture Overview

### **Technology Stack**
```
✅ Language:           Kotlin
✅ UI Framework:       Jetpack Compose (Material3)
✅ Dependency Inject:  Dagger Hilt (6 modules in app/di/)
✅ Database:           Room v30 (29 migrations)
✅ Image Loading:      Coil (25% RAM cache)
✅ Preferences:        DataStore
✅ Infrastructure:     Firebase (FCM, Remote Config) + WorkManager
✅ Localization:       English + Arabic (RTL)
✅ Min SDK:            30 | Target SDK: 37
```

### **Dependency Injection Container**

ViewModels and dependencies are managed and injected via **Dagger Hilt** (6 modules in `app/di/`). The legacy `AppModule` is retained only for early startup settings (language/theme) in MainActivity.

```kotlin
// ViewModels obtain dependencies via constructor injection:
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val quizRepository: QuizRepository,
    // ...
) : ViewModel()
```

---

## 📁 Project Structure & File Map

### **Source Tree** (Multi-Module Architecture)

```
MKS android/
├── app/                                  # Application module
│   ├── src/main/java/com/ahmedyejam/mks/
│   │   ├── MainActivity.kt               Entry point (edge-to-edge, theme/language)
│   │   ├── MksApplication.kt             @HiltAndroidApp, Coil, crash handler
│   │   └── di/                           Hilt DI modules (6 modules)
│   │       ├── HiltDaoModule.kt          Core DAOs
│   │       ├── HiltKnowledgeDaoModule.kt Knowledge DAOs
│   │       ├── HiltUtilityDaoModule.kt   Utility DAOs
│   │       ├── HiltDataModule.kt         Database, FileManager, ExportManager
│   │       ├── HiltRepositoryModule.kt   GlobalSearchRepository
│   │       └── HiltServiceModule.kt      Firebase messaging & remote config providers
│   │   ├── service/                      Background services & workers
│   │   │   ├── AppFirebaseMessagingService.kt Push notification proxy
│   │   │   ├── RemoteConfigManager.kt    Stale-while-revalidate config state
│   │   │   └── TokenSyncWorker.kt        Offline-first FCM token sync
│   └── src/test/                          Unit tests
│
├── core/model/                            Entity & domain model definitions
│   └── entity/                            26 Room entity classes
│
├── core/database/                         Room Database, DAOs, Migrations
│   └── MksDatabase.kt                     Room v30, 26 entities, 26 DAOs
│   └── MksMigrations.kt                   29 migration steps (v1→v30)
│   └── dao/                               26 DAO interfaces
│
├── core/data/                             Repositories, business logic, import/export
│   ├── repository/                        Domain repositories (7+)
│   │   ├── BookRepository.kt, QuizRepository.kt, StudyRepository.kt
│   │   ├── KnowledgeRepository.kt, WorkspaceRepository.kt, AssetRepository.kt
│   │   ├── AiMcqRepository.kt, ReviewRepository.kt, GlobalSearchRepository.kt
│   │   └── ExportManager.kt
│   ├── importer/                          Multi-format import pipeline (18+ files)
│   ├── exchange/v7/                       ZIP-based exchange format
│   ├── seeder/MksDatabaseSeeder.kt        Sample data seeding
│   ├── preferences/DataStoreManager.kt    User preferences
│   ├── local/FileManager.kt               File I/O, image management
│   ├── error/GlobalErrorHandler.kt        Centralized error handling
│   └── ...                                preview, repair, validation, focus
│
├── core/network/                          AI client, OCR, PDF services
│   ├── AiClient.kt                        OpenAI-compatible (Groq, Gemini, DeepSeek, Ollama)
│   ├── McqService.kt, OcrService.kt       AI-powered MCQ & OCR
│   ├── PdfRendererService.kt              PDF page rendering
│   ├── PdfTextExtractor.kt                PDF text extraction (PDFBox)
│   └── OllamaRepository.kt               Ollama native API
│
├── core/ui/                               Shared Compose theme & components
│   ├── theme/                             Color, Type, Theme, MksDesignTokens
│   ├── components/                        Reusable dialogs & components
│   └── utils/TtsManager.kt                Text-to-speech
│
└── feature/ui/                            All feature screens & ViewModels
    ├── MksNavHost.kt                      Navigation graph (24 routes)
    ├── library/                           📖 Library management
    │   ├── LibraryScreen.kt, LibraryViewModel.kt
    │   └── components/                    Grid, TopBar, FAB, Dialogs, Sort
    ├── quiz/                              ❓ Quiz features
    │   ├── QuizPlayerScreen.kt, QuizViewModel.kt
    │   ├── QuizQuestionsScreen.kt, QuizDetailTabsScreen.kt
    │   ├── CompilerDialog.kt, CompilerViewModel.kt
    │   └── ZoomableImageDialog.kt
    ├── flashcard/                          🎴 Flashcards
    ├── slideshow/                          🎬 Slideshow courses
    ├── booktools/                          📚 Knowledge Bank tools
    │   ├── BookKnowledgeDashboardScreen.kt, BookToolsViewModel.kt
    │   ├── BookToolScreens.kt             (lists & detail screens)
    │   ├── AiMcqGeneratorScreen.kt         🤖 AI quiz generation
    │   └── PdfExtractionScreen.kt          📄 PDF text extraction
    ├── category/                           🏷️ Categories
    ├── session/                            📋 Session management
    ├── summary/                            📊 Results summary
    ├── scanner/                            📱 OCR Scanner
    ├── settings/                           ⚙️ Settings + ProviderConfigDialog
    ├── search/                             🔍 Global search
    ├── review/                             ✅ Review dashboard
    ├── data/                               🛠️ Data tools
    ├── welcome/                            👋 Onboarding
    ├── trash/                              🗑️ Trash bin dialog
    ├── workspace/                          📂 Workspace manager
    └── importer/                           📥 Import UI
```

---

## 🗄️ Database Schema (Room v30)

**26 Entity Classes, 26 DAO Interfaces**

### **Workspace Entities**
- `WorkspaceEntity` - Multi-workspace support
- `WorkspaceSettingsEntity` - Language, theme, defaults

### **Core Quiz/Library**
- `BookEntity` - Book/library container
- `QuizEntity` - Quiz within a book
- `QuestionEntity` - Questions with options & answers
- `SessionEntity` - Quiz attempts & answers
- `CategoryMetadataEntity` - Category metadata (emoji, color)
- `QuestionCategoryEntity` - Normalized many-to-many
- `QuestionAssetEntity` - Generic assets
- `SourceDocumentEntity` - Reference materials

### **Knowledge Bank**
- `FlashcardDeckEntity` - Deck metadata
- `FlashcardEntity` - Individual cards
- `LearningSessionEntity` - Learning state
- `SlideshowCourseEntity` - Course metadata
- `CourseSlideEntity` - Individual slides
- `NoteBlueprintEntity` - Note templates
- `NoteCollectionEntity` - Collection of note blueprints
- `PromptDeckEntity` - AI prompt collections
- `PromptCardEntity` - Individual prompts
- `PromptRunEntity` - Execution history
- `KnowledgeStudySessionEntity` - Non-quiz progress
- `StudySessionEntity` - Non-quiz study session progress

### **Additional**
- `AssetReferenceEntity` - Asset ownership index
- `MistakeLogEntryEntity` - Mistake tracking
- `AnnotationEntity` - Highlights & notes

> **Note:** `SourceDocumentAssetEntity` was dropped in migration v28→v29 and is no longer part of the schema.

**Migrations**: 29 incremental steps (v1→v30) in `MksMigrations.kt`

---

## 🧭 Navigation Routes (24)

```
welcome                                      # 👋 Onboarding (conditional)
library                                      # 📖 Main hub (default)
├── quiz/{quizId}?sessionId={sessionId}     # ❓ Quiz player
│   └── summary/{sessionId}                 # 📊 Results
├── quiz_questions/{quizId}                 # ❓ Question browser
├── sessions/{quizId}                       # 📋 Session tabs
├── flashcards/{deckId}?cardId={cardId}     # 🎴 Flashcard study
├── book_dashboard/{bookId}                 # 📚 Book overview
├── book_slideshows/{bookId}                # 🎬 Slideshow list
├── book_blueprints/{bookId}                # 📝 Blueprint list
├── book_notes/{bookId}                     # 📄 Book notes
├── book_prompts/{bookId}                   # 🤖 Prompt list
├── slideshow/{courseId}                    # 🎬 Play slideshow
├── blueprint/{noteId}                      # 📝 View blueprint
├── prompt_deck/{promptId}                  # 🤖 AI prompts
├── ai_mcq_generator/{bookId}              # 🧠 AI quiz generation
├── pdf_extraction/{sourceId}              # 📄 PDF text extraction
├── global_search                           # 🔍 Cross-entity search
├── review_dashboard?mistakeId={mistakeId}  # ✅ Review queue
├── data_tools                              # 🛠️ Import/export
├── settings                                # ⚙️ Preferences
├── category/{category}                     # 🏷️ Category questions
├── adaptive/{type}/{id}                    # 🎯 Adaptive training
└── scanner/{quizId}                        # 📱 OCR scanner
```

---

## 📥 Multi-Format Import Pipeline

The `ImportLibraryManager` orchestrates a sophisticated import process:

```
File Selection (URI)
    ↓
Format Detection
├─ XLSX → XlsxLibraryCompiler
├─ JSON → JsonQuestionParser
├─ ZIP → ZipLibraryParser
├─ CSV/TSV → DelimitedSpreadsheetParser
├─ HTML → HtmlQuestionParser
└─ TEXT → TextQuestionParser
    ↓
Header Mapping (multi-language support)
    ↓
Question Parsing (per row)
    ↓
Validation (ImportValidator)
    ↓
Normalization (BundleNormalizer)
    ↓
Entity Mapping (LibraryMapper)
    ↓
Atomic Transaction (database.withTransaction)
    ↓
Result: ImportResult (counts, warnings, errors)
```

**Supported Formats:**
- `XLSX` - Excel with embedded images
- `CSV/TSV` - Comma/tab-separated values
- `JSON` - Structured question objects
- `HTML` - Table/div extraction
- `TEXT` - Line-by-line parsing
- `ZIP` - Bundle archives

**Multi-Language Headers:**
```
English:  question, answer, explanation, hint, image, categories
Arabic:   السؤال, الإجابة, الشرح, تلميح, صورة, التصنيفات
```

---

## 🎨 UI State Management

All ViewModels use **StateFlow** for reactive UI:

```kotlin
private val _uiState = MutableStateFlow(InitialState())
val uiState = _uiState.asStateFlow()

// In Composable:
val state by viewModel.uiState.collectAsState()
```

### **Key ViewModels**

| ViewModel | Lines | Purpose |
|-----------|-------|---------|
| `QuizViewModel` | 1396 | ⭐ Largest - quiz logic, scoring, stats |
| `LibraryViewModel` | 641 | Books/quizzes management |
| `BookToolsViewModel` | — | ⭐ Unified knowledge bank |
| `CompilerViewModel` | — | Import orchestration |
| `GlobalSearchViewModel` | — | Cross-entity search |
| `ReviewDashboardViewModel` | — | Unified review queue |
| `SlideshowCourseViewModel` | — | Slideshow progression |

---

## 🖼️ Knowledge Bank Features

Books are containers that hold multiple learning formats:

### **1. Flashcard Decks**
- Front/back cards for spaced repetition
- Optional link to quiz questions
- Progress tracking via `LearningSessionEntity`

### **2. Slideshow Courses**
- Ordered progressive sequences
- Optional derivation from existing quizzes
- Per-slide completion tracking

### **3. Note Blueprints**
- Markdown + bullet summaries
- Review counters
- Optional source question links

### **4. AI Prompt Decks**
- Collections of prompt cards
- Configurable agent role
- Variable substitution & output tracking

### **5. Knowledge Study Sessions**
- Generic progress tracking
- Time spent, streaks, completion status
- Works with any book asset

---

## 🔍 Key Development Patterns

### **Dagger Hilt DI Pattern**
```kotlin
@HiltAndroidApp
class MksApplication : Application() {
    lateinit var appModule: AppModule // Legacy container for startup settings
    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
    }
}

// ViewModels inject dependencies directly:
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val studyRepository: StudyRepository
) : ViewModel()

// Instantiation in NavHost:
val viewModel: QuizViewModel = hiltViewModel()
```

### **StateFlow in UI**
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val state by viewModel.uiState.collectAsState()
    // Recomposes when state changes
}
```

### **Data Loading with LaunchedEffect**
```kotlin
LaunchedEffect(quizId) {
    viewModel.loadQuiz(quizId)
}
```

### **Coroutine Scoping**
- `viewModelScope` - UI-bound operations
- `Dispatchers.IO` - Database/file I/O
- `Dispatchers.Main` - UI updates (implicit)

---

## 🎯 Common Tasks for AI Agents

### **Task: Add New Knowledge Bank Asset Type**
1. Create entity in `core/model/.../entity/YourAssetEntity.kt`
2. Create DAO in `core/database/.../dao/YourAssetDao.kt`
3. Update `MksDatabase` (add entity, DAO, migration in `MksMigrations.kt`)
4. Provide DAO via appropriate Hilt module in `app/di/`
5. Add repository methods in the appropriate domain repository
6. Create Screen + ViewModel in `feature/ui/`
7. Add route to `MksNavHost.kt`
8. Update seed data in `core/data/seeder/MksDatabaseSeeder.kt`

See **AGENTS.md** § "Add New Knowledge Bank Asset Type"

### **Task: Add Database Column**
1. Create migration: `MIGRATION_N_(N+1)` in `core/database/.../MksMigrations.kt`
2. Add to `HiltDataModule.provideMksDatabase()` builder
3. Update entity class in `core/model/.../entity/`

See **AGENTS.md** § "Add New Database Column"

### **Task: Add New Import Format**
1. Create parser in `core/data/.../importer/parser/YourFormatParser.kt`
2. Add `ImportFormat.YOUR_FORMAT` enum
3. Update `ImportFormatDetector`
4. Update `CompilerViewModel.loadNonSpreadsheet()`

See **AGENTS.md** § "Add New Import Format"

### **Task: Add New Screen**
1. Create `feature/ui/.../{ feature}/{Feature}Screen.kt`
2. Create `feature/ui/.../{ feature}/{Feature}ViewModel.kt` with `@HiltViewModel`
3. Add route to `MksNavHost.kt` using `hiltViewModel()`
4. Add navigation calls

See **AGENTS.md** § "Add New Screen"

---

## 🧪 Testing & Sample Data

### **Seed Database**
`MksDatabaseSeeder.seed()` (in `core/data/seeder/`) initializes test data:
- 1 sample Book
- 1 Quiz with 13 questions
- Sample flashcard deck
- Sample slideshow course
- Sample notes & prompts

### **Reset Database**
Database reset triggers re-seeding via `MksDatabaseSeeder`.

### **Build & Test**
```bash
./gradlew assembleDebug              # Debug APK
./gradlew test                       # Unit tests
./gradlew connectedAndroidTest       # Instrumented tests
./gradlew build                      # Full build
```

---

## 🌍 Localization

### **Supported Languages**
- ✅ English
- ✅ Arabic (RTL layout)

### **String Resources**
- `app/src/main/res/values/strings.xml` - English
- `app/src/main/res/values-ar/strings.xml` - Arabic

### **Multi-Language Header Detection**
The import pipeline recognizes both English and Arabic field names:
- Question: "question", "سؤال"
- Answer: "answer", "الإجابة"
- Etc. (full list in **AGENTS.md**)

---

## 🎨 Theming System

### **Available Themes (7)**
1. Dawn
2. Forest
3. Midnight
4. Lavender
5. Plain Light
6. Plain Dark
7. System (follows device)

### **Customization**
- Dynamic font scaling (0.8×–1.5×)
- UI density control (0.5×–1.5×)
- Material3 color schemes
- Design tokens via `LocalMksDesignTokens`

---

## 📊 Performance Characteristics

| Aspect | Details |
|--------|---------|
| **Memory** | 25% RAM for image cache (Coil) |
| **Database** | Room v30, 29 incremental migrations |
| **UI** | Jetpack Compose with lazy loading |
| **Images** | Coil disk + memory cache (crossfade) |
| **Lists** | LazyVerticalGrid with stable keys |
| **Min SDK** | 30 (Android 11) |
| **Target SDK** | 37 (Android 15) |

---

## 🔗 Documentation Reference

| Document | Size | Focus |
|----------|------|-------|
| **AGENTS.md** | 33 KB | Deep technical guidance |
| **APP_ARCHITECTURE_MAP.md** | 18 KB | System architecture |
| **USER_JOURNEY_MAP_claudeopus.md** | 42 KB | UI/UX flows |
| **user_Jour_Geminipro.md** | 13 KB | Quick UI reference |

---

## 🚀 Getting Started for AI Agents

### **For Understanding the Codebase**
1. **Start here** → `README.md` (this file)
2. **Architecture** → `APP_ARCHITECTURE_MAP.md`
3. **Deep dive** → `AGENTS.md`
4. **UI flows** → `USER_JOURNEY_MAP_claudeopus.md`

### **For Finding Specific Code**
- **Import logic** → `core/data/.../importer/repository/ImportLibraryManager.kt`
- **Database** → `core/database/.../MksDatabase.kt`
- **Repositories** → `core/data/.../repository/` (BookRepository, QuizRepository, etc.)
- **Quiz logic** → `feature/ui/.../quiz/QuizViewModel.kt`
- **Navigation** → `feature/ui/.../MksNavHost.kt`

### **For Adding Features**
- Consult **AGENTS.md** § "Common Tasks"
- Follow existing patterns (e.g., study `BookToolsViewModel` for unified VM)
- Update seed data for testing

---

## 📝 Code Statistics

- **Total Lines**: ~1000 Compose screens, 2600+ repository lines
- **Entities**: 26 Room entities
- **DAOs**: 26 data access objects
- **Routes**: 24 navigation routes
- **Migrations**: 29 database versions
- **Themes**: 7 variants + design tokens

---

## ✨ Key Features

- ✅ **Multi-format import** (XLSX, JSON, CSV, HTML, TEXT, ZIP)
- ✅ **Interactive quizzes** (single/multiple choice, fill-in)
- ✅ **Flashcard decks** (spaced repetition)
- ✅ **Slideshow courses** (progressive learning)
- ✅ **Note blueprints** (markdown summaries)
- ✅ **AI prompt decks** (agent-driven interactions)
- ✅ **Global search** (cross-entity FTS)
- ✅ **Adaptive training** (spaced repetition optimization)
- ✅ **Offline-First Infrastructure** (FCM Token syncing via WorkManager & Remote Config caching)
- ✅ **Image support** (local + remote with caching)
- ✅ **Bilingual UI** (English + Arabic RTL)
- ✅ **Session tracking** (quiz attempts, progress)
- ✅ **Review dashboard** (unified learning queue)
- ✅ **Data tools** (bulk import/export)
- ✅ **OCR scanner** (camera-based input)

---

## 📞 Contact & Support

- **Repository**: https://github.com/AhmedEjam/MKS_FInal
- **Owner**: AhmedEjam
- **License**: (See LICENSE file if present)

---

**Last Updated**: July 10, 2026  
**Database Version**: Room v30 (29 migrations)  
**Kotlin**: 99.5% of codebase

---

### 🤖 AI Agent Tip

This README is designed to be **AI agent-friendly**. Use it as your entry point, then:
1. Cross-reference with **AGENTS.md** for detailed patterns
2. Search for specific files using the tree structure above
3. Consult **APP_ARCHITECTURE_MAP.md** for system overview
4. Review **USER_JOURNEY_MAP_claudeopus.md** for UI context

For specific implementation details, AGENTS.md provides comprehensive guidance on architecture decisions, common tasks, and development workflows.
