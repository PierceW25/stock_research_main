package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.backend.stock_research_main.articleObjects.ArticleObject;
import com.backend.stock_research_main.articleObjects.ArticleResponseObject;
import com.backend.stock_research_main.articleObjects.NewsArticles;

@SpringBootApplication
public class MadeadbApplication {

	private static DataSource createDataSource() {
		final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
	}

	public static ArrayList<ArticleObject> createArticleList() {
		RestTemplate restTemplate = new RestTemplate();

		ArrayList<ArticleObject> localList = new ArrayList<ArticleObject>();
		String[] articleCategories = {"macro", "fiscal", "monetary"};

		for (String category : articleCategories) {
			ResponseEntity<NewsArticles> articleResponse = restTemplate.getForEntity(
				"https://www.alphavantage.co/query?function=NEWS_SENTIMENT&topics=economy_" + category + "&sort=LATEST&limit=50&apikey=HGP8743EDTZFQ8HO",
				NewsArticles.class
			);
			NewsArticles articles = articleResponse.getBody();
			ArrayList<ArticleObject> articleList = new ArrayList<ArticleObject>();

			if (articles != null) {
				ArticleResponseObject[] articleArray = articles.getFeed();
				for (ArticleResponseObject article : articleArray) {
					ArticleObject newArticle = new ArticleObject();
					newArticle.setTitle(article.getTitle());
					newArticle.setUrl(article.getUrl());
					newArticle.setTime_published(article.getTime_published());
					newArticle.setSource(article.getSource());
					newArticle.setBanner_image(article.getBanner_image());
					newArticle.setCategory(category);
					newArticle.setRelevance(Float.parseFloat(article.getOverall_sentiment_score()));
					articleList.add(newArticle);
				}
			}

			//sorting and trimming categories articles by relevance
			articleList.sort(Comparator.comparing(ArticleObject::getRelevance).reversed());
			articleList = new ArrayList<ArticleObject>(articleList.subList(0, 20));

			//adding categories articles to localList
			localList.addAll(articleList);
		}

		return localList;
	}

	public static void addArticlesToDatabase() {
		Set<ArticleObject> articles = new HashSet<>(createArticleList());
		DataSource datasource = createDataSource();
		
		try {
			Connection conn = datasource.getConnection();
			
			PreparedStatement addArticleStat = conn.prepareStatement(
				"INSERT INTO all_news_articles" + 
				"(title, url, time_published, banner_image, source, category, relevance)" +
				"VALUES (?, ?, ?, ?, ?, ?, ?)");
			
			for (ArticleObject article : articles) {
				addArticleStat.setString(1, article.getTitle());
				addArticleStat.setString(2, article.getUrl());
				addArticleStat.setString(3, article.getTime_published());
				addArticleStat.setString(4, article.getBanner_image());
				addArticleStat.setString(5, article.getSource());
				addArticleStat.setString(6, article.getCategory());
				addArticleStat.setFloat(7, article.getRelevance());
				addArticleStat.executeUpdate();
			}
			System.out.println("Articles added to database");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(MadeadbApplication.class, args);
		//System.out.println("Application started");
		//addArticlesToDatabase();
	}

}
