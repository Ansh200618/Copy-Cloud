# 📦 How to Build the APK

## ⚠️ Current Status
The APK **cannot be built in this CI environment** due to network restrictions blocking access to Google's Maven repository. However, you can easily build it on your local machine!

## 🏗️ Build Instructions (Local Machine)

### Prerequisites
1. **Android Studio** - Download from https://developer.android.com/studio
2. **JDK 17** or higher
3. **Android SDK** (comes with Android Studio)

### Method 1: Build with Android Studio (Easiest) 👍

1. Open Android Studio
2. Click **File → Open**
3. Navigate to and select the `apps` folder
4. Wait for Gradle sync to complete
5. Click **Build → Build Bundle(s) / APK(s) → Build APK(s)**
6. Wait for build to complete
7. Click **"locate"** in the notification

**APK Location**: `apps/app/build/outputs/apk/debug/app-debug.apk`

### Method 2: Build via Command Line

```bash
cd apps

# On Linux/Mac:
chmod +x gradlew
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

**APK Location**: `apps/app/build/outputs/apk/debug/app-debug.apk`

## 📍 Where to Find Your APK

After successful build, the APK will be at:
```
Online-Clipboard/
└── apps/
    └── app/
        └── build/
            └── outputs/
                └── apk/
                    └── debug/
                        └── app-debug.apk  ⬅️ YOUR APK IS HERE
```

## 📱 Installing the APK

### On Android Device:
1. Transfer `app-debug.apk` to your phone
2. Open the file
3. Allow "Install from Unknown Sources" if prompted
4. Click Install
5. Done! 🎉

### Via ADB (Developer):
```bash
adb install apps/app/build/outputs/apk/debug/app-debug.apk
```

## 🚀 Build for Release (Play Store)

To create a signed release APK:

1. Generate keystore:
```bash
keytool -genkey -v -keystore copycloud.keystore -alias copycloud -keyalg RSA -keysize 2048 -validity 10000
```

2. Create `apps/keystore.properties`:
```properties
storeFile=../copycloud.keystore
storePassword=YOUR_PASSWORD
keyAlias=copycloud
keyPassword=YOUR_PASSWORD
```

3. Build release APK:
```bash
./gradlew assembleRelease
```

**Release APK Location**: `apps/app/build/outputs/apk/release/app-release.apk`

## 🔧 Troubleshooting

### Build Failed?

1. **Update Gradle**: In Android Studio, go to Help → Check for Updates
2. **Clean Build**: `./gradlew clean`
3. **Invalidate Caches**: File → Invalidate Caches and Restart
4. **Check Internet**: Make sure you can access dl.google.com

### Gradle Sync Issues?

- Click **File → Sync Project with Gradle Files**
- Make sure you have Android SDK installed
- Check that JAVA_HOME points to JDK 17+

## 📊 Build Variants

The project has multiple build variants:

- **debug**: Development build with debugging enabled
- **release**: Production build (requires signing)

## 💾 APK Size

Expected APK size:
- Debug: ~15-20 MB
- Release (optimized): ~8-12 MB

## ✅ What's Included in the APK

Your premium native Android app includes:
- ✨ 8 luxury themes
- 📱 Device targeting (8-digit codes)
- 📋 Local history storage
- 📷 QR code scanner
- 🔔 Push notifications
- 💎 Premium glassmorphism UI
- 🥇 Gold gradient design
- 🔗 Social links integration

## 🎯 Next Steps After Building

1. Install on your Android device
2. Test all features
3. Share with testers
4. Collect feedback
5. Prepare for Play Store release

## 📞 Need Help?

If you encounter any issues:
1. Check the error message carefully
2. Review Android Studio's build output
3. Make sure all dependencies are downloaded
4. Try a clean rebuild

---

**Note**: This is a NATIVE Android app (not a WebView wrapper). It requires a proper Android build environment to compile.
