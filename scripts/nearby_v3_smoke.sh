#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.nearbyv3/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.nearbyv3 | grep -Eq '^[0-9]+'
adb exec-out screencap -p > nearby-v3-home-preview.png
test -s nearby-v3-home-preview.png
echo 'PASS: isolated Nearby V3 V2 installed, foreground activity started, process alive, screenshot saved'
