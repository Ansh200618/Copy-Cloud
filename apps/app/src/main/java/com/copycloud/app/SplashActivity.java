package com.copycloud.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.copycloud.app.utils.ThemeManager;

public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DURATION = 2500; // 2.5 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply theme before setting content view
        ThemeManager.applyThemeToActivity(this);
        
        setContentView(R.layout.activity_splash);
        
        // Find views
        ImageView logoImage = findViewById(R.id.splashLogo);
        TextView appName = findViewById(R.id.splashAppName);
        TextView tagline = findViewById(R.id.splashTagline);
        
        // Load and start animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        
        logoImage.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);
        tagline.startAnimation(slideUp);
        
        // Navigate to MainActivity after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            
            // Add smooth transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, SPLASH_DURATION);
    }
    
    @Override
    public void onBackPressed() {
        // Disable back button on splash screen
        // Do nothing
    }
}
