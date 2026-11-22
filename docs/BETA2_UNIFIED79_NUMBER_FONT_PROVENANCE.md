# GridMates Beta 2 — four bundled number fonts (79-cosmetic reviewer)

**Development status:** four added font resources and stable cosmetic IDs. The app has **nine font choices** in the 79-cosmetic review branch. The 4 new font IDs and unlock thresholds are `typewriter` 14, `editorial` 26, `soft_print` 45, `tall_print` 75 qualified puzzle completions. The five existing fonts and their original conditions are unchanged.

## Exact upstream source provenance

Official Google Fonts repository at immutable commit `06bfe0d62a98375cb09c942fa18e68bd32cff057`, **SIL Open Font License**. The following unchanged TTF binaries are vendored directly with matching Git blob SHAs; no runtime downloads, no machine-dependent fallback fonts.

| Cosmetic | Embedded resource | Official source path | Git blob SHA |
|---|---|---|---|
| Typewriter | `typewriter_courier_prime.ttf` | `ofl/courierprime/CourierPrime-Regular.ttf` | `2a0f8bde9778bb8c7b3a0ca5d4b4fb041737ef1a` |
| Editorial | `editorial_literata.ttf` | `ofl/literata/Literata[opsz,wght].ttf` | `0a81b4984a4a4eb101e0ddc28babc85bb137efef` |
| Soft Print | `soft_print_atkinson_next.ttf` | `ofl/atkinsonhyperlegiblenext/AtkinsonHyperlegibleNext[wght].ttf` | `92a357554bdcca62a7e80d5e3724bf4eb8932e5e` |
| Tall Print | `tall_print_oswald.ttf` | `ofl/oswald/Oswald[wght].ttf` | `d1a3b9cb1325bf20b3a06ef9849d21c411212a3b` |

The original full `OFL.txt` notices are stored under `docs/licenses/fonts/<family>/` **and included inside the APK** at `assets/licenses/fonts/<family>/OFL.txt`.

**Note:** Three TTFs are upstream variable fonts (Literata, Atkinson Hyperlegible Next, Oswald). Keep those source bytes unmodified to preserve naming and provenance; test glyph rendering on minSdk 26 and normal/large devices. Consider static font instances only after proper family/name/OFL review.

## Verification before owner approval

- [x] Exact original font SHA and license notice files committed. 
- [ ] Android build/lint/9-font JVM test and glyph layout tests run clean.
- [ ] Android screenshots of all four digit-1-to-9 previews verified as genuine, distinct PNGs.
- [ ] Compare nine fonts at 12sp pencil-note scale, 18sp/28sp numbers, all 11 boards and both light/dark high-contrast colors. Reject near-duplicates.
- [ ] Owner inspects real Android font appearance before public release.
- [ ] Co-op `uniformNumbers` and unknown-ID fallback tests, two phones LAN and signed install-over save retention.

**The fonts are only cosmetic.** They must never change puzzle difficulty, hint rules, scoring, or gameplay entitlement.

**Original artwork synchronized:** all eight original Ocean/Woodland PNGs are now included from the latest draft #105. CI validates their exact SHA-256s against the original review manifest; review-only SVG redraws are not used in this build.

**Release remains blocked:** owner phone approval, full 79-item cohesion audit, Versus V7, two-device LAN, and a production-signed Beta 1→Beta 2 save-preserving upgrade. Keep this branch in draft.
