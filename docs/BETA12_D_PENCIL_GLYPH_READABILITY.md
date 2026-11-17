# Beta 1.2 Phase D — Pencil glyph readability (2026-11-17)

## Evidence and motivation

[Android API 36 board/Coach visual audit 44146363596](https://github.com/PlayerEden/SudokuTogether/actions/runs/44146363596) generated **four verified, appropriately labelled** screenshots: Classic with notes, Midnight with Rose/Pencil notes, Coach introduction, and Coach "Show why" first deduction. The synthetic source fixture is **not** a real owner play session.

Full-size clue digits, board hierarchy and selected-square outlines read clearly. **Small 3×3 pencil marks**, especially the handwritten style on Midnight and crossed-off candidates on the 280dp Coach proof board, look unnecessarily tiny despite passing the earlier contrast checks. A first screenshot capture had contained a blank frame and stale scenes; it was rejected and fixed before drawing these conclusions.

## Targeted adjustment

- Increase the proportional **pencil-only** glyph size from `0.68` to `0.82` of the allocated 3×3 candidate slot (roughly +20.6%).
- Keep the exact square, 3×3 mark positions, full-size digits, font IDs, colors, contrast correction, board size, Coach prose, player data and multiplayer behavior unchanged.
- Preserve the existing system font-scale correction inside the 9×9 grid.
- Include the previously validated emulator screenshot instrumentation so future audits can capture real renderer frames without stale labels.

## Verification checklist

- [ ] Core and Android unit regression checks pass
- [ ] Four real emulator captures exported with all images nonblank and named according to the actual scene
- [ ] On the normal Classic and Midnight boards, small notes stay individually identifiable without overlapping each other or grid rules
- [ ] Coach "Show why" shows target/proof distinctions and crossed-off candidate notes clearly
- [ ] Physical Android review remains open: multiple phone widths, larger text, actual earned font and theme combinations
- [ ] No new APK or production/QA signer changes in this PR

The changes are limited to presentation. **Screenshot QA is not visual approval on a physical device.**
