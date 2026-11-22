# Beta 2 — 990 board × ink × font compatibility quality gate

**Status: draft, isolated from production.** Branch `qa/beta2-79-style-compatibility` is based on the Android-16-green unified 79 catalog (#106) and keeps the original eight source-hash-verified Ocean/Woodland PNGs and all nine original font files.

## Why this audit exists
The 79-cosmetic release comprises 11 board themes × 10 number inks × 9 number fonts = **990 distinct equipped styles**, plus all six possible normal/highlight background states. Simple Collection-card screenshots do not guarantee puzzle usability when a square is selected, marked by a partner, conflicting, matching, related or showing pencil notes.

## Implemented code change
The Sudoku renderer previously drew `palette.conflictInk` on any cell containing a conflicting entered number. When such a cell was selected or partner-highlighted, those fills took precedence over the normal conflict-cell background, resulting in **4.36:1 and 4.46:1** Classic/Linen contrast ratios (below the 4.5:1 target), despite 4.55:1 against the normal conflict fill. The fix calls `conflictDigitInk(background,palette)`: adjust only the *rendered* conflicting foreground until it clears 4.5:1. Original board colors, clue colors, save IDs, selected board and other game rules are unchanged. An existing readable conflict color is not changed.

## Test coverage
- JVM test `Beta2StyleCompatibilityMatrixTest`: **all 990 equipped combinations**, 5,940 background/ink/font state checks, clue contrast, displayed entry, tiny pencil notes, Coach crossed-off notes, conflicting-entry contrast, 79-ID catalog membership, font uniqueness, classic-display override and original unknown-ID fallbacks.
- **Historical rendering contract and accessibility repair:** all 11 boards now target >=4.5:1 for displayed digits and pencil notes. The authored/base-paper ink colors and stored selections remain unchanged. The deep static source audit found **18 of 350 original board×ink×normal/highlight combinations** below 4.5:1 (worst Classic/Linen + Rose + selected = 4.068:1). Those 18 are selected/partner-highlight cases, not unselected paper. The renderer now adjusts **only those displayed highlighted foregrounds** when below 4.5, using a 4.52:1 target; it never mutates the original color tokens or saved cosmetic IDs. The full 990-item JVM test fails if any legacy rendered highlight remains sub-4.5. This is not a claim that color contrast alone guarantees readable glyphs at every pixel density or for color-blind users.
- Android UI `Beta2StyleCompatibilityUiTest`: all **11 boards × 9 fonts × nine digits** at 100% **and 200%** effective system font size (1,782 text-layout checks) using the actual Compose Collection preview; verify selected font family, single-line fit and absence of overflow.
- Actual in-game 9×9 `SudokuBoard` with nine simultaneous pencil notes in one square: six representative light/dark boards × nine fonts × two compact/large grid sizes × digits 1–9 (**972 text-layout checks**) at large text scale. Extra per-note test tags enable reliable inspection; game behavior and accessibility descriptions are unchanged.
- Regression gate includes prior core/app JVM tests, AndroidTest compilation, lint, debug signer/identity verification, exact original image SHA/alpha checks, bundled OFL font source/license/0–9 checks and Android 16 connected instrumentation tests.
- Reviewer uses `com.playereden.sudokutogether.qa.beta2matrix` as an independent debug app. It cannot overwrite public Beta 1 or the earlier 79-item reviewer.

## Outstanding limits
This is a programmatic usability and regression audit, **not** an approval of all possible colors in every real-world visibility environment. Large-text tests are on the Android emulator, not both user devices. Keep a manual visual pass of full 9×9 puzzle, nine fonts with small notes, all 11 boards, color distinction, color-blind friendliness and Co-op partner presence. There are also separate hard release gates: Versus V7 physical-phone approval, real two-phone LAN, launcher icon, original production-signed Beta1→Beta2 install-over with full save preservation and owner's release signoff.

Review against draft #102. No changes to main, public Beta 1, saved records or released signing.
