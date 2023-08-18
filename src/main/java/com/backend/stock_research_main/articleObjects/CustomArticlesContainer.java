package com.backend.stock_research_main.articleObjects;

import java.util.LinkedHashSet;

public class CustomArticlesContainer {
    private LinkedHashSet<ArticleObject> customArticles;

    public CustomArticlesContainer(LinkedHashSet<ArticleObject> customArticles) {
        this.customArticles = customArticles;
    }

    public CustomArticlesContainer() {}

    public LinkedHashSet<ArticleObject> getCustomArticles() {
        return customArticles;
    }

    public void setCustomArticles(LinkedHashSet<ArticleObject> customArticles) {
        this.customArticles = customArticles;
    }
}
