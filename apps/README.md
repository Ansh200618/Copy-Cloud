# Copy Cloud - Android Application

This folder contains the Android application for Copy Cloud.

## Overview

Copy Cloud Android app is a WebView-based application that wraps the web version of Copy Cloud (https://copycloud.vercel.app/) into a native Android APK. It provides the same features and UI as the web application with additional native Android capabilities.

## Features

- **Full Web Functionality**: All features from the web version
- **WebView Integration**: Native Android WebView for smooth performance
- **File Upload/Download**: Native file picker and download manager integration
- **Offline Detection**: Shows alert when no internet connection is available
- **Swipe to Refresh**: Pull down to refresh the page
- **Back Button Support**: Navigate back through web history
- **Splash Screen**: Professional splash screen with app branding
- **Material Design**: Follows Android Material Design guidelines
- **Permissions**: Handles storage permissions for file operations

## Requirements

- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK 24+ (Android 7.0 and above)
- Gradle 8.2+

## Project Structure

```
apps/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/copycloud/app/
│   │       │   ├── MainActivity.java
│   │       │   └── SplashActivity.java
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_main.xml
│   │       │   │   └── activity_splash.xml
│   │       │   ├── values/
│   │       │   │   ├── strings.xml
│   │       │   │   ├── colors.xml
│   │       │   │   └── themes.xml
│   │       │   ├── mipmap-*/
│   │       │   │   └── ic_launcher.png
│   │       │   └── xml/
│   │       │       └── network_security_config.xml
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Building the APK

### Using Android Studio

1. Open Android Studio
2. Click "Open an existing project"
3. Navigate to the `apps` folder and select it
4. Wait for Gradle sync to complete
5. Click **Build > Build Bundle(s) / APK(s) > Build APK(s)**
6. The APK will be generated in `app/build/outputs/apk/debug/app-debug.apk`

### Using Command Line

```bash
cd apps

# For Debug APK
./gradlew assembleDebug

# For Release APK (requires signing configuration)
./gradlew assembleRelease
```

### Build Output

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## Installing the APK

### On Physical Device

1. Enable "Developer Options" on your Android device
2. Enable "USB Debugging" in Developer Options
3. Connect device via USB
4. Run: `adb install app/build/outputs/apk/debug/app-debug.apk`

### On Emulator

1. Start an Android emulator from Android Studio
2. Drag and drop the APK file onto the emulator window

### Manual Installation

1. Transfer the APK file to your Android device
2. Open the file manager on your device
3. Tap the APK file
4. Allow installation from unknown sources if prompted
5. Tap "Install"

## Configuration

### Changing the Web URL

To point the app to a different URL, edit `MainActivity.java`:

```java
private static final String BASE_URL = "https://your-custom-url.com/";
```

### App Branding

- **App Name**: Edit `app/src/main/res/values/strings.xml`
- **App Icon**: Replace icons in `app/src/main/res/mipmap-*/` folders
- **Colors**: Edit `app/src/main/res/values/colors.xml`
- **Splash Screen**: Edit `app/src/main/res/layout/activity_splash.xml`

## Permissions

The app requests the following permissions:

- `INTERNET`: Required for loading web content
- `ACCESS_NETWORK_STATE`: Check internet connectivity
- `READ_EXTERNAL_STORAGE`: Read files for upload (Android 12 and below)
- `WRITE_EXTERNAL_STORAGE`: Write downloaded files (Android 9 and below)
- `READ_MEDIA_IMAGES/VIDEO/AUDIO`: Read media files (Android 13+)

## Troubleshooting

### Gradle Sync Failed

- Ensure you have the correct Android SDK and build tools installed
- Check your internet connection for downloading dependencies
- Update Android Studio to the latest version

### APK Installation Failed

- Enable "Install from Unknown Sources" in device settings
- Check if the device meets minimum SDK requirements (Android 7.0+)
- Ensure sufficient storage space on the device

### WebView Not Loading

- Check internet connection
- Verify the BASE_URL is accessible
- Check logcat for JavaScript errors: `adb logcat | grep chromium`

## Testing

### Run on Emulator

```bash
./gradlew installDebug
adb shell am start -n com.copycloud.app/.MainActivity
```

### Run on Physical Device

```bash
./gradlew installDebug
adb shell am start -n com.copycloud.app/.MainActivity
```

## Release Build

To create a signed release APK:

1. Generate a keystore:
```bash
keytool -genkey -v -keystore copycloud-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias copycloud
```

2. Add signing config to `app/build.gradle`:
```gradle
android {
    signingConfigs {
        release {
            storeFile file("copycloud-release-key.jks")
            storePassword "your-store-password"
            keyAlias "copycloud"
            keyPassword "your-key-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

3. Build release APK:
```bash
./gradlew assembleRelease
```

## Version Information

- **App Version**: 1.0
- **Version Code**: 1
- **Target SDK**: 34 (Android 14)
- **Min SDK**: 24 (Android 7.0)
- **Package**: com.copycloud.app

## Support

For issues or questions:
- Repository: https://github.com/Ansh200618/Online-Clipboard
- Developer: Anshdeep Singh

## License

This project follows the same license as the main Copy Cloud project.
