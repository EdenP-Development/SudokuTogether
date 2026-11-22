# Beta 2: offline production-readiness checklist (no owner phone required yet)

## Current truth
- Android 16 unified 75 reviewer passed in [run 44959100046](https://github.com/PlayerEden/SudokuTogether/actions/runs/44959100046).
- 43 player portraits (Woodland 17 / Ocean 11 / Garden 9 / Farm 6), 11 boards, 10 inks, 5 fonts, 6 emblems = **75**.
- The four new Ocean sprites in that APK are **vector redraws**, not the approved-candidate originals. Do not merge or ship them as final art.
- Four OFL-bundled font options are now implemented in the stacked **draft #106** reviewer to reach a 79-item catalog, subject to Android and on-device quality checks.

## Original artwork source of truth
The earlier chat's `GridMates_Beta2_Unified75_IntegrationKit.zip` contains `GridMates_Beta2_Ocean_Woodland_8Icon_ReviewPack.zip`, which contains the original eight `candidate_icons/*.png` binaries. Exact hashes, names and unlock thresholds are in `design/beta2-content-pack/ocean-woodland-eight-review/manifest.json`. **Do not replace the originals with an approximate redraw simply because the SVG renderer passed.**

To import original art from the local integration ZIP into a full checkout, unpack its inner review ZIP and run the existing `scripts/stage_beta2_last_eight.py` in dry-run mode first. Only run `--apply` after verifying the script's arguments, paths, all eight hashes and collisions. The existing CI renderer must be disabled or modified to prefer exact original PNGs without overwriting them; the current renderer intentionally refuses to overwrite mismatched images.

Use the offline gate after import:
```bash
python scripts/audit_beta2_original_art.py --require-original-art
```
The gate verifies all eight exact original SHA-256s, PNG dimensions/alpha, catalog family counts, and Android drawable mappings. A pass does **not** replace phone review.

## Four-font implementation brief
The planned additional number fonts are **Typewriter, Editorial, Soft Print, Tall Print**. These are design roles, not yet licensed typeface files. Select four distinct OFL-compatible families; store exact license text, source URL, version, and original filenames with the font files. Do not call a system font or CSS-style alias a new bundled font. Do not claim a font is OFL without its actual license.

Before registering new font IDs, verify each face contains 0–9, legible 1/7 and 3/8, and sufficient contrast at pencil-note size. Test on all 11 boards, selected cells, conflict states, and Co-op/Versus shared boards. Assign stable IDs and modest solo-accessible unlocks; do not remove or rename the existing five font IDs. Ensure save migration preserves old selected fonts and owned cosmetics.

## Release gate sequence
1. Original Ocean art hash verification and 43-icon cohesion sheets at 30/48/92 px on cream and forest.
2. Four real licensed number fonts + catalog, drawable, accessibility, progress and persistence tests.
3. All-79 collection review: no clipped art, consistent outlines, clear unlock labels, and meaningful board/font contrast.
4. Owner phone approval for Versus V7, board play states, all new art and fonts.
5. Two physical Android phones: same-Wi-Fi Co-op/Versus host/join, disconnect/rejoin, partner presence.
6. Production-signed Beta 1 → Beta 2 install-over on a backed-up device: verify profiles, puzzles, earned unlocks, appearance and statistics persist. Debug-signed side-by-side QA APK cannot verify this.
7. Only then mark Beta 2 releasable.

No public Beta 1 release files should be altered by this draft branch.
