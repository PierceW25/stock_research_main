package com.backend.stock_research_main.stockObjects;

public class IntervalData {
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;

    public IntervalData() {
    }

    public IntervalData(String open, String high, String low, String close, String volume) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getOpen() {
        return this.open;
    }
    public String getHigh() {
        return this.high;
    }
    public String getLow() {
        return this.low;
    }
    public String getClose() {
        return this.close;
    }
    public String getVolume() {
        return this.volume;
    }

    public void setOpen(String open) {
        this.open = open;
    }
    public void setHigh(String high) {
        this.high = high;
    }
    public void setLow(String low) {
        this.low = low;
    }
    public void setClose(String close) {
        this.close = close;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
}
