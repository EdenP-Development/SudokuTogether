#!/usr/bin/env python3
"""Capture actual Co-op and Versus Android screens from the installed isolated QA APK.

Requires a running emulator and the launched Sudoku Woodland V5 app.
This is *not* a composited design mockup. Fail closed if navigation fails.
"""
import hashlib
import json
import re
import struct
import subprocess
import time
import xml.etree.ElementTree as ET
from pathlib import Path

PACKAGE = "com.playereden.sudokutogether.qa.woodlandv5"
DUMP = "/sdcard/gridmates-nearby-window.xml"
OUTPUT = Path(".")
PNG_MAGIC = b"\x89PNG\r\n\x1a\n"


def adb(*args, capture=True):
    return subprocess.run(["adb", *args], check=True, capture_output=capture)


def hierarchy():
    adb("shell", "uiautomator", "dump", DUMP)
    xml = adb("exec-out", "cat", DUMP).stdout
    root = ET.fromstring(xml)
    parents = {child: parent for parent in root.iter() for child in parent}
    return root, parents, xml


def selected_node(root, parents, label):
    matches = []
    for node in root.iter("node"):
        words = " ".join((node.attrib.get("text", ""), node.attrib.get("content-desc", ""))).lower()
        if label.lower() not in words:
            continue
        clickable = node
        while clickable is not None and clickable.attrib.get("clickable") != "true":
            clickable = parents.get(clickable)
        candidate = clickable if clickable is not None else node
        bounds = re.findall(r"\d+", candidate.attrib.get("bounds", ""))
        if len(bounds) == 4:
            x1, y1, x2, y2 = map(int, bounds)
            if x2 > x1 and y2 > y1:
                matches.append((int(clickable is not None), x1, y1, x2, y2))
    if not matches:
        return None
    matches.sort(reverse=True)
    _, x1, y1, x2, y2 = matches[0]
    return ((x1 + x2) // 2, (y1 + y2) // 2)


def navigate(label):
    for attempt in range(5):
        root, parents, xml = hierarchy()
        point = selected_node(root, parents, label)
        if point:
            adb("shell", "input", "tap", str(point[0]), str(point[1]))
            time.sleep(2)
            print("Tapped", label, "at", point, flush=True)
            return
        if attempt == 4:
            Path("woodland-v5-navigation-failure.xml").write_bytes(xml)
            raise RuntimeError("Cannot locate Android accessibility control: " + label)
        adb("shell", "input", "swipe", "510", "1600", "510", "550", "350")
        time.sleep(1)


def expect(label):
    for _ in range(5):
        root, parents, xml = hierarchy()
        if selected_node(root, parents, label):
            return
        time.sleep(1)
    Path("woodland-v5-navigation-failure.xml").write_bytes(xml)
    raise RuntimeError("Android screen did not show expected title: " + label)


def capture(filename):
    data = adb("exec-out", "screencap", "-p").stdout
    if not data.startswith(PNG_MAGIC) or len(data) < 24:
        raise RuntimeError("Screenshot is missing or is not a PNG: " + filename)
    width, height = struct.unpack(">II", data[16:24])
    if width < 320 or height < 480:
        raise RuntimeError("Screenshot has unexpected dimensions: " + str((width, height)))
    (OUTPUT / filename).write_bytes(data)
    print("Captured", filename, width, height, "pixels", flush=True)
    return {"file": filename, "width": width, "height": height,
            "bytes": len(data), "sha256": hashlib.sha256(data).hexdigest()}


def main():
    running = adb("shell", "pidof", PACKAGE).stdout.strip()
    if not running:
        raise RuntimeError("Isolated Sudoku Woodland V5 is not running")
    adb("shell", "settings", "put", "system", "font_scale", "1.0")
    navigate("Co-op")
    expect("Shared board")
    coop = capture("woodland-v5-coop-android.png")
    navigate("Versus")
    expect("Race to finish")
    versus = capture("woodland-v5-versus-android.png")
    evidence = {
        "kind": "ACTUAL_ANDROID_EMULATOR_WINDOW_CAPTURES_NOT_MOCKUPS",
        "source_commit": __import__("os").environ.get("GITHUB_SHA", "local"),
        "package": PACKAGE,
        "screen_font_scale": "1.0",
        "screens": {"coop": coop, "versus": versus},
        "real_phone_approved": False,
        "two_phone_lan_tested": False,
        "production_update": False,
    }
    Path("woodland-v5-capture-receipt.json").write_text(
        json.dumps(evidence, indent=2) + "\n", encoding="utf-8"
    )
    print("PASS: Both mode screens captured and PNG signatures checked", flush=True)


if __name__ == "__main__":
    main()
