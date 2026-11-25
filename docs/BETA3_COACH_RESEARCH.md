# Coach research and curriculum backlog

2026-11-25 — Owner encouraged external research, including extreme puzzles. This is a research plan, not implemented functionality or a commitment to ship every technique in Beta3.

## Reference reading
- https://hodoku.dev/techniques — technique taxonomy: singles, intersections, subsets, fish, wings, uniqueness, coloring, chains, ALS.
- https://hodoku.dev/docs_solv — configurable technique order and solver constraints.
- https://hodoku.dev/docs_cre — technique-targeted practice generation.
- https://sudoku.coach/en/learn/sudoku-difficulty — difficulty and teaching considerations.
- https://www.conceptispuzzles.com/index.aspx?uri=puzzle%2Fsudoku%2Ftechniques — scanning and candidate analysis.
- https://www.youtube.com/channel/UCdh4SoUiHPACqY1SGumgKYQ — Sudoku Swami instructional reference, channel identified; individual videos not reviewed.
- https://sudoku.ad/learn/ — additional curriculum taxonomy.
Reference ideas only. Write original explanations, diagrams, implementations and fixtures; review licenses before importing any code or puzzle corpus. Do not reproduce proprietary lessons.

## Proposed curriculum (not public difficulty bands)
1. Foundations: rules, row/column/box scanning, candidate notation, full house, naked/hidden singles.
2. Intermediate: pointing/claiming (existing locked candidates), naked/hidden pairs, triples, quads.
3. Advanced patterns: X-Wing (existing), Swordfish, Jellyfish, Skyscraper, Two-String Kite.
4. Expert patterns: XY-Wing, XYZ-Wing, W-Wing, Empty Rectangle, uniqueness patterns.
5. Master: coloring, X/XY chains, alternating inference chains, finned fish.
6. Extreme: ALS, complex fish, forcing chains and contradiction proofs.

## Teaching and correctness contract
For each technique document prerequisites, how to spot it, precise proof rule, valid examples and near misses, before/after candidates, graduated prompts, practice and feedback. Build original reproducible fixtures; verify candidate removals independently with a solution oracle, check transposition/digit relabeling, invalid/ambiguous boards, no-op and false positives. Oracle is a test/validation tool, not a substitute for an explained deduction. Uniqueness-dependent methods must establish their assumptions; explicitly distinguish hypothetical contradiction proofs from guessing and direct reveals. Keep generation grading and existing records unchanged. Bound expensive search; honest unsupported-step fallback. Do not promise coverage of all hardest puzzles.

## Delivery
Immediate: finish proof navigation and before/after demonstration; then naked/hidden triples as candidate next solver additions, only with tests. Practice curriculum and beginner scanning guidance alongside engine. Later fish/wings/chains as separately reviewed slices. Optional local learner model remains proposal. See BETA3_COACH_TEACHING_ARCHITECTURE.md. No external service required at runtime.
