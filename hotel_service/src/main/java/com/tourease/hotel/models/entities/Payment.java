package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.PaidFor;
import com.tourease.hotel.models.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Table(name = "payment")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private BigDecimal hotelPrice;
    @Enumerated(EnumType.STRING)
    private Currency hotelCurrency;
    private BigDecimal nightPrice;
    private BigDecimal mealPrice;
    private BigDecimal discount;
    private BigDecimal advancedPayment;
    private boolean paid;
    private OffsetDateTime paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private PaidFor paidFor;
    private Long reservationNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

}
