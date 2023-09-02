package com.backend.stock_research_main.stockObjects;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FullResponse {
    @JsonAlias("Global Quote")
    private GlobalQuote global_quote;

    public FullResponse() {
    }

    public FullResponse(GlobalQuote global_quote) {
        this.global_quote = global_quote;
    }

    public GlobalQuote getGlobal_quote() {
        return this.global_quote;
    }

    public void setGlobal_quote(GlobalQuote global_quote) {
        this.global_quote = global_quote;
    }
}
