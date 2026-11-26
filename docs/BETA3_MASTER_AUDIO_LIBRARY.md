# AUD-10.01 — Master audio library intake (provisional)

Owner accepted general audio review at cb37562; new production candidate CI and rights gates remain open. This is a catalog and recovery contract, not permission to distribute or an approved portfolio standard.

## Cozy Puzzle collection — proposed stable IDs, version 0.1
Keep existing runtime filenames and preference identity unchanged. Map library IDs to game-local runtime IDs rather than migrating player data.

| Library ID | Title | Runtime file | Master duration (s) |
|---|---|---|---:|
| cozy-01 | The Final Piece | track_01.m4a | 149.96 |
| cozy-02 | Library Puzzle | track_02.m4a | 133.88 |
| cozy-03 | Warm Puzzle Time (main theme) | track_03.m4a | 174.00 |
| cozy-04 | Sunny Sunday Puzzle | track_04.m4a | 147.60 |
| cozy-05 | Thoughtful Discovery | track_05.m4a | 154.92 |
| cozy-06 | Evening Puzzle | track_06.m4a | 184.40 |

Authoritative per-track master and derivative hashes: BETA3_SOUNDTRACK_INVENTORY.md. Master format Opus stereo 48 kHz M4A; working copies AAC-LC 128 kbps, normalization targets -20 LUFS and -2 dBTP (not measured acceptance). Existing working archive 15,369,442 bytes, SHA-256 273f9d88e0a795cad51af8b58a82ee9181a3acf792e9c0dbe42e7f2648080aa2; reconstruction and checksums via scripts/prepare_beta3_soundtrack.py and design/audio/transfer. Original masters not yet verified in durable canonical storage.

## Original synthesized feedback catalog
Canonical implementation reference: app/src/main/java/com/playereden/sudokutogether/PuzzleFeedback.kt at c9ae4e6 (reviewed source); 22,050 Hz mono signed 16-bit PCM, runtime SoundPool. Proposed IDs and nominal durations:
- puzzle-select: SELECT, 35 ms.
- puzzle-number: NUMBER, 85 ms, approved softer number cue.
- puzzle-note: NOTE, 28 ms.
- puzzle-erase: ERASE, 240 ms, two friction strokes, individual listening regression open.
- puzzle-undo: UNDO, 65 ms.
- puzzle-complete: COMPLETE, 720 ms, three-note arpeggio; same-round transition guard.

Exported WAV files and checksums not yet generated. Preserve synthesis source and deterministic fixtures; verify exported byte hashes against actual generated files before marking library ready. Do not claim Kotlin random sequence portability across implementations. Keep haptics as semantic actions, device-specific implementations and user preference; not audio assets.

## Canonical archive contract (proposed)
Use existing Eden-Development-Standards/audio/gridmates direction; its main branch is public. Keep actual masters, un-cleared derivatives, receipts and private account evidence in access-controlled storage, not a public hub. Publish only approved non-sensitive metadata and implementation references after review. For every asset record stable ID, revision, title, role, family, format, duration, source and export hashes, source location/access status, generator and generation/download dates, license/rights evidence status, normalization targets and measured values separately, approved games and test evidence. New edits receive new versions; old hashes remain reproducible. Pin chosen game-local copies at build time; no runtime network dependency.

## Acceptance and next steps
1. Locate all six original masters and verify hashes before declaring durable archive complete.
2. Record track-specific commercial entitlement, generation and download history; no public distribution while unresolved. Exclude earlier free-tier experiments.
3. Generate and verify six original effect exports or retain reproducible source plus verified exports; document haptic mapping separately.
4. Finish candidate CI and production audit; retain targeted device regression.
5. Deliver AUD-11 copyable hub handoff for shared review; do not publish an approved standard prematurely.

No new runtime changes, signing, publication or binary transfers.
