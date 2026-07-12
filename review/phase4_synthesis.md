# Phase 4 — Synthesis, Cross-Pipeline Map & Prioritized Roadmap

> Consolidates `review/phase1_import_pipeline.md`, `review/phase2_study_pipelines.md`, `review/phase3_aux_pipelines.md` against the intended-journey docs (`user_Jour_Geminipro.md`, `USER_JOURNEY_MAP_claudeopus.md`) and `mks_enhancement_plan.md`. No source re-read. Refs `file:line`; `...` = `src/main/java/com/ahmedyejam/mks`. **§4 (Prioritized Improvement Roadmap) is the primary deliverable.**
>
> **⚠️ UPDATE 2026-07-12 — Round-1 fixes applied and reviewed. See [§6 Applied-Changes Review](#6-applied-changes-review--round-1) at the bottom. The build is currently RED (`:feature:ui:compileDebugKotlin` fails, 3 errors); two are from the applied roadmap edits, one is a pre-existing/theme-related missing-drawable issue. Fix those three before anything else — details and exact patches in §6.**

## Executive Summary (10 lines)
1. MKS is a genuinely capable, feature-rich offline knowledge app with sound module boundaries (UI→VM→Repo→DAO→Room) and a well-designed import engine and export/exchange format.
2. Its dominant risk class is **silent data corruption/loss**, not crashes: wrong answers marked correct on import, XLSX images dropped, adaptive-training progress lost on kill, knowledge-bank "resume" that never restores.
3. Two P0 security holes span the app: SSRF in the remote asset fetcher and plaintext API-key storage; two P0 XML/ZIP parsing holes (XXE, encrypted zip-bomb) sit in the import path.
4. One shipping **blocker**: quiz Edit is dead (a misplaced brace nests the dialog inside the trash-bin block).
5. Three god-objects (QuizViewModel, BookToolsViewModel, LibraryViewModel) concentrate risk and defeat Compose skippability.
6. The single biggest structural gap is **zero repository/engine tests** — the enhancement plan agrees (Phase 1.1, "zero repository tests").
7. Several "READY/DONE" claims in the enhancement plan and journey docs are contradicted by the code: Session Resumption, workspace isolation, language switching, 7-type review queue.
8. The infrastructure layer (FCM token sync, notifications, RemoteConfig cold-start) is scaffolding, not wired.
9. Roughly a dozen small, high-ROI fixes (brace, toast-lie, language apply, review queue types) would materially lift perceived quality within one sprint.
10. The roadmap below sequences P0 correctness/security first, then a test safety net, then the god-object refactors that unblock everything else.

---

## §1 End-User Verdict

**Full-journey narrative.** A new user lands on Welcome, picks a language (which *doesn't actually switch the UI* — `[Settings]`), and reaches the Library — the hub for books, quizzes, and the knowledge bank. They import a spreadsheet through a polished multi-step compiler with live preview; the import mostly works, but some questions silently vanish on save and some correct answers land on the wrong option `[Import]`. They study: quizzes persist progress well and resume accurately; flashcards and slideshows *look* like they save a session but always restart from card 1 `[Study]`. They generate AI MCQs and extract PDF text — powerful, but failure states are opaque (infinite spinners, "saved" toasts that didn't save) `[AI]`. They export a book to ZIP and re-import it — most data round-trips, but images and some answers degrade `[Export]`. Multi-workspace isolation holds for the library but leaks in global search `[Sync]`.

**Top 10 frictions, ranked by severity:**
1. **`[Study]` Quiz Edit does nothing.** Tapping Edit on a quiz renders no dialog — a misplaced brace (LibraryScreen.kt:628). *Blocker.*
2. **`[Import]` Questions silently disappear on save.** The preview shows rows the validator later skips; the Compiler save path discards the result so the user gets no "N skipped" feedback (CompilerViewModel.kt:509).
3. **`[Import]` Wrong option marked correct.** 0-vs-1-based answer-index ambiguity across three resolvers imports the wrong answer with no signal (JsonQuestionParser.kt:130).
4. **`[Study]` Adaptive training loses all progress on kill.** `sessionId=null` → no session row (QuizViewModel.kt:484). Contradicts the journey doc's "saves progress automatically."
5. **`[Study]` "Resume where you left off" doesn't work** for flashcards/slideshows — state is serialized but never read back (FlashcardDeckViewModel.kt:159). Contradicts enhancement-plan Phase 11 ("READY").
6. **`[Study]` Summary can show a card as WRONG under the "Correct" filter** — three answer ledgers disagree on repeats (SummaryViewModel.kt:150).
7. **`[AI]` Encrypted/corrupt PDF → infinite spinner**, no error (PdfExtractionViewModel.kt:69); and Cancel permanently bricks generation progress (AiMcqGeneratorViewModel.kt:137).
8. **`[Settings]` Language change doesn't apply** until manual restart (SettingsScreen.kt:330). Journey doc claims "instantly re-renders."
9. **`[Sync]` Global search leaks across workspaces** — returns other workspaces' content (GlobalSearchDao.kt). Journey doc claims each workspace is "isolated."
10. **`[Export]` Round-trip degrades images/answers** — mapper writes a path into the data-URL field and drops unmatched answer refs (LibraryMapper.kt:93,87).

**Intended-vs-observed divergences (from journey/enhancement docs):**
- "Language instantly switches, entire UI re-renders" (geminipro:13, claudeopus:49) → **written but not applied** (A9).
- "Session Resumption — READY, sessions already persisted" (enhancement Phase 11) → **true only for quizzes; knowledge-bank resume never reads state** (U6).
- "Each workspace has its own isolated set of books/quizzes/knowledge data" (claudeopus:780) → **library isolates, global search does not** (AUX1).
- "Unified review queue... 7 queue types" (AGENTS.md, marketing) → **only 5 types built; SLIDE + ANNOTATION missing** (AUX4). Both journey docs actually describe only 5, matching code — the "7" claim is aspirational.
- "Back Arrow: saves session progress automatically" (geminipro:89) → **true for session quizzes, false for adaptive** (U1).

---

## §2 Senior-Developer Verdict — Top 10 Architectural Risks

| # | Risk | Category | file:line | One-line remediation |
|---|---|---|---|---|
| 1 | SSRF: remote fetcher allows private-IP/localhost, doesn't re-validate redirects, checks plain-HTTP consent on initial scheme only | Security | core/network/.../network/RemoteAssetFetcher.kt:19 | Add IP-range denylist + per-redirect re-validation; enforce consent on final scheme |
| 2 | API keys in plaintext Preferences DataStore; Bearer sent over http | Security | core/data/.../preferences/DataStoreManager.kt:381 | Move to EncryptedDataStore/Keystore; warn on non-TLS base URL |
| 3 | Unhardened XML parsing (XXE/entity-expansion) on untrusted xlsx; encrypted-ZIP zip-bomb trusts declared sizes | Security | core/data/.../importer/xlsx/XlsxImageResolver.kt:26 | Disable DOCTYPE/external entities; bound encrypted extraction on actual bytes |
| 4 | Non-atomic session read-modify-write race; concurrent submits clobber answers/streak | Data-integrity | feature/ui/.../ui/quiz/QuizViewModel.kt:1060 | Single-writer funnel + atomic partial `@Update` |
| 5 | Three parallel answer ledgers (answers/answersByIndex/resultTaxonomy) drift on repeats | SSOT | feature/ui/.../ui/quiz/QuizViewModel.kt:1064 | Collapse to one canonical per-attempt list, derive the rest |
| 6 | Adaptive training persists nothing (sessionId=null) | Missing persistence | feature/ui/.../ui/quiz/QuizViewModel.kt:484 | Create a session (reuse SessionEntity/StudySessionEntity) |
| 7 | Knowledge-bank resume state written but never deserialized; Moshi failures → empty stateJson | Missing persistence | feature/ui/.../ui/flashcard/FlashcardDeckViewModel.kt:159 | Deserialize on load or delete the dead write; surface Moshi errors |
| 8 | Import writes image files inside the DB transaction → rollback orphans files on disk | Data-integrity / error-handling | core/data/.../importer/repository/ImportLibraryManager.kt:1202 | Stage files, commit refs in txn, cleanup on rollback |
| 9 | Duplicated asset-ref helpers with **divergent** allow-lists (import excludes `assets/`, repo excludes `file:///android_asset/`) | SSOT / correctness | core/data/.../importer/repository/ImportLibraryManager.kt:126 | Extract one `AssetReferenceTracker` |
| 10 | Three god-objects with whole-`state` recomposition; AiClient retry uncancellable (Thread.sleep) | Thread/perf/maintainability | feature/ui/.../ui/quiz/QuizViewModel.kt:1 · core/network/.../network/AiClient.kt:260 | Split VMs; make retry suspend + cancel Call |

Cross-cutting: **zero repository/engine tests** (enhancement Phase 1.1) leaves every item above unguarded against regression.

---

## §3 Cross-Pipeline Data Map

| Pipeline | Origin of Input | Transforms | Final Entity/Table | Owner ViewModel | Owner Repository | Persistence Guarantee |
|---|---|---|---|---|---|---|
| Spreadsheet/CSV import | File URI | detect→parse→normalize→validate→map→resolveImage | QuestionEntity, QuestionCategoryEntity, AssetReferenceEntity | CompilerViewModel | ImportLibraryManager→Quiz/Book/Asset | On explicit save; **result discarded (no skip feedback)** |
| JSON/HTML/TEXT import | File URI | parser→ParsedQuestion→wrapToBundle→pipeline | QuestionEntity + knowledge entities | CompilerViewModel/ImportViewModel | ImportLibraryManager | On save; HTML nested-JSON often fails |
| ZIP/exchange import | File URI (AES) | ZipLibraryParser→V7Archive→validate→normalize→pipeline | Full graph incl. sessions/assets/annotations | ImportViewModel | ImportLibraryManager | One Room txn; **file writes leak on rollback** |
| Export → ZIP | Book/Quiz/Library id | DAOs→LibraryMapper→resolveLocalPaths→V7 archive (SHA-256) | mks_exchange.zip | DataTools/Settings VM | ExportManager | Atomic read txn; **image/answer field asymmetry** |
| Quiz play | User tap (select/submit) | applyAnswer→finalizeSubmission | SessionEntity (+question metrics, mistake log) | QuizViewModel | QuizRepository/StudyRepository | Per-answer; touches lastStudiedAt+stats; **non-atomic race** |
| Adaptive training | FocusManager selection | same as quiz play, sessionId=null | question metrics only | QuizViewModel | Study/QuizRepository | **No session persistence** |
| Quiz session (create/resume) | Session dialog / list | createSession→startQuiz repair | SessionEntity | SessionViewModel/QuizViewModel | Quiz/StudyRepository | Persisted; progress % computed vs live quiz size |
| Flashcard study | Flip/rate | rateFlashcard (SRS: dueAt/difficulty) | FlashcardEntity, FlashcardDeck (studiedCount) | FlashcardDeckViewModel | KnowledgeRepository | Rating durable; **session resume never restored** |
| Slideshow study | Swipe/mark-studied | toggleSlideStudied | CourseSlideEntity (isCompleted) | SlideshowCourseViewModel | KnowledgeRepository | Slide flag durable; **course aggregate stale until re-entry** |
| Note reader | Read/TTS/mark-reviewed | recordNoteReview | NoteBlueprintEntity (reviewCount) | BookToolsViewModel | KnowledgeRepository | Review durable; scroll/TTS position not saved |
| Prompt deck run | Variables + Ollama | render→stream→route output | PromptRunEntity → Note/Blueprint/Flashcard/Quiz | BookToolsViewModel | Knowledge/OllamaRepository | Run saved only on explicit "Save run"; output lost on rotation |
| AI MCQ generation | Source text | OCR→chunk→AiClient→parse→validate(none) | QuestionEntity (as quiz) | AiMcqGeneratorViewModel | AiMcqRepository→McqService→AiClient | Saved inside generateAndSave; **failures→emptyList silently** |
| OCR (camera) | Camera bitmap | ML Kit→regex→edit | QuestionEntity | ScannerViewModel | QuizRepository | On explicit import |
| OCR (AI vision) | PDF page bitmap | render→AiClient vision | text stream | PdfExtractionViewModel | AiMcqRepository/AiClient | Per-block; errors baked into text |
| PDF text extraction | Source URI | PdfBox extract | text → note/slides | PdfExtractionViewModel | (network service) | **Encrypted→infinite spinner; toast lies** |
| Preference change | Settings control | edit{} (sanitized) | DataStore (mks_settings) | SettingsViewModel | DataStoreManager | Atomic per key; **language not applied** |
| FCM token sync | onNewToken | cache→enqueue worker | DataStore FCM_TOKEN | — | — (TokenSyncWorker) | Cached locally; **worker is a no-op; never reaches backend** |
| RemoteConfig | Firebase | fetchAndActivate (2s) | in-memory StateFlow | — | RemoteConfigManager | **Ignores cached value on cold start** |
| Review queue | DAO due-queries (fixed now) | loadQueues (5 of 7 types) | read-only ReviewQueueItem | ReviewDashboardViewModel | ReviewRepository | Refreshes after markReviewed/snooze; **SLIDE+ANNOTATION missing** |
| Global search | Query string | 14-way UNION LIKE (bound params) | read-only GlobalSearchResult | GlobalSearchViewModel | GlobalSearchRepository | SQL-safe, soft-delete correct; **NOT workspace-scoped** |
| Trash/restore | Trash dialog | soft-delete/restore (deletedAt) | *_deletedAt columns | LibraryViewModel | Workspace/Book/Knowledge repos | Confirmations present; restore deletedAt-filtered |
| Workspace switch | Workspace dialog | setCurrentWorkspaceId | DataStore CURRENT_WORKSPACE_ID | LibraryViewModel | WorkspaceRepository | Library scopes correctly; search leaks |

---

## §4 Prioritized Improvement Roadmap  ★ PRIMARY DELIVERABLE

Grouped into 7 thematic clusters. Priority: P0 (do now) → P4 (nice-to-have). Effort: S/M/L. Impact: Critical/High/Medium.

### Cluster A — Import Reliability
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P0 | Bug/Fix | Surface ImportResult on Compiler save (N imported / M skipped + reasons) | Import | S | Critical | CompilerViewModel.kt:509 |
| P0 | Bug/Fix | Unify answer resolution; resolve 0-vs-1-based index ambiguity | Import | M | Critical | JsonQuestionParser.kt:130 |
| P0 | Bug/Fix | Stop dropping XLSX inline images / fix export-import image field asymmetry | Import/Export | M | Critical | LibraryMapper.kt:93 |
| P1 | Bug/Fix | Warn (not silently drop) unmatched correct-answer refs | Import | S | High | LibraryMapper.kt:87 |
| P1 | Bug/Fix | Strip marker chars (`*✓☑`) from option text after setting `marked` | Import | S | High | SpreadsheetQuestionParser.kt:42 |
| P1 | Bug/Fix | Route legacy `.xls` through POI, not the ZIP compiler | Import | M | High | ImportFormatDetector.kt:23 |
| P1 | Bug/Fix | Cleanup image files on transaction rollback (stage, don't write in-txn) | Import | M | High | ImportLibraryManager.kt:1202 |
| P2 | Maturize | HTML nested-JSON brace-matching; TextQuestionParser continuation stub | Import | M | Medium | HtmlQuestionParser.kt:12 |
| P2 | Improvement | Badge validator-skip questions in preview | Import | S | Medium | CompilerDialog.kt:356 |
| P3 | Feature | Undo-last-import (affectedBookIds/QuizIds already returned) | Import | M | Medium | ImportLibraryManager.kt:1117 |
| P3 | Feature | Saved column-mapping presets; batch multi-file import | Import | M | Medium | CompilerViewModel.kt:396 |

### Cluster B — Study Persistence
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P0 | Bug/Fix | Give adaptive training a real session (persist progress) | Study | M | Critical | QuizViewModel.kt:484 |
| P0 | Bug/Fix | Single-writer atomic session persistence (kill the RMW race) | Study | M | Critical | QuizViewModel.kt:1060 |
| P1 | Bug/Fix | Deserialize knowledge-bank resume state (or delete the dead write) | Study | M | High | FlashcardDeckViewModel.kt:159 |
| P1 | Bug/Fix | Unify Summary on one answer key + one classifier | Study | M | High | SummaryViewModel.kt:150 |
| P1 | Bug/Fix | Session progress vs snapshot, not live quiz size; add "quiz changed" badge | Study | S | High | SessionManagementScreen.kt:580 |
| P1 | Bug/Fix | Move terminal session flush to NonCancellable/app scope | Study | S | High | QuizViewModel.kt:1301 |
| P1 | Bug/Fix | Refresh book summary/lastStudiedAt on knowledge-bank study | Study | S | High | KnowledgeRepository.kt:1381 |
| P2 | Bug/Fix | Slide completion → live course aggregate (observe course) | Study | S | Medium | SlideshowCourseViewModel.kt:303 |
| P2 | Maturize | Wire syncDerivedAssets into question-edit paths (honor syncConfig) | Study | M | Medium | AssetRepository.kt:551 |
| P2 | Bug/Fix | Carry assets/annotations on question move/copy; make atomic | Study | M | Medium | CategoryQuestionsViewModel.kt:170 |

### Cluster C — AI Robustness
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P0 | Bug/Fix | Fix cancelGeneration (cancel held Job, not viewModelScope children) | AI | S | Critical | AiMcqGeneratorViewModel.kt:137 |
| P1 | Maturize | MCQ structural validation; validate review pass before accepting | AI | M | High | McqService.kt:139 |
| P1 | Bug/Fix | Make AiClient retry cancellable (delay+ensureActive+cancel Call); add callTimeout | AI | M | High | AiClient.kt:260 |
| P1 | Bug/Fix | Distinguish offline from "no MCQs"; connectivity precheck | AI | S | High | AiMcqRepository.kt:108 |
| P2 | Bug/Fix | Cap AI response bodies; recycle PDF bitmaps; clamp render size | AI | M | Medium | AiClient.kt:238 · PdfRendererService.kt:55 |
| P2 | Refactor | Typed extraction results (no error-in-content sentinels) | AI | M | Medium | OcrService.kt:61 |
| P3 | Feature | Streaming chat + multi-provider registry (generalize OllamaRepository) | AI | L | Medium | AiClient.kt:1 |

### Cluster D — Export Fidelity
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P1 | Bug/Fix | Lossless image + answer round-trip (depends on Cluster A mapper fixes) | Export | M | High | LibraryMapper.kt:93 |
| P2 | Bug/Fix | Deterministic timestamps (stop System.currentTimeMillis defaults) for idempotent re-import | Export/Import | S | Medium | LibraryMapper.kt:51 |
| P2 | Bug/Fix | DataTools export: write inside coroutine before stream closes | Export | S | Medium | DataToolsScreen.kt:40 |

### Cluster E — Infrastructure Hardening
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P0 | Security | SSRF guard (IP denylist + redirect re-validation + final-scheme consent) | Sync/Import | M | Critical | RemoteAssetFetcher.kt:19 |
| P0 | Security | Encrypt API keys; disable XXE; bound encrypted-ZIP on actual bytes | Import/AI | M | Critical | XlsxImageResolver.kt:26 · ZipLibraryParser.kt:60 · DataStoreManager.kt:381 |
| P0 | Bug/Fix | Implement TokenSyncWorker.doWork (+retry/backoff) | Sync | M | High | TokenSyncWorker.kt:16 |
| P1 | Feature | Notifications: channel + POST_NOTIFICATIONS + NotificationCompat | Sync | M | High | AppFirebaseMessagingService.kt:47 |
| P1 | Bug/Fix | RemoteConfig: seed StateFlow from cached getBoolean on init | Sync | S | Medium | RemoteConfigManager.kt:20 |

### Cluster F — UX Polish
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P0 | Bug/Fix | Fix quiz-edit dialog brace (unreachable dialog) | Study | S | Critical | LibraryScreen.kt:628 |
| P1 | Bug/Fix | Apply language change immediately (setApplicationLocales+recreate) | Settings | S | High | SettingsScreen.kt:330 |
| P1 | Bug/Fix | Add SLIDE + ANNOTATION review queues (fulfill 7-type promise) | Study | S | High | ReviewRepository.kt:94 |
| P1 | Bug/Fix | Scope global search to current workspace | Sync | M | High | GlobalSearchDao.kt:1 |
| P1 | Bug/Fix | Fix focus-mode grant race; only toast "Saved" on real insert | Settings/AI | S | High | SettingsScreen.kt:363 · PdfExtractionScreen.kt:234 |
| P1 | Bug/Fix | Surface encrypted/corrupt-PDF error (no infinite spinner) | AI | S | High | PdfExtractionViewModel.kt:69 |
| P2 | Improvement | Feedback everywhere (dashboard/note/prompt SnackbarHost; reset/export toasts) | Study/Settings | S | Medium | BookToolsViewModel.kt (multi) |
| P2 | Improvement | Recent/Resume reflect study not edits; TTS pause/resume + lifecycle stop | Study | M | Medium | LibraryViewModel.kt:190 · TtsManager.kt:27 |
| P3 | Improvement | Rapid-mode/elimination undo; document-picker for question assets | Study | S | Medium | QuizViewModel.kt:1118 · QuestionAssetsDialog.kt:494 |

### Cluster G — Testing Coverage & Refactor (structural)
| Priority | Category | Recommendation | Pipeline | Effort | Impact | file:line |
|---|---|---|---|---|---|---|
| P0 | Testing | Repository/engine test harness (in-mem Room + fakes) — see §5 | All | L | Critical | (enhancement Phase 1.1) |
| P1 | Refactor | Extract AssetReferenceTracker (kill divergent duplicate) | Import | S | High | ImportLibraryManager.kt:126 |
| P1 | Refactor | Split QuizViewModel/QuizPlayerScreen; pass primitives not whole state | Study | L | High | QuizViewModel.kt:1 |
| P1 | Refactor | Split BookToolsViewModel (partial-overwrite state bleed) | Study | L | High | BookToolsViewModel.kt:98 |
| P2 | Refactor | Split LibraryViewModel; consolidate quiz-write ownership onto one repo | Study | L | Medium | LibraryViewModel.kt:57 |
| P2 | Refactor | Unify the two AI HTTP stacks behind one client | AI | L | Medium | AiClient.kt:1 |
| P2 | Performance | Whole-state recomposition; flow-factories-in-composition; category re-filter on checkbox | Study | M | Medium | QuizPlayerScreen.kt:220 · CategoryQuestionsViewModel.kt:88 |
| P3 | Performance | FTS5-backed global search for scale | Sync | M | Medium | GlobalSearchDao.kt:1 |

**Suggested execution order:** (1) Cluster F P0 brace + Cluster E P0 security + Cluster A/B/C P0 correctness — one hardening sprint. (2) Cluster G P0 test harness — lock the P0 fixes. (3) P1 correctness across A–F. (4) Cluster G refactors (god-objects) to unblock sustained velocity. (5) P2–P4 features/polish.

---

## §5 Testing Gap Analysis

**Current coverage (Part 3, Chunks 20–21):** parser unit tests (Spreadsheet/Text/Article/Slide/Flashcard, SourceDetector, ImportValidator, BoundedStreams, FileManager, Sanitizers, Question/SessionState validators, route builders) + instrumented migration tests (v15→v30) + 4 DAO tests + 3 import-reconciliation tests. **Gap:** every ViewModel, every repository, the whole persist engine, the mapper, and all security paths are untested — matching enhancement-plan Phase 1.1 ("zero repository tests").

| Gap | Risk it introduces | Recommended test class | Extends |
|---|---|---|---|
| `ImportLibraryManager.executeImportPipeline` untested | Non-idempotent re-import, rollback orphans, wrong skip counts, OVERWRITE clobbering study stats | `ImportPipelineTest` (instrumented, per merge strategy) | ImportReconciliationTest |
| `LibraryMapper` round-trip untested | Silent image/answer/session field loss on export→import | `LibraryMapperRoundTripTest` (property-based) | — (new) |
| Answer resolution (3 resolvers) untested for index base | Wrong option marked correct | `AnswerResolverTest` | SpreadsheetHeaderMapperTest |
| SSRF / XXE / zip-bomb untested | Exploitable network + parser security holes | `RemoteAssetSecurityTest`, `XlsxXxeTest`, `EncryptedZipBombTest` | ImportValidatorTest/BoundedStreamsTest |
| Session persistence race + adaptive loss untested | Lost answers/streak; adaptive progress gone | `QuizSessionPersistenceTest` | — (new) |
| Summary answer-consistency untested | Filter/badge/category-stat disagree | `SummaryConsistencyTest` | — (new) |
| Knowledge-session round-trip untested | "Resume" silently broken; Moshi failure → empty state | `LearningSessionRoundTripTest` | — (new) |
| Workspace isolation in search untested | Cross-workspace data leak | `SearchWorkspaceIsolationTest` | — (new) |
| Trash cascade round-trip untested | Orphans / resurrected pre-existing trash | `TrashCascadeTest` | — (new) |
| Repository suites (Knowledge/Quiz/Asset/Book/Study/Workspace) | Regression across all study/CRUD logic | Per-repo test suites (enhancement Phase 1.1) | — (new) |
| `MigrateAll1To30Test` chain | End-to-end migration integrity (dropped columns) | `MigrateAll1To30Test` (enhancement Phase 1.2) | migration tests |
| TokenSyncWorker / RemoteConfig cold-start | Silent no-op infra passes trivially today | `TokenSyncWorkerTest`, `RemoteConfigColdStartTest` | — (new) |

**Highest-leverage test to write first:** `ImportPipelineTest` — it guards the most dangerous file in the app (the ~1065-LOC persist engine) and directly catches the idempotency, rollback-orphan, and skip-count defects that generate the top end-user frictions.

---

## §6 Applied-Changes Review — Round 1

> Reviewed against the working tree on 2026-07-12. I read the full `git diff` of all 15 changed source files **and compiled the module** (`./gradlew :feature:ui:compileDebugKotlin`) to verify solidity rather than eyeball it. Verdict legend: ✅ solid · 🟡 works but needs follow-up · 🔴 broken / must fix.

### 6.0 Build status: 🔴 RED — do this first

`:feature:ui:compileDebugKotlin` **fails** with 3 errors. Nothing else matters until these compile; the app cannot be built or tested in its current state.

| # | Error | File:line | Origin | Severity |
|---|---|---|---|---|
| E1 | `unresolved reference 'MksResult'` (x4) + non-exhaustive `when` — wrong package | CompilerViewModel.kt:521,534 | **Applied change (Cluster A P0)** | 🔴 blocker |
| E2 | `syntax error: Expecting a top level declaration` — stray `}` | LibraryScreen.kt:653 | **Applied change (Cluster F P0)** | 🔴 blocker |
| E3 | `unresolved reference 'contact_banner_en_light_forest_path'` / `..._lavender_valley` | LibraryComponents.kt:204-205, QuizPlayerScreen.kt:1438-1439 | Pre-existing (missing drawables) | 🔴 blocker |

**E1 — CompilerViewModel MksResult package is wrong.** The new feedback block references `com.ahmedyejam.mks.data.importer.model.MksResult.Success/.Error`, but `importCompiledQuestions` returns `com.ahmedyejam.mks.data.model.MksResult<ImportResult>?` (the sealed class lives in **core:model** at `data/model/MksResult.kt` — there is no `importer.model.MksResult`). Fix: change both branch qualifiers to `com.ahmedyejam.mks.data.model.MksResult` (or add `import com.ahmedyejam.mks.data.model.MksResult` and use the short name). The `null` branch is correct because the return type is nullable, so the `when` becomes exhaustive once the type resolves. Exact edit:
```kotlin
// CompilerViewModel.kt ~line 521 and 534 — replace the qualifier:
is com.ahmedyejam.mks.data.model.MksResult.Success -> { ... }
is com.ahmedyejam.mks.data.model.MksResult.Error   -> { ... }
```

**E2 — LibraryScreen has an unbalanced brace.** The brace fix correctly pulled `showQuizEditDialog` out of the `showTrashBin` block (LibraryScreen.kt:628-651 now reads correctly), but a leftover closing `}` remains at line 653 — the old inner block's brace was not removed, so the file has one `}` too many at top level. Fix: delete the extra `}` at LibraryScreen.kt:653 (the function should end with a single `}` at line 652). Verify with a brace-balance check or by re-compiling.

**E3 — Missing theme drawables (pre-existing, now blocking).** `LibraryComponents.kt:204` and `QuizPlayerScreen.kt:1438` reference `R.drawable.contact_banner_en_light_forest_path` and `contact_banner_en_light_lavender_valley`, which **do not exist** in `core/ui/src/main/res/drawable-nodpi/` (only the `_ar_` variants of those two, plus `_en_..._sunrise_valley` and `_moonlit_valley`, are present). This is in the initial commit — not introduced by the round-1 edits — but it means the module has **never compiled** in this tree, which is why the applied changes shipped without a green build. Two options: (a) add the two missing `_en_` webp assets, or (b) fall back the `en` branches to an existing drawable (e.g. `contact_banner_en_light_sunrise_valley`). Because it blocks everything, treat it as P0 regardless of origin.

> **Process gap:** the round-1 edits were committed against a tree that doesn't compile, so no change in this batch has actually been built or run. Establish "green build before commit" (even just `:feature:ui:compileDebugKotlin`) as a gate — this is exactly what the Cluster G test-harness/CI recommendation is meant to enforce.

### 6.1 Per-change verdicts

**✅ Solid (correct, complete, no follow-up needed):**

- **F P0 — Quiz-edit brace intent** (LibraryScreen.kt:628-651): the dialog is now correctly hoisted out of `showTrashBin`; logic is right. *Only* the stray `}` (E2) needs removing — the substance is correct. → after E2 fix, this closes the blocker fully.
- **B P1 — Summary answer-ledger unification** (SummaryViewModel.kt:31,43-168 + SummaryScreen.kt:330,412): excellent fix. Introduced `IndexedQuestion(sequenceIndex, question)`, changed `filteredQuestions` to carry the sequence index, and made **both** the on-screen filter and `getExportText()` key on `session.answersByIndex[sequenceIndex]`. The card status now reads `answersByIndex[index] ?: answers[question.id]` (SummaryScreen.kt:412) so filter membership, card badge, and export agree. This resolves the exact "card shows WRONG under the Correct filter" defect. Category stats still use `resultTaxonomy` separately — acceptable, but see 6.2.
- **C P0 — cancelGeneration scope fix** (AiMcqGeneratorViewModel.kt:112-140): now holds `generationJob` and cancels only that job, not `viewModelScope.children`. The `init{}` progress collector survives, so a second generation reports progress. Correct and complete.
- **A P1 — Marked-cell text stripping** (SpreadsheetQuestionParser.kt:39-48): computes `marked` once, strips `MARKED_REGEX` from the displayed text when marked, keeps the boolean. Clean; the marker no longer leaks into option text or breaks text-match. Complete.
- **A P1 — Unmatched correct-answer warning** (LibraryMapper.kt:89-96): logs via `MksLogger.w` when `dto.correct` is non-empty but resolved to nothing. Good observability. *(Note: it warns but still persists the question with no correct answer — that's the intended scope of this specific item; the deeper "surface to user" work is a separate roadmap row.)*

**🟡 Works but needs follow-up:**

- **A P0 — Answer index 0-vs-1-based** (JsonQuestionParser.kt:133-150): adds a numeric-index branch that prefers 1-based, falling back to 0-based when out of range. This is a **real improvement** for `JsonQuestionParser`, but the roadmap item was *unify all three resolvers*. `SpreadsheetQuestionParser.resolveCorrectAnswers` and `JsonLibraryParser` still have their own (differing) index logic and were **not** touched. Follow-up: extract a shared `AnswerResolver(indexBase)` or at least replicate this exact heuristic in the other two, plus a unit test matrix. Also: the "prefer 1-based" heuristic silently mis-maps a genuinely 0-based source whose index happens to fall in `1..size` — acceptable as a pragmatic default, but document it and consider honoring an explicit `answerMode`/`indexBase` hint when present.
- **B P0 — Adaptive session persistence** (QuizViewModel.kt:502-531): now inserts a real `SessionEntity` (label, `questionIds`, `originalQuestionCount`) and sets `sessionId` in UI state, so `finalizeSubmission` persists progress. Solid core fix. Follow-ups: (1) it always creates a **new** session on entry — there's no "resume existing adaptive session" lookup, so re-entering an interrupted adaptive run starts fresh (acceptable for now, but note it). (2) The session's `quizId` is set to `questions.first().quizId`; for `ALL`/multi-book adaptive runs that's a somewhat arbitrary parent — verify Summary/Session-list rendering for a multi-quiz adaptive session doesn't mislabel. (3) No cleanup of abandoned adaptive sessions — they'll accumulate in the session list; consider marking them or filtering.
- **B P0 — Session RMW race → Mutex** (QuizViewModel.kt:166,1082-1141): wraps the read-modify-write in `sessionMutex.withLock`. This **does** serialize the concurrent-submit race within one VM instance — a correct, minimal fix. Caveats: (1) it only guards `finalizeSubmission`; the *other* session writers (`dropOption` ~1173, `jumpToQuestion` ~826, `nextQuestion` index write, rapid-mode toggle) still do their own unguarded get→copy→update and can interleave with the mutex-protected path. Wrap all session writers in the same mutex (or route them through one `updateSession{}` helper) for full coverage. (2) A Mutex is per-VM-instance; it doesn't protect against a second VM/process, but that's out of scope here.
- **B P1 — Terminal flush NonCancellable** (QuizViewModel.kt:1327-1345): wraps the completion write + `clearSession()` in `withContext(NonCancellable)`. Correct — the final write now survives navigation cancelling the scope. Minor: it's still launched in `viewModelScope`, so if the VM is cleared the `launch` may not start at all; the safest form launches on an application/`@ApplicationScope` coroutine then `NonCancellable`. Low priority given the current fix already closes the common case.
- **B P1 — Flashcard resume** (FlashcardDeckViewModel.kt:238-271): now looks up an incomplete `KnowledgeStudySessionEntity`, deserializes `stateJson` via `sessionStateAdapter`, restores index/reviewed/scores/attempts, and sets `activeSessionId`. All referenced symbols and `LearningSessionState` fields exist; the incremental save path (line 159) does persist `stateJson`, so there is real state to read — the round-trip is now closed for flashcards. Follow-ups: (1) **the slideshow surface was not given the same treatment** — `SlideshowCourseViewModel.setPresentationMode` still resets to 0 and never restores; apply the identical pattern there. (2) index clamp reads `_uiState.value.cards.size` inside the launch — if cards load asynchronously after `setStudyMode`, `cards` may be empty at restore time and the index clamps to 0; gate the restore on cards being loaded (or re-clamp when cards arrive). (3) Moshi failure still falls back to a fresh session (fine) but the incremental save still writes `""` on serialization failure (line 181) — pair this with surfacing/logging that failure.
- **E Security P0 — SSRF guard** (RemoteAssetFetcher.kt:25-68): adds a host denylist + `InetAddress.getAllByName` resolution check rejecting loopback/site-local/link-local/any-local + `169.254.169.254`. This is a **strong, correct addition** that closes the direct-private-IP and DNS-rebind-at-request-time vectors. **Two gaps remain from the original finding:** (1) `followRedirects(true)`/`followSslRedirects(true)` are still set (lines 20-21) and the resolve-check runs only on the *initial* URL — a `https://trusted` that 302-redirects to `http://169.254.169.254` is **not** re-validated, so the redirect-based SSRF + TLS-downgrade bypass is still open. Fix: either `followRedirects(false)` and re-run the guard on each hop, or add an OkHttp `Interceptor` that re-checks `chain.request().url.host` on every redirected request. (2) TOCTOU: the guard resolves DNS, then OkHttp resolves again at connect time — a rebinding attacker could differ between the two. Lower risk than the redirect gap; a custom `Dns`/socket-level check would close it. Net: **meaningfully safer, not yet complete.**
- **F P1 — Language applied immediately** (SettingsScreen.kt:329-355): now calls `AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(...))` alongside the DataStore write — correct approach, and `MainActivity` extends `AppCompatActivity` so per-app locales are supported. **Compile risk to verify:** `androidx.appcompat` is not a declared dependency of `:feature:ui` (I checked `feature/ui/build.gradle.kts` — no `appcompat`). It may resolve transitively today, but that's fragile; add `implementation(libs.androidx.appcompat)` (or `androidx.core` for `LocaleListCompat`) explicitly to `:feature:ui`. Also confirm the Gradle `resourceConfigurations`/locale config includes `ar`, and that `AndroidManifest` opts into per-app locales (`autoStoreLocales` or the app handles config changes) so the setting sticks across restarts.
- **C P1 — PDF encrypted/error surface** (PdfExtractionViewModel.kt:64-108 + PdfExtractionScreen.kt:48,95-117): adds `_loadError` StateFlow, wraps `getPageCount` in try/catch with an encrypted-PDF-specific message, and renders an error card. Correct and closes the infinite-spinner. Follow-ups: (1) the message is inferred from `e.message` containing "password"/"encrypt" — brittle across PDFBox versions/locales; if PdfRenderer/PdfBox exposes an encrypted flag, prefer it. (2) `getPageBitmap`/`renderPage` per-page decode failures are still swallowed to null (image-only page still yields an empty DONE block with no "no selectable text — try Vision" nudge) — the *load* path is fixed, the *extract* path guidance is still open.
- **A P0/Export P1 — Mapper image field asymmetry** (LibraryMapper.kt:241-247): export now writes `imageDataUrl = ""` (instead of stuffing the local path into the data-URL field) and derives `imageName` from the path basename. This **stops the export side from mislabeling a path as a data URL** — a genuine improvement to the round-trip. But the *import* side (LibraryMapper.kt:93-99, where `imageDataUrl` is funneled into `rawSource` and sanitized to null) was **not** changed, and the deeper defect from Phase 1 (resolved XLSX embedded `data:` images not surviving to a persisted `imagePath`) is only partially addressed. Follow-up: add the `LibraryMapperRoundTripTest` (Cluster G) and confirm an export→import of a book with (a) a local image path and (b) an embedded data-URL image both round-trip to a viewable image.

**Cosmetic / out-of-scope (not in the roadmap, flag for awareness):**

- **QuizPlayerScreen GradientProgressBar** (QuizPlayerScreen.kt:927-937) + new `core/ui/.../components/MidnightComponents.kt` + `Color.kt`/`Theme.kt` edits: a "Midnight Premium" gradient progress bar replacing the flat `LinearProgressIndicator`. `GradientProgressBar` is correctly defined in `:core:ui`, gated on `tokens.useGradients`, with a flat fallback — it's clean and will compile. This is unrelated to the correctness roadmap; just note it's an added surface to theme-test (RTL, Plain themes, dynamic color) and it wasn't part of the reviewed plan.

### 6.2 Did anything get broken or regress?

- **Build broke** (E1, E2) — the two P0 edits introduced the compile errors above. No *runtime* regressions could be assessed because the module doesn't build.
- **No silent logic regressions spotted** in the diffs that do compile in isolation: the Summary refactor is consistent across filter/export/card; the Mutex/NonCancellable/adaptive-session/flashcard-resume changes are additive and internally consistent; the SSRF and marked-cell changes are localized.
- **Watch item — Summary category stats vs cards:** the card/filter path now uses `answersByIndex`, but `categoryPerformance` (SummaryViewModel) still classifies via `resultTaxonomy`. These *can* still disagree for corrected-after-repeat questions. Not a regression (pre-existing), but the round-1 fix makes the remaining inconsistency more conspicuous — finish it by having category stats consume the same `answersByIndex`-derived classifier.
- **Watch item — adaptive session `quizId`:** binding an `ALL`/multi-book adaptive session to `questions.first().quizId` is new behavior; verify the Session Management list and Summary header don't mislabel or mis-scope such a session.

### 6.3 How to proceed — sequenced next steps

**Step 0 — Get to green (do immediately, ~30 min):**
1. E1: fix the `MksResult` package qualifier in CompilerViewModel.kt (521, 534) → `com.ahmedyejam.mks.data.model.MksResult`.
2. E2: delete the stray `}` at LibraryScreen.kt:653.
3. E3: add the two missing `_en_` webp drawables **or** fall back those `when` branches to an existing banner.
4. Run `./gradlew :feature:ui:compileDebugKotlin` until green; then `./gradlew :feature:ui:assembleDebug`.
5. Add `implementation(libs.androidx.appcompat)` to `:feature:ui` if the locale API only resolved transitively.

**Step 1 — Lock the fixes with tests (this is the enabler for everything else):**
- Stand up the in-memory Room + Hilt test harness (enhancement Phase 1.1). Then, in priority order, write the tests that directly cover what round-1 just changed so a re-break is caught:
  - `SummaryConsistencyTest` — filter membership == card badge == export, incl. repeated ids (guards the B P1 fix).
  - `QuizSessionPersistenceTest` — concurrent submits under the Mutex; adaptive session persists and reloads; terminal flush survives cancellation (guards B P0/P1).
  - `AnswerResolverTest` — `"0"`,`"1"`,`"2"`,`"A"`,`"A,C"`, marked-only, across all three resolvers (guards A P0 and exposes the two untouched resolvers).
  - `LearningSessionRoundTripTest` — flashcard save→reload→resume, Moshi-failure path (guards B P1).
  - `RemoteAssetSecurityTest` — private-IP reject **and** a redirect-to-internal-IP case (will currently FAIL on the redirect case → proves the remaining SSRF gap).

**Step 2 — Finish the partially-done P0/P1 items (from 6.1 🟡):**
- SSRF: close the redirect bypass (`followRedirects(false)` + per-hop re-validation).
- Answer index: unify the other two resolvers or share one `AnswerResolver`.
- Session Mutex: extend to `dropOption`/`jumpToQuestion`/`nextQuestion` writers.
- Flashcard resume: replicate for slideshow; gate index clamp on cards-loaded.
- Mapper round-trip: fix the import side + add `LibraryMapperRoundTripTest`.
- Category stats: switch to the `answersByIndex` classifier for full Summary consistency.

**Step 3 — Resume the untouched P0/P1 roadmap rows (not yet started):**
- Security P0 not yet addressed: **XXE hardening** (XlsxImageResolver), **encrypted-ZIP zip-bomb** bound (ZipLibraryParser), **API-key encryption** (DataStore). These are the highest-risk remaining items and are independent of the UI build break.
- Import P0/P1: XLSX inline-image loss (import side), `.xls` routing, rollback-orphan image cleanup, the `AssetReferenceTracker` de-dup (fixes the divergent allow-list).
- Infra P0/P1: `TokenSyncWorker` implementation, notification channel + POST_NOTIFICATIONS, RemoteConfig cold-start seed.
- UX P1: SLIDE + ANNOTATION review queues, workspace-scoped global search.

**Step 4 — Structural refactors (Cluster G):** once tests exist, split the three god-objects (QuizViewModel, BookToolsViewModel, LibraryViewModel). Do this *after* the safety net, not before.

**Guardrail going forward:** wire `:feature:ui:compileDebugKotlin` (and the growing unit suite) into the existing CI so a red build can't be committed again — this batch shipped broken precisely because that gate is missing.

### 6.4 Applied-changes scorecard

| Roadmap item | Cluster/Pri | Status | Verdict |
|---|---|---|---|
| Quiz-edit dialog reachable | F P0 | Applied | ✅ correct intent, 🔴 stray brace (E2) — fix then done |
| Surface ImportResult on save | A P0 | Applied | 🔴 wrong MksResult package (E1) — logic good, won't compile |
| Answer 0/1-based index | A P0 | Applied (partial) | 🟡 JsonQuestionParser only; 2 resolvers untouched |
| Marked-cell text strip | A P1 | Applied | ✅ complete |
| Unmatched-answer warning | A P1 | Applied | ✅ complete (log-only, by design) |
| Mapper image-field asymmetry | A/Export | Applied (partial) | 🟡 export side fixed; import side + test pending |
| Adaptive session persistence | B P0 | Applied | 🟡 solid core; no resume/cleanup, multi-book quizId |
| Session RMW race → Mutex | B P0 | Applied (partial) | 🟡 finalizeSubmission only; other writers unguarded |
| Terminal flush NonCancellable | B P1 | Applied | ✅ closes common case |
| Summary answer-ledger unify | B P1 | Applied | ✅ excellent; category-stats follow-up |
| Flashcard resume | B P1 | Applied | 🟡 flashcards done; slideshow not; clamp caveat |
| cancelGeneration scope | C P0 | Applied | ✅ complete |
| PDF encrypted/error surface | C P1 | Applied | ✅ load path fixed; extract-path nudge pending |
| SSRF guard | E P0 | Applied (partial) | 🟡 private-IP closed; redirect bypass open |
| Language applied immediately | F P1 | Applied | 🟡 correct; verify appcompat dep on :feature:ui |
| Midnight gradient progress bar | (not in plan) | Applied | ✅ compiles; cosmetic, theme-test only |
| XXE / zip-bomb / key encryption | E P0 | Not started | ⬜ highest remaining security risk |
| TokenSyncWorker / notifications | E P0/P1 | Not started | ⬜ |
| SLIDE+ANNOTATION review queues | F P1 | Not started | ⬜ |
| Workspace-scoped search | F P1 | Not started | ⬜ |
| Test harness / CI gate | G P0 | Not started | ⬜ — the reason the build shipped red |
