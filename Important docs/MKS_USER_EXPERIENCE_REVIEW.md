# MKS — Final User Experience Review & Journey Map

> **Supersedes and absorbs** `USER_JOURNEY_MAP_claudeopus.md` and `user_Jour_Geminipro.md`. Both are folded into this document in full and are no longer needed. This is the single authoritative **user-perspective** reference: what every screen and control does, where the experience is smooth, where it frustrates or silently loses the user's work, and an exhaustive catalog of every addon, feature, option, interactable, and utility that would improve MKS as a thing people actually study with.
>
> **Lens:** Written entirely from the **end user's** point of view — a student, resident, or self-learner. No code, no file paths, no architecture. Security/engineering internals live in the separate developer review; only the user-facing edges of those (privacy, trust, data loss) appear here.
>
> **App in one sentence:** MKS (My Knowledge Space) is an offline-first Android study app that turns imported content (spreadsheets, documents, PDFs, photos, AI output) into interactive quizzes, flashcards, slideshows, note blueprints, and AI prompt decks — organized as Books inside isolated Workspaces, with progress tracking, spaced-repetition review, and cross-content search.

---

## Table of Contents

- **Part A — The Experience at a Glance**
  - A1. The full journey, as a user lives it
  - A2. What delights
  - A3. Top frictions, ranked by how much they hurt
  - A4. Where the app quietly loses your work (trust-breakers)
  - A5. Promises vs. reality
- **Part B — Screen-by-Screen Journey Map** (absorbs both prior maps)
  - B1 Launch & Onboarding · B2 Library Hub · B3 Import / Compiler · B4 Quiz Player · B5 Summary · B6 Session Management · B7 Quiz Questions Browser · B8 Category Questions · B9 Adaptive Training · B10 Book Knowledge Dashboard · B11 Flashcards · B12 Slideshows · B13 Note Blueprints / Reader · B14 AI Prompt Decks · B15 Source Documents · B16 Book Notes · B17 AI MCQ Generator · B18 PDF Extraction · B19 Scanner (OCR) · B20 Global Search · B21 Review Dashboard · B22 Data Tools · B23 Settings · B24 Dialogs & Overlays
- **Part C — Cross-Cutting Experience Themes**
- **Part D — Exhaustive Improvement Catalog** (every addon / feature / option / interactable / utility)
- **Part E — Quick Wins & a Delight Roadmap**

> **Legend used throughout:** 😀 delight · ⚠️ friction · 🩹 trust-breaker (can lose your work/data) · 💡 improvement idea. Priority tags on ideas: **P0** (fix now, actively hurts), **P1** (high value), **P2** (roadmap), **P3** (nice-to-have).

---

## Part A — The Experience at a Glance

### A1. The full journey, as a user lives it

You install MKS and land on a **Welcome** screen: pick a language, peek at features, tap **Get Started**. From then on the app always opens to the **Library** — your home base, pre-seeded with one sample book so it's never empty on day one.

You bring in your own material. Tapping the **+** button (or sharing a file from another app) opens the **Import/Compiler** flow: MKS auto-detects the format (spreadsheet, CSV, JSON, HTML, text, or a ZIP bundle), finds the header row, maps columns to fields, and shows a **live preview** of parsed questions you can toggle and correct before saving. Alternatively you photograph a page (**Scanner/OCR**), extract text from a **PDF**, paste text into a deck, or have the **AI MCQ Generator** draft questions for you.

Content is organized as **Books** → which hold **Quizzes** (of Questions) plus a **Knowledge Bank** (flashcard decks, slideshow courses, note blueprints, AI prompt decks, source documents). Everything lives inside a **Workspace** you can switch between for isolation (e.g., "Med School" vs. "Board Prep").

You study. Open a quiz → **Session Management** lets you resume a saved session or start a new one with rich options (filters, ranges, shuffles, timers, rapid/repeat modes). The **Quiz Player** is the heart of the app: one question at a time, options you tap/eliminate, a draggable control sheet, a live score/streak/accuracy readout, per-question navigation grid, and an expandable explanation after answering. Finishing takes you to the **Summary**: score, best streak, per-category performance, and a filterable review of every question, plus share/retry.

Between quizzes you review with **Flashcards** (flip + Again/Good/Easy spaced-repetition), **Slideshows** (swipe + mark studied), **Note Blueprints** (an immersive reader with autoscroll and text-to-speech), and **AI Prompt Decks** (fill variables → run → route the output into a note/blueprint/deck/quiz). The **Review Dashboard** gathers everything due today across flashcards, blueprints, and mistakes. **Global Search** finds anything by text. **Book Dashboard** shows a book's whole learning ecosystem with one-tap "Magic Actions." **Settings** controls themes, font/density, language, study defaults, focus mode, and backup. **Trash** recovers soft-deleted items; **Data Tools** does full library backup/restore.

That's the arc: **bring in → organize → study → review → track → back up.** It's a genuinely deep, capable study system. The rough edges are almost all about **feedback, resume, and confidence that your work was saved** — not missing features.

### A2. What delights 😀

- **Never a blank slate.** The seeded sample book means a brand-new user has something to tap immediately.
- **Import is unusually flexible.** Auto-format detection, header auto-mapping, a live editable preview, and support for spreadsheets/CSV/JSON/HTML/text/ZIP/PPTX/PDF/photos/pasted text/AI. Few study apps ingest this many sources.
- **One idea, five formats.** The same source questions can become flashcards, slides, notes, or a quiz with a tap ("Magic Actions," "Create Flashcards from selected," prompt-output routing). Content compounds instead of being siloed.
- **The Quiz Player is rich.** Rapid mode, one-by-one reveal, elimination, per-question timers, marking, drop/restore, category toggles, a jump grid with color-coded states, swipe navigation — power users get a lot of control.
- **Bilingual + RTL.** English/Arabic with right-to-left layout, and even Arabic header aliases when importing spreadsheets — a real edge for that audience.
- **Immersive note reader.** Autoscroll with adjustable speed plus text-to-speech with pitch/rate — a hands-free study mode most flashcard apps lack.
- **Workspaces + Trash + full backup.** Isolation, soft-delete recovery, and ZIP export/import show a maturity of data-stewardship thinking.

### A3. Top frictions, ranked (by how much they hurt)

1. ⚠️🩹 **"My questions disappeared after import."** The preview shows rows that later get silently skipped on save; you get no "N saved, M skipped, here's why." *(Partially addressed in a recent update that now shows a save summary — verify it reaches the dialog.)*
2. 🩹 **Flashcard/slideshow "resume" didn't remember where I was.** You could rate cards for ten minutes, leave, come back, and start again from card 1. *(A recent update added flashcard resume; slideshows still restart.)*
3. 🩹 **Adaptive/Personalized training lost all progress if the app closed** mid-session. *(A recent update gives adaptive runs a real saved session.)*
4. ⚠️ **The Summary could contradict itself** — a card shown under the "Correct" filter could display as WRONG. *(A recent update unified this.)*
5. ⚠️ **Editing a quiz did nothing** — tapping Edit on a quiz opened no dialog. *(A recent update fixed the dialog.)*
6. ⚠️ **Language change didn't actually switch the UI** until a manual restart. *(A recent update applies it immediately.)*
7. ⚠️ **Encrypted or image-only PDFs spun forever** with no explanation. *(A recent update shows an error.)*
8. ⚠️ **Global Search returned other workspaces' content** — breaking the "each workspace is isolated" promise.
9. ⚠️ **The Review Dashboard promises 7 queues but only builds 5** — "unfinished slides" and "annotations" never show up as review items.
10. ⚠️ **No feedback almost everywhere in the Knowledge Bank.** Creating a note/deck/source, saving AI output, deleting — often silent, so you're never sure it worked.
11. ⚠️ **"Recent" and "Resume" reflect edits, not study.** Editing a quiz's title bumps it to the top of "recent," and "Resume" can point at a quiz you never actually studied.
12. ⚠️ **Session progress bars can be wrong** after you add/remove questions — a finished custom session can read "10%."
13. ⚠️ **Marked-answer spreadsheets showed a literal `*`** in the option text. *(A recent update strips the marker.)*
14. ⚠️ **AI generation "Cancel" bricked progress** — after one cancel, later generations showed no progress. *(A recent update fixed the cancel scope.)*
15. ⚠️ **Attaching an image/PDF to a question is a raw text field**, not a picker — and a pasted link can break after an app restart.

### A4. Where the app quietly loses your work (trust-breakers) 🩹

Trust is the currency of a study app — you're pouring hours of content and progress into it. These are the moments that erode it. Several were addressed in a recent round of fixes (marked ✅ *reportedly fixed*), but the *pattern* is what matters: **persist continuously, restore faithfully, and always confirm the save.**

- **Import skips without telling you.** ✅ *reportedly fixed* (save summary added).
- **Knowledge-bank study doesn't resume.** ✅ *flashcards fixed*; ⬜ slideshows still restart; note-reader scroll/TTS position not saved.
- **Adaptive training had no saved session.** ✅ *reportedly fixed*.
- **In-progress AI output is lost on rotation** (prompt-deck output field isn't preserved through a screen rotation or backgrounding).
- **A partially-selected multiple-choice answer isn't saved** if the app is killed before you submit — you re-enter the question blank.
- **"Saved to notes" can be a lie** — the toast appears even when nothing was saved (e.g., a PDF source with no parent book).
- **No undo after import** — a bad column mapping that imported 300 malformed questions has to be cleaned up by hand.
- **Emptying the Trash can silently no-op** in some states while still reporting success.

### A5. Promises vs. reality

The marketing/journey descriptions make claims the experience doesn't fully honor yet. Closing these gaps is high-leverage because they're about **living up to what the user was told**:

| The user is told… | What actually happens | Fix direction |
|---|---|---|
| "Language switches instantly, the whole UI re-renders" | Written to settings but UI didn't change until restart | ✅ *reportedly applied immediately now* |
| "Sessions are persisted / you can resume" | True for quizzes; knowledge-bank resume didn't restore | ✅ *flashcards fixed*; extend to slideshows/notes |
| "Each workspace has its own isolated data" | Library isolates, but Global Search leaks across workspaces | Scope search to the active workspace |
| "Unified review queue, 7 types" | Only 5 queue types are actually built | Add unfinished-slides + annotations queues |
| "Back saves session progress automatically" | True for normal sessions; adaptive lost it | ✅ *reportedly fixed* |
| "Export = full library backup" | Media/images historically not bundled in the plain Data-Tools export | Bundle media, or clearly label what's included |

---

## Part B — Screen-by-Screen Journey Map

> This section absorbs the complete screen inventories from both prior journey maps. Every screen, control, gesture, and dialog is listed with what it does, plus inline **⚠️ friction / 🩹 trust / 💡 idea** notes where the experience can improve.

### B1. Launch & Onboarding

**Welcome Screen** (`welcome`) — first launch only (re-enable in Settings).

| Element | What it does |
|---|---|
| **Language pill (EN / العربية)** | Switches language, saves to preferences. |
| **"Explore Features" button** | Opens an informational dialog listing capabilities. |
| **"Get Started" button** | Marks onboarding complete, goes to Library, removes Welcome from the back stack. |

- ⚠️ Onboarding is a single static screen — it *tells* rather than *shows*. 💡 **P2:** Replace with a 3–4 card interactive tour (import a sample, answer a sample question, flip a card) so the first five minutes create a "win." 💡 **P2:** Offer a "Start with a template" choice (Medical, Language, Law, Custom) that seeds relevant sample content. 💡 **P3:** Theme preview carousel on this screen so the first impression is personalized.

### B2. Library (Main Hub)

**Route:** `library`. The central dashboard: Books at root; a book's Quizzes + Knowledge-Bank items when you tap in; also browse by Category.

**Top App Bar**
| Element | What it does |
|---|---|
| **← Back** | Appears inside a book or in search; returns to root / exits search. |
| **⋮ Overflow → Sort** | Sort dialog: Name A–Z/Z–A, Date Created, Date Edited, Progress. |
| **⋮ → Grid/List toggle** | 2-column grid vs single-column list; persisted. |
| **⋮ → Search** | Inline search bar filtering books/quizzes/categories by name, with a clear (✕). |
| **⋮ → Settings** | Opens Settings. |
| **Title** | "MKS Library" at root, book title inside a book, "Category: [name]" when browsing a category. |

**Content area (grid/list)**
| Element | What it does |
|---|---|
| **Book card — tap** | Opens the book (filters to its quizzes + knowledge items). |
| **Book card — long-press** | Opens the OptionsSheet (Pin, Edit, Dashboard, Export, Import Into, Delete). |
| **Quiz card — tap** | Goes to Session Management for that quiz. |
| **Quiz card — long-press** | OptionsSheet with quiz options (incl. Scanner). |
| **Category chip — tap** | Category Questions screen (that category across the book). |
| **Knowledge-bank item — tap** | Opens the relevant screen (deck, course, blueprint, prompt deck). |

**Floating Action Button (+)** — expands a context-dependent menu:
- *Always:* **Import** (file picker: XLSX/CSV/TSV/JSON/HTML/TXT/ZIP → Compiler), **Export** (ZIP of current book or whole library).
- *Inside a book:* **New Quiz, Adaptive Training, Flashcard Deck, Slideshow Course, Review Blueprint, Sources, Book Notes, AI Prompt Deck.**
- *At root:* **New Book, Adaptive Training (all books).**

**OptionsSheet (long-press):** Pin/Unpin · Edit · Book Dashboard (books) · Export · Import Into · Scanner (quizzes) · Delete (cascades: book→quizzes→flashcards→slideshows→notes; quiz→questions→sessions).

**Frictions & ideas**
- ⚠️ **Sort options can silently do nothing** — sorting categories by "Last studied" or books by certain metrics is a no-op; the user picks a sort and sees no change. 💡 **P1:** Hide sorts that don't apply to the current view, or make them work.
- ⚠️ **"Recent"/"Resume" reflect edits, not study** — renaming a quiz floats it to the top of "recent," and Resume can surface a quiz you never studied. 💡 **P1:** Track *last studied* separately from *last edited*; base Recent/Resume on study.
- ⚠️ **No workspace indicator on the hub** — you can't tell which workspace you're in at a glance, and switching is buried. 💡 **P1:** Show the active workspace name in the top bar with a one-tap switcher.
- 💡 **P1:** A **home "Today" strip** above the books: "12 flashcards due · 3 mistakes to review · resume 'Cardio quiz' (18/40)." Turns the hub into a launchpad, not just a file list.
- 💡 **P2:** **Continue studying** card that deep-links to the exact question/card you left on.
- 💡 **P2:** **Multi-select on the hub** (bulk pin/export/delete/move-to-workspace).
- 💡 **P2:** **Book cover art auto-suggestions**, book color-coding, and a **favorites/pinned row**.
- 💡 **P3:** **Folders/collections** for books (many med students have 20+ books).
- 💡 **P3:** Drag-to-reorder books; swipe actions on list rows (swipe to pin/delete).

### B3. Import / Compiler flow

Reached from the FAB **Import**, a book/quiz's **Import Into**, or an Android **Share** from another app. Two paths: the **Compiler** (spreadsheets, CSV, JSON, HTML, text) and the **ZIP/bundle Review** dialog.

**Compiler steps the user sees:** format auto-detected → (XLSX) pick a sheet → header row auto-found → column→field mapping shown → **live preview list** of parsed questions with per-question include toggles, correct-answer highlighting, range include/exclude, long-press to mark the correct option → **Save** (title + target book/quiz/deck).

**Frictions & ideas**
- 🩹 **Silent skips on save** — the preview shows questions that the save step drops (no correct answer, no options, blank stem). ✅ *reportedly fixed* (a save summary now reports counts + warnings). 💡 **P1:** Go further — **badge the doomed rows in the preview** with the reason ("no correct answer") so they're fixed *before* saving, not reported after.
- ⚠️ **Wrong option marked correct** on some spreadsheets/JSON due to 1-based vs 0-based answer numbering. ✅ *partially fixed*. 💡 **P1:** Detect ambiguity and, when unsure, show a one-line "we guessed the answer key — confirm?" prompt.
- ⚠️ **Legacy `.xls` imports fail outright.** 💡 **P1:** Support old Excel or show "please save as .xlsx."
- ⚠️ **HTML imports usually fail** (nested question data isn't parsed). 💡 **P2:** Robust HTML table/JSON extraction, or drop HTML from the "supported" list until it works.
- ⚠️ **Insecure-HTTP images silently dropped** in the non-ZIP path (no consent checkbox like the ZIP path has). 💡 **P2:** Offer the same "allow HTTP images" choice everywhere, with a plain-language privacy note.
- ⚠️ **Large files can sit on a spinner** with no progress or cancel. 💡 **P1:** Determinate progress + a Cancel button; show row/size counts up front.
- 💡 **P1: Undo last import** — one tap to reverse the most recent import (the app already knows which books/quizzes it touched).
- 💡 **P2: Saved mapping presets** — remember the column layout for a recurring spreadsheet format and auto-apply it next time.
- 💡 **P2: Batch multi-file import** with a queue and a per-file result summary.
- 💡 **P2: Dry-run / validate-only** mode — see the skip report without committing.
- 💡 **P3:** Drag-to-reorder columns in the mapping editor; a "sample row" preview under each column so mapping is visual.
- 💡 **P3:** Import directly from a URL / Google Sheets link.

### B4. Quiz Player

**Route:** `quiz/{quizId}?sessionId={sessionId}`. The core study interface — one question at a time.

**Top bar:** ← Back (exits, saves progress) · Timer (MM:SS, counts down if a timer is set) · Progress "3/25" · Score "★ 5/25" · Streak "🔥 3" (hidden at 0) · thin progress bar.

**Question area:** stem (multi-line) · image (tap → fullscreen pinch-zoom) · Assets paperclip (read-only attachments) · horizontal swipe (next/prev, animated) · explanation card after answering (also Hint, High-Yield, Reference).

**Options:** tap to select (radio/checkbox) · double-tap to select+submit (if enabled) · long-press to eliminate (strikethrough) · elimination-mode ✕ per option. After submit: green (correct) / red (your wrong pick); Submit → Next.

**Bottom sheet (drag up):**
- *Peek:* live Accuracy % (color-coded) · Streak · Submit/Next/Reveal button.
- *Expanded toggles:* Categories · One-by-One · Rapid Mode · Eliminate · Drop Question (confirm) · Focus Mode (DND) · Mark/Bookmark · Finish Quiz.
- *Navigation:* filter chips (ALL/ANSWERED/UNANSWERED/MISSED/MARKED/DROPPED) · color-coded question-number grid (tap to jump) · "Restore dropped question" chip.

**Frictions & ideas**
- ⚠️ **No "saved" reassurance** — progress *is* saved continuously for normal sessions, but nothing tells the user, so long sessions feel risky. 💡 **P1:** A subtle "progress saved" micro-indicator, and an "exit without finishing?" confirmation that reassures ("your progress is saved — resume anytime").
- 🩹 **Rapid mode auto-advances with no undo** — a mis-tap commits and jumps. 💡 **P1:** Show a brief countdown with a Cancel/Undo tap during the auto-advance delay.
- 🩹 **Eliminating the correct option instantly scores it wrong**, no confirm (unlike Drop Question, which confirms). 💡 **P1:** Confirmation or an undo window; treat elimination as reversible until submit.
- ⚠️ **Timer keeps burning while the app is backgrounded** on timed quizzes — step away and you "lose" time you never spent. 💡 **P1:** Pause the countdown while backgrounded; restore exactly on return.
- ⚠️ **Per-question time isn't remembered on resume**, so the resumed timer restarts oddly. 💡 **P2:** Persist elapsed time per question.
- 💡 **P1: Confidence rating** on each answer (guessed / unsure / confident) feeding adaptive selection and the summary.
- 💡 **P1: Flag for later** distinct from Mark, plus a quick "add note to this question" from within the player.
- 💡 **P2: Keyboard/volume-key answering** and a larger-tap-target accessibility mode.
- 💡 **P2: Strikethrough persistence** — remember eliminated options if you navigate away and back.
- 💡 **P2: Explanation-first (learn) mode** — reveal the answer + explanation immediately for first-pass learning vs testing.
- 💡 **P2: Haptic + optional sound** feedback on correct/incorrect (toggleable).
- 💡 **P3: Swipe-to-eliminate** gesture; **double-column layout** on tablets/landscape.
- 💡 **P3: Read question aloud** (TTS) button in the player for accessibility / eyes-free review.

### B5. Summary (Post-Quiz)

**Route:** `summary/{sessionId}`.

**Score header:** big % · fraction ("17/20 correct") · best streak · session label · average time/question.
**Top bar:** Share (system share sheet with text export) · ✕ Close (→ Library).
**Review section:** gear → Visible-Details panel (toggle Stem/Options/Hint/High-Yield/Reference/Number/Explanation) · filter chips (ALL/CORRECT/WRONG/UNANSWERED/DROPPED/WITH EXPLANATION) · color-coded review cards.
**Category performance:** per-category accuracy breakdown.
**Bottom:** Retry (new session, same quiz) · Library.

**Frictions & ideas**
- ⚠️ **The screen could contradict itself** — a card under the "Correct" filter could show WRONG; category stats used different logic than the cards. ✅ *reportedly unified.* 💡 **P1:** Finish the job — make category performance use the same correct/incorrect classification as the cards and export.
- ⚠️ **"Avg time/question" can be misleading** (mixes lifetime question time and placeholders). 💡 **P2:** Compute strictly from this session.
- ⚠️ **"Clear Marks" is hidden behind a bookmark icon** and is quiz-wide/destructive. 💡 **P1:** Give destructive actions explicit labels and clear confirmation copy.
- 💡 **P1: Actionable summary** — one-tap "Make flashcards from the ones I missed," "Draft a note from my mistakes," "Retry only the wrong ones."
- 💡 **P1: Progress over time** — "you scored 72%, up from 65% last attempt on this quiz," with a small trend sparkline.
- 💡 **P2: Time-per-question breakdown**, slowest/fastest questions, and a "questions you flip-flopped on" list.
- 💡 **P2: Export the summary as PDF/image** for sharing or a study log, not just plain text.
- 💡 **P3: Celebratory moment** for personal bests (streak record, first 100%) — a small, tasteful animation.

### B6. Session Management

**Route:** `sessions/{quizId}`. All saved sessions for a quiz.

| Element | What it does |
|---|---|
| **← Back** | Previous screen. |
| **+ FAB** | Start Session Dialog. |
| **Session card** | Label, accuracy ring, score metrics, progress bar, last-active date. Tap: resume (incomplete) or view Summary (complete). Delete: confirm → removes the record. |
| **Empty state** | Tap to create a session. |

**Start Session Dialog:** label · include filters (Unanswered/Missed/Marked/Categorized/Uncategorized) · question range (From/To) · Shuffle Questions · Shuffle Options · Rapid Mode · Repeat Wrong · Quiz Timer (min) · Question Timer (sec) · Remember Settings · Start.

**Frictions & ideas**
- ⚠️ **Progress % can be wrong** — computed against the quiz's *current* size, so after you add/remove questions (or for a custom subset session) a finished session can read "10%." 💡 **P1:** Compute against the session's own question set; add a "quiz changed since this session" badge.
- ⚠️ **No staleness cue** — resuming silently remaps onto a changed quiz. 💡 **P1:** Warn when the underlying quiz changed.
- ⚠️ **The range fields can lock up** if the dialog opens before the question count loads (range stuck at 0, Start disabled). 💡 **P2:** Default the range once the count arrives; validate gracefully.
- 💡 **P2: Session templates** — save named presets ("Timed 50 random," "Missed-only review") for one-tap start.
- 💡 **P2: Duplicate a past session's settings**; rename sessions after the fact.
- 💡 **P3: Auto-name sessions** meaningfully ("Ch.3, 40 Q, timed") instead of blank/generic.

### B7. Quiz Questions Browser

**Route:** `quiz_questions/{quizId}`. Manage/review/organize questions (not for taking the quiz).

**Top bar:** ← Back · Search (filter by text) · Filter (toggle panel) · title + "X/Y Questions."
**Filter chips:** Stem, Options, Correct Answer, Explanation, Hint, Reference, Info, Marked Only, Has Attachments (toggle field visibility on cards).
**Question cards:** long-press → multi-select · tap → Edit Question Dialog · bookmark icon · edit (pencil) · attachments (paperclip → Question Assets Dialog) · image tap → fullscreen.
**Selection-mode bar:** Clear · Select All · Bookmark/Unbookmark selected · Move (to another quiz) · Copy (to another quiz) · Export (new quiz from selection) · Create Flashcards (from selection, option to clear marks) · Delete (confirm).
**+ FAB:** manual new question. **Summary card:** totals (questions/marked/missed/notes/assets/sources).

**Frictions & ideas**
- 🩹 **Move/Copy strands attachments** — moving/copying questions doesn't carry their assets/annotations, and moved questions' assets point at the wrong book. 💡 **P1:** Carry attachments with the question; make move/copy atomic; confirm the result.
- ⚠️ **The editor edits a snapshot** — if the list refreshes while the dialog is open, saving can overwrite newer changes. 💡 **P2:** Re-read on save or lock the row.
- ⚠️ **No inline feedback** on delete/move/copy. 💡 **P1:** Snackbar with Undo for destructive bulk actions.
- 💡 **P1: Bulk edit** — set weight, add/remove a category, or add a tag across selected questions.
- 💡 **P2: Duplicate-question detection** within a quiz; find-and-replace across stems.
- 💡 **P2: Reorder questions** (drag) and set a canonical order used by non-shuffled sessions.
- 💡 **P3: Rich-text / LaTeX / code formatting** in stems and explanations (huge for STEM/med).

### B8. Category Questions

**Route:** `category/{categoryName}`. Same as the Questions Browser but scoped to a category across the book, plus a **"Start Quiz" FAB** that launches adaptive training on just that category.

- 💡 **P2:** Category management here — rename, merge, recolor, set an emoji, and see per-category mastery %.
- 💡 **P2:** "Study weakest category" shortcut surfaced when a category's accuracy is low.

### B9. Adaptive Training

**Route:** `adaptive/{type}/{id}` — not a separate screen; it's the Quiz Player fed intelligently selected questions. Types: **BOOK** (weak/unanswered/recently-missed), **CATEGORY**, **QUIZ** (a quiz's weak spots), **ALL** (everything). Priority: unanswered > recently wrong > low-weight > marked → Summary at the end.

**Frictions & ideas**
- 🩹 **Historically lost all progress on app kill** (no saved session). ✅ *reportedly fixed.* 💡 **P1:** Also let the user **resume** an interrupted adaptive run rather than always starting fresh, and label these sessions clearly in the session list so they don't accumulate as mystery entries.
- ⚠️ **The user can't see *why* a question was chosen.** 💡 **P2:** A tiny "chosen because: you missed this twice" tag builds trust in the algorithm.
- 💡 **P1: Tunable adaptivity** — a simple slider (Review my weak spots ⇄ Cover everything evenly) and a "focus this session on [category]" option.
- 💡 **P2: Daily adaptive goal** ("20 adaptive questions/day") with a streak.

### B10. Book Knowledge Dashboard

**Route:** `book_dashboard/{bookId}`. A book's whole learning ecosystem.

**Study Progress card:** completion bar · Due · Weak · Marked · Mistakes counts.
**Magic Actions:** "Draft Note from Marked" (if marked>0) · "Note from Mistakes" (if open mistakes>0) — plus (per the FAB context) generate flashcards/slides from questions/marked/missed.
**Learning Tools grid:** Quizzes · Flashcards · Slideshows · Notes · AI Prompts · Sources (each with a count, tapping into its list).

**Frictions & ideas**
- ⚠️ **The Study Progress card only reflects quiz answering** — studying flashcards/slides/notes doesn't move it, so the book feels "stuck" even after hours of review. 💡 **P1:** Fold all study activity into the book's progress and "last studied."
- ⚠️ **No feedback when Magic Actions run** — a deck/note is created silently. 💡 **P1:** Snackbar "Created 'Marked Cards' deck — Open," and route straight into the new asset.
- ⚠️ **Auto-generated titles are timestamp fragments** ("Marked Cards - 4821"). 💡 **P2:** Human titles ("Marked cards from Cardiology — Jul 12").
- 💡 **P1: A real book overview** — mastery %, cards due today, next exam countdown (if set), a 7-day study heatmap, weakest categories.
- 💡 **P2: More Magic Actions** — "Make a mock exam (50 mixed)," "Summarize this book into one blueprint," "Generate a cram sheet."
- 💡 **P2: Per-book study goal & reminder** ("study Pharmacology 30 min/day").

### B11. Flashcard Deck Screen

**Route:** `flashcards/{deckId}`. Two modes.

**Top bar:** ← Back · mode toggle (List/Study) · edit deck.
**List mode:** stats card (title/desc/count/studied/mastery + progress) · Add card · Study · "Generate from questions" (Marked / Missed, option to clear marks) · card rows (front/back preview, attempt/correct counts, source link, move up/down, edit, delete).
**Study mode:** counter "3/15" · card surface (tap to flip) · Flip button · rating buttons after flip (Again/Good/Easy) · Previous/Next.

**Frictions & ideas**
- 🩹 **Resume didn't remember your place** — you'd restart at card 1. ✅ *reportedly fixed for flashcards.* 💡 **P1:** Confirm the resume prompt ("Resume where you left off?") is offered and the flip state is remembered too.
- ⚠️ **No deck-complete recap** — reaching the last card just stops; the tracked accuracy isn't shown. 💡 **P1:** A "deck complete" screen with accuracy, time, and "review the ones you got wrong."
- ⚠️ **No "due today" surfacing** — spaced repetition runs under the hood but the user can't see which cards are due per deck. 💡 **P1:** "N due" badge per deck and a "Study due" button.
- ⚠️ **No feedback that a rating registered.** 💡 **P2:** Subtle confirmation on each rating.
- 💡 **P1: Card text import / paste** is supported — surface it prominently; add **image occlusion** cards (huge for anatomy) and **cloze deletion** ({{c1::...}}).
- 💡 **P1: Undo last rating** (mis-tapped Again vs Easy).
- 💡 **P2: Audio on cards** (pronunciation/listening), **two-sided study** (front→back and back→front), **type-the-answer** cards.
- 💡 **P2: Session length control** ("study 20 cards," "study 10 min").
- 💡 **P3: Leech detection** ("this card keeps failing — rewrite or suspend it?").

### B12. Slideshow Course Screens

**List** (`book_slideshows/{bookId}`): + FAB (new course: title/desc) · course rows (title/desc/slide-count, tap to open, edit, delete).
**Course** (`slideshow/{courseId}`): + FAB (new slide) · slide rows (title + preview, edit, delete) · in study/present mode, swipe through slides and mark studied.

**Frictions & ideas**
- 🩹 **Resume not restored** and **completion stats go stale** — marking a slide studied updates the slide but the "X completed / Y%" card doesn't move until you leave and return. 💡 **P1:** Apply the same resume treatment as flashcards; update the course progress live.
- 💡 **P1: Autoplay / presentation mode** with a timer per slide and optional TTS narration (the note reader already has TTS — reuse it).
- 💡 **P2: Slide images, PPTX import** (supported — surface it), speaker notes view, and a "quiz me on these slides" action.
- 💡 **P3: Landscape full-screen present mode** with tap zones for next/prev.

### B13. Note Blueprints / Immersive Reader

**List** (`book_blueprints/{bookId}`): + FAB → create (Manual with title/body/mode, or "From Marked" / "From Missed") · note rows (title/mode/status/preview, tap, edit, delete).
**Reader/Editor** (`blueprint/{noteId}`): body editor · Save · Mark reviewed (increments counter/status) · To flashcards · Append to question note · status info (mode/status/review count). Immersive reader adds: hide system bars, pinned title, **autoscroll (adjustable speed)**, **text-to-speech (adjustable pitch/rate)**.

**Frictions & ideas**
- 😀 The immersive reader with autoscroll + TTS is a genuine differentiator.
- 🩹 **Reader position and TTS position aren't saved** — leave and you lose your place. 💡 **P1:** Persist scroll/read position per note.
- ⚠️ **TTS has no pause/resume and can keep talking after you leave**; the play/stop button can get out of sync when speech finishes. 💡 **P1:** Proper play/pause/resume, stop on background, word-follow highlighting, and a lock-screen/media control.
- ⚠️ **TTS reads the saved text while you're editing a different draft.** 💡 **P2:** Read what's on screen.
- ⚠️ **No save confirmation.** 💡 **P1:** Snackbar on save / mark-reviewed.
- 💡 **P1: Markdown rendering** (headings/lists/bold) in reader mode, not raw text.
- 💡 **P2: Highlight + annotate** within the reader; **font/line-height/serif** reading options; **dark sepia reading theme**.
- 💡 **P2: "Read this to me" from anywhere** (queue multiple notes for a hands-free commute session).
- 💡 **P3: Estimated reading time**, progress bar through the note, and a bionic-reading toggle.

### B14. AI Prompt Deck Screens

**List** (`book_prompts/{bookId}`): + FAB (new deck) · deck rows (tap/edit/delete).
**Deck** (`prompt_deck/{promptId}`): **Prompt cards** (create with `{variable}` placeholders + output type; "Use this card," edit, delete) · **Run section** (variable input fields, rendered-prompt preview, copy [records a run], AI-output paste field, Save run, and route output → Note / Blueprint / Flashcards / Quiz) · **Run history** (last 10).

**Frictions & ideas**
- 😀 Output routing (prompt → any content type) is powerful and unusual.
- 🩹 **In-progress AI output is lost on rotation/backgrounding** (the field isn't preserved). 💡 **P1:** Preserve the output field across rotation; auto-save drafts.
- ⚠️ **No feedback** on save/route; **variable detection** can misfire on ordinary parentheses in prose. 💡 **P2:** Snackbars; tighten variable syntax or let the user confirm detected variables.
- ⚠️ **Clipboard-based AI loop is manual** — copy the prompt, paste into ChatGPT, paste the answer back. 💡 **P1:** If a provider (Ollama/OpenAI-compatible) is configured, **run in-app** and stream the answer directly (the app already has the plumbing).
- 💡 **P1: Prompt template library** (Quiz generator, Flashcard maker, Explain-like-I'm-5, Mnemonic maker, Clinical-vignette) shipped ready to use.
- 💡 **P2: Per-card model/temperature settings**; re-run with tweaks; compare two outputs side by side.

### B15. Source Documents Screen

**Route:** `book_sources/{bookId}`. + FAB (title, type [Textbook/PDF/Lecture/Website/Guideline/Other], details) · source rows (edit, delete with a citation-warning).

- 💡 **P1: Attach the actual file** (PDF/image) via a proper picker that copies it into the app (so links don't rot), then jump straight to **PDF Extraction** or **AI MCQ Generator** from the source.
- 💡 **P2: Open the source inline** (built-in PDF viewer) rather than bouncing to an external app.
- 💡 **P2: Citations** — link a source to specific questions/notes and show "referenced by 12 questions."

### B16. Book Notes Screen

**Route:** `book_notes/{bookId}`. Read-only list of all questions that have notes attached.

- ⚠️ Read-only here; editing happens elsewhere, which is a small detour. 💡 **P2:** Inline edit; filter by "questions with notes / without notes"; export all notes as one study sheet.

### B17. AI MCQ Generator

**Route:** `ai_mcq_generator/{bookId}`. Generate quiz questions from source material with AI.

Controls: source-document selector · AI provider config · generation settings (count, difficulty, types, topic focus) · **Generate** (loading) · generated-questions preview (per-question edit/delete) · **Save to quiz**.

**Frictions & ideas**
- 🩹 **Save happens before review** — questions are persisted before you see the preview, so you can't edit/delete *before* they're committed. 💡 **P1:** Preview first, edit freely, then commit on an explicit Save.
- 🩹 **Cancel used to brick progress** for the rest of the session. ✅ *reportedly fixed.*
- ⚠️ **Offline/timeout is opaque** — a failed generation looks the same as "no questions found," and there's no "you appear offline." 💡 **P1:** Distinct offline/timeout/empty states; a connectivity hint.
- ⚠️ **Generated MCQs aren't quality-checked** — a hallucinated question with a wrong answer key can be saved as-is. 💡 **P1:** Validate structure (4 options, one clearly-correct key), flag low-confidence items, and let the user approve each.
- 💡 **P1: Streaming generation** so the user sees questions appear live, with a running count and a Stop button.
- 💡 **P2: "Explain this question" and "make a harder/easier variant"** one-tap actions; generate distractors for an existing question.
- 💡 **P2: Provider setup wizard** with a "test connection" and friendly guidance for local (Ollama) vs cloud.

### B18. PDF Extraction

**Route:** `pdf_extraction/{sourceId}`. Extract text from a PDF for study material.

Controls: PDF selector · page preview · **Extract text** (progress) · extracted-text viewer (page-by-page, selectable) · Copy all · Save as source · Create slides · Create blueprint.

**Frictions & ideas**
- 🩹 **Encrypted/corrupt PDFs spun forever** with no message. ✅ *reportedly fixed* (now shows an error). 💡 **P1:** Confirm the message is specific ("this PDF is password-protected").
- ⚠️ **Image-only (scanned) PDFs return empty text** with no nudge. 💡 **P1:** Detect no-selectable-text and suggest "Use Vision/OCR instead."
- 🩹 **"Saved to notes" can appear even when nothing saved** (source with no book). 💡 **P1:** Only confirm on a real save; otherwise tell the user why it couldn't.
- 🩹 **Sending text to the MCQ generator uses the system clipboard** (privacy + a manual paste). 💡 **P1:** Hand off in-app without touching the clipboard.
- 💡 **P2: Page range selection**, OCR fallback for scans, and table-aware extraction.
- 💡 **P2: Built-in PDF reader** so the user can read and highlight without leaving the app.

### B19. Scanner (Camera OCR)

**Route:** `scanner/{quizId}`. Photograph a page → OCR → questions.

Camera view (viewfinder + big capture button, "Processing…") → Review view ("Review X recognized questions," per-question edit/delete, Discard, Import All) → Error view (message + Try Again).

**Frictions & ideas**
- 😀 Turning a printed question bank into a digital quiz by photo is a strong feature.
- ⚠️ **Single-shot only** — no multi-page capture, no gallery import, no crop/rotate. 💡 **P1:** Multi-capture queue, import-from-gallery, and a crop/deskew step before OCR.
- ⚠️ **OCR accuracy varies** and there's no confidence cue. 💡 **P2:** Highlight low-confidence text for quick correction; support handwriting where possible.
- 💡 **P2: Language selection** for OCR (Arabic!), and "scan answer key separately then merge."
- 💡 **P3: Live edge-detection overlay** to guide framing.

### B20. Global Search

**Route:** `global_search`. Search everything by text (2+ chars) across books, quizzes, questions, notes, flashcards, blueprints, prompts, mistakes, assets → tap a result to jump to it.

**Frictions & ideas**
- 🩹 **Leaks across workspaces** — returns results from workspaces you're not in, breaking isolation. 💡 **P1:** Scope to the active workspace (with an explicit "search all workspaces" opt-in).
- ⚠️ **No filters or highlighting** — results are a flat list; the matched text isn't emphasized. 💡 **P1:** Type-filter chips (Questions/Notes/Flashcards/…) and snippet highlighting of the matched term.
- ⚠️ **Slows on large libraries** (whole-text scan). 💡 **P2:** Faster indexed search as libraries grow.
- 💡 **P2: Recent searches, saved searches, and "search within this book."**
- 💡 **P3: Search operators** ("marked:true category:cardio"), and voice search.

### B21. Review Dashboard

**Route:** `review_dashboard`. Unified "due today" queue.

Summary cards: Flashcards due · Blueprints due · Mistakes due · Mistakes scheduled · Marked/weak. Queue items: type + title + subtitle; tap to open, "Reviewed" button, "Snooze 1 week."

**Frictions & ideas**
- ⚠️ **Promises 7 queue types, builds 5** — "unfinished slides" and "annotations" never appear as review items even though they're counted. 💡 **P1:** Add those two queues.
- ⚠️ **Snooze is fixed at 1 week.** 💡 **P1:** Snooze options (tomorrow / 3 days / 1 week / custom).
- 💡 **P1: A single "Start review" button** that runs a mixed due-items session end-to-end, rather than tapping items one by one.
- 💡 **P2: Calendar/forecast** ("42 cards due tomorrow, 15 Thursday") so users can plan.
- 💡 **P2: Daily review reminder notification** (needs the notification system finished) and a review streak.

### B22. Data Tools

**Route:** `data_tools`. Advanced backup/restore.

Export full library (ZIP of JSON dumps) · Save export file · Choose ZIP & preview (parse without importing; shows conflicts/warnings) · local ZIP path field · Preview pasted path · export warnings.

**Frictions & ideas**
- ⚠️ **Media not always bundled** in the plain export — a "backup" that omits images isn't a full backup. 💡 **P0/P1:** Always bundle media, or state plainly what's included/excluded.
- ⚠️ **The local-path field is a developer affordance** exposed to users. 💡 **P2:** Hide behind a "developer options" toggle.
- 💡 **P1: Scheduled auto-backup** to device storage / cloud (Drive) with a "last backed up" timestamp.
- 💡 **P2: Selective export/import** (pick specific books) and a clear merge-strategy explanation in plain language.
- 💡 **P2: Cross-device restore** guidance and a versioned backup history.

### B23. Settings

**Route:** `settings`.

**Library & Backup:** Export full library · Advanced import/export preview (→ Data Tools) · Global search · Review dashboard.
**Appearance:** 7 themes (Dawn/Forest/Midnight/Lavender/Plain Light/Plain Dark/System) · Reset · Font Scale (0.5×–2.0×) · UI Density (0.5×–1.5×) · Show cover images · Language (EN/AR, RTL).
**Global Configuration:** Show Welcome · Auto-hide knowledge summary · Focus Mode (DND, with Grant-permission) · Skip Unanswered · Double-tap to submit · Rapid-mode advance delay (500–5000ms).
**Danger Zone:** Clear Categories (confirm) · Reset Database (confirm; wipes + re-seeds, irreversible).

**Frictions & ideas**
- 😀 Deep personalization (7 themes, font/density, study defaults) is a strength.
- ⚠️ **Language didn't apply until restart.** ✅ *reportedly fixed.* ⚠️ **Focus-mode toggle couldn't latch on first permission grant.** 💡 **P1:** Re-check permission when the user returns and flip the toggle on.
- ⚠️ **Reset Database and Export give no feedback** (dialog closes; success/failure invisible). 💡 **P1:** Progress + result for both; a "type RESET to confirm" guard on the wipe.
- 🩹 **AI API keys are stored unencrypted** and can travel over plain HTTP. 💡 **P1 (privacy):** Encrypt secrets at rest; warn when a provider URL isn't secure.
- 💡 **P1: App lock** (PIN/biometric) — study apps hold personal notes and mistakes.
- 💡 **P1: Search within Settings**, and group density-heavy sections better.
- 💡 **P2: Per-workspace settings** surfaced here; backup/restore of *settings*; a "reset just this section" per group.
- 💡 **P2: Notification preferences** (study reminders, review-due, streak nudges) once notifications are wired.
- 💡 **P3: Accent-color picker**, true-black OLED theme, and font-family choice.

### B24. Dialogs & Overlays Reference

- **Edit Entity Dialog** — create/edit Books & Quizzes (title, description, cover image picker).
- **Edit Question Dialog** — full editor (stem, options with add/remove, correct checkboxes, explanation, hint, reference, info, weight). Used in Scanner review, Questions Browser, Category screen.
- **Question Assets Dialog** — per-question asset management: attachments/links, annotations (highlight + colored notes), source-document links, linked blueprints, "create blueprint from question."
- **Import Review Dialog** (ZIP) — stats to create/update, merge strategy (Merge Only vs Merge & Update), HTTP-image permission checkbox, errors/warnings, detail lists, Confirm.
- **Import Warnings Dialog** — post-import warning list with line/row detail.
- **Zoomable Image Dialog** — fullscreen pinch-zoom + pan for any image.
- **Sort Dialog** — Name A–Z/Z–A, Date Created/Edited, Progress.
- **Trash Bin Dialog** — soft-deleted items across 6 types; restore or permanently delete; auto-purge after retention.
- **Workspace Manager Dialog** — create/switch/rename/delete workspaces; each is isolated; default can't be deleted.

**Frictions & ideas**
- ⚠️ **Question-asset attach is a text field, not a picker**; pasted content URIs can break after restart. 💡 **P1:** Real file/image picker that copies into the app.
- ⚠️ **Trash retention is invisible** — the user doesn't know how long they have to restore. 💡 **P2:** Show "deleted 3 days ago · auto-purges in 27 days"; add "Restore all" / "Empty trash" with counts.
- 🩹 **"Empty Trash" can report success while doing nothing** in some states. 💡 **P1:** Verify the purge actually ran before confirming.
- ⚠️ **Workspace switching is buried in a dialog** with no active-workspace indicator elsewhere. 💡 **P1:** Surface the active workspace and a quick switcher on the hub; add per-workspace color/icon; confirm switches.
- 💡 **P2:** Move a book between workspaces; export/import a whole workspace; workspace-level stats.

---

## Part C — Cross-Cutting Experience Themes

These patterns repeat across many screens; fixing them once lifts the whole app.

### C1. Feedback & confirmation (the biggest single theme)
Across the Knowledge Bank, actions succeed silently — create, delete, save AI output, mark reviewed, attach an asset. The user is left guessing whether it worked.
- 💡 **P1:** A consistent snackbar system app-wide: every create/update/delete confirms, and every destructive action offers **Undo**.
- 💡 **P1:** Loading states that are honest — determinate progress for imports/exports/generation, and a Cancel where the work is long.
- 💡 **P2:** Optimistic UI (show the result immediately, reconcile in the background) so the app feels instant.

### C2. Trust & persistence (never lose my work)
The recurring anxiety is "did it save, and will it be there when I come back?"
- 💡 **P1:** Continuous autosave everywhere, with a visible "saved" cue; restore *exactly* where the user left off (quiz question, card, slide, scroll position, half-typed answer, in-progress AI output).
- 💡 **P1:** A "resume" prompt for any interrupted study session.
- 💡 **P1:** Undo for imports and bulk operations.

### C3. Empty states & guidance
New books, empty decks, no-results search — these are teaching moments, not dead ends.
- 💡 **P2:** Every empty state gets a friendly illustration + a primary action ("No flashcards yet — Generate from your marked questions" / "Add your first card" / "Paste text to import").
- 💡 **P2:** Contextual tips the first time a user reaches a complex screen (Compiler, Prompt Deck, Review Dashboard).

### C4. Errors that explain and recover
Today several failures are opaque (spinners, generic messages, failures disguised as content).
- 💡 **P1:** Human error messages with a cause and a next step ("This PDF is password-protected — remove the password and re-add it").
- 💡 **P1:** Never present a failure as if it were data (e.g., extraction errors shouldn't flow into a note as text).

### C5. Navigation & orientation
- 💡 **P1:** Always show where you are (workspace + book breadcrumb) and make back-stack behavior predictable.
- 💡 **P2:** A global bottom navigation or a persistent "Study / Library / Review / Search" quick-switch so the core loops are always one tap away.
- 💡 **P2:** Deep links / app shortcuts (long-press the app icon → "Resume last quiz," "Today's review," "Scan a page").

### C6. Accessibility
- 💡 **P1:** Full screen-reader labels, large-tap-target mode, and respecting system font scaling (the app already has a font-scale slider — make sure it doesn't fight TalkBack).
- 💡 **P1:** Color-blind-safe correct/incorrect indicators (add icons/shapes, not color alone).
- 💡 **P2:** Reduce-motion setting; high-contrast theme; TTS everywhere (questions, explanations, cards), not just the note reader.
- 💡 **P2:** Voice control for hands-free flashcard review ("flip," "good," "again").

### C7. Localization & RTL
- 😀 Bilingual EN/AR with RTL is already a differentiator.
- 💡 **P2:** Per-content language (a card can be Arabic in an otherwise-English deck), full RTL polish on every custom component, and more languages.
- 💡 **P2:** Localized number/date formatting and localized TTS voices.

### C8. Performance perception
- 💡 **P1:** Big screens (Quiz Player, question lists) should feel instant — no jank when selecting an option or scrolling long lists.
- 💡 **P2:** Skeleton loaders instead of blank spinners; pre-load the next question/card.

### C9. Privacy & trust (user-facing edges of security)
- 💡 **P1:** Encrypt stored AI API keys; never send them over insecure connections without warning.
- 💡 **P1:** App lock (PIN/biometric) for private study material.
- 💡 **P2:** Clear, plain-language control over remote-image downloads and any network use; an "everything stays on your device" reassurance where true.
- 💡 **P2:** Keep extracted text off the system clipboard unless the user explicitly copies.

---

## Part D — Exhaustive Improvement Catalog

> Every addon, feature, function, improvement, interactable, choice, option, and utility worth considering, grouped by theme. Not all are equal — priorities are tagged — but the goal here is completeness.

### D1. Studying & mastery (core loop)
- **Confidence-rated answers** feeding adaptivity (P1) · **Learn vs Test mode** (reveal-first) (P2) · **Practice/non-scored mode** to remove anxiety (P2) · **Mixed/cumulative "mock exam" generator** (P1) · **Timed exam simulation** with a real exam clock and a mark-for-review palette (P2) · **Negative marking option** for exam realism (P3) · **Question weighting by difficulty** the user can tune (P2) · **"Retry only wrong"** after any session (P1) · **Cram mode** (rapid-fire highest-yield) (P2) · **Interleaving** across books/categories (P2) · **Daily goal + study streak** with a calendar heatmap (P1) · **XP/levels/badges** gamification, toggleable (P3) · **Focus timer / Pomodoro** integrated with Focus Mode (P2).

### D2. Spaced repetition & review
- **Visible "due today" per deck/book** (P1) · **Snooze options** beyond 1 week (P1) · **One-tap "Start review"** mixed session (P1) · **Forecast calendar** of upcoming reviews (P2) · **Tunable SR algorithm** (intervals, ease) or presets (Aggressive/Standard/Relaxed) (P2) · **Leech handling** (P2) · **Review reminders** notification + streak protection ("study to keep your 12-day streak") (P1, needs notifications) · **Mistake review workflow** — resolve, add a "why I missed it" note, schedule a recheck (P2).

### D3. Content creation & editing
- **Rich text / Markdown / LaTeX / code blocks** in stems, options, explanations, notes (P1 for STEM/med) · **Image occlusion** and **cloze deletion** cards (P1) · **Inline image picker** everywhere an image is referenced (P1) · **Audio on cards/questions** (P2) · **Reorder** questions/cards/slides by drag (P2) · **Bulk edit** (category/tag/weight across selection) (P1) · **Duplicate detection** & merge (P2) · **Templates** for notes/prompts/decks (P1) · **Find & replace** across a quiz/book (P2) · **Version history / undo** on edits (P2) · **Question banks / tags** beyond categories (P2).

### D4. Import / export / sync
- **Undo last import** (P1) · **Saved mapping presets** (P2) · **Batch multi-file import** (P2) · **Dry-run validate** (P2) · **Import from URL / Google Sheets** (P3) · **Anki (.apkg) import/export** — massive for adoption (P1) · **Quizlet / CSV round-trip** (P2) · **Scheduled auto-backup** to Drive/local with timestamps (P1) · **Cloud sync across devices** (P1, if it fits the product) · **Selective export** (pick books) (P2) · **Full media in every backup** (P1) · **Export a summary/report as PDF** (P2) · **Share a deck/book** with another user via a bundle or link (P2).

### D5. AI features
- **In-app run** of prompts/generation (no clipboard round-trip) with streaming (P1) · **"Explain this question"** one-tap (P1) · **"Make easier/harder variant"** and **distractor generation** (P2) · **Auto-generate flashcards/notes/slides from a quiz** as first-class Magic Actions (P1) · **AI grader** for free-text/typed answers (P2) · **Summarize a book/source into a cram sheet** (P2) · **Mnemonic generator** (P3) · **MCQ quality validation** so AI questions are trustworthy (P1) · **Provider setup wizard + test connection** (P1) · **Multiple providers** with easy switching (P2) · **On-device model option** for privacy (P3).

### D6. Organization & navigation
- **Active-workspace indicator + quick switch** (P1) · **Folders/collections for books** (P3) · **Global "Today" launchpad** (P1) · **Bottom nav / persistent quick-switch** (P2) · **App shortcuts & deep links** (P2) · **Favorites/pinned** across types (P2) · **Recently studied** (true study, not edits) (P1) · **Move items between workspaces** (P2) · **Per-workspace color/icon** (P2).

### D7. Analytics & motivation
- **Book/quiz mastery over time** with trend charts (P1) · **Per-category strengths/weaknesses** dashboard (P1) · **Study-time tracking** (per day/book/session) (P2) · **Personal bests & milestones** with tasteful celebration (P2) · **Weekly report** ("you studied 4.2h, mastered 38 cards, weakest: Pharmacology") delivered in-app or via notification (P2) · **Exam countdown** with a suggested daily plan to be ready in time (P2) · **Heatmap calendar** of activity (P2).

### D8. Accessibility & inclusivity
- **Full screen-reader support** (P1) · **Color-blind-safe indicators** (P1) · **TTS everywhere** (P2) · **Voice answering / voice control** (P2) · **Large-text & high-contrast & reduce-motion** modes (P1) · **Dyslexia-friendly font option** (P3) · **One-handed / reachability** layout on large phones (P2).

### D9. Personalization & delight
- **Accent-color picker, true-black OLED theme, font-family choice** (P2/P3) · **Custom card/quiz layouts** (P3) · **Widgets** (home-screen "cards due today," "resume quiz") (P2) · **Wear OS / quick-review companion** (P3) · **Celebration animations & sound (toggleable)** (P3) · **Study soundscapes / focus music** (P3) · **Personal study mascot / streak buddy** (P3, optional).

### D10. Reliability & confidence (mostly invisible, deeply felt)
- **Autosave + faithful resume everywhere** (P1) · **Undo for destructive/bulk actions** (P1) · **Honest, specific error messages** (P1) · **Consistent success confirmations** (P1) · **App lock (PIN/biometric)** (P1) · **Encrypted secrets** (P1) · **"Nothing is lost" guarantees** — crash recovery that restores in-progress work (P2) · **A visible "last backed up" reassurance** (P1).

### D11. Utilities & small power-tools
- **In-app PDF viewer & highlighter** (P2) · **Multi-page scanner with crop/deskew & gallery import** (P1) · **Quick-add capture** (a floating "jot a question/card" from anywhere) (P3) · **Clipboard-aware paste** ("we detected quiz text — import it?") (P3) · **Print a quiz / answer key** (P3) · **QR/deep-link to open a specific book/quiz** (P3) · **Search operators & saved searches** (P2) · **Bulk category/tag manager** (P2) · **Duplicate-and-tweak** for any entity (P2).

---

## Part E — Quick Wins & a Delight Roadmap

**Now (already-in-flight or tiny, high-relief fixes):**
- Confirm the recent fixes actually land for the user: import save-summary, flashcard resume, adaptive session persistence, unified summary, quiz-edit dialog, immediate language switch, PDF error message, cancel-generation.
- Add snackbar confirmations + Undo across the Knowledge Bank (C1).
- Show the active workspace + a quick switcher; scope Global Search to the current workspace.
- Add the two missing Review-Dashboard queues (slides, annotations) and flexible Snooze.
- Replace the question-asset text field with a real picker.

**Next (the trust & momentum sprint):**
- Faithful resume + autosave everywhere (slideshows, note reader position, in-progress AI output, half-typed answers).
- A home "Today" launchpad (due counts + resume) and study streak.
- One-tap "Start review" mixed session; deck "due today" badges.
- Actionable Summary ("make cards from what I missed," "retry wrong only") and progress-over-time.
- Encrypt API keys + app lock (privacy).

**Then (the differentiators):**
- Anki import/export and cloud/scheduled backup.
- In-app AI (streaming generation + "explain this question") with quality validation.
- Rich text / LaTeX / image-occlusion / cloze content.
- Analytics dashboard (mastery trends, weak-area coaching, weekly report + reminders).
- Exam simulation mode and a tunable spaced-repetition engine.

**Guiding principle:** MKS already *has* remarkable breadth. The fastest path to a beloved app isn't more features — it's making the existing ones **trustworthy** (never lose my work, always tell me it saved), **legible** (I know where I am and what's due), and **finishing the promises** it already makes (resume, isolation, the full review queue). Delight compounds from there.

---

*This document is the single source of truth for the MKS user experience. It absorbs and replaces the two earlier journey maps. Keep it updated as screens change; pair it with the developer/technical review for engineering detail.*
