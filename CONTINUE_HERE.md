# Continue GridMates: Sudoku

## Current Beta 1.2+ checkpoint — November 17, 2026

**Canonical source:** `main` through merged [PR #63](https://github.com/PlayerEden/SudokuTogether/pull/63), commit `f9f9caa`. The existing game now includes Collection navigation, category-aware style previews, font/Notes legibility improvements (#55–#60); fresh Solo input-mode reset (#61); new Co-op/Versus round Notes reset and dense pencil-mark fit (#62); and the more prominent, direct Profile avatar → Collection customization route, responsive character gallery and larger portrait previews (#63). All are source changes verified in focused Android QA, **not** a newly shipped public or compatible QA APK.

**Latest Android evidence:** [LAN Notes/round CI run 44162194038](https://github.com/PlayerEden/SudokuTogether/actions/runs/44162194038) passed core/Android units and 13/13 API 36 UI regressions before [PR #62](https://github.com/PlayerEden/SudokuTogether/pull/62) merged as `b364d48`. [Profile/Collection UI run 44166537365](https://github.com/PlayerEden/SudokuTogether/actions/runs/44166537365) passed core/Android units plus **3/3 Profile and 8/8 Collection Android UI tests** before PR #63 merged. The earlier gallery-width test failure was resolved before this passing run. Test evidence: [Profile/Collection QA notes](docs/BETA12_PROFILE_COLLECTION_SHOWCASE_QA.md). These tests do not replace visual approval on a physical phone.

**Latest delivered isolated APK is still:** [Beta 1.2+ Notes Review build 44150071309](https://github.com/PlayerEden/SudokuTogether/actions/runs/44150071309), applicationId `com.playereden.sudokutogether.qa.beta12notesreview`, launcher **Sudoku B1.2+**, source `f40f927` (PR #60). It is an **isolated install with its own saves** and does **not** contain PRs #61–#63. No public itch.io or original QA signing/update has been changed.

**Next focus:** produce a separately packaged, source-identified Beta 1.2 visual preview only after safety/QA checks; review the Profile/Collection 320–430dp layouts, small text and enlarged fonts on the owner's phone alongside Notes and Coach legibility. Then continue backup/resume and two-physical-phone Co-op/Versus finish, rematch and reconnect checks. See [Beta stabilization issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43). Do not assume emulator UI tests prove independent phones are connected or the owner has approved visual polish.

**Priority:** stabilize existing Sudoku gameplay, saves, and LAN rather than adding monetization, new modes or more cosmetic items. Always inspect latest `main`, open PRs and Actions before trusting an older milestone document.

**Current starting point:** The **v1 Beta 1 Sudoku application source is now on `main`** after [PR #42's merge](https://github.com/PlayerEden/SudokuTogether/pull/42) on November 16, 2026. The individual v0.11E–M draft PRs were superseded, with their milestone commits preserved. New Sudoku bug fixes should branch from **`main`**, not the older stacked branches. The signed itch.io production APK was built from **source `76cbfc1`**, not from the documentation-only follow-up commits, so a fresh binary/CI check is a separate task. The owner confirms installing/launching the public itch APK and QA build on a real phone, while checksum and larger device/multiplayer verification remain open in [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43).

**Beta 1.2 refinement starting point (Nov 17, 2026):** [Parchment + Graphite PR #53](https://github.com/PlayerEden/SudokuTogether/pull/53) and [Ocean + Sepia/Indigo PR #54](https://github.com/PlayerEden/SudokuTogether/pull/54) are merged **in source only**; current catalog is 22 portraits, 6 emblems, 7 boards, 10 inks and 5 fonts. The existing public APK is unchanged. [Beta 1.2 refinement execution plan](docs/BETA12_REFINEMENT_EXECUTION_PLAN.md) prioritizes observable interface/gameplay polish, stable local saves, full QA and a future isolated phone preview **before any new major features**. Phase A Collection navigation [PR #55](https://github.com/PlayerEden/SudokuTogether/pull/55) and the initial Phase B Notes-accessibility [PR #56](https://github.com/PlayerEden/SudokuTogether/pull/56) are merged with focused Android emulator QA. Remaining gameplay polish and on-phone review are tracked in [the Phase B audit](docs/BETA12_PUZZLE_INTERACTION_AUDIT.md). Do not mistake source-only tests for on-device approval or a compatible QA update.

[Beta packages and publishing](docs/GRIDMATES_V1_BETA_RELEASE.md) · [Project health/history audit](docs/SUDOKU_REPO_HEALTH_AND_BETA_STEWARDSHIP_2026-11-16.md)

- [Current handoff](docs/PROJECT_HANDOFF.md) and [delivery plan](docs/GRIDMATES_DELIVERY_PLAN.md)
- [v0.11L Garden collection, fonts and update QA](docs/V0.11L_GARDEN_COSMETICS_QA.md)
- [v0.11K Profile, Collection and update QA](docs/V0.11K_PROFILE_COLLECTION_QA.md)
- [v0.11J focused audit and signed upgrade evidence](docs/V0.11J_FOCUSED_AUDIT_QA.md)
- [v0.11I Coach practice](docs/V0.11I_COACH_PRACTICE_QA.md)
- [v0.11H Coach depth](docs/V0.11H_COACH_DEPTH_QA.md)
- [v0.11G Coach and Ocean collection](docs/V0.11G_COACH_OCEAN_QA.md)
- [Roadmap](docs/ROADMAP_V08_TO_LAUNCH.md), [issues](https://github.com/PlayerEden/SudokuTogether/issues) and [manual QA workflows](https://github.com/PlayerEden/SudokuTogether/actions)

Open the v1 Beta 1 QA APK and tap **Update** for the current v0.11M QA install. For the public beta, use the production APK. [Progress transfer instructions](docs/GRIDMATES_V1_BETA_RELEASE.md#android-downloads)

For another APK, verify the package, original signer and increasing version, then rehearse the update on the actual previously delivered APK with progress saved. Keep signing backups outside Git. [QA update guide](docs/GRIDMATES_UPDATE_AND_SAVE_PREPARATION.md)

**Next visual QA gate (Nov 17, 2026):** [PR #58](https://github.com/PlayerEden/SudokuTogether/pull/58) improves tiny pencil mark contrast (including Coach crossed-off candidates) without modifying palette definitions. [Run 44120831973](https://github.com/PlayerEden/SudokuTogether/actions/runs/44120831973) passed Kotlin/core, Android units and 3/3 Coach emulator UI tests. Treat this as **source/emulator verification only**. Before visual approval, compare 320–430dp displays, Rose/Violet on Midnight/Circuit, Coach crossed-off marks on Classic/Linen, and all three provisional Beta 1.2 board themes. Continue to protect the original QA signer and saved game data.
