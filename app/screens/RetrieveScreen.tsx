import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  ScrollView,
  ActivityIndicator,
} from 'react-native';
import * as Clipboard from 'expo-clipboard';
import { doc, getDoc } from 'firebase/firestore';
import { signInAnonymously } from 'firebase/auth';
import { db, auth } from '../config/firebase';

interface ClipboardData {
  type: 'text' | 'file';
  content?: string;
  fileName?: string;
  fileType?: string;
  fileSize?: number;
  createdAt: string;
  expiresAt: string;
}

export default function RetrieveScreen() {
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<ClipboardData | null>(null);

  // Retrieve content from Firebase
  const retrieveContent = async () => {
    if (!code.trim() || code.length !== 6) {
      Alert.alert('Error', 'Please enter a valid 6-character code');
      return;
    }

    setLoading(true);
    setData(null);

    try {
      // Authenticate anonymously
      await signInAnonymously(auth);

      // Get document from Firestore
      const docRef = doc(db, `clipboard_items/${code.toUpperCase()}`);
      const docSnap = await getDoc(docRef);

      if (!docSnap.exists()) {
        Alert.alert('Error', 'Code not found or expired');
        return;
      }

      const clipboardData = docSnap.data() as ClipboardData;

      // Check if expired
      if (new Date(clipboardData.expiresAt) < new Date()) {
        Alert.alert('Error', 'This code has expired');
        return;
      }

      setData(clipboardData);
    } catch (error) {
      console.error(error);
      Alert.alert('Error', 'Failed to retrieve content. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Copy text to clipboard
  const copyToClipboard = async () => {
    if (data?.content) {
      await Clipboard.setStringAsync(data.content);
      Alert.alert('Success', 'Text copied to clipboard!');
    }
  };

  // Calculate time remaining
  const getTimeRemaining = () => {
    if (!data) return '';
    const now = new Date();
    const expires = new Date(data.expiresAt);
    const diff = expires.getTime() - now.getTime();
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m remaining`;
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>Retrieve Content</Text>
        <Text style={styles.subtitle}>
          Enter your 6-character code to access shared content
        </Text>

        {/* Code Input */}
        <TextInput
          style={styles.codeInput}
          placeholder="Enter code (e.g., A3K9Z7)"
          value={code}
          onChangeText={(text) => setCode(text.toUpperCase())}
          maxLength={6}
          autoCapitalize="characters"
          autoCorrect={false}
        />

        {/* Retrieve Button */}
        <TouchableOpacity
          style={[styles.retrieveButton, loading && styles.retrieveButtonDisabled]}
          onPress={retrieveContent}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.retrieveButtonText}>Retrieve Content</Text>
          )}
        </TouchableOpacity>

        {/* Display Retrieved Content */}
        {data && (
          <View style={styles.resultContainer}>
            <View style={styles.resultHeader}>
              <Text style={styles.resultType}>
                {data.type === 'text' ? '📝 Text' : '📁 File'}
              </Text>
              <Text style={styles.resultExpiry}>{getTimeRemaining()}</Text>
            </View>

            {data.type === 'text' ? (
              <View>
                <ScrollView style={styles.textContent}>
                  <Text style={styles.textContentText}>{data.content}</Text>
                </ScrollView>
                <TouchableOpacity
                  style={styles.copyButton}
                  onPress={copyToClipboard}
                >
                  <Text style={styles.copyButtonText}>📋 Copy to Clipboard</Text>
                </TouchableOpacity>
              </View>
            ) : (
              <View style={styles.fileInfo}>
                <Text style={styles.fileName}>{data.fileName}</Text>
                <Text style={styles.fileDetails}>
                  Type: {data.fileType}
                </Text>
                <Text style={styles.fileDetails}>
                  Size: {data.fileSize ? Math.round(data.fileSize / 1024) : 0}KB
                </Text>
                <TouchableOpacity style={styles.downloadButton}>
                  <Text style={styles.downloadButtonText}>
                    ⬇️ Download File
                  </Text>
                </TouchableOpacity>
              </View>
            )}

            <Text style={styles.resultFooter}>
              Created: {new Date(data.createdAt).toLocaleString()}
            </Text>
          </View>
        )}
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
    fontSize: 28,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: '#666',
    marginBottom: 24,
  },
  codeInput: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    fontSize: 20,
    borderWidth: 1,
    borderColor: '#ddd',
    marginBottom: 16,
    textAlign: 'center',
    letterSpacing: 4,
    fontWeight: 'bold',
  },
  retrieveButton: {
    backgroundColor: '#4F46E5',
    borderRadius: 8,
    padding: 16,
    alignItems: 'center',
    marginBottom: 24,
  },
  retrieveButtonDisabled: {
    opacity: 0.6,
  },
  retrieveButtonText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  resultContainer: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 20,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  resultHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
    paddingBottom: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  resultType: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#4F46E5',
  },
  resultExpiry: {
    fontSize: 14,
    color: '#999',
  },
  textContent: {
    maxHeight: 300,
    marginBottom: 16,
  },
  textContentText: {
    fontSize: 16,
    color: '#333',
    lineHeight: 24,
  },
  copyButton: {
    backgroundColor: '#10B981',
    borderRadius: 8,
    padding: 14,
    alignItems: 'center',
  },
  copyButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  fileInfo: {
    alignItems: 'center',
    paddingVertical: 20,
  },
  fileName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 12,
    textAlign: 'center',
  },
  fileDetails: {
    fontSize: 14,
    color: '#666',
    marginBottom: 8,
  },
  downloadButton: {
    backgroundColor: '#10B981',
    borderRadius: 8,
    padding: 14,
    alignItems: 'center',
    marginTop: 16,
    minWidth: 200,
  },
  downloadButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  resultFooter: {
    fontSize: 12,
    color: '#999',
    marginTop: 16,
    paddingTop: 16,
    borderTopWidth: 1,
    borderTopColor: '#eee',
    textAlign: 'center',
  },
});
