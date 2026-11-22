#!/usr/bin/env python3
"""Validate actual emulator V7 and Co-op screenshots, not adb error text."""
from pathlib import Path
from hashlib import sha256
from struct import unpack
paths = [Path("beta2-v7-versus-android.png"),Path("beta2-v7-coop-android.png")]
digests = set()
for p in paths:
    content = p.read_bytes()
    if not content.startswith(b"\x89PNG\r\n\x1a\n") or content[12:16] != b"IHDR" or len(content) < 12000:
        raise SystemExit(f"FAIL: {p} is not a real Android PNG")
    w,h = unpack(">II", content[16:24])
    if w < 320 or h < 500: raise SystemExit(f"FAIL: {p} too small at {w}x{h}")
    digests.add(sha256(content).digest())
    print(f"PASS {p}: {w}x{h}, {len(content)} bytes")
if len(digests) != len(paths): raise SystemExit("FAIL: Versus and Co-op images identical")
print("PASS actual distinct Android Versus and Co-op screenshots")
