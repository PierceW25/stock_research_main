package com.backend.stock_research_main.articleObjects;

public class ArticleResponseObject {
    private String title;
    private String url;
    private String time_published;
    private String banner_image;
    private String source;
    private String overall_sentiment_score;
    private TickerSentimentObject[] ticker_sentiment;


    public ArticleResponseObject(String title, String url, String time_published, String banner_image, String source, String overall_sentiment_score, TickerSentimentObject[] ticker_sentiment) {
        this.title = title;
        this.url = url;
        this.time_published = time_published;
        this.banner_image = banner_image;
        this.source = source;
        this.overall_sentiment_score = overall_sentiment_score;
        this.ticker_sentiment = ticker_sentiment;
    }
    public ArticleResponseObject() {}
    

    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getTime_published() {
        return time_published;
    }
    public String getSource() {
        return source;
    }
    public String getBanner_image() {
        return banner_image;
    }
    public String getOverall_sentiment_score() {
        return overall_sentiment_score;
    }
    public TickerSentimentObject[] getTicker_sentiment() {
        return ticker_sentiment;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setTime_published(String time_published) {
        this.time_published = time_published;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
    public void setOverall_sentiment_score(String overall_sentiment_score) {
        this.overall_sentiment_score = overall_sentiment_score;
    }
    public void setTicker_sentiment(TickerSentimentObject[] ticker_sentiment) {
        this.ticker_sentiment = ticker_sentiment;
    }
}
