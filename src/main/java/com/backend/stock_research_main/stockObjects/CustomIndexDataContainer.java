package com.backend.stock_research_main.stockObjects;

public class CustomIndexDataContainer {
    private String ticker;
    private DailyTimeSeriesItem[] daily_indexes_data;
    private GeneralTimeSeriesItem[] week_indexes_data;
    private GeneralTimeSeriesItem[] month_indexes_data;
    private GeneralTimeSeriesItem[] three_month_indexes_data;
    private GeneralTimeSeriesItem[] one_year_indexes_data;
    private GeneralTimeSeriesItem[] five_year_indexes_data;


    public CustomIndexDataContainer() {
    }

    public CustomIndexDataContainer(String ticker, DailyTimeSeriesItem[] daily_indexes_data, GeneralTimeSeriesItem[] week_indexes_data, GeneralTimeSeriesItem[] month_indexes_data, GeneralTimeSeriesItem[] three_month_indexes_data, GeneralTimeSeriesItem[] one_year_indexes_data, GeneralTimeSeriesItem[] five_year_indexes_data) {
        this.ticker = ticker;
        this.daily_indexes_data = daily_indexes_data;
        this.week_indexes_data = week_indexes_data;
        this.month_indexes_data = month_indexes_data;
        this.three_month_indexes_data = three_month_indexes_data;
        this.one_year_indexes_data = one_year_indexes_data;
        this.five_year_indexes_data = five_year_indexes_data;
    }

    public String getTicker() {
        return this.ticker;
    }
    public DailyTimeSeriesItem[] getDaily_indexes_data() {
        return this.daily_indexes_data;
    }
    public GeneralTimeSeriesItem[] getWeek_indexes_data() {
        return this.week_indexes_data;
    }
    public GeneralTimeSeriesItem[] getMonth_indexes_data() {
        return this.month_indexes_data;
    }
    public GeneralTimeSeriesItem[] getThree_month_indexes_data() {
        return this.three_month_indexes_data;
    }
    public GeneralTimeSeriesItem[] getOne_year_indexes_data() {
        return this.one_year_indexes_data;
    }
    public GeneralTimeSeriesItem[] getFive_year_indexes_data() {
        return this.five_year_indexes_data;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public void setDaily_indexes_data(DailyTimeSeriesItem[] daily_indexes_data) {
        this.daily_indexes_data = daily_indexes_data;
    }
    public void setWeek_indexes_data(GeneralTimeSeriesItem[] week_indexes_data) {
        this.week_indexes_data = week_indexes_data;
    }
    public void setMonth_indexes_data(GeneralTimeSeriesItem[] month_indexes_data) {
        this.month_indexes_data = month_indexes_data;
    }
    public void setThree_month_indexes_data(GeneralTimeSeriesItem[] three_month_indexes_data) {
        this.three_month_indexes_data = three_month_indexes_data;
    }
    public void setOne_year_indexes_data(GeneralTimeSeriesItem[] one_year_indexes_data) {
        this.one_year_indexes_data = one_year_indexes_data;
    }
    public void setFive_year_indexes_data(GeneralTimeSeriesItem[] five_year_indexes_data) {
        this.five_year_indexes_data = five_year_indexes_data;
    }
}
