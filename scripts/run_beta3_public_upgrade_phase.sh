#!/usr/bin/env bash
# Explicitly opt-in disposable emulator only. No uninstall, clear, run-as or signing.
# Seed and verify via ordinary UI between phases; identity alone is not acceptance.
set -euo pipefail
[[ $# == 3 ]] || { echo 'Usage: bash scripts/run_beta3_public_upgrade_phase.sh seed|upgrade OLD.apk NEW.apk'; exit 2; }
phase=$1
old=$2
new=$3
[[ "$phase" == seed || "$phase" == upgrade ]]
bash "$(dirname "$0")/verify_beta3_public_upgrade_pair.sh" "$old" "$new"
pkg=com.playereden.gridmates.sudoku.beta
serial=$(adb devices | awk '$2=="device" {print $1}')
[[ "$serial" =~ ^emulator-[0-9]+$ ]] || { echo 'BLOCKED: exactly one emulator required'; exit 1; }
export ANDROID_SERIAL="$serial"
[[ "$(adb shell getprop ro.kernel.qemu | tr -d '\r')" == 1 ]]
installed_code() {
  adb shell dumpsys package "$pkg" | tr -d '\r' | awk '/versionCode=/{if (!found++) {sub(/.*versionCode=/,""); print $1}}'
}
if [[ "$phase" == seed ]]; then
  [[ -z "$(adb shell pm list packages "$pkg" | tr -d '\r')" ]]
  adb install "$old"
  [[ "$(installed_code)" == 31 ]]
  adb shell am start -n "$pkg/com.playereden.sudokutogether.MainActivity"
  echo 'Beta 2 installed. Seed progress via normal UI and retain pre-upgrade evidence.'
else
  [[ "$(installed_code)" == 31 ]]
  adb shell am force-stop "$pkg"
  adb install -r "$new"
  [[ "$(installed_code)" == 32 ]]
  adb shell am start -n "$pkg/com.playereden.sudokutogether.MainActivity"
  echo 'Beta 3 installed over Beta 2. Compare seeded progress, resume and restart before acceptance.'
fi
