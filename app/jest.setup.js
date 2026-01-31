import '@testing-library/jest-native/extend-expect';

// Polyfill for global object
if (typeof global !== 'undefined') {
  global.self = global;
}

// Mock Firebase
jest.mock('./config/firebase', () => ({
  db: {},
  auth: {},
  app: {},
}));

// Mock expo-constants
jest.mock('expo-constants', () => ({
  default: {
    expoConfig: {
      extra: {
        FIREBASE_API_KEY: 'test-api-key',
        FIREBASE_AUTH_DOMAIN: 'test-auth-domain',
        FIREBASE_PROJECT_ID: 'test-project-id',
        FIREBASE_STORAGE_BUCKET: 'test-storage-bucket',
        FIREBASE_MESSAGING_SENDER_ID: 'test-sender-id',
        FIREBASE_APP_ID: 'test-app-id',
      },
    },
  },
}));

// Mock react-native-safe-area-context
jest.mock('react-native-safe-area-context', () => ({
  SafeAreaProvider: ({ children }) => children,
  SafeAreaView: ({ children }) => children,
  useSafeAreaInsets: () => ({ top: 0, right: 0, bottom: 0, left: 0 }),
}));

// Silence the warning: Animated: `useNativeDriver` is not supported
jest.mock('react-native/Libraries/Animated/NativeAnimatedHelper');
