# MKS Project Guidelines

This document provides essential information for developers working on the MKS Android project. MKS is a native Android quiz and study application built with Kotlin and Jetpack Compose.

## 1. Build and Configuration Instructions

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 11 (used for project compilation)
- Android SDK 35 (Compile/Target SDK)
- Min SDK 30

### Common Build Commands
Use the following Gradle commands for development:

```bash
# Build the debug APK
./gradlew assembleDebug

# Install the debug APK on a connected device/emulator
./gradlew installDebug

# Clean the project
./gradlew clean

# Run a full build including tests
./gradlew build
```

### Configuration
- **Dependency Injection**: The project uses **Manual DI** via the `AppModule` class. Do not use Hilt or Dagger.
- **Database**: Room v22. Schema is exported to `app/schemas`.
- **Image Loading**: Coil is used for image loading with custom memory and disk caching configured in `MksApplication`.

## 2. Testing Information

### Running Tests
The project includes both unit tests and instrumented tests.

```bash
# Run all unit tests
./gradlew test

# Run unit tests for the debug variant
./gradlew testDebugUnitTest

# Run instrumented tests on a connected device
./gradlew connectedAndroidTest
```

### Adding New Tests
- **Unit Tests**: Place in `app/src/test/java/`. Use JUnit 4 and MockK for mocking.
- **Instrumented Tests**: Place in `app/src/androidTest/java/`. These are required for Room migrations and DAO testing.

### Example Unit Test
The following example demonstrates how to test the `SpreadsheetHeaderMapper` logic, which is critical for the import pipeline.

```kotlin
package com.ahmedyejam.mks.data.import.parser

import org.junit.Assert.assertEquals
import org.junit.Test

class SpreadsheetHeaderMapperTest {

    private val mapper = SpreadsheetHeaderMapper()

    @Test
    fun `mapHeaders should correctly identify Arabic and English headers`() {
        val headers = listOf("سؤال", "الإجابة", "Explanation", "random")
        val result = mapper.mapHeaders(headers)

        assertEquals(0, result["question"])
        assertEquals(1, result["answer"])
        assertEquals(2, result["explanation"])
        assertEquals(null, result["random"])
    }
}
```

## 3. Additional Development Information

### Architecture & Design Patterns
- **Manual Dependency Injection**: Access global dependencies through `MksApplication.appModule`. ViewModels should receive dependencies via `ViewModelProvider.Factory`.
- **Repository Pattern**: `MksRepository` is the single source of truth for all data operations.
- **Import Pipeline**: The app uses a multi-stage parsing pipeline:
    1. **Format Detection**: `ImportFormatDetector` identifies the file type.
    2. **Header Mapping**: `SpreadsheetHeaderMapper` maps spreadsheet columns to entity fields using multi-language aliases (English & Arabic).
    3. **Parsing**: Format-specific parsers (XLSX, CSV, JSON, etc.) convert rows into `ParsedQuestion` objects.
- **Room Migrations**: All migrations are centralized in `MksMigrations.kt`. When adding columns or tables, ensure a proper `Migration` object is added to `AppModule.databaseBuilder()`.

### Localization
- **Arabic Support**: The app fully supports RTL layouts and includes Arabic translations in `res/values-ar/strings.xml`.
- **Header Aliases**: `SpreadsheetHeaderMapper` contains aliases for both English and Arabic field names, allowing users to import content in either language.

### Code Style
- Follow standard Kotlin coding conventions.
- Use `StateFlow` in ViewModels to expose UI state.
- Use Jetpack Compose for all UI components.
- Keep `AppModule` as the central hub for dependency management.
