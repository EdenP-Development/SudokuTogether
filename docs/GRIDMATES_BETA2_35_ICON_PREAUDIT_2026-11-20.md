# GridMates Beta 2 — first combined art-cohesion pre-audit (35 Player Icons)

**Date:** 2026-11-20. **Status:** preliminary visual/source-asset review of a verified isolated debug reviewer APK, NOT final owner art approval or Beta 2 release authorization.

## Exact evidence / repeatability
- Source: [PR #101 Farm owner-review APK](https://github.com/PlayerEden/SudokuTogether/pull/101) from [Android QA #44671291566](https://github.com/PlayerEden/SudokuTogether/actions/runs/44671291566), downloaded artifact `#14600512444`.
- Verified extracted APK: `GridMates-Beta2-Farm-OwnerPreview.apk`, **24,297,481 bytes**, SHA-256 `c5545778b66fad01ba035896b6b7d169ba0df1b6bd0f359960eb7de529bfe576`.
- Inspected **the actual embedded** `res/drawable-nodpi-v4/<family>_*.png` entries, not generated substitutes or the design-plan list.
- Generated independent, all-family comparison sheets on cream and forest surfaces, including normal 120px comparison and smaller 48px/30px tests. These are review artifacts produced locally during the discussion, not committed into the game repo.
- The separate new `scripts/audit_collection_art.py` is prepared for future repo-root runs; this first inspection used a local extraction script, **not a claim that the checked-in tool or full final audit has passed CI**.

## Observed catalog on exact build
| Family | Actual PNGs in Farm APK | Final planned |
| --- | ---: | ---: |
| Woodland | 13 | 17 |
| Ocean | 7 | 11 |
| Garden | 9 | 9 |
| Farm | 6 | 6 |
| **Player Icons** | **35** | **43** |

- **35/35** PNGs are distinct by SHA-256 (no identical file bytes).
- **35/35** are RGBA and square; dimensions are 384×384 for established sprites and 512×512 for newer work. Mixed source resolution is intentional; compare normalized displayed size.
- No asset was empty. The 35 illustrated portraits appear compatible with the original cream/dark-green, simple-eyes, rounded-shape, restrained shading style when viewed side by side; this is an assistant **preliminary visual judgment**, not owner phone approval.
- Farm's Sheep, Pig, Pumpkin, Hay Bale visibly share the house dark-green outline and natural solid colors. Crop/objects are appropriately without humanoid faces.
- Garden's insects alongside flowers, produce and watering can remain thematically coherent. Sea Turtle has marine flippers and is visibly distinct from Woodland Tortoise.

## Follow-up findings (do not auto-correct canonical art)
- **P2 / inspect:** Legacy `woodland_rabbit.png` touches the top canvas border at 69 pixels over alpha threshold 12, unlike other portraits in this sample. Ears remain recognizable in comparison sheet, but inspect physical phone's avatar circular crop and 30dp thumbnail for any true ear-tip clipping. Prefer rendering padding first; altering an approved canonical sprite is higher-risk.
- **P2 / subjective size check:** `garden_potted_tulip` has a naturally narrow silhouette (alpha bbox width ~58% of its 512px canvas) and `farm_hay_bale` a naturally squat silhouette (alpha bbox height ~62%). Both look legible in 48px contact-sheet view. Do **not** force all subjects to fill an identical bounding box; just check perceived emphasis alongside peer tiles on actual Android screens.
- **Still unverified:** Full gallery scroll/equip and locked rows at 200% text, native device alpha fringe/outlined scale, original 50 IDs/save compatibility under a production update, actual two-phone LAN, icon v2 adoption, final complete 43-icon and 79-cosmetic run, board palettes and new number fonts.

## Recommendation
Keep current Farm candidate art provisionally as-is pending owner phone review; no mass redraw is justified by this pre-audit. Follow up the single rabbit edge contact as a low-risk display-crop verification. Add 4 Woodland + 4 Ocean, then repeat the combined 43-icon contact-sheet review and **full 79-cosmetic functional/visual audit** on the unified Beta 2 candidate. Do not mark final release ready.
