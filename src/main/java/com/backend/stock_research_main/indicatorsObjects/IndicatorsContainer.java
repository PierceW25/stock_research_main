package com.backend.stock_research_main.indicatorsObjects;

public class IndicatorsContainer {
    private DatabaseIndicatorObject quarterlyGDP;
    private DatabaseIndicatorObject annualGDP;
    private DatabaseIndicatorObject unemploymentRate;
    private DatabaseIndicatorObject fedFundsRate;
    private DatabaseIndicatorObject quarterlyCPI;
    private DatabaseIndicatorObject annualCPI;

    public IndicatorsContainer(DatabaseIndicatorObject quarterlyGDP, DatabaseIndicatorObject annualGDP, DatabaseIndicatorObject unemploymentRate, DatabaseIndicatorObject fedFundsRate, DatabaseIndicatorObject quarterlyCPI, DatabaseIndicatorObject annualCPI) {
        this.quarterlyGDP = quarterlyGDP;
        this.annualGDP = annualGDP;
        this.unemploymentRate = unemploymentRate;
        this.fedFundsRate = fedFundsRate;
        this.quarterlyCPI = quarterlyCPI;
        this.annualCPI = annualCPI;
    }

    public IndicatorsContainer() {
    }

    public DatabaseIndicatorObject getQuarterlyGDP() {
        return quarterlyGDP;
    }
    public DatabaseIndicatorObject getAnnualGDP() {
        return annualGDP;
    }
    public DatabaseIndicatorObject getUnemploymentRate() {
        return unemploymentRate;
    }
    public DatabaseIndicatorObject getFedFundsRate() {
        return fedFundsRate;
    }
    public DatabaseIndicatorObject getQuarterlyCPI() {
        return quarterlyCPI;
    }
    public DatabaseIndicatorObject getAnnualCPI() {
        return annualCPI;
    }

    public void setQuarterlyGDP(DatabaseIndicatorObject quarterlyGDP) {
        this.quarterlyGDP = quarterlyGDP;
    }
    public void setAnnualGDP(DatabaseIndicatorObject annualGDP) {
        this.annualGDP = annualGDP;
    }
    public void setUnemploymentRate(DatabaseIndicatorObject unemploymentRate) {
        this.unemploymentRate = unemploymentRate;
    }
    public void setFedFundsRate(DatabaseIndicatorObject fedFundsRate) {
        this.fedFundsRate = fedFundsRate;
    }
    public void setQuarterlyCPI(DatabaseIndicatorObject quarterlyCPI) {
        this.quarterlyCPI = quarterlyCPI;
    }
    public void setAnnualCPI(DatabaseIndicatorObject annualCPI) {
        this.annualCPI = annualCPI;
    }
}
