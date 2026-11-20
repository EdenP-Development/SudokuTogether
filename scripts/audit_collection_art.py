#!/usr/bin/env python3
"""GridMates Collection image inventory + fixed-scale contact sheets.

This tool INSPECTS art, never rewrites source images or game saves.
Requires Pillow. From the repo root:
  python scripts/audit_collection_art.py \
    --expected design/collection-audit/beta2-target-portrait-ids.json \
    --output build/collection-audit
Add --strict when the final integrated 43-icon candidate is ready.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import math
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageOps

FAMILIES = ("Woodland", "Ocean", "Garden", "Farm")
SIZES = (30, 48, 72)
SURFACES = {"cream": "#F7F4EC", "forest": "#214F3F"}
CELL_W, CELL_H, COLS = 154, 150, 6


def read_expected(path: Path | None) -> dict[str, list[str]]:
    if path is None:
        return {family: [] for family in FAMILIES}
    data = json.loads(path.read_text(encoding="utf-8"))
    families = data.get("families", {})
    if set(families) != set(FAMILIES):
        raise ValueError(f"Expected exactly {FAMILIES} in manifest")
    names = [item for family in FAMILIES for item in families[family]]
    if len(names) != len(set(names)):
        raise ValueError("Duplicate expected portrait IDs")
    for family in FAMILIES:
        prefix = family.lower() + "_"
        if any(not item.startswith(prefix) for item in families[family]):
            raise ValueError(f"Wrong ID prefix in {family}")
    return families


def alpha_bbox(image: Image.Image):
    alpha = image.getchannel("A").point(lambda value: 255 if value >= 12 else 0)
    return alpha.getbbox()


def edge_contacts(image: Image.Image) -> int:
    alpha = image.getchannel("A")
    w, h = image.size
    # Avoid counting a corner twice; tiny stray edge pixels warrant inspection.
    return sum(v >= 12 for v in alpha.crop((0, 0, w, 1)).getdata()) + \
        sum(v >= 12 for v in alpha.crop((0, h - 1, w, h)).getdata()) + \
        sum(v >= 12 for v in alpha.crop((0, 1, 1, h - 1)).getdata()) + \
        sum(v >= 12 for v in alpha.crop((w - 1, 1, w, h - 1)).getdata())


def centered_label(draw: ImageDraw.ImageDraw, x: int, y: int,
                   label: str, font, color: str):
    box = draw.textbbox((0, 0), label, font=font)
    width = box[2] - box[0]
    draw.text((x - width // 2, y), label, fill=color, font=font)


def make_sheet(ordered: dict[str, list[dict]], output: Path,
               size: int, surface_name: str, surface_color: str):
    section_h = 25
    total_rows = sum(math.ceil(len(ordered[family]) / COLS)
                     for family in FAMILIES)
    width = CELL_W * COLS
    height = 30 + len(FAMILIES) * section_h + total_rows * CELL_H + 20
    sheet = Image.new("RGB", (width, height), surface_color)
    draw = ImageDraw.Draw(sheet)
    font = ImageFont.load_default()
    on_dark = surface_name == "forest"
    text_color = "#F8F5ED" if on_dark else "#214F3F"
    subtle = "#E1D4BC" if on_dark else "#E6D9C3"
    y = 15
    centered_label(draw, width // 2, y, f"GridMates Collection - {size}px / {surface_name}", font, text_color)
    y += 25

    for family in FAMILIES:
        draw.text((16, y + 5), f"{family} ({len(ordered[family])})",
                  font=font, fill=text_color)
        y += section_h
        for offset, item in enumerate(ordered[family]):
            col, row = offset % COLS, offset // COLS
            bx, by = col * CELL_W, y + row * CELL_H
            draw.rounded_rectangle((bx + 6, by + 4, bx + CELL_W - 6,
                                    by + CELL_H - 6), radius=9,
                                   outline=subtle, width=1)
            with Image.open(item["path"]) as source:
                portrait = ImageOps.contain(source.convert("RGBA"),
                                            (size, size),
                                            Image.Resampling.LANCZOS)
            px = bx + (CELL_W - portrait.width) // 2
            py = by + 15 + (90 - portrait.height) // 2
            sheet.paste(portrait, (px, py), portrait)
            short_name = item["id"].split("_", 1)[1].replace("_", " ")
            centered_label(draw, bx + CELL_W // 2, by + 111,
                           short_name.title(), font, text_color)
        y += math.ceil(len(ordered[family]) / COLS) * CELL_H
    sheet.save(output)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--assets", type=Path,
                        default=Path("app/src/main/res/drawable-nodpi"))
    parser.add_argument("--expected", type=Path)
    parser.add_argument("--output", type=Path,
                        default=Path("build/collection-audit"))
    parser.add_argument("--strict", action="store_true",
                        help="Fail on missing expected art, empty image or exact duplicate bytes")
    args = parser.parse_args()

    args.output.mkdir(parents=True, exist_ok=True)
    expected = read_expected(args.expected)
    all_expected = {name for items in expected.values() for name in items}
    findings = []
    ordered = {family: [] for family in FAMILIES}
    hash_first_seen = {}
    hard_errors = []
    actual_ids = set()

    for family in FAMILIES:
        prefix = family.lower() + "_"
        files = sorted(args.assets.glob(prefix + "*.png"))
        by_id = {p.stem: p for p in files}
        ordered_names = [name for name in expected[family] if name in by_id]
        ordered_names += sorted(name for name in by_id if name not in ordered_names)
        for asset_id in ordered_names:
            path = by_id[asset_id]
            digest = hashlib.sha256(path.read_bytes()).hexdigest()
            with Image.open(path) as source:
                original_mode = source.mode
                original_size = list(source.size)
                rgba = source.convert("RGBA")
                bbox = alpha_bbox(rgba)
                contacts = edge_contacts(rgba)
                had_alpha = "A" in source.getbands()
            warnings = []
            if not had_alpha:
                warnings.append("no_alpha_channel")
            if original_size[0] != original_size[1]:
                warnings.append("not_square")
            # Legacy canonical portraits can legitimately be 384px; new
            # generation uses 512px. Never resize originals here.
            if original_size[0] not in (384, 512):
                warnings.append("nonstandard_source_resolution_review")
            if bbox is None:
                warnings.append("empty_image")
                hard_errors.append(f"{asset_id}: transparent/empty")
            else:
                occupancy = round(max((bbox[2]-bbox[0]) / original_size[0],
                                      (bbox[3]-bbox[1]) / original_size[1]), 3)
                if occupancy < 0.50 or occupancy > 0.99:
                    warnings.append("visual_scale_review")
            if contacts > 0:
                warnings.append("alpha_touches_canvas_edge")
            if digest in hash_first_seen:
                warnings.append(f"same_bytes_as:{hash_first_seen[digest]}")
                hard_errors.append(f"{asset_id}: duplicates {hash_first_seen[digest]}")
            else:
                hash_first_seen[digest] = asset_id
            record = {
                "id": asset_id, "family": family, "path": str(path),
                "dimensions": original_size, "mode": original_mode,
                "alpha_bbox": bbox, "edge_contact_pixels": contacts,
                "sha256": digest, "warnings": warnings
            }
            actual_ids.add(asset_id)
            ordered[family].append(record)
            findings.append(record)

    missing = sorted(all_expected - actual_ids)
    extras = sorted(actual_ids - all_expected) if args.expected else []
    if args.strict and missing:
        hard_errors.append(f"Missing expected portraits: {', '.join(missing)}")
    for size in SIZES:
        for surface_name, surface_color in SURFACES.items():
            make_sheet(ordered, args.output / f"contact-{size}px-{surface_name}.png",
                       size, surface_name, surface_color)

    report = {
        "purpose": "Pixel/asset pre-audit only; manual art cohesion and Android UX review remain mandatory.",
        "counts": {family: len(ordered[family]) for family in FAMILIES},
        "total_actual": len(findings),
        "total_expected": len(all_expected) if args.expected else None,
        "missing_expected_ids": missing,
        "unplanned_ids": extras,
        "findings": findings,
        "hard_errors": hard_errors
    }
    (args.output / "asset-report.json").write_text(
        json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({
        "actual": report["total_actual"], "expected": report["total_expected"],
        "missing": len(missing), "warnings": sum(bool(x["warnings"]) for x in findings),
        "hard_errors": len(hard_errors),
        "output": str(args.output)
    }, indent=2))
    raise SystemExit(1 if args.strict and hard_errors else 0)


if __name__ == "__main__":
    main()
