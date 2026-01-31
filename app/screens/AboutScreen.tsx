import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Linking,
} from 'react-native';

export default function AboutScreen() {
  const openLink = (url: string) => {
    Linking.openURL(url);
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>📋 Online Clipboard</Text>
        <Text style={styles.version}>Version 1.0.0</Text>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>🌟 Features</Text>
          <View style={styles.featureList}>
            <Text style={styles.feature}>🔐 No Login Required - Start sharing instantly</Text>
            <Text style={styles.feature}>⚡ Real-time Transfer - Powered by Firebase</Text>
            <Text style={styles.feature}>📱 Cross-Device Sharing - iOS, Android, Web</Text>
            <Text style={styles.feature}>📝 Text & Files - Support for various content types</Text>
            <Text style={styles.feature}>🔒 Auto-Expiry - Content deletes after 24 hours</Text>
            <Text style={styles.feature}>🎨 Modern UI - Beautiful native mobile experience</Text>
          </View>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>🚀 How It Works</Text>
          <View style={styles.stepContainer}>
            <View style={styles.step}>
              <Text style={styles.stepNumber}>1</Text>
              <View style={styles.stepContent}>
                <Text style={styles.stepTitle}>Upload Content</Text>
                <Text style={styles.stepDescription}>
                  Navigate to the Send tab and choose between text or file input
                </Text>
              </View>
            </View>

            <View style={styles.step}>
              <Text style={styles.stepNumber}>2</Text>
              <View style={styles.stepContent}>
                <Text style={styles.stepTitle}>Get Unique Code</Text>
                <Text style={styles.stepDescription}>
                  Click "Generate Secure Code" to receive a random 6-character code
                </Text>
              </View>
            </View>

            <View style={styles.step}>
              <Text style={styles.stepNumber}>3</Text>
              <View style={styles.stepContent}>
                <Text style={styles.stepTitle}>Retrieve Anywhere</Text>
                <Text style={styles.stepDescription}>
                  Go to the Retrieve tab on any device and enter the code
                </Text>
              </View>
            </View>
          </View>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>📊 Usage Limits</Text>
          <View style={styles.limitsList}>
            <View style={styles.limitRow}>
              <Text style={styles.limitLabel}>Text Length:</Text>
              <Text style={styles.limitValue}>Unlimited</Text>
            </View>
            <View style={styles.limitRow}>
              <Text style={styles.limitLabel}>File Size:</Text>
              <Text style={styles.limitValue}>Max 1MB</Text>
            </View>
            <View style={styles.limitRow}>
              <Text style={styles.limitLabel}>Expiration:</Text>
              <Text style={styles.limitValue}>24 hours</Text>
            </View>
            <View style={styles.limitRow}>
              <Text style={styles.limitLabel}>Authentication:</Text>
              <Text style={styles.limitValue}>Anonymous</Text>
            </View>
          </View>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>🔒 Security & Privacy</Text>
          <Text style={styles.securityText}>
            • Anonymous Authentication - No personal data collected{'\n'}
            • Temporary Storage - All data expires after 24 hours{'\n'}
            • Secure Firebase - Industry-standard cloud infrastructure{'\n'}
            • No Tracking - Your privacy is our priority
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>🛠️ Technology Stack</Text>
          <Text style={styles.techText}>
            • React Native & Expo - Cross-platform mobile framework{'\n'}
            • Firebase Firestore - Real-time NoSQL database{'\n'}
            • Firebase Auth - Anonymous authentication{'\n'}
            • TypeScript - Type-safe development
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>👨‍💻 Developer</Text>
          <Text style={styles.developerName}>Anshdeep Singh</Text>
          <View style={styles.linkContainer}>
            <TouchableOpacity
              style={styles.linkButton}
              onPress={() => openLink('http://ansh200618.github.io/Portfolio/')}
            >
              <Text style={styles.linkButtonText}>🌐 Portfolio</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.linkButton}
              onPress={() => openLink('https://www.linkedin.com/in/anshdeep-singh-editor')}
            >
              <Text style={styles.linkButtonText}>💼 LinkedIn</Text>
            </TouchableOpacity>
          </View>
        </View>

        <View style={styles.footer}>
          <Text style={styles.footerText}>Made with ❤️ by Anshdeep Singh</Text>
          <Text style={styles.copyright}>© 2024 Online Clipboard</Text>
        </View>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  content: {
    padding: 20,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#333',
    textAlign: 'center',
    marginBottom: 8,
  },
  version: {
    fontSize: 16,
    color: '#666',
    textAlign: 'center',
    marginBottom: 24,
  },
  section: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 20,
    marginBottom: 16,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 16,
  },
  featureList: {
    gap: 12,
  },
  feature: {
    fontSize: 16,
    color: '#555',
    lineHeight: 24,
    marginBottom: 8,
  },
  stepContainer: {
    gap: 16,
  },
  step: {
    flexDirection: 'row',
    gap: 16,
    marginBottom: 16,
  },
  stepNumber: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: '#4F46E5',
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
    lineHeight: 32,
  },
  stepContent: {
    flex: 1,
  },
  stepTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 4,
  },
  stepDescription: {
    fontSize: 14,
    color: '#666',
    lineHeight: 20,
  },
  limitsList: {
    gap: 12,
  },
  limitRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  limitLabel: {
    fontSize: 16,
    color: '#555',
  },
  limitValue: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#4F46E5',
  },
  securityText: {
    fontSize: 15,
    color: '#555',
    lineHeight: 24,
  },
  techText: {
    fontSize: 15,
    color: '#555',
    lineHeight: 24,
  },
  developerName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 16,
    textAlign: 'center',
  },
  linkContainer: {
    flexDirection: 'row',
    gap: 12,
    justifyContent: 'center',
  },
  linkButton: {
    backgroundColor: '#4F46E5',
    borderRadius: 8,
    paddingVertical: 12,
    paddingHorizontal: 20,
  },
  linkButtonText: {
    color: '#fff',
    fontSize: 14,
    fontWeight: 'bold',
  },
  footer: {
    marginTop: 24,
    marginBottom: 40,
    alignItems: 'center',
  },
  footerText: {
    fontSize: 16,
    color: '#666',
    marginBottom: 8,
  },
  copyright: {
    fontSize: 14,
    color: '#999',
  },
});
