# Woodpecker portrait candidate A — front-facing Woodland study

**Status: DESIGN-ONLY, UNAPPROVED.** Draft concept only; **do not overwrite** `app/src/main/res/drawable-nodpi/woodland_woodpecker.png`.

## Why this is needed
Real owner phone screenshots showed the current Woodpecker has a narrow, side-facing full-body silhouette with less apparent mass than neighboring Rabbit, Bear, Chipmunk and Raccoon. This is an art-composition concern separate from the already-fixed `Woodpecke / r` text-wrapping bug.

Source image Git blob from current 85-cosmetic reviewer: `woodland_woodpecker.png` **`5d35c45e02dd2d1b10e97ecd69119810efc188c7`**.

## Concept A design decisions
- Front-facing, broad head and approachable expression analogous to the canonical Rabbit/Bear bust portraits.
- Keep the recognizably **woodpecker** red crest, dark forest-green body, cream cheek/belly, warm ochre beak and tucked wings.
- Round, simple massing; forest-green strokes; flat, matte coloring rather than gradients/painterly rendering.
- Transparent 512×512 SVG in `design/collection-art/woodpecker-concept-a.svg`, with no background, text, or UI frame.
- Candidate is a **human-authored vector layout proof**; **not** a replacement for the previously generated image with an RGB checkerboard baked into the background. Any RGB/checkerboard export is unsuitable as a game resource.

## Required before considering approval
1. Export a 512×512 transparent **RGBA PNG**, validate alpha and corners, source hash, safe margins, visual center and duplicate content with the existing script in draft #113.
2. Compare candidate in actual Android `AnimalPortrait` at 48, 62, 96dp, and 116dp modal. Compare with **actual** Rabbit/Bear and the original current Woodpecker. Notice whether simple crest/beak reads recognizably and the head fills a similar fraction of the circular badge.
3. Check whether candidate feels too large/heavy relative to Rabbit and Bear at 48dp. Adjust line thickness and crest/face if needed. Test clip on all sides.
4. Obtain **owner's explicit visual approval**. Otherwise mark TWEAK / REDRAW and iterate. No silent original-source replacement.
5. Only after approval create a separate Android-art branch with original preserved for rollback, hash manifest, a reviewer APK, and UI source-protection tests.

Related: [Collection Art Bible](../../docs/GRIDMATES_COLLECTION_ART_BIBLE.md), [issue #112](https://github.com/PlayerEden/SudokuTogether/issues/112), [mode art Bible](../../docs/GRIDMATES_MODE_ART_BIBLE.md).
