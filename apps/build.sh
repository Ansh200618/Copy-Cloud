#!/bin/bash
# Build script for Copy Cloud Android APK

echo "================================================"
echo "Copy Cloud - Android APK Build Script"
echo "================================================"
echo ""

# Check if running from apps directory
if [ ! -f "settings.gradle" ]; then
    echo "❌ Error: Please run this script from the 'apps' directory"
    echo "Usage: cd apps && ./build.sh"
    exit 1
fi

# Check for required tools
echo "🔍 Checking build requirements..."

if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install JDK 11 or later."
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

# Set Android SDK path if not set
if [ -z "$ANDROID_HOME" ]; then
    echo "⚠️  ANDROID_HOME not set."
    echo "   Please set ANDROID_HOME to your Android SDK path:"
    echo "   export ANDROID_HOME=/path/to/android/sdk"
    echo ""
    echo "   Common paths:"
    echo "   - macOS: ~/Library/Android/sdk"
    echo "   - Linux: ~/Android/Sdk"
    echo "   - Windows: C:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk"
    echo ""
    read -p "Enter Android SDK path (or press Enter to skip): " sdk_path
    if [ -n "$sdk_path" ]; then
        export ANDROID_HOME="$sdk_path"
        echo "✅ ANDROID_HOME set to: $ANDROID_HOME"
    fi
fi

# Initialize Gradle wrapper if not exists
if [ ! -f "gradlew" ]; then
    echo ""
    echo "📦 Gradle wrapper not found. Initializing..."
    gradle wrapper --gradle-version 8.2
    chmod +x gradlew
fi

# Clean previous builds
echo ""
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo ""
echo "🔨 Building debug APK..."
./gradlew assembleDebug

# Check if build succeeded
if [ $? -eq 0 ]; then
    echo ""
    echo "================================================"
    echo "✅ BUILD SUCCESSFUL!"
    echo "================================================"
    echo ""
    echo "📦 APK Location:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    
    # Get APK size
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        size=$(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)
        echo "📊 APK Size: $size"
        echo ""
    fi
    
    echo "📱 To install on connected device:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "Or transfer the APK to your device and install manually."
    echo ""
else
    echo ""
    echo "================================================"
    echo "❌ BUILD FAILED"
    echo "================================================"
    echo ""
    echo "Please check the error messages above."
    echo ""
    echo "Common solutions:"
    echo "1. Ensure ANDROID_HOME is set correctly"
    echo "2. Check internet connection for downloading dependencies"
    echo "3. Update Android SDK and build tools"
    echo "4. Run: ./gradlew build --stacktrace for detailed error"
    echo ""
    exit 1
fi
