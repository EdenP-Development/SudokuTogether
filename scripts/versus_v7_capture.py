#!/usr/bin/env python3
"""Capture two actual Android 16 screens after instrumentation uninstalls the test app.

Android Gradle's connectedDebugAndroidTest can uninstall/reinstall the target APK,
which removes app-private test screenshots. Reinstall the isolated reviewer APK,
navigate using Android's accessibility bounds and capture live framebuffers.
Compose instrumentation independently verifies V7/no-duplicate/Co-op behavior.
"""
from pathlib import Path
import re
import subprocess
import time
import xml.etree.ElementTree as ET

PACKAGE = "com.playereden.sudokutogether.qa.versusv7"
ACTIVITY = "com.playereden.sudokutogether.MainActivity"
MAGIC = bytes((137,80,78,71,13,10,26,10))


def adb(*args):
    proc = subprocess.run(["adb", *args], capture_output=True, check=True)
    return proc.stdout


def hierarchy():
    adb("shell", "uiautomator", "dump", "/sdcard/gridmates-versus-v7-ui.xml")
    raw = adb("exec-out", "cat", "/sdcard/gridmates-versus-v7-ui.xml")
    root = ET.fromstring(raw)
    parent = {child: node for node in root.iter() for child in node}
    return root, parent


def find(label):
    root, parents = hierarchy()
    choices = []
    for node in root.iter("node"):
        value = (node.attrib.get("text","") + " " +
                 node.attrib.get("content-desc","")).strip().lower()
        if label.lower() not in value:
            continue
        current = node
        while current is not None and current.attrib.get("clickable") != "true":
            current = parents.get(current)
        chosen = current if current is not None else node
        bounds = re.findall(r"\d+", chosen.attrib.get("bounds", ""))
        if len(bounds) != 4:
            continue
        x1,y1,x2,y2 = map(int,bounds)
        if x2 <= x1 or y2 <= y1:
            continue
        exact = value == label.lower()
        choices.append((int(exact), int(current is not None), (x1+x2)//2, (y1+y2)//2))
    if not choices:
        return None
    choices.sort(reverse=True)
    return choices[0][2:]


def tap_text(label, allow_scroll=False):
    for attempt in range(4 if allow_scroll else 2):
        p = find(label)
        if p is not None:
            adb("shell", "input", "tap", str(p[0]), str(p[1]))
            time.sleep(2.1)
            return
        if allow_scroll:
            adb("shell", "input", "swipe", "540","1800","530","630","360")
            time.sleep(.7)
    raise RuntimeError(f"Android UIAutomator could not find a visible {label!r} control")


def screenshot(file):
    if not adb("shell", "pidof", PACKAGE).strip():
        raise RuntimeError("The isolated Versus reviewer app stopped running")
    data=adb("exec-out", "screencap", "-p")
    if data[:8] != MAGIC or len(data) < 12000:
        raise RuntimeError(f"Invalid live Android screen capture: {file}")
    Path(file).write_bytes(data)
    print("PASS captured live Android screen:",file,len(data),flush=True)


def main():
    # Gradle tests finished and may have uninstalled the app. Ensure a fresh
    # local Home launch before taking screenshots.
    adb("shell","am","force-stop",PACKAGE)
    adb("shell","am","start","-W","-n",PACKAGE+"/"+ACTIVITY)
    time.sleep(2.5)
    tap_text("Versus",allow_scroll=True)
    screenshot("versus-v7-actual-android.png")
    # The same nearby menu exposes the Co-op tab; this must retain its V5 art.
    tap_text("Co-op",allow_scroll=True)
    screenshot("versus-v7-coop-control-android.png")


if __name__ == "__main__":
    main()
