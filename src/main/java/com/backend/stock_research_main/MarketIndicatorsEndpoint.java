package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stock_research_main.indicatorsObjects.DatabaseIndicatorObject;
import com.backend.stock_research_main.indicatorsObjects.IndicatorsContainer;

@RestController
public class MarketIndicatorsEndpoint {
    public static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
        final PGSimpleDataSource datasource = new PGSimpleDataSource();
        datasource.setUrl(url);
        return datasource;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/marketIndicators", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IndicatorsContainer> getMarketIndicators() {
        final DataSource dataSource = createDataSource();
        ArrayList<DatabaseIndicatorObject> indicators = new ArrayList<DatabaseIndicatorObject>();
        try {
            final Connection connection = dataSource.getConnection();
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM market_indicators order by indicator, timeframe asc");
            ResultSet returnedRows = sql.executeQuery();

            while (returnedRows.next()) {
                DatabaseIndicatorObject indicator = new DatabaseIndicatorObject();
                indicator.setIndicator(returnedRows.getString("indicator"));
                indicator.setValue(Float.parseFloat(returnedRows.getString("value")));
                indicator.setColor(returnedRows.getString("color"));
                indicator.setTimeframe(returnedRows.getString("timeframe"));

                if (!indicator.equals(null)) {
                    indicators.add(indicator);
                }
            }

            IndicatorsContainer formattedIndicators = new IndicatorsContainer();
            formattedIndicators.setMonthlyCPI(indicators.get(0));
            formattedIndicators.setAnnualCPI(indicators.get(1));
            formattedIndicators.setFedFundsRate(indicators.get(2));
            formattedIndicators.setQuarterlyGDP(indicators.get(3));
            formattedIndicators.setAnnualGDP(indicators.get(4));
            formattedIndicators.setUnemploymentRate(indicators.get(5));

            if (!formattedIndicators.equals(null)) {
                return new ResponseEntity<>(formattedIndicators, HttpStatus.OK);
            } else {
                System.out.println("No market indicators found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (SQLException e) {
            System.out.println("Error getting market indicators");
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }
}
