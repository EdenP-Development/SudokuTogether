# Beta 2 — Nov 22 owner playtest: Collection, Woodland, and meaningful unlocks

**Working draft:** [PR #110](https://github.com/PlayerEden/SudokuTogether/pull/110), stacked on [PR #108](https://github.com/PlayerEden/SudokuTogether/pull/108). No public Beta 1 or production update, no networking change. Full automation must pass at current head before distributing a reviewer.

## What actual owner phone screenshots showed
The app icon is acceptable for now. Ocean/Garden icons mostly feel coherent; Farm looks too sparse. Several later Woodland sprites appear smaller or stylistically inconsistent relative to Rabbit, Bear, Badger, etc. Solo gameplay looked readable and functional; large hint dialogs may deserve later UI polish. A player asked for more diverse unlocks and modest additions to Garden/Farm/Ocean.

### Collection chapter fix (already passed Android 16 on #108)
All available/equipped items now stay inside their named permanent gallery chapter, not in an unrelated available-items strip. A small progress summary and next goal remain. Five starter portraits now occupy their own chapter grids, explaining the previously missing first card in Ocean/Garden/Farm.

## Existing original Woodland artwork: measurable source-canvas mismatch

The current APK contains two source families:
- Most older Woodland PNGs are **384×384** with visible-alpha bounding extents covering around **76–92%** of the original canvas.
- Newly imported **512×512** Chipmunk, Beaver, Woodpecker, and Skunk originals have comparatively padded bounding rectangles covering **42.1–43.6%** of the original canvas.

All four PNGs are genuine originals and MUST NOT be replaced with an approximate SVG redraw. The 62dp Collection avatar currently uses identical `ContentScale.Fit` and inner 8%-padding on these visually unequal canvases. The newer characters thus appear noticeably smaller.

This PR adds a **render-only, reversible size adjustment**:
| Character | Render scale | Source alpha bounding area |
|---|---:|---:|
| Chipmunk | 1.30× | 43.3% |
| Beaver | 1.28× | 43.6% |
| Woodpecker | 1.26× | 42.1% |
| Skunk | 1.28× | 42.7% |

The factors are intentionally conservative to avoid cropping the Beaver and Skunk tails at the circular mask. All other 39 portrait IDs render at 1.00×. Unit tests enforce this; CI continues verifying all eight recent original PNG **byte hashes**. Artwork pixels, original image blobs, profile image IDs, and unlock ownership are **unchanged**.

A separate owner-conversation review ZIP contains proposed *new illustration candidates* of those four characters, plus a side-by-side image; **none of the newly drawn candidates is integrated**. If the owner still dislikes the actual art after resizing, refine/approve individual artwork intentionally rather than replacing all four blindly.

## Unlock diversity — original path always retained

Current 79-cosmetic catalog remains 79. **15 portraits now have two attainable routes:** the *existing* total completed puzzle threshold, OR a simple local accomplishment. No account, online store, currency, forced perfect-play, time zone streak, or paid reward.

| Portrait | Historical finish count | Optional challenge |
|---|---:|---|
| Garden Strawberry | 23 | Complete 2 different Daily dates |
| Ocean Seahorse | 30 | Complete 3 different Daily dates |
| Farm Pumpkin | 58 | Complete 4 different Daily dates |
| Garden Watering Can | 62 | Complete 5 different Daily dates |
| Ocean Seashell | 67 | Complete 7 different Daily dates |
| Garden Potted Tulip | 100 | Complete 10 different Daily dates |
| Farm Sheep | 20 | Solve 3 Medium solo puzzles |
| Woodland Beaver | 39 | Solve 5 Medium solo puzzles |
| Ocean Dolphin | 46 | Solve 7 Medium solo puzzles |
| Woodland Woodpecker | 56 | Solve 3 Hard solo puzzles |
| Farm Hay Bale | 85 | Solve 4 Hard solo puzzles |
| Ocean Clownfish | 95 | Solve 5 Hard solo puzzles |
| Farm Pig | 36 | Solve 4 solo puzzles without hints or checks |
| Garden Firefly | 42 | Solve 5 solo puzzles without hints or checks |
| Woodland Skunk | 90 | Solve 12 solo puzzles without hints or checks |

Progress bar and preview text report **both** routes. Daily goals count *different validated ISO local dates* from saved DAILY/SOLVED records; repeated result IDs, callbacks, or multiple records for a date don't multiply progress. Medium/Hard use actual solved SOLO difficulty; hints are allowed for these difficulty goals. No-hint goals exclude assistance, hints and solution checks; Daily/Co-op/Versus results don't satisfy them. All existing FINISH thresholds continue to count assisted Daily, Co-op and Versus wins exactly as before.

Previously earned `ownedCosmeticIds` continue to work after old records are pruned. `preserveEarned` now also persists ownership from an alternate goal. No completed records are deleted or changed. Unit tests cover original thresholds, owner retention, deduplication, legacy missing dates/difficulties, all five starters, and the unchanged total of 79 cosmetic IDs.

## Planned, not shipped

Up to seven new assets for future owner approval: Farm Goat / Duck / Barn; Garden Snail / Daisy / Mushroom; Ocean Sea Star. **No new IDs, unlock requirements or artwork for these are included in this branch.** Current Collection totals remain 17 Woodland + 11 Ocean + 9 Garden + 6 Farm = 43 portraits.

Further checks: view four normalized original icons at real 48/62/96dp on device, player Profile and Co-op/Versus; inspect candidate reillustrations separately; continue actual two-phone Wi-Fi testing, and validate a production-signed, save-preserving Beta 1→Beta 2 install-over using a backed-up device. Keep every PR draft until owner review.
