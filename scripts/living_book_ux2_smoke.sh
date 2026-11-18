#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.livingbook2/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.livingbook2 | grep -Eq '^[0-9]+'
adb exec-out screencap -p > living-book-ux2-preview-home.png
test -s living-book-ux2-preview-home.png
echo 'PASS: isolated Living Puzzle Book installed, foreground activity started, process alive, screenshot saved'
