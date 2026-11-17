# Continue GridMates: Sudoku

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
