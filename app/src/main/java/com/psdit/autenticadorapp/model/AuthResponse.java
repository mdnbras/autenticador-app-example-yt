package com.psdit.autenticadorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @Expose
    @SerializedName("ok")
    private boolean ok;

    @Expose
    @SerializedName("userToken")
    private String userToken;


    @Expose
    @SerializedName("message")
    private String message;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "ok=" + ok +
                ", userToken='" + userToken + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
