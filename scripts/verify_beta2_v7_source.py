#!/usr/bin/env python3
"""Reject tampered original Versus V7 art and broken icon resources."""
import hashlib
import json
import struct
from pathlib import Path
from xml.etree import ElementTree as ET

root = Path(__file__).resolve().parents[1]
manifest = json.loads((root / "design/versus-v7/manifest.json").read_text())
png_magic = b"\x89PNG\r\n\x1a\n"
for entry in (manifest["source_scene"], manifest["output"]):
    path = root / entry["path"]
    data = path.read_bytes()
    assert data.startswith(png_magic) and data[12:16] == b"IHDR", path
    assert struct.unpack(">II", data[16:24]) == (1774, 887), path
    assert len(data) == entry["bytes"], path
    assert hashlib.sha256(data).hexdigest() == entry["sha256"], path
    print("PASS original Versus V7 artwork", entry["path"])
assert manifest["composition"]["all_titles_native_compose"]
assert manifest["composition"]["decorative_boards"]["rows"] == 9
assert manifest["composition"]["decorative_boards"]["columns"] == 9

android_ns = "{http://schemas.android.com/apk/res/android}"
fg = ET.parse(root / "app/src/main/res/drawable/ic_launcher_foreground.xml").getroot()
legacy = ET.parse(root / "app/src/main/res/drawable/ic_launcher.xml").getroot()
mono = ET.parse(root / "app/src/main/res/drawable/ic_launcher_monochrome.xml").getroot()
adaptive = ET.parse(root / "app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml").getroot()
assert fg.tag == legacy.tag == mono.tag == "vector"
assert adaptive.tag == "adaptive-icon"
assert adaptive.find("foreground").attrib[android_ns + "drawable"] == "@drawable/ic_launcher_foreground"
assert adaptive.find("monochrome").attrib[android_ns + "drawable"] == "@drawable/ic_launcher_monochrome"
assert adaptive.find("background").attrib[android_ns + "drawable"] == "@color/icon_background"
assert fg.attrib[android_ns + "viewportWidth"] == "108"
assert fg.attrib[android_ns + "viewportHeight"] == "108"
assert len(list(fg.iter("path"))) >= 7
assert len(list(mono.iter("path"))) >= 2
assert (root / "design/launcher-icon-v2/source-beta1-foreground.xml").exists()
assert (root / "design/launcher-icon-v2/source-beta1-legacy.xml").exists()
for name in ("gm_mode_coop_woodland_v5_hd.png", "gm_mode_versus_woodland_v5_hd.png"):
    assert (root / "app/src/main/res/drawable-nodpi" / name).is_file()
print("PASS launcher icon foreground/legacy/monochrome structure, original art backups and Co-op V5 preservation")
