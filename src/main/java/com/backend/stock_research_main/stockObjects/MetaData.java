package com.backend.stock_research_main.stockObjects;

import com.fasterxml.jackson.annotation.JsonSetter;

public class MetaData {
    @JsonSetter("1. Information")
    private String information;
    @JsonSetter("2. Symbol")
    private String symbol;
    @JsonSetter("3. Last Refreshed")
    private String lastRefreshed;
    @JsonSetter("4. Interval")
    private String interval;
    @JsonSetter("5. Output Size")
    private String outputSize;
    @JsonSetter("6. Time Zone")
    private String timeZone;
}
