# Smart TV Ad Player

A standalone offline Android TV advertisement and digital signage application.

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK with API 34 installed
- JDK 17

## Setup

1. Open this project in Android Studio.
2. Android Studio will auto-configure `local.properties` with your SDK path.
3. Sync Gradle when prompted.
4. Build and run on an Android TV device or emulator (API 23+).

## Minimum Android Version

- **minSdk = 23** (Android 6.0 Marshmallow)
- Supports all Android versions from 6.0 onward.

## Features

- **Offline operation** — no internet, backend, or API required
- **Android TV D-pad navigation** — all controls accessible via remote
- **Multiple layouts** — Full Screen, 1×2, 2×1, 2×2
- **Up to 4 independent zones** — each with its own content and timer
- **Video playback** — via ExoPlayer/Media3 with SAF file picker
- **Image display** — with aspect ratio preservation
- **Static text** — configurable size, color, alignment, background
- **Scrolling text** — configurable direction, speed, duration
- **Independent zone timers** — each zone expires independently
- **Company advertisement fallback** — expired zones show bundled ad
- **Local persistence** — settings survive app restart

## HOW TO REPLACE COMPANY ADVERTISEMENT

1. Replace `app/src/main/res/raw/company_intro.mp4` with your MP4 video.
2. Keep the filename exactly as `company_intro.mp4`.
3. Rebuild the APK in Android Studio.
4. Install the new APK on your TV device.
5. No backend configuration is required.
6. No API configuration is required.
7. No database configuration is required.

The advertisement is bundled directly inside the application.

## Architecture

```
ui/
  screens/          — All screen composables
  components/       — Reusable TV UI components (TvButton, TvCard)
  theme/            — Material3 dark TV theme

data/
  local/            — SharedPreferences persistence
  repository/       — Configuration repository

domain/
  model/            — AppConfig, ZoneConfig, LayoutType, ContentType, etc.

media/
  player/           — ExoPlayer wrapper, CompanyAdProvider

storage/            — Storage Access Framework helper

navigation/         — Screen route definitions
```

## Building

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```
