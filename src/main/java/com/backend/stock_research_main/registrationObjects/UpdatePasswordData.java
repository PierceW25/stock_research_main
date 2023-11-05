package com.backend.stock_research_main.registrationObjects;

public class UpdatePasswordData {
    private String token;
    private String password;

    public UpdatePasswordData() {}

    public UpdatePasswordData(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }
    public String getPassword() {
        return this.password;
    }

    public void setToken(String token) {
        this.token = token;
    } 
    public void setPassword(String password) {
        this.password = password;
    }
}
