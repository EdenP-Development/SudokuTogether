#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.woodlandv51/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.woodlandv51 | grep -Eq '^[0-9]+'
adb exec-out screencap -p > woodland-v5-home-preview.png
test -s woodland-v5-home-preview.png
echo 'PASS: isolated Woodland V5 installed, foreground activity started, process alive, screenshot saved'
