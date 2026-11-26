# GridMates: Sudoku — PRIVACY POLICY DRAFT (not published)

**Preparation date:** November 17, 2026 (revised November 25, 2026)  
**Status:** Internal source-backed draft for future itch.io, Amazon Appstore and Google Play posting. **Do not link from the live app or claim compliance until publisher details, contact, URL, exact public APK and wording are approved.**

## Information and data handling in the current app

**Offline play:** GridMates: Sudoku can play Solo puzzles and its locally generated Daily puzzle without signing in, creating an account or connecting to a hosted game server. The app saves player-selected name, puzzle state, pencil marks, hints/assistance, completions, achievements, cosmetic unlocks, preferences and recent play statistics in the app's private local storage. This data is needed to continue games and show rewards.

**Nearby multiplayer:** Co-op and Versus use a direct TCP connection on shared Wi-Fi. A participating device hosts the room and the other connects to it. Your chosen nickname, appearance, game state, approved Quick Chat messages, emote identifiers and (in Co-op only) temporary cell attention markers can be transmitted to your connected partner. The updated Beta 3 source removes unrestricted chat and advertises a generic GridMates room label rather than a nickname. Discovery still exposes a room identifier, local network endpoint, protocol version and room-code requirement to nearby devices before joining. Connected devices necessarily use local network addresses. Custom nicknames are still exchanged after joining and may be retained in saved multiplayer room state. Avoid using personal or identifying information in nicknames.

The host retains up to 30 recent approved messages/reactions in room memory, including for same-partner reconnection; replacing the partner or closing the room clears this history. Partner Ping markers are not saved or included in reconnect history. Players may temporarily hide incoming messages, reactions, unread indicators and explicit markers; hiding communication does not erase temporary room history or prevent ordinary partner selection from being shown. The current LAN game protocol is not encrypted. Use a trusted Wi-Fi network. Room codes are enabled by default for users without a saved preference; players can change this setting. Room codes restrict admission but do not encrypt traffic. No developer-operated matchmaking or internet-wide account service is identified in this version. These statements describe updated Beta 3 source and require verification against the exact delivered build, not earlier public builds.

**App operator servers:** The current build has no developer-operated gameplay server for storing puzzle results, player accounts or nearby chat. We should confirm third-party dependency behavior and the actual release APK before asserting a comprehensive "no collection" position for store privacy disclosures.

**Local backup and restore:** Settings allows you to export an optional progress-backup file. You choose where to save/share that file; if you send it to a separate service, that service's privacy terms apply. It includes Solo/Daily progress, match records, cosmetic unlock ownership and preferences, but **not active host/guest multiplayer rooms or reconnection credentials**. Restore asks for an explicit confirmation and verifies imported data before replacing local progress.

**Retention and control:** Local app data remains on your device until you delete the app's data or uninstall; uninstalling may erase it. A manual backup should be saved in a safe location *before* switching devices or uninstalling. We don't offer account-based cloud recovery in the current release. You can delete local game data using Android app settings, subject to your saved external backup files being under your control. Other players may have their own copies of information you shared in a nearby game.

**Advertising, in-app purchases, analytics:** The checked source includes no advertising, billing or analytics SDK integration. If any of those systems are added later, this policy and any relevant platform disclosures need revision before the updated version is published. We should verify the shipped dependency manifest and live APK as part of release QA.

**Store distribution:** If you obtain the game through Amazon Appstore or another storefront, that storefront may separately process information for its own account, delivery and platform services. Its privacy notices apply to those services. The exact store-delivered package and disclosures must be reviewed before this draft is published.

## Still required before publishing this as a live policy

- [ ] Confirm exact publisher/developer name and support/privacy contact address (not fabricated here).
- [ ] Choose effective date and stable HTTPS public URL (itch.io or owned site as appropriate); make it reachable without an account.
- [ ] Independently inspect the exact release package, Android permissions, network calls and dependency tree.
- [ ] Review Google Play definitions for data *collection* and *sharing*, especially ephemeral peer data and manually exported files; make the Data safety form consistent with the policy.
- [ ] Review the intended audience and relevant children's privacy requirements before setting any store target audience; review Amazon local chat and user-generated-content requirements as well as Google Play requirements.
- [ ] Link the policy from the public page and add an in-app link after final approval, not a broken placeholder.
- [ ] Verify that any future ad, purchase, remote multiplayer or external crash-reporting SDK changes are reflected in this policy before their release.

**Important:** This is a preparatory product document, not legal advice, a published privacy policy or an assertion that Amazon Appstore or Google Play has reviewed the game.
