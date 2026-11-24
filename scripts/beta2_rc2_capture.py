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
    # The fresh home opens directly on Solo's "Play Sudoku" hero and the
    # "New puzzle" button; there is no top-level button named "Solo" here.
    tap_text("New puzzle", allow_scroll=True)
    screenshot("beta2-rc2-solo-board-android.png")
    relaunch()
    tap_text("Collection", allow_scroll=True)
    screenshot("beta2-rc2-basic-gallery-android.png")
    # The Basic album is below a summary and the six new icons are rows 3–4.
    # Scrolling WITHOUT tapping yields the screenshot that actually proves the
    # six candidate vectors appear together in the real game.
    from beta2_v7_capture import find
    for attempt in range(5):
        if find("Constellation") is not None and find("Grid Master") is not None:
            break
        adb("shell", "input", "swipe", "540", "1780", "535", "960", "350")
        time.sleep(.8)
    else:
        raise RuntimeError("Could not display all six candidate insignias in album")
    screenshot("beta2-rc2-six-insignias-android.png")
    tap_text("Constellation")
    screenshot("beta2-rc2-insignia-comparison-android.png")
    print("PASS: solo board/dock, basic gallery and original-vs-vector comparison",flush=True)

if __name__ == "__main__":
    main()
