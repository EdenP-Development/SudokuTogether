# STAB-08 — Beta 3 feature freeze

2026-11-27 | Active branch `feat/beta3-foundation` | Development scope freeze, not release approval.

## Scope baseline
Latest build-verified code `8e2dcee916bf7189a6a252951046b5d5fca52f62`, quality gate 45962075841 and unsigned candidate 45962075789 passed. Integrated runtime 45962562834 at 8242358 still executing at inspection. No release tag or final candidate pin yet.

Retain existing Solo, Co-op, Versus, Daily, ten-technique Coach scope, 85 cosmetics, 47 achievements, 171 name words, 14 approved replies, 12 Fox V1 emotes, Co-op Partner Ping, portable backup, startup/branding and six-track audio candidate subject to rights and release acceptance. Preserve stable IDs, player identity, saves and existing itch signing identity. No additional Coach techniques, modes, collectible variants, advanced markers, soundtrack additions, cloud accounts or cross-game ownership synchronization without separate owner direction. Do not remove existing features merely to achieve freeze.

## Allowed changes after freeze
- P0: data loss, security/privacy, signing or upgrade blocker: stop release and repair with regression evidence.
- P1: crash, unusable core flow, incorrect puzzle/reward logic, accessibility blocker or platform compatibility defect: repair with focused regression and relevant CI/runtime rerun.
- P2: reproducible polish/performance/copy issue within existing scope: assess impact and risk; defer nonessential redesigns.
- New feature requests: capture in future backlog; no automatic implementation.

Every change records reproduction, affected source, minimal fix, test evidence, unresolved limitations and exact commit. Documentation and acceptance testing remain in scope. Re-pin candidate only after fixes and verification; no version bump, signing or upload implied by this freeze.

## Outstanding acceptance gates
See STAB-07-RELEASE-EVIDENCE.md. Integrated reward/save runtime, targeted accessibility instrumentation, actual physical phone/tablet/Fire and two-device tests, rights and privacy review, exact signed install-over and explicit artifact/channel approval remain open. Preserve itch Beta 2 package com.playereden.gridmates.sudoku.beta, code 31, certificate 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d until separately approved higher-version release. Amazon rehearsal only; Google Play long-term priority.

Next STAB-08.02: review integrated runtime outcome and assemble consolidated owner QA matrix, including defect severity and evidence capture. No release authorization.

## STAB-08.02 — consolidated QA preparation
- `a1df17e`: prepared [STAB-09 consolidated owner QA matrix](STAB-09-CONSOLIDATED-OWNER-QA.md), 19 scenarios with P0/P1/P2 priorities and evidence template. Reuses APK upgrade gate and prior audio acceptance checklist; exact build and device results required. No physical tests executed.
- Integrated reward runtime 45962562834 still executing at inspection. Next inspect outcome, repair failures, then identify isolated consolidated QA candidate workflow and required accessibility runtime coverage before owner testing. No production signing or public distribution.

## STAB-08.03 — consolidated candidate preparation
- Inspected beta3-visual-preview.yml, beta3-audio-candidate.yml and app/build.gradle.kts. Existing visual preview flag includes approved art and six-track assets in debug, isolated package com.playereden.sudokutogether.qa.beta3coach2, temporary debug signer. Existing installations may have incompatible debug signer; never uninstall/clear to bypass. Before owner build, reconcile QA identity or choose a fresh isolated suffix, exact source receipt and appropriate private artifact access; rights review remains open. Unsigned permanent identity staging is not installable and must not be distributed. No new APK built or uploaded.
- `7063c87`: added beta3-accessibility-runtime.yml, based on earlier passing save emulator configuration; selects five existing suites CompactPlayLayoutTest, SudokuKeypadAccessibilityTest, Beta3BoardClarityTest, CoachProofUiTest, PlaySmokeTest. API36, report-only, no production secrets. Execution pending. Integrated reward/save runtime 45962562834 still executing at inspection.
- Next STAB-08.04: inspect both runtime results, resolve failures, verify exact isolated QA candidate configuration and rights/access before artifact build. Physical and signed upgrade QA remain separate. No production feature or signing changes.

## STAB-08.04 — isolated candidate identity
- Live inspection: reward/save 45962562834 and accessibility 45967679674 both executing instrumentation, setup successful; neither accepted yet.
- `eb64249`: opt-in `gridmatesBeta3ConsolidatedQa=true` requires `gridmatesBeta3VisualPreview=true`, selects fresh debug suffix `.qa.beta3integrated1` and matching version name suffix. Existing preview restrictions reject production identity/signing and persistent QA signing. Default and prior preview IDs unchanged. Compile/build verification pending. Fresh temporary signer is not an update continuity strategy; preserve prior QA installations and their progress, do not uninstall on mismatch.
- Next: verify CI and runtime outcomes, inspect private artifact access and audio entitlement before an owner APK build. Use existing visual preview asset verification, exact SHA receipt, and new suffix assertion. No APK generated or uploaded. No release version or production signing changes.

## STAB-08.05A–C — runtime repair and isolation
- `eb64249` ordinary quality 45968523890 and unsigned 45968524029 passed; opt-in preview itself unbuilt.
- Accessibility 45967679674: 24 executed, 21 passed, 3 failed. Logs job 140400196590; report 15183608038 expires 2026-12-04. Failures: tablet landscape configuration timeout; outdated Room chat/free-text smoke expectations suspected, exact implementation not located; Coach Show why before Show evidence confirmed outdated.
- `20d2481`: Coach smoke test adds evidence stage. `97a776f`: explicitly rotate emulator and inspect activity configuration; orientation cause remains hypothesis. `5613d36`: Daily ribbon assertions match actual 1 / 7 and 0 / 7. Runtime verification pending for all.
- Reward runtime 45962562834 cancelled near 60-minute job limit, last log 52/73 completed, 2 failures (Daily summary text); stall cause unknown. Job 140383573196. `a60b9d2` splits original ten suites across five independent matrix jobs with fail-fast false, unique reports and 35-minute job limits; execution pending. No test suites removed.
- Next locate actual RoomChatSheet source using branch-specific repository inspection, correct test only against verified UI; rerun accessibility workflow on corrected source, inspect matrix outcomes and fix remaining failures. Do not distribute owner APK until gates and rights/access resolved. No production gameplay, signing or release change.

## STAB-08.05D / 08.06 — runtime acceptance follow-up
- `d372abc` hosted room smoke test now verifies approved Quick Chat and emote controls using dedicated MultiplayerPolishUiTest selectors as evidence; direct RoomChatSheet source not located. Quality 45971639961 and unsigned 45971639925 passed. `a878c8b` expands accessibility runtime to include MultiplayerPolishUiTest; run 45971660048 in progress at inspection.
- Matrix 45970942057 finished all 73 tests: storage 43/43, completion 12/12 (both Daily fixes passed), names 3/3, collection 12/13, backup UI 1/2. Collection job 140410943518 failed on removed player_name text input; backup job 140410943928 expected legacy Transferred player rather than normalized canonical name. No evidence from these two failures of save loss. Original stall cause unknown, matrix completed successfully in execution terms.
- `0c1d775` replaces obsolete free-text collection interaction with Happy Maple Fox -> Clever Maple Fox Name Builder sequence, preserving touch target and icon navigation checks. `6f01bb2` verifies deterministic NameCollection.normalizedProfileName for imported legacy name against destination profile; retains identity/cancel/difficulty checks. Verification pending. Reward runtime trigger updated to include changed test paths. No production logic changed.
- Next inspect new accessibility and reward matrix results, repair remaining issues, then verify opt-in QA artifact configuration and rights/access before building owner APK. No release approval.

## STAB-08.06A–B — follow-up verification
- Accessibility 45971660048: 35/36 passed, all original three failures cleared. New failure MultiplayerPolishUiTest.approvedRepliesAndEmotesAreReachableWithoutTextEntry, generic assertIsDisplayed error; job 140413296033, artifact 15185512290. `305e576` adds labeled assertion diagnostics without weakening any checks, rerun triggered by workflow path. Actual failing control and root cause still unconfirmed.
- Reward matrix 45972419647: storage passed, collection 13/13, backup UI 2/2 (canonical name fixes verified). Completion job 140415812134 stalled after 9/12 with zero reported failures and timed out. Names job 140415812163 failed before tests due missing Maven dependencies in UTP configuration. Both groups passed previous run 45970942057; do not claim current full gate green. Quality 45972386331 and unsigned 45972386285 passed at 6f01bb2. Next isolate communication failure and retry incomplete reward jobs; no release authorization or APK generation.

## STAB-08.06C / 08.07 — consolidated QA preparation
- Diagnostic accessibility 46052800293: 35/36, exact failure history sender You not displayed. `034f083` adds scroll-to before both sender label visibility assertions; no production change or weakened visibility check. Quality 46054499731 and unsigned 46054499790 passed; runtime 46054499738 pending.
- GitHub accepted failed-job retry for reward matrix 45972419647. Latest jobs completion 140693526490 and names 140693526957 in progress; storage, collection and backup UI successful. Full gate not green yet.
- `0f31071` adds `.github/workflows/beta3-consolidated-qa.yml`, active branch restricted, checkout credentials disabled, existing verified launcher/branding/six-track preparation, both isolated preview flags, unit/lint/test compilation and debug assembly, asserts package com.playereden.sudokutogether.qa.beta3integrated1 and version 31 / 1.0.0-beta.2-qa.beta3integrated1, verifies debug signature and packaged audio, retains only source SHA, APK checksum, badging and signer receipts. No APK/audio artifact upload. Build verification pending; exact emote asset coverage, distribution access and entitlement remain open. No release authorization or production change. Next inspect CI, close runtime gates and verify distribution prerequisites before owner APK delivery.
