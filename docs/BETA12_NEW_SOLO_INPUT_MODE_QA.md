# Beta 1.2 — Fresh Solo input-mode consistency (November 17, 2026)

## Reproduced source inconsistency

- The gameplay toolbar lets players toggle **Notes** in Solo.
- The in-puzzle **New puzzle** button asks for confirmation before replacing an unfinished Solo puzzle, protecting progress.
- But `GameViewModel.solo(false)` originally initialized a new board without clearing `notes`. If a player started another Solo puzzle from Notes mode, the first keypad tap could create a pencil mark rather than enter a number.
- `openDaily()` already initializes `notes = false`, so this behavior was inconsistent across new Solo/Daily puzzle starts.
- This is a **source-derived interaction bug**; it was not observed in a physical phone recording.

## Narrow fix and acceptance

- Reset the **input-mode flag only** when opening a Solo puzzle. No deletion of note masks in saved boards, records, profiles, settings, or other session files.
- Preserve the existing **Start a new puzzle? / Keep playing / Start new** confirmation when an unfinished puzzle exists.
- Android instrumentation `GameConvenienceTest.freshPuzzleConfirmationProtectsOldNotesAndResetsInputModeAfterAcceptance`:
  - opens a real Solo puzzle in a fresh emulator;
  - activates Notes;
  - cancels New puzzle and checks the same session and Notes input mode remain;
  - confirms New puzzle and checks a new session ID, Notes off, and visible Notes toggle label.
- Core/Android unit and the full `GameConvenienceTest` UI suite must pass before merge.
- The existing published Beta and QA apps remain unchanged. This source fix does **not** verify physical LAN, original signing, or save-upgrade continuity.

## Wider finding (separate future check)

A newly started *multiplayer* round also needs an input-mode transition review; it is not changed by this Solo-specific patch. Do not claim two-device rematch correctness from a Solo-only emulator test.

## Automated verification (Nov 17, 2026)

[GitHub Actions run 44152100241](https://github.com/PlayerEden/SudokuTogether/actions/runs/44152100241) **passed** core JVM tests, Android Kotlin compilation and unit tests, and **all 4/4** `GameConvenienceTest` Android 36 emulator tests (including the new fresh-puzzle cancellation/Notes test). No failing/skipped test cases reported. This is emulator evidence only: no new physical device test, APK release, saved-data migration or two-phone review.
