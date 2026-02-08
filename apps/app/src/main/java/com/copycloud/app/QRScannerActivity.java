package com.copycloud.app;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRScannerActivity extends CaptureActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_qr_scanner);
        return findViewById(R.id.zxing_barcode_scanner);
    }
}
