# Beta 2 — cohesive Basic veteran insignia study (six original vectors)

**2026-11-23 · Status: SOURCE IMPLEMENTED IN ISOLATED REVIEWER; NOT ARTISTICALLY APPROVED OR RELEASED**

This branch stacks on the [Collection Art Bible (#122)](https://github.com/PlayerEden/SudokuTogether/pull/122). The current 85-cosmetic game has twelve Basic insignias, but the last six use independent Unicode symbols. The owner phone screenshots show a bright yellow Sunrise, thin Moonlight, Constellation/True North lookalikes, and Grid Master too similar to starter Gridkeeper. This study replaces **only their test-build visual representation**, not their IDs, requirements, permanent unlocks, or production app.

## Canonical palette and shared construction

All six candidates are *original editable Android vector drawable XML*, not generated raster sheets, third-party icons, or font glyphs.

- 96×96 vector viewport, transparent outside all forms.
- Unified forest-green `#214F3F` strokes and outer circular medallion rim.
- Restrained muted gold `#9A7641` and warm pale-gold `#F4E7C4` accents; no neon.
- A consistent rounded-line structure, silhouette clarity and proportional mass.
- Intentional **different main symbols**, not six slight changes to a star/diamond.
- Rendered by real Android `AnimalPortrait` at 48, 62, 96 and 116dp with source inset and CircleShape.

| Stable reward ID | User-facing name | Original glyph | New candidate concept | Why distinct |
|---|---|---|---|---|
| `constellation` | Constellation | ✦ | Interconnected constellation nodes and one characteristic guiding star | Network of stars, NOT a standalone True North sparkle |
| `sunrise` | Sunrise | ☀ | Half-sun rising above horizon with measured rays | Warm pale gold, not saturated cartoon yellow |
| `focus` | Focused Solver | ◉ | Magnifying-glass lens over a tiny Sudoku 3×3 motif | Focused inspection, not another bullseye |
| `compass` | True North | ✧ | Compass circle and bold north/south needle | Directional instrument, not Constellation's starfield |
| `moonlight` | Moonlight | ☾ | Heavier crescent plus small star details | Stronger presence at thumbnail size |
| `grid_master` | Grid Master | ❖ | Nine-tile Sudoku grid, highlighted solution and small crown | Advanced puzzle mastery, clearly unlike Gridkeeper's diamond |

### Exact source resource paths

- `app/src/main/res/drawable/gm_insignia_candidate_constellation.xml`
- `app/src/main/res/drawable/gm_insignia_candidate_sunrise.xml`
- `app/src/main/res/drawable/gm_insignia_candidate_focus.xml`
- `app/src/main/res/drawable/gm_insignia_candidate_compass.xml`
- `app/src/main/res/drawable/gm_insignia_candidate_moonlight.xml`
- `app/src/main/res/drawable/gm_insignia_candidate_grid_master.xml`

The source XML stays editable: color/shape/path refinements do not require regenerating external AI imagery.

## Gating: absolutely no accidental production switch

Compile special debug reviewer with **`-PgridmatesBeta2InsigniaPreview=true`**. This gives independent Android ID **`com.playereden.sudokutogether.qa.insignias`**. In this reviewer ONLY:

1. Six candidate emblems are displayed in the normal Collection gallery / profile / portrait component.
2. Tapping their Collection item opens a side-by-side **CURRENT / PROPOSED** panel using the literal existing glyph and the exact candidate vector.
3. All other original Basic, Woodland, Farm, Garden, Ocean, colors, themes and fonts remain unchanged.

Standard builds have `gridmates_insignia_preview_enabled=false` and the renderer continues using `emblemSymbol(id)`, not the prototypes. Nothing in this study adds/removes catalog IDs, changes `Cosmetics`, saved appearances, durable earned ownership, LAN compatibility or game rules.

### Acceptance and rejection

**Do not auto-approve this batch.** Verify on actual 48/62/96dp Android screenshots and ideally on a physical phone.

- Basic gallery as a whole must look cohesive with the original six glyph-like symbols, while new six deserve long-term milestone distinction.
- Each must be clearly distinguishable at a glance in a row of three, with matching optic weight and color.
- No lines vanish, no rings become too prominent, and no icon becomes a cramped scribble in the 62dp avatar.
- Compare Sunrise/Gold to muted cream/forest UI; Moonlight legibility; Constellation and True North distinct identity; Grid Master unmistakable vs Gridkeeper; Focused Solver reads as Sudoku inspection.
- Assess title wrapping at 320dp and enlarged fonts.
- Record PASS / TWEAK / REDRAW individually with owner screenshot evidence and final asset SHA. **Passing Android UI tests is not aesthetic approval.**
- If a concept fails, change only its independent vector XML, then rebuild the reviewer. Keep previous candidate for comparison if visually material.

### Technical QA

- CI: `.github/workflows/gridmates-beta2-insignias-android-review.yml`.
- Six-source palette, XML shape and distinct-resource checks; existing original art/font SHA audits and APK separate-signature checks.
- `InsigniaConceptMappingTest` ensures all six IDs exist, distinct drawables, and unchanged standard fallback glyphs.
- `InsigniaConceptUiTest` opens each of six unlocked/locked entries in an actual Android 16 emulator and checks side-by-side comparison.
- Capture live gallery + Constellation comparison screenshots alongside unchanged Co-op and Versus screenshot controls.
- Do not claim a verified APK until the new exact-head run is green.

## Relationship to other Beta 2 work

- [Collection Art Bible](../../docs/GRIDMATES_COLLECTION_ART_BIBLE.md) remains the authority for the style.
- [Woodpecker concept A](woodpecker-concept-a.svg) remains a separate design candidate (not implemented as PNG).
- [Seven Farm/Garden/Ocean candidates](https://github.com/PlayerEden/SudokuTogether/issues/112) remain unapproved and not in the 85 catalog.
- [Achievement album #120](https://github.com/PlayerEden/SudokuTogether/pull/120) has separately passed Android QA, but is not merged here.
- [Versus art issue #97](https://github.com/PlayerEden/SudokuTogether/issues/97) is still independent, and V8 visual acceptance is unresolved.
- Beta 1 owner data is never edited or replaced by this isolated reviewer.
