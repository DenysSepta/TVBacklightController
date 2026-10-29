# TV Backlight Controller for Android TV

**Adjust your Android TV's backlight directly from the remote—without navigating picture settings.**

TV Backlight Controller is a lightweight Android TV app that maps **Channel Up** and **Channel Down** on a compatible remote to backlight adjustments. Each press changes the requested level by 5%; holding a button repeats the adjustment. A temporary on-screen indicator displays the level tracked by the app.

The project was developed and tested by its maintainer on a **Xiaomi Android TV**. Other TV models and firmware versions are not yet confirmed.

[![Android TV](https://img.shields.io/badge/Android%20TV-Android%2010%2B-green?logo=android)](https://www.android.com/tv/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-purple?logo=kotlin)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Features

- **Remote backlight control:** Channel Up increases and Channel Down decreases the level.
- **Press-and-hold adjustment:** Repeats while a button is held.
- **On-screen level indicator:** Shows the value tracked by the app.
- **Automatic overlay dismissal:** The indicator hides after about 3 seconds.
- **Background service:** Keeps the service available while the TV is running.
- **Boot handling:** Attempts to start the service after the TV boots.
- **Android TV interface:** Designed for a TV remote and D-pad-based navigation.

## Compatibility and limitations

- **Maintainer-tested:** Xiaomi Android TV (specific model/firmware not yet listed).
- **Minimum Android version:** Android 10 / API 29, as configured by the app.
- **Not guaranteed:** Other Xiaomi models, Google TV devices, and TVs from other manufacturers.

Android TV vendors can change their settings provider, supported key events, and backlight implementation. This app writes to Android's global `picture_backlight` setting; a TV must actually honor that setting for the backlight to change. Some TVs may ignore it or use a vendor-specific control.

The on-screen percentage is the value tracked by this app, not a hardware brightness measurement. It starts at 50% when the process initializes and changes in 5% steps; it is not currently read back from the TV's actual panel state. If the display already has a different backlight setting, the indicator may not match it until you adjust it.

## Installation

### Requirements

- Android Studio with the Android SDK.
- JDK compatible with the Gradle version configured in this repository.
- An Android TV device running Android 10 (API 29) or later.
- ADB access for installing the APK and granting the required permission.
- A remote whose Channel Up/Down events are exposed to Android's Accessibility service.

### 1. Clone the repository

```bash
git clone https://github.com/DenysSepta/TVBacklightController.git
cd TVBacklightController
```

Open the project in Android Studio, allow Gradle sync to finish, and build the `app` module.

Or build a debug APK from the command line:

```bash
./gradlew assembleDebug
```

On Windows, use:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is normally generated at `app/build/outputs/apk/debug/app-debug.apk`.

### 2. Connect to the TV and install

Enable ADB / Developer options on the TV and connect to it from your computer. Replace `TV_IP` with the TV's address:

```bash
adb connect TV_IP:5555
adb install app/build/outputs/apk/debug/app-debug.apk
```

If ADB is already paired or connected, use the connection method supported by your TV.

### 3. Grant the required system permission

The app writes the `picture_backlight` global setting and requires `WRITE_SECURE_SETTINGS`. Grant it through ADB:

```bash
adb shell pm grant com.tv.backlight android.permission.WRITE_SECURE_SETTINGS
```

This permission is privileged and normally cannot be granted from the standard Android TV settings interface.

### 4. Enable the Accessibility service

On the TV, open the Accessibility settings and enable **TV Backlight**. The exact menu path varies by TV firmware; on the tested setup it is under:

**Settings → Device Preferences → Accessibility → TV Backlight**

The service needs key-event filtering so the app can react to Channel Up/Down while other apps are open.

## Usage

| Remote action | Result |
|---|---|
| Press Channel Up | Increase the tracked level by 5% |
| Press Channel Down | Decrease the tracked level by 5% |
| Hold Channel Up | Continue increasing after the initial repeat delay |
| Hold Channel Down | Continue decreasing after the initial repeat delay |

The tracked value is clamped between 0% and 100%. The overlay appears while adjusting and hides automatically after approximately 3 seconds.

## How it works

1. Android's `AccessibilityService` receives Channel Up/Down key events.
2. The app adjusts its in-memory level in 5% increments.
3. It attempts to write that value to `Settings.Global` under `picture_backlight`.
4. A `TYPE_ACCESSIBILITY_OVERLAY` displays the tracked value.
5. A foreground service is started on boot and when the screen turns on, subject to Android and vendor background-service restrictions.

The project does not capture the screen or use a camera. It changes a system setting rather than analyzing video to create ambient lighting.

## Troubleshooting

### The buttons do nothing

- Confirm **TV Backlight** is enabled under Accessibility.
- Check whether your remote exposes Channel Up/Down as standard Android key events. Some remotes or TV launchers reserve these buttons.
- Connect ADB and inspect logs:

  ```bash
  adb logcat -s TVBACKLIGHT
  ```

  Look for key-code messages when pressing the buttons.

### The overlay changes, but the physical backlight does not

- Confirm the permission was granted:

  ```bash
  adb shell pm grant com.tv.backlight android.permission.WRITE_SECURE_SETTINGS
  ```

- Inspect logs for a failed `Settings.Global.putInt` call.
- The TV firmware may ignore `picture_backlight` or control the panel through a vendor-specific mechanism. This project does not currently include vendor-specific implementations.

### The displayed percentage does not match the TV

The app currently initializes its own tracked value at 50%; it does not read the current value back from the TV before adjusting. This is a known limitation. If you can reproduce behavior on another model, please report it with the TV model and firmware details.

### The service stops after sleep or reboot

Android TV firmware may restrict background services. Confirm Accessibility remains enabled and reopen the app once after installation. If the issue persists, include your TV model, Android version, and relevant `TVBACKLIGHT` log lines in a report.

## Privacy and permissions

- The app uses an Accessibility service to observe remote key events.
- It requests `WRITE_SECURE_SETTINGS` to update the global backlight setting.
- It does not need an account or cloud service for its core behavior.
- ADB should only be enabled and exposed on trusted networks. Disable wireless debugging when it is no longer needed.

## Report a bug or contribute

Compatibility reports are especially useful because Android TV manufacturers implement system settings and remote controls differently.

Before opening an issue, include:

- TV manufacturer and exact model.
- Android TV / Google TV version and firmware build, if available.
- Whether the Accessibility service is enabled.
- Whether the overlay changes when pressing Channel Up/Down.
- Relevant `adb logcat -s TVBACKLIGHT` output.

Remove private information from logs before posting them publicly. See [CONTRIBUTING.md](CONTRIBUTING.md) for guidance on bug reports and pull requests.

## License

This project is released under the [MIT License](LICENSE).
