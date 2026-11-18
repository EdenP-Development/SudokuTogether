# Beta 2 — About, backup and public installer readiness

## Scope
A small consumer-facing polish slice. Existing Sudoku rules, cosmetics/unlock requirements, LAN/gameplay, profile schema, original production signing, package/version and public itch APK stay unchanged.

## Player-facing behavior
- About displays the real `BuildConfig.VERSION_NAME`, the existing brand identity, offline Solo/Daily, direct same-Wi-Fi play, local progress and trustworthy Wi-Fi limitations.
- About explains that compatible updates should retain existing progress, that exporting a manual backup provides extra protection, and that uninstall is not an update procedure.
- **Only standalone nondebug Android installs** may offer an installer-sharing action; the installed APK is not a progress backup. Debug/QA builds and split APK installations do not offer this option.
- The Progress settings card explains what is in a portable backup, that active multiplayer room state is not included, and that backing up before changing phones or updating is prudent.
- Backup Restore confirms a **saved earned-Collection item count** from existing records and/or permanent ownership IDs before replacing local progress, even for Beta 1 backups that predate the ownership field; it warns that the destination's current progress is replaced.

## Verification plan
- JVM `InstallerSharingEligibilityTest` for production, debug and split-package decisions.
- API 36 `PublicReleaseCopyUiTest`: About version/content/hidden QA share, visible public share caption, old saved-match-backed earned item count, ledger-only earned item count, 200% font Progress settings.
- API 36 existing `ProgressBackupSettingsUiTest`: export/cancel/confirm/restore data continuity in an isolated emulator.
- Permanent app compile/JVM/lint debug quality gate on every app PR. If all checks pass, remove temporary focused QA workflow to avoid CI overhead.
- Full production-signed Beta 1 to Beta 2 install-over is still **unverified**; the successful debug-signer rehearsal does not certify the real public update.
- Real owner-phone layout review, final support contact and a stable privacy-policy URL are still needed before Google Play; do not fabricate these inside the app.

## Public listing preparation
An updated copy-ready but unpublished [Beta 2 itch listing draft](GRIDMATES_ITCH_BETA2_LISTING_DRAFT.md) describes actual Solo/Daily/nearby features, manual backups, expected update continuity, source privacy behavior and the requirement to stop on a signer mismatch instead of uninstalling. Do not upload preview or QA APKs to the public page.

## Verified Android QA (November 17, 2026 ET / November 18 UTC)
- [Permanent Android PR quality gate 44212095728](https://github.com/PlayerEden/SudokuTogether/actions/runs/44212095728): **passed** Kotlin app / androidTest compilation, core and app JVM regressions, lint and debug assembly. Included `InstallerSharingEligibilityTest`.
- [Focused API 36 emulator run 44212095818](https://github.com/PlayerEden/SudokuTogether/actions/runs/44212095818): **5/5 `PublicReleaseCopyUiTest`** and **2/2 existing `ProgressBackupSettingsUiTest`**, zero skipped or failed. The emulator boot printed ordinary transient warnings, then ran all seven test methods and completed both Gradle instrumentation tasks successfully.
- An earlier run 44211775789 was **cancelled**, not failed, by the PR concurrency policy after a QA documentation commit; the final run above was started against the newer code/documentation revision and passed.
- Temporary dedicated PR workflow was removed after the green run; test files remain in the repository, and the permanent Android app CI gate remains active on future source changes.
- **No production-signed artifact was built**, no itch.io project was modified, and no family app installation or progress was touched by this work. Actual old-public-APK-to-new-public-APK signer continuity and owner phone display remain independent release gates.
