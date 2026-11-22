# GridMates — Future Daily Puzzle Rewards (parked for later)

**Status:** Idea bank only — not part of the current Beta 2 owner-review scope. Requested during the Nov 22 collection/progression work. No rewards, currencies, claim timers, unlock IDs, or additional prompts are implemented by this document.

## Product goal

Daily Sudoku should have its **own gentle, satisfying reward loop** alongside the regular Collection. Players should feel that returning to a different day's puzzle means something, without turning a relaxing, local-first puzzle game into a mandatory login habit or a punishing streak challenge.

## Current implementation and constraints (verified in source)

- Each local date has a stable, unique, deterministic Sudoku puzzle; the existing Daily screen shows the last **seven days** and recent completion history.
- Saved DAILY/SOLVED GameRecords include a unique record ID, local `dailyDate`, duration, hints and assistance. `DailyChallenge.completedDays` counts distinct dates. Duplicate callbacks and duplicate IDs must never grant twice.
- The Daily play history preserves unfinished sessions; it retains only 31 completed boards for review while completion records are stored separately (up to MAX_RECORDS). Long-term reward ownership cannot rely solely on the 7-day visual history or 31 completed boards.
- No current streak bonuses or check-in requirement; don't quietly introduce mandatory consecutive-day goals.
- Current Beta 2 portrait progression will *also* offer several alternate goals based on 2/3/4/5/7/10 different Daily completions. A dedicated Daily reward category must coexist with those goals, without treating one completion as two different Daily *dates*.

## Candidate direction — for discussion, NOT approved implementation

**A quiet Daily Journal**: finishing a unique daily puzzle earns a local stamp for that date. The journal records dates solved and shows satisfying progress, not a timer-driven pressure system.

Possible separate reward tracks:
1. **Calendar stamps**: one visible collectible stamp per distinct solved day, organized by month.
2. **Weekly accomplishment**: optional milestones for any 3/5/7 daily puzzles in one calendar week, not required to be consecutive; weekly completion resets display progress but never removes earned collectibles.
3. **Long-term collection**: modest Daily-exclusive illustrated frames, page corners, number accents, or other *cosmetic-only* rewards for cumulative milestones. Keep their number small and distinct from Woodland/Ocean/Garden/Farm portraits.
4. **Special finishes**: an optional extra flourish for finishing without hints or checks. Any assisted completed Daily must still earn the regular daily stamp.

**Explicitly avoid for now**: daily currency, loot boxes, forced advertisements, disappearing rewards, penalty for missed days, paywalls, login requirements, cloud-dependent seasons, mandatory streaks or progression traps.

## Engineering questions for the future feature pass

- Where is a *durable, idempotent reward ledger* stored, and how will it survive GameRecord pruning, backup/restore and same-package upgrades?
- How should a user-facing Journal distinguish completed-but-assisted and completed-unassisted dates without devaluing normal completion?
- What is the simplest one-screen home for stamps/rewards for older and nontechnical players—Daily card expansion, Journal tab, or Collection subcategory?
- What weekly calendar convention is appropriate and how are local date/clock changes handled? No extra rewards by changing time zone or replaying the same date.
- How many rewards and how frequently? Prototype a small pilot first, then assess pacing based on actual gameplay and player choice.
- Do we permit late completion of unfinished past Daily puzzles to count toward a stamp, matching current daily-save behavior? Prefer yes if legitimate completion and records corroborate it.
- Add deterministic tests for unique dates, multiple same-date attempts, older records, zero-hint vs assisted, snapshot restores, ledger migration, idempotent rewards, and no streak penalties.

## Ordering

Finish current draft [PR #110](https://github.com/PlayerEden/SudokuTogether/pull/110) first: verify gallery chapter layout, alternate unlock criteria, original Woodland scale correction and Android owner reviewer. Evaluate proposed seven Farm/Garden/Ocean portraits separately. Return to **Daily Rewards** as its own design/implementation milestone, after the base cosmetic collection becomes stable. Do not add a prototype Daily-only item silently to the 79-item catalog.
