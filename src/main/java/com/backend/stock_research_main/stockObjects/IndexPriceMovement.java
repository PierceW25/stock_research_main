package com.backend.stock_research_main.stockObjects;

public class IndexPriceMovement {
    private DailyInfo[] daily_info;

    public IndexPriceMovement() {
    }

    public IndexPriceMovement(DailyInfo[] daily_info) {
        this.daily_info = daily_info;
    }

    public DailyInfo[] getDaily_info() {
        return this.daily_info;
    }

    public void setDaily_info(DailyInfo[] daily_info) {
        this.daily_info = daily_info;
    }
}
