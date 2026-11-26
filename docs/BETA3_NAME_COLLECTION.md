# Beta 3 — Name Collection balance and release contract

## Current authoritative checkpoint — Beta 3 stabilization (2026-11-26)

Supersedes all earlier checkpoint status statements below; retain older entries as development history. Latest verified runtime [1088451](https://github.com/EdenP-Development/SudokuTogether/commit/1088451bdb4995697c9d20c2781acb3f44900500): [Android quality gate 45894433045](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45894433045) and [unsigned candidate 45894433028](https://github.com/EdenP-Development/SudokuTogether/actions/runs/45894433028) completed successfully. Instrumentation compiled, not executed. No public Beta 3 distribution.

- Player identity: unified curated builder, 171 stable words (30 starter, 141 achievement-linked), optional middle, max 40 characters, Name Collection album, durable ownership and saved-transition unlock feedback implemented. 188,442 structural combinations, longest 37; vocabulary audit docs/BETA3_NAME_VOCABULARY_AUDIT.md, literal IDs 8dce738. Human contextual and localization review remain open. Legacy names automatically normalize to owned canonical starter names; migration explanation and long-name physical review pending. No cross-game ownership synchronization.
- Network: protocol 11 canonical name boundary, authenticated reconnect migration, strict incoming snapshot/outcome agreement, approved error messages, generic UUID-based discovery labels and targeted malicious-message tests. Runtime a8e1548 passed both gates. Legacy network peers incompatible; saved formats unchanged. Remote word ownership not authenticated, LAN TCP not encrypted. Privacy draft updated at 152bc54; no policy compliance or public URL claim.
- Communication: 14 approved replies, 12 stable emote IDs, no arbitrary text, bounded chat, contextual Co-op Partner Ping and session-only visibility controls. Original Fox V1 approved, 512px upscaled transparent PNG masters and 192px WebP runtime assets. Canonical transport cd404ef, design/emotes/fox/v1/transfer, 13 ordered binary parts, 3754384 bytes, SHA256 c10b3fcae7e09d09a4c500d74a479c1842a19c54de6f9a7b1907714aa38bdf5b. Independent artwork verification 45791675576 passed. Gradle checksum import and 48dp picker/72dp history stickers f37640c initially failed script compilation; repair 1c38be0 passed 45890871448/45890871246. Updated UI assertions ac8f0b4 passed 45893530550/45893530555. Physical appearance, TalkBack, two-device testing and rights acceptance pending. Shared hub communication/gridmates/README.md fae6fe7 records canonical art source; no other-game rollout.
- STAB-01: 1088451 clears transient completion rewards in closeRoom, leaving permanent achievements, cosmetics and name words intact. Both gates passed; targeted lifecycle and concurrent callback verification still open. Do not claim physical acceptance.
- Audio: six-track candidate, soundtrack checksums and release dependency/permission screening integrated. Owner reported general physical audio acceptance; targeted erase/Final Piece/completion/focus regression and commercial rights remain open. Original master archive retention and exact final packaged review still required.
- Release: preserve itch Beta2 com.playereden.gridmates.sudoku.beta, versionCode 31, versionName 1.0.0-beta.2, existing certificate SHA256 9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d, existing 85 cosmetics/47 achievements, player identity, saves and reconnect tokens. No signing or package change, upload, LAT, publication or automatic other-game adoption authorized. Amazon rehearsal only; Google Play continuity is long-term priority. Public privacy URL/contact, current policy/audience review, actual Fire/tablet screenshots, physical compatibility, rights, signed upgrade and final release acceptance open.

Next: STAB-01 targeted regression/callback timing; STAB-02 identity polish and migration explanation; STAB-03 interface consistency; STAB-04 accessibility/tablet; STAB-05 progression audit; STAB-06 save preservation; STAB-07 ongoing repository synchronization; STAB-08 feature freeze; STAB-09 consolidated owner QA; STAB-10 storefront readiness. Coach expansion, collectible emote variants and advanced markers deferred. Do not add major features without owner direction. Follow shared ARTWORK_PIPELINE.md and release standards before inventing new workflows.

## Authoritative checkpoint — Name Collection / protocol 11 (2026-11-26)

Owner requires Sudoku Beta 3 as Amazon publishing rehearsal for future Google Play; other GridMates games adopt only after verified pilot. Latest verified runtime e3fe486cb5178462986f51f90d8d894b5120693b: Android quality gate 45763304064 and unsigned candidate 45763304081 PASS. Latest runtime changes 327742c26c65a1d06986827091d046ce3e0af236 and regression f0ac5b866cf0823b660721d571cff3a95c690585 are committed, CI not yet verified. Android instrumentation compiled by prior gates, not executed.

ID-01 core: 171 words, 57 per slot, 30 starters, 141 earned through unchanged 47 achievements. 188,442 structural combinations, max 37 characters, canonical limit 40; 29,723 exceed legacy 24. 34 offline bundles (132 total accessible words including starters). Exact vocabulary/combination linguistic review still open; exclusions currently empty. Core structural, eligibility, starter randomizer and migration tests added; no cross-app entitlement sync. NameBuilderUi.kt with optional middle, carousel controls/dropdowns, preview, randomize and ownership count; accessibility and 200% font fixtures compiled. Profile editor now uses builder; setName validates eligibility. Startup and successful restore normalize legacy or unearned names to deterministic starter fallback based on installation ID; no forced onboarding; migration notice still pending. Existing preferences and backup schema 1 retained, canonical long names export/import without truncation; older versions may reject such backups. Android restore/device regressions not executed. Review restore journal atomicity and stable selection IDs/localization before final acceptance.

ID-03: saved rooms retain old format, board/history/token; prepareCanonicalNames updates host name, replaces legacy reserved guest with Friendly Fox, token-authenticated reconnect updates guest name/appearance. Core migration and failure tests passed at e3fe486. Protocol 11 integration at 327742c requires canonical host/guest at server start, strict admission, client outgoing and incoming snapshot/outcome name validation, approved remote error text, host preparation before publishing. Network test fixtures updated; f0ac5b adds raw malformed names, v10 rejection and long approved name join. Fresh CI pending; inspect remaining Android/core fixtures for assumptions, add malicious welcome/state/outcome and actual reconnect tests. Remote earned-word entitlement cannot be authenticated by local peers. Generic discovery receive-side validation and host-visible label still open. Legacy client compatibility intentionally broken for network only. No public release authorized.

Next: verify integrated CI, fix regressions, complete migration tests and disclosure/safety audit; then ID-02 Name Collection album and nonintrusive unlock feedback, original emote artwork, integrated physical QA and all Amazon readiness gates. Update privacy draft only to match verified behavior; policy compliance not guaranteed. Preserve public itch Beta2 package com.playereden.gridmates.sudoku.beta versionCode 31, signing certificate, existing 85 cosmetics/47 medals, progress and reconnect tokens. No store upload, LAT, publication, signing change or other-game rollout authorized.

2026-11-25 | Owner requires implementation in Beta 3 before Amazon publishing rehearsal; Sudoku pilot before other GridMates adoption. Google Play is the later long-term target. No store upload authorized.

## ID-01/02 proposed launch catalog

10 starter words per slot, optional middle; 47 existing achievement-linked bundles, each one word per slot. 171 total words (57 per slot), 185,193 three-word plus 3,249 two-word theoretical combinations before safety exclusions. No new achievements or changes to their thresholds. This is a proposed catalog, not approved word/combination review or runtime implementation. Starter choices follow the shared identity standard.

| Existing achievement ID | First | Middle | Last |
|---|---|---|---|
| first-grid | First | Acorn | Solver |
| steady-hand | Patient | Fern | Wren |
| dedicated | Dedicated | Birch | Deer |
| solo-50 | Steady | Oak | Hare |
| solo-100 | Wise | Amber | Lynx |
| solo-250 | Seasoned | Cedar | Crane |
| solo-500 | Legendary | Golden | Stag |
| solo-750 | Resolute | Silver | Heron |
| solo-1000 | Masterful | Crystal | Falcon |
| solo-1500 | Timeless | Ancient | Phoenix |
| daily-3 | Bright | Dawn | Robin |
| daily-7 | Cheerful | Starlight | Finch |
| daily-15 | Radiant | Sunrise | Lark |
| daily-30 | Dreamy | Moonlight | Moth |
| daily-60 | Everlasting | Evergreen | Kingfisher |
| daily-100 | Faithful | Twilight | Swan |
| daily-180 | Enduring | Solstice | Albatross |
| medium-10 | Thoughtful | Brook | Beaver |
| medium-25 | Measured | Flint | Marmot |
| medium-40 | Focused | Compass | Kestrel |
| medium-100 | Skillful | Jasper | Eagle |
| medium-250 | Precise | Quartz | Osprey |
| hard-5 | Determined | Granite | Bison |
| hard-15 | Inventive | Nebula | Raven |
| hard-50 | Brilliant | Constellation | Dragon |
| hard-100 | Strategic | Obsidian | Griffin |
| hard-250 | Insightful | Aurora | Unicorn |
| clean-5 | Mindful | Lotus | Butterfly |
| clean-12 | Careful | Dewdrop | Ladybug |
| clean-25 | Composed | Snowdrop | Cricket |
| clean-50 | Unshaken | Glacier | Seal |
| clean-100 | Exact | Diamond | Dolphin |
| clean-250 | Independent | Sapphire | Whale |
| two-minds | Helpful | Daisy | Penguin |
| teamwork | Loyal | Orchard | Panda |
| team-15 | Supportive | Blossom | Koala |
| team-30 | Harmonious | Garden | Bee |
| team-75 | Trusted | Haven | Companion |
| challenger | Bold | Ember | Hawk |
| rivalry | Playful | Breeze | Raccoon |
| champion | Victorious | Laurel | Champion |
| versus-15 | Spirited | Comet | Cheetah |
| versus-50 | Sporting | Thunder | Gazelle |
| versus-100 | Persistent | Tempest | Mustang |
| versus-30 | Triumphant | Crown | Jaguar |
| versus-win-75 | Fearless | Summit | Panther |
| quick-thinker | Nimble | Spark | Hummingbird |

## Balance model
- Use existing stable achievement IDs and durable earnedAchievementIds, including retroactive credit and record pruning. One three-word bundle per achievement; no currency or duplicate awards, no grind invented solely for words. 30 starters and 141 earned words. Same reward count for each achievement; difficulty and rarity reflected by vocabulary, not power. Existing medals/cosmetics unchanged.
- Solo first completion grants 3 words, five solo grants 3 more, twenty grants 3 more. Daily 3/7 dates, Co-op 1/5, Versus first win and five completed races offer parallel early rewards. Existing clean-solve, difficulty, speed and late milestones supply specialized/long-term rewards, not prerequisites for ordinary naming. Solo+Daily+Difficulty+Clean+speed account for 34 of 47 bundles; Co-op/Versus account for 13. A fully offline solo/daily player can reach 44 words per slot (102 earned +30 starters). No multiplayer-only requirement for basic customization. No penalty for hints, breaks, losses or missed dates. Competitive win words optional.
- Projected per-slot choices after 1/5/20 solo completions: at least 11/12/13 respectively, plus other earned medals. Milestone collisions intentional but inspect reward bursts and avoid interrupting gameplay; summarize newly unlocked words in existing completion/collection presentation. Existing veterans receive all qualifying bundles retroactively, no repeat work. Catalog terms and all combinations require safety/locale and readability review; examples not final. No cross-app entitlement sync.
- Identity is independent of name; duplicate display names allowed. Stable slot-qualified IDs; no arbitrary typed suffix. No unlock purchases, expiry, competitive effect or random paid rewards. Never remove earned ownership because of record trimming.

## Implementation sequence and acceptance
ID-01 catalog plus pure Kotlin name validation/formatting and starter builder; ID-02 derive eligibility from existing durable medals, integrate collection and completion summaries; ID-03 enforce canonical approved names in host/guest/welcome/state and eliminate unrestricted text entry, reject legacy clients with explicit protocol bump; ID-04 atomic save/backup migration preserving playerId, active Solo/Daily, saved LAN state and reconnect credentials, all 85 cosmetics/47 medals; inspect old backup schema before changes. Legacy custom names must not leak in resumed snapshots. ID-05 exhaustive catalog combination validation, threshold boundary and duplicate/prune tests, Android compact/200%/tablet UI tests, physical reconnect and signed upgrade. Do not claim retroactive ownership, safe migration or compliance before evidence.
COM-05 finish physical and visibility regressions; COM-06 update shared evidence. ART-EMOTE-01 original starter artwork and rights/manifest acceptance, required for Beta3 release candidate as proposed scope. B3-AMZ all ten gates remain open until evidence, including Fire OS, rights, privacy URL, audience and exact package/signature, real tablet screenshots and LAT with explicit approval. Amazon rehearsal does not alter itch identity or commit to parity. Future Google Play review separate.
## Evidence baseline
59b509f quality gate 45753411985 and unsigned candidate 45753411962 passed, including generic discovery and earlier visibility tests (instrumentation compiled, not executed). Intermediate d3b601a candidate failed; follow-up 8e555e7 and 59b509f passed. Name Collection runtime not implemented. Existing source Achievements.kt defines 47 achievement IDs and durable earned ledger; PersonalPlay.kt schema 1 and 10,000-record cap. Current privacy draft describes existing custom names, not proposed identity system; revise only after implementation. No publication authorized.
