# STAB-05.08 — APK upgrade acceptance gate

## Source-audited baseline
Existing published itch Beta 2 identity per CONTINUE_HERE.md: `com.playereden.gridmates.sudoku.beta`, versionCode 31, versionName `1.0.0-beta.2`, certificate SHA-256 `9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d`. Exact released APK bytes/receipt must be verified before rehearsal. Current unsigned Beta 3 workflow intentionally still checks code 31; it is not an update-ready signed release. Do not distribute it.

## Acceptance procedure (not executed)
1. Obtain exact prior signed APK and receipt, confirm digest/package/certificate/version and preserve original artifact. Use isolated QA device/emulator, not a player's only installation.
2. Approve a strictly higher release versionCode and intended versionName, confirm original signer availability privately and verify compatibility with release channel. No replacement key, uninstall, clear-data, downgrade, or cross-package install-over workaround. Do not print secrets.
3. Seed baseline through actual app actions: canonical name, equipped earned style, completed Solo record, unfinished Solo entries/notes/undo/time, unfinished Daily, earned ledgers, preferences and saved room. Export portable backup as additional protection. Capture exact baseline APK and profile evidence privately.
4. Stop app, install candidate with `adb install -r` only after package/signer/version checks. Confirm installed version and first-launch behavior; verify identity, records, ledgers, appearance, settings, Solo and Daily resume, room/reconnect state. Verify subsequent save and restart. Do not equate byte equality after migration with logical continuity; compare before first launch where accessible, then semantic state after launch.
5. Record exact source commit, APK hashes, public certificate fingerprint, device/API, checks and failures. No signed release approval until acceptance. Test Google Play signing separately; different app-signing certificate or package requires deliberate migration strategy. Amazon test channel is not proof of Play compatibility.

## Existing tooling limitations
`scripts/apk_update_preflight.py` is QA-only (.qa policy); do not use it to certify production. `run_beta2_install_over.sh` and `gridmates-beta2-upgrade-rehearsal.yml` pin old 30→31 QA flow and temporary signer. `UpgradeContinuityTest.kt` defaults to 18→19 and hardcodes legacy name/launcher label; adapt on a dedicated modern fixture, not blindly run against Beta 3. Existing signed workflow is Beta 2 branch-gated. No new signed APK, version bump, signing action, release or device install in this checkpoint.
