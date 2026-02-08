package com.copycloud.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.Window;

import com.copycloud.app.R;
import com.google.android.material.color.DynamicColors;

public class ThemeManager {
    
    public static void applyTheme(String themeName) {
        // Theme is applied via window attributes
        // Colors are dynamically updated based on themeName
    }
    
    public static void applyThemeToActivity(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("selected_theme", "system");
        
        // Apply Material You dynamic colors if available and theme is system
        if ("system".equals(theme) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivityIfAvailable(activity);
        }
        
        // If system theme is selected, determine light or dark mode
        if ("system".equals(theme)) {
            theme = isSystemDarkMode(activity) ? "ocean_dark" : "sunset_light";
        }
        
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        
        // Apply colors based on theme
        switch (theme) {
            case "midnight_blue":
                window.setStatusBarColor(activity.getColor(R.color.theme_midnight_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_midnight_bg));
                break;
            case "purple_night":
                window.setStatusBarColor(activity.getColor(R.color.theme_purple_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_purple_bg));
                break;
            case "forest_dark":
                window.setStatusBarColor(activity.getColor(R.color.theme_forest_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_forest_bg));
                break;
            case "sunset_light":
                window.setStatusBarColor(activity.getColor(R.color.theme_sunset_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_sunset_bg));
                // Light theme - use light status bar icons
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                break;
            case "sky_blue":
                window.setStatusBarColor(activity.getColor(R.color.theme_sky_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_sky_bg));
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                break;
            case "pink_blossom":
                window.setStatusBarColor(activity.getColor(R.color.theme_pink_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_pink_bg));
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                break;
            case "mint_fresh":
                window.setStatusBarColor(activity.getColor(R.color.theme_mint_bg));
                window.setNavigationBarColor(activity.getColor(R.color.theme_mint_bg));
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                break;
            default: // ocean_dark
                window.setStatusBarColor(activity.getColor(R.color.background));
                window.setNavigationBarColor(activity.getColor(R.color.background));
                break;
        }
    }
    
    public static int getBackgroundColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("selected_theme", "system");
        
        // If system theme, determine based on system mode
        if ("system".equals(theme)) {
            theme = isSystemDarkMode(context) ? "ocean_dark" : "sunset_light";
        }
        
        switch (theme) {
            case "midnight_blue": return context.getColor(R.color.theme_midnight_bg);
            case "purple_night": return context.getColor(R.color.theme_purple_bg);
            case "forest_dark": return context.getColor(R.color.theme_forest_bg);
            case "sunset_light": return context.getColor(R.color.theme_sunset_bg);
            case "sky_blue": return context.getColor(R.color.theme_sky_bg);
            case "pink_blossom": return context.getColor(R.color.theme_pink_bg);
            case "mint_fresh": return context.getColor(R.color.theme_mint_bg);
            default: return context.getColor(R.color.background);
        }
    }
    
    public static int getSurfaceColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("selected_theme", "system");
        
        // If system theme, determine based on system mode
        if ("system".equals(theme)) {
            theme = isSystemDarkMode(context) ? "ocean_dark" : "sunset_light";
        }
        
        switch (theme) {
            case "midnight_blue": return context.getColor(R.color.theme_midnight_surface);
            case "purple_night": return context.getColor(R.color.theme_purple_surface);
            case "forest_dark": return context.getColor(R.color.theme_forest_surface);
            case "sunset_light": return context.getColor(R.color.theme_sunset_surface);
            case "sky_blue": return context.getColor(R.color.theme_sky_surface);
            case "pink_blossom": return context.getColor(R.color.theme_pink_surface);
            case "mint_fresh": return context.getColor(R.color.theme_mint_surface);
            default: return context.getColor(R.color.surface);
        }
    }
    
    public static int getPrimaryColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("selected_theme", "system");
        
        // If system theme, determine based on system mode
        if ("system".equals(theme)) {
            theme = isSystemDarkMode(context) ? "ocean_dark" : "sunset_light";
        }
        
        switch (theme) {
            case "midnight_blue": return context.getColor(R.color.theme_midnight_primary);
            case "purple_night": return context.getColor(R.color.theme_purple_primary);
            case "forest_dark": return context.getColor(R.color.theme_forest_primary);
            case "sunset_light": return context.getColor(R.color.theme_sunset_primary);
            case "sky_blue": return context.getColor(R.color.theme_sky_primary);
            case "pink_blossom": return context.getColor(R.color.theme_pink_primary);
            case "mint_fresh": return context.getColor(R.color.theme_mint_primary);
            default: return context.getColor(R.color.primary);
        }
    }
    
    public static int getTextColor(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("selected_theme", "system");
        
        // If system theme, determine based on system mode
        if ("system".equals(theme)) {
            return isSystemDarkMode(context) ? 
                context.getColor(R.color.text_primary) : 
                context.getColor(R.color.theme_sunset_text);
        }
        
        // Light themes have dark text
        if (theme.contains("light") || theme.equals("sky_blue") || theme.equals("pink_blossom") || theme.equals("mint_fresh")) {
            return context.getColor(R.color.theme_sunset_text);
        }
        return context.getColor(R.color.text_primary);
    }
    
    /**
     * Check if system is in dark mode
     */
    public static boolean isSystemDarkMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    
    /**
     * Check if dynamic colors are available (Android 12+)
     */
    public static boolean isDynamicColorsAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }
    
    /**
     * Get the current selected theme
     */
    public static String getSelectedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        return prefs.getString("selected_theme", "system");
    }
}
