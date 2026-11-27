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

## STAB-04.08 — responsive keypad
- Previous dialog changes verified at `f4c4942`: quality gate 45946925532 and unsigned candidate 45946925536 succeeded.
- `795a755`: use three columns below 264dp available dock width (five 48dp keys and four 6dp gaps require 264dp); split toolbar below 210dp regardless of font scale. Existing font-scale split rule retained. Normal phone/tablet layouts unchanged.
- New layout is source-reviewed only; CI and physical narrow/landscape checks pending. Three columns need at least 156dp for 48dp keys; extreme constraints remain outside this guarantee. Extra row increases dock height, so verify board viewport and scrolling.
- Next: runtime bounds and font-scale regression tests; TalkBack duplicate labels/focus/state audit. Do not mark physical QA complete.

## STAB-04.09 — ultra-narrow regression coverage
- `795a755` responsive keypad baseline passed Android quality gate (45947701258) and unsigned candidate (45947701214).
- `29cd896`: enable whole-page scrolling below 288dp window width at normal text scale, accounting for 24dp dock padding and the extra keypad row.
- `e3ff9a3`: add CompactPlayLayoutTest fixture at nominal 260x900dp, 100% font scale. Checks nine visible/reachable keys, at least 47.5dp measured width and height (rounding tolerance), repeated three-column alignment, toolbar and board reachability, and note-mode content description.
- Existing tests already cover 200% compact portrait/landscape, large tablet landscape, expanded tablet portrait and keypad note-mode semantics/geometry. New fixture is committed, execution not yet verified. Check workflow coverage and actual test results before declaring pass.
- Next: verify CI and fixture execution; review TalkBack focus, duplicate announcements and remaining controls; physical tablet QA remains open. No save, gameplay, networking, artwork or signing changes.

## STAB-04.10 — screen-reader semantics
- `e3ff9a3` passed quality gate 45949048724 and unsigned candidate 45949048731. Job inspection confirms source/UI-test compilation, JVM regression and lint; no instrumented test execution established.
- `367be0c`: selection semantics restricted to Notes toggle; ordinary Erase/Undo/Hint actions no longer expose selected state. Hide redundant visual keypad number/count and descriptive Hint child labels from semantics, retain button actions and number count test tags.
- `296c32c`: add semantics assertions for no redundant keypad Text property, Notes selection state, and absence of Selected on ordinary actions. Compilation and instrumentation execution pending. Review any existing tests that locate Hint by visible text; semantics change can require locator migration.
- Next: verify CI and audit text-based test selectors, then remaining board cell/heading/focus semantics and physical TalkBack/tablet QA. No game rules, visual styling, saved data, networking or signing changes.

## STAB-04.11 — test integration
- `367be0c` unsigned candidate passed (45950127346); `296c32c` quality gate and unsigned candidate were still running when inspected.
- `e22fcbf`, `96fcfb1`: migrate Hint selectors in CompactPlayLayoutTest and PlaySmokeTest to the descriptive content description. Targeted review of ten gameplay/Coach test files found these two files using old Hint selectors. Full suite search not completed; directory retrieval retry failed.
- Latest changes are committed, not yet build/runtime verified. Next: complete selector review, inspect board cell duplicate announcements and navigation focus, verify instrumentation execution and physical QA before closing accessibility. No production code changes in this checkpoint.

## STAB-04.12 — board and navigation semantics
- `296c32c` unsigned candidate passed (45950138255); quality gate cancelled by newer work. `96fcfb1` workflows 45950479083 and 45950479102 in progress when inspected.
- `3d81b60`: suppress redundant digit and pencil glyph text semantics while retaining cell descriptions, actions, state descriptions, and pencil test tags.
- `77bc1d1`: extend Beta3BoardClarityTest with value, clue and pencil descriptions, absence of redundant Text, selection and click assertions, and retained pencil tags.
- Navigation source review confirms selectableGroup, Tab role, selected state, single label, 69dp minimum height, journal heading and 48dp back button. Actual focus order and narrow widths remain unverified.
- New commits require compile and instrumentation verification. Full selector audit and physical TalkBack/tablet testing remain open. No visual, rules, save, network or signing changes. Next: verify builds, audit remaining selectors and run instrumentation where available; then source-level accessibility closeout with explicit deferred device QA.
