package com.backend.stock_research_main;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stock_research_main.registrationObjects.receivedUser;

@RestController
public class registerUser {
    
    private static DataSource createDataSource() {
		final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody receivedUser user) {
        final DataSource dataSource = createDataSource();
        final String hashedPassword = passwordEncoder().encode(user.getPassword());
        try {
        final Connection connection = dataSource.getConnection();
        PreparedStatement sql = connection.prepareStatement("INSERT INTO users (username, email, password, user_level) VALUES (?, ?, ?, ?)");
        sql.setString(1, user.getUsername());
        sql.setString(2, user.getEmail());
        sql.setString(3, hashedPassword);
        sql.setString(4, user.getUserLevel());    
        sql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();    
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path= "/userWatchlists")
    public ResponseEntity<String> createUserWatchlists(@RequestBody receivedUser user) {
        final DataSource dataSource = createDataSource();
        try {
            final Connection connection = dataSource.getConnection();
            String[] watchlistOne = {"AAPL", "MSFT"};
            String[] watchlistGeneral = {};
            Array watchlistOneArr = connection.createArrayOf("VARCHAR", watchlistOne);
            Array array = connection.createArrayOf("VARCHAR", watchlistGeneral);
            PreparedStatement sql = connection
            .prepareStatement("INSERT INTO userWatchlists (email, watchlist_one_title, watchlist_one, watchlist_two_title, watchlist_two, watchlist_three_title, watchlist_three, selected_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            sql.setString(1, user.getEmail());
            sql.setString(2, "Primary");
            sql.setArray(3, watchlistOneArr);
            sql.setString(4, "Secondary");
            sql.setArray(5, array);
            sql.setString(6, "Tertiary");
            sql.setArray(7, array);
            sql.setString(8, "Primary");
            sql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/users/{email}")
    public Boolean checkUserExists(@PathVariable String email) {
        final DataSource dataSource = createDataSource();
        ResultSet usersRetrieved = null;
        try {
            final Connection connection = dataSource.getConnection();
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            sql.setString(1, email);
            usersRetrieved = sql.executeQuery();
            if (usersRetrieved.next()) {
            return true;
            } else {
            return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

}
