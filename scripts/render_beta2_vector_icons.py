#!/usr/bin/env python3
"""Reproducibly render 8 review-only GridMates SVG icons to Android RGBA PNGs.

CI renders PNG assets before Android build. New Ocean SVGs are candidate vector
redraws of the existing owner-review art, NOT byte-identical to original PNGs.
Woodland SVGs are preserved source drawings. No public release may use these
without owner visual approval.
"""
from __future__ import annotations

import hashlib
import io
import json
from pathlib import Path
import xml.etree.ElementTree as ET

import cairosvg
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
SOURCES = ROOT / "design/beta2-vector-review"
DEST = ROOT / "app/src/main/res/drawable-nodpi"
REPORT = ROOT / "build/beta2-unified75-render-manifest.json"
IDS = (
    "woodland_chipmunk", "woodland_beaver", "woodland_woodpecker",
    "woodland_skunk", "ocean_seahorse", "ocean_dolphin",
    "ocean_seashell", "ocean_clownfish",
)
ALLOWED = {"svg", "g", "path", "ellipse", "circle", "rect"}
SVG_NS = "{http://www.w3.org/2000/svg}"


def inspect_svg(source: bytes, identifier: str) -> None:
    if len(source) > 50000 or b"<!DOCTYPE" in source.upper():
        raise ValueError(f"{identifier}: invalid SVG source")
    root = ET.fromstring(source)
    if root.tag != SVG_NS + "svg":
        raise ValueError(f"{identifier}: invalid SVG root")
    for node in root.iter():
        local = node.tag.rsplit("}", 1)[-1]
        if local not in ALLOWED:
            raise ValueError(f"{identifier}: unsafe/unsupported SVG element: {local}")
        if any("href" in key.lower() or "url(" in value.lower()
               for key, value in node.attrib.items()):
            raise ValueError(f"{identifier}: external references not permitted")
    if root.attrib.get("viewBox") != "0 0 512 512":
        raise ValueError(f"{identifier}: requires 512-square artboard")


def main() -> None:
    DEST.mkdir(parents=True, exist_ok=True)
    rows = []
    all_hashes = set()
    for identifier in IDS:
        source_path = SOURCES / f"{identifier}.svg"
        src = source_path.read_bytes()
        inspect_svg(src, identifier)
        rendered = cairosvg.svg2png(bytestring=src, output_width=512,
                                   output_height=512)
        with Image.open(io.BytesIO(rendered)) as image:
            output = image.convert("RGBA")
        if output.size != (512, 512):
            raise ValueError(f"{identifier}: wrong rendered PNG dimensions")
        alpha = output.getchannel("A")
        if alpha.getextrema()[0] != 0 or alpha.getbbox() is None:
            raise ValueError(f"{identifier}: background not transparent or artwork empty")
        bbox = alpha.point(lambda v: 255 if v >= 12 else 0).getbbox()
        if bbox is None or min(bbox[0], bbox[1], 512-bbox[2], 512-bbox[3]) < 14:
            raise ValueError(f"{identifier}: clipped or too close to canvas edge: {bbox}")
        output_stream = io.BytesIO()
        output.save(output_stream, format="PNG", optimize=True)
        data = output_stream.getvalue()
        digest = hashlib.sha256(data).hexdigest()
        if digest in all_hashes:
            raise ValueError(f"Duplicate artwork: {identifier}")
        all_hashes.add(digest)
        path = DEST / f"{identifier}.png"
        if path.exists() and path.read_bytes() != data:
            raise FileExistsError(f"{identifier}: existing asset differs; refusing to overwrite")
        path.write_bytes(data)
        rows.append({
            "id": identifier,
            "status": "REVIEW_ONLY",
            "drawable": str(path.relative_to(ROOT)),
            "source": str(source_path.relative_to(ROOT)),
            "source_sha256": hashlib.sha256(src).hexdigest(),
            "sha256": digest,
            "bytes": len(data),
            "dimensions": [512,512],
            "mode": "RGBA",
            "alpha_bbox": list(bbox),
        })
        print(f"PASS {identifier}: PNG RGBA 512x512 {len(data)}B {digest}")
    REPORT.parent.mkdir(parents=True, exist_ok=True)
    REPORT.write_text(json.dumps({
        "status": "UNAPPROVED_VECTOR_REVIEW_ONLY",
        "count": len(rows),
        "note": "Ocean candidate vector redraws require comparison with original review-pack PNGs before any final merge.",
        "items": rows,
    }, indent=2) + "\n", encoding="utf-8")
    print("PASS: all eight independent RGBA sprites rendered reproducibly")


if __name__ == "__main__":
    main()
