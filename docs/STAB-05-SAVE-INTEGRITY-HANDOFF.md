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

## STAB-05.03 — restore transaction readiness
- `9e7f7a7` quality gate 45952097346 and unsigned candidate 45952097399 were running at inspection; no new pass claimed.
- `57c8fa9`: reject new portable export/restore operations while either pending or committed restore marker exists. Existing startup recovery handles the committed marker without rolling back a successful restore. This prevents subsequent backup operations superseding unresolved commit evidence.
- `812be3f`: add instrumentation fixture simulating retained committed marker, checking export/restore rejection, unchanged primary bytes/settings, startup cleanup without rollback, and successful export afterward.
- Scope deliberately limited to readiness guard; marker deletion durability, normal gameplay writes, and physical interruption/storage exhaustion tests remain to audit. Latest commits pending CI/runtime verification. Next: inspect profile persistence and completion idempotence, with restore cleanup durability tracked as open. No changes to schema, backup format or signing.

## STAB-05.04 — profile persistence
- `9e7f7a7` unsigned candidate passed (45952097399); quality gate cancelled by newer work. `812be3f` workflows 45952485521 and 45952485511 were running at inspection.
- Reviewed GameViewModel startup, name normalization, cosmetic selection and profile navigation. Existing normal store updates preserve identity and union earned reward ledgers; legacy migration and portable restore tests already exist.
- `637bcf3`: when local progress is unreadable or otherwise blocked, do not persist startup name normalization based on an incomplete/read-only profile; preserve stored name and migration notice for future healthy recovery. Display still uses safe normalized name.
- `10f522c`: startup regression checks corrupted profile and existing name preferences remain unchanged. CI and runtime verification pending. Explicit settings changes, async preference durability and wider progression persistence still warrant review.
- Next: STAB-05.05 completion idempotence, including duplicate LAN completion events and record retention boundaries. Keep restore cleanup, instrumentation and physical accessibility QA open. No schema/signing/gameplay changes.

## STAB-05.05 — completion duplication prevention
- `10f522c` unsigned candidate passed (45952734650); quality gate 45952734664 running at inspection. Restore-marker candidate 812be3f passed (45952485511).
- Reviewed PersonalPlay, CompletionRewards and GameViewModel.recordMultiplayer. Completed Solo edits are rejected, Solo record insertion checks ID, LAN completion checks session:epoch:role both before and inside synchronized update, reward announcements compare durable before/after state. Deduplication based on retained records has a boundary at MAX_RECORDS=10000; older evicted LAN IDs are not covered by this guard.
- `fbad349`: PersonalPlay.stats counts distinct record IDs consistently with soloSummary, retaining first occurrence without changing saved history. Prevents duplicate legacy records from inflating counts and total solo time.
- `3d51276`: JVM regression tests duplicate Solo, Co-op, Versus win/loss/draw IDs and confirm original record list stays intact. CI pending.
- Next STAB-05.06: inspect record retention, achievement/cosmetic thresholds and old LAN session replay after eviction; avoid destructive deduplication or schema changes without justification. Instrumentation/device and restore cleanup gates remain open.

## STAB-05.06 — history and reward retention
- `3d51276` passed quality gate 45953188719 and unsigned candidate 45953188710.
- Source review: 10,000 retained completion records, 47 achievement IDs, 85 cosmetic IDs; earned ledgers survive record trimming. Daily review retains up to 31 completed boards, total 128 sessions with unfinished boards prioritized. Lifetime counts/best times and unearned thresholds remain retention-window dependent; evicted multiplayer completion IDs can no longer be deduplicated by record lookup. Do not claim lifetime statistics or unbounded replay protection. A new durable counter/identity design would require migration and backup compatibility review; deferred rather than altering schema in stabilization.
- `069cf9f`: real LocalPlayerStore instrumentation fixture fills 10,000 records, appends five unique Co-op records to evict all five original Solo records, deliberately omits ledgers from proposed update, reopens save, checks identity and all earned reward IDs retained. New test pending compile/runtime verification; no production changes.
- Next STAB-05.07: Daily persistence, date identity, duplicate completion and review window tests. Keep unresolved retention/replay and restore cleanup risks tracked; no signing or schema changes.
