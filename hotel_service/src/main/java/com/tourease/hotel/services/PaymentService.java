package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.*;
import com.tourease.hotel.models.dto.response.CurrencyRateVO;
import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Payment;
import com.tourease.hotel.models.entities.Worker;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.repositories.CustomerRepository;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.PaymentRepository;
import com.tourease.hotel.repositories.WorkerRepository;
import com.tourease.hotel.services.communication.ConfigServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final HotelRepository hotelRepository;
    private final WorkerRepository workerRepository;
    private final ConfigServiceClient configServiceClient;

    public Payment createPayment(PaymentCreateVO paymentCreateVO, Long workerId) {
        configServiceClient.checkConnection();
        List<CurrencyRateVO> currencyRates = configServiceClient.getCurrencyRates();

        Customer customer = customerRepository.getReferenceById(paymentCreateVO.customerId());
        Hotel hotel = hotelRepository.getReferenceById(paymentCreateVO.hotelId());
        Worker worker = workerId == null ? null : workerRepository.findById(workerId).orElse(null);

        Payment payment = Payment.builder()
                .customer(customer)
                .hotel(hotel)
                .price(paymentCreateVO.price())
                .currency(paymentCreateVO.currency())
                .hotelCurrency(hotel.getCurrency())
                .mealPrice(paymentCreateVO.mealPrice())
                .nightPrice(paymentCreateVO.nightPrice())
                .discount(paymentCreateVO.discount())
                .advancedPayment(paymentCreateVO.advancedPayment())
                .hotelPrice(paymentCreateVO.price().multiply(Currency.getRate(hotel.getCurrency(), paymentCreateVO.currency(), currencyRates)).setScale(2, RoundingMode.HALF_UP))
                .paidFor(paymentCreateVO.paidFor())
                .reservationNumber(paymentCreateVO.reservationNumber())
                .worker(worker)
                .build();

        paymentRepository.save(payment);

        return payment;
    }

    public List<Payment> getAllPaymentsByCustomersForHotel(List<Long> customers, Long hotelId, Long reservationNumber, boolean isPaid) {
        return paymentRepository.findByCustomer_IdInAndHotel_IdAndPaid(customers, hotelId, isPaid, reservationNumber);
    }

    public void markPaymentAsPaid(MarkPaymentVO paymentVO, Long workerId) {
        Payment payment = paymentRepository.findById(paymentVO.id()).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        payment.setPaid(true);
        payment.setWorker(worker);
        payment.setPaymentDate(OffsetDateTime.now());
        payment.setPaymentType(paymentVO.paymentType());
        payment.setPrice(paymentVO.price());
        payment.setCurrency(paymentVO.currency());

        paymentRepository.save(payment);
    }

    public void deletePaymentById(Long paymentId, Long workerId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new IllegalArgumentException("Worker not found"));

        if (worker.getWorkerType().equals(WorkerType.MANAGER) && !payment.isPaid()) {
            paymentRepository.delete(payment);
        } else {
            throw new IllegalArgumentException("You don't have permission to delete this payment");
        }
    }

    public void createNewPayment(NewPaymentVO newPayment, Long userId) {
        Payment payment = createPayment(new PaymentCreateVO(newPayment), userId);
        if (newPayment.paymentType() != null) {
            markPaymentAsPaid(new MarkPaymentVO(payment, newPayment.paymentType()), userId);
        }
    }

    public Payment getPaymentForReservationNumber(Long reservationNumber) {
        return paymentRepository.findNotPaidByReservationNumberAndReservationType(reservationNumber);
    }

    public List<Payment> getPaymentsForReservationNumber(Long reservationNumber) {
        return paymentRepository.findByReservationNumberAndReservationType(reservationNumber);
    }

    public Payment updatePayment(PaymentCreateVO paymentCreateVO, Long userId) {
        Payment payment = paymentRepository.findByReservationNumberAndPaidForAndPaid(paymentCreateVO.reservationNumber(), PaidFor.RESERVATION, false);
        if (payment != null) {
            configServiceClient.checkConnection();
            List<CurrencyRateVO> currencyRates = configServiceClient.getCurrencyRates();

            payment.setPrice(paymentCreateVO.price());
            payment.setCurrency(paymentCreateVO.currency());
            payment.setHotelCurrency(payment.getHotel().getCurrency());
            payment.setHotelPrice(paymentCreateVO.price().multiply(Currency.getRate(payment.getHotel().getCurrency(), paymentCreateVO.currency(), currencyRates)).setScale(2,  RoundingMode.HALF_UP));
            payment.setMealPrice(paymentCreateVO.mealPrice());
            payment.setNightPrice(paymentCreateVO.nightPrice());
            payment.setDiscount(paymentCreateVO.discount());
            payment.setAdvancedPayment(paymentCreateVO.advancedPayment());

            return paymentRepository.save(payment);
        } else {
            return createPayment(paymentCreateVO, userId);
        }
    }
}
