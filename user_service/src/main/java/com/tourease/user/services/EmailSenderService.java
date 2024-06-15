package com.tourease.user.services;

import com.tourease.user.models.dto.request.EmailInfoVO;
import com.tourease.user.services.communication.AuthenticationServiceClient;
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
    private final AuthenticationServiceClient authenticationServiceClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private JavaMailSender getMailSender(EmailInfoVO emailInfoVO){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(emailInfoVO.emailFrom());
        mailSender.setPassword(emailInfoVO.emailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }

    private void sendEmail(EmailInfoVO emailInfoVO, String toEmail, String subject, String body) {
        JavaMailSender mailSender = getMailSender(emailInfoVO);

        String senderName = "TourEase";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
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
        configurationServiceClient.checkConnection();
        authenticationServiceClient.checkConnection();

        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();
        String token = authenticationServiceClient.generateTokenForEmail(email);

        String subject = "Please verify your registration";
        String body = "Dear "+email+",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "TourEase.";

        String verifyURL = emailInfoVO.activateProfileURL()+token;

        body = body.replace("[[URL]]", verifyURL);
        sendEmail(emailInfoVO, email, subject, body);

        kafkaTemplate.send("email_sender", email, "Activation link send!");
    }

    public void sendPassportDateExpiredNotify(String email, String fullName) {
        configurationServiceClient.checkConnection();

        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();

        String subject = "Passport date expired";
        String body = "Dear "+fullName+",<br>"
                + "Your passport has expired<br>"
                + "Please click the link below to log in your profile:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Update passport</a></h3>"
                + "Thank you,<br>"
                + "TourEase.";
        String verifyURL = emailInfoVO.passportExpiredURL();

        body = body.replace("[[URL]]", verifyURL);
        sendEmail(emailInfoVO, email, subject, body);

        kafkaTemplate.send("email_sender", email, "Passport update notify send!");
    }

    public void sendPasswordChangeLink(String email) {
        configurationServiceClient.checkConnection();
        authenticationServiceClient.checkConnection();
        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();
        String token = authenticationServiceClient.generateTokenForEmail(email);

        String subject = "Password change";
        String body = "Dear "+email+",<br>"
                + "Please click the link below to change your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Change password</a></h3>"
                + "Thank you,<br>"
                + "TourEase.";
        String verifyURL = emailInfoVO.changePasswordURL()+token;

        body = body.replace("[[URL]]", verifyURL);
        sendEmail(emailInfoVO, email, subject, body);

        kafkaTemplate.send("email_sender", email, "Password change email send!");
    }

    public void sendDeclinedReservation(String email, String fullname, Long reservationNumber) {
        configurationServiceClient.checkConnection();
        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();

        String subject = "Declined reservation with number:"+reservationNumber;
        String body = "Dear "+fullname+",<br>"
                + "Your reservation with number:"+reservationNumber+" has been declined.<br>"
                + "Thank you for the understanding,<br>"
                + "TourEase.";

        sendEmail(emailInfoVO, email, subject, body);

        kafkaTemplate.send("email_sender", email, "Reservation declined!");
    }

    public void sendPaymentChangeReservation(PaymentChangeReservationVO paymentChangeReservationVO) {
        configurationServiceClient.checkConnection();
        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();

        String subject = "Payment changed for reservation with number:"+paymentChangeReservationVO.reservationNumber();
        String body = "Dear "+paymentChangeReservationVO.name()+",<br>"
                + "The price of your reservation with number:"+paymentChangeReservationVO.reservationNumber()+" has been changed.<br>"
                + "Old price: "+paymentChangeReservationVO.oldPrice()+" "+paymentChangeReservationVO.currency()+"<br>"
                + "<b>New price: "+paymentChangeReservationVO.newPrice()+" "+paymentChangeReservationVO.currency()+"</b><br>"
                + "Thank you for the understanding,<br>"
                + "TourEase.";

        sendEmail(emailInfoVO, paymentChangeReservationVO.email(), subject, body);

        kafkaTemplate.send("email_sender", paymentChangeReservationVO.email(), "Reservation payment change!");
    }
}
