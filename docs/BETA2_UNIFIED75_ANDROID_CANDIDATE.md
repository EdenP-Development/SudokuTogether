# GridMates: Sudoku — 75-cosmetic unified Android candidate

**STAGING / review only.** This branch combines Farm, Garden, five first portraits, four board materials and **eight new Ocean/Woodland portrait IDs**. It is built from the already Android-QA-passed 67-cosmetic board branch. No production merge or signed install-over has occurred.

### Catalog

- Woodland 17, Ocean 11, Garden 9, Farm 6 = **43 portraits**
- 6 emblems + 11 boards + 10 number inks + 5 fonts
- **75 cosmetics**, with 4 licensed number fonts still pending for the full 79 target
- Planned new icon unlocks: Chipmunk 16, Beaver 39, Woodpecker 56, Skunk 90, Seahorse 30, Dolphin 46, Seashell 67, Clownfish 95 qualified puzzle completions

### Artwork provenance and approval caveat
- Woodland SVG source graphics reproduce the previously generated editable source drawings.
- Ocean SVG source graphics are newly authored **review vector reconstructions**, not pixel-exact copies of the original PNGs in `GridMates_Beta2_Ocean_Woodland_8Icon_ReviewPack.zip`. This distinction is intentional: the repository's text connector cannot transfer the original PNG binaries directly. The original review ZIP remains the reference, not a claim that byte-for-byte integration has happened.
- CI renders eight 512-square transparent RGBA PNGs from these SVGs with CairoSVG/Pillow, checking alpha/margins/duplicate bytes. They are embedded in the **isolated** Android reviewer APK, without replacing the distributed game.
- Full collection cohesion review must compare the vector reconstructions against the original candidate PNGs and existing Fox/Owl/Octopus etc. If visible drift is unacceptable, replace Ocean vectors with the original approved PNGs before Beta 2.
- Do **not** claim these new sprites have already received owner phone approval.

### Reproduce a review build

```bash
python -m pip install Pillow==11.3.0 CairoSVG==2.8.2
python scripts/render_beta2_vector_icons.py
./gradlew :core:test :app:testDebugUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:assembleDebug -PgridmatesBeta2Unified75Preview=true --no-daemon --max-workers=2
```

Separate QA application ID: `com.playereden.sudokutogether.qa.beta2unified75`. CI runs focused Android 16 Collection UI checks for **all eight new portraits**, captures actual on-device PNGs into shared MediaStore and validates PNG signatures, dimensions and uniqueness. No extracted screenshots or checkboxes count as visual approvals until the actual image pixels are examined.

### Remaining hard release gates
1. Owner phone approval for new ocean/woodland icons, four new boards and Versus V7 design.
2. Four visually distinct OFL-bundled number fonts (check font provenance and tiny pencil-note glyph legibility).
3. Full **79-cosmetic** cross-collection contact-sheet and in-game art cohesion audit recorded in design PR #102.
4. Two physical Android phones for Co-op/Versus LAN.
5. Production-signed higher-version **Beta 1 → Beta 2 install-over** on backed-up device, preserving all existing saves/owned cosmetics/puzzles.
6. No public beta update until release blockers clear.

**Never merge or publish this staging branch as a production update.**
