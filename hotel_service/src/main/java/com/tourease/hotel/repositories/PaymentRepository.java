package com.tourease.hotel.repositories;

import com.tourease.hotel.models.entities.Payment;
import com.tourease.hotel.models.enums.PaidFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.customer.id in ?1 and p.hotel.id = ?2 and p.paid = ?3 and p.reservationNumber = ?4")
    List<Payment> findByCustomer_IdInAndHotel_IdAndPaid(List<Long> customers, Long hotelId, boolean isPaid, Long reservationNumber);

    @Query("select p from Payment p where p.reservationNumber = ?1")
    List<Payment> findByReservationNumber(Long reservationNumber);

    @Query("select p from Payment p where p.reservationNumber = ?1 and p.paidFor = ?2 and p.paid = ?3")
    Payment findByReservationNumberAndPaidForAndPaid(Long reservationNumber, PaidFor paidFor, boolean paid);
}