package com.backend.stock_research_main.registrationObjects;

public class User {
    private String username;
    private String email;
    private byte[] password;
    private String userLevel;

    public User() {
    }

    public User(String username, String email, byte[] password, String userLevel) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userLevel = userLevel;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public String getUserLevel() {
        return this.userLevel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
