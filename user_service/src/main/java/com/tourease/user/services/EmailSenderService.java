package com.tourease.user.services;

import com.tourease.user.models.dto.request.EmailInfoVO;
import com.tourease.user.models.dto.request.PaymentChangeReservationVO;
import com.tourease.user.models.dto.request.ReservationConfirmationVO;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private final ConfigurationServiceClient configurationServiceClient;
    private final AuthenticationServiceClient authenticationServiceClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TemplateEngine templateEngine;

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

    public void sendReservationConfirmation(ReservationConfirmationVO reservationConfirmationVO) {
        configurationServiceClient.checkConnection();
        EmailInfoVO emailInfoVO = configurationServiceClient.getEmailInfo();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        TemplateSpec templateSpec = new TemplateSpec(reservationConfirmationVO.country()!=null && reservationConfirmationVO.country().equals("Bulgaria") ? "reservationBG" : "reservationEN", TemplateMode.HTML);

        Context context = new Context();
        context.setVariable("checkIn", reservationConfirmationVO.reservationVO().checkIn().format(dateFormatter));
        context.setVariable("checkOut", reservationConfirmationVO.reservationVO().checkOut().format(dateFormatter));
        context.setVariable("nights", reservationConfirmationVO.reservationVO().nights());
        context.setVariable("people", reservationConfirmationVO.reservationVO().people());
        context.setVariable("currency", reservationConfirmationVO.reservationVO().currency());
        context.setVariable("price", reservationConfirmationVO.reservationVO().price().setScale(2, RoundingMode.HALF_UP));

        context.setVariable("type", reservationConfirmationVO.reservationVO().roomType());

        if (reservationConfirmationVO.country()!=null && reservationConfirmationVO.country().equals("Bulgaria")) {
            context.setVariable("title","РЕЗЕРВАЦИЯ №:" + (reservationConfirmationVO.reservationVO().reservationNumber()));

            switch (reservationConfirmationVO.reservationVO().meal()) {
                case "BREAKFAST" -> context.setVariable("meal", "Закуска");
                case "HALFBOARD" -> context.setVariable("meal", "Закуска и вечеря");
                case "FULLBOARD" -> context.setVariable("meal", "Закуска, обяд и вечеря");
                case "ALLINCLUSIVE" -> context.setVariable("meal", "Ол инклузив");
                default -> context.setVariable("meal", "Нощувка");
            }
        } else {
            context.setVariable("title","RESERVATION №:" + reservationConfirmationVO.reservationVO().reservationNumber());

            switch (reservationConfirmationVO.reservationVO().meal()) {
                case "BREAKFAST" -> context.setVariable("meal", "Breakfast");
                case "HALFBOARD" -> context.setVariable("meal", "Breakfast and dinner");
                case "FULLBOARD" -> context.setVariable("meal", "Breakfast, lunch and dinner");
                case "ALLINCLUSIVE" -> context.setVariable("meal", "All inclusive");
                default -> context.setVariable("meal", "Нощувка");
            }
        }

        context.setVariable("name", reservationConfirmationVO.reservationVO().hotelName());
        context.setVariable("country", reservationConfirmationVO.reservationVO().hotelCountry());
        context.setVariable("city", reservationConfirmationVO.reservationVO().hotelCity());
        context.setVariable("address", reservationConfirmationVO.reservationVO().hotelAddress());

        context.setVariable("workerName", reservationConfirmationVO.reservationVO().workerName());
        context.setVariable("workerEmail", reservationConfirmationVO.reservationVO().workerEmail());
        context.setVariable("workerPhone", reservationConfirmationVO.reservationVO().workerPhone());

        context.setVariable("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        String body = templateEngine.process(templateSpec, context);

        String subject = "Reservation confirmation with number:"+reservationConfirmationVO.reservationVO().reservationNumber();

        sendEmail(emailInfoVO, reservationConfirmationVO.email(), subject, body);

        kafkaTemplate.send("email_sender", reservationConfirmationVO.email(), "Reservation confirmation email send!");
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
