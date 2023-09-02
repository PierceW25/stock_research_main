package com.backend.stock_research_main.stockObjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class GlobalQuote {
    @JsonAlias("05. price")
    private String price;
    @JsonAlias("10. change percent")
    private String days_change;

    public GlobalQuote() {
    }

    public GlobalQuote(String price, String days_change) {
        this.price = price;
        this.days_change = days_change;
    }

    public String getPrice() {
        return this.price;
    }

    public String getDays_change() {
        return this.days_change;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDays_change(String days_change) {
        this.days_change = days_change;
    }

}
