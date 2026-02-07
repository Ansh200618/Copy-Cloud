# Copy Cloud - Native Android App

A modern, native Android application for Copy Cloud with proper app vibes and native UI.

## Features

- ✨ **Native Android UI** - Built with Material Design 3
- 📤 **Send Content** - Share text or files with generated 6-character codes
- 📥 **Retrieve Content** - Enter code or scan QR code to retrieve shared content
- 📷 **QR Code Scanner** - Built-in QR scanner to automatically populate codes
- 🔒 **Secure** - All data expires after 24 hours
- 🎨 **Modern Design** - Dark theme with glassmorphic cards
- ⚡ **Fast & Lightweight** - Native performance

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or later
- Android SDK 24+ (Android 7.0 and above)
- Gradle 8.2+

## Building the APK

### Using Gradle Command Line

```bash
cd apps

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

### Using Android Studio

1. Open Android Studio
2. Click **File > Open** and select the `apps` folder
3. Wait for Gradle sync
4. Click **Build > Build Bundle(s) / APK(s) > Build APK(s)**
5. Find the APK in `app/build/outputs/apk/`

## Project Structure

```
apps/
├── app/
│   ├── src/main/
│   │   ├── java/com/copycloud/app/
│   │   │   ├── MainActivity.java
│   │   │   ├── fragments/
│   │   │   │   ├── SendFragment.java
│   │   │   │   ├── RetrieveFragment.java
│   │   │   │   └── AboutFragment.java
│   │   │   ├── adapters/
│   │   │   ├── models/
│   │   │   ├── api/
│   │   │   └── utils/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Configuration

### Supabase Setup

Update the Supabase credentials in `SupabaseClient.java`:

```java
private static final String SUPABASE_URL = "https://your-project.supabase.co";
private static final String SUPABASE_KEY = "your-anon-key";
```

## Key Features

### QR Code Integration

- **Generate QR Codes**: Automatically generated for each uploaded content
- **Scan QR Codes**: Built-in scanner to quickly retrieve content
- **Cross-Platform**: QR codes work between web and mobile apps

### Native UI Components

- **Tab Navigation**: Swipe between Send, Retrieve, and About tabs
- **Material Design 3**: Modern, beautiful UI following latest guidelines
- **Dark Theme**: Eye-friendly dark theme
- **Smooth Animations**: Native animations for better UX

## Permissions

The app requests:
- `INTERNET` - For API communication
- `ACCESS_NETWORK_STATE` - Check connectivity
- `CAMERA` - QR code scanning
- `READ_EXTERNAL_STORAGE` - File uploads (Android 12 and below)
- `READ_MEDIA_*` - Media file access (Android 13+)

## Dependencies

- AndroidX Libraries
- Material Design Components 3
- OkHttp - HTTP client
- Gson - JSON parsing
- Glide - Image loading
- ZXing - QR code scanning and generation

## Version

- **App Version**: 1.0.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Package**: com.copycloud.app

## License

This project follows the same license as the main Copy Cloud project.
