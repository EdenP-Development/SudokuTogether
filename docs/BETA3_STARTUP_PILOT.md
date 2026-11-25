# Sudoku Beta 3 — shared startup pilot v1.0

Owner-approved assets; implementation and physical acceptance are separate. Sudoku is the sole initial pilot. No publication or other-project adoption authorized.

## Source and provenance
Implementation commit fb9e4afd91d6aa58fac642d932e9f5620c7c60af on feat/beta3-foundation.
- Spudacus: app/src/main/res/drawable-nodpi/spudacus_v1_full_transparent.png; pinned central commit 015645bffb612e0f4311ec5a2d29e9c8bad91289; SHA-256 74773dbda822161f73a639f959e8bce5c6e0b333a94ebeb8ee27f620d4995d25.
- GridMates: app/src/main/res/drawable-nodpi/gridmates_v1_runtime_transparent.png; pinned central commit 9affedc94ca5a6b896d36148f46f14a4e8218c3a; SHA-256 eef6d631cac0b9523f7e0dbebac229f9a2458d36fe2e73e111d251704d9ff292; source and destination Git blob 8f2f3a53a5873f747a14e1bb86767fc1ad07725a matched. CI SHA-256 checks configured for both assets; fresh result pending.

## Behavior
MainActivity composes existing game behind StartupBranding.kt. Spudacus then GridMates then existing menu. Shared #FAE7CD background, no added visible wording or sound. Each stage nominal 160ms fade in, 280ms hold, 160ms fade out, total 1200ms at normal animation scale; actual wall time not measured, system animation scale may affect duration. ContentScale.Fit, safeDrawingPadding, 24dp padding, max width 420dp, width fraction .82, max height 260dp. Existing launcher art unchanged from Visual2 (58dp adaptive foreground). Tap or Back dismisses entire sequence; disabled system animators skip; activity recreation and ON_STOP skip. The timed overlay does obscure ready UI until skipped or finished; no readiness-based early dismissal yet. Verify real device startup performance, accessibility focus and resource failure fallback before final acceptance; no claim those are fully validated. No runtime network fetch.

## Verification
Visual2 run 45546351053 and standard gate 45546362833 passed; Visual2 did not contain GridMates second stage. Visual3 uses isolated temporary debug package com.playereden.sudokutogether.qa.beta3visual3, not an install-over of Visual2 or public Beta2. New build includes actual resource-ID stage ordering tests and both source hashes; APK packaging presence/signature checks configured. No production package, signing credentials or save logic changes. Fresh CI pending. Instrumentation compilation is not execution.

## Owner review checklist
Cold launch both stages in order; logo scale/alpha edges/contrast portrait and landscape; timing and transition; tap and Back during either stage; reduced motion; warm resume and rotation; offline launch; accessibility focus and navigation; startup performance compared with prior build; normal puzzle and save behavior. Record actual feedback, limitations and final source/receipt here before proposing shared standards. No rollout until owner acceptance.
