# AUD-09.01 — production audio promotion gates

## AUD-09 automated candidate verification — 2026-11-25

Latest runtime 325c03e638d02b4e946550f9311e70c5ea81b7e8: ordinary Android quality gate 45735580949 and unsigned audio candidate 45735580929 both completed successfully; individual build/test and identity/audio verification steps confirmed. Candidate validates release JVM tests, lint, unsigned assembly, existing staging package/version and six stored soundtrack hashes. UI instrumentation compiled, not executed. Automated candidate gate passed; final production promotion/signing/version increment and physical regression remain open. Earlier incomplete status lookup superseded. Original six masters recovered (ffe1f63), six reproducible desktop WAV effects cataloged (16fde84), existing private Drive optimized archive metadata verified (15,369,442 bytes; no fresh Drive rehash). Do not duplicate existing archives. Canonical master ZIP durable upload and commercial entitlement per track remain open. AUD-11 hub handoff next, then integrated B3-08; no public release authorized.

## AUD-09.05 — production audio checkpoint

Owner acceptance remains cb37562; approved Audio Review 4 source c9ae4e6. Candidate source changes: private import restriction 35ec2a9, unsigned checksum-gated configuration f8a91e3, candidate Music Corner e21152f, Gradle imports 74519f0, CI workflow 04b8146, preparing/playing status cf3ac7c, lifecycle status correction 325c03e. Candidate requires gridmatesPermanentIdentityStaging=true and gridmatesBeta3AudioCandidate=true, no signing credentials, six verified soundtrack files. Workflow extracts using scripts/prepare_beta3_soundtrack.py, runs core/release tests and lint, assembles and verifies unsigned APK and stored audio hashes; deliberately no APK upload. Existing public version 31 / 1.0.0-beta.2 unchanged. Candidate CI execution not verified: commit workflow interface returned no PR-triggered runs and combined statuses empty; neither implies failure or success. New runtime changes are not yet compiled or handset-tested. Next: obtain candidate and ordinary quality-gate run evidence, address failures, review focus/backup/rotation and release visual differences, then AUD-10 archive and AUD-11 hub handoff. Commercial entitlement per track, original masters, actual signed upgrade, targeted listening and physical multiplayer/accessibility remain open. No public release authorized.

Status: active; static audit, no runtime promotion yet. Baseline owner acceptance cb37562722246707ebee8f559fcfabd0a06b12e1; runtime c9ae4e6f8614eddd02f3f2d30af88e86c985eed2.

## Verified source restrictions
- app/build.gradle.kts adds build/beta3-audio-assets only to debug source sets when gridmatesBeta3VisualPreview is true; release does not package the soundtrack. M4A noCompress is already configured.
- MainActivity.kt calls rememberPrivateMusicPilot only under BuildConfig.DEBUG. It gates playback on startupFinished and backup working/recovery status. Preserve these behaviors.
- Release version remains 31 / 1.0.0-beta.2; dedicated beta signing and package com.playereden.gridmates.sudoku.beta must remain intact. Do not change public artifacts or signing material in AUD-09.

## Promotion acceptance
1. Audit player implementation and settings, including release exclusion of private import UI, truthful prepared/playing status, focus/lifecycle, backup locks and persisted explicit choices. No new audio feature scope.
2. Obtain track-specific evidence of commercial entitlement at generation for all six masters; inventory says owner reported Pro before delivery, which is not itself proof of generation entitlement. Archive masters and metadata privately. No distribution while unresolved.
3. Use a separate explicit production-candidate switch, not a broad DEBUG removal. Ensure assets are extracted before packaging and fail closed on missing or mismatched files. Pin six inventory SHA-256 hashes and verify release APK paths/storage. Retain isolated preview support. Audit release visual differences separately.
4. Build/test unsigned candidate first; no signing, publishing or store upload in this phase. JVM/lint/build and compiled instrumentation must be distinguished from executed device tests.
5. Regression: silent splash/skip, foreground/background, focus interruption, backup lock, default 50% volumes and explicit off preservation, last track, Final Piece intro, distinct number/erase, completion cue, accessibility, offline playback. Consolidate physical testing.
6. Later signed upgrade: use original itch certificate, higher version code, identical package, verify saves/profiles/records/settings/85 cosmetics/47 achievements. Amazon rehearsal remains separate; Google Play long-term priority.

Next: inspect controller and CI extraction/verification workflows, then implement bounded candidate integration. No release authorized. See BETA3_AUDIO_REVIEW_CHECKLIST.md and BETA3_SOUNDTRACK_INVENTORY.md.
