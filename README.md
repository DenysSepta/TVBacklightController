# TV Backlight Controller

An Android TV utility that allows you to control your TV's backlight level using the **Channel Up** and **Channel Down** buttons on your remote. It features a custom overlay to show the current backlight level and supports "hold to repeat" for quick adjustments.

## Features
- **Remote Control Integration**: Map Channel Up/Down keys to backlight control.
- **Hold to Repeat**: Long-press buttons to rapidly increase or decrease brightness.
- **On-Screen Overlay**: A visual progress bar appears at the top of the screen when adjustments are made.
- **Auto-Hide**: Overlay automatically disappears after 3 seconds.
- **Boot Persistence**: Automatically starts at boot.

## Installation & Setup

### 1. Install the App
Build the APK and install it on your Android TV.

### 2. Grant Permissions
Because modifying system-level backlight settings is a sensitive operation, you must manually grant the `WRITE_SECURE_SETTINGS` permission via ADB:

```bash
adb shell pm grant com.tv.backlight android.permission.WRITE_SECURE_SETTINGS
```

### 3. Enable Accessibility Service
1. Go to **Settings** on your TV.
2. Navigate to **Device Preferences** > **Accessibility**.
3. Find **TV Backlight** and turn it **ON**.

## How to Use
- **Press Channel Up**: Increase backlight by 5%.
- **Press Channel Down**: Decrease backlight by 5%.
- **Hold Button**: Continuously change the backlight level.

## Technical Details
- Uses `AccessibilityService` to intercept key events system-wide.
- Uses `WindowManager` to display a custom `TYPE_ACCESSIBILITY_OVERLAY` for the UI.
- Modifies the `picture_backlight` global setting.

## Build Requirements
- Android Studio Ladybug or newer.
- Android SDK 21+ (Designed for Android TV).
- Kotlin 2.x.
