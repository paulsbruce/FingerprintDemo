#!/usr/bin/env bash

AVD_NAME=Nexus_6_API_25 # Pixel_API_25 #

nohup $ANDROID_HOME/tools/emulator -avd $AVD_NAME -verbose & # background this task to continue process

# extract new device id and provide in stdout

while true; do
  if [[ $($ANDROID_HOME/platform-tools/adb shell getprop sys.boot_completed) =~ "1" ]]; then
    break
  fi
  sleep 1
done

sleep 10s # because there's no better way than above, which is kind of close

$ANDROID_HOME/platform-tools/adb shell input keyevent 26
$ANDROID_HOME/platform-tools/adb shell input keyevent 82
$ANDROID_HOME/platform-tools/adb shell input keyevent 82
$ANDROID_HOME/platform-tools/adb shell input text 1111
$ANDROID_HOME/platform-tools/adb shell input keyevent 66
$ANDROID_HOME/platform-tools/adb shell input keyevent 3

exit 0