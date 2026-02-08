import React from 'react';
import { View, Text, TouchableOpacity, ScrollView, Modal, Linking } from 'react-native';
import { X, Lock, Clock, Key, QrCode, BarChart3, Type, Moon, Upload } from 'lucide-react-native';
import { BlurView } from 'expo-blur';
import GlassPanel from './GlassPanel';

export default function ComingSoonModal({ visible, onClose }) {
  const features = [
    {
      icon: Lock,
      title: 'End-to-End Encryption',
      description: 'Extra security layer for sensitive data',
    },
    {
      icon: Clock,
      title: 'Custom Expiration Times',
      description: 'Choose 1h, 12h, 7d, or custom duration',
    },
    {
      icon: Key,
      title: 'Password Protection',
      description: 'Secure content with a password',
    },
    {
      icon: QrCode,
      title: 'QR Code Sharing',
      description: 'Easy code sharing with QR codes',
    },
    {
      icon: BarChart3,
      title: 'Usage Statistics',
      description: 'Track your sharing activity',
    },
    {
      icon: Type,
      title: 'Rich Text Editor',
      description: 'Format text with styles and colors',
    },
    {
      icon: Moon,
      title: 'Dark/Light Theme Toggle',
      description: 'Switch between themes',
    },
    {
      icon: Upload,
      title: 'Enhanced Multi-File Upload',
      description: 'Upload multiple files seamlessly',
    },
  ];

  return (
    <Modal
      visible={visible}
      transparent={true}
      animationType="fade"
      onRequestClose={onClose}
    >
      <BlurView intensity={80} style={{ flex: 1 }} tint="dark">
        <View className="flex-1 items-center justify-center p-4">
          <GlassPanel className="w-full max-w-2xl max-h-[85vh]">
            {/* Header */}
            <View className="flex-row items-center justify-between mb-6 pb-4 border-b border-purple-500/20">
              <View className="flex-row items-center">
                <Text className="text-purple-400 text-xl mr-2">✨</Text>
                <Text className="text-white text-xl font-bold">Coming Soon Features</Text>
              </View>
              <TouchableOpacity onPress={onClose}>
                <X color="#fff" size={24} />
              </TouchableOpacity>
            </View>

            <ScrollView className="mb-6" showsVerticalScrollIndicator={false}>
              <Text className="text-slate-300 text-base leading-6 mb-6">
                We're constantly working to improve your experience. Here's what we're building next!
              </Text>

              <View className="space-y-4">
                {features.map((feature, index) => {
                  const IconComponent = feature.icon;
                  return (
                    <View 
                      key={index} 
                      className="bg-white/5 rounded-xl p-4 border border-white/5"
                    >
                      <View className="flex-row items-start">
                        <View className="bg-purple-500/20 p-2 rounded-lg mr-4">
                          <IconComponent color="#A855F7" size={24} />
                        </View>
                        <View className="flex-1">
                          <Text className="text-white font-semibold text-base mb-1">
                            {feature.title}
                          </Text>
                          <Text className="text-slate-400 text-sm leading-5">
                            {feature.description}
                          </Text>
                        </View>
                      </View>
                    </View>
                  );
                })}
              </View>

              <View className="mt-6 p-4 bg-purple-500/10 border border-purple-500/20 rounded-xl">
                <Text className="text-slate-300 text-sm leading-6">
                  Want to see a specific feature?{' '}
                  <Text 
                    className="text-purple-400 underline"
                    onPress={() => Linking.openURL('https://github.com/Ansh200618/Online-Clipboard/issues')}
                  >
                    Let us know on GitHub!
                  </Text>
                </Text>
              </View>
            </ScrollView>

            <TouchableOpacity 
              className="bg-purple-600 py-4 rounded-xl"
              onPress={onClose}
            >
              <Text className="text-white text-center font-bold text-base">Awesome!</Text>
            </TouchableOpacity>
          </GlassPanel>
        </View>
      </BlurView>
    </Modal>
  );
}
