# GridMates Assets-First Home: Integration Notes (2026-11-18)

Status: **experimental draft PR #84, not public Beta 2 and not a finished art pass**.

## Concrete problem

The prior Home used `LivingBookIllustration.kt` for a Canvas scene with hardcoded sample Sudoku numbers and a different visual style from the 22 packaged original portrait assets. A collage generated for Asset Sprint 1 looked richer but its Fox/Owl characters, material detail and opaque icon cutouts were **not matched perfectly** to the actual Collection and were not production-ready.

## What this PR does now

- `HomeChapterArt.kt` uses the exact existing Collection drawables (Fox and Owl on the default profile; the user's actual selected portrait for a Woodland/Ocean/Garden appearance).
- `AnimalPortraits.kt` remains the sole lookup table from saved portrait ID to packaged drawable.
- Home uses **no sample Sudoku digits**, no fake streaks, no new collectible objects, and no invented current puzzle.
- The real Solo progress/status, Continue/New action, Daily history, local Co-op/Versus setup, and profile/achievement stores are untouched.
- A native, accessible light chapter stage provides visual breathing room. This is a bridge until the final reviewed background art exists, not another attempt to recreate elaborate artwork in Compose.

## Asset workflow: approved vs pending

**Proven real files in repository**: 22 transparent portrait PNGs in `app/src/main/res/drawable-nodpi`, accompanied by source-generation notes and family manifests.

**Prototype artwork produced separately, not yet committed**:
- Sprint 1: Woodland and Basic scene test crops, paper/cloth texture concept, four *opaque* mode cutouts.
- Sprint 2: cleaner coordinated Solo, Co-op, Versus, Daily line-art concepts; still opaque, not transparent production sprites.
- Do not publish or silently use these prototypes in an APK, and do not claim any generated fake game stats are accurate. Do not regenerate any of the established animal portraits.

## Next art gate

1. Inspect Home emulator and real phone, especially 320dp/200%-text, and make sure original portrait art and live menu actions read as one composition.
2. Create a clean **original background family** at 1536×768 (Basic and Woodland) with quiet margins, no text, no digits, no animals baked in. Their imagery must frame the exact shipped portrait illustrations rather than replace them.
3. Create four transparent original mode icons at 384×384 (Solo, Co-op, Versus, Daily) with clean alpha and no panel/button backing.
4. Add image-source provenance, asset hashes, safe crops and actual Android resource compression stats to `design/ui-art/v1/asset-manifest.json`.
5. Build a new Home design that starts with Play/Continue and Daily, using the art below Compose controls. Compare it to the owner screenshots and get approval before merging.

## Release guardrails

Draft only. Parent PR chain is #80 -> #81 -> #82 -> #84. Existing production signer and Beta 1->2 install-over preservation remain unresolved and must be tested separately. The isolated reviewer application ID must never be confused with the public install/update.
