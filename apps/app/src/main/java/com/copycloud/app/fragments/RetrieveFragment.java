package com.copycloud.app.fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.copycloud.app.R;
import com.copycloud.app.adapters.FileAdapter;
import com.copycloud.app.api.SupabaseClient;
import com.copycloud.app.models.FileItem;
import com.copycloud.app.utils.NetworkUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class RetrieveFragment extends Fragment {
    
    private TextInputEditText editTextCode;
    private MaterialButton btnRetrieve, btnScanQr, btnCopyText, btnDownloadAll;
    private MaterialCardView contentCard;
    private LinearLayout textContentLayout, fileContentLayout;
    private TextView tvContentType, tvCreatedAt, tvTextContent;
    private ImageView imagePreview;
    private RecyclerView recyclerViewFiles;
    private ProgressBar progressBar;
    
    private SupabaseClient supabaseClient;
    private List<FileItem> fileList = new ArrayList<>();
    private FileAdapter fileAdapter;
    
    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    launchQRScanner();
                } else {
                    Toast.makeText(requireContext(), R.string.permission_camera, Toast.LENGTH_SHORT).show();
                }
            }
    );
    
    private final ActivityResultLauncher<ScanOptions> qrScannerLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    handleQRCodeResult(result.getContents());
                }
            }
    );
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_retrieve, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        supabaseClient = new SupabaseClient();
        initViews(view);
        setupListeners();
        setupRecyclerView();
    }
    
    private void initViews(View view) {
        editTextCode = view.findViewById(R.id.editTextCode);
        btnRetrieve = view.findViewById(R.id.btnRetrieve);
        btnScanQr = view.findViewById(R.id.btnScanQr);
        contentCard = view.findViewById(R.id.contentCard);
        tvContentType = view.findViewById(R.id.tvContentType);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
        textContentLayout = view.findViewById(R.id.textContentLayout);
        fileContentLayout = view.findViewById(R.id.fileContentLayout);
        tvTextContent = view.findViewById(R.id.tvTextContent);
        imagePreview = view.findViewById(R.id.imagePreview);
        recyclerViewFiles = view.findViewById(R.id.recyclerViewFiles);
        btnCopyText = view.findViewById(R.id.btnCopyText);
        btnDownloadAll = view.findViewById(R.id.btnDownloadAll);
        progressBar = view.findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        btnRetrieve.setOnClickListener(v -> retrieveContent());
        btnScanQr.setOnClickListener(v -> checkCameraPermissionAndScan());
        btnCopyText.setOnClickListener(v -> copyTextToClipboard());
    }
    
    private void setupRecyclerView() {
        fileAdapter = new FileAdapter(fileList, file -> {
            Toast.makeText(requireContext(), "Downloading: " + file.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Implement file download
        });
        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewFiles.setAdapter(fileAdapter);
    }
    
    private void checkCameraPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(requireContext(), 
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchQRScanner();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
    
    private void launchQRScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt(getString(R.string.qr_scan_instruction));
        options.setCameraId(0);
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(false);
        options.setOrientationLocked(false);
        qrScannerLauncher.launch(options);
    }
    
    private void handleQRCodeResult(String code) {
        // Extract code if it's a full URL or just use the code
        String extractedCode = code;
        if (code.contains("/")) {
            String[] parts = code.split("/");
            extractedCode = parts[parts.length - 1];
        }
        
        // Set the code and retrieve
        editTextCode.setText(extractedCode.toUpperCase());
        retrieveContent();
    }
    
    private void retrieveContent() {
        String code = editTextCode.getText().toString().trim().toUpperCase();
        
        if (code.isEmpty() || code.length() != 6) {
            Toast.makeText(requireContext(), R.string.error_invalid_code, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        btnRetrieve.setEnabled(false);
        
        supabaseClient.getClipByCode(code, new SupabaseClient.ApiCallback<com.copycloud.app.models.ClipData>() {
            @Override
            public void onSuccess(com.copycloud.app.models.ClipData data) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRetrieve.setEnabled(true);
                    displayContent(data);
                });
            }
            
            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnRetrieve.setEnabled(true);
                    Toast.makeText(requireContext(), R.string.error_code_not_found, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void displayContent(com.copycloud.app.models.ClipData data) {
        contentCard.setVisibility(View.VISIBLE);
        
        if ("text".equals(data.getType())) {
            displayTextContent(data);
        } else {
            displayFileContent(data);
        }
        
        // Set created time
        tvCreatedAt.setText(getString(R.string.created_at, getTimeAgo(data.getCreated_at())));
    }
    
    private void displayTextContent(com.copycloud.app.models.ClipData data) {
        tvContentType.setText(R.string.content_type_text);
        textContentLayout.setVisibility(View.VISIBLE);
        fileContentLayout.setVisibility(View.GONE);
        
        tvTextContent.setText(data.getContent());
    }
    
    private void displayFileContent(com.copycloud.app.models.ClipData data) {
        tvContentType.setText(R.string.content_type_file);
        textContentLayout.setVisibility(View.GONE);
        fileContentLayout.setVisibility(View.VISIBLE);
        
        // TODO: Parse file list and display
        Toast.makeText(requireContext(), "File display coming soon", Toast.LENGTH_SHORT).show();
    }
    
    private void copyTextToClipboard() {
        String text = tvTextContent.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy Cloud Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(), R.string.text_copied, Toast.LENGTH_SHORT).show();
    }
    
    private String getTimeAgo(String timestamp) {
        // Simple time ago calculation
        // TODO: Implement proper time ago logic
        return "recently";
    }
}
