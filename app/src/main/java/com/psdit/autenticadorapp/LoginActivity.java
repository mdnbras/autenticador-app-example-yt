package com.psdit.autenticadorapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.psdit.autenticadorapp.interfaces.Notificador;
import com.psdit.autenticadorapp.service.LoginService;

public class LoginActivity extends AppCompatActivity implements Notificador {

    private LoginService loginService;
    private Button btnAcessar;
    private Button btnAutenticar;
    private Button btnSair;
    private TextInputEditText inputEmail;
    private TextInputEditText inputSenha;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutSenha;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    loginService.getTokenFromApi(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnAcessar = findViewById(R.id.btnAcessar);
        btnAutenticar = findViewById(R.id.btnAutenticar);
        btnSair = findViewById(R.id.btnSair);
        inputEmail = findViewById(R.id.input_email);
        inputSenha = findViewById(R.id.input_senha);
        layoutEmail = findViewById(R.id.layout_email);
        layoutSenha = findViewById(R.id.layout_senha);
        loginService = new LoginService(this, this);


        setupUI();

        btnAcessar.setOnClickListener(v -> {
            if (formValidate(inputEmail.getText().toString(), inputSenha.getText().toString())) {
                loginService.login(inputEmail.getText().toString(), inputSenha.getText().toString());
            }
        });

        btnSair.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            setupUI();
        });

        btnAutenticar.setOnClickListener(v -> {
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

    private boolean formValidate(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Email ou Senha incorretos.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setupUI() {
        if (loginService.isLogado()) {
            layoutEmail.setVisibility(View.GONE);
            layoutSenha.setVisibility(View.GONE);
            btnAcessar.setVisibility(View.GONE);
            btnAutenticar.setVisibility(View.VISIBLE);
            btnSair.setVisibility(View.VISIBLE);
        } else {
            layoutEmail.setVisibility(View.VISIBLE);
            layoutSenha.setVisibility(View.VISIBLE);
            btnAcessar.setVisibility(View.VISIBLE);
            btnAutenticar.setVisibility(View.GONE);
            btnSair.setVisibility(View.GONE);
        }
    }

    @Override
    public void login(boolean success) {
        setupUI();
    }
}