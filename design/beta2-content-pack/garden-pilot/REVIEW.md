# GridMates Beta 2 — Garden mixed-theme art pilot

**Status: ART REVIEW ONLY, not playable, not in the public APK.** This branch starts from the approved V5.1 `main` source without modifying Android resources, cosmetic IDs, unlock thresholds, profile files or LAN.

## What this solves

The current Garden Collection is mainly insects. The owner clarified the collection should instead evoke **bugs, blooms, harvest and growing things**. A Garden player icon can be a flower, fruit or watering can, using the *same visual art family* as Fox, Owl, Ladybug and Honeybee. **No humans or humanoid figures.**

## First non-bug candidates

| Stable future icon ID | Type | Proposed unlock | Original image |
| --- | --- | ---: | --- |
| `garden_sunflower` | plant | 11 completed puzzles | `garden_sunflower.png` |
| `garden_strawberry` | produce | 23 completed puzzles | `garden_strawberry.png` |
| `garden_watering_can` | item | 62 completed puzzles | `garden_watering_can.png` |
| `garden_potted_tulip` | plant | 100 completed puzzles | `garden_potted_tulip.png` |

These four add to the unchanged first-five Garden Firefly (42 finishes), and preserve the existing Ladybug, Butterfly, Dragonfly and starter Honeybee. They keep the future expansion at **21 new icons / 29 total cosmetics** in the [design plan PR #93](https://github.com/PlayerEden/SudokuTogether/pull/93). No previously tested first-five ID or threshold was changed.

## Artistic review

- Original transparent 1254×1254 four-subject source sheet is `garden_mixed_source_sheet.png`. Four individual RGBA sprites are each 512×512; source and export SHA-256 values are pinned in `manifest.json`.
- All sprites use clean dark forest-green outer lines, natural colors, simplified silhouettes, minimal shading and no human features. Unlike animal icons, a flower or watering can does not need a face.
- **Requires a visual comparison to canonical portraits** (`garden_honeybee.png`, `garden_ladybug.png`, `garden_butterfly.png`, `garden_dragonfly.png`) at 30dp, 48dp and 72dp. The lines here may be a little heavier; lighten/re-export if real Android presentation looks inconsistent.
- Verify no black matte, halos, detached alpha pixels, clipping, microscopic seeds/petals, unreadable handles or distorted pot at final size.
- A pretty concept sheet is not sufficient; inspect actual Collection cards on a reviewer APK before main integration.

## Integration gates

1. Owner reviews the four pilot artworks alongside existing Garden assets.
2. Copy approved `garden_*.png` files into app drawables and register exact curated IDs in `Cosmetics.kt` + `AnimalPortraits.kt`. Keep `category="portrait"` internally because the user-facing category is **Player icons**, not only animal portraits.
3. Test unlock targets 11/23/62/100 finishes, record deduplication, permanent earned ownership, backup/restore, Farm Garden filtering, lazy scrolling, small screen/font.
4. Build isolated Android 16 reviewer APK, compare against real screenshots, and test update safety. Do not publish with the wrong signer or overwrite spouse's Beta 1.
5. Approved assets may enter the shared GridMates offline art pack so Word Search can reuse the **same file bytes** with its own progression system.

This art-only PR does **not** update `main`, the tested first-five draft PR #95, signing, monetization or multiplayer. The Versus fox/owl canonical-style correction remains a separate pre-Beta 2 gate.
