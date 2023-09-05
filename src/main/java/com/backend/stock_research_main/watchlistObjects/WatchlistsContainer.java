package com.backend.stock_research_main.watchlistObjects;

import java.util.List;

public class WatchlistsContainer {
    private String watchlist_one_title;
    private String watchlist_two_title;
    private String watchlist_three_title;
    private List<String> watchlist_one;
    private List<String> watchlist_two;
    private List<String> watchlist_three;
    private String selected_watchlist;


    public WatchlistsContainer(String watchlist_one_title, String watchlist_two_title, String watchlist_three_title, List<String> watchlist_one, List<String> watchlist_two, List<String> watchlist_three, String selected_watchlist) {
        this.watchlist_one_title = watchlist_one_title;
        this.watchlist_two_title = watchlist_two_title;
        this.watchlist_three_title = watchlist_three_title;
        this.watchlist_one = watchlist_one;
        this.watchlist_two = watchlist_two;
        this.watchlist_three = watchlist_three;
        this.selected_watchlist = selected_watchlist;
    }

    public WatchlistsContainer() {
    }

    public String getWatchlist_one_title() {
        return watchlist_one_title;
    }

    public void setWatchlist_one_title(String watchlist_one_title) {
        this.watchlist_one_title = watchlist_one_title;
    }

    public String getWatchlist_two_title() {
        return watchlist_two_title;
    }

    public void setWatchlist_two_title(String watchlist_two_title) {
        this.watchlist_two_title = watchlist_two_title;
    }

    public String getWatchlist_three_title() {
        return watchlist_three_title;
    }

    public void setWatchlist_three_title(String watchlist_three_title) {
        this.watchlist_three_title = watchlist_three_title;
    }

    public List<String> getWatchlist_one() {
        return watchlist_one;
    }

    public void setWatchlist_one(List<String> array) {
        this.watchlist_one = array;
    }

    public List<String> getWatchlist_two() {
        return watchlist_two;
    }

    public void setWatchlist_two(List<String> watchlist_two) {
        this.watchlist_two = watchlist_two;
    }

    public List<String> getWatchlist_three() {
        return watchlist_three;
    }

    public void setWatchlist_three(List<String> watchlist_three) {
        this.watchlist_three = watchlist_three;
    }
    
    public String getSelected_watchlist() {
        return selected_watchlist;
    }

    public void setSelected_watchlist(String selected_watchlist) {
        this.selected_watchlist = selected_watchlist;
    }
}
