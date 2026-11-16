# GridMates: Sudoku — itch.io Beta 1 publishing kit

**Prepared:** November 16, 2026  
**Status:** Ready-to-copy project-page content and publication checklist; **no itch.io project/page has been created or published by this document**.  
**Verified source:** [PR #42 — GridMates: Sudoku v1 Beta 1](https://github.com/PlayerEden/SudokuTogether/pull/42), [Beta 1 release and QA report](GRIDMATES_V1_BETA_RELEASE.md), and the actual Android source on `feat/gridmates-v1-beta`.  
**Release intent:** Free **Android APK** Beta 1 on itch.io. Keep offline Solo/Daily, nearby same-Wi-Fi Co-op/Versus, and accessibility at the center; do not claim online matchmaking, Bluetooth, cloud saves, or a web/browser playable build.

## 1. Crucial publication gate — verify the public APK first

Current PR #42's report is substantial but **not an all-green public APK acceptance**. It records:
- **147 JVM tests** and **13 package preflight checks** passed; lint zero errors.
- Android 8 QA update installed in place over v0.11M, with local progress and data preserved.
- **17 of 21 broader UI cases passed** in the Android 8 emulator. Four timed out; Android's System UI/Telecom processes also stalled.
- **The production APK installed but standalone launcher activity timed out** in that emulator session; the aborted emulator reboot did not verify successful launch.
- Physical **Versus finish/rematch**, modern Android, TalkBack, daily rollover remain deferred.
- Owner has previously reported real two-phone Co-op play, but **not a specific current production Beta 1 two-device acceptance**.

**Required before public visibility:** on at least one real Android 8+ phone, install the **exact public APK**, open it from the launcher, start Solo, enter and erase a digit, open/complete Daily or resume, try Coach practice, open Profile/Collection, exit and reopen, verify no crash; and confirm the downloadable file is the signed public APK. Prefer a fresh installation on a spare phone or a backup-first install; do not uninstall the owner's QA app to test this. For a public beta advertising local multiplayer, use two phones with the same public build to host/join Co-op and, if possible, finish a Versus race and rematch. If unable to test full Versus, disclose limitations explicitly.

### What to upload

| File | Usage | Public? |
|---|---|---|
| `GridMates-Sudoku-v1-Beta-1.apk` | Standalone installable Android game, package `com.playereden.sudokutogether` | **YES: use this on itch.io** |
| `GridMates-Sudoku-v1-Beta-1-QA.apk` | Update for the owner's installed QA app, package `com.playereden.sudokutogether.qa` | **NO: do not upload for general players** |
| `GridMates-Sudoku-v1-Beta-1.aab` | Google Play App Bundle for later store submission | **NO: do not upload as an Android installer to itch.io** |

Reported public APK SHA-256: `6d7dda34129fc4a6c74ab8146dcb36128967340558db50d9f7bac58cbe41c8c3`. This hash is from the release report; it still needs to match the **actual downloaded artifact** before uploading.

The GitHub repository currently has **no GitHub Release file attached** for this candidate. Don't infer or invent a publicly accessible APK URL from the PR or filename. Retrieve the exact artifact from the Work chat's delivered package or build pipeline. If missing, request the exact signed public APK from Work—not a locally debug-signed replacement.

### Existing testers transferring progress

QA and public builds have different package IDs, so they **install side-by-side**; installing the public APK does not automatically move QA data. Use **QA app → Settings → Progress → Back up progress**, then **public app → Settings → Progress → Restore backup**. Test transfer with a non-critical copy before relying on it. Existing future public updates should preserve the production package ID, production signing key and increment `versionCode`, so users can install over the public version without data loss.

## 2. Recommended itch.io project configuration

| itch.io field | Proposed value / decision |
|---|---|
| Title | **GridMates: Sudoku** |
| URL slug | `gridmates-sudoku` if available; resulting URL depends on the owner's itch.io username |
| Classification | **Games** |
| Kind of project | **Downloadable** (NOT HTML / playable in browser) |
| Platform/file checkboxes | **Android** for the `.apk`; do NOT claim Windows, macOS, Linux or iOS |
| Release status | **In development** (Beta 1) |
| Price | **No payments** for a truly free public test, or **$0 / Donate** if the owner wants completely optional tips and has configured seller details; **do not add a paid minimum** without owner approval |
| Visibility during setup | **Draft** while editing and waiting for public-APK install verification |
| Visibility for initial small test | **Restricted** with a password or download keys if a small closed beta is desired |
| Visibility once ready | **Public**, perhaps unlisted initially, after all materials and a physical APK smoke test are ready |
| Comment/community | Enable comments for bug reports and feature feedback if the owner can moderate/respond; consider discussion board only later |
| Tags | Choose only supported/relevant suggestions such as **Sudoku**, **Puzzle**, **Singleplayer**, **Multiplayer**, **Android**, **Casual**, **Relaxing**, **Local Multiplayer** when offered (avoid claiming Online Multiplayer) |
| AI disclosure | **Complete itch.io's AI Disclosure accurately**: some illustrated animal portrait assets were generated with AI assistance and selected/processed for the game. Procedural Sudoku puzzle generation alone is conventional game logic, not generative-AI imagery. Declare actual applicable categories, don't falsely mark "no AI." |
| Language | **English** for the current UI if no additional localized strings are verified |

See official docs:
- https://itch.io/docs/creators/getting-started
- https://itch.io/docs/creators/access-control
- https://itch.io/docs/creators/pricing
- https://itch.io/docs/creators/quality-guidelines
- https://itch.io/updates/android-is-now-an-available-platform

**Important:** itch.io's visible menus and field labels can change; follow the current form. Account login and the creator's decision to make the project Public are manual actions. No direct itch.io publishing integration is connected in this conversation.

## 3. Copy-ready itch.io listing

### Page title
`GridMates: Sudoku`

### Short description
`A cozy, offline-first Sudoku game for Android. Solve puzzles solo, enjoy a daily challenge, or play together over local Wi-Fi.`

### Full description (paste and edit if physical QA changes the stated scope)

# GridMates: Sudoku — Beta 1

**Good puzzles. Good company.**

Settle in with a Sudoku puzzle, solve today's challenge, or invite someone nearby to play together.

**GridMates: Sudoku** is a cozy, offline-first Sudoku game for Android, inspired by the approachable feel of a classic paperback puzzle book. It's designed for easy, comfortable play—with enough depth to keep you coming back.

### What you can play

- **Solo Sudoku:** Choose Easy, Medium, or Hard. Make pencil notes, undo moves, and pick up where you left off.
- **Daily puzzle:** A fresh daily challenge that works offline.
- **Co-op:** Connect two Android phones on the same Wi-Fi and solve one shared board together.
- **Versus:** Compete on separate boards with the same starting puzzle and see who finishes first.
- **Sudoku Coach:** Get logical help when stuck, plus short practice lessons for solving techniques.
- **Personalize your play:** Earn player portraits, number colors and fonts, and board themes through play.
- **Your progress matters:** Keep local records and use the built-in progress backup/restore tool.

### Beta 1 — Android

This is an **early public beta**, not a finished commercial release. Your feedback can help shape the next version.

**Requirements:** Android 8.0 or newer. The download is an APK installed directly on your Android phone or tablet. Google Play installation is not available yet.

**Internet:** Solo and Daily play work offline. **Co-op and Versus require both phones on the same reachable local Wi-Fi network**, with matching game versions. There is currently **no online matchmaking or remote multiplayer**.

**Beta limitations:** The game is still being tested across different phones, accessibility settings, and multiplayer situations. Please report any crashes, installation problems, or multiplayer disconnects.

### Installation

1. Download `GridMates-Sudoku-v1-Beta-1.apk` on your Android phone.
2. Open the downloaded APK. If Android asks you to allow installations from the browser or Files app, review and enable that permission for the source you trust.
3. Follow the installation prompt, then open **Sudoku** from your home screen or app drawer.
4. You can turn off that install permission again afterward. The game does not require this permission once installed.

**Have an older public beta?** Normally you can install the newer signed public APK over the old one to preserve local data. Back up your progress before updating. Do not uninstall or clear the app's storage unless you have a verified backup.

### Help shape GridMates

If you try it, I'd love to hear what works and what needs improvement—especially whether the Sudoku board feels comfortable, whether the Coach explanations help, and how local multiplayer behaves on your phones.

Please leave feedback in this page's comments. Helpful bug reports include your Android version, phone model, what you were doing, and whether the problem can be repeated. Please don't post personal account information, IP addresses, or private room codes.

**Thanks for trying GridMates: Sudoku!**

---

### Optional small disclosure line (in addition to the actual itch AI field)

`Some collectible animal artwork was created with generative-AI assistance and then curated for the game. Sudoku puzzles and gameplay logic use locally implemented game algorithms.`

### Optional brief local-data notice (confirm no other data-processing SDKs before publishing)

`Solo puzzles, records, and cosmetic progress are saved locally on your device. Nearby Co-op and Versus exchange game data, player names, and optional chat with the connected phone over local Wi-Fi. No online account or cloud backup is included in this beta.`

Do not publish a sweeping “we collect no data” claim without auditing connectivity, logs, analytics libraries and future crash reporting. Do not imply this page is a substitute for a future Play Store privacy policy.

## 4. Image asset checklist

Do **not** put a random picture of a generic Sudoku app on the listing. Use real screenshots from this exact candidate on a real Android phone.

- **Cover image:** 315×250 minimum, **630×500 preferred** (itch.io's recommended 1.26:1 ratio). Show real GridMates identity and an attractive 9×9 grid; don't include a bogus “Play now” button or imply browser play.
- **3–5 screenshots:** (1) Home with Solo/Daily, (2) real Sudoku board with entry controls, (3) Coach practice with visible logical lesson, (4) Collection showing portraits/boards/fonts, (5) Co-op or Versus on two phones if tested and accurately represented.
- Use actual current UI; avoid outdated placeholder images. Mask private IP addresses, room codes, real chat names and personally identifying details.
- Check portrait screenshot framing on desktop itch page; keep real phone aspect ratio (don't stretch 9×9 grid). Add short captions if useful.

Official itch guide: https://itch.io/docs/creators/getting-started

## 5. Precise setup sequence

1. Log in to https://itch.io/dashboard (create and confirm an account if needed).
2. Choose **Create new game / New project**.
3. Set title, short description, `Downloadable`, `Games`, release status `In development`, and a free pricing option.
4. Paste the page description above; select Android and truthful tags/metadata; complete AI Disclosure accurately.
5. Upload **only** the exact public signed APK and select the **Android** platform checkbox for that file. Keep QA APK and AAB private.
6. Upload the cover and three to five honest screenshots.
7. Configure comments/feedback and look over the listing on desktop and on mobile. Keep **Draft** while incomplete.
8. Test the **download link itself** using a second phone/browser; download the APK from itch and verify checksum/package/signature if possible. Install and launch the downloaded copy on a real device; confirm progress/restore and basic features.
9. For a controlled first test, switch to **Restricted** and give friends access keys/password; otherwise switch to **Public** only after confirming the checks above. Once Public for the first time, the release enters itch discovery; don't waste the initial launch on an unfinished page.
10. Publish a brief Beta 1 devlog, invite feedback, and retain the exact APK, hashes and source commit for updates.
11. For future patches: update versionCode, preserve production package and signing key, retest data preservation, upload new APK and post a concise changelog.

Official access control: https://itch.io/docs/creators/access-control

## 6. Beta release notes for players

**GridMates: Sudoku — Beta 1**
- More consistent progress tracking on Home and during puzzles.
- Shorter first-time How to Play instructions with direct access to Coach practice.
- Co-op completions are included in your Profile history.
- Earlier late-stage features include the earned animal icon collections, number styles and board themes, expanded Coach lessons, Co-op shared-board themes, Versus rematches, and a swipeable Collection.

**Known limitations / testing needs:**
- Online matchmaking and Bluetooth-only play aren't included.
- Nearby multiplayer uses the **same Wi-Fi** network on both devices.
- Some Android UI cases timed out on the release emulator run; the public APK's real-device launch must be checked before going Public.
- Modern-device accessibility, physical Versus rematch and edge-case connectivity remain under testing.

## 7. Launch verification boxes

- [ ] Acquire actual signed **public** `.apk` (not QA / debug / AAB).
- [ ] Verify its SHA-256 matches the release report.
- [ ] Install/launch on real Android phone; play Solo/Daily, reopen save; test Coach/Collection.
- [ ] Test two public APKs on the same Wi-Fi for Co-op and Versus where possible.
- [ ] Test direct itch download after upload and install the downloaded package.
- [ ] Include clear Android-only and local-Wi-Fi-only wording.
- [ ] Use current, rights-cleared cover and 3–5 screenshots, no private room code/IP.
- [ ] Complete required itch AI disclosure honestly.
- [ ] Select **No payments** or **Donate** intentionally, with no surprise paywall.
- [ ] Decide Draft/Restricted/Public explicitly and test the resulting visibility.
- [ ] Prepare feedback route (itch comments), archive artifact/source/hash and note known limitations.
- [ ] Keep the production signing keystore and its secure backup private, outside GitHub/public downloads. Never share passwords or keystores.

**Ready to post is not equal to already published.** Human account access, APK upload, original screenshots and the final Public switch remain with the owner or a supported browser workflow explicitly authorized by the owner.
