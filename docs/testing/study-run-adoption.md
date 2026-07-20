# Manual Test Plan — StudyRun Adoption

> Covers the migration of resume state onto `study_runs` for the flashcard and slideshow players.
> Nothing here has been verified on a device: the development machine has no emulator, AVD, or
> system image, so only compilation and JVM unit tests were run. **Treat every case below as
> unverified until you tick it.**
>
> Related: `docs/roadmap.md` §1.0, `StudyRunRepositoryImplTest`, `Migration31To32Test`.

---

## 0. Prerequisites

```bash
# One-time, if the SDK has no system image yet
sdkmanager "system-images;android-34;google_apis;arm64-v8a"
avdmanager create avd -n mks-test -k "system-images;android-34;google_apis;arm64-v8a"

# Build and install
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew installDebug

# Run the instrumented migration tests (also never yet executed)
./gradlew :core:database:connectedDebugAndroidTest
```

Two ways to kill the app; they are **not** equivalent and both matter:

- **Process death** — `adb shell am kill com.ahmedyejam.mks`. Simulates the OS reclaiming memory.
  The ViewModel is destroyed but the task remains. This is the case resume exists for.
- **Swipe-away** — remove from recents. Triggers `onCleared()` and the `applicationScope` save.

---

## 1. Slideshow resume — the bug this work fixed

This player previously **always restarted at slide 1**. These are the cases that were broken.

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 1.1 | Open a course with 10+ slides. Enter presentation. Advance to slide 5. Back out to the course screen. Re-enter presentation. | Opens at **slide 5**, not slide 1. | ☐ |
| 1.2 | Repeat 1.1 but kill via process death instead of backing out. Relaunch, re-enter presentation. | Opens at slide 5. | ☐ |
| 1.3 | Repeat 1.1 but swipe the app away from recents. | Opens at slide 5. | ☐ |
| 1.4 | Advance to slide 5, then rotate the device. | Stays on slide 5; no duplicate run created (see 5.2). | ☐ |
| 1.5 | Advance through **every** slide to the last one. Exit. Re-enter presentation. | Starts fresh at **slide 1** — the run completed, so nothing is resumed. | ☐ |
| 1.6 | Resume a course at slide 5, then use the dots to jump to slide 2. Exit and re-enter. | Opens at slide 2. Position follows the dots, not only next/prev. | ☐ |

## 2. Flashcard resume — regression check

This player already worked via `LearningSession.stateJson`. It now reads from `StudyRun`, so the
risk here is **regression**, not new capability.

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 2.1 | Open a deck with 10+ cards. Study to card 4. Back out. Re-enter study mode. | Opens at **card 4**, face down. | ☐ |
| 2.2 | Repeat 2.1 with process death. | Opens at card 4. | ☐ |
| 2.3 | Rate several cards Again/Good/Easy, exit at card 6, re-enter. | Position **and** the mastery/attempt counters survive — the deck header should not reset to 0 studied. | ☐ |
| 2.4 | Resume a deck, then check the deck stats card. | `studied` and `mastery %` are continuous with the pre-exit values, not restarted. | ☐ |
| 2.5 | Review every card in the deck. Exit. Re-enter. | Starts fresh at card 1. | ☐ |
| 2.6 | Flip a card, exit **while flipped**, re-enter. | Returns face **down**. A resumed card must not reveal its answer. | ☐ |

## 3. Upgrade path — existing users

The old flashcard resume lived in `LearningSession.stateJson`; `study_runs` will be empty for anyone
upgrading. **One lost resume position per in-flight deck is expected and acceptable** — but it must
degrade gracefully, not crash.

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 3.1 | Install the **previous** release. Study a deck to card 5. Force-stop. Install this build over it (no uninstall). Open the deck, enter study mode. | Starts at **card 1** without crashing. A new run is created from there. | ☐ |
| 3.2 | After 3.1, exit at card 3 and re-enter. | Opens at card 3 — the new mechanism has taken over. | ☐ |
| 3.3 | Same as 3.1 but for a slideshow. | Starts at slide 1, no crash. | ☐ |

## 4. Isolation — runs must not bleed across content

`getLatestIncomplete` is keyed on `(contentType, contentId)`. These confirm the key holds.

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 4.1 | Leave deck A at card 5 and deck B at card 2. Re-enter each. | A opens at 5, B at 2. No cross-talk. | ☐ |
| 4.2 | Leave a deck at card 5 and a **course** at slide 2, then re-enter both. | Independent. A slideshow must never resume a flashcard run. | ☐ |
| 4.3 | Leave a deck mid-run, switch workspace, return, re-enter the deck. | Position preserved; no leakage from another workspace's content. | ☐ |
| 4.4 | Leave a deck at card 5. Delete the deck (trash). Restore it. Enter study mode. | Either resumes at 5 or starts clean — but does **not** crash. Note which. | ☐ |

## 5. Edge cases — where I would expect breakage first

These probe the seams I could not exercise without a device. **Highest-value section.**

| # | Steps | Expected | ✅ |
|---|---|---|---|
| 5.1 | Leave a deck at card 8. **Delete cards** so only 3 remain. Re-enter study mode. | Clamps to the last valid card; no `IndexOutOfBounds`. The stored index is coerced, but verify. | ☐ |
| 5.2 | Enter and exit presentation mode ~5 times without finishing. Then inspect the DB (see §6). | Exactly **one** incomplete `study_runs` row for that course — not five. Orphan accumulation was the old bug. | ☐ |
| 5.3 | Leave a deck mid-run. **Add** new cards to it. Re-enter. | Resumes at the old index; new cards reachable. No crash. | ☐ |
| 5.4 | Enter study mode on an **empty** deck (0 cards). | Empty state shown; no run created; no crash. | ☐ |
| 5.5 | Rapidly swipe through 20+ cards, then immediately background the app. | Final position persists — writes go through `applicationScope`, so the last save must survive teardown. | ☐ |
| 5.6 | Enter study mode, immediately background before the first save. Relaunch. | No crash; either fresh start or card 1. | ☐ |
| 5.7 | Turn the device to Arabic (RTL), resume a deck and a course. | Position restores identically; nav arrows mirror correctly. | ☐ |

## 6. Database inspection

```bash
adb exec-out run-as com.ahmedyejam.mks \
  sqlite3 databases/mks_database "SELECT id, contentType, contentId, currentIndex, isCompleted, completedItemIds FROM study_runs;"
```

Assertions worth making directly against the table:

- ☐ At most **one** `isCompleted = 0` row per `(contentType, contentId)` pair.
- ☐ `completedItemIds` is valid JSON (`[]` or `[1,2,3]`), never `null` or empty string.
- ☐ Finishing all items flips `isCompleted` to `1` and sets a non-zero `completedAt`.
- ☐ `orderedItemIds` matches the real item ids of that deck/course.

## 7. Regression sweep — things adoption could have broken

| # | Area | Check | ✅ |
|---|---|---|---|
| 7.1 | Stats | Study time still accrues on the review dashboard; `LearningSession` records still created. | ☐ |
| 7.2 | Slideshow "Mark studied" | Toggling still persists the per-slide `isCompleted` flag independently of the run. | ☐ |
| 7.3 | Flashcard rating | Again/Good/Easy still update deck mastery. | ☐ |
| 7.4 | Session management screen | Existing sessions list is unaffected. | ☐ |
| 7.5 | Trash | Deleting a deck/course with an active run does not orphan or crash. | ☐ |

---

## Known limitations of this change

- **Article/notes player and AI prompt deck were not migrated.** They are not item-sequence players,
  so `StudyRun`'s index/ordered-ids model does not fit them cleanly. They keep their current
  behaviour.
- **`ADAPTIVE_QUIZ` was not migrated.** It already persists via `SessionEntity`, so audit finding #6
  is closed independently. Moving it is optional cleanup.
- **`LearningSession.stateJson` is still written** by both migrated players. It is now a
  write-only analytics artifact for resume purposes. Removing it is safe follow-up work once the
  cases above pass, but it was left in place to keep this change reversible.

*Status: Unverified | Owner: AhmedEjam | Created: 2026-07-20*
