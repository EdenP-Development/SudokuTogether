#!/usr/bin/env python3
"""Capture ACTUAL Android screenshots of the six Basic insignias and comparison.
Never substitutes synthetic contact sheets for emulator/physical-phone evidence.
"""
import os
import time

os.environ.setdefault("GRIDMATES_REVIEW_PACKAGE",
                      "com.playereden.sudokutogether.qa.insignias")
from beta2_v7_capture import adb, find, tap_text, screenshot, PACKAGE, ACTIVITY


def main():
    adb("shell", "am", "force-stop", PACKAGE)
    adb("shell", "am", "start", "-W", "-n", PACKAGE + "/" + ACTIVITY)
    time.sleep(2)
    tap_text("Collection")
    # Show the new designs, not the top gallery row containing old icons.
    for n in range(9):
        if find("Constellation") is not None:
            break
        adb("shell", "input", "swipe", "540", "1750", "535", "690", "360")
        time.sleep(.85)
    else:
        raise RuntimeError("Constellation not visible in Android gallery")
    screenshot("beta2-insignia-gallery-android.png")
    tap_text("Constellation")
    screenshot("beta2-insignia-comparison-android.png")
    print("PASS: actual Android gallery and original-vs-proposal dialog captured", flush=True)


if __name__ == "__main__":
    main()
