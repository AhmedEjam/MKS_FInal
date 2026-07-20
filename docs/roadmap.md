# MKS Enhancement Plan

> Last updated: 2026-07-20. Current state: Room v33, 6-module architecture, 6 domain repositories, all 28 entities in place.

## Phase 1: Code Solidity & Testing Safety Net 🔴 PRIORITY

### 1.0 Adopt `study_runs` ✅ RESOLVED — adopted (needs device verification)

**Status:** 🟡 ADOPTED for flashcards and slideshow; **unverified on a device.**

`study_runs` shipped as a migration in v31→v32 but had no consumer. It is now the source of truth
for resumable study position, with a clear split of responsibility:

- **`StudyRun`** — in-flight resumable state ("where was I"): ordered item ids, current index,
  completed items, plus free-form `stateJson` for per-player detail.
- **`LearningSession`** — the analytics record ("a study session happened"): time spent, completion.
  It is no longer also the resume mechanism.

**This fixed a live bug.** `SlideshowCourseViewModel.setPresentationMode(true)` unconditionally reset
to slide 1 and created a *new* `LearningSession` every time. It wrote `stateJson` on every slide
change that nothing ever read back, so presentation resume never worked and orphaned incomplete
sessions accumulated. This was audit finding #7, still live for slideshows (it had been fixed for
flashcards, which did read their state back).

**Adoption surfaced an API defect:** `StudyRunState` had no `runId`, so a caller could resume a run
but had no handle to save further progress to it — resume would have worked exactly once and then
silently stopped persisting. `runId` added; `StudyRunRepositoryImplTest` now has a regression test.

Still on the old path (deliberately, they are not item-sequence players): the article/notes player
and the AI prompt deck. `ADAPTIVE_QUIZ` is already persisted via `SessionEntity`, so audit #6 is
closed independently — migrating it to `StudyRun` is optional cleanup, not a fix.

⚠️ Resume cannot be meaningfully tested without process death on a real device. See the manual test
plan in `docs/testing/study-run-adoption.md` before shipping.

### 1.1 Repository Test Harness & Test Suites

**Status:** 🟡 STARTED — `core/data/src/test/.../repository/` now has `WorkspaceIsolationTest`,
`SoftDeleteCascadeTest` and `StudyRunRepositoryImplTest`. The six domain repositories below still
have no direct coverage.

> **Note:** `GlobalErrorHandler.kt` now exists in `core:data/error/` (partially addresses crash observability in Phase 1.6).

Add a shared in-memory Room + Hilt test rule and fake `FileManager`/`RemoteAssetFetcher`. Create test suites for:

- `KnowledgeRepository` (1544 lines) — largest, highest priority
- `QuizRepository` (670 lines)
- `AssetRepository` (592 lines)
- `BookRepository`, `StudyRepository`, `WorkspaceRepository` (lower priority)

### 1.2 Migration Test Completeness

**Status:** 🟡 WRITTEN, NOT YET RUN — coverage now extends to v33.

- ~~Add `Migration26To27Test`, `Migration27To28Test`, `Migration28To29Test`~~ ✅ COMPLETED
- `Migration29To30Test` also exists (covers v29→v30: adds `resultTaxonomy` to sessions)
- ~~Missing: `Migration30To31Test`, `Migration31To32Test`, `Migration32To33Test`~~ ✅ WRITTEN
  (2026-07-20). `Migration32To33Test` asserts both failure modes that would otherwise pass
  silently: soft-deleted content leaking into the index, and live content missing from it. It also
  exercises `MATCH` rather than only `SELECT`, since FTS tokenization is the point of the table.
- ⚠️ **These are instrumented tests and have only been compile-verified.** The dev machine has no
  emulator, AVD, or system image installed, so `connectedAndroidTest` cannot run. Install a system
  image (`sdkmanager "system-images;android-34;google_apis;arm64-v8a"`), create an AVD, then run
  `./gradlew :core:database:connectedDebugAndroidTest` before trusting them.
- Add one full `MigrateAll1To33Test` chain test (validates end-to-end migration integrity, catches dropped columns like `source_document_assets` in v29)
- Fill gap coverage: `1→15`, `17→22` (may not exist in real deployments)

### 1.3 Repository Hygiene (Scripts Cleanup)

**Status:** 🔴 OPEN (~25 scripts remain in root)

Move all to `scripts/legacy/`:

- Python: `add_hilt*.py`, `fix_*.py`, `patch_*.py`, `update_*.py`, `test_plugins*.py` (6+)
- Kotlin: `query_books.kt`, `test_parser.kt`
- Reports: `duplications_report.txt`, `build_core_data.sh`, `mks_repair_diff.patch`

Document in `CONTRIBUTING.md` for onboarding clarity. Add `.gitignore` entries for generated reports.

### 1.4 Finish AppModule Decoupling (Phase 2 Preparation)

**Status:** 🟡 IN PROGRESS (3 coupling points remain)

After migrating `ReviewRepository`, the 4 preview/audit services, and `applicationScope` to `@Inject`/Hilt-provided, `AppModule` can be deleted and Room builder folded into `HiltDataModule.provideDatabase()`.

Acceptance gate: `grep -r "AppModule"` must return only the database-builder owner.

### 1.5 Static Analysis & Coverage Gates in CI

**Status:** 🟡 IN PROGRESS (CI exists, Detekt 1.23.6 + Ktlint 12.1.1 configured)

- ~~Add **detekt + ktlint** linting to CI pipeline~~ ✅ CONFIGURED (Detekt 1.23.6, Ktlint 12.1.1)
- Add **Kover** coverage thresholds (fail under 40% on `core/data`)
- Add **Room schema-export diff check** (fail if `core/database/.../schemas/30.json` out of date vs. entities)

### 1.6 Crash & Observability

**Status:** 🔴 OPEN

- Add global `Thread.setDefaultUncaughtExceptionHandler` funneling through `MksLogger`
- Convert silent `runCatching{}.getOrNull()` patterns to `Result<T>`/sealed outcomes surfaced to UI (image download, import paths)

---

## Phase 2: AppModule Cleanup & Full Hilt Migration

**Status:** 🟡 BLOCKED on Phase 1 completion

---

## Phase 3–14: Feature & UX Enhancements

### Phase 3 (Existing)

Crash observability integration (links to Phase 1.6).

### Phase 4–6 (Existing)

Performance, debugging, dependency optimization.

### Phase 7: Global Search Filters & Highlighting

**Status:** 🟢 READY

- Add type-chip filter row to `GlobalSearchScreen`
- Add snippet highlighting in search results
- `GlobalSearchDao` already returns typed rows; UI layer only

### Phase 8: Spaced Repetition (SM-2 / FSRS)

**Status:** 🟢 READY (data layer in place)

- `FlashcardEntity.reviewMetrics` tracks count/interval/ease
- `KnowledgeStudySessionEntity` tracks timestamps and streaks
- Implement algorithm UI only

### Phase 9: Note Collections Activation

**Status:** 🟢 READY (schema exists, unused)

- `NoteCollectionEntity` already in v30 schema
- Wire up in UI: group notes by collection, add collection CRUD to dashboard
- Low-cost, high-perceived-value organization feature

### Phase 10: Import History with Undo

**Status:** 🟢 READY (data layer ready)

- Leverage `externalId` dedup + soft deletes
- Track imports as units, enable bulk undo

### Phase 11: Session Resumption

**Status:** 🟢 READY (sessions already persisted)

### Phase 12: Study Streaks & Weekly Report

**Status:** 🟢 READY (data layer complete)

- `KnowledgeStudySessionEntity`/`StudySessionEntity` track timestamps and streak counters
- Add UI layer: streak display on dashboard, weekly report generation and delivery

### Phase 13: Practice (Non-Scored) Mode

**Status:** 🟢 READY (session model supports it)

Trivial to add; removes anxiety for casual review.

### Phase 14: AI Deepening

**Status:** 🟢 READY (Ollama integration + prompt pipeline in place)

#### 14a. "Explain This Question" One-Tap

Reuse existing Ollama + prompt-rendering pipeline. Lowest-effort, highest-delight AI feature.

#### 14b. Auto-Generate Assets from Quiz

`KnowledgeRepository.convertPromptOutputTo*()` plumbing already exists. Expose as Magic Action:

- Generate flashcards from quiz questions
- Auto-draft notes from quiz explanations
- Create slides from quiz question/answer pairs

---

## Suggested Implementation Priority

1. 🔴 **Phase 1.1–1.3** (Repository tests, migration tests, script cleanup) — Safety net & hygiene
2. 🔴 **Phase 1.5–1.6** (CI extensions, crash observability) — Code quality gates
3. 🔴 **Phase 1.4** (AppModule decoupling) — Dependency cleanup
4. 🟡 **Phase 2** (Full Hilt migration) — Foundational cleanup
5. 🟢 **Phase 7, 9, 12, 14a** (Search filters, note collections, streaks, explain feature) — High-ROI UX wins
6. 🟢 **Phase 8, 10, 13, 14b** (Spaced repetition, import undo, practice mode, asset generation) — Engaging features

---

## Infrastructure Status

| Item | Status | Notes |
|------|--------|-------|
| Version Catalog (`gradle/libs.versions.toml`) | ✅ DONE | In use |
| CI Pipeline (`.github/workflows/android-ci.yml`) | ✅ DONE | Runs lint→test→build; can be extended |
| Static Analysis (Detekt + Ktlint) | ✅ DONE | Detekt 1.23.6, Ktlint 12.1.1 configured |
| Repository Hygiene (root scripts) | 🔴 OPEN | ~25 scripts need moving to `scripts/legacy/` |
| Migration Tests (v26→v30) | ✅ DONE | v26→v27, v27→v28, v28→v29, v29→v30 all tested |
| Repository Test Suite | 🔴 OPEN | Zero repository tests |
| AppModule Decoupling | 🟡 IN PROGRESS | 3 coupling points remain |
| Crash Observability | 🟡 IN PROGRESS | `GlobalErrorHandler.kt` exists in `core:data/error/`; Result<T> patterns still needed |
