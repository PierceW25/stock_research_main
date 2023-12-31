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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stock_research_main.registrationObjects.TokenAndEmailUserData;
import com.backend.stock_research_main.registrationObjects.UpdatePasswordData;
import com.backend.stock_research_main.registrationObjects.ValidationMessageAndEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@RestController
public class UpdateUserInfo {

    @Autowired
    private SendPasswordRecoveryEmail sendPasswordRecoveryEmail = new SendPasswordRecoveryEmail();

    @Autowired
    private SendEmailChangeEmail sendEmailChangeEmail = new SendEmailChangeEmail();

    @Autowired
    private SendDeleteAccountEmail sendDeleteAccountEmail = new SendDeleteAccountEmail();

    @Bean
    public PasswordEncoder passwordEncoder2() {
        return new BCryptPasswordEncoder();
    }
    
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
            ResultSet userSearchResult = sql.executeQuery();
            boolean userExists = false;
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
                return ResponseEntity.ok("Recovery email sent,#00C805");
            } else {
                return ResponseEntity.ok("No account with that email,#c83f00");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Try again later,#c83f00");
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/deleteAccountRequest")
    public ResponseEntity<String> deleteAccountRequest(@RequestBody String email) {
        final DataSource dataSource = createDataSource();

        try {
            final Connection conn = dataSource.getConnection();
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM USERS WHERE email = ?");
            sql.setString(1, email);
            ResultSet userSearchResult = sql.executeQuery();
            boolean userExists = false;
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

                sendDeleteAccountEmail.sendRecoveryEmail(email, token);
                return ResponseEntity.ok("User validation email sent,#00C805");
            } else {
                return ResponseEntity.ok("Try again later,#c83f00");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Try again later,#c83f00");
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/updateEmailRequest")
    public ResponseEntity<String> updateEmailRequest(@RequestBody String email) {
        final DataSource dataSource = createDataSource();
        try {
            final Connection conn = dataSource.getConnection();
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM USERS WHERE email = ?");
            sql.setString(1, email);
            ResultSet userSearchResult = sql.executeQuery();
            boolean userExists = false;
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

                sendEmailChangeEmail.sendRecoveryEmail(email, token);
                return ResponseEntity.ok("Recovery email sent,#00C805");
            } else {
                return ResponseEntity.ok("No account with that email,#c83f00");
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok("Try again later,#c83f00");
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/validateToken")
    public ResponseEntity<ValidationMessageAndEmail> validateRecoveryToken(@RequestBody String token) {
        final DataSource dataSource = createDataSource();
        ValidationMessageAndEmail validationObject = new ValidationMessageAndEmail();

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
                String userEmail = foundTokenRow.getString("email");
                validationObject.setEmail(userEmail);

                if (currentTime.before(expirationTime)) {
                    validationObject.setTokenMessage("Token is valid");
                    return ResponseEntity.ok(validationObject);
                } else {
                    validationObject.setTokenMessage("Token is expired");
                    return ResponseEntity.ok(validationObject);
                }
            }

            if (!tokenExists) {
                validationObject.setTokenMessage("Token does not exist");
                return ResponseEntity.ok(validationObject);
            }

            validationObject.setTokenMessage("Something went wrong");
            return ResponseEntity.ok(validationObject);
        } catch (Exception e) {
            validationObject.setTokenMessage("Error");
            return ResponseEntity.ok(validationObject);
        } 
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordData newPasswordData) {

        final DataSource datasource = createDataSource();
        final String hashedPassword = passwordEncoder2().encode(newPasswordData.getPassword());
        String userEmail = "";
        Boolean tokenValidated = false;

        try {
            final Connection conn = datasource.getConnection();
            PreparedStatement findTokenSql = conn.prepareStatement("SELECT * FROM reset_password_values where token = ?");
            findTokenSql.setString(1, newPasswordData.getToken());
            final ResultSet foundTokenRow = findTokenSql.executeQuery();
            Boolean tokenExists = false;

            while (foundTokenRow.next()) {
                tokenExists = true;
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                Timestamp expirationTime = foundTokenRow.getTimestamp("expiration_date");

                if (currentTime.before(expirationTime)) {
                    tokenValidated = true;
                    userEmail = foundTokenRow.getString("email");
                } else {
                    return ResponseEntity.ok("Token is expired,#c83f00");
                }
            }

            if (!tokenExists) {
                return ResponseEntity.ok("Token does not exist,#c83f00");
            }

            if (tokenValidated) {
                PreparedStatement replacePassword = conn.prepareStatement("UPDATE users set password = ? where email = ?");
                replacePassword.setString(1, hashedPassword);
                replacePassword.setString(2, userEmail);
                replacePassword.executeUpdate();

                return ResponseEntity.ok("Password updated,#00C805");
            } else {
                return ResponseEntity.ok("Failed to update password,#c83f00");
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Try again later,#c83f00");
        }
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/changeEmail")
    public ResponseEntity<String> updateEmail(@RequestBody TokenAndEmailUserData newEmailData) {

        final DataSource datasource = createDataSource();
        String userEmail = "";
        Boolean tokenValidated = false;

        try {
            final Connection conn = datasource.getConnection();
            PreparedStatement findTokenSql = conn.prepareStatement("SELECT * FROM reset_password_values where token = ?");
            findTokenSql.setString(1, newEmailData.getToken());
            final ResultSet foundTokenRow = findTokenSql.executeQuery();
            Boolean tokenExists = false;

            while (foundTokenRow.next()) {
                tokenExists = true;
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                Timestamp expirationTime = foundTokenRow.getTimestamp("expiration_date");

                if (currentTime.before(expirationTime)) {
                    tokenValidated = true;
                    userEmail = foundTokenRow.getString("email");
                } else {
                    return ResponseEntity.ok("Token is expired,#c83f00");
                }
            }

            if (!tokenExists) {
                return ResponseEntity.ok("Token does not exist,#c83f00");
            }

            if (tokenValidated) {
                PreparedStatement checkEmail = conn.prepareStatement("select * from users where email = ?");
                checkEmail.setString(1,newEmailData.getEmail());
                ResultSet emailSearch = checkEmail.executeQuery();

                while (emailSearch.next()) {
                    return ResponseEntity.ok("Email is already connected to an account,#c83f00");
                }

                PreparedStatement replaceEmail = conn.prepareStatement("UPDATE users set email = ? where email = ?");
                replaceEmail.setString(1, newEmailData.getEmail());
                replaceEmail.setString(2, userEmail);
                replaceEmail.executeUpdate();

                replaceEmail = conn.prepareStatement("UPDATE userWatchlists set email = ? where email = ?");
                replaceEmail.setString(1, newEmailData.getEmail());
                replaceEmail.setString(2, userEmail);
                replaceEmail.executeUpdate();

                return ResponseEntity.ok("Email updated,#00C805");
            } else {
                return ResponseEntity.ok("Failed to update Email,#c83f00");
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok("Try again later,#c83f00");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestBody String token) {
        final DataSource datasource = createDataSource();
        String userEmail = "";
        Boolean tokenValidated = false;

        try {
            final Connection conn = datasource.getConnection();
            PreparedStatement findToken = conn.prepareStatement("Select * from reset_password_values where token = ?");
            findToken.setString(1, token);
            ResultSet tokenSearch = findToken.executeQuery();
            Boolean tokenExists = false;

            while (tokenSearch.next()) {
                tokenExists = true;
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                Timestamp expirationTime = tokenSearch.getTimestamp("expiration_date");

                if (currentTime.before(expirationTime)) {
                    tokenValidated = true;
                    userEmail = tokenSearch.getString("email");
                } else {
                    return ResponseEntity.ok("Token is expired,#c83f00");
                }
            }

            if (!tokenExists) {
                return ResponseEntity.ok("Token does not exist,#c83f00");
            }

            if (tokenValidated) {
                PreparedStatement deleteUser = conn.prepareStatement("Delete from users where email = ?");
                PreparedStatement deleteUserWatchlists = conn.prepareStatement("Delete from userWatchlists where email = ?");
                deleteUser.setString(1, userEmail);
                deleteUserWatchlists.setString(1, userEmail);

                deleteUser.executeUpdate();
                deleteUserWatchlists.executeUpdate();

                PreparedStatement deleteToken = conn.prepareStatement("delete from reset_password_values where token = ?");
                deleteToken.setString(1, token);
                deleteToken.executeUpdate();

                return ResponseEntity.ok("Your account has been deleted,#00C805");
            } else {
                return ResponseEntity.ok("Please try again later,#c83f00");
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok("Please try again later,#c83f00");
        }
    }
}
