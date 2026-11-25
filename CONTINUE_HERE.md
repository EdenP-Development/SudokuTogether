# Continue GridMates: Sudoku

## AUD-07.02 — foreground playback and integrated review

Preference gate 45691354185 passed at 6144b9b. StartupBranding reports actual completion/skip, MainActivity enables music only afterward and outside backup lock. Previously enabled bundled soundtrack automatically starts on foreground, stops on pause/stop, restarts selected track on normal return; audio focus or playback failure latches interruption until explicit Play, switch or selection. No automatic import playback, no background service. AudioReview preview includes all six music files, effects, haptics and preferences; new CI and physical tests pending. Public Beta2 unchanged.

## AUD-07.01 — local music preferences

Completion gate 45690392399 passed at 444d577. Music Corner now stores enabled preference and slider volume in separate device-local SharedPreferences, independent from progress/backup schema. Explicit Play/switch/track action starts playback; restored enabled preference does not yet auto-start (silent splash and foreground resume policy pending). Volume sanitized; fresh gate and handset verification pending. No public release changes.

## AUD-05.03 — completion feedback

Feedback repair gate 45689483591 passed at 54b0f95. Added original 720ms three-note completion cue, higher SoundPool priority, confirmation haptic on API30+ with keyboard fallback. Guard emits once on observed same-round incomplete-to-complete transition, no first-loaded completed board or changed-round celebration, suppress Versus loss cue. Does not stack separate reward sounds. New tests for round guard and cue duration. Fresh gate and handset listening pending; no public/save changes.

## AUD-05.02 — feedback compile repair

Bundled soundtrack preview 45686336504 passed at 6c65acfb (all six APK hashes verified). Interaction cue gate 45686836413 failed unresolved feedback in PlayGridViewport. Pass callback through PlayBoardPane, suppress selection cue on completed boards, use descriptor-based SoundPool loading before temporary WAV deletion, detach listener on release. New gate pending; no handset verification. Completion/reward cues deferred until this gate passes. Codex review quota is not a build dependency. Public Beta2 unchanged.

## AUD-04.03B — bundled soundtrack preview

Archive gate 45684820645 passed. Isolated preview now packages six verified uncompressed AAC assets, recognizes main theme, hides import controls when bundled assets available, retains manual Play and foreground stop. APK gate verifies all six asset hashes and storage method. Fresh CI and handset playback pending; no public release or permanent preferences yet. Portfolio pilot handoff remains open.

## AUD-04.02 — Music Corner review

Provisional shared soundtrack UI in debug Settings: current track, previous/play-stop/next, volume, collapsible selectable playlist, import controls. Track names cached on selection; underscore-normalized main theme recognition. Existing activity-scoped pilot remains; no bundled music or permanent settings yet. Previous soundtrack CI was in progress at inspection. MusicCorner preview workflow triggered, same isolated package and temporary debug signing (install-over not guaranteed). Fresh CI and handset test pending. Next production asset transfer and controller, original effects and haptics. No public release changes.


## AUD-03 / AUD-04 soundtrack review

Six masters verified; inventory and checksums in docs/BETA3_SOUNDTRACK_INVENTORY.md. Owner-approved main theme Warm Puzzle Time. Local AAC working copies prepared; no binaries in repo yet. Debug playlist review implemented with main theme first, cyclic non-repeating rotation of remaining shuffled tracks, next track and existing foreground music controls. Preview workflow renamed SoundtrackReview, package unchanged. Fresh CI and handset playback pending. Permanent bundled playback, settings, effects and haptics still open.


## AUD-02 — soundtrack production direction

Six proposed production briefs recorded in docs/BETA3_SOUNDTRACK_DIRECTION.md. No production masters received yet. Next AUD-03 inventory and AUD-04 foreground playlist controller; keep native effects/haptics issue open. No runtime or public release changes.


## AUD-01 — audio research active checkpoint

B3-07 code review closed: gate 45677167962 passed; physical multiplayer and instrumentation execution remain pending integrated review. B3-08 active, AUD-01 research recorded in docs/BETA3_AUDIO_RESEARCH.md; next AUD-02 soundtrack identity and track audition. No new runtime audio changes. Owner reports Suno Pro; positive single-track pilot, native effects/haptics failed owner perception. Preserve public Beta2 and saves.


## B3-07.05 — Final multiplayer presentation pass (2026-11-25)

Co-op next-round guard gate 45676240771 passed at b621b1e. Completed Co-op guest now sees one reconnect action within finish card; host next-puzzle action disabled while busy, connected guest gets host-away guidance. Scroll resets per session/epoch/completion in both modes, portrait Co-op status reserves two text lines and announces changes. Added Compose regression fixture for finish controls (compile/run status pending). Owner reports Suno Pro subscription; start dedicated audio research next after integrated gate, with fresh licensed tracks and original SFX/haptic design. B3-07 code closure candidate, physical two-phone and responsive checks still required in integrated review. No public release or save/protocol changes.


## B3-07.04 — Co-op next-round guard and audio checkpoint

Owner handset feedback positive for Gentle Rests gameplay music; Settings screenshots show Play/Stop and volume. Native touch effects and haptics reportedly not noticeable/working; exact device settings unknown. Music pilot gates 45671002042 and 45671002085 passed. Owner exploring Suno Pro and soundtrack creation independently. Defer finished audio integration to B3-08 audio design research: music placement, silence, transitions, loop/playlist strategy, volume/focus/lifecycle, short effect/haptic design, asset size and handset validation. No new soundtrack or effects in this checkpoint. Existing Versus rematch has epoch/foreground/returning-rival regression; Co-op previously lacked equivalent stale-epoch guard. Add guarded host nextCoopPuzzle and generation/visibility callback, plus JVM regression for cancellation, duplicate stale request, identity/style and board reset. Fresh CI pending. Next verify and refine completed Co-op next-puzzle controls/layout. Public Beta2 unchanged.


## Music gameplay pilot

Previous audition gate 45669505340 passed. Moved existing playback owner to activity composition so music continues across Settings, Home and gameplay. One local file, Play/Stop and volume; foreground only, stops on focus loss or background, no automatic restart. No bundled track. Preview workflow triggered with MusicPilot artifact name, retains existing isolated coach2 package for upgrade continuity. Fresh CI and handset test pending. Owner selects Gentle Rests once using Settings, starts music and opens puzzle. Resume multiplayer afterward.


## Private music audition checkpoint (2026-11-25)

Owner supplied Gentle Rests.m4a, inspected locally: Opus stereo 48kHz, 177.96s, 3,171,094 bytes. Sole private prototype, no audio bytes committed. Debug-only settings panel uses Android document picker, async MediaPlayer loop, volume and audio focus; stops on lifecycle pause/stop or panel exit, no persistent URI or background playback. This is an audition, not in-game music yet. Release UI excluded by DEBUG check. Fresh CI/device tests pending. Reconnect identity gate 45591876027 passed. Resume B3-07 rematch work after audition, public Beta2 unchanged.


## Current checkpoint — B3-07.03 reconnect identity guard (2026-11-25)

Integrated Co-op/Versus presence gate 45591133224 passed at 714ca811. Reconnect audit found the guest callback recorded multiplayer results before checking expected room identity. GuestClient now optionally validates expected session ID before emitting welcome, and ViewModel independently rejects mismatched snapshots before records or mirror writes; guards duplicate/busy/non-join activation. Added loopback JVM fixtures for rejected and accepted room identity. Wire messages unchanged; server admission can still occur before client rejects a wrong welcome, so not a new authentication guarantee. Fresh gate pending. Next audit rematch and completed Co-op reconnect control, then layout/integrated release review. Physical two-phone checks and signed upgrade remain pending. Audio prototypes private and unlicensed for distribution. Public Beta2 unchanged; no publication authorized.


## B3-07 handoff — audio pilot verified; soundtrack rights hold (2026-11-25)

Gate 45588866784 passed at 263bbba9: optional native sound feedback, fail-safe dispatch and JVM tests; portable restore instrumentation compiled, not executed. B3-06 REVIEW; custom music deferred and not a release dependency. Suno official terms and FAQs reviewed: four owner-reported free-tier M4A prototypes restricted to personal noncommercial experiments; later subscription does not automatically clear prior tracks; obtain explicit track-specific rights or regenerate/download while commercially licensed. No prototype binary in public repository or release. Shared provisional soundtrack families documented at https://github.com/EdenP-Development/Eden-Development-Standards/blob/8febbd923f663889d8ea00160f1c5f78af16d9d3/audio/gridmates/README.md (Sudoku/Word Search cozy, Grid Hunt/Crossroads proposed board-strategy). B3-07 ACTIVE: next inspect LAN presence/rejoin, status UI, rematch and regression coverage before targeted edits. Physical multiplayer, audio and upgrade checks remain open. No protocol, save, signing or publication changes.


## B3-06 — feedback reliability and soundtrack checkpoint (2026-11-25)

Reward continuity gate 45585519590 and sound pilot gate 45586137570 passed. B3-05 REVIEW, B3-06 ACTIVE. Audited state initialization/home/reset/restore: soundEffects default false, reread after restore; backup allowlist only writes known portable preferences and leaves device-local soundEffects untouched. Added instrumentation fixture for both on/off restore preservation and exclusion from export; not executed yet. Native click dispatch now fails silently on device RuntimeException so gameplay continues; copy-room-code retains haptic only. Added JVM feedback regression fixtures. Fresh CI pending. Owner generated M4A tracks 01 Woodland Study, 02 Quiet Pages, 03 Good Puzzles, Good Company, 04 Sunday Morning; owner likes them, files not yet supplied, no audio audition or rights verification. Owner reached free Suno limit and deferred further generation. Do not block Beta3 on custom music or assume commercial rights (generation plan at creation matters). Next finish feedback verification and advance multiplayer refinement while soundtrack intake deferred. No public release, signing or save schema changes.


## B3-05 reward continuity and future audio direction (2026-11-25)

Medal detail gate 45584899760 failed on missing FontFamily import; corrected. Existing core audits cover deduplicated records, stable ownership after pruning, eligibility and bulk progress. Added cross-ledger regression for all 85 cosmetic and 47 medal IDs/counts, valid emblem references, assisted solve exclusion from clean medals and retained ownership after record pruning. Fresh integrated CI pending; no threshold/save changes. B3-05 remains active until gate verified, then B3-06 optional restrained tactile/audio feedback. Owner wants future Suno experiments with assistant-written prompts for original instrumental background music and potentially useful sound material, owner generates and supplies files. Not a Beta3 release dependency. Verify commercial rights for specific generated assets and document provenance before integration; audition/trim/loop/normalize offline assets, no mandatory music or online dependency. No audio generated or added at this checkpoint.


## Current owner decision — Coach scope freeze; B3-05 active (2026-11-25)

Coach Review 2 gate 45580000685 and preview 45580000688 passed; artifact 15006471379. Owner defers new Coach techniques and substantial teaching expansion until Beta 4 following Beta 3 feedback. Ten techniques/lessons retained; correctness, performance, accessibility and release regression audits remain in scope. No requirement for immediate owner Coach2 playtest; device instrumentation not executed by these gates. B3-04 REVIEW / feature scope frozen, not full release acceptance. B3-05 ACTIVE: static collection review found existing chapter navigation, equipped previews, next unlock and responsive grids. Added per-chapter available/total counts and progress tracks to All icons view using existing progress calculation, no unlock/identity/save changes. Fresh CI pending. Next inspect collection regression and remaining progression UX; then sound/haptics and multiplayer. Preserve public Beta2 identity, 85 cosmetics and 47 achievements; no publication authorized.


## Owner direction — audit-led Coach development (2026-11-25)

Owner prefers code verification and audits as the primary Coach development gates, with deeper real-world gameplay feedback after Beta 3 is published on itch.io. Do not block each technique or small UI increment on manual owner playtesting. Continue bounded deterministic pattern tests, independent solution oracles, generated-puzzle and regression audits, performance/invalid-input checks, UI automation where executable, and save/scoring isolation checks. Distinguish compiled instrumentation from executed tests. Consolidate physical checks at meaningful UI/release checkpoints; public feedback complements rather than replaces pre-release correctness and upgrade-safety checks. Coach Review 2 is optional for immediate owner review, not a prerequisite to further engine development. Next: audit existing ten-technique coverage and gaps, then expand advanced techniques with verified original fixtures. No publication authorized by this direction.


## Latest — Coach Review 2

Integrated gate 45579221416 passed. Coach1 owner screenshot feedback implemented: evidence stage, stable board anchor, explicit hidden/naked single reasoning, read-only error review, guarded erase, responsive footer and four-section ten-lesson curriculum. Coach2 isolated APK configured; inspect latest preview run and verify artifact before sharing. Physical testing next; no production upgrade rehearsal or device instrumentation execution claimed. Then consider additional advanced techniques after feedback.


## Latest — Coach Review 1

Ten techniques and lessons integrated. Isolated Coach1 preview configured on top of shared startup timing trial (dd1ea099, 8db5c3a8). Inspect latest beta3-visual-preview workflow and ordinary gate; verify artifact package/signature before offering download. Physical review next: ten lessons, multi-step hint navigation, before/after, startup timing, gameplay. No production upgrade test.


## Latest — ten Coach techniques and lessons

Discovery 45571340860 FOUND three independently validated original hidden quadruple fixtures; gate 45571340874 passed. Seed 183027 attempt 4751 is tenth practice lesson; other two retained in regression. Fresh CI pending. Next prepare isolated Coach Review 1 APK; no public release, signing or save changes.


## Latest — naked quadruple practice

Discovery 45567877813 FOUND original unique fixture on attempt 613; ordinary gate 45567877886 passed. Ninth practice lesson and proof regression added. Verify fresh CI, then hidden quadruple engine and original fixture. Full logical solve not claimed; fixture ends in explicit reveal. Coach Review 1 planned after ten techniques/lessons. No release or save changes.


## Latest — hidden triple practice

Discovery 45566580197 found original unique fixture on attempt 72; ordinary gate 45566580191 failed one ambiguous synthetic pattern test. Corrected test input and added eighth practice lesson with permanent proof assertions. Verify fresh CI before advancing to quadruples. Full logical solve not claimed; fixture ends in explicit reveal. No release or save changes.


## Latest — Coach naked triple practice

Discovery run 45565311197 FOUND original unique puzzle on attempt 8, with independently validated naked triple. Seventh practice lesson and pinned regression now implemented; verify fresh CI and device review. Next hidden triple engine, fixture and teaching support. Existing public Beta2/save identity untouched. See docs/BETA3_PROGRESS.md.


## Latest — B3-04 Coach proof fidelity

Gameplay1 preview 45555816455 passed, artifact 14996246056; physical portrait evidence logged in acc2bd99. B3-04 active: exact pre-deduction proof candidates captured and used in live hint board, regression coverage added. Verify CI; next audit teaching copy, hint navigation and proof visualization, then consider additional techniques without changing generation grading. Outstanding gameplay device checks remain open. No public release.


## Latest — Gameplay Review 1

B3-03 slices 1–4 passed ordinary CI through 45555324321. Completion presentation and isolated Gameplay1 review workflow now configured; verify latest CI and artifact before owner testing. See docs/BETA3_PROGRESS.md for details. Do not mark instrumentation executed or production upgrade verified. After physical gameplay review continue Coach milestone; preserve approved startup, public Beta2 and save/signing continuity.


## Current development checkpoint — 2026-11-24 local

Active branch: feat/beta3-foundation. Visual4 source 96747991e98f78c90e10d6eda447d1be085a3674 passed visual CI 45550962814 and Android gate 45550962759; artifact 14993249624. Owner physically approved both startup screens and revised nominal 3-second timing. See BETA3_STARTUP_PILOT.md for exact asset hashes, implementation and remaining technical validation. Fox launcher rendered physically at 58dp; all four habitats rendered in earlier owner screenshots. Native launcher promotion, habitat persistence/contrast acceptance, original archive durability and startup accessibility/lifecycle/performance verification remain open. No public release or cross-project rollout here.

B3-03 ACTIVE: e1ab642465b8e4cf3f750e7372e002e06b282b71 added highlighted pencil candidates and context-aware erase; e0adae262e6738669dcf75e2ed2c6b3025ccadf3 added reserved guidance/caption space. CI 45552906603 failed Android instrumentation compilation because DpRect width/height accessors were unavailable; corrected by subtracting edges, fresh CI pending. Next: verify fixes, audit board clarity, compact/large-font controls and completion UX; group improvements into Gameplay Review 1 rather than frequent micro-builds. Coach, collection/progression, sound/haptics, multiplayer and integrated release milestones remain as scoped in roadmap. Preserve puzzle logic, 85 cosmetics, 47 achievements, save compatibility and signing. Public Beta2 version 31 remains untouched. No percentage estimate is an engineering measurement.


## Owner visual acceptance — Visual4
Owner supplied two physical portrait screenshots of the actual Spudacus and GridMates startup screens and stated “I think that was pretty much perfect.” Accept the visual presentation and nominal timing as the owner-approved pilot design. Visual4 source 96747991e98f78c90e10d6eda447d1be085a3674; visual CI 45550962814 PASSED, artifact 14993249624; standard gate 45550962759 PASSED. Both exact asset checks, compile/JVM regression/lint/assembly/package/signature checks passed; startup instrumentation compiled, not executed. Screenshots show centered aspect-fit logos, cream background and no additional wording. Do not infer that tap/Back skip, reduced motion, warm resume, rotation, offline behavior, accessibility or performance were individually physically tested; these remain verification items. Three-second nominal sequence is a skippable overlay, not readiness-based; actual wall time not measured. Owner intends to return this accepted visual design to shared branding chat for central standard and subsequent adoption. No other-project changes or public release performed here. Continue B3-03 gameplay milestone after handoff.


## Latest checkpoint — Visual4 timing review

Visual3 CI 45547418369 and Android gate 45547547064 passed. Owner physically confirmed both splashes appeared but too fast to screenshot, and agreed to longer timing with tap skip. Timing commit 914ef9af7fbf62c0be47d548b679bb462fb1d8c9: 250ms fade in + 1000ms hold + 250ms fade out per logo; 3 seconds nominal combined. Visual4 isolated debug package; no public changes. See docs/BETA3_STARTUP_PILOT.md. Next verify Visual4 artifact and owner review; then continue B3-03 gameplay audit. No other-project rollout.

## Latest checkpoint — GridMates v1.0 two-stage startup pilot

Visual2 CI 45546351053 and standard gate 45546362833 passed. Owner approved exact GridMates series logo and Sudoku-only pilot. Implementation commit fb9e4afd91d6aa58fac642d932e9f5620c7c60af bundles exact GridMates asset, enables second stage, configures both checksum gates and Visual3 isolated debug build, and updates B3-01 roadmap. See docs/BETA3_STARTUP_PILOT.md for source hashes, timings, scaling, skip behavior and limitations. Fresh CI and physical feedback pending. No rollout to other games or public release; existing production package/signing/saves unchanged. Next verify Visual3, obtain owner feedback and refine before documenting reusable central implementation. Continue existing Beta3 gameplay roadmap.


## Latest checkpoint — Visual Review 2 implementation

Visual1 physical screenshots show fox launcher and default/Ocean/Garden/Farm/Woodland habitat switching, not complete acceptance. Visual2 source 8be7f32606297e9934f42f16d05351af6383b800 adds exact Spudacus startup overlay (780ms nominal, silent, tap/Back skip, disabled-animation skip, stop/recreation skip), optional disabled GridMates stage, stronger non-Woodland caption gradient and 58dp adaptive foreground (previously 54dp). Isolated package com.playereden.sudokutogether.qa.beta3visual2 uses temporary debug signer; public Beta2 untouched. CI run 45546351053 observed checksum and icon generation passed, compilation pending; newer ordinary quality gate 45546362833 includes stage ordering tests at 63467c670d8867688faa3cc4c9bb188ed277f8fb. Earlier independent Spudacus checksum and baseline gate 45545507332 passed. Next inspect both final runs, deliver verified Visual2 APK, physically test cold/warm launch, skip, reduced motion, orientation, habitat readability and masks. GridMates art pending; Woodland wrapping and native launcher promotion still open. No public release or save changes. Historical checkpoints below.


## Latest checkpoint — Visual Review 1 passed; shared branding adoption

Visual Review 1 run 45543876470 PASSED (source 6fb83504a13534e046ea9af96e835c7cf1125656); artifact 14990771979, https://github.com/EdenP-Development/SudokuTogether/actions/runs/45543876470/artifacts/14990771979 ; standard Android gate 45543853735 PASSED. Isolated visual preview only; physical acceptance pending. Owner approved adding shared Spudacus v1.0 startup sequence to B3-01, roadmap commit f7e55e3255a696156dd9ccf6f7cc13bd567d725e. Exact source blob 7f64339220f851c7e36ae1b0c0662892c0ed1694 copied to app/src/main/res/drawable-nodpi/spudacus_v1_full_transparent.png in 33a6c972663ab30618692fc8b1d100e33673db0b. CI SHA-256 gate committed a3133a4bf014dc171c0502515e614fdb3031e35f; fresh result pending. Startup behavior NOT implemented yet. Next implement lifecycle-safe silent cream splash, tap skip, reduced motion, no forced readiness delay, disabled GridMates stage pending approved art. Keep Visual1 unchanged for owner review; next separate preview after splash tests. No public release or save/signing changes.


## Current checkpoint — Beta 3 Visual Review 1 (2026-11-24 local)

Resume on `feat/beta3-foundation`. Branding transfer commit 61c27feb7eeae9d552c9abc59579a2bebaa9f0a7 contains 14 raw archive parts. Independent verification [45543787474](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45543787474) PASSED: archive checksum, six manifest hashes, actual image formats, dimensions and decode. Wide cover and social image are WebP with historical PNG filenames; verified artifact exports correct extensions without altering bytes. Preserve original transport until native archive/import is durable. Habitat quality gate 45531206695 PASSED (not physical visual acceptance). Visual preview workflow source 6fb83504a13534e046ea9af96e835c7cf1125656; [run 45543876470](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45543876470) observed icon generation successful, Android compilation in progress. Separate ordinary quality gate 45543853735 in progress. Preview only uses generated adaptive fox foreground, retained monochrome glyph, temporary debug signer and isolated package com.playereden.sudokutogether.qa.beta3visual1. No public APK, signing or save changes. Next: inspect final CI status and artifact receipt, fix failures if any, deliver isolated review APK and mask samples, obtain owner crop/habitat feedback. Native source integration and public release remain pending. Older checkpoints below are historical.


## Latest checkpoint — habitat integration

2026-11-24 — B3-005: four original habitat PNG blobs accepted via individual Google Drive transfer. Both Ocean candidates retained in design; second (treasure chest) provisionally selected for review. Garden and Farm integrated, Woodland/default unchanged. Runtime references same original blobs, no compression yet. Darker left overlay for new art; actual crop/readability and independent pre-transfer SHA-256 verification pending. New CI pending. Large 8.32 MB branding ZIP failed GitHub request; icon still pending. No production changes. Exact successful connector sequence: upload_file(file_uri=original conversation file ID), get_file_metadata, fetch(url,download_raw_file=true,include_base64=true), use result.b64_string directly in create_blob(encoding=base64), create tree/commit/CAS update. Do not encode twice or print bytes.

Next: verify CI, transfer approved icon individually, validate adaptive mask, review actual banner crops and prepare isolated visual QA APK. Older handoffs historical.

## Active handoff — 2026-11-24: habitat artwork review

Resume on feat/beta3-foundation. B3-001 and B3-003/004 implemented; unsigned quality gates 45519754181 and 45527791591 completed successfully (core/Android JVM tests, instrumentation compilation, lint, debug assembly; not emulator execution). B3-002 fox launcher artwork and B3-005 new environments generated, but image binaries not committed or integrated. Existing Woodland scene remains unchanged. Owner responded positively to the habitat set. Ocean has two generated candidates (reef/shipwreck and reef/shipwreck/treasure chest); choose final after comparing actual banner crop, do not silently assume a selection. Garden: sunlit cottage garden with rose arch, flowers and path. Farm: red barn, windmill, fence and golden fields. Small environmental creatures are decorative, not equipped companions; verify composition and readability with overlays. Preserve exact masters, no text baked in, left text space and right companion space. Next: resolve binary transfer/archive, create runtime exports, test crops, map resources with safe fallback, integrate approved launcher with adaptive mask checks, then produce isolated visual QA build. If binary transfer blocked, continue targeted gameplay layout work rather than regenerate approved artwork. No production APK change. Physical multiplayer evidence and outstanding release housekeeping remain in foundation audit. Older handoffs below are historical.


## Active Beta 3 checkpoint — 2026-11-24

Resume on `feat/beta3-foundation`. B3-00 ACTIVE, initial static audit in docs/BETA3_FOUNDATION_AUDIT.md, roadmap and progress in docs/BETA3_ROADMAP.md and docs/BETA3_PROGRESS.md. No new runtime edits or tests. Finish focused audit and baseline QA; then B3-01 typed backup feedback and approved icon. Older branch instructions below are historical. Keep published Beta2 unchanged.


## Active Beta 3 roadmap — 2026-11-24

Owner approved the Beta 3 direction and requested a living roadmap. Read [Beta 3 roadmap](docs/BETA3_ROADMAP.md) and [progress checkpoint](docs/BETA3_PROGRESS.md) first. **Next: B3-00 foundation audit**, not implementation yet. Current branch feat/beta2-permanent-identity-staging; preserve accepted Beta 2 source and signing identity. Establish reviewed Beta 3 branch after inspecting main/PR state. Roadmap status and acceptance evidence must be maintained across chats. Existing branding/release records below remain historical evidence. No fresh audit, build, CI or runtime changes in this planning checkpoint.


## Final branding handoff — 2026-11-24

Beta 2 is owner-reported published. Storefront, devlog and evergreen promo designs completed in conversation. Owner selected the first text-free fox/grid icon (plain forest background) for itch favicon and the second (leaf accents) for future Beta 3 Android launcher icon. Retain full fox emblem with title for promotional branding. Do not change the accepted Beta 2 APK. Actual upload of all optional metadata images not individually verified. Android adaptive icon masks, small-size readability and launcher implementation are pending Beta 3. Keep project name and signing identity unchanged.

Evergreen social image: phone/woodland composition, no version/counts; UI is an illustrated approximation, not evidence. Wide cover: woodland group with core features, 21:9. Logo: horizontal fox/grid wordmark, transparent PNG intended; verify alpha and contrast on light/dark backgrounds. Favicon: square plain-background fox/grid. Devlog cover only: Beta 2, 85 cosmetics, 47 achievements. Do not regenerate approved designs simply because a chat changes. See docs/ITCH_PROMO_ASSETS.md and shared hub ITCH_PUBLISHING_GUIDE.md. Actual image binaries must be archived separately; this checkpoint documents selections, not an assertion that binaries are in Git.

NEXT: Beta 3 planning and implementation; optional public download verification, durable APK/art archive and reviewed source promotion remain housekeeping. No new Beta 2 build or storefront redesign required. Public listing and devlog URLs in preceding checkpoint. Keep spouse migration and physical multiplayer deferrals recorded. 


## Current handoff — November 24, 2026: Beta 2 published (owner reported)

Owner uploaded GridMates-Sudoku-Beta2.apk, hid but retained legacy Beta 1, saved evergreen description/install instructions, replaced cover, added promotional panels, and published the personal short devlog with AI-assisted programming/art disclosure. Public URLs: https://sir-spudacus.itch.io/gridmates-sudoku and https://sir-spudacus.itch.io/gridmates-sudoku/devlog/1804670/gridmates-sudoku-beta-2-growing-our-little-puzzle-game . Recheck on Nov 24 returned cached Beta 1 listing and devlog cache miss; public bytes/layout still not independently verified. Owner publication is not a pending permission request. Candidate code 31, version 1.0.0-beta.2, source c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c, CI 45487842212. No rebuild necessary. Keep exact accepted APK and key backup; durable release archive and source promotion still pending.

NEXT: optional itch Metadata > Promo images: evergreen social image (suggested 1200x630), square favicon using actual app icon, 21:9 wide cover for special homepage featuring (suggested 1680x720), horizontal transparent PNG logo readable on light and dark backgrounds. No Beta numbers in these. Devlog art is version-specific. Use actual cropped screenshots for factual UI (AI-rendered recreations are promotional illustrations, not raw evidence). Owner prefers one task at a time. No changes to itch performed by assistant. After optional promo polish, resume Beta 3: advanced Coach, cohesive art and new collections, personalized habitat polish, success feedback styling, accessibility, game feel, and multiplayer feedback. No premature Google Play work.

Canonical shared hub: https://github.com/EdenP-Development/Eden-Development-Standards . Active branch remains feat/beta2-permanent-identity-staging until reviewed source promotion. Historical checkpoints below superseded where inconsistent.


## Beta 2 publication — 2026-11-24 (owner reported)

Owner reports upload of clean GridMates-Sudoku-Beta2.apk, evergreen listing and installation instructions, new evergreen woodland cover, promotional panels and published Beta 2 devlog. Upload editor screenshots show success and Android selected; legacy Beta 1 retained but hidden. Public project: https://sir-spudacus.itch.io/gridmates-sudoku . Devlog: https://sir-spudacus.itch.io/gridmates-sudoku/devlog/1804670/gridmates-sudoku-beta-2-growing-our-little-puzzle-game . Public web retrieval still returns cached Beta 1 page and devlog fetch unavailable; actual public download checksum, current listing layout and devlog rendering not independently verified. No claim of fresh public verification. Accepted build remains source c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c, CI 45487842212, package com.playereden.gridmates.sudoku.beta, code 31. Owner reported same-version reinstall and retained progress. Keep original backups and migration instructions. Next: verify live download, durable archive, reviewed source promotion, then Beta 3. Promotional illustrations are AI-assisted; use actual cropped original screenshots for factual UI evidence, not generated recreations. No private data committed.


## Release preparation checkpoint — 2026-11-24

Accepted candidate remains build 31 from c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c. No gameplay rebuild requested. Owner reports same-version reinstall with retained progress; sufficient physical acceptance for beta preparation, not higher-version upgrade evidence. Final public announcement and migration guide in docs/BETA2_ITCH_RELEASE_DRAFT.md. Publication and durable archive still pending; no direct release-asset or itch publishing capability confirmed. Do not claim published. Keep legacy download and player backups. Next: owner publication decision/upload and record actual public release URL, then promote reviewed source and resume Beta 3. Independent owner key backup remains unconfirmed.


## Owner acceptance checkpoint — 2026-11-24

Owner reports signed Beta 2 APK installed over existing signed Beta 2 without uninstall and existing progress retained. This is physical same-version reinstall evidence, not a higher-version upgrade or independent cold-restart check. Earlier positive visual review and portable migration evidence remain valid. Beta 2 publication pending explicit approval; public itch.io remains Beta 1. Preserve legacy app and backup; do not regenerate signing key. Next: archive candidate and prepare publication. No private player data recorded.


## Verified downloadable candidate — November 24, 2026

Downloaded actual signed artifact 14962779795 from run 45487842212. Archive SHA-256 matched GitHub metadata; archive and APK CRC checks passed. APK SHA-256 `28f9fb4888f6ee32e960d22152b84aeed4daef55e9ec0230cb53725daa50b89c`, 29602050 bytes, matches build receipt and checksum file. All six packaged artwork byte hashes independently matched. Local Android signing tools unavailable; signer/package checks rely on successful CI. Candidate remains unpublished; owner positive visual review recorded, same-identity update test and final publication acceptance pending. GitHub Actions archive expires Dec 8; save local copy. No release-asset upload action available in connector. Do not confuse candidate with public release.


## Latest release checkpoint — 2026-11-24 (signed Beta 2 candidate)

**Resume on `feat/beta2-permanent-identity-staging`.** Public itch.io remains Beta 1; no publication yet. Signed candidate source c59bdf6860f7ff3ea0e01e28fe63643a7ac5342c, [CI run 45487842212](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45487842212), job 138777865597: all steps passed, including approved art gate, dedicated signer, core tests/release lint/assembly, package/version/certificate/permission checks and exact six artwork hashes resolved through the compiled Android resource table. [Candidate ZIP](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45487842212/artifacts/14962779795), archive size 29144606 bytes, SHA-256 0dc6b20ac75db841f5b2fd407e4991111764301d6f9f28faead74c01afd9b432, expires 2026-12-08T21:12:14Z. This is archive metadata, not APK hash; independent downloaded-byte verification pending. Receipt inside artifact.

- New beta package com.playereden.gridmates.sudoku.beta, code 31, version 1.0.0-beta.2, certificate SHA-256 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d. Organization secrets verified; owner independent backup not confirmed. Do not regenerate signer. Legacy public package remains separate.
- Owner's signed-candidate phone screenshots show launch/home, intended dynamic equipped companion (explicitly liked and retained), approved Co-op Fox/Owl and Versus Bear/Rabbit, profile counts matching earlier restore, 47-achievement catalog and detailed assisted/unassisted statistics. Positive visual feedback; not a complete signed-candidate migration, cold restart, puzzle or two-phone test. Wife and additional two-phone tests deferred by owner. No personal screenshots or save contents committed.
- Scope frozen at 85 cosmetics / 47 achievements. Earlier 113 Android regression checks and 750-record portable rehearsal remain recorded below. Beta 3 backlog: success banner semantics, profile name wrapping, audit Farm companion wording and explore themed habitats, advanced Coach, collections and visual polish. No extra Beta 2 features.
- NEXT: independently verify candidate receipt/APK if accessible, confirm signed-candidate acceptance and same-beta-identity update continuity, prepare durable release archive and public migration instructions (keep legacy installed), obtain explicit publication approval and publish to itch.io. No automatic publication. Shared hub canonical URL https://github.com/EdenP-Development/Eden-Development-Standards. Owner directs, assistant handles technical work. Keep beta processes proportionate.

Earlier checkpoints retained below; this section supersedes conflicting status.


## Latest authoritative release checkpoint — 2026-11-24

Repository transferred to EdenP-Development/SudokuTogether; organization installation and read/write access verified. **Resume on `feat/beta2-permanent-identity-staging`**, not main or the older integrated review PR #126. Latest verified staging source d490fc32d7c4a381b07b4d542b6bd685e1824813. Public itch.io remains Beta 1. No signed Beta 2 APK published.

- Game scope frozen: 85 cosmetics, 47 achievements; 113 Android regression tests passed at ba620d247d10e9d4cbe7ecaeb0a4dc6819a69ff0. Owner physical Beta1 QA -> Beta2 reviewer backup restore matched visible counts and equipped portrait. 750-record automated portable migration passed; temporary signer install-over passed. Wife's individual and two-phone tests deferred by owner; not passed. Actual production migration/restart/full save comparison not yet verified.
- New itch.io beta package `com.playereden.gridmates.sudoku.beta`, versionCode 31, versionName 1.0.0-beta.2. Legacy public package/signature preserved, original key unresolved. Proposed future Play package com.playereden.gridmates.sudoku; no promised beta-to-Play progress migration. Existing QA unchanged.
- Dedicated beta signer SHA-256 `9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d`; alias gridmates-sudoku-beta. Organization secrets GRIDMATES_SUDOKU_BETA_KEYSTORE_BASE64 and GRIDMATES_SUDOKU_BETA_KEYSTORE_PASSWORD restricted to this repository per owner setup. [Signing preflight PASSED](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45479430913): matching cert and private-key proof. [Unsigned staging PASSED](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45479430644): build/lint/package/permissions. Owner independent backup still unconfirmed; secrets are not recoverable backup.
- NEXT: implement dedicated fail-closed signed beta build (do not use legacy secrets), promote exact owner-approved artwork and verify art guard, verify signed APK package/version/cert/digest/permissions/non-debug status, test launch/restore/restart and future update continuity, obtain owner release acceptance, publish with one-time migration instructions. Never uninstall legacy before verifying transfer. See docs/BETA2_ITCH_RELEASE_DRAFT.md. No automatic publication. Minor notification/profile polish, expanded collections and advanced Coach deferred to Beta 3.
- Owner is design director; assistant handles engineering. Keep beta processes proportionate. Other projects must audit existing signers before generating anything; no reuse of Sudoku key across projects.
- Prior reviewer APK digest claims in older comments not independently substantiated by this checkpoint; use CI evidence, verify actual bytes before asserting exact APK hash. No private save data or credentials in GitHub.

Historical checkpoints below are retained for context, superseded by this section where inconsistent.


## Current Beta 2 checkpoint — November 24, 2026

**This section supersedes all historical checkpoints below.** Active draft [PR #126](https://github.com/PlayerEden/SudokuTogether/pull/126), branch `feat/beta2-integrated-rc3-review`. Last tested game source `ba620d247d10e9d4cbe7ecaeb0a4dc6819a69ff0`; subsequent test/documentation-only integration does not alter game runtime. Public itch.io remains Beta 1; Beta 2 is not published.

- 85 cosmetics (43 portraits, 12 Basic insignias, 11 boards, 10 inks, 9 fonts), 47 achievements. Approved Versus V9, Co-op V5, Solo/Home retained, five illustrated replacement portraits and Ocean art frozen. No new Beta 2 content.
- [Android regression](https://github.com/PlayerEden/SudokuTogether/actions/runs/45451955312) attempt 2: 113/113 passed, same source as first attempt's capture timeout. [Isolated reviewer archive](https://github.com/PlayerEden/SudokuTogether/actions/runs/45451955312/artifacts/14952310625), expires Dec 8; not a production update. ZIP digest e3d9cb58ff346681368b383e359100dea61f6591946e974870c65d6dbc3300cf. Independent latest APK hash check pending.
- [Install-over rehearsal](https://github.com/PlayerEden/SudokuTogether/actions/runs/45451955470) GREEN with temporary matching test signer, exact Beta1 source -> Beta2, not actual public signing continuity.
- [Portable migration rehearsal](https://github.com/PlayerEden/SudokuTogether/actions/runs/45464118951) GREEN: test-only commit `70cf8e186d5b07961b2e0cb7a9ec4ac8bc014e7d`, original Beta1 source 76cbfc149569938b97286f3efe4ad44e08c5934f and separate Beta2 reviewer package. Two instrumentation tests passed, 750 synthetic records, Solo/Daily, notes, Undo/hints, preferences, equipped cosmetics, retroactive medals (solo-750 earned, solo-1000 locked), destination identity and reopened save verified; old profile/settings byte-identical. Actual family save and published APK not tested. Tests integrated below scripts/portable-migration-fixtures and workflow; use manual workflow dispatch or dedicated staging branch. No production key generated.
- Owner deferred physical two-phone retest to public feedback/Beta3; not a passed check. Focused multiplayer source audit found unchanged HostServer, GuestClient, Protocol, SessionStore, RoomDiscovery, TransportTest; GameSession emoji fix and ViewModel round/input/error refinements. Trusted local Wi-Fi, unencrypted transport. Critical data-loss/security/broken functionality remains release-blocking.
- **BLOCKED: original production signing custody.** Package com.playereden.sudokutogether, Beta1 code30, intended Beta2 code31, cert SHA256 d362dab018a255ccb313582df1e3cf836246e7da1778a198eb1da03a7cfe7cd1. Last preflight missing original secrets. Never generate substitute for existing package; no uninstall/clear data. Original APK exposes public certificate only. Private original backup location unknown.
- Shared [Eden Development Standards](https://github.com/PlayerEden/Eden-Development-Standards): release, signing, backup, save and recovery rules and APP_REGISTRY.json. Owner-controlled encrypted vault and independent recovery copy still unverified. Keep keys/passwords out of Git, logs and chat.
- Preferred release: recover original key, certificate preflight, owner-authorized release art flag, inspect actual production APK and rehearse actual public install-over on backed-up noncritical data. Fallback only with owner approval: distinct permanent package and securely backed-up new signer, export/restore using Settings -> Progress, preserve original app until actual migration verified. Existing live room credentials not portable; destination player ID retained. No new identity or public release authorized.
- Next: check signing backup custody, verify latest reviewer APK receipt/hash, finish release variant permissions/art/ancestry checks and approved support/privacy/disclosure copy. Update current handoff and shared registry with evidence; no secrets. Beta3 advanced Coach, longer-term collectibles, further art and multiplayer feedback. See [release tracker #115](https://github.com/PlayerEden/SudokuTogether/issues/115) and [audit #127](https://github.com/PlayerEden/SudokuTogether/issues/127).

---



## Current canonical source — Woodland V5.1 (November 19, 2026)

**Visual source has been consolidated into `main`** through [merged PR #92](https://github.com/PlayerEden/SudokuTogether/pull/92). Home/Solo remains the HD woodland with dynamically selected Collection companion; Co-op and Versus now have distinctive 1920×819 illustrated backgrounds. The Versus art was revised to eliminate the distorted checkered flag. V5.1 emulator review [#44633405848](https://github.com/PlayerEden/SudokuTogether/actions/runs/44633405848) and the direct `main` integration gate [#44633601301](https://github.com/PlayerEden/SudokuTogether/actions/runs/44633601301) passed; 13/13 Android 16 connected UI tests passed. The separate-install V5.1 preview APK is available in the emulator workflow artifact; it **does not update** public Beta 1 or share saved data.

**One canonical baseline:** [Woodland V5.1 status, asset inventory and release gates](docs/WOODLAND_V51_CURRENT_BASELINE.md). Stacked V3/V4/V5 design PRs #89/#90/#91 are superseded. Older sections below document *historical* checkpoints, not the current default branch state.

**Release remains separately blocked:** Public itch.io is still Beta 1; production Beta 2 install-over requires recovering the original signing identity and proving save preservation. Two-real-phone LAN gameplay is still awaiting final hands-on verification. Do not uninstall an existing game, use a different signer for its update, or publish the V5.1 reviewer APK as a public Beta 2 update.

## Newer Home gamefront visual-review checkpoint (Nov 17 ET / Nov 18 UTC)

**Owner feedback:** The public-facing Home menu feels too much like a settings/utility page; the Daily Puzzle card is a visual strength to preserve. Beta 2 phone screenshots also revealed a genuine Profile achievement placeholder/title layout defect. [PR #75](https://github.com/PlayerEden/SudokuTogether/pull/75) fixed that defect on `main` with Android CI passing. [PR #77](https://github.com/PlayerEden/SudokuTogether/pull/77) documents the full owner screenshot review and prioritized issues.

**Home design is in a DRAFT PR, not merged:** [PR #76](https://github.com/PlayerEden/SudokuTogether/pull/76) introduces a distinct illustrated gamefront banner, larger Solo/Co-op/Versus tiles and warmer main play card. It preserves the Daily card and game/save/network behaviors. The normal app compile/unit/lint quality gate passed. Visual approval remains with the owner.

**Latest verified standalone Home Review APK:** [Android API36 run 44225639203](https://github.com/PlayerEden/SudokuTogether/actions/runs/44225639203) **PASSED**, including exact package/version/certificate/checksum validation, actual install/launch, and **2/2 Home-gamefront Android UI tests** for mode navigation, Daily access, narrow screens and 200% text. [Download ZIP artifact 14392109116](https://github.com/PlayerEden/SudokuTogether/actions/runs/44225639203/artifacts/14392109116), expiring Dec 2, 2026 UTC; extract `GridMates-Sudoku-HomeGamefront-OwnerPreview.apk`. Application ID `com.playereden.sudokutogether.qa.homegamefront`, launcher **Sudoku Home Review**, source `45e82b984f3147d969da2b88e914021bb9721324`. This previews PR #76 **plus** the already-merged Profile text repair on its isolated branch, without updating existing public/QA installations. No public Beta 2 APK has been produced.

**Next decision:** Review the new Home screen physically and decide if it feels sufficiently like a polished casual puzzle game. The emulator screenshot shows a cleaner focal point, but the Daily card approaches the lower navigation edge; don't treat emulator success as aesthetic approval. Keep PR #76 draft until owner feedback. Following visual approval, handle Hint-dialog spacing, Solo keypad alignment and long Collection lists in separate scoped changes. **Do not bypass the original production signing key or uninstall a saved app** to publish Beta 2.


## Latest verified owner-preview and release checkpoint (Nov 17 ET / Nov 18 UTC)

**Public release:** The real itch.io public game is **still Beta 1**. Do not uninstall or clear existing installs. A compatible Beta 2 install-over remains **blocked** because the original Beta 1 production keystore/secrets were unavailable to the verified GitHub Actions signer preflight. Do not generate a new key for the same-package update or ask for the private keystore in chat.

**Source/release quality:** `main` includes [PR #71](https://github.com/PlayerEden/SudokuTogether/pull/71) (explicit opt-in before uploading publicly accessible candidate artifacts), [PR #72](https://github.com/PlayerEden/SudokuTogether/pull/72) (truthful current README and unpublished Beta 2 devlog), and [PR #73](https://github.com/PlayerEden/SudokuTogether/pull/73) (fix release metadata verification: anchor `package: name=` instead of greedy `name=` and avoid `aapt | head` with pipefail). No public APK was built or uploaded by these PRs.

**Latest isolated Beta 2 owner review — VERIFIED, not a production update:** [Successful workflow run 44219560802](https://github.com/PlayerEden/SudokuTogether/actions/runs/44219560802), [artifact 14390417112](https://github.com/PlayerEden/SudokuTogether/actions/runs/44219560802/artifacts/14390417112), 14-day retention through Dec 2, 2026 UTC. Exact preview branch source [`91c3776`](https://github.com/PlayerEden/SudokuTogether/commit/91c3776bdca61578bb1075e498e0a7a715ba2d66), based on Beta 2 gameplay source `90382797543faaa9c2711b543d67a7cf04b48603` with **only branch-local** launcher and debug package isolation changes. The artifact contains `GridMates-Sudoku-Beta2-Isolated-OwnerPreview.apk`, checksum, identity receipt, and emulator screenshot. Package `com.playereden.sudokutogether.qa.beta2owner`, launcher **Sudoku B2 Review**, versionCode 31. Core/Android unit and lint build, APK package/version/signature/SHA-256 inspection, and API 36 emulator **actual install and launch** all PASSED. [Owner-preview scope and safe testing](https://github.com/PlayerEden/SudokuTogether/blob/preview/beta2-source31-owner-phone/docs/BETA2_ISOLATED_OWNER_PHONE_PREVIEW.md). The initial runs 44218281893 and 44218867247 failed packaging metadata checks and **should not be downloaded for phone testing**.

**Next gates:** Owner may visually test the isolated build *side by side* without touching spouse's existing game. Recover the **original** production signing backup privately, rerun source-pinned certificate preflight, deliberately build a signed production candidate, and test an actual published Beta 1 → signed Beta 2 non-destructive update on backed-up, noncritical progress. Only then finish real screenshots, publisher contact, privacy policy URL, itch.io distribution. Formal renewed two-phone LAN testing remains owner-deferred.


## Current Beta 2 source-readiness checkpoint — November 17, 2026

**Latest overall audit:** [November 17 source/UX/LAN/save/release readiness and prioritized QA roadmap](docs/SUDOKU_OVERALL_AUDIT_2026-11-17.md). Findings distinguish code/test evidence from still-pending actual-phone and release gates. Audit publication modified **documentation only**, not APK source, game mechanics, or signing.

**Canonical source:** `main` through merged [PR #69](https://github.com/PlayerEden/SudokuTogether/pull/69), `9d62d2c61e321755acc846f4bf37ff0b2a1cc6b0`. [PR #67](https://github.com/PlayerEden/SudokuTogether/pull/67) added permanent earned-cosmetic ownership and backward-compatible save/backup migration; [PR #68](https://github.com/PlayerEden/SudokuTogether/pull/68) added repeatable manual Beta 1 → Beta 2 **debug-signer** install-over rehearsal. PR #69 adds consumer-facing About/Progress text, earned-cosmetic counts before backup restore, and hides the "Share game installer" option on debug/QA builds or split-install bundles so test APKs aren't distributed as public games. All **50 existing unlock requirements, puzzle rules, saved profile schema, local multiplayer protocol and release package identity are unchanged**. Source default is versionCode **31**, `1.0.0-beta.2`. The currently distributed public itch.io APK remains Beta 1; there is **no production-signed Beta 2 update yet**.

**Latest production release gate (Nov 17 ET):** [PR #70](https://github.com/PlayerEden/SudokuTogether/pull/70) added the source-pinned original-certificate preflight; [PR #71](https://github.com/PlayerEden/SudokuTogether/pull/71) proposes making candidate builds manual opt-in and correctly labeling their GitHub artifacts as publicly readable. In this public repository, Actions artifacts are downloadable by readers, so an uploaded candidate is not private. Its [first live main-branch run 44214878784](https://github.com/PlayerEden/SudokuTogether/actions/runs/44214878784) **BLOCKED safely** because one or both required GitHub Actions secrets, `SUDOKU_KEYSTORE_BASE64` and `SUDOKU_KEYSTORE_PASSWORD`, were unavailable. The candidate-build job was **skipped**, so no production APK was built. The actual original keystore is not in the source and its backup location is unknown; do not assume it is irretrievable, ask for its contents, create a new key, or tell users to uninstall. Authorized key custodian needs to restore the **original** backup in GitHub repository Actions secrets, then manually re-run the workflow. [Current release-key gate and recovery notes](docs/BETA2_PRODUCTION_SIGNING_GATE.md).\n\n**Latest Android evidence:** [PR #69 permanent Android compile/unit/lint/debug-assembly run 44212095728](https://github.com/PlayerEden/SudokuTogether/actions/runs/44212095728) **passed**. [Focused API 36 Android run 44212095818](https://github.com/PlayerEden/SudokuTogether/actions/runs/44212095818) passed **5/5 PublicReleaseCopyUiTest** and **2/2 existing ProgressBackupSettingsUiTest**. [Beta 1 → Beta 2 same-test-signer upgrade runs 44207050207](https://github.com/PlayerEden/SudokuTogether/actions/runs/44207050207) and [44208331788](https://github.com/PlayerEden/SudokuTogether/actions/runs/44208331788) passed original-source save seeding, versionCode 30 → 31 `adb install -r`, byte-identical installed profile and owner cosmetic migration. [Full release-facing QA](docs/BETA2_PLAYER_FACING_RELEASE_QA.md); [upgrade receipt](docs/BETA2_ACTUAL_APK_INSTALL_OVER_QA.md). **No emulator check establishes the original public signing key is available, or verifies a live family install-over update.**

**Latest delivered isolated APK — Beta 1.2 Results Review (Nov 17):** [Passing build 44194810113](https://github.com/PlayerEden/SudokuTogether/actions/runs/44194810113), [artifact 14378423120](https://github.com/PlayerEden/SudokuTogether/actions/runs/44194810113/artifacts/14378423120), retained through December 1, 2026. APK **GridMates-Sudoku-Beta12-Results-Review.apk**; exact source `564a203e3e0a96e070d8555b354ba57b9df2026d` (through PR #66), workflow revision `4e289dc412b66d48a8b788f2c234bb5a8b334c4c`. Package `com.playereden.sudokutogether.qa.beta12results`, launcher **Sudoku B1.2 Results**, size **14,486,370 bytes**, SHA-256 `ba90717cf8410d8d981e70249c15e14100cdefc17e710c9aee98c259ec73bc3f`, min SDK 26, isolated temporary signer `bdeed691439e680138d3659875100aa52a881bdf03aae9aa9587bd3417454314`. Core/Android units, lint, APK identity, signature, hash and emulator API36 install/launch all passed. ZIP receipt checked against exact extracted APK. This app installs **alongside** and does not update previous isolated `Sudoku B1.2 Review`, production or QA apps; each has separate saves. Physical owner approval still pending.

**Next focus — owner decision:** recover the **original** Beta 1 production signing backup and restore its two GitHub Actions secrets privately; the live certificate gate currently blocks and no private Beta 2 APK exists yet. Once an exact signer match passes, build the source-identified Beta 2 private candidate and rehearse **actual public Beta 1 APK** → production-signed Beta 2 install-over on backed-up noncritical progress. **Never uninstall or wipe the wife's game.** Meanwhile finish itch.io copy, screenshots and the approved support/privacy URL; [listing draft](docs/GRIDMATES_ITCH_BETA2_LISTING_DRAFT.md). Two-phone formal LAN retesting remains owner-deferred; existing LAN play is reported working. New Collection content/thresholds are lower priority than preservation and release-readiness.

**Priority:** prepare a practical, approachable public itch.io **Beta 2 update** without adding feature bloat or blocking on unrequested two-phone retesting. Hold Google Play publisher/privacy declarations until a real public page exists. User wants a few prestigious rare unlocks among accessible long-term themes; see [balanced 12-item pack proposal](docs/GRIDMATES_ITCH_PUBLIC_RELEASE_AND_REWARDS_PLAN_2026-11-17.md). Read newest `main` and release receipts before relying on older v0.11/Beta 1 documentation.

**Current starting point:** The **v1 Beta 1 Sudoku application source is now on `main`** after [PR #42's merge](https://github.com/PlayerEden/SudokuTogether/pull/42) on November 16, 2026. The individual v0.11E–M draft PRs were superseded, with their milestone commits preserved. New Sudoku bug fixes should branch from **`main`**, not the older stacked branches. The signed itch.io production APK was built from **source `76cbfc1`**, not from the documentation-only follow-up commits, so a fresh binary/CI check is a separate task. The owner confirms installing/launching the public itch APK and QA build on a real phone, while checksum and larger device/multiplayer verification remain open in [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43).

**Beta 1.2 refinement starting point (Nov 17, 2026):** [Parchment + Graphite PR #53](https://github.com/PlayerEden/SudokuTogether/pull/53) and [Ocean + Sepia/Indigo PR #54](https://github.com/PlayerEden/SudokuTogether/pull/54) are merged **in source only**; current catalog is 22 portraits, 6 emblems, 7 boards, 10 inks and 5 fonts. The existing public APK is unchanged. [Beta 1.2 refinement execution plan](docs/BETA12_REFINEMENT_EXECUTION_PLAN.md) prioritizes observable interface/gameplay polish, stable local saves, full QA and a future isolated phone preview **before any new major features**. Phase A Collection navigation [PR #55](https://github.com/PlayerEden/SudokuTogether/pull/55) and the initial Phase B Notes-accessibility [PR #56](https://github.com/PlayerEden/SudokuTogether/pull/56) are merged with focused Android emulator QA. Remaining gameplay polish and on-phone review are tracked in [the Phase B audit](docs/BETA12_PUZZLE_INTERACTION_AUDIT.md). Do not mistake source-only tests for on-device approval or a compatible QA update.

[Beta packages and publishing](docs/GRIDMATES_V1_BETA_RELEASE.md) · [Project health/history audit](docs/SUDOKU_REPO_HEALTH_AND_BETA_STEWARDSHIP_2026-11-16.md)

- [Current handoff](docs/PROJECT_HANDOFF.md) and [delivery plan](docs/GRIDMATES_DELIVERY_PLAN.md)
- [v0.11L Garden collection, fonts and update QA](docs/V0.11L_GARDEN_COSMETICS_QA.md)
- [v0.11K Profile, Collection and update QA](docs/V0.11K_PROFILE_COLLECTION_QA.md)
- [v0.11J focused audit and signed upgrade evidence](docs/V0.11J_FOCUSED_AUDIT_QA.md)
- [v0.11I Coach practice](docs/V0.11I_COACH_PRACTICE_QA.md)
- [v0.11H Coach depth](docs/V0.11H_COACH_DEPTH_QA.md)
- [v0.11G Coach and Ocean collection](docs/V0.11G_COACH_OCEAN_QA.md)
- [Roadmap](docs/ROADMAP_V08_TO_LAUNCH.md), [issues](https://github.com/PlayerEden/SudokuTogether/issues) and [manual QA workflows](https://github.com/PlayerEden/SudokuTogether/actions)

Open the v1 Beta 1 QA APK and tap **Update** for the current v0.11M QA install. For the public beta, use the production APK. [Progress transfer instructions](docs/GRIDMATES_V1_BETA_RELEASE.md#android-downloads)

For another APK, verify the package, original signer and increasing version, then rehearse the update on the actual previously delivered APK with progress saved. Keep signing backups outside Git. [QA update guide](docs/GRIDMATES_UPDATE_AND_SAVE_PREPARATION.md)

**Next visual QA gate (Nov 17, 2026):** [PR #58](https://github.com/PlayerEden/SudokuTogether/pull/58) improves tiny pencil mark contrast (including Coach crossed-off candidates) without modifying palette definitions. [Run 44120831973](https://github.com/PlayerEden/SudokuTogether/actions/runs/44120831973) passed Kotlin/core, Android units and 3/3 Coach emulator UI tests. Treat this as **source/emulator verification only**. Before visual approval, compare 320–430dp displays, Rose/Violet on Midnight/Circuit, Coach crossed-off marks on Classic/Linen, and all three provisional Beta 1.2 board themes. Continue to protect the original QA signer and saved game data.
