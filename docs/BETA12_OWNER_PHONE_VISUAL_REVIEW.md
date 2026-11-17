# GridMates Beta 1.2 — owner-phone screenshot review (November 17, 2026)

## Evidence boundary
Owner supplied screenshots of the isolated **Sudoku B1.2 Review** installation, source checkpoint `84db607`. They show the Home, first-use Profile, Collection available and locked character cards, a fresh Solo grid and a Solo grid with three entered digits and pencil marks. This is **a real-device presentation sample**, not a functional two-phone or saved-data upgrade regression. Screenshots are intentionally not copied into the public repository.

## Confirmed visual strengths
- The parchment-and-forest-green Home, Profile and Collection feel consistent rather than looking like separate experiments.
- The enlarged animal art retains character and readable names at standard phone scale.
- The responsive Collection actually displays three portrait columns on the owner device; category and family navigation are visible.
- Solo retains a large board, clear 3×3 boundaries, readable filled digits, an obvious selected square and bottom keypad.
- The owner successfully started a Solo puzzle, placed entries, and added at least some pencil marks in a brief test session.

## Findings, ranked by impact
1. **Duplicate feedback can incorrectly imply a fixed clue is wrong.** The screenshot shows red duplicate entered 6/9s and red fixed clue digits. Existing source marked any index from `Sudoku.conflicts` red indiscriminately, and the selected-square caption still said "Enter a number" even when the selection contained a duplicate. Repair in PR #64: distinguish fixed and entered cells visually and by accessibility, and give an inline duplicate explanation.
2. **Keypad mini-counts lack a visual legend.** New players see small 0–9 counts below large digits but no reason for them. Repair in PR #64: one quiet, readable explanation in number-entry mode; omit it when Notes mode hides the counts.
3. **Default profile is a bare diamond despite available starter animals.** Retain the current save/default logic, but point first-time players to the already available Owl, Fox, Octopus and Honeybee. Repair in PR #64.
4. **Solo has conspicuous vertical slack between board and input dock.** The centered text-only "New puzzle" action looks detached. This is a layout/style review candidate, not proof of a bug. Explore an improved contextual action placement **after** the current QA pass, without reducing board size or interfering with short/landscape/200% font layouts.
5. **Sparse pencil marks appear small on a real device.** Keep current tested font-fit safeguards; evaluate active notes under realistic densities and all five bundled fonts before increasing font sizing, as prior attempts caused measurable full-grid overflow.
6. **Locked cards are legible but repetitive below the fold.** Potential longer-term collection presentation: a more inviting achievements/unlock explanation or compact family-section exploration. Do not add currencies, unlock purchases or excessive badges.

## QA / release discipline
- PR #64 is a small source refinement covering findings #1–3 with focused UI tests.
- A passing emulator test does not make the physical screenshots automatically obsolete. Owner review of the revised APK remains a separate gate.
- Original QA/public app signers, data formats, core solver, rewards and LAN protocol remain untouched.
- The unified isolated preview is a separate installation and does not receive this change until a new explicitly versioned preview is built.
