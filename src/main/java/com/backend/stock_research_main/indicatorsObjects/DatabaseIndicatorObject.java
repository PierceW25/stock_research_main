package com.backend.stock_research_main.indicatorsObjects;

import java.sql.Date;

public class DatabaseIndicatorObject {
    private String indicator;
    private Number value;
    private String color;
    private Date date_added;
    private String timeframe;

    public DatabaseIndicatorObject(String indicator, Number value, String color, Date date_added, String timeframe) {
        this.indicator = indicator;
        this.value = value;
        this.color = color;
        this.date_added = date_added;
        this.timeframe = timeframe;
    }

    public DatabaseIndicatorObject() {
    }

    public String getIndicator() {
        return indicator;
    }

    public Number getValue() {
        return value;
    }
    public String getColor() {
        return color;
    }
    public Date getDateAdded() {
        return date_added;
    }
    public String getTimeframe() {
        return timeframe;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }
    public void setValue(Number value) {
        this.value = value;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setDateAdded(Date date_added) {
        this.date_added = date_added;
    }
    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }
}
