# GridMates Sudoku — Versus V6 canonical Fox/Owl art

**Status:** isolated reviewer artwork, not part of signed/public Beta 2. This branch is stacked on the latest Garden content-review branch, so it includes the nine new cosmetic pilots while leaving the original main and spouse's Beta 1 saves alone.

## Why this matters

On the approved V5.1 play screen, Versus uses `gm_mode_versus_woodland_v5_hd.png`, a single detailed forest image with Fox and Owl painted into it in a *different* illustration style from Collection, Home and Co-op. `NearbyModeBanner.kt` correctly suppresses extra fox/owl overlays, so simply enabling them would show duplicates. A proper replacement scene is needed.

## What is built

- A **new** animal-free 1774×887 woodland master (`design/versus-v6/source/forest_race_empty.png`) with warm forest scenery, two mossy stumps, woodland flowers and distant hills. This was AI-assisted art; it is not a code-drawn forest or an extracted screenshot.
- A **new** 1774×887 Versus V6 scene PNG (`app/src/main/res/drawable-nodpi/gm_mode_versus_woodland_v6_hd.png`). It uses the *actual approved Collection Fox/Owl pixel artwork* as the foreground mascots, with identical outline colors and facial designs. The characters are visually tucked behind the boards, not pasted over their surfaces.
- The two decorative game boards were redrawn programmatically as **exact 9×9** Sudoku grids with true 3×3 divisions and no invented digits, not left to image-generation guesses. No flag (checkered or otherwise) is present.
- All game-mode titles, buttons and actions remain native Compose. **No baked-in game text**, rank, player identity, fake room state or puzzle numbers.
- `NearbyModeBanner.kt` uses the V6 asset for Versus only; the approved Co-op V5 and original V5 Versus PNGs stay in the repository untouched. V4 fallback code and Collection portrait assets also remain.
- `gridmatesVersusV6Preview=true` builds a debug APK with **`com.playereden.sudokutogether.qa.versusv6`** identity, separate from public Beta 1, V5, first-five, and Garden reviewer data.

The exact source/output PNG dimensions, source SHA-256, canonical portrait provenance, and image-output SHA are recorded in [manifest.json](../design/versus-v6/manifest.json). The exported PNG is an intentionally composited single raster so Android does not rely on device-specific sprite overlay placement/cropping.

## QA and approval gates

- [x] Exact original PNG checksum/dimension integrity and retained V5 originals
- [x] New resource referenced by Android code without removing existing mode screens or controls
- [ ] Android 16 emulator compilation, lint, JVM and Compose UI regressions
- [ ] Actual emulator screenshot versus Co-op control at same display size
- [ ] 320dp / 200% font-scale title and start/join action reachability
- [ ] Owner phone visual approval of character positions, line consistency, forest background and both Sudoku grids
- [ ] Merged into Beta 2 release branch in an approved order; original production signing, save upgrade and two-phone LAN checks handled separately

**Release guard:** a passing APK is a review candidate only, not authorization to overwrite the spouse's current game. Do not mark [issue #97](https://github.com/PlayerEden/SudokuTogether/issues/97) complete until the owner approves phone screenshots.

Relevant content PRs: [first five #95](https://github.com/PlayerEden/SudokuTogether/pull/95), [Garden #98](https://github.com/PlayerEden/SudokuTogether/pull/98).
