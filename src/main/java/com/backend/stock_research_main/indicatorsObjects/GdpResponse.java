package com.backend.stock_research_main.indicatorsObjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class GdpResponse {
    @JsonAlias("data")
    private QuartersGdp[] allQuartersGDP;

    public GdpResponse() {
    }

    public GdpResponse(QuartersGdp[] allQuartersGDP) {
        this.allQuartersGDP = allQuartersGDP;
    }

    public QuartersGdp[] getAllQuartersGDP() {
        return this.allQuartersGDP;
    }

    public void setAllQuartersGDP(QuartersGdp[] allQuartersGDP) {
        this.allQuartersGDP = allQuartersGDP;
    }
}
