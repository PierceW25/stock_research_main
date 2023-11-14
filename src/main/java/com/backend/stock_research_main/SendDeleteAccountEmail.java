package com.backend.stock_research_main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendDeleteAccountEmail {
    
    @Autowired
    private final JavaMailSender javaMailSender = null;

    @Async
    public void sendRecoveryEmail(String toEmail, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Delete Account");
        mailMessage.setText("To delete your account, visit this page: "
        + "http://localhost:4200/deleteAccount/" + token);
        mailMessage.setFrom("piercewdevs@gmail.com");
        javaMailSender.send(mailMessage);
    }
}
