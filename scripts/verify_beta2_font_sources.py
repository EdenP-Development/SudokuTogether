#!/usr/bin/env python3
"""Verify exact pinned OFL font binaries and digit coverage before Android build.

Requires fontTools; never modifies fonts or exports files to end users.
"""
from __future__ import annotations

import hashlib
from pathlib import Path
import zipfile
from fontTools.ttLib import TTFont

ROOT = Path(__file__).resolve().parents[1]
FONTS = (
    ("typewriter_courier_prime", "courierprime", "2a0f8bde9778bb8c7b3a0ca5d4b4fb041737ef1a", "Courier"),
    ("editorial_literata", "literata", "0a81b4984a4a4eb101e0ddc28babc85bb137efef", "Literata"),
    ("soft_print_atkinson_next", "atkinsonhyperlegiblenext", "92a357554bdcca62a7e80d5e3724bf4eb8932e5e", "Atkinson"),
    ("tall_print_oswald", "oswald", "d1a3b9cb1325bf20b3a06ef9849d21c411212a3b", "Oswald"),
)

def git_sha(contents: bytes) -> str:
    obj = b"blob " + str(len(contents)).encode() + b"\0" + contents
    return hashlib.sha1(obj).hexdigest()

def verify_sources() -> None:
    existing = set()
    for resource, folder, pinned_git_sha, expected_name in FONTS:
        path = ROOT / "app/src/main/res/font" / f"{resource}.ttf"
        blob = path.read_bytes()
        if git_sha(blob) != pinned_git_sha:
            raise ValueError(f"Changed official font bytes in {path}")
        if blob[:4] not in (b"\x00\x01\x00\x00", b"OTTO"):
            raise ValueError(f"Invalid TTF/OpenType signature: {path}")
        font = TTFont(path)
        cmap = font.getBestCmap() or {}
        missing = [str(n) for n in range(10) if 48+n not in cmap]
        if missing:
            raise ValueError(f"{resource}: missing numerals: {','.join(missing)}")
        name = font["name"].getDebugName(1) or ""
        if expected_name.lower() not in name.lower():
            raise ValueError(f"Unexpected font family {resource}: {name}")
        digest = hashlib.sha256(blob).hexdigest()
        if digest in existing:
            raise ValueError(f"Duplicate font file: {resource}")
        existing.add(digest)
        ofl = ROOT / "app/src/main/assets/licenses/fonts" / folder / "OFL.txt"
        license_text = ofl.read_text(encoding="utf-8")
        if "SIL OPEN FONT LICENSE" not in license_text.upper():
            raise ValueError(f"{folder}: no in-APK SIL Open Font License")
        print(f"PASS {resource}: {len(blob)} bytes, family {name}, digits 0–9, sha256 {digest}")
    print("PASS four distinct original fonts and four bundled OFL notices")

def verify_apk(apk: Path) -> None:
    with zipfile.ZipFile(apk) as package:
        names = set(package.namelist())
        for resource, folder, _, _ in FONTS:
            # Android AAPT may flatten or repackage resources; use directory+extension.
            matches = [n for n in names if n.startswith("res/font/") and resource in n and n.endswith(".ttf")]
            if not matches:
                raise ValueError(f"Missing Android packaged font: {resource}")
            license_path = f"assets/licenses/fonts/{folder}/OFL.txt"
            if license_path not in names:
                raise ValueError(f"Missing in-APK font license {license_path}")
        print(f"PASS: all four font TTFs and licenses packaged in {apk.name}")

if __name__ == "__main__":
    import sys
    verify_sources()
    if len(sys.argv) > 1:
        verify_apk(Path(sys.argv[1]))
