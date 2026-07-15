# Phase 2 — Quiz Play & Knowledge-Bank Study Pipelines

> Scope: study/playback pipelines — quiz answering/scoring/sessions, flashcards (SRS), slideshows, note blueprints (reader/TTS/autoscroll), prompt decks — plus the library hub (origin of most journeys), category editing, session management, and summary. Read directly: `StudyRepository`, `QuizRepository` (session methods), `KnowledgeRepository` (rateFlashcard, refresh paths), `BookRepository` (updateLastStudied), `LibraryScreen` (verification), study entities; plus 3 subagent deep-reads over ~30 UI/VM files. `build/` excluded. Refs are `file:line`; `...` = `src/main/java/com/ahmedyejam/mks`.
>
> **Correction note (verified against repo layer):** Two subagent claims were checked and overturned — (a) quiz play *does* update `lastStudiedAt` and refresh stats via `QuizRepository.updateSession`; (b) the "slideshow abuses a flashcard-deck FK / CASCADE" claim is invalid — both study VMs write `KnowledgeStudySessionEntity`, not the FK-bearing `LearningSessionEntity`. Corrected findings below are marked **[verified]**.

## TL;DR
1. Quiz play persists per-answer and correctly touches `lastStudiedAt` + refreshes stats through the repo **[verified]** — better than it first appears; the real defects are a non-atomic read-modify-write session race and **zero persistence for adaptive-training mode** (sessionId=null).
2. **Shipping blocker:** quiz Edit dialog is unreachable — a misplaced brace nests `showQuizEditDialog` inside `if (showTrashBin)` (LibraryScreen.kt:628-651), so editing a quiz silently does nothing.
3. Knowledge-bank study "resume" is a phantom: `KnowledgeStudySessionEntity.stateJson` is written on every flip/swipe but **never deserialized** on load — index always resets to 0. Serialization failures are swallowed to `""`.
4. Summary analytics can self-contradict: filter uses `answersByIndex` while cards render from `answers[id]`, and category stats use `resultTaxonomy` — three answer ledgers that disagree on repeats.
5. Three god-objects (QuizViewModel ~1436, BookToolsViewModel ~1124, LibraryViewModel 737) with whole-`state` recomposition; flashcard SRS is real at the repo (`rateFlashcard`) but the VM's per-card score is dead.

---

## §1 End-User Lens

### (a) Quiz play — library → player → answer → score → session → summary
Entry: `QuizDetailTabsScreen` → session → `QuizPlayerScreen`/`QuizViewModel.startQuiz` (QuizViewModel.kt:220). Select→submit→`finalizeSubmission` persists `SessionEntity` on **every** submit (QuizViewModel.kt:1101 → QuizRepository.updateSession:553). Navigation persists index (:1279, :834). **So app-kill mid-quiz recovers the last answered question** — progress is genuinely saved.
- **U1 — Adaptive/training loses everything on kill (High).** `startAdaptiveTraining` runs with `sessionId=null` (QuizViewModel.kt:484-520) → the session-write block never executes. Long-term per-question metrics still update (:1041), but there is no session row → no resume. A killed adaptive session is gone.
- **U2 — Resume timer burns wall-clock while backgrounded (Medium).** Per-question elapsed is reset to 0 on resume (:395-399, admitted TODO) and the countdown is reconstructed from `lastModifiedAt` (:387-390) — stepping away from a timed quiz silently drains the clock.
- **U3 — Rapid mode has no undo/countdown (Medium).** Auto-advance after `autoAdvanceDelayMs` (:1118) with no visible timer or catch; a mis-tap commits and advances.
- **U4 — Dropping the correct option = instant wrong, no confirm (Medium).** Elimination scores it wrong immediately (:1141-1154), unlike question-drop which has a dialog (QuizPlayerScreen.kt:194).
- **U5 — No "saved" affordance anywhere.** Persistence is invisible; on long sessions the user has no signal their work is safe (and in adaptive it isn't).

### (b) Flashcards / slideshow / note reader / prompt deck
- **U6 — "Resume where you left off" doesn't work (High).** Both surfaces serialize index/reviewed/timer into `KnowledgeStudySessionEntity.stateJson` on every action (FlashcardDeckViewModel.kt:159; SlideshowCourseViewModel.kt:201) but **no loader deserializes it** — `setStudyMode`/`setPresentationMode` reset `currentIndex=0` (FlashcardDeckViewModel.kt:230; SlideshowCourseViewModel.kt:266). The user always restarts from card/slide 1.
- **U7 — Slideshow completion stats are stale until re-entry (Medium).** `toggleSlideStudied` durably flips the slide row (SlideshowCourseViewModel.kt:303), but the course is fetched once (not observed) so `studiedSlideCount`/`progress` on the stats card don't move (SlideshowCourseScreen.kt:440) until you leave and return.
- **U8 — Flashcard flip state not persisted; no deck-complete recap (Medium).** `isFlipped` is never saved; reaching the last card just sits with no accuracy summary despite tracked `correctAttempts`.
- **U9 — TTS has no real pause/resume and lies on natural completion (Medium).** `TtsManager` exposes only play/stop with `QUEUE_FLUSH` (TtsManager.kt:27-46); no `UtteranceProgressListener`, so the Stop icon stays on after speech ends; no `ON_PAUSE` observer, so backgrounding keeps it talking until the screen is disposed (BookToolScreens.kt:408). TTS also reads `note.body` while the editor edits a separate `editedBody` (:457 vs :404).
- **U10 — No feedback on dashboard/note/prompt actions (Medium).** `BookToolsViewModel` sets `successMessage`/`error` in ~40 places but the dashboard, note reader, and prompt screens have **no SnackbarHost** — creating/deleting/saving-AI-output is silent, and stale messages bleed across the shared-VM tabs.
- **U11 — In-progress AI output lost on rotation (Medium).** Prompt-editor `outputText`/`values` use `remember`, not `rememberSaveable` (BookToolScreens.kt:809,838) — a config change mid-generation discards a long streamed answer.

### (c) Library hub, category, session, summary
- **U12 — Quiz Edit is dead (Blocker) [verified].** LibraryScreen.kt:628-651: the `if (showQuizEditDialog)` block is physically nested inside `if (showTrashBin)`, so tapping Edit on a quiz renders nothing. Book edit works (:556), making it more confusing.
- **U13 — "Resume"/"Recent" reflect edits, not study (Medium).** `resumeQuiz` falls back to `maxByOrNull { updatedAt }` (LibraryViewModel.kt:190) and `updateQuiz` bumps `updatedAt` on any cover/title edit (:317) → editing a never-studied quiz makes it the resume target; `recentQuizzes` sorts by `updatedAt` (:183).
- **U14 — Session progress/percent computed against the live quiz size, not the snapshot (High).** `SessionItem` divides `currentQuestionIndex / quizQuestionCounts[quizId]` (SessionManagementScreen.kt:262,580) — after questions are added/deleted, or for a custom subset session, the ring % and "remaining" are wrong; no "quiz changed" staleness badge.
- **U15 — Summary can show a card as WRONG under the "Correct" filter (High).** Filter membership uses `answersByIndex[index]` (SummaryViewModel.kt:150) but the card badge reads `answers[question.id]` (SummaryScreen.kt:412) — different maps; repeats collapse in `answers[id]`. "Avg time/question" sums the per-*entity* `timeSpentMs` over placeholders+repeats (SummaryScreen.kt:201) → not this session's time.
- **U16 — Attaching a question asset is a raw text field (Medium).** IMAGE/PDF assets require typing a path/`content://` URI (QuestionAssetsDialog.kt:494) that isn't copied into app storage → permission revoked on restart → broken attachment. Book covers, by contrast, are copied via `assetRepository.saveImage` (LibraryViewModel.kt:286).

---

## §2 Senior-Developer Lens

### State ownership & single-source-of-truth
- **S1 — Triple answer ledger in SessionEntity.** `answers` (by questionId), `answersByIndex` (by index), and `resultTaxonomy` (by original index) are maintained in parallel by hand (QuizViewModel.kt:1064-1099). On repeats, `answers[id]` overwrites the first attempt (:1067) while `answersByIndex` keeps both → they drift, and Summary reads all three inconsistently (U15). No invariant enforces consistency.
- **S2 — Two overlapping session models; one orphaned [verified].** Quiz play uses `SessionEntity`; knowledge-bank study uses `KnowledgeStudySessionEntity` via `StudyRepository.createLearningSession` (StudyRepository.kt:154). The FK-bearing `LearningSessionEntity` (with `deckId` CASCADE, LearningSessionEntity.kt:11-23) is **not written by these study VMs** — it's dead or written on a path not exercised here. `StudySessionEntity` (StudySessionEntity.kt) is likewise unused by the quiz path. Three session tables, unclear ownership.
- **S3 — God-objects.** `QuizViewModel` ~1436 (play + timers + category CRUD :678-793 + AI deck creation :1413), `BookToolsViewModel` ~1124 (slideshow-list + note-list + prompt-list + reader + editor + dashboard, one `_uiState` with ~25 fields), `LibraryViewModel` 737 (7 collaborators: Book/Quiz/Knowledge/Workspace/Asset + Export + DataStore). In BookToolsViewModel the three loaders overwrite disjoint state subsets (loadPromptDeck :514 doesn't set `bookSummary`/`flashcardDecks`), so tab-to-tab state bleed is structural.
- **S4 — Duplicated quiz-write ownership.** In LibraryViewModel, `insertQuiz`/`createNewQuiz` route to `knowledgeRepository` (:359,399) while `updateQuiz`/`deleteQuiz` route to `quizRepository` (:319,273). Category question edits route through `assetRepository.updateQuestion` (CategoryQuestionsViewModel.kt:247) — surprising ownership.

### Persistence guarantees
- **S5 — lastStudiedAt & stats ARE refreshed on quiz play [verified — corrects subagent].** `QuizRepository.updateSession` calls `updateLastStudied(quizId)` on every write and `refreshQuizStats` when `isCompleted` (QuizRepository.kt:553-558); `StudyRepository.updateQuestionMetrics` also calls `refreshQuizStats` per answer (StudyRepository.kt:254). `BookRepository.updateLastStudied` touches both quiz and parent book (:288-294). So quiz-play aggregates stay fresh.
- **S6 — Knowledge-bank study does NOT refresh book aggregates [verified].** `rateFlashcard` implements a real basic SRS (dueAt/difficulty/reviewCount/markReviewed) and touches **deck** `lastStudiedAt` (KnowledgeRepository.kt:1381-1411) — but it does **not** call `refreshBookStats` or touch `book.lastStudiedAt`. `refreshBookStats` is only called on CRUD (create/edit/delete deck/course/note, KnowledgeRepository.kt:110-187,855,1005). So studying flashcards/slides never updates the book-level summary the dashboard shows → the "study didn't count" perception (BookKnowledgeDashboardScreen.kt:566, which is quiz-only).
- **S7 — Non-atomic session writes (race).** Every session mutation is get→copy→update across a suspend gap (finalizeSubmission QuizViewModel.kt:1060-1101; dropOption :1159; jumpToQuestion :810). Two fast submits, or submit-then-navigate, each read the stale row and write conflicting copies → lost answer/streak. No mutex/single-writer; `SessionStateValidator` guards reads only.
- **S8 — Resume state serialized but never read (dead persistence).** Confirmed by the reset-to-0 loaders (U6); `stateJson` write is pure overhead, and Moshi failures write `""` silently (FlashcardDeckViewModel.kt:180).
- **S9 — Terminal flush runs in cancellable `viewModelScope`.** `finishSession` (:651) and terminal `nextQuestion` (:1301) launch the completion write + `clearSession()` in `viewModelScope`; navigating away can cancel the final write mid-flight. Should use `NonCancellable`/application scope.

### Derived-asset sync
- **S10 — `syncDerivedAssets` exists but isn't wired to question edits.** `AssetRepository.syncDerivedAssets` (AssetRepository.kt:551) honors `syncConfig["text"]`/`["body"]` to push a changed question into linked flashcards/slides and touches notes — but Phase-1/Phase-2 review found no call site invoking it when a question is edited via CategoryQuestionsViewModel.updateQuestion (:247 calls `assetRepository.updateQuestion`, not `syncDerivedAssets`). So editing a source question drifts its derived cards/slides. (Confirm call sites in Phase 3/synthesis.)

### Compose performance
- **S11 — Whole-`state` recomposition.** `QuizState`/`uiState` passed wholesale into `QuestionContent`/`QuizSheetContent`/`QuizTopBar` (QuizPlayerScreen.kt:449) → selecting one option recomposes the sheet, nav grid, toggles. `getQuestionStatus` reads `_uiState.value` directly inside a LazyRow item (QuizPlayerScreen.kt:1155) — outside snapshot observation.
- **S12 — Flow factories in composition.** `sourceDocumentsForCurrentQuiz` builds a new cold `flow{}` per call (QuizViewModel.kt:1401) collected unkeyed (QuizPlayerScreen.kt:220); `assetsForQuestion`/`sourceDocumentsForBook`/`annotationsForQuestion` return new Flows each recomposition in CategoryQuestionsScreen.kt:221-225. Re-subscribe/re-query storm.
- **S13 — Category screen re-filters on every checkbox.** `_selectedQuestionIds` is inside the 7-arg `combine(Array<Any>)` (CategoryQuestionsViewModel.kt:88-107, unchecked positional casts) so ticking one box re-runs `applyFilter` over all questions.
- **S14 — Double flow subscription.** `BookKnowledgeDashboardScreen` collects `uiState` at :86 and `MagicActionsSection` collects it again at :636. `SessionViewModel.loadSessions` leaks a collector per call on `viewModelScope` (SessionViewModel.kt:31) — use `flatMapLatest`.

### null-safety / silent catches
- **S15** — `animQuestion.hint!!` (QuizPlayerScreen.kt:632, mid-transition NPE risk); `deck.description!!` (FlashcardDeckScreen.kt:878); `!!` chains in QuestionNoteItem (BookToolScreens.kt:649-654) and SummaryScreen.kt:522-534. Silent `catch → printStackTrace` in startQuiz/startAdaptiveTraining (QuizViewModel.kt:421,515); category VM has no try/catch across delete/move/copy/asset ops (CategoryQuestionsViewModel.kt:138-357).

### Correctness
- **S16 — Move/copy strands assets [subagent, plausible].** `moveSelectedQuestionsToQuiz` updates only `question.quizId` (CategoryQuestionsViewModel.kt:170); assets keep old quiz/book ids. `copySelectedQuestionsToQuiz` copies only the question row (:185), dropping assets/annotations. Non-atomic, no feedback.
- **S17 — `emptyTrash` can silently no-op.** Reads `.value` of `WhileSubscribed(5000)` flows (LibraryViewModel.kt:599); with no warm collector, `.value` is the initial `emptyList()` — success snackbar shown, nothing purged.
- **S18 — Streak/first-attempt uses index-position heuristic.** "first attempt" = `currentIndex < initialQuestionCount` but repeats are appended beyond that boundary (QuizViewModel.kt:972), and `resultTaxonomy` "CORRECTED_AFTER_REPEAT" uses `indexOf` (first match only) on a duplicated id (:1093).

---

## §3 Recommendations

### Potential Improvements
- **I1 (P0):** Give adaptive/training a real session (reuse `SessionEntity` or the unused `StudySessionEntity`) so progress survives kill (QuizViewModel.kt:484). Also add an `onStop` flush + freeze the countdown while backgrounded (U2).
- **I2 (P0):** Fix the LibraryScreen brace so `showQuizEditDialog` renders outside `showTrashBin` (LibraryScreen.kt:628-651).
- **I3 (P1):** Deserialize `stateJson` in `loadDeck`/`loadCourseSafe` and offer "Resume?"; or delete the write if resume isn't shipping (S8).
- **I4 (P1):** Unify Summary on one answer key + one classifier shared by filter, card badge, and category stats (SummaryViewModel.kt:150 / SummaryScreen.kt:412).
- **I5 (P1):** Compute session progress against the session's own `originalQuestionCount`/`questionIds` and add a "quiz changed since session" badge (SessionManagementScreen.kt:580).
- **I6 (P2):** Add a shared SnackbarHost to dashboard/note/prompt screens; surface `successMessage`/`error`; `clearMessages()` on nav (U10).

### Features to Add
- **FA1:** Deck/slide-complete recap with session accuracy (U8) and a spaced-repetition "N due today" surface per deck/course (fields already exist: FlashcardEntity dueAt/difficulty, CourseSlideEntity SRS scaffolding).
- **FA2:** Rapid-mode visible auto-advance countdown + undo; elimination undo/confirm (U3/U4).
- **FA3:** Document-picker for question assets that copies into app storage (U16); prompt-deck templates and output-routing history polish.
- **FA4:** TTS as a `MediaSession`/foreground service with lockscreen controls + word-sync highlight (U9).

### Functions to Maturize
- **M1:** Flashcard SRS — repo `rateFlashcard` (KnowledgeRepository.kt:1381) is decent but simplistic (linear `reviewCount+1` intervals); graduate to SM-2/FSRS and retire the dead VM-level `cardScores` (FlashcardDeckViewModel.kt:380).
- **M2:** Streak/first-attempt accounting — replace index-position heuristic with a stable per-question attempt record (QuizViewModel.kt:972,1093) (S18).
- **M3:** Slide completion → live course aggregate + observe the course (SlideshowCourseViewModel.kt:303) (U7).
- **M4:** TTS state machine — add `UtteranceProgressListener` + real pause/resume + lifecycle stop (TtsManager.kt) (U9).
- **M5:** Wire `syncDerivedAssets` into question-edit paths so `syncConfig` actually propagates (AssetRepository.kt:551) (S10).

### Refactor Opportunities
- **R1 (High):** Split `QuizViewModel` → play VM + `SessionPersistence` collaborator (single-writer, atomic partial `@Update`) + `QuizTimerController` + `CategoryManagementViewModel`; this also fixes the S7 race. Split `QuizPlayerScreen` and pass primitives, not whole `state`, to restore skippability (S11).
- **R2 (High):** Split `BookToolsViewModel` into Notes/Sources/PromptDeck/SlideshowAdmin/Dashboard VMs to kill the partial-overwrite state bleed (S3).
- **R3:** Split `LibraryViewModel` into Browse/Trash/Workspace/ImportExport VMs sharing a `WorkspaceContext`; consolidate quiz writes onto one repo (S4).
- **R4:** Replace the 7-arg `combine(Array<Any>)` with a typed data class and decouple selection from the filter derivation (CategoryQuestionsViewModel.kt:88) (S13).
- **R5:** Extract a single `updateSession` funnel through the repo with atomic column updates (SessionEntity DAO `@Query` partial updates) shared by finalize/drop/jump/next.

### Testing Gaps (most dangerous untested)
- **TG1 — Session persistence race + adaptive loss (0 tests).** Most dangerous. Instrumented: two submits in one frame; submit→immediate next; drop-correct-then-rapid-advance → assert no lost answer, monotonic score/streak, complete `answersByIndex`; and "resume adaptive after kill" (currently would fail, which is the point). QuizViewModel.kt:1021-1125.
- **TG2 — Summary index-vs-id consistency (0 tests).** Property: filter membership == card badge == category-stat classification, especially with repeated ids. SummaryViewModel.kt:150 / SummaryScreen.kt:412.
- **TG3 — startQuiz resume/repair (0 tests).** Corrupted session (missing current id :272), `canBeRepaired` (:261), empty-after-filter (:349), range coercion (:326), shuffle-seed determinism across resume.
- **TG4 — Knowledge session round-trip (0 tests).** save→reload→resume for KnowledgeStudySessionEntity (proves S8); force Moshi throw → assert no empty `stateJson` written (FlashcardDeckViewModel.kt:180).
- **TG5 — Move/copy asset propagation (0 tests).** Assert assets & annotations follow (or are intentionally excluded) and ids stay consistent (CategoryQuestionsViewModel.kt:159-198).
- **TG6 — Compose UI test for quiz-edit reachability** would have caught U12 instantly.

---

## Machine-readable recommendation table

| id | category | severity | title | file:line | fix effort |
|---|---|---|---|---|---|
| U12/I2 | Bug/Fix | P0 | Quiz Edit dialog unreachable (nested in trash-bin block) | feature/ui/.../ui/library/LibraryScreen.kt:628 | S |
| U1/I1 | Bug/Fix | P0 | Adaptive-training mode has zero session persistence | feature/ui/.../ui/quiz/QuizViewModel.kt:484 | M |
| S7/R5 | Bug/Fix | P0 | Non-atomic session read-modify-write race | feature/ui/.../ui/quiz/QuizViewModel.kt:1060 | M |
| U6/S8 | Bug/Fix | P1 | Knowledge-bank resume state written but never read | feature/ui/.../ui/flashcard/FlashcardDeckViewModel.kt:159 | M |
| U15/I4 | Bug/Fix | P1 | Summary filter/card/category use 3 disagreeing answer ledgers | feature/ui/.../ui/summary/SummaryViewModel.kt:150 | M |
| U14/I5 | Bug/Fix | P1 | Session progress computed vs live quiz size, not snapshot | feature/ui/.../ui/session/SessionManagementScreen.kt:580 | S |
| S6 | Bug/Fix | P1 | Flashcard/slide study never refreshes book summary/lastStudiedAt | core/data/.../repository/KnowledgeRepository.kt:1381 | S |
| S1 | Refactor | P1 | Collapse triple answer representation in SessionEntity | feature/ui/.../ui/quiz/QuizViewModel.kt:1064 | M |
| S16 | Bug/Fix | P1 | Move/copy question strands assets & annotations | feature/ui/.../ui/category/CategoryQuestionsViewModel.kt:170 | M |
| U16 | Improvement | P1 | Question-asset attach is raw text field, not picker (URI perm loss) | feature/ui/.../ui/category/QuestionAssetsDialog.kt:494 | S |
| S9 | Bug/Fix | P1 | Terminal session flush runs in cancellable viewModelScope | feature/ui/.../ui/quiz/QuizViewModel.kt:1301 | S |
| U9/M4 | Maturize | P1 | TTS: no pause/resume, no completion listener, no lifecycle stop | core/ui/.../ui/utils/TtsManager.kt:27 | M |
| R1 | Refactor | P1 | Split QuizViewModel/QuizPlayerScreen (god-object, whole-state recompose) | feature/ui/.../ui/quiz/QuizViewModel.kt:1 | L |
| R2 | Refactor | P1 | Split BookToolsViewModel (partial-overwrite state bleed) | feature/ui/.../ui/booktools/BookToolsViewModel.kt:98 | L |
| S17 | Bug/Fix | P2 | emptyTrash reads cold .value → can silently no-op | feature/ui/.../ui/library/LibraryViewModel.kt:599 | S |
| U7/M3 | Bug/Fix | P2 | Slide completion doesn't update course aggregate live | feature/ui/.../ui/slideshow/SlideshowCourseViewModel.kt:303 | S |
| S10/M5 | Maturize | P2 | syncDerivedAssets not wired to question edits | core/data/.../repository/AssetRepository.kt:551 | M |
| U13 | Improvement | P2 | Recent/Resume reflect edits, not study | feature/ui/.../ui/library/LibraryViewModel.kt:190 | S |
| U2 | Bug/Fix | P2 | Resume timer burns wall-clock while backgrounded | feature/ui/.../ui/quiz/QuizViewModel.kt:395 | M |
| S11/S12 | Performance | P2 | Whole-state recomposition + flow factories in composition | feature/ui/.../ui/quiz/QuizPlayerScreen.kt:220 | M |
| S13/R4 | Performance | P2 | Category re-filters on every checkbox; unchecked combine casts | feature/ui/.../ui/category/CategoryQuestionsViewModel.kt:88 | S |
| U3/U4 | Improvement | P3 | Rapid auto-advance + elimination need undo/confirm | feature/ui/.../ui/quiz/QuizViewModel.kt:1118 | S |
| U11 | Bug/Fix | P3 | Prompt-editor AI output lost on rotation (not rememberSaveable) | feature/ui/.../ui/booktools/BookToolScreens.kt:809 | S |
| TG1 | Testing | P0 | No tests on session race / adaptive loss | feature/ui/.../ui/quiz/QuizViewModel.kt:1021 | M |
| TG2 | Testing | P1 | No Summary answer-consistency test | feature/ui/.../ui/summary/SummaryViewModel.kt:150 | S |
| TG4 | Testing | P1 | No knowledge-session round-trip/Moshi-failure test | feature/ui/.../ui/flashcard/FlashcardDeckViewModel.kt:180 | S |
