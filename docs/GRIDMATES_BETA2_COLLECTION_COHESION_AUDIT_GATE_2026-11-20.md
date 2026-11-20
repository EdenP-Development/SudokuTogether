# GridMates: Sudoku — Beta 2 complete Collection cohesion audit gate

**Created:** 2026-11-20. **Status:** planned final quality gate, not an assertion that any unfinished assets are approved.

## Why this audit exists
The owner requested a **full pass across ALL collections**, not just a quick check of the newest batch. A single batch can look polished on its own while looking inconsistent beside original Woodland, Ocean, Garden, Farm, boards or fonts. Run this audit when all Beta 2 items are implemented **and also run a short regression after each themed batch**. Audit the combined candidate build, not a mixture of separate reviewer APKs.

## Scope and branch reality at plan creation
- Public distribution remains **Beta 1**; no production install-over has been verified.
- Current default branch `main` has the 22 original approved 384px-class Collection portraits (12 Woodland / 6 Ocean / 4 Garden), 6 emblems, 7 boards, 10 number inks, 5 fonts = **50** cosmetics.
- Draft first-five #95 adds Cow, Chicken, Raccoon, Sea Turtle and Firefly (**55** total), draft Garden #98 adds Sunflower, Strawberry, Watering Can and Potted Tulip (**59**), draft Farm #101 adds Sheep, Pig, Pumpkin and Hay Bale (**63**, Android 16 QA green but owner visual approval pending).
- Proposed final **79** cosmetics: **43 Player Icons** (Woodland 17, Ocean 11, Garden 9, Farm 6), 6 emblems, 11 boards, 10 inks, 9 fonts. Remaining after #101: Woodland Chipmunk/Beaver/Woodpecker/Skunk, Ocean Seahorse/Dolphin/Seashell/Clownfish, 4 boards, 4 fonts.
- Design-only roster in #93 proposes unlock milestones; *do not assume design entries are real app resources* until manifests, renderer mappings and installed screenshots prove it.
- V7 Versus #99 passes Android technical QA but needs owner's physical phone layout approval; do not reuse rejected V6 artwork.

## Audit sequence (four passes, no shortcuts)

### Pass A — Asset inventory & exact identity (all 79)
- [ ] Enumerate every catalog ID exactly once with category, family, owner state, unlock kind/target and Android resource.
- [ ] Match each icon to the correct file bytes; report missing PNGs, unresolved resources, duplicate IDs, accidental aliases, placeholder/default/vector-only substitutions, redundant copies or unused sprites.
- [ ] Compare every new image to its source sheet, checksum manifest, alpha/bounds, actual in-app thumbnail. Note differences in old vs new source resolution: **do not reject original legacy PNGs just because new art exports at 512px.**
- [ ] Verify transparent alpha/no halos, centered crop, no clipped anatomy/objects, face/eye proportions, outline hue and width, highlights, shadow, color harmony, no human/humanoid cosmetics and no forcing eyes onto objects.
- [ ] Confirm all 4 families feel distinctive *and* share one `gridmates_chibi_v1` design language; near-duplicate motifs reviewed using `subject_key`, not just display names.
- [ ] Produce labeled contact sheets at native 30dp / 48dp / 72dp and on both cream/light and dark/forest surfaces. Compare old originals and all new characters on **one sheet**.
- [ ] Inspect all six emblems under same size/contrast rules.
- [ ] Cross-check approved shared art export for Sudoku / Word Search hashes and IDs. **No shared unlock database** or cross-app ownership assumptions.

### Pass B — Collection UI & player experience
- [ ] Test **All**, Woodland, Ocean, Garden and Farm tabs with available/locked/equipped previews at 360dp, 393dp and compact device width, and large system text (up to 200%).
- [ ] Check thumbnail cell sizing, aspect ratio, readable names (especially Sea Turtle, Watering Can, Potted Tulip, Hay Bale), clipped outlines, row order, intentional diversity in first visible row and scrolling cues.
- [ ] Check lock icon/unlock copy, progress-to-goal, tap response, equipment state, selected avatar/profile card, result/share card and both Co-op/Versus opponent identifiers.
- [ ] Test light/dark and high-contrast contexts and Android emulator + physical phone. Compare screenshots instead of trusting only instrumentation green.
- [ ] Check that Home and rich storybook mode art does not substitute inconsistent mascots for the approved Collection Fox/Owl (V6 was rejected for overlap/proportion errors).

### Pass C — Board themes and number styles
- [ ] Evaluate **existing seven boards plus proposed Moss/Rosewood/Pebble/Tidal** using the same 9×9 sample puzzle. Every palette must clearly differentiate givens, entries, pencil marks, matching, selection, region, partner and conflicts; include error/color-blind differentiation.
- [ ] Evaluate **all ten existing inks** across every relevant palette; identify near-duplicates and invisible or low-contrast combinations.
- [ ] Evaluate existing five fonts plus **Typewriter/Editorial/Soft Print/Tall Print** at all digits 1–9, realistic entered-number and tiny pencil-note sizes, both text-scale extremes and two phones.
- [ ] For new fonts, verify bundled static font file, actual license, resource mapping and no generic fallback; distinguish real glyph design from duplicate or merely renamed font family.
- [ ] Ensure board and font preview cards depict actual equipped renderers, not static marketing art or misleading 1-2-3 samples.

### Pass D — Function, upgrade and release integrity
- [ ] Verify milestones and counts via Solo, Daily, Co-op and legitimate Versus wins; deduplicate repeated results, maintain monotonic entitlement after record pruning.
- [ ] Verify original IDs, original thresholds, permanent ownership, active equipment and backup/export/restore from legacy saves.
- [ ] Unknown/new IDs from older partner devices must safely fall back; no network protocol or Wi-Fi LAN regression. Real **two physical phone** Co-op and Versus test mandatory.
- [ ] Confirm no rewarded asset changes puzzle difficulty, hint availability, score or gameplay advantage.
- [ ] Android API26–36 coverage with smaller/larger screens and large text. Reviewer builds must never replace existing public app.
- [ ] Recover original production signing identity and verify a higher-version **production-signed Beta 1 → Beta 2 install-over on a backed-up device**, preserving puzzles/profile/inventory.
- [ ] Owner approves on-device final integrated visuals and release notes. No public release before all hard blockers pass.

## Severity and acceptance
- **P0 STOP:** data loss, signer mismatch, wrong package, breakage of older saved IDs, failed two-phone network/missing game resources.
- **P1 BLOCK RELEASE:** visibly off-style item, materially broken board contrast/font glyph, clipped/colliding UI, unreadable 48dp portrait, unreproducible asset provenance or unlicensed font.
- **P2 CORRECT BEFORE APPROVAL WHEN PRACTICAL:** small spacing inconsistencies, awkward gallery ordering, subtle color drift, card copy/label polish.
- **P3 LATER:** optional new content, advanced animation, more emblems and future collection themes.

Deliverables: (1) machine-readable 79-row catalog snapshot, (2) normalized all-Collection contact sheets, (3) device screenshot grid, (4) per-issue P0–P3 findings with file+PR+screen and before/after evidence, (5) approved release candidate gate with explicit sign-off or remaining blockers. Do not mark the entire collection cohesive because an isolated 63-item Farm reviewer passed Android QA.

## Tracking
Use separate draft art/playable PRs, small batches and exact-review APK hashes. File final audit results against the integrated Beta 2 branch; avoid altering stable `main` as part of an audit-only plan. **This document is the owner's requested final all-collections audit obligation.**
