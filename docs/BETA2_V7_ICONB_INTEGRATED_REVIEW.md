# Beta 2 + Versus V7 + Sudoku icon B integration

**Review branch only.** Built from `qa/beta2-79-style-compatibility` (complete 79-cosmetic catalog plus 990-configuration contrast/typography fixes). Source V7 design files imported **by their exact Git blob IDs** from draft PR #99; Co-op V5 image was not edited. The current Beta 1 launcher source is backed up at `design/launcher-icon-v2/source-beta1-*.xml`.

## Changes
- Versus only: original detailed 1774×887 V7 forest banner with canonical Fox/Owl and separate 9×9 prop boards, 21sp native heading, standalone Compose mode controls.
- Co-op remains on original V5 illustrated banner; no new LAN rules, puzzles, themes, ownership or save migration.
- Trial launcher icon **B / Last Move**: matching adaptive foreground + legacy vector + optional Android 13 themed monochrome mark. Entire 3×3 grid and 2/5/8 numerals are stable SVG-like vector paths, no font loading or baked text.
- Debug reviewer package `com.playereden.sudokutogether.qa.beta2v7`; cannot install over Beta 1 or existing 79/990 reviewer builds.

## Automated gates
1. Source and image SHA-256 pinning from the original V7 manifest; original icon backups, full adaptive/monochrome resource structure; 8 original Ocean/Woodland SHA-256s, four OFL number font provenance licenses.
2. Complete core+app JVM regressions (including all 990 board/ink/font combinations), lint, AndroidTest compilation and separately signed debug APK.
3. Android 16 connected Compose tests: Versus V7 banner and controls, Co-op V5 preservation, Home menus at 200% text, 79-cosmetic board/ink/font 9×9 text layouts.
4. Actual emulator screenshot capture of Versus and Co-op after reinstall to avoid app-private file loss. PNG validity/dimensions/uniqueness required.
5. Later owner phone approval of banner visual overlap and launcher icon at Android 24/36/48dp with circle/squircle/themed masks; two real phones LAN and production-signed Beta1→Beta2 save-preserving upgrade required before release.

**No automatic merge into public main.** Revisit the icon if the large center 5 or tiny check mark becomes unreadable at 24 pixels. Favor in-app usability and brand cohesion over decorative details.
