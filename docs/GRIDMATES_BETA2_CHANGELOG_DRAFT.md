# GridMates: Sudoku — Beta 2 changelog and itch.io devlog draft

**November 24 update — unpublished:** Current draft includes 85 cosmetics, 47 achievements (16 additional veteran milestones), approved dedicated Bear/Rabbit Versus scene, Co-op woodland scene and five refined illustrated portraits. Earned cosmetic and achievement ownership persists locally. Portable progress backup and restore passed a synthetic 750-game cross-installation rehearsal; actual public update/migration remains unverified. Do not advertise a replacement package or normal update until its corresponding release path is accepted. Shared standards: https://github.com/PlayerEden/Eden-Development-Standards.


**Not published.** Prepared November 17, 2026; use only after the actual Beta 2 APK is signed with the original public certificate, has passed the real Beta 1 install-over preservation test, and receives owner approval. This is release copy, **not** evidence that Beta 2 is downloadable yet.

## Short changelog (copy-ready after release acceptance)

### Beta 2 — smoother puzzles, clearer progress

We've refined GridMates: Sudoku to feel better in everyday play while keeping the familiar calm puzzle-book style.

- **Easier-to-read puzzles:** improved pencil-mark legibility, duplicate-number feedback, number-entry explanations, and small-screen interactions.
- **More useful customization:** clearer Profile and Collection previews, easier browsing, and expanded board/number style choices.
- **Better Daily and completion feedback:** a recent-Daily overview, clearer finish information, and rewards that reflect genuinely earned milestones.
- **Durable Collection ownership:** earned cosmetics remain yours locally even when older completed-game records are trimmed. Existing earned items can be recognized from compatible Beta 1 saves.
- **Safer progress handling:** clearer backup and restore screens, including a preview of earned Collection items before restoring.
- **Clearer About and release information:** straightforward explanations of offline play, nearby Wi-Fi matches, progress storage, and backup limitations.

No account is needed. Solo and Daily work offline. Nearby Co-op and Versus use two Android phones on the same reachable Wi-Fi network.

## For existing Beta 1 players

**Do not uninstall Beta 1 to update.** First choose **Settings → Progress → Back up progress** and keep the exported file in a safe location. After the publisher has verified and released the original-signed Beta 2 APK, install it *over* the existing public Beta 1 app. A compatible Android update should retain your profile, unfinished Solo/Daily games, notes, records, preferences and earned cosmetics.

If Android reports an incompatible signature or refuses the update, **stop** rather than uninstalling or clearing data. QA/preview APKs are separate apps and are not a production update path.

## Limitations to communicate clearly

- The current nearby protocol exchanges gameplay and optional chat on local Wi-Fi **without application-layer encryption**. Play on a trusted network; a room code is not encryption.
- There is no remote matchmaking or online account sync.
- Progress backups are manual files; they do not include live multiplayer room state and are not cloud saves.
- Existing free gameplay does not include advertising, in-app purchases or developer-hosted analytics SDKs in the checked source. Confirm this against the exact shipped APK before making definitive store declarations.

## Unpublished acceptance checklist

- [ ] Recover original public Beta 1 signing credentials privately. The public certificate must match the original; **never** substitute a new key for an install-over.
- [ ] Independently verify actual itch-downloaded Beta 1 APK and new Beta 2 APK package, version codes, certificate fingerprint and SHA-256 hashes.
- [ ] Rehearse genuine, production-signed Beta 1 → Beta 2 Android in-place update using backed-up noncritical local progress.
- [ ] Owner-phone visual check of final signed APK and real screenshots from that candidate.
- [ ] Owner-approved publisher contact, privacy/support information, and honest itch.io AI disclosure.
- [ ] Test the itch.io downloadable file after uploading in a restricted/draft page, then deliberately approve public visibility.

**Reference:** [Beta 2 itch listing](GRIDMATES_ITCH_BETA2_LISTING_DRAFT.md), [actual test-signer upgrade rehearsal](BETA2_ACTUAL_APK_INSTALL_OVER_QA.md), [production signer gate](BETA2_PRODUCTION_SIGNING_GATE.md), [project handoff](../CONTINUE_HERE.md).
