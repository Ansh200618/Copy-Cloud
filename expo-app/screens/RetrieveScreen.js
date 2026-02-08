import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, ScrollView, Alert, ActivityIndicator } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import { Search, Copy, Download, FileText, QrCode, X } from 'lucide-react-native';
import { Camera } from 'expo-camera';
import { BarCodeScanner } from 'expo-barcode-scanner';
import GlassPanel from '../components/GlassPanel';
import { supabase } from '../lib/supabase';
import * as Clipboard from 'expo-clipboard';

export default function RetrieveScreen() {
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [clipData, setClipData] = useState(null);
  const [showScanner, setShowScanner] = useState(false);
  const [hasPermission, setHasPermission] = useState(null);
  const [scanned, setScanned] = useState(false);

  useEffect(() => {
    (async () => {
      const { status } = await Camera.requestCameraPermissionsAsync();
      setHasPermission(status === 'granted');
    })();
  }, []);

  const handleBarCodeScanned = ({ type, data }) => {
    setScanned(true);
    setShowScanner(false);
    
    // Extract code from QR data (it might be a URL or just the code)
    const extractedCode = data.includes('copycloud') ? data.split('/').pop() : data;
    setCode(extractedCode.toUpperCase());
    
    // Auto-retrieve after scanning
    setTimeout(() => {
      handleRetrieve(extractedCode);
    }, 300);
  };

  const openScanner = async () => {
    if (hasPermission === null) {
      const { status } = await Camera.requestCameraPermissionsAsync();
      setHasPermission(status === 'granted');
    }
    
    if (hasPermission === false) {
      Alert.alert('Permission Denied', 'Camera permission is required to scan QR codes');
      return;
    }

    setScanned(false);
    setShowScanner(true);
  };

  const handleRetrieve = async (codeToRetrieve = null) => {
    const retrieveCode = codeToRetrieve || code;
    
    if (!retrieveCode.trim() || retrieveCode.length !== 6) {
      Alert.alert('Error', 'Please enter a valid 6-character code');
      return;
    }

    setLoading(true);

    try {
      const { data, error } = await supabase
        .from('clips')
        .select('*')
        .eq('code', retrieveCode.toUpperCase())
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

  if (showScanner) {
    return (
      <View className="flex-1 bg-slate-900">
        <StatusBar style="light" />
        <Camera
          style={{ flex: 1 }}
          onBarCodeScanned={scanned ? undefined : handleBarCodeScanned}
          barCodeScannerSettings={{
            barCodeTypes: [BarCodeScanner.Constants.BarCodeType.qr],
          }}
        >
          <View className="flex-1 justify-between p-6">
            {/* Header */}
            <View className="flex-row justify-between items-center">
              <Text className="text-white text-xl font-bold">Scan QR Code</Text>
              <TouchableOpacity 
                onPress={() => setShowScanner(false)}
                className="bg-red-600 p-3 rounded-full"
              >
                <X color="#fff" size={24} />
              </TouchableOpacity>
            </View>

            {/* Scanner Frame */}
            <View className="items-center">
              <View className="w-64 h-64 border-4 border-white rounded-2xl opacity-50" />
              <Text className="text-white text-center mt-6 text-base">
                Point camera at QR code
              </Text>
            </View>

            <View className="h-20" />
          </View>
        </Camera>
      </View>
    );
  }

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
              onPress={() => handleRetrieve()}
              disabled={loading}
            >
              {loading ? (
                <ActivityIndicator color="#fff" />
              ) : (
                <Search color="#fff" size={24} />
              )}
            </TouchableOpacity>
          </View>

          {/* QR Scan Button */}
          <TouchableOpacity 
            className="bg-purple-600 py-3 rounded-lg mt-3 flex-row items-center justify-center"
            onPress={openScanner}
          >
            <QrCode color="#fff" size={20} />
            <Text className="text-white font-semibold ml-2">Scan QR Code</Text>
          </TouchableOpacity>
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
              Enter a code above or scan a QR code to retrieve content
            </Text>
          </GlassPanel>
        )}
      </ScrollView>
    </View>
  );
}
