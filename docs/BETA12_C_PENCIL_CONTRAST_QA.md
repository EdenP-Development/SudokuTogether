# Beta 1.2 Phase C — Small pencil-mark contrast refinement

**Scope:** rendering only; keep all cosmetic palettes, unlocks and saved games unchanged.

## Reproduced source issue

- On the **original dark boards**, Rose and Violet inks can fall below 4.5:1 contrast for tiny notes on partner/selected highlights: approximately **4.14:1** and **4.19:1** on the legacy partner highlight.
- On the **original light boards**, the existing Coach crossed-off note color `Brick` is around **4.36:1** on the selected-cell highlight and **4.46:1** on the partner highlight.
- These are **computed from registered Compose palette colors**, not a physical-phone or TalkBack certification. Existing tests guard full-sized ink on new themes; those tests do not alone guarantee pencil-mark readability on all seven board styles.

## Narrow rendering solution

- Keep all **7 board palettes**, **10 ink palettes**, values, keys, awards, records and cosmetic IDs byte-for-byte unchanged.
- When a 3×3 pencil glyph already has >=4.52:1, draw it with the original color. Otherwise blend only that glyph toward the board's neutral clue ink to the minimal fraction that exceeds 4.52:1. A contrast-safe black/white fallback protects against future unusual board highlights.
- Standard pencil marks use the existing muted gray on light boards and authored ink on dark boards; neither changes unless visibility requires it.
- Coach's crossed-off small candidate mark uses the existing red unless the current highlight makes it too faint. Preserve the strikethrough and its source/target outlines.
- Gameplay, Coach proof and practice boards all share the same `SudokuBoard` rendering, so one adjustment covers all contexts. Full-size Sudoku digits, conflicts, board colors, Coach answers and puzzle logic do not change.

## Acceptance checks

- [ ] `BoardPilotPaletteTest`: test all 7 boards × all 10 inks across background, selected, partner, matching, related and conflict surfaces; all rendered pencil marks >=4.5:1
- [ ] Coach eliminated-candidate notes >=4.5:1 across the same board states; original palette values remain unchanged
- [ ] Existing core, Android compile and Android unit tests pass
- [ ] Existing `CoachProofUiTest` passes on API 36 emulator (deduction order and hidden answers remain intact)
- [ ] Physical 320dp/360dp/430dp checks for tiny pencil marks, Coach strikethrough and font families before final artwork signoff

**No APK in this PR; no install-over guarantee.** If Android tests fail, fix the regression on the feature branch and do not merge prematurely. See [the overall refinement plan](BETA12_REFINEMENT_EXECUTION_PLAN.md) and [the gameplay audit](BETA12_PUZZLE_INTERACTION_AUDIT.md).
