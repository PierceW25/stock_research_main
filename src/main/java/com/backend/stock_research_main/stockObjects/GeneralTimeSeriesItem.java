package com.backend.stock_research_main.stockObjects;

import java.sql.Date;

public class GeneralTimeSeriesItem {
    private Date date;
    private Float price;
    
    public GeneralTimeSeriesItem() {
    }

    public GeneralTimeSeriesItem(Date date, Float price) {
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return this.date;
    }
    public Float getPrice() {
        return this.price;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    
}
