# AI Context & Navigation Guide for MKS Android Project

Welcome, AI Agent (Gemini Pro, Claude, etc.). This document serves as your **Master Context Guide** for the MKS Android application. It combines a comprehensive overview of the project's structure and architecture with dynamic instructions on how you should navigate, inspect, and safely modify this repository.

Whenever you are initialized in this workspace, **read this file first** to understand the project paradigms and how to find what you need.

---

## 1. Project Overview

**MKS** is an Android quiz and knowledge-bank application that imports educational content from spreadsheets and documents, presenting interactive quizzes, flashcards, slideshows, note blueprints, and prompt decks.

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Single-activity)
- **Dependency Injection:** Manual `AppModule` Container (NO Hilt/Dagger)
- **Database:** Room v26 (with 25 migration steps)
- **Image Handling:** Coil (memory + disk cache), embedded XLSX images, HTTP
- **Localization:** English + Arabic (RTL support natively)

---

## 2. Instructions for AI Code Exploration

To maintain full visibility of this codebase, do not guess file locations or architectures. Use your search and inspection tools dynamically according to these guidelines:

### A. How to Find Code (File Navigation Strategy)
The core package is `app/src/main/java/com/ahmedyejam/mks/`. Use your workspace search tools (`grep` or file tree listing) to navigate:

1. **UI & Screens:** Look inside `ui/`. 
   - *Example query:* `ls app/src/main/java/com/ahmedyejam/mks/ui/`
   - Screens are typically suffixed with `Screen.kt` and ViewModels with `ViewModel.kt`.
2. **Database Schema & Migrations:** Look inside `data/local/`.
   - The active database schema is centralized in `MksDatabase.kt`.
   - All migrations are in `MksMigrations.kt`. **Always check here before altering entities.**
3. **Entities:** Look inside `data/local/entity/`.
   - Contains exactly 24 entity classes (e.g., `BookEntity`, `QuizEntity`, `QuestionEntity`).
4. **Data Importing Logic:** Look inside `data/import/`.
   - To understand how spreadsheets are parsed, inspect `ImportFormatDetector.kt` and the `xlsx/` directory.
5. **Dependency Injection:** Look inside `di/`.
   - Inspect `AppModule.kt` (over 1,400 lines) to see how repositories, DAOs, and Managers are instantiated and scoped.

### B. Dynamic Inspection Workflow
When asked to implement a new feature or fix a bug, follow this workflow:
1. **Search Context:** If it's a UI bug, `grep` for the Compose screen name. If it's a data bug, `grep` for the Entity or DAO.
2. **Trace Dependencies:** All dependencies flow from `AppModule`. If you need to understand how a ViewModel gets a repository, check its `ViewModelProvider.Factory` inside `MksNavHost.kt` or the specific screen file.
3. **Verify DB State:** If modifying data, always check if a Room Migration is required by inspecting `MksDatabase.kt`'s version number and `MksMigrations.kt`.

---

## 3. Core Architectural Patterns to Respect

### Manual Dependency Injection (CRITICAL)
- **Rule:** Do NOT introduce or suggest Hilt, Dagger, or Koin.
- **Pattern:** The app uses a manual container `AppModule` initialized in `MksApplication`.
- **Usage:** Access dependencies via `appModule` (e.g., `appModule.repository`). ViewModels are instantiated via custom `ViewModelProvider.Factory` passing components from `appModule`.

### State Management
- **Rule:** Use `StateFlow` and `MutableStateFlow` in ViewModels.
- **Pattern:** ViewModels emit a unified UI state (e.g., `CompilerUiState`). Composables observe this via `uiState.collectAsState()`. Use `LaunchedEffect` for triggering initial data loads.

### Room Database & Migrations
- **Rule:** Never drop tables or destructively alter schemas. Always write explicit migrations.
- **Pattern:** When adding a column, increment the DB version in `MksDatabase.kt` and add a `Migration(N, M)` block in `MksMigrations.kt`, then add it to the `AppModule.databaseBuilder().addMigrations(...)` chain. Test files like `Migration15To16Test` exist for regression testing.

### Navigation (Jetpack Navigation Compose)
- **Rule:** Routing is handled centrally.
- **Pattern:** `MksNavHost.kt` manages all routes (e.g., `welcome`, `library`, `quiz/{quizId}`, `flashcards/{deckId}`). All route arguments must be extracted from `NavBackStackEntry`.

### Knowledge Bank Hierarchy
- **Concept:** `BookEntity` is the root container.
- It can hold:
  1. **Quizzes** (`QuizEntity`, `QuestionEntity`)
  2. **Flashcards** (`FlashcardDeckEntity`, `FlashcardEntity`)
  3. **Slideshows** (`SlideshowCourseEntity`, `CourseSlideEntity`)
  4. **Notes** (`NoteBlueprintEntity`)
  5. **AI Prompts** (`PromptDeckEntity`)
- **Pattern:** When creating new learning formats, they must link to a `BookEntity` parent.

---

## 4. Common Tasks & Commands

### Running Builds & Tests
- Compile Debug APK: `./gradlew assembleDebug`
- Run Unit Tests: `./gradlew test`
- Run DB Migration Tests: `./gradlew connectedAndroidTest` (requires emulator/device)

### Investigating Import Pipeline (Spreadsheets)
If modifying the spreadsheet parsing:
1. `CompilerViewModel` orchestrates the flow.
2. `ImportFormatDetector` detects XLSX/CSV/JSON.
3. `SpreadsheetHeaderMapper` dynamically maps localized columns (Arabic + English).
4. `SpreadsheetQuestionParser` processes individual rows.

---

**Final Note to AI Agents:** You are expected to read existing implementations before writing new code. If modifying a screen, read its ViewModel. If modifying an entity, read its DAO and migration history. Trust the explicit rules in this document over standard Android documentation when conflicts arise (especially regarding DI).
