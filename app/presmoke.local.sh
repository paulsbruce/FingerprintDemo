#!/usr/bin/env bash

nohup $ANDROID_HOME/tools/emulator -avd Nexus_6_API_25 -verbose & # background this task to continue process

while true; do
  if [[ $(adb shell getprop sys.boot_completed) =~ "1" ]]; then
    break
  fi
  sleep .5
done

sleep 10s # because there's no better way than above, which is kind of close

$ANDROID_HOME/platform-tools/adb shell input keyevent 26
$ANDROID_HOME/platform-tools/adb shell input keyevent 82
$ANDROID_HOME/platform-tools/adb shell input keyevent 82
$ANDROID_HOME/platform-tools/adb shell input text 1111
$ANDROID_HOME/platform-tools/adb shell input keyevent 66
$ANDROID_HOME/platform-tools/adb shell input keyevent 3

exit 0