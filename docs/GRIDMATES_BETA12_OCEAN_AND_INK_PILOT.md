# GridMates: Sudoku — Beta 1.2 Ocean and number-color pilot

**Status:** Provisional Android source pilot; not owner-approved final art, not a release or an APK.

## Scope

- **Ocean board**: the original sea-glass palette from the [Beta 1.2 theme pilot review](GRIDMATES_BETA12_THEME_PILOT_REVIEW_2026-11-16.md), with full grid, selection, partner, matching, conflict, Coach proof and Collection preview tokens. Proposed unlock: **12 Solo puzzles solved**.
- **Sepia number ink**: light `#76513D`, dark `#E9C29D`; proposed unlock: **6 Solo puzzles solved**.
- **Indigo number ink**: light `#3D4FA0`, dark `#C1CEFF`; proposed unlock: **14 qualifying completions** (Solo/Daily/Co-op solved or Versus won).
- The original four boards, Parchment and Graphite, and all eight older ink palettes retain their exact identifiers, colors and unlock requirements.
- All three newcomers have registered, curated IDs. Both inks and Ocean are earnable without online service, account or another phone. Co-op board priority remains catalog-based.
- No changes to Sudoku rules, saving schema, puzzle grading, Coach, multiplayer messages, monetary systems, fonts, portraits or installed APKs.

## Risk and verification gates

- Automated core tests must check the new milestone boundaries, unknown-ID fallback, saved appearance validation, and existing shared-board order.
- Android tests must verify **all ten inks** on Ocean, Parchment and Graphite's normal and highlighted cell surfaces at >=4.5:1 and the proposed Sepia/Indigo inks on *every* registered board's major surfaces at >=4.5:1. Legacy older inks on legacy board highlights are guarded at their existing >=4.0:1 floor, not falsely described as a new accessibility certification.
- Targeted Collection Android UI test checks narrow-phone locked preview and unlock/equip behavior for the new board pilot.
- Confirm on a physical phone that Ocean is visually distinct from Linen and Parchment, Sepia from Clay/Amber, and Indigo from Cobalt/Violet. Review pencil marks, notes, conflicts, selected/partner/proof cells, and large system text before owner approval.
- A new APK is **not** delivered as part of this change. In-place QA upgrading remains blocked until the original QA signing key is restored. Two-phone checks remain deferred and are **not** claimed to pass.

**Final release approval:** pending owner visual review. Track in [Beta 1.2 issue #50](https://github.com/PlayerEden/SudokuTogether/issues/50).
