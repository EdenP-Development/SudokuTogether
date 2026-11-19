# GridMates — Canonical Collection Character Style & Species Policy

**Status: design guardrail for all future portrait packs.** Owner feedback Nov 19, 2026. It applies to the 22 existing portraits, all 21 proposed Beta 2 portraits, and any future Collection families. It does not change existing resources or saved profiles.

## One art style across every Collection

Use the actual packaged artwork, particularly `design/woodland/originals/fox.png`, `owl.png`, `design/ocean/`, and the current Garden drawable portraits, as reference. **Do not adopt a new style just because a new collection has different subject matter.**

Required characteristics for every playable 512×512 original PNG:

1. **Consistent shape grammar:** friendly chibi animal silhouettes, rounded body/head ratios, intentionally simplified anatomy; compact, readable at 30dp, 48dp and 72dp.
2. **Consistent outlines:** dark forest-green contour and interior-line weights, steady round joins, no thick comic borders on one family and thin painterly outlines on another.
3. **Consistent face:** matching eye size/placement, simple snout/beak treatment, tiny mouth and friendly expression. Keep paws, fins and wings as clean graphic forms rather than highly detailed painted anatomy.
4. **Consistent color/shading:** limited solid natural palette, minimal soft shading (if any), restrained highlights; no painterly fur texture, photo detail, rough noisy AI edges, dramatic lighting, or complex scene behind one character.
5. **Consistent presentation:** transparent background; centered character; full head, ears, tails and appendages inside the safe circle; comparable scale relative to existing 48dp player-icon portraits.
6. **Works in app:** exact drawable/resource mapping, no embedded text, broken alpha, small floating pixels, ambiguous animal anatomy, inconsistent cropping or harsh halo effects. Audit silhouettes at 30/48/72dp on light and dark surfaces.
7. **Character vs background:** rich storybook/painterly environmental art is **fine for Home and game-mode backgrounds**. Collection characters and Co-op/Versus foreground mascots must keep the simpler Collection style. Reuse the *canonical* Fox/Owl visual identity across every screen, not a newly painted imitation.

### First-pilot art-review cautions

- **Cow / Chicken / Raccoon / Sea Turtle:** inspect fill geometry, outlines and eye/snout proportions against Fox/Owl. Don't approve a candidate just because it looks cute alone.
- **Firefly:** check the yellow glow/feathered edge and stray bright pixels. If the glow blurs at Collection size, remove/clean it rather than keeping inconsistent effects.
- If a new animal's head/body ratios, face or outline are incompatible, redraw that animal or the pilot batch rather than adjusting the 22 established character identities to match it.

## Repeated animal rule: disfavored, but not categorically forbidden

**Default: use a genuinely different animal/species/breed whenever possible.** There is enough biological variety to grow the collection without routine reskins.

When an overlapping common-name animal makes thematic sense in multiple families (example: woodland pond frog vs rainforest tree frog), allow it only after documenting all of the following in the species ledger:

1. **Biologically / visually distinct identity**: ideally a different species or recognizable breed, not merely a different hue of exactly the same drawing.
2. **Collection-specific silhouette or markings**: facial/ear/limb shapes, spotting, posture or clearly different anatomy/context, while preserving the same canonical illustration style.
3. **Justified collection placement**: an animal naturally belongs in both themes, not a slot-filler.
4. **Clear player names**: e.g., “Tree Frog” vs “Pond Frog”, never two indistinguishable cosmetics both called “Frog”.
5. **No copied art with recolor only.** Compare side-by-side at 48dp and show the visible distinction.
6. **Explicit owner review for repeats** in the content manifest and asset-review PR.

Two different turtle types (`woodland_tortoise` and proposed `ocean_sea_turtle`) are not an identical species repeat: the ocean version must look like a marine turtle with flippers, while the Woodland tortoise retains land-tortoise proportions. Avoid adding a second generic Turtle icon when a unique species can be chosen.

## Implementation architecture

- Proposed catalog entries carry `species_key` as a stable, normalized animal identity, along with `collection`, `art_family="gridmates_chibi_v1"`, and `repeat_exemption` only when explicitly reviewed.
- The portrait roster must fail automated contract checks for duplicate `species_key` without an approved, explained exemption. A common-name label match triggers manual review even where the species differ.
- Curated cosmetic IDs remain immutable when released. The species ledger is **design metadata**, not a network-message field, and doesn't migrate/change the player's `Appearance`.
- For non-animal cosmetics (boards, fonts, basic icons), no species record is needed.

## Acceptance before implementing a batch

Compare proposed animal art to actual Fox, Owl, Octopus and Honeybee originals **on a single annotated contact sheet**, same display scale. Check transparent-edge and circular-crop screenshots, then import approved images into `drawable-nodpi`. Verify no new species is a duplicate slot-filler and that every portrait has the same `gridmates_chibi_v1` design system. Keep original source images, export checksums, and a repeat-approval record when necessary.

This policy covers both [content specification PR #93](https://github.com/PlayerEden/SudokuTogether/pull/93) and [pilot artwork PR #94](https://github.com/PlayerEden/SudokuTogether/pull/94), and is the standard for all subsequent art production.
