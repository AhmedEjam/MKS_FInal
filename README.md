# 📚 MKS - My Knowledge Space

**MKS (My Knowledge Space)** is an advanced, offline-first Android study application. It transforms imported educational content (from spreadsheets, documents, PDFs, photos, and AI generation) into interactive learning experiences.

Whether you're studying for exams or mastering a new language, MKS organizes your knowledge into **Books** inside isolated **Workspaces**. From there, you can interact with your content through interactive quizzes, flashcards (with spaced-repetition), slideshows, note blueprints, and AI prompt decks.

---

## ✨ Features at a Glance

- **Multi-Format Import**: Ingest content seamlessly from XLSX, CSV, JSON, HTML, text files, and ZIP bundles.
- **Interactive Quizzes**: Multiple choice, single choice, fill-in-the-blank, and adaptive learning modes.
- **Rich Knowledge Bank**: Auto-generate flashcards, slideshow courses, and note blueprints directly from your materials.
- **Offline-First & Private**: Your data lives locally on your device in a Room database, with isolated workspaces.
- **AI-Powered**: Generate MCQs automatically and utilize AI prompt decks (compatible with Groq, Gemini, DeepSeek, and local Ollama).
- **Accessible & Localized**: Full support for both English and Arabic (RTL layout), with dynamic font scaling and multiple themes.

---

## 🚀 Quick Start & Build Instructions

MKS is built entirely in Kotlin using Jetpack Compose and Dagger Hilt.

### Requirements

- **Android Studio**: Ladybug (or newer recommended)
- **Min SDK**: 30 (Android 11)
- **Target SDK**: 37

### Build & Run

To compile and test the application, run the following Gradle commands from the project root:

```bash
# Build the debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Execute a full build
./gradlew build
```

---

## 📖 Documentation Directory

The project documentation has been newly reorganized to be clear, authoritative, and easy to navigate.

### **[Documentation Index (docs/README.md)](docs/README.md)**

Start here to understand the documentation authority map and where to find specific information.

- **[Getting Started](docs/getting-started.md)**: Toolchain, build, run, and test requirements.
- **[Architecture & DI](docs/architecture.md)**: Module boundaries, dependency flow, and Dagger Hilt injection map.
- **[Database Schema](docs/database.md)**: Room v33 entities, DAOs, and migrations.
- **[Importing Pipeline](docs/importing.md)**: Formats, limits, security rules, and parsing behavior.
- **[AI Systems](docs/ai.md)**: AI providers, pipelines, and prompt architectures.
- **[User Lifecycle](docs/lifecycle.md)**: Complete user journey from installation to daily use.
- **[User Guide](docs/user-guide.md)**: Stable screen-by-screen workflow and UI guide.
- **[Design System](docs/design-system.md)**: Durable visual rules, theming, and component contracts.
- **[Redesign Status](docs/redesign-status.md)**: Temporary implementation handoff and screen statuses.
- **[Roadmap](docs/roadmap.md)**: Issue-backed roadmap and enhancement plans.
- **[Tooling & MCP](docs/tooling/android-studio-mcp.md)**: Android Studio tooling and AI agent configurations.
- **[Audits & Reviews](docs/audits/)**: Dated historical audits and codebase reviews.

### 🤖 Note for AI Agents

Please refer to **[AGENTS.md](AGENTS.md)** for detailed AI-specific navigation rules, strict file path guidelines, and common task patterns. **Do not guess file locations**—use the authoritative paths provided in the agent guidelines.

---

## 📞 Contact & Support

- **Repository**: <https://github.com/AhmedEjam/MKS_FInal>
- **Owner**: AhmedEjam
- **License**: (See LICENSE file if present)

---
*Status: Current | Owner: AhmedEjam | Last Verified: 2026-07-15*
