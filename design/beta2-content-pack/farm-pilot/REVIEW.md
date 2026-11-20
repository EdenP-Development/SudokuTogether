# GridMates Beta 2 — Farm collection mixed-theme artwork

**Status:** art candidates only; no Android catalog/unlocks or existing app modified on this branch.

## Intent

Farm should be a little countryside world, not only livestock: the current starter **Cow** and milestone **Chicken** stay untouched. This batch adds **Sheep, Pig, Pumpkin, Hay Bale**. There are no people, human silhouettes or humanoid subjects.

## Exact approved-to-review candidates

| Stable ID | Theme | Progress requirement (proposal) |
| --- | --- | ---: |
| `farm_sheep` | Friendly farm animal | 20 completed puzzles |
| `farm_pig` | Friendly farm animal | 36 completed puzzles |
| `farm_pumpkin` | Farm harvest produce | 58 completed puzzles |
| `farm_hay_bale` | Rustic farm item | 85 completed puzzles |

The images have no text, backgrounds or fabricated humans. Original **1254×1254 transparent sheet** is retained, and the four individual **512×512 transparent PNG exports** are accompanied by exact SHA-256 metadata in `manifest.json`. `export_farm_icons.py` regenerates the crops deterministically using Pillow 12.3.0 with uniform 432px shape footprint inside a 512px safe circle.

## Art guidelines

- Keep `gridmates_chibi_v1`: dark forest-green outlines, rounded silhouettes, limited soft color palette, minimal realistic texture
- Sheep/Pig friendly expressions; **Pumpkin/Hay Bale intentionally have no human-like faces**
- Match the existing Cow and Chicken at **30dp, 48dp and 72dp**; reject cropped ears, straw, horns, halos or alpha-fringe artifacts
- Build an actual Android Collection preview before approving these as cosmetics
- Treat the *portrait* catalog category as the game's **Player Icons**, not a rule requiring every icon to depict an animal

## Safety and sequencing

Do not modify `main`, the completed first-five PR #95, Garden PR #98, or Versus V7 PR #99 from this art-only branch. When art is approved, integrate the exact bytes in a **new isolated Android reviewer**, add unit and Compose tests for 20/36/58/85 unlocks, and retain Cow/Chicken + all prior ID/requirements/saved ownership.

Public Beta 2 requires separately verified real two-phone same-Wi-Fi play and original-signer, save-preserving Beta 1 update checks. The art pack itself can be reused in Word Search without tying unlock progress across games.
