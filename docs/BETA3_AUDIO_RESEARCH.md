# AUD-01 — GridMates Sudoku audio design research
2026-11-25. Proposed implementation direction, not final acceptance.

## Evidence
Owner handset test of Gentle Rests was positive; native sound/haptics reportedly absent. Music Pilot CI passed (45671002042, 45671002085). Owner now reports Suno Pro. Multiplayer code closure gate 45677167962 passed; actual two-phone and responsive instrumentation execution remain open.

## Design
Music provides atmosphere, not correctness cues. Silent approved startup; fade in on Home, maintain track across Home/Play/Coach/chat/collection; completion cue without interrupting track; pause on background/focus loss; no service. Playlist rotation with optional silence, no immediate repeat if multiple tracks. Avoid escalating difficulty-based music or correctness-dependent cues. Versus rhythmic tracks optional after audition, not a requirement.
Three independent channels: music enable/volume, sound effects enable/volume, touch feedback enable. Device-local preferences; preserve existing toggles and save/backup behavior. Default music off initially pending owner acceptance; suggested initial volume 25%; effects quiet. Preserve user and device accessibility/system haptic preferences; never force vibration.
Suggested music family: cozy acoustic felt piano, soft guitar, understated strings; 60–85 BPM, instrumental; aim 3–5 minutes per track, no abrupt endings or dramatic peaks. Begin 4–6 approved tracks rather than all 15 concepts. Shared GridMates library later, no required dependency across games.

## Technical candidates
Media3 ExoPlayer for offline playlist, repeat/shuffle, audio focus and release lifecycle; verify compatible pinned dependency and size before adding. Existing MediaPlayer pilot is a fallback, not permanent architecture. SoundPool for preloaded original short effects; no per-tap allocation; rate-limit and avoid duplicate remote/replay cues. Use supported View haptic constants or predefined VibrationEffect with device/system checks; prefer clear short effects. Original sound assets may be generated locally. No audio correctness hints, no blocking puzzle actions on audio failures. Start with one owned local controller and explicit events. Pause on background, transient loss and noisy output; no surprise restart after permanent focus loss. Test headset/phone speaker, external music and call interruption.
Music exports: retain original master, audition AAC-LC M4A or Ogg Vorbis compressed working copy, compare size/quality/loop transitions on supported Android devices. Short effects PCM WAV. No claim of gapless crossfade until measured. Keep track manifest title, id, duration, checksum, provenance. Remove prototype import UI when permanent assets integrated.

## Research references (accessed 2026-11-25)
- https://developer.android.com/media/media3/exoplayer/playlists — playlist/repeat/shuffle.
- https://developer.android.com/reference/android/media/SoundPool — preloaded short effects.
- https://developer.android.com/media/optimize/audio-focus — focus handling and Android 15 foreground restriction.
- https://developer.android.com/develop/ui/views/haptics/haptics-principles — clear, restrained haptics.
- https://developer.android.com/develop/ui/views/haptics/haptic-feedback — event feedback and device behavior.
- https://developer.android.com/media/platform/supported-formats — codec/container compatibility.
- https://www.gamedeveloper.com/audio/rethinking-the-audio-loop-in-games — repetition fatigue.
- https://online.berklee.edu/takenote/how-to-make-your-interactive-game-soundscapes-come-alive/ — dynamic soundscape and repetition.

## Acceptance
AUD-02 musical identity/track auditions; AUD-03 inventory; AUD-04 playback; AUD-05 effects; AUD-06 haptics; AUD-07 settings; AUD-08 integrated handset testing. Verify no music on startup when disabled, playlist continuity, loop boundaries, volume independence, interruptions, rapid taps, no duplicate cues, no puzzle state or record changes, package size, offline operation, no prototype assets in public release. No new runtime implementation in AUD-01.
