#!/usr/bin/env python3
"""Capture ACTUAL Android screenshots of the six Basic insignias and comparison.
Never substitutes synthetic contact sheets for emulator/physical-phone evidence.
"""
import os
import time

os.environ.setdefault("GRIDMATES_REVIEW_PACKAGE",
                      "com.playereden.sudokutogether.qa.insignias")
from beta2_v7_capture import adb, tap_text, screenshot, PACKAGE, ACTIVITY


def main():
    adb("shell", "am", "force-stop", PACKAGE)
    adb("shell", "am", "start", "-W", "-n", PACKAGE + "/" + ACTIVITY)
    time.sleep(2)
    tap_text("Collection")
    screenshot("beta2-insignia-gallery-android.png")
    tap_text("Constellation", allow_scroll=True)
    screenshot("beta2-insignia-comparison-android.png")
    print("PASS: actual Android gallery and original-vs-proposal dialog captured", flush=True)


if __name__ == "__main__":
    main()
