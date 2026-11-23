# GridMates — Collection portrait art style and intake review

**Status (Nov 23, 2026):** review-only. This document does not approve or import any of the seven new character illustrations. It is stacked on the verified 79-cosmetic draft [PR #110](https://github.com/PlayerEden/SudokuTogether/pull/110) and related to [issue #112](https://github.com/PlayerEden/SudokuTogether/issues/112). No catalog IDs, save formats, LAN behavior, or gameplay requirements change.

## Canonical references actually examined

Sources on `feat/beta2-meaningful-cosmetic-unlocks`:
- Farm: `farm_cow.png`, `farm_chicken.png`.
- Garden: `garden_honeybee.png`, `garden_sunflower.png`.
- Ocean: `ocean_octopus.png`, `ocean_seahorse.png`.
- Woodland: `woodland_rabbit.png`, `woodland_bear.png`, plus `woodland_chipmunk.png`, `woodland_beaver.png`, `woodland_woodpecker.png`, `woodland_skunk.png`.

Those illustrations favor distinct, uncluttered silhouettes, dark forest-green contour lines, warm muted colors, rounded friendly shapes, simple eyes/mouths and restrained highlights. The older Rabbit/Bear and Cow/Chicken look particularly readable at avatar size. Some later Woodland originals use more body-focused compositions and overly padded canvases; their *existing* render-only size correction in #110 remains intact.

## Candidate acceptance rules

1. **Family fit:** compare every new Farm icon to Cow/Chicken, new Garden icon to Honeybee/Sunflower, and new Ocean icon to Octopus/Seahorse. A cohesive new sheet alone is not proof it matches the existing game.
2. **Visual language:** strong but not black outlines, simple face, rounded massing, matte fill or modest warm shading. Avoid detailed painterly highlights, tiny decorative marks and thin marks that disappear at 48dp.
3. **Avatar framing:** independent centered 512×512 transparent RGBA sources. Subject occupancy should be consistent with the older (generally ~76–92% extent) portraits without cropping in the `CircleShape` and internal 8% padding. The new seven-candidate revision aims for ~75–85% visible extent, subject to on-device review.
4. **No baked chrome:** no category text, panel backgrounds, grids, labels, checkerboard pattern, stray generation artifacts or watermarks in source PNGs.
5. **Naming:** stable curated IDs only after approval: `farm_goat`, `farm_duck`, `farm_barn`, `garden_snail`, `garden_daisy`, `garden_mushroom`, `ocean_sea_star`. Do not invent IDs or unlock requirements in the shipping app without review.
6. **Real-size test:** preview 48dp, 62dp and 96dp under Android's existing `AnimalPortrait` rendering, including circular clipping, profile/equipped and locked previews; also inspect physical phone results at normal and large font scaling. Raster previews drawn at 48/62/96 *physical pixels* are not equivalent to Android screenshots.

## Revised candidate (v2) checkpoint

A separate owner-conversation review ZIP was created with seven **individual** 512×512 transparent RGBA PNGs, a full transparent source spritesheet, checksum manifest, a checkerboard contact sheet, before/after original-candidate comparison, and approximate circle-size comparisons.

- Simplified v1's glossier styling, especially Duck, Snail, Daisy and Sea Star; Goat and Mushroom improved but require individual signoff.
- Barn is still a concern: door-cross detailing and small geometry may be muddy at 48dp. Do not silently approve.
- Local technical QA on the revised pack passed: all seven distinct PNGs, required alpha transparency and safe margins, roughly .75–.85 max-axis visible extents, and matching SHA-256 file contents. The prior candidates and existing shipped artwork are **not modified**.
- Local pack SHA-256: `ef54e1c19c850d2b03e5bde682fcc2e5ba85e1dc794a7260462627f448b8fb5d`.
- The ZIP is available in the owner conversation, **not in this GitHub repository**. This document does not claim current access to any pre-existing owner-only candidates from earlier conversations.

## Technical gate for future imported artwork

Run:

```bash
python -m pip install Pillow
python scripts/validate_collection_sprite_intake.py /path/to/new-sprite-candidates --output sprite-intake-report.json
```

This rejects broken PNG geometry (wrong size, nontransparent corners, off-center or implausibly padded figures, unsafe circular margins and byte-identical duplicates). **Passing is not art-style certification or consent to ship.** Use a side-by-side canonical original comparison and owner approval for each image.

## Release and integration restrictions

- Do not add seven new portraits to the existing 79-item catalog until explicit individual artwork approval. An approved seven-item addition would result in 86 cosmetics / 50 portraits, not the currently verified 79 / 43 portraits.
- Separate any eventual import into another draft PR. Pin each original image SHA, preserve existing portrait IDs and permanent reward ownership, and use solo-accessible + ordinary-completion reward paths.
- Before public Beta 2: actual Android screenshot review, two-device LAN, and production-signed Beta1→Beta2 save-preserving update must still be validated.
- Daily Journal rewards stay deferred in [issue #111](https://github.com/PlayerEden/SudokuTogether/issues/111), not part of an art change.
