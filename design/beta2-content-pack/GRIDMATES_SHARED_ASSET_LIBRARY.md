# GridMates shared character art library — reuse without copying gameplay

**Status:** cross-game asset convention, not a shared unlock database. Owner direction: make approved animals reusable in GridMates: Sudoku and GridMates: Word Search (and any future GridMates games) while preserving independently installed, local-first applications.

## Source of truth and style

- The canonical art standard is [CANONICAL_CHARACTER_STYLE.md](CANONICAL_CHARACTER_STYLE.md).
- **Original approved characters** live in Sudoku's `app/src/main/res/drawable-nodpi`, currently including `woodland_fox.png`, `woodland_owl.png`, `ocean_octopus.png`, `garden_honeybee.png`. New candidates remain in isolated review branches until approved and merged; do not pull directly from unfinished pilot folders.
- Every new shared animal gets a semantic stable asset ID (`woodland_raccoon`, `farm_cow`, etc.), plus `species_key`, collection, artist/source provenance, license/right-to-distribute record, dimensions, SHA-256, revision and approval status.
- One consistent character-style family: **`gridmates_chibi_v1`**. Illustrated backgrounds may be richly painted, but portrait sprites must use the same simplified silhouettes and facial/stroke conventions.
- New species are preferred over repeats. A repeated animal needs visually distinct biological/breed identity and explicit owner approval, not an alternate color of the same file.

## Recommended distribution workflow

1. Create/approve the source illustration independently of Sudoku/Word Search mechanics.
2. Keep immutable original PNG (transparent; no embedded UI text) plus exported Android-size PNG and checksum in the art registry. Verify display at 30dp, 48dp and 72dp.
3. Record the canonical asset ID and original SHA-256 in an editioned `gridmates-art-manifest.json`; **version the art pack** (e.g., `gridmates-characters-1.0.0`).
4. Export selected approved files into each game's *own* resources during build. Compare hashes; do not manually redraw the same fox/owl in the second game.
5. Add a test in each game that rejects missing assets, wrong dimensions, unexpectedly changed checksums, unknown species IDs and lack of license/provenance.
6. Each app independently registers which characters it exposes, how its rewards unlock, and how it stores ownership. Keep game-specific award formulas, progress, purchases and saves out of the shared art pack.

## Cross-game compatibility rules

- Sudoku `woodland_fox` and Word Search `woodland_fox` must point to the **same approved artwork revision** unless the game explicitly needs a separate format/pose, in which case record a distinct variant ID and source.
- A user's Sudoku unlock **does not automatically unlock** the Word Search variant; that would require a separate, privacy-conscious opt-in/account or device-to-device portability design. Never assume cross-game cloud accounts.
- Collections may differ across games. The asset ID means *which illustration*, not *who has earned it*. Unlocks are per game.
- Ensure every new GridMates app can be installed offline and retains its standalone art files. Shared library is a development-time package only, not a mandatory runtime download.
- Do not bundle copyrighted third-party art or redistributed font binaries without confirmed permissions.

## First implementation checkpoint

Use the approved existing fox/owl/octopus/honeybee resources as the baseline. The five Beta 2 candidates are captured in [art-only draft PR #94](https://github.com/PlayerEden/SudokuTogether/pull/94) and playable isolated [draft PR #95](https://github.com/PlayerEden/SudokuTogether/pull/95). They become shared *approved* assets only after phone-size portrait QA and source merge.

Before creating a separate public art-pack repository, confirm whether the two game codebases can consume the same exported manifest with pinned files. A simple versioned manifest and release ZIP is enough at the current scale; avoid unnecessary shared services, logins or runtime dependencies.
