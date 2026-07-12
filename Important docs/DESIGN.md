# MKS — Midnight Premium Design System

> The visual + component spec for the MKS redesign. Source of truth for porting Stitch designs
> into Compose. Pairs with `core/ui/.../theme/` (tokens) and
> `core/ui/.../components/MidnightComponents.kt` (reusable composables).
> Last updated: 2026-07-11.

---

## 1. Principles

- **Premium, nocturnal, calm.** Low-glare deep-navy surfaces, one clear focal element per screen,
  generous negative space. Feels like Linear / Arc / Things for study.
- **One design system, theme-scoped.** The redesign lives in the **Midnight** theme. Components read
  `MaterialTheme.colorScheme` + `LocalMksDesignTokens`, so they adapt to every theme automatically —
  never hardcode hex in a screen. Dawn / Forest / Lavender / Plain keep working unchanged.
- **RTL-safe.** Symmetric layouts, `start`/`end` (never `left`/`right`), so Arabic mirrors cleanly.
- **Accessible.** Respects `fontScale` (0.5×–2.0×) and `uiDensity` (already wired via `LocalDensity`).
  `isPlain` themes disable gradients/animations — components must honor `tokens.useGradients`.

---

## 2. Color tokens (Midnight)

Defined in `theme/Color.kt`. Do not inline these hex values in screens — reference them through
`MaterialTheme.colorScheme.*` or `LocalMksDesignTokens.current.*`.

| Role                | Hex        | ColorScheme slot / token          | Use                                   |
|---------------------|------------|-----------------------------------|---------------------------------------|
| Background          | `#0A0E1A`  | `background`                      | screen base (deep navy)               |
| Surface (card)      | `#111624`  | `surface`                        | glassy elevated cards                 |
| Surface high        | `#171D2E`  | `surfaceVariant`                 | nested / higher-elevation surfaces    |
| Primary (lavender)  | `#D2BCFB`  | `primary`                        | CTAs, active states, focus ring, selection |
| On-primary          | `#20182F`  | `onPrimary`                      | text on lavender fills                |
| Secondary (orange)  | `#FFB77D`  | `secondary`                      | streaks, energy accents, warn status  |
| Tertiary (blue)     | `#8FB8FF`  | `tertiary`                       | informational, links, question accent |
| Text primary        | `#E7E9F2`  | `onBackground` / `onSurface`     | body + headings                       |
| Text secondary      | `#A2A7BC`  | `onSurfaceVariant`               | subtitles, meta                       |
| Text muted          | `#6A7189`  | `tokens` → `MidnightTextMuted`   | timestamps, faint hints               |
| Outline (hairline)  | `#2A3348`  | `outline`                        | 1px card borders                      |
| Success / correct   | `#6DD3A0`  | `tokens.success` / `tokens.correct` | right answers, done states         |
| Warning / merged    | `#FFC978`  | `tokens.warning`                 | caution, "merged · skipped"           |
| Error / wrong       | `#FF9A8F`  | `error` / `tokens.wrong`         | wrong answers, destructive, danger zone |

**AI accent gradient:** lavender → orange (`primary` → `secondary`). Reserved for AI features
(MCQ generator, prompt deck, PDF extraction) — the "AI" sparkle badge and generate buttons.

**Per-type accent map** (search results, learning formats, prompt output types):

| Entity / format         | Accent      |
|-------------------------|-------------|
| Book / Quiz / Prompt-Quiz | tertiary blue `#8FB8FF` |
| Flashcards              | secondary orange `#FFB77D` |
| Notes / Blueprints      | success green `#6DD3A0` |
| Slides / general        | primary lavender `#D2BCFB` |
| Mistakes                | error red `#FF9A8F` |

---

## 3. Shape, elevation, spacing

From `MksDesignTokens` (theme-scoped; Plain themes flatten these):

- `cardRadius` **18dp** (Plain 12dp) — content cards, sheets
- `chipRadius` **14dp** (Plain 8dp) — chips, small pills
- Pill buttons: fully rounded (`50%` / `CircleShape` for height-bound pills)
- `cardElevation` **2dp** (Plain 0dp)
- `pagePadding` **16dp**, `compactGap` **8dp**, `relaxedGap` **16dp**
- 8px base grid for everything else.

**Glass surface:** use the existing `Modifier.premiumGlassBackground()` (in `MksDesignTokens.kt`) —
top-lit translucent gradient + surface tint. Add a 1px `outline` hairline border and, for
active/selected cards, a lavender glow ring (see `GlassCard(selected = true)`).

---

## 4. Typography

`theme/Type.kt`. Two families:

- **Headlines / numbers → Space Grotesk** (tight tracking, bold): screen titles, scores, big stats,
  card titles, slide headlines. `headlineLarge/Medium/Small`, `titleLarge`.
- **Body / labels → Inter** (line-height ~1.5): paragraphs, meta, chips, buttons.
  `bodyLarge/Medium/Small`, `labelLarge/Medium`.
- **Numeric (scores, timers, tokens):** semibold, tabular figures where possible.
- **Monospace:** `{{variables}}`, token counts, extracted-text previews.

> NOTE: `Type.kt` currently uses `FontFamily.Default`. Wiring Space Grotesk + Inter is a follow-up
> (add font resources + `FontFamily`). Until then, weight/size hierarchy still applies.

---

## 5. Component inventory

Reusable composables live in `core/ui/.../components/MidnightComponents.kt`. Build screens from these.

| Component            | Purpose                                                        |
|----------------------|----------------------------------------------------------------|
| `GlassCard`          | glassy surface w/ hairline border, optional selected glow ring |
| `PrimaryPillButton`  | lavender fill, dark text, optional soft glow + leading icon     |
| `GhostPillButton`    | transparent, hairline border — secondary actions               |
| `GradientProgressBar`| thin lavender→orange rounded progress bar                       |
| `ScoreRing`          | circular gradient ring w/ center content (scores, "due today")  |
| `CategoryChip`       | translucent pill + colored dot + label; selectable             |
| `AccentIconTile`     | rounded leading icon tile tinted by per-type accent             |
| `CountBadge`         | small pill count (due counts, "NEW")                            |
| `SrsRatingRow`       | 4 spaced-repetition rating pills (Again/Hard/Good/Easy)          |
| `AiBadge`            | lavender→orange "AI" sparkle chip for AI features               |
| `SectionHeader`      | uppercase muted group label + optional trailing action          |

Existing (keep using): `EmptyStateCard`, `SummaryCard`, `LoadingErrorState`, `StudyTopAppBar`.

---

## 6. Do / Don't

**Do**
- Reference `colorScheme` + `LocalMksDesignTokens` — never a raw `Color(0xFF…)` in a screen.
- Gate gradients/glow behind `tokens.useGradients` so Plain themes stay flat.
- Keep one dominant focal element (hero card / big score / big question) per screen.
- Use `start`/`end` alignment and paddings for RTL.
- Reuse the components in §5; add a new one to the shared file rather than styling inline.

**Don't**
- Hardcode navy/lavender hex in feature screens.
- Stack multiple competing glows — glow marks *the* primary action or *the* selected item only.
- Use pure black (`#000`) — the base is deep navy `#0A0E1A`.
- Apply the AI lavender→orange gradient outside AI features.

---

## 7. Screen → component mapping (15 Stitch screens)

| Screen               | Key components                                                       |
|----------------------|---------------------------------------------------------------------|
| Library              | `CategoryChip` row, resume `GlassCard` + `GradientProgressBar`, 2-col `GlassCard` grid, FAB, pill nav |
| Quiz Player          | top `GradientProgressBar`, streak `CountBadge`, option `GlassCard(selected)`, `PrimaryPillButton` submit |
| Summary              | `ScoreRing` hero, stat `GlassCard`s, per-category `GradientProgressBar`, retry/back buttons |
| Book Dashboard       | hero banner, quick-stat row, `AccentIconTile` format `GlassCard`s, `PrimaryPillButton` start |
| Flashcards           | hero `GlassCard`, bucket tallies, `SrsRatingRow`                    |
| Slideshow            | segmented tick progress, slide `GlassCard`, prev/next circular nav   |
| Note Reader          | TTS bar, Summary/Key-points `GlassCard`s, body type, action toolbar  |
| Review Dashboard     | `ScoreRing` hero, weekly dots, queue `GlassCard`s + `CountBadge`     |
| Settings             | grouped `GlassCard`s, theme swatches, switches, `SectionHeader`, danger zone (error tint) |
| Global Search        | search field, `CategoryChip` type filters, grouped results w/ `AccentIconTile` |
| Welcome              | brand hero, lang toggle, theme swatches, feature `GlassCard`s, `PrimaryPillButton` |
| Data Tools           | export/import `GlassCard`s, format chips, merge selector, activity rows + `CountBadge` |
| AI MCQ Generator     | `AiBadge`, source input, config strip, pipeline steps, preview MCQ `GlassCard`s |
| AI Prompt Deck       | `AiBadge`, tabs, prompt `GlassCard`s + `AccentIconTile`, run sheet   |
| PDF Extraction       | `AiBadge`, source strip, mode toggle, text preview, "Send to MCQ" `PrimaryPillButton` |

---

## 8. Porting workflow (per screen)

1. Read the existing `*Screen.kt` + its ViewModel — keep all state/flows, change only the UI shell.
2. Swap raw `Card`/`Button`/`Surface` for the §5 components.
3. Replace any inline colors with `colorScheme`/`tokens`.
4. Verify RTL (`start`/`end`) and Plain-theme fallback (`useGradients`).
5. Build `:feature:ui:compileDebugKotlin`, then eyeball on the Midnight theme.
