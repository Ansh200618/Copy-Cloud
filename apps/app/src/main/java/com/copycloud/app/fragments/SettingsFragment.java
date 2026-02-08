package com.copycloud.app.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.copycloud.app.R;
import com.copycloud.app.utils.ConnectedDevicesManager;
import com.copycloud.app.utils.DeviceManager;
import com.copycloud.app.utils.ThemeManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.Set;

public class SettingsFragment extends Fragment {
    
    private SharedPreferences prefs;
    private TextView tvDeviceCode;
    private LinearLayout connectedDevicesContainer;
    private MaterialButton btnAddDevice;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        prefs = requireContext().getSharedPreferences("CopyCloudPrefs", Context.MODE_PRIVATE);
        
        initViews(view);
        setupThemeListeners(view);
        setupDeviceCode();
        setupConnectedDevices();
        highlightCurrentTheme(view);
    }
    
    private void initViews(View view) {
        tvDeviceCode = view.findViewById(R.id.tvDeviceCode);
        connectedDevicesContainer = view.findViewById(R.id.connectedDevicesContainer);
        btnAddDevice = view.findViewById(R.id.btnAddDevice);
    }
    
    private void setupDeviceCode() {
        String deviceCode = DeviceManager.getDeviceCode(requireContext());
        String formattedCode = DeviceManager.formatDeviceCode(deviceCode);
        
        if (tvDeviceCode != null) {
            tvDeviceCode.setText(formattedCode);
            tvDeviceCode.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Device Code", deviceCode);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), R.string.device_code_copied, Toast.LENGTH_SHORT).show();
            });
        }
    }
    
    private void setupConnectedDevices() {
        if (btnAddDevice != null) {
            btnAddDevice.setOnClickListener(v -> showAddDeviceDialog());
        }
        updateConnectedDevicesList();
    }
    
    private void updateConnectedDevicesList() {
        if (connectedDevicesContainer == null) return;
        
        connectedDevicesContainer.removeAllViews();
        Set<String> devices = ConnectedDevicesManager.getConnectedDevices(requireContext());
        
        if (devices.isEmpty()) {
            TextView emptyView = new TextView(requireContext());
            emptyView.setText("No connected devices yet");
            emptyView.setTextColor(getResources().getColor(R.color.text_secondary, null));
            emptyView.setPadding(16, 16, 16, 16);
            connectedDevicesContainer.addView(emptyView);
        } else {
            for (String deviceCode : devices) {
                addDeviceView(deviceCode);
            }
        }
        
        // Update button state
        if (btnAddDevice != null) {
            int count = ConnectedDevicesManager.getConnectedDeviceCount(requireContext());
            btnAddDevice.setEnabled(count < ConnectedDevicesManager.getMaxDeviceLimit());
            btnAddDevice.setText(count >= ConnectedDevicesManager.getMaxDeviceLimit() 
                ? "Max " + ConnectedDevicesManager.getMaxDeviceLimit() + " Devices" 
                : "Add Device");
        }
    }
    
    private void addDeviceView(String deviceCode) {
        View deviceView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_connected_device, connectedDevicesContainer, false);
        
        TextView tvCode = deviceView.findViewById(R.id.tvConnectedDeviceCode);
        MaterialButton btnRemove = deviceView.findViewById(R.id.btnRemoveDevice);
        
        tvCode.setText(DeviceManager.formatDeviceCode(deviceCode));
        
        // Click to copy
        deviceView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Device Code", deviceCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), "Copied: " + deviceCode, Toast.LENGTH_SHORT).show();
        });
        
        // Long click to show options
        deviceView.setOnLongClickListener(v -> {
            showRemoveDeviceDialog(deviceCode);
            return true;
        });
        
        btnRemove.setOnClickListener(v -> showRemoveDeviceDialog(deviceCode));
        
        connectedDevicesContainer.addView(deviceView);
    }
    
    private void showAddDeviceDialog() {
        // Check limit
        if (!ConnectedDevicesManager.canAddMoreDevices(requireContext())) {
            Toast.makeText(requireContext(), R.string.max_devices_reached, Toast.LENGTH_SHORT).show();
            return;
        }
        
        EditText input = new EditText(requireContext());
        input.setHint("Enter 8-digit device code");
        input.setPadding(50, 20, 50, 20);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.connect_device_title)
                .setMessage(R.string.connect_device_message)
                .setView(input)
                .setPositiveButton("Connect", (dialog, which) -> {
                    String code = input.getText().toString().trim();
                    
                    if (!ConnectedDevicesManager.isValidDeviceCode(code)) {
                        Toast.makeText(requireContext(), R.string.invalid_device_code, Toast.LENGTH_LONG).show();
                        return;
                    }
                    
                    // Try to add device
                    boolean added = ConnectedDevicesManager.addConnectedDevice(requireContext(), code);
                    
                    if (added) {
                        updateConnectedDevicesList();
                        Toast.makeText(requireContext(), R.string.device_connected, Toast.LENGTH_SHORT).show();
                    } else {
                        int count = ConnectedDevicesManager.getConnectedDeviceCount(requireContext());
                        if (count >= ConnectedDevicesManager.getMaxDeviceLimit()) {
                            Toast.makeText(requireContext(), R.string.max_devices_reached, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), R.string.device_already_connected, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void showRemoveDeviceDialog(String deviceCode) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remove Device")
                .setMessage("Remove device " + DeviceManager.formatDeviceCode(deviceCode) + "?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    ConnectedDevicesManager.removeConnectedDevice(requireContext(), deviceCode);
                    updateConnectedDevicesList();
                    Toast.makeText(requireContext(), R.string.device_removed, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
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
        ThemeManager.applyTheme(themeName);
        requireActivity().recreate();
    }
    
    private void highlightCurrentTheme(View view) {
        String currentTheme = prefs.getString("selected_theme", "ocean_dark");
        
        removeHighlight(view, R.id.themeOceanDark);
        removeHighlight(view, R.id.themeMidnightBlue);
        removeHighlight(view, R.id.themePurpleNight);
        removeHighlight(view, R.id.themeForestDark);
        removeHighlight(view, R.id.themeSunsetLight);
        removeHighlight(view, R.id.themeSkyBlue);
        removeHighlight(view, R.id.themePinkBlossom);
        removeHighlight(view, R.id.themeMintFresh);
        
        int themeId = getThemeCardId(currentTheme);
        if (themeId != 0) {
            MaterialCardView card = view.findViewById(themeId);
            if (card != null) {
                card.setStrokeWidth(4);
            }
        }
    }
    
    private void removeHighlight(View view, int cardId) {
        MaterialCardView card = view.findViewById(cardId);
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
