#!/usr/bin/env bash
set -euo pipefail
mkdir -p device-checks
package=com.ipeproing.countdowntv
adb install -r deliverables/Kastrati-TV-debug.apk
adb shell wm size 1920x1080
adb shell wm density 240
adb shell am start -W -n "$package/.MainActivity"
sleep 3
adb shell run-as "$package" cat shared_prefs/countdown.xml > device-checks/before.xml
adb exec-out screencap -p > device-checks/countdown-1080p.png
adb shell input keyevent 23
sleep 2
adb shell uiautomator dump /sdcard/window.xml
adb pull /sdcard/window.xml device-checks/settings.xml
grep -q 'Konfigurimi' device-checks/settings.xml
adb exec-out screencap -p > device-checks/settings.png
adb shell input keyevent 4
adb shell am force-stop "$package"
sleep 2
adb shell am start -W -n "$package/.MainActivity"
sleep 2
adb shell run-as "$package" cat shared_prefs/countdown.xml > device-checks/after-process-restart.xml
cmp device-checks/before.xml device-checks/after-process-restart.xml
adb reboot
adb wait-for-device
for i in $(seq 1 90); do
  if [ "$(adb shell getprop sys.boot_completed | tr -d '\r')" = "1" ]; then break; fi
  sleep 2
done
test "$(adb shell getprop sys.boot_completed | tr -d '\r')" = "1"
adb shell am start -W -n "$package/.MainActivity"
sleep 3
adb shell run-as "$package" cat shared_prefs/countdown.xml > device-checks/after-reboot.xml
cmp device-checks/before.xml device-checks/after-reboot.xml
adb shell wm size 3840x2160
sleep 3
adb exec-out screencap -p > device-checks/countdown-4k.png
adb shell settings put secure screensaver_components "$package/.CountdownDreamService"
adb shell settings put secure screensaver_enabled 1
adb shell am start -a android.intent.action.MAIN -n com.android.systemui/.Somnambulator
sleep 3
adb shell dumpsys dreams > device-checks/dreams.txt
adb exec-out screencap -p > device-checks/screensaver.png
adb logcat -d -s AndroidRuntime:E > device-checks/crashes.txt
if grep -q 'FATAL EXCEPTION' device-checks/crashes.txt; then exit 1; fi
echo 'Install, OK settings, process restart and full reboot persistence passed.' > device-checks/result.txt
