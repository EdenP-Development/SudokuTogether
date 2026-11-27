# STAB-06 — achievements and cosmetic unlocks

Active branch `feat/beta3-foundation`. STAB-05 runtime gate 45957495632 at addc748 was in progress (SDK and KVM setup passed; instrumentation step running). Do not claim runtime acceptance. See STAB-05-SAVE-INTEGRITY-HANDOFF.md.

## STAB-06.01 — achievement catalog
47 medals: Solo 11 (including Quick Thinker), Difficulty 10, Versus 8, Daily 7, Clean solves 6, Together 5. Source audit confirms stable IDs, thresholds and separate earned ledger. Duplicate record IDs are ignored. Daily goals count distinct valid dates. No catalog changes.
- `5f11a01`: Quick Thinker now chooses fastest positive duration rather than allowing zero-duration legacy record to mask a qualifying solve. Existing ten-minute threshold and assistance allowance unchanged.
- `aa1f94a`: regression tests mixed zero and valid duration, inclusive 600000ms boundary and 600001ms rejection. CI pending.

Next: verify emulator and latest quality gate; repair genuine failures first, then STAB-06.02 cosmetic catalog/ownership audit. Subsequent: unlock balance, completion feedback, equipped style consistency, name collection, legacy reward compatibility and integrated verification. Preserve save schema, IDs, artwork and signing; retention limitations and physical QA remain open.
