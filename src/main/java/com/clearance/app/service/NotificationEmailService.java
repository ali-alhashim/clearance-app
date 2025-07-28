package com.clearance.app.service;

import com.clearance.app.model.Approval;
import com.clearance.app.model.Clearance;
import com.clearance.app.repository.ClearanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    ClearanceRepository clearanceRepository;

    public void sendApprovalEmail(List<Approval> approvals, String clearanceCode, String link) {

        List<String> emailList = approvals.stream()
                .map(Approval::getApprovalEmail)
                .distinct()
                .collect(Collectors.toList());


        Clearance clearance = clearanceRepository.findByCode(clearanceCode).orElse(null);
        if(clearance ==null)
        {
            clearance = new Clearance();
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username"))); // Sender email
        message.setTo(emailList.toArray(new String[0]));
        message.setSubject("New Clearance Request - Action Required");
        message.setText("Dear Approver,\n\nA new clearance request (Code: " + clearanceCode + ") is awaiting your approval.\n"
                +"Name:+"+clearance.getName()+" \n"
                +"Badge#:+"+clearance.getBadgeNumber()+" \n"
                + "Please visit the following link to review it:\n\n"
                + link + "\n\nThank you.");

        mailSender.send(message);
    }
}
