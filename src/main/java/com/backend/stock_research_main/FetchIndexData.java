package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.stock_research_main.stockObjects.DailyInfo;
import com.backend.stock_research_main.stockObjects.FullResponse;
import com.backend.stock_research_main.stockObjects.IndexPriceMovement;

@SpringBootApplication
@RestController
public class FetchIndexData {
    public static void main(String[] args) {
        SpringApplication.run(FetchIndexData.class, args);
        fetchAndStoreIndexDailyData();
    }

    public static DataSource createDataSource() {
		final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
	}

    public static void fetchAndStoreIndexDailyData() {
        final DataSource datasource = createDataSource();

        String[][] indexes = {{"SPY", "S&P 500"}, {"DIA", "Dow Jones"}, {"QQQM", "QQQ"}, {"VTWO", "Russel 2000"}, {"NDAQ", "Nasdaq"}};
        Timestamp timeRecordAdded = new Timestamp(System.currentTimeMillis());
        //Get Data for all indexes
        for (String[] index : indexes) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> indexesData = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + index[0] + "&apikey=HGP8743EDTZFQ8HO",
            String.class
            );
            String fullResponse = indexesData.getBody();
            ObjectMapper mapper = new ObjectMapper();

            try {
                FullResponse globalQuote = mapper.readValue(fullResponse, FullResponse.class);

                DailyInfo dailyInfo = new DailyInfo();
                dailyInfo.setTitle(index[1]);
                dailyInfo.setTicker(index[0]);
                dailyInfo.setPrice(Float.parseFloat(globalQuote.getGlobal_quote().getPrice()));
                dailyInfo.setDays_change(globalQuote.getGlobal_quote().getDays_change());
                dailyInfo.setDatetime_added(timeRecordAdded);

                try {
                    Connection conn = datasource.getConnection();
                    PreparedStatement insertDailyInfo = conn.prepareStatement(
                        "INSERT INTO indexes_data (title, ticker, price, percent_change, datetime_added) VALUES (?, ?, ?, ?, ?)"
                    );

                    insertDailyInfo.setString(1, dailyInfo.getTitle());
                    insertDailyInfo.setString(2, dailyInfo.getTicker());
                    insertDailyInfo.setFloat(3, dailyInfo.getPrice());
                    insertDailyInfo.setString(4, dailyInfo.getDays_change());
                    insertDailyInfo.setTimestamp(5, dailyInfo.getDatetime_added());

                    insertDailyInfo.executeUpdate();

                    conn.commit();
                    conn.close();

                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        //Update all data on indexes to be out of date
        
        
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/indexes_daily_data")
    public static IndexPriceMovement getIndexesDailyData() {
        DataSource datasource = createDataSource();
        ArrayList<DailyInfo> indexes_data = new ArrayList<DailyInfo>();

        try {
            Connection conn = datasource.getConnection();
            PreparedStatement getAllIndexes = conn.prepareStatement(
                "SELECT * FROM indexes_data order by datetime_added desc limit 5"
            );
            ResultSet allIndexes = getAllIndexes.executeQuery();
            while (allIndexes.next()) {
                DailyInfo index = new DailyInfo();
                index.setTitle(allIndexes.getString("title"));
                index.setTicker(allIndexes.getString("ticker"));
                index.setPrice(allIndexes.getFloat("price"));
                index.setDays_change(allIndexes.getString("percent_change"));
                index.setDatetime_added(allIndexes.getTimestamp("datetime_added"));
                indexes_data.add(index);
            } 
        } catch (SQLException e) {
            System.out.println(e);
        }

        IndexPriceMovement indexes = new IndexPriceMovement();
        indexes.setDaily_info(indexes_data.toArray(new DailyInfo[indexes_data.size()]));
        return indexes;
    }
}
