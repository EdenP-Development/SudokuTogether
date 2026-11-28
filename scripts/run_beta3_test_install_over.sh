#!/usr/bin/env bash
set -euo pipefail
pkg=com.playereden.sudokutogether.qa
test_pkg=$pkg.test
old=beta2/app/build/outputs/apk/debug/app-debug.apk
new=beta3/app/build/outputs/apk/debug/app-debug.apk
old_test=beta2/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
new_test=beta3/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
# Never target a physical device or ambiguous ADB connection.
serial=$(adb devices | awk '$2=="device" {print $1}')
[[ "$serial" =~ ^emulator-[0-9]+$ ]]
export ANDROID_SERIAL="$serial"
[[ -z "$(adb shell pm list packages "$pkg" | tr -d '\r')" ]]
adb install "$old"
adb install "$old_test"
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta2ToBeta3SeederTest "$test_pkg/androidx.test.runner.AndroidJUnitRunner" | tee beta2-seed.txt
grep -Fq 'OK (1 test)' beta2-seed.txt
adb shell am force-stop "$pkg"
adb exec-out run-as "$pkg" cat files/local-player.json > beta2-before.json
adb install -r "$new"
adb exec-out run-as "$pkg" cat files/local-player.json > beta3-before-launch.json
cmp beta2-before.json beta3-before-launch.json
adb install -r "$new_test"
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta2ToBeta3VerifierTest "$test_pkg/androidx.test.runner.AndroidJUnitRunner" | tee beta3-verify.txt
grep -Fq 'OK (1 test)' beta3-verify.txt
adb shell am force-stop "$pkg"
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta2ToBeta3VerifierTest "$test_pkg/androidx.test.runner.AndroidJUnitRunner" | tee beta3-restart.txt
grep -Fq 'OK (1 test)' beta3-restart.txt
echo 'PASS: synthetic Beta2 to Beta3 test-signer install-over, semantic continuity and restart.'
