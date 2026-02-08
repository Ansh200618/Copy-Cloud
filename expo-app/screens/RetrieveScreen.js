import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, ScrollView, Alert, ActivityIndicator } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import { Search, Copy, Download, FileText } from 'lucide-react-native';
import GlassPanel from '../components/GlassPanel';
import { supabase } from '../lib/supabase';
import * as Clipboard from 'expo-clipboard';

export default function RetrieveScreen() {
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [clipData, setClipData] = useState(null);

  const handleRetrieve = async () => {
    if (!code.trim() || code.length !== 6) {
      Alert.alert('Error', 'Please enter a valid 6-character code');
      return;
    }

    setLoading(true);

    try {
      const { data, error } = await supabase
        .from('clips')
        .select('*')
        .eq('code', code.toUpperCase())
        .single();

      if (error || !data) {
        Alert.alert('Error', 'Code not found or expired');
        return;
      }

      setClipData(data);
    } catch (error) {
      Alert.alert('Error', 'Failed to retrieve content');
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = async (text) => {
    await Clipboard.setStringAsync(text);
    Alert.alert('Success', 'Copied to clipboard!');
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  };

  return (
    <View className="flex-1 bg-slate-900">
      <StatusBar style="light" />
      <ScrollView className="flex-1 px-5 pt-16">
        <View className="mb-6">
          <Text className="text-white text-3xl font-bold">Retrieve Content</Text>
          <Text className="text-slate-400 text-sm mt-1">Enter your 6-character code</Text>
        </View>

        {/* Code Input */}
        <GlassPanel className="mb-6">
          <View className="flex-row">
            <TextInput
              className="flex-1 bg-slate-700/50 border border-white/10 rounded-lg p-4 text-white text-2xl font-mono tracking-widest uppercase"
              placeholder="CODE"
              placeholderTextColor="#64748b"
              value={code}
              onChangeText={(text) => setCode(text.toUpperCase())}
              maxLength={6}
              autoCapitalize="characters"
            />
            <TouchableOpacity 
              className="bg-indigo-600 px-6 rounded-lg ml-3 items-center justify-center"
              onPress={handleRetrieve}
              disabled={loading}
            >
              {loading ? (
                <ActivityIndicator color="#fff" />
              ) : (
                <Search color="#fff" size={24} />
              )}
            </TouchableOpacity>
          </View>
        </GlassPanel>

        {/* Retrieved Content Display */}
        {clipData && (
          <GlassPanel>
            <View className="flex-row justify-between mb-4">
              <View>
                <Text className="text-slate-400 text-xs mb-1">DATA TYPE</Text>
                <Text className="text-white font-semibold capitalize">{clipData.type}</Text>
              </View>
              <View>
                <Text className="text-slate-400 text-xs mb-1">CREATED AT</Text>
                <Text className="text-white font-semibold">{formatDate(clipData.created_at)}</Text>
              </View>
            </View>

            {clipData.type === 'text' ? (
              <>
                <View className="bg-slate-700/50 border border-white/10 rounded-lg p-4 mb-4">
                  <ScrollView className="max-h-96">
                    <Text className="text-white text-base leading-6">{clipData.content}</Text>
                  </ScrollView>
                </View>

                <TouchableOpacity 
                  className="bg-indigo-600 py-4 rounded-xl flex-row items-center justify-center"
                  onPress={() => copyToClipboard(clipData.content)}
                >
                  <Copy color="#fff" size={20} />
                  <Text className="text-white font-bold ml-2">Copy Text</Text>
                </TouchableOpacity>
              </>
            ) : (
              <>
                <View className="bg-slate-700/50 border border-white/10 rounded-lg p-4 mb-4">
                  <View className="flex-row items-center">
                    <FileText color="#6366F1" size={32} />
                    <View className="ml-4">
                      <Text className="text-white font-semibold">File Content</Text>
                      <Text className="text-slate-400 text-sm">Files feature coming soon</Text>
                    </View>
                  </View>
                </View>

                <TouchableOpacity 
                  className="bg-indigo-600 py-4 rounded-xl flex-row items-center justify-center"
                  onPress={() => Alert.alert('Info', 'File download feature coming soon')}
                >
                  <Download color="#fff" size={20} />
                  <Text className="text-white font-bold ml-2">Download</Text>
                </TouchableOpacity>
              </>
            )}
          </GlassPanel>
        )}

        {!clipData && (
          <GlassPanel className="items-center py-12">
            <Search color="#64748b" size={64} />
            <Text className="text-slate-400 mt-4 text-center">
              Enter a code above to retrieve content
            </Text>
          </GlassPanel>
        )}
      </ScrollView>
    </View>
  );
}
