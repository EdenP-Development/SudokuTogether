#!/usr/bin/env bash
# Sign only an audited unsigned candidate, in an isolated authorized runner.
set -euo pipefail
umask 077
[[ $# == 3 ]] || { echo 'Usage: sign_beta3_rehearsal.sh UNSIGNED.apk AUDIT.sha256 OUTPUT.apk'; exit 2; }
unsigned=$1
receipt=$2
output=$3
: "$ANDROID_HOME" "$RUNNER_TEMP" "$KEYSTORE_BASE64" "$KEYSTORE_PASSWORD"
tools="$ANDROID_HOME/build-tools/35.0.0"
cert=9d7b30b2b5dccaa4b87843fdff6f2d836d6207ea64b726ef837730572a04cf3d
[[ -f "$unsigned" && -f "$receipt" && ! -e "$output" ]]
expected=$(awk 'NR==1 {print $1}' "$receipt")
[[ "$expected" =~ ^[a-f0-9]{64}$ ]]
[[ "$(sha256sum "$unsigned" | awk '{print $1}')" == "$expected" ]]
badging=$("$tools/aapt" dump badging "$unsigned")
grep -Fq "package: name='com.playereden.gridmates.sudoku.beta' versionCode='32' versionName='1.0.0-beta.3'" <<< "$badging"
! grep -q '^application-debuggable' <<< "$badging"
if "$tools/apksigner" verify "$unsigned" >/dev/null 2>&1; then
  echo 'BLOCKED: input already signed'; exit 1
fi
private=$(mktemp -d "$RUNNER_TEMP/gridmates-rehearsal.XXXXXXXX")
trap 'rm -rf "$private"' EXIT
printf '%s' "$KEYSTORE_BASE64" | base64 --decode > "$private/key.jks"
unset KEYSTORE_BASE64
actual=$(keytool -exportcert -alias gridmates-sudoku-beta -storepass:env KEYSTORE_PASSWORD -keystore "$private/key.jks" 2>/dev/null | sha256sum | awk '{print $1}')
[[ "$actual" == "$cert" ]]
"$tools/zipalign" -P 16 -f 4 "$unsigned" "$private/aligned.apk"
"$tools/apksigner" sign --ks "$private/key.jks" --ks-key-alias gridmates-sudoku-beta --ks-pass env:KEYSTORE_PASSWORD --key-pass env:KEYSTORE_PASSWORD --out "$private/signed.apk" "$private/aligned.apk"
unset KEYSTORE_PASSWORD
actual=$("$tools/apksigner" verify --min-sdk-version 26 --print-certs "$private/signed.apk" | awk '/certificate SHA-256 digest:/ {print $NF}')
[[ "$actual" == "$cert" ]]
"$tools/zipalign" -c -P 16 4 "$private/signed.apk"
[[ "$("$tools/aapt" dump badging "$private/signed.apk")" == "$badging" ]]
# Compare uncompressed entry payloads; only signing metadata may differ.
python3 - "$unsigned" "$private/signed.apk" <<'PY'
import sys, zipfile, hashlib, re
def payload(path):
    with zipfile.ZipFile(path) as z:
        names = [n for n in z.namelist() if not re.fullmatch(r'META-INF/(?:MANIFEST\.MF|[^/]+\.(?:SF|RSA|DSA|EC))', n, re.I)]
        assert len(names) == len(set(names)), 'duplicate payload entries'
        return {n: hashlib.sha256(z.read(n)).digest() for n in names}
assert payload(sys.argv[1]) == payload(sys.argv[2]), 'APK payload changed'
PY
cp "$private/signed.apk" "$output"
sha256sum "$output"
echo 'PASS: original signer, unchanged application payload, aligned APK. Not approved for publication.'
