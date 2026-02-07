# Building the Copy Cloud Native Android App

## Prerequisites

1. **Java Development Kit (JDK)**
   - JDK 11 or later required
   - Verify: `java -version`

2. **Android SDK** (if using Android Studio)
   - Download from: https://developer.android.com/studio

3. **Gradle** (included via wrapper)
   - No separate installation needed

## Build Methods

### Method 1: Using Gradle Command Line (Recommended for CI/CD)

```bash
cd apps

# Clean previous builds
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Method 2: Using Android Studio

1. **Open Project**
   ```
   File → Open → Select the 'apps' folder
   ```

2. **Gradle Sync**
   - Wait for automatic Gradle sync to complete
   - If sync fails, click "Sync Project with Gradle Files"

3. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

4. **Locate APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## Configuration

### Required: Update Supabase Credentials

Edit `app/src/main/java/com/copycloud/app/api/SupabaseClient.java`:

```java
// Replace with your actual Supabase project credentials
private static final String SUPABASE_URL = "https://your-project.supabase.co";
private static final String SUPABASE_KEY = "your-anon-key-here";
```

**Note:** These must match the credentials used in the web app (`index.html`)

## Installing the APK

### On Physical Device

**Via USB (ADB):**
```bash
# Enable USB Debugging on your phone first
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Via File Transfer:**
1. Copy APK to device
2. Open file manager on device
3. Tap APK file
4. Allow "Install from Unknown Sources" if prompted
5. Tap "Install"

### On Android Emulator

**Drag & Drop:**
1. Start Android emulator from Android Studio
2. Drag `app-debug.apk` onto emulator window

**Or via ADB:**
```bash
adb -e install app/build/outputs/apk/debug/app-debug.apk
```

## Building Release APK (For Production)

### Step 1: Generate Signing Key

```bash
keytool -genkey -v -keystore copycloud-release.keystore \
  -alias copycloud \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

### Step 2: Configure Signing

Add to `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../copycloud-release.keystore")
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

### Step 3: Build Release

```bash
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

## Troubleshooting

### Problem: Gradle sync fails

**Solution:**
```bash
# Delete Gradle cache
rm -rf ~/.gradle/caches

# Re-sync in Android Studio
./gradlew clean build --refresh-dependencies
```

### Problem: SDK not found

**Solution:**
Create `local.properties` in apps folder:
```properties
sdk.dir=/path/to/Android/Sdk
```

### Problem: Build fails with "package does not exist"

**Solution:**
- Ensure all dependencies in `app/build.gradle` are downloaded
- Run `./gradlew build --refresh-dependencies`
- Check internet connection

### Problem: APK installs but crashes

**Solution:**
1. Check Supabase credentials are correct
2. View logs: `adb logcat | grep CopyCloud`
3. Ensure device has internet connection
4. Check camera permission granted (for QR scanner)

## Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## Project Structure

```
apps/
├── app/
│   ├── build.gradle                    # App-level build config
│   ├── proguard-rules.pro              # ProGuard rules
│   └── src/main/
│       ├── AndroidManifest.xml         # App manifest
│       ├── java/com/copycloud/app/
│       │   ├── MainActivity.java       # Main activity with tabs
│       │   ├── fragments/              # UI fragments
│       │   │   ├── SendFragment.java
│       │   │   ├── RetrieveFragment.java
│       │   │   └── AboutFragment.java
│       │   ├── adapters/               # RecyclerView adapters
│       │   ├── models/                 # Data models
│       │   ├── api/                    # Supabase client
│       │   │   └── SupabaseClient.java
│       │   └── utils/                  # Utility classes
│       │       ├── QRCodeGenerator.java
│       │       ├── CodeGenerator.java
│       │       └── NetworkUtils.java
│       └── res/                        # Resources
│           ├── layout/                 # XML layouts
│           ├── values/                 # Strings, colors, themes
│           └── mipmap-*/              # App icons
├── build.gradle                        # Project-level build
├── settings.gradle                     # Gradle settings
└── gradle.properties                   # Gradle properties
```

## Dependencies

- **AndroidX Libraries**: Core Android components
- **Material Design 3**: UI components
- **OkHttp**: HTTP client for API calls
- **Gson**: JSON parsing
- **Glide**: Image loading
- **ZXing**: QR code generation and scanning

## App Features

✅ **100% Native Android** - No WebView
✅ **Material Design 3** - Modern UI
✅ **QR Code Support** - Generate & scan
✅ **Text Sharing** - Upload and retrieve text
✅ **File Sharing** - Upload and download files (in progress)
✅ **Cross-Platform** - Works with web app
✅ **24-Hour Expiry** - Auto-delete feature

## Version Information

- **Version Code**: 1
- **Version Name**: 1.0.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Package**: com.copycloud.app

## Support

For issues or questions:
- GitHub: https://github.com/Ansh200618/Online-Clipboard
- Check logs: `adb logcat | grep CopyCloud`

## License

This project follows the same license as the main Copy Cloud project.
