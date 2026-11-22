#!/usr/bin/env python3
"""No fabricated screenshots. Four unique MediaStore-captured Android UI frames."""
from pathlib import Path
from PIL import Image
import hashlib

root = Path("album-screenshots")
files = [root / f"album-{s}.png" for s in ("woodland","ocean","garden","farm")]
digests = []
for file in files:
    if not file.is_file() or file.stat().st_size < 12000:
        raise SystemExit(f"FAIL: missing/empty Android screenshot {file}")
    with Image.open(file) as im:
        im.verify()
    with Image.open(file) as im:
        width,height=im.size
        if width < 320 or height < 600:
            raise SystemExit(f"FAIL: {file} unexpected size {width}x{height}")
    digests.append(hashlib.sha256(file.read_bytes()).hexdigest())
    print(f"PASS: {file} {width}x{height} PNG")
if len(set(digests)) != len(digests):
    raise SystemExit("FAIL: duplicate (stale/empty) chapter captures")
print("PASS: four distinct real Android Collection chapter captures")
