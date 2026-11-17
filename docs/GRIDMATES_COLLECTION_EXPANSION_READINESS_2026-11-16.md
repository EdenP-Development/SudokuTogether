# GridMates: Sudoku — Collection expansion readiness and next slices

**Reviewed:** November 16, 2026 • **Basis:** canonical `main` after Beta 1.1 preview • **Status:** source-verified audit + proposed roadmap; **no new cosmetics, reward currency, redemptions or paid mechanics are approved or shipped by this document**.

This document supplements (rather than supersedes) [board/font expansion](GRIDMATES_BOARD_THEMES_AND_NUMBER_FONTS_EXPANSION.md), [collections/Daily concepts](GRIDMATES_COLLECTIONS_DAILY_REWARDS_AND_FONTS_DESIGN.md), [simple gift codes](GRIDMATES_GIFT_CODES_SIMPLE_DESIGN.md), and [Co-op shared boards](GRIDMATES_SHARED_COOP_BOARD_THEMES_DESIGN.md). Older documents describe earlier code: the **current implementation** below takes precedence for shipped status.

## Current catalog — verified from source

| Group | Existing inventory | Starter examples | Implementation |
| --- | --- | --- | --- |
| Portraits | 12 Woodland, 6 Ocean, 4 Garden = **22** | Owl, Fox, Octopus, Honeybee | Curated IDs in `core/.../Cosmetics.kt`; bundled drawable assets and mapping in `app/.../AnimalPortraits.kt` |
| Emblems | **6** basic symbols | Gridkeeper | `Cosmetics.emblems` and `emblemSymbol` |
| Boards | **4**: Classic, Linen, Midnight, Circuit | Classic | `Cosmetics.boards`, `CosmeticUi.boardFillFor`, `PlayScreen` board rules and Co-op shared-board priority |
| Number inks | **8**: Forest, Cobalt, Amber, Rose, Violet, Lagoon, Clay, Slate | Forest, Lagoon | `Cosmetics.inks` and light/dark `inkFor` |
| Number fonts | **5**: Clear, Bookish, Terminal, Rounded, Pencil | Clear | `Cosmetics.fonts` and bundled local `res/font` fonts via `NumberAppearance.fontFor` |

**45 curated cosmetic entries total.** The current Collection screen separates Player Icons, Boards and Numbers (Colors/Fonts), with filters for Basic/Woodland/Ocean/Garden. Locked styles can be previewed; selected styles persist in local preferences. The same progress rule controls preview and equip eligibility. No online shop, downloadable font, remote image, purchase, coins/stamps, code-redemption entitlement or separate permanent ownership ledger exists in current code.

The historical board/font expansion proposal listed **Pencil** and **Rounded** as pilots. Both **already exist** on `main`; don't commission them again. The older shared-Co-op-board proposal is **already implemented** for current board IDs; each player's individual number ink/font remains separate. Older notes calling these future features should be interpreted by their dates.

## Confirmed architecture constraints

1. **Offline and local first.** Persisted match records and player preferences are device-local. `Appearance` only transports known curated IDs over LAN. New IDs must exist on both phones; an old client should not be assumed compatible.
2. **Milestones today:** finish counts Solo, Daily and Co-op solves plus Versus wins; other milestones count Solo solves, Co-op solves or Versus wins. Records are deduplicated by match ID. New content should be attainable for Solo players; special multiplayer unlocks can be optional extras.
3. **Performance:** Collection formerly scanned saved records for **each** of 45 items. [PR #49](https://github.com/PlayerEden/SudokuTogether/pull/49) is now **merged**: the Collection screen computes deduplicated totals once per profile for every item, without changing existing unlocks. [Core CI](https://github.com/PlayerEden/SudokuTogether/actions/runs/44006168991) and [Android compile/unit tests](https://github.com/PlayerEden/SudokuTogether/actions/runs/44006463041) passed.
4. **Co-op board priority:** shared-board rank still follows curated board order (not entitlement or player merit), and merged [PR #49](https://github.com/PlayerEden/SudokuTogether/pull/49) now derives that priority directly from the board catalog, avoiding a second hard-coded list.
5. **Styles are rendering, not rules.** Puzzle correctness, difficulty, Coach, scoring, answer visibility and clue distinction must not change with cosmetics. Validate contrast against normal, selected, peer, error and dark-board states, with hints/notes and large text. A decorative font must not make digits ambiguous.
6. **Font consistency:** Every new number font must be packaged as a fixed licensed asset inside the APK and mapped deliberately in `fontFor`. Current `Classic number display` can suppress selected entry fonts/inks on that device without unequipping them.
7. **Record retention:** `MAX_RECORDS = 10,000`; unlocks currently derive from the stored records. Existing threshold targets are modest, so this is not an immediate customer-facing defect, but **new long-lived entitlements/weekly claims cannot depend solely on a prunable recent record list**.
8. **Persistence and migration:** Adding permanent claimed rewards or gift ownership requires backward-compatible save format defaults, validated import/export and atomic save/restore tests. Never use a single mutable counter without migration/rollback and duplicate-claim protection.
9. **UI scalability:** current Collection renders a full `Column` of `CosmeticGrid` rows rather than a lazy virtualized grid. That's fine for the existing catalog but should be revisited if collections multiply substantially. A swipeable family row can show a clipped preceding tab (owner screenshot); consider a snap/selected-visible affordance when doing the next gallery UX batch.
10. **QA and release:** current Beta 1.1 isolated preview installs as a separate package. A real install-over QA release is **still blocked by the absent original QA signing secret**, and no newer two-phone Versus/reconnect test has been performed. Do not claim those gates passed.

## Recommended expansion sequence (small, independently verifiable batches)

### A. Safe catalog foundation — first

Keep the old **IDs, milestone meanings, visual assets, save compatibility and network representation** unchanged while consolidating unlock totals, Co-op priority and catalog tests. CI should explicitly reject duplicate cosmetic IDs and missing board priority definitions. Later, centralize the complete board palette (background, grid, highlighting, border, clue/entry contrast) rather than adding one-off hard-coded dark-board conditions.

**Acceptance:** all old inventory still available at the same milestones; duplicate games never earn two rewards; a new board cannot be omitted from Co-op priority; no new APK needed just for this refactor.

### B. Pilot a *small* free style pack

*Candidate designs only—owner has not approved final colors, art, unlock costs or font licenses.*

- **Boards:** Parchment (warm paper distinct from Linen), Ocean (cool light board), Graphite (clear neutral dark board). Start with **two** that are visibly distinct, and hold the third for a later pass.
- **Fonts:** Typewriter and Editorial are *new candidates*; Rounded and Pencil are **already implemented**. Select actual redistributable font faces and show digits **123456789** and pencil notes at real puzzle size before shipping.
- **Number colors:** develop **two** contrast-tested candidate inks (e.g., restrained Sepia and Indigo), accepting a candidate only if sufficiently different from Clay/Amber and Cobalt/Violet. Don't reserve final saved IDs until the colors pass visibility/contrast checks.
- **Portraits:** extend existing Garden/Ocean before inventing another large family; preserve source art proportions, transparent framing, file naming and Android packaging. Future set names are design choices, not shipped categories.

Propose several **Solo-accessible** unlock paths in the first batch. One optional Co-op/Versus achievement-style item is fine, but players without a second phone should still have variety. No advantage in Sudoku or hints, no account, no purchase. Don't create dozens of assets before testing a few representative cards, boards and glyphs.

**Acceptance:** exact bundled/allowlisted assets, distinct realistic previews, legible 9×9 cell rendering, selected/peer/error states, 200% text, save/equip/restore, catalog and appearance round-trip tests, and a measured APK-size check.

### C. Permanent earned entitlement and unlock mechanics (before time-limited gifts)

Design **monotonic lifetime achievement totals** and a separate **owned entitlement set** (needed for gifts/weekly item claims). Derive migration baselines from historic current records where possible and preserve all existing users' unlocked styles. Avoid relocking an already equipped cosmetic if old match history is pruned. Differentiate **earned by achievement**, **owned by claim**, and **equipped for appearance**.

**Acceptance:** migration from old profile and backup JSON; atomic claim + claim-ID persistence; duplicate match/redeem handling; restore/rollback after interrupted write; no loss of player ID/Solo/Daily/room data; no cross-mode reward loophole. Keep all IDs curated. Do not use this as an excuse to introduce an online economy.

### D. Daily/weekly cosmetic reward experiment — only after C

The prior design suggested special Daily rewards with optional **weekly** rotation/recycling to avoid endless content production. Start with a **small free weekly earned cosmetic or claim** rather than compulsory seven-day streaks. Missing a day should not permanently punish occasional players. If points/stamps are considered, define the smallest transparent rules and how repeat claims/time-zone changes behave **before** writing a wallet. Keep standard Sudoku and Coach fully accessible.

### E. Simple offline gift codes — later optional mechanic

Per the [owner's existing gift-code design](GRIDMATES_GIFT_CODES_SIMPLE_DESIGN.md), local reusable codes can grant **fixed, bundled, curated free cosmetics**, once per local profile. Treat these as fun gifts, not secure or globally unique licenses. Require the owned-entitlement infrastructure in C; avoid shipping private surprise artwork/phrases before owner approval. No payments, gacha, online verification or account requirement.

## Release checklist for each content increment

- Keep old IDs and appearance fallbacks intact; update catalog, bundled assets/renderer, previews, license records, lock requirements and unit/UI tests together.
- Confirm 81-cell readability for notes, given digits, user entries, selection, errors, light/dark boards and authored Co-op entries; screen-reader labels and large-text behavior remain usable.
- Make new content accessible without forcing online accounts or multiplayer; confirm no duplicate unlocks or unearned equip after restore.
- Re-run core CI and relevant Android Collection/font/board tests. Later obtain owner screenshots on an isolated QA preview before a signed release.
- Preserve old app data: a new **true QA update** requires the original signer and a higher version code. Current isolated previews cannot prove install-over continuity.

**Next engineering checkpoint:** prepare a centralized board palette/contrast definition plus tests ensuring every curated board/ink/font maps to a distinct valid render path; run the relevant Collection Android UI tests when the new visual content exists. **Next design checkpoint:** pick two board candidates and two *new* font samples for side-by-side owner review; avoid bulk content before their readability is approved. Merged [PR #49](https://github.com/PlayerEden/SudokuTogether/pull/49) already passed core and Android compilation/unit checks.
