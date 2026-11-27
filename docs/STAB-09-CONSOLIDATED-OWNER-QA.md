# STAB-09 — Consolidated owner QA matrix

Prepared 2026-11-27 under STAB-08.02. Not executed. Candidate source, APK SHA-256, package, certificate, version and device details must be filled before acceptance. Latest build-verified code 8e2dcee; integrated runtime 45962562834 still executing at inspection. Do not install unsigned artifacts.

## Test setup
Use one isolated QA candidate where feasible, plus second device for LAN. Record device/model, OS/API, screen size, text scale, build identity and test date. Protect the existing public Beta 2 installation; do not uninstall or clear data. Use noncritical profiles and a portable backup. Separate debug QA from actual signed install-over acceptance per STAB-05-APK-UPGRADE-GATE.md. Record PASS / FAIL / BLOCKED / NOT RUN; screenshots and logs sanitized of credentials or private data.

| ID | Workflow | Expected outcome | Priority |
| --- | --- | --- | --- |
| QA-01 | Cold launch, splash skip, Home, background/relaunch | No crash, silent splash, correct startup and navigation | P1 |
| QA-02 | Solo Easy/Medium/Hard, select/enter/erase/notes/undo/check/hint | Correct board, controls and saved progress | P1 |
| QA-03 | Leave unfinished Solo, force-stop/reopen, resume | Board, notes, time and settings retained | P0 |
| QA-04 | Complete Solo and reopen result | Correct record once; earned rewards only on new completion | P1 |
| QA-05 | Daily start/resume/complete/review | Correct date, distinct completion and no duplicate rewards | P1 |
| QA-06 | Coach existing lessons and proof UI | Valid teaching steps, readable controls, no unintended edits | P1 |
| QA-07 | Profile, collection, owned and locked styles, name builder | Valid equip only; retained selection after relaunch; readable long name | P1 |
| QA-08 | Achievements, name album, expanded completion rewards | Accurate ownership and reachable actions | P1 |
| QA-09 | Portable export/preview/cancel/restore on isolated data | Confirmed import only, correct profile/settings, local identity and room credentials retained | P0 |
| QA-10 | Two devices: Co-op host/join/code, edit, ping, replies/emotes, reconnect, finish/rematch | Shared state correct; no duplicate records; no arbitrary text | P1 |
| QA-11 | Two devices: Versus start/win/loss/reconnect/rematch | Correct outcome/records, no Co-op coordinate ping | P1 |
| QA-12 | Rapid appearance changes in LAN; leave during completion | Current appearance eventually agrees; no stale reward panel | P1 |
| QA-13 | Phone portrait/landscape; tablet/Fire portrait/landscape; 100% and 200% text | No clipped critical controls; board/keypad and dialogs reachable | P1 |
| QA-14 | TalkBack Home, navigation, board, keypad, Coach, rewards and settings | Clear labels, selection/role, usable focus order, no disruptive duplicate announcements | P1 |
| QA-15 | Airplane mode Solo/Daily/Coach and backup | Core offline flows work without account or network | P1 |
| QA-16 | Audio candidate: six tracks, Music Corner, independent 50% fresh defaults, saved off and volume, last track | Correct playback/status and preferences; no unexpected audio | P1 |
| QA-17 | Audio focus/background/lock/relaunch, Final Piece intro, number vs erase, completion once, haptics | Distinct cues, no duplicate chime or unwanted playback; respects settings | P1 |
| QA-18 | Exact prior signed APK to higher-version signed candidate on backed-up isolated device | Identity, records, notes, progress, cosmetics, preferences and saved room preserved | P0 |
| QA-19 | Actual candidate screenshots and branding on target devices | Accurate store images, approved visuals; no private data | P2 |

## Evidence entry (repeat per failure or acceptance)
- Test ID / result / severity:
- Candidate source SHA / APK digest / package / version / signer:
- Device and OS / orientation / font scale:
- Steps / expected / actual:
- Screenshot or sanitized log reference:
- Fix commit / rerun result:

P0 stops release; P1 repair before acceptance; P2 evaluate or explicitly defer. No public signing/version change, upload or acceptance implied. Earlier general handset audio acceptance is historical evidence only; retest exact candidate. Rights, privacy, support and storefront declarations are separate gates. No requirement that owner repeat destructive scenarios on real personal saves.

## STAB-09.01 — owner candidate preparation
- Automated acceptance: reward runtime 45972419647 (73/73), accessibility 46054499738 (36/36), ordinary quality 46054499731, unsigned 46054499790, isolated consolidated QA build 46055088268 passed on respective source revisions. Verification receipt artifact 15226062530; no retained APK. All physical QA-01 through QA-19 remain NOT RUN.
- Fox V1 source audit: 13 transfer segments present, first 12 each 300000 bytes, final 154384 bytes; Gradle verifies 3754384-byte archive SHA-256 c10b3fcae7e09d09a4c500d74a479c1842a19c54de6f9a7b1907714aa38bdf5b, extracts exact 12 resources. `269e531` adds APK verification of exact twelve gm_emote_fox_*.webp entries and byte fingerprints against generated resource files. CI pending; source archive presence is not proof of packaged fidelity until gate passes.
- Reopened STAB-07-RELEASE-EVIDENCE.md and BETA3_AUDIO_PRODUCTION_GATE.md: per-track commercial entitlement and archive custody unresolved, no distribution while unresolved. Repository artifact access scope not independently verified. Continue verification-only builds; no APK delivery or public release implied. Next verify CI and resolve authorization/access/rights for exact owner candidate, or prepare explicitly approved audio-excluded private QA variant if appropriate. Preserve production package, existing saves and signing identity.

## STAB-09.02 — asset acceptance and delivery boundary
- Consolidated build 46067783141 passed, job 140737185318 log explicitly reports all 12 Fox V1 packaged WebP fingerprints matched. Verified package/version unchanged. Receipt artifact 15231228727, verification only, no APK retained.
- Installed repository metadata confirms SudokuTogether visibility public. Ordinary workflow APK artifacts must not be treated as owner-private. Preserve existing public repository visibility; no public APK upload, no change to sharing permissions.
- Restricted Google Drive is proposed for owner-only delivery; connector search for GridMates returned no matching records, so no folder or ACL verified. Do not claim channel operational. Need verified restricted destination and entitlement/custody evidence before integrated soundtrack distribution, or explicitly scoped audio-excluded testing candidate. Any transfer must preserve exact APK digest and signer receipt and avoid secrets/player data. No physical acceptance yet.
