# Beta 3 progress — active checkpoint

## B3-04 — bounded Coach fixture discovery

Naked triple gate 45562709560 passed; previous ad hoc search timed out. Added opt-in JVM discovery test using fixed random seed and original completed grid, bounded uniqueness checks, 5000 attempts and 90-second search budget. Validates discovered triple eliminations independently via solution count and prints reproducible grid and proof. Dedicated unsigned workflow uploads search log; ordinary regression skips discovery. Successful workflow alone does not prove a fixture was found: inspect COACH_FIXTURE_FOUND marker. Next pin verified fixture and add practice lesson. No runtime changes.


## B3-04 — naked triple engine, slice 5

Multi-step navigation gate 45562106365 passed at 88949996. Added Coach-only naked triple search after existing six techniques, exact pre-step candidate evidence and observation prompt. Unit-mask tests cover three nonidentical pairs, independently enumerate distinct assignments, reject four-digit unions, no-op and malformed input. Full uniquely-solvable puzzle integration fixture and practice lesson still required; no claims of device validation. Existing grading branch exits before additional Coach techniques. Fresh CI pending; next hidden triples after integration audit.


## B3-04 — graduated multi-step navigation, slice 4

Candidate demonstration gate 45561577010 passed at 81c98927 (instrumentation compiled, not executed). Coach now tracks explanation visibility separately per deduction using saveable primitive index array; navigation is visible from start, Next requires current logical explanation, revisiting explained steps preserves their visibility, new steps start with observation instead of automatically exposing answers. Earlier deduction context label added; explicit reveal remains separate. Updated multi-step UI fixtures. No score, solver, generation, live board or save schema changes. Fresh CI and physical review pending. Next mathematical expansion with verified triple fixtures.


## B3-04 — candidate result demonstration, slice 3

Live Coach elimination steps now offer an explicit Show remaining candidates / Show deduction toggle after explanation. Target squares display exact surviving candidates from pre-step proof evidence; text lists before → after per affected square. Toggle resets with step navigation; no live puzzle mutation or score changes. Added UI fixture assertions for candidate result and return to deduction. Fresh CI and device verification pending. Research backlog remains separate from implemented support.


## Coach research backlog

Owner requested research through extreme Sudoku techniques. Added docs/BETA3_COACH_RESEARCH.md with reference links, proposed curriculum, correctness gates and scope controls. No external code/content imported, no new runtime capability or grading change. Next proof demonstration, then verified technique expansion.


## B3-04 — teaching architecture direction

Guided observation gate 45559098905 passed at 9108642d. Owner emphasized genuine coaching and suggested The Last Taters as inspiration. Read README and TATER_BRAIN_LAB at pinned source 0c768b7f; proposed explainable local teaching architecture in docs/BETA3_COACH_TEACHING_ARCHITECTURE.md. No code reuse or adaptive tracking implemented; next before/after proof presentation. Instrumentation still compiled only.


## B3-04 — guided observation, slice 2

Proof fidelity gate 45558322123 passed at 007120ad (including JVM trace tests; instrumentation compiled only). Added technique-specific observation prompts before Show why, without exposing specific answer digits, and clear explanation that crossed-off marks are demonstration-only. Existing full explanation and reveal gates retained, no scoring or solver changes. Expanded CoachProofUiTest for prompt visibility, hidden explanation and example-only guidance. Fresh CI and physical review pending.


## B3-04 — Coach proof fidelity, slice 1

Audited existing six techniques, six practice fixtures, graduated hints and explicit solution-check/reveal paths. Added per-step candidate snapshots for placement target and elimination proof cells, captured before mutation. Live proof supporting marks now intersect support digits with actual candidates at that step; no invented candidates. JVM test replays candidate removals for all six practice traces and checks snapshot fidelity. No change to solver scan order, generation grading, assistance counting, persistence or practice eligibility. Fresh CI and physical Coach review pending. B3-03 portrait evidence recorded; remaining integrated tests open.


## Gameplay Review 1 — physical portrait evidence

Owner supplied five physical screenshots of Solo Easy gameplay, showing stable board/dock positioning across selection, value entry, notes and conflict states; disabled Erase/Undo on initial empty square; enabled controls after entry; remaining counts updated for entered digits; four active pencil marks 2/4/6/8 reflected in keypad check indicators; repeated entered digits red and underlined even after moving selection; two-line conflict caption fits without board movement. No explicit owner acceptance statement yet. Screenshots establish visible behavior, not executed undo/erase, note removal, completion/reward expansion, persistence, Daily, landscape, large text, multiplayer or TalkBack validation. Note candidates are manually entered, not automatically validated. Review source e00e2b1954b69c027bbef73f8f572a4f6dc84633; ordinary gate 45555816433 passed; preview run 45555816455 completed/success; artifact 14996246056. Keep current visual layout pending owner feedback; next milestone B3-04 Coach can proceed independently with outstanding review items tracked. No production release or upgrade test.


## Gameplay Review 1 verification checkpoint

Source e00e2b1954b69c027bbef73f8f572a4f6dc84633 passed ordinary Android gate 45555816433. Isolated preview run 45555816455 passed both artwork checks and adaptive icon generation; assembly/signature/artifact pending at observation. Build URL https://github.com/EdenP-Development/SudokuTogether/actions/runs/45555816455. Owner review: notes toggling/highlight, empty erase, undo, conflict marker, board resize, large-text/landscape layout, finish/earned rewards, resume and Daily records. Separate fresh-save debug package; not a public upgrade test. Instrumentation compiled, not executed. Freeze review source until feedback; next major milestone B3-04 Coach.


## B3-03 — completion presentation and integrated Gameplay Review 1

Compact toolbar gate 45555324321 passed at 54d59364; instrumentation compiled, not executed. Completion card centers heading and summary text, makes Records a full-width 48dp action and allows expanding all earned rewards from the existing record-derived list rather than hiding extras permanently. No reward logic changes. Existing finish fixture now checks Records reachability. Isolated Gameplay1 preview configured with unique temporary debug package com.playereden.sudokutogether.qa.beta3gameplay1, exact approved startup and fox artwork, unchanged habitat assets. Fresh gates, APK receipt and physical review pending. Reward expansion needs device verification. No public release or save migration.


## B3-03 — compact controls, slice 4

Board clarity gate 45554437011 passed at e7905516 (instrumentation compiled, not executed). Narrow toolbars below 340dp at font scale above 1.3 use two controls per row; narrow large-text page below 364dp switches to whole-page scroll to preserve board/control reachability. Normal text layouts unchanged. Added narrow portrait reachability instrumentation fixture. Fresh CI and device execution pending. Next review completion card and prepare integrated Gameplay Review 1. No changes to approved branding, game rules, saves or signing.


## B3-03 — board clarity, slice 3

Prior repaired Android gate 45554050396 passed at 7ba1edda (instrumentation compiled, not executed). Add non-color underline to conflicting entered values, retaining fixed clue styling and Coach proof distinctions; add accessible matching-number and related-square states. New instrumentation fixture checks conflict/related/matching announcements and correction. No solution validation, scoring, rule, save or artwork change. Fresh CI and physical review pending.


## Current development checkpoint — 2026-11-24 local

Active branch: feat/beta3-foundation. Visual4 source 96747991e98f78c90e10d6eda447d1be085a3674 passed visual CI 45550962814 and Android gate 45550962759; artifact 14993249624. Owner physically approved both startup screens and revised nominal 3-second timing. See BETA3_STARTUP_PILOT.md for exact asset hashes, implementation and remaining technical validation. Fox launcher rendered physically at 58dp; all four habitats rendered in earlier owner screenshots. Native launcher promotion, habitat persistence/contrast acceptance, original archive durability and startup accessibility/lifecycle/performance verification remain open. No public release or cross-project rollout here.

B3-03 ACTIVE: e1ab642465b8e4cf3f750e7372e002e06b282b71 added highlighted pencil candidates and context-aware erase; e0adae262e6738669dcf75e2ed2c6b3025ccadf3 added reserved guidance/caption space. CI 45552906603 failed Android instrumentation compilation because DpRect width/height accessors were unavailable; corrected by subtracting edges, fresh CI pending. Next: verify fixes, audit board clarity, compact/large-font controls and completion UX; group improvements into Gameplay Review 1 rather than frequent micro-builds. Coach, collection/progression, sound/haptics, multiplayer and integrated release milestones remain as scoped in roadmap. Preserve puzzle logic, 85 cosmetics, 47 achievements, save compatibility and signing. Public Beta2 version 31 remains untouched. No percentage estimate is an engineering measurement.


## B3-03 — layout stability, slice 2

Audit identified conditional keypad guidance and variable board context height feeding viewport sizing. Keep a two-line guidance reservation across selection/notes states; show contextual number/notes/filled-square guidance. Reserve two board caption lines normally and three at larger text sizes while allowing further wrapping. Add instrumentation assertions for board width and dock height across note mode changes; compile/run and physical review pending. No game rule, scoring, save or branding changes. Extreme text/width combinations may still reflow; do not claim universal fixed board size.


## B3-03 — gameplay input feedback, slice 1

Notes keypad now highlights existing pencil marks in the selected editable blank square, with stronger outline, tint, check indicator and selected accessibility semantics. Erase disabled on empty unmarked squares; remains available for notes or entries. No change to entry rules, count computation, scoring or persistence. Existing keypad accessibility test expanded to cover selected notes and erase availability. CI and physical review pending. Approved startup unchanged.


## Owner visual acceptance — Visual4
Owner supplied two physical portrait screenshots of the actual Spudacus and GridMates startup screens and stated “I think that was pretty much perfect.” Accept the visual presentation and nominal timing as the owner-approved pilot design. Visual4 source 96747991e98f78c90e10d6eda447d1be085a3674; visual CI 45550962814 PASSED, artifact 14993249624; standard gate 45550962759 PASSED. Both exact asset checks, compile/JVM regression/lint/assembly/package/signature checks passed; startup instrumentation compiled, not executed. Screenshots show centered aspect-fit logos, cream background and no additional wording. Do not infer that tap/Back skip, reduced motion, warm resume, rotation, offline behavior, accessibility or performance were individually physically tested; these remain verification items. Three-second nominal sequence is a skippable overlay, not readiness-based; actual wall time not measured. Owner intends to return this accepted visual design to shared branding chat for central standard and subsequent adoption. No other-project changes or public release performed here. Continue B3-03 gameplay milestone after handoff.


## Visual4 — owner timing feedback

Visual3 build and separate Android gate passed (45547418369, 45547547064). Owner physically saw both logos but could not capture screenshots due to short timing; agreed tap-to-skip allows longer holds. Timing revised to 250/1000/250ms per logo, nominal 3s combined. Preserve exact assets, fox size, habitat art and existing skip/reduced-motion handling. Visual4 isolated review CI pending; no final approval or other-project rollout.


## Latest checkpoint — GridMates v1.0 two-stage startup pilot

Visual2 CI 45546351053 and standard gate 45546362833 passed. Owner approved exact GridMates series logo and Sudoku-only pilot. Implementation commit fb9e4afd91d6aa58fac642d932e9f5620c7c60af bundles exact GridMates asset, enables second stage, configures both checksum gates and Visual3 isolated debug build, and updates B3-01 roadmap. See docs/BETA3_STARTUP_PILOT.md for source hashes, timings, scaling, skip behavior and limitations. Fresh CI and physical feedback pending. No rollout to other games or public release; existing production package/signing/saves unchanged. Next verify Visual3, obtain owner feedback and refine before documenting reusable central implementation. Continue existing Beta3 gameplay roadmap.


## Latest checkpoint — Visual Review 2 implementation

Visual1 physical screenshots show fox launcher and default/Ocean/Garden/Farm/Woodland habitat switching, not complete acceptance. Visual2 source 8be7f32606297e9934f42f16d05351af6383b800 adds exact Spudacus startup overlay (780ms nominal, silent, tap/Back skip, disabled-animation skip, stop/recreation skip), optional disabled GridMates stage, stronger non-Woodland caption gradient and 58dp adaptive foreground (previously 54dp). Isolated package com.playereden.sudokutogether.qa.beta3visual2 uses temporary debug signer; public Beta2 untouched. CI run 45546351053 observed checksum and icon generation passed, compilation pending; newer ordinary quality gate 45546362833 includes stage ordering tests at 63467c670d8867688faa3cc4c9bb188ed277f8fb. Earlier independent Spudacus checksum and baseline gate 45545507332 passed. Next inspect both final runs, deliver verified Visual2 APK, physically test cold/warm launch, skip, reduced motion, orientation, habitat readability and masks. GridMates art pending; Woodland wrapping and native launcher promotion still open. No public release or save changes. Historical checkpoints below.


## Owner physical Visual Review 1 screenshots — 2026-11-24 local
Six owner screenshots show the fox launcher installed alongside existing Sudoku, default Woodland home, equipped Ocean octopus, Garden bee, Farm cow and Woodland owl with corresponding habitat and wording. Menu layout remains visually consistent; separate preview displays fresh daily progress. This is physical visual evidence, not explicit final owner acceptance, save migration, restart persistence or full gameplay testing. Assistant review: consider modest launcher foreground enlargement only after mask checks; strengthen local text gradient on brighter Garden/Farm and examine Woodland three-line wrapping. Preserve existing approved masters and companion style; retain header grid icon pending shared GridMates identity decision. No personal screenshots committed. Next: implement Spudacus startup hooks and targeted presentation refinements in subsequent isolated preview; GridMates stage disabled until approved art. No public release.


## Latest checkpoint — Visual Review 1 passed; shared branding adoption

Visual Review 1 run 45543876470 PASSED (source 6fb83504a13534e046ea9af96e835c7cf1125656); artifact 14990771979, https://github.com/EdenP-Development/SudokuTogether/actions/runs/45543876470/artifacts/14990771979 ; standard Android gate 45543853735 PASSED. Isolated visual preview only; physical acceptance pending. Owner approved adding shared Spudacus v1.0 startup sequence to B3-01, roadmap commit f7e55e3255a696156dd9ccf6f7cc13bd567d725e. Exact source blob 7f64339220f851c7e36ae1b0c0662892c0ed1694 copied to app/src/main/res/drawable-nodpi/spudacus_v1_full_transparent.png in 33a6c972663ab30618692fc8b1d100e33673db0b. CI SHA-256 gate committed a3133a4bf014dc171c0502515e614fdb3031e35f; fresh result pending. Startup behavior NOT implemented yet. Next implement lifecycle-safe silent cream splash, tap skip, reduced motion, no forced readiness delay, disabled GridMates stage pending approved art. Keep Visual1 unchanged for owner review; next separate preview after splash tests. No public release or save/signing changes.


## Current checkpoint — Beta 3 Visual Review 1 (2026-11-24 local)

Resume on `feat/beta3-foundation`. Branding transfer commit 61c27feb7eeae9d552c9abc59579a2bebaa9f0a7 contains 14 raw archive parts. Independent verification [45543787474](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45543787474) PASSED: archive checksum, six manifest hashes, actual image formats, dimensions and decode. Wide cover and social image are WebP with historical PNG filenames; verified artifact exports correct extensions without altering bytes. Preserve original transport until native archive/import is durable. Habitat quality gate 45531206695 PASSED (not physical visual acceptance). Visual preview workflow source 6fb83504a13534e046ea9af96e835c7cf1125656; [run 45543876470](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45543876470) observed icon generation successful, Android compilation in progress. Separate ordinary quality gate 45543853735 in progress. Preview only uses generated adaptive fox foreground, retained monochrome glyph, temporary debug signer and isolated package com.playereden.sudokutogether.qa.beta3visual1. No public APK, signing or save changes. Next: inspect final CI status and artifact receipt, fix failures if any, deliver isolated review APK and mask samples, obtain owner crop/habitat feedback. Native source integration and public release remain pending. Older checkpoints below are historical.


2026-11-24 — B3-005: four original habitat PNG blobs accepted via individual Google Drive transfer. Both Ocean candidates retained in design; second (treasure chest) provisionally selected for review. Garden and Farm integrated, Woodland/default unchanged. Runtime references same original blobs, no compression yet. Darker left overlay for new art; actual crop/readability and independent pre-transfer SHA-256 verification pending. New CI pending. Large 8.32 MB branding ZIP failed GitHub request; icon still pending. No production changes. Exact successful connector sequence: upload_file(file_uri=original conversation file ID), get_file_metadata, fetch(url,download_raw_file=true,include_base64=true), use result.b64_string directly in create_blob(encoding=base64), create tree/commit/CAS update. Do not encode twice or print bytes.

## Active handoff — 2026-11-24: habitat artwork review

Resume on feat/beta3-foundation. B3-001 and B3-003/004 implemented; unsigned quality gates 45519754181 and 45527791591 completed successfully (core/Android JVM tests, instrumentation compilation, lint, debug assembly; not emulator execution). B3-002 fox launcher artwork and B3-005 new environments generated, but image binaries not committed or integrated. Existing Woodland scene remains unchanged. Owner responded positively to the habitat set. Ocean has two generated candidates (reef/shipwreck and reef/shipwreck/treasure chest); choose final after comparing actual banner crop, do not silently assume a selection. Garden: sunlit cottage garden with rose arch, flowers and path. Farm: red barn, windmill, fence and golden fields. Small environmental creatures are decorative, not equipped companions; verify composition and readability with overlays. Preserve exact masters, no text baked in, left text space and right companion space. Next: resolve binary transfer/archive, create runtime exports, test crops, map resources with safe fallback, integrate approved launcher with adaptive mask checks, then produce isolated visual QA build. If binary transfer blocked, continue targeted gameplay layout work rather than regenerate approved artwork. No production APK change. Physical multiplayer evidence and outstanding release housekeeping remain in foundation audit. Older handoffs below are historical.


2026-11-24 — B3-003/004 first implementation: presentation-only CompanionHabitat mapping for Woodland/Ocean/Garden/Farm/default, corrected Farm caption, catalog and missing-art JVM tests. Existing Woodland scene and non-Woodland neutral gradients preserved until illustrated masters approved. B3-002 exact six evergreen assets packaged in conversation ZIP with SHA-256 manifest, Git binary transfer/launcher mask integration pending. B3-001 prior gate green. New CI pending. No changes to saves, networking, package or signing.

2026-11-24 — Physical Easy Co-op completion evidence recorded, contribution statistics already exist. Prioritize completed-state disconnect copy and board layout in B3-07. B3-002 approved three icon assets recovered and hashed locally; binary transfer/integration still pending. B3-001 quality gate passed; instrumentation execution pending.

2026-11-24 — Physical Co-op playtest evidence added; owner reports rejoin works decently overall. B3-07 now has partial physical evidence for both modes, all three Co-op difficulties, with board layout and connection UX follow-ups. See foundation audit. B3-001 CI status: success.

2026-11-24 — B3-001 first implementation: explicit successful backup/restore status drives green/check Notice on Settings and Home; default warnings unchanged, recovery state suppresses success. Extended existing Android instrumentation test. Baseline CI 45513523860 passed; new branch CI pending. No production release or save format changes. B3-00 remaining archival and measured/physical checks still open.

2026-11-24 — Owner supplied physical Beta 2 Versus playtest screenshots; partial real-device evidence and B3-07 follow-ups recorded in BETA3_FOUNDATION_AUDIT.md. Reconnect result unknown. Baseline CI run 45513523860 status at check: completed / success.

2026-11-24 — Second B3-00 static audit slice: persistence, LAN protocol, accessibility and asset references. Existing unsigned Android quality gate enabled on this branch to establish fresh baseline; run result pending. No runtime changes. Next verify actual CI outcome, then typed backup notice fix; archive and source promotion remain open.

2026-11-24: B3-00 ACTIVE. Initial static source/tree and branch audit recorded in [BETA3_FOUNDATION_AUDIT.md](BETA3_FOUNDATION_AUDIT.md). Active branch `feat/beta3-foundation` created from f0e0ec350344dffc0dfc670681ce233d4665644c. Next: finish focused audit and establish fresh regression baseline, then typed backup feedback and icon. No runtime changes or fresh tests yet. Existing signed Beta2 unchanged.

Historical planning checkpoint follows.

# Beta 3 progress — living checkpoint

Last updated 2026-11-24. Planning only; no Beta 3 gameplay implementation or fresh audit completed.

- Active continuation branch: feat/beta2-permanent-identity-staging (do not assume main is release baseline).
- Accepted Beta 2 runtime source: c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c.
- Pre-roadmap documentation head: 52ae50db0229e5c1c4147dec2a694fdedfe1dcdf.
- Next milestone: B3-00 foundation audit.
- Roadmap: [BETA3_ROADMAP.md](BETA3_ROADMAP.md).
- Next concrete action: inspect actual code, release branch/PR status, asset inventory and test/build evidence; produce prioritized findings and choose safe development branch.
- Outstanding housekeeping: durable exact APK and approved artwork archive, reviewed source promotion, independent public download verification, owner independent key backup unconfirmed. Existing public release unchanged.
- No fresh CI run, no new APK, no new runtime edits for this planning checkpoint.

## Session log
2026-11-24 — Owner accepted roadmap direction; canonical roadmap and handoff created. B3-00 next.

## Template for subsequent entries
Date | milestone/status | branch/commit | changes | tests/evidence | owner decisions | unresolved/deferred | next action.
