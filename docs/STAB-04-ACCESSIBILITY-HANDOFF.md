# STAB-04 accessibility recovery handoff

Updated 2026-11-27. Active development branch: `feat/beta3-foundation`. Do not resume from older main or publish an unsigned candidate.

## Verified baseline
- `95eb656a97e3edcfdef052361133ce113e10eb14`: Home help/profile/review explicit 48dp targets. Android PR quality gate and unsigned candidate both succeeded (runs 45944273048 and 45944273060).
- Tablet board/keypad expansion and landscape optimization previously build-verified. Landscape regression coverage included in branch; individual assertions not re-inspected in this pass.

## STAB-04.07
- `ec9fdbe`: explicit 48dp confirmation and dismissal actions in SudokuScreen.
- `f4c4942`: explicit 48dp Done action in PlayScreen game details.
- Source inspection: toolbar minimum height 52dp; keypad 48dp phone/60dp tablet minimum height, font-scale adaptive; shared navigation 69dp minimum height and selectable tab semantics; journal back 48dp; Coach actions generally 48dp minimum height. Heights alone do not establish width or runtime bounds.
- New commits require CI verification. No physical device testing performed.

## Next
Audit actual touch widths at narrow screens, large font scales and landscape; inspect remaining dialogs and navigation, TalkBack focus order, duplicate labels and state announcements; add regression assertions; finish accessibility documentation and physical tablet QA. Then progression/save preservation audit.

Preserve gameplay, artwork, progression, networking, save formats and signing. Ordinary touch target 48x48dp minimum where feasible; board cells treated separately. Do not claim comprehensive accessibility or release readiness from source inspection alone.
