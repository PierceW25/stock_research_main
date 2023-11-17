package com.backend.stock_research_main.registrationObjects;


public class ValidationMessageAndEmail {
    private String email;
    private String tokenMessage;

    public ValidationMessageAndEmail() {}

    public ValidationMessageAndEmail(String email, String tokenMessage) {
        this.email = email;
        this.tokenMessage = tokenMessage;
    }

    public String getEmail() {
        return this.email;
    }
    public String getTokenMessage() {
        return this.tokenMessage;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setTokenMessage(String tokenMessage) {
        this.tokenMessage = tokenMessage;
    }
}
