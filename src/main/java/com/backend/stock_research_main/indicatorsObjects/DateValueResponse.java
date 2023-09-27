package com.backend.stock_research_main.indicatorsObjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class DateValueResponse {
    @JsonAlias("data")
    private QuartersDateValue[] allQuartersGDP;

    public DateValueResponse() {
    }

    public DateValueResponse(QuartersDateValue[] allQuartersGDP) {
        this.allQuartersGDP = allQuartersGDP;
    }

    public QuartersDateValue[] getAllQuartersGDP() {
        return this.allQuartersGDP;
    }

    public void setAllQuartersGDP(QuartersDateValue[] allQuartersGDP) {
        this.allQuartersGDP = allQuartersGDP;
    }
}
