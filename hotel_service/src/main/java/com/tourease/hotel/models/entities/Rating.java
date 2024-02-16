package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "rating", schema = "public")
public class Rating {
    @Id
    @Column(name = "hotel_id", nullable = false)
    private Long id;

    private Long totalRating;
    private Long numberOfRates;
    private BigDecimal rating;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;
}
