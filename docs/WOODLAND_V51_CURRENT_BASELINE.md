# GridMates: Sudoku — Woodland V5.1 current visual baseline

**Source status (November 19, 2026):** The current `main` branch includes the owner-approved Woodland V5.1 gamefront via [PR #92](https://github.com/PlayerEden/SudokuTogether/pull/92), merged at `15c7fdbbc2b99adbdabc957ea6e5525c4fca78d2`. This is the primary development source baseline, **not a published public Android update**.

## Current visual implementation

| Screen | Selected visual resource | What is distinctive |
| --- | --- | --- |
| Solo/Home | `gm_woodland_storybook_hd_v4.png` | Detailed woodland landscape, with the player's actual selected Collection companion overlaid independently |
| Co-op | `gm_mode_coop_woodland_v5_hd.png` | Dedicated 1920×819 illustration of a Fox and Owl collaborating around one shared board |
| Versus | `gm_mode_versus_woodland_v5_hd.png` | Dedicated 1920×819 illustration of two separate race boards, revised in V5.1 to remove the distorted checkered flag |

The illustration resources live in `app/src/main/res/drawable-nodpi`. Their exact lengths and SHA-256 fingerprints are in `design/ui-art/v5/art_manifest.json`. Kotlin Compose renders the image behind *native* headings and controls; full-screen artwork never replaces actual game state. The V5.1 Versus image retains tiny simplified pennants as distant scenery, not the distorted checkered race banner. The art still contains stylized decorative grids that are **not** representative playable 9×9 Sudoku boards.

The historic `design/ui-art/v1`, `v2`, `v3`, and `v4` materials remain for design provenance, not as competing sources of truth. The superseded V3 vector scenery was retired on the visual branch. Older illustrations remain recoverable through Git history; **do not reintroduce** them through default asset selection.

## Verified GitHub / Android evidence

- **Current source promotion:** PR #92 to `main`, passing [normal PR Android compile, unit and lint workflow #44633601301](https://github.com/PlayerEden/SudokuTogether/actions/runs/44633601301).
- **V5.1 exact artwork import:** [run #44633049678](https://github.com/PlayerEden/SudokuTogether/actions/runs/44633049678), success. New image SHA-256 `3c3d2df41cfb6807123726a49902723be0f7271b57fbfb553bed3d1dd7212314`.
- **V5.1 isolated reviewer:** [run #44633405848](https://github.com/PlayerEden/SudokuTogether/actions/runs/44633405848), success. Core/app unit tests, lint, APK compile/identity/signer verification, Android 16 installation and actual Home/Co-op/Versus captures, **13/13 connected UI tests** passed.
- **Reviewer artifact:** `GridMates-WoodlandV51-OwnerPreview`, artifact ID `14582753719`, retained until December 3, 2026 (UTC). Android package `com.playereden.sudokutogether.qa.woodlandv51`, versionCode 31, versionName `1.0.0-beta.2-qa.woodlandv51`. APK SHA-256 `676e5706c92926d715e8afb74f1c8a33cd5284970e1ecad9ef2f0c802656414d`. **This separate package cannot update or share data with the production game.**
- Owner phone screenshots of the original V5 before the one-art revision were approved as a visual direction. V5.1 was visually reviewed in generated art and on an Android 16 emulator; physical-phone V5.1 confirmation is still recommended before treating the APK as a public release candidate.

## Release and functional guardrails

The currently distributed itch.io public Android game **remains Beta 1**. Merging art/Compose source into `main` does NOT sign, distribute, or non-destructively update any installed version.

The following separate gates remain open:
1. Recover and verify the **original** production Beta 1 signing identity; do not generate a different key for an in-place update.
2. Perform backed-up, actual production-signed Beta 1 to Beta 2 install-over with no save/Collection loss.
3. Run an end-to-end two-physical-phone same-Wi-Fi Co-op and Versus test.
4. Confirm any last phone-specific visual/crop issues on the final signed build.
5. Publish itch.io material only after release readiness is demonstrated.

## Canonical workflow going forward

- Work from `main` for normal development; branch per feature/bugfix and merge reviewed changes into `main`.
- Treat the old V3/V4/V5 stacked preview PRs #89/#90/#91 as **superseded by merged PR #92**. Keep the branch commit history and manifests as evidence; do not manually merge old drafts.
- Prioritize functional QA, UI polish, save preservation and release readiness over a fourth Woodland redesign.
