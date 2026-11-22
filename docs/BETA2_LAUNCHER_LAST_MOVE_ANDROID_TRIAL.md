# GridMates: Sudoku — Beta 2 launcher v2 owner-review prototype

**Not production-approved.** The isolated Beta 2 + Versus V7 reviewer activates a matching adaptive and legacy Launcher Icon B: **Last Move**, based on the older design-only concepts (PR #102). The public released app is unchanged.

## Design constraints
- Primary 108×108dp vector glyph: cream 3×3 puzzle-book grid on forest green; a single warm-gold center cell and large path-based numerals 2 / 5 / 8. The tiny gold check implies the final move, rather than a generic grid utility.
- All numerals are explicit vector paths; **no system font dependency** or tiny 9×9 layout.
- The adaptive foreground keeps the central 66dp square inside Android's usual safe region. A matching legacy standalone fallback has the same foreground on a solid green background, rather than a different logo.
- A monochrome foreground is supplied for Android 13+ themed icons. Owner must check circle/squircle masks and smaller apps-drawer densities.
- Original Beta 1 launcher XML preserved at `design/launcher-icon-v2/source-beta1-foreground.xml` and `design/launcher-icon-v2/source-beta1-legacy.xml`; all three concepts (A/B/C) remain in design PR #102.

## QA required
Check installed icon and app drawer on Android 16 and inspect renders at 24, 36, 48, 72 and 108 pixels, square/circle, cream/green wallpapers, and themed mode. Pixel clarity at 24px matters more than huge rendered samples. Do not merge as a final art choice until owner phone approval. No networking, account, save, puzzle, pricing or unlock changes.
