# GridMates Sudoku — Coach teaching architecture (B3-04)

## Owner direction — audit-led Coach development (2026-11-25)

Owner prefers code verification and audits as the primary Coach development gates, with deeper real-world gameplay feedback after Beta 3 is published on itch.io. Do not block each technique or small UI increment on manual owner playtesting. Continue bounded deterministic pattern tests, independent solution oracles, generated-puzzle and regression audits, performance/invalid-input checks, UI automation where executable, and save/scoring isolation checks. Distinguish compiled instrumentation from executed tests. Consolidate physical checks at meaningful UI/release checkpoints; public feedback complements rather than replaces pre-release correctness and upgrade-safety checks. Coach Review 2 is optional for immediate owner review, not a prerequisite to further engine development. Next: audit existing ten-technique coverage and gaps, then expand advanced techniques with verified original fixtures. No publication authorized by this direction.


Owner direction: make the Coach genuinely excellent at teaching independence, not merely supplying answers. Proposed architecture, not implemented feature claims.

## Cross-project inspiration
Read-only review of PlayerEden/the-last-taters at 0c768b7fe1b2a383760281140e36704b1754e683, README.md and docs/design/TATER_BRAIN_LAB.md. Taters describes rule/state/priority-driven production AI and a proposed explainable state -> considered actions -> selected action -> outcome trace; adaptive utility and bandit approaches are experiments, not verified production capabilities. Adapt the design principles, not colony simulation code. No changes to Taters.

## Contract
Coach owns guidance; player owns decisions. Offline deterministic logic, exact candidate evidence, explicit distinction between deductions and solution checks/reveals. Never invent a proof, silently edit a board, or disguise guessing as logic. Keep existing assisted classification and historical records. Separate teaching choices from the puzzle generation/grading engine.

## Architecture
1. Truth layer: current immutable puzzle snapshot, validate givens/visible values and solvability; enumerate legal deductions and exact before/after evidence. Existing six techniques remain the baseline.
2. Teaching layer: turn verified proof into observation, scaffolded clue, explanation, optional explicit reveal. Encourage a prediction before explanation, but never force a quiz.
3. Demonstration layer: show proof board, supporting candidates, elimination and resulting candidate state; optional step-by-step forward/back, explicit return to relevant square. No puzzle mutation.
4. Practice layer: existing six exercises, add varied verified fixtures, feedback that explains mistakes without humiliation; practice isolated from live puzzle stats.
5. Optional local learner model (later, proposed): per-technique evidence of independent practice success, attempts and requested scaffolds; conservative deterministic selection of practice recommendations. Unknown is unknown, no inference of ability from speed alone. No accounts, cloud or mandatory tracking; resettable. Never modify puzzle truth, difficulty or scoring based on learner model. Do not change save schema until reviewed.
6. Evaluation: deterministic replay of proofs, candidate elimination soundness against uniquely solvable fixtures, row/column symmetry, invalid and stalled positions, no answer leakage before explicit reveal, navigation and large text, practice isolation. Compare fixed fixtures before and after each change, record actual device evidence separately. Measure independent practice correctness and requested scaffolding only if optional local model is approved; don't claim learning outcomes from app use alone.

## Delivery order
A. Proof fidelity (implemented and gate passed 45558322123), guided observation (implemented and gate passed 45559098905).
B. Next: explicit before/after candidate demonstration and clear step navigation; no answer reveal regressions.
C. Practice variety and wrong-choice explanations; audit exact technique claims.
D. Additional techniques only with verified fixtures and no grading changes.
E. Consider local learning recommendations only after baseline teaching quality and owner scope approval; not a Beta3 release requirement.
F. Combined Coach review and outstanding device tests.

No external AI API or ML requirement. No copied code from Taters. Preserve current approved branding, progression, public beta identity, and offline functionality.
