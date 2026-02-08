import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, ScrollView, Alert, ActivityIndicator } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import * as DocumentPicker from 'expo-document-picker';
import { Upload, FileText, CheckCircle } from 'lucide-react-native';
import GlassPanel from '../components/GlassPanel';
import { supabase, generateCode } from '../lib/supabase';

export default function SendScreen() {
  const [inputType, setInputType] = useState('text'); // 'text' or 'file'
  const [textContent, setTextContent] = useState('');
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [generatedCode, setGeneratedCode] = useState('');
  const [showSuccess, setShowSuccess] = useState(false);

  const pickFiles = async () => {
    try {
      const result = await DocumentPicker.getDocumentAsync({
        type: '*/*',
        multiple: true,
        copyToCacheDirectory: true,
      });

      if (!result.canceled && result.assets) {
        setSelectedFiles(result.assets);
      }
    } catch (error) {
      Alert.alert('Error', 'Failed to pick files');
    }
  };

  const handleSend = async () => {
    if (inputType === 'text' && !textContent.trim()) {
      Alert.alert('Error', 'Please enter some text');
      return;
    }

    if (inputType === 'file' && selectedFiles.length === 0) {
      Alert.alert('Error', 'Please select files');
      return;
    }

    setLoading(true);

    try {
      const code = generateCode();
      
      // For now, only handle text upload (file upload requires Supabase Storage setup)
      const { error } = await supabase
        .from('clips')
        .insert([
          {
            code: code,
            content: textContent,
            type: 'text',
            created_at: new Date().toISOString(),
          },
        ]);

      if (error) throw error;

      setGeneratedCode(code);
      setShowSuccess(true);
    } catch (error) {
      Alert.alert('Error', error.message || 'Failed to upload content');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setTextContent('');
    setSelectedFiles([]);
    setGeneratedCode('');
    setShowSuccess(false);
  };

  if (showSuccess) {
    return (
      <View className="flex-1 bg-slate-900">
        <StatusBar style="light" />
        <ScrollView className="flex-1 px-5 pt-16">
          <GlassPanel className="items-center">
            <CheckCircle color="#10B981" size={64} />
            <Text className="text-white text-2xl font-bold mt-4">Upload Complete!</Text>
            <Text className="text-slate-400 text-sm mt-2">Your content is securely stored</Text>
            
            <View className="bg-indigo-600 rounded-xl px-8 py-6 mt-8">
              <Text className="text-white/70 text-sm mb-2">Your Code:</Text>
              <Text className="text-white text-4xl font-bold tracking-widest">{generatedCode}</Text>
            </View>

            <Text className="text-slate-500 text-xs mt-4">Expires in 24 hours</Text>

            <View className="flex-row gap-3 mt-8 w-full">
              <TouchableOpacity 
                className="flex-1 bg-indigo-600 py-4 rounded-xl"
                onPress={() => {
                  // Copy to clipboard logic would go here
                  Alert.alert('Copied!', `Code ${generatedCode} copied to clipboard`);
                }}
              >
                <Text className="text-white text-center font-semibold">Copy Code</Text>
              </TouchableOpacity>

              <TouchableOpacity 
                className="flex-1 bg-slate-700 py-4 rounded-xl"
                onPress={resetForm}
              >
                <Text className="text-white text-center font-semibold">Start New</Text>
              </TouchableOpacity>
            </View>
          </GlassPanel>
        </ScrollView>
      </View>
    );
  }

  return (
    <View className="flex-1 bg-slate-900">
      <StatusBar style="light" />
      <ScrollView className="flex-1 px-5 pt-16">
        <View className="mb-6">
          <Text className="text-white text-3xl font-bold">Send Content</Text>
          <Text className="text-slate-400 text-sm mt-1">Auto-deletes in 24h</Text>
        </View>

        {/* Mode Toggle */}
        <GlassPanel className="flex-row mb-6">
          <TouchableOpacity 
            className={`flex-1 py-3 rounded-lg ${inputType === 'text' ? 'bg-indigo-600' : 'bg-slate-700'}`}
            onPress={() => setInputType('text')}
          >
            <Text className="text-white text-center font-semibold">Text</Text>
          </TouchableOpacity>
          
          <TouchableOpacity 
            className={`flex-1 py-3 rounded-lg ml-2 ${inputType === 'file' ? 'bg-indigo-600' : 'bg-slate-700'}`}
            onPress={() => setInputType('file')}
          >
            <Text className="text-white text-center font-semibold">Files</Text>
          </TouchableOpacity>
        </GlassPanel>

        {/* Text Input */}
        {inputType === 'text' && (
          <GlassPanel className="mb-6">
            <Text className="text-white text-lg font-bold mb-3">Enter Text</Text>
            <TextInput
              className="bg-slate-700/50 border border-white/10 rounded-lg p-4 text-white min-h-[200px]"
              placeholder="Type or paste your content here..."
              placeholderTextColor="#64748b"
              multiline
              textAlignVertical="top"
              value={textContent}
              onChangeText={setTextContent}
            />
          </GlassPanel>
        )}

        {/* File Input */}
        {inputType === 'file' && (
          <GlassPanel className="mb-6">
            <Text className="text-white text-lg font-bold mb-3">Select Files</Text>
            <TouchableOpacity 
              className="bg-slate-700/50 border-2 border-dashed border-white/20 rounded-lg py-16 items-center"
              onPress={pickFiles}
            >
              <Upload color="#6366F1" size={48} />
              <Text className="text-slate-400 mt-4">Click to upload files</Text>
              <Text className="text-slate-500 text-xs mt-1">Max 40MB Total</Text>
            </TouchableOpacity>

            {selectedFiles.length > 0 && (
              <View className="mt-4">
                <Text className="text-slate-400 text-sm mb-2">
                  {selectedFiles.length} file(s) selected
                </Text>
                {selectedFiles.map((file, index) => (
                  <View key={index} className="flex-row items-center bg-slate-700/30 p-3 rounded-lg mb-2">
                    <FileText color="#6366F1" size={20} />
                    <Text className="text-white ml-3 flex-1" numberOfLines={1}>
                      {file.name}
                    </Text>
                  </View>
                ))}
              </View>
            )}
          </GlassPanel>
        )}

        {/* Send Button */}
        <TouchableOpacity 
          className="bg-gradient-to-r from-indigo-600 to-purple-600 py-4 rounded-xl mb-8"
          style={{
            backgroundColor: '#6366F1',
          }}
          onPress={handleSend}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <View className="flex-row items-center justify-center">
              <Upload color="#fff" size={20} />
              <Text className="text-white text-center font-bold ml-2">Generate Secure Code</Text>
            </View>
          )}
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
}
