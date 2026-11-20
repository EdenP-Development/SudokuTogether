# GridMates Beta 2 — Collection asset audit tooling

Run this from the **repository root** on the final combined Beta 2 review branch, not the currently distributed public APK:

```bash
python -m pip install Pillow
python scripts/audit_collection_art.py \
  --expected design/collection-audit/beta2-target-portrait-ids.json \
  --output build/collection-audit
```

Outputs:
- `asset-report.json`: every actual Collection PNG, family, source dimensions, actual alpha bounds, touching-edge warnings, SHA-256, exact-duplicate-byte detection, missing/extra IDs.
- `contact-30px-cream.png`, `contact-30px-forest.png`
- `contact-48px-cream.png`, `contact-48px-forest.png`
- `contact-72px-cream.png`, `contact-72px-forest.png`

The sheets display **all four collections together with the same visual scale**, both surfaces, and each icon labeled. Legacy canonical PNGs may be 384px and newer original PNGs 512px; the tool keeps originals untouched and renders normalized previews.

When the integrated candidate has all **43 planned portraits** and owner-approved visuals, rerun with `--strict`. Strict flags missing artwork, fully empty/transparent images and identical source bytes as failing conditions. Warnings for edge alpha and nonstandard scale are **human review findings**, not proof of a broken asset.

This script is a **pre-audit instrument** and is not a substitute for visually evaluating outline weights, silhouette, color discipline, eye placement, pose, alpha halos, family coherence, exact Android renderer mapping, Gallery UI, boards/fonts/inks, progression, LAN, or production-signed install-over. Those are mandatory in `docs/GRIDMATES_BETA2_COLLECTION_COHESION_AUDIT_GATE_2026-11-20.md`.

**Scope:** 43 player icons only. The complete cosmetic catalog target remains 79 total (43 icons + 6 emblems + 11 boards + 10 inks + 9 fonts).

**Release protection:** Do not ship unfinished art, merge draft stacked branches out of order, or replace the already-installed Beta 1 with QA-signed reviewer apps.
