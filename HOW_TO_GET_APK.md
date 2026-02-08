# 📱 How to Get the Copy Cloud Android APK

## TL;DR - Quick Answer

**After merging this PR:**
1. Wait 5 minutes for GitHub Actions to build
2. Go to: https://github.com/Ansh200618/Online-Clipboard/releases
3. Download `app-debug.apk`
4. Install on Android device

✅ **No setup required** - It builds automatically!

---

## Detailed Instructions

### Step 1: Merge the Pull Request

Simply click "Merge Pull Request" and confirm. That's it!

### Step 2: Wait for Automatic Build (5 minutes)

GitHub Actions will automatically:
- Build the Android APK using Gradle
- Upload it as an artifact
- Create a GitHub Release with the APK

You can watch the progress:
- Go to the [Actions tab](https://github.com/Ansh200618/Online-Clipboard/actions)
- Click on the "Build and Release Android APK" workflow
- Wait for the green checkmark ✓

### Step 3: Download the APK

**Option A: From Releases (Recommended)**
1. Go to: https://github.com/Ansh200618/Online-Clipboard/releases
2. Find "Latest Development Build"
3. Click on `app-debug.apk` to download

**Option B: From Actions Artifacts**
1. Go to: https://github.com/Ansh200618/Online-Clipboard/actions
2. Click on the latest successful workflow run
3. Scroll down to "Artifacts"
4. Click "CopyCloud-Debug-APK" to download

### Step 4: Install on Android Device

1. **Transfer APK to your Android device** (via USB, email, cloud storage, etc.)

2. **Enable installation from unknown sources:**
   - Go to Settings → Security → Unknown Sources
   - OR Settings → Apps → Special Access → Install Unknown Apps
   - Enable for your file manager or browser

3. **Install the APK:**
   - Open the APK file
   - Tap "Install"
   - Wait for installation to complete
   - Tap "Open" to launch the app

---

## What You Get

The APK includes:
- ✨ Full Copy Cloud functionality
- 📤 Send text and files
- 📥 Retrieve with 6-character codes
- 📷 QR code scanner for quick retrieval
- 💜 Purple gradient UI (matching website)
- 🔔 Push notifications
- 📋 Local history storage

---

## Technical Details

### Build Process

The build is handled by `.github/workflows/android-build.yml`:
- Runs on every push to main/master branches
- Uses Gradle to build APK directly (no external services needed)
- Takes approximately 5 minutes to complete
- Requires no manual setup or API tokens

### File Location

After GitHub Actions completes:
- **Artifact**: Available in Actions tab for 30 days
- **Release**: Permanently available in Releases section
- **File size**: Approximately 8-12 MB

### Build Configuration

- **Debug Build**: Includes debugging symbols
- **Unsigned**: No signing certificate required for testing
- **Architecture**: Universal APK (works on all Android devices)
- **Min Android**: 5.0 (API 21)
- **Target Android**: 13 (API 33)

---

## Troubleshooting

### Q: I don't see the APK in Releases

**A:** Wait 5 minutes after merging. Check the Actions tab to see if the build is still in progress.

### Q: Installation is blocked on my device

**A:** You need to enable "Install from Unknown Sources" in Android settings (see Step 4 above).

### Q: The build failed

**A:** Check the Actions tab for error logs. Common issues:
- Gradle sync errors (usually auto-resolves on retry)
- Network timeouts (GitHub Actions will retry automatically)

### Q: I want to build it myself locally

**A:** See `BUILD_GUIDE.md` for instructions on building with Android Studio.

---

## No Manual Setup Required! 🎉

Unlike the Expo app approach which requires:
- Creating an Expo account
- Getting an API token
- Adding secrets to GitHub
- Visiting external dashboards

The native Android app build:
- ✅ Works immediately after merge
- ✅ No external accounts needed
- ✅ No API tokens required
- ✅ Everything on GitHub
- ✅ Simple download and install

---

## Questions?

Check the main [README.md](README.md) or open an issue on GitHub.
