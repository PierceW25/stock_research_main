package com.backend.stock_research_main.stockObjects;

import java.sql.Timestamp;

public class DailyTimeSeriesItem {
    private Timestamp date;
    private Float price;

    public DailyTimeSeriesItem() {
    }

    public DailyTimeSeriesItem(Timestamp date, Float price) {
        this.date = date;
        this.price = price;
    }

    public Timestamp getDate() {
        return this.date;
    }
    public Float getPrice() {
        return this.price;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
}
