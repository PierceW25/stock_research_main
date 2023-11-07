package com.backend.stock_research_main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendEmailChangeEmail {
    
    @Autowired
    private final JavaMailSender javaMailSender = null;

    @Async
    public void sendRecoveryEmail(String toEmail, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Change Email");
        mailMessage.setText("To change your email, please click here: "
        + "http://localhost:4200/changeEmail/" + token);
        mailMessage.setFrom("piercewdevs@gmail.com");
        javaMailSender.send(mailMessage);
    }
}
