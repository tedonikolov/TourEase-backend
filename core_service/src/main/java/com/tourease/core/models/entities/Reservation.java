package com.tourease.core.models.entities;

import com.tourease.core.models.enums.Currency;
import com.tourease.core.models.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Table(name = "reservation")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long userId;
    private Long reservationNumber;
    @CreationTimestamp
    private OffsetDateTime creationDate;
    private OffsetDateTime checkIn;
    private OffsetDateTime checkOut;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private int nights;
    private int peopleCount;
    private boolean paid;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Rating rating;
}
