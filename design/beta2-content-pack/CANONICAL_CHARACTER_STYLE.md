# GridMates — Canonical Collection Icon Style & Subject Policy

**Status: design guardrail for all future Player Icon packs.** Owner feedback Nov 19, 2026. It applies to the 22 existing animal portraits, the 21 proposed Beta 2 themed icons (14 animals, 7 plants/produce/items), and all future Collection families. **No human/people/humanoid icons.** It does not change existing resources or saved profiles.

## One art style across every Collection

Use the actual packaged artwork, particularly `app/src/main/res/drawable-nodpi/woodland_fox.png`, `woodland_owl.png`, `ocean_octopus.png`, and `garden_honeybee.png`, as reference. **Do not adopt a new style just because a new collection has a flower, plant, crop or object instead of an animal.**

Required characteristics for every playable 512×512 original PNG:

1. **Consistent shape grammar:** friendly simplified chibi animal silhouettes or equally simplified flowers, plants and objects. Preserve recognizable subject geometry; compact and readable at 30dp, 48dp and 72dp.
2. **Consistent outlines:** dark forest-green contour and interior-line weights, steady round joins, no thick comic borders on one family and thin painterly outlines on another.
3. **Consistent faces where appropriate:** animal eye sizes/placement, simple snout/beak treatment, tiny mouth and friendly expression. **Faces are optional for plants and objects**; do not force eyes, mouths, limbs or personification onto a watering can or strawberry.
4. **Consistent color/shading:** limited solid natural palette, minimal soft shading (if any), restrained highlights; no painterly fur texture, photo detail, rough noisy AI edges, dramatic lighting, or complex scene behind one character.
5. **Consistent presentation:** transparent background; centered complete subject; no cropped ears, leaves, petals, stems, handles, tails or appendages; comparable scale relative to existing 48dp player-icon portraits.
6. **Works in app:** exact drawable/resource mapping, no embedded text, broken alpha, small floating pixels, incorrect anatomy or object geometry, inconsistent cropping or harsh halo effects. Audit silhouettes at 30/48/72dp on light and dark surfaces.
7. **Character vs background:** rich storybook/painterly environmental art is **fine for Home and game-mode backgrounds**. Collection characters and Co-op/Versus foreground mascots must keep the simpler Collection style. Reuse the *canonical* Fox/Owl visual identity across every screen, not a newly painted imitation.

### First-pilot art-review cautions

- **Cow / Chicken / Raccoon / Sea Turtle:** inspect fill geometry, outlines and eye/snout proportions against Fox/Owl. Don't approve a candidate just because it looks cute alone.
- **Firefly:** check the yellow glow/feathered edge and stray bright pixels. If the glow blurs at Collection size, remove/clean it rather than keeping inconsistent effects.
- If a new animal's head/body ratios, face or outline are incompatible, redraw that animal or the pilot batch rather than adjusting the 22 established character identities to match it.

## Mixed-theme collection identity

- **Garden:** bugs, flowers, produce, planting and tools. Beta 2 proposes Sunflower, Strawberry, Firefly, Watering Can and Potted Tulip; Garden should not be renamed Bug Collection or filled only with insects.
- **Farm:** animals alongside Pumpkin and Hay Bale, with no farmer, person or human figure.
- **Ocean:** sea animals and Seashell; varied sea-world motifs, consistent shape grammar.
- **Woodland:** mostly diverse forest animals at this stage; future Acorn, Pinecone or Mushroom can fit if the final asset respects the same icon style.

**No people, human faces, humanoid avatars or human silhouettes.** A flower, vegetable or tool does not need a drawn face. The visual consistency is in its linework, restrained palette, rounded geometry, scale and clarity. Preserve existing icon IDs and unlocks; only add fully illustrated, approved assets.

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

- Every proposed Player Icon has stable `subject_key` and `subject_kind` (`animal`, `plant`, `produce`, `item`), plus `art_family="gridmates_chibi_v1"`. **Only animal subjects** have `species_key`/`species_description`; repeats require explicit reviewed variant metadata.
- The Player Icon roster must fail automated contract checks for duplicate `subject_key`, and animal repeats need properly reviewed biological/visual exemptions. Check near-duplicate flowers and objects as carefully as animals.
- Curated cosmetic IDs remain immutable when released. The species ledger is **design metadata**, not a network-message field, and doesn't migrate/change the player's `Appearance`.
- For non-animal Player Icons (flowers, produce, items) and non-icon cosmetics (boards, fonts, emblems), no species record is needed.

## Acceptance before implementing a batch

Compare proposed animal, plant and object icons to actual Fox, Owl, Octopus and Honeybee originals **on a single annotated contact sheet**, same display scale. Check transparent-edge and circular-crop screenshots, then import approved images into `drawable-nodpi`. Verify no new species or motif is a duplicate slot-filler and that every portrait has the same `gridmates_chibi_v1` design system. Keep original source images, export checksums, and a repeat-approval record when necessary.

This policy covers both [content specification PR #93](https://github.com/PlayerEden/SudokuTogether/pull/93) and [pilot artwork PR #94](https://github.com/PlayerEden/SudokuTogether/pull/94), and is the standard for all subsequent art production.
