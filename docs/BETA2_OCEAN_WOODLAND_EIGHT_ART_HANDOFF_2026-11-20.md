# GridMates Beta 2 — Ocean + Woodland last-eight artwork review handoff

**Date:** 2026-11-20. **Status: eight original art candidates created and locally tested; GitHub branch contains exact manifest and safe importer, NOT the binary PNGs.** No game source changed and no public update is authorized.

## What exists today

The isolated Farm [PR #101](https://github.com/PlayerEden/SudokuTogether/pull/101) has 35 Player Icons (13 Woodland, 7 Ocean, 9 Garden, 6 Farm), plus 28 emblems/boards/inks/fonts = **63 cosmetics**. Its Android 16 reviewer workflow succeeded, but the owner's final on-phone sign-off is separate.

The standalone **GridMates_Beta2_Ocean_Woodland_8Icon_ReviewPack.zip** delivered in the current ChatGPT conversation contains:
- Eight original 512×512 transparent RGBA PNG icons;
- Four original editable Woodland SVGs and rendering script; ocean original transparent sheet plus deterministic crop script;
- Manifest of exact source SHA-256 hashes and unlock requirements;
- Full **43-portrait** contact sheets at **30, 48 and 92 actual pixels**, on cream and forest surfaces, combining 35 exact PNGs extracted from the QA-passed Farm APK plus these 8 new candidates;
- Full pre-audit JSON covering file SHA-256, alpha bounding box, edge contacts and duplicates.

**The ZIP is a local review deliverable, not uploaded into the GitHub PR.** The SHA-pinned manifest in this branch records which bytes should eventually be imported. Do not claim this branch is an eight-icon Android implementation.

## Last eight IDs and locked proposed targets

| ID | Subject | Family | Qualified puzzle completions |
|---|---|---|---:|
| `ocean_seahorse` | Seahorse | Ocean | 30 |
| `ocean_dolphin` | Dolphin | Ocean | 46 |
| `ocean_seashell` | Seashell (item, no face) | Ocean | 67 |
| `ocean_clownfish` | Clownfish | Ocean | 95 |
| `woodland_chipmunk` | Chipmunk | Woodland | 16 |
| `woodland_beaver` | Beaver | Woodland | 39 |
| `woodland_woodpecker` | Woodpecker | Woodland | 56 |
| `woodland_skunk` | Skunk | Woodland | 90 |

The roster and targets reproduce existing [Beta 2 candidate design PR #93](https://github.com/PlayerEden/SudokuTogether/pull/93); this work does **not** alter the earlier unlock thresholds or owned IDs.

## First combined 43-art visual pre-audit

- 35 exact source PNGs extracted from Farm APK plus 8 original new candidates = **43/43 proposed Player Icon illustrations present in the local comparison**. They are **not** all in the Android game yet; current review APK stays 35/43.
- New crop/export check: all 8 are **512×512 RGBA**, have real transparency, no opaque artwork touching canvas edges, and distinct SHA-256 values.
- Local 43-icon pre-audit: **43/43 unique hashes, 0 duplicate bytes, 0 missing intended portrait candidates**. All families have expected sizes (Woodland 17, Ocean 11, Garden 9, Farm 6).
- Existing canonical `woodland_rabbit` remains the only observed alpha-canvas-edge contact (69 pixels at upper edge); review actual crop before modifying approved legacy art.
- New Ocean orange/coral colors and outline boldness should be judged against the old muted Ocean 7 in the on-device Collection at 30/48/72 dp. New Woodland illustrations are simplified vectors, but they still require owner approval beside Fox/Owl and other established sprites.
- This is **not** the final full audit. Boards, ink contrast, fonts, portrait equip UI, exact Android renderer mappings, LAN unknown-ID fallback, signed install-over/save preservation all remain unverified.

## Safe next engineering step

1. Retrieve the review ZIP from this ChatGPT conversation and create a new isolated **playable** Beta 2 branch stacked on Farm #101 (or on a newly verified integrated preview branch, never on public main).
2. Copy or cherry-pick this manifest/import script to that branch. From repo root, use `python -m pip install Pillow` and `python scripts/stage_beta2_last_eight.py --zip /path/to/ReviewPack.zip`; it performs a **dry run** and validates all 8 against pinned exact file hashes.
3. After reviewing the output, rerun with `--apply`. Refuse existing resource collisions, never overwrite original artwork.
4. Add 8 new catalog entries and drawable mappings with original IDs and proposal targets; update Collection counts to **43 Player Icons, 6 emblems, 7 boards, 10 inks, 5 fonts = 71 preview cosmetics**. Keep all prior unlocks/ownership stable.
5. Add JVM unlock/ownership/unknown-ID tests plus actual Android 16 instrumented Collection and scroll screenshots. Use a **new distinct QA application ID**, not an install-over production package.
6. Inspect screenshots and owner physical phone. Confirm no new sprite shape/alpha errors or excessive visual weight. Only after sign-off consider merge order and begin four board palettes + four licensed fonts.
7. Use [full collection cohesion gate PR #102](https://github.com/PlayerEden/SudokuTogether/pull/102) before public Beta 2. A full eventual catalog of **79** requires the remaining 8 board/font items and all upgrade/network gates.

## Not done

- These 8 PNGs are **not committed in this GitHub PR**, not hooked into Compose catalog, not built in an APK, not Android QA verified, not approved for use.
- No adjustment to V7 Versus #99 or the current Farm reviewer #101.
- No final Sudoku app-icon replacement; its visual exploration is in PR #102.
- No release, signer change, save migration, network change, or public overwrite.

This intentionally avoids quietly switching the user's working installed Beta 1 while assembling Beta 2.
