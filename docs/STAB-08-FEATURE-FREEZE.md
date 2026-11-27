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
