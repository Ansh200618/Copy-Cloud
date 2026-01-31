# 📱 Building APK for Online Clipboard

This guide explains how to build an Android APK for the Online Clipboard mobile app.

## Prerequisites

1. **Expo Account**: Sign up at [expo.dev](https://expo.dev)
2. **EAS CLI**: Already installed globally
3. **Firebase Configuration**: Update `.env` file with real credentials
4. **Android Bundle ID**: Already configured in `app.json` as `com.ansh.onlineclipboard`

## Method 1: Build with EAS (Recommended)

EAS Build is Expo's cloud build service that builds your app in the cloud.

### Step 1: Login to Expo

```bash
cd app
eas login
```

Enter your Expo credentials when prompted.

### Step 2: Configure the Project

```bash
eas build:configure
```

This creates `eas.json` (already done).

### Step 3: Build APK

For preview/testing APK:
```bash
eas build --platform android --profile preview
```

For production APK:
```bash
eas build --platform android --profile production
```

### Step 4: Download APK

Once build completes, you'll get a download link. You can also find builds at:
```bash
eas build:list
```

Or visit: https://expo.dev/accounts/[your-account]/projects/online-clipboard/builds

## Method 2: Build Locally

### Requirements
- Android Studio installed
- Android SDK configured
- Java JDK 11 or higher

### Steps

1. **Install Android SDK**:
```bash
# On macOS
brew install --cask android-studio

# On Linux
sudo apt-get install android-studio
```

2. **Set Environment Variables**:
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

3. **Generate Android Project**:
```bash
npx expo prebuild --platform android
```

4. **Build APK**:
```bash
cd android
./gradlew assembleRelease
```

5. **Find APK**:
The APK will be at:
```
android/app/build/outputs/apk/release/app-release.apk
```

## Build Profiles Explained

### Development
```bash
eas build --profile development --platform android
```
- Development client
- Internal distribution
- For testing during development

### Preview
```bash
eas build --profile preview --platform android
```
- Production-like build
- APK format (not AAB)
- For internal testing/sharing
- **Best for sharing with friends/testers**

### Production
```bash
eas build --profile production --platform android
```
- Production build
- Ready for Google Play Store
- Optimized and signed

## Testing the APK

### On Physical Device

1. Download the APK to your Android device
2. Enable "Install from Unknown Sources" in Settings
3. Tap the APK file to install
4. Open the app and test

### On Emulator

1. Start Android emulator
2. Drag and drop APK onto emulator
3. Or use adb:
```bash
adb install app-release.apk
```

## Build Configuration

### app.json
```json
{
  "expo": {
    "name": "Online Clipboard",
    "slug": "online-clipboard",
    "version": "1.0.0",
    "android": {
      "package": "com.ansh.onlineclipboard",
      "versionCode": 1,
      "adaptiveIcon": {
        "foregroundImage": "./assets/adaptive-icon.png",
        "backgroundColor": "#ffffff"
      }
    }
  }
}
```

### eas.json
Already configured with three profiles:
- **development**: For dev testing
- **preview**: For APK builds (recommended)
- **production**: For store submission

## Troubleshooting

### Build Fails with Firebase Error
- Ensure `.env` file has valid Firebase credentials
- Check that Firebase project is active

### "Invalid keystore" Error
- EAS handles signing automatically
- For local builds, generate a keystore:
```bash
keytool -genkey -v -keystore my-release-key.keystore \
  -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
```

### Build Takes Too Long
- EAS builds typically take 10-20 minutes
- Check build status: `eas build:list`

### APK Won't Install
- Ensure device allows unknown sources
- Check minimum SDK version (Android 5.0+)
- Try uninstalling old version first

## Quick Commands Reference

```bash
# Login to Expo
eas login

# Build preview APK (recommended)
eas build --platform android --profile preview

# Check build status
eas build:list

# View build details
eas build:view [build-id]

# Submit to Play Store
eas submit --platform android
```

## Firebase Configuration Before Building

**CRITICAL**: Before building, update `.env` file:

```bash
# Edit .env file
EXPO_PUBLIC_FIREBASE_API_KEY=your_real_api_key
EXPO_PUBLIC_FIREBASE_AUTH_DOMAIN=your_real_auth_domain
EXPO_PUBLIC_FIREBASE_PROJECT_ID=your_real_project_id
EXPO_PUBLIC_FIREBASE_STORAGE_BUCKET=your_real_storage_bucket
EXPO_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your_real_sender_id
EXPO_PUBLIC_FIREBASE_APP_ID=your_real_app_id
```

## App Size

Expected APK size:
- **Preview build**: ~40-50 MB
- **Production build**: ~35-45 MB (optimized)

## Build Time

- **EAS Cloud Build**: 10-20 minutes
- **Local Build**: 5-10 minutes (after setup)

## Next Steps After Building

1. **Test APK** thoroughly on different devices
2. **Gather feedback** from test users
3. **Fix any issues** found during testing
4. **Create production build** for Play Store
5. **Submit to Google Play** using `eas submit`

## Support

- **EAS Build Docs**: https://docs.expo.dev/build/introduction/
- **Expo Forums**: https://forums.expo.dev/
- **React Native Docs**: https://reactnative.dev/

---

**Note**: Building requires an active internet connection and Expo account. The first build may take longer as EAS sets up your project.

## Estimated Costs

- **EAS Build**: Free tier includes builds (check expo.dev for current limits)
- **Paid Plan**: $29/month for unlimited builds and priority queue

---

Happy Building! 🚀
