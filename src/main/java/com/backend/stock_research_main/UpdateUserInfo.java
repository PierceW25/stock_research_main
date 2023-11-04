package com.backend.stock_research_main;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Base64;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class UpdateUserInfo {

    @Autowired
    private SendPasswordRecoveryEmail sendPasswordRecoveryEmail;
    
    private static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
        final PGSimpleDataSource datasource = new PGSimpleDataSource();
        datasource.setUrl(url);
        return datasource;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/updatePasswordRequest")
    public ResponseEntity<String> updatePasswordRequest(@RequestBody String email) {
        final DataSource dataSource = createDataSource();
        try {
            final Connection conn = dataSource.getConnection();
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM USERS WHERE email = ?");
            sql.setString(1, email);
            final ResultSet userSearchResult = sql.executeQuery();
            Boolean userExists = false;
            while (userSearchResult.next()) {
                userExists = true;
            }

            if (userExists) {
                final SecureRandom random = new SecureRandom();
                final Base64.Encoder encoder = Base64.getUrlEncoder();

                byte[] randomBytes = new byte[24];
                random.nextBytes(randomBytes);
                String token = encoder.encodeToString(randomBytes);

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Date originalDate = new Date(timestamp.getTime());

                long tenMinutesInMilliseconds = 600000;
                long newTimeInMilliseconds = originalDate.getTime() + tenMinutesInMilliseconds;
                Date newDate = new Date(newTimeInMilliseconds);
                Timestamp token_expires = new Timestamp(newDate.getTime());

                sql = conn.prepareStatement("INSERT INTO reset_password_values (email, token, expiration_date) VALUES (?, ?, ?)");
                sql.setString(1, email);
                sql.setString(2, token);
                sql.setTimestamp(3, token_expires);
                sql.executeUpdate();

                sendPasswordRecoveryEmail.sendRecoveryEmail(email, token);
                return ResponseEntity.ok("Password recovery email sent");
            } else {
                return ResponseEntity.ok("User does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Error");
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/validateToken")
    public ResponseEntity<String> validateForgotPasswordToken(@RequestBody String token) {
        final DataSource dataSource = createDataSource();

        try {
            final Connection conn = dataSource.getConnection();
            PreparedStatement findTokenSql = conn.prepareStatement("SELECT * FROM reset_password_values where token = ?");
            findTokenSql.setString(1, token);
            final ResultSet foundTokenRow = findTokenSql.executeQuery();
            Boolean tokenExists = false;

            while (foundTokenRow.next()) {
                tokenExists = true;
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                Timestamp expirationTime = foundTokenRow.getTimestamp("expiration_date");

                if (currentTime.before(expirationTime)) {
                    return ResponseEntity.ok("Token is valid");
                } else {
                    return ResponseEntity.ok("Token is expired");
                }
            }

            if (!tokenExists) {
                return ResponseEntity.ok("Token does not exist");
            }

            return ResponseEntity.ok("Something went wrong");
        } catch (Exception e) {
            return ResponseEntity.ok("Error");
        } 
    }
}
