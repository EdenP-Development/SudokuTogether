# GridMates: Sudoku — launcher icon v2 exploration (design-only, 2026-11-20)

**Status:** owner requested a stronger launcher icon; concepts require owner visual sign-off. This document does **not** approve shipping a replacement. No public APK, Android resource, signing, game logic or progress is changed.

## Checked actual baseline
- Main `app/src/main/res/drawable/ic_launcher_foreground.xml`: cream rounded 3×3 grid, dark green lines, gold center tile and tiny stroke-path numerals, with separate `@color/icon_background` (`#214F3F`) on the adaptive icon.
- Main `app/src/main/res/drawable/ic_launcher.xml`: distinct static fallback 3×3 grid (cream on green, gold corner accents); currently not guaranteed visually identical to the adaptive foreground. Verify all icon consumers before replacement.
- Main `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`: Android adaptive background/foreground XML.
- `docs/GRIDMATES_VISUAL_IDENTITY_BRIEF.md`: single 4–9-cell launcher symbol, not a tiny full 9×9 Sudoku board; common GridMates tile vocabulary, calm cream/green/gold current palette, accessible tiny-size silhouette.
- `docs/GRIDMATES_BRAND_AND_SERIES.md`: GridMates is a family of separate puzzle games. Keep a recognizably shared mark, but Sudoku must clearly differ from Word Search / future titles.
- `design/beta2-content-pack/CANONICAL_CHARACTER_STYLE.md` (draft #93): existing animal art is the reference for Collection avatars. Do not redraw the canonical Fox/Owl as an arbitrary icon ornament.

## What should improve
1. More recognizable **Sudoku** at Android launcher size (number/grid relationship) while avoiding a dense illegible 9×9.
2. Clear hierarchy: at most a few very large digits; 48px preview must work even with digit detail omitted.
3. Cleaner 3×3 tile geometry, consistent interior strokes, one deliberate gold emphasis instead of several competing hot spots.
4. Refined cream + forest green + gold from the actual installed identity; this is an evolution, not a total brand reboot.
5. Monochrome, round/circle mask, squircle and light/dark wallpaper behavior; respect Android's central adaptive safe zone.
6. Static fallback and adaptive foreground should feel like the same mark. Test actual launcher and app drawer, not only an SVG rendered large.
7. No words, tiny mascots, pasted full game screenshots, gradients, photographic materials, baked text labels or unlicensed fonts.
8. Keep `GridMates` series identity across future games via repeatable grid geometry/outline; differentiate with game-specific color/detail, not purely color.

## Three sketches to compare (SVG proposals, **not Android resources**)
- **A — Logic highlight** (`design/launcher-icon-v2/concept-a-logic-highlight.svg`): 3×3 tile board; single gold central cell and three large, widely spaced clue digits. Familiar Sudoku-first read.
- **B — Last move** (`design/launcher-icon-v2/concept-b-last-move.svg`): central prominent numeral on a gold tile, two supporting givens; simplest and strongest tiny-size hierarchy. **Recommended first candidate for device review.**
- **C — Connected puzzle** (`design/launcher-icon-v2/concept-c-connected-puzzle.svg`): two adjacent accent cells signal GridMates' play-together nature; needs extra care that it does not look like a generic grid utility.

All SVG files are editable proposals. They use generic SVG text for quick comparison, **not a redistributable font asset**. Before implementation, convert numeral outlines to stable vector paths or otherwise guarantee font rendering consistency. Do not use the existing Fox or Owl as a tiny stamp unless an owner-reviewed comparison proves the mascot remains readable *without* obscuring the puzzle grid.

## Approval & implementation gates
- [ ] Inspect A/B/C side-by-side at 24px, 36px, 48px, 72px and 108px on light/dark backgrounds; hide labels while reviewing.
- [ ] Check circle/squircle/adaptive safe-zone masks, foreground/background separation and optional Android themed monochrome icon.
- [ ] Choose one design after actual owner phone review; avoid changing `main` before approval.
- [ ] Implement as versioned Android vector foreground/background and matching legacy fallback on a separate branch. Confirm `AndroidManifest` launcher/icon references and product flavors.
- [ ] Verify installed shortcut, launcher/app drawer, Play/itch listings and package/release icons all use the intended files.
- [ ] Capture Android screenshots and check different density/icon masks. Zero impact to save, LAN, title, monetization and existing cosmetics.
- [ ] Keep old source available and an easy visual rollback path.

**Brand recommendation:** Puzzle first. Mascot as a full-size Collection and in-game asset; don't cram it into an approximately 48px launcher image.
