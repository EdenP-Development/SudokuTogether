# GridMates — Asset Sprint 03 / owner-phone QA (Nov 18, 2026)

## Evidence

Real owner screenshots from the standalone **Character Home** Android reviewer (PR #84) at 3:30 AM show the asset-backed Fox/Owl stage working, the existing Daily Puzzle active, Co-op and Versus setup reachable, and the original Collection and Player Journal intact.

The issues remain primarily **composition**, not a broken gameplay engine:
1. Home still uses a large dark-green card around an inset "Collection Spotlight" card. This creates a page-of-cards appearance and places promotional art ahead of the main Sudoku action.
2. The top-right "Solo" label inside an already-Solo hero is a no-op and costs important vertical room.
3. An in-progress Daily indicator is rendered as a tiny period in a nearly invisible circle. The date range rolls over the prior seven days, so "this week" is misleading.
4. Co-op and Versus forms are readable, but occupy only the upper half of a blank screen. A subtle optional illustrated background could connect these modes to the Home art; do **not** pad with irrelevant new controls or make Host/Join harder to reach.
5. The existing illustrated animal assets are strong and should be the unmodified sprites at all times.

## Applied in draft PR #85

- Remove the redundant Solo header action; preserve the actual Play/Continue, Daily, difficulty and room controls.
- Render an in-progress day with a clear gold outlined circle and gold center, not a tiny dot glyph; the completed and untouched states remain unchanged.
- Rename the history label to "past 7 days" to match the source calculation.
- Add Android UI assertion for that label.
- No save format, owned item, network protocol or public signer changes.

## Companion art review material — NOT in Android yet

The offline Sprint 03 review pack contains:
- `gm_home_chapter_woodland_v1_candidate.webp`, original illustrated environment, 1536 × 768, approximately 77 KB, no characters/text/buttons/digits baked in.
- `GridMates_Menu_Exploration_CONCEPT_ONLY.png` — a broad visual-direction exercise showing illustrated framing, not a producible screen or reliable gameplay values.
- `GridMates_Current_vs_Visual_Direction.png` — actual owner screenshot alongside concept, clearly labeled.
- Source hashes and dimensions in `asset-manifest.json`.

**Do not interpret the concept image as an approved game asset.** Its Fox/Owl artwork differs from the established portrait PNGs, and the Sudoku numbers, counters, and puzzle status shown there are invented artwork.

## Next engineering change, after backdrop import

1. Import the reviewed Woodland WebP into `app/src/main/res/drawable-nodpi/gm_home_chapter_woodland_v1.webp`, with source/provenance and SHA-256 in the art manifest. A separate Basic variant must also be made to avoid incorrectly assigning a forest collectible identity to every user.
2. Replace the **entire nested illustrated card** with a single responsive chapter stage: backdrop behind UI, original Fox/Owl portraits (or genuinely selected portrait) on top, readable native Compose title and real Continue/New action.
3. Reduce redundant headings: the word "Collection" should not dominate the main game CTA. Any chapter spotlight belongs after the Daily ribbon as a short teaser, with a real Collection destination.
4. Keep Daily visible quickly without a long scroll on common phone sizes; keep same-Wi-Fi Co-op/Versus setup direct.
5. Add original small mode artwork with clean alpha, verifying actual 48–80dp display and TalkBack semantic controls. Don't create extra menu destinations.
6. Test 320–430dp and 200% font scaling, Daily status distinctions, resume progress, selected portrait, host/join actions, and original app save preservation. Verify with actual APK screenshot and owner review.

## Release safety

The current branch is stacked on #84 -> #82 -> #81 -> #80. All remain draft. Nothing is merged into the public app. Original production Beta 1 -> Beta 2 signing and install-over save-preservation remain separate release gates.
