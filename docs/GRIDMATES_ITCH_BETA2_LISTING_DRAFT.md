# GridMates: Sudoku — itch.io Beta 2 listing and update draft

**Status:** Draft for the owner. **Do not publish or upload a new APK until it is signed with the original public certificate and an install-over test passes.** November 17, 2026.

## Short description

A cozy Sudoku game for Android. Solve puzzles solo, enjoy a Daily challenge, or play Co-op and Versus on the same Wi-Fi. Good puzzles. Good company.

## Public page description (copy and edit before release)

# GridMates: Sudoku — Beta 2

**Good puzzles. Good company.**

Take a little time for a good puzzle. Play at your own pace, challenge a friend nearby, and make your Sudoku collection your own.

GridMates: Sudoku brings a traditional paperback-puzzle feeling to Android, with cream pages, forest-green accents, a helpful Sudoku Coach and charming collectible animals.

### What's in the game

- **Classic Sudoku:** Easy, Medium and Hard 9×9 puzzles, pencil notes, Undo, and a choice of number and board styles.
- **Daily puzzles:** One locally generated Daily challenge, with history that doesn't punish missed days.
- **Sudoku Coach:** Logical help and short practice lessons when you need a hand.
- **Nearby Co-op:** Share one Sudoku board between two Android phones on the same Wi-Fi.
- **Nearby Versus:** Race a friend on matching puzzles, with separate boards.
- **Your Collection:** Earn animal portraits, board designs, number colors and fonts as you play.
- **Local progress:** Continue unfinished Solo/Daily puzzles and export or restore a progress-backup file.

**No account required.** Solo and Daily play work without internet access. Nearby multiplayer connects the two phones directly over a shared, reachable local Wi-Fi network. Internet matchmaking and Bluetooth-only play are not included in Beta 2.

### What's improved in Beta 2

- More understandable pencil marks, Sudoku feedback and menu navigation.
- More useful Profile and Collection previews, Daily history and puzzle-completion screens.
- Previously earned cosmetic items are retained as permanent local ownership even if older game records are eventually trimmed.
- Clearer progress-backup and restore guidance.
- Improved compatibility checks for existing saves and future updates.

### Download and install

Requires **Android 8.0 or newer**. Download the **publicly signed Android APK**. Open it from your phone's Downloads/Files app and review Android's install permission prompt.

**Already using the public Beta 1?** Install the **new public Beta 2 APK over the existing game**; don't uninstall it first. Compatible in-place updates normally preserve your local profile, unfinished puzzles and earned Collection. For extra safety, open the older app and use **Settings → Progress → Back up progress** before updating. If the new APK says the signatures don't match or Android refuses the update, **stop**—do not uninstall or clear data to bypass the error. Contact the game's publisher using the support channel on this itch page.

QA/preview APKs and public APKs are different installations. If you're moving from a QA preview or to another phone, use an exported progress backup and its Restore feature rather than assuming your data will transfer automatically. Current portable backups don't include live nearby multiplayer rooms.

### Feedback

The game is actively being refined. Please use the itch.io page comments to report crashes, difficult-to-read screens or feature problems. Your Android version, device model, and steps to reproduce a problem are useful; avoid posting private chat, IP addresses or room codes.

### Privacy and local play

Player profiles, puzzles, notes, completion records and earned cosmetics are saved locally on the device. Nearby multiplayer exchanges chosen names, game-state information and optional chat with the other phone over **unencrypted local Wi-Fi**. Use a trusted network. There are no developer-operated gameplay accounts or hosted matchmaking servers in this version. Manual backup files go wherever the player chooses to save or share them.

**Privacy policy:** Owner to review and publish the full document and add its URL here before any Play Store application. Do not leave this placeholder line on the public page; replace it with the actual URL when approved, or use only the accurate local-data summary above for a limited itch beta.

## Upload checklist — not yet completed

- [ ] Confirm the current public Beta 1 APK downloaded from itch.io, its certificate and baseline SHA-256.
- [ ] Verify original production keystore is available privately. The public package is `com.playereden.sudokutogether`; Beta 2 must carry the **same production signature**, versionCode **31** (from 30), versionName `1.0.0-beta.2`.
- [ ] Build and inspect final signed APK; run compatible install-over with preserved user records, active Solo/Daily, pencil marks, preferences and cosmetic ownership.
- [ ] Run basic gameplay/Coach/Profile/Collection/About/backup tests on the final release APK; check first launch and Android 8+ device support.
- [ ] Upload only the signed public APK (not the QA/debug build or Google Play App Bundle).
- [ ] Use real current phone screenshots; remove private IPs and room codes. Add a cover, truthful platform metadata, genre/tags and AI disclosure where applicable.
- [ ] Confirm publisher support/contact and privacy policy disclosures. Do not invent a support address or policy URL.
- [ ] Prepare a short Beta 2 devlog and test the itch download *after uploading* while the page is restricted/draft.
- [ ] Publish when the owner approves. Keep Beta 1 users' data safe and provide a clear update path.

**Current technical evidence:** [Beta 1 → Beta 2 Android install-over rehearsal](BETA2_ACTUAL_APK_INSTALL_OVER_QA.md) passed with the same *temporary* test signature in both versions. This is valuable update compatibility evidence but **is not** a verified production-signature update and must not be described as such.
