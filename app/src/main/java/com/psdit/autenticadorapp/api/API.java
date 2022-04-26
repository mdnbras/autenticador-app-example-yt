package com.psdit.autenticadorapp.api;

import com.psdit.autenticadorapp.model.AuthRequest;
import com.psdit.autenticadorapp.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {
    @Headers("Content-Type: application/json")
    @POST("/v1/auth")
    Call<AuthResponse> authRequestToken(@Body AuthRequest authRequest);
}
