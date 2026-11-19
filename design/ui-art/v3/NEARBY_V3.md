# GridMates: Sudoku — Nearby Mode V3 art review

## Owner feedback (real Android screenshots, Nov 18 2026)
V2 shares a 512×256 Home preview image across a much larger phone banner, making woodland scenery visibly soft next to the game's packaged collectible portraits. V2's intersecting pencil/book artwork also looks busy and inconsistent. Neither is approved final art.

## This isolated V3 experiment
- `gm_nearby_woodland_v3.xml`: newly authored resolution-independent native VectorDrawable using a 1200×500 coordinate system with separated mountain, tree, foliage, grassy and foreground layers. It is a **new standalone simplified illustration**, not an upscaled copy of Home or a full-resolution portrait master.
- `gm_mode_coop_symbol_v3.xml`: rewritten symmetrical book and two aligned grid pages; deliberately simpler pencil shapes, uncluttered negative space.
- `gm_mode_versus_symbol_v3.xml`: two readable board cards and a balanced trophy badge.
- `NearbyModeBanner.kt`: keeps the native Compose text and stage height; places smaller right-side artwork over the new banner backdrop, protecting title legibility. All game actions, difficulty selection, optional room code and host/join remain untouched.

## Acceptance (not yet established)
- Real emulator screenshots of **Co-op** and **Versus** from the V3 debug app, plus corresponding APK and checksum.
- Visual review on a physical Android phone at normal brightness (do not confuse design concepts with actual screenshots).
- Comparison against Home and the actual bundled Collection Fox/Owl art, including sharpness, family resemblance, and silhouette clarity.
- 320–430dp width and 100/200% font: headings not clipped; all Start/Join/difficulty/code actions reachable; mode switch stable.
- If the cleaner vector rendering is **too flat** relative to the painterly collectible portraits, refine or replace it rather than approving on technical sharpness alone.

## Release safety
Draft PR on V2 #88, not merged to main. No saved data, achievements, collectible ownership, LAN protocol or public signer changed. Separate preview is `com.playereden.sudokutogether.qa.nearbyv3`, named **Sudoku Nearby V3**; it cannot update the public install. Beta1→Beta2 same-package certificate/save-preserving update and real two-device Wi-Fi tests remain required before release.
