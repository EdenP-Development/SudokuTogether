# GridMates Beta 1.2 — completion and Daily presentation review

## Purpose
Refine already-working Solo/Daily results and locally saved Daily activity. No new streak or obligation mechanics, additional reward currency, solver changes, player-data schema change, multiplayer protocol change, or install/signing change.

## Source behavior
- When a saved completed match record matches the opened finish screen, `CompletionRewards` compares achievement/Collection states with the same record omitted. Only genuinely earned awards are shown (up to two of each family, with an overflow count). A completed board without a matching saved result shows no invented unlocks.
- The existing completion title, elapsed time, move count, assisted/unassisted label, Coach usage, entry checks, New puzzle/Back home, and Records navigation remain.
- The Daily finish shows the count of distinct saved Daily completion dates.
- The Home Daily card shows seven recent calendar days ending at the locally computed Today date, using durable result records plus retained saved Daily sessions. Completed, in-progress, and unsolved days are distinct; no streak bonuses or punishment.
- On large font scales the seven-day markers collapse to a text summary; play buttons and the board remain unaffected.

## Validation
- Initial [core run 44192805949](https://github.com/PlayerEden/SudokuTogether/actions/runs/44192805949) caught test-fixture misuse: `PersonalPlay.newSolo` appropriately rejects an all-blank invalid Sudoku. The model was not changed; test fixtures were corrected to directly construct archived sessions for date-only aggregation tests.
- Final [core run 44193208337](https://github.com/PlayerEden/SudokuTogether/actions/runs/44193208337): complete core JVM regression passed, including new unique-record, actual unlock, and calendar-window tests.
- Final [Android run 44193208288](https://github.com/PlayerEden/SudokuTogether/actions/runs/44193208288): core/Android unit tests and compilation passed, plus **4/4 FinishAndDailyPresentationUiTest** and **3/3 existing DailyFinishUiTest** on API 36 (7/7, zero failed). The existing Activity-based Daily tests exercise old Daily review after archive pruning, process recreation, 200% fonts, and landscape without losing completed details.

## Release boundary
- The isolated Beta 1.2 owner-phone APK built before PR #64 does not contain this newer finish/Daily work. A separately packaged preview is required for physical visual judgment.
- Full LAN two-phone Co-op/Versus remains a separate stabilization gate. Automated emulator UI is not physical owner approval or multi-device evidence.
