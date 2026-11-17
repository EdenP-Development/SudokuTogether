# GridMates: Sudoku — Beta 1.1 owner-phone playtest

**Status:** Pre-test plan; **not executed**, and **not a downloadable APK**. The current public itch.io release is still Beta 1. This checklist is for a future **same-signature QA update** of `com.playereden.sudokutogether.qa`, **not** the separate production package.

## 0. Protect existing progress before installing

1. On both devices, note the installed QA version, your names, your current Solo/Daily progress and the approximate number of Profile records/unlocked cosmetics. Keep a few screenshots for comparison.
2. When possible, use **Settings → Progress → Back up progress** to export a portable backup before the install. Keep that file safely outside the app. This export protects Solo/Daily, records and settings; it **does not transfer saved LAN room/rejoin material**.
3. Only use a QA artifact from the repository's [verified QA update workflow](https://github.com/PlayerEden/SudokuTogether/actions/workflows/qa-debug.yml) after a successful run on the **approved exact commit**. Check the accompanying receipt and SHA-256. It must identify the **existing QA package** `com.playereden.sudokutogether.qa`, the original QA signer, and a version code **greater than the installed code (Beta 1: 30)**.
4. Install **over** the existing QA app using Android's **Update** action. **Do not uninstall the app, clear storage, or install a generic debug build**—those may lose local records or fail signing continuity. The public itch.io app stays separate.

## 1. Quick solo / progress check (one phone)

- [ ] Reopen a previously started Solo board; verify its entered number, pencil note and difficulty survived.
- [ ] Enter and undo one legal number, background the app, and reopen; confirm the updated board persists.
- [ ] Open today's Daily puzzle and a previously saved unfinished Daily (if any). Verify Daily did not replace Solo.
- [ ] Visit Profile and Collection: prior results, earned icons, board styles and settings must still match the pre-update records.
- [ ] Try **How to play → Coach practice** and one real puzzle Coach hint; explanations and board focus should remain readable.

## 2. Local multiplayer check (two phones on the same Wi-Fi)

- [ ] Host a new **Co-op** room; discover/join on the second phone without unnecessary manual code entry (unless protection is enabled).
- [ ] Enter numbers and notes on both devices, use Undo, open Chat, and observe that shared progress updates correctly.
- [ ] Host **Versus** and join the second phone. Verify each phone shows **You** and **Rival** correctly and independent progress counters (never the rival's entered answers).
- [ ] Inspect the updated Versus nickname cards with a trailing emoji and with a longer name. No stranded emoji; long names should truncate reasonably. Also check large system text on at least one phone.
- [ ] Finish a Versus race: check winner and both progress counts **on both phones**, then use the host's **Next race** button. Confirm a new board opens for both players and old entries do not carry over.
- [ ] During a game, background one phone and return; if practical, disconnect/reconnect Wi-Fi and rejoin the original room. Neither device should silently replace the current board.

## 3. Basic polish / safety check

- [ ] Check narrow/landscape layout, scroll affordances, and board/keypad access with large font settings.
- [ ] Reopen Profile after a completed Co-op/Versus match and check one result is recorded (not duplicated).
- [ ] Exit and reopen both apps. Solo/Daily, room recovery cues and cosmetic selections should remain sensible.

## Playtest report (fill in with observed facts)

| Field | Your result |
| --- | --- |
| Exact GitHub Actions run / candidate source SHA | Pending |
| QA APK SHA-256, package, version code and signer receipt | Pending |
| Phone 1 model / Android version | Pending |
| Phone 2 model / Android version | Pending |
| Install-over: existing data retained? | Not tested |
| Solo/Daily/Coach/Collection | Not tested |
| Co-op join/edit/chat/undo | Not tested |
| Versus names/progress/finish/rematch | Not tested |
| Background / Wi-Fi disconnect and rejoin | Not tested |
| UI overflow, screenshot(s), unexpected behavior | Not tested |

**If a check fails:** keep the installation and its saved data intact; note the screen, action sequence, and versions and attach a screenshot. Avoid clearing app storage as a troubleshooting step. This checklist supplements [Beta 1 release evidence](GRIDMATES_V1_BETA_RELEASE.md) and [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43)—it does not claim any new physical tests have passed.
