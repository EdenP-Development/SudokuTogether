#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.homegamefront/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.homegamefront | grep -Eq '^[0-9]+'
adb exec-out screencap -p > home-gamefront-owner-preview-home.png
test -s home-gamefront-owner-preview-home.png
echo 'PASS: isolated Home gamefront installed, foreground activity started, process alive, screenshot saved'
