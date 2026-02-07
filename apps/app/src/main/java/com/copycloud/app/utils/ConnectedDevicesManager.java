package com.copycloud.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ConnectedDevicesManager {
    
    private static final String PREFS_NAME = "CopyCloudPrefs";
    private static final String KEY_CONNECTED_DEVICES = "connected_devices";
    
    /**
     * Add a connected device code
     */
    public static void addConnectedDevice(Context context, String deviceCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> devices = getConnectedDevices(context);
        devices.add(deviceCode);
        prefs.edit().putStringSet(KEY_CONNECTED_DEVICES, devices).apply();
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
