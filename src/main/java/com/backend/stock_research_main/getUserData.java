package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class getUserData {
    public static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
        final PGSimpleDataSource datasource = new PGSimpleDataSource();
        datasource.setUrl(url);
        return datasource;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/getUserData/{email}")
    public ResponseEntity<String> getAllUserData(@PathVariable String email) {
        final DataSource dataSource = createDataSource();
        String username = "";

        try {
            final Connection conn = dataSource.getConnection();
            PreparedStatement sql = conn.prepareStatement("SELECT username FROM users WHERE email = ?");
            sql.setString(1, email);
            ResultSet returnedRows = sql.executeQuery();
            while (returnedRows.next()) {
                username = returnedRows.getString("username");
            }
        return ResponseEntity.ok(username);

        } catch (Exception e) {
            return ResponseEntity.ok("Error");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/updateUsername/{email}")
    public ResponseEntity<String> updateUsername(@PathVariable String email, @RequestBody String username) {
        final DataSource dataSource = createDataSource();
        try {
            final Connection conn = dataSource.getConnection();
            PreparedStatement sql = conn.prepareStatement("UPDATE users SET username = ? WHERE email = ?");
            sql.setString(1, username);
            sql.setString(2, email);
            sql.executeUpdate();
            return ResponseEntity.ok("Username updated");
        } catch (Exception e) {
            return ResponseEntity.ok("Error");
        }
    }
}
