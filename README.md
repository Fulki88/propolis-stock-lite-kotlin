# Propolis Stock Lite Kotlin

A modern Android application for managing propolis stock inventory built with Kotlin and Jetpack Compose.

## 🚀 Features

- **Modern UI/UX**: Beautiful Material Design 3 interface with custom color palette
- **Stock Management**: Create, view, and manage propolis inventory
- **Purchase System**: Handle buyer purchases with ease
- **Multi-module Architecture**: Clean, maintainable code structure
- **Dark/Light Theme**: Automatic theme switching based on system preference
- **Responsive Design**: Optimized for various screen sizes

## 🎨 Design System

### Color Palette
- **Primary Dark Red**: `#780000` - Deep, professional red
- **Primary Red**: `#c1121f` - Vibrant primary color
- **Primary Cream**: `#fdf0d5` - Warm, elegant cream
- **Primary Dark Blue**: `#003049` - Professional dark blue
- **Primary Light Blue**: `#669bbc` - Calm, trustworthy blue

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Multi-module MVVM
- **Dependency Injection**: Hilt
- **Build System**: Gradle with Kotlin DSL
- **Material Design**: Material 3
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **JDK**: 17 (required)
- Kotlin: **1.9.24**, AGP **8.4.2**, Compose Compiler **1.5.14**

## Run

1. **Install JDK 17** (recommended):
   ```bash
   brew install --cask temurin17
   ```

2. Open the project in **Android Studio** and let it sync.
   - If Studio prompts for **Gradle Wrapper**, accept/generate it.

3. Select `app` → **Run** to install on a device/emulator.
   - First launch seeds demo products from `core/data/src/main/assets/seed.json`.

## Build APK (Debug)

- From Android Studio: **Build > Build Bundle(s)/APK(s) > Build APK(s)**
- Or via CLI (after wrapper is generated):
  ```bash
  ./gradlew :app:assembleDebug
  ```

The APK will be under: `app/build/outputs/apk/debug/app-debug.apk`

## Structure

```
app/                    # entry module (navigation, theme)
core/common/            # error types, shared utils
core/designsystem/      # reusable Compose UI pieces
core/data/              # Room DB, Repository, seed loader
feature/inventory/      # inventory list UI
feature/sales/          # sales flow placeholder
```

## Notes

- Compose, Hilt, Room are wired with stable versions that work with JDK 17.
- Seed data is loaded on first run; you can modify `seed.json` to your catalog.
- Next steps: implement **sales/cart**, **promotions engine**, **daily summary**.

## License

No license specified (private by default). Add MIT/Apache-2.0 if you plan to open source.


## macOS (Apple Silicon, 15.6.1) Setup — Free JDK

All tools below are **free** (no license fees).

```bash
# Homebrew (if not installed): https://brew.sh
# Free JDK 17 (Temurin / Adoptium)
brew install --cask temurin17

# Optional: Android Studio (Apple Silicon build)
brew install --cask android-studio

# Set JAVA_HOME for this shell session
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# (First time) Accept Android SDK licenses after Studio installs SDKs
yes | "$HOME/Library/Android/sdk/tools/bin/sdkmanager" --licenses || true
```

If you prefer to persist JAVA_HOME, add this to your `~/.zshrc`:

```zsh
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH="$JAVA_HOME/bin:$PATH"
```

Temurin is maintained by the Eclipse Adoptium project and is free to use for work and personal machines.
