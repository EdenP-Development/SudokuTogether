#!/usr/bin/env bash
# Identity gate only: no signing, installation or publication.
set -euo pipefail
[[ $# == 2 ]] || { echo 'Usage: bash scripts/verify_beta3_public_upgrade_pair.sh OLD.apk NEW.apk'; exit 2; }
old=$1
new=$2
: "$ANDROID_HOME"
tools="$ANDROID_HOME/build-tools/35.0.0"
expected_cert=9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d
expected_old=28f9fb4888f6ee32e960d22152b84aeed4daef55e9ec0230cb53725daa50b89c
pkg=com.playereden.gridmates.sudoku.beta
[[ -f "$old" && -f "$new" ]]
[[ "$(sha256sum "$old" | awk '{print $1}')" == "$expected_old" ]]
verify() {
  local apk=$1 code=$2 version=$3 cert badging
  cert=$("$tools/apksigner" verify --min-sdk-version 26 --print-certs "$apk" | awk '/certificate SHA-256 digest:/ {print $NF}')
  [[ "$cert" == "$expected_cert" ]]
  badging=$("$tools/aapt" dump badging "$apk")
  grep -Fq "package: name='$pkg' versionCode='$code' versionName='$version'" <<< "$badging"
  ! grep -q '^application-debuggable' <<< "$badging"
  printf 'PASS: %s code=%s version=%s cert=%s\n' "$pkg" "$code" "$version" "$cert"
  sha256sum "$apk"
}
verify "$old" 31 1.0.0-beta.2
verify "$new" 32 1.0.0-beta.3
echo 'PASS: exact historical APK and compatible production signer/version pair; gameplay continuity not tested.'
