# Beta 3 — Name Collection balance and release contract

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
