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
import com.backend.stock_research_main.indicatorsObjects.GdpResponse;
import com.backend.stock_research_main.indicatorsObjects.QuartersGdp;
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
        ResponseEntity<String> realGdpData = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=REAL_GDP&interval=quarterly&apikey=HGP8743EDTZFQ8HO",
            String.class
        );
        String fullGdpResponse = realGdpData.getBody();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            GdpResponse gdpResponse = mapper.readValue(fullGdpResponse, GdpResponse.class);
            QuartersGdp[] allQuartersGDP = gdpResponse.getAllQuartersGDP();

            QuartersGdp mostRecentQuarter = allQuartersGDP[0];
            QuartersGdp secondMostRecentQuarter = allQuartersGDP[1];
            QuartersGdp oneYearAgosQuarter = allQuartersGDP[4];

            double QuarterlyChange = (Float.parseFloat(mostRecentQuarter.getValue()) / Float.parseFloat(secondMostRecentQuarter.getValue()) - 1) * 100;
            double YearlyChange = (Float.parseFloat(mostRecentQuarter.getValue()) / Float.parseFloat(oneYearAgosQuarter.getValue()) - 1) * 100;

            DatabaseIndicatorObject quarterlyGdp;
            if (QuarterlyChange > 2.00) {
                quarterlyGdp = new DatabaseIndicatorObject("GDP", QuarterlyChange, "green", dateRecordAdded, "quarterly");
            } else if (QuarterlyChange < 2.00 && QuarterlyChange > 0.00) {
                quarterlyGdp = new DatabaseIndicatorObject("GDP", QuarterlyChange, "yellow", dateRecordAdded, "quarterly");
            } else {
                quarterlyGdp = new DatabaseIndicatorObject("GDP", QuarterlyChange, "red", dateRecordAdded, "quarterly");
            }

            DatabaseIndicatorObject yearlyGdp;
            if (YearlyChange > 2.00) {
                yearlyGdp = new DatabaseIndicatorObject("GDP", YearlyChange, "green", dateRecordAdded, "yearly");
            } else if (YearlyChange < 2.00 && YearlyChange > 0.00) {
                yearlyGdp = new DatabaseIndicatorObject("GDP", YearlyChange, "yellow", dateRecordAdded, "yearly");
            } else {
                yearlyGdp = new DatabaseIndicatorObject("GDP", YearlyChange, "red", dateRecordAdded, "yearly");
            }

            allIndicators.add(quarterlyGdp);
            allIndicators.add(yearlyGdp);

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
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
