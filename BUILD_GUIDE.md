# Build Instructions for Android APK

## Prerequisites
- Android Studio or Android SDK installed
- Java JDK 11 or higher
- Internet connection (for downloading dependencies)

## Building the APK

### Method 1: Using Android Studio (Recommended)
1. Open Android Studio
2. Click "Open an Existing Project"
3. Navigate to `Online-Clipboard/apps` directory
4. Wait for Gradle sync to complete
5. Go to `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
6. APK will be created in `apps/app/build/outputs/apk/debug/`

### Method 2: Using Command Line
```bash
cd Online-Clipboard/apps
./gradlew assembleDebug
```

The APK will be located at:
`apps/app/build/outputs/apk/debug/app-debug.apk`

### Method 3: Release Build (Signed)
```bash
cd Online-Clipboard/apps
./gradlew assembleRelease
```

## Installation on Device

### Via ADB:
```bash
adb install apps/app/build/outputs/apk/debug/app-debug.apk
```

### Via File Transfer:
1. Copy APK to device
2. Enable "Unknown Sources" in device settings
3. Open APK file and install

## Testing the Changes

### 1. Test No Gold Design
- Open app
- Check all screens (Send, Retrieve, Settings, History)
- Verify primary color is blue (#4169E1), not gold
- Check themes - ensure no gold colors appear

### 2. Test Device Targeting
**Setup:**
- Note your device code from Settings tab (e.g., 1234-5678)
- Have a second device or note a different 8-digit code

**Test Case 1 - Send to All Devices:**
1. Go to Send tab
2. Enter some text
3. Leave "Send to Specific Device" field empty
4. Click "Generate Secure Code"
5. Copy the code
6. Go to Retrieve tab
7. Enter the code - should show content ✓

**Test Case 2 - Send to Specific Device:**
1. Go to Send tab
2. Enter some text
3. Enter YOUR device code (from Settings) in "Send to Specific Device"
4. Click "Generate Secure Code"
5. Copy the code
6. Go to Retrieve tab
7. Enter the code - should show content ✓

**Test Case 3 - Wrong Device:**
1. Go to Send tab
2. Enter some text
3. Enter a DIFFERENT 8-digit code (e.g., 99999999)
4. Click "Generate Secure Code"
5. Copy the code
6. Go to Retrieve tab
7. Enter the code - should show "content is for device: 9999-9999" ✓

**Test Case 4 - Invalid Device Code:**
1. Go to Send tab
2. Enter some text
3. Enter invalid code (e.g., "123" or "abc")
4. Click "Generate Secure Code"
5. Should show error: "Invalid device code (must be 8 digits)" ✓

### 3. Test QR Scanner
1. Generate a code in Send tab
2. Screenshot the QR code
3. On another device/emulator, go to Retrieve tab
4. Click "Scan QR Code"
5. Point at the QR code (or use screenshot)
6. Scanner should show:
   - Square viewfinder (not rectangular barcode scanner)
   - 250dp x 250dp square
   - Blue laser line
   - "Point camera at QR code" instruction at bottom
7. Should auto-detect and retrieve content ✓

### 4. Test Settings
**Themes:**
1. Go to Settings tab
2. Tap each theme card
3. Verify theme changes and app restarts
4. Test both dark themes (Ocean, Midnight, Purple, Forest)
5. Test light themes (Sunset, Sky Blue, Pink, Mint)
6. Verify status bar and navigation bar colors match theme

**Device Code:**
1. Go to Settings tab
2. See your device code (formatted as 1234-5678)
3. Tap on it - should copy to clipboard
4. Paste somewhere - verify copied correctly

**Connected Devices:**
1. Go to Settings tab
2. Click "Add Device"
3. Enter 8-digit code
4. Should add to list
5. Tap device to copy code
6. Long press or click X to remove
7. Try adding 5 devices - 6th should show "Max 5 devices" error

## Verification Checklist

- [ ] App builds without errors
- [ ] No gold colors visible anywhere
- [ ] Device targeting field appears in Send tab
- [ ] Can send without device code (works for all)
- [ ] Can send with device code (shows filtering message)
- [ ] QR scanner shows square viewfinder
- [ ] All 8 themes work correctly
- [ ] Device code copying works
- [ ] Connected devices add/remove works
- [ ] No crashes or errors in logcat

## Screenshots to Capture

1. **Send Tab** - showing device targeting field (empty and filled)
2. **QR Scanner** - square viewfinder while scanning
3. **Retrieve Tab** - message when content is for different device
4. **Settings Tab** - showing themes and device code
5. **Theme Examples** - at least 2-3 different themes applied
6. **Success Screen** - after generating code with QR

## Logcat Commands

Watch for errors:
```bash
adb logcat | grep -E "CopyCloud|Error|Exception"
```

Filter by app:
```bash
adb logcat | grep "com.copycloud.app"
```

## Troubleshooting

**Build fails with missing SDK:**
- Install Android SDK 34: `sdkmanager "platforms;android-34"`

**Gradle sync issues:**
- Clean: `./gradlew clean`
- Rebuild: `./gradlew build --refresh-dependencies`

**APK installation fails:**
- Enable "Install from Unknown Sources"
- Check Android version (minimum API 24 / Android 7.0)

**Camera permission denied:**
- Go to App Settings → Permissions → Camera → Allow

**Network errors in app:**
- Check internet connection
- Verify Supabase credentials in SupabaseClient.java
- Check Supabase database is accessible
