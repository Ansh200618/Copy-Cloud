package com.copycloud.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android:view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.copycloud.app.R;
import com.copycloud.app.utils.ThemeManager;

public class SettingsFragment extends Fragment {
    
    private SharedPreferences prefs;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        prefs = requireContext().getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        
        setupThemeListeners(view);
        highlightCurrentTheme(view);
    }
    
    private void setupThemeListeners(View view) {
        // Dark themes
        view.findViewById(R.id.themeOceanDark).setOnClickListener(v -> applyTheme("ocean_dark"));
        view.findViewById(R.id.themeMidnightBlue).setOnClickListener(v -> applyTheme("midnight_blue"));
        view.findViewById(R.id.themePurpleNight).setOnClickListener(v -> applyTheme("purple_night"));
        view.findViewById(R.id.themeForestDark).setOnClickListener(v -> applyTheme("forest_dark"));
        
        // Light themes
        view.findViewById(R.id.themeSunsetLight).setOnClickListener(v -> applyTheme("sunset_light"));
        view.findViewById(R.id.themeSkyBlue).setOnClickListener(v -> applyTheme("sky_blue"));
        view.findViewById(R.id.themePinkBlossom).setOnClickListener(v -> applyTheme("pink_blossom"));
        view.findViewById(R.id.themeMintFresh).setOnClickListener(v -> applyTheme("mint_fresh"));
    }
    
    private void applyTheme(String themeName) {
        prefs.edit().putString("selected_theme", themeName).apply();
        Toast.makeText(requireContext(), R.string.theme_applied, Toast.LENGTH_SHORT).show();
        
        // Apply theme and restart activity
        ThemeManager.applyTheme(themeName);
        requireActivity().recreate();
    }
    
    private void highlightCurrentTheme(View view) {
        String currentTheme = prefs.getString("selected_theme", "ocean_dark");
        
        // Remove all highlights
        removeHighlight(view, R.id.themeOceanDark);
        removeHighlight(view, R.id.themeMidnightBlue);
        removeHighlight(view, R.id.themePurpleNight);
        removeHighlight(view, R.id.themeForestDark);
        removeHighlight(view, R.id.themeSunsetLight);
        removeHighlight(view, R.id.themeSkyBlue);
        removeHighlight(view, R.id.themePinkBlossom);
        removeHighlight(view, R.id.themeMintFresh);
        
        // Add highlight to current theme
        int themeId = getThemeCardId(currentTheme);
        if (themeId != 0) {
            CardView card = view.findViewById(themeId);
            if (card != null) {
                card.setStrokeWidth(4);
            }
        }
    }
    
    private void removeHighlight(View view, int cardId) {
        CardView card = view.findViewById(cardId);
        if (card != null) {
            card.setStrokeWidth(0);
        }
    }
    
    private int getThemeCardId(String themeName) {
        switch (themeName) {
            case "ocean_dark": return R.id.themeOceanDark;
            case "midnight_blue": return R.id.themeMidnightBlue;
            case "purple_night": return R.id.themePurpleNight;
            case "forest_dark": return R.id.themeForestDark;
            case "sunset_light": return R.id.themeSunsetLight;
            case "sky_blue": return R.id.themeSkyBlue;
            case "pink_blossom": return R.id.themePinkBlossom;
            case "mint_fresh": return R.id.themeMintFresh;
            default: return 0;
        }
    }
}
