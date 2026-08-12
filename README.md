# 📺 TV Backlight Controller

**Control your Android TV's backlight directly from your remote.**

TV Backlight Controller lets you use the **Channel Up** and **Channel Down** buttons on your remote to adjust your TV's backlight without opening the picture settings menu.

Press a button to change the level by **5%**, or hold it for continuous adjustment. A lightweight on-screen indicator shows the current backlight level.

> 💡 Built for Android TV users who want fast, physical remote control over their TV's backlight.

[![Android TV](https://img.shields.io/badge/Android%20TV-21%2B-green?logo=android)](https://www.android.com/tv/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-purple?logo=kotlin)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

⭐ **If you find this project useful, consider giving it a Star!**

## ✨ Features

- 🎮 **Remote control** — Channel Up/Down control the backlight.
- ⚡ **Hold to repeat** — Hold a button for rapid continuous adjustment.
- 📊 **On-screen indicator** — Shows the current backlight level with a progress bar.
- ⏱️ **Auto-hide** — The overlay disappears automatically after 3 seconds.
- 🚀 **Boot persistence** — Starts automatically when the TV boots.
- 🪶 **Lightweight** — Designed to stay out of your way while running in the background.

## 🎬 How It Works

```text
          Android TV Remote
                 │
        ┌────────┴────────┐
        ▼                 ▼
   Channel Up        Channel Down
        │                 │
        └────────┬────────┘
                 ▼
       AccessibilityService
                 │
                 ▼
        Change backlight
                 │
        ┌────────┴────────┐
        ▼                 ▼
   System setting      UI Overlay
        │                 │
        └────────┬────────┘
                 ▼
          Updated TV
          backlight level
```

## 📸 Screenshots

_Add screenshots or a short GIF here showing the remote button being pressed and the backlight overlay appearing._

A short GIF is particularly useful for demonstrating the app on the GitHub project page.

## 📦 Installation

### 1. Build the APK

Clone the repository:

```bash
git clone https://github.com/DenysSepta/TVBacklightController.git
cd TVBacklightController
```

Open the project in Android Studio and build the APK.

Or build it from the command line:

```bash
./gradlew assembleDebug
```

### 2. Install on your Android TV

Connect to the TV using ADB and install the APK:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 3. Grant `WRITE_SECURE_SETTINGS`

The app needs access to modify the Android TV backlight setting. Grant the privileged permission using ADB:

```bash
adb shell pm grant com.tv.backlight android.permission.WRITE_SECURE_SETTINGS
```

> ⚠️ `WRITE_SECURE_SETTINGS` is a privileged Android permission and normally cannot be granted through the regular TV settings UI.

### 4. Enable the Accessibility Service

On the TV, open:

**Settings → Device Preferences → Accessibility → TV Backlight → ON**

The Accessibility Service allows the app to receive remote key events while other applications are running.

## 🎮 Usage

| Remote action | Result |
|---|---|
| **Channel Up** | Increase backlight by 5% |
| **Channel Down** | Decrease backlight by 5% |
| **Hold Channel Up** | Continuously increase backlight |
| **Hold Channel Down** | Continuously decrease backlight |

The overlay automatically disappears after approximately **3 seconds**.

## 📺 Compatibility

The app has been tested on a **Xiaomi Android TV** and works with the tested Xiaomi TV configuration.

> **Xiaomi TV users:** If you have a modern Xiaomi Android TV and the app works for you, please consider reporting your TV model in an Issue or Pull Request so the compatibility list can be expanded.

Android TV manufacturers can customize system settings and remote-control behavior, so compatibility may vary between models and firmware versions.

Known/tested:

| Manufacturer | Device | Result |
|---|---|---|
| **Xiaomi** | Android TV | ✅ Tested |

If it doesn't work on your TV, please open an Issue with:

- TV manufacturer and model
- Android TV / Google TV version
- What happens when pressing Channel Up/Down
- Relevant ADB logs, if available

## 🔧 Technical Details

### AccessibilityService

The app uses `AccessibilityService` to receive remote key events system-wide, allowing Channel Up/Down to work while the user is inside other applications.

### WindowManager

The current backlight level is displayed using a custom overlay based on:

```text
TYPE_ACCESSIBILITY_OVERLAY
```

### Backlight control

The application modifies the Android system's:

```text
picture_backlight
```

global setting to change the TV's backlight level.

## 🛠️ Build Requirements

- **Android Studio Ladybug** or newer
- **Android SDK 21+**
- **Kotlin 2.x**
- Android TV device with a compatible backlight implementation
- ADB access for initial setup

## 🤝 Contributing

Contributions are welcome!

If you have an Android TV that behaves differently, you can help improve compatibility:

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Test them on your TV.
5. Open a Pull Request.

Bug reports and compatibility reports are also valuable. Please include your TV model and Android TV version whenever possible.

## 💡 Why?

Adjusting a TV's backlight often means navigating through several picture settings menus.

With TV Backlight Controller:

**Press a button. Done.**

The goal is to make a common adjustment as fast and convenient as possible.

## ⭐ Support the Project

If this project is useful to you:

- ⭐ **Star the repository**
- 🐛 Report bugs
- 📺 Share your TV model and compatibility results
- 🔧 Submit improvements
- 📢 Share the project with other Android TV users

Every star and contribution helps the project reach more Android TV users.

## 📄 License

This project is licensed under the [MIT License](LICENSE).
