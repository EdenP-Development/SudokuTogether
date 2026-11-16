# Continue GridMates: Sudoku

**Current source notice (November 16, 2026):** `main` contains the integrated **v0.11D QA baseline**, but the newer **v1 Beta 1 Android candidate** is in [draft PR #42](https://github.com/PlayerEden/SudokuTogether/pull/42) on `feat/gridmates-v1-beta`. Before editing or building the current Beta, inspect that PR and [the Beta 1 source/QA receipt](https://github.com/PlayerEden/SudokuTogether/blob/feat/gridmates-v1-beta/docs/GRIDMATES_V1_BETA_RELEASE.md), rather than assuming `main` contains the latest player-facing version. **Do not merge, delete, re-sign, or replace saved data based only on this notice.** The [November 16 repository health and consolidation audit](docs/SUDOKU_REPO_HEALTH_AND_BETA_STEWARDSHIP_2026-11-16.md) explains the staged PRs, evidence gaps and safe next steps.

The original `main`-based instructions below describe the older integrated source and remain useful for historical QA/recovery.

- [Current handoff](docs/PROJECT_HANDOFF.md) and [delivery plan](docs/GRIDMATES_DELIVERY_PLAN.md)
- [v0.11D repository audit and signed upgrade evidence](docs/V0.11D_INTEGRATION_AND_QA.md)
- [Roadmap](docs/ROADMAP_V08_TO_LAUNCH.md), [issues](https://github.com/PlayerEden/SudokuTogether/issues) and [manual QA workflows](https://github.com/PlayerEden/SudokuTogether/actions)

v0.11D is the QA update for the installed **Sudoku** app. For multiplayer, install the same QA build on both phones and use the same Wi-Fi. The [v0.11C checkpoint](https://github.com/PlayerEden/SudokuTogether/tree/checkpoint/v0.11c-nearby-woodland-qa) preserves the previous working source and its [APK receipt](docs/V0.11C_DELIVERY_RECEIPT.md).

For another APK, verify the package, original signer and increasing version, then rehearse the update on the actual previously delivered APK with progress saved. Keep signing backups outside Git. [QA update guide](docs/GRIDMATES_UPDATE_AND_SAVE_PREPARATION.md)
