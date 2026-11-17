# GridMates Beta 1.2 — existing progression spotlight

## Why
The owner-phone Beta 1.2 preview has strong readable game screens, but first-use Profile offers little direction beyond "0 of 9 earned," while the Collection presents many repeated locked cards. These refinements use *existing* locally saved achievements/cosmetics to explain one practical next step without adding a virtual economy, new prizes, streak obligations or gameplay clutter.

## Implementation
- Profile: retains the existing achievements tally and View achievements navigation; adds a quiet `NEXT MILESTONE` preview with its existing title, earned-record progress and description. Uses `Achievements.progress(records)` directly; shows a completion message when all nine are earned.
- Collection: before the locked gallery, shows a compact `NEXT TO UNLOCK` card using the lowest remaining target in the currently selected category/filter. Tapping the card opens the same locked-item preview (and unlock requirement), not a new claim/purchase action.
- Existing 9×9 Solo/Co-op/Versus boards, solver, fixed clues, input dock spacing, Coach, saved records, cosmetic IDs and progress rules remain unchanged.
- The selected item changes naturally with saved records, icon families and Numbers/Boards tabs. No copy of Meowdoku UI or assets.

## QA required
- Regression for Profile first-use First Grid / after first Solo solve Steady Hand.
- Regression for Collection locked entry, real progress text and its existing preview.
- Re-run existing Profile/Collection navigation, 200% font and equipped-state Android emulator checks.
- Ensure new card remains legible on 320–430dp phones and large fonts; emulator is not owner visual approval.
- Remove temporary UI workflow after all checks pass.

## Integration checkpoint
- Predecessor [PR #64](https://github.com/PlayerEden/SudokuTogether/pull/64) merged into main as `41f8b60` after passing 3/3 API 36 screenshot-driven input-feedback regressions. This new PR is retargeted to `main` and does not modify the fixed-clue or keypad feedback logic.
- Profile/Collection instrumentation is required before merging this progression presentation. Do not claim physical-screen review is complete merely because the emulator tests pass.
