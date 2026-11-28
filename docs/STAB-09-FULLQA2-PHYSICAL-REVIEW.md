
## STAB-09.22 — Approved Home header branding
Owner FullQA2 screenshot explicitly identified old grid icon. Root cause GridMatesBrand.kt default SUDOKU_GRID referenced legacy ic_launcher_foreground independent of adaptive launcher. Gradle d5afac3 adds prepareHeaderBranding to all variants: exact 14-part verified branding archive SHA256 487ee37d5087822e9b085b1dddf2e04b01b13a2d70d8e24dd90e60b233bf0dc8, launcher-master.png SHA256 ea3812eb46f6d3601fd5edf1d1f1f2d1c9c445a4140c7e1d2a86c08563d3367f, 1254 square, resampled to 256px ARGB generated drawable. c0b2cb7 uses this approved drawable in default mark with rounded clipping and Fit, retains alternate mark styles and header dimensions. 2839c80 adds resource regression. Existing splash/launcher resources, hero, cosmetics, saves and signing unchanged. Fresh CI and physical header verification pending; no new APK. Next STAB-09.23 Coach proof-specific shading, then name picker polish. FullQA2 remains current verified owner build; stable signing secret unavailable.
# FullQA2 physical screenshot review — STAB-09.21

Owner submitted 25 screenshot images across three batches, including repeated views, following the verified FullQA2 artifact 15246608732 (source 459030b). Review is visual evidence only; no screenshot binaries published.

## Verified visible refinements
- Available player icons: 6/55, compact two rows of three; Gridkeeper equipped, Owl/Fox/Octopus/Honeybee/Cow available. All-items view retains Basic and Woodland chapters and original portrait artwork.
- Profile guidance includes all five starter animals. Profile shows Quiet Willow Fox; later Name Collection shows Happy Moss Rabbit, consistent with a changed displayed name, not independent restart proof.
- Settings enabled-off switches visibly outlined and distinct from on switches; classic display and clean pencil marks off, touch feedback and puzzle sounds on, volume 50%.
- Music Corner shows Warm Puzzle Time, track 1 of 6, Playing, 50%, no duplicate playback status or developer import control in the shown view. Audio audibility/focus not established by screenshots.
- Name Collection 30/171, category 10/57, two-column starter cards, 47 locked first words, preserved progress/requirements, green selected filters. Last words collected view works visually. Category labels wrap into two lines.
- Name Builder arrows and dropdown indicators visible, first/last menus opened. Menus narrow and overlay other controls; partially clipped rows are consistent with scroll viewport, not proven inaccessible.
- Solo Easy 3/45 filled, home Continue shows 30s and separate Daily ready 2026-11-27, 0/7. No cold-restart proof.
- Coach practice lists 10 lessons in four categories; help dialog readable.
- Repeated-number review R2C8=9 outlined, directly related R2C7=9 teal. Unrelated R4C5=9 and R8C2=4 red, other related-to-other-conflict clues yellow; legend 'Shaded squares repeat that number' remains ambiguous because base conflict styling still appears. Main game should retain global conflicts; proof view should isolate direct evidence or clearly distinguish legend. No proof of changed puzzle rules.
- Owner explicitly reports old grid icon in Home header; adaptive launcher update did not replace this in-app mark. Inspect actual resource references and approved shared branding before changing. Keep launcher and art assets intact.
- Font/board thumbnail refinements not shown. No physical finish/rewards, backup restore, audio audibility, two-device LAN, offline, tablet/Fire OS, TalkBack or signed upgrade proof. Status/navigation waveform overlay not established as app defect.

## Next scoped corrections
1. STAB-09.22 Home header approved icon consistency (owner request).
2. STAB-09.23 Coach proof-specific shading/legend, preserve main-board conflicts and assistance rules.
3. STAB-09.24 Name Builder menu width and selected marker; shorten category labels First/Middle/Last while preserving semantic context, touch targets and tags.
4. Re-run integrated tests and complete cumulative candidate only if needed. Basic emblem art deferred optional polish, no release blocker.
Do not replace signer, uninstall/clear, omit assets or change unlocks/saves. Stable QA signing secret remains unavailable. FullQA2 remains current verified downloadable owner candidate. No official publication.
