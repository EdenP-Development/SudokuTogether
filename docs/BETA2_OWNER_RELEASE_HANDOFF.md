# GridMates: Sudoku — BETA 2 OWNER HANDOFF / SINGLE SOURCE OF TRUTH

**Updated:** 2026-11-23. **Status:** development / owner phone-review; NOT yet a public Beta 2 release.

**Companion Collection art authority:** [GridMates Collection Art Bible](GRIDMATES_COLLECTION_ART_BIBLE.md). Its per-asset statuses are preliminary until actual owner approval; do not use a previous chat's blanket PASS statements as authorization to import art.\n\n**Read this document first if a ChatGPT conversation fills up.** Open the current GitHub PRs, compare head SHAs, inspect tests, and continue from the latest green source. Do not assume GitHub's default branch already contains the draft Beta 2 features. This document records confirmed work, unresolved decisions, known risks, and an actionable order. It should be amended after each accepted visual pass and prior to release.

## 1. Product and owner decisions (authoritative)

GridMates: Sudoku is a *cozy, approachable, nostalgic-puzzle-book-meets-modern-mobile-game* with local-first Solo, Daily, same-Wi-Fi Co-op and Versus. A highly engaged family player has already unlocked nearly everything in the earlier version, making **satisfying progression/collections and polished, cohesive artwork important Beta 2 goals**. No mandatory online account or cloud dependence. Cosmetic-only rewards, no forced streaks/FOMO. Ease of use matters for older and nontechnical players.

The owner delivered **23 real-phone screenshots** (10 + 10 + 3) and expressly **rejected the current Versus V7 character composition**. Do not quote the earlier assistant assessment that V7 was good. Keep the strong Solo/Home banner; Co-op picture is appealing but its “Shared board” title overlaps the characters; Versus currently looks small, stiff, disconnected and less charming. The owner wants a genuinely improved Beta 2 for their spouse, *including reward depth*, not an arbitrary freeze at 79 items.

## 2. Confirmed technical baseline and PR dependency lineage

Repository: `PlayerEden/SudokuTogether`.

- **Production / main:** canonical Woodland V5.1 art integrated by [merged #92](https://github.com/PlayerEden/SudokuTogether/pull/92). This is not the latest Beta 2.
- **Versus investigations:** [issue #97](https://github.com/PlayerEden/SudokuTogether/issues/97) documents rejected V6, technically verified but visually unapproved V7. [Draft #99](https://github.com/PlayerEden/SudokuTogether/pull/99) holds V7 original source and manifest. [Draft #108](https://github.com/PlayerEden/SudokuTogether/pull/108) imported *identical* V7 image into the full catalog review. **Do not merge V7 merely because Android tests passed.**
- **Beta 2 collection foundation:** [draft #110](https://github.com/PlayerEden/SudokuTogether/pull/110), head `77b0c48cc685101e1aced284bb6e512245101144`: 79 cosmetics, chapter grouping, 15 additional challenge unlock routes, permanent cosmetic ownership, four source-preserving Woodland render-scale fixes, Android 16 **22/22** UI passing [run #45079974357](https://github.com/PlayerEden/SudokuTogether/actions/runs/45079974357).
- **Veteran reward expansion:** [draft #116](https://github.com/PlayerEden/SudokuTogether/pull/116), head when this plan began `d11f040b973a2930a21b48547624d33888523465`, stacked on #110: **85 cosmetics** = 79 previous + 6 new equippable Basic insignias (43 portraits unchanged); **31 achievements** from 9, six longer-term completion/challenge routes; local durable earned-medal ledger, backup/restore support; closer goals and earned album. [Android 16 run #45258494183](https://github.com/PlayerEden/SudokuTogether/actions/runs/45258494183) succeeded with **24/24** UI tests and APK package checks. Its separate package `com.playereden.sudokutogether.qa.beta2veteran` intentionally has empty, isolated progress. The owner tested this build on a physical phone.
- **Current staged UI polish:** branch `feat/beta2-owner-visual-polish-01`, created from #116 head above. Keep it stacked and draft; no production signing, puzzle engine, LAN protocol or save modifications planned here.
- **Art candidate work:** [issue #112](https://github.com/PlayerEden/SudokuTogether/issues/112), [style guide draft #113](https://github.com/PlayerEden/SudokuTogether/pull/113), and [standalone Android ZIP reviewer draft #114](https://github.com/PlayerEden/SudokuTogether/pull/114). Seven proposed transparent **Farm Goat/Duck/Barn; Garden Snail/Daisy/Mushroom; Ocean Sea Star** characters are *unapproved review art*. They have not been imported into the 85-cosmetic playable app. If all seven approved, the prospective catalog would become **92 cosmetics / 50 portraits**, not today's 85/43. Do not fabricate their presence in GitHub or add candidate drawings blindly.
- **Future Daily-specific rewards:** [idea bank #111](https://github.com/PlayerEden/SudokuTogether/issues/111). Distinct Daily days already contribute to some alternative cosmetic goals; an independent Daily Journal currency/stamp/gift system is a *separate product decision*, not currently implemented in #116.

Related issue tracking: [Beta 2 release gates #115](https://github.com/PlayerEden/SudokuTogether/issues/115); [23-screenshot visual audit #117](https://github.com/PlayerEden/SudokuTogether/issues/117). Earlier freeze wording on #115 was amended by owner: **extra reward depth IS part of Beta 2**.

## 3. Source-backed mode-art architecture and decision

`app/src/main/java/com/playereden/sudokutogether/HomeChapterArt.kt` (verified in #116) uses `gm_woodland_storybook_hd_v4` as forest environment and **separate canonical** `woodland_owl.png` and `woodland_fox.png` / equipped character sprites. Hero text is native Compose. This is the *identity anchor*.

`app/src/main/java/com/playereden/sudokutogether/NearbyModeBanner.kt` uses an original Co-op V5 high-res scene `gm_mode_coop_woodland_v5_hd.png`, with fox/owl already painted into the scene, native “Shared board” heading. It is visually appealing but heading currently crosses the foreground animal. Test a subtle contrasting text stroke/shadow **and** a safer title field; owner specifically suggested outline. Keep original image bytes unless a separately approved art revision is necessary.

Versus V7 uses `gm_mode_versus_woodland_v7_hd.png`, a single 1774×887 raster already containing **the exact canonical** Fox/Owl pixels and two smaller decorative 9×9 boards, with native “Race to finish” over dark left region. Its SHA-256 `b02fd7610576a7361326bf37f038f643bc9000322be6e0d452aeee32835231ea`, manifest `design/versus-v7/manifest.json`. Native stage height ~137dp, text region ~59% width. It avoids V6 title overlap but makes animals too small/stiff/off-model in *composition*. Merely resizing the FALLBACK Compose fox/owl overlays does **nothing** (they are disabled when V7 bitmap is active). Preserve this previous asset for rollback, but **create V8** rather than shipping unapproved V7.

**V8 acceptance:** richly illustrated approved woodland forest; large, expressive, easy-to-recognize canonical/on-model fox and owl actively solving on two separate correctly drawn 9×9 boards; no pasted floating busts or overdominant boards; no fake puzzle digits; no checkered flags; no duplicate characters; native text fully safe at normal/320dp/200%-font; phone-side-by-side vs original Solo and Co-op **owner approval**. Reference detailed [Mode Art Bible](GRIDMATES_MODE_ART_BIBLE.md). Avoid changing Solo to match broken Versus.

## 4. Owner screenshot findings (23 images; source screenshots in conversation, not committed to GitHub)

**P0/P1 required visual fixes**
1. Versus V8 mascot/composition correction and **Co-op “Shared board” legibility**.
2. Basic Collection “Constellati / on” **mid-word wrap**, other two-line label risks in 3-column portrait grid, 320dp and enlarged text.
3. Six veteran insignias currently look like unrelated glyphs (overbright Sunrise, thin Moonlight, Constellation/True North too similar, Grid Master close to Gridkeeper). Design coherent, recognizable collector-worthy vector emblems, source-verified; no arbitrary stock replacements.
4. Portrait style/size gaps: late Woodland Beaver/Chipmunk/Woodpecker/Skunk vs Bear/Owl/Fox/Raccoon; Ocean Seahorse/Clownfish saturation vs calmer octopus/turtle/seal; smaller Dragonfly etc. Compare *perceptual* size in real thumbnails/modals. Honeybee vs Firefly too similar.
5. Achievements look like an identical-trophy long checklist; humanize copy (remove “record pruning” language), show status distinctions and compact page with meaningful earned feedback. Existing 31-achievement gameplay/earned ledger are a strong baseline.

**P2 finish-worthy**
6. Portrait preview modal gives small drawn subjects in huge pale-green pane; optimize preview prominence, keep dual routes/progress text legible.
7. Number colors and fonts use “59” which reads as fifty-nine; use separable digits or 3-cell preview. Some fonts need larger specimens. Boards Midnight/Circuit/Tidal look too similar in miniature.
8. Long Collection pages repeat equipped/next-to-unlock before album; consider compact/foldable hierarchy while keeping straightforward navigation.
9. Solo board is readable and should stay large; enormous gap under board, bottom controls clustered. Preserve tap targets. Verify confirm-before-discard for “New puzzle.”
10. Home resumed Solo pushes Co-op/Versus shortcuts below fold, so visible scroll affordance matters; top-right current icon tiny in large blank tile.
11. Existing Farm 6 portraits, Garden 9 and Ocean 11 are attractive but category density varies; seven proposed new sprites may improve variety **only after art/owner review**.

**Do not redo working visuals:** original forest/cream/gold theme, Solo Hero and puzzle board, chapter-based 3-column gallery, navigation semantics, local Wi-Fi mechanics, approved old mascots, and font licenses.

## 5. Planned work packages and stop/go gates

### Pass A — low-risk screen polish (this branch / independent reviewer)
- [ ] Fix Collection label wraps and 320dp/200% test.
- [ ] Improve Co-op heading contrast / position without changing approved V5 PNG; screenshot & owner compare.
- [ ] Improve Achievements player-facing copy and compact visual hierarchy (preserve 31 IDs/earned ledger).
- [ ] Improve number previews to visually separated glyphs; maintain the underlying colors/fonts untouched.
- [ ] Run JVM/lint/Android instrumentation, new APK separate from production, ask owner for paired screenshots before advancing.

### Pass B — V8 visual-art correction (separate art branch; do NOT merge without owner signoff)
- [ ] Canonical source art reference sheets and scene-layout options, same coherent palette/linework.
- [ ] Produce V8 with correct foreground participation; preserve V7 as rollback.
- [ ] Integrate native title-safe layout with V8 new drawable; record manifest and artwork hashes.
- [ ] Real Android emulation across widths/font scales, owner physical-phone approval.
- [ ] Keep puzzle rules, LAN, save format, Co-op image unchanged.

### Pass C — Collection and reward art cohesion
- [ ] Design six coherent Basic insignias and verify visibility at 48/62/96dp.
- [ ] Audit and improve late Woodland portrait scale/composition against canonical originals; don't blindly enlarge or silently overwrite PNGs.
- [ ] Approve/revise seven Garden/Farm/Ocean candidates one by one; decide whether Beta 2 can ship them *without compromising art quality*. If all seven imported, update catalog assertions and unlock/earned-ledger tests from 85 to 92 **only then**.
- [ ] Finalize achievement and Daily reward pacing with active family player. Avoid reward inflation and loss of early-player motivation.

### Pass D — release-candidate integration, tests and spouse-safe upgrade
- [ ] Audit actual Git ancestry and dependencies; consolidate ONLY accepted PRs to one pinned source commit. Don't simply merge every draft PR.
- [ ] Build debug side-by-side APK for owner; test Solo/Daily/Collections/Co-op/Versus; check at phone size, 320dp and 200% font.
- [ ] Test two real phones on same Wi-Fi: host/join, Co-op both players, Versus both progress bars, disconnect/rejoin, finish/rematch and ability to return to Solo.
- [ ] **Back up and verify restore first.** For Beta1→Beta2 same-package install-over, use **original production signer** and versionCode progression; verify original local saved puzzle, avatar ownership, profiles and daily records all survive. Never uninstall Beta 1 or clear spouse data just to fix a QA signing mismatch. A side-by-side `.qa.*` reviewer has isolated progress by design.
- [ ] Freeze exact release APK identity/hash/signing/art/font resource audit; Android 16 and lower supported version smoke; owner approval; publish exactly one endorsed Beta 2 build. Keep rollback and backup recovery instructions.

## 6. Permanent handoff rules for future chats / Codex / Work

1. Read **this handoff**, [Mode Art Bible](GRIDMATES_MODE_ART_BIBLE.md), [issues #97](https://github.com/PlayerEden/SudokuTogether/issues/97) and [#117](https://github.com/PlayerEden/SudokuTogether/issues/117), and current PR #116 status and checks. Distinguish draft branches from released app.
2. Before work, fetch exact branch head and source; avoid relying on old conversation summaries or optimistic QA claims.
3. Keep a clear issue/PR and per-stage screenshot or test evidence; never declare artwork approved based on automation alone.
4. When owner sends screenshots, attach findings to #117; record positive references as carefully as defects.
5. Source binary illustrations should have traceable hashes and no unapproved silent substitutions. Do not invent binary uploads to GitHub.
6. Save/backup/production-signature and real-LAN gates are mandatory, never assumed from emulator testing.
7. At conversation handoff, update a dated **Current checkpoint** below with commit, PR, passing QA run, next step, and unresolved blockers.

## 7. Current checkpoint — 2026-11-23

- **Stable public Beta 1** must remain installed/untouched.
- **Most recent tested playable Beta 2:** #116's 85-cosmetic / 31-achievement standalone reviewer, [run #45258494183](https://github.com/PlayerEden/SudokuTogether/actions/runs/45258494183) **24/24 Android UI checks green**, visual issues documented from 23 owner phone screenshots.
- **Current polishing branch:** `feat/beta2-owner-visual-polish-01` stacked on #116; first-pass visual changes require their own new CI verification and APK; **NOT YET approved or public**.
- **Main art blocker:** V7 Versus rejected by owner; V8 needed.
- **Other release blockers:** Co-op text, Collection/emblem visual consistency, seven candidate sprite owner review, two-phone LAN final regression, and same-signer save-preserving Beta 1→Beta 2 upgrade.
- **Immediate next action:** implement tightly scoped low-risk UI polish + targeted tests, produce separate owner APK, solicit screenshots; in parallel prepare a **separate** approved-art V8 proposal without altering the accepted Solo forest hero.
