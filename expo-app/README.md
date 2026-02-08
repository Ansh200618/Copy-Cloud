# Copy Cloud - React Native Expo App (Optional)

> **Note**: This is an experimental React Native version. The **recommended approach** is to use the native Android app in the `apps/` directory, which builds automatically without any setup.

This is the React Native Expo version of Copy Cloud that replicates the HTML interface.

## Features

- 📤 **Send Content**: Upload text or files and get a 6-character code
- 📥 **Retrieve Content**: Enter a code to fetch shared content
- 🎨 **Purple Gradient UI**: Matches the website's design with glassmorphism effects
- 🔒 **Secure**: Uses Supabase for backend storage
- ⏰ **Auto-Delete**: Content expires after 24 hours

## Prerequisites

1. **Expo Account**: Sign up at [expo.dev](https://expo.dev)
2. **Expo CLI**: Install globally with `npm install -g expo-cli eas-cli`
3. **Node.js**: Version 18.x or higher

## Local Development

```bash
# Install dependencies
cd expo-app
npm install

# Start the development server
npm start

# Run on Android
npm run android

# Run on iOS
npm run ios
```

## Automated APK Build Setup

To enable automatic APK builds via GitHub Actions:

### Step 1: Get Expo Token

1. Go to [expo.dev](https://expo.dev) and log in
2. Navigate to **User Settings** → **Access Tokens**
3. Click **Create Token**
4. Copy the generated token

### Step 2: Add Token to GitHub

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `EXPO_TOKEN`
5. Value: (Paste your Expo token)

### Step 3: Push to Main Branch

When you push code to the `main` branch that affects the `expo-app/` directory, GitHub Actions will automatically:

1. Install dependencies
2. Build the Android APK using EAS Build
3. Upload the APK to your Expo dashboard

You can download the built APK from:
- Expo Dashboard: [https://expo.dev/accounts/YOUR_USERNAME/projects/copy-cloud/builds](https://expo.dev/accounts/YOUR_USERNAME/projects/copy-cloud/builds)
- Or check the GitHub Actions logs for the build URL

## Manual Build

To build manually without GitHub Actions:

```bash
cd expo-app

# Build APK for Android
eas build --platform android --profile preview

# Build for production
eas build --platform android --profile production
```

## Project Structure

```
expo-app/
├── App.js                 # Main app with navigation
├── screens/
│   ├── SendScreen.js      # Upload text/files
│   └── RetrieveScreen.js  # Fetch content by code
├── components/
│   └── GlassPanel.js      # Reusable glass UI component
├── lib/
│   └── supabase.js        # Supabase client configuration
├── app.json               # Expo configuration
├── eas.json               # EAS Build configuration
├── package.json           # Dependencies
└── tailwind.config.js     # NativeWind/Tailwind config
```

## Supabase Configuration

The app uses the same Supabase instance as the web version:
- URL: `https://luunzeonlmzvmewaucqj.supabase.co`
- The credentials are configured in `lib/supabase.js`

## UI Design

The app matches the HTML version with:
- Dark mode (slate-900 background)
- Purple gradient (indigo to purple)
- Glassmorphism effects
- Consistent typography and spacing
- Lucide icons for consistency

## Build Profiles

Defined in `eas.json`:

- **preview**: Builds APK for testing (default for GitHub Actions)
- **production**: Builds AAB for Play Store submission

## Troubleshooting

### Build fails with "EXPO_TOKEN not found"
- Make sure you added the `EXPO_TOKEN` secret to GitHub

### "eas command not found"
- Install EAS CLI: `npm install -g eas-cli`

### App crashes on launch
- Check that all dependencies are installed
- Ensure Supabase credentials are correct

## License

Same as the main project.
