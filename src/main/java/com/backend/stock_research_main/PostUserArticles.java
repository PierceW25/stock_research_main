package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.backend.stock_research_main.articleObjects.ArticleObject;
import com.backend.stock_research_main.articleObjects.ArticleResponseObject;
import com.backend.stock_research_main.articleObjects.NewsArticles;
import com.backend.stock_research_main.articleObjects.TickerSentimentObject;

@RestController
public class PostUserArticles {
    
    /*
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/articles/custom/{email}")
    public static ResponseEntity<String> postUserArticles(@PathVariable String email) {
        
        DataSource datasource = createDataSource();

        //Global function variables
        String userTickers = "";
        Timestamp lastUpdated = null;
        ArrayList<String> userTickersList = new ArrayList<String>();
        ArrayList<String> userArticleStocks = new ArrayList<String>();
        ArrayList<String> userStocksTrimmed = new ArrayList<String>();

        //Getting user's tickers
        try {
        Connection conn = datasource.getConnection();
        PreparedStatement getTickers = conn.prepareStatement(
        "SELECT watchlist_one, watchlist_two, watchlist_three from userWatchlists where email = ?");
        getTickers.setString(1, email);
        ResultSet tickerRows = getTickers.executeQuery();

        while (tickerRows.next()) {
            userTickers = tickerRows.getString("watchlist_one")
            .replace("{", "").replace("}", "")
             + "," + tickerRows.getString("watchlist_two")
             .replace("{", "").replace("}", "")
             + "," + tickerRows.getString("watchlist_three")
             .replace("{", "").replace("}", "");
        }
        userTickers = userTickers.replaceAll(",*$", "");
        userTickersList = new ArrayList<String>(Set.of(userTickers.split(",")));
        
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error getting user's tickers");
        }

        //Getting stocks already used to get articles in db
        try {
            Connection conn = datasource.getConnection();
            PreparedStatement getArticlesStocks = conn.prepareStatement(
                "SELECT DISTINCT article_stock, datetime_added FROM news_articles WHERE email = ?");
            getArticlesStocks.setString(1, email);
            ResultSet articleStocks = getArticlesStocks.executeQuery();
            while (articleStocks.next()) {
                userArticleStocks.add(articleStocks.getString("article_stock"));

                //Add logic to check if lastUpdate is earlier than articleStocks.getTimestamp("datetime_added")
                //if so then leave lastUpdate as is, otherwise set it to articleStocks.getTimestamp("datetime_added")
                lastUpdated = articleStocks.getTimestamp("datetime_added");
            }
            System.out.println(userArticleStocks);
            userStocksTrimmed = userTickersList;
            userStocksTrimmed.removeAll(userArticleStocks);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error getting articles already in database");
        }

        //Checking which tickers should be included in the article get request
        LocalDateTime lastUpdatedLocal = null;
        if (lastUpdated != null) {
            lastUpdatedLocal = lastUpdated.toLocalDateTime();
        } else {
            lastUpdatedLocal = LocalDateTime.now();
        }

        if (lastUpdatedLocal.isBefore(LocalDateTime.now().minusHours(4)) ) {
            userTickersList = userStocksTrimmed;
        } else {
            return ResponseEntity.ok("Articles already up to date");
        }

        //Getting news articles
        for (String ticker : userTickersList) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<NewsArticles> articleResponse = restTemplate.getForEntity(
		    "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=" + 
            ticker + 
            "&limit=50&apikey=HGP8743EDTZFQ8HO",
		    NewsArticles.class);
            NewsArticles articles = articleResponse.getBody();
            ArticleResponseObject[] articleArray = articles.getFeed();


            ArrayList<ArticleObject> articleObjects = new ArrayList<ArticleObject>();
            for (ArticleResponseObject article : articleArray) {
                ArticleObject articleObject = new ArticleObject();
                articleObject.setTitle(article.getTitle());
                articleObject.setUrl(article.getUrl());
                articleObject.setTime_published(article.getTime_published());
                articleObject.setBanner_image(article.getBanner_image());
                articleObject.setSource(article.getSource());
                articleObject.setEmail(email);
                articleObject.setArticle_stock(ticker);

                TickerSentimentObject[] tickerSentimentArray = article.getTicker_sentiment();
                for (TickerSentimentObject tickerSentiment : tickerSentimentArray) {
                    if (tickerSentiment.getTicker() == ticker) {
                        articleObject.setRelevance(tickerSentiment.getRelevance_score());
                    }
                }
                articleObjects.add(articleObject);
            }

            try {
            Connection conn = datasource.getConnection();
            PreparedStatement addArticleStat = conn.prepareStatement(
            "INSERT INTO news_articles (title, url, time_published, banner_image," + 
            "source, category, email, relevance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            if (articles != null) {
                ArticleResponseObject[] articleArray = articles.getFeed();
                for (ArticleObject article : articleArray) {
                addArticleStat.setString(1, article.getTitle());
                addArticleStat.setString(2, article.getUrl());
                addArticleStat.setString(3, article.getTime_published());
                addArticleStat.setString(4, article.getSummary());
                addArticleStat.setString(5, article.getBanner_image());
                addArticleStat.setString(6, article.getSource());
                addArticleStat.setString(7, article.getCategory());
                addArticleStat.setString(8, email);
                addArticleStat.executeUpdate();
                }

            }
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.ok("Error adding articles to database");
            }
        }
        return ResponseEntity.ok("Articles added to database");
    } */

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/articles/update/{email}")
    public static ResponseEntity<String> updateArticles(@PathVariable String email) {
        final DataSource datasource = createDataSource();

        //Getting users watchlists and storing distinct tickers in userTickersList
        LinkedHashSet<String> userTickersSet = new LinkedHashSet<String>();

        try {
            Connection conn = datasource.getConnection();
            PreparedStatement getTickers = conn.prepareStatement(
            "SELECT watchlist_one, watchlist_two, watchlist_three from userWatchlists where email = ?");
            getTickers.setString(1, email);
            ResultSet tickerRows = getTickers.executeQuery();
            while (tickerRows.next()) {
                String userTickers = tickerRows.getString("watchlist_one")
                .replace("{", "").replace("}", "")
                + "," + tickerRows.getString("watchlist_two")
                .replace("{", "").replace("}", "")
                + "," + tickerRows.getString("watchlist_three")
                .replace("{", "").replace("}", "");

                // the below line removes trailing commas, which occur when a user has an empty watchlist
                userTickers = userTickers.replaceAll(",*$", "");

                // Creates a distinct list of tickers using set and split then adds them to the userTickersList
                String userTickersArray[] = userTickers.split(",");
                for (String ticker : userTickersArray) {
                    userTickersSet.add(ticker);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error getting user's watchlists/tickers");
        }


        //Getting stocks already used to generate custom articles in db
        final ArrayList<String> customArticleStocks = new ArrayList<String>();

        try {
            Connection conn = datasource.getConnection();
            PreparedStatement getArticlesStocks = conn.prepareStatement(
            "SELECT DISTINCT article_stock FROM all_news_articles WHERE category = ?");
            getArticlesStocks.setString(1, "custom");
            ResultSet articleStocks = getArticlesStocks.executeQuery();

            while (articleStocks.next()) {
                customArticleStocks.add(articleStocks.getString("article_stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error getting articles already in database");
        }


        //Trimming userTickersList to only include tickers not already used to generate articles for them
        final ArrayList<String> userStocksTrimmed = new ArrayList<String>(userTickersSet);
        userStocksTrimmed.removeAll(customArticleStocks);


        final ArrayList<ArticleObject> articleObjects = new ArrayList<ArticleObject>();
        //Looping through tickers that need articles fetched for them
        for (String stock : userStocksTrimmed) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<NewsArticles> articleResponse = restTemplate.getForEntity(
		    "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=" + 
            stock +
            "&limit=50&apikey=HGP8743EDTZFQ8HO",
		    NewsArticles.class);
            NewsArticles articles = articleResponse.getBody();
            
            if (articles != null) {
                ArticleResponseObject[] articleArray = articles.getFeed();

                if (articleArray == null) {
                    System.out.println("no articles for " + stock);
                    continue;
                } else {
                    for (ArticleResponseObject article : articleArray) {
                        ArticleObject articleObject = new ArticleObject();
                        articleObject.setTitle(article.getTitle());
                        articleObject.setUrl(article.getUrl());
                        articleObject.setTime_published(article.getTime_published());
                        articleObject.setBanner_image(article.getBanner_image());
                        articleObject.setSource(article.getSource());
                        articleObject.setCategory("custom");

                        TickerSentimentObject[] tickerSentimentArray = article.getTicker_sentiment();
                        for (TickerSentimentObject tickerSentiment : tickerSentimentArray) {
                            if (tickerSentiment.getTicker().equals(stock.toUpperCase())) {
                                articleObject.setRelevance(Float.parseFloat(tickerSentiment.getRelevance_score()));
                                break;
                            } else {
                                articleObject.setRelevance(0.0f);
                            }
                        }
                        articleObjects.add(articleObject);
                    }
                }
                if (articleObjects.isEmpty()) {
                    System.out.println("no articles for " + stock);
                    continue;
                } else {
                    articleObjects.sort(Comparator.comparing(ArticleObject::getRelevance));
                }
            }

            final ArrayList<ArticleObject> sortedArticles = new ArrayList<ArticleObject>(articleObjects.subList(0, 5));


            try {
            Connection conn = datasource.getConnection();
            PreparedStatement addArticleStat = conn.prepareStatement(
            "INSERT INTO all_news_articles (title, url, time_published, banner_image," + 
            "source, category, relevance, article_stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            if (articleObjects != null) {
                for (ArticleObject article : sortedArticles) {
                addArticleStat.setString(1, article.getTitle());
                addArticleStat.setString(2, article.getUrl());
                addArticleStat.setString(3, article.getTime_published());
                addArticleStat.setString(4, article.getBanner_image());
                addArticleStat.setString(5, article.getSource());
                addArticleStat.setString(6, article.getCategory());
                addArticleStat.setFloat(7, article.getRelevance());
                addArticleStat.setString(8, stock);
                addArticleStat.executeUpdate();
                }

            }
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.ok("Error adding articles to database");
            }
        }

        return ResponseEntity.ok("Articles added to database");
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/articles/stocks")
    public static ResponseEntity<String> updateCustomArticles(@RequestBody String newStock) {
        System.out.println(newStock);

        final DataSource datasource = createDataSource();

        //Getting stocks already used to generate custom articles in db
        final ArrayList<String> customArticleStocks = new ArrayList<String>();
        final ArrayList<ArticleObject> articleObjects = new ArrayList<ArticleObject>();

        try {
            Connection conn = datasource.getConnection();
            PreparedStatement getArticlesStocks = conn.prepareStatement(
            "SELECT DISTINCT article_stock FROM all_news_articles WHERE category = ?");
            getArticlesStocks.setString(1, "custom");
            ResultSet articleStocks = getArticlesStocks.executeQuery();

            while (articleStocks.next()) {
                customArticleStocks.add(articleStocks.getString("article_stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error getting articles already in database");
        }

        if (customArticleStocks.contains(newStock)) {
            return ResponseEntity.ok("Stocks articles already in database");
        } else {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<NewsArticles> articleResponse = restTemplate.getForEntity(
            "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=" +
            newStock +
            "&limit=50&apikey=HGP8743EDTZFQ8HO",
            NewsArticles.class);
            NewsArticles articles = articleResponse.getBody();

            if (articles != null) {
                ArticleResponseObject[] articleArray = articles.getFeed();

                if (articleArray == null) {
                    System.out.println("no articles for " + newStock);
                    return ResponseEntity.ok("No articles for " + newStock);
                } else {
                    for (ArticleResponseObject article : articleArray) {
                        ArticleObject articleObject = new ArticleObject();
                        articleObject.setTitle(article.getTitle());
                        articleObject.setUrl(article.getUrl());
                        articleObject.setTime_published(article.getTime_published());
                        articleObject.setBanner_image(article.getBanner_image());
                        articleObject.setSource(article.getSource());
                        articleObject.setCategory("custom");

                        TickerSentimentObject[] tickerSentimentArray = article.getTicker_sentiment();
                        for (TickerSentimentObject tickerSentiment : tickerSentimentArray) {
                            if (tickerSentiment.getTicker().equals(newStock.toUpperCase())) {
                                articleObject.setRelevance(Float.parseFloat(tickerSentiment.getRelevance_score()));
                                break;
                            } else {
                                articleObject.setRelevance(0.0f);
                            }
                        }
                        articleObjects.add(articleObject);
                    }
                }
                if (articleObjects.isEmpty()) {
                    System.out.println("no articles for " + newStock);
                    return ResponseEntity.ok("No articles for " + newStock);
                } else {
                    articleObjects.sort(Comparator.comparing(ArticleObject::getRelevance));
                }
            }

            final ArrayList<ArticleObject> sortedArticles = new ArrayList<ArticleObject>(articleObjects.subList(0, 5));


            try {
            Connection conn = datasource.getConnection();
            PreparedStatement addArticleStat = conn.prepareStatement(
            "INSERT INTO all_news_articles (title, url, time_published, banner_image," + 
            "source, category, relevance, article_stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            if (articleObjects != null) {
                for (ArticleObject article : sortedArticles) {
                addArticleStat.setString(1, article.getTitle());
                addArticleStat.setString(2, article.getUrl());
                addArticleStat.setString(3, article.getTime_published());
                addArticleStat.setString(4, article.getBanner_image());
                addArticleStat.setString(5, article.getSource());
                addArticleStat.setString(6, article.getCategory());
                addArticleStat.setFloat(7, article.getRelevance());
                addArticleStat.setString(8, newStock);
                addArticleStat.executeUpdate();
                }

            }
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.ok("Error adding articles to database");
            }
        }

        return ResponseEntity.ok("Articles added to database");

    }

    private static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
    }
}
