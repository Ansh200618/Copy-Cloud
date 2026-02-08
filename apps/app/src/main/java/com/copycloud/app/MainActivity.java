package com.copycloud.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.copycloud.app.adapters.ViewPagerAdapter;
import com.copycloud.app.utils.NotificationHelper;
import com.copycloud.app.utils.ThemeManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    
    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;
    
    private BottomNavigationView bottomNavigation;
    private ViewPager2 viewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply saved theme
        ThemeManager.applyThemeToActivity(this);
        
        setContentView(R.layout.activity_main);
        
        // Initialize notification channel
        NotificationHelper.createNotificationChannel(this);
        
        // Request notification permission if needed
        requestNotificationPermissionIfNeeded();
        
        initViews();
        setupViewPager();
        setupBottomNavigation();
        
        // Handle notification intent
        handleNotificationIntent(getIntent());
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNotificationIntent(intent);
    }
    
    private void handleNotificationIntent(Intent intent) {
        if (intent != null && intent.getBooleanExtra("open_retrieve", false)) {
            // Open retrieve tab
            viewPager.setCurrentItem(1, true);
            
            // Optionally pre-fill code if provided
            String code = intent.getStringExtra("code");
            // TODO: Pass code to RetrieveFragment
        }
    }
    
    private void requestNotificationPermissionIfNeeded() {
        if (NotificationHelper.needsNotificationPermission()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }
    
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        bottomNavigation = findViewById(R.id.bottomNavigation);
        viewPager = findViewById(R.id.viewPager);
    }
    
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // Sync ViewPager with BottomNavigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.nav_send);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.nav_retrieve);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.nav_history);
                        break;
                    case 3:
                        bottomNavigation.setSelectedItemId(R.id.nav_settings);
                        break;
                }
            }
        });
    }
    
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_send) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_retrieve) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_history) {
                viewPager.setCurrentItem(2, true);
                return true;
            } else if (itemId == R.id.nav_settings) {
                viewPager.setCurrentItem(3, true);
                return true;
            }
            return false;
        });
    }
}
