package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rating", schema = "public")
public class Rating {
    @Id
    @Column(name = "hotel_id", nullable = false)
    private Long id;

    private BigDecimal totalRating;
    private Long numberOfRates;
    private BigDecimal rating;

    @OneToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;
}
