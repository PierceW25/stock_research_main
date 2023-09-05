package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stock_research_main.watchlistObjects.WatchlistsContainer;

@RestController
public class WatchlistServices {
    
    private static DataSource createDataSource() {
		final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
	}

    //GET METHODS FOR ALL WATCHLISTS

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/userWatchlists/{email}")
    public ResponseEntity<WatchlistsContainer> getAllWatchlistData(@PathVariable String email) {
        final DataSource datasource = createDataSource();
        WatchlistsContainer watchlistsContainer = new WatchlistsContainer();
        try {
            final Connection connection = datasource.getConnection();
            final ResultSet returnedRows = connection
            .prepareStatement("SELECT * FROM userWatchlists WHERE email = '" + email + "';")
            .executeQuery();
            while (returnedRows.next()) {
                watchlistsContainer.setWatchlist_one_title(returnedRows.getString("watchlist_one_title"));
                watchlistsContainer.setWatchlist_two_title(returnedRows.getString("watchlist_two_title"));
                watchlistsContainer.setWatchlist_three_title(returnedRows.getString("watchlist_three_title"));
                watchlistsContainer.setSelected_watchlist(returnedRows.getString("selected_list"));
                
                String[] watchlist_one = returnedRows.getString("watchlist_one")
                .replaceAll("\\{|\\}", "")
                .split("\\s*,\\s*");
                String watchlist_two[] = returnedRows.getString("watchlist_two")
                .replaceAll("\\{|\\}", "")
                .split("\\s*,\\s*");
                String[] watchlist_three = returnedRows.getString("watchlist_three")
                .replaceAll("\\{|\\}", "")
                .split("\\s*,\\s*");

                List<String> watchlist_one_array = new ArrayList<>();
                List<String> watchlist_two_array = new ArrayList<>();
                List<String> watchlist_three_array = new ArrayList<>();

                for (String item : watchlist_one) {
                    String trimmedItem = item.trim();
                    if (!trimmedItem.equals("")) {
                        watchlist_one_array.add(trimmedItem);
                    }
                }

                for (String item : watchlist_two) {
                    String trimmedItem = item.trim();
                    if (!trimmedItem.equals("")) {
                        watchlist_two_array.add(trimmedItem);
                    }
                }

                for (String item : watchlist_three) {
                    String trimmedItem = item.trim();
                    if (!trimmedItem.equals("")) {
                        watchlist_three_array.add(trimmedItem);
                    }
                }

                watchlistsContainer.setWatchlist_one(watchlist_one_array);
                watchlistsContainer.setWatchlist_two(watchlist_two_array);
                watchlistsContainer.setWatchlist_three(watchlist_three_array);
            }

            System.out.println(watchlistsContainer.getWatchlist_one());
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseEntity<>(watchlistsContainer, HttpStatus.OK);
    }

    //POST METHODS FOR ALL WATCHLISTS

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/userWatchlists/primary/{email}")
    public String updatePrimaryWatchlist(@PathVariable String email, @RequestBody String watchlist) {
        final DataSource datasource = createDataSource();
        String[] items = watchlist.split(",");
        ArrayList<String> itemsList = new ArrayList<>();
        for (String item : items) {
            itemsList.add(item.trim());
        }
        System.out.println(itemsList);
        String newWatchlist = itemsList.toString().replace("[", "{").replace("]", "}");
        System.out.println(newWatchlist);
        try {
            final Connection connection = datasource.getConnection();
            connection.
            prepareStatement("UPDATE userWatchlists SET watchlist_one = '" + newWatchlist + "' WHERE email = '" + email + "';")
            .execute();
            return "Watchlist updated";
        } catch (Exception e) {
            System.out.println(e);
            return "Watchlist not updated";
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/userWatchlists/secondary/{email}")
    public String updateSecondaryWatchlist(@PathVariable String email, @RequestBody String watchlist) {
        final DataSource datasource = createDataSource();
        String[] items = watchlist.split(",");
        ArrayList<String> itemsList = new ArrayList<>();
        for (String item : items) {
            itemsList.add(item.trim());
        }
        String newWatchlist = itemsList.toString().replace("[", "{").replace("]", "}");

        try {
            final Connection connection = datasource.getConnection();
            connection.
            prepareStatement("UPDATE userWatchlists SET watchlist_two = '" + newWatchlist + "' WHERE email = '" + email + "';")
            .execute();
            return "Watchlist updated";
        } catch (Exception e) {
            System.out.println(e);
            return "Watchlist not updated";
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/userWatchlists/tertiary/{email}")
    public String updateTertiaryWatchlist(@PathVariable String email, @RequestBody String watchlist) {
        final DataSource datasource = createDataSource();
        String[] items = watchlist.split(",");
        ArrayList<String> itemsList = new ArrayList<>();
        for (String item : items) {
            itemsList.add(item.trim());
        }
        String newWatchlist = itemsList.toString().replace("[", "{").replace("]", "}");

        try {
            final Connection connection = datasource.getConnection();
            connection.
            prepareStatement("UPDATE userWatchlists SET watchlist_three = '" + newWatchlist + "' WHERE email = '" + email + "';")
            .execute();
            return "Watchlist updated";
        } catch (Exception e) {
            System.out.println(e);
            return "Watchlist not updated";
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/userWatchlists/selectedWatchlist/{email}")
    public ResponseEntity<String> changeSelectedWatchlist(@PathVariable String email, @RequestBody String selected_list) {
        final DataSource datasource = createDataSource();
        try {
            final Connection connection = datasource.getConnection();
            connection.
            prepareStatement("UPDATE userWatchlists SET selected_list = '" + selected_list + "' WHERE email = '" + email + "';")
            .execute();
            return new ResponseEntity<>("Watchlist updated", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Watchlist not updated", HttpStatus.BAD_REQUEST);
        }
    }



}
