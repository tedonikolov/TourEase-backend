package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.FacilityEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "facility", schema = "public")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FacilityEnum name;
    private boolean paid;
    private BigDecimal price;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;
}
