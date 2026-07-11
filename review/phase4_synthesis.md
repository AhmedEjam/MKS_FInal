# Phase 4 — Synthesis, Cross-Pipeline Map & Prioritized Roadmap

> Consolidates `review/phase1_import_pipeline.md`, `review/phase2_study_pipelines.md`, `review/phase3_aux_pipelines.md` against the intended-journey docs (`user_Jour_Geminipro.md`, `USER_JOURNEY_MAP_claudeopus.md`) and `mks_enhancement_plan.md`. No source re-read. Refs `file:line`; `...` = `src/main/java/com/ahmedyejam/mks`. **§4 (Prioritized Improvement Roadmap) is the primary deliverable.**

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
