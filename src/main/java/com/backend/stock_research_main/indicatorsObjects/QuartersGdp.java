package com.backend.stock_research_main.indicatorsObjects;

public class QuartersGdp {
    private String date;
    private String value;

    public QuartersGdp() {
    }

    public QuartersGdp(String date, String value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return this.date;
    }

    public String getValue() {
        return this.value;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
