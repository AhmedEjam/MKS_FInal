# MKS — Midnight Premium Design System & Redesign Handoff

> **Read this first.** This is the single source of truth for the MKS UI/UX redesign. It documents
> (a) the target visual system, (b) exactly what has been built so far in Stitch and in code,
> (c) what remains, and (d) how any AI agent should continue the work — safely, without breaking
> the app. If you are an agent picking this up cold, read §0 → §2 → §11 before touching anything.
>
> Last updated: 2026-07-12.

---

## 0. TL;DR — orientation for a new agent

- **What:** Full visual redesign of MKS (native Android quiz + knowledge-bank app; Kotlin + Jetpack
  Compose + Material 3). Target look = **"Midnight Premium"**: a premium, nocturnal, low-glare deep-navy
  study app with soft lavender + warm orange accents. Think Linear / Arc / Things, for studying.
- **Two tracks already exist:**
  1. **Stitch mockups** — 15 finished screen designs in a Stitch project (§3). These are *design blueprints only*, not app code.
  2. **Code port** — the redesign is being ported into the real Compose app, theme-first. §2 lists exactly what's done.
- **The redesign lives in the existing `MIDNIGHT` theme.** MKS has 7 themes; we upgraded Midnight to be
  the premium system. Because Compose screens read `MaterialTheme.colorScheme` + `LocalMksDesignTokens`,
  the new palette already cascades app-wide when Midnight is active. Other themes are untouched.
- **Golden rule:** never hardcode a hex color in a screen. Always go through `colorScheme.*` or
  `LocalMksDesignTokens.current.*`, and gate gradients/glow on `tokens.useGradients`. This is what keeps
  all 7 themes working from one screen implementation.
- **Build blocker (important):** the dev environment used to write this could **not compile** — no Android
  SDK, `gradle.properties`/`local.properties` point at another user (`ahmedy.ajam`), only JDK 26 installed
  (project needs 17). All code so far is written + statically checked but **not compile-verified**. Whoever
  has a working toolchain must run the compile (§10) and report errors.

---

## 1. Design principles

- **Premium, nocturnal, calm.** Deep-navy surfaces, one clear focal element per screen, generous negative
  space, subtle depth/glow. Never harsh, never pure-black.
- **One design system, theme-scoped.** Components read tokens, not literals, so they adapt to every theme
  and flatten correctly on Plain themes. Dawn / Forest / Lavender / Plain keep working unchanged.
- **RTL-safe.** MKS is bilingual (English + Arabic). Use `start`/`end` (never `left`/`right`), symmetric
  layouts, so Arabic mirrors cleanly.
- **Accessible.** Respect `fontScale` (0.5×–2.0×) and `uiDensity` — already wired through `LocalDensity` in
  `MKSTheme`. Honor `tokens.isPlain` / `tokens.useGradients` (Plain themes disable gradients + animations).
- **Preserve behavior; restyle the shell.** Ports must keep all existing state, flows, and interactions.
  Change the *look*, not the logic. (See §11 for the do-not-break list.)

---

## 2. CURRENT STATUS — what has been done in the real codebase

> Stitch mockups (§3) are design only. This section is the actual app code.

### 2.1 Done (written, statically verified, NOT yet compiled)

| # | File | Change | Notes |
|---|------|--------|-------|
| 1 | `core/ui/src/main/java/com/ahmedyejam/mks/ui/theme/Color.kt` | Replaced the **Midnight** palette with exact Midnight Premium tokens; added `MidnightSuccess`, `MidnightWarning`, `MidnightTextMuted`. | Only Midnight changed. |
| 2 | `core/ui/src/main/java/com/ahmedyejam/mks/ui/theme/Theme.kt` | Added `isMidnight` branch so `MksDesignTokens` (success/warning/selected/correct/wrong) use the premium accents when Midnight is active. | Other themes untouched. |
| 3 | `core/ui/src/main/java/com/ahmedyejam/mks/ui/components/MidnightComponents.kt` | **New file** — 11 reusable composables (§5). | Unused until screens call them. |
| 4 | `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/QuizPlayerScreen.kt` | (a) Top question-progress bar `LinearProgressIndicator` → `GradientProgressBar`. (b) Fixed 2 missing-drawable refs (see §9.1). | Sliding sheet + toggles intentionally untouched (§11). |
| 5 | `feature/ui/src/main/java/com/ahmedyejam/mks/ui/quiz/CompilerViewModel.kt` | Fixed wrong `MksResult` package in the `when` block (§9.2). | Pre-existing bug, not from redesign. |
| 6 | `Important docs/DESIGN.md` | This document. | — |

### 2.2 Pending / in-flight

- **`LibraryComponents.kt` drawable fix (NOT yet applied).** Lines ~204–205 reference the same two missing
  drawables as QuizPlayerScreen did. Apply the identical fix from §9.1 there.
- **Compile verification.** Nothing above has been built. Run §10.
- **14 of 15 screens not ported.** Only the Quiz Player top progress bar is restyled. Every other screen
  still uses its original layout (recolored only by the new Midnight palette cascade).
- **Fonts not wired.** `Type.kt` still uses `FontFamily.Default`. Space Grotesk + Inter are specified but
  not added as resources (§4).

### 2.3 Explicit user constraints (do not violate)

- **Quiz Player up-sliding panel + toggle arrangement must stay exactly as-is.** The user explicitly asked
  that the anchored-draggable sheet, the `ControlToggle` grid (Categories, Explain, One-by-one, Rapid,
  Eliminate, Drop, Focus, Mark, Finish), the nav-filter chips, and the question-status dots are **not**
  restyled. The submit button *inside the sheet* also stays. Redesign only the main question area.

---

## 3. Stitch project reference

- **Project name:** `MKS — Premium Dark Redesign`
- **Project ID:** `12704736459654377525`
- **Design-system asset (apply to every screen generation):** `assets/39368094394243647` — name `MKS Midnight Premium`
- **Device type:** `MOBILE` for all screens.
- **MCP tools:** `mcp__tools__stitch_mcp_*` — `list_projects`, `get_project`, `list_screens`, `get_screen`,
  `generate_screen_from_text`, `edit_screens`, `generate_variants`, `create/update_design_system`.

### 3.1 Design-system parameters (as sent to `create_design_system`)

```
displayName:  MKS Midnight Premium
colorMode:    DARK
colorVariant: EXPRESSIVE
headlineFont: SPACE_GROTESK
bodyFont:     INTER
labelFont:    INTER
roundness:    ROUND_TWELVE
customColor / overridePrimaryColor:   #D2BCFB  (lavender)
overrideSecondaryColor:               #FFB77D  (orange)
overrideTertiaryColor:                #8FB8FF  (blue)
overrideNeutralColor:                 #0A0E1A  (deep navy)
designMd:     (the Midnight Premium narrative — see §4 for the canonical color list)
```

To regenerate or add a screen, call `generate_screen_from_text` with
`{ projectId: "12704736459654377525", designSystem: "assets/39368094394243647", deviceType: "MOBILE",
prompt: <detailed screen prompt> }`. Prompts should be explicit about layout top→bottom, name each color
by hex, and describe component states (selected/answered/etc.). See §8 for per-screen prompt intent.

### 3.2 The 15 generated screens (Stitch screen IDs)

| # | Screen | Stitch screen ID | Maps to app file (§7) |
|---|--------|------------------|------------------------|
| 1 | Library Home | `28530363297d477bbb0e2b42f83aed0c` | `library/LibraryScreen.kt` + `library/components/*` |
| 2 | Quiz Player | `585bb5e392934775972dabe1ddab1b24` | `quiz/QuizPlayerScreen.kt` |
| 3 | Session Summary | `c7948b033c3647ac9b8715e79943d716` | `summary/SummaryScreen.kt` |
| 4 | Book Dashboard | `5b2450a703b74f8e9ef119901b481684` (banner img `332191b899c841fa87a963d74b8306b7`) | `booktools/BookKnowledgeDashboardScreen.kt` + `BookDashboardTabs.kt` |
| 5 | Flashcard Study | `23b68f5a30414665965180ec8c226fbc` | `flashcard/FlashcardDeckScreen.kt` |
| 6 | Slideshow Player | `9ff935cc7eec4941bbf0c1cf2b5604a3` (img `5e9055172caa4d3499925c02a62055d1`) | `slideshow/SlideshowCourseScreen.kt` |
| 7 | Note / Article Reader | `aefa7c5495d2410ab57b6b09bd384b73` | `booktools/ReviewBlueprintScreen.kt` |
| 8 | Review Dashboard | `49d46530c74a4e37b5d2e8ff104418d8` | `review/ReviewDashboardScreen.kt` |
| 9 | Settings | `138c218c86bd48ac85d1e680bee81549` | `settings/SettingsScreen.kt` |
| 10 | Global Search | `ffc002549b284456becfa762f7ba3af7` | `search/GlobalSearchScreen.kt` |
| 11 | Welcome / Onboarding | `a439fe13156d4db683dcd13d2df048ad` (img `3cbf5243ecda447dba16ea005c3be135`) | `welcome/WelcomeScreen.kt` |
| 12 | Data Tools (Import/Export) | `5ac252772d61418790348ae812f68682` | `data/DataToolsScreen.kt` |
| 13 | AI MCQ Generator | `c914cc02e9bb42318a3e3d61758d4050` | `booktools/AiMcqGeneratorScreen.kt` |
| 14 | AI Prompt Deck | `aad86612a85f49459cb0d38b6251502e` | `booktools/AiPromptDeckScreen.kt` |
| 15 | PDF Extraction | `d5bebc07dfc24185a1054211afe5c322` | `booktools/PdfExtractionScreen.kt` |

> To view any screen render: `get_screen { name: "projects/12704736459654377525/screens/<id>" }` and read
> the `screenshot.downloadUrl` (curl → Read the PNG). The `htmlCode.downloadUrl` gives the exact HTML/CSS.

---

## 4. Color, typography, shape tokens (canonical)

Defined in `core/ui/.../theme/Color.kt` (Midnight block) + `MksDesignTokens.kt`. **Never inline these hex
in a screen** — reference the ColorScheme slot / token.

### 4.1 Color → token map (Midnight)

| Role | Hex | Access in Compose | Use |
|------|-----|-------------------|-----|
| Background | `#0A0E1A` | `colorScheme.background` | screen base (deep navy) |
| Surface (card) | `#111624` | `colorScheme.surface` | glassy elevated cards |
| Surface high | `#171D2E` | `colorScheme.surfaceVariant` | nested / higher elevation |
| Primary (lavender) | `#D2BCFB` | `colorScheme.primary` | CTAs, active/selected, focus ring |
| On-primary | `#20182F` | `colorScheme.onPrimary` | text on lavender fills |
| Secondary (orange) | `#FFB77D` | `colorScheme.secondary` | streaks, energy accents |
| Tertiary (blue) | `#8FB8FF` | `colorScheme.tertiary` | info, links, question accent |
| Text primary | `#E7E9F2` | `colorScheme.onBackground` / `onSurface` | body + headings |
| Text secondary | `#A2A7BC` | `colorScheme.onSurfaceVariant` | subtitles, meta |
| Text muted | `#6A7189` | `tokens` → `MidnightTextMuted` | timestamps, faint hints |
| Outline (hairline) | `#2A3348` | `colorScheme.outline` | 1px card borders |
| Success / correct | `#6DD3A0` | `tokens.success` / `tokens.correct` | right answers, done |
| Warning / merged | `#FFC978` | `tokens.warning` | caution, "merged · skipped" |
| Error / wrong | `#FF9A8F` | `colorScheme.error` / `tokens.wrong` | wrong answers, destructive, danger zone |

**AI accent gradient:** lavender → orange (`primary` → `secondary`). **Reserved for AI features only**
(MCQ generator, prompt deck, PDF extraction): the "AI" sparkle badge + generate buttons. Do not use elsewhere.

**Per-type accent map** (search results, learning formats, prompt output types, queue cards):

| Entity / format | Accent |
|-----------------|--------|
| Book / Quiz / Prompt-Quiz | tertiary blue `#8FB8FF` |
| Flashcards | secondary orange `#FFB77D` |
| Notes / Blueprints | success green `#6DD3A0` |
| Slides / general | primary lavender `#D2BCFB` |
| Mistakes | error red `#FF9A8F` |

### 4.2 Typography

`theme/Type.kt`. Target families (NOT yet wired — see §2.2):

- **Headlines / numbers → Space Grotesk** (tight tracking, bold): screen titles, scores, big stats, card
  titles, slide headlines. Compose: `headlineLarge/Medium/Small`, `titleLarge`.
- **Body / labels → Inter** (line-height ~1.5): paragraphs, meta, chips, buttons. `bodyLarge/Medium/Small`,
  `labelLarge/Medium`.
- **Numeric** (scores, timers, tokens): semibold, tabular figures where available.
- **Monospace**: `{{variables}}`, token counts, extracted-text previews.

> To wire fonts: add Space Grotesk + Inter font resources (`core/ui/src/main/res/font/`), build a
> `FontFamily` for each, and set them on the `Typography` styles in `Type.kt`. Until then, size/weight
> hierarchy still applies; only the typeface differs.

### 4.3 Shape, elevation, spacing (from `MksDesignTokens`)

Theme-scoped; Plain themes flatten these automatically.

- `cardRadius` **18dp** (Plain 12dp) — content cards, sheets
- `chipRadius` **14dp** (Plain 8dp) — chips, small pills
- Pill buttons: fully rounded (`CircleShape` for height-bound pills)
- `cardElevation` **2dp** (Plain 0dp)
- `pagePadding` **16dp**, `compactGap` **8dp**, `relaxedGap` **16dp**
- 8px base grid everywhere else.
- **Glass surface:** use `Modifier.premiumGlassBackground()` (in `MksDesignTokens.kt`) — top-lit translucent
  gradient + surface tint. Add a 1px `outline` hairline; selected cards get a lavender glow ring
  (`GlassCard(selected = true)`).

### 4.4 Motion

Subtle depth, glow-on-focus, gentle elevation. All animation must go through `tokens.animationSpec()` which
returns `snap()` on Plain themes (so Plain = instant, no motion). Glow marks *the* primary action or *the*
selected item only — never stack competing glows.

---

## 5. Component library (`MidnightComponents.kt`)

Location: `core/ui/src/main/java/com/ahmedyejam/mks/ui/components/MidnightComponents.kt`. All are
theme-scoped (read `colorScheme` + `LocalMksDesignTokens`, gate gradients on `useGradients`). **Build screens
from these; add new shared components here rather than styling inline.**

| Component | Signature intent | Use |
|-----------|------------------|-----|
| `GlassCard(selected, onClick, contentPadding) { }` | glassy surface, hairline border, lavender glow ring when `selected` | every content card |
| `PrimaryPillButton(text, onClick, leadingIcon, enabled, glow)` | lavender fill, dark text, soft glow | primary CTAs |
| `GhostPillButton(text, onClick, leadingIcon, enabled)` | transparent + hairline border | secondary actions |
| `GradientProgressBar(progress, height, trackColor)` | thin lavender→orange rounded bar (0f..1f) | quiz/deck/mastery progress |
| `ScoreRing(progress, diameter, stroke) { center }` | circular gradient ring with arbitrary center content | summary score, review "due today" |
| `CategoryChip(label, dotColor, selected, trailingCount, onClick)` | translucent pill + colored dot; lavender fill when selected | filters, category tags |
| `AccentIconTile(icon, accent, size)` | rounded icon tile tinted by per-type accent | list-item leading icons |
| `CountBadge(text, color)` | small count pill | due counts, "NEW", streak |
| `AiBadge(label = "AI")` | lavender→orange sparkle chip | AI features only |
| `SectionHeader(title, trailing)` | uppercase muted group label + optional action | section dividers |
| `SrsRatingRow(ratings, onRate, emphasizedIndex)` + `data class SrsRating(label, interval, color)` | 4 spaced-repetition rating pills | flashcard study |

Existing components to keep using: `EmptyStateCard`, `SummaryCard`, `LoadingErrorState`
(`MksReusableComponents.kt`), `StudyTopAppBar` (`StudyTopAppBar.kt`), `EntityEditDialog`,
`ChangePreviewDialog`, `CategoryComponents`.

---

## 6. Do / Don't

**Do**
- Reference `colorScheme` + `LocalMksDesignTokens` — never a raw `Color(0xFF…)` in a screen.
- Gate gradients/glow behind `tokens.useGradients`; gate motion behind `tokens.animationSpec()`.
- Keep one dominant focal element per screen.
- Use `start`/`end` for RTL.
- Reuse §5 components; extend the shared file for new patterns.
- Preserve all ViewModel state/flows/interactions when porting (§11).

**Don't**
- Hardcode navy/lavender hex in feature screens.
- Stack multiple competing glows.
- Use pure black (`#000`) — base is deep navy `#0A0E1A`.
- Apply the AI lavender→orange gradient outside AI features.
- Restyle the Quiz Player sliding sheet / toggles (§2.3).
- Claim a build passed that you did not run (§10).

---

## 7. Screen → app-file map & module architecture

MKS is a 6-module app (`app`, `core/{model,database,data,network,ui}`, `feature/ui`). UI lives in
`feature/ui/src/main/java/com/ahmedyejam/mks/ui/`. Package base: `com.ahmedyejam.mks`.

| Screen | Screen file | ViewModel |
|--------|-------------|-----------|
| Library | `library/LibraryScreen.kt` (+ `library/components/`) | `library/LibraryViewModel.kt` |
| Quiz Player | `quiz/QuizPlayerScreen.kt` | `quiz/QuizViewModel.kt` |
| Summary | `summary/SummaryScreen.kt` | `summary/SummaryViewModel.kt` |
| Book Dashboard | `booktools/BookKnowledgeDashboardScreen.kt`, `BookDashboardTabs.kt`, `BookToolScreens.kt` | `booktools/BookToolsViewModel.kt` |
| Flashcards | `flashcard/FlashcardDeckScreen.kt` | `flashcard/FlashcardDeckViewModel.kt` |
| Slideshow | `slideshow/SlideshowCourseScreen.kt` | `slideshow/SlideshowCourseViewModel.kt` |
| Note Reader | `booktools/ReviewBlueprintScreen.kt` | `booktools/BookToolsViewModel.kt` |
| Review Dashboard | `review/ReviewDashboardScreen.kt` | `review/ReviewDashboardViewModel.kt` |
| Settings | `settings/SettingsScreen.kt` | `settings/SettingsViewModel.kt` |
| Global Search | `search/GlobalSearchScreen.kt` | `search/GlobalSearchViewModel.kt` |
| Welcome | `welcome/WelcomeScreen.kt` | — |
| Data Tools | `data/DataToolsScreen.kt` | `data/DataToolsViewModel.kt` |
| AI MCQ Generator | `booktools/AiMcqGeneratorScreen.kt` | `booktools/AiMcqGeneratorViewModel.kt` |
| AI Prompt Deck | `booktools/AiPromptDeckScreen.kt` | `booktools/BookToolsViewModel.kt` |
| PDF Extraction | `booktools/PdfExtractionScreen.kt` | `booktools/PdfExtractionViewModel.kt` |

Navigation graph: `feature/ui/.../MksNavHost.kt`; routes in `core/model/.../ui/MksRoutes.kt`. Theme applied
at `MainActivity` via `MKSTheme(themeMode, dynamicColor, fontScale, uiDensity)`.

---

## 8. Per-screen design spec (what each Stitch screen contains → what to build)

Each entry: the intended layout + the §5 components to assemble. Match the Stitch render (get it via §3.2).

1. **Library Home** — top bar (workspace avatar + name + chevron, search, settings); greeting headline +
   subtitle; horizontal `CategoryChip` filter row; full-width "Continue studying" resume `GlassCard` with
   cover thumb + `GradientProgressBar` + lavender play button; `SectionHeader("Your Library", trailing=Sort)`;
   2-col `GlassCard` grid (cover, category dot+label, title, meta counts, progress + %); lavender FAB (+);
   floating pill bottom nav (Library active, Review, Search, Profile).
2. **Quiz Player** — slim top bar (X, `GradientProgressBar` "Question n/m", streak `CountBadge` orange);
   meta row (timer ring, `CategoryChip` w/ blue dot, flag); question `GlassCard` (Space Grotesk + optional
   image); 4 option cards = `GlassCard(selected=lavender glow)`, correct=green, eliminated=dimmed
   strike-through; quick tools (eliminate/TTS/50-50); `PrimaryPillButton` "Submit". **Main area only —
   sheet/toggles untouched (§2.3).**
3. **Session Summary** — top bar (X, "Session Summary", share); hero `ScoreRing` (85%, "34/40 correct",
   congrats + quiz name); 3 stat `GlassCard`s (Time/Streak/XP); "Performance by category" rows =
   `GradientProgressBar` colored by score (green/orange/red); "Review your mistakes" `GlassCard` (red tint,
   count, chevron); `PrimaryPillButton` "Retry weak areas" + `GhostPillButton` "Back to Library".
4. **Book Dashboard** — collapsing hero banner (gradient img, X, ⋯, title, subtitle, mastery
   `GradientProgressBar`); quick-stats row; 5 learning-format `GlassCard`s each w/ `AccentIconTile`
   (Quizzes=lavender+ring, Flashcards=orange+`CountBadge` "48 due", Slideshows=blue+progress, Notes=green,
   AI Prompts=gradient tile + `AiBadge`); Sources mini-card + "Generate MCQs with AI" link;
   `PrimaryPillButton` "Start studying".
5. **Flashcard Study** — top bar (X, deck title + "Card n/m", shuffle); deck `GradientProgressBar` + bucket
   tallies (New blue / Learning orange / Review green); hero flip `GlassCard` (front + revealed back, hint
   chip, TTS, star); `SrsRatingRow` (Again red/Hard orange/Good lavender emphasized/Easy green, each w/
   interval); undo + swipe hints.
6. **Slideshow Player** — top bar (X, course title + "Slide n/m", index icon); segmented tick progress
   (filled lavender, current glowing) + "% complete"; slide `GlassCard` (kicker, headline, illustration,
   bullets, collapsed Speaker Notes); Listen (TTS) + Mark Done pills; prev/next circular nav (next = lavender
   glow) + counter + bookmark.
7. **Note / Article Reader** — minimal reading bar (X, Aa, TTS, bookmark); header (`CategoryChip`, big title,
   meta); TTS playback bar (play/pause, progress line, speed); Summary callout `GlassCard` (lavender tint);
   Key-points `GlassCard`; long-form body w/ Space Grotesk subheadings + one orange highlight annotation;
   floating toolbar (Highlight / Add note / Make flashcard) + `PrimaryPillButton` "Mark reviewed".
8. **Review Dashboard** — top bar (menu, "Daily Review", calendar/streak); hero `GlassCard` w/ `ScoreRing`
   (18/42 due today), "24 remaining", streak, `PrimaryPillButton` "Start review"; weekly consistency dots;
   `SectionHeader("Review Queues")`; queue `GlassCard`s w/ `AccentIconTile` + `CountBadge` (Flashcards
   orange 28, Blueprints green 9, Mistakes red 5, Weak categories blue 3); "Build a custom session" ghost
   card; pill bottom nav (Review active).
9. **Settings** — top bar; profile/workspace strip; grouped `GlassCard`s w/ `SectionHeader` per group:
   APPEARANCE (theme swatch row w/ Midnight selected+ring, EN/العربية toggle, show-cover switch, font-scale
   + density sliders), QUIZ BEHAVIOR (focus/double-tap/skip switches, rapid-advance value), AI & EXTRACTION
   (Ollama "connected" green dot, extraction settings), LIBRARY & BACKUP (export ZIP / import / data tools),
   DANGER ZONE (error-tinted: clear categories, reset database); footer version line. Real strings from
   `SettingsScreen.kt`.
10. **Global Search** — top bar with prominent glassy search field (magnifier, active query, clear ×,
    lavender focus ring); horizontal `CategoryChip` type filters w/ counts; result-count line; results
    grouped by type w/ `SectionHeader` (accent per type) + rows = `GlassCard` + `AccentIconTile` + title
    (match highlighted lavender) + parent subtitle + snippet + chevron. Types map to
    `GlobalSearchResultType` enum (BOOK, QUIZ, QUESTION, FLASHCARD, BLUEPRINT, SLIDE, PROMPT_*, MISTAKE, …).
11. **Welcome / Onboarding** — glowing brand orb hero (use a vector brand mark in code, NOT the AI image —
    it baked in placeholder text); "MKS Library / My Knowledge Space" wordmark + real tagline "Your space to
    collect, understand and grow. All offline. All yours."; EN/العربية toggle; theme swatch picker; 3 feature
    `GlassCard`s (Import & organize / Focused study / Fully offline); `PrimaryPillButton` "Get Started" +
    "Explore Features" ghost; page dots. Real strings from `WelcomeScreen.kt` / `strings.xml`.
12. **Data Tools** — top bar; intro line; Export `GlassCard` (Schema 7 ZIP desc, stats strip,
    `PrimaryPillButton` "Export to ZIP"); Import `GlassCard` (dashed drop zone, format chips
    XLSX/CSV/JSON/HTML/TEXT/PPTX/ZIP, merge-strategy selector Replace/Merge/Skip, `GhostPillButton` "Preview"
    + `PrimaryPillButton` "Start import"); Recent activity rows w/ `CountBadge` status pills (green Done /
    orange Merged).
13. **AI MCQ Generator** — top bar w/ `AiBadge` + gear; intro; source-input `GlassCard` (section name field,
    multiline paste area, token counter, "paste from PDF"); config strip (Ollama connected dot, chat model,
    Advanced Options, "Simple Extraction Mode" toggle); generation options (MCQ count stepper, "Enrich"
    + "Review pass" toggles = the 3-pass pipeline); `PrimaryPillButton` "Generate MCQs" (glow); pipeline
    steps (Extract ✓ → Generate ● spinner → Review pending) + `GradientProgressBar`; preview MCQ `GlassCard`s
    (Q badge, stem, A–D w/ green-check correct, hint chip, edit); "Save as quiz" + "Discard". Real strings
    from `AiMcqGeneratorScreen.kt`.
14. **AI Prompt Deck** — gradient hero (title, subtitle, `AiBadge`); Cards/Runs/About tabs; prompt
    `GlassCard`s w/ `AccentIconTile` colored by output type (NOTE green / FLASHCARDS orange / QUIZ blue /
    BLUEPRINT lavender — see `PromptOutputType`), title, prompt preview w/ `{{variable}}` chips, tag chips +
    "used n×" + Run pill; "New prompt card" ghost row; raised run bottom-sheet (variable inputs, output-type
    indicator, model line + green dot, `PrimaryPillButton` "Run prompt").
15. **PDF Extraction** — top bar w/ `AiBadge` + gear; source-doc strip (red PDF icon, filename, page/size,
    Replace); controls `GlassCard` (page-range w/ thumbnails, Text-layer / Vision-OCR mode toggle + llava
    model line, `PrimaryPillButton` "Extract text"); progress row; extracted-text preview `GlassCard`
    (monospace, green confidence chip, cleanup toggle pills, word/token stats); bottom bar (Copy / Save as
    source ghost + `PrimaryPillButton` "Send to MCQ Generator →").

---

## 9. Known issues & tech debt (pre-existing, discovered during the port)

> These are NOT caused by the redesign — they surfaced when the codebase was inspected/compiled. Fix as part
> of getting a clean build.

### 9.1 Missing drawables (2 files)

`contact_banner_en_light_forest_path` and `contact_banner_en_light_lavender_valley` are referenced but do
**not exist anywhere** in the repo (only their `_ar_` counterparts + an `_en_light_sunrise_valley` exist in
`core/ui/src/main/res/drawable-nodpi/`). Referenced in **two** places:
- `quiz/QuizPlayerScreen.kt` `resolveQuestionImage()` — **FIXED** (English Forest/Lavender now fall back to
  `contact_banner_en_light_sunrise_valley`).
- `library/components/LibraryComponents.kt` lines ~204–205 — **STILL TO FIX** (apply the same fallback).

Proper long-term fix: add real `contact_banner_en_light_forest_path.webp` +
`contact_banner_en_light_lavender_valley.webp` assets (English variants of the existing AR banners) to
`drawable-nodpi/`, then restore the original references. AI agents cannot author binary images — either
generate them or leave the sunrise fallback.

### 9.2 Non-exhaustive `when` in CompilerViewModel (FIXED)

`quiz/CompilerViewModel.kt` (~line 520) matched `com.ahmedyejam.mks.data.importer.model.MksResult.Success/Error`,
but `MksResult` actually lives in **`com.ahmedyejam.mks.data.model`** (`core/model/.../data/model/MksResult.kt`).
Wrong FQN → branches didn't match the real sealed type → non-exhaustive. Fixed by correcting the package on
both `is …Success` and `is …Error` branches. `Success.data` and `Error.message` field access were already
correct.

### 9.3 Other

- Fonts not wired (§4.2). - `Type.kt` only overrides `bodyLarge`; full type scale is Material defaults.
- After swaps, prune now-unused imports (e.g. `LinearProgressIndicator` in QuizPlayerScreen) — warnings, not
  errors, but keep it clean.

---

## 10. Build & verify (the compile the writing environment could not run)

```bash
# 1. Fix the machine-specific paths (they point at another dev, 'ahmedy.ajam'):
#    - local.properties: sdk.dir=<your Android SDK>
#    - gradle.properties: org.gradle.java.home=<your JDK 17>   (project targets JVM 11 / JDK 17 toolchain)
# 2. Compile the touched modules first:
./gradlew :core:ui:compileDebugKotlin :feature:ui:compileDebugKotlin
# 3. Full debug build + install:
./gradlew assembleDebug        # or installDebug
# 4. Run on a device, switch to the MIDNIGHT theme (Settings → Appearance), and eyeball each ported screen.
```

**Verification discipline:** do not report a screen "done" until it compiles AND has been visually checked on
the Midnight theme AND spot-checked on one light theme (Dawn/Forest) + Plain (to confirm gradients/motion
fall back). Never claim a green build you didn't run.

---

## 11. Porting workflow & guardrails

### 11.1 Per-screen workflow
1. Open the target `*Screen.kt` and its ViewModel. Identify the UI-only composables vs. state/logic.
2. Get the Stitch render (§3.2) for the target look.
3. Swap `Card`/`Button`/`Surface`/`LinearProgressIndicator`/chips → §5 components. Replace inline colors with
   `colorScheme`/`tokens`.
4. Keep every `viewModel.*` call, `collectAsState`, `LaunchedEffect`, remembered state, and callback signature
   identical.
5. Verify RTL (`start`/`end`) and Plain fallback (`useGradients`, `animationSpec`).
6. Compile `:feature:ui`; eyeball on Midnight (§10).
7. Do **one screen at a time**; get a clean compile before the next. Prefer the smallest reversible change
   when introducing a new component to isolate linkage errors.

### 11.2 Guardrails (do NOT break)
- **Preserve all behavior.** No changes to ViewModels, repositories, DAOs, navigation routes, or DI unless a
  bug fix (like §9.2) requires it.
- **Quiz Player sheet + toggles stay as-is** (§2.3).
- **All 7 themes must keep working.** Test that a screen still renders on Dawn/Forest/Lavender/Plain — the
  point of token-based styling.
- **RTL must stay correct** — the app ships Arabic.
- **Don't delete/rename drawables or string resources** without checking every reference first.
- **Don't introduce hardcoded colors, pure black, or AI gradient outside AI screens.**

### 11.3 Suggested order (highest value first)
Library → Summary → Book Dashboard → Review Dashboard → Flashcards → Slideshow → Note Reader → Global Search
→ Settings → Welcome → Data Tools → AI MCQ → AI Prompt Deck → PDF Extraction. (Quiz Player main area partially
done; finish its `OptionItem` states + streak `CountBadge` after the first clean compile.)

---

## 12. Ultimate vision

A single, coherent **Midnight Premium** experience across all 15 screens and every daily-use loop —
discover (Library) → dive into a subject (Book Dashboard) → study (Quiz / Flashcards / Slideshow / Reader) →
reinforce (Review) → reflect (Summary), plus utilities (Settings / Search / Welcome / Data Tools) and the AI
differentiators (MCQ generator / Prompt deck / PDF extraction). The finished app should:

- Feel like a premium, low-glare nocturnal study companion — calm, focused, one clear action per screen.
- Be driven entirely by tokens + the shared component library, so future screens are assembly, not styling,
  and all 7 themes + RTL + accessibility scaling keep working for free.
- Have Space Grotesk + Inter wired, real brand assets (Welcome orb, English banner variants) in place, and a
  fully compile-verified, visually QA'd build.
- Retain 100% of existing functionality — the redesign is a re-skin over the proven MKS logic, never a
  behavior change.

**Definition of done:** all 15 screens ported to §5 components with zero hardcoded colors; `./gradlew
assembleDebug` green; every screen visually verified on Midnight + one light + one Plain theme, in both LTR
and RTL; §9 tech debt resolved; this document kept up to date as screens land (move rows from §2.2 to §2.1).
