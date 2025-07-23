package com.clearance.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public void sendApprovalEmail(String to, String clearanceCode, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username"))); // Sender email
        message.setTo(to);
        message.setSubject("New Clearance Request - Action Required");
        message.setText("Dear Approver,\n\nA new clearance request (Code: " + clearanceCode + ") is awaiting your approval.\n"
                + "Please visit the following link to review it:\n\n"
                + link + "\n\nThank you.");

        mailSender.send(message);
    }
}
