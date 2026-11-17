# GridMates: Sudoku — itch.io release hardening, save continuity and collection pacing

**November 17, 2026 · Source-backed plan for Beta 2 · Not a published release**

## Owner decisions and guardrails
1. Defer formal **two-physical-phone regression** until the owner elects to repeat it. Prior family Co-op sessions and owner Versus screenshots confirm meaningful successful use; don't call LAN broken or put the unrequested testing ahead of save preservation.
2. **Protect installed player progress above all other concerns.** Never uninstall or ask the owner/family to clear app data to install a build. Review/QA APKs with alternate package IDs are separate profiles, **not upgrade evidence**. For the next public update, use the same production package `com.playereden.sudokutogether`, **original production certificate**, and an increasing version code. No debug-signing replacement.
3. Keep the game modest and player-first: quiet nostalgic puzzle-book UI, readable Sudoku, no forced account, no ads in the middle of puzzles, and no artificial daily-pressure streak mechanic.
4. Keep existing 50 cosmetic IDs and thresholds; do **not** relock any earned item during rebalancing. The in-progress ownership-ledger PR is a prerequisite to introducing a larger catalog.

## Current collection pacing — calculated from November 17 Cosmetics.kt
There are **50 curated items**: 22 portraits, 6 emblems, 7 boards, 10 number colors, 5 fonts.
The numbers below assume a fresh profile playing **only Solo** with no Co-op, Versus or Daily; a Solo solve counts as both a Solo and any-finish result. They are exact implications of today's milestone rules, not usage analytics.

| Solo puzzles finished | Available cosmetics, including 9 starters | Percent |
| --- | ---: | ---: |
| 0 | 9 | 18% |
| 1 | 13 | 26% |
| 2 | 16 | 32% |
| 3 | 18 | 36% |
| 5 | **25** | **50%** |
| 8 | 30 | 60% |
| 10 | 33 | 66% |
| 15 | 37 | 74% |
| 20 | 40 | 80% |
| 35 | 42 | 84% |
| 50 | 43 | 86% |
| 75 | 44 | 88% |

The remaining 6 items depend on Co-op/Versus outcomes. This explains why an active player can earn roughly half the catalog in one day even after resetting. This is a **content balance consideration**, not an error in unlock calculations.

## Proposed long-term distribution for **new** collections
The existing collection stays intact, serving as the generous introduction. Before publishing any new themed bundle, verify rarity, future ownership, asset registry and honest progress text. A **12-item thematic pack** (for example Fall, Winter, Castle, Jungle) could use:

| Band | Items in pack | Suggested requirement | Design intent |
| --- | ---: | --- | --- |
| Welcome | 1 | Available immediately | Players can try the theme without earning first |
| Early | 2 | 3–12 total finishes | Quick encouragement |
| Regular | 4 | 15–60 total finishes / distinct Daily completions | A reason to return across weeks |
| Dedicated | 3 | 75–150 total finishes / long-term Daily totals | Aspirational but reasonably attainable |
| Prestige | 2 | 175–250 total finishes or an unusual opt-in achievement | A small number of genuinely rare rewards |

**All numbers above are proposals, not thresholds applied in code.** Keep at least 75% of the pack attainable through Solo or Daily, not only via LAN. Multiple types of progress are more welcoming than endless finish-count grinding: distinct Daily dates completed (not consecutive streaks), Solo puzzle totals, special Coach-practice milestones, and optional friendly Co-op/Versus rewards. Rare cosmetic goals should remain optional, visible, and never affect puzzle fairness. Don't lock major legibility/accessibility settings behind grind. Avoid weekly expiry or lost rewards if someone skips days.

**Permanent-ownership first:** Store earned IDs separately from the rolling `MAX_RECORDS = 10_000` match log. Award existing already-earned IDs on first compatible update without clearing local profile or finished boards, and persist newly awarded IDs during the normal atomic save. Include the same ledger in validated portable progress backup and restore. Version migration must preserve older JSON files and older backup payloads. Never silently lower previously earned ownership after tuning. Later gifts/code claims need their own idempotent proof, and lifetime counters may be required beyond pruned history.

## Non-destructive public update path
- Build and test exact `main` source, pin full Git SHA, version and APK SHA-256.
- Beta 1 public baseline uses package `com.playereden.sudokutogether` and source `76cbfc1`; verify the **actual itch-downloaded** installed APK's certificate and hash against the recorded receipt. Do not assume a passing isolated QA APK shares that identity.
- Prepare next candidate as **versionCode 31** / **1.0.0-beta.2**; default Beta 1 had versionCode 30. Keep a distinct application ID for owner visual previews to protect installed family saves.
- Publish a properly signed build **only with the original production signing key**, and verify certificate fingerprint matches the release baseline. If key is unavailable, stop release rather than generate a replacement. Android install-over with the same package/certificate and higher versionCode should preserve app data; prove this with a controlled before/after test.
- On an independent test installation, save unfinished Solo with pencil marks, complete a Daily, equip earned animal/board/ink, export a backup, then perform `adb install -r` of the real update APK. Verify player identity, both saves, history, styles, current difficulty and manual restore. Preserve evidence and confirm user data before uploading.
- For itch, upload verified **APK, not QA/review APK**. Link release notes and accurate Android installation/update instructions. Older versions should not be silently overwritten with different signing certificates. Don't distribute the AAB as a sideload package.
- Keep local progress export prominently available in Settings. Users moving to a new installation or device must manually export/restore progress; active LAN rooms do not travel in this backup. Do not claim it makes automatic cloud backups.
- Source already targets API 36, with min API 26. Google Play will separately need current policy declarations, contact and privacy URL, Play app signing plan, store images, eligible testing track, and review of any added SDKs.

## Player-facing presentation review
- The release app should be called **GridMates: Sudoku** with a useful version shown in About, and clear Play / Profile / Collection / Settings navigation.
- About & Progress should explain offline Solo/Daily, same-Wi-Fi Nearby play, local progress, backup boundaries, and trusted Wi-Fi (raw LAN chat/game traffic is **not encrypted**). No fake privacy-policy or support URL.
- Check first-launch without existing saves, portrait/landscape, phone fonts, About dialog scroll, backup buttons, Notes and Coach, Collection previews, reward results, and long-running saved progress. Detect accidental placeholder/developer-facing text and technical exceptions on each release candidate.
- Retain the intentional breathing room between Sudoku board and number dock. No need to fill it with decorative material yet.
- Maintain a single short changelog in the itch page explaining what changed without exposing internal GitHub milestone jargon or CI/test build names.

## Public privacy policy preparation (not yet a public URL)
The manifest declares INTERNET, NETWORK_STATE, WIFI_STATE and multicast-related Wi-Fi access for local multiplayer. Source includes no ad/analytics/Billing SDK at this checkpoint. The game stores a local profile, player name, puzzle states and records, settings and optional local LAN chat messages. In Nearby mode other participants' devices receive peer data and may see their IPs; gameplay/chat transport is plain TCP over local Wi-Fi, not encrypted. A signed public privacy policy must accurately reflect all of this. Don't claim "we collect no information" without assessing Google Play Data safety's scope for ephemeral peer transmissions. The owner must choose the publisher/support contact and publish a stable URL before any Play submission. Full initial draft in `docs/GRIDMATES_PRIVACY_POLICY_DRAFT.md`.

## P2P/network decision
Existing game **already uses direct host-to-guest TCP on the same Wi-Fi**. No dedicated web/game server is involved. That is sufficient for the initial itch.io release. Internet-wide P2P between different homes can sometimes use router port forwarding or ICE/STUN, but reliable cross-network discovery/NAT traversal typically needs some signaling service and sometimes TURN relay. It is not reliably "zero servers" for ordinary users. Don't promise internet rooms or Bluetooth until implemented, tested and scoped; no changes to existing LAN protocol in this phase.

## Gates before pressing Publish
- No lost progress in controlled signed install-over, no incompatible version/downgrade.
- Owned unlocks and backup/restore verified through old saves (automation + emulator + owner expectation).
- Public APK certificate/version/SHA and first-launch smoke validated, and original app data preserved.
- Human review of public-facing screens and text; privacy/update links and release notes accurate.
- No obligation to repeat two-phone testing immediately; keep optional owner gate documented, do not represent untested post-update LAN changes as physically approved.
