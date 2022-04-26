package com.psdit.autenticadorapp.service;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psdit.autenticadorapp.LoginActivity;
import com.psdit.autenticadorapp.api.API;
import com.psdit.autenticadorapp.app.AutenticadorApp;
import com.psdit.autenticadorapp.interfaces.Notificador;
import com.psdit.autenticadorapp.model.AuthRequest;
import com.psdit.autenticadorapp.model.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {

    private final LoginActivity loginActivity;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Notificador notificador;

    public LoginService(LoginActivity loginActivity, Notificador notificador) {
        this.loginActivity = loginActivity;
        this.notificador = notificador;
    }

    public void login(String email, String senha) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("USER", user.getEmail());
                            notificador.login(true);
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(loginActivity, "Login incorreto, revise seus dados!", Toast.LENGTH_LONG).show();
                            notificador.login(false);
                        }
                    }
                });
    }

    public boolean isLogado() {
        return mAuth.getCurrentUser() != null;
    }

    public void getTokenFromApi(String qrcodeContents) {
        API api = AutenticadorApp.retrofit.create(API.class);
        String uid = mAuth.getCurrentUser().getUid();
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUid(uid);

        api.authRequestToken(authRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Log.i("RESPONSE", response.body().toString());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference("eventos").child(qrcodeContents).setValue(response.body().getUserToken());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e("RESPONSE", t.getMessage());
            }
        });
    }

}
