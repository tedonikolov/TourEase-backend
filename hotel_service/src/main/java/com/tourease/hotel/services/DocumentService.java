package com.tourease.hotel.services;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.entities.Payment;
import com.tourease.hotel.models.entities.Reservation;
import com.tourease.hotel.models.entities.Worker;
import com.tourease.hotel.repositories.ReservationRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final TemplateEngine templateEngine;
    private final ReservationRepository reservationRepository;
    private final PaymentService paymentService;
    private final WorkerService workerService;

    @Transactional
    public byte[] generateInvoicePdf(HttpServletResponse response, Long userId, Long reservationId, String language) {
        try {
            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new CustomException("Reservation not found", ErrorCode.Failed));
            Payment payment = paymentService.getPaymentByReservationNumber(reservation.getReservationNumber());
            Worker worker = workerService.findById(userId);

            TemplateSpec templateSpec = new TemplateSpec(language.equals("bg") ? "reservationBG" : "reservationEN", TemplateMode.HTML);
            Context context = getContext(reservation, payment, worker, language);
            String invoice = templateEngine.process(templateSpec, context);

            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfDocument pdfDocument;

            //write to response if the return Value is not used
            pdfDocument = new PdfDocument(new PdfWriter(response.getOutputStream()));

            pdfDocument.setDefaultPageSize(PageSize.A4);
            HtmlConverter.convertToPdf(invoice, pdfDocument, new ConverterProperties());
            return pdfOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("Could not generate PDF", ErrorCode.Failed);
        }
    }

    private Context getContext(Reservation reservation, Payment payment, Worker worker, String language) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Context context = new Context(Locale.getDefault());

        context.setVariable("date", LocalDate.now().format(dateFormatter));
        context.setVariable("checkIn", reservation.getCheckIn().format(dateFormatter));
        context.setVariable("checkOut", reservation.getCheckOut().format(dateFormatter));
        context.setVariable("nights", reservation.getNights());
        context.setVariable("people", reservation.getPeopleCount());
        context.setVariable("currency", payment==null ? " " : payment.getHotelCurrency());
        context.setVariable("price", payment==null ? 0 : payment.getHotelPrice().setScale(2, RoundingMode.HALF_UP));

        context.setVariable("room", reservation.getRoom().getName());
        context.setVariable("type", reservation.getType().getName());

        if (language.equals("bg")) {
            context.setVariable("title","РЕЗЕРВАЦИЯ №:" + (reservation.getReservationNumber()));

            switch (reservation.getStatus().name()){
                case "CONFIRMED" -> context.setVariable("status", "ПРИСТИГАЩ");
                case "CANCELED" -> context.setVariable("status", "ОТКАЗАНА");
                case "PENDING" -> context.setVariable("status", "ИЗЧАКВАЩА");
                case "ENDING" -> context.setVariable("status", "НАПУСКАЩ");
            }

            switch (reservation.getMeal().getType().name()) {
                case "BREAKFAST" -> context.setVariable("meal", "Закуска");
                case "HALFBOARD" -> context.setVariable("meal", "Закуска и вечеря");
                case "FULLBOARD" -> context.setVariable("meal", "Закуска, обяд и вечеря");
                case "ALLINCLUSIVE" -> context.setVariable("meal", "Ол инклузив");
                default -> context.setVariable("meal", "Нощувка");
            }
        } else {
            context.setVariable("title","RESERVATION №:" + reservation.getReservationNumber());
            context.setVariable("status", reservation.getStatus());

            switch (reservation.getMeal().getType().name()) {
                case "BREAKFAST" -> context.setVariable("meal", "Breakfast");
                case "HALFBOARD" -> context.setVariable("meal", "Breakfast and dinner");
                case "FULLBOARD" -> context.setVariable("meal", "Breakfast, lunch and dinner");
                case "ALLINCLUSIVE" -> context.setVariable("meal", "All inclusive");
                default -> context.setVariable("meal", "Нощувка");
            }
        }

        context.setVariable("name", reservation.getCustomers().stream().findFirst().get().getFullName());
        context.setVariable("email", reservation.getCustomers().stream().findFirst().get().getEmail());
        context.setVariable("telephone", reservation.getCustomers().stream().findFirst().get().getPhoneNumber());
        context.setVariable("country", reservation.getCustomers().stream().findFirst().get().getCountry());

        BigDecimal totalPrice =  payment==null ? BigDecimal.valueOf(0) : BigDecimal.valueOf((payment.getMealPrice().doubleValue() * reservation.getPeopleCount() + payment.getNightPrice().doubleValue()) * reservation.getNights()).setScale(2, RoundingMode.HALF_UP);
        context.setVariable("totalPrice", totalPrice);
        context.setVariable("discount",  payment==null ? 0 : payment.getDiscount());
        context.setVariable("discountedPrice",  payment==null ? 0 :
                payment.getDiscount().intValue() != 0 ?
                        BigDecimal.valueOf((totalPrice.doubleValue() * (1 - payment.getDiscount().doubleValue() / 100)) - payment.getAdvancedPayment().doubleValue()).setScale(2, RoundingMode.HALF_UP)
                        :
                        BigDecimal.valueOf(totalPrice.doubleValue() - payment.getAdvancedPayment().doubleValue()).setScale(2, RoundingMode.HALF_UP));
        context.setVariable("nightPrice",  payment==null ? 0 : payment.getNightPrice().setScale(2, RoundingMode.HALF_UP));
        context.setVariable("mealPrice",  payment==null ? 0 : BigDecimal.valueOf(payment.getMealPrice().doubleValue() * reservation.getPeopleCount()).setScale(2, RoundingMode.HALF_UP));
        context.setVariable("advancedPayment",  payment==null ? 0 : payment.getAdvancedPayment().setScale(2, RoundingMode.HALF_UP));


        context.setVariable("worker", worker.getFullName());
        context.setVariable("workerPhone", worker.getPhone());
        context.setVariable("hotel", worker.getHotel().getName());
        context.setVariable("address", worker.getHotel().getLocation().getCountry() + ", " + worker.getHotel().getLocation().getCity() + ", " + worker.getHotel().getLocation().getAddress());

        context.setVariable("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        return context;
    }

}
