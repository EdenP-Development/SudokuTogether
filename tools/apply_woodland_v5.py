#!/usr/bin/env python3
"""Verify and import the two original HD V5 images from an extracted review ZIP.

The source integration has already been committed to the V5 draft branch.
This script copies ONLY verified image assets. It never patches Kotlin,
removes Collection portraits, or touches release-signing configuration.

Usage:
    python tools/apply_woodland_v5.py /path/to/unzipped/V5_pack --dry-run
    python tools/apply_woodland_v5.py /path/to/unzipped/V5_pack
"""
from __future__ import annotations

import argparse
import hashlib
import json
import struct
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MANIFEST = ROOT / "design/ui-art/v5/art_manifest.json"
BANNER = ROOT / "app/src/main/java/com/playereden/sudokutogether/NearbyModeBanner.kt"
TEST = ROOT / "app/src/androidTest/java/com/playereden/sudokutogether/HomeLivingBookUiTest.kt"
PNG_MAGIC = bytes((137, 80, 78, 71, 13, 10, 26, 10))


def validate_source_integration() -> None:
    code = BANNER.read_text(encoding="utf-8")
    test = TEST.read_text(encoding="utf-8")
    required_code = (
        'gm_mode_coop_woodland_v5_hd',
        'gm_mode_versus_woodland_v5_hd',
        'val hasV5Art = distinctAssetId != 0',
        'testTag("nearby_art_v5")',
        'if (hasV5Art)',
        'R.drawable.gm_woodland_storybook_hd_v4',
    )
    for marker in required_code:
        if marker not in code:
            raise RuntimeError(f"V5 integration missing from Android source: {marker}")
    if test.count('assertModeArtDoesNotDuplicateMascots()') < 3:
        # One function declaration + two separate mode assertions.
        raise RuntimeError("V5 instrumentation regression checks are missing")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("artpack", type=Path,
                        help="Directory extracted from the V5 Complete Review Pack")
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()
    validate_source_integration()

    entries = json.loads(MANIFEST.read_text(encoding="utf-8"))["new_assets"]
    pending: list[tuple[Path, bytes]] = []
    for spec in entries:
        src = args.artpack.resolve() / spec["resource"]
        dst = ROOT / spec["resource"]
        if not src.is_file():
            raise RuntimeError(f"Missing original image: {src}")
        data = src.read_bytes()
        digest = hashlib.sha256(data).hexdigest()
        if digest != spec["sha256"]:
            raise RuntimeError(f"SHA-256 mismatch for {src}: {digest}")
        if len(data) != spec["bytes"]:
            raise RuntimeError(f"Byte length mismatch for {src}")
        if data[:8] != PNG_MAGIC or data[12:16] != b"IHDR":
            raise RuntimeError(f"Invalid PNG header: {src}")
        if struct.unpack(">II", data[16:24]) != (spec["width"], spec["height"]):
            raise RuntimeError(f"Wrong image dimensions: {src}")
        if dst.exists() and dst.read_bytes() != data:
            raise RuntimeError(f"Destination differs; refusing overwrite: {dst}")
        pending.append((dst, data))
        print(f"PASS {src.name}: {len(data):,} bytes, {digest}")

    if args.dry_run:
        print("Dry-run passed. No files changed.")
        return
    for dst, data in pending:
        dst.parent.mkdir(parents=True, exist_ok=True)
        if not dst.exists():
            dst.write_bytes(data)
            print(f"Imported {dst.relative_to(ROOT)}")
        else:
            print(f"Already present (exact checksum): {dst.relative_to(ROOT)}")
    print("V5 art import complete. Next: commit both images, run Android QA.")


if __name__ == "__main__":
    main()
