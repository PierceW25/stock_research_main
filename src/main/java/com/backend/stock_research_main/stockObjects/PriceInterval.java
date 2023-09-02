package com.backend.stock_research_main.stockObjects;

public class PriceInterval {
    private IntervalData date_time_interval;

    public PriceInterval() {
    }

    public PriceInterval(IntervalData date_time_interval) {
        this.date_time_interval = date_time_interval;
    }

    public IntervalData getDate_time_interval() {
        return this.date_time_interval;
    }

    public void setDate_time_interval(IntervalData date_time_interval) {
        this.date_time_interval = date_time_interval;
    }

}
