package com.tourease.user.services;

import com.tourease.user.models.dto.request.EmailInfoVO;
import com.tourease.user.services.communication.ConfigurationServiceClient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private final ConfigurationServiceClient configurationServiceClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();

        mailSender.setUsername(emailInfoVO.emailFrom());
        mailSender.setPassword(emailInfoVO.emailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }

    private void sendEmail(String toEmail, String subject, String body) {
        JavaMailSender mailSender = getMailSender();
        configurationServiceClient.checkConnectionCore();
        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();

        String senderName = "TourEase";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(emailInfoVO.emailFrom(), senderName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendActivationMail(String email) {
        String subject = "Please verify your registration";
        String body = "Dear "+email+",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "TourEase.";

        //TODO update link with Frontend one
        String verifyURL = "http://localhost:3000/activateProfile?email="+email;

        body = body.replace("[[URL]]", verifyURL);
        sendEmail(email, subject, body);

        kafkaTemplate.send("email_sender", email, "Activation link send!");
    }

    public void sendPassportDateExpiredNotify(String email, String fullName) {
        String subject = "Passport date expired";
        String body = "Dear "+fullName+",<br>"
                + "Your passport has expired<br>"
                + "Please click the link below to log in your profile:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Update passport</a></h3>"
                + "Thank you,<br>"
                + "TourEase.";
        String verifyURL = "http://localhost:3000/profile?passportExpired=1";

        body = body.replace("[[URL]]", verifyURL);
        sendEmail(email, subject, body);

        kafkaTemplate.send("email_sender", email, "Passport update notify send!");
    }

    public void sendPasswordChangeLink(String email) {
        String subject = "Password change";
        String body = "Dear "+email+",<br>"
                + "Please click the link below to change your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Update passport</a></h3>"
                + "Thank you,<br>"
                + "TourEase.";
        String verifyURL = "http://localhost:3000/changePassword?email="+email;

        body = body.replace("[[URL]]", verifyURL);
        sendEmail(email, subject, body);

        kafkaTemplate.send("email_sender", email, "Password change email send!");
    }
}
