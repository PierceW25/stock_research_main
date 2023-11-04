package com.backend.stock_research_main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendPasswordRecoveryEmail {
    
    @Autowired
    private final JavaMailSender javaMailSender = null;

    @Async
    public void sendRecoveryEmail(String toEmail, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Recovery");
        mailMessage.setText("To reset your password, please click here: "
        + "http://localhost:4200/resetPassword/" + token);
        mailMessage.setFrom("piercewdevs@gmail.com");
        javaMailSender.send(mailMessage);
    }
}
