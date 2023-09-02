package com.backend.stock_research_main.stockObjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeSeries {
    @JsonProperty("Time Series (5min)")
    @JsonAlias("Time Series (Daily)")
    private PriceInterval[] time_series;

    public TimeSeries() {
    }

    public TimeSeries(PriceInterval[] time_series) {
        this.time_series = time_series;
    }

    public PriceInterval[] getTime_series() {
        return this.time_series;
    }

    public void setTime_series(PriceInterval[] time_series) {
        this.time_series = time_series;
    }
}
