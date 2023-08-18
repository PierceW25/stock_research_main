package com.backend.stock_research_main.registrationObjects;

public class receivedUser {
    private String username;
    private String email;
    private String password;
    private String userLevel;

    public receivedUser() {
    }

    public receivedUser(String username, String email, String password, String userLevel) {
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

    public String getPassword() {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
