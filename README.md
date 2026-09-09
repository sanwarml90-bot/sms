# MessageLock - SMS Application

A secure messaging application built with Android (Kotlin) to lock and protect your SMS messages.

## Features

- 🔒 Message encryption and security
- 📱 Android native app
- 🎯 User-friendly UI
- 🚀 Fast and reliable

## Tech Stack

- **Language**: Kotlin
- **Platform**: Android (API 24+)
- **Build System**: Gradle
- **UI Framework**: AndroidX & ConstraintLayout
- **Minimum SDK**: 24
- **Target SDK**: 34

## Project Structure

```
MessageLock/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/messagelock/
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── mipmap/
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 17 or higher
- Gradle 8.4+

### Building

```bash
# Clone the repository
git clone https://github.com/sanwarml90-bot/sms.git
cd sms

# Build debug APK
gradle assembleDebug

# Build release APK
gradle assembleRelease
```

## CI/CD

This project uses GitHub Actions for automated builds. The workflow automatically builds a debug APK on every push to `main` or `master` branch.

## License

This project is open source and available under the MIT License.

## Author

sanwarml90-bot