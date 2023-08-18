package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stock_research_main.articleObjects.ArticleObject;
import com.backend.stock_research_main.articleObjects.ArticlesContainer;
import com.backend.stock_research_main.articleObjects.CustomArticlesContainer;

@RestController
public class FetchArticles {

    public static DataSource createDataSource() {
		final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
	}
    
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/articles/general")
	public static ArticlesContainer getArticles() {
		ArticlesContainer articles = new ArticlesContainer();
        articles.setMacros(getArticlesByCategory("macro"));
        articles.setFiscals(getArticlesByCategory("fiscal"));
        articles.setMonetaries(getArticlesByCategory("monetary"));
        return articles;
	}

    public static LinkedHashSet<ArticleObject> getArticlesByCategory(String articleType) {
        DataSource datasource = createDataSource();
        ArrayList<ArticleObject> fetchedArticles = new ArrayList<ArticleObject>();

        try {
            Connection conn = datasource.getConnection();
            PreparedStatement getAllArticles = conn.prepareStatement(
                "SELECT * FROM all_news_articles WHERE category = ?"
            );
            getAllArticles.setString(1, articleType);
            ResultSet allArticles = getAllArticles.executeQuery();
            while (allArticles.next()) {
                ArticleObject article = new ArticleObject();
                article.setTitle(allArticles.getString("title"));
                article.setUrl(allArticles.getString("url"));
                article.setTime_published(allArticles.getString("time_published"));
                article.setBanner_image(allArticles.getString("banner_image"));
                article.setSource(allArticles.getString("source"));
                article.setCategory(allArticles.getString("category"));
                fetchedArticles.add(article);
            } 
        } catch (SQLException e) {
            System.out.println(e);
        }
        Collections.sort(fetchedArticles, new Comparator<ArticleObject>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

            @Override
            public int compare(ArticleObject o1, ArticleObject o2) {
                try {
                    Date date1 = dateFormat.parse(o1.getTime_published());
                    Date date2 = dateFormat.parse(o2.getTime_published());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        return new LinkedHashSet<ArticleObject>(fetchedArticles);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/articles/custom/{email}")
    public static CustomArticlesContainer getCustomArticles(@PathVariable String email) {
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
        }


        // Getting custom articles from the database and storing them in fetchedArticles
        ArrayList<ArticleObject> fetchedArticles = new ArrayList<ArticleObject>();

        for (String ticker : userTickersSet) {
            try {
                Connection conn = datasource.getConnection();
                PreparedStatement getArticles = conn.prepareStatement(
                    "SELECT * FROM all_news_articles WHERE article_stock = ?"
                );
                getArticles.setString(1, ticker);
                ResultSet articles = getArticles.executeQuery();
                
                while (articles.next()) {
                    ArticleObject article = new ArticleObject();
                    article.setTitle(articles.getString("title"));
                    article.setUrl(articles.getString("url"));
                    article.setTime_published(articles.getString("time_published"));
                    article.setBanner_image(articles.getString("banner_image"));
                    article.setSource(articles.getString("source"));
                    article.setCategory(articles.getString("category"));
                    article.setArticle_stock(articles.getString("article_stock"));
                    article.setRelevance(articles.getFloat("relevance"));
                    fetchedArticles.add(article);
                }
                conn.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        fetchedArticles.sort(Comparator.comparing(ArticleObject::getRelevance).reversed());

        LinkedHashSet<ArticleObject> articlesSet = new LinkedHashSet<ArticleObject>(fetchedArticles);
        CustomArticlesContainer customArticles = new CustomArticlesContainer(articlesSet);

        return customArticles;
    }
}
