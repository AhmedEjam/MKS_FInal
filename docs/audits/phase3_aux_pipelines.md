# Phase 3 — AI, OCR, PDF, Export, Sync & Infrastructure + Aux Surfaces

> Scope: auxiliary pipelines — AI MCQ generation, OCR, PDF extraction, Ollama, export/exchange round-trip, DataStore preferences, seeder, Firebase (FCM + RemoteConfig), WorkManager — plus the amendment surfaces (trash, workspace, review queue, global search). Read directly: `DataStoreManager`, `ExportManager`, `WorkspaceRepository`, `ReviewRepository`, `GlobalSearchRepository`, `GlobalSearchDao`, `ReviewDashboardViewModel`, `TrashBinDialog`, `GlobalSearchViewModel`; plus 2 subagent deep-reads (network/AI layer; aux UI + infra services). The trash/workspace/review/search subagent died twice on a transient API error, so those four surfaces were reviewed by direct read. `build/` excluded. Refs `file:line`; `...` = `src/main/java/com/ahmedyejam/mks`.

## TL;DR
1. **Two security holes:** SSRF in `RemoteAssetFetcher`/`RemoteAssetPolicy` (no private-IP/localhost block, redirects not re-validated, plain-HTTP consent checks only the initial scheme) and AI keys stored in **plaintext** Preferences DataStore.
2. **Infrastructure is scaffolding, not wired:** `TokenSyncWorker.doWork()` is a `Result.success()` stub (token never reaches a backend), `onMessageReceived` shows no notification (no channel, no POST_NOTIFICATIONS), and `RemoteConfigManager` ignores Firebase's cached value on cold start.
3. **AI pipeline swallows failures:** malformed LLM JSON → `emptyList()` silently, no MCQ structural validation, unbounded response buffering (OOM), and `AiClient` retry uses `Thread.sleep` so a cancelled coroutine keeps firing HTTP calls.
4. **Aux-surface correctness:** the review queue builds only **5 of 7** types (SLIDE + ANNOTATION missing); global search is SQL-safe and soft-delete-correct but **not workspace-scoped** (leaks results across workspaces); encrypted/image-only PDFs give an infinite spinner.
5. **Bright spots:** `GlobalSearchDao` (parameter-bound, ancestor soft-delete filtered), `OllamaRepository` streaming (real cancellation), `WorkspaceRepository` restore (deletedAt-filtered to avoid resurrecting pre-existing trash), export round-trip design (supplemental data + SHA-256 media).

---

## §1 End-User Lens

### (a) AI MCQ generation — source → configure → generate → review → save
Flow: paste/select text → configure provider → Generate → `AiMcqGeneratorViewModel.startGeneration` → `aiMcqRepository.generateAndSave` (AiMcqGeneratorViewModel.kt:126) → OCR/chunk → `McqService` → `AiClient`/`OllamaRepository`.
- **A1 — Save happens before review (High).** The preview list is read-only and persistence is *inside* `generateAndSave` — the user sees generated MCQs only after they're already saved as a quiz; no edit/delete-before-save (AiMcqGeneratorScreen.kt:245).
- **A2 — Cancel bricks progress permanently (High).** `cancelGeneration` calls `viewModelScope.coroutineContext[Job]?.cancelChildren()` (AiMcqGeneratorViewModel.kt:137), which also cancels the `init{}` progress collector (:66) — after one Cancel, no future generation updates the UI.
- **A3 — Generation may use the *previous* provider config (Med).** Config fields persist on focus-loss (AiMcqGeneratorScreen.kt:383); `startGeneration` doesn't flush, so editing base-URL/key then tapping Generate races the focus-flush.
- **A4 — Offline is indistinguishable from "no MCQs" (Med).** `McqService` swallows every failed chunk to `emptyList()`; offline burns the full retry/backoff per chunk then shows a generic error; `mcqs.isEmpty()` can't tell "empty text" from "all calls failed" (AiMcqRepository.kt:108).

### (b) PDF text extraction
- **A5 — Encrypted/corrupt PDF → infinite spinner (High).** `loadSource` has no try/catch; `getPageCount` throwing on a protected PDF silently cancels, leaving `totalPages=0` → perpetual spinner, no "protected PDF" message (PdfExtractionViewModel.kt:69-97; PdfExtractionScreen.kt:168).
- **A6 — Image-only PDF → silent empty result (Med).** `extractViaText` returns empty for scanned pages with no "no selectable text — try Vision AI" nudge (PdfExtractionViewModel.kt:206).
- **A7 — "Saved to notes" toast lies (Med).** `saveToNote` early-returns on null `source.bookId` (PdfExtractionViewModel.kt:265) but the caller toasts success unconditionally (PdfExtractionScreen.kt:234).
- **A8 — MCQ handoff via global clipboard (Low/privacy).** Extracted text is copied to the system clipboard then the user must paste manually (PdfExtractionScreen.kt:239).

### (c) Preference change (theme/font/language)
- **A9 — Language written but not applied (High).** `setLanguage` only writes DataStore (SettingsScreen.kt:330); nothing calls `AppCompatDelegate.setApplicationLocales`/`recreate()` — strings and RTL don't switch until a manual restart.
- **A10 — Focus-mode grant race (Med).** After launching the DND settings screen, the code immediately re-reads `hasNotificationPolicyAccess()` (always stale-false) so the toggle can't latch on first grant (SettingsScreen.kt:363-377).
- **A11 — Theme/font/density apply immediately and persist (positive).** `setThemeMode`/`setFontScale`/`setUiDensity` write sanitized values (DataStoreManager.kt:281-296); `autoAdvanceDelay` writes on every drag frame though (SettingsScreen.kt:404) — should defer to `onValueChangeFinished` like the others.
- **A12 — Reset DB and Settings-export give no feedback (Med).** `resetDatabase` closes the dialog immediately, `isResetting` never surfaced; failure only `printStackTrace` (SettingsViewModel.kt:73). Settings export never renders `exportMessage` (DataTools does).

### (e) Export → import round-trip
- **A13 — Round-trip design is sound but lossy where the mapper drops fields (see Phase 1 C2/C3).** `ExportManager.exportBookAsBundle` bundles quizzes+questions+sessions+all knowledge assets (ExportManager.kt:87-120); `buildSupplementalData` uses `xxxIncludingDeleted` DAOs so soft-deleted items and asset refs/annotations survive (IMPORT_INPUT_PATHS.md §2G); media carries SHA-256 hashes (§2H). But the Phase-1 finding stands: `LibraryMapper` writes `imagePath` into the DTO's data-URL field and drops `correct` refs on mismatch, so image + answer fidelity can degrade on round-trip.

---

## §2 Senior-Developer Lens

### Security
- **SEC1 — SSRF (Critical).** `RemoteAssetFetcher` validates only scheme ∈ {http,https} (RemoteAssetFetcher.kt:27); `RemoteAssetPolicy` has no host allowlist/denylist (RemoteAssetPolicy.kt:3-15). A user/content-supplied URL can hit `169.254.169.254` (cloud metadata), `localhost`, or `10./192.168.` hosts. `followRedirects(true)` + `followSslRedirects(true)` with no redirect re-validation (:19-20) → an `https://trusted` URL can 30x-redirect to an internal `http://` host, and the plain-HTTP consent gate only inspects the *initial* scheme (:31) so an https→http downgrade bypasses consent. This feeds import image resolution (Phase 1) and remote covers.
- **SEC2 — API keys plaintext at rest (High).** Keys live in standard Preferences DataStore `AI_API_KEY` (DataStoreManager.kt:65,381) — unencrypted app-private storage. `AiClient` also sends the Bearer key over plain `http://` when the base URL has no TLS (AiClient.kt:221,267; OllamaRepository defaults to `http://`, :70).
- **SEC3 — Provider error bodies leak to UI (Low).** `AiHttpError` embeds up to 300 chars of the provider body (AiClient.kt:25) which propagates to the user-facing error string via `AiMcqRepository` (:141) — can echo internal detail.
- **SEC4 — Prompt injection (Low-Med).** OCR'd document text is passed as the `user` message with no delimiting (AiClient.kt:129) — untrusted content can hijack extraction/review instructions (output-quality impact, not RCE).

### Network correctness
- **N1 — AiClient cancellation broken.** `fetchWithRetry` is a plain blocking fn using `Thread.sleep(backoffMs)` (AiClient.kt:260) and never checks `isActive` or holds/cancels the OkHttp `Call` — a cancelled coroutine keeps sleeping and firing up to 4 attempts × 180s (:224) with no `callTimeout`.
- **N2 — Unbounded response buffering (OOM).** `response.body?.string()` reads whole bodies with no cap (AiClient.kt:238; RemoteAssetFetcher relies on `copyToWithLimit`); PDF render is 300-DPI ARGB_8888 (~35 MB/A4 page) never recycled after compress (PdfRendererService.kt:39,55; PdfExtractionViewModel.kt:181).
- **N3 — OllamaRepository is the model to follow (positive).** Real streaming with `reader.useLines` + `ensureActive()` per line + `flowOn(IO)` + separate quick/heavy clients (OllamaRepository.kt:136-171,49-60). Generalize this.
- **N4 — Errors baked into content stream (anti-pattern).** OCR failures become `"[OCR FAILED ON PAGE N]"` in the returned text (OcrService.kt:61), PDF extraction returns `"Error extracting text: …"` as the value (PdfTextExtractor.kt:45) — failures flow downstream as if they were document content; callers can't distinguish success from failure.

### AI robustness
- **AI1 — No MCQ structural validation.** `McqService` wraps parsing in `runCatching{}.getOrElse{ emptyList() }` (McqService.kt:89) and blindly accepts the review pass `if (reviewedList.isNotEmpty()) reviewedList else parsed` (:139) — a hallucinated review that flips the answer key replaces good data. Nothing checks `key ∈ options` or non-empty stems.
- **AI2 — `_progress` clobber.** `AiMcqRepository._progress` is a single MutableStateFlow (:45) with no in-flight guard — two concurrent generations overwrite each other.

### Infrastructure
- **INF1 — TokenSyncWorker is a no-op.** `doWork()` returns `Result.success()` without reading the token or hitting a backend (TokenSyncWorker.kt:16). The enqueue *shape* is correct (CONNECTED constraint, unique REPLACE, AppFirebaseMessagingService.kt:39) but no `setBackoffCriteria`; when implemented it must `Result.retry()` on failure. The token never actually syncs.
- **INF2 — No notifications ever shown.** `onMessageReceived` is a TODO (AppFirebaseMessagingService.kt:53) — no `NotificationCompat`, no channel creation, no POST_NOTIFICATIONS (API 33+) handling. Real DB work launched in `serviceScope` can be killed when the service is torn down (:52).
- **INF3 — RemoteConfig ignores cached value on cold start.** `_isGlobalSearchEnabled` is hardcoded `true` (RemoteConfigManager.kt:20) and only updated inside a successful `fetchAndActivate` within a 2s window (:28); it never seeds from `remoteConfig.getBoolean(...)` on init, so offline/first-2s it serves the default, not the last activated value — the missing half of stale-while-revalidate. Fully silent catch (:33).

### DataStore
- **DS1 — DataStore itself is race-safe (positive, corrects a subagent worry).** Every setter is a transactional `context.dataStore.edit{}` with sanitize-on-read via `SettingsSanitizer` (DataStoreManager.kt:281-401). The "non-atomic multi-write" concern is real only at the *UI* level (separate UI-issued edits, e.g. Appearance reset SettingsScreen.kt:232) — observers can see intermediate states — not inside the manager. Batched writers like `saveDefaultSessionSettings`/`saveSession` correctly group keys in one edit (:412,:427).

### Aux surfaces (direct read — subagent unavailable)
- **AUX1 — Global search leaks across workspaces (High).** `GlobalSearchDao.search` joins `workspaces` only to filter `w.deletedAt IS NULL` — it does **not** filter by the current workspace id (GlobalSearchDao.kt, all 14 UNION branches). So search returns books/quizzes/notes from *every* workspace, breaking the multi-workspace isolation promise. `GlobalSearchRepository.search` passes no workspace param (GlobalSearchRepository.kt:12).
- **AUX2 — Search is SQL-injection safe + soft-delete correct (positive).** `:likeQuery`/`:limit` are Room bound parameters (GlobalSearchDao.kt) — the `%$cleaned%` wrap is a bound value, not concatenation. Every branch filters `deletedAt IS NULL` on the entity AND all ancestors (→quiz→book→workspace), so trashed items never surface. Covers 14 result types.
- **AUX3 — Search perf (Med).** 14-way `UNION ALL` with leading-wildcard `LIKE '%q%'` (non-sargable full scans) per query, no FTS index — O(total rows); fine for small DBs, degrades at scale. Manual debounce via `searchJob?.cancel()` (GlobalSearchViewModel.kt:35), errors swallowed by `runCatching` (:50).
- **AUX4 — Review queue builds only 5 of 7 types (High).** `ReviewRepository.loadQueues` constructs FLASHCARD, BLUEPRINT, MISTAKE, MARKED_QUESTION, WEAK_QUESTION (ReviewRepository.kt:38-94) — **SLIDE and ANNOTATION queues are never built**, even though `loadSummary` counts `unfinishedSlides` (:32). The dashboard promises 7 types; 2 are dead.
- **AUX5 — Review `now` snapshot is per-call, not shared (Low).** `loadSummary` and `loadQueues` each default `now = System.currentTimeMillis()` in separate calls (ReviewRepository.kt:22,36), so summary counts and queue lists use slightly different `now` — minor drift at due-date boundaries. Within each function `now` is fixed (good). `refresh()` re-runs on init and after every markReviewed/snooze (ReviewDashboardViewModel.kt:62,81,88) — so the queue does refresh after review actions.
- **AUX6 — Workspace restore is thoughtfully correct (positive).** `deleteWorkspace` soft-deletes workspace + its books (WorkspaceRepository.kt:145-149, deeper content via DB cascade); `restoreWorkspace` restores books using a `deletedAt` filter equal to the workspace's own `deletedAt` (:156) so items already in trash *before* the workspace was deleted are not resurrected. `getOrCreateDefaultWorkspace` guards concurrent init with a `Mutex` (:85-87). `resetDatabase` clears+reseeds (:167).
- **AUX7 — Trash confirmations present (positive).** Empty-trash and permanent-delete both gated by `AlertDialog` (TrashBinDialog.kt:243,278); restore/permanent-delete wired per entity type across 6 tabs. Cascade correctness is delegated to the VM/repo soft-delete chain (verify book→quiz→question→annotation cascade in a test — see TG).

---

## §3 Recommendations

### Potential Improvements
- **I1 (P0):** SSRF guard in `RemoteAssetPolicy` — reject private/loopback/link-local/ULA ranges, re-validate on every redirect (custom interceptor or `followRedirects(false)` + manual loop), enforce plain-HTTP consent on the *final* scheme (RemoteAssetFetcher.kt:19).
- **I2 (P0):** Fix `cancelGeneration` to cancel a held generation `Job`, not `viewModelScope` children (AiMcqGeneratorViewModel.kt:137).
- **I3 (P0):** Wrap `loadSource` and surface encrypted/corrupt-PDF errors instead of an infinite spinner (PdfExtractionViewModel.kt:69).
- **I4 (P1):** Add SLIDE + ANNOTATION queues to `ReviewRepository.loadQueues` (:94) so the dashboard's 7-type promise holds.
- **I5 (P1):** Scope global search to the current workspace (add a `workspaceId` param + `w.id = :workspaceId` filter) (GlobalSearchDao.kt).
- **I6 (P1):** Apply language immediately via `AppCompatDelegate.setApplicationLocales` + recreate (SettingsScreen.kt:330); fix focus-mode grant to re-check on lifecycle resume (:363).
- **I7 (P1):** Only toast "Saved to notes" on an actual insert (PdfExtractionScreen.kt:234); surface reset/export feedback in Settings.

### Features to Add
- **FA1:** Encrypt API keys (EncryptedDataStore/Keystore-wrapped) + cleartext-HTTP warning for base URLs (DataStoreManager.kt:381; OllamaRepository.kt:70).
- **FA2:** Real notifications — channel + POST_NOTIFICATIONS request + `NotificationCompat` in `onMessageReceived`; scheduled study reminders off the review-due data (AppFirebaseMessagingService.kt:47).
- **FA3:** Multi-provider registry (the `providerId` branching is half there) + streaming chat generalized from `OllamaRepository`; batch PDF import; export format options.
- **FA4:** FTS-backed global search (SQLite FTS5 virtual table) for scale.

### Functions to Maturize
- **M1:** MCQ validation — enforce option-key set, `key ∈ options`, non-empty stem; validate the review pass didn't corrupt structure before accepting (McqService.kt:139).
- **M2:** `AiClient` cancellable retry — make `fetchWithRetry` suspend, use `delay()`, hold+cancel the `Call`, add `callTimeout`, honor `Retry-After` (AiClient.kt:220-260).
- **M3:** `TokenSyncWorker.doWork` — read token, POST, `Result.retry()` on failure + `setBackoffCriteria` (TokenSyncWorker.kt:16).
- **M4:** `RemoteConfigManager` — seed StateFlow from cached `getBoolean` on init; add `setDefaultsAsync`/`minimumFetchInterval` (RemoteConfigManager.kt:20).
- **M5:** PDF bitmap sizing — clamp max pixels, RGB_565 fallback, downscale, open the renderer once, recycle after compress (PdfRendererService.kt:39-55).

### Refactor Opportunities
- **R1:** Unify the two divergent HTTP stacks (`AiClient` org.json+retry vs `OllamaRepository` Moshi+streaming) behind one `AiHttpClient` so streaming and retry are available to both (AiClient.kt / OllamaRepository.kt).
- **R2:** Replace the sentinel-string-in-content failure pattern with typed results (`ExtractionResult.Failure`) so OCR/PDF errors can't masquerade as document content (OcrService.kt:61; PdfTextExtractor.kt:45).
- **R3:** Extract the duplicated `pingProvider`/`fetchModels`/`testCall` (copy-pasted in PdfExtractionViewModel.kt:280 and SettingsViewModel.kt:102) into one `AiProviderTester`.
- **R4:** Push `dataStoreManager` behind VM methods instead of exposing it publicly to composables (AiMcqGeneratorViewModel.kt:48).

### Testing Gaps (most dangerous untested)
- **TG1 — SSRF/redirect policy (0 tests, exploitable).** redirect-to-internal-IP, https→http consent bypass, missing-Content-Type, `contentLength==-1` cap enforcement, private-IP rejection (RemoteAssetFetcher.kt). Highest risk.
- **TG2 — MCQ JSON robustness (0 tests).** malformed/truncated JSON, missing `key`, `key ∉ options`, review pass dropping/renaming options (McqService.kt:89-143).
- **TG3 — `cancelGeneration` regression (0 tests).** assert a second generation still reports progress after a Cancel (AiMcqGeneratorViewModel.kt:137).
- **TG4 — Encrypted/image-only/zero-page PDF (0 tests).** assert error state, not infinite spinner (PdfExtractionViewModel.kt:69,206).
- **TG5 — Workspace isolation in search (0 tests).** create 2 workspaces, assert search in A doesn't return B's content (GlobalSearchDao.kt) — currently would fail (AUX1).
- **TG6 — Trash cascade round-trip (0 tests).** soft-delete book → assert quizzes/questions/annotations hidden everywhere; restore → assert exact set returns (no orphans, no pre-existing trash resurrected) (WorkspaceRepository.kt:151; TrashBinDialog).
- **TG7 — TokenSyncWorker end-to-end + RemoteConfig cold-start-offline** (both currently pass trivially because they're stubs — the test would expose that).

---

## Machine-readable recommendation table

| id | category | severity | title | file:line | fix effort |
|---|---|---|---|---|---|
| SEC1/I1 | Security | P0 | SSRF: no private-IP block, redirects/consent not re-validated | core/network/.../network/RemoteAssetFetcher.kt:19 | M |
| SEC2/FA1 | Security | P0 | AI keys stored plaintext; Bearer sent over http | core/data/.../preferences/DataStoreManager.kt:381 | M |
| A2/I2 | Bug/Fix | P0 | cancelGeneration bricks progress collector | feature/ui/.../ui/booktools/AiMcqGeneratorViewModel.kt:137 | S |
| A5/I3 | Bug/Fix | P0 | Encrypted/corrupt PDF → infinite spinner | feature/ui/.../ui/booktools/PdfExtractionViewModel.kt:69 | S |
| INF1/M3 | Bug/Fix | P0 | TokenSyncWorker is a no-op; token never syncs | app/.../service/TokenSyncWorker.kt:16 | M |
| AUX4/I4 | Bug/Fix | P1 | Review queue builds only 5 of 7 types (no SLIDE/ANNOTATION) | core/data/.../review/ReviewRepository.kt:94 | S |
| AUX1/I5 | Bug/Fix | P1 | Global search not workspace-scoped (cross-workspace leak) | core/database/.../dao/GlobalSearchDao.kt:1 | M |
| INF2/FA2 | Feature | P1 | No notifications shown (no channel/POST_NOTIFICATIONS) | app/.../service/AppFirebaseMessagingService.kt:47 | M |
| A9/I6 | Bug/Fix | P1 | Language written but never applied | feature/ui/.../ui/settings/SettingsScreen.kt:330 | S |
| A10 | Bug/Fix | P1 | Focus-mode permission grant race | feature/ui/.../ui/settings/SettingsScreen.kt:363 | S |
| N1/M2 | Bug/Fix | P1 | AiClient retry uncancellable (Thread.sleep, no callTimeout) | core/network/.../network/AiClient.kt:260 | M |
| AI1/M1 | Maturize | P1 | No MCQ structural validation; blind review-pass accept | core/network/.../network/McqService.kt:139 | M |
| INF3/M4 | Bug/Fix | P1 | RemoteConfig ignores cached value on cold start | app/.../service/RemoteConfigManager.kt:20 | S |
| N4/R2 | Refactor | P1 | Errors baked into content stream (OCR/PDF sentinel strings) | core/network/.../network/OcrService.kt:61 | M |
| A7 | Bug/Fix | P1 | "Saved to notes" toast lies when bookId null | feature/ui/.../ui/booktools/PdfExtractionScreen.kt:234 | S |
| N2/M5 | Performance | P2 | PDF bitmap OOM (35MB/page, never recycled) | core/network/.../network/PdfRendererService.kt:55 | M |
| AI2 | Bug/Fix | P2 | AiMcqRepository _progress clobbered by concurrent runs | core/data/.../repository/AiMcqRepository.kt:45 | S |
| A11 | Performance | P2 | autoAdvanceDelay writes DataStore per drag frame | feature/ui/.../ui/settings/SettingsScreen.kt:404 | S |
| AUX3/FA4 | Performance | P2 | Search 14-way UNION + leading-wildcard LIKE, no FTS | core/database/.../dao/GlobalSearchDao.kt:1 | M |
| A12 | Improvement | P2 | Reset DB / Settings export give no feedback; silent failure | feature/ui/.../ui/settings/SettingsViewModel.kt:73 | S |
| R1 | Refactor | P2 | Unify two divergent AI HTTP stacks | core/network/.../network/AiClient.kt:1 | L |
| R3 | Refactor | P3 | Duplicated provider-tester code | feature/ui/.../ui/booktools/PdfExtractionViewModel.kt:280 | S |
| TG1 | Testing | P0 | No SSRF/redirect regression tests | core/network/.../network/RemoteAssetFetcher.kt:1 | M |
| TG5 | Testing | P1 | No workspace-isolation test for search | core/database/.../dao/GlobalSearchDao.kt:1 | S |
| TG6 | Testing | P1 | No trash cascade round-trip test | core/data/.../repository/WorkspaceRepository.kt:151 | M |
