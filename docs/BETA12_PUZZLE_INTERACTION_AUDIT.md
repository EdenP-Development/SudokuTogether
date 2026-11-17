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
- Notes on: announces `Toggle pencil mark N` instead.
- No changes to number placement, note masks, saved records, scoring, Coach, multiplayer sync, screen layouts, or cosmetics.
- `SudokuKeypadAccessibilityTest` uses a valid Solo grid, switches Notes on/off, asserts the corresponding descriptions, and confirms tapping still sends the same number action.

## Remaining Phase B acceptance, not claimed completed

- [ ] Physical 9×9 text/notes/selection/keypad readability on narrow Android phones and at increased system font size
- [ ] Check generated difficulty bands for human playtime/technique distinctions without misleading claims of empirical calibration
- [ ] Try actual Coach explanations and proof/error highlights with a new player
- [ ] Confirm touch feedback, cell selection, Undo and Notes inputs feel comfortable in long sessions
- [ ] Run full source, Android and emulator QA for any changes; review or revert if regressions occur

The authoring plan is [Beta 1.2 refinement execution](BETA12_REFINEMENT_EXECUTION_PLAN.md), which will merge separately through Phase A PR #55.
