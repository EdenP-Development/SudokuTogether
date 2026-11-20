# GridMates Beta 2 — Farm mixed-theme playable pilot

**Status: isolated Android reviewer, not approved for release.** This branch starts from Garden review [PR #98](https://github.com/PlayerEden/SudokuTogether/pull/98), which itself includes the first-five animal pilot. The separately approved-looking-but-unmerged Versus V7 [PR #99](https://github.com/PlayerEden/SudokuTogether/pull/99) is not a dependency of this Farm test branch.

## What changed

Four real, source-backed and hash-pinned RGBA PNG icons enter the existing Player Icons `portrait` catalog:

| Icon ID | Name | Type | Unlock goal |
| --- | --- | --- | ---: |
| `farm_sheep` | Sheep | animal | 20 qualified puzzle finishes |
| `farm_pig` | Pig | animal | 36 qualified puzzle finishes |
| `farm_pumpkin` | Pumpkin | produce | 58 qualified puzzle finishes |
| `farm_hay_bale` | Hay Bale | item | 85 qualified puzzle finishes |

Farm becomes six icons including the unchanged Cow starter and Chicken 7-finish reward. **No people/humans**, no arbitrary uploaded cosmetics, no cloud account or runtime art downloader.

- **Total reviewer catalog: 63 cosmetics:** 35 Player Icons, 6 emblems, 7 boards, 10 number inks, 5 fonts
- Garden's nine icons and the first-five animal IDs/requirements stay unchanged
- Existing de-duplicated finish counting, preview lock information, earned item ownership and retention logic are reused
- Source art tracked in [PR #100](https://github.com/PlayerEden/SudokuTogether/pull/100); exact drawables verified against their SHA-256 in `design/beta2-content-pack/farm-android-review-manifest.json`
- New `Beta2FarmThemeIconsTest.kt` checks 20/36/58/85 milestones, permanent unlocks after record pruning, duplicate-result resistance and safe unknown IDs
- `CollectionPolishUiTest.kt` checks new cards, locked previews, Farm filter selection and overall count changes

## Reviewer isolation and gates

Build with `-PgridmatesBeta2FarmPreview=true` to produce separate-install `com.playereden.sudokutogether.qa.beta2farm`. This is a debug preview that **cannot update public Sudoku or the Garden/V7 review apps** and does not share installed production saves.

1. ✅ Four new Farm drawables are real 512×512 RGBA images, exactly checked against [art import #44670749053](https://github.com/PlayerEden/SudokuTogether/actions/runs/44670749053).
2. ⏳ Android 16 connected tests, actual screenshot and APK hash/identity: [reviewer #44670915761](https://github.com/PlayerEden/SudokuTogether/actions/runs/44670915761)
3. ⏳ Review actual Farm cards at normal/small/200% text size and compare art with Cow/Chicken and Garden sprites
4. ⏳ Owner approval of final phone images
5. ⏳ Real two-phone LAN test and original-signer Beta 1→Beta 2 save preservation before public rollout

No production or main branch merges. Artwork is reusable in Word Search independently from Sudoku progress.
