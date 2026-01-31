import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { StatusBar } from 'expo-status-bar';
import SendScreen from './screens/SendScreen';
import RetrieveScreen from './screens/RetrieveScreen';
import AboutScreen from './screens/AboutScreen';
import { AuthProvider, ClipboardProvider } from './context';

const Tab = createBottomTabNavigator();

export default function App() {
  return (
    <AuthProvider>
      <ClipboardProvider>
        <NavigationContainer>
          <StatusBar style="auto" />
          <Tab.Navigator
            screenOptions={{
              headerStyle: {
                backgroundColor: '#4F46E5',
              },
              headerTintColor: '#fff',
              headerTitleStyle: {
                fontWeight: 'bold',
              },
              tabBarActiveTintColor: '#4F46E5',
              tabBarInactiveTintColor: 'gray',
            }}
          >
            <Tab.Screen 
              name="Send" 
              component={SendScreen}
              options={{
                title: 'Send Content',
                tabBarLabel: 'Send',
              }}
            />
            <Tab.Screen 
              name="Retrieve" 
              component={RetrieveScreen}
              options={{
                title: 'Retrieve Content',
                tabBarLabel: 'Retrieve',
              }}
            />
            <Tab.Screen 
              name="About" 
              component={AboutScreen}
              options={{
                title: 'About',
                tabBarLabel: 'About',
              }}
            />
          </Tab.Navigator>
        </NavigationContainer>
      </ClipboardProvider>
    </AuthProvider>
  );
}
