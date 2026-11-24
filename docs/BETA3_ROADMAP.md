# GridMates: Sudoku — Beta 3: Polish and depth

Living roadmap, revision 1 — 2026-11-24. Owner endorsed the direction in conversation. Individual implementation decisions remain subject to playtesting and revision. This is the canonical Beta 3 plan, not evidence that features are implemented. Historical docs/ROADMAP_V08_TO_LAUNCH.md remains historical.

## Current checkpoint
B3-00 ACTIVE on feat/beta3-foundation; see BETA3_FOUNDATION_AUDIT.md. Existing Coach already supports six techniques including X-Wing; B3-04 extends existing engine. No Beta 3 runtime changes yet. Beta 2 owner-reported published at https://sir-spudacus.itch.io/gridmates-sudoku; devlog https://sir-spudacus.itch.io/gridmates-sudoku/devlog/1804670/gridmates-sudoku-beta-2-growing-our-little-puzzle-game . Accepted build 31 / 1.0.0-beta.2, source c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c, signed CI https://github.com/EdenP-Development/SudokuTogether/actions/runs/45487842212 . Permanent itch package com.playereden.gridmates.sudoku.beta and certificate SHA256 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d. Keep signing secrets and existing save schemas compatible. Public download checksum/live listing independent verification, durable APK/art archive, independent owner key backup and reviewed source promotion remain open; do not claim completed. Owner confirmed same-version signed reinstall retained progress; higher-version install-over and two-device testing remain outstanding. Do not rebuild or replace published Beta 2 merely for documentation.

Baseline: Solo/Daily/Co-op/Versus, three difficulty levels, offline-first, local Wi-Fi multiplayer, existing Coach, profiles/stats/history, backup/restore, 85 cosmetics (43 portraits, 12 insignias, 11 boards, 10 inks, 9 fonts), 47 achievements. Preserve all earned rewards, current unlock thresholds and assisted/unassisted accounting. Existing generated promo panels are illustrations, not raw UI evidence.

## Product direction
Good puzzles. Good company. Comfortable puzzle-book nostalgia, accessible, social without accounts or unnecessary setup, educational, rewarding without obligation, practical and local-first. Beta 3 is a better game, not merely a bigger game. Owner is design director and tester; assistant handles technical work and documentation wherever possible. No forced cloud, ads, accounts, paid infrastructure or Google Play launch work this cycle. Keep evergreen itch branding unless a substantive change warrants it.

## Milestones
Statuses: NOT STARTED / ACTIVE / REVIEW / DONE / DEFERRED. Mark DONE only with evidence and actual acceptance; record failures and scope changes. The phases are ordered defaults, not rigid dependencies. Split into small reviewable builds; avoid overlapping broad rewrites.

| ID | Scope | Status |
| --- | --- | --- |
| B3-00 | Foundation audit | ACTIVE |
| B3-01 | Branding and design system | NOT STARTED |
| B3-02 | Personalized puzzle corner | NOT STARTED |
| B3-03 | Gameplay experience | NOT STARTED |
| B3-04 | Advanced Sudoku Coach | NOT STARTED |
| B3-05 | Collection and progression | NOT STARTED |
| B3-06 | Sound and haptics | NOT STARTED |
| B3-07 | Multiplayer refinement | NOT STARTED |
| B3-08 | Integrated polish | NOT STARTED |
| B3-09 | Beta 3 release | NOT STARTED |

### B3-00 — Foundation audit
Inspect exact current branch, main/PR differences, build configuration, release receipts, tests and workflows. Inventory screens, components, assets, asset reuse and licensing/provenance; assess accessibility, performance, persistence, networking and Coach. Create findings with severity, evidence, reproduction and recommended disposition; avoid assuming a clean build means bug-free. Address release archival/source promotion through normal reviewed workflow, recover exact approved icon and art binaries (no regenerated substitutes without approval). Record public verification status and technical debt. Establish a Beta 3 development branch from reviewed baseline; do not disrupt other projects or publish.
Acceptance: inspectable audit, known baseline, prioritized backlog, explicit unresolved housekeeping, regression plan and next small implementation slice.

### B3-01 — Branding and design system
Use approved leaf-accent fox/grid text-free Android icon (see ITCH_PROMO_ASSETS.md); plain-background icon is itch favicon, full title emblem is promotional. Archive exact source art when available. Generate adaptive foreground/background, legacy densities and assess clipping, contrast and 16/32/48px readability. Audit typography, paper/green/gold palette, texture, spacing, cards, icons, button states, focus and feedback. Build reusable components incrementally; improve profile name/emoji wrapping and success/error banner semantics (green/check for success, distinct errors).
Acceptance: approved icon survives launcher masks and real-device review; consistent reusable components, text scaling, contrast and regression coverage. No package/signing change.

### B3-02 — Personalized puzzle corner
Preserve current dynamic equipped companion behavior. Map Woodland/Ocean/Garden/Farm to distinct reusable habitats; audit Farm wording and fallback for basic insignias or missing art. Design scenes with reserved text/portrait space and readable contrast; no per-portrait full background explosion. Improve main menu hierarchy and subtle motion without hiding Daily, Solo or multiplayer access. Later seasonal/collectible habitats are optional.
Acceptance: correct habitat, portrait and wording after equip/restart; all existing collections/fallbacks work; assets meet size/performance budget; owner visual approval.

### B3-03 — Gameplay experience
Audit board clarity, selected/related cells, notes, keypad, remaining counts, undo, erase, hints and completion/results. Refine touch targets, landscape/small-screen and larger text behavior, color-independent states, subtle transitions and optional reduced motion. Fix reproducible bugs without changing puzzle logic or scoring arbitrarily.
Acceptance: actual valid puzzles, notes/undo/resume, Daily and completion records unchanged; accessible interactions; smooth performance and physical visual review.

### B3-04 — Advanced Sudoku Coach
Deterministic offline reasoning engine, not an external AI service. Validate every explanation against actual candidates and constraints. Start with rules, candidates, naked/hidden singles; then locked candidates, naked/hidden pairs. Triples, X-Wing and harder strategies are stretch scope. Add technique-specific practice and graduated hints (highlight, explain, optionally reveal), with accurate assistance classification. Explicitly distinguish teaching from direct answers, preserve existing assisted/unassisted records and unlock rules. Do not silently reclassify historical records.
Acceptance: fixtures per technique, invalid-board and no-supported-step handling, no false deductions, deterministic explanations, test coverage and owner usability review. No guessing presented as logic.

### B3-05 — Collection and progression
Improve album browsing, chapter navigation, previews, locked/unlocked states, next milestones and achievement presentation; keep all stable IDs and rewards. Refine Woodland/Ocean/Garden/Farm artwork and expand selectively (including nautical/ship-in-bottle ideas if suitable). Weekly gifts, duplicate recycling, collection completion rewards and redeem codes are experimental, not commitments; avoid daily pressure or monetized randomness. Do not increase existing unlock requirements.
Acceptance: all 85 existing cosmetics and 47 achievements remain available with preserved eligibility; new assets have manifests and explicit unlock rules; no save corruption or duplicates; visual review.

### B3-06 — Sound and haptics
Optional gentle pencil/tap/erase, unlock and completion cues, restrained animation and haptics. Music optional, only appropriately licensed/original audio. Separate controls where useful, persist preferences, honor silence/audio focus and reduced motion; no network dependence. Avoid interruptions during solving.
Acceptance: silent mode usable, preferences persist, no stuck audio or intrusive effects, licensing documented and device-tested.

### B3-07 — Multiplayer refinement
Retain simple same-Wi-Fi host/join, optional room code and compatible protocol behavior. Audit presence, ready/waiting states, co-op partner activity, versus progress, results, reconnect, rematch and cleanup. Test two real devices when available; distinguish simulated/emulator evidence from physical. Trusted LAN scope; no remote matchmaking or online infrastructure in this cycle. Protocol changes require explicit compatibility policy.
Acceptance: targeted automated tests plus real-device evidence or explicit owner deferral for noncritical cases; critical data-loss/security defects not deferred.

### B3-08 — Integrated polish
Run full regression, verify art manifests, accessibility, performance, Android lifecycle and persistence, validate signed upgrade from actual Beta 2 with backed-up noncritical data. Review new/old saves, achievements/cosmetics, assisted counts and restoration. Inspect APK identity, permissions, version and signature. Record failures, fixes and deferrals; don't erase data to make tests pass.
Acceptance: release scope frozen, no known critical defects, accepted build and test receipt, owner review.

### B3-09 — Release
Use shared ITCH_PUBLISHING_GUIDE.md and RELEASE_POLICY.md. Increase versionCode above 31; proposed public version 1.0.0-beta.3 (confirm against registry before implementation). Sign using existing permanent beta identity, keep exact accepted APK, verify and archive it, prepare concise personal changelog and release-specific cover. Keep evergreen storefront unless needed. Owner approves publication. Update public links, release receipts, handoff and shared registry. Independent verification limitations remain explicit.
Acceptance: signed release accepted, publication recorded honestly, next-cycle handoff complete.

## Priority and scope control
Essential: save compatibility, icon, visual consistency, accessibility, bug fixes. High: Coach logical core, companion habitats, gameplay feel. Medium: collection design/expansion, audio, multiplayer polish. Experimental: weekly gifts/recycling/redeem codes, seasonal habitats, advanced Coach techniques. Future: remote play, new formats, monetization, Google Play. Scope can move to later betas rather than forcing feature bloat. No timeline commitment.

## How to maintain this roadmap
At each milestone or significant session: update status and next action, record exact commit/branch, CI/artifact links, evidence, owner decisions, blockers and deferred work in BETA3_PROGRESS.md. Update CONTINUE_HERE.md when the resume point changes. Update shared APP_REGISTRY.json only for meaningful channel/milestone changes using fresh CAS edits; don't overwrite other apps. Preserve completed release records. Changes to scope require a dated rationale, not silent deletion. For new chats, read the roadmap, progress log, handoff and actual repo state before coding. Shared standards: https://github.com/EdenP-Development/Eden-Development-Standards .

## Decision history
- 2026-11-24: Owner endorsed Beta 3 direction and requested living roadmap. Milestone acceptance criteria are assistant-proposed operational details, adjustable with owner review.
- Prior owner choices: keep offline-first, simple LAN play, dynamic companions, evergreen branding, selected fox launcher art; spouse/two-phone Beta 2 testing deferred; commercial launch much later.
