# GridMates: Sudoku — Beta 1.2 refinement execution plan

**Established November 16–17, 2026 · Scope: quality and polish, not feature expansion.**

## Baseline and rules

- Canonical Android source: `main`, after merged PRs #42–#54. Public itch.io APK remains **Beta 1**. A separate **Beta 1.1 isolated preview** exists; the newest Beta 1.2 palettes are **source only** until a newly verified Android preview is produced.
- Current source has **22 animal portraits + 6 emblems + 7 boards + 10 inks + 5 fonts = 50 curated cosmetics**. Parchment, Ocean, Graphite, Sepia, and Indigo are **provisional**, not yet approved by owner on a physical phone.
- Preserve normal 9×9 Sudoku rules, the offline Solo/Daily experience, same-Wi-Fi Co-op/Versus protocol, persisted profile IDs/records, save formats, cosmetic IDs and old unlock thresholds.
- No new game modes, currencies, purchases, reward systems, fonts/assets, extra splash screens or cloud dependencies in this cycle. Refuse cosmetic edits that reduce digit readability or make gameplay unfair.
- Keep **source implementation**, **automated test coverage**, **physical phone observations**, and **shipping evidence** separate. Do not call an untested behavior verified.

## Phase A — UX and consistency (first)

### A1 — Concrete Collection family-navigation issue (implementation slice)

Evidence: owner screenshots noted a clipped icon-family tab; the present `CollectionChoices` uses one horizontal `Row` with `rememberScrollState`, but no explicit affordance or selected-item visibility request.

- [x] Existing swipe row and family filters confirmed in `CosmeticUi.kt` and `CollectionPolishUiTest.kt`
- [ ] Only when the family row overflows, show a compact swipe cue
- [ ] When a family is selected/restored, bring it into view; preserve one-row swipe and selection while returning from Boards/Numbers
- [ ] Verify 320dp overflow and wide-screen no-hint UI behavior
- [ ] Preserve current gallery order, unlocks, equipped states and scroll-to-top on equip

### A2 — Consistency/accessibility audit (observation before edits)

- [ ] Phone screenshots or instrumentation checks at **320, 360 and 430dp**, and **200% font**, portrait and landscape where feasible
- [ ] Home: mode switcher, Continue/New puzzle difference, editable-only progress, Daily state, lower-scroll discoverability
- [ ] Gameplay: actual 9×9 with notes, selection, highlight, check-work, error, Coach panel, digit keypad and smaller viewport
- [ ] Profile/Collection/Settings: empty and populated accounts, large names/emoji, Collection categories and previews, keyboard, screen-reader labels
- [ ] Audit report lists **reproduced**, **observed but unverified**, and **already resolved** issues; do not invent problems to justify refactors

**A complete when:** narrow and large layouts can reach their controls; no item is silently hidden, and any modified UI has Android regression coverage.

## Phase B — Puzzle interaction and Coach refinement

- [ ] Validate generated Easy/Medium/Hard puzzles for unique solutions, responsiveness on Android-class hardware, and technique-tier consistency; **do not claim human difficulty calibration** without testers
- [ ] Validate entry/erase/Undo/pencil-note interactions; assistance remains optional and assisted results remain differentiated
- [ ] Inspect board selection, authored-number colors, proof/conflict highlights and note legibility, especially at large system text
- [ ] Review hint explanations and six Coach practice lessons for understandability. Improve only demonstrated pain points.

**B complete when:** core/JVM regression passes; targeted Android UI checks pass; each adjusted behavior has a before/after observation.

## Phase C — Existing Collection and Profile polish

- [ ] Cross-check all **50 current cosmetic entries** for unique IDs, correct bundled art/font/board mappings, preview vs full-grid styling, legible digits 1–9, selection/lock/equip states and backup-safe restoration
- [ ] Review provisional board/ink colors on a real phone; test partner/error/proof and normal/highlight combinations. New palette target >=4.5:1; legacy contrast tracked honestly
- [ ] Check 22 portrait assets and five font licenses/registrations; avoid creating duplicate families or fonts before confirming existing catalog quality
- [ ] Profile/achievements/statistics/history: verify names, counts, progress, empty states and export usability
- [ ] Do not implement weeklies or gift codes yet: permanent unlock ownership and record-pruning migration need a **separate future design/QA gate**.

**C complete when:** preview accurately represents gameplay, locked items state the correct requirement, and equip/restart/backups behave consistently.

## Phase D — Reliability, accessibility and LAN verification

- [ ] Solo and Daily resumes, device-local rollover, background/foreground, import/export, save corruption recovery and older-data migration
- [ ] Crash/ANR and memory/performance evaluation on a realistic Android device matrix; avoid unsubstantiated stability percentages
- [ ] TalkBack, large text, accessible touch targets, landscape/narrow screens, and high-contrast board states
- [ ] **Deferred physical two-phone gate:** Co-op edit/notes/chat, independent Versus answers and counts, both sides' finished result, host rematch, disconnect/rejoin/room recovery, matching-version cosmetics
- [ ] Protect local data. Original QA signature is not currently available in GitHub Actions; do **not** create an incompatible install-over APK.

**D complete when:** evidence log records device/build, reproduction steps and results. Physical LAN must be complete before describing multiplayer release QA as finished.

## Phase E — Unified isolated Beta 1.2 preview

- [ ] Build from one reviewed source commit only after approved polish slices
- [ ] Run `:core:test`, `:app:testDebugUnitTest`, `:app:lintDebug`, Android UI/emulator checks and verify APK package/signature/SHA-256 and size
- [ ] Provide an **isolated** preview that does not replace either public or QA data; verify it installs and launches on the owner's phone
- [ ] Compare Beta 1.1 against Beta 1.2 with actual screenshots before final visual signoff
- [ ] Keep full same-identity QA install-over pending **original private QA signing key** and a higher version code

## Tracked references

- [Beta stabilization and release acceptance #43](https://github.com/PlayerEden/SudokuTogether/issues/43)
- [Collections and durable unlocks planning #50](https://github.com/PlayerEden/SudokuTogether/issues/50)
- [Beta 1.2 cosmetic source review](GRIDMATES_BETA12_THEME_PILOT_REVIEW_2026-11-16.md)
- [Beta 1.2 Ocean/ink source pilot](GRIDMATES_BETA12_OCEAN_AND_INK_PILOT.md)
- [Earlier v0.11J on-device observations](V0.11J_FOCUSED_AUDIT_QA.md)

The first A1 source changes are scoped to the existing Collection UI and its Android instrumentation tests; this plan alone is not evidence that A2–E have passed.

## Phase A2 first source-review observations (not device findings)

| Area | Confirmed in source | Next evidence required |
| --- | --- | --- |
| Home | A vertical scroll container, large Solo/Co-op/Versus targets, editable-only `fillProgress`, and the previously added `New puzzle difficulty` label for an active Solo puzzle | Watch a first-time player navigate new vs resumed puzzles; 320dp/200% text and portrait-to-landscape checks |
| Playing grid | `PlayGridViewport` measures metadata and a bounded grid in one layout pass; `DockTool` gives keypad/action buttons a >=52dp minimum height | On-device 81-cell tap targets, note density, proof/error colors, and keypad reachability on short landscape screens |
| Collection | Family chips are one horizontally scrolling row; gallery renders available then locked items; preview uses a stylized **3×3** sample instead of a real 9×9 puzzle | Validate visual match between thumbnail, preview and actual 9×9 board, plus clipped/selected family handling |
| Profile | Personal statistics distinguish assisted/unassisted Solo and Daily; achievements derive from local records | Empty/long-name/200% font screenshots and no-regression backup checks |
| Settings | Classic number display, touch feedback and Solo note cleanup already exist | Confirm toggles and descriptions match actual gameplay, especially classic-number appearance |
| Multiplayer | Co-op board selection and Versus presence/result states exist | Real two-phone finished results, restart/rematch and reconnect sequence—**not** established by code review |

These observations set a test itinerary, not permission to redesign all six areas. Record an issue only after an actual reproducible defect, usability observation, or failed regression.
