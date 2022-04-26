package com.psdit.autenticadorapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class LoginActivity extends AppCompatActivity {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
        result -> {
            if (result.getContents() == null) {
                Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnAcessar = findViewById(R.id.btnAcessar);

        btnAcessar.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setPrompt("Scan a barcode");
            options.setCameraId(0);
            options.setBeepEnabled(false);
            options.setBarcodeImageEnabled(true);
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });
    }
}