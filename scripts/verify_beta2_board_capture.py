#!/usr/bin/env python3
"""Fail Android QA if a claimed screenshot is actually adb error text.

Run from the repo root after the Android emulator export. Uses only stdlib.
"""
import hashlib
import struct
from pathlib import Path

EXPECTED = ["moss", "rosewood", "pebble", "tidal"]
PNG_SIGNATURE = b"\x89PNG\r\n\x1a\n"
digests = set()
for label in EXPECTED:
    path = Path(f"beta2-board-{label}-android.png")
    blob = path.read_bytes()
    if len(blob) < 500 or blob[:8] != PNG_SIGNATURE or blob[12:16] != b"IHDR":
        raise SystemExit(f"FAIL: {path} is not a nontrivial PNG ({len(blob)} bytes)")
    width, height = struct.unpack(">II", blob[16:24])
    if width < 90 or height < 90:
        raise SystemExit(f"FAIL: {path} has unexpected {width}x{height} image size")
    digest = hashlib.sha256(blob).hexdigest()
    if digest in digests:
        raise SystemExit(f"FAIL: duplicate preview content in {path}")
    digests.add(digest)
    print(f"PASS: {label} Android screenshot {width}x{height}, {len(blob)} bytes, sha256={digest}")
print("PASS: four actual, distinct Android PNG board screenshots")
