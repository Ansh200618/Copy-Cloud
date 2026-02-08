package com.copycloud.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ConnectedDevicesManager {
    
    private static final String PREFS_NAME = "CopyCloudPrefs";
    private static final String KEY_CONNECTED_DEVICES = "connected_devices";
    private static final int MAX_CONNECTED_DEVICES = 5;
    
    /**
     * Add a connected device code (max 5 devices)
     */
    public static boolean addConnectedDevice(Context context, String deviceCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> devices = getConnectedDevices(context);
        
        // Check if already connected
        if (devices.contains(deviceCode)) {
            return false; // Already connected
        }
        
        // Check max limit
        if (devices.size() >= MAX_CONNECTED_DEVICES) {
            return false; // Max limit reached
        }
        
        devices.add(deviceCode);
        prefs.edit().putStringSet(KEY_CONNECTED_DEVICES, devices).apply();
        return true;
    }
    
    /**
     * Remove a connected device code
     */
    public static void removeConnectedDevice(Context context, String deviceCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> devices = getConnectedDevices(context);
        devices.remove(deviceCode);
        prefs.edit().putStringSet(KEY_CONNECTED_DEVICES, devices).apply();
    }
    
    /**
     * Get all connected device codes
     */
    public static Set<String> getConnectedDevices(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> devices = prefs.getStringSet(KEY_CONNECTED_DEVICES, null);
        if (devices == null) {
            return new HashSet<>();
        }
        return new HashSet<>(devices); // Return mutable copy
    }
    
    /**
     * Check if a device code is connected
     */
    public static boolean isDeviceConnected(Context context, String deviceCode) {
        return getConnectedDevices(context).contains(deviceCode);
    }
    
    /**
     * Get count of connected devices
     */
    public static int getConnectedDeviceCount(Context context) {
        return getConnectedDevices(context).size();
    }
    
    /**
     * Check if can add more devices
     */
    public static boolean canAddMoreDevices(Context context) {
        return getConnectedDeviceCount(context) < MAX_CONNECTED_DEVICES;
    }
    
    /**
     * Get max device limit
     */
    public static int getMaxDeviceLimit() {
        return MAX_CONNECTED_DEVICES;
    }
    
    /**
     * Clear all connected devices
     */
    public static void clearAllConnectedDevices(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_CONNECTED_DEVICES).apply();
    }
    
    /**
     * Validate device code format (8 digits)
     */
    public static boolean isValidDeviceCode(String code) {
        if (code == null || code.length() != 8) {
            return false;
        }
        try {
            Long.parseLong(code);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
