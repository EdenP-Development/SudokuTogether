#!/usr/bin/env python3
"""Technical intake QA for seven prospective GridMates cosmetic portrait PNGs.

Usage: python scripts/validate_collection_sprite_intake.py path/to/candidates
Checks dimensions, transparency, occupancy, center alignment and duplicate assets.
This does NOT approve artwork or measure subjective illustration-style fidelity.
"""
import argparse
import hashlib
import json
from pathlib import Path

from PIL import Image

CANDIDATES = (
    "farm_goat", "farm_duck", "farm_barn",
    "garden_snail", "garden_daisy", "garden_mushroom",
    "ocean_sea_star",
)


def inspect_file(path: Path) -> dict:
    if not path.is_file():
        return {"file": path.name, "passed": False, "errors": ["missing file"]}
    errors = []
    sha = hashlib.sha256(path.read_bytes()).hexdigest()
    try:
        with Image.open(path) as image:
            image_format, mode, size = image.format, image.mode, image.size
            if image_format != "PNG":
                errors.append("not PNG format")
            if size != (512, 512):
                errors.append(f"expected 512x512, got {size}")
            if mode != "RGBA":
                errors.append(f"expected RGBA transparency, got {mode}")
            rgba = image.convert("RGBA")
    except Exception as exc:
        return {"file": path.name, "sha256": sha, "passed": False,
                "errors": [f"image unreadable: {exc}"]}

    alpha = rgba.getchannel("A")
    bbox = alpha.point(lambda value: 255 if value >= 64 else 0).getbbox()
    if bbox is None:
        errors.append("no visible foreground")
        return {"file": path.name, "sha256": sha, "passed": False,
                "errors": errors}
    width, height = bbox[2] - bbox[0], bbox[3] - bbox[1]
    extent = [round(width / 512, 3), round(height / 512, 3)]
    if max(extent) < 0.72:
        errors.append("foreground too small for collection thumbnails")
    if max(extent) > 0.91:
        errors.append("foreground may clip in circular Collection mask")
    if min(extent) < 0.59:
        errors.append("foreground unusually narrow/short")
    margins = [bbox[0], bbox[1], 512 - bbox[2], 512 - bbox[3]]
    if min(margins) < 15:
        errors.append("less than 15px safe margin")
    corners = ((0, 0), (511, 0), (0, 511), (511, 511))
    if max(alpha.getpixel(point) for point in corners) > 1:
        errors.append("canvas corner is not transparent")
    center_x, center_y = (bbox[0] + bbox[2]) / 2, (bbox[1] + bbox[3]) / 2
    if abs(center_x - 256) > 32 or abs(center_y - 256) > 32:
        errors.append("foreground off-center")
    return {"file": path.name, "sha256": sha, "size": list(size), "mode": mode,
            "visible_bbox": list(bbox), "visible_extent": extent,
            "passed": not errors, "errors": errors}


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("directory", type=Path)
    parser.add_argument("--output", type=Path, help="optional JSON audit output")
    args = parser.parse_args()
    checks = [inspect_file(args.directory / f"{name}_candidate.png") for name in CANDIDATES]
    hashes = [row.get("sha256") for row in checks if row.get("sha256")]
    if len(hashes) != len(set(hashes)):
        for row in checks:
            if row.get("sha256") and hashes.count(row["sha256"]) > 1:
                row["errors"].append("duplicate file content")
                row["passed"] = False
    passed = all(row["passed"] for row in checks)
    report = {
        "passed": passed,
        "note": "Geometry/transparency only. Canonical-art visual comparison and owner approval required.",
        "assets": checks,
    }
    if args.output:
        args.output.parent.mkdir(parents=True, exist_ok=True)
        args.output.write_text(json.dumps(report, indent=2) + "\n")
    for row in checks:
        print(("PASS" if row["passed"] else "FAIL") + ": " + row["file"] +
              (" " + "; ".join(row["errors"]) if row["errors"] else ""))
    print("OVERALL:", "PASS" if passed else "FAIL", "- artwork NOT automatically approved")
    return 0 if passed else 1


if __name__ == "__main__":
    raise SystemExit(main())
