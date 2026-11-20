# GridMates Beta 2 — four new number-font candidates (research checkpoint, 2026-11-20)

**Status: licensing/source shortlist only. No font binaries downloaded, embedded, or approved.** The current app has five working bundled number-font IDs: Clear, Bookish, Terminal, Rounded and Pencil. Future additions must not be renamed copies of those existing resources.

## Candidate matrix

| Proposed cosmetic ID | Player-facing name | Candidate face | Reference source | Licensing verified from official Google Fonts metadata |
| --- | --- | --- | --- | --- |
| `typewriter` | Typewriter | Courier Prime Regular | https://github.com/google/fonts/blob/main/ofl/courierprime/METADATA.pb | OFL; monospaced text/serif; static `CourierPrime-Regular.ttf` is listed |
| `editorial` | Editorial | Literata Regular | https://github.com/google/fonts/blob/main/ofl/literata/METADATA.pb | OFL; optical-size and weight variable; verify a legal fixed-instance static export |
| `soft_print` | Soft Print | Atkinson Hyperlegible Next Regular | https://github.com/google/fonts/blob/main/ofl/atkinsonhyperlegiblenext/METADATA.pb | OFL; variable weight; verify a fixed-instance static export |
| `tall_print` | Tall Print | Oswald Regular | https://github.com/google/fonts/blob/main/ofl/oswald/METADATA.pb | OFL; tall narrow variable weight; verify a fixed-instance static export |

All four upstream Google Fonts metadata files explicitly report `license: "OFL"`, as checked on November 20, 2026. **This does not by itself confirm the exact app-ready binary has been inspected, licensed correctly, legally bundled and QA'd.** Retain the OFL notice/copyright text with any future app distribution, plus source/commit/checksum. Prefer static TTFs on Android for reproducible dimensions and to avoid relying on system font fallbacks. Never distribute loose font files in chat.

## Important distinctness risks

- Courier Prime vs the existing Liberation Mono/Terminal: the numerals must look **genuinely different at 18sp and in pencil notes**. If not, pick a different mechanical typewriter font rather than adding a name-only duplicate.
- Literata vs existing Noto Serif/Bookish: compare numeral construction, stroke contrast and spacing at authentic Sudoku grid sizes, not a marketing screenshot of words.
- Atkinson Hyperlegible Next vs Clear/Noto Sans and Nunito Rounded: retain the accessibility advantages without duplicating the established look.
- Oswald's tall and condensed figures may have narrow counters and high top/bottom extents; check 3/8, 1/7, 4/9 at high zoom and actual 9×9 cell constraints.

## Required next checks before adding a single catalog ID

1. Examine exact static regular faces and their glyphs 1–9, 0 and pencil mini-glyphs; record raster screenshots at 12sp, 18sp and 28sp on light and dark boards.
2. Contrast against all existing five fonts, especially the close neighbors above, and discard any duplicate-looking candidate.
3. Verify upstream license notices and exact committed binary SHA-256. Bundle with source and OFL text, never a runtime web font.
4. Profile memory/APK growth; verify rendering identically on two Android phones at normal and 200% text settings.
5. Only after explicit visual review, register the stable four IDs with their proposed completed-puzzle unlocks: Typewriter 14, Editorial 26, Soft Print 45, Tall Print 75.
6. Test actual Co-op authorship, `uniformNumbers` override, backup/restore, old-client fallback, board palette compatibility and eventual full 79-item audit.

**No public Beta 2 release and no production signing, save, gameplay, or multiplayer logic changed by this research note.**
