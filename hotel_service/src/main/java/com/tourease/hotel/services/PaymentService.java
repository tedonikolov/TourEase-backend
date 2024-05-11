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
                .worker(worker)
                .build();

        paymentRepository.save(payment);

        return payment;
    }

    public List<Payment> getAllPaymentsByCustomersForHotel(List<Long> customers, Long hotelId, boolean isPaid) {
        return paymentRepository.findByCustomer_IdInAndHotel_IdAndPaid(customers, hotelId, isPaid);
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

    public void removeReservationPayment(List<Long> customers, Long hotelId) {
        for (Long id : customers) {
            Customer customer = customerRepository.findById(id).orElse(null);
            if (customer != null) {
                List<Payment> payments = paymentRepository.findByCustomer_IdAndPaidForAndPaidAndHotel_Id(customer.getId(), PaidFor.RESERVATION, false, hotelId);
                paymentRepository.deleteAll(payments);
            }
        }
    }

    public void createNewPayment(NewPaymentVO newPayment, Long userId) {
        Payment payment = createPayment(new PaymentCreateVO(newPayment), userId);
        if(newPayment.paymentType()!=null){
            markPaymentAsPaid(new MarkPaymentVO(payment, newPayment.paymentType()), userId);
        }
    }
}
