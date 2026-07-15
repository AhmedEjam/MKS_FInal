# MKS User Guide

> **MKS — Final User Experience Review & Journey Map**
>
> **Lens:** Written entirely from the **end user's** point of view — a student, resident, or self-learner. No code, no file paths, no architecture. Security/engineering internals live in the separate developer review; only the user-facing edges of those (privacy, trust, data loss) appear here.
>
> **App in one sentence:** MKS (My Knowledge Space) is an offline-first Android study app that turns imported content (spreadsheets, documents, PDFs, photos, AI output) into interactive quizzes, flashcards, slideshows, note blueprints, and AI prompt decks — organized as Books inside isolated Workspaces, with progress tracking, spaced-repetition review, and cross-content search.

---

## Table of Contents

- A1. The full journey, as a user lives it
- A2. What delights
- A3. Top frictions, ranked by how much they hurt
- A4. Where the app quietly loses your work (trust-breakers)
- A5. Promises vs. reality
- B1 Launch & Onboarding · B2 Library Hub · B3 Import / Compiler · B4 Quiz Player · B5 Summary · B6 Session Management · B7 Quiz Questions Browser · B8 Category Questions · B9 Adaptive Training · B10 Book Knowledge Dashboard · B11 Flashcards · B12 Slideshows · B13 Note Blueprints / Reader · B14 AI Prompt Decks · B15 Source Documents · B16 Book Notes · B17 AI MCQ Generator · B18 PDF Extraction · B19 Scanner (OCR) · B20 Global Search · B21 Review Dashboard · B22 Data Tools · B23 Settings · B24 Dialogs & Overlays
- **Part C — Cross-Cutting Experience Themes**
- **Part D — Exhaustive Improvement Catalog** (every addon / feature / option / interactable / utility)
- **Part E — Quick Wins & a Delight Roadmap**

---

## Part A — The Experience at a Glance

### A1. The full journey, as a user lives it

You install MKS and land on a **Welcome** screen: pick a language, peek at features, tap **Get Started**. From then on the app always opens to the **Library** — your home base, pre-seeded with one sample book so it's never empty on day one.

You bring in your own material. Tapping the **+** button (or sharing a file from another app) opens the **Import/Compiler** flow: MKS auto-detects the format (spreadsheet, CSV, JSON, HTML, text, or a ZIP bundle), finds the header row, maps columns to fields, and shows a **live preview** of parsed questions you can toggle and correct before saving. Alternatively you photograph a page (**Scanner/OCR**), extract text from a **PDF**, paste text into a deck, or have the **AI MCQ Generator** draft questions for you.

Content is organized as **Books** → which hold **Quizzes** (of Questions) plus a **Knowledge Bank** (flashcard decks, slideshow courses, note blueprints, AI prompt decks, source documents). Everything lives inside a **Workspace** you can switch between for isolation (e.g., "Med School" vs. "Board Prep").

You study. Open a quiz → **Session Management** lets you resume a saved session or start a new one with rich options (filters, ranges, shuffles, timers, rapid/repeat modes). The **Quiz Player** is the heart of the app: one question at a time, options you tap/eliminate, a draggable control sheet, a live score/streak/accuracy readout, per-question navigation grid, and an expandable explanation after answering. Finishing takes you to the **Summary**: score, best streak, per-category performance, and a filterable review of every question, plus share/retry.

Between quizzes you review with **Flashcards** (flip + Again/Good/Easy spaced-repetition), **Slideshows** (swipe + mark studied), **Note Blueprints** (an immersive reader with autoscroll and text-to-speech), and **AI Prompt Decks** (fill variables → run → route the output into a note/blueprint/deck/quiz). The **Review Dashboard** gathers everything due today across flashcards, blueprints, and mistakes. **Global Search** finds anything by text. **Book Dashboard** shows a book's whole learning ecosystem with one-tap "Magic Actions." **Settings** controls themes, font/density, language, study defaults, focus mode, and backup. **Trash** recovers soft-deleted items; **Data Tools** does full library backup/restore.

That's the arc: **bring in → organize → study → review → track → back up.** It's a genuinely deep, capable study system. The rough edges are almost all about **feedback, resume, and confidence that your work was saved** — not missing features.

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

### B3. Import / Compiler flow

Reached from the FAB **Import**, a book/quiz's **Import Into**, or an Android **Share** from another app. Two paths: the **Compiler** (spreadsheets, CSV, JSON, HTML, text) and the **ZIP/bundle Review** dialog.

**Compiler steps the user sees:** format auto-detected → (XLSX) pick a sheet → header row auto-found → column→field mapping shown → **live preview list** of parsed questions with per-question include toggles, correct-answer highlighting, range include/exclude, long-press to mark the correct option → **Save** (title + target book/quiz/deck).

### B4. Quiz Player

**Route:** `quiz/{quizId}?sessionId={sessionId}`. The core study interface — one question at a time.

**Top bar:** ← Back (exits, saves progress) · Timer (MM:SS, counts down if a timer is set) · Progress "3/25" · Score "★ 5/25" · Streak "🔥 3" (hidden at 0) · thin progress bar.

**Question area:** stem (multi-line) · image (tap → fullscreen pinch-zoom) · Assets paperclip (read-only attachments) · horizontal swipe (next/prev, animated) · explanation card after answering (also Hint, High-Yield, Reference).

**Options:** tap to select (radio/checkbox) · double-tap to select+submit (if enabled) · long-press to eliminate (strikethrough) · elimination-mode ✕ per option. After submit: green (correct) / red (your wrong pick); Submit → Next.

**Bottom sheet (drag up):**

- *Peek:* live Accuracy % (color-coded) · Streak · Submit/Next/Reveal button.
- *Expanded toggles:* Categories · One-by-One · Rapid Mode · Eliminate · Drop Question (confirm) · Focus Mode (DND) · Mark/Bookmark · Finish Quiz.
- *Navigation:* filter chips (ALL/ANSWERED/UNANSWERED/MISSED/MARKED/DROPPED) · color-coded question-number grid (tap to jump) · "Restore dropped question" chip.

### B5. Summary (Post-Quiz)

**Route:** `summary/{sessionId}`.

**Score header:** big % · fraction ("17/20 correct") · best streak · session label · average time/question.
**Top bar:** Share (system share sheet with text export) · ✕ Close (→ Library).
**Review section:** gear → Visible-Details panel (toggle Stem/Options/Hint/High-Yield/Reference/Number/Explanation) · filter chips (ALL/CORRECT/WRONG/UNANSWERED/DROPPED/WITH EXPLANATION) · color-coded review cards.
**Category performance:** per-category accuracy breakdown.
**Bottom:** Retry (new session, same quiz) · Library.

### B6. Session Management

**Route:** `sessions/{quizId}`. All saved sessions for a quiz.

| Element | What it does |
|---|---|
| **← Back** | Previous screen. |
| **+ FAB** | Start Session Dialog. |
| **Session card** | Label, accuracy ring, score metrics, progress bar, last-active date. Tap: resume (incomplete) or view Summary (complete). Delete: confirm → removes the record. |
| **Empty state** | Tap to create a session. |

**Start Session Dialog:** label · include filters (Unanswered/Missed/Marked/Categorized/Uncategorized) · question range (From/To) · Shuffle Questions · Shuffle Options · Rapid Mode · Repeat Wrong · Quiz Timer (min) · Question Timer (sec) · Remember Settings · Start.

### B7. Quiz Questions Browser

**Route:** `quiz_questions/{quizId}`. Manage/review/organize questions (not for taking the quiz).

**Top bar:** ← Back · Search (filter by text) · Filter (toggle panel) · title + "X/Y Questions."
**Filter chips:** Stem, Options, Correct Answer, Explanation, Hint, Reference, Info, Marked Only, Has Attachments (toggle field visibility on cards).
**Question cards:** long-press → multi-select · tap → Edit Question Dialog · bookmark icon · edit (pencil) · attachments (paperclip → Question Assets Dialog) · image tap → fullscreen.
**Selection-mode bar:** Clear · Select All · Bookmark/Unbookmark selected · Move (to another quiz) · Copy (to another quiz) · Export (new quiz from selection) · Create Flashcards (from selection, option to clear marks) · Delete (confirm).
**+ FAB:** manual new question. **Summary card:** totals (questions/marked/missed/notes/assets/sources).

### B8. Category Questions

**Route:** `category/{categoryName}`. Same as the Questions Browser but scoped to a category across the book, plus a **"Start Quiz" FAB** that launches adaptive training on just that category.

- 💡 **P2:** Category management here — rename, merge, recolor, set an emoji, and see per-category mastery %.
- 💡 **P2:** "Study weakest category" shortcut surfaced when a category's accuracy is low.

### B9. Adaptive Training

**Route:** `adaptive/{type}/{id}` — not a separate screen; it's the Quiz Player fed intelligently selected questions. Types: **BOOK** (weak/unanswered/recently-missed), **CATEGORY**, **QUIZ** (a quiz's weak spots), **ALL** (everything). Priority: unanswered > recently wrong > low-weight > marked → Summary at the end.

### B10. Book Knowledge Dashboard

**Route:** `book_dashboard/{bookId}`. A book's whole learning ecosystem.

**Study Progress card:** completion bar · Due · Weak · Marked · Mistakes counts.
**Magic Actions:** "Draft Note from Marked" (if marked>0) · "Note from Mistakes" (if open mistakes>0) — plus (per the FAB context) generate flashcards/slides from questions/marked/missed.
**Learning Tools grid:** Quizzes · Flashcards · Slideshows · Notes · AI Prompts · Sources (each with a count, tapping into its list).

### B11. Flashcard Deck Screen

**Route:** `flashcards/{deckId}`. Two modes.

**Top bar:** ← Back · mode toggle (List/Study) · edit deck.
**List mode:** stats card (title/desc/count/studied/mastery + progress) · Add card · Study · "Generate from questions" (Marked / Missed, option to clear marks) · card rows (front/back preview, attempt/correct counts, source link, move up/down, edit, delete).
**Study mode:** counter "3/15" · card surface (tap to flip) · Flip button · rating buttons after flip (Again/Good/Easy) · Previous/Next.

### B12. Slideshow Course Screens

**List** (`book_slideshows/{bookId}`): + FAB (new course: title/desc) · course rows (title/desc/slide-count, tap to open, edit, delete).
**Course** (`slideshow/{courseId}`): + FAB (new slide) · slide rows (title + preview, edit, delete) · in study/present mode, swipe through slides and mark studied.

### B13. Note Blueprints / Immersive Reader

**List** (`book_blueprints/{bookId}`): + FAB → create (Manual with title/body/mode, or "From Marked" / "From Missed") · note rows (title/mode/status/preview, tap, edit, delete).
**Reader/Editor** (`blueprint/{noteId}`): body editor · Save · Mark reviewed (increments counter/status) · To flashcards · Append to question note · status info (mode/status/review count). Immersive reader adds: hide system bars, pinned title, **autoscroll (adjustable speed)**, **text-to-speech (adjustable pitch/rate)**.

### B14. AI Prompt Deck Screens

**List** (`book_prompts/{bookId}`): + FAB (new deck) · deck rows (tap/edit/delete).
**Deck** (`prompt_deck/{promptId}`): **Prompt cards** (create with `{variable}` placeholders + output type; "Use this card," edit, delete) · **Run section** (variable input fields, rendered-prompt preview, copy [records a run], AI-output paste field, Save run, and route output → Note / Blueprint / Flashcards / Quiz) · **Run history** (last 10).

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

### B18. PDF Extraction

**Route:** `pdf_extraction/{sourceId}`. Extract text from a PDF for study material.

Controls: PDF selector · page preview · **Extract text** (progress) · extracted-text viewer (page-by-page, selectable) · Copy all · Save as source · Create slides · Create blueprint.

### B19. Scanner (Camera OCR)

**Route:** `scanner/{quizId}`. Photograph a page → OCR → questions.

Camera view (viewfinder + big capture button, "Processing…") → Review view ("Review X recognized questions," per-question edit/delete, Discard, Import All) → Error view (message + Try Again).

### B20. Global Search

**Route:** `global_search`. Search everything by text (2+ chars) across books, quizzes, questions, notes, flashcards, blueprints, prompts, mistakes, assets → tap a result to jump to it.

### B21. Review Dashboard

**Route:** `review_dashboard`. Unified "due today" queue.

Summary cards: Flashcards due · Blueprints due · Mistakes due · Mistakes scheduled · Marked/weak. Queue items: type + title + subtitle; tap to open, "Reviewed" button, "Snooze 1 week."

### B22. Data Tools

**Route:** `data_tools`. Advanced backup/restore.

Export full library (ZIP of JSON dumps) · Save export file · Choose ZIP & preview (parse without importing; shows conflicts/warnings) · local ZIP path field · Preview pasted path · export warnings.

### B23. Settings

**Route:** `settings`.

**Library & Backup:** Export full library · Advanced import/export preview (→ Data Tools) · Global search · Review dashboard.
**Appearance:** 7 themes (Dawn/Forest/Midnight/Lavender/Plain Light/Plain Dark/System) · Reset · Font Scale (0.5×–2.0×) · UI Density (0.5×–1.5×) · Show cover images · Language (EN/AR, RTL).
**Global Configuration:** Show Welcome · Auto-hide knowledge summary · Focus Mode (DND, with Grant-permission) · Skip Unanswered · Double-tap to submit · Rapid-mode advance delay (500–5000ms).
**Danger Zone:** Clear Categories (confirm) · Reset Database (confirm; wipes + re-seeds, irreversible).

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
