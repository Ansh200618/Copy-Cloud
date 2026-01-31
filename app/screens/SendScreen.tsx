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
import * as DocumentPicker from 'expo-document-picker';
import { doc, setDoc } from 'firebase/firestore';
import { signInAnonymously } from 'firebase/auth';
import { db, auth } from '../config/firebase';

interface DocumentAsset {
  name: string;
  size?: number;
  uri: string;
  mimeType?: string;
}

interface ClipboardData {
  type: 'text' | 'file';
  createdAt: string;
  expiresAt: string;
  content?: string;
  fileName?: string;
  fileType?: string;
  fileSize?: number;
}

export default function SendScreen() {
  const [text, setText] = useState('');
  const [file, setFile] = useState<DocumentAsset | null>(null);
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [contentType, setContentType] = useState<'text' | 'file'>('text');

  // Generate a random 6-character code
  const generateCode = () => {
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    let result = '';
    for (let i = 0; i < 6; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  };

  // Pick a file
  const pickFile = async () => {
    try {
      const result = await DocumentPicker.getDocumentAsync({
        type: '*/*',
        copyToCacheDirectory: true,
      });

      if (result.assets && result.assets.length > 0) {
        const asset = result.assets[0];
        if (asset.size && asset.size > 1048576) {
          Alert.alert('Error', 'File size must be under 1MB');
          return;
        }
        setFile(asset);
      }
    } catch (error) {
      Alert.alert('Error', 'Failed to pick file');
    }
  };

  // Upload content to Firebase
  const uploadContent = async () => {
    if (contentType === 'text' && !text.trim()) {
      Alert.alert('Error', 'Please enter some text');
      return;
    }

    if (contentType === 'file' && !file) {
      Alert.alert('Error', 'Please select a file');
      return;
    }

    setLoading(true);

    try {
      // Authenticate anonymously
      await signInAnonymously(auth);

      // Generate code
      const newCode = generateCode();
      
      // Prepare data
      const data: ClipboardData = {
        type: contentType,
        createdAt: new Date().toISOString(),
        expiresAt: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
      };

      if (contentType === 'text') {
        data.content = text;
      } else {
        // For files, we would need to read the file and convert to base64
        // This is simplified - in production, you'd handle file reading properly
        data.fileName = file.name;
        data.fileType = file.mimeType;
        data.fileSize = file.size;
      }

      // Save to Firestore
      const docRef = doc(db, `clipboard_items/${newCode}`);
      await setDoc(docRef, data);

      setCode(newCode);
      Alert.alert('Success', `Your code is: ${newCode}\n\nContent expires in 24 hours.`);
    } catch (error) {
      console.error(error);
      Alert.alert('Error', 'Failed to upload content. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>Send Content</Text>
        <Text style={styles.subtitle}>
          Share text or files with a unique code
        </Text>

        {/* Content Type Selector */}
        <View style={styles.typeSelector}>
          <TouchableOpacity
            style={[
              styles.typeButton,
              contentType === 'text' && styles.typeButtonActive,
            ]}
            onPress={() => setContentType('text')}
          >
            <Text
              style={[
                styles.typeButtonText,
                contentType === 'text' && styles.typeButtonTextActive,
              ]}
            >
              Text
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[
              styles.typeButton,
              contentType === 'file' && styles.typeButtonActive,
            ]}
            onPress={() => setContentType('file')}
          >
            <Text
              style={[
                styles.typeButtonText,
                contentType === 'file' && styles.typeButtonTextActive,
              ]}
            >
              File
            </Text>
          </TouchableOpacity>
        </View>

        {/* Text Input */}
        {contentType === 'text' && (
          <TextInput
            style={styles.textInput}
            placeholder="Enter text to share..."
            value={text}
            onChangeText={setText}
            multiline
            numberOfLines={10}
            textAlignVertical="top"
          />
        )}

        {/* File Picker */}
        {contentType === 'file' && (
          <View style={styles.fileSection}>
            <TouchableOpacity style={styles.fileButton} onPress={pickFile}>
              <Text style={styles.fileButtonText}>
                {file ? '✓ File Selected' : '📁 Choose File'}
              </Text>
            </TouchableOpacity>
            {file && (
              <Text style={styles.fileInfo}>
                {file.name} ({Math.round(file.size / 1024)}KB)
              </Text>
            )}
          </View>
        )}

        {/* Upload Button */}
        <TouchableOpacity
          style={[styles.uploadButton, loading && styles.uploadButtonDisabled]}
          onPress={uploadContent}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.uploadButtonText}>Generate Secure Code</Text>
          )}
        </TouchableOpacity>

        {/* Display Code */}
        {code && (
          <View style={styles.codeContainer}>
            <Text style={styles.codeLabel}>Your Code:</Text>
            <Text style={styles.codeText}>{code}</Text>
            <Text style={styles.codeExpiry}>Expires in 24 hours</Text>
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
  typeSelector: {
    flexDirection: 'row',
    marginBottom: 20,
    backgroundColor: '#e0e0e0',
    borderRadius: 8,
    padding: 4,
  },
  typeButton: {
    flex: 1,
    paddingVertical: 12,
    alignItems: 'center',
    borderRadius: 6,
  },
  typeButtonActive: {
    backgroundColor: '#4F46E5',
  },
  typeButtonText: {
    fontSize: 16,
    color: '#666',
    fontWeight: '600',
  },
  typeButtonTextActive: {
    color: '#fff',
  },
  textInput: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    fontSize: 16,
    minHeight: 200,
    borderWidth: 1,
    borderColor: '#ddd',
    marginBottom: 20,
  },
  fileSection: {
    marginBottom: 20,
  },
  fileButton: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 20,
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#4F46E5',
    borderStyle: 'dashed',
  },
  fileButtonText: {
    fontSize: 16,
    color: '#4F46E5',
    fontWeight: '600',
  },
  fileInfo: {
    marginTop: 12,
    fontSize: 14,
    color: '#666',
    textAlign: 'center',
  },
  uploadButton: {
    backgroundColor: '#4F46E5',
    borderRadius: 8,
    padding: 16,
    alignItems: 'center',
    marginBottom: 20,
  },
  uploadButtonDisabled: {
    opacity: 0.6,
  },
  uploadButtonText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  codeContainer: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 24,
    alignItems: 'center',
    borderWidth: 2,
    borderColor: '#4F46E5',
  },
  codeLabel: {
    fontSize: 16,
    color: '#666',
    marginBottom: 8,
  },
  codeText: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#4F46E5',
    letterSpacing: 4,
    marginBottom: 8,
  },
  codeExpiry: {
    fontSize: 14,
    color: '#999',
  },
});
