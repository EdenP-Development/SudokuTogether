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

## STAB-09.03 — private storage established
- Created owner-only My Drive folder named Eden Practical - Private QA Builds; metadata readback confirms shared=false, only user/owner permission, no shared drive. Do not publish folder ID or private links in public repository. No files uploaded and no existing sharing modified.
- Reopened BETA3_SOUNDTRACK_INVENTORY.md: all six master and runtime fingerprints documented; owner reported Suno Pro before supplying tracks, generation-time entitlement evidence still outstanding. Preserve originals and current runtime copies. No music regeneration required.
- Storage destination is verified but no private build-to-storage transfer path established. Public GitHub artifacts remain verification-only. Next establish restricted build output handoff (e.g. local build on owner machine or separately authorized private runner/repository), verify entitlement and exact candidate receipt before owner delivery. No APK distribution, signing or physical QA acceptance yet.

## STAB-09.04 — owner direction: reuse established GitHub delivery
- Owner agreed to restore existing GitHub Actions QA delivery instead of requiring private Drive transfer. Private Drive folder optional for backups/evidence, not a build prerequisite; no sharing changes. Public repository remains public.
- Reopened shared RELEASE_POLICY.md, RELEASE_CHECKLIST.md and ASSET_LICENSING.md: isolated disposable review builds distinct from signed updates, public artifacts must have no secrets/personal data, rights gates still apply to free distribution. Owner direction covers development QA, not official release.
- `424f4da` creates beta3-downloadable-qa.yml from passed consolidated verification workflow, omitting six soundtrack tracks pending generation-time entitlement, verifies absence of soundtrack entries and retains exact 12 emote fingerprints, branding, package and signer checks, tests/lint/assembly, uploads GameplayQA1 APK with SHA256SUMS, source, badging, signature and scope notes. Full soundtrack verification workflow unchanged. No production keys or version changes. Same isolated package as prior verification-only build, temporary debug signer, first downloadable candidate; not an update channel. Preserve any existing QA data, do not uninstall/clear on conflict. Future repeated QA updates need explicit stable QA identity or a new isolated package; existing SUDOKU_QA_KEYSTORE_FILE continuity mechanism not used here.
- CI pending, no physical acceptance. QA-16 music and relevant QA-17 listening deferred for this scoped artifact, not waived for full candidate. Track entitlement owner confirmation/evidence still needed for soundtrack distribution. Public release and signed upgrade remain separate gates.

## STAB-09.05 — downloadable gameplay QA ready
- Workflow 46083936347 job 140789029218 passed; artifact 15238442305 GridMates-Sudoku-Beta3-GameplayQA1, 43150010 bytes ZIP, expires 2026-12-11T23:44:58Z. Source 424f4da. Isolated debug package, soundtrack intentionally omitted, full soundtrack candidate unchanged. APK, checksum, signer, source receipt and notes included by workflow. No production signing or version change.
- Owner can download ZIP and extract/install isolated APK alongside Beta 2; do not uninstall or clear existing data if conflict. Physical QA still NOT RUN. Prior 109 runtime tests passed across separate revisions; this build passed compilation/unit/lint/asset gates, not fresh runtime execution. Next collect QA-01 through QA-15 and applicable QA-17/19 results, defer soundtrack-specific checks and signed upgrade to respective candidates. No public release authorization.

## STAB-09.06 — owner rejected reduced candidate; restore full QA scope
- Owner screenshots show fresh profile, approved name, 47 achievements, collection, branding and developer manual soundtrack picker. Owner reports build not representative of intended complete game. GameplayQA1 is superseded for owner acceptance; do not uninstall/clear existing installs or treat fresh QA as lost production progress.
- `ffcd0d6` adds explicit gridmatesBeta3FullQa flag, requires isolated consolidated preview and six prepared tracks, distinct package com.playereden.sudokutogether.qa.beta3full1 / version 31 / 1.0.0-beta.2-qa.beta3full1. `f1fbb18` disables manual developer import for full QA only. `b2e0950` creates full QA workflow based on verified six-track and twelve-emote checks, launcher/branding, core/unit/lint/compile and APK identity checks. Push runs verification only; manually dispatched workflow with soundtrack_rights_confirmed=true retains complete APK and receipt. Do not substitute another audio-excluded build. Earlier Pro subscription evidence exists but exact final-track generation eligibility not directly confirmed; owner confirmation still outstanding. No production signer or version change. Next inspect full QA workflow and obtain confirmation for distribution, then exact candidate physical acceptance. No official release authorized.

## Owner direction — cumulative integrated testing builds (2026-11-27)
Owner explicitly prefers complete cumulative APKs, not small feature-specific builds: music, artwork, effects, haptics, interface, progression and gameplay must be evaluated together for overall feel. Targeted automated/internal verification remains appropriate, but owner-facing builds must retain all implemented and previously approved features and assets. Do not intentionally omit an existing feature without prior discussion and explicit agreement. Verify complete feature/asset inventory, source identity, package, signer and checksum before handoff. Prefer stable dedicated QA signing and monotonically increasing versions for subsequent testing updates after checking existing channel identity; never replace keys or destroy saves to resolve conflicts. Official publication and rights verification remain separate gates. If a required component cannot be included, disclose the blocker rather than silently deliver a reduced APK. Recommend this policy for portfolio-wide adoption; shared standards repository not changed in this checkpoint.

## 2026-11-27 — confirmed final soundtrack generation
Owner explicitly confirms all six final inventory tracks were generated during Suno Pro, purchased for these game projects. Canonical shared record: https://github.com/EdenP-Development/Eden-Development-Standards/blob/3c8eedfd242056442861d6807d6cee2971eaae5f/audio/gridmates/library/v0.1/README.md . Supersedes previous generation-time uncertainty for these exact six tracks; do not ask again for unchanged inventory. Earlier free-tier prototypes excluded. Private supporting records/final publication review remain distinct. Owner direction authorizes complete integrated development QA delivery; no production release or signer change.
