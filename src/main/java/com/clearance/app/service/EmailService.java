package com.clearance.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public int emailService(String toEmail,String otp)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username"))); // Sender email
            message.setTo(toEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your One-Time Password (OTP) is: " + otp + "\n\nIt is valid for 5 minutes.");

            mailSender.send(message);
            System.out.println("otp has been sent to email: "+toEmail);
            return 200;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 500;
        }
    }
}
