# GridMates: Sudoku — Beta 3: Polish and depth

## B3-06 — feedback reliability and soundtrack checkpoint (2026-11-25)

Reward continuity gate 45585519590 and sound pilot gate 45586137570 passed. B3-05 REVIEW, B3-06 ACTIVE. Audited state initialization/home/reset/restore: soundEffects default false, reread after restore; backup allowlist only writes known portable preferences and leaves device-local soundEffects untouched. Added instrumentation fixture for both on/off restore preservation and exclusion from export; not executed yet. Native click dispatch now fails silently on device RuntimeException so gameplay continues; copy-room-code retains haptic only. Added JVM feedback regression fixtures. Fresh CI pending. Owner generated M4A tracks 01 Woodland Study, 02 Quiet Pages, 03 Good Puzzles, Good Company, 04 Sunday Morning; owner likes them, files not yet supplied, no audio audition or rights verification. Owner reached free Suno limit and deferred further generation. Do not block Beta3 on custom music or assume commercial rights (generation plan at creation matters). Next finish feedback verification and advance multiplayer refinement while soundtrack intake deferred. No public release, signing or save schema changes.


## B3-05 reward continuity and future audio direction (2026-11-25)

Medal detail gate 45584899760 failed on missing FontFamily import; corrected. Existing core audits cover deduplicated records, stable ownership after pruning, eligibility and bulk progress. Added cross-ledger regression for all 85 cosmetic and 47 medal IDs/counts, valid emblem references, assisted solve exclusion from clean medals and retained ownership after record pruning. Fresh integrated CI pending; no threshold/save changes. B3-05 remains active until gate verified, then B3-06 optional restrained tactile/audio feedback. Owner wants future Suno experiments with assistant-written prompts for original instrumental background music and potentially useful sound material, owner generates and supplies files. Not a Beta3 release dependency. Verify commercial rights for specific generated assets and document provenance before integration; audition/trim/loop/normalize offline assets, no mandatory music or online dependency. No audio generated or added at this checkpoint.


## Current owner decision — Coach scope freeze; B3-05 active (2026-11-25)

Coach Review 2 gate 45580000685 and preview 45580000688 passed; artifact 15006471379. Owner defers new Coach techniques and substantial teaching expansion until Beta 4 following Beta 3 feedback. Ten techniques/lessons retained; correctness, performance, accessibility and release regression audits remain in scope. No requirement for immediate owner Coach2 playtest; device instrumentation not executed by these gates. B3-04 REVIEW / feature scope frozen, not full release acceptance. B3-05 ACTIVE: static collection review found existing chapter navigation, equipped previews, next unlock and responsive grids. Added per-chapter available/total counts and progress tracks to All icons view using existing progress calculation, no unlock/identity/save changes. Fresh CI pending. Next inspect collection regression and remaining progression UX; then sound/haptics and multiplayer. Preserve public Beta2 identity, 85 cosmetics and 47 achievements; no publication authorized.


Living roadmap, revision 3 — 2026-11-24. Owner endorsed the direction in conversation. Individual implementation decisions remain subject to playtesting and revision. This is the canonical Beta 3 plan, not evidence that features are implemented. Historical docs/ROADMAP_V08_TO_LAUNCH.md remains historical.

## Current development checkpoint — 2026-11-24 local

Active branch: feat/beta3-foundation. Visual4 source 96747991e98f78c90e10d6eda447d1be085a3674 passed visual CI 45550962814 and Android gate 45550962759; artifact 14993249624. Owner physically approved both startup screens and revised nominal 3-second timing. See BETA3_STARTUP_PILOT.md for exact asset hashes, implementation and remaining technical validation. Fox launcher rendered physically at 58dp; all four habitats rendered in earlier owner screenshots. Native launcher promotion, habitat persistence/contrast acceptance, original archive durability and startup accessibility/lifecycle/performance verification remain open. No public release or cross-project rollout here.

B3-03 ACTIVE: e1ab642465b8e4cf3f750e7372e002e06b282b71 added highlighted pencil candidates and context-aware erase; e0adae262e6738669dcf75e2ed2c6b3025ccadf3 added reserved guidance/caption space. CI 45552906603 failed Android instrumentation compilation because DpRect width/height accessors were unavailable; corrected by subtracting edges, fresh CI pending. Next: verify fixes, audit board clarity, compact/large-font controls and completion UX; group improvements into Gameplay Review 1 rather than frequent micro-builds. Coach, collection/progression, sound/haptics, multiplayer and integrated release milestones remain as scoped in roadmap. Preserve puzzle logic, 85 cosmetics, 47 achievements, save compatibility and signing. Public Beta2 version 31 remains untouched. No percentage estimate is an engineering measurement.

## Historical baseline and release continuity
B3-00 ACTIVE on feat/beta3-foundation; see BETA3_FOUNDATION_AUDIT.md. Existing Coach already supports six techniques including X-Wing; B3-04 extends existing engine. B3-001 and B3-003/004 implemented and automated gates passed. B3-002/B3-005 artwork integrated into isolated previews; see current checkpoint for remaining promotion/acceptance. See BETA3_PROGRESS.md for current handoff. Beta 2 owner-reported published at https://sir-spudacus.itch.io/gridmates-sudoku; devlog https://sir-spudacus.itch.io/gridmates-sudoku/devlog/1804670/gridmates-sudoku-beta-2-growing-our-little-puzzle-game . Accepted build 31 / 1.0.0-beta.2, source c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c, signed CI https://github.com/EdenP-Development/SudokuTogether/actions/runs/45487842212 . Permanent itch package com.playereden.gridmates.sudoku.beta and certificate SHA256 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d. Keep signing secrets and existing save schemas compatible. Public download checksum/live listing independent verification, durable APK/art archive, independent owner key backup and reviewed source promotion remain open; do not claim completed. Owner confirmed same-version signed reinstall retained progress; higher-version install-over remains outstanding; physical Beta 2 Versus and Co-op completion observed, with positive owner-reported rejoin feedback. Full Beta 3 two-device validation remains pending. Do not rebuild or replace published Beta 2 merely for documentation.

Baseline: Solo/Daily/Co-op/Versus, three difficulty levels, offline-first, local Wi-Fi multiplayer, existing Coach, profiles/stats/history, backup/restore, 85 cosmetics (43 portraits, 12 insignias, 11 boards, 10 inks, 9 fonts), 47 achievements. Preserve all earned rewards, current unlock thresholds and assisted/unassisted accounting. Existing generated promo panels are illustrations, not raw UI evidence.

## Product direction
Good puzzles. Good company. Comfortable puzzle-book nostalgia, accessible, social without accounts or unnecessary setup, educational, rewarding without obligation, practical and local-first. Beta 3 is a better game, not merely a bigger game. Owner is design director and tester; assistant handles technical work and documentation wherever possible. No forced cloud, ads, accounts, paid infrastructure or Google Play launch work this cycle. Keep evergreen itch branding unless a substantive change warrants it.

## Milestones
Statuses: NOT STARTED / ACTIVE / REVIEW / DONE / DEFERRED. Mark DONE only with evidence and actual acceptance; record failures and scope changes. The phases are ordered defaults, not rigid dependencies. Split into small reviewable builds; avoid overlapping broad rewrites.

| ID | Scope | Status |
| --- | --- | --- |
| B3-00 | Foundation audit | ACTIVE |
| B3-01 | Branding and design system | REVIEW |
| B3-02 | Personalized puzzle corner | REVIEW |
| B3-03 | Gameplay experience | ACTIVE |
| B3-04 | Advanced Sudoku Coach | REVIEW |
| B3-05 | Collection and progression | REVIEW |
| B3-06 | Sound and haptics | ACTIVE |
| B3-07 | Multiplayer refinement | NOT STARTED |
| B3-08 | Integrated polish | NOT STARTED |
| B3-09 | Beta 3 release | NOT STARTED |

### B3-00 — Foundation audit
Inspect exact current branch, main/PR differences, build configuration, release receipts, tests and workflows. Inventory screens, components, assets, asset reuse and licensing/provenance; assess accessibility, performance, persistence, networking and Coach. Create findings with severity, evidence, reproduction and recommended disposition; avoid assuming a clean build means bug-free. Address release archival/source promotion through normal reviewed workflow, recover exact approved icon and art binaries (no regenerated substitutes without approval). Record public verification status and technical debt. Establish a Beta 3 development branch from reviewed baseline; do not disrupt other projects or publish.
Acceptance: inspectable audit, known baseline, prioritized backlog, explicit unresolved housekeeping, regression plan and next small implementation slice.

### B3-01 — Branding and design system
Use approved leaf-accent fox/grid text-free Android icon (see ITCH_PROMO_ASSETS.md); plain-background icon is itch favicon, full title emblem is promotional. Archive exact source art when available. Generate adaptive foreground/background, legacy densities and assess clipping, contrast and 16/32/48px readability. Audit typography, paper/green/gold palette, texture, spacing, cards, icons, button states, focus and feedback. Build reusable components incrementally; improve profile name/emoji wrapping and success/error banner semantics (green/check for success, distinct errors).
Acceptance: approved icon survives launcher masks and real-device review; consistent reusable components, text scaling, contrast and regression coverage. No package/signing change.

#### B3-01 shared startup branding — owner-approved 2026-11-24
Adopt pinned Spudacus v1.0 runtime PNG from EdenP-Development/Eden-Development-Standards commit 015645bffb612e0f4311ec5a2d29e9c8bad91289, SHA-256 74773dbda822161f73a639f959e8bce5c6e0b333a94ebeb8ee27f620d4995d25. Bundle locally, verify checksum in CI. Startup: developer mark, approved GridMates v1.0 series stage (Sudoku sole initial pilot; pinned source 9affedc94ca5a6b896d36148f46f14a4e8218c3a, SHA-256 eef6d631cac0b9523f7e0dbebac229f9a2458d36fe2e73e111d251704d9ff292), existing menu. Warm cream #FAE7CD, safe centered aspect fit, silent short fade, tap skips remaining stages, reduced motion static/skip, no replay on resume or rotation and no forced loading delay. Keep native startup separate. Test cold/warm, offline, rotation, accessibility, skip and existing navigation/save regressions. Owner review before publication. No package, signing or save changes. Follow shared BRAND_IDENTITY.md, branding/spudacus/v1.0/README.md and branding/gridmates/v1.0/README.md. No other project rollout until owner accepts physical pilot and central implementation standard is documented.

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
Retain simple same-Wi-Fi host/join, optional room code and compatible protocol behavior. Audit presence, ready/waiting states, co-op partner activity, versus progress, results, reconnect, rematch and cleanup. Owner Beta 2 physical evidence now covers Versus and Co-op (Easy/Medium/Hard Co-op); rejoin works decently overall per owner. See foundation audit for limitations. Prioritize stable board layout, compact partner legend and connection UX; preserve distinct player styles. Test two real devices when available; distinguish simulated/emulator evidence from physical. Trusted LAN scope; no remote matchmaking or online infrastructure in this cycle. Protocol changes require explicit compatibility policy.
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
