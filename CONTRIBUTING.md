# Contributing

Thanks for helping improve TV Backlight Controller.

Android TV manufacturers can implement backlight settings and remote keys differently. Reports from real devices help distinguish app issues from firmware-specific limitations.

## Bug reports

Before opening an issue, include as much of the following as you can:

- TV manufacturer and exact model.
- Android TV / Google TV version and firmware build.
- Whether the TV Backlight Accessibility service is enabled.
- Whether Channel Up/Down changes the on-screen indicator.
- Whether the physical backlight changes.
- Relevant output from `adb logcat -s TVBACKLIGHT`.

Please remove private information from logs before posting. Do not include account details, Wi-Fi credentials, or other secrets.

## Pull requests

- Keep changes focused and describe the problem they solve.
- Test on physical Android TV hardware when possible.
- Clearly state which TV model and firmware you tested.
- Do not claim broad compatibility based on a single device.
- Preserve the distinction between the app's tracked percentage and the TV's actual hardware backlight state.
- Avoid committing generated build output, local configuration, logs, or credentials.

## Testing notes

A successful write to `Settings.Global` does not necessarily mean every TV's panel backlight will change. Device-specific testing should confirm both the reported level and the visible result.
