# GridMates Sudoku Beta 2 — integration order and release safety

**This is the coordination plan, not a command to merge.** All content work is in isolated draft PRs. Current public `main` and the spouse's Beta 1 package/save data are protected.

## Review branches and tested scopes

| PR | Scope | Stacked on | Gate |
| --- | --- | --- | --- |
| [#95](https://github.com/PlayerEden/SudokuTogether/pull/95) | First five animals (Cow, Chicken, Raccoon, Sea Turtle, Firefly) | `main` | Android 16 UI QA passed, owner art approval |
| [#98](https://github.com/PlayerEden/SudokuTogether/pull/98) | Garden: Sunflower, Strawberry, Watering Can, Potted Tulip | #95 | Android QA passed, owner art approval |
| [#99](https://github.com/PlayerEden/SudokuTogether/pull/99) | Versus V7 fox/owl canonical-style banner fix | #98 | Android QA/screenshot passed, owner phone review |
| [#100](https://github.com/PlayerEden/SudokuTogether/pull/100) | Original Farm source sheet + Sheep/Pig/Pumpkin/Hay Bale art | `main` | Art file hashes/crops verified; art-only |
| [#101](https://github.com/PlayerEden/SudokuTogether/pull/101) | Playable Farm: 4 source-pinned icons, 20/36/58/85 finishes | #98 (sibling of #99) | Android QA and owner review still pending |
| [#93](https://github.com/PlayerEden/SudokuTogether/pull/93) | 79-cosmetic roadmap, subject/style standards, offline cross-game asset library | `main` | Design docs, no runtime changes |

**Caution:** #99 and #101 are siblings, both based on #98. They are not a single combined APK yet. When preparing final integration, start from the same approved ancestor and carry forward both changes explicitly, then rerun the complete app, tests and screenshot QA. Do not tell the owner that a standalone V7 review APK includes Farm unless it actually does.

## Suggested merge path after owner visual approvals

1. Create one `release/beta2-integration` candidate branch from the verified `main` base. Keep original `main` unchanged until the eventual release is approved.
2. Integrate #95 first, then #98. Verify that original 50 cosmetics, existing numeric unlocks, profile backups and room/LAN protocol retain exact behavior.
3. Integrate approved Farm content from #101 (the **real** PNG byte hashes are pinned in #100), and the approved Versus V7-only changes from #99. Ensure there are no duplicated portrait IDs, new package-suffix flags or discarded approved artwork.
4. Complete the planned 16 remaining content items: four Woodland, four Ocean, four board themes and four font styles. Use licensed/redistributable font files, not unlicensed system-font lookalikes or copied fonts.
5. Test full save migration, owned-but-old games, locked previews, category filters, large text and 320dp screens, standalone multiplayer in two real same-Wi-Fi Android devices, friend/room-code flow, and updated results/awards.
6. **Only after** an original signing certificate match is proven: install-over an actual Beta 1 device containing real earned cosmetics/profile/history, confirm data survives. Do not sideload a differently signed QA APK in place of that installation.
7. Final release checklist: same package ID, monotonic versionCode, public release notes, asset/license manifest, no debug/reviewer suffix, no stale test-only flags, no advertisements unexpectedly introduced, no use of any user saved data outside app.

## Beta 2 counts and style boundaries

- Original 50 cosmetics = 22 animal Player Icons + 6 emblems + 7 boards + 10 number inks + 5 fonts.
- #95 adds five Player Icons; #98 adds four Garden; #101 adds four Farm = **63 cosmetics in the latest Farm-only reviewer** (not combined with V7).
- Full target **79** (16 further unlockables planned).
- All icon collections may include animals, flowers, plants, vegetables, crops and themed tools/objects, but **no people or humanoids**. Consistent `gridmates_chibi_v1` stroke/scale/simplification; no forced face for objects.
- Shared GridMates asset pack is a *development-time* source. Sudoku and Word Search remain offline-first and keep **independent unlock ownership**.
