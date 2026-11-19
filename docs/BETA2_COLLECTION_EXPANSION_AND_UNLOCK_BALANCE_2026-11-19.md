# GridMates: Sudoku — Beta 2 Expanded Collections & Unlock Balance

**Status:** Owner-approved *direction*; exact content and milestone thresholds recorded as implementation candidates. This PR is a **design and asset-production contract, not playable cosmetics yet**. Current canonical app source: `main` after merged Woodland V5.1 PR #92.

## 1. Why this should be a meaningful Beta 2 update

Beta 2 should offer returning players visible progression and recognizable new collections alongside the polished Woodland UI, not simply reskin menus. Preserve cozy/offline-first Sudoku, accessibility, no grind pressure, no account or cloud requirement, no gameplay bonuses from cosmetics, and a safe upgrade for existing family saves. Do **not** block future gameplay work on speculative economy/shop infrastructure.

## 2. Source-verified baseline

| Existing inventory | Count | Notes |
| --- | ---: | --- |
| Woodland portraits | 12 | Owl, Fox, Rabbit, Bear, Badger, Hedgehog, Robin, Deer, Squirrel, Otter, Tortoise, Cat |
| Ocean portraits | 6 | Octopus, Seal, Hermit Crab, Manta Ray, Jellyfish, Narwhal |
| Garden portraits | 4 | Honeybee, Ladybug, Butterfly, Dragonfly |
| Basic icons/emblems | 6 | Separate from animal portrait families |
| Boards | 7 | Classic, Linen, Midnight, Circuit, Parchment, Ocean, Graphite |
| Number inks | 10 | Forest, Cobalt, Amber, Rose, Violet, Lagoon, Clay, Slate, Sepia, Indigo |
| Number fonts | 5 | Clear, Bookish, Terminal, Rounded, Pencil; all **already bundled** as local files |
| **Total** | **50** | 22 portraits, 6 emblems, 7 boards, 10 inks, 5 fonts |

Verified in `core/src/main/kotlin/com/playereden/sudokutogether/core/Cosmetics.kt`, `app/src/main/java/com/playereden/sudokutogether/AnimalPortraits.kt`, `NumberAppearance.kt`, `CosmeticPalette.kt`, and the installed font-asset record. Earlier lists calling Squirrel, Pencil or Rounded future additions were stale; never duplicate current IDs.

The current unlock system counts distinct match IDs from Solo, Daily and Co-op solves plus Versus **wins** for FINISH milestones. RACE uses wins, COOP uses completed shared puzzles, SOLO uses solved Solo. Existing milestones range from early single-digit requirements to Ocean Narwhal at 75 completions. There are currently nine starter items total. The `ownedCosmeticIds` ledger is already implemented and backed up; new work must *use it*, not reimplement it.

## 3. Proposed Beta 2 content pack

**New animal portraits: 21** (15 additional characters in existing collections and 6 Farm characters in one entirely new family). Each is a separate transparent PNG in the established Collection portrait style, NOT a cropped image from the painterly gameplay banners.

| Family | Character (new stable ID suffix) | Qualified finishes required |
| --- | --- | ---: |
| Woodland | Chipmunk | 16 |
| Woodland | Raccoon | 24 |
| Woodland | Beaver | 39 |
| Woodland | Woodpecker | 56 |
| Woodland | Skunk | 90 |
| Ocean | Sea Turtle | 12 |
| Ocean | Seahorse | 30 |
| Ocean | Dolphin | 46 |
| Ocean | Sea Star (ID `ocean_starfish`) | 67 |
| Ocean | Clownfish | 95 |
| Garden | Snail | 11 |
| Garden | Caterpillar | 23 |
| Garden | Firefly | 42 |
| Garden | Grasshopper | 62 |
| Garden | Praying Mantis | 100 |
| **Farm (new)** | Cow | **Starter (0)** |
| Farm | Chicken | 7 |
| Farm | Sheep | 20 |
| Farm | Pig | 36 |
| Farm | Duck | 58 |
| Farm | Goat | 85 |

This leaves the **existing 22 portrait requirements unchanged**. There is one new starter (Cow), early Farm progress, unlocks spread across multiple families, and long-term cosmetic goals for someone already many puzzles into the game. No new Farm icon requires a Co-op opponent. Qualified finishes are the existing deduplicated FINISH rule, so the player gets progress through Solo, Daily, Co-op or Versus wins.

### New board styles: four

| ID | Name | Target | Visual notes |
| --- | --- | ---: | --- |
| `moss` | Moss | 16 finishes | Soft pale green paper with deep green grid lines, strong error red |
| `rosewood` | Rosewood | 28 finishes | Warm tan / muted brown paper, distinct from Parchment |
| `pebble` | Pebble | 48 finishes | Cool neutral light-gray board, not just another beige variant |
| `tidal` | Tidal | 70 finishes | Medium-deep coastal teal, high-contrast digits and clue outlines |

Must create **complete** `BoardPalette` entries, not just backgrounds. Screenshots/test matrices must cover small notes, given versus user digits, selected cell, related cell, partner/multiplayer cells, matching numbers, conflicts, proof outlines and accessible color contrast. Co-op already derives board priority from catalog; a newly registered board must appear in both preview and game.

### New **number font styles**: four

| ID | Display | Target | Required distinctiveness |
| --- | --- | ---: | --- |
| `typewriter` | Typewriter | 14 finishes | Subtle vintage mechanical serif/mono feel, distinct from Terminal |
| `editorial` | Editorial | 26 finishes | Strong literary serif, visibly different from Bookish |
| `soft_print` | Soft Print | 45 finishes | Friendly legible humanist print, unlike existing Rounded |
| `tall_print` | Tall Print | 75 finishes | Narrow, upright numerals with readable 1/7/9 and 6/8 |

All font files must be **bundled, proven redistributable and pinned**, with exact SHA-256 and notices. Check digits 123456789 in real 9×9 board, pencil notes, previews, and OEM variation on both phones. Do not ship a style if the actual numeric glyphs are indistinguishable; substitute another curated family before approving a permanent ID. **No external font files should be shared with the user.**

**New ink colors:** zero for this pack. Current 10 shades already provide enough choice; prioritizing 4 genuinely distinct font shapes and 4 complete board themes is better than adding another near-duplicate shade.

**Projected inventory when fully implemented:** 43 portraits + 6 emblems + 11 boards + 10 inks + 9 fonts = **79 cosmetic items** (29 new). The manifest `design/beta2-content-pack/expansion-candidates.json` is the sole proposed-ID and proposed-threshold list for this milestone.

## 4. Progression balance decision

**Preserve the current 50 items' unlock rules, exactly.** Established players have earned those through gameplay, and the catalog already unlocks ~50% of available items after 5 Solo solves; retroactively raising thresholds would be unfair. Rather than relock them or add grindy streak mechanics, distribute the 29 *new* items from starter to 100 qualified finishes. That makes veteran profiles eligible for already-met requirements automatically, while leaving additional targets to pursue.

- Fresh players immediately see Farm Cow and four existing animal starters; the new collection is approachable from first launch.
- Players with early completion counts encounter new artwork without taking away previous rewards.
- Long-term portraits and fonts provide optional, clearly labeled goals without changing Sudoku correctness, assists, score or hint access.
- Existing race/Co-op-exclusive cosmetics remain; this batch must not force two physical phones to unlock new material.
- Preserve monotonic ownership before pruning game records. A player who earned an item must retain it after saving, upgrading, backup/restore, or reducing recorded history.
- A veteran already beyond some new milestones legitimately receives those items upon update. That is a *feature*, not a balancing failure.

### Daily rewards: separate optional second slice, not a prerequisite

The current match record supports Daily, but the collection catalog does **not** have a verified milestone type for unique Daily dates. Before adding Daily-specific rewards, decide whether to count distinct `dailyDate` values, avoid retroactive multiple grants and test timezone/replay behavior. Prefer **fixed non-expiring milestones at 3/7/14 distinct Daily dates** over a wallet, shop, weekly expiry, or compulsory streak. Do not add a new currency, ads, gifts, or paid/random loot as part of the initial content pack.

## 5. Character design consistency: definitive art rule

The owner pointed out that V5.1 Versus characters have too much painted detail relative to Home, Co-op and Collection mascots. **Keep the painterly forest and two separate Sudoku boards but use the exact Fox and Owl Collection art identity**. The canonical character reference assets are `design/woodland/originals/fox.png` and `owl.png` with their actual Android `R.drawable.woodland_fox` and `woodland_owl` counterparts.

Do not simply paint simplified animals over the old painted animals. First create a clean **background-only Versus board scene** (same banner aspect, empty stumps/boards, clear left title area), then add real packaged Fox/Owl art as separately positioned native layers with Compose (or produce a composited reviewed raster from those exact source portraits). The selected method must prevent doubled characters and preserve safe title contrast/cropping on phone widths. Never render text, buttons, incorrect Sudoku grids as a fake gameplay screenshot, or race flags with distorted checker patterns into the image. The winner is whichever method yields the best visual consistency in **actual Android screenshot captures**, not the prettiest standalone concept.

## 6. Implementation slices (each independently QA-able)

1. **Art pilot:** 2 Farm (Cow, Chicken), 1 Woodland (Raccoon), 1 Ocean (Sea Turtle), 1 Garden (Firefly). Compare them side by side with existing icons at 30dp/48dp/72dp and verify consistent strokes, shapes, eye/mouth scale, local background transparency, 1:1 framing. Review 1 new Versus clean-background / canonical-mascot prototype. Do not prematurely register placeholder portraits in the live catalog.
2. **First playable portraits:** import approved icons and curated stable IDs, family mapping and grouping, light asset/provenance manifest, progress/completion tests, device screenshot. Avoid big bitmap assets, preserve old resources.
3. **Expand existing families:** finish 15 additional Woodland/Ocean/Garden portraits in reviewed batches; ensure no duplicate existing species/IDs. Farm 6 in same style, verify naming and filtering in Collection.
4. **Progression/balance:** implement/add newly approved milestone targets from machine manifest and profile backfill safely. Test a fresh profile and a profile with 0, 1, 5, 15, 40, 75 and 100 qualified finishes. Test offline, save reload, record dedup, permanently earned ledger and JSON backup/import.
5. **Board pack:** implement four distinct complete palettes and app+core coverage tests; low-end performance and contrast QA.
6. **Fonts:** first acquire/license-check four exact static font faces, then register fonts + app resource mapping and preview/real-board tests. Reject indistinguishable/illegible numbers. Existing five IDs must remain unchanged.
7. **V5.2 Versus mascots:** update painterly background with clean canonical Collection fox/owl art and prove no duplicate figures in native screenshot. Keep all other gamefront areas unchanged.
8. **Integrate/test release candidate:** verify full 79 IDs, portrait resource completeness, font licenses, all board palettes, visual screenshots, APK size and old-save compatibility. Test real LAN on two phones where possible. **Original production signing and save-preserving actual Beta 1 → Beta 2 install-over are independent release blockers.**

Work in small PRs from `main`; don't merge draft catalog IDs that have no validated renderer resources.

## 7. QA conditions and safeguards

- Unique IDs across all five cosmetic categories. Missing animal image or font family must cause a test failure, not fall back to a misleading generic image.
- Existing all-50 IDs, owners, requirements and currently equipped appearances must survive exact update. No profile data clearing, new package name on public update, signer rotation or network protocol changes.
- Current `Appearance` is a curated allowlist; reject arbitrary remote font files, URLs, colors and unknown portrait IDs. Older clients should use safe fallback for newer unsupported cosmetics.
- Preserve stable, deduplicated completion counts; no repeat Daily puzzle reward exploitation; no negative or disappearing owned cosmetics.
- Collection UX with 43 animal portraits: make long lists efficient (lazy sections or paging), keep category tabs scroll indicators, restore browsing position, check bottom navigation obscuring final rows and font-scale 200%.
- 9×9 board clarity takes priority over ornament. Avoid ads in a puzzle or features requiring online accounts.
- Final signed public Beta 2 update must use original exact production signing identity; the current isolated reviewer APK cannot update installed public Beta 1 or wife's saved game.

### Definition of done

**Not done by this planning PR:** no new cosmetics installed, no portraits/images added to APK, no reward economy changed, no Beta 2 publish. The plan is done when implementation teams can build/test each piece without duplicate IDs, contradictory unlock rules, or uncertainty about the player's saved progress.
