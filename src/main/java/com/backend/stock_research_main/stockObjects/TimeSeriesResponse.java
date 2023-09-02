package com.backend.stock_research_main.stockObjects;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeSeriesResponse {
    @JsonAlias("Meta Data")
    private Map<String, String> meta_data;
    @JsonProperty("Time Series (5min)")
    @JsonAlias("Time Series (Daily)")
    private Map<String, Map<String, String>> time_series;

    public TimeSeriesResponse() {
    }

    public TimeSeriesResponse(Map<String, Map<String, String>> time_series, Map<String, String> meta_data) {
        this.time_series = time_series;
        this.meta_data = meta_data;
    }

    public Map<String, Map<String, String>> getTime_series() {
        return this.time_series;
    }

    @JsonAnySetter
    public void setTime_series(Map<String, Map<String, String>> time_series) {
        this.time_series = time_series;
    }

    public Map<String, String> getMeta_data() {
        return this.meta_data;
    }

    @JsonAnySetter
    public void setMeta_data(Map<String, String> meta_data) {
        this.meta_data = meta_data;
    }
}
