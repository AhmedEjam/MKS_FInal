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
- **DI:** Dagger Hilt (6 modules in `app/di/`)
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

## Detailed Architecture

For detailed information on the project architecture, file structure, database schema, parsing algorithms, UI reference, and more, please refer to the authoritative architecture document at:
[Architecture Document](docs/architecture.md)

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

The following complementary documentation exists in `docs/`:

| File | Purpose |
|---|---|
| `docs/architecture.md` | High-level system architecture with component diagrams |
| `docs/lifecycle.md` | Complete user journey and interaction lifecycle |
| `docs/user-guide.md` | Detailed screen-by-screen UI interaction map |
| `docs/database.md` | Database inspection notes |
| `docs/importing.md` | Import input path documentation |
| `docs/roadmap.md` | Planned enhancement roadmap |
| `docs/ai.md` | Gemini AI tool (MCP) integration guidance |
| `docs/audits/` | Historical UX reviews and inspection maps |

For most tasks, read the relevant sections in this AGENTS.md. For deep architectural questions, consult the additional markdown files in `docs/`.

---

## AI Agent Tools (MCP)

To enhance Gemini's capabilities with external tools (GitHub, Figma, custom servers), follow the
instructions in [ai.md](docs/ai.md).
