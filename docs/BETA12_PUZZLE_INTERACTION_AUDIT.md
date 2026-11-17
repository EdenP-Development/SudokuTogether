# Beta 1.2 Phase B — Puzzle interaction and Coach source audit

**Scope:** Existing gameplay experience, without additional mechanics or content.

## Source-verified observations

- `SudokuBoard` exposes per-cell row/column, clue/entry/notes, selected, conflict, and Coach proof semantics. A physical TalkBack review is still required; this is **not** an accessibility certification.
- `SudokuInputDock` provides a minimum 48dp height for number keys, responsive height for larger font scales, and at least 52dp for Notes/Erase/Undo/Hint tools.
- `PlayScreen` uses bounded board measurement and scrollable short-window layouts. Existing `CompactPlayLayoutTest` covers a 200% system font scale in landscape and checks keypad reachability.
- `GameViewModel.checkWork()` selects the identified incorrect entry, and the existing `EntryCheckUiTest` verifies Erase/Undo/resume behavior. Avoid changing this flow without a concrete regression.
- `CoachPracticeDialog` contains six existing lessons. `CoachProofUiTest` checks deduction navigation, proof descriptions and delayed reveal. Human usability of long explanations still needs phone testing.
- **Confirmed semantics mismatch**: the shared number keypad previously announced `Number N, X remaining` when Notes mode was active. In that mode the same button actually toggles a pencil mark; the remaining-placed-digits count is not relevant to that operation.

## Current tiny refinement

- Notes off: screen-reader description remains `Number N, X remaining`.
- Notes on (initial Phase B): announces `Toggle pencil mark N` instead. The Phase B follow-up announces `Add pencil mark N` or `Remove pencil mark N` based on the selected square's note mask; placed-digit remaining counts are hidden while Notes is active.
- **Observed invalid input path:** both `PersonalPlay.edit` and `GameSession` reject pencil marks on an already-filled square, but the input dock used to keep its number keys enabled there. In Notes mode those keys are now disabled for filled squares and the board caption says to erase the entry first. Number replacement remains possible with Notes off.
- No changes to number placement, note masks, saved records, scoring, Coach, multiplayer sync, screen layouts, or cosmetics.
- `SudokuKeypadAccessibilityTest` uses a valid Solo grid, switches Notes on/off, asserts the corresponding descriptions, and confirms tapping still sends the same number action.

## Remaining Phase B acceptance, not claimed completed

- [ ] Physical 9×9 text/notes/selection/keypad readability on narrow Android phones and at increased system font size
- [ ] Check generated difficulty bands for human playtime/technique distinctions without misleading claims of empirical calibration
- [ ] Try actual Coach explanations and proof/error highlights with a new player
- [ ] Confirm touch feedback, cell selection, Undo and Notes inputs feel comfortable in long sessions
- [ ] Run full source, Android and emulator QA for any changes; review or revert if regressions occur

The authoring plan is [Beta 1.2 refinement execution](BETA12_REFINEMENT_EXECUTION_PLAN.md), which will merge separately through Phase A PR #55.

## Related Phase C legibility check (do not conflate with this keypad fix)

A source-color relative-luminance calculation found existing **Rose** and **Violet** dark-board inks at approximately **4.14:1** and **4.19:1** respectively against the older partner-selection highlight, below the usual 4.5:1 text target. Other existing combinations were deliberately accepted under a lower legacy floor; the colors are unchanged by this PR. These ratios are **computed**, not owner phone or TalkBack evidence. Physically inspect tiny notes and related selected/partner highlights before choosing a targeted contrast correction in Phase C. Do not recolor all existing cosmetic palettes merely to close a checklist.

**Phase B follow-up QA:** [PR #57](https://github.com/PlayerEden/SudokuTogether/pull/57) focused API 36 emulator [run 44117992119](https://github.com/PlayerEden/SudokuTogether/actions/runs/44117992119) passed core, Android units and keypad UI assertions on the source revision `f5daaf8`. No phone APK or physical review was performed.

**Phase C source follow-up:** [PR #58](https://github.com/PlayerEden/SudokuTogether/pull/58) guards the tiny pencil glyph color on dark-board highlights and the crossed-off Coach note on older light-board highlights. Its automated source/Coach emulator run [44120831973](https://github.com/PlayerEden/SudokuTogether/actions/runs/44120831973) passed. Cosmetic palette tokens, normal full-size digits, and all saved game state remain unchanged. **This does not certify every large digit/theme combination or replace physical phone signoff.** See [Phase C QA details](BETA12_C_PENCIL_CONTRAST_QA.md).
