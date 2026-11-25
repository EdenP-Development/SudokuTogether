# Beta 3 integrated audio review — AUD-08

Baseline afee70a0404d78c8b603f2756a4990ea99c52067. Quality gate 45692435411 and preview 45692435615 passed. Artifact 15059657081 (58,113,746 bytes archive), expires 2026-12-09T20:14:01Z. Isolated package com.playereden.sudokutogether.qa.beta3coach2; debug signing may vary. Public Beta2 unchanged. JVM tests and lint passed, instrumentation compiled rather than executed; APK asset checks verified all six hashes. No physical acceptance yet.

## Owner device checklist
- Clean install or upgrade same QA package if signature permits; do not uninstall public Beta2. Record device, Android version, QA version and existing system vibration/media settings.
- Fresh preferences: music off, effects off, touch feedback on. Enable music, confirm Warm Puzzle Time starts and no file picker is needed. Listen to all six tracks, previous/next and automatic rollover. Confirm selected title matches audio. Try slider at zero, 25%, higher volume; balance versus effects.
- Navigate Home, Settings, Solo, Daily and Coach; music should not restart on ordinary screen changes. Background and lock should stop playback; normal return restarts selected track. Competing audio or call should stop and not repeatedly request focus; explicit Play retries. Relaunch with music enabled: both splash logos silent, music starts after completion/skip. Disable music, relaunch and confirm silence. Confirm saved volume.
- Enable sound effects and try square selection, number entry, pencil mode/mark, erase and undo. No correctness cues, no excessive loudness or duplicate sounds. Test with music off/on and system media volume audible.
- Independently enable/disable touch feedback; verify tap and completion haptics on supported hardware and respect system settings. Document perceived strength, not merely API invocation.
- Complete Solo/Daily, reopen completed board, verify one chime only. If available use two devices for Co-op completion and Versus win/loss, reconnection and rematch. No celebration stacking. No replay for completed save or duplicate snapshots.
- Inspect Music Corner portrait, landscape and large font, TalkBack labels, slider, track list and disabled controls. Report misleading playing status, clipping, or inconsistent labels.
- Confirm existing puzzles, records, cosmetics and backup remain intact. Public signed higher-version upgrade remains a separate later release gate.

## Known boundaries
Music only packaged and exposed in isolated debug review; production promotion is pending. Automatic return restarts current track rather than resuming timestamp. No separate SFX volume, crossfade, reward cue or background service. Original Suno masters have not been archived to canonical hub. Device-local music preference does not migrate via portable progress backup. Playback status currently optimistic until MediaPlayer prepares; audit before release. Physical tests required before master standard is approved. Codex review quota not a dependency of regular CI.

## Owner review 2026-11-25
Owner reports all music works, haptics work, closing stops music. Screenshots show six tracks, 25% volume, main and Final Piece selected, effects switch off. Owner cannot hear effects (enabled-state test unconfirmed), wants music default on without Play, last played track restored after closure, and reports Final Piece immediately restarts/skips backwards at intro. Change default to enabled for absent preference (preserve explicit off), restore stable bundled track identity, remove redundant bundled switch-start path and deduplicate same-URI active requests. Root cause of Final Piece issue not established; inspect audio source separately and retest. SFX preview/volume pending. No claim of physical verification of fixes.

## Owner follow-up
Owner enabled effects and confirmed audible. Previous label confusing. Requested defaults music volume 50%, puzzle sounds on; separate effects volume. Number cue too poppy, erase sounds like drum/pan. Revised number to lower, softer mixed noise with slow attack; erase to filtered non-tonal friction noise. Added independent persistent effects volume (proposed default 50%) and number/erase audition buttons; renamed Puzzle sounds. Preserve explicitly stored preferences. Existing haptics unchanged. New audio perception and CI pending.
