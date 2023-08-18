package com.backend.stock_research_main.articleObjects;

public class TickerSentimentObject {
    private String ticker;
    private String relevance_score;
    private String ticker_sentiment_score;
    private String ticker_sentiment_label;
    

    public TickerSentimentObject(String ticker, String relevance_score, String ticker_sentiment_score, String ticker_sentiment_label) {
        this.ticker = ticker;
        this.relevance_score = relevance_score;
        this.ticker_sentiment_score = ticker_sentiment_score;
        this.ticker_sentiment_label = ticker_sentiment_label;
    }

    public TickerSentimentObject() {}


    public String getTicker() {
        return ticker;
    }
    public String getRelevance_score() {
        return relevance_score;
    }
    public String getTicker_sentiment_score() {
        return ticker_sentiment_score;
    }
    public String getTicker_sentiment_label() {
        return ticker_sentiment_label;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public void setRelevance_score(String relevance_score) {
        this.relevance_score = relevance_score;
    }
    public void setTicker_sentiment_score(String ticker_sentiment_score) {
        this.ticker_sentiment_score = ticker_sentiment_score;
    }
    public void setTicker_sentiment_label(String ticker_sentiment_label) {
        this.ticker_sentiment_label = ticker_sentiment_label;
    }
}
