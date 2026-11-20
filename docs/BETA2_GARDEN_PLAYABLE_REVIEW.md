# GridMates Beta 2 — Garden themed Player Icons, playable pilot

**Status: isolated Android preview, not merged into main.** Stacked on first-five portrait [PR #95](https://github.com/PlayerEden/SudokuTogether/pull/95), not a public Beta 2 update.

## Owner-approved Collection identity

The collection is a themed set, not a list of only animals. Garden should evoke blooms, harvest, useful gardening things and friendly bugs. Player icons need not be humanoid or even have faces. **No humans or humanlike portraits.** All use the established simplified chibi/forest-green-outline style from actual Fox/Owl/Honeybee artwork.

## Art resources and exact catalog contract

| ID | Name | Type | Qualified finishes |
| --- | --- | --- | ---: |
| garden_sunflower | Sunflower | Plant | 11 |
| garden_strawberry | Strawberry | Produce | 23 |
| garden_watering_can | Watering Can | Item | 62 |
| garden_potted_tulip | Potted Tulip | Plant | 100 |

The original source sprites are in [draft art PR #96](https://github.com/PlayerEden/SudokuTogether/pull/96). Exactly the same RGBA 512×512 bytes are copied to Android drawable-nodpi on this branch; hashes and sizes are in `design/beta2-content-pack/garden-android-review-manifest.json`. The new source reference sheet uses actual existing Garden sprites at 30dp, 48dp and 72dp.

**Still present:** existing Garden Honeybee/Ladybug/Butterfly/Dragonfly, Beta 2 first-wave Firefly, the other 26 animal icons, all old accessories. This makes Garden 9 icons, Player Icon portraits 31, basic emblems 6 and complete cosmetic count **59** (31 + 6 + 7 boards + 10 inks + 5 fonts).

## Progress/save compatibility

The existing `Cosmetics.kt` milestone algorithm is reused (deduped completed games, Solo/Daily/Co-op/Versus wins). No new grant currency/shop/ads/system. The new IDs are curated and safe in the existing `Appearance.portrait` field, which already uses an allowlist and safe fallback for unknown IDs. `ownedCosmeticIds` retains earned ownership after older records are pruned and through existing local backup. Existing milestones and already-earned item IDs are *unchanged*.

New `Beta2GardenThemeIconsTest.kt` exercises exact thresholds, unlock gating, duplicate game records, permanently earned ownership and unknown remote icons. Android `CollectionPolishUiTest.kt` now browses all four in the Garden filter and verifies locked-preview targets.

## Isolated preview build

Use `-PgridmatesBeta2GardenPreview=true` with Android debug packaging. Identity is `com.playereden.sudokutogether.qa.beta2garden`; separate from production, the prior first-five preview and V5.1 reviewer, so it does not update or erase anyone's installed saves.

[Actual Android 16 reviewer workflow](https://github.com/PlayerEden/SudokuTogether/actions/runs/44656229437) handles art checksums, JVM tests, lint, build, install, screenshots and UI instrumentation. **Do not mark ready until completed successfully**. The APK and captures are recorded as short-lived workflow artifacts, not a final public release.

## Release gates still open

- Real owner phone evaluation of Garden's visual consistency, tint, scale and transparency
- [Versus Fox/Owl detailed-style mismatch #97](https://github.com/PlayerEden/SudokuTogether/issues/97)
- Remaining Farm crop, Ocean seashell and Woodland expansions
- Four planned BoardPalette themes and four distinct redistributed licensed number fonts
- Two-phone same-Wi-Fi multiplayer regression
- Original-signer, save-preserving Beta 1 → Beta 2 update test on existing user data

Keep `main`, release-signing secrets, networking and game rules untouched until the separately reviewed content has passed. No forced user action required for internal prototyping.
