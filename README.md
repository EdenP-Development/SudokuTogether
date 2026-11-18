# GridMates: Sudoku

**Release status — November 17, 2026 (US Eastern):** **Beta 1 remains the public Android release on itch.io.** The `main` source is preparing Beta 2 (`1.0.0-beta.2`, versionCode **31**) with permanent earned-Collection ownership and consumer-facing polish. It has **not** been distributed as a public Beta 2 update. The first production signer check [blocked safely](https://github.com/PlayerEden/SudokuTogether/actions/runs/44214878784) because the original signing secrets are unavailable. [PR #71](https://github.com/PlayerEden/SudokuTogether/pull/71) additionally prevents automatic uploading of a release-candidate artifact from this public repository.

**What we have verified:** The original Beta 1 source and Beta 2 source passed an [Android install-over rehearsal](docs/BETA2_ACTUAL_APK_INSTALL_OVER_QA.md) with the **same temporary debug signer**, preserving Solo/Daily progress, notes, records, preferences and earned cosmetics. [Beta 2 UI QA](docs/BETA2_PLAYER_FACING_RELEASE_QA.md) also passed for About, restore information and installer-sharing eligibility. **These tests are not proof of a production-signed upgrade over the real itch.io Beta 1 APK.** To publish safely, recover the original key, verify the exact old and new APKs, and run an actual production-signed update and owner-phone review. Do not uninstall the existing game to bypass an Android signature mismatch.

**Player-facing release preparation:** [Unpublished Beta 2 changelog](docs/GRIDMATES_BETA2_CHANGELOG_DRAFT.md) · [Itch listing draft](docs/GRIDMATES_ITCH_BETA2_LISTING_DRAFT.md) · [Production signing gate](docs/BETA2_PRODUCTION_SIGNING_GATE.md) · [Current handoff](CONTINUE_HERE.md) · [Roadmap](docs/GRIDMATES_DELIVERY_PLAN.md).

The public Beta 1 APK was built from source [`76cbfc1`](https://github.com/PlayerEden/SudokuTogether/commit/76cbfc149569938b97286f3efe4ad44e08c5934f). Preview and QA apps use separate Android packages/data, and must not be described as compatible updates of public Beta 1. Existing players should use **Settings → Progress → Back up progress** before moving devices or applying a verified future public update.

---

## Development history (historical checkpoints, not current release status)

**Beta 1.1 quality work (November 16, 2026):** [PR #46](https://github.com/PlayerEden/SudokuTogether/pull/46) introduced a lightweight, secret-free, core-only [GitHub Actions test workflow](.github/workflows/sudoku-core-ci.yml) (path-filtered, 15-minute cap); its [first run passed](https://github.com/PlayerEden/SudokuTogether/actions/runs/43996910731). [PR #45](https://github.com/PlayerEden/SudokuTogether/pull/45) added solution-consistency checks for human grading and Coach; [its core test run also passed](https://github.com/PlayerEden/SudokuTogether/actions/runs/43997216944) and the test-only change was merged. The [Versus nickname/emoji UI improvement in PR #44](https://github.com/PlayerEden/SudokuTogether/pull/44) is now merged to `main`: its [API 36 Android instrumentation passed 5/5 tests](https://github.com/PlayerEden/SudokuTogether/actions/runs/43999395717), including names, large text and Versus results. [PR #47](https://github.com/PlayerEden/SudokuTogether/pull/47) also repaired Android SDK initialization in the three manual build/QA workflows. The [emoji-safe names and saved Versus-room restart checks in PR #48](https://github.com/PlayerEden/SudokuTogether/pull/48) were also merged after [a passing full core CI run](https://github.com/PlayerEden/SudokuTogether/actions/runs/44001131055). **Real-phone Beta 1.1 visual, install-over, Co-op and rematch tests are still pending.** The planned [owner phone playtest steps](docs/BETA_1_1_OWNER_PHONE_PLAYTEST.md) are available now. A separate [Beta 1.1 **Sudoku Preview** APK](https://github.com/PlayerEden/SudokuTogether/actions/runs/44003023663) is now available for side-by-side phone testing (package `com.playereden.sudokutogether.qa.preview`, **not** an update for existing QA saves). Its full core/Android unit/lint build and APK identity checks passed. The original QA install-over build is blocked because the repository does not have the original QA signing secret configured. The published itch.io production app and installed QA package remain Beta 1. Full checklist: [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43).

**v1 Beta 1** brings a consistent board progress count, shorter help with direct access to Coach practice, and Co-op solves on Profile. [Beta packages and publishing](docs/GRIDMATES_V1_BETA_RELEASE.md)

**v0.11D QA** adds both players' identities and progress to the Versus screen, reconnect feedback, and consistent bundled number styles. The signed update uses the installed Sudoku QA app's identity. [Repository audit and upgrade evidence](docs/V0.11D_INTEGRATION_AND_QA.md)

**v0.11M** adds a swipeable row for player icon sets in Collection. [Collection swipe and signed update QA](docs/V0.11M_COLLECTION_SWIPE_QA.md)

**v0.11L** adds four Garden portraits, three number colors, and the Rounded and Pencil fonts. Existing completed games count toward the new unlocks. [Garden collection and update QA](docs/V0.11L_GARDEN_COSMETICS_QA.md)

**v0.11K** brings a compact Profile card, available items above locked ones in Collection, board thumbnails, and a choice of number colors or fonts. [Profile and Collection QA](docs/V0.11K_PROFILE_COLLECTION_QA.md)

**v0.11I** adds six one-move lessons in Coach practice. Open **How to play → Coach practice** from Home. [Practice and signed upgrade evidence](docs/V0.11I_COACH_PRACTICE_QA.md)

**v0.11H** adds hidden-pair and X-Wing explanations, a direct return to the suggested square and an optional one-number reveal on a stalled board. [Coach and signed upgrade evidence](docs/V0.11H_COACH_DEPTH_QA.md)

**v0.11G** adds Coach proof steps and six Ocean portraits. [QA evidence](docs/V0.11G_COACH_OCEAN_QA.md) · [Delivery plan](docs/GRIDMATES_DELIVERY_PLAN.md)

**v0.11F** introduced shared Co-op themes, Versus finish summaries and a same-room rematch. [QA evidence](docs/V0.11F_LOCAL_MULTIPLAYER_QA.md)

**v0.11E** introduced Profile and Collection, portable progress backup, and the Android 16 build and signing path. [Candidate PR #33](https://github.com/PlayerEden/SudokuTogether/pull/33) · [QA evidence](docs/V0.11E_PROFILE_PROGRESS_QA.md)

## Earlier QA milestones

> **v0.11C rollback QA** introduced open nearby joining, optional remembered code protection and 12 earned Woodland portraits. [Native QA audit](docs/V0.11C_NEARBY_WOODLAND_QA.md) · [APK receipt](docs/V0.11C_DELIVERY_RECEIPT.md)

> **v0.11B rollback QA** on [feat/gridmates-coach-launcher](https://github.com/PlayerEden/SudokuTogether/tree/feat/gridmates-coach-launcher), stacked on v0.11A. Launcher **Sudoku**, full **GridMates: Sudoku** brand retained; explicit Solo/Daily entry checks with saved assistance. **103 JVM +46 API26 native cases passed**, lint0 errors/14 warnings, exact17→18 update verified. [QA audit](docs/V0.11B_COACH_LAUNCHER_QA.md) · [Delivery plan](docs/GRIDMATES_DELIVERY_PLAN.md) · [Recovery log](docs/V0.11B_RECOVERY_LOG.md). Main/development and earlier checkpoints remain unchanged.

[v0.11A GridMates identity and concise UI — draft PR26](https://github.com/PlayerEden/SudokuTogether/pull/26)

> **Earlier identity milestone: GridMates v0.11A**, on [`feat/gridmates-brand-copy`](https://github.com/PlayerEden/SudokuTogether/tree/feat/gridmates-brand-copy). [Brand/copy audit](docs/V0.11A_GRIDMATES_QA.md). The approved public name is GridMates: Sudoku; logo concepts remain provisional. The earlier v0.10D source is preserved below.

> **v0.11A QA:** version17 / `0.11.0-dev1-qa`; 87 JVM tests and 31 accepted API26 native cases passed, lint0 errors/20 warnings. The exact APK updated v0.10D while preserving the seeded profile, active puzzle/note, identity and preferences. [QA checkpoint](https://github.com/PlayerEden/SudokuTogether/tree/checkpoint/v0.11a-gridmates-qa-green). Two-phone/device release gates remain open.

> **Earlier v0.10D Visual QA**, stacked on the verified v0.10C source. [Visual QA audit](docs/V0.10D_VISUAL_QA.md)

> **Changes:** a cohesive native game theme and icon; immediate Solo Resume and Daily access; Play/Records/Collection/Settings navigation; cosmetic previews; framed grid, clearer controls, adaptive landscape/large-text layouts, improved results and Join keyboard flow. QA `0.10.0-dev4-qa`  / code16. The v0.10C save integrity work remains intact. 87 JVM tests and 42 API26 native cases passed; lint 0 errors/18 warnings. Both independent-review regressions are GREEN after observed RED fixtures. Physical two-phone testing remains deferred; this is not a Play release.

> **Historical notes below** document the original v0.1–v0.4 development stages, including test-status statements from those stages. They are preserved for traceability, not the current build status.

A native Android Sudoku game built for **solo, local Wi-Fi co-op, and head-to-head racing**. Co-op and versus use two phones: one hosts, the other joins on the same home Wi-Fi. Solo and local personal records work offline. A cloud account, internet rooms, and synced leaderboards are **not** included.

GridMates: Sudoku is the approved public name. The logo is provisional; see the [identity brief](docs/GRIDMATES_VISUAL_IDENTITY_BRIEF.md). The game uses its own implementation and original generated puzzles.

## v0.4 experimental: player badges and earned milestones

The stacked `feat/player-identity-v0.4` branch adds **six earned player emblems**, **nine achievements with progress**, compact opponent identity cards, and a post-game co-op summary showing each partner's *final filled entries*. Achievements derive from locally saved completed games; they are not verified by an online server. Player-selected emblems are now exchanged over protocol **v4** on matching Android `0.4.0-dev` APKs.

Players can enable **Classic number display** to show all entries in a uniform, readable style only on their own device. The cosmetic identity still belongs to the author in the underlying game state. This pass also repairs unread-chat counts while the chat sheet is open and expands local record retention to 10,000 finished games.

**Development status:** source and new test cases committed, with a static source pass that caught and fixed two missing Kotlin parentheses. The app has **not** been compiled, signed, or physically tested on two phones in this environment. [Detailed v0.4 handoff and QA](docs/V0.4_HANDOFF.md).

## v0.3 experimental: player identity and local chat

The stacked `feat/chat-cosmetics-v0.3` branch introduces an optional collapsible room chat for co-op and versus, with quick phrases and a 30-line memory-only chat history. It also adds earned number colors, local board backgrounds and built-in font choices under **My profile & records**.

In co-op, numbers written by each player carry that player's equipped color and number font. The starting clues stay neutral; undo restores the previous contributor's style. In versus, an opponent's equipped cosmetic set is visible as a small identity label, but their private solution entries remain hidden. Profile unlocks are earned from device-local saved achievements, not online accounts or a store.

**Status:** source and tests have been authored; compilation, Android tests, signing and two-phone acceptance are pending. Wi-Fi protocol **v3** and Android `0.3.0-dev` require both players to upgrade together. [Implementation and QA handoff](docs/V0.3_HANDOFF.md).

## v0.2 development branch: solo, versus, and player records

Branch: `feat/solo-profiles-race-v0.2` (based on the existing v0.1 co-op branch).

- **Solo:** Standard 9×9 Sudoku, pencil marks, erase, undo, running active-play timer, resume after leaving, and save-on-edit. Solo progress is stored separately from any hosted multiplayer room.
- **Versus:** host or join a same-Wi-Fi race; each phone gets the same starting clues on its own independent grid; the host accepts the first fully valid solution as the winner. Both players see fill counts, **never the opponent's values or notes**. Games pause if a phone leaves; host can create a fresh race, resume the room, or replace the rival.
- **Personal profile:** solved count, best solo time, accumulated solo solving time, head-to-head W/L/D, co-op completions and recent results. Records and a random local profile ID are persisted atomically on the device and duplicate completions are prevented.
- Existing v0.1 co-op remains the default `Host a puzzle` option, with `Host a versus race` offered separately.

**Development status:** Source and regression/smoke tests are committed, but the new Android build and two-phone v0.2 acceptance tests **have not yet run**. Do not treat this branch as a validated release APK. All v0.2 test phones must install the same updated APK because the LAN wire protocol advances to version 2. The Android versionCode is 2 (`0.2.0-dev`).

**No cloud sync yet:** local stats survive ordinary restarts and upgrades using the same package/signing key, but clearing app storage or uninstalling can erase them. Sync, sign-in, account recovery, and leaderboards require a separate secured service in a later milestone. See [v0.2 handoff](docs/V0.2_HANDOFF.md).

## First test version (v0.1 co-op baseline)

- Simultaneous shared entries and pencil marks.
- Nearby-room discovery, a six-digit room code, and manual IP/port fallback.
- Partner selection markers and shared conflict highlighting.
- Own-move undo that preserves a partner's subsequent edits.
- Automatic host saves, backup recovery, and credential-backed guest rejoining.
- Eight original Standard puzzles generated locally from bundled seeds.
- An in-app **Share APK** action to send the installed app using Android's sharing menu.

## Play on two phones

1. Install the same accepted QA APK on both Android phones (Android 8 or newer). If Sudoku is already installed, open the APK and choose **Update** to keep your progress.
2. Connect both phones to the same home Wi-Fi. Guest networks with device isolation can block connections.
3. Host a co-op puzzle or Versus race on one phone. On the other, choose **Join a game** and select the nearby room. Enter the displayed code if the host enabled **Require a code**.
4. If the room is missing from the list, enter the complete host address, including its port. Supply the code for a protected room.
5. Both people can enter numbers and shared notes freely. Starting clues stay fixed. Duplicate numbers are highlighted; wrong guesses do not end the game.
6. Keep both apps visible while playing. If either phone leaves the game, entries pause. Return to the apps, or use **Reconnect** and select the host's room again. The host can reopen **Resume your puzzle** after a restart.

Play multiplayer on a trusted home network. Optional room codes control who can join.

## Build

Install JDK 17, Android SDK Platform 36 and Build Tools 35.0.0. Set `ANDROID_HOME` to the SDK directory (or use an untracked `local.properties`).

```bash
./gradlew :core:test :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
```

For an update to the installed Sudoku QA app, follow the original-signer and increasing-version checks in the [QA update guide](docs/GRIDMATES_UPDATE_AND_SAVE_PREPARATION.md). The ordinary developer build above uses the local debug signer. The manual QA workflow builds the selected revision and verifies APK identity. The production signing workflow serves the separate production app. [Actions](https://github.com/PlayerEden/SudokuTogether/actions) · [Development notes](docs/DEVELOPMENT.md)

## Project records

- [Approved design](docs/superpowers/specs/2026-10-25-sudoku-together-design.md)
- [Implementation plan](docs/superpowers/plans/2026-10-25-local-coop-implementation.md)
- [Verification and device checklist](docs/TESTING.md)
- [Development and signing](docs/DEVELOPMENT.md)
