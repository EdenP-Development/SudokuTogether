# Beta 2 — 990 board × ink × font compatibility quality gate

**Status: draft, isolated from production.** Branch `qa/beta2-79-style-compatibility` is based on the Android-16-green unified 79 catalog (#106) and keeps the original eight source-hash-verified Ocean/Woodland PNGs and all nine original font files.

## Why this audit exists
The 79-cosmetic release comprises 11 board themes × 10 number inks × 9 number fonts = **990 distinct equipped styles**, plus all six possible normal/highlight background states. Simple Collection-card screenshots do not guarantee puzzle usability when a square is selected, marked by a partner, conflicting, matching, related or showing pencil notes.

## Implemented code change
The Sudoku renderer previously drew `palette.conflictInk` on any cell containing a conflicting entered number. When such a cell was selected or partner-highlighted, those fills took precedence over the normal conflict-cell background, resulting in **4.36:1 and 4.46:1** Classic/Linen contrast ratios (below the 4.5:1 target), despite 4.55:1 against the normal conflict fill. The fix calls `conflictDigitInk(background,palette)`: adjust only the *rendered* conflicting foreground until it clears 4.5:1. Original board colors, clue colors, save IDs, selected board and other game rules are unchanged. An existing readable conflict color is not changed.

## Test coverage
- JVM test `Beta2StyleCompatibilityMatrixTest`: **all 990 equipped combinations**, 5,940 background/ink/font state checks, clue contrast, displayed entry, tiny pencil notes, Coach crossed-off notes, conflicting-entry contrast, 79-ID catalog membership, font uniqueness, classic-display override and original unknown-ID fallbacks.
- **Historical rendering contract:** four new boards use >=4.49:1 digit contrast on normal/highlight states; original seven retain >=4.0:1 for previously issued digit colors, while pencil marks and corrected conflicting digits meet >=4.5:1. The old raw-ink floor is an explicit known legacy accessibility limitation, not a claim of perfect contrast.
- Android UI `Beta2StyleCompatibilityUiTest`: all **11 boards × 9 fonts × nine digits** at 100% **and 200%** effective system font size (1,782 text-layout checks) using the actual Compose Collection preview; verify selected font family, single-line fit and absence of overflow.
- Actual in-game 9×9 `SudokuBoard` with nine simultaneous pencil notes in one square: six representative light/dark boards × nine fonts × two compact/large grid sizes × digits 1–9 (**972 text-layout checks**) at large text scale. Extra per-note test tags enable reliable inspection; game behavior and accessibility descriptions are unchanged.
- Regression gate includes prior core/app JVM tests, AndroidTest compilation, lint, debug signer/identity verification, exact original image SHA/alpha checks, bundled OFL font source/license/0–9 checks and Android 16 connected instrumentation tests.
- Reviewer uses `com.playereden.sudokutogether.qa.beta2matrix` as an independent debug app. It cannot overwrite public Beta 1 or the earlier 79-item reviewer.

## Outstanding limits
This is a programmatic usability and regression audit, **not** an approval of all possible colors in every real-world visibility environment. Large-text tests are on the Android emulator, not both user devices. Keep a manual visual pass of full 9×9 puzzle, nine fonts with small notes, all 11 boards, color distinction, color-blind friendliness and Co-op partner presence. There are also separate hard release gates: Versus V7 physical-phone approval, real two-phone LAN, launcher icon, original production-signed Beta1→Beta2 install-over with full save preservation and owner's release signoff.

Review against draft #102. No changes to main, public Beta 1, saved records or released signing.
