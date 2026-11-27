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

## STAB-06.04 — reward presentation and completion feedback
- `2b71fb5` quality gate 45960029643 and unsigned candidate 45960029590 were running at inspection; no pass claimed yet.
- Reviewed PlayScreen CompletionRewardPanel and GameViewModel saved-transition paths. Panel matches Solo session ID or LAN session:epoch:role, suppresses empty reward state, defaults to two achievements, two cosmetics and three name words, and expands remaining rewards with a 48dp minimum button. Existing finish tests cover saved Solo rewards, historical reopen suppression and Daily presentation. Saved transition is based on durable records, not merely solved snapshot. Room navigation clears transient highlights.
- `95fe350`: two Compose regression fixtures exercise collapsed/expanded/collapsed rewards and suppression when completion record ID mismatches displayed puzzle. Synthetic presentation fixture intentionally isolated from actual ownership calculation. No production UI change. Compilation and emulator execution pending. Physical readability and late concurrent multiplayer callbacks still open; existing save runtime workflow does not select this UI test class.
- Next STAB-06.05 equipped customization consistency; verify pacing and presentation CI, then plan targeted reward UI emulator execution. Preserve artwork, unlock thresholds, save schema and signing.

## STAB-06.05 — equipped customization consistency
- `95fe350` passed quality gate 45960384227 and unsigned candidate 45960384201, including preceding pacing regressions; reward UI tests compiled, not executed.
- Reviewed GameViewModel startup, restore, equip, Home navigation, host resume and guest join; portable backup validates categories and ownership. Five preferences saved as one editor update; choosing an emblem clears portrait. Existing asynchronous preference apply durability remains open.
- `39f4389`: queued appearance broadcast checks current equipped appearance as well as room generation before sending; avoids obsolete queued selections. This is a best-effort stale-task guard, not full serialization of concurrent transport writes. Two-device runtime test remains open.
- `7c230a2`: instrumented fixture checks starter ink/portrait selection, wrong-category rejection, Home/Profile navigation, fresh ViewModel reading same preferences and emblem clearing portrait. Does not simulate process death or verify remote appearance delivery. CI/runtime pending.
- Next STAB-06.06 Name Collection integration, then legacy compatibility and targeted runtime tests. No artwork, catalog, schema, signing or unlock changes.
