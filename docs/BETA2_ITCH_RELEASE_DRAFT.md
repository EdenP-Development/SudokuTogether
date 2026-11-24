# GridMates: Sudoku — Beta 2 itch.io release draft

Not published. Signed build 31 passed CI and owner reports successful visual review and same-version reinstall with progress retained. Ready for owner publication decision; higher-version update and cold restart not independently demonstrated.

## What's new
- Expanded Collections: 85 cosmetics across portraits, insignias, boards, inks and fonts.
- 47 achievements, including longer-term milestones and retroactive recognition of eligible previous gameplay.
- Refined collection illustrations and Versus presentation.
- Portable progress backup and restore in Settings.

## Important: one-time migration
This release is a separate installation because the original public Beta 1 signing key remains unavailable. Do not uninstall Beta 1 or clear its data.
1. Open Beta 1, go to Settings > Progress > Back up progress, and save the JSON somewhere accessible.
2. Install the new signed Beta 2 APK alongside Beta 1.
3. In Beta 2, open Settings > Progress > Restore backup and select that JSON.
4. Review the restore preview and confirm. This replaces destination offline progress; it does not merge saves.
5. Check profile, records, collections, settings and active puzzles. Close and reopen Beta 2 to confirm persistence.
6. Keep the original app and backup until satisfied. Never share your backup publicly.

If restore fails, keep both installations and report the issue without posting private save contents. Live multiplayer rooms and reconnect tokens are not migrated.

## Beta expectations
This is a public development beta, not the final Google Play product. Future itch.io beta updates are intended to retain the new package and signer. No promise of progress transfer into a future Google Play edition. Minor bugs and multiplayer feedback will inform Beta 3.

## Maintainer publication record

- Signed candidate: https://github.com/EdenP-Development/SudokuTogether/actions/runs/45487842212/artifacts/14962779795 (expires December 8, 2026).
- Build source: c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c.
- Package: com.playereden.gridmates.sudoku.beta; versionCode 31; versionName 1.0.0-beta.2.
- CI verified certificate, package, permissions and exact six artwork files.
- Owner reports positive visual review and same-version reinstall preserving progress. Earlier physical reviewer migration and automated 750-record migration passed. Do not claim higher-version update or complete production migration independently verified.
- Keep original key backup; independent owner backup remains unconfirmed.
- Preserve the legacy Beta 1 download and provide migration instructions. Keep a durable copy of the accepted APK and receipt; connector does not support uploading GitHub Release assets directly.
- Publication not yet performed. Owner must approve and upload to itch.io (or provide an authorized publishing capability).

## Suggested public announcement

GridMates: Sudoku Beta 2 is here! Explore an expanded Collection of 85 cosmetics, earn 47 achievements, enjoy refreshed illustrations and see your equipped companion appear in Solo. Progress backup and restore make it possible to bring your existing offline progress into the new beta installation.

Important for Beta 1 players: Beta 2 installs separately. Back up your progress in Beta 1 first, restore it in Beta 2, and verify your progress before removing the old app. Future beta updates are intended to use the new stable signing identity.

This is still a development beta. Feedback on gameplay, visuals, progression and local multiplayer is welcome as we work toward Beta 3.
