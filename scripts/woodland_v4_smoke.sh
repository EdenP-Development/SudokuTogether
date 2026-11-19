#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.woodlandv4/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.woodlandv4 | grep -Eq '^[0-9]+'
adb exec-out screencap -p > woodland-v4-home-preview.png
test -s woodland-v4-home-preview.png
echo 'PASS: isolated Woodland V4 installed, foreground activity started, process alive, screenshot saved'
