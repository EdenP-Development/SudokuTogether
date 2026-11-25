# Beta 3 progress — active checkpoint

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
