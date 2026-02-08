import React from 'react';
import { View } from 'react-native';

export default function GlassPanel({ children, className = '' }) {
  return (
    <View 
      className={`bg-slate-800/60 border border-white/10 rounded-2xl p-5 ${className}`}
      style={{
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.3,
        shadowRadius: 8,
        elevation: 8,
      }}
    >
      {children}
    </View>
  );
}
