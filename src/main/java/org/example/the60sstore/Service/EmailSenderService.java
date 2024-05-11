package org.example.the60sstore.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/* EmailSenderService is the fastest way to send mail in Controller. */
@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    /* To send email, the service needs to create object JavaMailSender. */
    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /* sendEmail method uses 3 param to set To, Subject, Text before sending. */
    public void sendEmail(String toMail, String subject, String content) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("springservice0396@gmail.com");
        mail.setTo(toMail);
        mail.setSubject(subject);
        mail.setText(content);
        javaMailSender.send(mail);
    }
}
