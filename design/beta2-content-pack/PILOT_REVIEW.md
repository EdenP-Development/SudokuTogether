# GridMates Beta 2 — first five portrait art pilots

**Status:** Generated candidates for owner review; these images are intentionally stored under `design/`, **not** `app/src/main/res`. Nothing in this branch changes the current game, Collection, unlocks, builds, or saved progress.

## Pilot set

| Proposed cosmetic ID | Family | Character | Candidate 512×512 file |
| --- | --- | --- | --- |
| `farm_cow` | new Farm | Cow | `pilot-art/farm_cow_pilot.png` |
| `farm_chicken` | new Farm | Chicken | `pilot-art/farm_chicken_pilot.png` |
| `woodland_raccoon` | Woodland | Raccoon | `pilot-art/woodland_raccoon_pilot.png` |
| `ocean_sea_turtle` | Ocean | Sea Turtle | `pilot-art/ocean_sea_turtle_pilot.png` |
| `garden_firefly` | Garden | Firefly | `pilot-art/garden_firefly_pilot.png` |

These are derived from the source image `pilot-art/pilot_source_sheet.png`. File integrity is documented in [pilot-art-manifest.json](pilot-art-manifest.json), including original sprite sheet dimensions (2172×724), precise SHA-256 and exported individual 512×512 PNG hashes.

## What to review

Compare alongside the existing `woodland_fox`, `woodland_owl`, `ocean_octopus`, and `garden_honeybee` **in the actual Collection card circles** at 30dp / 48dp / 72dp. Match outline strength, facial proportions, eyes, minimal shading, character scale, framing, palette and contrast. Inspect transparency and feathered Firefly edge at small sizes; do not assume the initial generation is flawless.

Only after owner visual approval should each resource be copied or re-exported to `app/src/main/res/drawable-nodpi` and registered in both `Cosmetics.kt` and `AnimalPortraits.kt` with real milestone tests. The new Farm family needs an explicit `IconFamily.FARM` filter and `identityStyleName` mapping.

The proposed complete 29-item catalog, exact starter/finish targets, risk controls and QA gates are in [Beta 2 content specification PR #93](https://github.com/PlayerEden/SudokuTogether/pull/93).

## Important

- Every animal is currently **art-only**. Do not display it as unlocked, call it shipped, or include the images in the APK before reviewed.
- All earlier existing cosmetic IDs and requirements remain unchanged.
- The legacy Beta 1 → Beta 2 original-certificate upgrade/save-preservation gate remains open and must not be bypassed.
- No font assets, paid mechanics or network behavior are modified here.
