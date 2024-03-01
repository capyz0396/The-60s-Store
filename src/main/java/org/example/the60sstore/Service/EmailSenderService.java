package org.example.testspring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String toMail, String subject, String content) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("springservice0396@gmail.com");
        mail.setTo(toMail);
        mail.setSubject(subject);
        mail.setText(content);
        javaMailSender.send(mail);

        System.out.println("Mail is sent successfully!");
    }
}
