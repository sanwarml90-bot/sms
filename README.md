# Message Lock

Message Lock is an Android security utility that accepts exact SMS commands only from its two configured trusted numbers. It has no network permission, cloud component, analytics, or tracking.

## Commands

| Exact command (case-insensitive) | Result |
| --- | --- |
| `LOCK` | Immediately locks the phone after Device Admin is enabled. |
| `FIND` | Starts a continuous foreground siren. |
| `STOP` | Stops the siren. |

The app authorizes `7568324805` and `8112289897` only. SMS bodies must contain only the command (surrounding whitespace is allowed); sentences and typos are ignored.

## Device setup

1. Install the APK and open Message Lock.
2. Grant SMS permission when prompted.
3. Select **Set up Device Admin** and enable it. This is required for `LOCK`.
4. Optional: allow notifications so the active siren foreground-service notification is visible.

## Build

Requires JDK 17 and Android SDK 34:

```bash
gradle test lint assembleDebug
```

The FIND siren is synthesized on-device at runtime, so the repository contains no binary audio asset.

The GitHub Actions workflow runs tests and lint, builds `app-debug.apk`, and uploads it as an artifact for each push and pull request.
