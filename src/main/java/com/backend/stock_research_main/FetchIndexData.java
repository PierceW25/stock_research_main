package com.backend.stock_research_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FetchIndexData {
    public static void main(String[] args) {
        SpringApplication.run(FetchIndexData.class, args);
        fetchIndexDailyData();
    }

    public static void fetchIndexDailyData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> indexesData = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=SPY&interval=5min&apikey=HGP8743EDTZFQ8HO",
            String.class
        );
        String body = indexesData.getBody();
        System.out.println(body);
    }
}
