package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.backend.stock_research_main.stockObjects.CustomIndexDataContainer;
import com.backend.stock_research_main.stockObjects.DailyTimeSeriesItem;
import com.backend.stock_research_main.stockObjects.GeneralTimeSeriesItem;
import com.backend.stock_research_main.stockObjects.HistoricalPriceData;
import com.backend.stock_research_main.stockObjects.TimeSeriesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@RestController
public class TimeSeriesIndexes {

    public static void main(String[] args) {
        SpringApplication.run(TimeSeriesIndexes.class, args);
        updateIntradayData();
        updateTimeSeriesData();
    }

    public static DataSource createDataSource() {
		final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
	}
    
    public static void updateIntradayData() {
        final DataSource dataSource = createDataSource();
        String currentDateTime = java.time.Clock.systemUTC().instant().toString();
        String[] indexes = {"SPY", "DIA", "QQQM", "VTWO", "NDAQ", "AAPL", "MSFT", "GOOG", "AMZN", "NVDA"};

        for (String index : indexes) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> indexesData = restTemplate.getForEntity(
                "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + index + "&interval=5min&outputsize=full&apikey=HGP8743EDTZFQ8HO",
                String.class
            );
            String fullResponse = indexesData.getBody();
            ObjectMapper mapper = new ObjectMapper();
            String dayOfTrading = "-";

            try {
                TimeSeriesResponse fullResponseObject = mapper.readValue(fullResponse, TimeSeriesResponse.class);
                for (String key : fullResponseObject.getTime_series().keySet()) {

                    if (dayOfTrading != "-" && !key.substring(0, 10).equals(dayOfTrading)) {
                        break;
                    }
                    
                    Map<String, String> fullIntervalData = fullResponseObject.getTime_series().get(key);
                    dayOfTrading = key.substring(0, 10);

                    String timestamp = key;
                    String close = fullIntervalData.get("4. close");
                    String ticker = index;

                    try {
                        Connection conn = dataSource.getConnection();
                        conn.prepareStatement(
                            "INSERT INTO index_daily_series_data (trading_datetime, closing_price, ticker, datetime_added)" + 
                            "VALUES ('" + timestamp + "', '" + close + "', '" + ticker + "', '" + currentDateTime + "');"
                        ).execute();

                        conn.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        System.out.println(currentDateTime);

        try {
            Connection conn = dataSource.getConnection();
            conn.prepareStatement(
                "DELETE FROM index_daily_series_data WHERE datetime_added < " + currentDateTime + ";"
            ).execute();

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateTimeSeriesData() {
        final DataSource dataSource = createDataSource();
        String[] indexes = {"SPY", "DIA", "QQQM", "VTWO", "NDAQ", "AAPL", "MSFT", "GOOG", "AMZN", "NVDA"};
        String currentDateTime = java.time.Clock.systemUTC().instant().toString();
        LocalDate currentDate = LocalDate.now();
        LocalDate oneWeekAgo = currentDate.minusWeeks(1);
        LocalDate oneMonthAgo = currentDate.minusMonths(1);
        LocalDate threeMonthsAgo = currentDate.minusMonths(3);
        LocalDate oneYearAgo = currentDate.minusYears(1);
        LocalDate fiveYearsAgo = currentDate.minusYears(5);

        for (String index : indexes) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> indexesData = restTemplate.getForEntity(
                "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + index + "&outputsize=full&apikey=HGP8743EDTZFQ8HO",
                String.class
            ); 
            String fullResponse = indexesData.getBody();
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<HistoricalPriceData> oneWeekSeries = new ArrayList<HistoricalPriceData>();
            ArrayList<HistoricalPriceData> oneMonthSeries = new ArrayList<HistoricalPriceData>();
            ArrayList<HistoricalPriceData> threeMonthSeries = new ArrayList<HistoricalPriceData>();
            ArrayList<HistoricalPriceData> oneYearSeries = new ArrayList<HistoricalPriceData>();
            ArrayList<HistoricalPriceData> fiveYearSeries = new ArrayList<HistoricalPriceData>();

            try {
                TimeSeriesResponse fullResponseObject = mapper.readValue(fullResponse, TimeSeriesResponse.class);
                
                for (String key : fullResponseObject.getTime_series().keySet()) {

                    Map<String, String> fullIntervalData = fullResponseObject.getTime_series().get(key);

                    LocalDate trading_day = LocalDate.parse(key, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String adjusted_close = fullIntervalData.get("5. adjusted close");
                    String ticker = index;

                    HistoricalPriceData generalDataPoint = new HistoricalPriceData(ticker, trading_day, Float.parseFloat(adjusted_close), "placeholder", currentDateTime);

                    if (trading_day.isEqual(oneWeekAgo) || trading_day.isAfter(oneWeekAgo)) {
                        HistoricalPriceData oneWeekDataPoint = generalDataPoint.deepCopy();
                        oneWeekDataPoint.setSeries("1 week");
                        oneWeekSeries.add(oneWeekDataPoint);
                    }

                    if (trading_day.isEqual(oneMonthAgo) || trading_day.isAfter(oneMonthAgo)) {
                        HistoricalPriceData oneMonthDataPoint = generalDataPoint.deepCopy();
                        oneMonthDataPoint.setSeries("1 month");
                        oneMonthSeries.add(oneMonthDataPoint);
                    }

                    if (trading_day.isEqual(threeMonthsAgo) || trading_day.isAfter(threeMonthsAgo)) {
                        HistoricalPriceData threeMonthDataPoint = generalDataPoint.deepCopy();
                        threeMonthDataPoint.setSeries("3 months");
                        threeMonthSeries.add(threeMonthDataPoint);
                    }

                    if (trading_day.isEqual(oneYearAgo) || trading_day.isAfter(oneYearAgo)) {
                        HistoricalPriceData oneYearDataPoint = generalDataPoint.deepCopy();
                        oneYearDataPoint.setSeries("1 year");
                        oneYearSeries.add(oneYearDataPoint);
                    }

                    if (trading_day.isEqual(fiveYearsAgo) || trading_day.isAfter(fiveYearsAgo)) {
                        HistoricalPriceData fiveYearDataPoint = generalDataPoint.deepCopy();
                        fiveYearDataPoint.setSeries("5 years");
                        fiveYearSeries.add(fiveYearDataPoint);
                    }
                }

                //Adding data to database by looping through each arraylist

                try {
                    Connection conn = dataSource.getConnection();
                    for (HistoricalPriceData dataPoint : oneWeekSeries) {
                        conn.prepareStatement(
                            "INSERT INTO index_time_series_data (trading_date, closing_price, ticker, datetime_added, series)" + 
                            "VALUES ('" + dataPoint.getDate() + "', '" + dataPoint.getPrice() + "', '" + dataPoint.getTicker() + "', '" + currentDateTime + "', '" + dataPoint.getSeries() + "');"
                        ).execute();
                    }
                    for (HistoricalPriceData dataPoint : oneMonthSeries) {
                        conn.prepareStatement(
                            "INSERT INTO index_time_series_data (trading_date, closing_price, ticker, datetime_added, series)" + 
                            "VALUES ('" + dataPoint.getDate() + "', '" + dataPoint.getPrice() + "', '" + dataPoint.getTicker() + "', '" + currentDateTime + "', '" + dataPoint.getSeries() + "');"
                        ).execute();
                    }
                    for (HistoricalPriceData dataPoint : threeMonthSeries) {
                        conn.prepareStatement(
                            "INSERT INTO index_time_series_data (trading_date, closing_price, ticker, datetime_added, series)" + 
                            "VALUES ('" + dataPoint.getDate() + "', '" + dataPoint.getPrice() + "', '" + dataPoint.getTicker() + "', '" + currentDateTime + "', '" + dataPoint.getSeries() + "');"
                        ).execute();
                    }
                    for (HistoricalPriceData dataPoint : oneYearSeries) {
                        conn.prepareStatement(
                            "INSERT INTO index_time_series_data (trading_date, closing_price, ticker, datetime_added, series)" + 
                            "VALUES ('" + dataPoint.getDate() + "', '" + dataPoint.getPrice() + "', '" + dataPoint.getTicker() + "', '" + currentDateTime + "', '" + dataPoint.getSeries() + "');"
                        ).execute();
                    }
                    for (HistoricalPriceData dataPoint : fiveYearSeries) {
                        conn.prepareStatement(
                            "INSERT INTO index_time_series_data (trading_date, closing_price, ticker, datetime_added, series)" + 
                            "VALUES ('" + dataPoint.getDate() + "', '" + dataPoint.getPrice() + "', '" + dataPoint.getTicker() + "', '" + currentDateTime + "', '" + dataPoint.getSeries() + "');"
                        ).execute();
                    }

                    System.out.println(currentDateTime);
                    conn.prepareStatement(
                    "DELETE FROM index_time_series_data WHERE datetime_added < " + currentDateTime + ";"
                    ).execute();

                    conn.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/indexes_time_series_data")
    public static CustomIndexDataContainer[] getIndexTimeSeries() {
        final DataSource dataSource = createDataSource();
        String[] indexes = {"SPY", "DIA", "QQQM", "VTWO", "NDAQ"};

        ArrayList<CustomIndexDataContainer> allIndexData = new ArrayList<CustomIndexDataContainer>();

        try {
            Connection conn = dataSource.getConnection();

            for (String index : indexes) {
                ArrayList<DailyTimeSeriesItem> daily_data = new ArrayList<DailyTimeSeriesItem>();
                ArrayList<GeneralTimeSeriesItem> one_week_data = new ArrayList<GeneralTimeSeriesItem>();
                ArrayList<GeneralTimeSeriesItem> one_month_data = new ArrayList<GeneralTimeSeriesItem>();
                ArrayList<GeneralTimeSeriesItem> three_month_data = new ArrayList<GeneralTimeSeriesItem>();
                ArrayList<GeneralTimeSeriesItem> one_year_data = new ArrayList<GeneralTimeSeriesItem>();
                ArrayList<GeneralTimeSeriesItem> five_year_data = new ArrayList<GeneralTimeSeriesItem>();

                PreparedStatement getDailyData = conn.prepareStatement(
                    "SELECT * FROM index_daily_series_data WHERE ticker = ? order by trading_datetime desc"
                );
                getDailyData.setString(1, index);
                ResultSet dailyData = getDailyData.executeQuery();

                while (dailyData.next()) {
                    DailyTimeSeriesItem dataPoint = new DailyTimeSeriesItem();
                    dataPoint.setDate(dailyData.getTimestamp("trading_datetime"));
                    dataPoint.setPrice(dailyData.getFloat("closing_price"));
                    daily_data.add(dataPoint);
                }

                PreparedStatement getSeriesData = conn.prepareStatement(
                    "SELECT * FROM index_time_series_data WHERE ticker = ? order by trading_date desc"
                );
                getSeriesData.setString(1, index);
                ResultSet seriesData = getSeriesData.executeQuery();

                while (seriesData.next()) {
                    GeneralTimeSeriesItem dataPoint = new GeneralTimeSeriesItem();
                    dataPoint.setDate(seriesData.getDate("trading_date"));
                    dataPoint.setPrice(seriesData.getFloat("closing_price"));

                    if (seriesData.getString("series").equals("1 week")) {
                        one_week_data.add(dataPoint);
                    } else if (seriesData.getString("series").equals("1 month")) {
                        one_month_data.add(dataPoint);
                    } else if (seriesData.getString("series").equals("3 months")) {
                        three_month_data.add(dataPoint);
                    } else if (seriesData.getString("series").equals("1 year")) {
                        one_year_data.add(dataPoint);
                    } else if (seriesData.getString("series").equals("5 years")) {
                        five_year_data.add(dataPoint);
                    } else {
                        System.out.println("Time series table is structurally incorrect");
                    }
                }

                CustomIndexDataContainer indexDataContainer = new CustomIndexDataContainer();
                indexDataContainer.setTicker(index);
                indexDataContainer.setDaily_indexes_data(daily_data.toArray(new DailyTimeSeriesItem[daily_data.size()]));
                indexDataContainer.setWeek_indexes_data(one_week_data.toArray(new GeneralTimeSeriesItem[one_week_data.size()]));
                indexDataContainer.setMonth_indexes_data(one_month_data.toArray(new GeneralTimeSeriesItem[one_month_data.size()]));
                indexDataContainer.setThree_month_indexes_data(three_month_data.toArray(new GeneralTimeSeriesItem[three_month_data.size()]));
                indexDataContainer.setOne_year_indexes_data(one_year_data.toArray(new GeneralTimeSeriesItem[one_year_data.size()]));
                indexDataContainer.setFive_year_indexes_data(five_year_data.toArray(new GeneralTimeSeriesItem[five_year_data.size()]));

                allIndexData.add(indexDataContainer);
            }

            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return allIndexData.toArray(new CustomIndexDataContainer[allIndexData.size()]);
    }
}
