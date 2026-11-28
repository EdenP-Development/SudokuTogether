#!/usr/bin/env python3
"""Public APK UI continuity smoke test; no instrumentation or private save access."""
import pathlib
import re
import subprocess
import sys
import time
import xml.etree.ElementTree as ET

root = pathlib.Path(sys.argv[1])
root.mkdir(parents=True, exist_ok=True)
old, new = sys.argv[2:4]
pkg = "com.playereden.gridmates.sudoku.beta"

def adb(*args):
    return subprocess.check_output(["adb", *args], text=True)

def snapshot(label):
    adb("shell", "uiautomator", "dump", "/sdcard/gridmates-rehearsal.xml")
    data = adb("shell", "cat", "/sdcard/gridmates-rehearsal.xml")
    (root / (label + ".xml")).write_text(data)
    return list(ET.fromstring(data).iter("node"))

def wait(label, predicate):
    deadline = time.monotonic() + 60
    while time.monotonic() < deadline:
        nodes = snapshot(label)
        found = [n for n in nodes if predicate(n)]
        if found:
            return found
        time.sleep(1)
    raise AssertionError("UI target missing: " + label)

def named(name):
    return lambda n: name in (n.get("text"), n.get("content-desc"))

def tap(node):
    x1, y1, x2, y2 = map(int, re.findall(r"\d+", node.get("bounds", "")))
    assert x2 > x1 and y2 > y1
    adb("shell", "input", "tap", str((x1+x2)//2), str((y1+y2)//2))

def cell(n):
    return re.match(r"Row (\d+) column (\d+), (.*)", n.get("content-desc", ""))

def board(label):
    nodes = wait(label, lambda n: cell(n) is not None)
    result = {}
    for n in nodes:
        m = cell(n)
        result[(m[1], m[2])] = m[3]
    assert len(result) == 81, (label, len(result))
    return result

def phase(name):
    subprocess.run(["bash", "scripts/run_beta3_public_upgrade_phase.sh", name, old, new], check=True)

phase("seed")
tap(wait("beta2-home", named("New puzzle"))[0])
before = board("beta2-board-initial")
target = wait("beta2-empty", lambda n: bool(cell(n) and cell(n)[3] == "empty"))[0]
m = cell(target)
key = (m[1], m[2])
tap(target)
tap(wait("beta2-keypad", lambda n: n.get("content-desc", "").startswith("Number 1,") and n.get("enabled") == "true")[0])
wait("beta2-entered", lambda n: bool(cell(n) and (cell(n)[1], cell(n)[2]) == key and cell(n)[3].startswith("value 1")))
seeded = board("beta2-board-seeded")
assert before != seeded
tap(wait("beta2-home-control", named("Home"))[0])
wait("beta2-continue", named("Continue"))
phase("upgrade")
tap(wait("beta3-continue", named("Continue"))[0])
assert board("beta3-board-resumed") == seeded, "Board differs after upgrade"
tap(wait("beta3-home-control", named("Home"))[0])
wait("beta3-home-saved", named("Continue"))
adb("shell", "am", "force-stop", pkg)
adb("shell", "am", "start", "-n", pkg + "/com.playereden.sudokutogether.MainActivity")
tap(wait("beta3-restart-continue", named("Continue"))[0])
assert board("beta3-board-restarted") == seeded, "Board differs after restart"
(root / "public-ui-result.txt").write_text("PASS: all 81 accessible cell descriptions preserved after upgrade and restart; one entered value seeded. Other progress categories not covered.\n")
print("PASS: public signed Solo board continuity and restart")
