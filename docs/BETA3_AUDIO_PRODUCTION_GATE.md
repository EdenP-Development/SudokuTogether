# AUD-09.01 — production audio promotion gates

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
