# GridMates: Sudoku — Premium Mobile Puzzle UI Research & Art-Direction Plan

**Date:** 2026-11-17 US local / 2026-11-18 GitHub UTC  
**Status:** Research-backed design proposal, **not approved visuals or a feature mandate**. No code changes, signers, save files, player records, cosmetics, or release artifacts.  
**Source material:** Owner's actual Android screenshot reviews and current `HomeScreen.kt`, `GameTheme.kt`, `JournalScreen.kt`; see `GRIDMATES_BETA2_OWNER_PHONE_VISUAL_FEEDBACK_2026-11-17.md`, `GRIDMATES_VISUAL_IDENTITY_BRIEF.md`, and `CONTINUE_HERE.md`.

## Owner's current criticism and the design problem

Current Beta 2 Home is a top app logo, a separate slogan/hero card, three mode-choice tiles, a large dark configuration panel, a Daily card, and a persistent bottom navigation bar. On the owner's phone the entire Daily activity tracker can fall below the fold. The hero looks more intentional but remains another stacked card. Profile/Collection/Settings use familiar app form/card/list patterns, so the combined impression is **a nicely designed productivity app**, not a distinctive puzzle game.

**The owner likes the Daily card and its recent-seven-days indicator**. The palette, readable Sudoku board, small illustrated collectible portraits and permanent-ownership model are worth protecting. The Profile achievement placeholder bug is **already fixed on main by PR #75**. Do not lose this context by asking the owner to repeat it.

Our target is premium *casual puzzle-game* craftsmanship, not a fake AAA marketing label, more paywalls, or 3D mechanics.

## Comparative reference research (screenshots are design references, not assets to copy)

| Reference | Relevant finding | Adapt for GridMates | Avoid |
|---|---|---|---|
| **Good Sudoku** (Zach Gage / Jack Schlesinger) | Large distinctive logo, immediately recognizable Play/Resume and Daily hierarchy; contextual education without incessant interruption | One dominant action; calm teaching that appears when needed | Cloning yellow/black typography or assuming its iOS screenshots represent the 2026 Play listing |
| **Two Dots** | Simple puzzle gameplay supported by a coordinated illustrative world and progression map; visual art, motion, product and UI direction were explicit production roles | Curated art world and progress illustration tying Home, completion and Collections together | Copying its map economy, monetization, characters, or art assets |
| **Monument Valley** | Scene-like menu composition and striking art/space make navigation feel like being inside a game | Background/world cues and transitions that make menu feel spatial and intentional | Overbuilding 3D environments or compromising quick Sudoku entry |
| **NYT Games** | Daily game rituals and distinct tiles work well in a multi-game hub | Strong Daily ritual and recognizable puzzle metadata | Copying a feed of cards into a *single* Sudoku title |
| **Knotwords** | Strong typography, focused menu, and gentle tutorialization without needless visual busyness | Clearly selected puzzle paths; explanatory content near its controls | Assuming minimalism alone provides polish |

Research sources:
- Good Sudoku screenshots/design: https://www.macstories.net/reviews/game-day-good-sudoku/ and https://www.playgoodsudoku.com/
- Knotwords/Good Sudoku design analysis: https://mgmarlow.com/words/2023-01-15-case-study-zach-gage/
- Two Dots visual-design case study (team and illustration/motion disciplines): https://www.benrudlin.com/work/twodots
- GDC Playdots minimalist UI talk (design simplicity is hard to execute well): https://www.gdcvault.com/play/1024288/Dots-That-Go-for-Walks
- Monument Valley 3 Google Play: https://play.google.com/store/apps/details?id=com.ustwogames.mv3
- Two Dots Google Play: https://play.google.com/store/apps/details?id=com.weplaydots.twodotsandroid
- NYT Games Google Play: https://play.google.com/store/apps/details?id=com.nytimes.crossword
- Game-menu clarity and obvious primary CTA: https://developer.apple.com/videos/play/wwdc2017/811/
- Google Android game/app UX guidance: https://developer.android.com/quality/user-experience

## Design direction: The Living Puzzle Book

**One continuous gamefront composition, not three equal-weight panels on a scrollable application page.**

Think of opening a cherished puzzle book on a small table: modern, illustrated, calm, warm, and tactile, with the next unfinished puzzle waiting. Not a literal heavy skeuomorphic desk or a static picture where baked-in buttons cannot respond.

### Target first-screen hierarchy, top to bottom

1. **Compact identity bar:** small GridMates wordmark + avatar/help. Don't spend a quarter of the viewport repeating the product name or advertising tagline.
2. **Main scene and dominant action** (the real centerpiece): large open-book/page/board scene, a live decorative nine-grid/progress vignette, and a clearly oversized **Continue** or **Play Sudoku** button. If an unfinished Solo exists, show its genuine difficulty, player-entered cell progress, and elapsed time. If none, offer obvious new-game start and only then difficulty. Use real state; don't fabricate puzzle progress or completed pages.
3. **Daily ritual ribbon:** retain the gold Daily status and seven-day tracker. Display within the initial viewport on a typical phone where feasible; when space is tight, use a compact variant with an accessible expanded view.
4. **Secondary game modes:** co-op "Solve together" and versus "Race a friend" as two smaller illustrated tiles (honestly labeled **Same Wi-Fi**), not equal in scale to the main Solo CTA. Keep accessible one-tap Host/Join once selected and default low-friction joining.
5. **Low-priority navigation**: Profile, Collection, Settings accessible, but visually subordinate. Do not move save/export or accessibility controls into cryptic inaccessible menus.

On smaller screens or 200% font: prioritize Play/Continue and Daily, allow the rest to scroll without clipping; don't shrink the board in the actual Play screen. The current Home selection state and all callbacks must be preserved or deliberately migrated.

### Visual tokens and assets

Preserve existing forest/cream/gold palette as a starting point (`Forest #214F3F`, `Cream #F4F3EB`, `Paper #FFFDF8`, `Gold #8A6729`). Express **three material families** intentionally: smooth parchment/paper with subtle grain; warm dark-green book binding or frame; restrained brass/gold edge highlights. Avoid gratuitous gradients, thick uniform card borders, or flat CTA boxes everywhere.

Build one **cohesive asset family** rather than one-off illustrations:
- Living book / central puzzle-page illustration and three responsive crops (portrait narrow, portrait normal, landscape/wide)
- 2 secondary mode motifs (co-op shared pencil/paired tiles; versus two mirrored grids)
- 1 full-page or card-safe subtle paper texture (low contrast and small memory cost)
- Achievement medallion/badge foundation and earn state treatment
- Background corner decoration and carefully restrained gold dividers
- Existing portrait gallery retained and extended only through original compatible art

**Never put functional copy, dynamically changing scores, puzzle digits, accessibility labels, or buttons inside flattened raster imagery.** Keep interactive layers in Compose. Export decorative assets in correctly sized WebP/vector where appropriate; keep original source provenance/licensing and a manifest.

### Motion, sound and touch (tasteful, optional)

- 100–180ms tactile selection feedback for mode tiles, consistent easing and pressed/selected states
- 180–280ms content transitions when switching modes or opening the puzzle; fade/slide/scale used purposefully, not all at once
- Subtle page settle/turn when entering an existing puzzle, but never delay access to play
- Gentle completion highlight/gold tick and optional original soft sound
- Honor touch-feedback preference, Android accessibility motion settings where feasible, sound-off environments and battery/performance constraints

Compose is sufficient for this scope; `AnimatedContent`, `AnimatedVisibility`, `animateContentSize`, `updateTransition`, `Canvas`, and original raster/vector backgrounds can produce premium presentation without moving the puzzle engine to another framework.

Sources: https://developer.android.com/develop/ui/compose/animation/quick-guide and https://developer.android.com/develop/ui/views/haptics/haptics-principles

## AI-assisted production pipeline (not "prompt one card, ship one card")

1. **Reference and critique**: keep owner's actual screenshots, cited non-copied competitor reference images and an explicit list of problems to solve. Use AI as a visual-analysis and exploratory art tool, *not* as a substitute for usability observation.
2. **Design bible**: freeze gamefront hierarchy, component names, spacing grid, type hierarchy, line weight, art palette, icons and the material surface system. Reuse the same family across Home, Profile, Collection, Daily and results.
3. **Whole-screen concepts**: make 2–3 visually distinct *complete Home screen* compositions before coding. Compare by legibility, first-action clarity, Daily visibility, and recognition as Sudoku; pick one direction. Do not implement a collage of independent concepts.
4. **Batch original assets with constraints**: generate coherent sprite, texture and illustration packs using fixed references/style directives; rework/crop/curate by a human before import. Unity's 2026 UI-generator example is relevant as a *workflow* (AI sprites + textures + layout assistant), **not a recommendation to port this Compose app to Unity**. Reference: https://unity.com/blog/unity-ai-ui-generator
5. **Structured reusable Compose kit**: gamefront/hero, primary action, secondary mode tiles, daily compact/expanded card, progress marker, achievement badge, responsive nav; centralized colors, dimensions, typography, press/disabled/selected states.
6. **AI/code review loop**: update one controlled design branch, compile, run unit/UI/accessibility tests, capture full-screen emulator snapshots at several widths/font scales, compare to approved visual "goldens," then review the actual owner-phone APK. Humans decide whether visual changes feel better.
7. **Safety gate**: never replace the production install/signature or rewrite local save formats for a visual change. Keep PR #76 draft until the next direction is physically reviewed.

Figma's AI guidance similarly recommends higher-order reusable components, semantic names, auto layout, variants and color/typography tokens rather than expecting AI to improvise consistent UI from primitive blocks: https://help.figma.com/hc/en-us/articles/38978644498199-AI-workflows-collection-Refine-your-components-and-variables-for-the-Figma-agent

Android recommends visual regression screenshot tests with reference/golden images: https://developer.android.com/training/testing/ui-tests/screenshot

## Prioritized build plan and acceptance gates

**Phase A — Home scene prototype:** Build one standout, intentionally composed gamefront **before** changing other screens. Keep `PR #76` open/draft as the baseline, not the final visual commitment. Use its verified isolated Home Review APK for before/after comparison. No gameplay changes.

**Phase B — Whole-game visual kit:** Apply approved button, depth, edge, color, typography, icon and motion tokens to Profile, Collection and Achievements without changing unlock logic. Preserve earned 50-item catalog and any old ownership records. Simplify long gallery lists while keeping locked item discovery.

**Phase C — Play/Hints:** Optimize keypad 5+4 alignment, spacing near New Puzzle, Hint dialog hierarchy, assistance explanations and solved/reward moments. Preserve legible board size and scoring/Assisted semantics.

**Phase D — QA:** Phone review of first launch, resumed Solo, Daily, toggled mode/Join flow, profile cosmetics, settings/progress backup, landscape, 320–430dp widths and 200% fonts, accessibility focus. Automated tests *and* aesthetic sign-off. Record real screen evidence, not only a green build result.

**Publish gating:** Production Beta 1 → Beta 2 update still needs the original production signing keystore and safe install-over test. Design work must not replace public APK identity or jeopardize existing players' saves.

## Evidence-to-decision rules

- **Primary action test:** An unfamiliar player can immediately identify how to start or resume Sudoku.
- **First viewport test:** An opening glance looks like a distinct game world, not app settings. Keep Daily findable without hunting; preserve overflow access on smaller screens.
- **Consistency test:** No random asset families, inconsistent icon weights, mismatched corner treatments, placeholder text, or ambiguous button states.
- **Performance test:** No noticeable interaction lag or heavyweight loading sequence in routine navigation.
- **Continuity test:** Existing saved Solo/Daily puzzles, records, settings, and earned cosmetics are unchanged.

**Decision:** Research favors a large **living Sudoku book/board as the hero**, real progress as a visual feature, an accessible **Play/Continue** primary action, the beloved Daily ritual card, and local multiplayer as meaningful secondary modes—rather than further enlarging the current stack of generic cards.
