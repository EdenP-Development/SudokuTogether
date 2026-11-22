# GridMates — Versus V7 layout correction (Nov 19 phone review)

**Status: isolated reviewer, awaiting Android and owner phone review.** This replaces the rejected V6 art that overlapped the "Race to finish" heading and left mascots floating behind giant decorative boards. Source and tested Garden work are preserved in the existing stacked PRs.

## Concrete corrections

1. **Text-safe region:** real original forest darkens the left ~56% of the image. No main props or mascot pixels should intrude there; the Versus native title has been reduced slightly to 21sp while Co-op remains at 23sp. The title is still native Compose, not written into any bitmap.
2. **Gameplay-first hierarchy:** smaller, aligned stump-mounted boards toward the right side only. Two separate Sudoku boards are composed from **exact nine row/column cells and accurate 3×3 region boundaries**, without any actual puzzle digits, fake game progress, AI scribbles or checkered flags.
3. **Canonical identity:** the Fox and Owl are scaled from the game's actual original transparent Collection sprites, preserving their simple recognizable face/outline design. Their forearms/paws connect visually to the separate wooden boards; no floating portrait cutouts. The scenery retains the approved warm, detailed woodland style.
4. **Co-op and Home remain unchanged.** The Play screen retains its own existing Native title and native difficulty, room-code, start/join actions.
5. **Clean APK:** V6 was rejected by the user's phone screenshot and has been deleted from drawable resources and replaced with V7. Its historical diff is in prior commits on this PR. No old V6 resource gets included in this next review APK.

## Files and immutable provenance

- Android PNG: `app/src/main/res/drawable-nodpi/gm_mode_versus_woodland_v7_hd.png`
- 1774×887 forest source: `design/versus-v7/source/woodland_race_v7_empty.png`
- Original source/final SHA-256, sizes, no-text, exact grid requirements, separate install identity: `design/versus-v7/manifest.json`
- Native draw selector: `NearbyModeBanner.kt` (Versus V7 only)
- Isolated debug preview: `-PgridmatesVersusV7Preview=true`, Android package `com.playereden.sudokutogether.qa.versusv7`
- QA: `VersusV7ArtUiTest`, `HomeLivingBookUiTest`, `scripts/versus_v7_capture.py` and `gridmates-versus-v7-android-review.yml`.

The forest was AI-assisted; the final foreground animals are composited from the original approved image resources rather than regenerated. Board grid geometry and arm-placement/composition were corrected to remove the visually malformed V6 arrangement.

## Required before any release

- [ ] Android 16 Gradle, 5 targeted Compose UI tests, lint and full workflow passed
- [ ] Real emulator screenshot shows no "Race to finish" overlap and unobstructed character faces, in both standard and 320dp/200% cases
- [ ] Owner approves the V7 screenshot or phone APK
- [ ] Keep source/owners' original V5 Co-op PNG and all Garden unlock IDs unchanged
- [ ] Only merge the approved stack in dependency order (#95 first-five, #98 Garden, #99 repaired Versus)
- [ ] Original signer, existing local save continuity and two-phone LAN tests before a public Beta 2 update

**Separate reviewer only. No changes to Sudoku logic, unlock counts, LAN, saves, billing or original public signing.**
