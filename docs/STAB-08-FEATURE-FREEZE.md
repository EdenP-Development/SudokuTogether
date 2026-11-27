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
