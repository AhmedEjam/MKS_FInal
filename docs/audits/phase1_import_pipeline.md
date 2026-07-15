# Phase 1 — Import Pipeline Review (File → Persist)

> Scope: the complete import journey — file selection → format detection → parse → normalize → validate → map → persist to Room, plus image resolution and asset-reference tracking. Files read: `ImportLibraryManager`, `CompilerViewModel`, `CompilerDialog`, `ImportViewModel`, `FileManager`, `ImportValidator`, `BundleNormalizer`, `ImportLimits`, `SpreadsheetQuestionParser`, `AssetRepository`, plus 18 parser/xlsx/mapper/zip/dto/detector files (2 subagent deep-reads). `build/` excluded. All refs are `file:line`; abbreviated paths use `...` = `src/main/java/com/ahmedyejam/mks`.

## TL;DR
1. The pipeline is architecturally sound: a single `executeImportPipeline()` inside one Room `withTransaction` (ImportLibraryManager.kt:537) with progress, per-record try/catch, and skip-not-fail semantics.
2. **Highest-severity defects are silent data corruption/loss, not crashes:** 0-vs-1-based answer-index ambiguity across 3 answer resolvers, XLSX inline images sanitized to `null` on persist, HTML nested-JSON truncation, dropped correct-answer refs, and the CompilerDialog "preview shows it, save silently drops it" desync.
3. **Two security holes:** unhardened XML parsing (XXE/entity-expansion) in XlsxImageResolver, and an encrypted-ZIP zip-bomb bypass that trusts attacker-declared sizes.
4. Pervasive duplication: `replaceOwnerAssetReferences` + `isTrackableLocalAsset` exist verbatim in two classes with **divergent** allow-lists; answer resolution + image extraction + category splitting reimplemented per parser.
5. Zero unit tests on the orchestration engine, mapper, and FileManager — the three most dangerous files to leave untested.

---

## §1 End-User Lens — "I selected a spreadsheet and imported it"

**The happy path the user sees** (traced through the two-flow dispatch, IMPORT_INPUT_PATHS.md §1C):

1. User taps Library FAB → "Import" → system file picker (`OpenDocument("*/*")`). *(Origin of input: a `content://` URI.)*
2. `handleImportUri()` routes by detected format. XLSX/CSV/JSON/HTML/TEXT → `CompilerViewModel.onFileSelected()` → `CompilerDialog`; ZIP → `ImportViewModel.getImportPreview()` → review dialog. The user is not told which flow they're in.
3. For XLSX: file is copied to `cacheDir/import_<millis>.<ext>` (CompilerViewModel.kt:119), workbook opened, **all sheet names listed**, first sheet auto-selected (CompilerViewModel.kt:217-228).
4. Header row auto-detected by scoring the first ~10 rows (CompilerViewModel.kt:255-306). Column mapping + option columns guessed. **User can adjust header row and mapping** (updateHeaderRow / updateMapping).
5. Parsed questions render live in a `LazyColumn` (CompilerDialog.kt:149) with per-question include toggles, correct-answer highlighting (green, CompilerDialog.kt:356), and a mapping editor.
6. User taps Save → title + target book/quiz/deck dialog (CompilerDialog.kt:214-301) → `saveParsedQuestions()` → either `KnowledgeRepository.insertFlashcards()` (deck target) or `quizRepository.importCompiledQuestions()` → engine.

**Friction points, dead-ends, and failure modes a real user hits:**

- **F1 — "My question disappeared after saving." (High).** The preview shows every parsed row, including ones with no identified correct answer (SpreadsheetQuestionParser.kt:71 flags the issue but keeps the row; CompilerDialog.kt:356 just doesn't highlight anything). But on save, the quiz path runs through `ImportValidator`, which **skips** any question with `no correct answer IDs` / `no options` / `blank stem` (ImportValidator.kt:115, :105, :104). The row silently vanishes into `skippedRecords`; the only signal is a count in `ImportResult.skippedRecordsCount` — and `CompilerViewModel.saveParsedQuestions()` **discards the result entirely** (CompilerViewModel.kt:509-517 ignores the return of `importCompiledQuestions`). Net: preview-vs-persist desync with zero user feedback.
- **F2 — Legacy `.xls` import always fails. (High).** `.xls` is mapped to `ImportFormat.XLSX` (ImportFormatDetector.kt:23) and routed to `XlsxLibraryCompiler.compile()`, which opens it as a `ZipFile` (XlsxLibraryCompiler.kt:58). OLE2 `.xls` is not a ZIP → `ZipException` → whole import aborts with a raw message. The user with an old Excel file just sees "Import failed."
- **F3 — Marked-answer spreadsheets can lose the correct answer. (High).** When the answer column is blank and correctness is indicated by a `*`/`✓` in the option cell, `resolveCorrectAnswers` only falls back to marked options *after* text/letter matching fails (SpreadsheetQuestionParser.kt:214-219). But the marker char is **never stripped from the option text** — `isMarked` detects it (SpreadsheetQuestionParser.kt:144) yet the option `text` still contains `*Paris`, so an answer-column value of `Paris` fails the exact-text match (SpreadsheetQuestionParser.kt:197) and the option displays with a literal asterisk to the user.
- **F4 — Wrong header row on banner spreadsheets. (Medium).** Header scan is capped at the first 10 rows (CompilerViewModel.kt:253, XlsxLibraryCompiler.kt:76). A sheet with a title banner + blank spacer rows pushing the header past row 10 falls back to row 0 → garbage column mapping → user must manually fix, if they notice.
- **F5 — Large-file silence. (Medium).** XLSX up to 50 MB is copied then fully materialized by POI before any row/cell limit is checked (XlsxLibraryCompiler.kt:60-95). On a big or decompression-bomb file the app can ANR/OOM during `WorkbookFactory.create()` with no progress and no timeout — the spinner just sits.
- **F6 — Plain-HTTP images silently skipped. (Low/expected).** By default HTTP image URLs are not downloaded; the user must find and tick "Allow insecure image downloads" in the ZIP review dialog (ImportLibraryManager.kt:1288). For the CompilerDialog (non-ZIP) flow there is **no such checkbox**, so HTTP images are always dropped with only a per-warning that the UI discards.
- **F7 — HTML import mostly fails. (Medium).** Embedded-JSON extraction uses non-greedy brace matching that truncates at the first `}`/`]` (HtmlQuestionParser.kt:12), so realistic nested quiz JSON never parses; the user gets a misleading "No embedded quiz JSON found."
- **F8 — No undo after import.** Once persisted there is no "undo last import"; a bad mapping that imported 300 malformed questions must be cleaned up manually via trash.

---

## §2 Senior-Developer Lens

### Data-flow diagram (actual, verified)
```
content:// URI
  │  ImportFormatDetector.detectFormat()  [extension → magic-bytes fallback]   (ImportFormatDetector.kt:17,62)
  ▼
┌─ CompilerViewModel (XLSX/CSV/JSON/HTML/TEXT) ─────────────┐   ┌─ ImportViewModel (ZIP) ──────────┐
│ prepareTempFile → WorkbookFactory / CsvParser / textParser │   │ getImportPreview → ZipLibraryParser │
│ SpreadsheetHeaderMapper.calculateRowScore/mapHeaders       │   │ (zip4j AES / java.util.zip)         │
│ SpreadsheetQuestionParser.parseRow → ParsedQuestion[]      │   │ → LibraryBundleDto + manifest       │
│ (live preview; user edits mapping/answers/inclusion)       │   └─────────────┬────────────────────┘
└───────────────┬───────────────────────────────────────────┘                 │
                │ saveParsedQuestions (isIncluded filter)                       │
                ▼                                                               ▼
        quizRepository.importCompiledQuestions ─────────────► ImportLibraryManager.importLibrary/importQuestions
                                                                     │
                                                     wrapQuestionsToBundle → LibraryBundleDto
                                                                     ▼
                                              executeImportPipeline()  (ImportLibraryManager.kt:500)
                                              1. ImportValidator.validate  [skips bad Qs]  (:516)
                                              2. BundleNormalizer.normalize [trim, infer mode] (:534)
                                              3. database.withTransaction {                 (:537)
                                                   workspace → categories → books → quizzes
                                                   → questions → sessions → flashcards →
                                                   slides → notes → prompts → studySessions
                                                   → sourceDocs → questionAssets → annotations
                                                   resolveImagePath() per entity  (:1174)
                                                   replaceOwnerAssetReferences() per entity (:136)
                                                 }
                                              4. refreshBookStats / refreshQuizStats (:1134,:1155)
                                                     ▼
                                                  Room (LibraryMapper DTO→Entity)
```

### Correctness risks
- **C1 — 0-vs-1-based answer index (Critical, data corruption).** Three independent resolvers assume different bases and none detects the source base: `JsonQuestionParser.resolveCorrect` treats `"1"` → `opt_1` = the *second* option (JsonQuestionParser.kt:130); `JsonLibraryParser` maps index → `opt_$idx` raw (JsonLibraryParser.kt:187); `SpreadsheetQuestionParser.resolveCorrectAnswers` uses `part.toInt()-1` (SpreadsheetQuestionParser.kt:191, correct). Sources exporting 1-based answer indices import with the **wrong option marked correct**, silently.
- **C2 — XLSX inline images dropped on persist (Critical).** `XlsxLibraryCompiler` produces `QuestionDto.imageDataUrl` (data URL). But `LibraryMapper.mapToQuestionEntity` funnels `imageDataUrl` into the `rawSource` fallback (LibraryMapper.kt:93) which is then sanitized to `null` precisely because it `startsWith("data:")` (LibraryMapper.kt:95-99). Meanwhile `ImportLibraryManager.resolveImagePath` *does* handle `data:` (ImportLibraryManager.kt:1184) — but the mapper runs on the DTO before the entity is built, so the resolved path must arrive via a separate `imagePath`. Result: resolved embedded XLSX cell images can be lost. Also the export/import direction is asymmetric — export writes a *path* into the *data-URL* field (LibraryMapper.kt:236).
- **C3 — Dropped correct-answer references (High).** `mapToQuestionEntity` resolves `dto.correct` via `options.indexOfFirst { it.id == correctId }` and `mapNotNull` (LibraryMapper.kt:87-90); any unmatched ID is dropped with no warning → question silently becomes "no correct answer" → then skipped by the validator on the next round-trip.
- **C4 — Marked-cell text not stripped (High).** See F3; `text` retains the marker so text-match fails and the marker shows in the UI (SpreadsheetQuestionParser.kt:42-47, :197).
- **C5 — Header mapper `indexOf(cell)` bug (Medium).** Inside `indexOfFirst`, the current index is recomputed with `headerRow.indexOf(cell)` (SpreadsheetHeaderMapper.kt:88,97,113), returning the *first* index of a duplicate/blank cell → wrong column claimed/skipped when headers repeat (two "Answer" columns, multiple blanks). Also O(n²).
- **C6 — Option over-capture (Medium).** When `answer` column is unmapped, `guessOptionColumns` uses `aIdx = headerRow.size` so all trailing columns become options (SpreadsheetHeaderMapper.kt:163-173), swallowing explanation/reference columns that failed alias matching.
- **C7 — Non-deterministic timestamps break idempotency (Medium).** ~30 `?: System.currentTimeMillis()` defaults across LibraryMapper (e.g. :51-53,:328,:667) mean re-importing the same bundle yields different `createdAt/updatedAt` each time — dedup/merge that keys on timestamps can't be idempotent.

### Error handling & rollback
- **E1 — Transaction integrity is good.** All persistence is inside one `database.withTransaction` (ImportLibraryManager.kt:537). A throw rolls back the whole import. Per-record failures are caught and converted to warnings + `skippedRecordsCount++` (e.g. :644,:809) so one bad row doesn't abort the batch. **However**, `resolveImagePath` writes image files to disk *inside* the transaction (FileManager writes at ImportLibraryManager.kt:1202,:1263,:1307); a rollback leaves **orphaned image files** on disk (DB rows gone, files remain) — no compensating cleanup.
- **E2 — Temp-file cleanup mostly correct but leaky on partial copy.** ZIP `rootDir` deleted in `finally` (ImportLibraryManager.kt:368); `cleanupStaleImportCache()` reaps `import_*` older than 24h (:113). But `XlsxLibraryCompiler.prepareTempFile` leaks the created cache file if `copyToWithLimit` throws mid-copy (XlsxLibraryCompiler.kt:37-47). CompilerViewModel deletes `tempFile` only in `onCleared` (:100) — if the process dies first, it lingers until the 24h sweep.
- **E3 — Result discarded on the primary UI path.** `CompilerViewModel.saveParsedQuestions` ignores the `MksResult`/`ImportResult` from `importCompiledQuestions` (CompilerViewModel.kt:509) — warnings, skip counts, and errors never reach the user (root cause of F1). `ImportViewModel` handles the result correctly (ImportViewModel.kt:66-78), showing the two flows are inconsistent.

### Security
- **S1 — XXE / entity-expansion (Critical).** `XlsxImageResolver.parseXml` builds a `DocumentBuilderFactory` with only `isNamespaceAware=true` — no `FEATURE_SECURE_PROCESSING`, no `disallow-doctype-decl`, no external-entity disabling (XlsxImageResolver.kt:26-29,:257). Every XML part of an untrusted xlsx is parsed here → billion-laughs DoS (OOM/CPU) and potential local-file exfiltration on JVMs that resolve external entities.
- **S2 — Encrypted-ZIP zip-bomb bypass (High).** The encrypted extraction path validates entry count/size against **attacker-declared** `header.uncompressedSize`, then extracts with `zipFile.extractFile()` which enforces no byte cap (ZipLibraryParser.kt:60-86). A crafted archive declaring small sizes but inflating large evades `MAX_SINGLE_FILE_SIZE`/`MAX_TOTAL_SIZE`. The plain path is correctly hardened via `copyToWithLimit` on *actual* bytes (ZipLibraryParser.kt:170) — the encrypted path should mirror it.
- **S3 — Positive controls.** Path-traversal defenses (canonical containment, `..` rejection) exist in both ZIP paths (ZipLibraryParser.kt:81,:167) and in `resolveRelativeAssetPath` (ImportLibraryManager.kt:1325-1341); FileManager rejects absolute paths outside the images dir (FileManager.kt:155-162) and validates asset dirs (:132). Image normalization caps dimensions/pixels/bytes and re-encodes to WEBP (FileManager.kt:324-376) — good defense against decompression bombs at the image layer.

### Thread / coroutine correctness
- File I/O and parsing are correctly off the main thread: `ImportLibraryManager` wraps everything in `withContext(Dispatchers.IO)` (:171,:298,:382); `CompilerViewModel` uses `Dispatchers.IO` for loading and `Dispatchers.Default` for CPU parsing (:245,:341,:407). No main-thread blockers found. PptxSlideParser correctly uses `Dispatchers.IO`.
- **T1 — No cancellation/timeout on large parses.** POI `WorkbookFactory.create` and the row loop aren't cooperatively cancellable; backing out of the dialog leaves work running until it finishes or OOMs.

### State desync
- **D1 — CompilerUiState preview vs persisted (confirmed).** The preview is the source of truth for the UI, but persistence re-runs validation/normalization on the server side (ImportLibraryManager.kt:516,:534) which can drop rows the preview showed (F1/C3). The two never reconcile because the result is discarded (E3).

---

## §3 Recommendations

### Potential Improvements
- **I1 (P0):** Surface the `ImportResult` on the CompilerViewModel save path — show "N imported, M skipped (reasons)" and route warnings to the user (fixes F1). CompilerViewModel.kt:509.
- **I2 (P1):** In the preview, badge questions the validator *will* skip (no correct answer / no options) with a distinct color + inline reason, so the user fixes them before saving. Wire `ParsedQuestion.issues` (already populated, SpreadsheetQuestionParser.kt:69) into CompilerDialog.kt:356.
- **I3 (P1):** Add the "Allow insecure HTTP images" affordance to the CompilerDialog flow, not just ZIP (F6).
- **I4 (P2):** Progress + cancel for large XLSX; show row-count/size before parse and a determinate bar (F5, T1).
- **I5 (P2):** "Undo last import" using `affectedBookIds`/`affectedQuizIds` already returned by `ImportResult` (ImportLibraryManager.kt:1117-1118) — trivial to build a one-tap rollback.

### Features to Add
- **FA1:** Saved column-mapping presets keyed by header signature — re-importing a familiar spreadsheet layout auto-applies the last good mapping.
- **FA2:** Batch multi-file import (`OpenMultipleDocuments`) with a queue and per-file result summary; manifest already supports it via share intent (IMPORT_INPUT_PATHS.md §1A).
- **FA3:** Drag-and-drop reordering of option columns in the mapping editor.
- **FA4:** Dry-run/validate-only mode that runs the validator and shows the skip report without persisting.

### Functions to Maturize
- **M1:** `TextQuestionParser` continuation-line handling is a documented stub (TextQuestionParser.kt:43-50, empty body) → multi-line options/prose silently dropped; and its `^[A-Z])` option regex false-matches stems like "A patient presents…" (TextQuestionParser.kt:71). Both need real handling.
- **M2:** `HtmlQuestionParser` nested-JSON extraction (HtmlQuestionParser.kt:12) must brace-match (depth counter or a real JSON scanner), not non-greedy regex.
- **M3:** Unify answer resolution into one `AnswerResolver(idScheme, indexBase)` with an explicit/ inferred index-base policy (fixes C1 across all three resolvers).
- **M4:** Marker-stripping: strip `*/✓/☑` from option `text` after setting `marked` (SpreadsheetQuestionParser.kt:42) so text-match and display are clean (C4/F3).
- **M5:** `PptxSlideParser` unguarded POI load with no size cap (PptxSlideParser.kt:21) — wrap + bound.

### Refactor Opportunities
- **R1 (High):** `replaceOwnerAssetReferences` + `isTrackableLocalAsset` are **duplicated verbatim** in ImportLibraryManager.kt:136/126 and AssetRepository.kt:95/602 — and the allow-lists **diverge**: the importer excludes `assets/` (ImportLibraryManager.kt:133) while AssetRepository excludes `file:///android_asset/` (AssetRepository.kt:609). Extract one `AssetReferenceTracker` to eliminate the drift bug.
- **R2:** Route all image extraction through `GenericImageExtractor`; `JsonLibraryParser` reimplements image aliasing and bypasses it (JsonLibraryParser.kt:210).
- **R3:** Enforce ImportLimits (rows/cells/sheets) *before* POI fully loads the workbook (stream/SAX pre-check), closing the OOM window (XlsxLibraryCompiler.kt:60-95).
- **R4:** Factor a common `TextAssetParser` base for the alternating-paragraph + label-extraction logic duplicated across TextFlashcardParser/TextSlideParser/TextArticleParser (each independently drops the trailing unpaired item).
- **R5:** Give every parser a shared `Parser<T>` interface + typed `ImportError` instead of raw `throw Exception(...)` (HtmlQuestionParser.kt:28, ImportLibraryManager.kt:173).

### Testing Gaps (most dangerous untested surfaces)
- **TG1 — `ImportLibraryManager.executeImportPipeline` (0 tests, ~1065 LOC, the whole persist path).** Most dangerous file in the phase. Needs an instrumented Room test per merge strategy (SKIP/OVERWRITE/DUPLICATE) asserting: idempotent re-import (catches C7), OVERWRITE preserves study stats (verified in code at :781-796 — lock it with a test), rollback leaves no orphan image files (catches E1), and skip-count accuracy (catches F1). Extend `ImportReconciliationTest`.
- **TG2 — `LibraryMapper` round-trip (0 tests).** Property test: entity → DTO → entity must be lossless for `imagePath`, `correct`, categories (catches C2/C3). Add `LibraryMapperRoundTripTest`.
- **TG3 — Answer resolution 0/1-based (partial).** `SpreadsheetHeaderMapperTest` exists; add `AnswerResolverTest` covering `"1"`, `"A"`, `"1,3"`, numeric-vs-letter, and marked-only for all three current resolvers (catches C1).
- **TG4 — `FileManager` image path security (has `FileManagerTest`, extend).** Add cases: absolute path outside images dir rejected (FileManager.kt:155), oversized base64 rejected (:53), and rollback orphan cleanup.
- **TG5 — Security regression tests.** Add an XXE payload xlsx (S1) and a size-lying encrypted zip (S2) to the instrumented suite; both currently pass silently.

---

## Machine-readable recommendation table

| id | category | severity | title | file:line | fix effort |
|---|---|---|---|---|---|
| F1/E3 | Bug/Fix | P0 | Import result (skips/warnings) discarded on Compiler save path | feature/ui/.../ui/quiz/CompilerViewModel.kt:509 | S |
| C2 | Bug/Fix | P0 | XLSX inline images sanitized to null on persist; export/import image field asymmetry | core/data/.../importer/mapping/LibraryMapper.kt:93 | M |
| S1 | Security | P0 | Unhardened XML parsing (XXE/entity-expansion) in XLSX image resolver | core/data/.../importer/xlsx/XlsxImageResolver.kt:26 | S |
| S2 | Security | P0 | Encrypted-ZIP zip-bomb bypass (trusts declared sizes) | core/data/.../importer/parser/ZipLibraryParser.kt:60 | M |
| C1/M3 | Bug/Fix | P0 | 0-vs-1-based answer index ambiguity across 3 resolvers | core/data/.../importer/parser/JsonQuestionParser.kt:130 | M |
| C3 | Bug/Fix | P1 | Correct-answer refs silently dropped on ID mismatch | core/data/.../importer/mapping/LibraryMapper.kt:87 | S |
| C4/M4 | Bug/Fix | P1 | Marked-cell marker not stripped from option text | core/data/.../importer/parser/SpreadsheetQuestionParser.kt:42 | S |
| F2 | Bug/Fix | P1 | Legacy .xls routed to ZIP-based compiler → total failure | core/data/.../importer/detector/ImportFormatDetector.kt:23 | M |
| M2 | Maturize | P1 | HTML nested-JSON extraction truncates at first brace | core/data/.../importer/parser/HtmlQuestionParser.kt:12 | M |
| M1 | Maturize | P1 | TextQuestionParser continuation-line stub + false-positive options | core/data/.../importer/parser/TextQuestionParser.kt:43 | M |
| E1 | Bug/Fix | P1 | Rollback leaves orphaned image files (writes inside txn) | core/data/.../importer/repository/ImportLibraryManager.kt:1202 | M |
| R1 | Refactor | P1 | Duplicated asset-ref helpers with divergent allow-lists | core/data/.../importer/repository/ImportLibraryManager.kt:126 | S |
| C5 | Bug/Fix | P2 | Header mapper indexOf(cell) picks wrong column on duplicates | core/data/.../importer/parser/SpreadsheetHeaderMapper.kt:88 | S |
| C7 | Bug/Fix | P2 | Non-deterministic timestamps break re-import idempotency | core/data/.../importer/mapping/LibraryMapper.kt:51 | S |
| R3 | Performance | P2 | XLSX limits enforced after POI full load → OOM window | core/data/.../importer/xlsx/XlsxLibraryCompiler.kt:60 | M |
| I2 | Improvement | P2 | Badge validator-skip questions in preview | feature/ui/.../ui/quiz/CompilerDialog.kt:356 | S |
| I5 | Feature | P2 | Undo-last-import using returned affected IDs | core/data/.../importer/repository/ImportLibraryManager.kt:1117 | M |
| R2 | Refactor | P2 | JsonLibraryParser bypasses GenericImageExtractor | core/data/.../importer/parser/JsonLibraryParser.kt:210 | S |
| FA1 | Feature | P3 | Saved column-mapping presets by header signature | feature/ui/.../ui/quiz/CompilerViewModel.kt:396 | M |
| FA2 | Feature | P3 | Batch multi-file import | feature/ui/.../ui/importer/ImportViewModel.kt:44 | M |
| TG1 | Testing | P1 | No tests on executeImportPipeline (idempotency/rollback/skip) | core/data/.../importer/repository/ImportLibraryManager.kt:500 | M |
| TG2 | Testing | P1 | No LibraryMapper round-trip test | core/data/.../importer/mapping/LibraryMapper.kt:1 | S |
| TG5 | Testing | P1 | No XXE / zip-bomb security regression tests | core/data/.../importer/xlsx/XlsxImageResolver.kt:26 | M |
