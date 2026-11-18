# GridMates — Nearby modes illustration V2 (2026-11-18)

**Owner feedback:** V1 Co-op/Versus generated scene art looked blurry, generic and visibly less coherent than the Woodland Home screen in actual phone screenshots. This revision intentionally retires the low-resolution complete-scene AI exports.

## Implementation

- Both multiplayer scenes now reuse `gm_home_chapter_woodland_v1.webp`, **the exact installed Home background**.
- Mode-specific artwork lives in **native Android vector resources**, not 320×130 WebPs:
  - `app/src/main/res/drawable/gm_mode_coop_symbol_v2.xml` — original hand-authored open book, blank guide grids, forest/gold pencils.
  - `app/src/main/res/drawable/gm_mode_versus_symbol_v2.xml` — mirrored blank puzzle cards and a centered gold trophy medallion.
- `NearbyModeBanner.kt` draws backdrop, then scale-independent vector, then a left-side text contrast scrim and real native labels.
- Both obsolete binary scene exports are **deleted** from the V2 branch. The retained original portrait PNGs are the only true Owl/Fox illustrations; neither banner invents another version of a collectible.
- No baked text, puzzle digits, fake scores, fake player status, or generated portraits.
- Android controls for difficulty, Host/Join, optional room-code and same-Wi-Fi LAN remain native and unchanged.
- `HomeLivingBookUiTest.kt` now also asserts there is a decorative mode-specific vector.
- At 200% font size, long decorative eyebrow labels remain hidden and the actual mode title and Host/Join remain reachable.

## Visual pilot evidence

Original whole-banner V2 compositional prototypes were locally drawn using the full-resolution Woodland landscape and editable vector-source icon drawings:
`GridMates_Mode_Refinement/mode_banners_phone_scale_comparison.png`.
This is a **review composition**, not a promised exact emulator screenshot. Android draws its editable vector assets over the installed same-background resource; the Android render is the next acceptance gate.

## Acceptance

1. The whole Co-op and Versus menu must be sharp and legible on an actual phone. Their scenic and color identity should match Home.
2. Recognize Co-op (shared book/pencils) vs Versus (two boards/trophy) from the right-side artwork even without reading the top title.
3. No distorted or regenerated Fox/Owl characters; no false ownership implications.
4. On 320–430dp phones and 100/200% font, title and buttons are not clipped; Host, Join, optional code, difficulty, mode switching and back remain usable.
5. Existing Solo, Daily, collections, save and multiplayer callbacks untouched; isolated reviewer app uses its own package ID.
6. Before production, owner should inspect emulator and phone screenshots. The public Beta1→Beta2 signing and actual same-package install-over preservation gate remain unresolved. Do not merge or publish this branch based only on screenshot tests.

## Other review notes

This is a targeted visual repair, not a new monetization, progression or settings project. Native vector motifs are intentional: crisp at any density, small in APK, reusable in result/collection cards later, and directly controllable in source for consistency.
