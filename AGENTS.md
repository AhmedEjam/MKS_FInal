# AGENTS.md - MKS Project Guidance

**MKS** is an Android quiz and knowledge-bank application that imports educational content from spreadsheets and documents, then presents interactive quizzes, flashcards, slideshows, and study materials with image support. This guide covers the actual implementation for AI agents.

## Project Overview

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (single-activity)
- **DI:** Manual `AppModule` (not Hilt)
- **Database:** Room v26 (25 migration steps, v1→v26)
- **File Import:** Multi-format (XLSX, CSV/TSV, JSON, HTML, TEXT, ZIP)
- **Images:** Coil (memory + disk cache), embedded XLSX images, HTTP downloads
- **Preferences:** DataStore
- **Localization:** English + Arabic (RTL support)
- **Knowledge Bank:** Books contain quizzes, flashcard decks, slideshow courses, note blueprints, and prompt decks

## Architecture Fundamentals

### Manual Dependency Injection Pattern

MKS uses a custom `AppModule` container instead of Hilt. This is initialized once in `MksApplication`:

```kotlin
class MksApplication : Application(), ImageLoaderFactory {
    lateinit var appModule: AppModule
    override fun onCreate() {
        appModule = AppModule(this)
    }
}
```

**Key insight:** Access dependencies via `appModule` throughout the codebase. All ViewModels receive `appModule.dependency` through manual `ViewModelProvider.Factory` (see Navigation section).

### Dependency Injection Container

`AppModule` provides lazy-initialized singletons (evaluated once, on first access):

```
database              → MksDatabase (Room v26 with 25 migration steps)
fileManager           → FileManager (image I/O, HTTP downloads)
repository            → MksRepository (single source of truth)
exportManager         → ExportManager (quiz/book/knowledge-bank ZIP export)
importManager         → ImportLibraryManager (multi-format import orchestration)
dataStoreManager      → DataStoreManager (user preferences)
focusManager          → FocusManager (adaptive training logic)
applicationScope      → CoroutineScope(Dispatchers.Default)
```

**Testing:** Call `appModule.resetDatabase()` to wipe and re-seed data.

## Database & Entities

### Schema (Room v26)

The active schema is defined in `app/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt` and exported through Room/KSP when the Android build runs.

**Workspace entities:**
- `WorkspaceEntity`: multi-workspace support with defaults, soft deletes.
- `WorkspaceSettingsEntity`: workspace-specific preferences (language, theme, defaults).

**Core quiz/library entities:**
- `BookEntity`: book/library container linked to workspace with `externalId`, title/description, field metadata, cover image, study stats, pin/system flags, and timestamps.
- `QuizEntity`: quiz inside a book with category, cover image, study stats, pin/system flags, and timestamps.
- `QuestionEntity`: question content, type, options/correct answers, explanation/hint/reference/image metadata, categories, attempts, and weight.
- `SessionEntity`: quiz session answers and completion state.
- `CategoryMetadataEntity`: category label metadata (`emoji`, `color`, `isPinned`).
- `QuestionCategoryEntity`: normalized many-to-many question/category index.

**Knowledge-bank/study entities:**
- `FlashcardDeckEntity`: deck metadata, progress counters, pin/system flags, and timestamps.
- `FlashcardEntity`: front/back card content, hint/image/tags, review metrics, optional `sourceQuestionId`, and `syncConfig`.
- `LearningSessionEntity`: flashcard-deck learning session state stored as JSON.
- `SlideshowCourseEntity`: slideshow course metadata, progress, pin/system flags, derivation flags, optional source quiz, and timestamps.
- `CourseSlideEntity`: slide body, notes, image, order, completion state, optional source question, and sync config.
- `NoteBlueprintEntity`: note/blueprint body, summary, bullet points, tags, review counters, optional source question, and timestamps.
- `PromptDeckEntity`: deck metadata for AI prompts, tags, and timestamps.
- `PromptCardEntity`: individual prompt within a deck, stem, variables, output type, and timestamps.
- `PromptRunEntity`: history of prompt execution with variables and output.
- `KnowledgeStudySessionEntity`: generic progress tracker for non-quiz study surfaces.

**Assets & Reference entities:**
- `AssetReferenceEntity`: normalized local asset ownership index.
- `QuestionAssetEntity`: generic assets linked to questions (images, docs, files).
- `SourceDocumentEntity`: source materials linked to books for reference.

**Additional Study entities:**
- `MistakeLogEntryEntity`: tracks mistakes across quizzes with user explanations.
- `AnnotationEntity`: highlights and notes linked to different content types.

### Migrations

Currently at v26 (25 migration steps total: 1→2, 2→3, ..., 25→26).
Active schema source of truth: `app/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt`.
All migrations are centralized in `com.ahmedyejam.mks.data.local.MksMigrations`.

Migration regression coverage now includes:
- `Migration15To16Test`: verifies the v15→v16 knowledge-bank tables/columns/indexes and guards against duplicate `slideshow_courses.isPinned` columns.
- `Migration16To17Test`: verifies the v16→v17 category/asset tables and indexes.

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

Add all migrations to `AppModule.databaseBuilder().addMigrations(...)`.

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
saveParsedQuestions() → MksRepository.importParsedQuestions()
  ↓
ImportLibraryManager routes to Room database
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

### JSON/HTML/TEXT Parsing

- Read entire content via `ContentResolver.openInputStream()`
- Route to:
  - `JsonQuestionParser` - parse nested question objects
  - `HtmlQuestionParser` - extract from HTML tables/divs
  - `TextQuestionParser` - line-by-line plain text

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
   - XlsxImageResolver.getCellImagePathMap() → cell address → image data
   - Used by SpreadsheetQuestionParser
2. **Image column** (URL or Base64)
3. **Question/answer cells** (embedded URL or Base64 data URI)
4. **Option cells** (fallback if no image column)
5. **Row-level images** (XLSX only)
6. **Merged cell regions** (if original cell empty)

### FileManager I/O

All image operations centralized:

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

## Navigation & Routing

### Route Structure

```
welcome                                 # WelcomeScreen (conditional start)
library                                 # LibraryScreen (start after welcome)
├─ quiz/{quizId}?sessionId={sessionId} # QuizPlayerScreen
│  └─ summary/{sessionId}               # SummaryScreen
├─ quiz_questions/{quizId}             # QuizQuestionsScreen
├─ flashcards/{deckId}                 # FlashcardDeckScreen (Knowledge Bank)
├─ slideshow/{courseId}                # SlideshowCourseScreen (Knowledge Bank)
├─ blueprint/{noteId}                  # ReviewBlueprintScreen (Knowledge Bank)
├─ book_notes/{bookId}                 # BookNotesScreen (Knowledge Bank)
├─ prompt_deck/{promptId}              # AiPromptDeckScreen (Knowledge Bank)
├─ category/{category}                 # CategoryQuestionsScreen
├─ adaptive/{type}/{id}                # AdaptiveTrainingScreen
│  (type = "BOOK"|"CATEGORY"|"QUIZ"|"QUESTION")
├─ sessions/{quizId}                   # SessionManagementScreen
├─ scanner/{quizId}                    # ScannerScreen
└─ settings                             # SettingsScreen
```

**Knowledge Bank Screens** (new):
- All routed through unified `BookToolsViewModel` or dedicated ViewModels
- All use manual DI pattern like other screens
- Integrated into `MksNavHost.kt` with appropriate route parameters

### ViewModelFactory Pattern (Manual DI)

All viewmodels created with custom factories (no Hilt):

```kotlin
val viewModel: CompilerViewModel = viewModel(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CompilerViewModel(appModule.context, appModule.repository) as T
        }
    }
)
```

**Pattern used in:**
- `MksNavHost.kt` for all screen routes
- Each Composable receives dependencies from `appModule`

### Data Loading in Effects

Use `LaunchedEffect` to trigger ViewModel data loads after recomposition:

```kotlin
LaunchedEffect(quizId) {
    viewModel.loadQuiz(quizId)
}
```

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
class BookToolsViewModel(repository: MksRepository) : ViewModel() {
    private val _slideshowState = MutableStateFlow<SlideshowUiState>(SlideshowUiState())
    private val _noteState = MutableStateFlow<NoteUiState>(NoteUiState())
    private val _promptState = MutableStateFlow<PromptUiState>(PromptUiState())
    
    // Separate functions to load/update each asset type
    fun loadSlideshow(courseId: Long) { /*...*/ }
    fun loadNoteBlueprint(noteId: Long) { /*...*/ }
    fun loadPromptDeck(promptId: Long) { /*...*/ }
}
```

Inject into multiple composables:

```kotlin
val viewModel: BookToolsViewModel = viewModel(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BookToolsViewModel(appModule.repository) as T
        }
    }
)

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

## Development Workflow

### Build Commands

```bash
./gradlew assembleDebug    # Compile debug APK
./gradlew test             # Run unit tests
./gradlew connectedAndroidTest  # Instrumented tests
./gradlew build            # Full build + tests (release)
```

### Sample Data & Testing

`AppModule.seedDatabase()` on first app launch:
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

**Reset for testing:** `appModule.resetDatabase()` wipes and re-seeds.

### WelcomeScreen & Onboarding

A new conditional first-run experience:

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

### Project Structure

```
app/src/main/
├─ java/com/ahmedyejam/mks/
│  ├─ MainActivity.kt                 # Single activity
│  ├─ MksApplication.kt               # App + Coil setup
│  ├─ di/
│  │  └─ AppModule.kt                 # DI container (838 lines)
│  ├─ data/
│  │  ├─ import/                      # Multi-format parsing pipeline
│  │  │  ├─ detector/ImportFormatDetector.kt
│  │  │  ├─ parser/                   # Header mapping, question parsing
│  │  │  ├─ model/ParsedQuestion, etc.
│  │  │  ├─ xlsx/
│  │  │  │  ├─ XlsxImageResolver.kt
│  │  │  │  └─ XlsxLibraryCompiler.kt
│  │  │  └─ validation/ImportValidator.kt
│  │  ├─ local/
│  │  │  ├─ MksDatabase.kt            # Room v17
│  │  │  ├─ entity/                   # 15 entity classes
│  │  │  │  ├─ {Book,Quiz,Question,Session,Category}Entity.kt
│  │  │  │  ├─ FlashcardDeckEntity.kt
│  │  │  │  ├─ FlashcardEntity.kt
│  │  │  │  ├─ SlideshowCourseEntity.kt
│  │  │  │  ├─ CourseSlideEntity.kt
│  │  │  │  ├─ NoteBlueprintEntity.kt
│  │  │  │  ├─ PromptEntity.kt
│  │  │  │  ├─ LearningSessionEntity.kt
│  │  │  │  ├─ KnowledgeStudySessionEntity.kt
│  │  │  │  ├─ QuestionCategoryEntity.kt
│  │  │  │  └─ AssetReferenceEntity.kt
│  │  │  ├─ dao/                      # DAO interfaces
│  │  │  ├─ converter/                # TypeConverters
│  │  │  └─ FileManager.kt
│  │  ├─ repository/
│  │  │  ├─ MksRepository.kt          # Main data access
│  │  │  └─ ExportManager.kt
│  │  ├─ preferences/DataStoreManager.kt
│  │  ├─ focus/FocusManager.kt
│  │  └─ model/                       # Domain models
│  └─ ui/
│     ├─ MksNavHost.kt                # Navigation setup
│     ├─ welcome/WelcomeScreen.kt     # Onboarding (new)
│     ├─ library/LibraryScreen + ViewModel
│     ├─ quiz/
│     │  ├─ CompilerViewModel.kt      # Import orchestrator
│     │  ├─ QuizViewModel.kt
│     │  ├─ QuizQuestionsViewModel.kt
│     │  ├─ QuizPlayerScreen.kt
│     │  └─ ...Screen files
│     ├─ flashcard/
│     │  ├─ FlashcardDeckScreen.kt    # Flashcard display (new)
│     │  └─ FlashcardDeckViewModel.kt
│     ├─ booktools/
│     │  ├─ BookToolScreens.kt        # Multiple Knowledge Bank screens (new)
│     │  │  ├─ SlideshowCourseScreen
│     │  │  ├─ ReviewBlueprintScreen
│     │  │  ├─ BookNotesScreen
│     │  │  └─ AiPromptDeckScreen
│     │  └─ BookToolsViewModel.kt     # Unified ViewModel
│     ├─ summary/SummaryScreen + ViewModel
│     ├─ category/CategoryQuestionsScreen + ViewModel
│     ├─ session/SessionManagementScreen + ViewModel
│     ├─ scanner/ScannerScreen + ViewModel
│     ├─ settings/SettingsScreen
│     ├─ import/ImportViewModel.kt
│     └─ theme/
│        ├─ Color.kt
│        ├─ Type.kt
│        ├─ Theme.kt
│        └─ MksDesignTokens.kt
└─ res/
   ├─ values/strings.xml
   └─ values-ar/strings.xml
```

## Architecture Decisions

### Why Manual DI Instead of Hilt?

- **Simpler scope management** for single-activity app
- **Explicit dependency visibility** (no annotation magic)
- **Easier debugging** during development
- **Legacy compatibility** if converting other projects

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

## Additional Resources

The following complementary documentation exists in the project root:

- **`CLAUDE.md`** - Guidance specific to Claude AI coding sessions
- **`KNOWLEDGE_BANK_IMPLEMENTATION_PLAN.md`** - Detailed vision and implementation notes for the Knowledge Bank feature
- **`KNOWLEDGE_BANK_CREATION_UI_NOTES.md`** - UI screen designs and navigation flow for Knowledge Bank screens
- **`APP_ARCHITECTURE_MAP.md`** - High-level architecture overview with component diagrams

For most tasks, read the relevant sections in this AGENTS.md. For deep architectural questions, consult the additional markdown files in the project root.

## Common Tasks

### Add New Knowledge Bank Asset Type

To add a new learning asset (similar to flashcards, slideshows, etc.):

1. Create entity in `data/local/entity/YourAssetEntity.kt`
   - Include `bookId`, timestamps, stats fields
   - Use `@PrimaryKey(autoGenerate = true)` for id
   - For ordered content (slides, cards), include `order: Int` field

2. Create DAO in `data/local/dao/YourAssetDao.kt`
   - Queries for CRUD, list by book, update progress
   - Foreign key on bookId or parent asset

3. Update `MksDatabase`:
   - Add entity to `@Database(entities = [...])` annotation
   - Add abstract dao accessor: `abstract fun yourAssetDao(): YourAssetDao`
   - Create migration adding table with `CREATE TABLE IF NOT EXISTS`

4. Add repository methods in `MksRepository`:
   - `createYourAsset()`, `updateYourAsset()`, `deleteYourAsset()`
   - `getYourAsset(id)`, `getYourAssetsByBook(bookId)`
   - Touch parent book's `lastEditedAt` on mutations
   - Create optional `KnowledgeStudySessionEntity` for progress

5. Create Screen and ViewModel:
   - Use existing `BookToolsViewModel` or create dedicated ViewModel
   - Add route to `MksNavHost.kt`
   - Implement StateFlow-based UI state

6. Update seed data in `AppModule.seedDatabase()`
   - Add sample asset instances to test database

### Manage Knowledge Asset Progress

Track KnowledgeStudySessionEntity for any non-quiz asset:

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

1. Create parser in `data/import/parser/YourFormatParser.kt`
   - Implement `parse(content: String): List<ParsedQuestion>`
2. Add `ImportFormat.YOUR_FORMAT` enum variant
3. Update `ImportFormatDetector.detectByExtension()` / `detectByMimeType()`
4. Add route in `CompilerViewModel.loadNonSpreadsheet()` when block
5. Add UI affordance in LibraryScreen

### Add New Database Column

1. Create migration in `AppModule`:
   ```kotlin
   val MIGRATION_N_(N+1) = object : Migration(N, N+1) {
       override fun migrate(database: SupportSQLiteDatabase) {
           database.execSQL("ALTER TABLE table_name ADD COLUMN new_column TYPE DEFAULT value")
       }
   }
   ```
2. Add to `database.builder().addMigrations(...)`
3. Update entity class with new field

### Add New Screen

1. Create `ui/{feature}/{Feature}Screen.kt` (Composable)
2. Create `ui/{feature}/{Feature}ViewModel.kt`
3. Add route to `MksNavHost.kt` with ViewModelFactory
4. Add navigation calls from existing screens

### Customize Header Detection

Edit `SpreadsheetHeaderMapper.aliases` to add field names:

```kotlin
"custom_field" to listOf("custom", "my_field", "حقلي")
```

Update `scoreHeaderRow()` scoring if needed (e.g., +5 for critical fields).
