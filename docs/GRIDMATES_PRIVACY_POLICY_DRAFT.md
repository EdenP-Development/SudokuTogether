# GridMates: Sudoku — PRIVACY POLICY DRAFT (not published)

**Preparation date:** November 17, 2026  
**Status:** Internal source-backed draft for future itch.io and Google Play posting. **Do not link from the live app or claim compliance until publisher details, contact, URL, exact public APK and wording are approved.**

## Information and data handling in the current app

**Offline play:** GridMates: Sudoku can play Solo puzzles and its locally generated Daily puzzle without signing in, creating an account or connecting to a hosted game server. The app saves player-selected name, puzzle state, pencil marks, hints/assistance, completions, achievements, cosmetic unlocks, preferences and recent play statistics in the app's private local storage. This data is needed to continue games and show rewards.

**Nearby multiplayer:** Co-op and Versus use a direct TCP connection on a shared Wi-Fi network. One participating device hosts a room and the second connects to it; nearby game discovery uses the local network. Your chosen nickname, player appearance, room/game state and optional messages you send in nearby chat are transmitted to the other player's device for the multiplayer session. Connected devices necessarily communicate using local network addresses. The current network protocol **does not encrypt the local Wi-Fi game/chat messages**, so use a Wi-Fi network you trust. Optional room codes limit joining but are not encryption. The game does not offer internet-wide remote accounts or cloud matchmaking in this version. A hosted multiplayer session may store room-state files locally on participating phones.

**App operator servers:** The current build has no developer-operated gameplay server for storing puzzle results, player accounts or nearby chat. We should confirm third-party dependency behavior and the actual release APK before asserting a comprehensive "no collection" position for Google Play Data safety.

**Local backup and restore:** Settings allows you to export an optional progress-backup file. You choose where to save/share that file; if you send it to a separate service, that service's privacy terms apply. It includes Solo/Daily progress, match records, cosmetic unlock ownership and preferences, but **not active host/guest multiplayer rooms or reconnection credentials**. Restore asks for an explicit confirmation and verifies imported data before replacing local progress.

**Retention and control:** Local app data remains on your device until you delete the app's data or uninstall; uninstalling may erase it. A manual backup should be saved in a safe location *before* switching devices or uninstalling. We don't offer account-based cloud recovery in the current release. You can delete local game data using Android app settings, subject to your saved external backup files being under your control. Other players may have their own copies of information you shared in a nearby game.

**Advertising, in-app purchases, analytics:** The checked source includes no advertising, billing or analytics SDK integration. If any of those systems are added later, this policy and any relevant platform disclosures need revision before the updated version is published. We should verify the shipped dependency manifest and live APK as part of release QA.

## Still required before publishing this as a live policy

- [ ] Confirm exact publisher/developer name and support/privacy contact address (not fabricated here).
- [ ] Choose effective date and stable HTTPS public URL (itch.io or owned site as appropriate); make it reachable without an account.
- [ ] Independently inspect the exact release package, Android permissions, network calls and dependency tree.
- [ ] Review Google Play definitions for data *collection* and *sharing*, especially ephemeral peer data and manually exported files; make the Data safety form consistent with the policy.
- [ ] Review the intended audience and relevant children's privacy requirements before setting a Play target audience.
- [ ] Link the policy from the public page and add an in-app link after final approval, not a broken placeholder.
- [ ] Verify that any future ad, purchase, remote multiplayer or external crash-reporting SDK changes are reflected in this policy before their release.

**Important:** This is a preparatory product document, not legal advice, a published privacy policy or an assertion that Google Play has reviewed the game.
