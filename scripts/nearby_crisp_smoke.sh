#!/usr/bin/env bash
set -euo pipefail
apk=app/build/outputs/apk/debug/app-debug.apk
test -s "$apk"
adb install "$apk"
adb shell am start -W -n com.playereden.sudokutogether.qa.crispmodes/com.playereden.sudokutogether.MainActivity
sleep 4
adb shell pidof com.playereden.sudokutogether.qa.crispmodes | grep -Eq '^[0-9]+'
adb exec-out screencap -p > nearby-crisp-home-preview.png
test -s nearby-crisp-home-preview.png
echo 'PASS: isolated Nearby Crisp V2 installed, foreground activity started, process alive, screenshot saved'
