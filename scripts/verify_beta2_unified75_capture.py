#!/usr/bin/env python3
"""Reject fake/empty Android captures before publishing unified 75 APK evidence."""
import hashlib
import struct
from pathlib import Path

ids = (
    "woodland_chipmunk", "woodland_beaver", "woodland_woodpecker", "woodland_skunk",
    "ocean_seahorse", "ocean_dolphin", "ocean_seashell", "ocean_clownfish",
)
seen = set()
for identifier in ids:
    path = Path(f"unified75-{identifier}-android.png")
    payload = path.read_bytes()
    if len(payload) < 500 or payload[:8] != b"\x89PNG\r\n\x1a\n" or payload[12:16] != b"IHDR":
        raise SystemExit(f"FAIL: invalid PNG/adb error at {path} ({len(payload)} bytes)")
    w, h = struct.unpack(">II", payload[16:24])
    if w < 80 or h < 80:
        raise SystemExit(f"FAIL: unexpected screenshot dimensions {path}: {w}x{h}")
    digest = hashlib.sha256(payload).hexdigest()
    if digest in seen:
        raise SystemExit(f"FAIL: duplicate screenshot file at {path}")
    seen.add(digest)
    print(f"PASS {identifier}: screenshot {w}x{h}, {len(payload)} bytes, sha256={digest}")
print("PASS all eight distinct, actual Android PNG portrait screenshots")
