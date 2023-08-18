package com.backend.stock_research_main.articleObjects;

public class ArticleObject {
    private String title;
    private String url;
    private String time_published;
    private String banner_image;
    private String source;
    private String category;
    private String article_stock;
    private Float relevance;

    @Override
    public String toString() {
        return "ArticleObject{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", time_published='" + time_published + '\'' +
                ", source='" + source + '\'' +
                ", banner_image='" + banner_image + '\'' +
                ", category='" + category + '\'' +
                ", relevance='" + relevance + '\'' +
                ", article_stock='" + article_stock + '\'' +
                '}';
    }

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
    public String getCategory() {
        return category;
    }
    public Float getRelevance() {
        return relevance;
    }
    public String getArticle_stock() {
        return article_stock;
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
    public void setCategory(String category) {
        this.category = category;
    }
    public void setRelevance(Float relevance) {
        this.relevance = relevance;
    }
    public void setArticle_stock(String article_stock) {
        this.article_stock = article_stock;
    }
}
