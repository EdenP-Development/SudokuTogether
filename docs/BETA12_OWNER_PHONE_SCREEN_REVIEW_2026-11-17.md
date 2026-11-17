# GridMates Sudoku — Beta 1.2 owner-phone screenshot review (November 17, 2026)

**Evidence:** Seven screenshots from the isolated Beta 1.2 Android review installation, provided by the owner. This is **visual observation only**, not a complete playtest or accessibility/QA sign-off. Do not copy phone status-bar data into any development record.

## Visible results

- **Home:** Solo/Co-op/Versus options are legible. The active Easy puzzle displays editable-squares progress, elapsed time, Continue, separate New puzzle difficulty choices and a New puzzle action. A Daily card is visible. Large blank space below the Daily card on the owner's tall phone is acceptable breathing room unless a first-time user reports confusion.
- **Profile:** Owl portrait, edit-name action, Change icon navigation, a sensible not-yet-played empty state, achievements, statistics and history cards appear consistent and readable. This confirms a normal-scale appearance, **not** 200% font/talkback coverage.
- **Collection — Player icons:** The five icon families remain in one row, Garden is partly visible at the edge and the new 'Swipe for more icon sets ↔' cue appears. The count is 5/28 available, consistent with the fresh isolated profile. The Owl equipped card is distinct.
- **Collection — Boards:** Seven tiles are visible, including Parchment, Ocean and Graphite. One unlocked Classic board; all others locked in the fresh preview. Small 3×3 gallery swatches show different palettes, but **do not establish** full 9×9 selected/partner/pencil readability.
- **Collection — Numbers:** Ten colors and five fonts are organized into separate choices. The available counts reflect fresh progress. The 59 samples show basic differences; more digits and a full board should be reviewed for small Pencil/Terminal font readability.
- **Settings:** Classic number display, touch feedback and clean-pencil-mark toggles are visible with explanatory descriptions. Backup button begins near the bottom of a screenshot; the page is implemented as a vertically scrolling Column. This is **not sufficient evidence** of an unreachable or hidden button.
- **Navigation:** Four-tab bottom navigation stays visible and the selected tab is distinct.

## Confirmed low-risk UX mismatch

The banner immediately after the Collection category tabs always showed '**Owl · Woodland / Style preview**', even on Boards and Numbers. Although the full combined-style dialog was intentional, this banner did not identify the active component being browsed. The owner screenshots show this repetition.

**Phase C phone-review refinement:** Keep the same full-style dialog but make the **banner thumbnail, title and subtitle follow the selected category**:
- Player icons: player portrait and icon identity
- Boards: current board swatch, its name, current-board preview label
- Numbers: current number ink/font sample and their names; state when Classic number display overrides the equipped number appearance

Preserve all game mechanics, cosmetic catalogs, earn conditions, selection and layout. Cover the icon/board/number tabs and Classic number display in Compose UI regression tests.

## Outstanding observations needed (not claiming problems)

- Real playing-board screenshot with number entry, pencil marks, selected and related highlights
- Coach 'Show why' full 9×9 proof view and crossed-off candidates
- Collection dialog preview for one board, one ink and one font
- Settings screen scrolled to the end, if backup/import reachability is in question
- Physical Co-op/Versus pairing, result and rematch remains separate from this visual review

This review complements [the refinement execution plan](BETA12_REFINEMENT_EXECUTION_PLAN.md), [the source-focused puzzle audit](BETA12_PUZZLE_INTERACTION_AUDIT.md) and [the Board/Coach contrast check](BETA12_C_PENCIL_CONTRAST_QA.md).
