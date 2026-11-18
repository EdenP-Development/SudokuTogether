#!/usr/bin/env bash
# The emulator action executes script: lines in separate shells unless one
# script file owns the full sequence. Keep all variables in THIS process.
set -euo pipefail
pkg=com.playereden.sudokutogether.qa
test_pkg=com.playereden.sudokutogether.qa.test
old=beta1/app/build/outputs/apk/debug/app-debug.apk
old_test=beta1/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
new=beta2/app/build/outputs/apk/debug/app-debug.apk
new_test=beta2/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk

test -s "$old" && test -s "$old_test" && test -s "$new" && test -s "$new_test"
echo 'Installing Beta 1 and using the Beta 1 storage code to create a saved profile.'
adb install "$old"
adb install "$old_test"
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta1UpgradeSeederTest \
  "$test_pkg/androidx.test.runner.AndroidJUnitRunner" | tee beta1-seed.txt
grep -Fq 'OK (1 test)' beta1-seed.txt
adb exec-out run-as "$pkg" cat files/local-player.json > beta1-before.json
python3 - <<'PY'
import json
with open('beta1-before.json', 'rb') as stream:
    profile = json.load(stream)
assert 'ownedCosmeticIds' not in profile
assert len(profile['records']) == 5
assert len(profile['dailyProgress']) == 1
assert profile['activeSolo']['cells'][0]['notes'] == 4
print('Beta 1 save verified: Solo Notes, Daily and five records, no new ownership field.')
PY

adb shell am start -W -n "$pkg/com.playereden.sudokutogether.MainActivity" | tee beta1-launch.txt
grep -q 'Status: ok' beta1-launch.txt
adb shell am force-stop "$pkg"
echo 'Updating same Android installation from versionCode 30 to 31.'
adb install -r "$new"
adb exec-out run-as "$pkg" cat files/local-player.json > beta2-before-first-launch.json
cmp beta1-before.json beta2-before-first-launch.json

adb shell am start -W -n "$pkg/com.playereden.sudokutogether.MainActivity" | tee beta2-launch.txt
grep -q 'Status: ok' beta2-launch.txt
adb install -r "$new_test"
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta2UpgradeVerifierTest \
  "$test_pkg/androidx.test.runner.AndroidJUnitRunner" | tee beta2-verify.txt
grep -Fq 'OK (1 test)' beta2-verify.txt

echo 'PASS: source-backed Beta 1 to Beta 2 install-over preserved local Solo, Daily, Notes, records and earned cosmetics.'
echo 'TEST SIGNER ONLY: the public app still requires the original production certificate.'
