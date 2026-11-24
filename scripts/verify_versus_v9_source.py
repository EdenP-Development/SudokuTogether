#!/usr/bin/env python3
"""Pin exact dedicated V9 source and keep unapproved art out of signed release."""
import argparse, hashlib, json, struct
from pathlib import Path

root = Path(__file__).resolve().parents[1]
p = argparse.ArgumentParser()
p.add_argument("--release", action="store_true")
args = p.parse_args()
m = json.loads((root / "design/versus-v9/manifest.json").read_text())
asset = root / m["path"]
approved = "ownerApproved=true" in (root / "design/versus-v9/release.properties").read_text().splitlines()
if args.release and not approved:
    raise SystemExit("BLOCKED: owner visual approval and approved art promotion are required")
if not asset.exists():
    if args.release or approved:
        raise SystemExit("BLOCKED: exact V9 artwork is missing")
    print("V9 source not imported: fallback reviewer only, NOT release eligible.")
else:
    data = asset.read_bytes()
    assert data[:8] == b"\x89PNG\r\n\x1a\n"
    assert struct.unpack(">II", data[16:24]) == (m["width"], m["height"])
    assert len(data) == m["size_bytes"] and hashlib.sha256(data).hexdigest() == m["sha256"], "V9 source differs from reviewed asset"
    print("PASS: exact dedicated V9 image source")
