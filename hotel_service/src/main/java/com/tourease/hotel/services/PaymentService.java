package com.tourease.hotel.services;

import com.tourease.hotel.models.dto.requests.*;
import com.tourease.hotel.models.entities.Customer;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Payment;
import com.tourease.hotel.models.entities.Worker;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.WorkerType;
import com.tourease.hotel.repositories.CustomerRepository;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.PaymentRepository;
import com.tourease.hotel.repositories.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final HotelRepository hotelRepository;
    private final WorkerRepository workerRepository;

    public Payment createPayment(PaymentCreateVO paymentCreateVO, Long workerId) {
        Customer customer = customerRepository.getReferenceById(paymentCreateVO.customerId());
        Hotel hotel = hotelRepository.getReferenceById(paymentCreateVO.hotelId());
        Worker worker = workerId==null ? null : workerRepository.findById(workerId).orElse(null);

        Payment payment = Payment.builder()
                .customer(customer)
                .hotel(hotel)
                .price(paymentCreateVO.price())
                .currency(paymentCreateVO.currency())
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

        if(worker.getWorkerType().equals(WorkerType.MANAGER) && !payment.isPaid()) {
            paymentRepository.delete(payment);
        } else {
            throw new IllegalArgumentException("You don't have permission to delete this payment");
        }
    }

    public void removeReservationPayment(Long reservationNumber) {
        List<Payment> payments = paymentRepository.findByReservationNumber(reservationNumber);
        paymentRepository.deleteAll(payments);
    }

    public void createNewPayment(NewPaymentVO newPayment, Long userId) {
        Payment payment = createPayment(new PaymentCreateVO(newPayment), userId);
        if(newPayment.paymentType()!=null){
            markPaymentAsPaid(new MarkPaymentVO(payment, newPayment.paymentType()), userId);
        }
    }

    public Payment getPaymentByReservationNumber(Long reservationNumber) {
        return paymentRepository.findByReservationNumber(reservationNumber).stream().findFirst().orElse(null);
    }

    public Payment updatePayment(PaymentCreateVO paymentCreateVO, Long userId) {
        Payment payment = paymentRepository.findByReservationNumberAndPaidForAndPaid(paymentCreateVO.reservationNumber(), PaidFor.RESERVATION, false);
        if(payment!=null){
            payment.setPrice(paymentCreateVO.price());
            payment.setCurrency(paymentCreateVO.currency());
            return paymentRepository.save(payment);
        } else {
            return createPayment(paymentCreateVO, userId);
        }
    }
}
