package com.copycloud.app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.copycloud.app.R;
import com.copycloud.app.api.SupabaseClient;
import com.copycloud.app.models.ClipData;
import com.copycloud.app.utils.CodeGenerator;
import com.copycloud.app.utils.NetworkUtils;
import com.copycloud.app.utils.QRCodeGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SendFragment extends Fragment {
    
    private static final int MAX_FILE_SIZE = 40 * 1024 * 1024; // 40MB
    
    private MaterialButton btnTextMode, btnFileMode;
    private MaterialCardView textInputCard, fileInputCard, successCard, deviceTargetCard;
    private EditText editTextContent;
    private TextInputEditText editTargetDevice;
    private MaterialButton btnSelectFiles, btnGenerate, btnCopyCode, btnStartNew;
    private TextView tvFileInfo, tvGeneratedCode;
    private ImageView imageQrCode;
    private ProgressBar progressBar;
    
    private boolean isTextMode = true;
    private List<Uri> selectedFiles = new ArrayList<>();
    private String generatedCode = "";
    
    private SupabaseClient supabaseClient;
    
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    handleFileSelection(result.getData());
                }
            }
    );
    
    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openFilePicker();
                } else {
                    Toast.makeText(requireContext(), R.string.permission_storage, Toast.LENGTH_SHORT).show();
                }
            }
    );
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        supabaseClient = new SupabaseClient();
        initViews(view);
        setupListeners();
    }
    
    private void initViews(View view) {
        btnTextMode = view.findViewById(R.id.btnTextMode);
        btnFileMode = view.findViewById(R.id.btnFileMode);
        textInputCard = view.findViewById(R.id.textInputCard);
        fileInputCard = view.findViewById(R.id.fileInputCard);
        successCard = view.findViewById(R.id.successCard);
        deviceTargetCard = view.findViewById(R.id.deviceTargetCard);
        editTextContent = view.findViewById(R.id.editTextContent);
        editTargetDevice = view.findViewById(R.id.editTargetDevice);
        btnSelectFiles = view.findViewById(R.id.btnSelectFiles);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        tvFileInfo = view.findViewById(R.id.tvFileInfo);
        tvGeneratedCode = view.findViewById(R.id.tvGeneratedCode);
        imageQrCode = view.findViewById(R.id.imageQrCode);
        btnCopyCode = view.findViewById(R.id.btnCopyCode);
        btnStartNew = view.findViewById(R.id.btnStartNew);
        progressBar = view.findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        btnTextMode.setOnClickListener(v -> switchToTextMode());
        btnFileMode.setOnClickListener(v -> switchToFileMode());
        btnSelectFiles.setOnClickListener(v -> checkPermissionAndOpenFilePicker());
        btnGenerate.setOnClickListener(v -> generateCode());
        btnCopyCode.setOnClickListener(v -> copyCodeToClipboard());
        btnStartNew.setOnClickListener(v -> resetForm());
    }
    
    private void switchToTextMode() {
        isTextMode = true;
        btnTextMode.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary));
        btnTextMode.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        btnFileMode.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.surface_light));
        btnFileMode.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        
        textInputCard.setVisibility(View.VISIBLE);
        fileInputCard.setVisibility(View.GONE);
    }
    
    private void switchToFileMode() {
        isTextMode = false;
        btnFileMode.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.primary));
        btnFileMode.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        btnTextMode.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.surface_light));
        btnTextMode.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
        
        textInputCard.setVisibility(View.GONE);
        fileInputCard.setVisibility(View.VISIBLE);
    }
    
    private void checkPermissionAndOpenFilePicker() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            openFilePicker();
        } else if (ContextCompat.checkSelfPermission(requireContext(), 
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select Files"));
    }
    
    private void handleFileSelection(Intent data) {
        selectedFiles.clear();
        
        if (data.getClipData() != null) {
            // Multiple files
            android.content.ClipData clipData = data.getClipData();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                selectedFiles.add(clipData.getItemAt(i).getUri());
            }
        } else if (data.getData() != null) {
            // Single file
            selectedFiles.add(data.getData());
        }
        
        updateFileInfo();
    }
    
    private void updateFileInfo() {
        if (selectedFiles.isEmpty()) {
            tvFileInfo.setVisibility(View.GONE);
            return;
        }
        
        tvFileInfo.setVisibility(View.VISIBLE);
        tvFileInfo.setText(getString(R.string.select_files, selectedFiles.size()));
    }
    
    private void generateCode() {
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (isTextMode) {
            String text = editTextContent.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter some text", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadText(text);
        } else {
            if (selectedFiles.isEmpty()) {
                Toast.makeText(requireContext(), "Please select files", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadFiles();
        }
    }
    
    private void uploadText(String text) {
        progressBar.setVisibility(View.VISIBLE);
        btnGenerate.setEnabled(false);
        
        String code = CodeGenerator.generateCode();
        String targetDevice = editTargetDevice.getText().toString().trim();
        
        // Validate device code if provided
        if (!targetDevice.isEmpty() && targetDevice.length() != 8) {
            Toast.makeText(requireContext(), R.string.invalid_device_code, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnGenerate.setEnabled(true);
            return;
        }
        
        ClipData clipData;
        if (targetDevice.isEmpty()) {
            clipData = new ClipData(code, text, "text");
        } else {
            clipData = new ClipData(code, text, "text", targetDevice);
        }
        
        supabaseClient.insertClip(clipData, new SupabaseClient.ApiCallback<ClipData>() {
            @Override
            public void onSuccess(ClipData data) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);
                    showSuccess(data.getCode());
                });
            }
            
            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void uploadFiles() {
        // For now, show a message that file upload is coming soon
        Toast.makeText(requireContext(), "File upload functionality will be implemented with Supabase Storage", Toast.LENGTH_LONG).show();
    }
    
    private void showSuccess(String code) {
        generatedCode = code;
        textInputCard.setVisibility(View.GONE);
        fileInputCard.setVisibility(View.GONE);
        btnGenerate.setVisibility(View.GONE);
        successCard.setVisibility(View.VISIBLE);
        
        tvGeneratedCode.setText(code);
        
        // Generate QR code
        Bitmap qrBitmap = QRCodeGenerator.generateQRCode(code, 600, 600);
        if (qrBitmap != null) {
            imageQrCode.setImageBitmap(qrBitmap);
        }
    }
    
    private void copyCodeToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copy Cloud Code", generatedCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(), R.string.code_copied, Toast.LENGTH_SHORT).show();
    }
    
    private void resetForm() {
        successCard.setVisibility(View.GONE);
        btnGenerate.setVisibility(View.VISIBLE);
        editTextContent.setText("");
        editTargetDevice.setText("");
        selectedFiles.clear();
        updateFileInfo();
        
        if (isTextMode) {
            textInputCard.setVisibility(View.VISIBLE);
        } else {
            fileInputCard.setVisibility(View.VISIBLE);
        }
    }
}
