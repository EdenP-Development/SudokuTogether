# GridMates — Character Art Bible and Species Registry

**Canonical standard for all Collection portraits and gamefront animal characters.** Applies to Woodland, Ocean, Garden, Farm, and every future collection. This supersedes free-form generation notes, **not** the actual approved source artwork.

## 1. One recognizable art family

The following packaged Android assets are the primary style references:

- `woodland_fox.png` and `woodland_owl.png` — principal game mascots, current Home/Co-op character language
- `ocean_octopus.png` — same approachable silhouette treatment for non-mammals
- `garden_honeybee.png` — same visual identity for insects
- Secondary samples: Rabbit, Bear, Harbor Seal, Butterfly

Use the same **visual grammar**, not the exact same face or body repeated: chunky recognizable chibi silhouette; simple bold dark-green line contour; bright cream facial/belly accents when anatomically appropriate; large, friendly readable eyes; calm minimal shading; restrained natural colors; centered transparent cutout; rounded, clean edges; consistent relative character size. Each creature must be recognizable at 30dp.

**Background painting and characters are intentionally separate styles.** A lush forest scene may have detailed leaves, depth, light shafts and brush texture. The fox, owl and any other recognizable mascot over that scene should still look like the Collection portraits. Don't accidentally let text-to-image painterly rendering invent a new animal art style.

## 2. Artwork production rules (asset acceptance)

- Master or source sheet retained under `design/`; one actual RGBA PNG per approved species/variant under `app/src/main/res/drawable-nodpi`.
- Preserve outline thickness and eye/head/body proportions relative to the approved reference animals; avoid excessive facial fur or painted feather detail, airbrushed modeling, photorealistic textures and glossy highlights.
- Keep main face and silhouette inside safe circular portrait crop, with consistent margins and no cutoff ears, horns, antennae, flippers or wings.
- Transparent outside silhouette; no baked-in background circles, labels, unlock locks, decorative stamps or frame (the app draws those). Check halo/fringe against both light and dark surfaces.
- Detail must survive a thumbnail at 30dp, ordinary 48dp profile, and large 72dp Collection preview. Smaller sizes should not collapse into muddy patches.
- Use authentic structure: believable anatomy in the simplified style (no extra limbs, incorrect ear count, duplicated wings, malformed eyes or unnecessary clothing). Species-specific features are allowed and encouraged.
- Final image checked in the actual Android Collection grid and active style preview; raw PNG integrity alone does **not** establish style approval.
- When a banner uses the same mascots, reuse source art/approved visual proportions; do not have painted alternative versions coexisting elsewhere in the game.

## 3. Species uniqueness: default no repeats

**Prefer distinct species.** There are plenty of animals, and a collection should feel new rather than a renamed or recolored duplicate. Maintain two identifiers separately:

1. **Cosmetic ID:** `family_species_or_variant`; stable user-facing saved unlock identifier (e.g., `ocean_sea_turtle`).
2. **Species key:** normalized biological/common animal identity (e.g., `sea_turtle`); supports duplicate checks across collections. It's a design registry field, **not** a new network protocol or a save field.

For the Beta 2 proposal, all 21 new portrait entries use unique species keys. Historical existing styles include `woodland_tortoise` and the proposed new `ocean_sea_turtle`: these are legitimately distinct animals, not a recolored duplicate. The new Cow/Chicken/Sheep/Pig/Duck/Goat are also absent from current portraits.

**Exception policy — only when justified:** If a future collection needs the same species again (e.g., an animal relevant in multiple ecosystems), explicitly record:
- `repeat_of`: original cosmetic ID,
- `variant_name`: actual distinctive breed/ecotype or meaningful version,
- `justification`: why both collections make sense,
- `visual_differentiators`: at least two observable differences (markings, body silhouette, physical features, pose, equipment only if relevant), and
- `style_reference`: same canonical house-art references.

A hue swap, a changed name, or adding a hat is insufficient by itself. We may reject a repeat even if the metadata are complete. Do not change or remove an already owned portrait to fix a newly spotted collision.

## 4. Existing species inventory (22 source-backed Collection animals)

| Woodland (12) | Ocean (6) | Garden (4) |
|---|---|---|
| Owl, Fox, Rabbit, Bear, Badger, Hedgehog, Robin, Deer, Squirrel, Otter, Tortoise, Cat | Octopus, Harbor Seal, Hermit Crab, Manta Ray, Jellyfish, Narwhal | Honeybee, Ladybug, Butterfly, Dragonfly |

This list is derived from `core/src/main/kotlin/com/playereden/sudokutogether/core/Cosmetics.kt`, not old brainstorming documents. Beta 2's proposed 21 species are enumerated in `design/beta2-content-pack/expansion-candidates.json`. Basic geometric emblems don't count as species.

### Taxonomy caution

A category label can legitimately be broad (e.g., “Owl” and later a distinct Snowy Owl), but before choosing a repeat **prefer a new species or a specific breed with a convincingly different silhouette**. Do not treat variant labels as a loophole for duplicate content.

## 5. Pilot art review — November 19, 2026

First five art-only candidates are tracked in [PR #94](https://github.com/PlayerEden/SudokuTogether/pull/94): Cow, Chicken, Raccoon, Sea Turtle and Firefly. These are NOT yet registered in the game.

**Provisional comparison based on the first contact sheet and the owner's existing screenshots:**
- Cow and Chicken: strong basic simple-shape direction; compare actual stroke weight and scale against Fox/Owl before accepting.
- Raccoon and Sea Turtle: viable silhouettes; make sure face construction doesn't feel more detailed than existing animals.
- Firefly: visible soft white glow/fringe near lower wings/body that is not part of the usual clean-edge Collection style; revise or remove the fringe before approval.

Do not approve based on file hashes alone. The art branch is an archive/review checkpoint, not the source of playable cosmetic IDs.

## 6. Automated gates

`scripts/tests/test_beta2_content_contract.py` enforces unique cosmetic IDs, no new duplicates across species keys, and required justification fields for any explicit repeat. The visual style itself still needs an actual side-by-side human review; a test cannot accurately prove character quality.

The **same checks apply to the Versus redraw**: keep detailed forest background and separate boards, but use the approved flat Fox/Owl house style. Test with real Android screenshot captures at typical phone widths and accessibility font scale.

## 7. What this does not change

No published cosmetics, no profile IDs/records, no achievement thresholds, no saved ownership, no offline LAN protocol, no signing identity, no public APK. Gate checks shape future content production only.
