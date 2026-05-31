# CLAUDE.md
> Finalization status correction (2026-05-25): active source is Room v17 with migrations through 16→17; older v15/v16 text below is historical and should not drive new patches.


This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MKS (Mobile Knowledge System) is a native Android quiz/study application built with Kotlin and Jetpack Compose. It's a conversion of a legacy "library_v16_portable" web-based quiz application, featuring hierarchical data management (Books → Quizzes → Questions), adaptive training, and session persistence.

## Build & Development Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install debug APK on connected device
./gradlew installDebug

# Run all tests
./gradlew test

# Run unit tests only
./gradlew testDebugUnitTest

# Run instrumented tests only
./gradlew connectedAndroidTest

# Clean and rebuild
./gradlew clean build

# Check for dependency updates
./gradlew dependencyUpdates
```

## Architecture

**Tech Stack:**
- Language: Kotlin
- UI: Jetpack Compose with Material 3
- DI: Manual dependency injection via `AppModule` (no Hilt/Dagger)
- Database: Room (with KSP)
- Preferences: Jetpack DataStore
- JSON: Moshi (with KSP)
- Images: Coil
- Navigation: Compose Navigation

**Project Structure:**
```
app/src/main/java/com/ahmedyejam/mks/
├── di/                  # AppModule - manual DI container
├── data/
│   ├── local/           # Room database, DAOs, entities, converters
│   ├── model/           # Data models
│   ├── preferences/     # DataStore manager
│   ├── repository/      # MksRepository, ExportManager
│   └── import/          # Import pipeline (parser, validator, mapper)
├── ui/
│   ├── library/         # Library management screens
│   ├── quiz/            # Quiz player, compiler
│   ├── category/        # Category question management
│   ├── session/         # Session management
│   ├── summary/         # Post-quiz analytics
│   ├── scanner/         # QR/barcode scanner
│   ├── settings/        # App settings
│   ├── theme/           # Theme, colors, typography
│   └── MksNavHost.kt    # Navigation graph
└── MainActivity.kt
```

**Key Components:**

- **`MksApplication`**: Application class holding `AppModule` instance
- **`AppModule`**: Central DI container providing database, repositories, managers
- **`MksDatabase`**: Room database (v17) with 16 migration steps defined
- **`MksRepository`**: Single source of truth for data operations
- **ViewModels**: One per screen, receiving dependencies from `AppModule`

**Data Flow:**
UI → ViewModel → Repository → DAO/DataStore → Database

**Navigation:**
Single-activity architecture with `NavHostController`. Routes:
- `library` (start) → `category/{category}` → `quiz/{quizId}?sessionId={sessionId}` → `summary/{sessionId}`
- `library` → `sessions/{quizId}` → resume quiz
- `library` → `scanner/{quizId}` (QR import)
- `library` → `adaptive/{type}/{id}` (adaptive training)

## Database Schema

**Entities:**
- `BookEntity` - Top-level container
- `QuizEntity` - Belongs to Book, contains Questions
- `QuestionEntity` - Supports Single/Multiple Choice, Boolean types
- `SessionEntity` - User progress, timer settings, streaks
- `CategoryMetadataEntity` - Category emojis, colors, pin state

**Migrations:** 10 schema versions with incremental migrations (1→10). Adding new columns requires a new migration in `MksDatabase`.

## Key Features

- **Quiz Engine**: Shuffling, timers, "Rapid Mode", option dropper, adaptive training
- **Import/Export**: JSON-based library export, supports legacy format via `LegacyBookDto`
- **Session Persistence**: Unfinished quizzes saved to Room/DataStore
- **Image Support**: Questions can have images (URLs or local paths)
- **Analytics**: Post-quiz summaries, streaks, performance metrics

## Notes

- KSP is used for Room and Moshi code generation
- Min SDK: 30, Target SDK: 35, Compile SDK: 35
- JVM target: Java 11
- Custom theme: "Speedy Dark" (vibrant Material 3)
- No external README exists — `.agent/plan.md` contains the original project brief
