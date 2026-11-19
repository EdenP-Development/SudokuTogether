#!/usr/bin/env python3
"""Apply GridMates Woodland V5 raster mode banners to a V4-based checkout.

Usage:
    python tools/apply_woodland_v5.py /path/to/extracted/V5-art-pack --dry-run
    python tools/apply_woodland_v5.py /path/to/extracted/V5-art-pack

This script deliberately fails closed on source drift. Commit the resulting
Kotlin, instrumentation-test, and binary image changes together; do not merge
until the Android build and actual screenshots are reviewed.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import struct
from pathlib import Path

REPO = Path(__file__).resolve().parent.parent
MANIFEST = REPO / "design/ui-art/v5/art_manifest.json"
BANNER = REPO / "app/src/main/java/com/playereden/sudokutogether/NearbyModeBanner.kt"
TEST = REPO / "app/src/androidTest/java/com/playereden/sudokutogether/HomeLivingBookUiTest.kt"
V4_PAINTER = "painter = painterResource(R.drawable.gm_woodland_storybook_hd_v4),"
V5_PAINTER = ("painter = painterResource(if (together) "
              "R.drawable.gm_mode_coop_woodland_v5_hd else "
              "R.drawable.gm_mode_versus_woodland_v5_hd),")
OVERLAY_START = "        // Reuse real high-fidelity portrait assets already packaged for Collection."
OVERLAY_END = "        // Native text stays readable; none of its words are baked into art."


def require_one(content: str, needle: str, title: str):
    if content.count(needle) != 1:
        raise RuntimeError(f"{title}: expected exactly one '{needle}'")


def verified_assets(artpack: Path):
    info = json.loads(MANIFEST.read_text(encoding="utf-8"))
    result = []
    for item in info["new_assets"]:
        source = artpack / item["resource"]
        target = REPO / item["resource"]
        if not source.is_file():
            raise RuntimeError(f"Missing packaged image: {source}")
        data = source.read_bytes()
        if hashlib.sha256(data).hexdigest() != item["sha256"]:
            raise RuntimeError(f"SHA-256 mismatch: {source}")
        if len(data) != item["bytes"]:
            raise RuntimeError(f"Byte-size mismatch: {source}")
        if data[:8] != bytes([137, 80, 78, 71, 13, 10, 26, 10]) or data[12:16] != b"IHDR":
            raise RuntimeError(f"Invalid PNG: {source}")
        if struct.unpack(">II", data[16:24]) != (item["width"], item["height"]):
            raise RuntimeError(f"Dimensions mismatch: {source}")
        if target.exists() and target.read_bytes() != data:
            raise RuntimeError(f"Existing checked-in image differs from verified V5 asset: {target}")
        result.append((target, data))
    return result


def patch_banner(code: str):
    require_one(code, V4_PAINTER, "V4 banner")
    require_one(code, OVERLAY_START, "V4 mascot overlay start")
    require_one(code, OVERLAY_END, "V4 mascot overlay end")
    code = code.replace(V4_PAINTER, V5_PAINTER, 1)
    start = code.index(OVERLAY_START)
    end = code.index(OVERLAY_END, start)
    code = (code[:start]
            + "        // V5: each multiplayer raster includes its own Fox/Owl characters.\n"
            + "        // Do not overlay duplicate Collection portraits.\n"
            + code[end:])
    old_comment = ("    // V4 uses the exact high-resolution illustrated Woodland raster used by Home.\n"
                   "    // The actual packaged collectible portraits, not synthetic substitutes or\n"
                   "    // hand-drawn scene symbols, provide the right-side visual accent.")
    new_comment = ("    // V5: unique full-resolution mode illustrations; Solo retains the V4 scene.\n"
                   "    // All headings and controls remain native Android Compose.")
    require_one(code, old_comment, "V4 banner documentation")
    return code.replace(old_comment, new_comment, 1)


def patch_tests(tests: str):
    for mode, following in [
        ("coop", '        compose.onNodeWithText("Shared board").performScrollTo().assertIsDisplayed()'),
        ("versus", '        compose.onNodeWithText("Race to finish").assertIsDisplayed()'),
    ]:
        marker = f'        compose.onNodeWithTag("nearby_art_{mode}", useUnmergedTree = true).assertIsDisplayed()'
        require_one(tests, marker, f"{mode} test")
        require_one(tests, following, f"{mode} heading test")
        start = tests.index(marker)
        end = tests.index(following, start)
        part = tests[start:end]
        for tag in ("nearby_art_motif", "nearby_art_fox", "nearby_art_owl"):
            old = f'compose.onNodeWithTag("{tag}", useUnmergedTree = true).assertExists()'
            new = f'compose.onNodeWithTag("{tag}", useUnmergedTree = true).assertDoesNotExist()'
            require_one(part, old, f"{mode} {tag} assertion")
            part = part.replace(old, new, 1)
        tests = tests[:start] + part + tests[end:]
    return tests


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("artpack", type=Path, help="Directory extracted from V5 review ZIP")
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()
    assets = verified_assets(args.artpack.resolve())
    banner = patch_banner(BANNER.read_text(encoding="utf-8"))
    tests = patch_tests(TEST.read_text(encoding="utf-8"))
    if args.dry_run:
        print("PASS: 2 HD PNGs verified; Kotlin and UI-test patch anchors verified.")
        print("DRY RUN: no source or resource files modified.")
        return
    for path, data in assets:
        path.parent.mkdir(parents=True, exist_ok=True)
        if not path.exists():
            path.write_bytes(data)
    BANNER.write_text(banner, encoding="utf-8")
    TEST.write_text(tests, encoding="utf-8")
    print("Applied V5 Co-op/Versus images and removed duplicate mascot layers.")
    print("Next: commit binaries+source+tests, build reviewer APK, run Android 16 UI QA.")


if __name__ == "__main__":
    main()
