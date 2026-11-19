# GridMates — Woodland V5: distinct mode banners (asset checkpoint)

**Status:** Review-only art checkpoint. This GitHub branch contains the V4 implementation unchanged plus this design/integration record. **The two binary images have NOT been committed yet**, and no V5 Android build or emulator QA has run.

**Base branch:** `design/woodland-v4-hd-raster` (draft PR #90).

## Locked direction
- **Solo/Home:** keep V4 `gm_woodland_storybook_hd_v4.png` (1774×887) and preserve the currently selected Collection companion overlay. This scene is now distinct from Co-op and Versus.
- **Co-op:** use a separate high-resolution 1920×819 illustrated forest scene of the Fox and Owl working together on one puzzle.
- **Versus:** use a separate high-resolution 1920×819 illustrated forest scene showing the Fox and Owl on separate puzzle boards beneath a racing flag.
- **Native Android Compose controls:** preserve native title/eyebrow text, adaptive high-contrast scrim, difficulty controls, buttons and room-code option.
- **Avoid duplicated characters:** Do not re-overlay `woodland_fox` or `woodland_owl` in Co-op/Versus, as those character illustrations are already part of their V5 banner backgrounds. Dynamic Solo selection remains unchanged.

## Generated artwork prepared on November 19, 2026
Two standalone originals with no baked-in buttons or lettering:
1. `app/src/main/res/drawable-nodpi/gm_mode_coop_woodland_v5_hd.png` — 1920×819, SHA-256 `943484370b66caf115c1033f632c2d0b0c0b92226bbaf9b3ae39d12ed98abdbc`
2. `app/src/main/res/drawable-nodpi/gm_mode_versus_woodland_v5_hd.png` — 1920×819, SHA-256 `a4f258fb640660a18b2d1061ba167dd670968578210da8ae2a2184bd07b09aee`

**Source of truth for binaries and integration script:** `GridMates_Woodland_V5_Complete_Review_Pack.zip` generated in the game-design chat on November 19, 2026. This ZIP includes both original asset files, `integration/apply_woodland_v5.py`, manifest, a review contact sheet, and full README. Import these actual files rather than rasterizing or cropping the illustrated moodboards.

## Expected integration patch
On the V4 `NearbyModeBanner.kt`:
- Replace the lone V4 background reference with `if (together) R.drawable.gm_mode_coop_woodland_v5_hd else R.drawable.gm_mode_versus_woodland_v5_hd`.
- Remove the now-duplicative Fox/Owl portrait overlay block in both multiplayer modes.
- Keep all native text and other controls unchanged.
- Update the two existing instrumented assertions that currently expect portrait overlays to expect absence of duplicated overlays. Preserve tests of both banner image nodes, headings and actions.

## QA/release gates
- Use a separate V5 review branch and build, never overwrite saved production data.
- Verify image dimensions and SHA-256, build core/app, run lint and Android 16 instrumentation, inspect real screenshots in Solo/Co-op/Versus including 200% font-scale and narrow screen widths.
- Confirm exact image crop and that decorative Sudoku-like boards are visually acceptable.
- Owner approval before merging the art; signing/install-over save-preservation and two physical phones on same Wi-Fi remain separate release tests.
- No changes to game engine, LAN, user progress, cosmetics ownership or monetization are proposed.
