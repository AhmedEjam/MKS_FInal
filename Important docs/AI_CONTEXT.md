# AI Context & Navigation Guide for MKS Android Project

Welcome, AI Agent (Gemini Pro, Claude, etc.). This document serves as your **Master Context Guide** for the MKS Android application. It combines a comprehensive overview of the project's structure and architecture with dynamic instructions on how you should navigate, inspect, and safely modify this repository.

Whenever you are initialized in this workspace, **read this file first** to understand the project paradigms and how to find what you need.

---

## 1. Project Overview

**MKS** is an Android quiz and knowledge-bank application that imports educational content from spreadsheets and documents, presenting interactive quizzes, flashcards, slideshows, note blueprints, and prompt decks.

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Single-activity)
- **Dependency Injection:** Dagger Hilt (`@HiltAndroidApp`, `@HiltViewModel`, `hiltViewModel()`)
- **Architecture:** Multi-module (app, core/database, core/model, core/data, core/network, core/ui, feature/ui)
- **Database:** Room v30 (with 29 migration steps, v1→v30)
- **Image Handling:** Coil (memory + disk cache), embedded XLSX images, HTTP
- **Localization:** English + Arabic (RTL support natively)
- **AI Integration:** Local LLM via Ollama (OllamaRepository)

---

## 2. Instructions for AI Code Exploration

To maintain full visibility of this codebase, do not guess file locations or architectures. Use your search and inspection tools dynamically according to these guidelines:

### A. How to Find Code (File Navigation Strategy)

The project is **modularized** into `app/`, `core/`, and `feature/` directories. The base package is `com.ahmedyejam.mks`.

1. **UI & Screens:** Look inside `feature/ui/src/main/java/.../ui/`.
   - *Example query:* `ls feature/ui/src/main/java/com/ahmedyejam/mks/ui/`
   - Screens are typically suffixed with `Screen.kt` and ViewModels with `ViewModel.kt`.
   - Shared UI components and theme are in `core/ui/src/main/java/.../ui/`.
2. **Database Schema & Migrations:** Look inside `core/database/src/main/java/.../data/local/`.
   - The active database schema is centralized in `MksDatabase.kt`.
   - All migrations are in `MksMigrations.kt`. **Always check here before altering entities.**
3. **Entities:** Look inside `core/model/src/main/java/.../data/local/entity/`.
   - Contains exactly 26 entity classes (e.g., `BookEntity`, `QuizEntity`, `QuestionEntity`).
4. **DAOs:** Look inside `core/database/src/main/java/.../data/local/dao/`.
   - Contains exactly 26 DAO interfaces.
5. **Repositories:** Look inside `core/data/src/main/java/.../data/repository/`.
   - Split into: `BookRepository`, `QuizRepository`, `KnowledgeRepository`, `StudyRepository`, `AssetRepository`, `WorkspaceRepository`, `ExportManager`.
6. **Network & LLM:** Look inside `core/network/src/main/java/.../`.
   - `OllamaRepository` for local LLM integration.
   - `RemoteAssetFetcher` and `RemoteAssetPolicy` for remote assets.
7. **Data Importing Logic:** Look inside `core/data/src/main/java/.../data/importer/`.
   - To understand how spreadsheets are parsed, inspect `ImportFormatDetector.kt` and the xlsx-related parsers.
8. **Dependency Injection:** Look inside `app/src/main/java/.../di/`.
   - Contains 6 Hilt modules: `HiltDataModule`, `HiltDaoModule`, `HiltKnowledgeDaoModule`, `HiltUtilityDaoModule`, `HiltRepositoryModule`, `HiltServiceModule`.
9. **Domain Models:** Look inside `core/model/src/main/java/.../data/model/`.
   - Contains models like `OllamaModels`, `CategoryWithMetadata`, `LearningSessionState`, etc.
10. **Route Constants:** `core/model/src/main/java/.../ui/MksRoutes.kt`.

### B. Dynamic Inspection Workflow
When asked to implement a new feature or fix a bug, follow this workflow:
1. **Search Context:** If it's a UI bug, `grep` for the Compose screen name in `feature/ui/`. If it's a data bug, `grep` for the Entity in `core/model/` or the DAO in `core/database/`.
2. **Trace Dependencies:** All dependencies flow through Dagger Hilt. ViewModels are annotated with `@HiltViewModel` and inject dependencies via constructor injection. In Compose screens, they are resolved using `hiltViewModel()`.
3. **Verify DB State:** If modifying data, always check if a Room Migration is required by inspecting `core/database/.../MksDatabase.kt`'s version constant (`MKS_DATABASE_VERSION`) and `MksMigrations.kt`.

---

## 3. Core Architectural Patterns to Respect

### Dagger Hilt Dependency Injection (CRITICAL)
- **Rule:** The app uses **Dagger Hilt** for DI. `MksApplication` is annotated with `@HiltAndroidApp`.
- **Pattern:** ViewModels use `@HiltViewModel` with `@Inject constructor(...)`. Screens resolve them with `hiltViewModel()`.
- **Modules:** DI is split across 6 Hilt modules in `app/src/main/java/.../di/`.

### Multi-Module Architecture
- **`app/`**: Entry point (`MainActivity`, `MksApplication`) and Hilt DI modules.
- **`core/database/`**: Room database, migrations, DAOs.
- **`core/model/`**: Entity classes, domain models, route constants.
- **`core/data/`**: Repositories, services, import pipeline, seeder.
- **`core/network/`**: Remote asset fetching, Ollama LLM integration.
- **`core/ui/`**: Shared UI components, theme, TTS.
- **`feature/ui/`**: All feature screens (library, quiz, flashcard, etc.) and navigation.

### State Management
- **Rule:** Use `StateFlow` and `MutableStateFlow` in ViewModels.
- **Pattern:** ViewModels emit a unified UI state (e.g., `CompilerUiState`). Composables observe this via `uiState.collectAsState()`. Use `LaunchedEffect` for triggering initial data loads.

### Room Database & Migrations
- **Rule:** Never drop tables or destructively alter schemas. Always write explicit migrations.
- **Pattern:** When adding a column, increment `MKS_DATABASE_VERSION` in `MksDatabase.kt` and add a `Migration(N, M)` block in `MksMigrations.kt`, then register it in the database builder's `addMigrations(...)` chain. Test files like `Migration15To16Test` exist for regression testing.

### Navigation (Jetpack Navigation Compose)
- **Rule:** Routing is handled centrally.
- **Pattern:** `feature/ui/.../MksNavHost.kt` manages all routes (e.g., `welcome`, `library`, `quiz/{quizId}`, `flashcards/{deckId}`). Route constants are defined in `core/model/.../ui/MksRoutes.kt`. All route arguments must be extracted from `NavBackStackEntry`.

### Knowledge Bank Hierarchy
- **Concept:** `BookEntity` is the root container.
- It can hold:
  1. **Quizzes** (`QuizEntity`, `QuestionEntity`)
  2. **Flashcards** (`FlashcardDeckEntity`, `FlashcardEntity`)
  3. **Slideshows** (`SlideshowCourseEntity`, `CourseSlideEntity`)
  4. **Notes** (`NoteBlueprintEntity`, `NoteCollectionEntity`)
  5. **AI Prompts** (`PromptDeckEntity`, `PromptCardEntity`)
- **Pattern:** When creating new learning formats, they must link to a `BookEntity` parent.

---

## 4. Common Tasks & Commands

### Running Builds & Tests
- Compile Debug APK: `./gradlew assembleDebug`
- Run Unit Tests: `./gradlew test`
- Run DB Migration Tests: `./gradlew connectedAndroidTest` (requires emulator/device)

### Investigating Import Pipeline (Spreadsheets)
If modifying the spreadsheet parsing:
1. `CompilerViewModel` (in `feature/ui/.../quiz/`) orchestrates the flow.
2. `ImportFormatDetector` detects XLSX/CSV/JSON.
3. `SpreadsheetHeaderMapper` dynamically maps localized columns (Arabic + English).
4. `SpreadsheetQuestionParser` processes individual rows.

---

**Final Note to AI Agents:** You are expected to read existing implementations before writing new code. If modifying a screen, read its ViewModel. If modifying an entity, read its DAO and migration history. Trust the explicit rules in this document over standard Android documentation when conflicts arise (especially regarding DI and module boundaries).

*Last updated: 2026-06-26*
