
## STAB-09.26 — All eight integrated runtime suites passed; FullQA3 initiated
Final complete QA2 variant runtime 46108005826 at fe841cc passed all eight suites: gameplay 140864988228, coach 140864988341, navigationAndNearby 140864988253, storage 140864988225, completion 140864988192, names 140864988178, collection 140864988161, backupUi 140864988028. Reports artifacts respectively 15248473302, 15248710584, 15248831286, 15249265425, 15249380860, 15249087286, 15248644425, 15249700691. Quality and unsigned candidate at 525d66e passed. af14554 adds separate FullQA3 flag/package .qa.beta3full3, mutually exclusive with QA2/stable, complete assets mandatory; dac199c updates full QA workflow to produce QA3 with additional packaged approved header resource check. Run 46110034960 in_progress at inspection. No verified QA3 artifact yet. Temporary debug signer remains isolated; preserve prior installations and use optional portable backup, never uninstall/clear. Exact QA3 package differs from runtime-tested QA2; gameplay source unchanged. Physical acceptance, production asset parity, original public signer, exact upgrade rehearsal and publication approval remain open. Stable QA secret remains unavailable. No public release or save changes. Next verify complete QA3 artifact and prepare owner functional testing.
# STAB-10.01 — Beta 3 release preparation (draft; not authorized)

## Current evidence
Cumulative source 525d66e: Android quality 46107555064 and unsigned audio candidate 46107555046 passed. Final complete-variant runtime 46108005826 at fe841cc: gameplay 140864988228, coach 140864988341, navigationAndNearby 140864988253, storage 140864988225 passed; names/completion running, collection/backupUi queued at inspection. Do not claim final runtime acceptance.

## Release identity
Proposed 1.0.0-beta.3 / versionCode 32, subject to registry and exact accepted Beta 2 APK/receipt verification. Preserve com.playereden.gridmates.sudoku.beta and original certificate SHA256 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d. Dedicated production signer availability unverified; missing QA secret does not prove production key missing. No signing or version change authorized here. Existing signed workflow is Beta2 branch-specific, not suitable unchanged. Final production variant must carry the complete approved soundtrack, artwork, emotes and visual flags; do not confuse unsigned audio candidate with full visual release. Verify exact packaged resources and absence of developer import controls.

## Draft player-facing devlog (hold until exact release accepted)
GridMates: Sudoku — Beta 3: More ways to make it yours

Beta 3 is our next step toward a more complete, comfortable Sudoku game. I've been refining the little things that make a puzzle feel at home: your player profile, collected words, music, and the way the game helps you learn.

- Build a personal name from collectible words earned through achievements.
- Explore the expanded Puzzle Coach, with step-by-step explanations and practice lessons.
- Enjoy six bundled soundtrack tracks and refined audio controls.
- Use preset messages, illustrated Fox reactions and Co-op attention markers in nearby play.
- Browse your collections more easily, with clearer board and number previews.
- Enjoy interface and accessibility refinements, plus work on local progress protection.

This is still a development beta. Feedback about puzzle play, readability, sound and playing together is welcome.

Update instructions (publish only after signed upgrade rehearsal): Beta 3 is intended to update Beta 2 in place. Back up your progress from Settings before installing. Do not uninstall or clear your previous game. If Android refuses the update, stop and report the issue. Beta 1 separate-package migration instructions remain applicable to legacy players. Backups replace destination offline progress rather than merge and exclude active multiplayer room credentials.

## Release gates
- [ ] All eight runtime suites, exact source and test reports.
- [ ] Complete cumulative owner APK with physical branding, Coach, picker, audio, finish/reward, restart, backup, offline, tablet and real two-device tests.
- [ ] Exact prior signed APK and receipt, original production signer and independent recoverable key custody; no new key or secret disclosure.
- [ ] Complete production-equivalent unsigned candidate, all approved assets, dependency/manifest and traffic review.
- [ ] Strictly higher version, exact same package/certificate, real signed install-over rehearsal according to STAB-05-APK-UPGRADE-GATE.md.
- [ ] Permanent public privacy and support URL, publisher/contact approval, audience and disclosure review. Existing GRIDMATES_PRIVACY_POLICY_DRAFT.md remains internal; no fabricated contact.
- [ ] Rights: existing owner attestation for all six Suno Pro tracks accepted for QA and no repeated confirmation needed; private evidence and final release rights review remain separate.
- [ ] Actual screenshots and approved release notes, explicit authorization for exact artifact and itch.io channel, durable archive and post-upload verification.

Amazon is a limited rehearsal, Google Play long-term target; neither submitted here. No public changes, new build, signing, or save changes in this checkpoint.
