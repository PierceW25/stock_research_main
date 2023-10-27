package com.backend.stock_research_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stock_research_main.registrationObjects.receivedUser;

@RestController
public class LoginUser {

    @Bean
    public PasswordEncoder passwordEncoderMatcher() {
        return new BCryptPasswordEncoder();
    }

    private static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/stock_research?user=postgres&password=KylerFNFL2025!";
		final PGSimpleDataSource datasource = new PGSimpleDataSource();
		datasource.setUrl(url);
		return datasource;
    }


    //Analyze the functionality of this ai generated function
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginUser(@RequestBody receivedUser user) {
        final DataSource dataSource = createDataSource();
        try {
            final Connection connection = dataSource.getConnection();
            PreparedStatement sql = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            sql.setString(1, user.getEmail());
            ResultSet returnedRows = sql.executeQuery();
            if (returnedRows.next()) {
                if (passwordEncoderMatcher().matches(user.getPassword(), returnedRows.getString("password"))) {
                    return new ResponseEntity<String>("User logged in", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("Incorrect password", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<String>("Email does not exist", HttpStatus.OK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
}
