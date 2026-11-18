# GridMates: Sudoku — Owner's Beta 2 phone-review observations

**Date:** November 17, 2026 US Eastern (GitHub records use November 18 UTC)  
**Evidence:** Screenshots supplied by the owner from the verified standalone *Sudoku B2 Review* Android installation. This is a visual and usability review; the screenshots do **not** establish all behaviors, multiplayer, or production update safety. Images are intentionally not copied to the public repository.

## Overall owner judgment

The working Sudoku gameplay and parchment/forest-green art direction are worth preserving, but **Home does not feel like a finished puzzle game's main menu**. It reads as an application/settings page with a selector, form panel and navigation, rather than a recognizable, welcoming gamefront. The user explicitly likes the **Daily Puzzle** card and its seven-day activity motif; preserve that feature and its tone.

Interpret "premium/AAA-like" as **polished casual puzzle-game presentation**, not flashy animations, microtransactions or online-services feature bloat. The game remains approachable for older and less-technical players. Keep recognizable actions, large touch targets, readable typography, and quick offline play.

## Grounded issues and decisions

| Priority | Area | Observation | Proposed handling |
|---|---|---|---|
| P0 | Profile achievements spotlight | Literal `${earned}`, `${milestones.size}`, `${upcoming.current}` and `${upcoming.target}` appear in the installed screenshot instead of numbers. The long erroneous string squeezes the **First Grid** title to one letter per line. | **Fixed on main by PR #75** with real interpolation, progress on its own row, and large-text regression coverage. |
| P1 | Home identity | Small app-like header, three selector buttons, and a dark configuration card dominate. Lots of unused cream space on a large phone. | **Draft PR #76** introduces an illustrated welcome/banner, more tactile game-mode cards and mode-specific copy. Judge on real phone before merge. Do not make Home a busy storefront. |
| Preserve | Daily puzzle | Gold card, date/status and seven-day markers supply a satisfying game ritual; the owner singled this out as good. | Keep the data and presentation, test screen visibility after Home changes. |
| P1 | Solo action spacing | Board itself reads clearly; **New puzzle** is detached in a vertical gap between grid caption and keypad. | Explore context grouping without shrinking the 9×9 grid; test small-screen, landscape and 200% font. |
| P2 | Number keypad | Five digits on the first row and four left-aligned on the second visually imbalance the dock. | Explore equal spacing/centering of the last row, preserving full-size targets and thumb access. |
| P2 | Hint dialog | Hint shows a useful complete preview grid but is very tall, with substantial white space and widely separated controls. | Explore better title/board/action grouping with an accessible scrollable fallback; keep Coach explanations and unassisted/assisted accounting. |
| P2 | Collection | Character artwork and board theme previews are consistent. Long repetitive locked rows and repeated next-unlock presentation make browsing feel like a list rather than a collectible gallery. | Refine grouped discovery/shelves and reduce repetition after release-safety work; do not alter earned cosmetic IDs or thresholds. |
| P2 | Bottom navigation | Functional but has a pronounced raised-sheet shadow; may contribute to 'page' impression. | Consider subtler separation consistent with the gamefront. Verify legibility and touch areas. |

## Source and release boundaries

- Main source: `PlayerEden/SudokuTogether`. Owner preview was built with a **different debug app ID** from production.
- PR #75: [Profile achievement display repair](https://github.com/PlayerEden/SudokuTogether/pull/75), merged and Android CI passed.
- PR #76: [Home gamefront first-pass review](https://github.com/PlayerEden/SudokuTogether/pull/76), draft and Android compile/unit/lint passed. No source change is a public APK update until a properly signed release is delivered.
- A dedicated `preview/home-gamefront-owner` branch builds under its own package/launcher to avoid replacing any saved installation.
- Do not add feature bloat, erase/rekey game progress, change puzzle generation/scoring, change LAN identity, or publish a QA APK to itch.io.
- **Public Beta 2 update** still requires the original Beta 1 keystore and actual signed install-over test on backed-up noncritical data.

## Practical acceptance

1. The home view should be immediately recognizable as **GridMates: Sudoku**, with a welcoming game identity before utility-like controls.
2. Solo starts with one obvious action; Co-op and Versus remain easy to find, with honest 'same Wi-Fi' descriptors.
3. A saved Solo puzzle presents a conspicuous **Continue** and distinct New Puzzle/difficulty actions.
4. Daily is still easy to discover and is unchanged functionally.
5. Screen works on 320–430dp widths, 200% system text and landscape, with full accessibility semantics.
6. The user reviews a separate phone build before any larger visual merge.

**Do not claim subjective design approval from emulator CI; source compilation and screenshots are separate evidence.**
