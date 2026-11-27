# STAB-05 — progression and save integrity

Active branch: `feat/beta3-foundation`. Accessibility handoff remains at docs/STAB-04-ACCESSIBILITY-HANDOFF.md; physical TalkBack/tablet and instrumentation execution remain open. Quality gate at cb77ee2 passed (45951007925); unsigned candidate still running when inspected.

## STAB-05.01
Source audit of LocalPlayerStore and ProgressBackupStore confirms schema 1, verified atomic writes, last-good recovery, damaged-source preservation, fail-closed saving, monotonic earned reward ledgers on normal updates, and journaled portable restore with rollback. Existing instrumented test inventory: 16 local store tests, 16 backup tests and 2 install-over continuity stages before this checkpoint. Execution not established.

- `4cf6180`: strict UTF-8 decoding of local profile candidates, matching existing journal decoding policy. Malformed bytes must not silently substitute characters in player identity.
- `333123c`: regression fixture corrupts a UTF-8 byte in the player ID, checks saving is blocked with no usable backup, and checks original bytes remain unchanged. New code and tests require CI and runtime verification.

## Next
Audit encoded write-size limits versus read limits; restore journal lifecycle and failure cases; then progression idempotence, record trimming and reward retention. Verify upgrade continuity with signed install-over on appropriate QA devices before release. Do not alter save schema, signing, existing artwork or player data without a specific justified migration.
