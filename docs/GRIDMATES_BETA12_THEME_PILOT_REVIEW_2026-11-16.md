# GridMates: Sudoku — Beta 1.2 board and digit pilot review

**Prepared:** November 16–17, 2026  
**Status (November 17, 2026):** **Parchment and Graphite are implemented in Android source as provisional Beta 1.2 board options** in [merged PR #53](https://github.com/PlayerEden/SudokuTogether/pull/53). The original four board skins are unchanged. Ocean, Sepia/Indigo inks, and Typewriter/Editorial font ideas remain concepts. Owner *physical* visual approval and a new APK are still pending.

**Foundation:** [PR #49](https://github.com/PlayerEden/SudokuTogether/pull/49) stabilized milestone calculations and shared Co-op board priority; [PR #51](https://github.com/PlayerEden/SudokuTogether/pull/51) centralized board background/darkness and ink palettes; [PR #52](https://github.com/PlayerEden/SudokuTogether/pull/52) centralized *all current board cell-state display tokens* in `CosmeticPalette.kt`, preserving the original colors in gameplay and Collection. [Android core/Kotlin/unit CI for #52](https://github.com/PlayerEden/SudokuTogether/actions/runs/44008639563) passed. No APK was built in this batch.

A standalone interactive HTML preview, titled **GridMates Beta12 Board Concepts**, was generated in the planning chat for owner review. It compares the same sample 9×9 board in the three candidate palettes, with live ink selection, normal/selected/partner/hint/error states and **browser-only** font approximations. It is *not* an Android implementation, packaged game, source of gameplay logic or test of mobile installation. The palette values below are the source of truth for the Android Parchment/Graphite pilot and remain the specification for the unimplemented Ocean candidate.


### Android pilot verification — source only

[PR #53](https://github.com/PlayerEden/SudokuTogether/pull/53) merged as [`a1bd649`](https://github.com/PlayerEden/SudokuTogether/commit/a1bd649307557b1e1a5075b0572465fc891c1ec7). It registers Parchment and Graphite in the curated cosmetic catalog, with full 9×9 board palettes, Collection previews, offline milestones and saved appearance allowlisting. It preserves the four existing boards and old unlocks. [GitHub Actions run 44010252182](https://github.com/PlayerEden/SudokuTogether/actions/runs/44010252182) **passed** core tests, Android compile/unit tests and **1/1 targeted Collection emulator tests** for locked views and equip controls at a phone width. The temporary test workflow was deleted before merge.

**Not verified:** no new Beta 1.2 APK, owner screenshot, full 9×9 physical gameplay with these styles, long text/contrast accessibility on actual devices, new-vs-old app LAN compatibility, or same-signature QA install-over. Physical two-phone regression remains deferred. The original QA signing key is still missing.

## Board color tokens (Parchment and Graphite now registered; Ocean remains a concept)

| Token | Parchment | Ocean | Graphite |
| --- | --- | --- | --- |
| Board fill | `#FCF4E4` | `#F1F8FA` | `#293137` |
| Selected / proof target | `#FFF9EF` | `#F9FCFF` | `#3F4748` |
| Partner / proof source | `#F5FAF5` | `#F6FCFA` | `#34454B` |
| Related row/column/box | `#FAF6ED` | `#F6FBFC` | `#303B43` |
| Matching digit cell | `#FCF9F1` | `#FAFCFB` | `#404148` |
| Given/clue number | `#3E352B` | `#263F49` | `#F3F1E8` |
| Thin grid rule | `#C9BAA3` | `#B3CFD2` | `#58646B` |
| Thick 3×3 rule | `#6A5648` | `#346F7D` | `#A9B7BC` |
| Conflict cell | `#FFF0EA` | `#FFF2EB` | `#603B39` |
| Conflict number | `#8D362B` | `#963B34` | `#FFC0BA` |
| Pencil-note hint | `#5E6257` | `#52747C` | `#BBCDE8` |
| Proof target border | `#775218` | `#8B5E26` | `#FFE6A8` |
| Dark theme? | No | No | Yes |
| Visual intent | Warm pencil-and-paper paperback | Light sea-glass coastal board | Neutral dark charcoal, distinct from blue-teal Midnight and Circuit |

### Calculated preliminary contrast

These ratios use WCAG relative luminance for the **eight existing** paired light/dark number ink hex values and *all* proposed normal, selected, partner, related and matching-digit surfaces. Measurements do not include the browser's typography or Android text rasterization.

| Proposed board | Worst digit-to-cell contrast (all existing inks) | Ink/state producing minimum | Given-to-fill | Conflict digit-to-conflict fill |
| --- | ---: | --- | ---: | ---: |
| Parchment | **4.66:1** | Rose / normal fill | 10.98:1 | 7.01:1 |
| Ocean | **4.74:1** | Rose / normal fill | 10.34:1 | 6.44:1 |
| Graphite | **5.24:1** | Rose / selected fill | 11.68:1 | 6.19:1 |

All exceed **4.5:1 for the checked number/board combinations**, but that alone does **not** establish full accessibility, contrast for every minor UI label, large-text layout safety, colorblind distinction or a passing device screenshot comparison. Existing shipped highlights in Beta 1.1 sometimes approach 4.07:1; the new [BoardPaletteStateTest](https://github.com/PlayerEden/SudokuTogether/blob/main/app/src/test/java/com/playereden/sudokutogether/BoardPaletteStateTest.kt) intentionally guards that legacy baseline at 4.0:1 rather than silently changing established skins. The new-content gate should remain 4.5:1.

### Two possible additional inks (still concepts)

| Candidate name | Proposed light-board digit | Proposed dark-board digit | Worst ratio across the three proposed themes / five main cell surfaces |
| --- | --- | --- | ---: |
| Sepia | `#76513D` | `#E9C29D` | **5.74:1** |
| Indigo | `#3D4FA0` | `#C1CEFF` | **6.14:1** |

These remain suggestions. Check that Sepia is distinguishable from existing Clay/Amber, and Indigo from existing Cobalt/Violet, before adding catalog IDs or unlock milestones.

## Font pilots — not yet selected assets

- **Typewriter**: a mechanical print-like style with distinctive 1/4/7/9, not merely the existing bundled Liberation Mono (`mono`/Terminal).
- **Editorial**: a readable print/editorial style with clear digit differentiation, not merely the existing bundled Noto Serif (`serif`/Bookish).
- The HTML demo uses browser `monospace` / `Georgia` **only as sketches**, not fixed licensed Android typefaces. They vary by device and are **not** evidence that a candidate works in Android.
- The game **already ships** Pencil (Patrick Hand) and Rounded (Nunito); don't reproduce them. Before adding any new font, source a redistributable static face, document the original license, source/version/hash and copyright notice, bundle it in `res/font`, and test `123456789` for entry digits, notes, compact cells, both light/dark boards and 200% system text. Current ID ownership/backups and LAN appearance allowlists must remain compatible.

## Owner review questions / go-no-go criteria

1. **Parchment and Graphite were provisionally selected for the first Android pilot** in PR #53, with Ocean reserved for a later pass. Review the actual on-device appearance before finalizing them; if either feels too similar to an existing board, revise it rather than inflating the catalog.
2. Decide whether Sepia/Indigo are distinctive enough; otherwise propose more original colors instead.
3. Inspect an actual bundled font sample with all nine digits before selecting any final typeface. Typewriter/Editorial are categories, not approved font binary or unlock.
4. Review provisional **Parchment: 8 Solo solves** and **Graphite: 20 completed puzzles** requirements; both are available through Solo play. These thresholds are implemented in the PR #53 source but remain adjustable before the next release. Cosmetics must not affect puzzle rules, Coach, saves, rank or network fairness; Co-op boards use catalog priority.
5. Run full 9×9 Android UI previews on a narrow phone, selection/conflict/partner/notes highlights, normal/large text, color contrast, save/equip/restore, and peer appearance before producing an updated QA candidate. Physical two-phone tests remain deferred and cannot be inferred from preview.

**The actual published/installed Beta 1.1 preview is unchanged.** The true QA install-over path still requires its **original** signing identity; never replace or bypass the signing baseline. Track approval and remaining implementation in [issue #50](https://github.com/PlayerEden/SudokuTogether/issues/50), and pending release QA in [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43).
