# Beta 1.2 — Co-op and Versus round input-mode reset (November 17, 2026)

## Source-verified issue

PR #61 fixed the **Solo** fresh-puzzle path: a new board should start in number-entry mode, not retain the Notes toggle from an earlier board. The LAN ViewModel already clears selected square and pending operation when the server or guest sees a new snapshot epoch, but it **did not clear Notes**. Consequently:
- Host choosing **New puzzle** in an unfinished Co-op room could leave Notes on for the next shared board.
- Host and guest could carry Notes into a new Versus epoch/rematch (whether a new race begins is still governed by the existing room rules).
- Ordinary same-epoch edits, presence notifications and reconnect events should **not** unexpectedly reset Notes.
- A new room is a fresh board even if an epoch is accidentally reused; compare both `sessionId` and `epoch`.

## Narrow implementation

- `GameUiState.isNewMultiplayerRound(next)` detects null prior board, changed room, or changed epoch.
- On host and guest snapshots: reset `notes`, selected cell, partner selection and pending operation **only** for an actual new round; keep input mode for same-round edits.
- When initially hosting or entering Join: set the transient input mode to number entry.
- No changes to saved `Cell.notes` masks, Undo/history, server protocol, session format, cosmetic IDs, opponent presence, chat, game rules, or room-code requirements.

## Verification and boundaries

- [ ] Core JVM and Android unit tests pass.
- [ ] `MultiplayerRoundInputModeTest` verifies identity for HOST/GUEST, same-round persistence, new room and new epoch.
- [ ] The actual Android hosted Co-op UI confirms cancellation keeps Notes and same epoch; accepted New puzzle produces a new epoch, clears selected cell and input-mode Notes, and retains an editable keypad.
- [ ] Existing `VersusPresenceUiTest` regression passes for name/progress, rematch availability, large text and reconnection UI.
- [ ] Existing `MultiplayerPolishUiTest` regression passes.
- [ ] Real two-device LAN Co-op/Versus finish/rematch/reconnect pending. Emulator tests do not establish a connected guest receiving a rematch.
- [ ] Existing public/QA install data and signing untouched; no preview APK in this slice.

**Do not describe physical multiplayer as verified** unless the owner completes both-phone checks on the same build. QA details remain in [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43).
