package com.backend.stock_research_main.stockObjects;

public class dailyInfo {
    private String ticker;
    private String price;
    private String days_change;
    private String most_recent;

    public dailyInfo() {
    }

    public dailyInfo(String ticker, String price, String days_change, String most_recent) {
        this.ticker = ticker;
        this.price = price;
        this.days_change = days_change;
        this.most_recent = most_recent;
    }

    public String getTicker() {
        return this.ticker;
    }
    public String getPrice() {
        return this.price;
    }
    public String getDays_change() {
        return this.days_change;
    }
    public String getMost_recent() {
        return this.most_recent;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setDays_change(String days_change) {
        this.days_change = days_change;
    }
    public void setMost_recent(String most_recent) {
        this.most_recent = most_recent;
    }
}
