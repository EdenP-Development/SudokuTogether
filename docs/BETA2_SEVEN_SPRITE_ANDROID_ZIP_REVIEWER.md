# GridMates Beta 2 — seven-sprite Android art-review app

**Draft-only.** This is an independently installed **Android ZIP art-reviewer**, NOT an approved cosmetics expansion. Source branch: `feat/beta2-art-zip-reviewer`, stacked on draft #113 / #110.

## What this tests
- See existing **original** Farm Cow/Chicken, Garden Honeybee/Sunflower and Ocean Octopus/Seahorse portraits drawn by the game's real `AnimalPortrait` composable.
- Choose the ZIP of the seven revised, unapproved candidate PNGs with Android's system document picker.
- Compare each candidate alongside two original portraits at **62dp**, and then see the candidate at **48 / 62 / 96dp**, with the same `CircleShape`, inner 8% padding, `GreenTint` and `ContentScale.Fit` used by the Collection.
- Gallery screen scrolls vertically through Goat, Duck, Barn, Snail, Daisy, Mushroom, Sea Star. All images remain in memory for this temporary review session.

## Download files (owner conversation, not committed to repository)
- **APK**: only once the dedicated Android 16 Actions run has succeeded; GitHub artifact name `GridMates-Beta2-Art-Zip-Review`.
- **Art input**: `GridMates_Beta2_ArtReview_Import.zip`, created from the previously reviewed individual style-matched 512×512 transparent PNGs. This *input ZIP* is distributed in the owner ChatGPT conversation, not bundled into the APK or pushed to GitHub. SHA-256 `b2e5be72e99cf3fccd4b316198f6e1e551dbf289f7f6a171665c80be7dac3b64`.
- The existing full art comparison ZIP remains an archival/review aid but is not needed in the picker.

### On-phone procedure
1. Download and install the isolated `GridMates-Beta2-Art-Zip-Review.apk` (after green CI), and download the art-input ZIP to the phone.
2. Launch **GridMates Art Review**, **not** the installed Beta 1 game. It has a separate package `com.playereden.sudokutogether.qa.beta2artzip`, and the ordinary gameplay launcher is disabled for this special build.
3. Tap **Load seven-sprite ZIP**, select `GridMates_Beta2_ArtReview_Import.zip` in the file picker. The QA app never extracts files to persistent storage; it reads seven verified PNGs in memory.
4. Scroll each themed item, compare two original characters beside the candidate, inspect framing and outline at 48/62/96 Android dp.
5. Note each art verdict; **Barn** fine door details and **Goat/Duck** proportions warrant extra attention. A candidate ZIP import is not approval to ship.

### Constraints and safety
- Candidate IDs do not exist in `Cosmetics.knownIds`. Real 79-item catalog, source originals, 15 extra unlock routes, networking and saves are untouched.
- ZIP is content-limited (12MB total compressed), entry-limited (50), and PNG-filename-whitelisted; seven required 512×512 PNGs must decode with transparency and transparent corners. No arbitrary ZIP entry paths written to disk.
- Do not mistake automation tests using **synthetic generated PNGs** for a device test with the owner's actual seven illustrations. The reviewer can display actual PNGs when the ZIP is selected on the phone.
- Before final Beta 2 merging, obtain original-vs-candidate owner visual approval, two-phone LAN test, and production-signed save-preserving Beta 1 upgrade verification.
