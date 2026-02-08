import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Upload, Download } from 'lucide-react-native';
import SendScreen from './screens/SendScreen';
import RetrieveScreen from './screens/RetrieveScreen';

const Tab = createBottomTabNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Tab.Navigator
        screenOptions={{
          headerShown: false,
          tabBarStyle: {
            backgroundColor: '#1e293b',
            borderTopColor: 'rgba(255, 255, 255, 0.1)',
            borderTopWidth: 1,
            paddingBottom: 8,
            paddingTop: 8,
            height: 60,
          },
          tabBarActiveTintColor: '#6366F1',
          tabBarInactiveTintColor: '#64748b',
          tabBarLabelStyle: {
            fontSize: 12,
            fontWeight: '600',
          },
        }}
      >
        <Tab.Screen
          name="Send"
          component={SendScreen}
          options={{
            tabBarIcon: ({ color, size }) => (
              <Upload color={color} size={size} />
            ),
          }}
        />
        <Tab.Screen
          name="Retrieve"
          component={RetrieveScreen}
          options={{
            tabBarIcon: ({ color, size }) => (
              <Download color={color} size={size} />
            ),
          }}
        />
      </Tab.Navigator>
    </NavigationContainer>
  );
}
