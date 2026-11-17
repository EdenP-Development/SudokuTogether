# Beta 1.2 — Profile and Collection presentation (November 17, 2026)

## Goal
Improve how the existing GridMates: Sudoku identity and customization screens feel, using competitor examples as interaction inspiration but **not** copying art, mechanics, economy or UI assets. The established paperback / forest-green look remains intact.

## Source changes
- The equipped avatar is more prominent in Profile and acts as a direct, labeled entry to Collection.
- The Profile name and name-edit button reflow beneath the portrait row when system text scaling is high, instead of crowding a long name in the same line.
- The existing Change icon action explains that Collection also contains board and number styling.
- Collection presents the equipped appearance in a larger, category-aware showcase, preserving the previous full-style dialog, selected styles, IDs and preview logic.
- Animal portraits in Collection have larger gallery thumbnails and centered modal previews.
- Icons show three columns when content width allows (>=320dp at default text scaling); narrower or large-text layouts fall back to two or one column. Boards and Numbers retain their prior column rules.
- No game rules, save format, unlock progression, art assets, advertisements, network protocol or signing keys changed.

## Android QA evidence
- Initial [run 44164833762](https://github.com/PlayerEden/SudokuTogether/actions/runs/44164833762) passed core and Android units, **3/3 Profile tests** and **7/8 Collection tests**, exposing a real responsive gallery test failure at an emulator-bounded width. The original three-column breakpoint of 360dp content width did not activate on a standard-width emulated phone.
- Corrected the gallery breakpoint to 320dp content width and grounded the wider test fixture at 390dp (with a 320dp narrow fallback).
- Final [run 44166537365](https://github.com/PlayerEden/SudokuTogether/actions/runs/44166537365): core/Android unit stage passed; **3/3 ProfileNavigationUiTest** and **8/8 CollectionPolishUiTest** on API 36 (11 total, zero skipped or failed). This includes previous selection/unlock/category regressions, avatar quick access, fontScale 2.0 layout and gallery reflow.

## Verification boundary
- Android emulator instrumentation is not an owner physical-phone visual review.
- Check the 320–430dp Profile and Collection layouts on the actual phone, enlarged Android font settings, gallery tap targets, and legibility of full portrait/board/number previews.
- The previously distributed Beta 1.2 isolated preview **does not contain** this source change. A fresh separately packaged and verified preview is needed to review it on-device.
- Original QA/public install data and signer are unchanged; never issue an incompatible install-over update.

## Next refinement
After the owner reviews this visual slice, continue source-backed work on Daily completion presentation, results, first-time Coach guidance and small-screen consistency, retaining the original Sudoku 9×9 and accessible Solo/LAN rules.
