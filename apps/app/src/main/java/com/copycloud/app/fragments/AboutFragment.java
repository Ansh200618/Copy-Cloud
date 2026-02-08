package com.copycloud.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.copycloud.app.R;
import com.google.android.material.button.MaterialButton;

public class AboutFragment extends Fragment {
    
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    private static final String PRIVACY_POLICY_URL = "https://copycloud.vercel.app/privacy";
    private static final String FEEDBACK_EMAIL = "anshdeepsingh200618@gmail.com";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupActionButtons(view);
    }
    
    private void setupActionButtons(View view) {
        // Rate App Button
        MaterialButton btnRateApp = view.findViewById(R.id.btnRateApp);
        if (btnRateApp != null) {
            btnRateApp.setOnClickListener(v -> openPlayStore());
        }
        
        // Share App Button
        MaterialButton btnShareApp = view.findViewById(R.id.btnShareApp);
        if (btnShareApp != null) {
            btnShareApp.setOnClickListener(v -> shareApp());
        }
        
        // Privacy Policy Button
        MaterialButton btnPrivacyPolicy = view.findViewById(R.id.btnPrivacyPolicy);
        if (btnPrivacyPolicy != null) {
            btnPrivacyPolicy.setOnClickListener(v -> openPrivacyPolicy());
        }
        
        // Feedback Button
        MaterialButton btnFeedback = view.findViewById(R.id.btnFeedback);
        if (btnFeedback != null) {
            btnFeedback.setOnClickListener(v -> sendFeedback());
        }
    }
    
    private void openPlayStore() {
        String packageName = requireContext().getPackageName();
        try {
            // Try to open Play Store app
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Fallback to web browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL + packageName));
            startActivity(intent);
        }
    }
    
    private void shareApp() {
        String shareText = "Check out Copy Cloud - Share files and text instantly without login!\n\n" +
                "Download: " + PLAY_STORE_URL + requireContext().getPackageName();
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Copy Cloud - Online Clipboard");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        startActivity(Intent.createChooser(shareIntent, "Share Copy Cloud via"));
    }
    
    private void openPrivacyPolicy() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "No browser found", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void sendFeedback() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{FEEDBACK_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Copy Cloud - Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n\nI would like to share my feedback:\n\n");
        
        try {
            startActivity(Intent.createChooser(emailIntent, "Send feedback via"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}
