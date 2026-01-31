# Online Clipboard - React Native/Expo Mobile App

A native iOS and Android mobile application for the Online Clipboard service, built with React Native and Expo.

## 📱 Features

- ✅ True native app performance
- ✅ Access to device features (camera, files, clipboard)
- ✅ Publish to App Store & Google Play
- ✅ Shared codebase for iOS & Android
- ✅ Real-time Firebase integration
- ✅ Beautiful native UI components

## 🛠️ Tech Stack

- **React Native** - Cross-platform mobile framework
- **Expo** - Development toolchain and runtime
- **TypeScript** - Type-safe development
- **Firebase** - Backend services (Firestore, Auth)
- **React Navigation** - Native navigation

## 📦 Installation

### Prerequisites

- Node.js (v16 or later)
- npm or yarn
- Expo CLI (`npm install -g expo-cli`)
- iOS Simulator (Mac only) or Android Studio (for Android emulator)

### Setup Steps

1. **Install Dependencies**
   ```bash
   cd app
   npm install
   ```

2. **Configure Firebase**
   - Update Firebase configuration in `config/firebase.ts`
   - Replace the placeholder values with your Firebase project credentials:
     ```typescript
     const firebaseConfig = {
       apiKey: "YOUR_API_KEY",
       authDomain: "YOUR_AUTH_DOMAIN",
       projectId: "YOUR_PROJECT_ID",
       storageBucket: "YOUR_STORAGE_BUCKET",
       messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
       appId: "YOUR_APP_ID"
     };
     ```

3. **Start Development Server**
   ```bash
   npm start
   ```

4. **Run on Device/Simulator**
   - **iOS**: Press `i` in the terminal or run `npm run ios`
   - **Android**: Press `a` in the terminal or run `npm run android`
   - **Web**: Press `w` in the terminal or run `npm run web`
   - **Physical Device**: Scan QR code with Expo Go app

## 📁 Project Structure

```
app/
├── App.tsx                 # Main app entry with navigation
├── screens/
│   ├── SendScreen.tsx      # Upload text/files and generate codes
│   ├── RetrieveScreen.tsx  # Retrieve content using codes
│   └── AboutScreen.tsx     # App information and features
├── components/             # Reusable UI components (empty for now)
├── package.json            # Dependencies and scripts
├── app.json                # Expo configuration
└── README.md               # This file
```

## 🚀 Development

### Available Scripts

- `npm start` - Start the Expo development server
- `npm run android` - Run on Android emulator/device
- `npm run ios` - Run on iOS simulator (Mac only)
- `npm run web` - Run in web browser

### Adding Components

Place reusable components in the `components/` directory:

```
components/
├── Button.tsx
├── Input.tsx
├── Card.tsx
└── index.ts
```

## 📱 Building for Production

### iOS (Mac only)

```bash
expo build:ios
```

### Android

```bash
expo build:android
```

### Using EAS Build (Recommended)

```bash
npm install -g eas-cli
eas build --platform ios
eas build --platform android
```

## 🔧 Configuration

### app.json

Update the following fields in `app.json`:

- `expo.name` - App display name
- `expo.slug` - URL-friendly name
- `expo.version` - App version
- `expo.ios.bundleIdentifier` - iOS bundle ID
- `expo.android.package` - Android package name

### Assets

Add the following assets to an `assets/` directory:

- `icon.png` - App icon (1024x1024)
- `splash.png` - Splash screen image
- `adaptive-icon.png` - Android adaptive icon
- `favicon.png` - Web favicon

## 🔒 Security

- Uses Firebase Anonymous Authentication
- Content expires after 24 hours
- Secure HTTPS connections
- No personal data collection

## 🐛 Troubleshooting

### Common Issues

1. **"Firebase already initialized"**
   - Solution: Ensure Firebase is only initialized once

2. **"Module not found"**
   - Solution: Run `npm install` and restart the server

3. **Android build fails**
   - Solution: Check Android SDK installation and environment variables

4. **iOS build fails**
   - Solution: Update CocoaPods with `cd ios && pod install`

## 📚 Learn More

- [Expo Documentation](https://docs.expo.dev/)
- [React Native Documentation](https://reactnative.dev/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [React Navigation](https://reactnavigation.org/)

## 👨‍💻 Developer

**Anshdeep Singh**

- [Portfolio](https://ansh200618.github.io/Portfolio/)
- [LinkedIn](https://www.linkedin.com/in/anshdeep-singh-editor)

## 📄 License

MIT License - See main repository LICENSE file

---

Made with ❤️ by Anshdeep Singh
