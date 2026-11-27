# STAB-06 — achievements and cosmetic unlocks

Active branch `feat/beta3-foundation`. STAB-05 runtime gate 45957495632 at addc748 was in progress (SDK and KVM setup passed; instrumentation step running). Do not claim runtime acceptance. See STAB-05-SAVE-INTEGRITY-HANDOFF.md.

## STAB-06.01 — achievement catalog
47 medals: Solo 11 (including Quick Thinker), Difficulty 10, Versus 8, Daily 7, Clean solves 6, Together 5. Source audit confirms stable IDs, thresholds and separate earned ledger. Duplicate record IDs are ignored. Daily goals count distinct valid dates. No catalog changes.
- `5f11a01`: Quick Thinker now chooses fastest positive duration rather than allowing zero-duration legacy record to mask a qualifying solve. Existing ten-minute threshold and assistance allowance unchanged.
- `aa1f94a`: regression tests mixed zero and valid duration, inclusive 600000ms boundary and 600001ms rejection. CI pending.

Next: verify emulator and latest quality gate; repair genuine failures first, then STAB-06.02 cosmetic catalog/ownership audit. Subsequent: unlock balance, completion feedback, equipped style consistency, name collection, legacy reward compatibility and integrated verification. Preserve save schema, IDs, artwork and signing; retention limitations and physical QA remain open.

## STAB-06.02 — cosmetic catalog and ownership
- `aa1f94a` passed quality gate 45957993381 and unsigned candidate 45957993449. STAB-05 emulator gate 45957495632 passed; job steps confirm instrumentation command and report upload succeeded. Individual report counts not inspected. Physical and signed upgrade acceptance remain open.
- Catalog: 85 items, 43 portraits (17 woodland, 11 ocean, 9 garden, 6 farm), 10 inks, 11 boards, 9 fonts, 12 emblems. 10 starter and 75 earnable items. Existing alternate unlock routes and ownership ledgers retained.
- `7f50d82`: accessible appearance now checks category as well as ownership for ink, board, font and emblem; portrait already did so. Prevents a valid owned ID in the wrong preference slot from producing invalid appearance. Correctly equipped items unchanged.
- `85c84e7`: regression tests wrong-slot IDs fall back to defaults and correctly equipped owned items survive absent history. CI pending. No artwork/threshold/ID changes. Artwork reference coverage and physical presentation still open.
- Next STAB-06.03: evaluate unlock thresholds, alternative paths, milestone consistency and pacing without raising existing requirements.

## STAB-06.03 — unlock requirements and progression balance
- `85c84e7` passed quality gate 45958911100 and unsigned candidate 45958911097.
- Source-derived assisted Easy Solo-only simulation, no alternate goal credits: completions -> available cosmetics (including starters): 0:10, 1:14, 2:17, 3:19, 5:26, 10:35, 20:48, 50:62, 75:69, 100:73, 135:74, 160:75, 190:76, 230:77, 300:78, 500:79. 21 items have alternate goals. Six multiplayer-only items: Violet (2 race wins), Circuit (3), Terminal (2 Co-op completions), Two Minds (1 Co-op), Challenger (1 race win), Champion (3 race wins). All 85 available after 500 assisted Easy Solo solves, two Co-op completions and three race wins. All 43 portraits reachable through Solo. No real-player retention or satisfaction evidence; model is deterministic catalog analysis only.
- `2b71fb5`: JVM regression fixture verifies existing milestone curve and multiplayer exclusives; pending CI. Deliberately no rebalance or new requirements; veteran cosmetic gaps after 100 and 500 completions remain a future content/design decision. Achievement goals extend to 1500 Solo solves; name words provide additional progression. Next STAB-06.04 completion feedback and reward presentation. Keep physical QA, signed upgrade and artwork review open.
