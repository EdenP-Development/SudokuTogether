# GridMates Woodland V5 — mode-specific illustration integration

**Status:** Android Compose changes + tests + safe fallback committed. Both high-resolution PNG binaries still need to be imported from the locally produced V5 Complete Review Pack. This is a draft, NOT a released V5 APK.

**Branch:** `design/woodland-v5-mode-art-checkpoint` • **Draft PR:** #91 • **Stacks on:** draft PR #90 (V4 artwork).

## Runtime behavior

- Solo/Home retains V4 `gm_woodland_storybook_hd_v4.png` (1774×887) and the player's selected Collection companion.
- Co-op looks up `gm_mode_coop_woodland_v5_hd`; Versus looks up `gm_mode_versus_woodland_v5_hd`.
- When the matching original PNG is bundled, that mode uses the dedicated V5 scene. Characters are baked into the V5 art, so no duplicate fox/owl portraits overlay the scene.
- When the V5 asset is not yet packaged, the existing V4 woodland backdrop and original Fox/Owl Collection sprites render unchanged. This protects an incomplete build from broken resources.
- All game controls, text, difficulty, room-code selection, game saves, collection ownership and LAN mechanics are untouched.

The dynamic drawable lookup is a **temporary staging strategy**. Once both V5 image files pass full Android QA, it can be replaced with typed `R.drawable` identifiers and the fallback removed.

## Binary artwork

Original generated resources in `GridMates_Woodland_V5_Complete_Review_Pack.zip`:

| Mode | Path | Dimensions | SHA-256 |
|---|---|---|---|
| Co-op | `app/src/main/res/drawable-nodpi/gm_mode_coop_woodland_v5_hd.png` | 1920×819 | `943484370b66caf115c1033f632c2d0b0c0b92226bbaf9b3ae39d12ed98abdbc` |
| Versus | `app/src/main/res/drawable-nodpi/gm_mode_versus_woodland_v5_hd.png` | 1920×819 | `a4f258fb640660a18b2d1061ba167dd670968578210da8ae2a2184bd07b09aee` |

Use the original image bytes, **not screenshots or composite design sheets**. To import in an extracted repository checkout:

```bash
python3 tools/apply_woodland_v5.py /path/to/unpacked/V5-review-pack --dry-run
python3 tools/apply_woodland_v5.py /path/to/unpacked/V5-review-pack
git add app/src/main/res/drawable-nodpi/gm_mode_*_woodland_v5_hd.png
git commit -m "art: bundle distinct 1920px Co-op and Versus scenes"
```

The importer verifies both checksums, PNG headers, dimensions, and byte counts before writing; it refuses to replace differing content. Use its `--dry-run` first. On GitHub Actions, the new `.github/workflows/gridmates-woodland-v5-branch-qa.yml` checks the branch on push (compile, JVM tests, lint, AndroidTest compile), but does not generate a downloadable APK or run an emulator.

## Acceptance gates

1. Import both original PNG binaries, with exact manifest SHA-256 match.
2. Review decorative puzzle boards for 9×9 correctness (currently stylized and not strict Sudoku grids). Do not call them accurate game screenshots.
3. Run full Android CI, actual emulator instrumentation and captured screenshots for Home, Co-op, Versus, 320dp / 200% text.
4. Build a separately installed reviewer APK for owner phone inspection; do not claim public upgrade compatibility.
5. Owner visual approval before merging V5 -> V4 and then considering release.
6. Keep original production signing/save-preserving Beta1-to-Beta2 install-over and real two-physical-phone Wi-Fi testing as independent release gates.

**Never merge PR #91 or replace the production APK solely on the basis of source-level checks.**
