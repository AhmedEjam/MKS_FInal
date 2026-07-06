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
✅ Dependency Inject:  Dagger Hilt (with legacy AppModule)
✅ Database:           Room v30 (29 migrations)
✅ Image Loading:      Coil (25% RAM cache)
✅ Preferences:        DataStore
✅ Localization:       English + Arabic (RTL)
✅ Min SDK:            30 | Target SDK: 35
```

### **Dependency Injection Container**

ViewModels and dependencies are managed and injected via **Dagger Hilt**. The legacy `AppModule` is retained only for early startup settings (language/theme) in MainActivity.

```kotlin
// ViewModels obtain dependencies via constructor injection:
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: MksRepository,
    // ...
) : ViewModel()
```

---

## 📁 Project Structure & File Map

### **Source Tree** (`app/src/main/java/com/ahmedyejam/mks/`)

```
mks/
├── MainActivity.kt                    [99 lines] Entry point
├── MksApplication.kt                  [34 lines] App init + Coil setup
│
├── di/
│   └── AppModule.kt                   [1403 lines] ⭐ DI Container
│
├── data/
│   ├── local/
│   │   ├── MksDatabase.kt            Room v30, 26 entities, 26 DAOs
│   │   ├── MksMigrations.kt          29 migration steps (1→30)
│   │   ├── Converters.kt             Type converters
│   │   ├── FileManager.kt            I/O, HTTP, image management
│   │   ├── entity/                   24 entity classes (see below)
│   │   └── dao/                      24 DAO interfaces
│   │
│   ├── repository/
│   │   ├── MksRepository.kt          [2632 lines] ⭐ Main data access
│   │   └── ExportManager.kt          Library export logic
│   │
│   ├── import/                       Multi-format import pipeline
│   │   ├── repository/
│   │   │   └── ImportLibraryManager.kt [934 lines] Orchestration
│   │   ├── detector/ImportFormatDetector.kt Format detection
│   │   ├── parser/
│   │   │   ├── SpreadsheetHeaderMapper.kt
│   │   │   ├── SpreadsheetQuestionParser.kt
│   │   │   ├── JsonQuestionParser.kt
│   │   │   ├── HtmlQuestionParser.kt
│   │   │   └── TextQuestionParser.kt
│   │   ├── xlsx/
│   │   │   ├── XlsxLibraryCompiler.kt Excel processing
│   │   │   └── XlsxImageResolver.kt   Embedded image extraction
│   │   ├── mapping/LibraryMapper.kt   Entity conversion
│   │   ├── validation/ImportValidator.kt
│   │   ├── security/RemoteAssetPolicy.kt
│   │   └── model/ParsedQuestion, etc. DTOs
│   │
│   ├── preferences/
│   │   └── DataStoreManager.kt       User preferences
│   │
│   ├── focus/
│   │   └── FocusManager.kt           Adaptive training
│   │
│   ├── search/
│   │   ├── GlobalSearchRepository.kt Cross-entity search
│   │   └── GlobalSearchDao           (in database)
│   │
│   ├── review/
│   │   ├── ReviewRepository.kt       Unified review queue
│   │   └── dao/
│   │
│   ├── export(full)/                 Full library export
│   ├── exchange/                     Bundle format (v7)
│   ├── network/                      RemoteAssetFetcher
│   ├── preview/                      Delete/merge previews
│   ├── repair/                       Asset audit
│   ├── simulation/                   Training simulation
│   ├── validation/                   Data validation
│   └── model/                        Domain models
│
└── ui/                               Jetpack Compose screens (19 packages)
    ├── MksNavHost.kt                 [708 lines] ⭐ Navigation graph (22+ routes)
    ├── MksRoutes.kt                  Route constants & builders
    │
    ├── library/                      📖 Library management
    │   ├── LibraryScreen.kt          [756 lines]
    │   ├── LibraryViewModel.kt       [641 lines]
    │   └── components/
    │       ├── LibraryComponents.kt  [1128 lines]
    │       ├── LibraryContentGrid.kt
    │       ├── LibraryTopBar.kt
    │       ├── LibraryFabMenu.kt
    │       ├── LibraryDialogs.kt
    │       └── SortDialog.kt
    │
    ├── quiz/                         ❓ Quiz features
    │   ├── QuizPlayerScreen.kt       [1336 lines] Main player
    │   ├── QuizViewModel.kt          [1396 lines] ⭐ Largest VM
    │   ├── QuizQuestionsScreen.kt    Question browser
    │   ├── QuizQuestionsViewModel.kt
    │   └── CompilerViewModel.kt      Import orchestrator
    │
    ├── flashcard/                    🎴 Flashcards
    │   └── FlashcardDeckScreen.kt
    │
    ├── slideshow/                    🎬 Slideshow courses
    │   ├── SlideshowCourseScreen.kt
    │   └── SlideshowCourseViewModel.kt
    │
    ├── booktools/                    📚 Knowledge Bank tools
    │   ├── BookToolsViewModel.kt     ⭐ Unified ViewModel
    │   ├── BookKnowledgeDashboardScreen.kt
    │   ├── AiPromptDeckScreen.kt
    │   ├── AiPromptDeckListScreen.kt
    │   ├── BookNotesScreen.kt
    │   ├── ReviewBlueprintScreen.kt
    │   ├── ReviewBlueprintListScreen.kt
    │   ├── SlideshowCourseListScreen.kt
    │   └── SourceDocumentListScreen.kt
    │
    ├── category/                     🏷️ Categories
    │   ├── CategoryQuestionsScreen.kt
    │   └── CategoryQuestionsViewModel.kt
    │
    ├── session/                      📋 Session management
    │   ├── SessionManagementScreen.kt
    │   └── SessionViewModel.kt
    │
    ├── summary/                      📊 Results summary
    │   ├── SummaryScreen.kt          [500 lines]
    │   └── SummaryViewModel.kt
    │
    ├── scanner/                      📱 OCR Scanner
    │   ├── ScannerScreen.kt
    │   └── ScannerViewModel.kt
    │
    ├── settings/                     ⚙️ Settings
    │   └── SettingsScreen.kt         [525 lines]
    │
    ├── search/                       🔍 Global search
    │   ├── GlobalSearchScreen.kt
    │   └── GlobalSearchViewModel.kt
    │
    ├── review/                       ✅ Review dashboard
    │   ├── ReviewDashboardScreen.kt
    │   └── ReviewDashboardViewModel.kt
    │
    ├── data/                         🛠️ Data tools
    │   ├── DataToolsScreen.kt
    │   └── DataToolsViewModel.kt
    │
    ├── welcome/                      👋 Onboarding
    │   └── WelcomeScreen.kt
    │
    ├── import/                       📥 Import UI
    │   └── ImportViewModel.kt
    │
    ├── components/                   🧩 Shared UI components
    ├── navigation/                   Route builders & helpers
    ├── common/                       InvalidRouteScreen.kt
    └── theme/                        🎨 Theming
        ├── Color.kt
        ├── Type.kt
        ├── Theme.kt
        └── MksDesignTokens.kt
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
- `SourceDocumentAssetEntity` - Assets linked to source documents

**Migrations**: 29 incremental steps (v1→v30) in `MksMigrations.kt`

---

## 🧭 Navigation Routes (22+)

```
welcome                                      # 👋 Onboarding (conditional)
library                                      # 📖 Main hub (default)
├── quiz/{quizId}?sessionId={sessionId}     # ❓ Quiz player
│   └── summary/{sessionId}                 # 📊 Results
├── quiz_questions/{quizId}                 # ❓ Question browser
├── flashcards/{deckId}?cardId={cardId}     # 🎴 Flashcard study
├── book_dashboard/{bookId}                 # 📚 Book overview
├── book_slideshows/{bookId}                # 🎬 Slideshow list
├── book_blueprints/{bookId}                # 📝 Blueprint list
├── book_sources/{bookId}                   # 📄 Source docs
├── book_prompts/{bookId}                   # 🤖 Prompt list
├── book_notes/{bookId}                     # 📄 Book notes
├── slideshow/{courseId}                    # 🎬 Play slideshow
├── blueprint/{noteId}                      # 📝 View blueprint
├── prompt_deck/{promptId}                  # 🤖 AI prompts
├── global_search                           # 🔍 Cross-entity search
├── review_dashboard?mistakeId={mistakeId}  # ✅ Review queue
├── data_tools                              # 🛠️ Import/export
├── settings                                # ⚙️ Preferences
├── category/{category}                     # 🏷️ Category questions
├── adaptive/{type}/{id}                    # 🎯 Adaptive training
├── sessions/{quizId}                       # 📋 Session history
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
    private val repository: MksRepository
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
1. Create entity in `data/local/entity/YourAssetEntity.kt`
2. Create DAO in `data/local/dao/YourAssetDao.kt`
3. Update `MksDatabase` (add entity, DAO, migration)
4. Add repository methods in `MksRepository`
5. Create Screen + ViewModel in `ui/`
6. Add route to `MksNavHost.kt`
7. Update seed data in `AppModule.seedDatabase()`

See **AGENTS.md** § "Add New Knowledge Bank Asset Type"

### **Task: Add Database Column**
1. Create migration: `MIGRATION_N_(N+1)` in `AppModule`
2. Add to `database.builder().addMigrations(...)`
3. Update entity class

See **AGENTS.md** § "Add New Database Column"

### **Task: Add New Import Format**
1. Create parser in `data/import/parser/YourFormatParser.kt`
2. Add `ImportFormat.YOUR_FORMAT` enum
3. Update `ImportFormatDetector`
4. Update `CompilerViewModel.loadNonSpreadsheet()`

See **AGENTS.md** § "Add New Import Format"

### **Task: Add New Screen**
1. Create `ui/{feature}/{Feature}Screen.kt`
2. Create `ui/{feature}/{Feature}ViewModel.kt`
3. Add ViewModelFactory in `MksNavHost.kt`
4. Add navigation calls

See **AGENTS.md** § "Add New Screen"

---

## 🧪 Testing & Sample Data

### **Seed Database**
`AppModule.seedDatabase()` initializes test data:
- 1 sample Book
- 1 Quiz with 13 questions
- Sample flashcard deck
- Sample slideshow course
- Sample notes & prompts

### **Reset Database**
```kotlin
appModule.resetDatabase()  // Wipe and re-seed
```

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
- Dynamic font scaling (0.5×–2.0×)
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
| **Target SDK** | 35 (Android 15) |

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
- **Import logic** → `data/import/repository/ImportLibraryManager.kt`
- **Database** → `data/local/MksDatabase.kt`
- **Main repo** → `data/repository/MksRepository.kt`
- **Quiz logic** → `ui/quiz/QuizViewModel.kt`
- **Navigation** → `ui/MksNavHost.kt`

### **For Adding Features**
- Consult **AGENTS.md** § "Common Tasks"
- Follow existing patterns (e.g., study `BookToolsViewModel` for unified VM)
- Update seed data for testing

---

## 📝 Code Statistics

- **Total Lines**: ~1000 Compose screens, 2600+ repository lines
- **Entities**: 26 Room entities
- **DAOs**: 26 data access objects
- **Routes**: 22+ navigation routes
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

**Last Updated**: June 26, 2026  
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
