#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.beta2owner/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.beta2owner | grep -Eq '^[0-9]+'
adb exec-out screencap -p > beta2-owner-preview-home.png
test -s beta2-owner-preview-home.png
echo 'PASS: isolated Beta 2 installed, foreground activity started, process alive, screenshot saved'
