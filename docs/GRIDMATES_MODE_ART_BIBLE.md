# GridMates: Sudoku — Mode Art Bible (Beta 2)

**Status:** binding visual QA specification for the three mode hero panels, subject to owner approval. Paired with [Beta 2 Owner Release Handoff](BETA2_OWNER_RELEASE_HANDOFF.md) and [Versus release blocker #97](https://github.com/PlayerEden/SudokuTogether/issues/97). Do not treat technically green images as visually accepted.

## Creative identity

**World:** cozy illuminated woodland, rich storybook tree/fern/mountain background with little gold and cream accents; modern Sudoku controls remain sharply readable. The UI uses warm cream, forest green, muted gold, rounded cards, and generous natural texture in the illustrations, not low-contrast administrative forms.

**Characters:** original GridMates Collection `woodland_fox.png` and `woodland_owl.png` are the *identity model*. Clear large face, rounded readable silhouette, dark green contour, limited stylized markings and friendly emotion. Scenic detail may be richer than character detail: **do not turn friendly iconic mascots into painterly realistic wildlife, tiny stick figures, disembodied busts, or unrelated generative animals.**

**Function:** all titles, mode labels, difficulty choices, room toggles, and Start/Join buttons stay **native Android Compose**. Absolutely no baked words or fake Sudoku numbers in hero imagery. If decorative Sudoku boards appear, 9×9 grid geometry and 3×3 sub-boxes must be correct.

## Mode references (source inspected 2026-11-23)

### Solo/Home — APPROVED reference; retain

File `app/src/main/java/com/playereden/sudokutogether/HomeChapterArt.kt`.

- Environment resource: `gm_woodland_storybook_hd_v4`.
- Dynamically overlays original `woodland_owl` and `woodland_fox`, or currently equipped character. Character presentation remains independently sharp and recognizable; no mascot baked into the landscape.
- Native headings: “YOUR PUZZLE CORNER” / “A quiet moment, one grid at a time,” with forest contrast overlay.
- Baseline normal-text stage: 153dp high; accessible large-text stage: 112dp.
- **Action:** do not redraw Solo to match weaker Co-op or Versus. Preserve it as the mascot-proportions benchmark.

### Co-op — KEEP scene; repair heading

File `NearbyModeBanner.kt`, image `gm_mode_coop_woodland_v5_hd.png`.

- Dedicated HD scene already includes both characters actively participating on a shared board. Characters *look good* to the owner; forest scene feels rich and cooperative.
- Real phone: native 23sp serif heading “Shared board” intrudes into the character/board area and becomes difficult to read. A subtle forest outline/shadow or left text plate plus safer text width is appropriate. **Test before deciding.**
- **Do not** darken the full art, redraw character pixels, or apply a generic banner-wide heavy overlay simply to rescue title contrast. Use local text styling and/or safe alignment. Keep “PLAY TOGETHER”, room code, Start/Join untouched.
- **Pass:** at normal and 200% font on narrow 320dp and common devices, no character face occluded by native text, heading fully legible, illustration still bright/charming.

### Versus — V7 REJECTED, V8 NECESSARY

`NearbyModeBanner.kt` uses `gm_mode_versus_woodland_v7_hd.png` from [#99](https://github.com/PlayerEden/SudokuTogether/pull/99) and [#108](https://github.com/PlayerEden/SudokuTogether/pull/108). Original forest source `design/versus-v7/source/woodland_race_v7_empty.png`; original final SHA-256 `b02fd7610576a7361326bf37f038f643bc9000322be6e0d452aeee32835231ea`.

**What went wrong in earlier iterations:**
- V5: attractive woodland and meaningful two-board composition, but more painterly fox/owl than canonical Gallery originals.
- V6: literal canonical sprite pixel imports but oversized boards, floating/disconnected truncated characters, and title overlapping characters/left board — explicitly rejected.
- V7: repaired title/board collision by pushing small characters/props to the far right, so Fox/Owl became **miniature, stiff and disconnected**. Passing Android tests didn't establish visual approval. Owner rejected V7 again in latest 23-phone-screenshot audit.

**V8 target:**
- Approved lush scene/backdrop and two discrete side-by-side *proper* Sudoku board stations / wooden stumps as narrative props.
- Fox and Owl noticeably larger relative to mode banner, faces immediately recognizable, natural competitive poses, expressive friendly rivalry. Hands/paws/wings plausibly interact with their own boards, not perched portrait cutouts.
- Mascots have original linework, expressions, shapes and palette; any newly posed variants require owner side-by-side comparison against real `woodland_fox.png` and `woodland_owl.png`.
- Boards are secondary to characters (do not block faces), each one 9×9 grid with 3×3 divisions. No checkered flag, no invented puzzle digits, no text burned into pixels.
- Reserve a real title-safe area without compressing all foreground action into a tiny right-hand corner. Try layout/sketch variations before an Android raster integration; if a slightly taller Versus stage is necessary, verify content below stays reachable on 320dp and enlarged text. Native “Race to finish” must not intersect fox/board.
- **No doubling:** when a raster contains animals, do not separately draw canonical fox/owl over it. If V8 uses an *empty backdrop + live sprite overlays*, choose the renderer path intentionally instead of leaving painted V7 animals underneath. Treat original asset SHA checks and target drawable replacement as explicit versioned work.

**Hard acceptance:**
1. Owner sees side-by-side **Solo, Co-op, new Versus** screenshots from same real-phone density, with source Gallery Fox/Owl comparison.
2. Character proportions are visually balanced and genuinely on-model, not merely same source pixels.
3. Titles fully legible at 320dp and 200% text; controls below reachable; no hidden room-code or Start/Join controls.
4. No duplicated characters, malformed boards, flags or text artifacts.
5. V8 binary/provenance/manifest pinned; original V5/V7 preserved for rollback; no puzzle, LAN or local progress changes.
6. Owner explicitly approves *the phone result*, not just the AI concept sheet or Android automation.

## Gallery portraits and reward identity

Canonical simple portraits: Fox/Owl, Bear/Raccoon, Cow/Chicken/Sheep/Pig, Honeybee/Ladybug/Butterfly, Octopus/Turtle/Seal. Not every original is perfect, but **keep the clean friendly outline language**. Later sprites (Beaver, Chipmunk, Woodpecker, Skunk; some Ocean and Garden art) have uneven visible occupancy/pose and need *perceptual* 48/62/96dp comparison instead of canvas-size assumptions. Do not silently replace a collectible's PNG merely because its alpha canvas differs. Review separately, and preserve IDs, rewards and earned ownership.

New six Basic insignias in #116 need a consistent designed set, not a mix of unrelated Unicode glyphs. Distinguish Constellation from True North; Grid Master from Gridkeeper; control Sunrise saturation and Moonlight thinness. Names must not split inside a word on mobile 3-column tiles.

Seven additional Farm/Garden/Ocean characters are subject to art approval in #112. Only transparent character-only 512×512 RGBA source PNGs, not white contact-sheet crops, would be eligible for import. Authored asset SHA, transparent bounds, original-collection match, icon-size and owner approval required. No auto-import.

## Asset pipeline and QA handoff

1. Inspect existing image resources and exact source hashes before proposing edits.
2. Create isolated visual design alternatives with no alterations to shipped mascot originals.
3. Render phone-size previews **using the actual banner crop and title overlay**, not only full-width concept art, at normal and large fonts.
4. Obtain owner choice before integrating binary art.
5. Use independent review package + Android instrumentation; validate bitmap resources and screenshots, not only presence of `R.drawable` IDs.
6. Compare gallery and three banners side-by-side; reject art that is visually inconsistent even if instrumentation passes.
7. Once art is approved, merge accepted assets into unified Beta 2 RC and perform real dual-phone LAN / signed save migration separately.

## Design decisions that must survive chat context loss

- Solo is intentionally scene + **separately rendered canonical portrait sprites** (confirmed source).
- Co-op is a **dedicated illustrated scene** and can look slightly more storybook than Solo while preserving character identity.
- Versus V7 **is not approved** despite its technical pass, and requires V8 with more present, on-model characters.
- Use richer environments and simple recognizable characters; do not remove good Solo art to match bad Versus.
- Co-op heading can be fixed through native typography/contrast/placement; avoid replacing good artwork just for text.


## RC3 owner verdict — November 24: V8-B rejected, dedicated V9 required

On the owner's physical phone the static canonical Fox/Owl busts above the two boards look pasted on. This explicitly rejects the V8-B experiment regardless of green Android tests. Do not rearrange the same portrait layers again. Use the **Co-op V5 dedicated illustration and interaction** as the composition benchmark. Characters must physically engage with separate in-scene boards (paws/wings, posture, expressive rivalry), preserve canonical visual style without copying Solo's static pose, and consider a different character pairing. Native heading remains separate. The existing scene, art manifests and all rejected versions are retained for reference only. V9 is not yet produced or integrated; current preview is still rejected V8-B until the real new scene exists.


### V9 import-ready candidate checkpoint

Dedicated Bear/Rabbit interaction artwork generated and its decorative board grids corrected in perspective. Pinned file metadata: [manifest](../design/versus-v9/manifest.json). Actual binary remains local to conversation pending upload. No new overlays; the integrated reviewer auto-selects the dedicated bitmap only when present. Owner approval, crop, original-source and APK hash checks still required. Keep old V8-B only as rejected comparison fallback.


### V9 binary integration resolved

The exact corrected Rabbit/Bear PNG is now in drawable-nodpi and pinned in the manifest, not an external attachment. The source and APK checks and the Android test must verify its presence. The integrated reviewer selects V9; old V8-B layers must not appear. No owner approval is inferred from technical success. For future binary assets, lossless base64 staging with numbered text lines, checksum verification and the GitHub binary blob API is a working transfer method; do not redraw an existing asset to solve an upload limitation.
