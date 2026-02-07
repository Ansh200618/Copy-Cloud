package com.copycloud.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.security.MessageDigest;
import java.util.Random;

public class DeviceManager {
    
    private static final String PREFS_NAME = "CopyCloudPrefs";
    private static final String KEY_DEVICE_CODE = "device_code";
    
    /**
     * Get or generate 8-digit device code
     */
    public static String getDeviceCode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String code = prefs.getString(KEY_DEVICE_CODE, null);
        
        if (code == null) {
            code = generateDeviceCode(context);
            prefs.edit().putString(KEY_DEVICE_CODE, code).apply();
        }
        
        return code;
    }
    
    /**
     * Generate unique 8-digit code based on Android ID
     */
    private static String generateDeviceCode(Context context) {
        try {
            // Get Android device ID
            String androidId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
            );
            
            // Hash it to get consistent but unique code
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(androidId.getBytes());
            
            // Convert to 8-digit number
            long hash = 0;
            for (int i = 0; i < 8; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }
            
            // Ensure it's 8 digits (10000000 to 99999999)
            long code = Math.abs(hash % 90000000) + 10000000;
            return String.valueOf(code);
            
        } catch (Exception e) {
            // Fallback: generate random 8-digit code
            Random random = new Random();
            int code = 10000000 + random.nextInt(90000000);
            return String.valueOf(code);
        }
    }
    
    /**
     * Format device code for display (e.g., 1234-5678)
     */
    public static String formatDeviceCode(String code) {
        if (code.length() == 8) {
            return code.substring(0, 4) + "-" + code.substring(4);
        }
        return code;
    }
}
