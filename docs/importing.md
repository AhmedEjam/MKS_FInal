# MKS Import & Input Paths — Complete Map

## Overview

Data enters the MKS app through **6 primary categories** of input paths, each with distinct entry
points, processing pipelines, and persistence mechanisms:

1. **File Import Pipeline** (XLSX, CSV/TSV, JSON, HTML, TEXT, ZIP)
2. **Exchange Archive Import** (Schema-7 encrypted ZIP bundles)
3. **Manual Creation** (UI dialogs for all entity types)
4. **Derived Creation** (Magic Actions generating assets from existing data)
5. **Camera/OCR Scanner** (physical text → digital questions)
6. **Database Seeding** (initial sample data on first launch)

---

## 1. File Import Pipeline

### 1A. Entry Points

| Entry Point                    | Trigger                                                             | Flow                                                                              |
|--------------------------------|---------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| **Library FAB "Import"**       | User taps FAB → "Import" mini-FAB                                   | `ActivityResultContracts.OpenDocument("*/*")` → `handleImportUri()`               |
| **Book/Quiz Options "Import"** | Context menu → "Import" action                                      | Same launcher, but pre-sets `targetBookId` or `targetQuizId`                      |
| **Android Share Intent**       | External app shares file via `ACTION_SEND` / `ACTION_SEND_MULTIPLE` | `MainActivity.handleIntent()` → `_sharedUris` → `LibraryScreen.handleImportUri()` |

**Supported MIME types for share intent** (declared in AndroidManifest.xml):

- `application/zip`, `application/x-zip-compressed`
- `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- `application/vnd.ms-excel`
- `text/csv`, `text/comma-separated-values`, `text/tab-separated-values`

### 1B. Format Detection

**File:** `core/data/.../importer/detector/ImportFormatDetector.kt`

Detection priority order:

1. **Extension:** `.json`, `.zip`, `.xlsx/.xlsm/.xltx/.xltm`, `.xls`, `.csv/.tsv`, `.txt`,
   `.html/.htm`
2. **Stream magic bytes** (if extension unknown):
    - `PK\x03\x04` + `xl/` or `[Content_Types].xml` → XLSX
    - `PK\x03\x04` (no xl/) → ZIP
    - `PK\x07\x07` → encrypted ZIP
    - `D0 CF 11 E0` → legacy XLS (mapped to XLSX mode)
    - `{` or `[` → JSON
    - `<!doc`, `<html`, `<body` → HTML
    - Tabular heuristic → CSV_TSV
    - Fallback → TEXT

### 1C. Import Dispatch (`handleImportUri`)

```
handleImportUri(uri)
├─ XLSX / CSV_TSV → CompilerViewModel.onFileSelected() → CompilerDialog
├─ ZIP → ImportViewModel.getImportPreview() → ImportReviewDialog
└─ JSON / HTML / TEXT → CompilerViewModel.onFileSelected() → CompilerDialog
```

Two distinct flows emerge:

- **Spreadsheet/Question formats** → `CompilerViewModel` → user previews/adjusts → saves
- **ZIP/Bundle formats** → `ImportViewModel` → preview with merge strategy → confirms

### 1D. CompilerViewModel Flow (Spreadsheet & Question Import)

**File:** `feature/ui/.../ui/quiz/CompilerViewModel.kt`

#### Step-by-step:

1. **Format detection** → `ImportFormatDetector.detectFormat(uri)` → ImportMode
2. **File loading**:
    - XLSX → `prepareTempFile(uri)` (max 50MB) → `ZipFile` for embedded images →
      `WorkbookFactory.create()` → sheet list for user selection
    - CSV/TSV → `CsvParser` with delimiter inference (`,` / `\t` / `;`)
    - JSON/HTML/TEXT → `readTextWithLimit()` (10-20MB) → `parseContent()`
3. **Header detection** (XLSX/CSV only):
    - `SpreadsheetHeaderMapper.calculateRowScore()` scores first 25 rows
    - Multi-language aliases: English + Arabic (e.g., "question"/"سؤال", "answer"/"الإجابة")
    - Option column detection: matches "Option A" patterns, or heuristic (columns between question
      and answer)
    - User can adjust header row and column mapping
4. **Row parsing**:
    - `SpreadsheetQuestionParser.parseRow()` per row
    - Stem from "question" column, options from option columns
    - Correct answers: letter-based ("A, C"), numeric ("1, 2"), text match, or marked cells (`*`,
      `✓`, `☑`, `✅`)
    - Image: image column → question cell → option cells → row-level → merged regions
    - Categories: split by `[,،;؛/|]`
5. **User preview** (`CompilerDialog`):
    - Question list with per-question inclusion toggle
    - Mapping editor (adjust header row, column-to-field mapping)
    - Range selection (bulk include/exclude)
    - Answer selection (long-press to mark correct)
    - Save dialog: title, target book/quiz/deck selection
6. **Save**:
    - **Flashcard target**: `ParsedQuestion` → `FlashcardEntity` (front=stem+options,
      back=answers+explanation) → `KnowledgeRepository.insertFlashcards()`
    - **Quiz target**: `ParsedQuestion` → wrap into `LibraryBundleDto` →
      `ImportLibraryManager.importQuestions()` → `executeImportPipeline()`

#### ParsedQuestion Data Structure

```
ParsedQuestion
├─ stem: String
├─ externalId: String?
├─ options: List<ParsedOption> (text + isMarked)
├─ correctAnswers: List<String>
├─ explanation, hint, reference, additionalInfo: String?
├─ categories: List<String>
├─ imageDataUrl, imageSource: String?
├─ type: QuestionType (SINGLE_CHOICE, MULTIPLE_CHOICE)
├─ isIncluded: Boolean (user toggleable)
├─ issues: List<String>
└─ sourceLine: Int
```

### 1E. ImportViewModel Flow (ZIP/Bundle Import)

**File:** `feature/ui/.../ui/importer/ImportViewModel.kt`

#### Step-by-step:

1. **`getImportPreview(uri)`** → `ImportLibraryManager.getImportPreview()`
    - Detect format → parse via `ZipLibraryParser` or `JsonLibraryParser`
    - Validate via `ImportValidator` (schema, duplicate IDs, question validity)
    - Normalize via `BundleNormalizer` (trim text, infer answerMode)
    - Check existing database entities by externalId → preview counts (to create vs to update)
    - Check for plain HTTP assets → warnings
2. **`ImportReviewDialog`**:
    - Summary card: book/quiz/question counts
    - **Merge strategy** selection: `SKIP_EXISTING` / `OVERWRITE_EXISTING` / `DUPLICATE`
    - "Allow insecure HTTP images" checkbox
3. **`importLibrary()`** → `ImportLibraryManager.importLibrary()` → `executeImportPipeline()` (
   inside Room transaction)

### 1F. Format-Specific Parsers

| Parser                        | File                                      | Input                      | Output                                                      |
|-------------------------------|-------------------------------------------|----------------------------|-------------------------------------------------------------|
| **CsvParser**                 | `.../parser/CsvParser.kt`                 | Raw CSV/TSV text           | `List<List<String>>` (rows × cells)                         |
| **SpreadsheetHeaderMapper**   | `.../parser/SpreadsheetHeaderMapper.kt`   | Header row                 | `Map<String, Int>` (field → column index)                   |
| **SpreadsheetQuestionParser** | `.../parser/SpreadsheetQuestionParser.kt` | Row data + mapping         | `ParsedQuestion`                                            |
| **JsonQuestionParser**        | `.../parser/JsonQuestionParser.kt`        | JSON text                  | `List<ParsedQuestion>`                                      |
| **JsonLibraryParser**         | `.../parser/JsonLibraryParser.kt`         | Full library JSON          | `LibraryBundleDto`                                          |
| **HtmlQuestionParser**        | `.../parser/HtmlQuestionParser.kt`        | HTML with embedded JSON    | `List<ParsedQuestion>`                                      |
| **TextQuestionParser**        | `.../parser/TextQuestionParser.kt`        | Plain text (Q1/A/B format) | `List<ParsedQuestion>`                                      |
| **ZipLibraryParser**          | `.../parser/ZipLibraryParser.kt`          | ZIP file                   | `LibraryBundleDto` + manifest + assets                      |
| **XlsxLibraryCompiler**       | `.../xlsx/XlsxLibraryCompiler.kt`         | XLSX URI                   | `LibraryBundleDto`                                          |
| **XlsxImageResolver**         | `.../xlsx/XlsxImageResolver.kt`           | XLSX ZipFile + sheet       | `SheetImageResolution` (cell-address → data URL)            |
| **PptxSlideParser**           | `.../parser/PptxSlideParser.kt`           | PPTX file                  | `List<CourseSlideEntity>`                                   |
| **TextSlideParser**           | `.../parser/TextSlideParser.kt`           | Plain text                 | `List<CourseSlideEntity>`                                   |
| **TextFlashcardParser**       | `.../parser/TextFlashcardParser.kt`       | Plain text                 | `List<FlashcardEntity>`                                     |
| **TextArticleParser**         | `.../parser/TextArticleParser.kt`         | Plain text                 | `List<NoteBlueprintEntity>`                                 |
| **GenericImageExtractor**     | `.../parser/GenericImageExtractor.kt`     | Text content               | `ResolvedImage` (data URL / HTTP URL / HTML img / Markdown) |

#### JsonQuestionParser Details

- Flexible JSON: list of objects, map with "questions" key, or single object
- Option formats: string list, object list with "text"/"correct" fields, or A/B/C letter fields
- 5-level answer resolution: exact text → letter match → multi-answer parse → marked options →
  word-boundary match
- Field aliases: "stem"/"question"/"q"/"text" for stem; "answer"/"correctAnswer"/"correct" for
  answers

#### HtmlQuestionParser Details

- 4 regex patterns for embedded JSON extraction:
    1. `<script id="quiz-data">...</script>`
    2. `window.__QUIZ__ = [...] | {...}`
    3. `window.QUIZ_DATA = [...] | {...}`
    4. `const allData = [...]`
- Each match is passed to `JsonQuestionParser.parse()`

#### TextQuestionParser Details

- Question start: `Q1)` / `Question 1.` / `سؤال 1)` patterns
- Option lines: `A) text` / `B) text` with optional mark indicators (*, checkmarks)
- Field lines: "answer:", "explanation:", "hint:", "reference:", "categories:" with Arabic aliases
- Separate answer key format: `1: A`

#### XlsxImageResolver Details

- Three strategies: DISPIMG formula images, anchored images (oneCellAnchor/twoCellAnchor), combined
- Reads `xl/cellimages.xml` + sheet drawing relationships
- Converts to data URLs (`data:image/png;base64,...`)
- Security: max 20MB per ZIP entry, canonical path validation

### 1G. ImportLibraryManager — Central Import Engine

**File:** `core/data/.../importer/repository/ImportLibraryManager.kt` (1065 lines)

All imports converge here. The `executeImportPipeline()` runs **inside a Room database transaction
**:

```
executeImportPipeline()
1. Validate → ImportValidator (schema, duplicates, question validity)
2. Normalize → BundleNormalizer (trim text, infer answerMode)
3. Persist (Room withTransaction):
   ├─ Get/create default workspace
   ├─ Import categories → categoryMetadataDao
   ├─ Import books → bookDao (workspace resolution + cover image + asset refs)
   ├─ Import quizzes → quizDao (book resolution + cover image + asset refs)
   ├─ Import questions → questionDao (image resolution + category sync + asset refs)
   ├─ Import sessions → sessionDao
4. Image resolution (resolveImagePath per entity):
   ├─ Data URLs → FileManager.saveBase64AsImageDetailed()
   ├─ ZIP-relative paths → directory search + FileManager.saveImageDetailed()
   ├─ HTTP URLs → FileManager.downloadAndSaveImageDetailed()
   └─ Local paths → FileManager.saveImageDetailed()
5. Return ImportResult (counts, warnings, errors)
```

#### Merge Strategy Handling

| Strategy             | Behavior                                                                                    |
|----------------------|---------------------------------------------------------------------------------------------|
| `SKIP_EXISTING`      | If entity with same externalId exists, skip it                                              |
| `OVERWRITE_EXISTING` | Update existing entity, preserving immutable fields (createdAt, lastStudiedAt, study stats) |
| `DUPLICATE`          | Always insert new, ignoring externalId checks                                               |

#### Workspace Context

- `activeWorkspaceId` parameter overrides workspace assignment
- Falls back to `DataStoreManager.currentWorkspaceId` → `getOrCreateDefaultWorkspaceId()`
- Books can reference `workspaceExternalId` that maps to an existing workspace

### 1H. LibraryBundleDto — Universal Intermediate Format

**File:** `core/data/.../importer/dto/LibraryBundleDto.kt`

All import paths converge to this DTO. It contains:

```
LibraryBundleDto
├─ schema: Int (default 6, V7 = 7)
├─ kind: String ("library-bundle" | "book-bundle" | "quiz-bundle" | "full-library")
├─ books: List<BookDto>
├─ quizzes: List<QuizDto> (each containing questions)
├─ flashcardDecks: List<FlashcardDeckDto> (each containing cards)
├─ slideshowCourses: List<SlideshowCourseDto> (each containing slides)
├─ noteBlueprints: List<NoteBlueprintDto>
├─ promptDecks: List<PromptDeckDto> (each containing cards)
├─ studySessions: List<KnowledgeStudySessionDto>
├─ sessions: List<SessionDto>? (quiz session history)
├─ categories: List<CategoryMetadataDto>
├─ progress: Map<String, JsonElement>?
```

### 1I. Image Handling During Import

#### Priority Order (resolveImagePath)

1. **Data URLs** (`data:image/...;base64,...`) → `FileManager.saveBase64AsImageDetailed()` (max
   12MB, dimension normalization)
2. **ZIP-relative paths** → search extracted temp directory for exact match → manifest mappings →
   recursive suffix/name search
3. **HTTP/HTTPS URLs** → `FileManager.downloadAndSaveImageDetailed()` via `RemoteAssetFetcher` (max
   12MB, 10s connect/20s read timeout)
    - Plain HTTP blocked unless `allowInsecureRemoteImages` checkbox is enabled
4. **Absolute local paths** → `FileManager.saveImageDetailed()` with path validation
5. **Unresolvable** → kept as-is with warning

#### Image Storage

All images saved as `img_<UUID>.<ext>` in `context.filesDir/images/`. Never overwrite.
Asset references tracked via `assetReferenceDao.replaceOwnerReferences()`.

### 1J. Validation & Security

#### ImportValidator

**File:** `core/data/.../importer/validation/ImportValidator.kt`

Checks: schema version (supports 4-7), bundle kind, duplicate book/quiz IDs, unbound quizzes,
per-question validation (blank stem, no options, no correct answers, duplicate IDs). **Invalid
questions are skipped** (added to `skippedRecords`).

#### Import Limits

**File:** `core/data/.../importer/security/ImportLimits.kt`

| Limit                        | Value   |
|------------------------------|---------|
| ZIP compressed size          | 100 MB  |
| ZIP entries                  | 1,000   |
| ZIP single file uncompressed | 50 MB   |
| ZIP total uncompressed       | 200 MB  |
| Text import                  | 10 MB   |
| CSV import                   | 20 MB   |
| HTML import                  | 10 MB   |
| XLSX import                  | 50 MB   |
| XLSX sheets                  | 20      |
| XLSX rows per sheet          | 20,000  |
| XLSX cells per sheet         | 400,000 |
| XLSX columns                 | 120     |
| Remote image download        | 12 MB   |
| Base64 image input           | 12 MB   |
| Image max dimension          | 2048px  |
| Image max pixels             | 12M     |
| Image quality                | 82      |

ZipLibraryParser security: path traversal check (`..` and `/` prefix blocked, canonical path
validation), entry count limits.

---

## 2. Exchange Archive Import (Schema-7 ZIP)

### 2A. Archive Structure

The v7 exchange format is a **split-directory, JSON-based archive** inside an AES-256 encrypted
ZIP (password: `"mks_secure_bundle_2024"`), designed for cross-platform Android/iOS interchange.

```
mks_exchange.zip (AES-256 encrypted)
├─ manifest.json              ← format, schema version, counts, warnings
├─ workspace.json             ← workspaces + settings
├─ data/books.json
├─ data/quizzes.json
├─ data/questions.json
├─ data/question_categories.json
├─ data/flashcard_decks.json
├─ data/flashcards.json
├─ data/slideshows.json
├─ data/slides.json
├─ data/notes.json
├─ data/prompt_decks.json
├─ data/prompt_cards.json
├─ data/study_sessions.json
├─ data/asset_references.json
├─ data/question_assets.json
├─ data/source_documents.json
├─ data/annotations.json
├─ data/media_manifest.json   ← SHA-256 hashes for each media file
├─ data/soft_deletes.json     ← records of soft-deleted entities
└─ media/                     ← actual binary files (images, PDFs, etc.)
```

### 2B. Manifest

```json
{
  "format": "mks.exchange",
  "schemaVersion": 7,
  "androidRoomSchema": 30,
  "includesMedia": true,
  "stableIdPolicy": "externalId required for workspace/book/quiz/question; numeric ids are local.",
  "softDeletePolicy": "deletedAt rows are preserved through soft_deletes.json when present.",
  "entries": [
    ...
  ],
  "counts": {
    "books": N,
    "quizzes": N,
    "questions": N,
    ...
  },
  "warnings": [
    ...
  ]
}
```

### 2C. Import Flow

```
ZIP file received
→ ZipLibraryParser.parse()
  ├─ Extract ZIP (zip4j, AES-256, password)
  ├─ Security validations (entry count, file size, path traversal)
  ├─ Read manifest.json
  │   ├─ format == "mks.exchange" && schemaVersion == 7?
  │   │   → MksExchangeV7Archive.readLegacyBundleFromDirectory()
  │   │     → Converts split v7 JSON files → LibraryBundleDto
  │   └─ Otherwise
  │       → Search for library.json / bundle.json / data.json
  │       → Parse via JsonLibraryParser
→ ImportValidator.validate(bundle)
→ BundleNormalizer.normalize(bundle)
→ ImportPreviewDto → ImportReviewDialog (merge strategy + insecure images)
→ Confirm → executeImportPipeline() (Room transaction)
```

### 2D. Export Flow

```
ExportManager.exportXxxToZip(outputStream)
→ Read Room entities via DAOs inside database.withTransaction
→ Map to DTOs via LibraryMapper (entity → DTO)
→ Resolve local paths for images via resolveBundleLocalPaths()
→ Build supplemental data (asset refs, question assets, source docs, annotations)
→ MksExchangeV7Archive.writeLegacyBundleToSchema7Zip(bundle, stream, supplemental)
  → Convert DTOs to v7 models, assign sequential IDs
  → buildMediaPayload() for each image (SHA-256 hashes)
  → Write split JSON files + manifest + media files
  → Create AES-256 encrypted ZIP via zip4j
```

### 2E. Export Scopes

| Method                      | Scope        | What It Includes                                                                                                                               |
|-----------------------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `exportQuizToZip(quizId)`   | Single quiz  | Book + quiz + questions + categories                                                                                                           |
| `exportBundleToZip(bookId)` | Single book  | Book + all quizzes + questions + sessions + flashcard decks + slideshow courses + note blueprints + prompt decks + study sessions + categories |
| `exportAllToZip()`          | Full library | All books, quizzes, questions, sessions, all knowledge bank assets, all categories                                                             |

### 2F. Export Entry Points

| Entry Point                     | Trigger                                    |
|---------------------------------|--------------------------------------------|
| Library FAB "Export"            | User taps FAB → "Export" mini-FAB          |
| Settings "Export Full Library"  | Settings screen → Library & Backup section |
| DataTools "Export full library" | DataTools screen → single button           |
| Book context menu "Export"      | 3-dot menu → "Export"                      |
| Quiz context menu "Export"      | 3-dot menu → "Export"                      |

### 2G. Supplemental Data

`MksExchangeV7SupplementalData` carries Room-native rows not in the legacy DTO bridge:

- `SupplementalAssetReference` (with `ownerExternalId`)
- `SupplementalQuestionAsset` (with `bookExternalId`, `quizExternalId`, `questionExternalId`)
- `SupplementalSourceDocument` (with `bookExternalId`)
- `SupplementalAnnotation` (with `bookExternalId`, `ownerExternalId`)

These are populated during export by `ExportManager.buildSupplementalData()` using DAO queries (
including `xxxIncludingDeleted` methods for soft-deleted entities).

### 2H. Round-Trip Integrity

- `externalId` is the stable identifier surviving export/import round-trips
- Numeric `id` values are local-only, reassigned during import
- Soft-deleted entities are preserved in `soft_deletes.json`
- Media files carry SHA-256 hashes for integrity verification
- Merge strategies (SKIP/OVERWRITE/DUPLICATE) handle existing entities

---

## 3. Manual Creation Paths

### 3A. Dialog Inventory

| Dialog                   | File                                                       | Used For                                                                                   |
|--------------------------|------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| `EntityEditDialog`       | `core/ui/.../components/EntityEditDialog.kt`               | Book, Flashcard Deck, Slideshow Course, Prompt Deck (title + description + optional image) |
| `CreateQuizDialog`       | `feature/ui/.../library/components/LibraryDialogs.kt`      | Quiz (title + source selection + filters)                                                  |
| `EditQuestionDialog`     | `feature/ui/.../scanner/ScannerScreen.kt`                  | Question (manual add or edit)                                                              |
| `ArticleCreateDialog`    | `feature/ui/.../booktools/BookToolScreens.kt`              | Note Blueprint (title + body + mode + from-marked/missed shortcuts)                        |
| `PromptDeckCreateDialog` | `feature/ui/.../booktools/BookKnowledgeDashboardScreen.kt` | Prompt Deck (title + description + template seeding checkbox)                              |
| `SourceDocumentDialog`   | `feature/ui/.../booktools/BookToolScreens.kt`              | Source Document (title + type + URL/path + details)                                        |
| `WorkspaceManagerDialog` | `feature/ui/.../workspace/WorkspaceManagerDialog.kt`       | Workspace (name + description)                                                             |

### 3B. Contextual FAB on Book Knowledge Dashboard

| Tab        | FAB Creates          | Dialog                                                          |
|------------|----------------------|-----------------------------------------------------------------|
| DASHBOARD  | No FAB               | —                                                               |
| QUIZZES    | New quiz             | `CreateQuizDialog`                                              |
| SLIDES     | New slideshow course | `EntityEditDialog` (title + description)                        |
| FLASHCARDS | New flashcard deck   | `EntityEditDialog` (name + description + image, allowBlankName) |
| NOTES      | New note/blueprint   | `ArticleCreateDialog` (title + body + mode)                     |
| PROMPTS    | New prompt deck      | `PromptDeckCreateDialog` (title + description + seed templates) |
| SOURCES    | New source document  | `SourceDocumentDialog` (title + type + URL/path)                |
| MISTAKES   | No FAB               | —                                                               |

### 3C. Entity Creation Details

#### Book Creation

- **Trigger:** Library FAB "New Book" or edit context menu on existing book
- **Fields:** Title (required), Description (optional), Cover Image (optional — image picker +
  URL/path)
- **Defaults:** `workspaceId` = current workspace, `externalId` = UUID, `description = ""`, all
  stats = 0, `isPinned = false`, `isSystem = false`
- **Validation:** Title must be non-blank
- **Post-creation:** HTTP cover images auto-downloaded, content URIs saved locally

#### Quiz Creation

Three distinct paths:

1. **From Book Dashboard** — `CreateQuizDialog`: title, description, cover image, mode (empty or
   from existing quizzes/categories), source quiz/category selection with filters (mistaken only,
   marked only, unanswered only)
2. **From Category** — "Make separate quiz": title, book selection. Auto-populated with all
   questions from that category.
3. **From Library** — same `CreateQuizDialog` with book context

**Defaults:** `externalId` = UUID, `description = ""`, `category = null`, `tags = emptyList()`, all
stats = 0

#### Question Creation (Manual Add)

- **Trigger:** FAB on `QuizQuestionsScreen`
- **Fields:** Question text (required), 4 initial option fields with correct-answer checkboxes, Add
  Option button, Explanation, Hint, Reference, Additional Info, Weight (default 1)
- **Auto-determination:** If multiple correct answers → `MULTIPLE_CHOICE`, else → `SINGLE_CHOICE`
- **Validation (QuestionValidator):** stem non-blank, ≥2 options, no blank options, ≥1 correct
  answer, correct indices in range, single choice ≤1 correct

#### Flashcard Deck Creation

- **Trigger:** FAB on FLASHCARDS tab
- **Fields:** Title (optional, defaults "Untitled deck"), Description (optional), Cover Image (
  optional)
- **Defaults:** `cardCount = 0`, `studiedCount = 0`, `masteryPercentage = 0f`, `isPinned = false`

#### Slideshow Course Creation

- **Trigger:** FAB on SLIDES tab
- **Fields:** Title (defaults "Untitled slideshow" if blank), Description (optional)
- **Defaults:** `slideCount = 0`, `progress = 0f`, `isDerived = false`, `sourceQuizId = null`

#### Note Blueprint Creation

- **Trigger:** FAB on NOTES tab
- **Fields:** Title (defaults "Untitled blueprint"), Body (optional, multi-line), Mode (dropdown:
  SIMPLE_NOTE, OUTLINE, CHECKLIST, ALGORITHM, DISEASE_TEMPLATE, DRUG_TEMPLATE, CONCEPT_TEMPLATE,
  MISTAKE_REVIEW, CUSTOM)
- **Action buttons inside dialog:** "From marked", "From missed" (derived creation shortcuts)
- **Defaults:** `bulletPoints = emptyList()`, `tags = emptyList()`, `blueprintMode = "SIMPLE_NOTE"`,
  `reviewStatus = "NEW"`

#### Prompt Deck Creation

- **Trigger:** FAB on PROMPTS tab
- **Fields:** Title (required), Description (optional), "Seed with best-in-class templates" checkbox
- **If seeded:** Creates template cards (Quiz generator, Flashcard generator, Blueprint maker,
  Explain & Teach)
- **Defaults:** `tags = emptyList()`, no stats fields

#### Source Document Creation

- **Trigger:** FAB on SOURCES tab
- **Fields:** Title (required), Type (dropdown: Book, Document, PDF, Image, Video, Audio,
  Tablesheet, Powerpoint, Others), URL or Path (with local file browser), Details (optional)
- **Auto-detection:** `SourceDocumentTypes.detectType(url)` infers type from file extension
- **Defaults:** `sourceType = "Others"`, all metadata fields null

#### Workspace Creation

- **Trigger:** Library top bar workspace manager → "Create Workspace"
- **Fields:** Name (required), Description (optional)
- **Defaults:** `externalId = UUID`, `isDefault = false`

---

## 4. Derived Creation (Magic Actions)

The Dashboard tab contains a **"Magic Actions"** section with dynamic chips that create Knowledge
Bank entities from existing data in one tap (no dialog, auto-generated title with timestamp suffix):

| Chip                               | Condition                | Creates         | Mode             |
|------------------------------------|--------------------------|-----------------|------------------|
| **Draft Note from Marked**         | `markedQuestions > 0`    | NoteBlueprint   | `MISTAKE_REVIEW` |
| **Cards from Marked**              | `markedQuestions > 0`    | FlashcardDeck   | —                |
| **Note from Mistakes**             | `openMistakes > 0`       | NoteBlueprint   | `MISTAKE_REVIEW` |
| **Cards from Mistakes**            | `openMistakes > 0`       | FlashcardDeck   | —                |
| **Note from Questions**            | `questions.isNotEmpty()` | NoteBlueprint   | `SIMPLE_NOTE`    |
| **Cards from Questions**           | `questions.isNotEmpty()` | FlashcardDeck   | —                |
| **Generate Slides from Questions** | `questions.isNotEmpty()` | SlideshowCourse | —                |

Titles use pattern `"Marked Cards - ${System.currentTimeMillis() % 10000}"`.

### Additional Derived Creation Paths

| Path                               | Trigger              | ViewModel Method                                                 | Creates                            |
|------------------------------------|----------------------|------------------------------------------------------------------|------------------------------------|
| **Blueprint from single question** | Question detail view | `createBlueprintFromQuestion(bookId, questionId, mode)`          | NoteBlueprint (`CONCEPT_TEMPLATE`) |
| **Flashcard deck from questions**  | Question selection   | `createFlashcardDeckFromQuestions(bookId, title, questionIds)`   | FlashcardDeck                      |
| **Slideshow from questions**       | Question selection   | `createSlideshowCourseFromQuestions(bookId, title, questionIds)` | SlideshowCourse                    |
| **Flashcards from blueprint**      | Blueprint view       | `createFlashcardsFromBlueprint(noteId)`                          | FlashcardDeck + cards              |
| **Prompt output → Note**           | AI prompt run        | `savePromptOutputAsNote(card, output, title)`                    | NoteBlueprint                      |
| **Prompt output → Blueprint**      | AI prompt run        | `savePromptOutputAsBlueprint(card, output, title)`               | NoteBlueprint                      |
| **Prompt output → Flashcards**     | AI prompt run        | `savePromptOutputAsFlashcards(card, output, title)`              | FlashcardDeck + cards              |
| **Prompt output → Quiz**           | AI prompt run        | `savePromptOutputAsQuiz(card, output, title)`                    | Quiz + questions                   |
| **Quiz from category**             | Category edit dialog | `createQuizFromCategory(category, title, bookId)`                | Quiz + questions                   |

---

## 5. Camera/OCR Scanner Path

**File:** `feature/ui/.../scanner/ScannerViewModel.kt`

- Uses Google ML Kit `TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)`
- Captures bitmap from camera, runs OCR to extract text
- Parses recognized text into `QuestionEntity` using regex patterns:
    - Question start: numbered patterns (e.g., `1.`, `Q1)`)
    - Options: lettered patterns (e.g., `A)`, `B)`), with marked correct answers (`*` prefix)
    - Falls back to paragraph-based splitting if no structured questions found
- Questions are editable via `EditQuestionDialog` before saving
- Saved via `knowledgeRepository.insertQuestions()`

---

## 6. Paste/Text Import for Knowledge Bank Sub-Assets

These are secondary import paths for populating existing containers (decks, courses, articles) from
pasted or selected text:

| Path                      | Trigger                                          | Parser                                                                                      | Produces                   |
|---------------------------|--------------------------------------------------|---------------------------------------------------------------------------------------------|----------------------------|
| **Flashcard text import** | `PasteTextDialog` in FlashcardDeckScreen         | `TextFlashcardParser` (3 modes: ALTERNATING_PARAGRAPHS, EXPLICIT_LABELS, HEADER_BODY_NOTES) | `FlashcardEntity` list     |
| **Slide text import**     | `PasteTextDialog` in SlideshowCourseScreen       | `TextSlideParser` (3 modes: ALTERNATING_PARAGRAPHS, EXPLICIT_LABELS, HEADER_BODY_NOTES)     | `CourseSlideEntity` list   |
| **Slide PPTX import**     | File picker in SlideshowCourseScreen             | `PptxSlideParser` (Apache POI XMLSlideShow)                                                 | `CourseSlideEntity` list   |
| **Article text import**   | `importArticlesFromText()` in BookToolsViewModel | `TextArticleParser` (2 modes: BASIC, EXPLICIT_LABELS)                                       | `NoteBlueprintEntity` list |

**TextParseMode details:**

- `ALTERNATING_PARAGRAPHS`: every other paragraph becomes the next item (front/back for flashcards,
  title/body for slides)
- `EXPLICIT_LABELS`: text with explicit `Front:`/`Back:`, `Title:`/`Body:` markers
- `HEADER_BODY_NOTES`: slides separated by `---` or `#`, with `>` prefixed notes

---

## 7. Database Seeding

**File:** `core/data/.../seeder/MksDatabaseSeeder.kt` (1264 lines)

**Trigger:** `WorkspaceRepository.getOrCreateDefaultWorkspace()` / `ensureWorkspaceSettings()` /
`resetDatabase()` → `seederProvider.get().seedDatabase(workspaceId)`

**Idempotency guard:** Checks
`if (books.any { it.externalId == "book_general_knowledge" }) return` — only seeds once per
workspace.

**What it seeds:**

- 1 sample Book ("General Knowledge & معلومات عامة")
- 4 Quizzes: English/Arabic "Sample quiz" (25 questions each), English/Arabic "How to start" (12
  tutorial questions each)
- 1 Flashcard Deck ("Basic Concepts Flashcards") with 2 sample cards
- 1 Slideshow Course ("Introduction to Science & History") with a welcome slide
- 1 Note Blueprint ("Study Note: Solar System") with bullet points

Several seeded questions include remote Unsplash image URLs.

---

## 8. Preferences Initialization (DataStore)

**File:** `core/data/.../preferences/DataStoreManager.kt`

All preferences have implicit defaults (no explicit initialization transaction):

| Preference               | Default                   | Notes                     |
|--------------------------|---------------------------|---------------------------|
| `showWelcomeOnStartup`   | `true`                    | Controls onboarding       |
| `language`               | `"en"`                    | Sanitized to "en" or "ar" |
| `themeMode`              | `"DAWN"`                  |                           |
| `fontScale`              | `1.0f`                    | Clamped 0.5-2.0           |
| `uiDensity`              | `1.0f`                    | Clamped 0.5-1.5           |
| `currentWorkspaceId`     | `null`                    | Active workspace          |
| `librarySortOption`      | `"TITLE"`                 |                           |
| `libraryViewMode`        | `"LIST"`                  |                           |
| `ollamaBaseUrl`          | `"http://10.0.2.2:11434"` | Emulator localhost        |
| `ollamaModelName`        | `"llama3"`                |                           |
| `doubleTapToSubmit`      | `true`                    |                           |
| `unansweredSkipEnabled`  | `true`                    |                           |
| `focusModeEnabled`       | `false`                   |                           |
| `eliminationModeEnabled` | `false`                   |                           |

`SettingsSanitizer` validates/clamps every preference on read.

---

## 9. Welcome/Onboarding Screen

**File:** `feature/ui/.../welcome/WelcomeScreen.kt`

- Does NOT create any database data
- Sets `showWelcomeOnStartup = false` on "Get Started"
- Provides EN/AR language toggle → `dataStoreManager.setLanguage(lang)`
- Start destination: `if (showWelcomeOnStartup) "welcome" else "library"`

---

## 10. Paths That Do NOT Exist

- **No deep links** (custom scheme or HTTPS)
- **No clipboard import** (clipboard only used for copying output)
- **No cloud sync / remote server sync** (no server-side data pull; `syncConfig` fields are for
  future use)
- **No push notifications or background data fetch**
- **No QR code scanning** (scanner uses camera for OCR text, not QR codes)
- **No ContentProvider** (app does not expose data to other apps)
- **No Google Play backup** (`android:allowBackup="false"`)

---

## 11. Complete Import Flow Diagram

```
EXTERNAL DATA SOURCE
│
├─ File (any format)
│  ├─ Library FAB "Import" → file picker → handleImportUri()
│  ├─ Book/Quiz context menu "Import" → file picker → handleImportUri()
│  ├─ Android Share Intent → MainActivity → LibraryScreen → handleImportUri()
│  │
│  └─ handleImportUri(uri)
│     ├─ XLSX ─→ CompilerViewModel.onFileSelected()
│     │   └─ prepareTempFile → WorkbookFactory → sheet selection
│     │   └─ header detection → column mapping → row parsing
│     │   ─→ CompilerDialog (preview/edit) → saveParsedQuestions()
│     │     ├─ Flashcard target → KnowledgeRepository.insertFlashcards()
│     │     └─ Quiz target → ImportLibraryManager.importQuestions()
│     │
│     ├─ CSV/TSV ─→ CompilerViewModel.onFileSelected()
│     │   └─ CsvParser → header detection → column mapping → row parsing
│     │   ─→ CompilerDialog → saveParsedQuestions() → same flow
│     │
│     ├─ JSON ─→ CompilerViewModel.onFileSelected()
│     │   └─ JsonQuestionParser.parse() → ParsedQuestion list
│     │   ─→ CompilerDialog → saveParsedQuestions() → same flow
│     │
│     ├─ HTML ─→ CompilerViewModel.onFileSelected()
│     │   └─ HtmlQuestionParser → embedded JSON extraction → JsonQuestionParser
│     │   ─→ CompilerDialog → saveParsedQuestions() → same flow
│     │
│     ├─ TEXT ─→ CompilerViewModel.onFileSelected()
│     │   └─ TextQuestionParser.parse() → ParsedQuestion list
│     │   ─→ CompilerDialog → saveParsedQuestions() → same flow
│     │
│     └─ ZIP ─→ ImportViewModel.getImportPreview()
│        └─ ZipLibraryParser → extract (AES-256) → security validation
│        │  ├─ Schema-7? → MksExchangeV7Archive.readLegacyBundleFromDirectory()
│        │  └─ Otherwise → JsonLibraryParser from library.json
│        ─→ ImportValidator → BundleNormalizer → ImportPreviewDto
│        ─→ ImportReviewDialog (merge strategy + insecure images)
│        ─→ ImportLibraryManager.importLibrary() → executeImportPipeline()
│
├─ PPTX (for slides)
│  └─ SlideshowCourseScreen file picker → PptxSlideParser → CourseSlideEntity list
│
├─ Pasted Text (for sub-assets)
│  ├─ FlashcardDeckScreen → PasteTextDialog → TextFlashcardParser → FlashcardEntity list
│  ├─ SlideshowCourseScreen → PasteTextDialog → TextSlideParser → CourseSlideEntity list
│  └─ BookToolsViewModel → importArticlesFromText → TextArticleParser → NoteBlueprintEntity list
│
├─ Camera/OCR
│  └─ ScannerScreen → ML Kit OCR → regex parsing → EditQuestionDialog → QuestionEntity
│
├─ Manual Creation (UI dialogs)
│  ├─ Library FAB "New Book" → EntityEditDialog → BookEntity
│  ├─ Dashboard FAB per tab → specific dialog → entity type
│  ├─ QuizQuestionsScreen FAB → EditQuestionDialog → QuestionEntity
│  └─ WorkspaceManager → WorkspaceManagerDialog → WorkspaceEntity
│
├─ Derived Creation (Magic Actions + one-click)
│  ├─ Dashboard "Draft Note from Marked" → NoteBlueprint (MISTAKE_REVIEW)
│  ├─ Dashboard "Cards from Marked" → FlashcardDeck
│  ├─ Dashboard "Note from Mistakes" → NoteBlueprint (MISTAKE_REVIEW)
│  ├─ Dashboard "Cards from Mistakes" → FlashcardDeck
│  ├─ Dashboard "Generate Slides" → SlideshowCourse
│  ├─ Prompt output routing → Note / Blueprint / FlashcardDeck / Quiz
│  └─ Category → Quiz creation
│
└─ Database Seeding (first launch)
   └─ WorkspaceRepository → MksDatabaseSeeder.seedDatabase(workspaceId)
      └─ 1 sample Book + 4 Quizzes + 1 Deck + 1 Course + 1 Blueprint

All import paths converge at:
┌─────────────────────────────────────────────┐
│ ImportLibraryManager.executeImportPipeline() │
│ (Room database transaction)                  │
│                                              │
│ 1. Validate → ImportValidator                │
│ 2. Normalize → BundleNormalizer              │
│ 3. Persist: workspace → categories → books   │
│    → quizzes → questions → sessions          │
│ 4. Image resolution per entity               │
│ 5. Return ImportResult                       │
└─────────────────────────────────────────────┘
```

---

## 12. Source File Inventory

### Core Import Engine (`core/data/.../importer/`)

- `detector/ImportFormatDetector.kt`
- `repository/ImportLibraryManager.kt`
- `model/ParsedQuestion.kt`, `ImportPreviewDto.kt`, `ImportResult.kt`
- `dto/LibraryBundleDto.kt`
- `mapping/LibraryMapper.kt`
- `normalization/BundleNormalizer.kt`
- `validation/ImportValidator.kt`
- `security/ImportLimits.kt`

### Parsers (`core/data/.../importer/parser/`)

- `CsvParser.kt`
- `GenericImageExtractor.kt`
- `HtmlQuestionParser.kt`
- `JsonLibraryParser.kt`
- `JsonQuestionParser.kt`
- `PptxSlideParser.kt`
- `SourceDetector.kt`
- `SpreadsheetHeaderMapper.kt`
- `SpreadsheetQuestionParser.kt`
- `TextArticleParser.kt`
- `TextFlashcardParser.kt`
- `TextQuestionParser.kt`
- `TextSlideParser.kt`
- `ZipLibraryParser.kt`

### XLSX Processing (`core/data/.../importer/xlsx/`)

- `XlsxImageResolver.kt`
- `XlsxLibraryCompiler.kt`
- `PoiInitializer.kt`

### Exchange Format V7 (`core/data/.../exchange/v7/`)

- `MksExchangeV7Archive.kt`
- `MksExchangeV7Models.kt`

### Repositories (`core/data/.../repository/`)

- `ExportManager.kt`
- `QuizRepository.kt`
- `KnowledgeRepository.kt`
- `BookRepository.kt`
- `WorkspaceRepository.kt`

### Seeding & Preferences

- `seeder/MksDatabaseSeeder.kt`
- `preferences/DataStoreManager.kt`
- `preferences/SettingsSanitizer.kt`

### UI Layer (`feature/ui/.../ui/`)

- `quiz/CompilerViewModel.kt`, `quiz/CompilerDialog.kt`
- `importer/ImportViewModel.kt`
- `library/LibraryScreen.kt`, `library/LibraryViewModel.kt`
- `library/components/LibraryFabMenu.kt`, `library/components/LibraryDialogs.kt`
- `data/DataToolsScreen.kt`, `data/DataToolsViewModel.kt`
- `booktools/BookKnowledgeDashboardScreen.kt`, `booktools/BookToolsViewModel.kt`
- `booktools/BookToolScreens.kt`
- `quiz/QuizQuestionsScreen.kt`, `quiz/QuizQuestionsViewModel.kt`
- `scanner/ScannerScreen.kt`, `scanner/ScannerViewModel.kt`
- `flashcard/FlashcardDeckScreen.kt`, `flashcard/FlashcardDeckViewModel.kt`
- `slideshow/SlideshowCourseScreen.kt`, `slideshow/SlideshowCourseViewModel.kt`
- `settings/SettingsScreen.kt`, `settings/SettingsViewModel.kt`
- `welcome/WelcomeScreen.kt`
- `workspace/WorkspaceManagerDialog.kt`
- `category/CategoryEditDialog.kt`

### App Module (entry point)

- `app/.../MainActivity.kt` (shared intent handler)

### Network

- `core/network/.../RemoteAssetFetcher.kt`
- `core/network/.../RemoteAssetPolicy.kt`
- `core/network/.../OllamaRepository.kt`

---

*Last updated: 2026-07-10*
