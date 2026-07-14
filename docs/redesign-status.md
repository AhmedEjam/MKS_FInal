# Redesign Status & Workflow
This document tracks the current status of the MKS redesign and porting workflow, extracted from the original DESIGN.md.

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
