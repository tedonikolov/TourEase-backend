package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.Currency;
import com.tourease.hotel.models.enums.MealType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "meal", schema = "public")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealType type;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

    @OneToMany(mappedBy = "meal", orphanRemoval = true)
    @JsonIgnore
    private Set<Reservation> reservations = new LinkedHashSet<>();

    public Meal(MealType type, BigDecimal price, Currency currency, Hotel hotel) {
        this.type = type;
        this.price = price;
        this.currency = currency;
        this.hotel = hotel;
    }
}
