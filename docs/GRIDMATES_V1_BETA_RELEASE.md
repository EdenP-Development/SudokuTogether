# GridMates: Sudoku v1 Beta 1

Play a classic 9×9 Sudoku, try the offline Daily puzzle, or connect two Android phones on the same Wi-Fi for a shared Co-op board or a Versus race. Coach practice teaches one move at a time. Finished puzzles unlock player icons, number styles, and boards.

## GitHub source integration — November 16, 2026

With the owner's approval, the consolidated [Beta 1 PR #42](https://github.com/PlayerEden/SudokuTogether/pull/42) was merged into `main` as [commit `9256a5f`](https://github.com/PlayerEden/SudokuTogether/commit/9256a5f239e232b9211aa5589a9e4383e96fea80). Its history includes the v0.11E–M development milestone commits. PR #33 was automatically recognized as merged, and drafts #34–#41 were closed as superseded after confirming their exact head commits occur in PR #42's commit history; their source branches remain available.

**Reproducibility note:** The public/QA APKs described below were built from **`76cbfc149569938b97286f3efe4ad44e08c5934f`**. Moving the source into `main` and subsequent documentation edits did **not** rebuild, retest or redistribute those APKs. The owner confirmed public itch.io and QA versions installed and launched on a real phone; independent checksum of the itch download and wider physical QA remain open in [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43). No GitHub tag/Release was created during consolidation.

## Beta 1.1 source/CI progress — November 16–17, 2026

These commits are **future Beta 1.1 source preparation** and **do not change the delivered Beta 1 public or QA APK**. The exact released Beta 1 artifact provenance and hashes below remain unchanged.

- [Core JVM CI PR #46](https://github.com/PlayerEden/SudokuTogether/pull/46) merged and [its first run passed](https://github.com/PlayerEden/SudokuTogether/actions/runs/43996910731). Path-filtered, 15-minute, read-only, unsigned test workflow.
- [Solver/Coach proof tests PR #45](https://github.com/PlayerEden/SudokuTogether/pull/45) merged after [full core JVM tests passed](https://github.com/PlayerEden/SudokuTogether/actions/runs/43997216944). Tests verify sample technique placements and Coach eliminations against unique puzzle solutions; these do not establish real-world difficulty calibration.
- [Versus name layout PR #44](https://github.com/PlayerEden/SudokuTogether/pull/44) merged as [`4cfe132`](https://github.com/PlayerEden/SudokuTogether/commit/4cfe1321d554833fde39a9ec639d8955666bb433). The nickname gets the card's full width below the portrait/role heading, to avoid an orphaned trailing emoji. A new focused Android UI workflow [completed 5/5 API 36 emulator tests successfully](https://github.com/PlayerEden/SudokuTogether/actions/runs/43999395717): 0 failures, 0 skipped, including 320-dp emoji, 200%-text layout, result/reconnect/rematch presentation. Initial workflow runs failed **before UI tests** owing to SDK command-path and shell command-format mistakes; both were corrected before the passing run.
- [Android CI maintenance PR #47](https://github.com/PlayerEden/SudokuTogether/pull/47) repaired explicit SDK-tool initialization in the three older **manually dispatched** Android build/QA/visual workflows, without changing signing, packages, distribution or gameplay.

**Not yet verified for Beta 1.1:** physical install-over of an update using the original QA signing identity; real-phone visual and two-phone reconnect/rematch regression; actual public APK hash cross-check; a newly produced QA APK; Play submission. The relevant steps remain in [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43). The existing [same-identity QA update workflow](https://github.com/PlayerEden/SudokuTogether/actions/workflows/qa-debug.yml) is manual-only and must use the original private QA key, baseline policy and a new version code. **Never reinstall/uninstall or clear player data merely to move to a new QA build.**

## Beta polish

- Home and the board count the squares players have filled. The board shows the count once above the grid.
- **How to play Sudoku** starts with three short instructions and a **Coach practice** button on Home. **More rules and controls** opens the full reference.
- **Your play** on Profile shows completed Co-op puzzles alongside Solo, Daily, and Versus results.

## Android downloads

| File | Use |
| --- | --- |
| `GridMates-Sudoku-v1-Beta-1.apk` | Public Android download for itch.io |
| `GridMates-Sudoku-v1-Beta-1-QA.apk` | In-place update for the owner's existing QA app |
| `GridMates-Sudoku-v1-Beta-1.aab` | App Bundle for a future Google Play submission |

The public app supports Android 8.0 and later and targets Android 16. The QA and public packages can be installed side by side. To transfer Solo and Daily progress, records, and settings, open **Settings → Progress → Back up progress** in QA, then choose **Restore backup** in the public app.

## Publishing

**itch.io publishing preparation:** [Copy-ready Beta 1 page, screenshots, AI disclosure and public APK device checklist](GRIDMATES_ITCH_IO_BETA_PUBLISHING_KIT.md). Keep the project Draft until the **public** APK launches successfully on a real phone and the itch download itself is checked; the Android emulator production-launch timeout remains unresolved in the report.



For itch.io, upload the public APK as a downloadable file and mark it for Android. Set the page visibility when the listing is ready. [Your first itch.io page](https://itch.io/docs/creators/getting-started) · [Android uploads](https://itch.io/updates/android-is-now-an-available-platform)

Google Play takes the App Bundle for a new app. Personal accounts created after November 13, 2023 need at least 12 testers opted into a closed test for 14 continuous days before applying for production access. To support install-over updates from itch.io to Play, supply the same production app-signing key when setting up Play App Signing. [Play release setup](https://support.google.com/googleplay/android-developer/answer/9859348) · [Personal-account testing](https://support.google.com/googleplay/android-developer/answer/14151465) · [App signing across stores](https://developer.android.com/studio/publish/app-signing)

## Owner device confirmation after itch.io upload (November 16, 2026)

The owner confirms that they **downloaded the public APK from the itch.io page, installed it and successfully launched it on their own Android phone**. They also confirmed installing and launching the **QA build**. This closes the *basic physical installation/launch smoke concern* raised by the earlier emulator timeout, **but it is a user-reported result, not independent verification of the exact downloadable file's checksum, signer, package/version, or all gameplay paths**. The earlier emulator timeouts remain historical QA evidence; this newer device confirmation does not retroactively change those test results.

The broader Solo/Daily, save/restore, two-phone Versus/reconnect, Android-version coverage and APK hash verification checklist remains open in [Sudoku Beta stabilization issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43). Device make/model, Android API and public APK hash were not supplied, so don't invent them or silently mark their checks complete.

## Physical Versus session — owner screenshots (November 16, 2026)

The owner shared **three Android screenshots from a Versus match played against their spouse**. On the owner's device the game displayed:
- An **Easy · Classic 9×9** Versus board, individual **local/rival** progress panels, and Game details/Chat controls.
- Early race progress changing from **0/45 each** to **4/45 local versus 2/45 rival**, consistent with independently updating player counts.
- A final **“Race complete / Your rival won”** state showing **40/45 local versus 45/45 rival**, the owner's unfinished grid captioned **“Your grid at the finish”**, and **“Your host can start another race.”**

**Evidence scope:** The owner confirmed this was the **Beta 1 QA app**, not the public itch.io APK. Both variants were built from the same reported Beta 1 game-source commit but have different application IDs, signatures and build configurations; similar code does not itself verify both packages' multiplayer runtime behavior. The screenshots document a real two-person QA session **from one device** with an observed rival victory and changing progress. They do **not** independently establish the QA APK's SHA-256, whether the second phone rendered the same final state, whether a next race started successfully, or disconnect/reconnect behavior. Do not close those outstanding physical regression gates on this evidence alone. [Track remaining tasks in issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43).

**Visual polish consideration:** A longer player name with a trailing emoji wrapped the emoji onto a separate line in the local participant panel. Consider testing long names/emoji/font scaling with a responsive card layout. The result screen is serviceable and readable; optional restrained finish/rematch emphasis can be explored without claiming a game bug.

## Build and package verification

- Source [`76cbfc149569938b97286f3efe4ad44e08c5934f`](https://github.com/PlayerEden/SudokuTogether/commit/76cbfc149569938b97286f3efe4ad44e08c5934f), built November 16, 2026.
- Production `com.playereden.sudokutogether` uses version `1.0.0-beta.1`. QA `com.playereden.sudokutogether.qa` uses `1.0.0-beta.1-qa.30`. Both are Android version code 30.
- 147 JVM tests and 13 APK preflight tests passed. Android lint reported zero errors and 17 warnings.
- The production APK and App Bundle matched the existing production signing identity. Bundletool validated the bundle and generated an installable APK with the same package and certificate. Both APK paths passed ZIP and 64-bit native-library alignment checks for 16 KB pages.
- The Android 8 emulator installed the exact beta QA APK over the delivered v0.11M APK. Both on-device APK hashes matched the delivered files, and Android kept the first-install timestamp. The profile hash matched after the update, and the saved Solo and Daily puzzles, notes, records, cosmetics, preferences, and code-protected Co-op room reopened.
- The three focused Android regressions reproduced the previous behavior on v0.11M and passed on Beta 1. Android checks also covered the Collection swipe, compact layouts at large text, Coach lessons, progress backup, and scripted Co-op and Versus.
- The broader Android 8 run passed 17 of 21 cases. One Coach replay case timed out after sending system Back. Three cases timed out waiting for window focus, covering Daily/Solo isolation, Daily history at large text, and Co-op entry and undo. Android logged stalls in System UI and the Telecom service. The exact production APK installed, and its standalone activity launch timed out. The emulator restart ended before completing those rechecks.
- Physical Versus testing and modern Android device coverage are deferred for this beta. The owner previously confirmed regular Co-op play on two phones.

| Artifact | SHA-256 |
| --- | --- |
| Public APK | `6d7dda34129fc4a6c74ab8146dcb36128967340558db50d9f7bac58cbe41c8c3` |
| QA APK | `0b630ccd5bca9f367325da373c36f1671646bb8e82ae520e651283ada25fdd59` |
| App Bundle | `a8b8af9e4773ae43582fd8739596d42ad22b1e5c865b4c6fe4c110d629e29765` |
