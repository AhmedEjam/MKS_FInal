# Android MKS: Master Architecture & Codebase Inspection Protocol

> **⚠️ REGENERATED:** 2026-07-10. **Part 4 added:** 2026-07-11. This document is the **authoritative current-state inspection** and **master protocol**, combining the 6-module architecture map, the v2.2 AI Review Protocol, the codebase chunks, and a concrete multi-phase review execution plan (Part 4).

---

## Part 1: Architecture & Project Map

### 1.1 Module Layout (6-Module Architecture)
```text
/Users/ahmedy.ajam/Android MKS/
├── app/                          # Main application module (UI entry point, Activities, Hilt DI)
├── core/
│   ├── database/                 # Room database module (v30, 26 entities, 29 migrations, 26 DAOs)
│   ├── data/                     # Domain repos, import pipeline, preferences, preview services
│   ├── model/                    # Shared domain models (entities, UI routes, search DTOs)
│   ├── network/                  # Network module (OllamaRepository, RemoteAssetFetcher)
│   └── ui/                       # Shared UI components (theme, tokens, common composables)
└── feature/
    └── ui/                       # Feature UI screens (library, quiz, flashcard, dashboard, etc.)
```

### 1.2 Domain Repositories (The Source of Truth)
All repositories are injected via `@Inject constructor` and manage the boundary between DAOs and remote networks.
| Repository | Location | Responsibility | Test Status |
|---|---|---|---|
| `BookRepository` | `core/data` | Book CRUD, cover images, stats, study bundles | 🔴 No tests |
| `QuizRepository` | `core/data` | Quiz/Question CRUD, answers, scoring, categories | 🔴 No tests |
| `KnowledgeRepository` | `core/data` | Flashcards, slideshows, notes, prompts, learning sessions | 🔴 No tests |
| `StudyRepository` | `core/data` | Quiz sessions, knowledge study sessions, mistake logs | 🔴 No tests |
| `WorkspaceRepository` | `core/data` | Workspace CRUD, default workspace, soft deletes | 🔴 No tests |
| `AssetRepository` | `core/data` | Asset references, question assets, source documents | 🔴 No tests |

> **Note:** Circular Dependencies between these repositories are broken via `javax.inject.Provider<T>`.

### 1.3 Database Schema (Room v30)
**26 Core Entities across 4 Tiers:**
- **Workspace Tier (2):** `WorkspaceEntity`, `WorkspaceSettingsEntity`
- **Quiz/Library Tier (7):** `BookEntity`, `QuizEntity`, `QuestionEntity`, `SessionEntity`, `CategoryMetadataEntity`, `QuestionCategoryEntity`, `QuestionAssetEntity`
- **Knowledge Bank Tier (10):** `FlashcardDeckEntity`, `FlashcardEntity`, `LearningSessionEntity`, `SlideshowCourseEntity`, `CourseSlideEntity`, `NoteBlueprintEntity`, `NoteCollectionEntity`, `PromptDeckEntity`, `PromptCardEntity`, `PromptRunEntity`
- **Study Tracking & Assets (7):** `KnowledgeStudySessionEntity`, `StudySessionEntity`, `AssetReferenceEntity`, `SourceDocumentEntity`, `MistakeLogEntryEntity`, `AnnotationEntity`

**Migration Status (v1 → v30):**
- v15 to v30 are explicitly covered by automated Android Instrumentation Tests (`MigrationXXToYYTest`).
- *Action Required:* Create one full `MigrateAll1To30Test` chain test.

### 1.4 Hilt Dependency Injection
All modules are located in `app/di/`:
- **`HiltDataModule`**: `MksDatabase`, `FileManager`, `ExportManager`, `ImportLibraryManager`, `DataStoreManager`, `FocusManager`.
- **`HiltDaoModule`**: 6 Core Quiz DAOs.
- **`HiltKnowledgeDaoModule`**: 10 Knowledge Bank DAOs.
- **`HiltUtilityDaoModule`**: 10 Utility DAOs.
- **`HiltRepositoryModule`**: `OllamaRepository`, `GlobalSearchRepository`, `ReviewRepository`.
- **`HiltServiceModule`**: Preview & Audit Services (`DeletePreviewService`, etc.).

### 1.5 The Import Pipeline Engine
An advanced, multi-format parsing engine converting external files into domain DTOs.
```text
User selects file (URI)
  ↓
ImportLibraryManager.orchestrateImport()
  ├─ ImportFormatDetector.detectFormat() (extension → MIME → magic bytes → heuristic)
  │
  ├─ Format-Specific Parsers (12 total):
  │  ├─ XLSX → XlsxLibraryCompiler.compile() (Apache POI)
  │  ├─ CSV/TSV → CsvParser.parse()
  │  ├─ JSON → JsonLibraryParser, JsonQuestionParser
  │  ├─ HTML → HtmlQuestionParser
  │  ├─ TEXT → TextQuestionParser, TextFlashcardParser, TextSlideParser, TextArticleParser
  │  ├─ PPTX → PptxSlideParser
  │  └─ ZIP → ZipLibraryParser (MKS bundle format)
  │
  ├─ BundleNormalizer.normalize() (answer mode inference, trimming)
  ├─ ImportValidator.validate() (pre-persist security validation)
  └─ Domain repos insert → Database
```


---

## Part 2: Autonomous AI Code & App Review Protocol (v2.2)

## 1. System Objective
> **Total Volume:** ~0 lines of code.

To systematically analyze the Android MKS application utilizing multi-dimensional contexts, surfacing both structural (Developer) and experiential (User) defects, and documenting a re-evaluated codebase vision in a permanent, indexed ledger.

## 2. Dual Perspective Engine
> **Total Volume:** ~0 lines of code.

The AI must evaluate all code through two distinct lenses simultaneously:

### ⚙️ The Developer Perspective (Structural Integrity)
*   **Architecture & Boundaries:** Does the code violate layered architecture? (e.g., UI writing directly to DB).
*   **Code Health:** Are we adhering to DRY principles? Can tiny files be merged? Is there dead code?
*   **Testability:** Is the logic decoupled enough to be easily unit-tested?
*   **Performance:** Are heavy operations blocking the main thread? Are Jetpack Compose redraws minimized?
*   **Security:** Are we sanitizing inputs and safely storing sensitive data?

### 👤 The User Perspective (Experiential Integrity)
*   **Usability & Polish:** Are empty states, loading spinners, and error messages clear? Does the UI scale smoothly across different device sizes?
*   **Data Integrity:** When the user clicks "Save", is the data *actually* persisted exactly as intended without silent drops?
*   **Resilience:** How does the app react to unpredictable user behavior (rapid clicking, offline mode, backgrounding the app)?

---

## 3. Execution Pipeline & Tool Integration
> **Total Volume:** ~0 lines of code.


### Phase 1: Static Chunk Analysis (Subroutine: Isolate & Inspect)
*   **Input:** A defined "Chunk" of 10-20 highly related files from `codebase_review_chunks.md`.
*   **Process:**
    1.  Parse syntax for null-safety (`!!` vs `?`) and missing `try/catch` blocks.
    2.  Scan Jetpack Compose files for missing `remember` blocks and heavy inline computations.
    3.  Flag duplicated logic for refactoring into reusable helper functions.
    4.  Identify unused variables, imports, and layouts.
*   **Tool Execution:**
    *   Execute `./gradlew detekt` or `./gradlew ktlintCheck` using `run_command` to programmatically find styling and code smell violations.
    *   Use `grep_search` to trace if functions within the chunk are completely unused throughout the rest of the project.
*   **Output:** Immediate Refactoring PR / Code Health Report.

### Phase 2: Vertical Dataflow Tracing (Subroutine: Pipeline Trace)
*   **Input:** A specific User Action (e.g., "User taps 'Export Library'").
*   **Process:**
    1.  **UI Layer:** Locate the button click event and the intent sent to the ViewModel.
    2.  **Domain Layer:** Trace the data transformation through the ViewModel and UseCases/Repositories.
    3.  **Data Layer:** Verify the exact SQL query or File I/O operation executed.
    4.  **Reconciliation:** Compare the data sent by the UI against the data actually saved/exported. Flag any "Middleman Drops" where data is unintentionally filtered or lost.
*   **Tool Execution:**
    *   For configuration chunks (e.g., Chunk 1), run `./gradlew dependencies` to map compile-time pipelines and verify dependency constraints.
    *   Use `grep_search` to map how entities move from Room DAOs to ViewModel properties.
*   **Output:** Dataflow map and identified pipeline gaps.

### Phase 3: Chaos Simulation & Brainstorming (Subroutine: Break It)
*   **Input:** The completed dataflow from Phase 2.
*   **Process:**
    1.  **Adversarial Generation:** Actively brainstorm ways to break the flow.
        *   *Temporal:* What if the user double-taps the submit button?
        *   *Network:* What if the connection drops exactly midway through the API call?
        *   *Data:* What if the user imports a 500MB Excel file? What if an integer field receives a string?
    2.  **State Corruption:** Mentally simulate app suspension/destruction by the OS mid-task.
*   **Tool Execution:**
    *   Draft a temporary scratch test or write unit tests to verify the adversarial scenarios using `write_to_file`.
    *   Run `./gradlew test` to execute the unit tests and verify code resilience.
*   **Output:** A list of critical **Edge Cases** and concrete recommendations for Unit/UI tests to prevent them.

---

## 4. The Code Inspection Ledger (`code_inspection_ledger.md`)
> **Total Volume:** ~0 lines of code.


To guarantee long-term maintainability, the AI must record every audited file, function, and configuration block in a separate, permanent, structured ledger: **[code_inspection_ledger.md](file:///Users/ahmedy.ajam/Android%20MKS/Important%20docs/code_inspection_ledger.md)**.

Rather than copying code blindly, the AI must rebuild and document the "ideal vision" of the function from scratch.

### Ledger Entry Schema
For each inspected codebase segment, the AI generates a structured entry:

```markdown
### [MKS-REV-XXX] Feature / Function Name
- **File Path:** [filename](file:///absolute/path/to/file)
- **Inspected At:** YYYY-MM-DD THH:MM:SSZ
- **Understood End Goal:** A plain-english description of what this function does and why it exists.
- **Failures & Edge Cases:** List of potential temporal, data, network, and system crashes or silent bugs.
- **End-User Usability Improvements:** Impact of improvements on UX (spinners, toasts, inputs, sizes).
- **The Re-Evaluated Code Vision (Scratch Implementation):**
  ```kotlin
  // The rewritten, robust, fully-optimized function/class containing zero static and flow bugs
  ```
- **Cross-Chunk Dependency Mapping (Follow-up Actions):**
  List of subsequent lines or files elsewhere in the project (regardless of chunk boundaries) that must be updated to wire this new function in:
  - [ ] Update DI registration in [HiltModule](file://...) at line XX.
  - [ ] Update ViewModel injection call in [ViewModel](file://...) at line YY.
  - [ ] Update Compose view handler in [Screen](file://...) at line ZZ.
```

### Special Code Indexing Method
Entries use the `MKS-REV-XXX` index. Sub-routines or newly added functions can be inserted between reviews using a dot notation (e.g., `MKS-REV-001.1` for a Hilt module update supporting `MKS-REV-001`), ensuring that all subsequent wires are tracked linearly.

---

## 5. Verification & Validation Loop (Compile & Test Check)
> **Total Volume:** ~0 lines of code.


Whenever a potential bug or optimization is identified during any phase, the AI must follow this loop before reporting to the user:
```
      [Identify Defect]
              ↓
   [Draft Test/Scratch Script]
              ↓
     [Run Compiler/Tests] (Verify Failure)
              ↓
        [Refactor Code]
              ↓
  [Re-run Compiler/Tests] (Verify Success)
              ↓
     [Report to User/Merge]
```
*   **Compile Step:** Ensure the project compiles cleanly using `./gradlew compileDebugKotlin` or `./gradlew compileDebugSources`.
*   **Test Step:** Run `./gradlew test` (or specific target tests) to ensure zero regressions.

---


---

---

## Part 3: Codebase Review Chunks

I have analyzed the project structure and grouped the source files (`.kt`, `.kts`, `.xml`) into logical chunks of 10-20 most related files each. These are organized by feature or layer.
 and grouped the source files (`.kt`, `.kts`, `.xml`) into logical chunks of 10-20 most related files each. These are organized by feature or layer.

> **Total Project Volume (Analyzed Chunks):** ~52866 lines of code.


Please let me know which chunk you would like to start reviewing, or if you'd like to adjust the grouping!

## 1. Build & Project Configuration
> **Context:** Orchestrates dependency management, build flavors, SDK targeting, and modularization for the entire project.
> **Total Volume:** ~511 lines of code.

- `build.gradle.kts` *(~34 lines)*
- `settings.gradle.kts` *(~35 lines)*
- `app/build.gradle.kts` *(~134 lines)*
- `core/data/build.gradle.kts` *(~48 lines)*
- `core/database/build.gradle.kts` *(~44 lines)*
- `core/model/build.gradle.kts` *(~29 lines)*
- `core/network/build.gradle.kts` *(~33 lines)*
- `core/ui/build.gradle.kts` *(~52 lines)*
- `feature/ui/build.gradle.kts` *(~72 lines)*
- `test_zip.kts` *(~30 lines)*

## 2. App Entry Point, DI, & Main Config
> **Context:** The core skeleton of the app, including Android manifest configurations, the main activity, and Hilt dependency injection modules (v30 architecture).
> **Total Volume:** ~609 lines of code.

- `app/src/main/AndroidManifest.xml` *(~53 lines)*
- `app/src/main/java/com/ahmedyejam/mks/MainActivity.kt` *(~123 lines)*
- `app/src/main/java/com/ahmedyejam/mks/MksApplication.kt` *(~51 lines)*
- `app/src/main/res/xml/network_security_config.xml` *(~9 lines)*
- `app/src/main/res/xml/backup_rules.xml` *(~11 lines)*
- `app/src/main/res/xml/data_extraction_rules.xml` *(~21 lines)*
- `app/src/main/java/com/ahmedyejam/mks/di/HiltDaoModule.kt` *(~54 lines)*
- `app/src/main/java/com/ahmedyejam/mks/di/HiltDataModule.kt` *(~95 lines)*
- `app/src/main/java/com/ahmedyejam/mks/di/HiltKnowledgeDaoModule.kt` *(~82 lines)*
- `app/src/main/java/com/ahmedyejam/mks/di/HiltRepositoryModule.kt` *(~19 lines)*
- `app/src/main/java/com/ahmedyejam/mks/di/HiltServiceModule.kt` *(~9 lines)*
- `app/src/main/java/com/ahmedyejam/mks/di/HiltUtilityDaoModule.kt` *(~82 lines)*

## 3. Core Models: Database Entities (Part 1)
> **Context:** Room database schemas defining the foundational data structures (Books, Notes, Flashcards).
> **Total Volume:** ~587 lines of code.

- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/AnnotationEntity.kt` *(~68 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/AssetReferenceEntity.kt` *(~24 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/BookEntity.kt` *(~48 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/CategoryMetadataEntity.kt` *(~19 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/CourseSlideEntity.kt` *(~59 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/FlashcardDeckEntity.kt` *(~48 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/FlashcardEntity.kt` *(~51 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/KnowledgeStudySessionEntity.kt` *(~22 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/LearningSessionEntity.kt` *(~35 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/MistakeLogEntryEntity.kt` *(~49 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/NoteBlueprintEntity.kt` *(~70 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/NoteCollectionEntity.kt` *(~47 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/PromptCardEntity.kt` *(~47 lines)*

## 4. Core Models: Database Entities (Part 2)
> **Context:** Additional Room entities for Quizzes, Prompts, Sessions, and Workspace management.
> **Total Volume:** ~621 lines of code.

- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/PromptDeckEntity.kt` *(~35 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/PromptEntity.kt` *(~35 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/PromptRunEntity.kt` *(~35 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/QuestionAssetEntity.kt` *(~68 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/QuestionCategoryEntity.kt` *(~28 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/QuestionEntity.kt` *(~80 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/QuizEntity.kt` *(~52 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/SessionEntity.kt` *(~71 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/SlideshowCourseEntity.kt` *(~45 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/SourceDocumentEntity.kt` *(~79 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/StudySessionEntity.kt` *(~36 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/WorkspaceEntity.kt` *(~23 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/local/entity/WorkspaceSettingsEntity.kt` *(~34 lines)*

## 5. Core Models: General Data Models
> **Context:** Non-persisted domain models, UI state configurations, search outputs, and DTOs used across the application.
> **Total Volume:** ~978 lines of code.

- `core/model/src/main/java/com/ahmedyejam/mks/data/model/AiProviderConfig.kt` *(~182 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/ArticleGenerationConfig.kt` *(~15 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/CategoryWithMetadata.kt` *(~16 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/ExportResult.kt` *(~14 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/FlashcardGenerationConfig.kt` *(~20 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/LearningSessionState.kt` *(~53 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/McqPrompts.kt` *(~230 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/MksResult.kt` *(~40 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/OllamaModels.kt` *(~25 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/ParsedMcq.kt` *(~153 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/model/SlideGenerationConfig.kt` *(~23 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/search/GlobalSearchModels.kt` *(~20 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/data/simulation/ChangeSimulationModels.kt` *(~28 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/ui/MksRoutes.kt` *(~61 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/util/BoundedStreams.kt` *(~53 lines)*
- `core/model/src/main/java/com/ahmedyejam/mks/util/MksLogger.kt` *(~45 lines)*

## 6. Database: DAOs (Part 1)
> **Context:** Data Access Objects containing SQL queries for CRUD operations on core entities like Books, Notes, and Categories.
> **Total Volume:** ~778 lines of code.

- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/AnnotationDao.kt` *(~88 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/AssetReferenceDao.kt` *(~50 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/BookDao.kt` *(~98 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/CategoryMetadataDao.kt` *(~26 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/CourseSlideDao.kt` *(~62 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/FlashcardDao.kt` *(~87 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/FlashcardDeckDao.kt` *(~45 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/GlobalSearchDao.kt` *(~68 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/KnowledgeStudySessionDao.kt` *(~53 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/LearningSessionDao.kt` *(~27 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/MistakeLogDao.kt` *(~67 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/NoteBlueprintDao.kt` *(~77 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/NoteCollectionDao.kt` *(~30 lines)*

## 7. Database: DAOs (Part 2)
> **Context:** DAOs handling complex relational queries for Prompts, Quizzes, Sessions, and Assets.
> **Total Volume:** ~1040 lines of code.

- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/PromptCardDao.kt` *(~52 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/PromptDao.kt` *(~32 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/PromptDeckDao.kt` *(~49 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/PromptRunDao.kt` *(~49 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/QuestionAssetDao.kt` *(~91 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/QuestionCategoryDao.kt` *(~87 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/QuestionDao.kt` *(~323 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/QuizDao.kt` *(~146 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/SessionDao.kt` *(~45 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/SlideshowCourseDao.kt` *(~38 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/SourceDocumentDao.kt` *(~46 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/StudySessionDao.kt` *(~30 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/dao/WorkspaceDao.kt` *(~52 lines)*

## 8. Database: Core Config & Network Layer
> **Context:** Database migration paths, type converters, local DB setup, alongside remote API clients (Ollama, OCR, PDF extraction).
> **Total Volume:** ~2600 lines of code.

- `core/database/src/main/java/com/ahmedyejam/mks/data/local/Converters.kt` *(~292 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksDatabase.kt` *(~149 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/MksMigrations.kt` *(~1040 lines)*
- `core/database/src/main/java/com/ahmedyejam/mks/data/local/WorkspaceDefaults.kt` *(~7 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/AiClient.kt` *(~274 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/McqService.kt` *(~247 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/OcrService.kt` *(~120 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/PdfRendererService.kt` *(~73 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/PdfTextExtractor.kt` *(~50 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/RemoteAssetFetcher.kt` *(~66 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/network/RemoteAssetPolicy.kt` *(~23 lines)*
- `core/network/src/main/java/com/ahmedyejam/mks/data/repository/OllamaRepository.kt` *(~259 lines)*

## 9. Data Layer: Importer Parsers
> **Context:** The multi-format parsing engine converting XLSX, CSV, JSON, HTML, PPTX, and text files into standard DTOs.
> **Total Volume:** ~2051 lines of code.

- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/CsvParser.kt` *(~194 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/GenericImageExtractor.kt` *(~80 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/HtmlQuestionParser.kt` *(~30 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/JsonLibraryParser.kt` *(~235 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/JsonQuestionParser.kt` *(~204 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/PptxSlideParser.kt` *(~81 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/SourceDetector.kt` *(~55 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/SpreadsheetHeaderMapper.kt` *(~192 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/SpreadsheetQuestionParser.kt` *(~223 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/TextArticleParser.kt` *(~101 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/TextFlashcardParser.kt` *(~115 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/TextQuestionParser.kt` *(~219 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/TextSlideParser.kt` *(~144 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/parser/ZipLibraryParser.kt` *(~178 lines)*

## 10. Data Layer: Importer Logic & Excel Handling
> **Context:** Business logic for format detection, library normalization, Apache POI integration for Excel, and security validation.
> **Total Volume:** ~3585 lines of code.

- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/detector/ImportFormatDetector.kt` *(~126 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/dto/LibraryBundleDto.kt` *(~401 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/mapping/LibraryMapper.kt` *(~693 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/model/ImportPreviewDto.kt` *(~21 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/model/ImportResult.kt` *(~58 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/model/ParsedQuestion.kt` *(~78 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/normalization/BundleNormalizer.kt` *(~34 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/repository/ImportLibraryManager.kt` *(~1390 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/security/ImportLimits.kt` *(~20 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/validation/ImportValidator.kt` *(~131 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/xlsx/PoiInitializer.kt` *(~25 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/xlsx/XlsxImageResolver.kt` *(~365 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/importer/xlsx/XlsxLibraryCompiler.kt` *(~243 lines)*

## 11. Data Layer: Repositories
> **Context:** The 6 core domain repositories acting as the single source of truth for ViewModels, orchestrating data between DAOs and Network.
> **Total Volume:** ~4893 lines of code.

- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/AiMcqRepository.kt` *(~158 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/AssetRepository.kt` *(~638 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/BookRepository.kt` *(~373 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/ExportManager.kt` *(~586 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/KnowledgeRepository.kt` *(~1673 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/MksRepositoryModels.kt` *(~87 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/QuizRepository.kt` *(~696 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/StudyRepository.kt` *(~289 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repository/WorkspaceRepository.kt` *(~185 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/review/ReviewModels.kt` *(~22 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/review/ReviewRepository.kt` *(~127 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/search/GlobalSearchRepository.kt` *(~16 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/search/GlobalSearchResult.kt` *(~43 lines)*

## 12. Data Layer: Utilities, Settings, Handlers
> **Context:** Cross-cutting concerns including DataStore preferences, File I/O, error handling, preview services, and database seeding.
> **Total Volume:** ~4397 lines of code.

- `core/data/src/main/java/com/ahmedyejam/mks/data/error/GlobalErrorHandler.kt` *(~30 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/exchange/v7/MksExchangeV7Archive.kt` *(~1266 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/exchange/v7/MksExchangeV7Models.kt` *(~542 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/focus/FocusManager.kt` *(~53 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/local/FileManager.kt` *(~435 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/model/FileManagerModels.kt` *(~8 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/preferences/DataStoreManager.kt` *(~437 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/preferences/SettingsSanitizer.kt` *(~41 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/preferences/ThemeMode.kt` *(~25 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/preview/CategoryMergePreviewService.kt` *(~39 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/preview/ClearMarksPreviewService.kt` *(~22 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/preview/DeletePreviewService.kt` *(~49 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/repair/AssetReferenceAuditService.kt` *(~71 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/seeder/MksDatabaseSeeder.kt` *(~1273 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/validation/QuestionValidator.kt` *(~58 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/data/validation/SessionStateValidator.kt` *(~39 lines)*
- `core/data/src/main/java/com/ahmedyejam/mks/di/ApplicationScope.kt` *(~9 lines)*

## 13. UI Core: Theme, Components & Nav
> **Context:** Jetpack Compose design system (colors, typography), reusable UI widgets, and global navigation routing paths.
> **Total Volume:** ~1490 lines of code.

- `core/ui/src/main/java/com/ahmedyejam/mks/ui/common/InvalidRouteScreen.kt` *(~52 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/components/ChangePreviewDialog.kt` *(~99 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/components/EntityEditDialog.kt` *(~125 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/components/MksReusableComponents.kt` *(~66 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/components/StudyTopAppBar.kt` *(~58 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/theme/Color.kt` *(~80 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/theme/MksDesignTokens.kt` *(~72 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/theme/Theme.kt` *(~183 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/theme/Type.kt` *(~34 lines)*
- `core/ui/src/main/java/com/ahmedyejam/mks/ui/utils/TtsManager.kt` *(~47 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/MksNavHost.kt` *(~627 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/navigation/MksRouteBuilders.kt` *(~23 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/navigation/MksRouteValidator.kt` *(~24 lines)*

## 14. UI Features: Book Tools
> **Context:** Screens and ViewModels for AI MCQ generation, PDF text extraction, and book-specific knowledge dashboards.
> **Total Volume:** ~5637 lines of code.

- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/AiMcqGeneratorScreen.kt` *(~526 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/AiMcqGeneratorViewModel.kt` *(~177 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookDashboardTabs.kt` *(~340 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookKnowledgeDashboardScreen.kt` *(~831 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookToolScreens.kt` *(~1946 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookToolsViewModel.kt` *(~1124 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/PdfExtractionScreen.kt` *(~374 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/PdfExtractionViewModel.kt` *(~319 lines)*

## 15. UI Features: Categories, Data Tools & Flashcards
> **Context:** Presentation layer for category management, data export/import workflows, and the flashcard study player.
> **Total Volume:** ~4099 lines of code.

- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/category/CategoryComponents.kt` *(~181 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/category/CategoryEditDialog.kt` *(~501 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/category/CategoryQuestionsScreen.kt` *(~871 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/category/CategoryQuestionsViewModel.kt` *(~358 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/category/QuestionAssetsDialog.kt` *(~593 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/data/DataToolsScreen.kt` *(~87 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/data/DataToolsViewModel.kt` *(~42 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/flashcard/FlashcardDeckScreen.kt` *(~957 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/flashcard/FlashcardDeckViewModel.kt` *(~509 lines)*

## 16. UI Features: Library & Imports
> **Context:** The main entry hub of the app displaying books, quizzes, fab menus, sorting dialogs, and import coordination.
> **Total Volume:** ~3881 lines of code.

- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/importer/ImportViewModel.kt` *(~101 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/LibraryScreen.kt` *(~651 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/LibraryViewModel.kt` *(~737 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/components/LibraryComponents.kt` *(~1265 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/components/LibraryContentGrid.kt` *(~302 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/components/LibraryDialogs.kt` *(~443 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/components/LibraryFabMenu.kt` *(~118 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/components/LibraryTopBar.kt` *(~190 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/library/components/SortDialog.kt` *(~74 lines)*

## 17. UI Features: Quiz Player
> **Context:** The interactive quiz playing experience, including question navigation, option selection, compiler dialogs, and zoomable images.
> **Total Volume:** ~5214 lines of code.

- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/CompilerDialog.kt` *(~623 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/CompilerViewModel.kt` *(~611 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/QuizDetailTabsScreen.kt` *(~123 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/QuizPlayerScreen.kt` *(~1444 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/QuizQuestionsScreen.kt` *(~529 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/QuizQuestionsViewModel.kt` *(~348 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/QuizViewModel.kt` *(~1436 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/ZoomableImageDialog.kt` *(~100 lines)*

## 18. UI Features: Scanners, Search, Reviews & Sessions
> **Context:** Presentation logic for OCR camera scanning, global full-text search, the unified review queue, and active study sessions.
> **Total Volume:** ~2358 lines of code.

- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/review/ReviewDashboardScreen.kt` *(~518 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/review/ReviewDashboardViewModel.kt` *(~131 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerScreen.kt` *(~467 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerViewModel.kt` *(~182 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/search/GlobalSearchScreen.kt` *(~96 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/search/GlobalSearchViewModel.kt` *(~55 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/session/SessionManagementScreen.kt` *(~778 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/session/SessionViewModel.kt` *(~131 lines)*

## 19. UI Features: Misc (Settings, Workspace, Welcome, Trash, Slideshow)
> **Context:** Application settings, workspace switching, onboarding screens, soft-delete recovery, and slideshow presentation.
> **Total Volume:** ~4377 lines of code.

- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/ProviderConfigDialog.kt` *(~272 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/SettingsScreen.kt` *(~557 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/SettingsViewModel.kt` *(~141 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/slideshow/SlideshowCourseScreen.kt` *(~1008 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/slideshow/SlideshowCourseViewModel.kt` *(~584 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/summary/SummaryScreen.kt` *(~590 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/summary/SummaryViewModel.kt` *(~260 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/trash/TrashBinDialog.kt` *(~370 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/welcome/WelcomeScreen.kt` *(~282 lines)*
- `feature/ui/src/main/java/com/ahmedyejam/mks/ui/workspace/WorkspaceManagerDialog.kt` *(~313 lines)*

## 20. Unit Tests
> **Context:** Automated tests isolating parsers, validators, file I/O, and data mappers to prevent regressions.
> **Total Volume:** ~1573 lines of code.

- `app/src/test/java/com/ahmedyejam/mks/ExampleUnitTest.kt` *(~16 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/GenericImageExtractorTest.kt` *(~189 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/ParserBugFixTest.kt` *(~144 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/SourceDetectorTest.kt` *(~148 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/SpreadsheetHeaderMapperTest.kt` *(~90 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/TextArticleParserTest.kt` *(~178 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/TextFlashcardParserTest.kt` *(~183 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/parser/TextSlideParserTest.kt` *(~221 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/security/BoundedStreamsTest.kt` *(~29 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/importer/validation/ImportValidatorTest.kt` *(~91 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/local/FileManagerTest.kt` *(~100 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/preferences/SettingsSanitizerTest.kt` *(~42 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/validation/QuestionValidatorTest.kt` *(~65 lines)*
- `app/src/test/java/com/ahmedyejam/mks/data/validation/SessionStateValidatorTest.kt` *(~53 lines)*
- `app/src/test/java/com/ahmedyejam/mks/ui/navigation/MksRouteBuildersTest.kt` *(~24 lines)*

## 21. Android Instrumentation Tests
> **Context:** On-device tests validating Room database schema migrations (v15->v30) and complex integration workflows.
> **Total Volume:** ~1587 lines of code.

- `app/src/androidTest/java/com/ahmedyejam/mks/ExampleInstrumentedTest.kt` *(~22 lines)*
- `app/src/androidTest/java/com/ahmedyejam/mks/data/importer/ImportReconciliationTest.kt` *(~173 lines)*
- `app/src/androidTest/java/com/ahmedyejam/mks/data/importer/ImportTargetQuizTest.kt` *(~101 lines)*
- `app/src/androidTest/java/com/ahmedyejam/mks/data/importer/XlsxImportTest.kt` *(~92 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration15To16Test.kt` *(~245 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration16To17Test.kt` *(~85 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration22To23Test.kt` *(~64 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration23To24Test.kt` *(~69 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration24To25Test.kt` *(~69 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration25To26Test.kt` *(~60 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration26To27Test.kt` *(~68 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration27To28Test.kt` *(~42 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration28To29Test.kt` *(~57 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/Migration29To30Test.kt` *(~40 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/dao/AnnotationDaoTest.kt` *(~136 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/dao/AssetReferenceDaoTest.kt` *(~47 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/dao/QuestionCategoryDaoTest.kt` *(~61 lines)*
- `core/database/src/androidTest/java/com/ahmedyejam/mks/data/local/dao/QuestionDaoTest.kt` *(~156 lines)*

---

## Part 4: Recommended Multi-Phase Review Execution Plan

> **Added:** 2026-07-11. This section operationalizes Part 2's dual-perspective protocol into a concrete, bounded execution plan. It maps each phase to the file chunks in Part 3, specifies exactly which files to read (to conserve attention budget), and defines the output artifacts that accumulate across phases. Each phase traces data from origin → transforms → final destination through both the end-user and senior-developer lenses, and concludes with actionable recommendations for improvements, new features, functions to maturize, and refactor opportunities.
>
> **Validation directive:** Before executing, the AI agent should review this plan against the current codebase state (AGENTS.md + file tree) and validate that it is the best approach. If the agent identifies gaps or better groupings, it should propose adjustments and wait for user approval before proceeding.

### Plan Principles

1. **Bounded reads per phase** — each phase names the exact files to read. Do not explore beyond the list. This prevents context exhaustion.
2. **Artifacts accumulate** — each phase writes a markdown file to `review/`. Subsequent phases read prior artifacts (cheap) instead of re-reading source code.
3. **No search in `build/`** — build output wastes context. Do not grep or glob into `build/`.
4. **Dual lens per phase** — every phase produces both an End-User Lens and a Senior-Developer Lens analysis.
5. **Recommendations per phase** — every phase produces a Recommendations section (improvements, features to add, functions to maturize, refactor opportunities).
6. **Single session** — run all phases in the same Claude Code session so prior artifacts remain in context.

### Chunk Cross-Reference Map

| Phase | Part 3 Chunks | Primary Focus |
|---|---|---|
| Phase 1 — Import Pipeline | 9, 10, parts of 12 (FileManager), 16 (ImportViewModel), 17 (CompilerDialog/CompilerViewModel) | File → ParsedQuestion → Room |
| Phase 2 — Quiz & Knowledge Bank Study | 14 (Book Tools), 15 (Flashcards), 17 (Quiz Player), 19 (Slideshow/Summary), parts of 11 (Quiz/Study/Knowledge repos) | User interaction → Session/Progress entities |
| Phase 3 — AI, OCR, PDF, Export & Infra | 8 (Network), parts of 11 (ExportManager/AiMcqRepository), 12 (exchange, preferences, seeder), parts of 14 (AiMcq/PdfExtraction), app/service/ | Network calls, workers, export round-trip, preferences |
| Phase 4 — Synthesis | Reads Phase 1–3 artifacts + user journey docs + enhancement plan | Cross-pipeline map + prioritized roadmap |

---

### Phase 1 — Import Pipeline (File → Persist)

**Goal:** Trace the complete import journey from user file selection to Room persistence. Identify where data enters, what transforms it undergoes, and where it lands.

**Read first (in this order):**
1. `AGENTS.md` (project guidance — use AI Navigation Guide paths)
2. `docs/importing.md` (import input path documentation)

**Read these source files (bounded list — do not explore beyond):**

*Chunk 9 — Importer Parsers:*
- `core/data/.../data/importer/parser/SpreadsheetHeaderMapper.kt`
- `core/data/.../data/importer/parser/SpreadsheetQuestionParser.kt`
- `core/data/.../data/importer/parser/CsvParser.kt`
- `core/data/.../data/importer/parser/JsonQuestionParser.kt`
- `core/data/.../data/importer/parser/JsonLibraryParser.kt`
- `core/data/.../data/importer/parser/HtmlQuestionParser.kt`
- `core/data/.../data/importer/parser/TextQuestionParser.kt`
- `core/data/.../data/importer/parser/TextFlashcardParser.kt`
- `core/data/.../data/importer/parser/TextSlideParser.kt`
- `core/data/.../data/importer/parser/TextArticleParser.kt`
- `core/data/.../data/importer/parser/PptxSlideParser.kt`
- `core/data/.../data/importer/parser/ZipLibraryParser.kt`
- `core/data/.../data/importer/parser/GenericImageExtractor.kt`
- `core/data/.../data/importer/parser/SourceDetector.kt`

*Chunk 10 — Importer Logic & Excel:*
- `core/data/.../data/importer/detector/ImportFormatDetector.kt`
- `core/data/.../data/importer/dto/LibraryBundleDto.kt`
- `core/data/.../data/importer/mapping/LibraryMapper.kt`
- `core/data/.../data/importer/normalization/BundleNormalizer.kt`
- `core/data/.../data/importer/repository/ImportLibraryManager.kt`
- `core/data/.../data/importer/security/ImportLimits.kt`
- `core/data/.../data/importer/validation/ImportValidator.kt`
- `core/data/.../data/importer/xlsx/XlsxImageResolver.kt`
- `core/data/.../data/importer/xlsx/XlsxLibraryCompiler.kt`

*Chunk 12 (partial) — File I/O:*
- `core/data/.../data/local/FileManager.kt`

*Chunk 16 (partial) — Import UI:*
- `feature/ui/.../ui/importer/ImportViewModel.kt`

*Chunk 17 (partial) — Compiler UI:*
- `feature/ui/.../ui/quiz/CompilerDialog.kt`
- `feature/ui/.../ui/quiz/CompilerViewModel.kt`

**Output:** `review/phase1_import_pipeline.md`

**Sections required in the output:**

**§1 End-User Lens**
Trace one real user action: "I selected a spreadsheet and imported it." Describe each step the user sees, each decision they make (header row selection, column mapping, preview review), and where data is at each point (file → temp cache → parsed preview → DB). Note friction points, dead-ends, confusing UI states, and failure modes the user would encounter (e.g., wrong format detected, header row misidentified, image not extracted, large file timeout).

**§2 Senior-Developer Lens**
For the same pipeline, provide:
- Data flow diagram (text-based): file → detector → parser → normalizer → validator → mapper → repository → DAO → Room
- Correctness risks: image priority order (XLSX embedded → image column → question cell → option cell → row-level → merged cell), marked-cell vs explicit answer priority, merged-cell handling, empty-row filtering logic
- Error handling gaps: what happens when a parser throws? Is the temp file cleaned up on failure? Are partial imports rolled back?
- Thread/coroutine correctness: is file I/O on `Dispatchers.IO`? Are there main-thread blockers?
- State desync: can the CompilerUiState preview diverge from what actually gets persisted?

**§3 Recommendations**
- **Potential Improvements:** Specific, actionable improvements to existing behavior (e.g., better error messages, progress indicators for large files, undo after import).
- **Features to Add:** New capabilities that would enhance the import experience (e.g., drag-and-drop reordering of column mapping, saved mapping presets, batch multi-file import).
- **Functions to Maturize:** Existing functions that are partial/stub/fragile and should be hardened (e.g., fuzzy text answer matching, delimiter inference edge cases, PPTX image extraction).
- **Refactor Opportunities:** Duplicated logic, overly large functions, classes that should be split, patterns that should be unified (e.g., all parsers sharing a common interface, image extraction logic centralized).

**Constraints:** Use `file:line` references. Do not write code or make changes. When done, reply with the file path and a 5-line summary only.

---

### Phase 2 — Quiz Play & Knowledge Bank Study Pipelines

**Goal:** Trace the study/playback pipelines — quiz answering/scoring/sessions, flashcards (spaced repetition), slideshows, note blueprints (reader/TTS/autoscroll), and AI prompt decks — from user entry to data persistence.

**Read first:** `review/phase1_import_pipeline.md` (the prior artifact — do not re-read the importer code).

**Read these source files (bounded list):**

*Chunk 11 (partial) — Repositories:*
- `core/data/.../data/repository/QuizRepository.kt`
- `core/data/.../data/repository/StudyRepository.kt`
- `core/data/.../data/repository/KnowledgeRepository.kt`

*Chunk 14 — Book Tools:*
- `feature/ui/.../ui/booktools/BookKnowledgeDashboardScreen.kt`
- `feature/ui/.../ui/booktools/BookDashboardTabs.kt`
- `feature/ui/.../ui/booktools/BookToolScreens.kt`
- `feature/ui/.../ui/booktools/BookToolsViewModel.kt`

*Chunk 15 — Flashcards:*
- `feature/ui/.../ui/flashcard/FlashcardDeckScreen.kt`
- `feature/ui/.../ui/flashcard/FlashcardDeckViewModel.kt`

*Chunk 17 — Quiz Player:*
- `feature/ui/.../ui/quiz/QuizPlayerScreen.kt`
- `feature/ui/.../ui/quiz/QuizViewModel.kt`
- `feature/ui/.../ui/quiz/QuizQuestionsScreen.kt`
- `feature/ui/.../ui/quiz/QuizQuestionsViewModel.kt`
- `feature/ui/.../ui/quiz/QuizDetailTabsScreen.kt`

*Chunk 18 (partial) — Sessions:*
- `feature/ui/.../ui/session/SessionManagementScreen.kt`
- `feature/ui/.../ui/session/SessionViewModel.kt`

*Chunk 19 (partial) — Slideshow & Summary:*
- `feature/ui/.../ui/slideshow/SlideshowCourseScreen.kt`
- `feature/ui/.../ui/slideshow/SlideshowCourseViewModel.kt`
- `feature/ui/.../ui/summary/SummaryScreen.kt`
- `feature/ui/.../ui/summary/SummaryViewModel.kt`

*Entity context (read selectively for field verification):*
- `core/model/.../data/local/entity/SessionEntity.kt`
- `core/model/.../data/local/entity/FlashcardEntity.kt`
- `core/model/.../data/local/entity/LearningSessionEntity.kt`
- `core/model/.../data/local/entity/KnowledgeStudySessionEntity.kt`
- `core/model/.../data/local/entity/StudySessionEntity.kt`
- `core/model/.../data/local/entity/CourseSlideEntity.kt`
- `core/model/.../data/local/entity/NoteBlueprintEntity.kt`
- `core/model/.../data/local/entity/PromptDeckEntity.kt`
- `core/model/.../data/local/entity/PromptCardEntity.kt`
- `core/model/.../data/local/entity/PromptRunEntity.kt`

*Navigation:*
- `feature/ui/.../ui/MksNavHost.kt` (routes only, for navigation flow)

*Shared UI:*
- `core/ui/.../ui/utils/TtsManager.kt`

**Output:** `review/phase2_study_pipelines.md`

**Sections required in the output:**

**§1 End-User Lens**
For EACH of: quiz, flashcards, slideshow, note reader, prompt deck — trace the user journey from entry (library → book dashboard → tab → screen) through interaction to where their input/state ends up (which entity, which session row). Call out any surface where progress can be lost (e.g., app kill without save), where the back-stack is confusing, where two surfaces disagree on the same data (e.g., dashboard stats vs. quiz list), and where the user has no feedback that their action was saved.

**§2 Senior-Developer Lens**
Per pipeline:
- State ownership: which ViewModel owns which StateFlow? Is there any duplicated source of truth?
- Persistence guarantees: is partial progress saved on app kill? Is session state serialized atomically?
- Session-entity correctness: does `SessionEntity` capture all user answers? Does `KnowledgeStudySessionEntity` track all knowledge-bank progress? Are streaks computed correctly?
- Parent book touch: does every study action touch the parent book's `lastStudiedAt`? Are stats refreshed?
- Derived asset sync: when a question changes, do linked flashcards/slides/notes update? Is `syncConfig` honored?
- Compose performance: any unnecessary recompositions? Heavy computations in composition?

**§3 Recommendations**
- **Potential Improvements:** Specific UX/UX-technical improvements (e.g., session resume indicator, progress auto-save frequency, slideshow swipe gesture feedback).
- **Features to Add:** New study capabilities (e.g., quiz branching based on performance, flashcard SRS algorithm tuning, collaborative note annotation, prompt deck templates).
- **Functions to Maturize:** Existing functions that are incomplete or fragile (e.g., spaced repetition scoring, streak calculation, slide completion tracking, TTS pause/resume on backgrounding).
- **Refactor Opportunities:** Duplicated ViewModel state management, BookToolsViewModel splitting if too large, shared study-session logic extraction, unified progress-tracking abstraction.

**Constraints:** `file:line` references. No code changes. Reply with file path + 5-line summary only.

---

### Phase 3 — AI, OCR, PDF, Export, Sync & Infrastructure

**Goal:** Trace the auxiliary pipelines — AI MCQ generation, OCR, PDF text extraction, Ollama LLM, export (ZIP exchange), Firebase (FCM + Remote Config), WorkManager, DataStore preferences — from input origin through network/worker hops to output persistence.

**Read first:** `review/phase1_import_pipeline.md` and `review/phase2_study_pipelines.md`.

**Read these source files (bounded list):**

*Chunk 8 — Network Layer:*
- `core/network/.../data/network/AiClient.kt`
- `core/network/.../data/network/McqService.kt`
- `core/network/.../data/network/OcrService.kt`
- `core/network/.../data/network/PdfRendererService.kt`
- `core/network/.../data/network/PdfTextExtractor.kt`
- `core/network/.../data/network/RemoteAssetFetcher.kt`
- `core/network/.../data/network/RemoteAssetPolicy.kt`
- `core/network/.../data/repository/OllamaRepository.kt`

*Chunk 11 (partial) — Repositories:*
- `core/data/.../data/repository/AiMcqRepository.kt`
- `core/data/.../data/repository/ExportManager.kt`
- `core/data/.../data/review/ReviewRepository.kt`
- `core/data/.../data/search/GlobalSearchRepository.kt`

*Chunk 12 (partial) — Exchange, Preferences, Seeder:*
- `core/data/.../data/exchange/v7/MksExchangeV7Archive.kt`
- `core/data/.../data/exchange/v7/MksExchangeV7Models.kt`
- `core/data/.../data/preferences/DataStoreManager.kt`
- `core/data/.../data/preferences/SettingsSanitizer.kt`
- `core/data/.../data/seeder/MksDatabaseSeeder.kt`

*Chunk 14 (partial) — AI MCQ & PDF UI:*
- `feature/ui/.../ui/booktools/AiMcqGeneratorScreen.kt`
- `feature/ui/.../ui/booktools/AiMcqGeneratorViewModel.kt`
- `feature/ui/.../ui/booktools/PdfExtractionScreen.kt`
- `feature/ui/.../ui/booktools/PdfExtractionViewModel.kt`

*Chunk 19 (partial) — Settings UI:*
- `feature/ui/.../ui/settings/SettingsScreen.kt`
- `feature/ui/.../ui/settings/SettingsViewModel.kt`
- `feature/ui/.../ui/settings/ProviderConfigDialog.kt`

*Chunk 15 (partial) — Data Tools:*
- `feature/ui/.../ui/data/DataToolsScreen.kt`
- `feature/ui/.../ui/data/DataToolsViewModel.kt`

*App services:*
- `app/.../service/AppFirebaseMessagingService.kt`
- `app/.../service/RemoteConfigManager.kt`
- `app/.../service/TokenSyncWorker.kt`

**Output:** `review/phase3_aux_pipelines.md`

**Sections required in the output:**

**§1 End-User Lens**
Trace each of:
- (a) **AI MCQ generation:** user opens generator → selects source material → configures parameters → generates → reviews → saves as quiz. Where does the AI call happen? What does the user see on failure/offline/timeout?
- (b) **PDF text extraction:** user opens source → extracts text → reviews → saves. What if PDF is encrypted or image-only?
- (c) **Export → import round-trip:** user exports a book to ZIP → deletes the book → imports the ZIP. Does the reimported book match the original? Are images preserved? Are sessions/stats preserved?
- (d) **Preference change:** user changes theme/font scale/language. Does it apply immediately or require restart? Is it persisted?
- (e) **AI prompt deck:** user creates a prompt → fills variables → runs it → routes output. What happens if Ollama is unreachable?

**§2 Senior-Developer Lens**
Per pipeline:
- API-key/secret handling: are keys hardcoded, in DataStore, or in BuildConfig? Are they logged?
- Network thread correctness: are AI/OCR/PDF calls on `Dispatchers.IO`? Are there timeouts? Cancellation support?
- Offline-first guarantees: does WorkManager retry with backoff? Are FCM tokens synced when network returns?
- RemoteConfig: is stale-while-revalidate correct? What happens on first launch with no config?
- Export round-trip fidelity: does `LibraryMapper` + `ExportManager` + `ImportLibraryManager` produce a lossless round-trip? Are soft-deleted items included? Are images bundled?
- Unbounded calls: any network call without timeout? Any file read without size limit?
- DataStore: is it thread-safe? Are there read-modify-write races?

**§3 Recommendations**
- **Potential Improvements:** Specific improvements (e.g., AI generation progress streaming, export file naming, OCR confidence display, offline queue visibility).
- **Features to Add:** New capabilities (e.g., cloud sync, multiple AI provider switching, batch PDF import, export format options, FCM topic subscriptions, scheduled study reminders).
- **Functions to Maturize:** Existing functions that are incomplete (e.g., AI MCQ quality validation, PDF table extraction, Ollama streaming responses, export progress reporting, RemoteConfig default fallbacks).
- **Refactor Opportunities:** AI client abstraction (support multiple providers uniformly), export/import symmetry enforcement, centralized network error handling, DataStore preference grouping.

**Constraints:** `file:line` references. No code changes. Reply with file path + 5-line summary only.

---

### Phase 4 — Synthesis, Cross-Pipeline Map & Prioritized Roadmap

**Goal:** Consolidate all prior findings into a coherent end-user narrative, a senior-developer risk register, a cross-pipeline data map, and a prioritized improvement roadmap.

**Read only (do NOT re-read source code):**
- `review/phase1_import_pipeline.md`
- `review/phase2_study_pipelines.md`
- `review/phase3_aux_pipelines.md`
- `docs/audits/UX_REVIEW_2026.md` (intended user journey — compare against observed implementation)
- `docs/roadmap.md` (any planned work already identified)
- `docs/audits/UX_REVIEW_2026.md` (comprehensive screen-by-screen interaction map)

**Output:** `review/phase4_synthesis.md`

**Sections required in the output:**

**§1 End-User Verdict**
A coherent narrative of the full user journey across all pipelines (import → study → AI → export → settings). Name the top 10 frictions/gaps a real user would feel, ranked by severity. Mark each as `[Import]` `[Study]` `[AI]` `[Sync]` `[Export]` `[Settings]`. Compare the intended journey (from the user-journey docs) against the observed implementation and flag every divergence.

**§2 Senior-Developer Verdict**
Top 10 architectural risks across the whole app: data-integrity, single-source-of-truth violations, missing persistence on kill, error-handling gaps, thread/coroutine misuse, security concerns. Each with `file:line` and a one-line remediation.

**§3 Cross-Pipeline Data Map**
A single text table answering "where does data come from, what does it go through, where does it end up" for every pipeline:

| Pipeline | Origin of Input | Transforms | Final Entity/Table | Owner ViewModel | Owner Repository | Persistence Guarantee |
|---|---|---|---|---|---|---|
| Spreadsheet import | File URI | detect → parse → normalize → validate → map | QuestionEntity, QuestionCategoryEntity | CompilerViewModel | ImportLibraryManager → Quiz/BookRepository | On explicit save only |
| Flashcard study | User tap (flip/rate) | update SRS metrics → session state | FlashcardEntity, LearningSessionEntity | FlashcardDeckViewModel | KnowledgeRepository | On each card action |
| ... | ... | ... | ... | ... | ... | ... |

(Complete this table for ALL pipelines: import, quiz play, quiz session, flashcard, slideshow, note reader, prompt deck, AI MCQ, OCR, PDF extraction, export, import-round-trip, preference change, FCM token sync, review queue, global search, trash/restore, workspace switch.)

**§4 Prioritized Improvement Roadmap**
Consolidate all Phase 1–3 Recommendations into a single prioritized roadmap:

| Priority | Category | Recommendation | Pipeline | Effort | Impact |
|---|---|---|---|---|---|
| P0 | Bug/Fix | ... | ... | S/M/L | Critical/High/Medium |
| P1 | Improvement | ... | ... | ... | ... |
| P2 | Feature | ... | ... | ... | ... |
| P3 | Refactor | ... | ... | ... | ... |
| P4 | Maturize | ... | ... | ... | ... |

Categories: `Bug/Fix`, `Improvement`, `Feature`, `Refactor`, `Maturize`, `Security`, `Performance`, `Testing`.
Priority levels: P0 (critical, do now), P1 (high, next sprint), P2 (medium, roadmap), P3 (low, backlog), P4 (nice-to-have).

Group the roadmap into thematic clusters (e.g., "Import Reliability", "Study Persistence", "AI Robustness", "Export Fidelity", "Infrastructure Hardening", "UX Polish", "Testing Coverage") so the team can tackle them coherently.

**§5 Testing Gap Analysis**
Consolidate the test-coverage gaps identified across all phases. Map each gap to the risk it introduces and recommend specific test classes to write. Reference existing tests (Part 3, Chunks 20–21) that should be extended.

**Constraints:** No code changes. Reply with the file path and a 10-line executive summary.

---

### Execution Notes

1. **Run phases sequentially** in the same Claude Code session. Each phase explicitly re-reads the prior `.md` artifact, which is cheap (one file) versus re-reading source code.
2. **Create the `review/` directory** at the project root before Phase 1 (or let Phase 1 create it).
3. **Do not skip phases.** Each phase's output is input to Phase 4's synthesis. Skipping produces an incomplete data map.
4. **Do not search `build/`.** It is build output and wastes context.
5. **If the AI agent identifies a gap in this plan** (e.g., a pipeline not covered, a file that should be read, a better grouping), it should flag it to the user before executing the affected phase rather than silently deviating.
6. **After Phase 4**, the user should review `review/phase4_synthesis.md` §4 (Prioritized Improvement Roadmap) as the primary deliverable — it is the actionable output of the entire review.
