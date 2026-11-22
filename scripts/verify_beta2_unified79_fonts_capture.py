#!/usr/bin/env python3
"""Strict Android PNG evidence for four final GridMates number font previews."""
import hashlib
import struct
from pathlib import Path

digests = set()
for identifier in ("typewriter", "editorial", "soft_print", "tall_print"):
    file = Path(f"unified79-font-{identifier}-android.png")
    data = file.read_bytes()
    if len(data) < 2000 or data[:8] != b"\x89PNG\r\n\x1a\n" or data[12:16] != b"IHDR":
        raise SystemExit(f"FAIL {file}: missing/invalid image; may contain adb error text")
    width, height = struct.unpack(">II", data[16:24])
    if width < 300 or height < 300:
        raise SystemExit(f"FAIL {file}: unexpectedly small {width}x{height}")
    sha = hashlib.sha256(data).hexdigest()
    if sha in digests:
        raise SystemExit(f"FAIL {file}: duplicate screenshots")
    digests.add(sha)
    print(f"PASS {identifier}: Android screenshot {width}x{height}, {len(data)} bytes, {sha}")
print("PASS four distinct real Android number-font preview screenshots")
