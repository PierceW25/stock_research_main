package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.backend.stock_research_main.indicatorsObjects.DatabaseIndicatorObject;
import com.backend.stock_research_main.indicatorsObjects.DateValueResponse;
import com.backend.stock_research_main.indicatorsObjects.QuartersDateValue;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class FetchMarketIndicators {
    
    public static void main(String[] args) {
        SpringApplication.run(FetchMarketIndicators.class, args);
        fetchAndStoreMarketIndicators();
    }

    public static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
        final PGSimpleDataSource datasource = new PGSimpleDataSource();
        datasource.setUrl(url);
        return datasource;
    }

    public static void fetchAndStoreMarketIndicators() {
        final DataSource datasource = createDataSource();
        Date dateRecordAdded = new Date(System.currentTimeMillis());
        ArrayList<DatabaseIndicatorObject> allIndicators = new ArrayList<DatabaseIndicatorObject>();

        RestTemplate restTemplate = new RestTemplate();

        //Getting gdp data and storing it in market_indicators table
        ResponseEntity<String> realGdpData = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=REAL_GDP&interval=quarterly&apikey=HGP8743EDTZFQ8HO",
            String.class
        );
        String fullGdpResponse = realGdpData.getBody();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            DateValueResponse gdpResponse = mapper.readValue(fullGdpResponse, DateValueResponse.class);
            QuartersDateValue[] allQuartersGDP = gdpResponse.getAllQuartersGDP();

            QuartersDateValue mostRecentQuarter = allQuartersGDP[0];
            QuartersDateValue secondMostRecentQuarter = allQuartersGDP[1];
            QuartersDateValue oneYearAgosQuarter = allQuartersGDP[4];

            double QuarterlyChange = Math.round(((Float.parseFloat(mostRecentQuarter.getValue()) / Float.parseFloat(secondMostRecentQuarter.getValue()) - 1) * 100) * 100.0) / 100.0;
            double YearlyChange = Math.round(((Float.parseFloat(mostRecentQuarter.getValue()) / Float.parseFloat(oneYearAgosQuarter.getValue()) - 1) * 100) * 100.0) / 100.0;

            DatabaseIndicatorObject quarterlyGdp;
            if (QuarterlyChange > 2.00) {
                quarterlyGdp = new DatabaseIndicatorObject("GDP - Quarter's Change", QuarterlyChange, "lightgreen", dateRecordAdded, "quarterly");
            } else if (QuarterlyChange < 2.00 && QuarterlyChange > 0.00) {
                quarterlyGdp = new DatabaseIndicatorObject("GDP - Quarter's Change", QuarterlyChange, "#ffc000", dateRecordAdded, "quarterly");
            } else {
                quarterlyGdp = new DatabaseIndicatorObject("GDP - Quarter's Change", QuarterlyChange, "lightcoral", dateRecordAdded, "quarterly");
            }

            DatabaseIndicatorObject yearlyGdp;
            if (YearlyChange > 2.00) {
                yearlyGdp = new DatabaseIndicatorObject("GDP - Annual Change", YearlyChange, "lightgreen", dateRecordAdded, "yearly");
            } else if (YearlyChange < 2.00 && YearlyChange > 0.00) {
                yearlyGdp = new DatabaseIndicatorObject("GDP - Annual Change", YearlyChange, "#ffc000", dateRecordAdded, "yearly");
            } else {
                yearlyGdp = new DatabaseIndicatorObject("GDP - Annual Change", YearlyChange, "lightcoral", dateRecordAdded, "yearly");
            }

            allIndicators.add(quarterlyGdp);
            allIndicators.add(yearlyGdp);
        } catch (Exception e) {
            System.out.println(e);
        }


        //Getting inflation data and storing it in market_indicators table
        ResponseEntity<String> cpiData = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=CPI&interval=monthly&apikey=HGP8743EDTZFQ8HO",
            String.class
        );
        String fullCpiResponse = cpiData.getBody();
        ObjectMapper mapper2 = new ObjectMapper();

        try {
            DateValueResponse cpiResponse = mapper2.readValue(fullCpiResponse, DateValueResponse.class);
            QuartersDateValue[] allCpiQuarters = cpiResponse.getAllQuartersGDP();

            QuartersDateValue mostRecentCpi = allCpiQuarters[0];
            QuartersDateValue secondMostRecentCpi = allCpiQuarters[1];
            QuartersDateValue oneYearAgosCpi = allCpiQuarters[12];

            double monthlyChange = Math.round(((Float.parseFloat(mostRecentCpi.getValue()) / Float.parseFloat(secondMostRecentCpi.getValue()) - 1) * 100) * 100.0) / 100.0 ;
            double yearlyChange = Math.round(((Float.parseFloat(mostRecentCpi.getValue()) / Float.parseFloat(oneYearAgosCpi.getValue()) - 1) * 100) * 100.0) / 100.0;

            DatabaseIndicatorObject monthlyCpi;
            if (monthlyChange > 0.00) {
                monthlyCpi = new DatabaseIndicatorObject("CPI - Month's Change", monthlyChange, "lightcoral", dateRecordAdded, "monthly");
            } else {
                monthlyCpi = new DatabaseIndicatorObject("CPI - Month's Change", monthlyChange, "lightgreen", dateRecordAdded, "monthly");
            }

            DatabaseIndicatorObject yearlyCpi;
            if (yearlyChange > 3.00) {
                yearlyCpi = new DatabaseIndicatorObject("CPI - Annual Change", yearlyChange, "lightcoral", dateRecordAdded, "yearly");
            } else if (yearlyChange <= 3.00 && yearlyChange > 2.00) {
                yearlyCpi = new DatabaseIndicatorObject("CPI - Annual Change", yearlyChange, "#ffc000", dateRecordAdded, "yearly");
            } else {
                yearlyCpi = new DatabaseIndicatorObject("CPI - Annual Change", yearlyChange, "lightgreen", dateRecordAdded, "yearly");
            }

            allIndicators.add(monthlyCpi);
            allIndicators.add(yearlyCpi);
        } catch (Exception e) {
            System.out.println(e);
        }

        //Getting unemployment rate
        ResponseEntity<String> unemploymentData = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=UNEMPLOYMENT&interval=monthly&apikey=HGP8743EDTZFQ8HO",
            String.class
        );
        String fullUnemploymentResponse = unemploymentData.getBody();
        ObjectMapper mapper3 = new ObjectMapper();

        try {
            DateValueResponse unemploymentResponse = mapper3.readValue(fullUnemploymentResponse, DateValueResponse.class);
            QuartersDateValue[] allUnemploymentQuarters = unemploymentResponse.getAllQuartersGDP();

            double mostRecentUnemployment = Math.round((Float.parseFloat(allUnemploymentQuarters[0].getValue())) * 100.0) / 100.0;

            DatabaseIndicatorObject unemployment;
            if (mostRecentUnemployment < 3.00 || mostRecentUnemployment > 7.00) {
                unemployment = new DatabaseIndicatorObject("Unemployment Rate", mostRecentUnemployment, "lightcoral", dateRecordAdded, "monthly");
            } else if (mostRecentUnemployment < 4.00 || mostRecentUnemployment > 6.00) {
                unemployment = new DatabaseIndicatorObject("Unemployment Rate", mostRecentUnemployment, "#ffc000", dateRecordAdded, "monthly");
            } else {
                unemployment = new DatabaseIndicatorObject("Unemployment Rate", mostRecentUnemployment, "lightgreen", dateRecordAdded, "monthly");
            }
            
            allIndicators.add(unemployment);
        } catch (Exception e) {
            System.out.println(e);
        }

        //Getting fed funds rate data
        ResponseEntity<String> fedFundsRateDate = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=FEDERAL_FUNDS_RATE&interval=monthly&apikey=HGP8743EDTZFQ8HO",
            String.class
        );
        String fullFedFundsRateResponse = fedFundsRateDate.getBody();
        ObjectMapper mapper4 = new ObjectMapper();

        try {
            DateValueResponse fedFundsRateResponse = mapper4.readValue(fullFedFundsRateResponse, DateValueResponse.class);
            QuartersDateValue[] allFedFundsRateQuarters = fedFundsRateResponse.getAllQuartersGDP();
            double lastSevenYearsSum = 0.00;
            double lastSevenYearsAvg = 0.00;

            for (int i = 0; i < 84; i++) {
                lastSevenYearsSum += Float.parseFloat(allFedFundsRateQuarters[i].getValue());
            }

            lastSevenYearsAvg = lastSevenYearsSum / 84;

            DatabaseIndicatorObject fedFundsRate;
            double currentFedFundsRate = Math.round((Float.parseFloat(allFedFundsRateQuarters[0].getValue())) * 100.0) / 100.0;
            double previousFedFundsRate = Float.parseFloat(allFedFundsRateQuarters[1].getValue());

            if (currentFedFundsRate > lastSevenYearsAvg) {
                fedFundsRate = new DatabaseIndicatorObject("Fed Funds Rate", currentFedFundsRate, "lightcoral", dateRecordAdded, "monthly");
            } else if (currentFedFundsRate > previousFedFundsRate + .1) {
                fedFundsRate = new DatabaseIndicatorObject("Fed Funds Rate", currentFedFundsRate, "#ffc000", dateRecordAdded, "monthly");
            } else {
                fedFundsRate = new DatabaseIndicatorObject("Fed Funds Rate", currentFedFundsRate, "lightgreen", dateRecordAdded, "monthly");
            }

            allIndicators.add(fedFundsRate);

        } catch (Exception e) {
            System.out.println(e);
        }

        for (DatabaseIndicatorObject indicator : allIndicators) {
            try {
                final Connection connection = datasource.getConnection();
                PreparedStatement sql = connection.prepareStatement("INSERT INTO market_indicators (indicator, value, color, date_added, timeframe) VALUES (?, ?, ?, ?, ?)");
                sql.setString(1, indicator.getIndicator());
                sql.setDouble(2, indicator.getValue().doubleValue());
                sql.setString(3, indicator.getColor());
                sql.setDate(4, indicator.getDateAdded());
                sql.setString(5, indicator.getTimeframe());
                sql.executeUpdate();
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
