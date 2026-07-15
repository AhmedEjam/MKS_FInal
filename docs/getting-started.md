# Getting Started

This guide covers the necessary toolchain, build process, and testing setup for MKS.

## Prerequisites & Toolchain

MKS is built entirely in Kotlin using Jetpack Compose and Dagger Hilt.

- **Android Studio**: Ladybug (or newer recommended)
- **Language**: Kotlin 99.5%
- **Min SDK**: 30 (Android 11)
- **Target SDK**: 37
- **JVM Target**: Java 11

## Build & Run

To compile and run the application, use the provided Gradle wrapper commands from the project root:

```bash
# Build the debug APK
./gradlew assembleDebug

# Execute a full build
./gradlew build
```

## Testing & Sample Data

MKS includes a database seeder for testing, unit tests, and instrumented tests.

### Seed Database

The `MksDatabaseSeeder.seed()` function (located in `core/data/seeder/`) initializes the database with useful test data so the app is never empty on first launch:

- 1 sample Book
- 1 Quiz with 13 questions
- A sample flashcard deck
- A sample slideshow course
- Sample notes & AI prompts

*To reset the database, clearing app data will trigger a re-seeding upon next launch.*

### Run Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

---
*Status: Current | Owner: AhmedEjam | Last Verified: 2026-07-15*
