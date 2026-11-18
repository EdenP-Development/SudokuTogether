# GridMates: Sudoku — Assets-first Home rebuild (implementation plan)

**Decision date:** 2026-11-18. **Status:** Build-ready proposal selected by owner as "Option 2" (practical asset list + Home replacement + GitHub order). Visual outputs still need owner's actual-phone approval. This document is not authorization to silently replace the released app.

## Why this replaces the last visual iteration

Owner inspected Book Chapters and Player Journal phone APKs. The existing large Canvas-generated book and generic rounded-card composition are functional but still read as "AI HTML code art." Do **not** spend another sprint tweaking that same procedural hero, enlarging the book, adding more cards, or generating random standalone icons. Keep the useful Solo/Continue mechanics, excellent Daily activity strip, Co-op/Versus top-level navigation, accessible Sudoku board, permanent-owned cosmetic IDs and backup data.

Current unmerged implementation chain, as verified 2026-11-18:
- [#80 Living Puzzle Book](https://github.com/PlayerEden/SudokuTogether/pull/80), draft
- [#81 Chapters/navigation/Collection](https://github.com/PlayerEden/SudokuTogether/pull/81), draft stacked on #80
- [#82 Player Journal](https://github.com/PlayerEden/SudokuTogether/pull/82), draft stacked on #81
- The real public Beta 1 -> Beta 2 **same-package update is still blocked** on the original production signer and an install-over save-preservation test. No isolated reviewer APK solves that.

## Existing reusable art — source of truth

- **22 real packaged portrait PNGs** in \`app/src/main/res/drawable-nodpi/\`: **12 Woodland, 6 Ocean, 4 Garden**. Do NOT recreate or rename these; use their existing drawable IDs and \`AnimalPortrait(appearance)\` in \`AnimalPortraits.kt\`.
- Woodland generation art-direction references: \`design/woodland/generation.json\`, \`design/woodland/originals/\` and \`design/woodland/packaged-assets.json\`. Ocean packaging: \`design/ocean/packaged-assets.json\`.
- Canonical editorial style: friendly and adult-appropriate, **broad flat cut-paper shapes, confident forest-green contour, restrained cream/gold/natural accent colors, simple facial shapes, calm expression, strong small-size silhouette**, no glossy 3D, giant baby eyes, decorative clutter, noisy photoreal textures, clothing or AI-mutated anatomy. Character illustrations use transparent alpha.
- Current palette: \`Forest #214F3F\`, \`NightForest #193F35\`, \`Cream #F4F3EB\`, \`Paper #FFFDF8\`, \`Gold #8A6729\` (actual Compose \`GameTheme.kt\`). A subtle material palette can accompany these; avoid replacing the whole brand.
- The current \`LivingBookIllustration.kt\` draws scenery, a book, and fixed sample Sudoku digits in Canvas. That generated look is the thing to **retire as the dominant Home art**, not a pattern to expand.

## Design system principle: "A curated puzzle journal with collectible chapters"

Home is the **cover and next page** of the game. Collections are the **illustrated album**. Profile is the **player journal**. Results are **a finished-page stamp and earned keepsake**. One shared original artwork family, not one separate AI art prompt per screen.

Base chapter visual variations: Basic, Woodland, Ocean, Garden. A chapter backdrop is only **visual presentation**; it does NOT imply a new owned unlock, game currency, or seasonal event. An equipped portrait may guide the background choice based on its known id prefix; otherwise show Basic. Only display portraits already available to that user. Themed collection spotlight may advertise locked characters **explicitly marked locked**, not falsely claim ownership.

## Art-production order: exact first eight master assets

Do **one coordinated pilot batch** before attempting a giant asset expansion. Export in lossless PNG or layered source as appropriate, then test a compressed WebP/PNG Android resource. Below are master design pixel sizes, **not physical UI dp dimensions**.

| # | Resource master filename | Master size | Exact visual purpose | First screen |
|---|---|---:|---|---|
| 1 | \`gm_home_chapter_woodland_v1.webp\` | 1536 × 768 | Main horizontal **environment / chapter backdrop**; layered paper/forest vignette, generous empty text-safe right/left zones; NO text, Sudoku digits, fake buttons or embedded animal portrait | Home |
| 2 | \`gm_home_chapter_basic_v1.webp\` | 1536 × 768 | Matching geometry for neutral players; quiet paper/cloth/abstract Sudoku-page motifs, not generic mountain poster | Home |
| 3 | \`gm_surface_bookcloth_v1.webp\` | 512 × 512 | Very subtle tileable woven forest cloth, extremely low contrast; no large fake depth | Home/Player Journal/Results |
| 4 | \`gm_surface_paper_v1.webp\` | 512 × 512 | Very subtle warm-paper grain; does not reduce legibility | Home/Daily/Collection |
| 5 | \`gm_mode_solo_v1.webp\` | 384 × 384 | Compact personal open-puzzle/one pencil motif; alpha, matches existing portrait contours | Home mode / first visit |
| 6 | \`gm_mode_coop_v1.webp\` | 384 × 384 | Two shared pencils or linked puzzle pages, NOT multiplayer avatars of different art style | Home / room setup |
| 7 | \`gm_mode_versus_v1.webp\` | 384 × 384 | Two mirrored puzzle-page corners and restrained brass race flag, not aggressive combat | Home / versus setup |
| 8 | \`gm_daily_seal_v1.webp\` | 384 × 384 | An original warm-gold calendar/page seal, small-size friendly | Daily card / reward |

All 8 assets must be designed with **the existing Owl + Fox asset images as exact reference anchors**, not with independent text-only "cute cozy" prompts. Generate a full-family contact sheet for review, then export each item with controlled crop/margins. No text baked into graphics. These are **proposed assets, not files already generated**.

**Pilot approval gate:** first show **two whole-phone Home compositions** using this family (not twenty unrelated hero variants) and pick a visual winner. Can instead reduce to 1 detailed concept + a modest alternate if that produces clearer review. Show first viewport at 360dp portrait and an in-game screenshot side by side. The user must recognize GridMates as a puzzle game without reading an introductory slogan.

## Second batch — only AFTER the pilot look wins

| Order | Filename | Master size | Purpose |
|---|---|---:|---|
| 9 | \`gm_home_chapter_ocean_v1.webp\` | 1536 × 768 | Ocean sibling of the approved Home stage |
| 10 | \`gm_home_chapter_garden_v1.webp\` | 1536 × 768 | Garden sibling |
| 11 | \`gm_reward_medallion_base_v1.webp\` | 512 × 512 | One shared completion/achievement medallion frame, overlays native text/state |
| 12 | \`gm_album_divider_v1.webp\` | 1024 × 128 | Very restrained chapter edge/label ornament reused on Collection and results |

Seasonal themes like Fall, Winter, Jungle, Castle are **future packs**, not prerequisites. Do not invent separate reward currencies, artificial Daily theme rotation, new collectible IDs, or additional stores at this stage.

## Replacement map — from source code to actual screens

### H1: Home (first code integration PR, after art approval)
- Keep the existing \`HomeScreen.kt\` callbacks and saved Solo/Daily/nearby routes. Replace the procedural \`LivingBookIllustration\` hero with a responsive **art-backed chapter stage**, rendered below Compose controls using Image and a gradient/contrast scrim where required. Art may frame a live saved progress snippet or mini preview, but never pretend a hardcoded sample grid is the player's puzzle.
- Make **Continue** (when unfinished) or **Play / New puzzle** the dominant action with one tap; keep difficulty selection predictable. Don't make players enter a decorative world before Sudoku.
- Keep beloved Daily as immediately accessible compact gold strip, including 7-day true history; avoid pushing it below the fold on a typical portrait phone. Single visible screen priority: primary game action then Daily. Nearby Co-op/Versus can remain a compact mode row or its dedicated setup view.
- Feature a **small real equipped portrait / unlocked chapter teaser** via existing \`AnimalPortrait\`; no new chapter unlock system. Avoid a fifth giant card.
- Use less visual wrapping: one main illustrated stage, one Daily ribbon, then lightweight secondary modes. Remove the current repetitive green rectangle-on-cream stack rather than decorating each card.
- UI text, controls, Sudoku numerals, hit boxes and progress indicators stay native Compose for accessibility and localization.

### H2: Local modes
- Reuse \`gm_mode_coop_v1\` and \`gm_mode_versus_v1\` as small identity art, NOT large scenery above Host/Join.
- Keep "Start a game" / "Join a game", chosen difficulty, same-Wi-Fi explanation, optional code, and Back-to-Solo actions visible at 320–430dp. Never imply online matchmaking.

### H3: Collection / Profile
- Existing \`CosmeticUi.kt\` album chapters and \`AnimalPortraits.kt\` assets stay authoritative; frame chapter headers with approved backdrops/divider later, no new 22 portraits.
- \`JournalScreen.kt\` Player Journal may reuse cloth texture and actual equipped portrait. Keep readable stats and no invented completions.
- Make outfit/board/style previews reflect true ownership and selected appearance; do not turn preview imagery into buttons.

### H4: Puzzle + results
- Preserve the readable playable board and functional controls. Improve selection/notes/hint feedback in a distinct follow-up PR, not while replacing Home art.
- Results: reuse approved medallion, stamps, existing achievement records and earned cosmetic data. Celebrate *only actual* completion and actual unlocks, with restrained 150–250ms animation and optional soft haptic, respecting reduced motion/feedback settings.

## Asset production contract

Use \`design/ui-art/v1/\` for layered/raw originals, approved contact sheet, provenance notes and the source prompts/revisions. Package optimized versions in \`app/src/main/res/drawable-nodpi/\` (or named density-independent resources only where technically warranted). Create \`design/ui-art/v1/asset-manifest.json\` with:
- immutable resource filename, pixel dimensions, alpha handling, asset purpose, authored style version
- source/original path and SHA-256; packaged path and SHA-256
- origin (original generated / hand-edited / composited), commercial usage/licensing check, owner review approval status
- where consumed and safe-crop/focal area requirements

No externally copied game screenshots, internet stock sprites, unlicensed fonts, watermarking or public-domain assumptions. Original AI output should be visually checked for malformed details. Export with clean alpha/no white halos and verify at final on-device rendered sizes.

**Performance budget (starting constraints to measure, not promises):** each visible chapter backdrop ideally <= 250 KB compressed, small mode motifs <= 80 KB each, textures <= 60 KB each, pilot total <= 1.2 MB compressed. If visual quality requires exceeding a number, measure startup memory, decode latency and APK delta first rather than silently sacrificing detail.

## Implementation order / PR structure

1. **Source baseline (no visual merge):** keep #80 -> #81 -> #82 stacked and draft. Record last green owner-preview run and screen screenshots, and make sure new art work starts from latest tested \`design/living-book-player-identity\` without rewriting Beta 1.
2. **A1 — Asset atlas only:** create the eight approved pilot master assets + manifest/contact sheet. No UI, no character replacement, no Save/Network changes. Review every asset with the original Owl, Fox and Ocean/Garden portraits.
3. **A2 — Two whole-screen Home comps:** visually evaluate the revised composition against the actual Nov 18 phone screenshot. Pick one; record decision, cropping, intended motion, what must remain visible on narrow screens.
4. **A3 — Home implementation:** replace the procedural hero, add active portrait/visual chapter variant, keep real Solo + Daily mechanics. Separate draft PR, signed-debug isolated Android package, screenshot diff.
5. **A4 — Daily + Nearby illustration:** integrate mode motifs and Daily seal carefully, without adding scroll or extra taps. Dedicated same-Wi-Fi setup screen remains direct.
6. **A5 — Album and Journal integration:** use approved chapter edges/texture, tiny collection teaser and existing saved portraits; optimize long catalog scrolling if necessary, preserve IDs and all locked item discovery.
7. **A6 — Finish/result moment:** medallion, existing stats/achievements, very restrained haptic/animation, optional animation-disabled path.
8. **A7 — Release-quality acceptance:** visual signoff on actual phone; regression and install-over tests; no public Beta 2 release unless original production signing key is recovered into an approved secure CI flow.

## Mandatory acceptance checklist

- On first launch, the screen is recognizable as **GridMates Sudoku** and looks from the same art family as the existing animal Collection. A new player identifies Play/New Puzzle instantly.
- On a 320–430dp portrait with system font at 100% and 200%, Play/Continue and Daily are accessible; no clipped title; navigate Co-op/Versus and return without hunting.
- Real resumed Solo progress is displayed honestly; no hard-coded "current board" or fabricated cosmetics/achievements. Daily's historical completion indicators are real.
- Art is decorative only; TalkBack and touch targets remain available, no fake pixel buttons.
- Tests cover first visit, active game, completed game, portrait change, locked/unlocked items, LAN room controls, no progress loss. Save/room protocols unchanged.
- Original PNG assets + manifests remain intact, and all new assets have sources and hashes; APK size and load time are checked.
- On-device visual approval compares whole screens, not only compilation and emulator test green.
- **Production Beta 1 to Beta 2 signing remains a separate unresolved release gate.** Never uninstall/overwrite to force a release.

## What NOT to do next

No more polishing a large code-drawn book; no unreviewed broad background pack; no new mascot art incompatible with the 22 existing portraits; no Unity/Godot migration; no clouds/accounts/economy redesign; no random animated confetti; no replacing the current public APK to test visuals.

**Immediate next deliverable:** the eight-piece coordinated **pilot art batch and a complete phone-sized Home composition** showing those assets together, with a reviewable contact sheet and versioned manifest. Only after that should anyone code another full Home redesign.
