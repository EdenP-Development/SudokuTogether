# GridMates Beta 2 — Moss, Rosewood, Pebble and Tidal board pilot

**20 November 2026: isolated draft implementation on top of Farm PR #101.** Not a public APK update. Last-eight Ocean/Woodland art PR #103 remains separate.

| Board ID | Display name | Appearance | Base board | Unlock |
| --- | --- | --- | --- | --- |
| moss | Moss | Soft sage paper and green rules | light | 16 qualified puzzle finishes |
| rosewood | Rosewood | Rich berry/brown print with warm highlights | dark | 28 qualified puzzle finishes |
| pebble | Pebble | Mineral-gray cool paper with slate rules | light | 48 qualified puzzle finishes |
| tidal | Tidal | Deep ocean teal, moonlit clue numbers | dark | 70 qualified puzzle finishes |

Each has complete explicit `BoardPalette` state colors for background, rules, clue ink, selected, related, partner, matching and conflict cells, proof outline and preview lines. Game board logic, puzzles, scoring and existing seven boards remain unchanged.

**Ink compatibility:** Newly added `BoardPalette.adaptiveNumberContrast` is **false for legacy palettes** and true only for these four. When the new board creates a low-contrast background/highlight with a chosen player's number ink, only the displayed digit color is contrast-adjusted against the actual cell fill. The saved cosmetic ID is unchanged, and preview uses the same adaptive rule. Tiny pencil marks already use the established adaptive helper.

**Review branch catalog:** 35 portraits + 6 emblems + 11 boards + 10 inks + 5 fonts = **67 cosmetics**. When the eight PNG-based Ocean/Woodland candidates are also integrated, their 43 portraits would bring the staged catalog to **75**. The remaining 4 licensed number fonts would reach **79**.

### Release gates
- New JVM tests check exact IDs/unlock milestones, record deduplication, ownership after record pruning, board priority, palette uniqueness and all 10 number-ink contrasts at 5 cell backgrounds.
- Isolated preview uses `-PgridmatesBeta2BoardsPreview=true` and installs as `com.playereden.sudokutogether.qa.beta2boards`. It cannot replace or inherit public Beta 1 saves.
- Android 16 CI captures actual emulator launch, runs the focused Collection board test and creates a signed-debug artifact with checksum if successful.
- **Owner physical-device visual approval remains required:** check all four boards during real gameplay, selected/related/conflict/partner states, pencil marks and cramped/large screens. A home screenshot or emulator green badge does not verify aesthetic quality.
- Keep this work unmerged pending full review; verify two physical-phone LAN and original-signer production Beta 1→Beta 2 install-over only on the final unified candidate. Full 79-cosmetic cohesion audit is tracked in #102.

No bundled art, fonts, social feature, score adjustments or versioned production signing is introduced here.
