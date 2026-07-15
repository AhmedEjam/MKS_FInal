# UX Review 2026

This document contains historical UX frictions, ideas, trust breakers, and improvement catalogs extracted from the original MKS_USER_EXPERIENCE_REVIEW.md.

## Part A — The Experience at a Glance

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

### B2. Library (Main Hub) - Frictions & Ideas

- ⚠️ **Sort options can silently do nothing** — sorting categories by "Last studied" or books by certain metrics is a no-op; the user picks a sort and sees no change. 💡 **P1:** Hide sorts that don't apply to the current view, or make them work.
- ⚠️ **"Recent"/"Resume" reflect edits, not study** — renaming a quiz floats it to the top of "recent," and Resume can surface a quiz you never studied. 💡 **P1:** Track *last studied* separately from *last edited*; base Recent/Resume on study.
- ⚠️ **No workspace indicator on the hub** — you can't tell which workspace you're in at a glance, and switching is buried. 💡 **P1:** Show the active workspace name in the top bar with a one-tap switcher.
- 💡 **P1:** A **home "Today" strip** above the books: "12 flashcards due · 3 mistakes to review · resume 'Cardio quiz' (18/40)." Turns the hub into a launchpad, not just a file list.
- 💡 **P2:** **Continue studying** card that deep-links to the exact question/card you left on.
- 💡 **P2:** **Multi-select on the hub** (bulk pin/export/delete/move-to-workspace).
- 💡 **P2:** **Book cover art auto-suggestions**, book color-coding, and a **favorites/pinned row**.
- 💡 **P3:** **Folders/collections** for books (many med students have 20+ books).
- 💡 **P3:** Drag-to-reorder books; swipe actions on list rows (swipe to pin/delete).

### B3. Import / Compiler flow - Frictions & Ideas

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

### B4. Quiz Player - Frictions & Ideas

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

### B5. Summary (Post-Quiz) - Frictions & Ideas

- ⚠️ **The screen could contradict itself** — a card under the "Correct" filter could show WRONG; category stats used different logic than the cards. ✅ *reportedly unified.* 💡 **P1:** Finish the job — make category performance use the same correct/incorrect classification as the cards and export.
- ⚠️ **"Avg time/question" can be misleading** (mixes lifetime question time and placeholders). 💡 **P2:** Compute strictly from this session.
- ⚠️ **"Clear Marks" is hidden behind a bookmark icon** and is quiz-wide/destructive. 💡 **P1:** Give destructive actions explicit labels and clear confirmation copy.
- 💡 **P1: Actionable summary** — one-tap "Make flashcards from the ones I missed," "Draft a note from my mistakes," "Retry only the wrong ones."
- 💡 **P1: Progress over time** — "you scored 72%, up from 65% last attempt on this quiz," with a small trend sparkline.
- 💡 **P2: Time-per-question breakdown**, slowest/fastest questions, and a "questions you flip-flopped on" list.
- 💡 **P2: Export the summary as PDF/image** for sharing or a study log, not just plain text.
- 💡 **P3: Celebratory moment** for personal bests (streak record, first 100%) — a small, tasteful animation.

### B6. Session Management - Frictions & Ideas

- ⚠️ **Progress % can be wrong** — computed against the quiz's *current* size, so after you add/remove questions (or for a custom subset session) a finished session can read "10%." 💡 **P1:** Compute against the session's own question set; add a "quiz changed since this session" badge.
- ⚠️ **No staleness cue** — resuming silently remaps onto a changed quiz. 💡 **P1:** Warn when the underlying quiz changed.
- ⚠️ **The range fields can lock up** if the dialog opens before the question count loads (range stuck at 0, Start disabled). 💡 **P2:** Default the range once the count arrives; validate gracefully.
- 💡 **P2: Session templates** — save named presets ("Timed 50 random," "Missed-only review") for one-tap start.
- 💡 **P2: Duplicate a past session's settings**; rename sessions after the fact.
- 💡 **P3: Auto-name sessions** meaningfully ("Ch.3, 40 Q, timed") instead of blank/generic.

### B7. Quiz Questions Browser - Frictions & Ideas

- 🩹 **Move/Copy strands attachments** — moving/copying questions doesn't carry their assets/annotations, and moved questions' assets point at the wrong book. 💡 **P1:** Carry attachments with the question; make move/copy atomic; confirm the result.
- ⚠️ **The editor edits a snapshot** — if the list refreshes while the dialog is open, saving can overwrite newer changes. 💡 **P2:** Re-read on save or lock the row.
- ⚠️ **No inline feedback** on delete/move/copy. 💡 **P1:** Snackbar with Undo for destructive bulk actions.
- 💡 **P1: Bulk edit** — set weight, add/remove a category, or add a tag across selected questions.
- 💡 **P2: Duplicate-question detection** within a quiz; find-and-replace across stems.
- 💡 **P2: Reorder questions** (drag) and set a canonical order used by non-shuffled sessions.
- 💡 **P3: Rich-text / LaTeX / code formatting** in stems and explanations (huge for STEM/med).

### B9. Adaptive Training - Frictions & Ideas

- 🩹 **Historically lost all progress on app kill** (no saved session). ✅ *reportedly fixed.* 💡 **P1:** Also let the user **resume** an interrupted adaptive run rather than always starting fresh, and label these sessions clearly in the session list so they don't accumulate as mystery entries.
- ⚠️ **The user can't see *why* a question was chosen.** 💡 **P2:** A tiny "chosen because: you missed this twice" tag builds trust in the algorithm.
- 💡 **P1: Tunable adaptivity** — a simple slider (Review my weak spots ⇄ Cover everything evenly) and a "focus this session on [category]" option.
- 💡 **P2: Daily adaptive goal** ("20 adaptive questions/day") with a streak.

### B10. Book Knowledge Dashboard - Frictions & Ideas

- ⚠️ **The Study Progress card only reflects quiz answering** — studying flashcards/slides/notes doesn't move it, so the book feels "stuck" even after hours of review. 💡 **P1:** Fold all study activity into the book's progress and "last studied."
- ⚠️ **No feedback when Magic Actions run** — a deck/note is created silently. 💡 **P1:** Snackbar "Created 'Marked Cards' deck — Open," and route straight into the new asset.
- ⚠️ **Auto-generated titles are timestamp fragments** ("Marked Cards - 4821"). 💡 **P2:** Human titles ("Marked cards from Cardiology — Jul 12").
- 💡 **P1: A real book overview** — mastery %, cards due today, next exam countdown (if set), a 7-day study heatmap, weakest categories.
- 💡 **P2: More Magic Actions** — "Make a mock exam (50 mixed)," "Summarize this book into one blueprint," "Generate a cram sheet."
- 💡 **P2: Per-book study goal & reminder** ("study Pharmacology 30 min/day").

### B11. Flashcard Deck Screen - Frictions & Ideas

- 🩹 **Resume didn't remember your place** — you'd restart at card 1. ✅ *reportedly fixed for flashcards.* 💡 **P1:** Confirm the resume prompt ("Resume where you left off?") is offered and the flip state is remembered too.
- ⚠️ **No deck-complete recap** — reaching the last card just stops; the tracked accuracy isn't shown. 💡 **P1:** A "deck complete" screen with accuracy, time, and "review the ones you got wrong."
- ⚠️ **No "due today" surfacing** — spaced repetition runs under the hood but the user can't see which cards are due per deck. 💡 **P1:** "N due" badge per deck and a "Study due" button.
- ⚠️ **No feedback that a rating registered.** 💡 **P2:** Subtle confirmation on each rating.
- 💡 **P1: Card text import / paste** is supported — surface it prominently; add **image occlusion** cards (huge for anatomy) and **cloze deletion** ({{c1::...}}).
- 💡 **P1: Undo last rating** (mis-tapped Again vs Easy).
- 💡 **P2: Audio on cards** (pronunciation/listening), **two-sided study** (front→back and back→front), **type-the-answer** cards.
- 💡 **P2: Session length control** ("study 20 cards," "study 10 min").
- 💡 **P3: Leech detection** ("this card keeps failing — rewrite or suspend it?").

### B12. Slideshow Course Screens - Frictions & Ideas

- 🩹 **Resume not restored** and **completion stats go stale** — marking a slide studied updates the slide but the "X completed / Y%" card doesn't move until you leave and return. 💡 **P1:** Apply the same resume treatment as flashcards; update the course progress live.
- 💡 **P1: Autoplay / presentation mode** with a timer per slide and optional TTS narration (the note reader already has TTS — reuse it).
- 💡 **P2: Slide images, PPTX import** (supported — surface it), speaker notes view, and a "quiz me on these slides" action.
- 💡 **P3: Landscape full-screen present mode** with tap zones for next/prev.

### B13. Note Blueprints / Immersive Reader - Frictions & Ideas

- 😀 The immersive reader with autoscroll + TTS is a genuine differentiator.
- 🩹 **Reader position and TTS position aren't saved** — leave and you lose your place. 💡 **P1:** Persist scroll/read position per note.
- ⚠️ **TTS has no pause/resume and can keep talking after you leave**; the play/stop button can get out of sync when speech finishes. 💡 **P1:** Proper play/pause/resume, stop on background, word-follow highlighting, and a lock-screen/media control.
- ⚠️ **TTS reads the saved text while you're editing a different draft.** 💡 **P2:** Read what's on screen.
- ⚠️ **No save confirmation.** 💡 **P1:** Snackbar on save / mark-reviewed.
- 💡 **P1: Markdown rendering** (headings/lists/bold) in reader mode, not raw text.
- 💡 **P2: Highlight + annotate** within the reader; **font/line-height/serif** reading options; **dark sepia reading theme**.
- 💡 **P2: "Read this to me" from anywhere** (queue multiple notes for a hands-free commute session).
- 💡 **P3: Estimated reading time**, progress bar through the note, and a bionic-reading toggle.

### B14. AI Prompt Deck Screens - Frictions & Ideas

- 😀 Output routing (prompt → any content type) is powerful and unusual.
- 🩹 **In-progress AI output is lost on rotation/backgrounding** (the field isn't preserved). 💡 **P1:** Preserve the output field across rotation; auto-save drafts.
- ⚠️ **No feedback** on save/route; **variable detection** can misfire on ordinary parentheses in prose. 💡 **P2:** Snackbars; tighten variable syntax or let the user confirm detected variables.
- ⚠️ **Clipboard-based AI loop is manual** — copy the prompt, paste into ChatGPT, paste the answer back. 💡 **P1:** If a provider (Ollama/OpenAI-compatible) is configured, **run in-app** and stream the answer directly (the app already has the plumbing).
- 💡 **P1: Prompt template library** (Quiz generator, Flashcard maker, Explain-like-I'm-5, Mnemonic maker, Clinical-vignette) shipped ready to use.
- 💡 **P2: Per-card model/temperature settings**; re-run with tweaks; compare two outputs side by side.

### B17. AI MCQ Generator - Frictions & Ideas

- 🩹 **Save happens before review** — questions are persisted before you see the preview, so you can't edit/delete *before* they're committed. 💡 **P1:** Preview first, edit freely, then commit on an explicit Save.
- 🩹 **Cancel used to brick progress** for the rest of the session. ✅ *reportedly fixed.*
- ⚠️ **Offline/timeout is opaque** — a failed generation looks the same as "no questions found," and there's no "you appear offline." 💡 **P1:** Distinct offline/timeout/empty states; a connectivity hint.
- ⚠️ **Generated MCQs aren't quality-checked** — a hallucinated question with a wrong answer key can be saved as-is. 💡 **P1:** Validate structure (4 options, one clearly-correct key), flag low-confidence items, and let the user approve each.
- 💡 **P1: Streaming generation** so the user sees questions appear live, with a running count and a Stop button.
- 💡 **P2: "Explain this question" and "make a harder/easier variant"** one-tap actions; generate distractors for an existing question.
- 💡 **P2: Provider setup wizard** with a "test connection" and friendly guidance for local (Ollama) vs cloud.

### B18. PDF Extraction - Frictions & Ideas

- 🩹 **Encrypted/corrupt PDFs spun forever** with no message. ✅ *reportedly fixed* (now shows an error). 💡 **P1:** Confirm the message is specific ("this PDF is password-protected").
- ⚠️ **Image-only (scanned) PDFs return empty text** with no nudge. 💡 **P1:** Detect no-selectable-text and suggest "Use Vision/OCR instead."
- 🩹 **"Saved to notes" can appear even when nothing saved** (source with no book). 💡 **P1:** Only confirm on a real save; otherwise tell the user why it couldn't.
- 🩹 **Sending text to the MCQ generator uses the system clipboard** (privacy + a manual paste). 💡 **P1:** Hand off in-app without touching the clipboard.
- 💡 **P2: Page range selection**, OCR fallback for scans, and table-aware extraction.
- 💡 **P2: Built-in PDF reader** so the user can read and highlight without leaving the app.

### B19. Scanner (Camera OCR) - Frictions & Ideas

- 😀 Turning a printed question bank into a digital quiz by photo is a strong feature.
- ⚠️ **Single-shot only** — no multi-page capture, no gallery import, no crop/rotate. 💡 **P1:** Multi-capture queue, import-from-gallery, and a crop/deskew step before OCR.
- ⚠️ **OCR accuracy varies** and there's no confidence cue. 💡 **P2:** Highlight low-confidence text for quick correction; support handwriting where possible.
- 💡 **P2: Language selection** for OCR (Arabic!), and "scan answer key separately then merge."
- 💡 **P3: Live edge-detection overlay** to guide framing.

### B20. Global Search - Frictions & Ideas

- 🩹 **Leaks across workspaces** — returns results from workspaces you're not in, breaking isolation. 💡 **P1:** Scope to the active workspace (with an explicit "search all workspaces" opt-in).
- ⚠️ **No filters or highlighting** — results are a flat list; the matched text isn't emphasized. 💡 **P1:** Type-filter chips (Questions/Notes/Flashcards/…) and snippet highlighting of the matched term.
- ⚠️ **Slows on large libraries** (whole-text scan). 💡 **P2:** Faster indexed search as libraries grow.
- 💡 **P2: Recent searches, saved searches, and "search within this book."**
- 💡 **P3: Search operators** ("marked:true category:cardio"), and voice search.

### B21. Review Dashboard - Frictions & Ideas

- ⚠️ **Promises 7 queue types, builds 5** — "unfinished slides" and "annotations" never appear as review items even though they're counted. 💡 **P1:** Add those two queues.
- ⚠️ **Snooze is fixed at 1 week.** 💡 **P1:** Snooze options (tomorrow / 3 days / 1 week / custom).
- 💡 **P1: A single "Start review" button** that runs a mixed due-items session end-to-end, rather than tapping items one by one.
- 💡 **P2: Calendar/forecast** ("42 cards due tomorrow, 15 Thursday") so users can plan.
- 💡 **P2: Daily review reminder notification** (needs the notification system finished) and a review streak.

### B22. Data Tools - Frictions & Ideas

- ⚠️ **Media not always bundled** in the plain export — a "backup" that omits images isn't a full backup. 💡 **P0/P1:** Always bundle media, or state plainly what's included/excluded.
- ⚠️ **The local-path field is a developer affordance** exposed to users. 💡 **P2:** Hide behind a "developer options" toggle.
- 💡 **P1: Scheduled auto-backup** to device storage / cloud (Drive) with a "last backed up" timestamp.
- 💡 **P2: Selective export/import** (pick specific books) and a clear merge-strategy explanation in plain language.
- 💡 **P2: Cross-device restore** guidance and a versioned backup history.

### B23. Settings - Frictions & Ideas

- 😀 Deep personalization (7 themes, font/density, study defaults) is a strength.
- ⚠️ **Language didn't apply until restart.** ✅ *reportedly fixed.* ⚠️ **Focus-mode toggle couldn't latch on first permission grant.** 💡 **P1:** Re-check permission when the user returns and flip the toggle on.
- ⚠️ **Reset Database and Export give no feedback** (dialog closes; success/failure invisible). 💡 **P1:** Progress + result for both; a "type RESET to confirm" guard on the wipe.
- 🩹 **AI API keys are stored unencrypted** and can travel over plain HTTP. 💡 **P1 (privacy):** Encrypt secrets at rest; warn when a provider URL isn't secure.
- 💡 **P1: App lock** (PIN/biometric) — study apps hold personal notes and mistakes.
- 💡 **P1: Search within Settings**, and group density-heavy sections better.
- 💡 **P2: Per-workspace settings** surfaced here; backup/restore of *settings*; a "reset just this section" per group.
- 💡 **P2: Notification preferences** (study reminders, review-due, streak nudges) once notifications are wired.
- 💡 **P3: Accent-color picker**, true-black OLED theme, and font-family choice.

### B24. Dialogs & Overlays Reference - Frictions & Ideas

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
