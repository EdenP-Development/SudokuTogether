# GridMates: Sudoku — Repository health, Beta 1 stewardship and next audit

**Snapshot date:** November 16, 2026  
**Purpose:** Give this dedicated Sudoku development chat and future contributors one reliable starting point for **auditing, refining, expanding, and organizing Sudoku**, without conflating it with the separate GridMates: Word Search project.
**Status:** Repository/QA planning audit. **No gameplay source, save files, APKs, tags, releases, pull requests or branches were merged, deleted, rebuilt or signed by this audit.** No complete independent build or device test was run.

## 1. Source truth — read this first

- Repository: [PlayerEden/SudokuTogether](https://github.com/PlayerEden/SudokuTogether); default branch **`main`**.
- **`main` remains the previously integrated v0.11D Android QA baseline** (main tip observed `e85b8e4aa45bdce469858bcd4f8a298bbf01caf6`). Its [README](../README.md), [CONTINUE_HERE.md](../CONTINUE_HERE.md), and [project handoff](PROJECT_HANDOFF.md) principally describe that milestone.
- **The current GridMates Sudoku Beta 1 candidate is on `feat/gridmates-v1-beta` in [draft PR #42](https://github.com/PlayerEden/SudokuTogether/pull/42)**. The branch includes many later milestones and its [specific v1 Beta 1 QA record](https://github.com/PlayerEden/SudokuTogether/blob/feat/gridmates-v1-beta/docs/GRIDMATES_V1_BETA_RELEASE.md). That document identifies the **verified built-source commit** `76cbfc149569938b97286f3efe4ad44e08c5934f` and reports production Android version `1.0.0-beta.1` / code 30. **The current branch tip may include subsequent documentation commits and is not necessarily byte-identical to that tested APK source.** Check the release record and actual APK hash before comparing builds.
- The owner has provided an [itch.io GridMates: Sudoku page](https://sir-spudacus.itch.io/gridmates-sudoku) and is preparing/using it for public Beta 1 distribution. **The GitHub repository currently has no GitHub Release record**; this audit did not independently download/install the itch-hosted public APK or verify itch page visibility. Do not claim a current end-to-end distribution check without evidence.
- Preserve the **offline-first** strategy. Solo, Daily and local Wi-Fi multiplayer remain the core; no online matchmaking or mandatory account is a v1 launch dependency. Word Search work belongs in its **separate repository and Work chat**, not this Sudoku issue queue.

**Source-selection rule:** For maintenance and Beta 1 regression work, inspect PR #42 and its target/base relationships *first*; for historical recovery, consult `main` and specifically named checkpoints. **Do not tell a new implementer to build the latest public Beta from `main` without checking promotion status.**

## November 16 follow-up: verified Git topology and documentation-only conflict prevention

The full PR #42 commit list includes **the 20 stage commits from v0.11E through v0.11M**, followed by Beta 1 implementation/QA/publishing documentation commits (24 commits in the PR, as observed). The intermediate draft PRs #33–#41 are therefore already represented in the combined Beta candidate's ancestry; **do not merge their changes separately**. This is not yet an exhaustive proof that every historic bug fix is behaviorally verified in Beta.

The Beta PR lists **98 changed files** against the integrated main baseline: core/app/test changes, new portrait assets and fonts, QA workflows, and staged docs. Its head was `8d8ed09305a7a7a459b272c652dfb51e56c860ff`. No check-runs or commit statuses were attached to that head during this audit; treat the release document's 147 JVM and 13 package checks as **recorded Work evidence**, not independently completed GitHub CI.

After this audit was created, two overlapping `CONTINUE_HERE.md` notes were briefly committed on `main`; that file is also modified by PR #42. GitHub temporarily reported the Beta PR as **dirty/unmergeable** following those updates. To avoid a self-inflicted documentation conflict, the `main` copy of `CONTINUE_HERE.md` was restored to its exact pre-audit content, retaining these Beta-specific warnings in **this standalone audit** and [issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43). This **did not** change Android code, Beta source, signed artifacts or the PR state. GitHub mergeability may need asynchronous recomputation; don't equate a brief `unknown` result with approval or an all-clear. If any other conflict remains, inspect current base/head and solve with a reviewed integration change later.

**Recommended next owner action:** Confirm whether the **exact public production APK** downloaded from itch.io launches on a real phone (and which APK/version is on the phone); no GitHub API record can substitute for the player's actual device installation. Only then review readiness for promoting #42 to main.

---

## 2. Current branch/PR topology — preserve until safely consolidated

The following **ten PRs are open** as of the audit; most are explicitly draft, staged development milestones:

| PR | Milestone / summary | Base in live PR | Role |
|---|---|---|---|
| [#33](https://github.com/PlayerEden/SudokuTogether/pull/33) | v0.11E Profile, progress backup, Play prep | `main` | Foundational later stage |
| [#34](https://github.com/PlayerEden/SudokuTogether/pull/34) | v0.11F shared Co-op themes, Versus rematch | v0.11E feature branch | Stacked |
| [#35](https://github.com/PlayerEden/SudokuTogether/pull/35) | v0.11G Coach proofs, Ocean portraits | v0.11F feature branch | Stacked |
| [#36](https://github.com/PlayerEden/SudokuTogether/pull/36) | v0.11H deeper Coach | v0.11G feature branch | Stacked |
| [#37](https://github.com/PlayerEden/SudokuTogether/pull/37) | v0.11I Coach practice | v0.11H feature branch | Stacked |
| [#38](https://github.com/PlayerEden/SudokuTogether/pull/38) | v0.11J icon, UX audit | v0.11I feature branch | Stacked |
| [#39](https://github.com/PlayerEden/SudokuTogether/pull/39) | v0.11K Profile and Collection | v0.11J feature branch | Stacked |
| [#40](https://github.com/PlayerEden/SudokuTogether/pull/40) | v0.11L Garden, inks, fonts | v0.11K feature branch | Stacked |
| [#41](https://github.com/PlayerEden/SudokuTogether/pull/41) | v0.11M Collection swipe | v0.11L feature branch | Stacked |
| [#42](https://github.com/PlayerEden/SudokuTogether/pull/42) | **v1 Beta 1 candidate**, combined branch | `main` | Intended consolidated integration |

The major organization problem is **not ten unrelated unreviewed feature ideas**: many are **stacked snapshots of work already represented in a subsequent Beta 1 branch**, and PR #42 is also open. Verify commit ancestry, file overlap, data migration, and final code/QA in the consolidated PR before deciding which superseded stacked PRs to close. **Do not merge the entire stack and then merge #42 without reviewing duplicate commits and conflicts.**

**Proposed merge/cleanup gate (not executed):**
1. Confirm that PR #42 contains every accepted stage and doesn't omit fixes from a still-open branch.
2. Build/test *exact* candidate: production signer/package, versionCode update, no data-loss APK upgrade and two-phone LAN, plus clearly recorded outstanding risks.
3. After owner approves **the verified integrated source**, merge an appropriate integration PR to `main` (perhaps #42 after review) so canonical source and public Beta lineage align. Record code SHA, APK SHA-256, release date, known issues and branch mapping. Don't promise automatic ability to update existing installs with a different signing key.
4. **Then**, after confirming merged history and preserving rollback checkpoints, close superseded draft PRs with a short reason and links to integrated commit/QA evidence. Closing is safer than deleting historical branches immediately.
5. Consider an annotated Beta 1 Git tag and GitHub Release with matching public artifacts only **after** verifying artifact provenance and owner approval. **No need to recreate or regenerate the private production keystore in GitHub.**

## 3. Verification status — what is supported and what is not

From the v1 Beta 1 QA report in PR #42 (these are **recorded Work-chat results**, not rerun in this audit):
- **147 JVM tests passed**, **13 APK preflight checks passed**; lint **0 errors and 17 warnings**.
- Native Android 8 QA update reportedly preserved original app install/time and local profile, puzzle, note, settings, cosmetics and LAN saved-room continuity from v0.11M.
- **17/21** broader Android UI cases passed. Four tests timed out with emulator/system instability; the release report says the **public APK's standalone activity launch also timed out in emulator**. These should be recorded as **unresolved release-confidence checks** rather than stating a verified production-launch pass or definitively blaming the game.
- The owner's older successful physical two-phone Co-op smoke test is valuable. It is **not** proof that every Beta 1 Versus rematch, reconnect or newer Android accessibility scenario passed.
- Deferred: current-build physical Versus/rematch, modern Android device compatibility/accessibility, broad TalkBack, Daily rollover and other edge cases.
- The currently queried GitHub Actions history shows historical workflows but **no branch-specific successful GitHub Actions run for this Beta 1 commit**. Local Work QA evidence still counts as evidence, but GitHub's Actions tab is not an authoritative current Beta 1 success badge.

**Release-device checklist (initial gate):**
- [ ] Download the **exact public APK** from the actual itch.io page, not QA, not AAB and not a promotional mockup.
- [ ] Verify reported public APK SHA-256 `6d7dda34129fc4a6c74ab8146dcb36128967340558db50d9f7bac58cbe41c8c3` against the file; check public production signing cert and package `com.playereden.sudokutogether`.
- [ ] Install on a real compatible Android device; launch, complete/resume Solo/Daily, check Coach, Profile, Collection, settings and local backup/restore.
- [ ] On two physical phones running the public build, host/join Co-op and Versus, finish, rematch, disconnect/rejoin, compare saved progress and work offline/on Wi-Fi without WAN access.
- [ ] Test time rollover, large system font, portrait/landscape, Android accessibility labels/TalkBack where feasible.
- [ ] Record date, device/API, APK hash, outcome, and remaining limitations; preserve screenshots with notification/private metadata removed.
- [ ] **Never** instruct players to uninstall/clear application storage as a workaround without verifying their backups.

## 4. Documentation organization: what needs attention

**Observed:** `main` has a substantial history of QA docs, strategy/spec proposals and handoffs. That's useful for traceability, but its main entrypoints emphasize v0.11D and older stages. The Beta 1 branch has more recent v0.11E–M and v1 docs that are **not yet in main**. A newcomer can easily pick up the wrong checkpoint.

### Recommended three-layer navigation

**A. Current (top level, maintained):** README → CONTINUE_HERE → single current status/handoff; release version, source SHA, current shipped package/download, supported features, open blockers, immediate task list.

**B. Active work:** One concise issue for current release QA/source consolidation, bug reports, player feedback, small-scope polish. Each issue has reproducibility evidence and links to source.

**C. Archive / research:** Keep dated historical QA receipts, old PRs and exploratory documents but label explicitly **historical**, **implemented**, **partially implemented**, **future proposal**, or **discarded**. Don't delete the owner’s extensive Sudoku research/decision history merely to shorten the repo.

**Cleanup candidates for later review (not automatic):**
- `docs/PROJECT_HANDOFF.md` is a large accumulated timeline and contains historical “latest” statements; make the new top section authoritative and archive old content cleanly.
- `CONTINUE_HERE.md` currently says start from `main`/v0.11D; **do not edit its overlapping intro on main while #42 is pending**. This audit and [active issue #43](https://github.com/PlayerEden/SudokuTogether/issues/43) give the Beta-specific source direction until the integration is approved.
- `README.md` similarly highlights v0.11D; do **not** edit blindly before #42 is merged, because PR #42 already changes README.
- Some future proposal docs describe **features subsequently implemented** (e.g. co-op shared themes, Ocean/Garden icons, hint improvements, native fonts). Mark portions superseded or implemented **only after checking exact current code**, while retaining open design alternatives.
- Public itch page copy, image assets and marketing reference should indicate which visuals are **authentic screen captures** versus **promotional illustration**; don't substitute AI-recreated gameplay grids as factual screenshots.
- Old open milestone issues [#10](https://github.com/PlayerEden/SudokuTogether/issues/10)–[#14](https://github.com/PlayerEden/SudokuTogether/issues/14) may have completed parts; close/relabel only after checking acceptance items against source/tests, not based on numeric version names alone.
- Many historical feature/checkpoint branches exist; **keep** until canonical merge/checkpoint and future rollback are confirmed, then assess archiving/deleting inactive branches separately with owner approval.
- The current GitHub Releases list is empty; consider a tagged release corresponding to the **verified** itch public APK when appropriate.

## 5. Proposed ongoing Sudoku audit lanes (not bugs yet)

1. **App stability:** crashes, lifecycle/background, no-signal/offline behavior, recovery after restart, saved puzzles/notes, incomplete Daily/Co-op rooms.
2. **Sudoku correctness:** unique solution guarantees, human difficulty grading, deterministic Daily, hints and Coach proof fidelity, mistakes/Undo/notes, large generated sample.
3. **Local multiplayer:** host/guest equality, admission, room codes, disconnect/rejoin, co-op authored styles, Versus progression and rematch, guest privacy.
4. **UX/accessibility:** first-run start vs Continue, large text, numbered keypad, error/status messages, color/contrast, left/right thumb comfort, screen size, older or low-tech-confident tester.
5. **Cosmetics/progression:** Woodland/Ocean/Garden, boards/inks/fonts, entitlement persistence, accurate unlock prompts, preview readability, duplicate cosmetic IDs, device-to-device style fallback.
6. **Beta release operations:** reproducible signed APK, changelog, versionCode, restore process, issue feedback/reporting, itch visibility/download checks, release artifacts, attribution and disclosure.
7. **Future expansion only after hardening:** Daily gift/stamps, redeem codes, post-match comparison enhancements, shareable cards, new theme packs, Bluetooth/no-router nearby. Online accounts/matchmaking explicitly deferred until worthwhile after offline launch.

### Recommended priority order

- **P0:** Real Beta public APK integrity / launch QA, protect local saves and production signing continuity.
- **P1:** Consolidate source truth: validate #42, make a *single* current release status, define safe PR cleanup order, prune stale “latest” language.
- **P2:** Focused quality testing with actual phones and bug reproduction; create evidence-based issues rather than giant vague TODOs.
- **P3:** Player-visible polish and reasonable collections expansion; integrate only approved, tested improvements.
- **Later:** Online service and cross-game accounts, if a sustainable post-launch use case exists.

## 6. Scope for this basic ChatGPT conversation

Use this chat as the **Sudoku design, research, repository audit, quality triage and small scoped source/documentation maintenance workspace**. The Work chat is now dedicated to Word Search. That is a workflow decision, **not** a reason to reduce Sudoku QA rigor or invent claims of a build this session didn't compile.

For any source patch, distinguish **reviewed**, **edited**, **tested locally**, **built**, **installed**, and **tested on two phones**. GitHub edits are not evidence of Android runtime verification. Don't upload a new APK or merge to main without the owner's consent.

### Compact status recap

**Beta playable/released via user-provided itch URL; the confirmed source record is still PR #42, not main.** This preliminary audit identifies repository state/traceability and specific QA gaps. The next meaningful task is **release/source reconciliation and a test record for the actual public APK**, not starting five more major features.
