# GridMates: Sudoku — Beta 1 → Beta 2 actual Android install-over rehearsal

**Date:** November 17, 2026 (US Eastern) / GitHub UTC November 18  
**Purpose:** Prove that Android keeps genuine Beta 1 local progress and the new Beta 2 runtime migrates earned cosmetic ownership when the newer APK is installed over the older APK **without uninstalling**.

## Verified source and runner

- Beta 1 exact application-source commit: `76cbfc149569938b97286f3efe4ad44e08c5934f` (the source recorded for the original public itch.io Beta 1).
- Beta 2 test source: [PR #68](https://github.com/PlayerEden/SudokuTogether/pull/68), at `7ad5726a99c461396b0457660cdd6c0b3cb76952`. Main application content through merged PR #67; PR #68 adds **androidTest fixture, verification test and release QA workflow only**.
- [Successful GitHub Actions run 44207050207](https://github.com/PlayerEden/SudokuTogether/actions/runs/44207050207) / job `134475592547`.
- The [ordinary Android compile/unit/lint PR check](https://github.com/PlayerEden/SudokuTogether/actions/runs/44207050278) also passed.
- Emulator: Android API 36 / Pixel 5 profile.
- Rehearsal application ID: **`com.playereden.sudokutogether.qa`**, Debug variant in BOTH builds (not public production). Both builds used the same ephemeral runner-only Android debug signing certificate, SHA-256 `52b749fcfa233390b5bf1e29481213e9e9cab02f286baef70e181bb35e44e19d`. This is **not** the public key.
- VersionCode: **30 → 31**; versionName: `1.0.0-beta.1-qa` → `1.0.0-beta.2-qa`. The workflow's independent `aapt`/ `apksigner` checks verified package, signer and codes **before installation**.

## The tested sequence (passing)

1. Check out exact Beta 1 and Beta 2 sources in isolated directories and build their APKs/test APKs on the same runner.
2. Copy a **test-only** `Beta1UpgradeSeederTest.kt` into the Beta 1 checkout; **none of its old application code is modified**. Install Beta 1 and execute the seeder against the Beta 1 runtime.
3. Beta 1's own `LocalPlayerStore` writes the real version 1 profile format, with:
   - An active Solo puzzle (one unfinished cell) with Notes bitmask 4, Undo history, one move, time and a recorded hint.
   - An unfinished deterministic Daily challenge dated November 17, 2026.
   - **Five completed Solo match records**, which legitimately unlock Rose number ink and the Robin portrait using original thresholds.
   - Player name `Upgrade Player`, Rose ink and Robin portrait equipped, Hard selected, Clean pencil marks on.
4. Check the saved JSON has **no `ownedCosmeticIds` field**, as expected for Beta 1. Launch the old app, then force-stop without uninstall.
5. Execute the **actual Android `adb install -r`** with Beta 2 APK using the **same package and signer**. Re-read the saved JSON **before the new app's first launch** and compare bytes against the original; **identical**.
6. Launch Beta 2 and run `Beta2UpgradeVerifierTest`. It validates original player identity, all five records, exact Solo Notes/Undo/hint data, exact unfinished Daily puzzle, saved preferences, original style ownership, and the new `ownedCosmeticIds` ledger. Merely reading the old profile doesn't overwrite it. An ordinary verified save writes the migration, then a later history-trim test proves earned Rose/Robin remain owned while the game saves persist.
7. Both instrumentation tests passed, both app launches reported `Status: ok`, and the run emitted **PASS**.

## Important boundaries

**This is strong evidence for Android migration behavior, but not a public production update certificate check.** We compiled the *original source* as a debug APK and intentionally used one temporary test signer. The actual published Beta 1 APK was not reused as the baseline, and the owner's wife/device/installed saves were never touched.

The real public APK uses package `com.playereden.sudokutogether` and must retain its **original** signing certificate fingerprint `d362dab018a255ccb313582df1e3cf836246e7da1778a198eb1da03a7cfe7cd1` when versionCode increases 30 → 31. The GitHub production builder `.github/workflows/android.yml` explicitly checks for that certificate and requires private secrets `SUDOKU_KEYSTORE_BASE64` and `SUDOKU_KEYSTORE_PASSWORD`; **secret availability has not been established by this rehearsal**. Do **not** generate a new replacement signing identity for the public update.

The recorded Beta 1 public APK SHA-256 is `6d7dda34129fc4a6c74ab8146dcb36128967340558db50d9f7bac58cbe41c8c3`. Cross-check the actual itch download before the final upgrade acceptance. Use a suitable non-critical device/test clone and a verified manual progress export; install the properly signed Beta 2 public APK **over** that existing public APK. Confirm exact installer signer, package, increasing versionCode, first-install timestamp, local profile, Notes, Daily, recorded results, earned and equipped cosmetics and progress backup. Never uninstall or clear live family data for testing.

## Repeatability
- The manual-only workflow is `gridmates-beta2-upgrade-rehearsal.yml`, invoking `scripts/run_beta2_install_over.sh` in a single shell (avoids `android-emulator-runner` running multiline commands as independent processes).
- The first runner attempt [44205723657](https://github.com/PlayerEden/SudokuTogether/actions/runs/44205723657) failed before any app install because a multiline `script:` lost its shell variable when each command ran separately. This was a **workflow mechanics error, not a game crash, data-loss result or rejected APK**.
- The succeeding run [44207050207](https://github.com/PlayerEden/SudokuTogether/actions/runs/44207050207) compiled both versions, passed preflight and completed the entire emulator upgrade. **No APK artifacts or signing secrets were published** by the rehearsal.

## Product status
The source is progressing toward a public itch.io Beta 2, not yet released. Existing public Beta 1 remains the real downloadable version until original-production-key and non-destructive signed update acceptance passes. The owner has deferred renewed formal two-phone LAN acceptance for now; direct same-Wi-Fi host/join is implemented and previous family testing was positive.
