# MKS Architecture

> **Source of Truth:** 6-module architecture, Dagger Hilt DI, MVVM pattern.

## Application Architecture Overview

```text
MKS (My Knowledge Space)
├── Framework: Jetpack Compose + Material3
├── Architecture: MVVM with Dagger Hilt DI (modular multi-module)
├── Database: Room v33 (32 migration steps)
├── Language: Kotlin
├── Target SDK: 37
└── Min SDK: 30
```

## Dependency Injection (Dagger Hilt)

All dependencies flow through Dagger Hilt. ViewModels use `@HiltViewModel` with `@Inject constructor(...)`; screens resolve them with `hiltViewModel()`.

### Hilt Modules (app/di/)

| Module | Provides |
|---|---|
| `HiltDataModule` | `MksDatabase`, `DataStoreManager`, `FileManager`, `ExportManager`, `ImportLibraryManager` |
| `HiltDaoModule` | Core quiz DAOs (`BookDao`, `QuizDao`, `QuestionDao`, `SessionDao`, etc.) |
| `HiltKnowledgeDaoModule` | Knowledge bank DAOs (`SlideshowCourseDao`, `FlashcardDao`, `NoteBlueprintDao`, etc.) |
| `HiltUtilityDaoModule` | Asset/utility DAOs (`AssetReferenceDao`, `SourceDocumentDao`, `WorkspaceDao`, etc.) |
| `HiltRepositoryModule` | Non-`@Inject` repos (`OllamaRepository`, `GlobalSearchRepository`, `ReviewRepository`) |
| `HiltServiceModule` | Firebase services (`FirebaseMessaging`, `FirebaseRemoteConfig` with 1h fetch interval / 2s timeout) |

> **Note on AppModule:** `AppModule` is a legacy trimmed class used primarily for database building and application scope; it is no longer the primary DI container for repositories.

## Data Flow Architecture

The application strictly follows a unidirectional data flow pattern.

```text
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

## Module Boundaries

The project is split into standard feature and core modules:

- `app/`: Single-activity entry point, Hilt initialization, DI modules.
- `core/database/`: Room schema, Daos, and central `MksMigrations.kt`.
- `core/data/`: Repositories and multi-format import pipeline (`ImportLibraryManager`).
- `core/model/`: Entities, Route constants, domain models.
- `core/network/`: Remote asset fetching, Ollama LLM integration.
- `core/ui/`: Shared Compose components, Design tokens, Themes.
- `feature/ui/`: NavHost, Screen implementations (grouped by feature).

## Navigation Structure

Navigation is entirely handled by Compose Navigation.
The central navigation graph is `MksNavHost.kt`. Route definitions and argument keys are defined in `core/model/src/main/java/com/ahmedyejam/mks/ui/MksRoutes.kt`. Route validation ensures navigation safety before displaying screens.

---
*Status: Current | Owner: AhmedEjam | Last Verified: 2026-07-15*
