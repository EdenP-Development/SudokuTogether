# GridMates Beta 2 — Owner feedback refinement checkpoint

**Review-only, unmerged.** Based on Android-16-green 79-cosmetic/990-style/Versus-V7 reviewer PR #108, with its exact original Ocean and Woodland source PNGs and nine fonts.

## 1. Complete Album chapters
- Icons now appear **once in their own family chapters**, including previously separated starters. Basic (6), Woodland (17), Ocean (11), Garden (9), and Farm (6) = 49 total player-icon choices.
- The compact overall 0–49 progress, closest-reward spotlight, filters, individual lock/equip dialogs, profile styles and accessibility semantics are retained. Non-icon board and number collections retain their previous available/locked separation.
- Repeated family-label lines under cards are replaced with status (Locked, Available, Equipped); group headings already indicate the family.
- Preserves selected portrait and all existing IDs and statuses. No duplicate Ocean Octopus/Garden Honeybee/Farm Cow cards.

## 2. Mixed reward paths without removing completed-game milestones
- **17 curated portraits** have optional achievements in addition to their pre-existing total-finish count. New goals use distinct valid Daily puzzle dates, Medium-or-Hard finishes, Hard finishes, or unassisted Solo solves. All original thresholds still work, including rare 90/95/100 solve items.
- Daily progress counts **unique nonempty local dates** only, not reruns. Match records are deduplicated by session ID. Only successfully solved/won results qualify; unassisted does not count hint/check-assisted sessions. No streak penalty, wallet, new account or network call.
- The displayed progress track chooses the route requiring the fewest remaining events. Unlocks still use the same persisted ID, and pre-owned cosmetics remain usable after record pruning. *This is additive early-review reward balancing; owner can request different targets before final release.*
- New Kotlin unit tests verify 79 IDs, all 43 legacy pathways, Daily duplicates/failed results, Hard/Medium+ and clean Solo, and ownership retention.

## 3. Woodland artwork consistency trial
- 4 approved source PNGs (Chipmunk, Beaver, Woodpecker, Skunk) have a ~1.22× display-only scale adjustment. Original PNGs and exact SHA-256 manifest verification remain untouched. The user's phone screenshots and APK contact-sheet audit revealed these four are rendered smaller mainly because they use **512×512 with broad transparency** versus the older 384×384 originals. Raccoon is also 512px but its artwork fills the frame; intentionally do not enlarge it.
- This only corrects padding/visual weight. Woodpecker's sideways posture and other subjective illustration differences are **not yet fully solved**. Real Android screenshots and owner judgment must decide any redraw.
- Original images will be preserved if redraw candidates are later approved.

## QA and release safety
- New isolated app ID `com.playereden.sudokutogether.qa.beta2album`, so previous Beta 1, unified79, 990-style and V7 installs are untouched.
- Dedicated Android 16 workflow runs original PNG hash, V7 original banner, font licenses, all unit tests including mixed-unlock 79-ID tests and new scale tests, AndroidTest compile, lint, build and signer. Connected UI includes `CollectionPolishUiTest`, `Beta2AlbumOwnerUiTest`, V7/Co-op, Home and 9×9 typography tests. Export four *real* chapter screenshots and two native mode screenshots.
- **Pending:** new Ocean/Garden/Farm art pilot (candidates only, no new IDs until owner approval); icon B accepted provisionally; physical two-phone LAN; production-signed Beta1→Beta2 save-preserving update. Keep PR draft.
