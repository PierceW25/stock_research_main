package com.backend.stock_research_main.stockObjects;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.util.SerializationUtils;

public class HistoricalPriceData implements Serializable {
    private String ticker;
    private LocalDate date;
    private float price;
    private String series;
    private String time_pushed;

    public HistoricalPriceData deepCopy() throws IOException, ClassNotFoundException {
        return (HistoricalPriceData) SerializationUtils.clone(this);
    }

    public HistoricalPriceData() {
    }

    public HistoricalPriceData(String ticker, LocalDate date, float price, String series, String time_pushed) {
        this.ticker = ticker;
        this.date = date;
        this.price = price;
        this.series = series;
        this.time_pushed = time_pushed;
    }

    public String getTicker() {
        return this.ticker;
    }
    public LocalDate getDate() {
        return this.date;
    }
    public float getPrice() {
        return this.price;
    }
    public String getSeries() {
        return this.series;
    }
    public String getTime_pushed() {
        return this.time_pushed;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setSeries(String series) {
        this.series = series;
    }
    public void setTime_pushed(String time_pushed) {
        this.time_pushed = time_pushed;
    }

}
