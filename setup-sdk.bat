@echo off
set JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot
set ANDROID_HOME=C:\Android
set SKIP_JDK_VERSION_CHECK=1

echo === Step 1: Accept licenses ===
echo y| "%ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager.bat" --licenses

echo === Step 2: Install SDK ===
"%ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager.bat" "platforms;android-35" "build-tools;35.0.0" "platform-tools"

echo === Step 3: Set ANDROID_HOME ===
setx ANDROID_HOME "C:\Android"

echo === Done! ===
dir "%ANDROID_HOME%\platforms"
