package com.backend.stock_research_main.stockObjects;

import java.sql.Timestamp;

public class DailyInfo {
    private String title;
    private String ticker;
    private float price;
    private String days_change;
    private Timestamp datetime_added;

    public DailyInfo() {
    }

    public DailyInfo(String title, String ticker, float price, String days_change, Timestamp datetime_added) {
        this.title = title;
        this.ticker = ticker;
        this.price = price;
        this.days_change = days_change;
        this.datetime_added = datetime_added;
    }

    public String getTitle() {
        return this.title;
    }
    public String getTicker() {
        return this.ticker;
    }
    public float getPrice() {
        return this.price;
    }
    public String getDays_change() {
        return this.days_change;
    }
    public Timestamp getDatetime_added() {
        return this.datetime_added;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setDays_change(String days_change) {
        this.days_change = days_change;
    }
    public void setDatetime_added(Timestamp datetime_added) {
        this.datetime_added = datetime_added;
    }
}
