# GridMates: Sudoku — Collection Art Bible
## Beta 2 visual identity, catalog verdicts, and acceptance gates

**Version:** 1.0 working specification · **Date:** 2026-11-23  
**Status:** art direction is established; individual candidate verdicts below are **provisional unless owner explicitly approves an actual on-phone comparison**. Creating this guide is not approval to merge art or ship Beta 2.

**First read in future conversations:** this document, [the Beta 2 owner release handoff](BETA2_OWNER_RELEASE_HANDOFF.md), [the Mode Art Bible](GRIDMATES_MODE_ART_BIBLE.md), and [visual audit #117](https://github.com/PlayerEden/SudokuTogether/issues/117). Re-check the latest PR head before editing, as this document is on a *documentation branch* and is not necessarily in `main`.

### Product promise

GridMates is a warm, nostalgic paperback-puzzle-book game with modern Android ease of use, local-first play, and satisfying cosmetic collection. Collectibles should feel like **one lovingly illustrated family**, not individually attractive images from different artists, sticker packs, or generative batches.

The owner's especially active family player has already collected most prior rewards. **Quality and reward meaning matter together**: enough to earn, but never pad the catalog with a visually off-style or duplicate-feeling asset merely to increase its count.

## A. Canonical art grammar — apply to every new or revised collectible

1. **Rounded, friendly and immediately legible.** Prefer recognizable large faces/primary forms over narrow full-body figures or tiny decorative details. Animals, flowers and objects can have different silhouettes while sharing proportions and illustration treatment.
2. **Forest-green, deliberate contour.** Existing clean originals use readable dark green outlines, rounded joins and consistent visual weight. Avoid harsh black, sketchy/thin inconsistent lines or ultra-heavy outlines on just one object.
3. **Warm matte palette.** Muted creams, natural greens, honey gold, gentle rusts and theme-appropriate ocean blues; moderate contrast. No random neon or over-saturated, glossy 3D, painterly/photorealistic lighting, unrelated shadows or textures.
4. **Simple readable face language.** Approachable eyes, modest expression, restrained highlights; avoid suddenly more realistic/anime facial anatomy, overly complex pupil/shading patterns or disproportionate eyebrows.
5. **Consistent composition and mass.** Center the subject with pleasing balance. Most older originals fill roughly 76–92% of their source image extent; revised seven-candidate target about 75–85%, but **numbers are guidelines, not acceptance criteria**. Visually judge the actual circle after cropping/padding.
6. **Small-size-first.** Each art piece must stay identifiable, expressive, and visually comparable beside its siblings at **48dp, 62dp and 96dp** in the *actual* Android Collection `AnimalPortrait`; also examine larger detail preview/equipped/profile at normal and enlarged fonts.
7. **No baked interface elements.** Portrait files are only the subject on transparent alpha: no category title, border, background panel, checkerboard, watermark, sample text, Sudoku numbers or cards.
8. **Recognizable uniqueness.** Rewards must feel distinct: Honeybee/Firefly cannot just read as the same round flying insect, Constellation/True North should not be two interchangeable sparkle glyphs, and Grid Master/Gridkeeper should not be indistinguishable diamonds.
9. **Preserve the established visual identity.** Do not redraw the approved fox/owl or best original icons merely to match weaker additions. Match additions to the clean originals.
10. **No artificial batching approval.** A consistent seven-candidate contact sheet does NOT establish a match to the actual old Farm/Garden/Ocean gallery. Review *each candidate against original relatives*.

### Exact reviewed art anchors (source-based)

| Collection | Canonical comparison references | What they establish |
|---|---|---|
| Woodland | `woodland_owl.png`, `woodland_fox.png`, `woodland_rabbit.png`, `woodland_bear.png` | Round expressive faces, simple friendly shapes, dark green contour |
| Farm | `farm_cow.png`, `farm_chicken.png` (also Sheep/Pig for category review) | Clean farm-animal proportions and distinct silhouettes |
| Garden | `garden_honeybee.png`, `garden_sunflower.png` (also Ladybug/Butterfly) | Garden warmth, simplified insect/flower geometry |
| Ocean | `ocean_octopus.png`, `ocean_seahorse.png` (also Sea Turtle/Harbor Seal) | Recognizable ocean species without excessive rendering |
| Basic emblems | Existing `tile`, `leaf`, `duo`, `flash`, `star`, `laurel` designs | Small, high-contrast symbolic language; new set should be more unified than mixed Unicode glyphs |

**Separate mode-art rule:** Solo/Home uses forest backdrop + actual live canonical Fox/Owl and is the character-identity anchor; Co-op's dedicated illustrated scene is a strong mood/interaction reference; Versus V7 is *not approved* even though tests passed. See the separate [Mode Art Bible](GRIDMATES_MODE_ART_BIBLE.md). Do not apply mode-banner painting techniques indiscriminately to flat Collection portraits.

## B. Actual catalog baseline (confirmed in `core/.../Cosmetics.kt` on #118)

| Area | Count | Current implementation |
|---|---:|---|
| Woodland portraits | 17 | Shipped/draft source sprites, original IDs maintained |
| Ocean portraits | 11 | Existing portraits |
| Garden portraits | 9 | Existing portraits |
| Farm portraits | 6 | Existing portraits |
| **Total character portraits** | **43** | No seven candidates integrated yet |
| Basic emblems | 12 | Original six + six veteran-reward symbols |
| Ink colors | 10 | Existing |
| Board themes | 11 | Existing |
| Number fonts | 9 | Existing |
| **Total cosmetic catalog** | **85** | Draft Beta 2 owner review, not production release |

**If all seven proposed portraits were actually approved and implemented**, counts would be Woodland 17, Ocean 12, Garden 12, Farm 9, **portraits 50**, and cosmetics **92**. This is a *scenario*, not current catalog state.

## C. Character verdict ledger — owner-facing review queue, not blanket approvals

These reflect the previous phone audit and **preliminary art-direction grading**. `PASS` below means a useful baseline / no urgent redraw identified, **not guaranteed individual pixel-perfect verification**. Changing a shipped PNG requires its own recorded before/after approval.

### Woodland — 17 existing portraits

| Portrait | Working verdict | Action / rationale |
|---|---|---|
| Owl | **ANCHOR / KEEP** | Canonical mascot; preserve pixel identity |
| Fox | **ANCHOR / KEEP** | Canonical mascot; preserve pixel identity |
| Rabbit | **ANCHOR / KEEP** | Readable original face and mass |
| Bear | **ANCHOR / KEEP** | Useful face/occupancy comparison |
| Badger | PASS / keep | Review only if phone shots show issue |
| Hedgehog | PASS / keep | Same |
| Robin | PASS / keep | Same |
| Deer | PASS / keep | Same |
| Squirrel | PASS / keep | Same |
| Otter | PASS / keep | Same |
| Tortoise | PASS / keep | Same |
| Cat | PASS / keep | Same |
| Raccoon | PASS / keep | Strong recent comparison reference |
| Chipmunk | **TWEAK / inspect** | Later art wave; modal size improved after renderer fix, check face and style with Rabbit/Raccoon |
| Beaver | **TWEAK / inspect** | Later art wave; occupation and proportions compared with Bear |
| Woodpecker | **REDRAW CANDIDATE (high)** | Narrow side-facing/full-body silhouette, inconsistent apparent mass; label-wrap bug is **separate** and fixed in UI polish |
| Skunk | **TWEAK / inspect** | Later art wave; check head/torso visual weight and outline |

**First redesign draft (not approved):** [Woodpecker concept A](../design/collection-art/woodpecker-concept-a.svg) is an editable front-facing, source-preserving vector experiment; [its brief](../design/collection-art/WOODPECKER_REDESIGN_BRIEF.md) specifies transparency, Android-size comparisons and explicit owner sign-off. It is **not** loaded by the app or a replacement for the canonical PNG.\n\n**Current art state:** the Beaver/Chipmunk/Woodpecker/Skunk source PNGs remain original in the 85-cosmetic review. Existing display-scaling correction and the larger preview control are **not proof of an artistic redraw**. A previous Woodland four-character illustration concept pack was discussed but not confirmed as an import-ready approved replacement in GitHub. No silent swaps.

### Ocean — 11 existing portraits

**Working PASS / keep, with consistency inspection:** Octopus (anchor), Sea Turtle, Harbor Seal (anchor), Hermit Crab, Seahorse (anchor), Manta Ray, Dolphin, Jellyfish, Seashell, Narwhal, Clownfish. Compare Ocean saturation and detail across more vivid Seahorse/Clownfish vs subdued Octopus/Turtle/Seal; assess Manta Ray/Dolphin visible mass and alignment. Do not rework an entire good set for trivial differences.

### Garden — 9 existing portraits

**Working PASS / keep, with specific inspection:** Honeybee (anchor), Ladybug, Butterfly, Sunflower (anchor), Dragonfly, Strawberry, Firefly, Watering Can, Potted Tulip. **Firefly vs Honeybee** is an actual *differentiation* concern in screenshots; place them side by side at 48/62dp before accepting unchanged. Check Dragonfly apparent size and small detail visibility on Watering Can / Potted Tulip.

### Farm — 6 existing portraits

**Working PASS / keep:** Cow (anchor), Chicken (anchor), Sheep, Pig, Pumpkin, Hay Bale. Hay Bale has been perceived as small/thin; inspect its actual 48/62dp footprint and whether tiny straw detail survives. Preserve the appealing variety of animals + crop + rustic object.

### Basic — 12 existing insignias

| Original six | Working verdict |
|---|---|
| Gridkeeper, First Sprout, Two Minds, Challenger, Star Solver, Champion | **KEEP** as established symbolic reference; review contrast and meaning |

| Six Beta 2 additions | Working verdict |
|---|---|
| Constellation | **REWORK VISUAL**: generic star appearance; distinguish from True North |
| Sunrise | **REWORK VISUAL**: bright hue/treatment mismatches muted forest set |
| Focused Solver | TWEAK: give an intentionally drawn coherent emblem |
| True North | **REWORK VISUAL**: too close to Constellation |
| Moonlight | **REWORK VISUAL**: thin, visually light crescent compared with other items |
| Grid Master | **REWORK VISUAL**: too close to Gridkeeper; meaningful higher-tier shape |

**Important correction to the earlier informal verdict list:** The initial statement that all 12 Basic symbols `PASS as-is` conflicts with subsequent owner screenshots and repeated audit findings. **No blanket Basic art sign-off exists.** Keep all six new reward IDs, unlock requirements and earned ownership, but treat their *artwork* as a coherent-set refinement task.

## D. Recovered but UNRELEASED seven candidates

**Artwork provenance:** seven revised transparent 512×512 RGBA v2 PNGs were produced and packed separately in the owner conversation; these assets are **not the live portrait catalog**. Source-reported reviewed ZIP SHA-256: `ef54e1c19c850d2b03e5bde682fcc2e5ba85e1dc794a7260462627f448b8fb5d`. Art-import ZIP SHA-256: `b2e5be72e99cf3fccd4b316198f6e1e551dbf289f7f6a171665c80be7dac3b64`. Verify those actual files again if materializing; a checksum note isn't a substitute for a file.

| Proposed ID / name | Compare against | Provisional review verdict | Decision still needed |
|---|---|---|---|
| `farm_duck` — Duck | Cow, Chicken | **PASS CANDIDATE** | Owner 48/62/96dp real-device approval |
| `farm_goat` — Goat | Cow, Sheep | **TWEAK** | Simplify proportions/face if out-of-family |
| `farm_barn` — Barn | Hay Bale, Pumpkin | **TWEAK / HOLD** | Doors, trim and fine geometry legible at 48dp |
| `garden_snail` — Snail | Honeybee, Ladybug | **PASS CANDIDATE** | Owner small-icon comparison |
| `garden_daisy` — Daisy | Sunflower, Potted Tulip | **PASS CANDIDATE** | Owner small-icon comparison |
| `garden_mushroom` — Mushroom | Sunflower, Watering Can | **TWEAK** | Consistent shape/outline/shading |
| `ocean_sea_star` — Sea Star | Octopus, Seahorse | **PASS CANDIDATE** | Owner small-icon comparison |

A “PASS CANDIDATE” **does not authorize import**. These names and IDs were documented in intake #113; new unlock routes, persistent ownership behavior and real artwork must be deliberately integrated/tested in their own PR.

## E. Acceptance procedure — apply to EVERY new or modified portrait

### Required side-by-side review

1. Start from the **actual PNG files**, not web lookalikes, screenshots of contact sheets, or memory of old generated concepts.
2. Use the real standalone Android art-reviewer [draft #114](https://github.com/PlayerEden/SudokuTogether/pull/114) when convenient. It loads a separate owner-provided ZIP and shows original Farm Cow/Chicken, Garden Honeybee/Sunflower and Ocean Octopus/Seahorse beside candidates in the actual `AnimalPortrait` UI at 48, 62 and 96dp. Confirm candidate from file picker—not synthetic test PNGs. The independent package protects Beta 1 save data.
3. For Woodland revisions, create the **equivalent** comparison against Fox/Owl/Rabbit/Bear/Raccoon with original vs new versions. Do not assume seven-sprite Farm/Garden/Ocean reviewer already supports Woodland imports.
4. Inspect source alpha bounds *and* visible visual weight inside the 8% padded circle. Check no clipping of ears, antlers, tail, wings, petals or outline at profile/preview sizes.
5. Compare contour thickness, matte shading, accent saturation, face expression, pose/portrait-versus-full-body convention, and difference from existing neighbors.
6. Test label wrapping, lock badge coverage and selected/equipped state in the *actual Collection screen*. UI text bugs do not require redrawing otherwise good art.
7. Record an **individual owner verdict** plus image filename and SHA-256 in issue #112 or a linked PR:
   - **PASS** = owner approved exact source version and phone crop.
   - **TWEAK** = specific repairs listed; not approved for production.
   - **REDRAW** = composition/style does not fit; start new version.
   - **HOLD** = technically/artistically incomplete or unreviewed.
8. A technical validator PASS is **necessary but not sufficient**; only owner phone judgment grants artistic approval.

### Mechanical sprite intake gate

- Source PNG: exactly **512×512 RGBA**, transparent background and corners, no baked app UI; subject centered and within safe circle.
- Match expected curated filenames / IDs, no duplicates, no accidental low-resolution upscales, wrong RGB/no-alpha, or inconsistent asset hashes.
- Validate via `scripts/validate_collection_sprite_intake.py` from [intake guide #113](https://github.com/PlayerEden/SudokuTogether/pull/113), retain output JSON and original source hashes.
- Verify in Android, using normal and enlarged text, at 48/62/96dp; larger detail previews may use 116dp. Pixel-only mockups are not enough.
- Any redraw must leave the previous approved image available for rollback until the replacement is approved.
- Preserve font licenses, stable catalog IDs, save compatibility, and unlocked ownership. No gameplay or LAN mutation in an art-only PR.

## F. Integration and release policy

**First matching-set study:** [six editable Basic insignia concepts and Android comparison gate](../design/collection-art/BASIC_INSIGNIAS_V1_REVIEW.md) are available in a separate preview branch. These are **not** owner-approved replacements; the existing symbols and reward IDs remain the default outside the test build.\n\n**Priority 1 — current mismatches:** evaluate Woodpecker redraw, compare Beaver/Chipmunk/Skunk with anchors; refine the six Basic symbols **as a set**; record render-size and source-art fixes separately.

**Priority 2 — seven candidate gate:** independently approve Duck, Snail, Daisy, Sea Star on Android; review Goat, Mushroom, Barn with specific adjustments. Do not auto-import partial or full packs.

**Priority 3 — content integration:** only after explicit art approvals, add curated IDs/reward thresholds to the 85-cosmetic branch, pin approved source hashes, add locked/equipped/progression tests, and update catalog-count expectations. Preserve pre-existing completion thresholds and permanently earned cosmetics. Avoid paywalls, forced Daily streaks and unrecoverable rewards.

**Priority 4 — Beta 2 release:** reconfirm the combined branch's actual source/CI ancestry; validate on physical phones, two-phone same-Wi-Fi host/join/Co-op/Versus and a properly signed *same-package* Beta1→Beta2 upgrade without deleting active puzzles, existing unlocks, Daily progress, stats or profile. A side-by-side debug reviewer is not that upgrade. Never uninstall the family's original Beta 1 to solve debug-signing conflicts.

## G. Decision log and future chat handoff

- **Established direction:** one cozy illustrated family; original approved work is the reference. Style cohesion outweighs raw catalog size.
- **Confirmed catalog at this checkpoint:** 85 cosmetics, 43 portraits, 12 emblems; the seven v2 candidates are separately recoverable, not integrated.
- **Source-confirmed art intake:** [#113](https://github.com/PlayerEden/SudokuTogether/pull/113), [#114](https://github.com/PlayerEden/SudokuTogether/pull/114), [#112](https://github.com/PlayerEden/SudokuTogether/issues/112).
- **Visual phone evidence:** 23 + 8 user screenshots, tracked in [#117](https://github.com/PlayerEden/SudokuTogether/issues/117).
- **Not yet approved:** seven new portraits, alternative Woodland redraws, all six new Basic insignia *artwork*.
- **Do not conflate art with rules:** the six Basic insignias are already functional reward entries even though their visual treatment needs work.
- **Future updates:** Add dated source hash, current branch/PR, screenshot evidence, and owner PASS/TWEAK/REDRAW/HOLD for *each* changed image. Never rewrite an unapproved candidate's status as approved because CI is green.

**Owner release rule:** A sprite belongs in Beta 2 only when it is technically valid, readable at the intended size, unmistakably from the GridMates visual family, sufficiently distinct to be worth earning, and explicitly approved in the actual Android interface.
