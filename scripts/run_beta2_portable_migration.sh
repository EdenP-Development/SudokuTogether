#!/usr/bin/env bash
set -euo pipefail
old=com.playereden.sudokutogether.qa
new=com.playereden.sudokutogether.qa.beta2integratedrc3
for pair in "beta1 $old" "beta2 $new"; do
  read -r dir pkg <<< "$pair"
  apk="$dir/app/build/outputs/apk/debug/app-debug.apk"
  testapk="$dir/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
  meta=$("$ANDROID_HOME/build-tools/35.0.0/aapt" dump badging "$apk")
  [[ "$meta" == *"name='$pkg'"* ]]
  adb install "$apk"
  adb install "$testapk"
done
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta1PortableExporterTest "$old.test/androidx.test.runner.AndroidJUnitRunner" | tee portable-export.txt
grep -Fq 'OK (1 test)' portable-export.txt
adb exec-out run-as "$old" cat files/local-player.json > old-before.json
adb exec-out run-as "$old" cat shared_prefs/settings.xml > old-settings-before.xml
adb exec-out run-as "$old" cat files/migration-export.json > portable.json
adb exec-out run-as "$old" cat files/migration-source-id.txt > source-id.txt
test -s portable.json
test -s source-id.txt
sha256sum portable.json
adb shell run-as "$new" mkdir -p files
adb shell -T run-as "$new" sh -c "'cat > files/migration-export.json'" < portable.json
adb shell -T run-as "$new" sh -c "'cat > files/migration-source-id.txt'" < source-id.txt
adb exec-out run-as "$new" cat files/migration-export.json > received.json
cmp portable.json received.json
adb shell am instrument -w -e class com.playereden.sudokutogether.Beta2PortableImporterTest "$new.test/androidx.test.runner.AndroidJUnitRunner" | tee portable-import.txt
grep -Fq 'OK (1 test)' portable-import.txt
adb exec-out run-as "$old" cat files/local-player.json > old-after.json
adb exec-out run-as "$old" cat shared_prefs/settings.xml > old-settings-after.xml
cmp old-before.json old-after.json
cmp old-settings-before.xml old-settings-after.xml
echo 'PASS: two distinct installations, actual Beta1 export restored in Beta2, original profile and settings byte-identical.'
