package com.backend.stock_research_main.articleObjects;

import java.util.LinkedHashSet;

public class ArticlesContainer {
    private LinkedHashSet<ArticleObject> macros;
    private LinkedHashSet<ArticleObject> fiscals;
    private LinkedHashSet<ArticleObject> monetaries;

    public ArticlesContainer(
    LinkedHashSet<ArticleObject> macros, 
    LinkedHashSet<ArticleObject> fiscals, 
    LinkedHashSet<ArticleObject> monetaries) {
        this.macros = macros;
        this.fiscals = fiscals;
        this.monetaries = monetaries;
    }

    public ArticlesContainer() {}

    public LinkedHashSet<ArticleObject> getMacros() {
        return macros;
    }

    public LinkedHashSet<ArticleObject> getFiscals() {
        return fiscals;
    }

    public LinkedHashSet<ArticleObject> getMonetaries() {
        return monetaries;
    }

    public void setMacros(LinkedHashSet<ArticleObject> macros) {
        this.macros = macros;
    }

    public void setFiscals(LinkedHashSet<ArticleObject> fiscals) {
        this.fiscals = fiscals;
    }

    public void setMonetaries(LinkedHashSet<ArticleObject> monetaries) {
        this.monetaries = monetaries;
    }
}
