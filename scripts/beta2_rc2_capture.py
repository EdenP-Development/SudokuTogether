#!/usr/bin/env python3
"""Exact Android 16 captures for the visually changed Beta 2 RC2 reviewer."""
import os
import time
os.environ.setdefault("GRIDMATES_REVIEW_PACKAGE",
                      "com.playereden.sudokutogether.qa.beta2visualrc2")
from beta2_v7_capture import adb, tap_text, screenshot, PACKAGE, ACTIVITY

def relaunch():
    adb("shell", "am", "force-stop", PACKAGE)
    adb("shell", "am", "start", "-W", "-n", PACKAGE + "/" + ACTIVITY)
    time.sleep(2.4)

def main():
    relaunch()
    tap_text("Solo", allow_scroll=True)
    tap_text("New puzzle", allow_scroll=True)
    screenshot("beta2-rc2-solo-board-android.png")
    relaunch()
    tap_text("Collection", allow_scroll=True)
    screenshot("beta2-rc2-basic-gallery-android.png")
    tap_text("Constellation", allow_scroll=True)
    screenshot("beta2-rc2-insignia-comparison-android.png")
    print("PASS: solo board/dock, basic gallery and original-vs-vector comparison",flush=True)

if __name__ == "__main__":
    main()
