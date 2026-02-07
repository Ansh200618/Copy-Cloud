# Copy Cloud Android - Build & Installation Guide

## Quick Start

### Prerequisites
1. **Android Studio** (Recommended) - Download from https://developer.android.com/studio
2. **JDK 11 or later** - Bundled with Android Studio or download separately
3. **Android SDK** - Installed through Android Studio SDK Manager

### Method 1: Build with Android Studio (Easiest)

1. **Open the Project**
   - Launch Android Studio
   - Click "Open an existing project"
   - Navigate to and select the `apps` folder
   - Click "OK"

2. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle
   - This may take a few minutes on first run
   - Wait for "Gradle sync finished" message

3. **Build the APK**
   - Click **Build** menu → **Build Bundle(s) / APK(s)** → **Build APK(s)**
   - Wait for build to complete
   - Click "locate" in the notification to open APK folder
   - Or find it at: `app/build/outputs/apk/debug/app-debug.apk`

### Method 2: Build with Command Line

```bash
# Navigate to apps directory
cd apps

# Make build script executable (Linux/macOS)
chmod +x build.sh

# Run build script
./build.sh
```

**Or manually:**

```bash
cd apps

# Initialize Gradle wrapper (if not exists)
gradle wrapper --gradle-version 8.2

# Build debug APK
./gradlew assembleDebug

# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

## Installation Guide

### Install on Physical Android Device

#### Option A: Via USB (ADB)

1. **Enable Developer Mode** on your Android device:
   - Go to **Settings** → **About Phone**
   - Tap **Build Number** 7 times
   - You'll see "You are now a developer!"

2. **Enable USB Debugging**:
   - Go to **Settings** → **Developer Options**
   - Enable **USB Debugging**

3. **Connect and Install**:
   ```bash
   # Connect your device via USB
   # Accept "Allow USB debugging" prompt on device
   
   # Install APK
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

#### Option B: Direct Installation (No USB)

1. **Transfer APK to your device**:
   - Email the APK to yourself
   - Use Google Drive, Dropbox, or similar
   - Transfer via Bluetooth
   - Use a file transfer app

2. **Install on device**:
   - Open the APK file on your device
   - Tap "Install" button
   - If prompted, enable "Install from Unknown Sources"
   - Wait for installation to complete
   - Tap "Open" or find "Copy Cloud" in your app drawer

### Install on Android Emulator

1. **Create Emulator in Android Studio**:
   - Click **Device Manager** (phone icon)
   - Click **Create Device**
   - Select a device (e.g., Pixel 4)
   - Select system image (Android 7.0 or higher)
   - Click **Finish**

2. **Install APK**:
   - Start the emulator
   - Drag and drop `app-debug.apk` onto the emulator window
   - Or use: `adb install app/build/outputs/apk/debug/app-debug.apk`

## Troubleshooting

### Build Issues

#### "ANDROID_HOME not set"
```bash
# Find your Android SDK path
# Common locations:
# - macOS: ~/Library/Android/sdk
# - Linux: ~/Android/Sdk
# - Windows: C:\Users\YourUsername\AppData\Local\Android\Sdk

# Set ANDROID_HOME
export ANDROID_HOME=/path/to/android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

#### "SDK location not found"
Create `local.properties` file in `apps/` folder:
```properties
sdk.dir=/path/to/android/sdk
```

#### "Could not resolve dependencies"
- Check internet connection
- Update Android Studio and SDK
- Try: `./gradlew clean build --refresh-dependencies`

#### "Gradle sync failed"
- Update Gradle wrapper: `gradle wrapper --gradle-version 8.2`
- Invalidate caches: **File** → **Invalidate Caches / Restart** in Android Studio

### Installation Issues

#### "App not installed" error
- Ensure device has sufficient storage
- Uninstall previous version if exists
- Check Android version (requires Android 7.0+)

#### "Install blocked" message
- Go to **Settings** → **Security**
- Enable **Unknown Sources** or **Install Unknown Apps**
- Try installation again

#### APK won't open on device
- Ensure file is completely downloaded
- Check file size matches build output
- Try different file transfer method

## Building Release APK (For Production)

### 1. Generate Signing Key

```bash
keytool -genkey -v -keystore copycloud-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias copycloud
```

Enter your information when prompted.

### 2. Configure Signing

Add to `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../copycloud-release-key.jks")
            storePassword "your-store-password"
            keyAlias "copycloud"
            keyPassword "your-key-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 3. Build Release APK

```bash
./gradlew assembleRelease
```

Release APK: `app/build/outputs/apk/release/app-release.apk`

## Customization

### Change App Name
Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

### Change App Icon
Replace icons in:
- `app/src/main/res/mipmap-mdpi/ic_launcher.png` (48x48)
- `app/src/main/res/mipmap-hdpi/ic_launcher.png` (72x72)
- `app/src/main/res/mipmap-xhdpi/ic_launcher.png` (96x96)
- `app/src/main/res/mipmap-xxhdpi/ic_launcher.png` (144x144)
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher.png` (192x192)

### Change Web URL
Edit `MainActivity.java`:
```java
private static final String BASE_URL = "https://your-url.com/";
```

### Change Colors
Edit `app/src/main/res/values/colors.xml`:
```xml
<color name="primary">#6366F1</color>
<color name="splash_background">#0F172A</color>
```

## Technical Details

### App Information
- **Package Name**: com.copycloud.app
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Version**: 1.0
- **Architecture**: WebView-based hybrid app

### Permissions Required
- `INTERNET` - Load web content
- `ACCESS_NETWORK_STATE` - Check connectivity
- `READ_EXTERNAL_STORAGE` - File uploads (Android ≤12)
- `READ_MEDIA_*` - File uploads (Android ≥13)

### App Size
- **Debug APK**: ~3-4 MB
- **Release APK**: ~2-3 MB (with ProGuard)

## Testing Checklist

Before releasing, test these features:

- [ ] App launches successfully
- [ ] Splash screen displays correctly
- [ ] Web content loads properly
- [ ] Text upload/download works
- [ ] File upload works (test with image/document)
- [ ] File download works
- [ ] Back button navigation works
- [ ] Swipe to refresh works
- [ ] No internet dialog appears when offline
- [ ] App icon displays correctly in launcher
- [ ] Permissions are requested and granted
- [ ] App works on different screen sizes

## Support & Resources

- **Repository**: https://github.com/Ansh200618/Online-Clipboard
- **Android Developer**: https://developer.android.com
- **Gradle Docs**: https://docs.gradle.org
- **WebView Guide**: https://developer.android.com/guide/webapps/webview

## Common ADB Commands

```bash
# List connected devices
adb devices

# Install APK
adb install app-debug.apk

# Uninstall app
adb uninstall com.copycloud.app

# View logs
adb logcat | grep "CopyCloud"

# Take screenshot
adb shell screencap -p /sdcard/screen.png
adb pull /sdcard/screen.png

# Clear app data
adb shell pm clear com.copycloud.app
```

## Building for Different Architectures

To build for specific CPU architectures (reduces APK size):

```gradle
android {
    splits {
        abi {
            enable true
            reset()
            include 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
            universalApk true
        }
    }
}
```

This creates separate APKs for each architecture.

---

**Need Help?** Create an issue on GitHub or contact the developer.
