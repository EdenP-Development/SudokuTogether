# STAB-05 — progression and save integrity

Active branch: `feat/beta3-foundation`. Accessibility handoff remains at docs/STAB-04-ACCESSIBILITY-HANDOFF.md; physical TalkBack/tablet and instrumentation execution remain open. Quality gate at cb77ee2 passed (45951007925); unsigned candidate still running when inspected.

## STAB-05.01
Source audit of LocalPlayerStore and ProgressBackupStore confirms schema 1, verified atomic writes, last-good recovery, damaged-source preservation, fail-closed saving, monotonic earned reward ledgers on normal updates, and journaled portable restore with rollback. Existing instrumented test inventory: 16 local store tests, 16 backup tests and 2 install-over continuity stages before this checkpoint. Execution not established.

- `4cf6180`: strict UTF-8 decoding of local profile candidates, matching existing journal decoding policy. Malformed bytes must not silently substitute characters in player identity.
- `333123c`: regression fixture corrupts a UTF-8 byte in the player ID, checks saving is blocked with no usable backup, and checks original bytes remain unchanged. New code and tests require CI and runtime verification.

## Next
Audit encoded write-size limits versus read limits; restore journal lifecycle and failure cases; then progression idempotence, record trimming and reward retention. Verify upgrade continuity with signed install-over on appropriate QA devices before release. Do not alter save schema, signing, existing artwork or player data without a specific justified migration.

## STAB-05.02 — atomic writes and storage limits
- `333123c` passed Android quality gate 45951523107 and unsigned candidate 45951523191; instrumentation execution not established.
- `8a38359`: enforce the same 64 MiB limit on encoded local profile reads and writes. Preflight both next and previous payloads before normal writes; encode and validate restored profile size before writing either copy. Public one-argument constructor and schema remain unchanged; internal constructor permits a smaller test limit. Recovery source archival and portable backup/journal limits unchanged.
- `9e7f7a7`: two instrumented tests use a small injected limit to check oversized updates and direct restore replacement fail without changing primary/recovery bytes or in-memory data; update test also reopens the original. Pending compile/runtime verification.
- Tradeoff: preflighting both byte arrays increases peak memory during unusually large saves; no stress benchmark performed. Next STAB-05.03: inspect restore journal lifecycle, commit-marker cleanup, storage failures, and existing interruption tests. Keep accessibility runtime gates open and preserve schema/signing.
