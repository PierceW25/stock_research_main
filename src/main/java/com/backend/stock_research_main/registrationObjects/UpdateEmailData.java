package com.backend.stock_research_main.registrationObjects;

public class UpdateEmailData {
    private String email;
    private String token;

    public UpdateEmailData() {}

    public UpdateEmailData(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return this.email;
    }
    public String getToken() {
        return this.token;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
