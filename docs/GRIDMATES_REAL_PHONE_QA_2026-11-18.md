# GridMates: Sudoku — Real phone visual QA (Nov 18, 2026)

## Evidence and context
Owner supplied seven real Android screenshots after the separately installed Forest Gamefront reviewer APK (source draft PR #85). These cover Home with help open; empty Solo board; Collection; Profile; Co-op; Versus; and clean Home.

**Important:** This is a separately installed QA application. Fresh player name, zero achievements and five available cosmetics **are not evidence that prior Beta 1 saves were erased**; this APK has a different application ID and its own sandbox. Production signer and real Beta 1 -> Beta 2 install-over migration remain unverified.

## Observations and choices

| Screen | Observed on phone | Priority | Recommendation |
|---|---|---|---|
| Home | Real forest with original Fox/Owl, readable Play CTA, proper Daily, mode entries; materially more cohesive than layered book panels | **Keep** | Do not redesign from scratch; finalize high-quality scenic master and safe cropping |
| Tutorial | Basic alert dialog is readable and offers Got it / Coach practice, but visually generic and tall | P2 | Later: concise branded instructional sheet; don't auto-open intrusive help without first-run requirements |
| Solo | Board is crisp, controls well separated; visible vacant space between board and keypad; keypad appears muted until a cell is selected | P2 | Preserve full-sized grid and accessible keypad; evaluate adaptive vertical spacing on tall phones, not decorative filler |
| Collection | Actual original 22 animal portraits, clear owned/locked sections and family tabs | **Keep** | Don't redraw existing mascots; style chapter boundaries carefully later |
| Profile | Player Journal matches green branding, yet statistics and milestone cards are still plain | P2 | Rework first completion/reward display before adding unrelated profile widgets |
| Co-op | Start/Join remain prominent; large blank area below a pale setup form; off room-code switch track looks too dark | **P1** | Add a compact illustrated mode header **in place of** title/subtitle; pale off-state track |
| Versus | Same setup emptiness as Co-op; distinct race mode already understandable | **P1** | Same art family, preserve exact LAN semantics and direct Host/Join |

## In-progress stacked draft PR #86

- Add `NearbyModeScene.kt` with the **real** bundled Woodland backdrop and canonical character PNGs (decorative, not fictional connected players). When actual equipped portrait belongs to Ocean/Garden, fall back to neutral backdrop until its proper scenic family exists.
- Replace only the textual Co-op/Versus title strip. Avoid a new stacked card, new pages or controls and avoid pushing Start/Join away on phones with enlarged text.
- Clarify off-state room-code switch; state/behavior unchanged.
- Add focused Android UI assertions for both mode scenes, room-code off semantics, and 320dp/200% font reachability.

## Further art and gameplay priorities (not part of this PR)

1. Original matching **Ocean and Garden** scenes at full production resolution; integrate only after consistent palette and crop review.
2. Home art master is presently a highly compressed review resource; export a final high-resolution master before public merge.
3. Improve the **puzzle-finish moment** from an ordinary result card to a restrained book-journal achievement stamp using actual earned rewards/records, not invented unlocks.
4. Improve optional tutorial visuals and Solo tall-screen whitespace only if doing so enhances usability.
5. Two-actual-phone LAN test, production signing, real install-over-save-preservation test remain required before Beta 2 publishing.

All PRs #80, #81, #82, #84, #85, #86 remain unmerged drafts pending owner review.
