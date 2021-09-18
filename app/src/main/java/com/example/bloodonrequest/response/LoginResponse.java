package com.example.bloodonrequest.response;

public class LoginResponse {
    private String status;
    private String token;
    private String message;


    public LoginResponse(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public LoginResponse(String status, String token, String message) {
        this.status = status;
        this.token = token;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

