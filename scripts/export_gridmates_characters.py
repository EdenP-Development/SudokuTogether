#!/usr/bin/env python3
"""Export approved GridMates animal artwork for use by sibling games.

Uses only committed approved Android portrait PNGs; no game progress, signed
APKs, login data, or font binaries. Generated zip is an offline development
asset pack, not a runtime service.

Usage:
  python3 scripts/export_gridmates_characters.py --output /tmp/gridmates-characters.zip
"""
from __future__ import annotations

import argparse
import hashlib
import json
import struct
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ART = ROOT / "app/src/main/res/drawable-nodpi"
GROUPS = {
    "Woodland": (
        "owl", "fox", "rabbit", "bear", "badger", "hedgehog",
        "robin", "deer", "squirrel", "otter", "tortoise", "cat",
    ),
    "Ocean": (
        "octopus", "seal", "hermit_crab", "manta_ray", "jellyfish", "narwhal",
    ),
    "Garden": ("honeybee", "ladybug", "butterfly", "dragonfly"),
}
PREFIX = {"Woodland": "woodland", "Ocean": "ocean", "Garden": "garden"}
PACK_VERSION = "1.0.0"
PNG_MAGIC = bytes((137, 80, 78, 71, 13, 10, 26, 10))


def build_manifest():
    assets = []
    buffers = []
    for family, animals in GROUPS.items():
        for animal in animals:
            asset_id = PREFIX[family] + "_" + animal
            src = ART / (asset_id + ".png")
            if not src.is_file():
                raise FileNotFoundError(f"Missing approved portrait: {src}")
            data = src.read_bytes()
            if data[:8] != PNG_MAGIC or data[12:16] != b"IHDR":
                raise ValueError(f"Malformed portrait PNG: {src}")
            width, height = struct.unpack(">II", data[16:24])
            if width != height or width < 64:
                raise ValueError(f"Expected square portrait >=64px: {src} ({width}x{height})")
            if data[25] not in (4, 6):
                raise ValueError(f"Expected PNG with alpha channel: {src}")
            name = f"portraits/{asset_id}.png"
            assets.append({
                "asset_id": asset_id,
                "collection": family,
                "style": "gridmates_chibi_v1",
                "source_path": str(src.relative_to(ROOT)),
                "package_path": name,
                "width": width,
                "height": height,
                "sha256": hashlib.sha256(data).hexdigest(),
                "bytes": len(data),
                "status": "approved_existing_collection",
            })
            buffers.append((name, data))
    if len(assets) != 22 or len(set(a["asset_id"] for a in assets)) != 22:
        raise AssertionError("Approved baseline portrait count or ID uniqueness changed")
    manifest = {
        "schema": "gridmates-characters-offline-v1",
        "version": PACK_VERSION,
        "style": "gridmates_chibi_v1",
        "source_game": "GridMates: Sudoku",
        "content": "artwork only; no cosmetic unlock ownership or game save data",
        "asset_count": len(assets),
        "assets": assets,
    }
    return manifest, buffers


def export(out):
    manifest, images = build_manifest()
    out.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(out, "w", compression=zipfile.ZIP_DEFLATED, compresslevel=6) as pack:
        for name, data in images:
            pack.writestr(name, data)
        pack.writestr("gridmates-art-manifest.json",
                      json.dumps(manifest, indent=2, ensure_ascii=False) + "\n")
    with zipfile.ZipFile(out) as pack:
        content = json.loads(pack.read("gridmates-art-manifest.json"))
        for asset in content["assets"]:
            body = pack.read(asset["package_path"])
            if hashlib.sha256(body).hexdigest() != asset["sha256"]:
                raise ValueError(f"Corrupt export: {asset['asset_id']}")
    print(f"Exported {manifest['asset_count']} approved portraits to {out}")
    print("Games may choose independent unlock rules; file hashes are pinned.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, required=True)
    export(parser.parse_args().output)
